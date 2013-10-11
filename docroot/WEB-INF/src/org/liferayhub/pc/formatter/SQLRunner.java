package org.liferayhub.pc.formatter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.NamingException;
import javax.sql.DataSource;

import com.liferay.portal.kernel.util.InfrastructureUtil;

/**
 * Class to run an SQL script, like psql(1), SQL*Plus, or similar programs.
 * Command line interface accepts options -c config [-f configFile]
 * [scriptFile].
 * <p>
 * Input language is: escape commands (begin with \ and MUST end with
 * semi-colon), or standard SQL statements which must also end with semi-colon);
 * <p>
 * Escape sequences:
 * <ul>
 * <li>\m (output-mode), takes character t for text, h for html, s for sql, x
 * for xml (not in this version) (the SQL output is intended to be usable to
 * re-insert the data into another identical table, but this has not been
 * extensively tested!).
 * <li>\o output-file, redirects output.
 * <li>\q quit the program
 * </ul>
 * TODO: Fix parsing so escapes don't need to end with SQL semi-colon.
 * <p>
 * This class can also be used from within programs such as servlets, etc.
 * <p>
 * TODO: knobs to set debug mode (interactively & from getopt!)
 * <p>
 * For example, this command and input:</pre> SQLrunner -c testdb \ms; select
 * *from person where person_key=4; </pre>might produce this output:
 * 
 * <pre>
 * Executing : <<select * from person where person_key=4>>
 *  insert into PERSON(PERSON_KEY,  FIRST_NAME, INITIAL, LAST_NAME, ... ) 
 * values (4, 'Ian', 'F', 'Darwin', ...);
 * </pre>
 * 
 * @author Ian Darwin, http://www.darwinsys.com/
 */
public class SQLRunner implements ResultsDecoratorPrinter {

	// TODO: This is an obvious candidate for a 1.5 "enum" (fixed in the 1.5
	// branch)

	/** The mode for textual output */
	public static final String MODE_TXT = "t";
	/** The mode for HTML output */
	public static final String MODE_HTML = "h";
	/** The mode for SQL output */
	public static final String MODE_SQL = "s";
	/** The mode for XML output */
	public static final String MODE_XML = "x";

	/** Database connection */
	protected Connection conn;

	/** SQL Statement */
	protected Statement stmt;

	/** Where the output is going */
	protected Writer out;

	private ResultsDecorator currentDecorator;

	private ResultsDecorator textDecorator;

	private ResultsDecorator sqlDecorator;

	private ResultsDecorator htmlDecorator;

	private ResultsDecorator xmlDecorator;

	boolean debug = false;

	/** print help; called from several places in main */
	private static void doHelp(int i) {
		System.out.println("Usage: SQLRunner [-f configFile] [-c config] [SQLscript[ ...]");
		System.exit(i);
	}

	/**
	 * main - parse arguments, construct SQLRunner object, open file(s), run
	 * scripts.
	 * 
	 * @throws SQLException
	 *             if anything goes wrong.
	 * @throws DatabaseException
	 *             if anything goes wrong.
	 */
	public static void main(String[] args) {
		try {
			SQLRunner runner = new SQLRunner(null, "h");
			runner.display("t");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Construct a SQLRunner object
	 * 
	 * @param outputMode
	 *            One of the MODE_XXX constants.
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws NamingException
	 */
	public SQLRunner(Writer writer, String outputMode) throws IOException, ClassNotFoundException, SQLException {
		DataSource ds = InfrastructureUtil.getDataSource();
		conn = ds.getConnection();
		finishSetup(writer, outputMode);
	}

	public SQLRunner(Connection c, Writer writer, String outputMode) throws IOException, SQLException {
		// set up the SQL input
		conn = c;
		finishSetup(writer, outputMode);
	}

	void finishSetup(Writer writer, String outputMode) throws IOException, SQLException {
		DatabaseMetaData dbm = conn.getMetaData();
		String dbName = dbm.getDatabaseProductName();
		System.out.println("SQLRunner: Connected to " + dbName);
		stmt = conn.createStatement();

		if (writer == null) {
			out = new PrintWriter(System.out);
		} else {
			out = writer;
		}

		setOutputMode(outputMode);
	}

	/**
	 * Set the output mode.
	 * 
	 * @param outputMode
	 *            Must be a value equal to one of the MODE_XXX values.
	 * @throws IllegalArgumentException
	 *             if the mode is not valid.
	 */
	void setOutputMode(String outputMode) {
		if (outputMode.length() == 0) {
			throw new IllegalArgumentException("invalid mode: " + outputMode + "; must be t, h or s");
		}

		// Assign the correct ResultsDecorator, creating them on the fly
		// using the lazy evaluation pattern.
		ResultsDecorator newDecorator = null;
		switch (outputMode.charAt(0)) {
		case 't':
			if (textDecorator == null) {
				textDecorator = new ResultsDecoratorText(this);
			}
			newDecorator = textDecorator;
			break;
		case 'h':
			if (htmlDecorator == null) {
				htmlDecorator = new ResultsDecoratorHTML(this);
			}
			newDecorator = htmlDecorator;
			break;
		case 's':
			if (sqlDecorator == null) {
				sqlDecorator = new ResultsDecoratorSQL(this);
			}
			newDecorator = sqlDecorator;
			break;
		case 'x':
			if (xmlDecorator == null) {
				xmlDecorator = new ResultsDecoratorXML(this);
			}
			newDecorator = sqlDecorator;
			break;
		default:
			throw new IllegalArgumentException("invalid mode: " + outputMode + "; must be t, h or s");
		}
		if (currentDecorator != newDecorator) {
			currentDecorator = newDecorator;
			System.out.println("Mode set to  " + currentDecorator.getName());
		}

	}

	/**
	 * Run one script file, by name. Called from cmd line main or from user
	 * code.
	 */
	public void runScript(String scriptFile) throws IOException, SQLException {

		BufferedReader is;

		// Load the script file first, it's the most likely error
		is = new BufferedReader(new FileReader(scriptFile));

		runScript(is);
	}

	/** Run one script, by name, given a BufferedReader. */
	public void runScript(BufferedReader is) throws IOException, SQLException {

		String stmt;
		int i = 0;
		System.out.println("SQLRunner: ready.");
		while ((stmt = getStatement(is)) != null) {
			stmt = stmt.trim();
			if (stmt.startsWith("\\")) {
				doEscape(stmt);
			} else {
				runStatement(stmt);
			}
		}
	}

	/**
	 * Process an escape like \ms; for mode=sql.
	 */
	private void doEscape(String str) throws IOException, SQLException {
		String rest = null;
		if (str.length() > 2) {
			rest = str.substring(2);
		}

		if (str.startsWith("\\d")) { // Display
			if (rest == null) {
				throw new IllegalArgumentException("\\d needs display arg");
			}
			display(rest);
		} else if (str.startsWith("\\m")) { // MODE
			if (rest == null) {
				throw new IllegalArgumentException("\\m needs output mode arg");
			}
			setOutputMode(rest);
		} else if (str.startsWith("\\o")) {
			if (rest == null) {
				throw new IllegalArgumentException("\\o needs output file arg");
			}
			setOutputFile(rest);
		} else if (str.startsWith("\\q")) {
			System.exit(0);
		} else {
			throw new IllegalArgumentException("Unknown escape: " + str);
		}

	}

	/**
	 * Display - something
	 * 
	 * @param rest
	 *            - what to display XXX: Move formatting to ResultsDecorator:
	 *            listTables(rs), listColumns(rs)
	 */
	public void display(String rest) throws SQLException {
		if (rest.equals("t")) {
			// Display list of tables
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getTables(null, null, "%", null);
			while (rs.next()) {
				System.out.println(rs.getString(3));
			}
		} else if (rest.startsWith("t")) {
			// Display one table
			String tableName = rest.substring(1).trim();
			System.out.println("# Display table " + tableName);
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getColumns(null, null, tableName, "%");
			while (rs.next()) {
				System.out.println(rs.getString(4));
			}
		} else
			throw new IllegalArgumentException("\\d" + rest + " invalid");
	}

	/**
	 * Set the output to the given filename.
	 * 
	 * @param fileName
	 */
	private void setOutputFile(String fileName) throws IOException {
		File file = new File(fileName);
		out = new PrintWriter(new FileWriter(file), true);
		System.out.println("Output set to " + file.getCanonicalPath());
	}

	/** Set the output file back to System.out */
	private void setOutputFile() throws IOException {
		out = new PrintWriter(System.out, true);
	}

	/**
	 * Run one Statement, and format results as per Update or Query. Called from
	 * runScript or from user code.
	 */
	public void runStatement(String str) throws IOException, SQLException {

		System.out.println("Executing : <<" + str.trim() + ">>");
		System.out.flush();
		try {
			boolean hasResultSet = stmt.execute(str);
			if (!hasResultSet)
				currentDecorator.write(stmt.getUpdateCount());
			else {
				ResultSet rs = stmt.getResultSet();
				currentDecorator.write(rs);
			}
		} catch (SQLException ex) {
			//if (debug) {
				throw ex;
			//} else {
			//	System.out.println("ERROR: " + ex.toString());
			//}
		}
	}

	/**
	 * Extract one statement from the given Reader. Ignore comments and null
	 * lines.
	 * 
	 * @return The SQL statement, up to but not including the ';' character. May
	 *         be null if not statement found.
	 */
	public static String getStatement(BufferedReader is) throws IOException {
		String ret = "";
		String line;
		boolean found = false;
		while ((line = is.readLine()) != null) {
			if (line == null || line.length() == 0) {
				continue;
			}
			if (!(line.startsWith("#") || line.startsWith("--"))) {
				ret += ' ' + line;
				found = true;
			}
			if (line.endsWith(";")) {
				// Kludge, kill off empty statements (";") by itself, continue
				// scanning.
				if (line.length() == 1)
					line = "";
				ret = ret.substring(0, ret.length() - 1);
				return ret;
			}
		}
		return null;
	}

	public void close() throws SQLException, IOException {
		stmt.close();
		conn.close();
		out.flush();
		out.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DatabaseWriterImpl#println(java.lang.String)
	 */
	public void print(String line) throws IOException {
		out.write(line);
	}

	public void println(String line) throws IOException {
		out.write(line + "\n");
		out.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DatabaseWriterImpl#println()
	 */
	public void println() throws IOException {
		out.write("\n");
		out.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ResultsDecoratorPrinter#getPrintWriter()
	 */
	public Writer getWriter() {
		return out;
	}

	/**
	 * @return Returns the debug.
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * @param debug
	 *            True to enable debug, false to disable.
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}

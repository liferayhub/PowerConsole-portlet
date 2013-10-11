package org.liferayhub.pc.formatter;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.rowset.WebRowSet;

public class ResultsDecoratorXML extends ResultsDecorator {
	WebRowSet results;

	ResultsDecoratorXML(ResultsDecoratorPrinter out) {
		super(out);
		try {
			// The name is com.sun.rowset.WebRowSetImpl in rowset.jar,
			// but sun.rowset.jar in J2SDK1.5. Go figure.
			Class c = Class.forName("com.sun.rowset.WebRowSetImpl");
			results = (WebRowSet) c.newInstance();

		} catch (Exception ex) {
			throw new IllegalArgumentException("can't load WebRowSetImpl, check CLASSPATH");
		}
	}

	public void write(ResultSet rs) throws SQLException {
		results.writeXml(rs, parent.getWriter());
	}

	void write(int rowCount) throws IOException {
		println("RowCount: " + rowCount);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ResultsDecorator#getName()
	 */
	String getName() {
		return "XML";
	}
}
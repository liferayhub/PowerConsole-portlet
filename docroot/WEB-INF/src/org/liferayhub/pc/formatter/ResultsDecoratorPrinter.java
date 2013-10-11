package org.liferayhub.pc.formatter;

import java.io.IOException;
import java.io.Writer;

public interface ResultsDecoratorPrinter {
	void print(String line) throws IOException;

	void println(String line) throws IOException;

	void println() throws IOException;

	Writer getWriter();
}

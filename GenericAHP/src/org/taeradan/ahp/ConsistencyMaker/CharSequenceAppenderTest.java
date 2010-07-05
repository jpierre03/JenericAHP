package org.taeradan.ahp.ConsistencyMaker;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Marianne
 */
public class CharSequenceAppenderTest {

	public static void main(String[] args) {
		try {
			CharSequenceAppender appender = new CharSequenceAppender("attsp.csv");

			appender.insertLineFeed();
			appender.append("ma matrice");
			appender.insertLineFeed();
			appender.append("a");
			appender.insertSeparator();
			appender.append("b");
			appender.insertLineFeed();
			appender.append("c");
			appender.insertSeparator();
			appender.append("d");

			appender.close();
		} catch (IOException ex) {
			Logger.getLogger(CharSequenceAppenderTest.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}

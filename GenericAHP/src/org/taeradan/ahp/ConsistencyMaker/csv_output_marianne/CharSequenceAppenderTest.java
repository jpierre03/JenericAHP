package org.taeradan.ahp.ConsistencyMaker.csv_output_marianne;

import org.taeradan.ahp.ConsistencyMaker.MyMatrix;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Marianne
 */
public class CharSequenceAppenderTest {

	public static void main(String[] args) {
		try {
			CharSequenceAppender appender = new CharSequenceAppender("ahp.csv");

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

			MyMatrix m = new MyMatrix(2, 2);
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 2; j++) {
					m.set(i, j, i + 1);
				}
			}
			appender.insertMatrix(m);

			appender.close();
		} catch (IOException ex) {
			Logger.getLogger(CharSequenceAppenderTest.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}

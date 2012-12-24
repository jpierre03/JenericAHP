package org.taeradan.ahp.prototype.ConsistencyMaker.csv_output_marianne;

import org.taeradan.ahp.matrix.MyMatrix;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Marianne
 */
public final class CharSequenceAppenderTest {

	private CharSequenceAppenderTest() {
	}

	public static void main(String[] args) {
		try {
			CharSequenceAppender appender = new CharSequenceAppender("ahp.csv");

			appender.appendLineFeed();
			appender.append("ma matrice");
			appender.appendLineFeed();
			appender.append("a");
			appender.appendCommaSeparator();
			appender.append("b");
			appender.appendLineFeed();
			appender.append("c");
			appender.appendCommaSeparator();
			appender.append("d");

			MyMatrix matrix = new MyMatrix(2, 2);
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 2; j++) {
					matrix.set(i, j, i + 1);
				}
			}
			appender.append(matrix);

			appender.close();
		} catch (IOException ex) {
			Logger.getLogger(CharSequenceAppenderTest.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}

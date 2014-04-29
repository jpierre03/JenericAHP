package org.taeradan.ahp.prototype.ConsistencyMaker.csv_output_marianne;

import org.taeradan.ahp.matrix.MyMatrix;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Marianne
 */
final class CharSequenceAppenderTest {

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
			appender.appendLineFeed();
			appender.appendLineFeed();

			final int rowCount = 40;
			final int columnCount = 15;
			MyMatrix matrix = new MyMatrix(rowCount, columnCount);

			/**
			 * Define matrix values
			 */
			for (int i = 0; i < rowCount; i++) {
				for (int j = 0; j < columnCount; j++) {
					final int value = i * j + 1;

					matrix.set(i, j, value);
				}
			}
			appender.append(matrix);

			appender.close();
		} catch (IOException ex) {
			Logger.getLogger(CharSequenceAppenderTest.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}

package org.taeradan.ahp.prototype.ConsistencyMaker.csv_output_marianne;

import org.taeradan.ahp.matrix.MyMatrix;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This class allows to insert separators (used in csv files) or datas in a file
 *
 * @author Marianne
 * @author Jean-Pierre PRUNARET
 */
public class CharSequenceAppender {

	private PrintWriter output;

	public CharSequenceAppender(String fileName) throws IOException {
		assert fileName != null;
		assert !fileName.trim().isEmpty();

		output = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));

		assert output != null;
	}

	public void append(CharSequence sequence) {
		assert sequence != null;

		output.append(sequence);
	}

	public void append(MyMatrix matrix) {
		assert matrix != null;

		appendMatrix(matrix);
	}

	public void appendLineFeed() {
		output.append("\n");
	}

	public void appendCommaSeparator() {
		output.append(";");
	}

	public void close() {
		output.close();
	}

	private void appendMatrix(MyMatrix matrix) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < matrix.getRowDimension(); i++) {
			for (int j = 0; j < matrix.getColumnDimension(); j++) {

				sb.append(matrix.getMatrixValue(i, j).getValue());
				sb.append(";");

			}
			sb.append("\n");
		}

		output.append(sb);
	}
}

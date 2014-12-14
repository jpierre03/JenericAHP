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

	public CharSequenceAppender append(CharSequence sequence) {
		assert sequence != null;

		output.append(sequence);

		return this;
	}

	public CharSequenceAppender append(double value) {
		output.append(value + "");

		return this;
	}

	public CharSequenceAppender append(MyMatrix matrix) {
		assert matrix != null;

		appendMatrix(matrix);

		return this;
	}

	public CharSequenceAppender appendLineFeed() {
		output.append("\n");

		return this;
	}

	public CharSequenceAppender appendCommaSeparator() {
		output.append(";");

		return this;
	}

	public CharSequenceAppender close() {
		output.close();

		return this;
	}

	private CharSequenceAppender appendMatrix(MyMatrix matrix) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < matrix.getRowDimension(); i++) {
			for (int j = 0; j < matrix.getColumnDimension(); j++) {

				sb.append(matrix.getMatrixValue(i, j).getValue());
				sb.append(";");

			}
			sb.append("\n");
		}

		output.append(sb);

		return this;
	}
}

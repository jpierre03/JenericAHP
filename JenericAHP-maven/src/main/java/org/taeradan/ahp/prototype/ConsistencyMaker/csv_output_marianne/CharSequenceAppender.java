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

	public CharSequenceAppender(String nomFichier)
			throws
			IOException {
		output = new PrintWriter(new BufferedWriter(new FileWriter(nomFichier, true)));
	}

	public void append(CharSequence object) {
		output.append(object);
	}

	public void append(MyMatrix myMatrix) {
		appendMatrix(myMatrix);
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

	private void appendMatrix(MyMatrix myMatrix) {
		String string = "";

		for (int i = 0; i < myMatrix.getRowDimension(); i++) {
			for (int j = 0; j < myMatrix.getColumnDimension(); j++) {


				string += myMatrix.getMatrixValue(i, j).getValue() + ";";

			}
			output.append(string);
			appendLineFeed();
			string = "";

		}
	}


}

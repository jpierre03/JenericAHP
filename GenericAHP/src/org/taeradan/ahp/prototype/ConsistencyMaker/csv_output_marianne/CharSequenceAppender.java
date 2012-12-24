package org.taeradan.ahp.prototype.ConsistencyMaker.csv_output_marianne;

import org.taeradan.ahp.matrix.MyMatrix;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Marianne
 *         This class allows to insert separators (used in csv files) or datas in a file
 */
public class CharSequenceAppender {

	private PrintWriter output;

	public CharSequenceAppender(String nomFichier) throws IOException {
		output = new PrintWriter(new BufferedWriter(new FileWriter(nomFichier, true)));
	}

	/**
	 * Write an object in the opened file
	 *
	 * @param object
	 */
	public void append(CharSequence object) {
		output.append(object);
	}

	/**
	 * Write a linefeed in the opened file
	 */
	public void insertLineFeed() {
		output.append("\n");
	}

	/**
	 * Write a separator separator in the opened file
	 */
	public void insertSeparator() {
		output.append(";");
	}


	/**
	 * Write a matrix in the opened file
	 *
	 * @param myMatrix
	 */
	public void insertMatrix(MyMatrix myMatrix) {
		String chaine = "";

		for (int i = 0; i < myMatrix.getRowDimension(); i++) {
			for (int j = 0; j < myMatrix.getColumnDimension(); j++) {


				chaine += myMatrix.getMatrixValue(i, j).getValue() + ";";

			}
			output.append(chaine);
			insertLineFeed();
			chaine = "";

		}
	}

	public void close() {
		output.close();
	}
}

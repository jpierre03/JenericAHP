package org.taeradan.ahp.ConsistencyMaker;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Marianne
 */
public class CharSequenceAppender {

	private PrintWriter output;

	public CharSequenceAppender(String nomFichier) throws IOException {
		output = new PrintWriter(new BufferedWriter(new FileWriter(nomFichier, true)));
	}

	public void append(CharSequence object) {
		output.append(object);
	}
	public void insertLineFeed(){
		output.append("\n");
	}
	public void insertSeparator(){
		output.append(";");
	}

	public void close() {
		output.close();
	}
}

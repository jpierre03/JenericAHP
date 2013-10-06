package org.taeradan.ahp.prototype;

/**
 * Created with IntelliJ IDEA. User: jpierre03 Date: 31/12/12 Time: 03:02 To change this template use File | Settings |
 * File Templates.
 */
public final class SampleMatrixHeaders {
	private SampleMatrixHeaders() {
	}

	/*
			 * redéfinition de la classe getColumnHeader(), elle permet de retourner le tableau de string des column
			 *
			 */
	public static String[] getColumnHeader(boolean isTestB, boolean isEnglish) {
		if (isTestB == true) {
			if (isEnglish == false) {
				String columnNames[] = {"Temps de transport",
					"Coût",
					"Confort",
					"Pollution",
					"Qualité de service",
					"Sécurité"};
				return columnNames;
			} else {
				String columnNames[] = {"Travel time",
					"Cost",
					"Comfort",
					"Pollution",
					"Quality of Service",
					"Security"};
				return columnNames;
			}
		} else {
			if (isEnglish == false) {
				String columnNames[] = {"Prix",
					"Sécurité",
					"Pollution",
					"Design",
					"Durée de vie",
					"Taille"};
				return columnNames;
			} else {
				String columnNames[] = {"Price",
					"Security",
					"Pollution",
					"Design",
					"Life",
					"Size"};
				return columnNames;
			}
		}
	}
}

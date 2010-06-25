package org.taeradan.ahp.ConsistencyMaker;

/**
 * @author jpierre03
 * @author Marianne
 */
public class MatrixValue {

	private double value = 0;
	private int row = 0;
	private int column = 0;

	public MatrixValue() {
	}

	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @param row the row to set
	 */
	public void setRow(int row) {
		this.row = row;
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * @param column the column to set
	 */
	public void setColumn(int column) {
		this.column = column;
	}

	@Override
	public String toString() {
		return value + " ";
	}
}

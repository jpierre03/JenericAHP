package org.taeradan.ahp.ConsistencyMaker;

/**
 * @author Jean-Pierre PRUNARET
 * @author Marianne
 */
public class MatrixValue {

	/**
	 *
	 */
	private double value = 0;
	/**
	 *
	 */
	private int row = 0;
	/**
	 *
	 */
	private int column = 0;

	/**
	 *
	 */
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
	public boolean equals(Object o) {
		boolean isEquals = true;
		if (o instanceof MatrixValue) {
			MatrixValue aValue = (MatrixValue) o;
			if (this.getColumn() != aValue.getColumn()) {
				isEquals = false;
			}
			if (this.getRow() != aValue.getRow()) {
				isEquals = false;
			}
			if (this.getValue() != aValue.getValue()) {
				isEquals = false;
			}

		} else {
			isEquals = false;
		}
		return  isEquals;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash =
		97 * hash + (int) (Double.doubleToLongBits(this.value) ^ (Double.doubleToLongBits(this.value)
																  >>> 32));
		hash = 97 * hash + this.row;
		hash = 97 * hash + this.column;
		return hash;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return value + " ";
	}
}

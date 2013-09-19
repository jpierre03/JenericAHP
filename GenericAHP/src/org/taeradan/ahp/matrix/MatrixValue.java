package org.taeradan.ahp.matrix;

/**
 * @author Jean-Pierre PRUNARET
 * @author Marianne
 */
public class MatrixValue {

	private double value;
	private int    row;
	private int    column;

	public MatrixValue(int row, int column, double value) {
		this.row = row;
		this.column = column;
		this.value = value;
	}

	public MatrixValue() {
		this(0, 0, 0);
	}

	public MatrixValue(final MatrixValue toClone) {
		this(toClone.getRow(), toClone.getColumn(), toClone.getValue());
	}

	private static double round(double what, int precision) {
		return (double) ((int) (what * Math.pow(10, precision) + .5)) / Math.pow(10, precision);
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		if (value <= 0) {
			throw new IllegalArgumentException("Value can't be negative (nor 0) [value=" + value + "]");
		}
		if (value > 9) {
			throw new IllegalArgumentException("Value can't be greater than 9 [value=" + value + "]");
		}

		if (value < 1) {
			this.value = round(value, 4);
		} else {
			this.value = round(value, 0);
		}
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

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
		return isEquals;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 97 * hash + (int) (Double.doubleToLongBits(this.value) ^ (Double.doubleToLongBits(this.value) >>> 32));
		hash = 97 * hash + this.row;
		hash = 97 * hash + this.column;
		return hash;
	}

	@Override
	public String toString() {
		return value + "";
	}
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.taeradan.ahp.gui;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.JOptionPane;
import org.nfunk.jep.JEP;

/**
 *
 * @author taeradan
 */
public class PrefMatrixChangeListener implements TableModelListener {

	public void tableChanged(TableModelEvent evt) {
		System.out.println("row="+evt.getFirstRow()+",column"+evt.getColumn());
		if(evt.getFirstRow()>=evt.getColumn()){
			PrefMatrixTableModel prefMatrix = (PrefMatrixTableModel)evt.getSource();
			Double value = null;
			Object nonParsedValue = prefMatrix.getValueAt(evt.getFirstRow(), evt.getColumn());
			System.out.println("Non parsed value = "+nonParsedValue);
			JEP myParser = new JEP();
			if(nonParsedValue instanceof String){
				myParser.parseExpression((String)nonParsedValue);
				value = myParser.getValue();
			}
			if(nonParsedValue instanceof Double)
				value = (Double)nonParsedValue;
			System.out.println("Parsed value = "+value.doubleValue());
			if(value == 0){
				String newValue = JOptionPane.showInputDialog("The value \"0\" is not allowed here\nEnter a new value :");
				if(newValue == null)
					newValue = "1";
				myParser.parseExpression((String)newValue);
				prefMatrix.setValueAt(myParser.getValue(),evt.getFirstRow(), evt.getColumn());
			}
			if(value == Double.NaN){
				String newValue = JOptionPane.showInputDialog("This can't be leaved blank\nPlease enter a value :");
				if(newValue == null)
					newValue = "1";
				myParser.parseExpression((String)newValue);
				prefMatrix.setValueAt(myParser.getValue(),evt.getFirstRow(), evt.getColumn());
			}
			prefMatrix.setValueAt((1/value), evt.getColumn(), evt.getFirstRow());
		}
	}

}

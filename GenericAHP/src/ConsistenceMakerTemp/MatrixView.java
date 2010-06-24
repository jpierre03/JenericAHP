/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ConsistenceMakerTemp;

import Jama.Matrix;
import java.awt.*;
import javax.swing.*;


/**
 *
 * @author Marianne
 */
public class MatrixView extends JFrame{



/*
 * This class allows to view the matrix on a Panel
 *
 */
	//constructeur
	public MatrixView(double[][] rowData,int rows,int columns){
	super();
	buildMatrixView(rowData,rows,columns);
}


	//Elements de la fenetre
	private void buildMatrixView(double[][] rowData,int Sizerows,int Sizecolumns){
	setTitle("Consistency Maker"); //On donne un titre à l'application
	setSize(360,250); //On donne une taille à notre fenêtre
	setLocationRelativeTo(null); //On centre la fenêtre sur l'écran
	setResizable(false); //On interdit la redimensionnement de la fenêtre
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //On dit à l'application de se fermer lors du clic sur la croix
	setContentPane(buildContentPane(rowData));
}

	private JPanel buildContentPane(double[][] rowData){
	JPanel panel = new JPanel();//creation du panel
	panel.setLayout(new BorderLayout());

	//Creation des critères
	String[] criteriasName = new String [2];
	criteriasName[0]="Critère 1";
	criteriasName[1]="Critère 2";

	//Conversion de la matrice en Array pour permettre affichage
	Object[][] matrixObject = new Object[2][2];
	for(int i=0;i<2;i++){
		for(int j=0;j<2;j++){
		matrixObject[i][j] = rowData[i][j];
		}
	}

	JTable matrixTable = new JTable(matrixObject, criteriasName);



	matrixTable.setDefaultRenderer(Object.class, new HighLighter());

    JScrollPane myScrollPane = new JScrollPane(matrixTable);    
	panel.add(BorderLayout.CENTER,myScrollPane);

	return panel;
}


	public Matrix getRefreshedMatrix(){
	//TO DO : return a matrix with new value
		Matrix m = null;
		return m;
}

}
   
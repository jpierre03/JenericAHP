/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ConsistenceMakerTemp;

import Jama.Matrix;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

/**
 *
 * @author Marianne
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here


//   prefMatrix.print(0,0);

   SwingUtilities.invokeLater(new Runnable(){
		public void run(){

		int rows = 2;
		int columns = 2;


	//Création de la matrice en Array  	/*A METTRE DANS UNE CLASSE A PART*/
	double[][] rowData = new double[rows][columns];

	rowData[0][0]=1;
	rowData[0][1]=1;

	rowData[1][0]=1;
	rowData[1][1]=1;

	

	int i;
	int j;
	Matrix prefMatrix = new Matrix(2,2);
	ArrayToMatrix ATM = new ArrayToMatrix();
	prefMatrix=ATM.convertArrayToMatrix(rowData, rows, columns);
	prefMatrix.print(0,0);


	/*//création de la matrice de type Matrix à partir d'un Array
	for(i=0;i<2;i++){
		for (j=0;j<2;j++){
			prefMatrix.set(i,j,rowData[i][j]);
		}
	}*/
		MatrixView windowMatrix = new MatrixView(rowData,rows,columns);
		windowMatrix.setVisible(true);
		}
		});







    }

}

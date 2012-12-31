package org.taeradan.ahp.prototype.gui.yannick;

import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.nfunk.jep.JEP;
import org.taeradan.ahp.ConsistencyChecker;
import org.taeradan.ahp.PriorityVector;
import org.taeradan.ahp.matrix.MatrixValue;
import org.taeradan.ahp.matrix.MyMatrix;
import org.taeradan.ahp.prototype.ConsistencyMaker.RandomTools;
import org.taeradan.ahp.prototype.ConsistencyMaker.SaatyTools;
import org.taeradan.ahp.prototype.ConsistencyMaker.csv_output_marianne.CharSequenceAppender;
import org.taeradan.ahp.prototype.SampleMatrixHeaders;
import org.taeradan.ahp.prototype.gui.matrix.MyMatrixTable;
import org.taeradan.ahp.prototype.gui.matrix.MyMatrixTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created on 31 mai 2011, 14:24:44
 *
 * @author Yannick
 * @author Jean-Pierre PRUNARET
 */
public class InterfaceAHP
		extends JFrame {

	private static final double[] SAATY_VALUES = {1. / 9,
												  1. / 8,
												  1. / 7,
												  1. / 6,
												  1. / 5,
												  1. / 4,
												  1. / 3,
												  1. / 2,
												  1,
												  2,
												  3,
												  4,
												  5,
												  6,
												  7,
												  8,
												  9};

	private MyMatrix             myMatrix;
	private MyMatrixTableModel   matrixTableModel;
	private CharSequenceAppender csa;
	private String               file;
	private String               fileHistorique;
	private int     finSimulation = 0;
	private boolean changerCoeff  = false;
	private Meter monHeure;
	private boolean boolHeure = true;
	private JEP    monJep;
	private double saatyConsistency;
	private boolean modeAnglais = false;

	/** Creates new form MaMatriceFrame */
	public InterfaceAHP() {
		//on redimensionne la taille de la jframe
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenSize.setSize(screenSize.getWidth(), screenSize.getHeight() - 100);
		this.setPreferredSize(screenSize);
		this.setResizable(true);

		initComponents();

		//On initialise la taille de la matrice à 6
		jTextFieldDimensions.setText("6");
		monJep = new JEP();
	}

	private void initComponents() {

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);

		jButtonValiderMatrice.setText("Valider Matrice");
		jButtonValiderMatrice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				jButtonValiderMatriceActionPerformed(evt);
			}
		});

		jTableMatrice.setModel(new DefaultTableModel(
				new Object[][]{
						{},
						{},
						{},
						{}
				},
				new String[]{

				}
		));
		jScrollPane1.setViewportView(jTableMatrice);

		jLabel3.setText("CR:");

		jPanelInit.setBorder(BorderFactory.createTitledBorder("Initialisation"));
		jPanelInit.setLayout(new AbsoluteLayout());

		buttonGroup1.add(jRadioButtonAleatoire);
		jRadioButtonAleatoire.setText("A");
		jPanelInit.add(jRadioButtonAleatoire, new AbsoluteConstraints(14, 27, -1, -1));

		buttonGroup1.add(jRadioButtonSaaty);
		jRadioButtonSaaty.setText("S");
		jPanelInit.add(jRadioButtonSaaty, new AbsoluteConstraints(49, 27, -1, -1));

		buttonGroup2.add(jRadioButtonP1);
		jRadioButtonP1.setText("P1");
		jPanelInit.add(jRadioButtonP1, new AbsoluteConstraints(98, 27, -1, -1));

		buttonGroup2.add(jRadioButtonP2);
		jRadioButtonP2.setText("P2");
		jPanelInit.add(jRadioButtonP2, new AbsoluteConstraints(137, 27, -1, -1));

		jLabel1.setText("Taille matrice:");
		jPanelInit.add(jLabel1, new AbsoluteConstraints(10, 70, -1, -1));
		jPanelInit.add(jTextFieldDimensions, new AbsoluteConstraints(100, 70, 43, -1));

		jPanelInit.add(jTextFieldChemin, new AbsoluteConstraints(290, 70, 299, -1));

		jLabel2.setText("Chemin de sauvegarde:");
		jPanelInit.add(jLabel2, new AbsoluteConstraints(156, 72, -1, -1));

		jButtonOK.setText("Ok");
		jButtonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				jButtonOKActionPerformed(evt);
			}
		});
		jPanelInit.add(jButtonOK, new AbsoluteConstraints(617, 97, -1, -1));

		jButtonParcourir.setText("Parcourir");
		jButtonParcourir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				jButtonParcourirActionPerformed(evt);
			}
		});
		jPanelInit.add(jButtonParcourir, new AbsoluteConstraints(590, 70, -1, -1));

		jPanelClassemnt.setBorder(BorderFactory.createTitledBorder("Classement"));
		jPanelClassemnt.setMaximumSize(new Dimension(635, 273));

		jPanel1erClassement.setBorder(BorderFactory.createTitledBorder("Classement intuitif"));
		jPanel1erClassement.setMaximumSize(new Dimension(184, 224));
		jPanel1erClassement.setLayout(new AbsoluteLayout());

		jLabel5.setText("1er:");
		jPanel1erClassement.add(jLabel5, new AbsoluteConstraints(26, 34, -1, -1));

		jLabel7.setText("2ème:");
		jPanel1erClassement.add(jLabel7, new AbsoluteConstraints(16, 60, -1, -1));

		jLabel8.setText("3ème:");
		jPanel1erClassement.add(jLabel8, new AbsoluteConstraints(16, 86, -1, -1));

		jLabel9.setText("4ème:");
		jPanel1erClassement.add(jLabel9, new AbsoluteConstraints(16, 112, -1, -1));

		jLabel10.setText("5ème:");
		jPanel1erClassement.add(jLabel10, new AbsoluteConstraints(16, 138, -1, -1));

		jLabel11.setText("6ème:");
		jPanel1erClassement.add(jLabel11, new AbsoluteConstraints(16, 164, -1, -1));

		jPanel1erClassement.add(jComboBox1erClass2, new AbsoluteConstraints(50, 57, 118, -1));

		jPanel1erClassement.add(jComboBox1erClass3, new AbsoluteConstraints(50, 83, 118, -1));

		jPanel1erClassement.add(jComboBox1erClass4, new AbsoluteConstraints(50, 109, 118, -1));

		jPanel1erClassement.add(jComboBox1erClass5, new AbsoluteConstraints(50, 135, 118, -1));

		jPanel1erClassement.add(jComboBox1erClass6, new AbsoluteConstraints(50, 161, 118, -1));

		jPanel1erClassement.add(jComboBox1erClass1, new AbsoluteConstraints(50, 31, 118, -1));

		jButtonOkClassIntuitif.setText("OK");
		jButtonOkClassIntuitif.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				jButtonOkClassIntuitifActionPerformed(evt);
			}
		});
		jPanel1erClassement.add(jButtonOkClassIntuitif,
								new AbsoluteConstraints(81, 187, -1, -1));

		jPanelClassementFinal.setBorder(BorderFactory.createTitledBorder("Classement Final"));
		jPanelClassementFinal.setMaximumSize(new Dimension(203, 199));
		jPanelClassementFinal.setLayout(new AbsoluteLayout());
		jPanelClassementFinal.add(jTextFieldClassFinal1, new AbsoluteConstraints(64, 31, 80, -1));
		jPanelClassementFinal.add(jTextFieldClassFinal2, new AbsoluteConstraints(64, 57, 80, -1));
		jPanelClassementFinal.add(jTextFieldClassFinal3, new AbsoluteConstraints(64, 83, 80, -1));
		jPanelClassementFinal.add(jTextFieldClassFinal4, new AbsoluteConstraints(64, 109, 80, -1));
		jPanelClassementFinal.add(jTextFieldClassFinal5, new AbsoluteConstraints(64, 135, 80, -1));
		jPanelClassementFinal.add(jTextFieldClassFinal6, new AbsoluteConstraints(64, 161, 80, -1));

		jLabel18.setText("1er:");
		jPanelClassementFinal.add(jLabel18, new AbsoluteConstraints(26, 34, -1, -1));

		jLabel19.setText("2ème:");
		jPanelClassementFinal.add(jLabel19, new AbsoluteConstraints(16, 60, -1, -1));

		jLabel20.setText("3ème:");
		jPanelClassementFinal.add(jLabel20, new AbsoluteConstraints(16, 86, -1, -1));

		jLabel21.setText("4ème:");
		jPanelClassementFinal.add(jLabel21, new AbsoluteConstraints(16, 112, -1, -1));

		jLabel22.setText("5ème:");
		jPanelClassementFinal.add(jLabel22, new AbsoluteConstraints(16, 138, -1, -1));

		jLabel23.setText("6ème:");
		jPanelClassementFinal.add(jLabel23, new AbsoluteConstraints(16, 164, -1, -1));
		jPanelClassementFinal.add(jLabelClassFinal1, new AbsoluteConstraints(148, 31, 39, 20));
		jPanelClassementFinal.add(jLabelClassFinal2, new AbsoluteConstraints(148, 57, 39, 20));
		jPanelClassementFinal.add(jLabelClassFinal3, new AbsoluteConstraints(148, 83, 39, 20));
		jPanelClassementFinal.add(jLabelClassFinal4, new AbsoluteConstraints(148, 109, 39, 20));
		jPanelClassementFinal.add(jLabelClassFinal5, new AbsoluteConstraints(148, 135, 39, 20));
		jPanelClassementFinal.add(jLabelClassFinal6, new AbsoluteConstraints(148, 161, 39, 20));

		jPanelClassementMatrice.setBorder(BorderFactory.createTitledBorder("Classement initial"));
		jPanelClassementMatrice.setMaximumSize(new Dimension(204, 231));
		jPanelClassementMatrice.setLayout(new AbsoluteLayout());
		jPanelClassementMatrice.add(jTextFieldClassMat1, new AbsoluteConstraints(64, 31, 80, -1));
		jPanelClassementMatrice.add(jTextFieldClassMat2, new AbsoluteConstraints(64, 57, 80, -1));
		jPanelClassementMatrice.add(jTextFieldClassMat3, new AbsoluteConstraints(64, 83, 80, -1));
		jPanelClassementMatrice.add(jTextFieldClassMat4, new AbsoluteConstraints(64, 109, 80, -1));
		jPanelClassementMatrice.add(jTextFieldClassMat5, new AbsoluteConstraints(64, 135, 80, -1));
		jPanelClassementMatrice.add(jTextFieldClassMat6, new AbsoluteConstraints(64, 161, 80, -1));

		jLabel12.setText("1er:");
		jPanelClassementMatrice.add(jLabel12, new AbsoluteConstraints(26, 34, -1, -1));

		jLabel13.setText("2ème:");
		jPanelClassementMatrice.add(jLabel13, new AbsoluteConstraints(16, 60, -1, -1));

		jLabel14.setText("3ème:");
		jPanelClassementMatrice.add(jLabel14, new AbsoluteConstraints(16, 86, -1, -1));

		jLabel15.setText("4ème:");
		jPanelClassementMatrice.add(jLabel15, new AbsoluteConstraints(16, 112, -1, -1));

		jLabel16.setText("5ème:");
		jPanelClassementMatrice.add(jLabel16, new AbsoluteConstraints(16, 138, -1, -1));

		jLabel17.setText("6ème:");
		jPanelClassementMatrice.add(jLabel17, new AbsoluteConstraints(16, 164, -1, -1));
		jPanelClassementMatrice.add(jLabelClassInit1, new AbsoluteConstraints(148, 31, 40, 20));
		jPanelClassementMatrice.add(jLabelClassInit2, new AbsoluteConstraints(148, 57, 40, 20));
		jPanelClassementMatrice.add(jLabelClassInit3, new AbsoluteConstraints(148, 83, 40, 20));
		jPanelClassementMatrice.add(jLabelClassInit4, new AbsoluteConstraints(148, 109, 40, 20));
		jPanelClassementMatrice.add(jLabelClassInit5, new AbsoluteConstraints(148, 135, 40, 20));
		jPanelClassementMatrice.add(jLabelClassInit6, new AbsoluteConstraints(148, 161, 40, 20));

		GroupLayout jPanelClassementLayout = new GroupLayout(jPanelClassemnt);
		jPanelClassemnt.setLayout(jPanelClassementLayout);
		jPanelClassementLayout.setHorizontalGroup(
				jPanelClassementLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
									  .addGroup(jPanelClassementLayout.createSequentialGroup()
																	  .addGap(14, 14, 14)
																	  .addComponent(jPanel1erClassement,
																					GroupLayout.PREFERRED_SIZE,
																					192,
																					GroupLayout.PREFERRED_SIZE)
																	  .addGap(18, 18, 18)
																	  .addComponent(jPanelClassementMatrice,
																					GroupLayout.PREFERRED_SIZE,
																					200,
																					GroupLayout.PREFERRED_SIZE)
																	  .addComponent(jPanelClassementFinal,
																					GroupLayout.PREFERRED_SIZE,
																					GroupLayout.DEFAULT_SIZE,
																					GroupLayout.PREFERRED_SIZE))
												 );
		jPanelClassementLayout.setVerticalGroup(
				jPanelClassementLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
									  .addGroup(jPanelClassementLayout.createSequentialGroup()
																	  .addGroup(jPanelClassementLayout.createParallelGroup(
																			  GroupLayout.Alignment.TRAILING,
																			  false)
																									  .addGroup(
																											  jPanelClassementLayout.createSequentialGroup()
																																	.addContainerGap()
																																	.addComponent(
																																			jPanel1erClassement,
																																			GroupLayout.DEFAULT_SIZE,
																																			GroupLayout.DEFAULT_SIZE,
																																			Short.MAX_VALUE))
																									  .addGroup(
																											  GroupLayout.Alignment.LEADING,
																											  jPanelClassementLayout.createSequentialGroup()
																																	.addGap(10,
																																			10,
																																			10)
																																	.addComponent(
																																			jPanelClassementMatrice,
																																			GroupLayout.PREFERRED_SIZE,
																																			230,
																																			GroupLayout.PREFERRED_SIZE))
																									  .addGroup(
																											  GroupLayout.Alignment.LEADING,
																											  jPanelClassementLayout.createSequentialGroup()
																																	.addGap(10,
																																			10,
																																			10)
																																	.addComponent(
																																			jPanelClassementFinal,
																																			GroupLayout.PREFERRED_SIZE,
																																			230,
																																			GroupLayout.PREFERRED_SIZE)))
																	  .addContainerGap(14, Short.MAX_VALUE))
											   );

		jTable3.setModel(new DefaultTableModel(
				new Object[][]{
						{"1: Importance égale"},
						{"3: Un peu plus important"},
						{"5: Plus important"},
						{"7: Beaucoup plus important"},
						{"9: Absolument plus important"},
						{"2,4,6: Valeurs intermédiaires"}
				},
				new String[]{"Scale of Saaty"}
		));
		jScrollPane3.setViewportView(jTable3);

		jTable4.setModel(new DefaultTableModel(
				new Object[][]{
						{"1/1: 1"},
						{"1/2: 0.5"},
						{"1/3: 0.3333"},
						{"1/4:0.25"},
						{"1/5:0.2"},
						{"1/6:0.1667"},
						{"1/7:0.1429"},
						{"1/8:0.125"},
						{"1/9:0.1111"}
				},
				new String[]{"Reverse scale of Saaty"}
		));
		jScrollPane4.setViewportView(jTable4);

		jButtonNewSimul.setText("Nouvelle Simulation");
		jButtonNewSimul.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				jButtonNewSimulActionPerformed(evt);
			}
		});

		jButtonAnglais.setText("Anglais");
		jButtonAnglais.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				jButtonAnglaisActionPerformed(evt);
			}
		});

		jButtonFrench.setText("Français");
		jButtonFrench.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				jButtonFrenchActionPerformed(evt);
			}
		});

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					  .addGroup(layout.createSequentialGroup()
									  .addContainerGap()
									  .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
													  .addGroup(layout.createSequentialGroup()
																	  .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
																					  .addGroup(layout.createParallelGroup(
																							  GroupLayout.Alignment.LEADING)
																									  .addGroup(layout.createSequentialGroup()
																													  .addComponent(
																															  jButtonNewSimul)
																													  .addPreferredGap(
																															  LayoutStyle.ComponentPlacement.RELATED,
																															  439,
																															  Short.MAX_VALUE)
																													  .addComponent(
																															  jLabelTime,
																															  GroupLayout.PREFERRED_SIZE,
																															  90,
																															  GroupLayout.PREFERRED_SIZE)
																													  .addGap(30,
																															  30,
																															  30))
																									  .addGroup(layout.createSequentialGroup()
																													  .addComponent(
																															  jLabel6,
																															  GroupLayout.PREFERRED_SIZE,
																															  562,
																															  GroupLayout.PREFERRED_SIZE)
																													  .addPreferredGap(
																															  LayoutStyle.ComponentPlacement.RELATED)))
																					  .addGroup(layout.createSequentialGroup()
																									  .addComponent(
																											  jPanelInit,
																											  GroupLayout.PREFERRED_SIZE,
																											  684,
																											  GroupLayout.PREFERRED_SIZE)
																									  .addPreferredGap(
																											  LayoutStyle.ComponentPlacement.RELATED)))
																	  .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
																					  .addGroup(layout.createSequentialGroup()
																									  .addComponent(
																											  jLabel3)
																									  .addPreferredGap(
																											  LayoutStyle.ComponentPlacement.UNRELATED)
																									  .addComponent(
																											  jTextFieldCR,
																											  GroupLayout.PREFERRED_SIZE,
																											  219,
																											  GroupLayout.PREFERRED_SIZE)
																									  .addPreferredGap(
																											  LayoutStyle.ComponentPlacement.RELATED)
																									  .addComponent(
																											  jLabel4,
																											  GroupLayout.DEFAULT_SIZE,
																											  320,
																											  Short.MAX_VALUE))
																					  .addGroup(layout.createSequentialGroup()
																									  .addGap(10,
																											  10,
																											  10)
																									  .addGroup(layout.createParallelGroup(
																											  GroupLayout.Alignment.LEADING)
																													  .addGroup(
																															  layout.createSequentialGroup()
																																	.addComponent(
																																			jScrollPane3,
																																			GroupLayout.PREFERRED_SIZE,
																																			175,
																																			GroupLayout.PREFERRED_SIZE)
																																	.addGap(18,
																																			18,
																																			18)
																																	.addComponent(
																																			jScrollPane4,
																																			GroupLayout.PREFERRED_SIZE,
																																			192,
																																			GroupLayout.PREFERRED_SIZE)
																																	.addPreferredGap(
																																			LayoutStyle.ComponentPlacement.RELATED,
																																			103,
																																			Short.MAX_VALUE)
																																	.addGroup(
																																			layout.createParallelGroup(
																																					GroupLayout.Alignment.LEADING)
																																				  .addComponent(
																																						  jButtonFrench)
																																				  .addComponent(
																																						  jButtonAnglais)))
																													  .addComponent(
																															  jButtonValiderMatrice)))))
													  .addGroup(layout.createSequentialGroup()
																	  .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
																					  .addComponent(jLabelCompteur,
																									GroupLayout.PREFERRED_SIZE,
																									105,
																									GroupLayout.PREFERRED_SIZE)
																					  .addComponent(jScrollPane1,
																									GroupLayout.PREFERRED_SIZE,
																									610,
																									GroupLayout.PREFERRED_SIZE))
																	  .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
																	  .addComponent(jPanelClassemnt,
																					GroupLayout.DEFAULT_SIZE,
																					639,
																					Short.MAX_VALUE)))
									  .addContainerGap())
								 );
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					  .addGroup(layout.createSequentialGroup()
									  .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
													  .addGroup(layout.createSequentialGroup()
																	  .addContainerGap()
																	  .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
																					  .addGroup(GroupLayout.Alignment.LEADING,
																								layout.createSequentialGroup()
																									  .addGroup(layout.createParallelGroup(
																											  GroupLayout.Alignment.LEADING)
																													  .addComponent(
																															  jButtonNewSimul)
																													  .addComponent(
																															  jLabelTime))
																									  .addPreferredGap(
																											  LayoutStyle.ComponentPlacement.RELATED)
																									  .addComponent(
																											  jPanelInit,
																											  GroupLayout.DEFAULT_SIZE,
																											  134,
																											  Short.MAX_VALUE)
																									  .addPreferredGap(
																											  LayoutStyle.ComponentPlacement.RELATED)
																									  .addComponent(
																											  jLabel6,
																											  GroupLayout.PREFERRED_SIZE,
																											  22,
																											  GroupLayout.PREFERRED_SIZE)
																									  .addGap(6, 6, 6))
																					  .addGroup(GroupLayout.Alignment.LEADING,
																								layout.createParallelGroup(
																										GroupLayout.Alignment.TRAILING)
																									  .addGroup(layout.createSequentialGroup()
																													  .addComponent(
																															  jScrollPane3,
																															  GroupLayout.PREFERRED_SIZE,
																															  128,
																															  GroupLayout.PREFERRED_SIZE)
																													  .addGap(18,
																															  18,
																															  18))
																									  .addGroup(
																											  GroupLayout.Alignment.LEADING,
																											  layout.createSequentialGroup()
																													.addComponent(
																															jScrollPane4,
																															GroupLayout.PREFERRED_SIZE,
																															171,
																															GroupLayout.PREFERRED_SIZE)
																													.addPreferredGap(
																															LayoutStyle.ComponentPlacement.RELATED)))))
													  .addGroup(layout.createSequentialGroup()
																	  .addComponent(jButtonAnglais)
																	  .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
																	  .addComponent(jButtonFrench)
																	  .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)))
									  .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
													  .addGroup(layout.createSequentialGroup()
																	  .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
																	  .addComponent(jScrollPane1,
																					GroupLayout.PREFERRED_SIZE,
																					336,
																					GroupLayout.PREFERRED_SIZE)
																	  .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
																	  .addComponent(jLabelCompteur,
																					GroupLayout.DEFAULT_SIZE,
																					34,
																					Short.MAX_VALUE))
													  .addGroup(layout.createSequentialGroup()
																	  .addGap(11, 11, 11)
																	  .addComponent(jButtonValiderMatrice)
																	  .addGap(18, 18, 18)
																	  .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
																					  .addGroup(layout.createParallelGroup(
																							  GroupLayout.Alignment.BASELINE)
																									  .addComponent(
																											  jTextFieldCR,
																											  GroupLayout.PREFERRED_SIZE,
																											  GroupLayout.DEFAULT_SIZE,
																											  GroupLayout.PREFERRED_SIZE)
																									  .addComponent(
																											  jLabel3))
																					  .addComponent(jLabel4,
																									GroupLayout.PREFERRED_SIZE,
																									20,
																									GroupLayout.PREFERRED_SIZE))
																	  .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
																					   23,
																					   Short.MAX_VALUE)
																	  .addComponent(jPanelClassemnt,
																					GroupLayout.PREFERRED_SIZE,
																					GroupLayout.DEFAULT_SIZE,
																					GroupLayout.PREFERRED_SIZE)))
									  .addContainerGap())
							   );

		pack();
	}

	/*
	 * Cette méthode creer les matrices et les retournes(vide ou rempli)
	 */
	public MyMatrix creerMatrice(int matrixSize, int type) {
		// ajoute un paramètre pour l'init du tableau ou la création de la matrice
		MyMatrix maMatrice = null;
		MatrixValue matrixValue = new MatrixValue();
		double newValue = 0;

		//Création matrice vide
		if (type == 0) {
			maMatrice = new MyMatrix(matrixSize, matrixSize);
		}
		//Création matrice rempli
		else {
			maMatrice = new MyMatrix(matrixSize, matrixSize);
			int choix;

			for (int i = 0; i < maMatrice.getRowDimension(); i++) {
				for (int j = i + 1; j < maMatrice.getColumnDimension(); j++) {

					//on récupère les valeurs saisies de la matrice
					monJep.parseExpression(jTableMatrice.getValueAt(i + 1, j + 1).toString());
					//on la stocke
					newValue = monJep.getValue();
					//newValue = Double.parseDouble(jTableMatrice.getValueAt(i+1, j+1).toString());
					//Si la valeur n'appartient pas à l'échelle de saaty
					//on demande a l'expert de la modifier
					while (!isInSaatysSacale(newValue)) {
						choix = 0;
						while (choix == 0) {
							String val = JOptionPane.showInputDialog(
									null,
									"Erreur avec la valeur " + newValue + " veuillez la re-saisir la valeur:",
									"Erreur saisi",
									JOptionPane.QUESTION_MESSAGE);
							jTableMatrice.setValueAt(val, i + 1, j + 1);
							//si clik Ok
							if (val != null) {
								//Si pas de saisi
								if (val.equalsIgnoreCase("")) {
									JOptionPane.showMessageDialog(
											null,
											"Veuillez saisir une valeur", "Information",
											JOptionPane.INFORMATION_MESSAGE);
								}
								//si saisi on récup la valeur
								else {
									choix = 1;
									//on récupère les valeurs saisies de la matrice
									monJep.parseExpression(val);
									//on la stocke
									newValue = monJep.getValue();
								}
							}
							//si clik Annuler
							else {
								choix = 1;
							}
						}
					}
					/*Partie supérieure*/
					matrixValue.setValue(newValue);
					matrixValue.setRow(i);
					matrixValue.setColumn(j);
					maMatrice.setMatrixValue(matrixValue);
					/*Réciprocité*/
					matrixValue.setValue(1 / newValue);
					matrixValue.setRow(j);
					matrixValue.setColumn(i);
					maMatrice.setMatrixValue(matrixValue);
				}
			}
			/*Diagonale*/
			for (int i = 0; i < maMatrice.getRowDimension(); i++) {
				matrixValue.setValue(1);
				matrixValue.setRow(i);
				matrixValue.setColumn(i);
				maMatrice.setMatrixValue(matrixValue);
			}
		}
		return maMatrice;
	}

	/** Cette méthode permet de tester les coefficients de saaty */
	public static boolean isInSaatysSacale(double value) {
		boolean result = false;

		for (int i = 0; i < SAATY_VALUES.length; i++) {
			double d = SAATY_VALUES[i];
			if (value == d) {
				result = true;
			}
		}
		return result;
	}

	/*
	 * Cette méthode permet de trouver les coefficients de saaty à changer
	 */
	public MatrixValue readSaatysRanking(
			Collection<MatrixValue> collectionOfSortedMatrixValues, MyMatrix myPreferenceMatrix,
			String file)
			throws
			IOException {

		boolean tempBoolean;
		int isValueChosen = 0;
		MatrixValue matrixValue = new MatrixValue();
		Iterator<MatrixValue> valueIterator;
		MatrixValue matrixValueToPrint = new MatrixValue();
		CharSequenceAppender monCsa = new CharSequenceAppender(file);
		MyMatrix tempMatrix = new MyMatrix();
		MyMatrix tempVector = new MyMatrix();
		String tempString;
		ConsistencyChecker consistencyChecker = new ConsistencyChecker();
		boolean isFound = false;
		MatrixValue tempMatrixValue = new MatrixValue();

		valueIterator = collectionOfSortedMatrixValues.iterator();

		MonCellRenderer monCell = new MonCellRenderer(0, 0);
		jTableMatrice.setDefaultRenderer(Object.class, monCell);

		while (isValueChosen == 0) {
			matrixValue = valueIterator.next();
			matrixValueToPrint.setRow(matrixValue.getRow());
			matrixValueToPrint.setColumn(matrixValue.getColumn());
			matrixValueToPrint.setValue(myPreferenceMatrix.get(matrixValueToPrint.getRow(), matrixValueToPrint.
																													  getColumn()));
			//on redessine la fenetre
			this.repaint();

			monCell.setCol(matrixValueToPrint.getColumn() + 1);
			monCell.setRow(matrixValueToPrint.getRow() + 1);
			//jTableMatrice.setDefaultRenderer(Object.class,new MonCellRenderer(matrixValueToPrint.getRow()+1,matrixValueToPrint.getColumn()+1));
			//jTable1.setDefaultRenderer(Object.class,new MonCellRenderer());
			//on ouvre un fenetre de dialogue pour afficher la valeur à modifier
			JOptionPane jop = new JOptionPane();
			int option = JOptionPane.showConfirmDialog(
					null,
					"Modifier la valeur "
					+ matrixValueToPrint.getValue()
					+ " ( "
					+ (matrixValueToPrint.getRow() + 1)
					+ " , "
					+ (matrixValueToPrint.getColumn() + 1)
					+ " )"
					+ " ?",
					"Modification des valeurs",
					JOptionPane.YES_NO_CANCEL_OPTION);

			//si on clique sur ok on sor du while
			if (option == JOptionPane.OK_OPTION) {
				isValueChosen = 1;
			} else if (option == JOptionPane.CANCEL_OPTION) {
				isValueChosen = 1;
				finSimulation = -1;
			} else if (!valueIterator.hasNext()) {
				//System.out.println("Retour en haut du classement");
				valueIterator = collectionOfSortedMatrixValues.iterator();
			}
		}
		//si l'expert n'a pas mit à la simulation
		if (finSimulation != -1) {
			/*parcours de la liste pour l'écriture dans le fichier*/
			valueIterator = collectionOfSortedMatrixValues.iterator();

			while ((valueIterator.hasNext()) && (!isFound)) {

				tempMatrixValue = valueIterator.next();
				monCsa.appendLineFeed();

				/*écriture du best fit associé à la valeur proposée*/
				//copie de la matrice initiale
				tempMatrix = tempMatrix.copyMyMatrix(myPreferenceMatrix);

				//calcul du vecteur propre associé à tempMatrix
				tempVector = PriorityVector.build(tempMatrix);
				//calcul du best fit
				double BestFit = SaatyTools.calculateBestFit(
						tempMatrix,
						tempVector,
						tempMatrixValue.getRow(),
						tempMatrixValue.getColumn());
				//écriture du best fit
				tempString = "" + BestFit;
				monCsa.append(tempString);
				monCsa.appendCommaSeparator();

				/*écriture des indices de la valeur proposée par Saaty dans le fichier*/
				tempString = "" + (tempMatrixValue.getRow() + 1);
				monCsa.append(tempString);
				monCsa.appendCommaSeparator();
				tempString = "" + (tempMatrixValue.getColumn() + 1);
				monCsa.append(tempString);
				monCsa.appendCommaSeparator();

				/*écriture de la cohérence si l'expert suivait les conseils de Saaty*/

				//remplacement de la valeur (i,j) par BestFit
				MatrixValue newMatrixValue = new MatrixValue();
				newMatrixValue.setRow(tempMatrixValue.getRow());
				newMatrixValue.setColumn(tempMatrixValue.getColumn());
				newMatrixValue.setValue(BestFit);
				tempMatrix.setMatrixValue(newMatrixValue);

				//remplacement de la valeur (j,i) par 1/BestFit
				newMatrixValue.setRow(tempMatrixValue.getColumn());
				newMatrixValue.setColumn(tempMatrixValue.getRow());
				newMatrixValue.setValue(1. / BestFit);
				tempMatrix.setMatrixValue(newMatrixValue);

				//rafraîchissement du vecteur de priorité
				tempVector = PriorityVector.build(tempMatrix);
				//calcul et écriture de la cohérence
				tempBoolean = consistencyChecker.isConsistent(tempMatrix, tempVector);
				saatyConsistency = consistencyChecker.getConsistencyRatio();
				tempString = "" + consistencyChecker.getConsistencyRatio();
				monCsa.append(tempString);
				monCsa.appendCommaSeparator();


				if (matrixValue.equals(tempMatrixValue)) {
					isFound = true;
				}
			}
			monCsa.close();
		} else {
			monCsa.close();
		}
		return matrixValue;
	}

	private void showMatrixTable(MyMatrixTable maTable, MyMatrix myMatrix)
			throws
			HeadlessException {
		// Show a frame with a table
		//this.setSize(1000, 35 * myMatrix.getRowDimension());
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//JPanel pan = new JPanel();
		//pan.add(new JButton("Valider"));
		this.setContentPane(maTable);
		//this.setContentPane(pan, BorderLayout.SOUTH);
		//this.setContentPane(maTable);
		//this.setSize(1000, 27 * myMatrix.getRowDimension());
	}

	private void jButtonOKActionPerformed(ActionEvent evt) {
		//on crée et lance le thread pour afficher un compteur

		monHeure = new Meter(jLabelTime, this);
		monHeure.setCommencerAZero(false);
		monHeure.start();

		myMatrix = new MyMatrix();
		matrixTableModel = new MyMatrixTableModel();
		//On crée la matrice vide
		myMatrix = creerMatrice(Integer.parseInt(jTextFieldDimensions.getText()), 0);
		//maTable = new MyMatrixTable();

		//on teste pour savoir quelle est le pb choisi
		if (jRadioButtonP1.isSelected() == true) {
			//on crée le fichier historique à partir de "file"
			fileHistorique = file + "histI.csv";
			file += "I.csv";
			try {
				csa = new CharSequenceAppender(file);
				csa.append("Probleme 1");
				csa.appendCommaSeparator();
				//On teste saaty ou aléatoire
				if (jRadioButtonSaaty.isSelected() == true) {
					csa.append("Saaty");
				} else {
					csa.append("Aleatoire");
				}
				csa.appendLineFeed();
			} catch (IOException ex) {
				Logger.getLogger(InterfaceAHP.class.getName()).log(Level.SEVERE, null, ex);
			}
		} else {
			//on crée le fichier historique à partir de "file"
			fileHistorique = file + "histII.csv";
			file += "II.csv";
			try {
				csa = new CharSequenceAppender(file);
				csa.append("Probleme 2");
				csa.appendCommaSeparator();
				//On teste saaty ou aléatoire
				if (jRadioButtonSaaty.isSelected() == true) {
					csa.append("Saaty");
				} else {
					csa.append("Aleatoire");
				}
				csa.appendLineFeed();
			} catch (IOException ex) {
				Logger.getLogger(InterfaceAHP.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		/*Interface graphique*/
		matrixTableModel.setMatrix(myMatrix,
								   SampleMatrixHeaders.getColumnHeader(jRadioButtonP1.isSelected(), modeAnglais));
		//on ajoute directement matrixTableModel à jTable1 bugg??????
		jTableMatrice.setModel(matrixTableModel);
		//maTable.setModel(matrixTableModel);
		jLabel6.setText(
				"Veuillez remplir la partie supérieure (cases blanches) de la matrice avec les coefficients de SAATY:");
		//on affiche la matrice
		//showMatrixTable((MyMatrixTable) jTable1,myMatrix);
		//on rempli les jComboBox par le nom des column pour le 1er classement
		String[] mesColonnes = new String[myMatrix.getColumnDimension()];
		mesColonnes = SampleMatrixHeaders.getColumnHeader(jRadioButtonP1.isSelected(), modeAnglais);
		remplirJComboBox(jComboBox1erClass1, mesColonnes);
		remplirJComboBox(jComboBox1erClass2, mesColonnes);
		remplirJComboBox(jComboBox1erClass3, mesColonnes);
		remplirJComboBox(jComboBox1erClass4, mesColonnes);
		remplirJComboBox(jComboBox1erClass5, mesColonnes);
		remplirJComboBox(jComboBox1erClass6, mesColonnes);
		//on modifie l'affichage de la table
		MonCellRenderer monCellRenderer = new MonCellRenderer(0, 0);
		jTableMatrice.setDefaultRenderer(Object.class, monCellRenderer);
	}

	/*
	 * cette méthode rempli les items d'un jComboBox
	 */
	private static void remplirJComboBox(JComboBox<String> jComboBox, String... names) {
		for (String aName : names) {
			jComboBox.addItem(aName);
		}
	}

	/*
	 * Cette méthode ecrit dans le fichier csv
	 */
	private double creerFichierCsv() {
		ConsistencyChecker consistencyChecker = new ConsistencyChecker();
		PriorityVector priorityVector = PriorityVector.build(myMatrix);
		MyMatrix epsilon = new MyMatrix();
		MatrixValue matrixValue = new MatrixValue();
		Collection<MatrixValue> collectionOfSortedMatrixValues = new ArrayList<MatrixValue>();
		double newValue = 0;
		double oldValue;
		int coordRowVal;
		int coordColVal;
		/*Ecriture de la matrice et du vecteur de priorité dans le fichier*/
		csa.append("Matrice de preferences initiale");
		csa.appendLineFeed();
		csa.append(myMatrix);
		csa.appendLineFeed();
		csa.append("Vecteur de priorite initial");
		csa.appendLineFeed();
		csa.append(priorityVector);
		csa.appendLineFeed();

		//on crée le fichier et on l'initialise avec la 1ère matrice
		this.ecrirefichierHistorique(myMatrix, 0, 0, 0, 0);

		//on va afficher le classement des critères de la 1ere matrice
		String[] classement = this.classerCriteres(priorityVector);
		jTextFieldClassMat1.setText(classement[5]);
		jTextFieldClassMat2.setText(classement[4]);
		jTextFieldClassMat3.setText(classement[3]);
		jTextFieldClassMat4.setText(classement[2]);
		jTextFieldClassMat5.setText(classement[1]);
		jTextFieldClassMat6.setText(classement[0]);
		double[] classementVal = classerPourcentageCritères(priorityVector);
		jLabelClassInit1.setText(classementVal[5] + "%");
		jLabelClassInit2.setText(classementVal[4] + "%");
		jLabelClassInit3.setText(classementVal[3] + "%");
		jLabelClassInit4.setText(classementVal[2] + "%");
		jLabelClassInit5.setText(classementVal[1] + "%");
		jLabelClassInit6.setText(classementVal[0] + "%");
		/*Ecriture du CR*/
		consistencyChecker.isConsistent(myMatrix, priorityVector);
		String tempString = "" + consistencyChecker.getConsistencyRatio();
		csa.append("CR initial");
		csa.appendLineFeed();
		csa.append(tempString);
		csa.appendLineFeed();
		csa.appendLineFeed();

		//On affiche le cr
		jTextFieldCR.setText(String.valueOf(consistencyChecker.getConsistencyRatio()));
		csa.append("Tableau de changement des valeurs");
		csa.appendLineFeed();
		//en-tête du tableau, on teste quelle est la méthode chosi(Aléatoire, Saaty)
		if (jRadioButtonSaaty.isSelected() == true) {
			csa.append(
					"BestFit;Saaty i;Saaty j; Saaty consistency;Expert Init Value;Expert Changed Value ; Expert Position in Saaty's ranking;CR;SaatyC-CR\n");
		} else {
			csa.append(
					"BestFit;Saaty i;Saaty j;Saaty consistency;BestFit for random value;Random i;Random j;Position in Saaty's ranking;Random consistency;Expert Init Value;Expert Changed Value;CR\n");
		}

		csa.appendLineFeed();
		csa.close();
		int iterationCounter = 0;
		boolean test = false;
		boolean myConsistent = consistencyChecker.isConsistent(myMatrix, priorityVector);

		while (!myConsistent) {

			//On continue si l'expert n'a pas choisi de mettre fin à la simulation
			if (finSimulation != -1) {
				//incrémentation du compteur du nombre d'itération
				iterationCounter++;

				//on teste quelles méthode(aléaloire, saaty)
				if (jRadioButtonSaaty.isSelected() == true) {
					/*Calcul matrice epsilon*/
					epsilon = SaatyTools.calculateEpsilonMatrix(myMatrix, priorityVector);
					/*Recherche de la valeur à modifier*/
					collectionOfSortedMatrixValues = SaatyTools.getRank(myMatrix, priorityVector, epsilon);
					try {
						matrixValue = readSaatysRanking(collectionOfSortedMatrixValues, myMatrix, file);
					} catch (IOException ex) {
						Logger.getLogger(InterfaceAHP.class.getName()).log(Level.SEVERE, null, ex);
					}
				} else {
					collectionOfSortedMatrixValues = RandomTools.getRank(myMatrix);
					matrixValue = getValueToModifiyByRanking(collectionOfSortedMatrixValues);
					try {
						/*Writing of Saaty's propositions and of random ranking*/
						RandomTools.writeRandomAndSaatyPropositions(
								myMatrix,
								collectionOfSortedMatrixValues,
								matrixValue,
								priorityVector,
								file);
					} catch (IOException ex) {
						Logger.getLogger(InterfaceAHP.class.getName()).log(Level.SEVERE, null, ex);
					}

				}
				//si l'expert n'a pas mis fin è la simulation et veut changer le coeff
				if (finSimulation != -1) {
					//on stocke les différentes valeur pour le fichier historique
					oldValue = myMatrix.get(matrixValue.getRow(), matrixValue.getColumn());
					coordRowVal = (matrixValue.getRow() + 1);
					coordColVal = (matrixValue.getColumn() + 1);
					//on va tester le joptionPane
					test = false;
					while (test == false) {
						JOptionPane jopValid = new JOptionPane();
						String val = JOptionPane.showInputDialog(
								null,
								"Vous avez choisi de remplacer la valeur " + oldValue + " de coordonnées " + " ( "
								+ coordRowVal + " , " + coordColVal + " ) par:", "Remplacement valeur",
								JOptionPane.QUESTION_MESSAGE);

						if (val != null) {
							if (val.equalsIgnoreCase("")) {
								JOptionPane.showMessageDialog(
										null,
										"Veuillez saisir une valeur", "Information",
										JOptionPane.INFORMATION_MESSAGE);
							} else {
								test = true;
								monJep.parseExpression(val);
								newValue = monJep.getValue();
							}
						} else {
							test = false;
						}
					}

					try {
						/*Ecrire la valeur que souhaite modifier l'expert*/
						csa = new CharSequenceAppender(file);
					} catch (IOException ex) {
						Logger.getLogger(InterfaceAHP.class.getName()).log(Level.SEVERE, null, ex);
					}
					tempString = "" + myMatrix.get(matrixValue.getRow(), matrixValue.getColumn());
					csa.append(tempString);
					csa.appendCommaSeparator();
					//Si la valeur modifier n'appartient pas à l'échelle de saaty
					//on demande a l'expert de la modifier
					while (!isInSaatysSacale(newValue)) {
						int choix = 0;
						while (choix == 0) {
							JOptionPane jop = new JOptionPane();
							String val = JOptionPane.showInputDialog(
									null,
									"Erreur avec la valeur " + newValue + " veuillez la re-saisir la valeur:",
									"Erreur saisi",
									JOptionPane.QUESTION_MESSAGE);
							if (val != null) {
								if (val.equalsIgnoreCase("")) {
									JOptionPane.showMessageDialog(
											null,
											"Veuillez saisir une valeur", "Information",
											JOptionPane.INFORMATION_MESSAGE);
								} else {
									choix = 1;
									monJep.parseExpression(val);
									newValue = monJep.getValue();
								}
							} else {
								choix = 1;
							}
						}
					}
					/*Ecrire la valeur modifiée par l'utilisateur*/
					tempString = "" + newValue;
					csa.append(tempString);
					csa.appendCommaSeparator();
					//uniquement si on fai la méthode saaty
					if (jRadioButtonSaaty.isSelected() == true) {
						/*Calculer le placement dans le classement de Saaty*/
						int location = SaatyTools.getLocationInRank(
								collectionOfSortedMatrixValues,
								matrixValue.getRow(),
								matrixValue.getColumn());

						tempString = "" + location;
						csa.append(tempString);
						csa.appendCommaSeparator();
					}
					/*Changement d'une valeur et de la valeur réciproque associée dans
					la matrice*/

					//Valeur directement modifiée
					matrixValue.setValue(newValue);
					myMatrix.setMatrixValue(matrixValue);

					//Valeur réciproquement modifiée
					int tempI = matrixValue.getRow();
					int tempJ = matrixValue.getColumn();
					matrixValue.setValue(1 / newValue);
					matrixValue.setRow(tempJ);
					matrixValue.setColumn(tempI);
					myMatrix.setMatrixValue(matrixValue);

					//Affichage nouvelle matrice
					//	myMatrix.print(5, 5);

					//Affichage nouvelle matrice
					matrixTableModel.setMatrix(myMatrix,
											   SampleMatrixHeaders.getColumnHeader(jRadioButtonP1.isSelected(),
																				   modeAnglais));
					jTableMatrice.setModel(matrixTableModel);

					//Réactualisation du vecteur de priorité associé à la nouvelle matrice
					priorityVector = PriorityVector.build(myMatrix);
					//		priorityVector.print(5, 5);

					//Ecriture du nouveau CR
					consistencyChecker.isConsistent(myMatrix, priorityVector);
					tempString = "" + consistencyChecker.getConsistencyRatio();
					csa.append(tempString);
					csa.appendCommaSeparator();
					//uniquement si on fai la méthode saaty
					if (jRadioButtonSaaty.isSelected() == true) {
						tempString = String.valueOf(saatyConsistency - consistencyChecker.getConsistencyRatio());
						csa.append(tempString);
					}
					csa.close();
					//on va ecrire dans le fichier historique
					this.ecrirefichierHistorique(myMatrix, oldValue, coordRowVal, coordColVal, newValue);
					//on récupére la nouvelle valeur
					myConsistent = consistencyChecker.isConsistent(myMatrix, priorityVector);
				}
				//si il à mi fin on sort de la boucle
				else if (finSimulation == -1) {
					//on arrete le thread
					monHeure.stop();
					myConsistent = true;
				}
				//on affiche un cpt pour les observations avec un coeff de 7.5
				jLabelCompteur.setText(String.valueOf(iterationCounter * 7.5));
				jTextFieldCR.setText(String.valueOf(consistencyChecker.getConsistencyRatio()));
			}
		}
		if (finSimulation != -1) {
			jLabel4.setText("Bravo la matrice est cohérente!!!!!");
		}
		try {
			csa = new CharSequenceAppender(file);
		} catch (IOException ex) {
			Logger.getLogger(InterfaceAHP.class.getName()).log(Level.SEVERE, null, ex);
		}
		/*Ecriture de la matrice et du vecteur de priorité dans le fichier*/
		csa.appendLineFeed();
		csa.appendLineFeed();
		csa.append("Matrice de preference finale");
		csa.appendLineFeed();
		csa.append(myMatrix);
		csa.appendLineFeed();
		csa.append("Vecteur de priorite final");
		csa.appendLineFeed();
		csa.append(priorityVector);
		csa.appendLineFeed();

		//on va afficher le classement final des critères
		String[] classementF = this.classerCriteres(priorityVector);
		jTextFieldClassFinal1.setText(classementF[5]);
		jTextFieldClassFinal2.setText(classementF[4]);
		jTextFieldClassFinal3.setText(classementF[3]);
		jTextFieldClassFinal4.setText(classementF[2]);
		jTextFieldClassFinal5.setText(classementF[1]);
		jTextFieldClassFinal6.setText(classementF[0]);
		double[] classementValF = classerPourcentageCritères(priorityVector);
		jLabelClassFinal1.setText(classementValF[5] + "%");
		jLabelClassFinal2.setText(classementValF[4] + "%");
		jLabelClassFinal3.setText(classementValF[3] + "%");
		jLabelClassFinal4.setText(classementValF[2] + "%");
		jLabelClassFinal5.setText(classementValF[1] + "%");
		jLabelClassFinal6.setText(classementValF[0] + "%");

		//Ecriture du CR
		consistencyChecker.isConsistent(myMatrix, priorityVector);
		tempString = "" + consistencyChecker.getConsistencyRatio();
		csa.append("CR final");
		csa.appendLineFeed();
		csa.append(tempString);
		csa.appendLineFeed();
		csa.appendLineFeed();

		tempString = "Number of Iterations;" + iterationCounter;
		csa.append(tempString);

		csa.close();
		return consistencyChecker.getConsistencyRatio();
	}

	private void jButtonValiderMatriceActionPerformed(ActionEvent evt) {
		// TODO add your handling code here:

		myMatrix = new MyMatrix();
		matrixTableModel = new MyMatrixTableModel();
		//On crée la matrice rempli
		myMatrix = creerMatrice(Integer.parseInt(jTextFieldDimensions.getText()), 1);
		//maTable = new MyMatrixTable();
		/*Interface graphique*/
		matrixTableModel.setMatrix(myMatrix,
								   SampleMatrixHeaders.getColumnHeader(jRadioButtonP1.isSelected(), modeAnglais));
		//on ajoute directement matrixTableModel à jTable1 bugg??????
		jTableMatrice.setModel(matrixTableModel);
		//maTable.setModel(matrixTableModel);
		//on va cree le fichier .csv et le modifier et on récupérer le CR et afficher
		jTextFieldCR.setText(String.valueOf(creerFichierCsv()));
		//on affiche la matrice
		//showMatrixTable( (MyMatrixTable) jTable1,myMatrix);
		//on ferme le flux
		csa.close();
		//on remet à 0 l'attribut
		finSimulation = 0;
		//on arrete le thread si c pas déja fait
		if (finSimulation != -1) {
			monHeure.stop();
		}
		//on réaffiche le classement intuitif
		jPanel1erClassement.setVisible(true);
	}

	/*
	 * Cette méthode permet de stocker les différentes matrices,les valeurs modifiées
	 */
	private void ecrirefichierHistorique(MyMatrix matrix, double oldValue, int coordx,
										 int coordy, double newVal) {
		CharSequenceAppender csa = null;
		String tempString;
		try {
			csa = new CharSequenceAppender(fileHistorique);
		} catch (IOException ex) {
			Logger.getLogger(InterfaceAHP.class.getName()).log(Level.SEVERE, null, ex);
		}
		tempString = "Ancienne valeur: " + oldValue;
		csa.append(tempString);
		csa.appendCommaSeparator();
		tempString = coordx + "," + coordy;
		csa.append(tempString);
		csa.appendCommaSeparator();
		tempString = "Nouvelle valeur: " + newVal;
		csa.append(tempString);
		csa.appendLineFeed();
		csa.append(matrix);
		csa.appendLineFeed();
		csa.close();
	}

	/** This method returns the value which will be modified by the expert */
	public MatrixValue getValueToModifiyByRanking(
			Collection<MatrixValue> collectionOfNonSortedMatrixValues) {

		int isValueChosen = 0;
		MatrixValue matrixValue = new MatrixValue();
		Iterator<MatrixValue> valueIterator;
		/* While loop which proposes a random ranking of MatrixValue
		 * while the expert hasn't chosen the value he wants to modify
		 */
		valueIterator = collectionOfNonSortedMatrixValues.iterator();
		MonCellRenderer monCell = new MonCellRenderer(0, 0);
		jTableMatrice.setDefaultRenderer(Object.class, monCell);
		while (isValueChosen == 0) {
			matrixValue = valueIterator.next();
			//on redessine la fenetre
			this.repaint();

			monCell.setCol(matrixValue.getColumn() + 1);
			monCell.setRow(matrixValue.getRow() + 1);
			//on ouvre un fenetre de dialogue pour afficher la valeur à modifier
			JOptionPane jop = new JOptionPane();
			int option = JOptionPane.showConfirmDialog(
					null,
					"Modifier la valeur "
					+ matrixValue.getValue()
					+ " ( "
					+ (matrixValue.getRow() + 1)
					+ " , "
					+ (matrixValue.getColumn() + 1)
					+ " )"
					+ " ?",
					"Modification des valeurs",
					JOptionPane.YES_NO_CANCEL_OPTION);
			//si on clique sur ok on sor du while
			if (option == JOptionPane.OK_OPTION) {
				isValueChosen = 1;
				changerCoeff = true;
			} else if (option == JOptionPane.CANCEL_OPTION) {
				isValueChosen = 1;
				finSimulation = -1;
			} else if (!valueIterator.hasNext()) {
				//System.out.println("Retour en haut du classement");
				valueIterator = collectionOfNonSortedMatrixValues.iterator();
			}
		}
		return matrixValue;
	}

	/*
	 * cette méthode classe le vector priority
	 */
	private static double[] classerPourcentageCritères(PriorityVector v) {
		double[] monClassement = new double[v.getRowDimension()];
		for (int i = 0; i < v.getRowDimension(); i++) {
			monClassement[i] = round(v.get(i, 0) * 100, 1);
		}
		//on classe par ordre croissant
		Arrays.sort(monClassement);
		return monClassement;
	}

	/*
	 * méthode qui arrondi la valeur
	 */
	private static double round(double what, int howmuch) {
		return (double) ((int) (what * Math.pow(10, howmuch) + .5)) / Math.pow(10, howmuch);
	}

	/*
	 * Cette méthode permet de classer les critères et de renvoyer le classement
	 */
	private String[] classerCriteres(PriorityVector v) {
		//on teste quel est le pb choisi
		if (jRadioButtonP1.isSelected() == true) {

			String[] classementString = new String[v.getRowDimension()];
			double[] monClassement = new double[v.getRowDimension()];
			for (int i = 0; i < v.getRowDimension(); i++) {
				monClassement[i] = v.get(i, 0);
			}
			//on classe par ordre croissant
			Arrays.sort(monClassement);

			for (int i = 0; i < v.getRowDimension(); i++) {
				String columnNames[] = SampleMatrixHeaders.getColumnHeader(jRadioButtonP1.isSelected(), modeAnglais);
				for (int j = 0; j < v.getRowDimension(); j++) {
					int temp = j;
					//on récupérer les critères classés
					if (monClassement[i] == v.get(j, 0)) {
						classementString[i] = columnNames[j];
						//on sor du for
						j = v.getRowDimension();
						if (i != 0) {
							//on teste l'ancienne valeur du classement pour éviter lesdoublons
							if (monClassement[i] != monClassement[i - 1]) {
								j = temp;
							}
						}
					}

				}
			}
			return classementString;
		} else {
			String columnNames[] = SampleMatrixHeaders.getColumnHeader(jRadioButtonP1.isSelected(), modeAnglais);
			String[] classementString = new String[v.getRowDimension()];
			double[] monClassement = new double[v.getRowDimension()];
			for (int i = 0; i < v.getRowDimension(); i++) {
				monClassement[i] = v.get(i, 0);

			}
			//on classe par ordre croissant
			Arrays.sort(monClassement);

			for (int i = 0; i < v.getRowDimension(); i++) {
				for (int j = 0; j < v.getRowDimension(); j++) {
					int temp = j;
					//on récupérer les critères classés
					if (monClassement[i] == v.get(j, 0)) {
						classementString[i] = columnNames[j];
						//on sor du for
						j = v.getRowDimension();
						if (i != 0) {
							//on teste l'ancienne valeur du classement pour éviter lesdoublons
							if (monClassement[i] != monClassement[i - 1]) {
								j = temp;
							}
						}
					}
				}
			}
			return classementString;
		}
	}

	private void jButtonNewSimulActionPerformed(ActionEvent evt) {
		initComponents();
		this.dispose();
		new InterfaceAHP().setVisible(true);
	}

	private void jButtonParcourirActionPerformed(ActionEvent evt) {
		JFileChooser jfc = new JFileChooser();
		jfc.setVisible(true);
		int choix = jfc.showSaveDialog(this);
		if (JFileChooser.APPROVE_OPTION == choix) {
			//On récup le chemin
			file = jfc.getSelectedFile().getAbsolutePath();
			//On affiche le chemin de sauvegarde
			jTextFieldChemin.setText(file);
		}
	}

	private void jButtonOkClassIntuitifActionPerformed(ActionEvent evt) {
		jPanel1erClassement.setVisible(false);
	}

	private void jButtonAnglaisActionPerformed(ActionEvent evt) {
		// TODO add your handling code here:
		modeAnglais = true;
		jButtonFrench.setText("French");
		//Panel init
		jButtonNewSimul.setText("New Simulation");
		jPanelInit.setBorder(BorderFactory.createTitledBorder("Initialization"));
		jLabel1.setText("Matrix size:");
		jLabel2.setText("Saving in:");
		jButtonParcourir.setText("Browse");
		//Tables échelle saaty
		jTable3.setValueAt("1: equally important ", 0, 0);
		jTable3.setValueAt("3: slightly larger ", 1, 0);
		jTable3.setValueAt("5: largest ", 2, 0);
		jTable3.setValueAt("7: much larger ", 3, 0);
		jTable3.setValueAt("9: absolutely more important ", 4, 0);
		jTable3.setValueAt("2,4,6: intermediate values ", 5, 0);
		//panel classement
		jPanel1erClassement.setBorder(BorderFactory.createTitledBorder("Intuitive classement"));
		jPanelClassementMatrice.setBorder(BorderFactory.createTitledBorder("Initial classement"));
		jPanelClassementFinal.setBorder(BorderFactory.createTitledBorder("Final classement"));
		//bouton valider matrice
		jButtonValiderMatrice.setText("Validate Matrix");
		//Jlabel dans panel classement
		jLabel5.setText("1st:");
		jLabel12.setText("1st:");
		jLabel18.setText("1st:");
		jLabel7.setText("2nd:");
		jLabel13.setText("2nd:");
		jLabel19.setText("2nd:");
		jLabel8.setText("3th:");
		jLabel14.setText("3th:");
		jLabel20.setText("3th:");
		jLabel9.setText("4th:");
		jLabel15.setText("4th:");
		jLabel21.setText("4th:");
		jLabel10.setText("5th:");
		jLabel16.setText("5th:");
		jLabel22.setText("5th:");
		jLabel11.setText("6th:");
		jLabel17.setText("6th:");
		jLabel23.setText("6th:");
		jLabel20.setText("3th:");
		jLabel20.setText("3th:");
	}

	private void jButtonFrenchActionPerformed(ActionEvent evt) {
		//on repass à false le mode anglais
		modeAnglais = false;
		initComponents();
		this.dispose();
		new InterfaceAHP().setVisible(true);
	}

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {

				new InterfaceAHP().setVisible(true);

			}
		});
	}

	// Variables declaration
	private ButtonGroup       buttonGroup1            = new ButtonGroup();
	private ButtonGroup       buttonGroup2            = new ButtonGroup();
	private JButton           jButtonAnglais          = new JButton();
	private JButton           jButtonFrench           = new JButton();
	private JButton           jButtonNewSimul         = new JButton();
	private JButton           jButtonOK               = new JButton();
	private JButton           jButtonOkClassIntuitif  = new JButton();
	private JButton           jButtonParcourir        = new JButton();
	private JButton           jButtonValiderMatrice   = new JButton();
	private JComboBox<String> jComboBox1erClass1      = new JComboBox();
	private JComboBox<String> jComboBox1erClass2      = new JComboBox();
	private JComboBox<String> jComboBox1erClass3      = new JComboBox();
	private JComboBox<String> jComboBox1erClass4      = new JComboBox();
	private JComboBox<String> jComboBox1erClass5      = new JComboBox();
	private JComboBox<String> jComboBox1erClass6      = new JComboBox();
	private JLabel            jLabel1                 = new JLabel();
	private JLabel            jLabel10                = new JLabel();
	private JLabel            jLabel11                = new JLabel();
	private JLabel            jLabel12                = new JLabel();
	private JLabel            jLabel13                = new JLabel();
	private JLabel            jLabel14                = new JLabel();
	private JLabel            jLabel15                = new JLabel();
	private JLabel            jLabel16                = new JLabel();
	private JLabel            jLabel17                = new JLabel();
	private JLabel            jLabel18                = new JLabel();
	private JLabel            jLabel19                = new JLabel();
	private JLabel            jLabel2                 = new JLabel();
	private JLabel            jLabel20                = new JLabel();
	private JLabel            jLabel21                = new JLabel();
	private JLabel            jLabel22                = new JLabel();
	private JLabel            jLabel23                = new JLabel();
	private JLabel            jLabel3                 = new JLabel();
	private JLabel            jLabel4                 = new JLabel();
	private JLabel            jLabel5                 = new JLabel();
	private JLabel            jLabel6                 = new JLabel();
	private JLabel            jLabel7                 = new JLabel();
	private JLabel            jLabel8                 = new JLabel();
	private JLabel            jLabel9                 = new JLabel();
	private JLabel            jLabelClassFinal1       = new JLabel();
	private JLabel            jLabelClassFinal2       = new JLabel();
	private JLabel            jLabelClassFinal3       = new JLabel();
	private JLabel            jLabelClassFinal4       = new JLabel();
	private JLabel            jLabelClassFinal5       = new JLabel();
	private JLabel            jLabelClassFinal6       = new JLabel();
	private JLabel            jLabelClassInit1        = new JLabel();
	private JLabel            jLabelClassInit2        = new JLabel();
	private JLabel            jLabelClassInit3        = new JLabel();
	private JLabel            jLabelClassInit4        = new JLabel();
	private JLabel            jLabelClassInit5        = new JLabel();
	private JLabel            jLabelClassInit6        = new JLabel();
	private JLabel            jLabelCompteur          = new JLabel();
	private JLabel            jLabelTime              = new JLabel();
	private JPanel            jPanel1erClassement     = new JPanel();
	private JPanel            jPanelClassementFinal   = new JPanel();
	private JPanel            jPanelClassementMatrice = new JPanel();
	private JPanel            jPanelClassemnt         = new JPanel();
	private JPanel            jPanelInit              = new JPanel();
	private JRadioButton      jRadioButtonAleatoire   = new JRadioButton();
	private JRadioButton      jRadioButtonP1          = new JRadioButton();
	private JRadioButton      jRadioButtonP2          = new JRadioButton();
	private JRadioButton      jRadioButtonSaaty       = new JRadioButton();
	private JScrollPane       jScrollPane1            = new JScrollPane();
	private JScrollPane       jScrollPane3            = new JScrollPane();
	private JScrollPane       jScrollPane4            = new JScrollPane();
	private JTable            jTable3                 = new JTable();
	private JTable            jTable4                 = new JTable();
	private JTable            jTableMatrice           = new JTable();
	private JTextField        jTextFieldCR            = new JTextField();
	private JTextField        jTextFieldChemin        = new JTextField();
	private JTextField        jTextFieldClassFinal1   = new JTextField();
	private JTextField        jTextFieldClassFinal2   = new JTextField();
	private JTextField        jTextFieldClassFinal3   = new JTextField();
	private JTextField        jTextFieldClassFinal4   = new JTextField();
	private JTextField        jTextFieldClassFinal5   = new JTextField();
	private JTextField        jTextFieldClassFinal6   = new JTextField();
	private JTextField        jTextFieldClassMat1     = new JTextField();
	private JTextField        jTextFieldClassMat2     = new JTextField();
	private JTextField        jTextFieldClassMat3     = new JTextField();
	private JTextField        jTextFieldClassMat4     = new JTextField();
	private JTextField        jTextFieldClassMat5     = new JTextField();
	private JTextField        jTextFieldClassMat6     = new JTextField();
	private JTextField        jTextFieldDimensions    = new JTextField();
	// End of variables declaration
}

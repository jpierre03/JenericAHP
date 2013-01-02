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
import org.taeradan.ahp.prototype.gui.matrix.MyMatrixTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Dimension;
import java.awt.EventQueue;
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

	private AHP_Data ahpData       = new AHP_Data();
	private boolean  finSimulation = false;
	private boolean  modeAnglais   = false;
	private JEP      monJep        = new JEP();
	private CharSequenceAppender csa;
	private String               file;
	private String               fileHistorique;
	private Meter                monHeure;


	private class AHP_Data {
		private final double[] saatyValues = {1. / 9,
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

		private MyMatrix           myMatrix         = new MyMatrix();
		private MyMatrixTableModel matrixTableModel = new MyMatrixTableModel();
		private double             saatyConsistency = Double.NaN;

		public boolean isInSaatyScale(double value) {
			boolean result = false;

			for (int i = 0; i < saatyValues.length; i++) {
				double d = saatyValues[i];
				if (value == d) {
					result = true;
				}
			}
			return result;
		}
	}

	public InterfaceAHP() {
		definePreferredFrameSize();

		initComponents();
	}

	private void definePreferredFrameSize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenSize.setSize(screenSize.getWidth(), screenSize.getHeight() - 100);
		this.setPreferredSize(screenSize);
		this.setResizable(true);
	}

	private void initComponents() {

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);

		jButtonValiderMatrice.setText("Valider Matrice");
		jButtonValiderMatrice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				jButtonValiderMatriceActionPerformed(event);
			}
		});

		jTableMatrice.setModel(new DefaultTableModel(
				new Object[][]{{}, {}, {}, {}},
				new String[]{}
		));
		jScrollPane1.setViewportView(jTableMatrice);

		jLabel3.setText("CR:");

		makeInitialisationPanel();

		makeRankingPanel();

		saatyScaleTable.setModel(new DefaultTableModel(
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
		jScrollPane3.setViewportView(saatyScaleTable);

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
			public void actionPerformed(ActionEvent event) {
				jButtonNewSimulActionPerformed(event);
			}
		});

		jButtonAnglais.setText("Anglais");
		jButtonAnglais.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				jButtonAnglaisActionPerformed(event);
			}
		});

		jButtonFrench.setText("Français");
		jButtonFrench.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				jButtonFrenchActionPerformed(event);
			}
		});

		makeGlobalLayout();

		pack();
	}

	private void makeGlobalLayout() {
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
																											  initialisationPanel,
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
																	  .addComponent(rankingPanel,
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
																											  initialisationPanel,
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
																	  .addComponent(rankingPanel,
																					GroupLayout.PREFERRED_SIZE,
																					GroupLayout.DEFAULT_SIZE,
																					GroupLayout.PREFERRED_SIZE)))
									  .addContainerGap())
							   );
	}

	private void makeRankingPanel() {
		rankingPanel.setBorder(BorderFactory.createTitledBorder("Classement"));
		rankingPanel.setMaximumSize(new Dimension(635, 273));

		intuitiveRankingPanel.setBorder(BorderFactory.createTitledBorder("Classement intuitif"));
		intuitiveRankingPanel.setMaximumSize(new Dimension(184, 224));
		intuitiveRankingPanel.setLayout(new AbsoluteLayout());

		intuitiveRanking_rank1_Label.setText("1er:");
		intuitiveRankingPanel.add(intuitiveRanking_rank1_Label, new AbsoluteConstraints(26, 34, -1, -1));

		intuitiveRanking_rank2_Label.setText("2ème:");
		intuitiveRankingPanel.add(intuitiveRanking_rank2_Label, new AbsoluteConstraints(16, 60, -1, -1));

		intuitiveRanking_rank3_Label.setText("3ème:");
		intuitiveRankingPanel.add(intuitiveRanking_rank3_Label, new AbsoluteConstraints(16, 86, -1, -1));

		intuitiveRanking_rank4_Label.setText("4ème:");
		intuitiveRankingPanel.add(intuitiveRanking_rank4_Label, new AbsoluteConstraints(16, 112, -1, -1));

		intuitiveRanking_rank5_Label.setText("5ème:");
		intuitiveRankingPanel.add(intuitiveRanking_rank5_Label, new AbsoluteConstraints(16, 138, -1, -1));

		intuitiveRanking_rank6_Label.setText("6ème:");
		intuitiveRankingPanel.add(intuitiveRanking_rank6_Label, new AbsoluteConstraints(16, 164, -1, -1));

		intuitiveRankingPanel.add(intuitiveRanking_rank1_ComboBox, new AbsoluteConstraints(50, 31, 118, -1));
		intuitiveRankingPanel.add(intuitiveRanking_rank2_ComboBox, new AbsoluteConstraints(50, 57, 118, -1));
		intuitiveRankingPanel.add(intuitiveRanking_rank3_ComboBox, new AbsoluteConstraints(50, 83, 118, -1));
		intuitiveRankingPanel.add(intuitiveRanking_rank4_ComboBox, new AbsoluteConstraints(50, 109, 118, -1));
		intuitiveRankingPanel.add(intuitiveRanking_rank5_ComboBox, new AbsoluteConstraints(50, 135, 118, -1));
		intuitiveRankingPanel.add(intuitiveRanking_rank6_ComboBox, new AbsoluteConstraints(50, 161, 118, -1));


		intuitiveRankingOKButton.setText("OK");
		intuitiveRankingOKButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				intuitiveRankingOKActionPerformed(event);
			}
		});
		intuitiveRankingPanel.add(intuitiveRankingOKButton, new AbsoluteConstraints(81, 187, -1, -1));

		finalRankingPanel.setBorder(BorderFactory.createTitledBorder("Classement Final"));
		finalRankingPanel.setMaximumSize(new Dimension(203, 199));
		finalRankingPanel.setLayout(new AbsoluteLayout());
		finalRankingPanel.add(finalRanking_rank1_TextField, new AbsoluteConstraints(64, 31, 80, -1));
		finalRankingPanel.add(finalRanking_rank2_TextField, new AbsoluteConstraints(64, 57, 80, -1));
		finalRankingPanel.add(finalRanking_rank3_TextField, new AbsoluteConstraints(64, 83, 80, -1));
		finalRankingPanel.add(finalRanking_rank4_TextField, new AbsoluteConstraints(64, 109, 80, -1));
		finalRankingPanel.add(finalRanking_rank5_TextField, new AbsoluteConstraints(64, 135, 80, -1));
		finalRankingPanel.add(finalRanking_rank6_TextField, new AbsoluteConstraints(64, 161, 80, -1));

		finalRanking_rank1_Label.setText("1er:");
		finalRankingPanel.add(finalRanking_rank1_Label, new AbsoluteConstraints(26, 34, -1, -1));

		finalRanking_rank2_Label.setText("2ème:");
		finalRankingPanel.add(finalRanking_rank2_Label, new AbsoluteConstraints(16, 60, -1, -1));

		finalRanking_rank3_Label.setText("3ème:");
		finalRankingPanel.add(finalRanking_rank3_Label, new AbsoluteConstraints(16, 86, -1, -1));

		finalRanking_rank4_Label.setText("4ème:");
		finalRankingPanel.add(finalRanking_rank4_Label, new AbsoluteConstraints(16, 112, -1, -1));

		finalRanking_rank5_Label.setText("5ème:");
		finalRankingPanel.add(finalRanking_rank5_Label, new AbsoluteConstraints(16, 138, -1, -1));

		finalRanking_rank6_Label.setText("6ème:");
		finalRankingPanel.add(finalRanking_rank6_Label, new AbsoluteConstraints(16, 164, -1, -1));

		finalRankingPanel.add(finalRanking_rank1_PercentLabel, new AbsoluteConstraints(148, 31, 39, 20));
		finalRankingPanel.add(finalRanking_rank2_PercentLabel, new AbsoluteConstraints(148, 57, 39, 20));
		finalRankingPanel.add(finalRanking_rank3_PercentLabel, new AbsoluteConstraints(148, 83, 39, 20));
		finalRankingPanel.add(finalRanking_rank4_PercentLabel, new AbsoluteConstraints(148, 109, 39, 20));
		finalRankingPanel.add(finalRanking_rank5_PercentLabel, new AbsoluteConstraints(148, 135, 39, 20));
		finalRankingPanel.add(finalRanking_rank6_PercentLabel, new AbsoluteConstraints(148, 161, 39, 20));

		initialRankingPanel.setBorder(BorderFactory.createTitledBorder("Classement initial"));
		initialRankingPanel.setMaximumSize(new Dimension(204, 231));
		initialRankingPanel.setLayout(new AbsoluteLayout());
		initialRankingPanel.add(initialRanking_rank1_TextField, new AbsoluteConstraints(64, 31, 80, -1));
		initialRankingPanel.add(initialRanking_rank2_TextField, new AbsoluteConstraints(64, 57, 80, -1));
		initialRankingPanel.add(initialRanking_rank3_TextField, new AbsoluteConstraints(64, 83, 80, -1));
		initialRankingPanel.add(initialRanking_rank4_TextField, new AbsoluteConstraints(64, 109, 80, -1));
		initialRankingPanel.add(initialRanking_rank5_TextField, new AbsoluteConstraints(64, 135, 80, -1));
		initialRankingPanel.add(initialRanking_rank6_TextField, new AbsoluteConstraints(64, 161, 80, -1));

		initialRanking_rank1_Label.setText("1er:");
		initialRankingPanel.add(initialRanking_rank1_Label, new AbsoluteConstraints(26, 34, -1, -1));

		initialRanking_rank2_Label.setText("2ème:");
		initialRankingPanel.add(initialRanking_rank2_Label, new AbsoluteConstraints(16, 60, -1, -1));

		initialRanking_rank3_Label.setText("3ème:");
		initialRankingPanel.add(initialRanking_rank3_Label, new AbsoluteConstraints(16, 86, -1, -1));

		initialRanking_rank4_Label.setText("4ème:");
		initialRankingPanel.add(initialRanking_rank4_Label, new AbsoluteConstraints(16, 112, -1, -1));

		initialRanking_rank5_Label.setText("5ème:");
		initialRankingPanel.add(initialRanking_rank5_Label, new AbsoluteConstraints(16, 138, -1, -1));

		initialRanking_rank6_Label.setText("6ème:");
		initialRankingPanel.add(initialRanking_rank6_Label, new AbsoluteConstraints(16, 164, -1, -1));

		initialRankingPanel.add(initialRanking_rank1_PercentLabel, new AbsoluteConstraints(148, 31, 40, 20));
		initialRankingPanel.add(initialRanking_rank2_PercentLabel, new AbsoluteConstraints(148, 57, 40, 20));
		initialRankingPanel.add(initialRanking_rank3_PercentLabel, new AbsoluteConstraints(148, 83, 40, 20));
		initialRankingPanel.add(initialRanking_rank4_PercentLabel, new AbsoluteConstraints(148, 109, 40, 20));
		initialRankingPanel.add(initialRanking_rank5_PercentLabel, new AbsoluteConstraints(148, 135, 40, 20));
		initialRankingPanel.add(initialRanking_rank6_PercentLabel, new AbsoluteConstraints(148, 161, 40, 20));

		final GroupLayout rankingPanelLayout = new GroupLayout(rankingPanel);
		rankingPanel.setLayout(rankingPanelLayout);
		rankingPanelLayout.setHorizontalGroup(
				rankingPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								  .addGroup(rankingPanelLayout.createSequentialGroup()
															  .addGap(14, 14, 14)
															  .addComponent(intuitiveRankingPanel,
																			GroupLayout.PREFERRED_SIZE,
																			192,
																			GroupLayout.PREFERRED_SIZE)
															  .addGap(18, 18, 18)
															  .addComponent(initialRankingPanel,
																			GroupLayout.PREFERRED_SIZE,
																			200,
																			GroupLayout.PREFERRED_SIZE)
															  .addComponent(finalRankingPanel,
																			GroupLayout.PREFERRED_SIZE,
																			GroupLayout.DEFAULT_SIZE,
																			GroupLayout.PREFERRED_SIZE))
											 );
		rankingPanelLayout.setVerticalGroup(
				rankingPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								  .addGroup(rankingPanelLayout.createSequentialGroup().addGroup(
										  rankingPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING,
																				 false)
															.addGroup(
																	rankingPanelLayout.createSequentialGroup()
																					  .addContainerGap()
																					  .addComponent(
																							  intuitiveRankingPanel,
																							  GroupLayout.DEFAULT_SIZE,
																							  GroupLayout.DEFAULT_SIZE,
																							  Short.MAX_VALUE))
															.addGroup(
																	GroupLayout.Alignment.LEADING,
																	rankingPanelLayout.createSequentialGroup()
																					  .addGap(10,
																							  10,
																							  10)
																					  .addComponent(
																							  initialRankingPanel,
																							  GroupLayout.PREFERRED_SIZE,
																							  230,
																							  GroupLayout.PREFERRED_SIZE))
															.addGroup(
																	GroupLayout.Alignment.LEADING,
																	rankingPanelLayout.createSequentialGroup()
																					  .addGap(10,
																							  10,
																							  10)
																					  .addComponent(
																							  finalRankingPanel,
																							  GroupLayout.PREFERRED_SIZE,
																							  230,
																							  GroupLayout.PREFERRED_SIZE)))
															  .addContainerGap(14, Short.MAX_VALUE))
										   );
	}

	private void makeInitialisationPanel() {
		initialisationPanel.setBorder(BorderFactory.createTitledBorder("Initialisation"));
		initialisationPanel.setLayout(new AbsoluteLayout());

		consistencyMakerTypeButtonGroup.add(consistencyMakerTypeRandomRadioButton);
		consistencyMakerTypeRandomRadioButton.setText("A");
		initialisationPanel.add(consistencyMakerTypeRandomRadioButton, new AbsoluteConstraints(14, 27, -1, -1));

		consistencyMakerTypeButtonGroup.add(consistencyMakerTypeSaatyRadioButton);
		consistencyMakerTypeSaatyRadioButton.setText("S");
		initialisationPanel.add(consistencyMakerTypeSaatyRadioButton, new AbsoluteConstraints(49, 27, -1, -1));

		datasetButtonGroup.add(datasetP1RadioButton);
		datasetP1RadioButton.setText("P1");
		initialisationPanel.add(datasetP1RadioButton, new AbsoluteConstraints(98, 27, -1, -1));

		datasetButtonGroup.add(datasetP2RadioButton);
		datasetP2RadioButton.setText("P2");
		initialisationPanel.add(datasetP2RadioButton, new AbsoluteConstraints(137, 27, -1, -1));

		matrixSizeLabel.setText("Taille matrice:");
		initialisationPanel.add(matrixSizeLabel, new AbsoluteConstraints(10, 70, -1, -1));

		matrixSizeTextField.setText("6");
		initialisationPanel.add(matrixSizeTextField, new AbsoluteConstraints(100, 70, 43, -1));

		initialisationPanel.add(saveFilePathTextField, new AbsoluteConstraints(290, 70, 299, -1));

		saveFilePathLabel.setText("Chemin de sauvegarde:");
		initialisationPanel.add(saveFilePathLabel, new AbsoluteConstraints(156, 72, -1, -1));

		saveFilePathOkButton.setText("Ok");
		saveFilePathOkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				saveFilePathOkButtonActionPerformed(event);
			}
		});
		initialisationPanel.add(saveFilePathOkButton, new AbsoluteConstraints(617, 97, -1, -1));

		saveFilePathExploreButton.setText("Parcourir");
		saveFilePathExploreButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				saveFilePathExploreButtonActionPerformed(event);
			}
		});
		initialisationPanel.add(saveFilePathExploreButton, new AbsoluteConstraints(590, 70, -1, -1));
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
					while (!ahpData.isInSaatyScale(newValue)) {
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

	/*
	 * Cette méthode permet de trouver les coefficients de saaty à changer
	 */
	public MatrixValue readSaatysRanking(Collection<MatrixValue> sortedMatrixValues,
										 MyMatrix myPreferenceMatrix,
										 String file)
			throws
			IOException {

		CharSequenceAppender csa = new CharSequenceAppender(file);
		MatrixValue matrixValue = new MatrixValue();

		Iterator<MatrixValue> valueIterator = sortedMatrixValues.iterator();
		MatrixValue matrixValueToPrint = new MatrixValue();
		int isValueChosen = 0;
		boolean isFound = false;

		MonCellRenderer monCell = new MonCellRenderer(0, 0);
		jTableMatrice.setDefaultRenderer(Object.class, monCell);

		while (isValueChosen == 0) {
			matrixValue = valueIterator.next();
			matrixValueToPrint.setRow(matrixValue.getRow());
			matrixValueToPrint.setColumn(matrixValue.getColumn());
			matrixValueToPrint.setValue(myPreferenceMatrix.get(matrixValueToPrint.getRow(),
															   matrixValueToPrint.getColumn()));
			//on redessine la fenetre
			this.repaint();

			monCell.setCol(matrixValueToPrint.getColumn() + 1);
			monCell.setRow(matrixValueToPrint.getRow() + 1);

			//on ouvre un fenetre de dialogue pour afficher la valeur à modifier
			JOptionPane jop = new JOptionPane();
			int option = JOptionPane.showConfirmDialog(
					null,
					"Souhaitez-vous modifier la valeur "
					+ matrixValueToPrint.getValue()
					+ " ( "
					+ (matrixValueToPrint.getRow() + 1)
					+ " , "
					+ (matrixValueToPrint.getColumn() + 1)
					+ " )"
					+ " ?",
					"Modification des valeurs",
					JOptionPane.YES_NO_CANCEL_OPTION);

			//si on clique sur ok on sort du while
			if (option == JOptionPane.OK_OPTION) {
				isValueChosen = 1;
			} else if (option == JOptionPane.CANCEL_OPTION) {
				isValueChosen = 1;
				finSimulation = true;
			} else if (!valueIterator.hasNext()) {
				System.out.println("Retour en haut du classement");
				valueIterator = sortedMatrixValues.iterator();
			}
		}

		// si l'expert n'a pas mit à la simulation
		if (finSimulation == true) {
			csa.close();
		} else {
			/*parcours de la liste pour l'écriture dans le fichier*/
			valueIterator = sortedMatrixValues.iterator();

			while ((valueIterator.hasNext()) && (!isFound)) {

				final MatrixValue tempMatrixValue = valueIterator.next();
				csa.appendLineFeed();

				/*écriture du best fit associé à la valeur proposée*/
				//copie de la matrice initiale
				final MyMatrix tempMatrix = MyMatrix.copyMyMatrix(myPreferenceMatrix);

				//calcul du vecteur propre associé à tempMatrix
				PriorityVector tempVector = PriorityVector.build(tempMatrix);
				//calcul du best fit
				double BestFit = SaatyTools.calculateBestFit(tempMatrix,
															 tempVector,
															 tempMatrixValue.getRow(),
															 tempMatrixValue.getColumn());
				//écriture du best fit
				String tempString = "" + BestFit;
				csa.append(tempString);
				csa.appendCommaSeparator();

				/*écriture des indices de la valeur proposée par Saaty dans le fichier*/
				tempString = "" + (tempMatrixValue.getRow() + 1);
				csa.append(tempString);
				csa.appendCommaSeparator();
				tempString = "" + (tempMatrixValue.getColumn() + 1);
				csa.append(tempString);
				csa.appendCommaSeparator();

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
				final MyMatrix tempVector2 = PriorityVector.build(tempMatrix);

				//calcul et écriture de la cohérence
				ConsistencyChecker consistencyChecker = new ConsistencyChecker();
//				tempBoolean = consistencyChecker.isConsistent(tempMatrix, tempVector2);
				ahpData.saatyConsistency = consistencyChecker.getConsistencyRatio();
				tempString = "" + consistencyChecker.getConsistencyRatio();
				csa.append(tempString);
				csa.appendCommaSeparator();

				if (matrixValue.equals(tempMatrixValue)) {
					isFound = true;
				}
			}
			csa.close();
		}
		return matrixValue;
	}

	private void saveFilePathOkButtonActionPerformed(ActionEvent event) {
		//on crée et lance le thread pour afficher un compteur

		monHeure = new Meter(jLabelTime, this);
		monHeure.setCommencerAZero(false);
		monHeure.start();

		ahpData.myMatrix = new MyMatrix();
		ahpData.matrixTableModel = new MyMatrixTableModel();
		//On crée la matrice vide
		ahpData.myMatrix = creerMatrice(Integer.parseInt(matrixSizeTextField.getText()), 0);
		//maTable = new MyMatrixTable();

		//on teste pour savoir quelle est le pb choisi
		if (datasetP1RadioButton.isSelected() == true) {
			//on crée le fichier historique à partir de "file"
			fileHistorique = file + "histI.csv";
			file += "I.csv";
			try {
				csa = new CharSequenceAppender(file);
				csa.append("Probleme 1");
				csa.appendCommaSeparator();
				//On teste saaty ou aléatoire
				if (consistencyMakerTypeSaatyRadioButton.isSelected() == true) {
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
				if (consistencyMakerTypeSaatyRadioButton.isSelected() == true) {
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
		ahpData.matrixTableModel.setMatrix(ahpData.myMatrix,
										   SampleMatrixHeaders.getColumnHeader(datasetP1RadioButton.isSelected(),
																			   modeAnglais));
		//on ajoute directement matrixTableModel à jTable1 bugg??????
		jTableMatrice.setModel(ahpData.matrixTableModel);
		//maTable.setModel(matrixTableModel);
		jLabel6.setText(
				"Veuillez remplir la partie supérieure (cases blanches) de la matrice avec les coefficients de SAATY:");
		//on affiche la matrice
		//showMatrixTable((MyMatrixTable) jTable1,myMatrix);
		//on rempli les jComboBox par le nom des column pour le 1er classement
		String[] mesColonnes = new String[ahpData.myMatrix.getColumnDimension()];
		mesColonnes = SampleMatrixHeaders.getColumnHeader(datasetP1RadioButton.isSelected(), modeAnglais);
		remplirJComboBox(intuitiveRanking_rank1_ComboBox, mesColonnes);
		remplirJComboBox(intuitiveRanking_rank2_ComboBox, mesColonnes);
		remplirJComboBox(intuitiveRanking_rank3_ComboBox, mesColonnes);
		remplirJComboBox(intuitiveRanking_rank4_ComboBox, mesColonnes);
		remplirJComboBox(intuitiveRanking_rank5_ComboBox, mesColonnes);
		remplirJComboBox(intuitiveRanking_rank6_ComboBox, mesColonnes);
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
		PriorityVector priorityVector = PriorityVector.build(ahpData.myMatrix);
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
		csa.append(ahpData.myMatrix);
		csa.appendLineFeed();
		csa.append("Vecteur de priorite initial");
		csa.appendLineFeed();
		csa.append(priorityVector);
		csa.appendLineFeed();

		//on crée le fichier et on l'initialise avec la 1ère matrice
		this.ecrirefichierHistorique(ahpData.myMatrix, 0, 0, 0, 0);

		//on va afficher le classement des critères de la 1ere matrice
		String[] classement = this.classerCriteres(priorityVector);
		initialRanking_rank1_TextField.setText(classement[5]);
		initialRanking_rank2_TextField.setText(classement[4]);
		initialRanking_rank3_TextField.setText(classement[3]);
		initialRanking_rank4_TextField.setText(classement[2]);
		initialRanking_rank5_TextField.setText(classement[1]);
		initialRanking_rank6_TextField.setText(classement[0]);
		double[] classementVal = classerPourcentageCritères(priorityVector);
		initialRanking_rank1_PercentLabel.setText(classementVal[5] + "%");
		initialRanking_rank2_PercentLabel.setText(classementVal[4] + "%");
		initialRanking_rank3_PercentLabel.setText(classementVal[3] + "%");
		initialRanking_rank4_PercentLabel.setText(classementVal[2] + "%");
		initialRanking_rank5_PercentLabel.setText(classementVal[1] + "%");
		initialRanking_rank6_PercentLabel.setText(classementVal[0] + "%");

		//Writing of the CR
		consistencyChecker.isConsistent(ahpData.myMatrix, priorityVector);
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
		if (consistencyMakerTypeSaatyRadioButton.isSelected() == true) {
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
		boolean myConsistent = consistencyChecker.isConsistent(ahpData.myMatrix, priorityVector);

		while (!myConsistent) {

			//On continue si l'expert n'a pas choisi de mettre fin à la simulation
			if (finSimulation == false) {
				//incrémentation du compteur du nombre d'itération
				iterationCounter++;

				//on teste quelles méthode(aléaloire, saaty)
				if (consistencyMakerTypeSaatyRadioButton.isSelected() == true) {
					/*Calcul matrice epsilon*/
					epsilon = SaatyTools.calculateEpsilonMatrix(ahpData.myMatrix, priorityVector);
					/*Recherche de la valeur à modifier*/
					collectionOfSortedMatrixValues = SaatyTools.getRank(ahpData.myMatrix, priorityVector, epsilon);
					try {
						matrixValue = readSaatysRanking(collectionOfSortedMatrixValues, ahpData.myMatrix, file);
					} catch (IOException ex) {
						Logger.getLogger(InterfaceAHP.class.getName()).log(Level.SEVERE, null, ex);
					}
				} else {
					collectionOfSortedMatrixValues = RandomTools.getRank(ahpData.myMatrix);
					matrixValue = getValueToModifiyByRanking(collectionOfSortedMatrixValues);
					try {
						/*Writing of Saaty's propositions and of random ranking*/
						RandomTools.writeRandomAndSaatyPropositions(
								ahpData.myMatrix,
								collectionOfSortedMatrixValues,
								matrixValue,
								priorityVector,
								file);
					} catch (IOException ex) {
						Logger.getLogger(InterfaceAHP.class.getName()).log(Level.SEVERE, null, ex);
					}

				}
				//si l'expert n'a pas mis fin è la simulation et veut changer le coeff
				if (finSimulation == false) {
					//on stocke les différentes valeur pour le fichier historique
					oldValue = ahpData.myMatrix.get(matrixValue.getRow(), matrixValue.getColumn());
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
					tempString = "" + ahpData.myMatrix.get(matrixValue.getRow(), matrixValue.getColumn());
					csa.append(tempString);
					csa.appendCommaSeparator();
					//Si la valeur modifier n'appartient pas à l'échelle de saaty
					//on demande a l'expert de la modifier
					while (!ahpData.isInSaatyScale(newValue)) {
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
					if (consistencyMakerTypeSaatyRadioButton.isSelected() == true) {
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
					ahpData.myMatrix.setMatrixValue(matrixValue);

					//Valeur réciproquement modifiée
					int tempI = matrixValue.getRow();
					int tempJ = matrixValue.getColumn();
					matrixValue.setValue(1 / newValue);
					matrixValue.setRow(tempJ);
					matrixValue.setColumn(tempI);
					ahpData.myMatrix.setMatrixValue(matrixValue);

					//Affichage nouvelle matrice
					//	myMatrix.print(5, 5);

					//Affichage nouvelle matrice
					ahpData.matrixTableModel.setMatrix(ahpData.myMatrix,
													   SampleMatrixHeaders.getColumnHeader(datasetP1RadioButton.isSelected(),
																						   modeAnglais));
					jTableMatrice.setModel(ahpData.matrixTableModel);

					//Réactualisation du vecteur de priorité associé à la nouvelle matrice
					priorityVector = PriorityVector.build(ahpData.myMatrix);
					//		priorityVector.print(5, 5);

					//Ecriture du nouveau CR
					consistencyChecker.isConsistent(ahpData.myMatrix, priorityVector);
					tempString = "" + consistencyChecker.getConsistencyRatio();
					csa.append(tempString);
					csa.appendCommaSeparator();
					//uniquement si on fai la méthode saaty
					if (consistencyMakerTypeSaatyRadioButton.isSelected() == true) {
						tempString = String.valueOf(ahpData.saatyConsistency - consistencyChecker.getConsistencyRatio());
						csa.append(tempString);
					}
					csa.close();
					//on va ecrire dans le fichier historique
					this.ecrirefichierHistorique(ahpData.myMatrix, oldValue, coordRowVal, coordColVal, newValue);
					//on récupére la nouvelle valeur
					myConsistent = consistencyChecker.isConsistent(ahpData.myMatrix, priorityVector);
				}
				//si il à mi fin on sort de la boucle
				else if (finSimulation == true) {
					//on arrete le thread
					monHeure.stop();
					myConsistent = true;
				}
				//on affiche un cpt pour les observations avec un coeff de 7.5
				jLabelCompteur.setText(String.valueOf(iterationCounter * 7.5));
				jTextFieldCR.setText(String.valueOf(consistencyChecker.getConsistencyRatio()));
			}
		}
		if (finSimulation == true) {
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
		csa.append(ahpData.myMatrix);
		csa.appendLineFeed();
		csa.append("Vecteur de priorite final");
		csa.appendLineFeed();
		csa.append(priorityVector);
		csa.appendLineFeed();

		//on va afficher le classement final des critères
		String[] classementF = this.classerCriteres(priorityVector);
		finalRanking_rank1_TextField.setText(classementF[5]);
		finalRanking_rank2_TextField.setText(classementF[4]);
		finalRanking_rank3_TextField.setText(classementF[3]);
		finalRanking_rank4_TextField.setText(classementF[2]);
		finalRanking_rank5_TextField.setText(classementF[1]);
		finalRanking_rank6_TextField.setText(classementF[0]);
		double[] classementValF = classerPourcentageCritères(priorityVector);
		finalRanking_rank1_PercentLabel.setText(classementValF[5] + "%");
		finalRanking_rank2_PercentLabel.setText(classementValF[4] + "%");
		finalRanking_rank3_PercentLabel.setText(classementValF[3] + "%");
		finalRanking_rank4_PercentLabel.setText(classementValF[2] + "%");
		finalRanking_rank5_PercentLabel.setText(classementValF[1] + "%");
		finalRanking_rank6_PercentLabel.setText(classementValF[0] + "%");

		//Ecriture du CR
		consistencyChecker.isConsistent(ahpData.myMatrix, priorityVector);
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

	private void jButtonValiderMatriceActionPerformed(ActionEvent event) {
		// TODO add your handling code here:

		ahpData.myMatrix = new MyMatrix();
		ahpData.matrixTableModel = new MyMatrixTableModel();
		//On crée la matrice rempli
		ahpData.myMatrix = creerMatrice(Integer.parseInt(matrixSizeTextField.getText()), 1);
		//maTable = new MyMatrixTable();
		/*Interface graphique*/
		ahpData.matrixTableModel.setMatrix(ahpData.myMatrix,
										   SampleMatrixHeaders.getColumnHeader(datasetP1RadioButton.isSelected(),
																			   modeAnglais));
		//on ajoute directement matrixTableModel à jTable1 bugg??????
		jTableMatrice.setModel(ahpData.matrixTableModel);
		//maTable.setModel(matrixTableModel);
		//on va cree le fichier .csv et le modifier et on récupérer le CR et afficher
		jTextFieldCR.setText(String.valueOf(creerFichierCsv()));
		//on affiche la matrice
		//showMatrixTable( (MyMatrixTable) jTable1,myMatrix);
		//on ferme le flux
		csa.close();
		//on remet à 0 l'attribut
		finSimulation = false;
		//on arrete le thread si c pas déja fait
		if (finSimulation == false) {
			monHeure.stop();
		}
		//on réaffiche le classement intuitif
		intuitiveRankingPanel.setVisible(true);
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
			} else if (option == JOptionPane.CANCEL_OPTION) {
				isValueChosen = 1;
				finSimulation = true;
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
		if (datasetP1RadioButton.isSelected() == true) {

			String[] classementString = new String[v.getRowDimension()];
			double[] monClassement = new double[v.getRowDimension()];
			for (int i = 0; i < v.getRowDimension(); i++) {
				monClassement[i] = v.get(i, 0);
			}
			//on classe par ordre croissant
			Arrays.sort(monClassement);

			for (int i = 0; i < v.getRowDimension(); i++) {
				String columnNames[] = SampleMatrixHeaders.getColumnHeader(datasetP1RadioButton.isSelected(),
																		   modeAnglais);
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
			String columnNames[] = SampleMatrixHeaders.getColumnHeader(datasetP1RadioButton.isSelected(), modeAnglais);
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

	private void jButtonNewSimulActionPerformed(ActionEvent event) {
		initComponents();
		this.dispose();
		new InterfaceAHP().setVisible(true);
	}

	private void saveFilePathExploreButtonActionPerformed(ActionEvent event) {
		JFileChooser jfc = new JFileChooser();
		jfc.setVisible(true);
		int choix = jfc.showSaveDialog(this);
		if (JFileChooser.APPROVE_OPTION == choix) {
			//On récup le chemin
			file = jfc.getSelectedFile().getAbsolutePath();
			//On affiche le chemin de sauvegarde
			saveFilePathTextField.setText(file);
		}
	}

	private void intuitiveRankingOKActionPerformed(ActionEvent event) {
		intuitiveRankingPanel.setVisible(false);
	}

	private void jButtonAnglaisActionPerformed(ActionEvent event) {
		// TODO add your handling code here:
		modeAnglais = true;
		jButtonFrench.setText("French");
		//Panel init
		jButtonNewSimul.setText("New Simulation");
		initialisationPanel.setBorder(BorderFactory.createTitledBorder("Initialization"));
		matrixSizeLabel.setText("Matrix size:");
		saveFilePathLabel.setText("Saving in:");
		saveFilePathExploreButton.setText("Browse");
		//Tables échelle saaty
		saatyScaleTable.setValueAt("1: equally important ", 0, 0);
		saatyScaleTable.setValueAt("3: slightly larger ", 1, 0);
		saatyScaleTable.setValueAt("5: largest ", 2, 0);
		saatyScaleTable.setValueAt("7: much larger ", 3, 0);
		saatyScaleTable.setValueAt("9: absolutely more important ", 4, 0);
		saatyScaleTable.setValueAt("2,4,6: intermediate values ", 5, 0);
		//panel classement
		intuitiveRankingPanel.setBorder(BorderFactory.createTitledBorder("Intuitive classement"));
		initialRankingPanel.setBorder(BorderFactory.createTitledBorder("Initial classement"));
		finalRankingPanel.setBorder(BorderFactory.createTitledBorder("Final classement"));
		//bouton valider matrice
		jButtonValiderMatrice.setText("Validate Matrix");
		//Jlabel dans panel classement
		intuitiveRanking_rank1_Label.setText("1st:");
		initialRanking_rank1_Label.setText("1st:");
		finalRanking_rank1_Label.setText("1st:");
		intuitiveRanking_rank2_Label.setText("2nd:");
		initialRanking_rank2_Label.setText("2nd:");
		finalRanking_rank2_Label.setText("2nd:");
		intuitiveRanking_rank3_Label.setText("3th:");
		initialRanking_rank3_Label.setText("3th:");
		finalRanking_rank3_Label.setText("3th:");
		intuitiveRanking_rank4_Label.setText("4th:");
		initialRanking_rank4_Label.setText("4th:");
		finalRanking_rank4_Label.setText("4th:");
		intuitiveRanking_rank5_Label.setText("5th:");
		initialRanking_rank5_Label.setText("5th:");
		finalRanking_rank5_Label.setText("5th:");
		intuitiveRanking_rank6_Label.setText("6th:");
		initialRanking_rank6_Label.setText("6th:");
		finalRanking_rank6_Label.setText("6th:");
		finalRanking_rank3_Label.setText("3th:");
		finalRanking_rank3_Label.setText("3th:");
	}

	private void jButtonFrenchActionPerformed(ActionEvent event) {
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
	private ButtonGroup       consistencyMakerTypeButtonGroup       = new ButtonGroup();
	private ButtonGroup       datasetButtonGroup                    = new ButtonGroup();
	private JButton           jButtonAnglais                        = new JButton();
	private JButton           jButtonFrench                         = new JButton();
	private JButton           jButtonNewSimul                       = new JButton();
	private JButton           saveFilePathOkButton                  = new JButton();
	private JButton           intuitiveRankingOKButton              = new JButton();
	private JButton           saveFilePathExploreButton             = new JButton();
	private JButton           jButtonValiderMatrice                 = new JButton();
	private JComboBox<String> intuitiveRanking_rank1_ComboBox       = new JComboBox();
	private JComboBox<String> intuitiveRanking_rank2_ComboBox       = new JComboBox();
	private JComboBox<String> intuitiveRanking_rank3_ComboBox       = new JComboBox();
	private JComboBox<String> intuitiveRanking_rank4_ComboBox       = new JComboBox();
	private JComboBox<String> intuitiveRanking_rank5_ComboBox       = new JComboBox();
	private JComboBox<String> intuitiveRanking_rank6_ComboBox       = new JComboBox();
	private JLabel            matrixSizeLabel                       = new JLabel();
	private JLabel            intuitiveRanking_rank5_Label          = new JLabel();
	private JLabel            intuitiveRanking_rank6_Label          = new JLabel();
	private JLabel            initialRanking_rank1_Label            = new JLabel();
	private JLabel            initialRanking_rank2_Label            = new JLabel();
	private JLabel            initialRanking_rank3_Label            = new JLabel();
	private JLabel            initialRanking_rank4_Label            = new JLabel();
	private JLabel            initialRanking_rank5_Label            = new JLabel();
	private JLabel            initialRanking_rank6_Label            = new JLabel();
	private JLabel            finalRanking_rank1_Label              = new JLabel();
	private JLabel            finalRanking_rank2_Label              = new JLabel();
	private JLabel            saveFilePathLabel                     = new JLabel();
	private JLabel            finalRanking_rank3_Label              = new JLabel();
	private JLabel            finalRanking_rank4_Label              = new JLabel();
	private JLabel            finalRanking_rank5_Label              = new JLabel();
	private JLabel            finalRanking_rank6_Label              = new JLabel();
	private JLabel            jLabel3                               = new JLabel();
	private JLabel            jLabel4                               = new JLabel();
	private JLabel            intuitiveRanking_rank1_Label          = new JLabel();
	private JLabel            jLabel6                               = new JLabel();
	private JLabel            intuitiveRanking_rank2_Label          = new JLabel();
	private JLabel            intuitiveRanking_rank3_Label          = new JLabel();
	private JLabel            intuitiveRanking_rank4_Label          = new JLabel();
	private JLabel            finalRanking_rank1_PercentLabel       = new JLabel();
	private JLabel            finalRanking_rank2_PercentLabel       = new JLabel();
	private JLabel            finalRanking_rank3_PercentLabel       = new JLabel();
	private JLabel            finalRanking_rank4_PercentLabel       = new JLabel();
	private JLabel            finalRanking_rank5_PercentLabel       = new JLabel();
	private JLabel            finalRanking_rank6_PercentLabel       = new JLabel();
	private JLabel            initialRanking_rank1_PercentLabel     = new JLabel();
	private JLabel            initialRanking_rank2_PercentLabel     = new JLabel();
	private JLabel            initialRanking_rank3_PercentLabel     = new JLabel();
	private JLabel            initialRanking_rank4_PercentLabel     = new JLabel();
	private JLabel            initialRanking_rank5_PercentLabel     = new JLabel();
	private JLabel            initialRanking_rank6_PercentLabel     = new JLabel();
	private JLabel            jLabelCompteur                        = new JLabel();
	private JLabel            jLabelTime                            = new JLabel();
	private JPanel            intuitiveRankingPanel                 = new JPanel();
	private JPanel            finalRankingPanel                     = new JPanel();
	private JPanel            initialRankingPanel                   = new JPanel();
	private JPanel            rankingPanel                          = new JPanel();
	private JPanel            initialisationPanel                   = new JPanel();
	private JRadioButton      consistencyMakerTypeRandomRadioButton = new JRadioButton();
	private JRadioButton      datasetP1RadioButton                  = new JRadioButton();
	private JRadioButton      datasetP2RadioButton                  = new JRadioButton();
	private JRadioButton      consistencyMakerTypeSaatyRadioButton  = new JRadioButton();
	private JScrollPane       jScrollPane1                          = new JScrollPane();
	private JScrollPane       jScrollPane3                          = new JScrollPane();
	private JScrollPane       jScrollPane4                          = new JScrollPane();
	private JTable            saatyScaleTable                       = new JTable();
	private JTable            jTable4                               = new JTable();
	private JTable            jTableMatrice                         = new JTable();
	private JTextField        jTextFieldCR                          = new JTextField();
	private JTextField        saveFilePathTextField                 = new JTextField();
	private JTextField        finalRanking_rank1_TextField          = new JTextField();
	private JTextField        finalRanking_rank2_TextField          = new JTextField();
	private JTextField        finalRanking_rank3_TextField          = new JTextField();
	private JTextField        finalRanking_rank4_TextField          = new JTextField();
	private JTextField        finalRanking_rank5_TextField          = new JTextField();
	private JTextField        finalRanking_rank6_TextField          = new JTextField();
	private JTextField        initialRanking_rank1_TextField        = new JTextField();
	private JTextField        initialRanking_rank2_TextField        = new JTextField();
	private JTextField        initialRanking_rank3_TextField        = new JTextField();
	private JTextField        initialRanking_rank4_TextField        = new JTextField();
	private JTextField        initialRanking_rank5_TextField        = new JTextField();
	private JTextField        initialRanking_rank6_TextField        = new JTextField();
	private JTextField        matrixSizeTextField                   = new JTextField();
	// End of variables declaration
}

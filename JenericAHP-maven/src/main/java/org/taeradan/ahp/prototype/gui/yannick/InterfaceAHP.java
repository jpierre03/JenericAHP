package org.taeradan.ahp.prototype.gui.yannick;

import org.nfunk.jep.JEP;
import org.taeradan.ahp.ConsistencyChecker;
import org.taeradan.ahp.PriorityVector;
import org.taeradan.ahp.gui.component.MonCellRenderer;
import org.taeradan.ahp.matrix.MatrixValue;
import org.taeradan.ahp.matrix.MyMatrix;
import org.taeradan.ahp.prototype.ConsistencyMaker.RandomTools;
import org.taeradan.ahp.prototype.ConsistencyMaker.SaatyTools;
import org.taeradan.ahp.prototype.ConsistencyMaker.csv_output_marianne.CharSequenceAppender;
import org.taeradan.ahp.prototype.SampleMatrixHeaders;
import org.taeradan.ahp.prototype.gui.matrix.MyMatrixTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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

	private final AHP_Data ahpData = new AHP_Data();
	private final JEP monJep = new JEP();
	// Variables declaration
	private final ButtonGroup consistencyMakerTypeButtonGroup = new ButtonGroup();
	private final ButtonGroup datasetButtonGroup = new ButtonGroup();
	private final JButton setEnglishLangButton = new JButton();
	private final JButton setFrenchLangButton = new JButton();
	private final JButton newSimulationButton = new JButton();
	private final JButton saveFilePathOkButton = new JButton();
	private final JButton intuitiveRankingOKButton = new JButton();
	private final JButton saveFilePathExploreButton = new JButton();
	private final JButton validateMatrixButton = new JButton();
	private final JLabel matrixSizeLabel = new JLabel();
	private final JLabel saveFilePathLabel = new JLabel();
	private final JLabel crLabel = new JLabel();
	private final JLabel finishLabel = new JLabel();
	private final JLabel matrixCommentLabel = new JLabel();
	private final JLabel counterLabel = new JLabel();
	private final JLabel timeLabel = new JLabel();
	private final JPanel rankingPanel = new JPanel();
	private final JPanel initialisationPanel = new JPanel();
	private final JRadioButton consistencyMakerTypeRandomRadioButton = new JRadioButton();
	private final JRadioButton datasetP1RadioButton = new JRadioButton();
	private final JRadioButton datasetP2RadioButton = new JRadioButton();
	private final JRadioButton consistencyMakerTypeSaatyRadioButton = new JRadioButton();
	private final JScrollPane jScrollPane1 = new JScrollPane();
	private final JScrollPane saatyScaleScrollPane = new JScrollPane();
	private final JScrollPane reverseSaatyScaleScrollPane = new JScrollPane();
	private final JTable saatyScaleTable = new JTable();
	private final JTable reverseSaatyScaleTable = new JTable();
	private final JTable matrixTable = new JTable();
	private final JTextField crTextField = new JTextField();
	private final JTextField saveFilePathTextField = new JTextField();
	private final RankingGui initialRanking = new RankingGui();
	private final RankingGui intuitiveRanking = new RankingGui();
	private final RankingGui finalRanking = new RankingGui();
	private final JTextField matrixSizeTextField = new JTextField();
	private boolean isEndSimulation = false;
	private boolean modeAnglais = false;
	private CharSequenceAppender csa;
	private String file;
	private String fistoryFile;
	private Meter monHeure;

	public InterfaceAHP() {
		definePreferredFrameSize();

		initComponents();
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

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new InterfaceAHP().setVisible(true);
			}
		});
	}

	private void definePreferredFrameSize() {
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		screenSize.setSize(screenSize.getWidth(), screenSize.getHeight() - 100);
		this.setPreferredSize(screenSize);
		this.setResizable(true);
	}

	private void initComponents() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(true);

		validateMatrixButton.setText("Valider Matrice");
		validateMatrixButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				validateMatrixActionPerformed();
			}
		});

		matrixTable.setModel(new DefaultTableModel(
			new Object[][]{{}, {}, {}, {}},
			new String[]{}
		));
		jScrollPane1.setViewportView(matrixTable);

		crLabel.setText("CR:");

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
		saatyScaleScrollPane.setViewportView(saatyScaleTable);

		reverseSaatyScaleTable.setModel(new DefaultTableModel(
			new Object[][]{
				{"1/1: 1"},
				{"1/2: 0.5"},
				{"1/3: 0.3333"},
				{"1/4: 0.25"},
				{"1/5: 0.2"},
				{"1/6: 0.1667"},
				{"1/7: 0.1429"},
				{"1/8: 0.125"},
				{"1/9: 0.1111"}
			},
			new String[]{"Reverse scale of Saaty"}
		));
		reverseSaatyScaleScrollPane.setViewportView(reverseSaatyScaleTable);

		newSimulationButton.setText("Nouvelle Simulation");
		newSimulationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				newSimulationButtonActionPerformed();
			}
		});

		setEnglishLangButton.setText("English");
		setEnglishLangButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				setEnglishLangButtonActionPerformed();
			}
		});

		setFrenchLangButton.setText("Français");
		setFrenchLangButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				setFrenchLangButtonActionPerformed();
			}
		});

		makeGlobalLayout();

		pack();
	}

	private void makeGlobalLayout() {

		final JPanel crPanel = new JPanel(new BorderLayout());
		crPanel.add(crLabel, BorderLayout.WEST);
		crPanel.add(crTextField, BorderLayout.CENTER);

		final JPanel p = new JPanel(new GridLayout(4, 3, 20, 20));

		p.add(timeLabel);
		p.add(matrixCommentLabel);
		p.add(initialisationPanel);
		p.add(crPanel);
		p.add(counterLabel);

		p.add(saatyScaleScrollPane);
		p.add(reverseSaatyScaleScrollPane);
		p.add(validateMatrixButton);
		p.add(jScrollPane1);
		p.add(rankingPanel);

		final JPanel north = new JPanel(new GridLayout(1, 2, 20, 20));
		north.add(setEnglishLangButton);
		north.add(newSimulationButton);
		north.add(setFrenchLangButton);

		final JPanel south = new JPanel(new GridLayout(1, 1, 20, 20));
		south.add(finishLabel);


		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(north, BorderLayout.NORTH);
		getContentPane().add(p, BorderLayout.CENTER);
		getContentPane().add(south, BorderLayout.SOUTH);
	}

	private void makeRankingPanel() {
		rankingPanel.setBorder(BorderFactory.createTitledBorder("Classement"));

		rankingPanel.setLayout(new GridLayout(1, 3));

		{
			final RankingGui gui = intuitiveRanking;
			final String rankingTitle = "Classement intuitif";
			{
				gui.panel.setBorder(BorderFactory.createTitledBorder(rankingTitle));
				gui.panel.setLayout(new GridLayout(6 + 1, 2));

				gui.panel.add(gui.rank1_Label);
				gui.panel.add(gui.rank1_ComboBox);

				gui.panel.add(gui.rank2_Label);
				gui.panel.add(gui.rank2_ComboBox);

				gui.panel.add(gui.rank3_Label);
				gui.panel.add(gui.rank3_ComboBox);

				gui.panel.add(gui.rank4_Label);
				gui.panel.add(gui.rank4_ComboBox);

				gui.panel.add(gui.rank5_Label);
				gui.panel.add(gui.rank5_ComboBox);

				gui.panel.add(gui.rank6_Label);
				gui.panel.add(gui.rank6_ComboBox);

				gui.setFrench();

				intuitiveRankingOKButton.setText("OK");
				intuitiveRankingOKButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent event) {
						intuitiveRankingOKActionPerformed();
					}
				});
				gui.panel.add(intuitiveRankingOKButton);
			}
			rankingPanel.add(gui.panel);
		}

		{
			final RankingGui gui = finalRanking;
			final String rankingTitle = "Classement Final";
			{
				buildRankingArea(gui, rankingTitle);
			}

			rankingPanel.add(gui.panel);
		}

		{
			final RankingGui gui = initialRanking;
			final String rankingTitle = "Classement initial";
			{
				buildRankingArea(gui, rankingTitle);
			}
			rankingPanel.add(gui.panel);
		}
	}

	private void buildRankingArea(RankingGui gui, String rankingTitle) {
		gui.panel.setBorder(BorderFactory.createTitledBorder(rankingTitle));
		gui.panel.setLayout(new GridLayout(6 + 1, 3));

		gui.panel.add(gui.rank1_Label);
		gui.panel.add(gui.rank1_TextField);
		gui.panel.add(gui.rank1_PercentLabel);

		gui.panel.add(gui.rank2_Label);
		gui.panel.add(gui.rank2_TextField);
		gui.panel.add(gui.rank2_PercentLabel);

		gui.panel.add(gui.rank3_Label);
		gui.panel.add(gui.rank3_TextField);
		gui.panel.add(gui.rank3_PercentLabel);

		gui.panel.add(gui.rank4_Label);
		gui.panel.add(gui.rank4_TextField);
		gui.panel.add(gui.rank4_PercentLabel);

		gui.panel.add(gui.rank5_Label);
		gui.panel.add(gui.rank5_TextField);
		gui.panel.add(gui.rank5_PercentLabel);

		gui.panel.add(gui.rank6_Label);
		gui.panel.add(gui.rank6_TextField);
		gui.panel.add(gui.rank6_PercentLabel);

		gui.setFrench();
	}

	private void makeInitialisationPanel() {
		initialisationPanel.setBorder(BorderFactory.createTitledBorder("Initialisation"));
		initialisationPanel.setLayout(new GridLayout(3, 4));

		consistencyMakerTypeRandomRadioButton.setText("A");
		consistencyMakerTypeButtonGroup.add(consistencyMakerTypeRandomRadioButton);
		initialisationPanel.add(consistencyMakerTypeRandomRadioButton);

		consistencyMakerTypeSaatyRadioButton.setText("S");
		consistencyMakerTypeButtonGroup.add(consistencyMakerTypeSaatyRadioButton);
		initialisationPanel.add(consistencyMakerTypeSaatyRadioButton);

		datasetP1RadioButton.setText("P1");
		datasetButtonGroup.add(datasetP1RadioButton);
		initialisationPanel.add(datasetP1RadioButton);

		datasetP2RadioButton.setText("P2");
		datasetButtonGroup.add(datasetP2RadioButton);
		initialisationPanel.add(datasetP2RadioButton);

		matrixSizeLabel.setText("Taille matrice:");
		initialisationPanel.add(matrixSizeLabel);

		matrixSizeTextField.setText("6");
		initialisationPanel.add(matrixSizeTextField);

		saveFilePathLabel.setText("Chemin de sauvegarde:");
		initialisationPanel.add(saveFilePathLabel);
		initialisationPanel.add(saveFilePathTextField);

		initialisationPanel.add(new JPanel());
		initialisationPanel.add(new JPanel());

		saveFilePathOkButton.setText("Ok");
		saveFilePathOkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				saveFilePathOkButtonActionPerformed();
			}
		});
		initialisationPanel.add(saveFilePathOkButton);

		saveFilePathExploreButton.setText("Parcourir");
		saveFilePathExploreButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				saveFilePathExploreButtonActionPerformed();
			}
		});
		initialisationPanel.add(saveFilePathExploreButton);
	}

	/*
	 * Cette méthode creer les matrices et les retournes(vide ou rempli)
	 */
	public MyMatrix createMatrix(int matrixSize, int type) {

		// ajoute un paramètre pour l'init du tableau ou la création de la matrice
		final MyMatrix maMatrice = new MyMatrix(matrixSize, matrixSize);

		if (type != 0) { //Création matrice pleine
			for (int i = 0; i < maMatrice.getRowDimension(); i++) {
				for (int j = i + 1; j < maMatrice.getColumnDimension(); j++) {
					boolean choix = false;

					//on récupère les valeurs saisies de la matrice
					monJep.parseExpression(matrixTable.getValueAt(i + 1, j + 1).toString());
					//on la stocke
					double newValue = monJep.getValue();
					//Si la valeur n'appartient pas à l'échelle de Saaty. On demande a l'expert de la modifier
					while (!ahpData.isInSaatyScale(newValue)) {
						while (choix == false) {
							final String val = JOptionPane.showInputDialog(
								null,
								"Erreur avec la valeur " + newValue + " veuillez la re-saisir la valeur:",
								"Erreur saisi",
								JOptionPane.QUESTION_MESSAGE);
							matrixTable.setValueAt(val, i + 1, j + 1);

							if (val == null) { //si clik Annuler
								choix = true;
							} else { //si clik Ok
								if (val.equalsIgnoreCase("")) { //Si pas de saisi
									JOptionPane.showMessageDialog(
										null,
										"Veuillez saisir une valeur",
										"Information",
										JOptionPane.INFORMATION_MESSAGE);
								} else { //si saisi on récup la valeur
									choix = true;
									//on récupère les valeurs saisies de la matrice
									monJep.parseExpression(val);
									//on la stocke
									newValue = monJep.getValue();
								}
							}
						}
					}
					/*Partie supérieure*/
					maMatrice.setMatrixValue(new MatrixValue(i, j, newValue));
					/*Réciprocité*/
					maMatrice.setMatrixValue(new MatrixValue(j, i, 1 / newValue));
				}
			}
			/*Diagonale*/
			for (int i = 0; i < maMatrice.getRowDimension(); i++) {
				maMatrice.setMatrixValue(new MatrixValue(i, i, 1));
			}
		} else { //Création matrice vide
			// default values
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

		final CharSequenceAppender csa = new CharSequenceAppender(file);
		MatrixValue matrixValue = new MatrixValue();

		Iterator<MatrixValue> valueIterator = sortedMatrixValues.iterator();
		MatrixValue matrixValueToPrint = new MatrixValue();
		int isValueChosen = 0;
		boolean isFound = false;

		MonCellRenderer monCell = new MonCellRenderer(0, 0);
		matrixTable.setDefaultRenderer(Object.class, monCell);

		while (isValueChosen == 0) {
			matrixValue = valueIterator.next();
			matrixValueToPrint.setRow(matrixValue.getRow());
			matrixValueToPrint.setColumn(matrixValue.getColumn());
			matrixValueToPrint.setValue(
				myPreferenceMatrix.get(matrixValueToPrint.getRow(), matrixValueToPrint.getColumn()));
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
				isEndSimulation = true;
			} else if (!valueIterator.hasNext()) {
				System.out.println("Retour en haut du classement");
				valueIterator = sortedMatrixValues.iterator();
			}
		}

		// si l'expert n'a pas mit à la simulation
		if (isEndSimulation) {
			csa.close();
		} else {
			/** parcours de la liste pour l'écriture dans le fichier */
			valueIterator = sortedMatrixValues.iterator();

			while ((valueIterator.hasNext()) && (!isFound)) {

				final MatrixValue tempMatrixValue = valueIterator.next();
				csa.appendLineFeed();

				/** écriture du best fit associé à la valeur proposée */
				//copie de la matrice initiale
				final MyMatrix tempMatrix = MyMatrix.copyMyMatrix(myPreferenceMatrix);

				//calcul du vecteur propre associé à tempMatrix
				PriorityVector tempVector = PriorityVector.build(tempMatrix);
				//calcul du best fit
				double BestFit = SaatyTools.calculateBestFit(tempMatrix,
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
				consistencyChecker.isConsistent(tempMatrix, tempVector2);
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

	private void saveFilePathOkButtonActionPerformed() {
		//on crée et lance le thread pour afficher un compteur

		monHeure = new Meter(timeLabel);
		monHeure.setCommencerAZero(false);
		monHeure.start();

		ahpData.myMatrix = new MyMatrix();
		ahpData.matrixTableModel = new MyMatrixTableModel();
		//On crée la matrice vide
		ahpData.myMatrix = createMatrix(Integer.parseInt(matrixSizeTextField.getText()), 0);
		//maTable = new MyMatrixTable();

		//on teste pour savoir quelle est le pb choisi
		if (datasetP1RadioButton.isSelected()) {
			//on crée le fichier historique à partir de "file"
			fistoryFile = file + "histI.csv";
			file += "I.csv";
			try {
				csa = new CharSequenceAppender(file);
				csa.append("Probleme 1");
				csa.appendCommaSeparator();
				//On teste saaty ou aléatoire
				if (consistencyMakerTypeSaatyRadioButton.isSelected()) {
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
			fistoryFile = file + "histII.csv";
			file += "II.csv";
			try {
				csa = new CharSequenceAppender(file);
				csa.append("Probleme 2");
				csa.appendCommaSeparator();
				//On teste saaty ou aléatoire
				if (consistencyMakerTypeSaatyRadioButton.isSelected()) {
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
		matrixTable.setModel(ahpData.matrixTableModel);
		//maTable.setModel(matrixTableModel);
		matrixCommentLabel.setText(
			"Veuillez remplir la partie supérieure (cases blanches) de la matrice avec les coefficients de SAATY:");
		//on affiche la matrice
		//showMatrixTable((MyMatrixTable) jTable1,myMatrix);
		//on rempli les jComboBox par le nom des column pour le 1er classement
		String[] mesColonnes = SampleMatrixHeaders.getColumnHeader(datasetP1RadioButton.isSelected(), modeAnglais);
		remplirJComboBox(intuitiveRanking.rank1_ComboBox, mesColonnes);
		remplirJComboBox(intuitiveRanking.rank2_ComboBox, mesColonnes);
		remplirJComboBox(intuitiveRanking.rank3_ComboBox, mesColonnes);
		remplirJComboBox(intuitiveRanking.rank4_ComboBox, mesColonnes);
		remplirJComboBox(intuitiveRanking.rank5_ComboBox, mesColonnes);
		remplirJComboBox(intuitiveRanking.rank6_ComboBox, mesColonnes);
		//on modifie l'affichage de la table
		MonCellRenderer monCellRenderer = new MonCellRenderer(0, 0);
		matrixTable.setDefaultRenderer(Object.class, monCellRenderer);
	}

	/*
	 * Cette méthode ecrit dans le fichier csv
	 */
	private double creerFichierCsv() {
		final ConsistencyChecker consistencyChecker = new ConsistencyChecker();

		PriorityVector priorityVector = PriorityVector.build(ahpData.myMatrix);
		MyMatrix epsilon = new MyMatrix();
		MatrixValue matrixValue = new MatrixValue();
		Collection<MatrixValue> collectionOfSortedMatrixValues = new ArrayList<>();
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
		this.writeHistoryFile(ahpData.myMatrix, 0, 0, 0, 0);

		//on va afficher le classement des critères de la 1ere matrice
		String[] classement = this.classerCriteres(priorityVector);
		initialRanking.rank1_TextField.setText(classement[5]);
		initialRanking.rank2_TextField.setText(classement[4]);
		initialRanking.rank3_TextField.setText(classement[3]);
		initialRanking.rank4_TextField.setText(classement[2]);
		initialRanking.rank5_TextField.setText(classement[1]);
		initialRanking.rank6_TextField.setText(classement[0]);
		double[] classementVal = classerPourcentageCritères(priorityVector);
		initialRanking.rank1_PercentLabel.setText(classementVal[5] + "%");
		initialRanking.rank2_PercentLabel.setText(classementVal[4] + "%");
		initialRanking.rank3_PercentLabel.setText(classementVal[3] + "%");
		initialRanking.rank4_PercentLabel.setText(classementVal[2] + "%");
		initialRanking.rank5_PercentLabel.setText(classementVal[1] + "%");
		initialRanking.rank6_PercentLabel.setText(classementVal[0] + "%");

		//Writing of the CR
		consistencyChecker.isConsistent(ahpData.myMatrix, priorityVector);
		String tempString = "" + consistencyChecker.getConsistencyRatio();
		csa.append("CR initial");
		csa.appendLineFeed();
		csa.append(tempString);
		csa.appendLineFeed();
		csa.appendLineFeed();

		//On affiche le cr
		crTextField.setText(String.valueOf(consistencyChecker.getConsistencyRatio()));
		csa.append("Tableau de changement des valeurs");
		csa.appendLineFeed();
		//en-tête du tableau, on teste quelle est la méthode chosi(Aléatoire, Saaty)
		if (consistencyMakerTypeSaatyRadioButton.isSelected()) {
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
			if (isEndSimulation == false) {
				//incrémentation du compteur du nombre d'itération
				iterationCounter++;

				//on teste quelles méthode(aléaloire, saaty)
				if (consistencyMakerTypeSaatyRadioButton.isSelected()) {
					/*Calcul matrice epsilon*/
					epsilon = SaatyTools.calculateEpsilonMatrix(ahpData.myMatrix, priorityVector);
					/*Recherche de la valeur à modifier*/
					collectionOfSortedMatrixValues = SaatyTools.getRank(epsilon);
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
				if (isEndSimulation == false) {
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
						/** Ecrire la valeur que souhaite modifier l'expert */
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
					if (consistencyMakerTypeSaatyRadioButton.isSelected()) {
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
					matrixTable.setModel(ahpData.matrixTableModel);

					//Réactualisation du vecteur de priorité associé à la nouvelle matrice
					priorityVector = PriorityVector.build(ahpData.myMatrix);
					//		priorityVector.print(5, 5);

					//Ecriture du nouveau CR
					consistencyChecker.isConsistent(ahpData.myMatrix, priorityVector);
					tempString = "" + consistencyChecker.getConsistencyRatio();
					csa.append(tempString);
					csa.appendCommaSeparator();
					//uniquement si on fai la méthode saaty
					if (consistencyMakerTypeSaatyRadioButton.isSelected()) {
						tempString = String.valueOf(ahpData.saatyConsistency - consistencyChecker.getConsistencyRatio());
						csa.append(tempString);
					}
					csa.close();
					//on va ecrire dans le fichier historique
					this.writeHistoryFile(ahpData.myMatrix, oldValue, coordRowVal, coordColVal, newValue);
					//on récupére la nouvelle valeur
					myConsistent = consistencyChecker.isConsistent(ahpData.myMatrix, priorityVector);
				}
				//si il à mi fin on sort de la boucle
				else if (isEndSimulation) {
					//on arrete le thread
					monHeure.stop();
					myConsistent = true;
				}
				//on affiche un cpt pour les observations avec un coeff de 7.5
				counterLabel.setText(String.valueOf(iterationCounter * 7.5));
				crTextField.setText(String.valueOf(consistencyChecker.getConsistencyRatio()));
			}
		}
		if (isEndSimulation) {
			finishLabel.setText("Bravo la matrice est cohérente!!!!!");
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
		finalRanking.rank1_TextField.setText(classementF[5]);
		finalRanking.rank2_TextField.setText(classementF[4]);
		finalRanking.rank3_TextField.setText(classementF[3]);
		finalRanking.rank4_TextField.setText(classementF[2]);
		finalRanking.rank5_TextField.setText(classementF[1]);
		finalRanking.rank6_TextField.setText(classementF[0]);
		double[] classementValF = classerPourcentageCritères(priorityVector);
		finalRanking.rank1_PercentLabel.setText(classementValF[5] + "%");
		finalRanking.rank2_PercentLabel.setText(classementValF[4] + "%");
		finalRanking.rank3_PercentLabel.setText(classementValF[3] + "%");
		finalRanking.rank4_PercentLabel.setText(classementValF[2] + "%");
		finalRanking.rank5_PercentLabel.setText(classementValF[1] + "%");
		finalRanking.rank6_PercentLabel.setText(classementValF[0] + "%");

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

	private void validateMatrixActionPerformed() {
		ahpData.myMatrix = new MyMatrix();
		ahpData.matrixTableModel = new MyMatrixTableModel();
		//On crée la matrice rempli
		ahpData.myMatrix = createMatrix(Integer.parseInt(matrixSizeTextField.getText()), 1);

		/*Interface graphique*/
		ahpData.matrixTableModel.setMatrix(
			ahpData.myMatrix,
			SampleMatrixHeaders.getColumnHeader(datasetP1RadioButton.isSelected(), modeAnglais)
		);
		//on ajoute directement matrixTableModel à jTable1 bugg??????
		matrixTable.setModel(ahpData.matrixTableModel);

		//on va cree le fichier .csv et le modifier et on récupérer le CR et afficher
		crTextField.setText(String.valueOf(creerFichierCsv()));
		//on affiche la matrice
		//showMatrixTable( (MyMatrixTable) jTable1,myMatrix);
		//on ferme le flux
		csa.close();
		//on remet à 0 l'attribut
		isEndSimulation = false;
		//on arrete le thread si c pas déja fait
		if (isEndSimulation == false) {
			monHeure.stop();
		}
		//on réaffiche le classement intuitif
		intuitiveRanking.panel.setVisible(true);
	}

	/*
	 * Cette méthode permet de stocker les différentes matrices,les valeurs modifiées
	 */
	private void writeHistoryFile(MyMatrix matrix, double oldValue, int coordx, int coordy, double newValue) {
		CharSequenceAppender csa = null;

		try {
			csa = new CharSequenceAppender(fistoryFile);

			csa.append("Ancienne valeur: ").append(oldValue);
			csa.appendCommaSeparator();
			csa.append(coordx).append(",").append(coordy);
			csa.appendCommaSeparator();
			csa.append("Nouvelle valeur: ").append(newValue);
			csa.appendLineFeed();
			csa.append(matrix);
			csa.appendLineFeed();
			csa.close();
		} catch (IOException ex) {
			Logger.getLogger(InterfaceAHP.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * This method returns the value which will be modified by the expert
	 */
	public MatrixValue getValueToModifiyByRanking(Collection<MatrixValue> collectionOfNonSortedMatrixValues) {

		int isValueChosen = 0;
		MatrixValue matrixValue = new MatrixValue();
		Iterator<MatrixValue> valueIterator;
		/* While loop which proposes a random ranking of MatrixValue
		 * while the expert hasn't chosen the value he wants to modify
		 */
		valueIterator = collectionOfNonSortedMatrixValues.iterator();
		MonCellRenderer monCell = new MonCellRenderer(0, 0);
		matrixTable.setDefaultRenderer(Object.class, monCell);
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
				String.format("Modifier la valeur %s ( %d , %d ) ?", matrixValue.getValue(), matrixValue.getRow() + 1, matrixValue.getColumn() + 1),
				"Modification des valeurs",
				JOptionPane.YES_NO_CANCEL_OPTION);
			//si on clique sur ok on sor du while
			if (option == JOptionPane.OK_OPTION) {
				isValueChosen = 1;
			} else if (option == JOptionPane.CANCEL_OPTION) {
				isValueChosen = 1;
				isEndSimulation = true;
			} else if (!valueIterator.hasNext()) {
				// Retour en haut du classement
				valueIterator = collectionOfNonSortedMatrixValues.iterator();
			}
		}
		return matrixValue;
	}

	/*
	 * Cette méthode permet de classer les critères et de renvoyer le classement
	 */
	private String[] classerCriteres(PriorityVector v) {
		//on teste quel est le pb choisi
		if (datasetP1RadioButton.isSelected()) {

			String[] classementString = new String[v.getRowDimension()];
			double[] monClassement = new double[v.getRowDimension()];
			for (int i = 0; i < v.getRowDimension(); i++) {
				monClassement[i] = v.get(i, 0);
			}
			//on classe par ordre croissant
			Arrays.sort(monClassement);

			for (int i = 0; i < v.getRowDimension(); i++) {
				final String columnNames[] = SampleMatrixHeaders.getColumnHeader(datasetP1RadioButton.isSelected(), modeAnglais);

				for (int j = 0; j < v.getRowDimension(); j++) {
					int temp = j;
					//on récupérer les critères classés
					if (monClassement[i] == v.get(j, 0)) {
						classementString[i] = columnNames[j];
						//on sort du for
						j = v.getRowDimension();
						if (i != 0) {
							//on teste l'ancienne valeur du classement pour éviter les doublons
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
							//on teste l'ancienne valeur du classement pour éviter les doublons
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

	private void newSimulationButtonActionPerformed() {
		initComponents();
		this.dispose();
		new InterfaceAHP().setVisible(true);
	}

	private void saveFilePathExploreButtonActionPerformed() {
		JFileChooser jfc = new JFileChooser();
		jfc.setVisible(true);
		final int option = jfc.showSaveDialog(this);
		if (JFileChooser.APPROVE_OPTION == option) {
			//On récup le chemin
			file = jfc.getSelectedFile().getAbsolutePath();
			//On affiche le chemin de sauvegarde
			saveFilePathTextField.setText(file);
		}
	}

	private void intuitiveRankingOKActionPerformed() {
		intuitiveRanking.panel.setVisible(false);
	}

	private void setEnglishLangButtonActionPerformed() {
		modeAnglais = true;
		setFrenchLangButton.setText("French");
		//Panel init
		newSimulationButton.setText("New Simulation");
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
		intuitiveRanking.panel.setBorder(BorderFactory.createTitledBorder("Intuitive classement"));
		initialRanking.panel.setBorder(BorderFactory.createTitledBorder("Initial classement"));
		finalRanking.panel.setBorder(BorderFactory.createTitledBorder("Final classement"));
		//bouton valider matrice
		validateMatrixButton.setText("Validate Matrix");

		intuitiveRanking.setEnglish();
		initialRanking.setEnglish();
		finalRanking.setEnglish();
	}

	private void setFrenchLangButtonActionPerformed() {
		//on repasse à false le mode anglais
		modeAnglais = false;
		initComponents();
		this.dispose();
		new InterfaceAHP().setVisible(true);
	}

	class RankingGui {

		final JPanel panel = new JPanel();
		final JTextField rank1_TextField = new JTextField();
		final JTextField rank2_TextField = new JTextField();
		final JTextField rank3_TextField = new JTextField();
		final JTextField rank4_TextField = new JTextField();
		final JTextField rank5_TextField = new JTextField();
		final JTextField rank6_TextField = new JTextField();
		final JLabel rank1_Label = new JLabel();
		final JLabel rank2_Label = new JLabel();
		final JLabel rank3_Label = new JLabel();
		final JLabel rank4_Label = new JLabel();
		final JLabel rank5_Label = new JLabel();
		final JLabel rank6_Label = new JLabel();
		final JLabel rank1_PercentLabel = new JLabel();
		final JLabel rank2_PercentLabel = new JLabel();
		final JLabel rank3_PercentLabel = new JLabel();
		final JLabel rank4_PercentLabel = new JLabel();
		final JLabel rank5_PercentLabel = new JLabel();
		final JLabel rank6_PercentLabel = new JLabel();
		final JComboBox<String> rank1_ComboBox = new JComboBox<>();
		final JComboBox<String> rank2_ComboBox = new JComboBox<>();
		final JComboBox<String> rank3_ComboBox = new JComboBox<>();
		final JComboBox<String> rank4_ComboBox = new JComboBox<>();
		final JComboBox<String> rank5_ComboBox = new JComboBox<>();
		final JComboBox<String> rank6_ComboBox = new JComboBox<>();

		void setEnglish() {
			rank1_Label.setText("1 st  ");
			rank2_Label.setText("2 nd  ");
			rank3_Label.setText("3 th  ");
			rank4_Label.setText("4 th  ");
			rank5_Label.setText("5 th  ");
			rank6_Label.setText("6 th  ");
		}

		void setFrench() {
			rank1_Label.setText("1 er  ");
			rank2_Label.setText("2 nd  ");
			rank3_Label.setText("3 ème ");
			rank4_Label.setText("4 ème ");
			rank5_Label.setText("5 ème ");
			rank6_Label.setText("6 ème ");
		}
	}

	private class AHP_Data {

		private final double[] saatyValues = {
			1. / 9, 1. / 8, 1. / 7, 1. / 6, 1. / 5, 1. / 4, 1. / 3, 1. / 2, 1, 2, 3, 4, 5, 6, 7, 8, 9
		};
		private MyMatrix myMatrix = new MyMatrix();
		private MyMatrixTableModel matrixTableModel = new MyMatrixTableModel();
		private double saatyConsistency = Double.NaN;

		public boolean isInSaatyScale(double value) {
			boolean result = false;

			for (double d : saatyValues) {
				if (value == d) {
					result = true;
				}
			}
			return result;
		}
	}
}

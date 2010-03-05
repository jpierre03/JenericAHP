/* Copyright 2009 Yves Dubromelle @ LSIS(www.lsis.org)
 * 
 * This file is part of GenericAHP.
 * 
 * GenericAHP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GenericAHP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GenericAHP.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.taeradan.ahp;

import Jama.Matrix;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 *  This is the root class of the AHP tree. It contains Criterias and execute its part of the AHP algorithm.
 * @author Yves Dubromelle
 */
public class Root {

    /**
     * Contains the path to access indicators
     */
    public static String indicatorPath = "org.taeradan.ahp.test.ind.";

//	AHP configuration attributes
    /**
     *
     */
    private String name;
    /**
     *
     */
    transient private PreferenceMatrix matrixCrCr;
    /**
     *
     */
    transient private PriorityVector vectorCrOg;
    /**
     *
     */
    transient private Collection<Criteria> criterias;
//	AHP execution attributes
    /**
     *
     */
    transient private PriorityVector vectorAltOg;
    /**
     *
     */
    transient private Matrix matrixAltCr;
//	Execution control attributes
    /**
     *
     */
    transient private boolean calculationOccured = false;

    /**
     * Class constructor that creates the AHP tree from a configuration file given in argument.
     * @param inFile Path to the configuration file
     * @param indicatorPath
     */
    public Root(File inFile, String indicatorPath) {
        if (inFile == null) {
            name = "";
            matrixCrCr = new PreferenceMatrix();
            criterias = new ArrayList<Criteria>();
        } else {
            Root.indicatorPath = indicatorPath;
//			XML parser creation
            SAXBuilder parser = new SAXBuilder();
            try {
//				JDOM document created from XML configuration file
                Document inXmlDocument = parser.build(inFile);
//				Extraction of the root element from the JDOM document
                Element xmlRoot = inXmlDocument.getRootElement();
//				Initialisation of the AHP tree name
                name = xmlRoot.getChildText("name");
//				Initialisation of the preference matrix
                Element xmlPrefMatrix = xmlRoot.getChild("prefmatrix");
                matrixCrCr = new PreferenceMatrix(xmlPrefMatrix);
                vectorCrOg = new PriorityVector(matrixCrCr);
//				Consistency verification
                if (!ConsistencyChecker.isConsistent(matrixCrCr, vectorCrOg)) {
                    Logger.getAnonymousLogger().severe("Is not consistent (root)");
                }
//				Initialisation of the criterias
                @SuppressWarnings("unchecked")
                List<Element> xmlCriteriasList = (List<Element>) xmlRoot.getChildren("criteria");
                @SuppressWarnings("unchecked")
                List<Element> xmlRowsList = (List<Element>) xmlPrefMatrix.getChildren("row");
                criterias = new ArrayList<Criteria>(xmlCriteriasList.size());
//				Verification that the number of criterias matches the size of the preference matrix
                if (xmlCriteriasList.size() != xmlRowsList.size()) {
                    Logger.getAnonymousLogger().severe("Error : the number of criterias and the size of the preference matrix does not match !");
                }
                for (int i = 0; i < xmlCriteriasList.size(); i++) {
                    Element xmlCriteria = xmlCriteriasList.get(i);
                    criterias.add(new Criteria(xmlCriteria));
//				System.out.println("Root.criteria="+criterias.get(i));
                }
            } catch (FileNotFoundException e) {
                Logger.getAnonymousLogger().severe("File not found : " + inFile.getAbsolutePath());
                name = "unknow";
                matrixCrCr = new PreferenceMatrix();
                criterias = new ArrayList<Criteria>();
            } catch (JDOMException e) {
                Logger.getAnonymousLogger().severe(e.getLocalizedMessage());
            } catch (IOException e) {
                Logger.getAnonymousLogger().severe(e.getLocalizedMessage());
            }
        }
    }

    /**
     *
     * @param crit
     */
    public void delCriteria(Criteria crit) {
        if (criterias.contains(crit)) {
            int critIndex = new ArrayList<Criteria>(criterias).lastIndexOf(crit);
            criterias.remove(crit);
            matrixCrCr.remove(critIndex);
        } else {
            Logger.getAnonymousLogger().severe("Criteria not found");
        }
    }

    /**
     * Returns a string describing the AHP root, and NOT its children.
     * @return Describing string
     */
    @Override
    public String toString() {
        return " AHP Root : " + name + ", " + criterias.size() + " criterias";
    }

    /**
     * Returns a big string (multiple lines) containing recursively the description of the whole AHP tree. Best suited for testing purposes.
     * @return Multiline string describing the AHP tree
     */
    public String toStringRecursive() {
        String string = this.toString();
        string = string.concat("\n" + matrixCrCr);
        DecimalFormat printFormat = new DecimalFormat("0.000");
        for (int i = 0; i < criterias.size(); i++) {
            string = string.concat("\n\t("
                    + printFormat.format(vectorCrOg.getVector().get(i, 0))
                    + ") "
                    + ((Criteria) criterias.toArray()[i]).toStringRecursive());
        }
        return string;
    }

    /**
     * Returns a JDOM element that represents the AHP root and its children
     * @return JDOM Element representing the whole AHP tree
     */
    public Element toXml() {
        Element xmlRoot = new Element("root");
        xmlRoot.addContent(new Element("name").setText(name));
        xmlRoot.addContent(matrixCrCr.toXml());
        for (int i = 0; i < criterias.size(); i++) {
            xmlRoot.addContent(((Criteria) criterias.toArray()[i]).toXml());
        }
        return xmlRoot;
    }

    /**
     *
     * @return
     */
    public String resultToString() {
        String string;
        if (calculationOccured) {
            string = this.toString();
            for (int i = 0; i < criterias.size(); i++) {
                string = string.concat("\n\t" + ((Criteria) criterias.toArray()[i]).resultToString());
            }
            string = string.concat("\nvectorAltOg=\n" + PreferenceMatrix.toString(vectorAltOg.getVector(), null));
        } else {
            string = "There is no result, please do a ranking first";
        }
        return string;
    }

    /**
     * Saves the whole AHP tree in a XML file
     * @param outputFile Output XML file path
     */
    public void saveConfig(String outputFile) {
        try {
//			Save the AHP tree in a XML document matching the Doctype "ahp_conf.dtd"
            Document outXmlDocument = new Document(toXml(), new DocType("root", getClass().getResource("/org/taeradan/ahp/conf/ahp_conf.dtd").getFile()));
//			Use a write format easily readable by a human
            XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());
//			Write the output into the specified file
            output.output(outXmlDocument, new FileOutputStream(outputFile));
        } catch (IOException ex) {
            Logger.getLogger(Root.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Root method of the AHP execution.
     * Calculates the final alternatives ranking with the alternatives priority vectors from the criterias and the criterias priority vectors.
     * @param alternatives
     */
    public void calculateRanking(Collection<? extends Alternative> alternatives) {
        matrixAltCr = new Matrix(alternatives.size(), criterias.size());
//		Concatenation in a matrix of the vectors calculated by the criterias
        for (int i = 0; i < criterias.size(); i++) {
            matrixAltCr.setMatrix(0, alternatives.size() - 1, i, i, ((Criteria) criterias.toArray()[i]).calculateAlternativesPriorityVector(alternatives).getVector());
        }
//		Calculation of the final alternatives priority vector
        vectorAltOg = new PriorityVector();
        vectorAltOg.setVector(matrixAltCr.times(vectorCrOg.getVector()));
//		Ranking of the alternatives with the MOg vector
        Matrix sortedvectorAltOg = new Matrix(vectorAltOg.getVector().getArrayCopy());
        double lastAltOgValue = Double.POSITIVE_INFINITY;
        int lastAltOgIndex = Integer.MIN_VALUE;
//		For each rank to be given to the alternatives
        for (int rank = 0; rank < alternatives.size(); rank++) {
//			We search which alternative has the highest priority (AltOg value), but lower than the previous priority found
            double maxAltOgValue = Double.NEGATIVE_INFINITY;
            int maxAltOgIndex = 0;
            for (int altOgIndex = 0; altOgIndex < alternatives.size(); altOgIndex++) {
                final double altOgValue = sortedvectorAltOg.get(altOgIndex, 0);
                if ((altOgValue > maxAltOgValue) && (altOgValue <= lastAltOgValue) && (altOgIndex > lastAltOgIndex)) {
                    maxAltOgValue = altOgValue;
                    maxAltOgIndex = altOgIndex;
                }
            }
            lastAltOgValue = maxAltOgValue;
            lastAltOgIndex = maxAltOgIndex;
            ((Alternative) alternatives.toArray()[maxAltOgIndex]).setRank(rank + 1);
        }
        calculationOccured = true;
    }

    /**
     *
     * @return
     */
    public PreferenceMatrix getMatrixCrCr() {
        return matrixCrCr;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public Collection<Criteria> getCriterias() {
        return criterias;
    }
}

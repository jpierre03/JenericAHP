/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package org.taeradan.ahp;

import java.util.ArrayList;

/**
 *  This is the root class of the AHP tree. It contains Criterias and execute its part of the AHP algorithm.
 * @author Yves Dubromelle
 */
public class Root {
    
    private ArrayList<Criteria> criterias;
    private PreferenceMatrix matrixCC;
    private PriorityVector vectorCrOg;
    private AlternativesPriorityVector vectorMOg;
    
    /**
     * Class default constructor
     */
    public Root() {
    }
    
    
}

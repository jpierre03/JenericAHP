/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.taeradan.ahp;

import java.util.ArrayList;

/**
 * This class provides the basis of the indicators of AHP. It is abstract because we can't know what the calculus method should do for each user.
 * @author Yves Dubromelle
 */
public abstract class Indicator {
    
    private AlternativesPriorityVector vectorMCr;
    private ArrayList alternatives;
    
    
    /**
     * Class default constructor
     */

    public Indicator() {
    }

}

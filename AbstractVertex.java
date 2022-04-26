/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xdat.categoricalClustering.kmodes;

import java.util.ArrayList;

/**
 *
 * @author Stefan
 */
abstract public class AbstractVertex {
    
    private int attributeIndex;
    private String value;
    private AbstractVertex predecessor;

    public AbstractVertex(int attributeIndex, String value, AbstractVertex predecessor) {
        this.attributeIndex = attributeIndex;
        this.value = value;
        this.predecessor = predecessor;
    }

    public int getAttributeIndex() {
        return attributeIndex;
    }

    public AbstractVertex getPredecessor() {
        return predecessor;
    }

    public String getValue() {
        return value;
    }
    
    abstract public void toString(String string);
    
    abstract public boolean isTerminal();
}

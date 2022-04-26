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
public class TerminalVertex extends AbstractVertex {

    private ArrayList<Integer> indices;

    public TerminalVertex(int attributeIndex, String value, AbstractVertex predecessor, int index) {
        super(attributeIndex, value, predecessor);
        indices = new ArrayList<>();
        indices.add(index);

    }

    public ArrayList<Integer> getIndices() {
        return indices;
    }

    public void addIndex(int index) {
        if (!indices.contains(index)) {
            indices.add(index);
        }
    }

    @Override
    public void toString(String string) {
        System.out.println(string + " - (" + this.getAttributeIndex() + ") " + this.getValue() + " => has " + indices.size() + " data points");
    }

    public void selfToString() {
        String output = " - (" + this.getAttributeIndex() + ") " + this.getValue() + " => has " + indices.size() + " data points";
        AbstractVertex tmp = this;
        while (tmp.getPredecessor() != null) {
            tmp = tmp.getPredecessor();
            output = " - (" + tmp.getAttributeIndex() + ") " + tmp.getValue() + output;
        }
        System.out.println(output);
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

}

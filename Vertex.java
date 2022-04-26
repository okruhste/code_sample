/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xdat.categoricalClustering.kmodes;

import java.util.ArrayList;
import java.util.List;
import org.xdat.data.DataSheet;

/**
 *
 * @author Stefan
 */
public class Vertex extends AbstractVertex {
    
    private ArrayList<AbstractVertex> successors;
    
    public Vertex(int attributeIndex, String value, AbstractVertex predecessor) {
        super(attributeIndex, value, predecessor);
        successors = new ArrayList<>();
    }
    
    public ArrayList<AbstractVertex> getSuccessors() {
        return successors;
    }
    
    public void proccessItem(int index, int maxIndex, int dataIndex, List<String> data) {
        if (index < (maxIndex - 1)) {
            if (successors.isEmpty()) {
                successors.add(new Vertex(index, data.get(index), this));
                ((Vertex) successors.get(0)).proccessItem(index + 1, maxIndex, dataIndex, data);
            } else {
                boolean foundMatch = false;
                int matchIndex = 0;
                for (int i = 0; i < successors.size(); i++) {
                    if ((successors!=null && successors.get(i) != null && successors.get(i).getValue()!=null) && successors.get(i).getValue().equals(data.get(index))) {
                        foundMatch = true;
                        matchIndex = i;
                        break;
                    }
                }
                if (!foundMatch) {                    
                    successors.add(new Vertex(index, data.get(index), this));
                    matchIndex = successors.size() - 1;
                }
                ((Vertex) successors.get(matchIndex)).proccessItem(index + 1, maxIndex, dataIndex, data);
            }
        } else if (index == (maxIndex - 1)) {
            boolean foundMatch = false;
            int matchIndex = 0;
            for (int i = 0; i < successors.size(); i++) {
                if (successors.get(i).getValue().equals(data.get(index))) {
                    foundMatch = true;
                    matchIndex = i;
                    break;
                }
            }
            if (!foundMatch) {
                successors.add(new TerminalVertex(index, data.get(index), this, dataIndex));
            } else {
                ((TerminalVertex) successors.get(matchIndex)).addIndex(dataIndex);
            }
        }
    }
    
    @Override
    public void toString(String string) {
        for (AbstractVertex successor : successors) {
            successor.toString(string + " - (" + this.getAttributeIndex() + ") " + this.getValue());
        }
    }

    @Override
    public boolean isTerminal() {
        return false;
    }
}

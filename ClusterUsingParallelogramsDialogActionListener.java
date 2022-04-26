/*
 *  Copyright 2014, Enguerrand de Rochefort
 * 
 * This file is part of xdat.
 *
 * xdat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * xdat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with xdat.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package org.xdat.actionListeners.clusteringParallelograms;

import org.xdat.actionListeners.clusterDialog.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

import org.xdat.Main;
import org.xdat.categoricalClustering.kmodes.AbstractVertex;
import org.xdat.categoricalClustering.kmodes.TerminalVertex;
import org.xdat.categoricalClustering.kmodes.Vertex;
import org.xdat.data.ClusteringTableParameter;
import org.xdat.data.DataSheet;
import org.xdat.data.Parameter;
import org.xdat.data.ParameterSet;
import org.xdat.gui.dialogs.ClusterDialog;
import org.xdat.gui.dialogs.ClusterUsingParallelogramsDialog;

/**
 * ActionListener for a {@link ClusterDialog}.
 *
 */
public class ClusterUsingParallelogramsDialogActionListener implements ActionListener {

    /**
     * Flag to enable debug message printing for this class.
     */
    static final boolean printLog = false;

    /**
     * The main window.
     */
    private Main mainWindow;

    private DataSheet dataSheet;

    /**
     * The dialog.
     */
    private ClusterUsingParallelogramsDialog dialog;

    /**
     * Instantiates a new cluster dialog action listener.
     *
     * @param mainWindow the main window
     * @param dialog the dialog
     * @param dataSheet the data sheet
     *
     */
    public ClusterUsingParallelogramsDialogActionListener(Main mainWindow, ClusterUsingParallelogramsDialog dialog, DataSheet dataSheet) {
        this.mainWindow = mainWindow;
        this.dataSheet = dataSheet;
        this.dialog = dialog;

    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        log("Action Command = " + actionCommand);
        if (actionCommand.equals("Cancel")) {
            dialog.setVisible(false);
            dialog.dispose();
        } else if (actionCommand.equals("Ok")) {
            // make sure the cell is not in editing mode anymore
            if (this.dialog.getParametersTable().isEditing() && this.dialog.getParametersTable().getCellEditor() != null) {
                this.dialog.getParametersTable().getCellEditor().stopCellEditing();
            }

            int primaryIndex = 0;
            ParameterSet model = (ParameterSet) dialog.getParametersTable().getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                if ((boolean) model.getValueAt(i, 2)) {
                    primaryIndex = model.getParameterRows().get(i).getColumnIndex();
                }
            }

            ArrayList<Integer> priorities = new ArrayList<>();
            for (ClusteringTableParameter parameterRow : model.getParameterRows()) {
                priorities.add(parameterRow.getPriority());
            }
            dialog.setThreshold();

            //load distinct categories
            for (int i = 0; i < dataSheet.getParameterCount(); i++) {
                dataSheet.getParameter(i).loadDistinctCategories();
            }

            Vertex root = new Vertex(-1, "", null);
            for (int i = 0; i < dataSheet.getDesignCount(); i++) {
                root.proccessItem(0, dataSheet.getDesign(i).getListOfStringValues(dataSheet).size(), i, dataSheet.getDesign(i).getCompleteListOfStringValues(dataSheet));
            }
            //root.toString("");
            Parameter primaryAttribute = dataSheet.getParameter(primaryIndex);

            ArrayList<TerminalVertex>[] divisionByCluster = new ArrayList[primaryAttribute.getCategories().size()];
            for (int i = 0; i < divisionByCluster.length; i++) {
                divisionByCluster[i] = new ArrayList<>();
            }
            Queue<AbstractVertex> queue = new ArrayDeque<>();
            queue.add(root);
            AbstractVertex vertex, tmp;
            while (!queue.isEmpty()) {
                vertex = queue.poll();
                if (vertex.isTerminal()) {
                    tmp = vertex;
                    while (tmp.getAttributeIndex() != primaryIndex) {
                        tmp = tmp.getPredecessor();
                    }
                    divisionByCluster[primaryAttribute.getCategories().indexOf(tmp.getValue())].add((TerminalVertex) vertex);
                } else {
                    queue.addAll(((Vertex) vertex).getSuccessors());
                }
            }

            /*for (int i = 0; i < divisionByCluster.length; i++) {
                System.out.println("Cluster " + i);
                for (TerminalVertex terminalVertex : divisionByCluster[i]) {
                    terminalVertex.selfToString();
                }
            }*/
            //---------------------------------------------------------------------------------------------------------------------------------------------------
            int[] centroids = new int[divisionByCluster.length];
            ArrayList<TerminalVertex> terminalVertices = new ArrayList<>();
            for (int i = 0; i < divisionByCluster.length; i++) {
                int maxCount = Integer.MIN_VALUE;
                terminalVertices.addAll(divisionByCluster[i]);
                for (int j = 0; j < divisionByCluster[i].size(); j++) {
                    if (maxCount < divisionByCluster[i].get(j).getIndices().size()) {
                        maxCount = divisionByCluster[i].get(j).getIndices().size();
                        centroids[i] = j;
                    }
                }
            }
            double[][] distancesToCentroids = new double[centroids.length][terminalVertices.size()];
            for (int i = 0; i < distancesToCentroids.length; i++) {
                for (int j = 0; j < terminalVertices.size(); j++) {
                    int centroidItemIndex = divisionByCluster[i].get(centroids[i]).getIndices().get(0);
                    int parallelogramItemIndex = terminalVertices.get(j).getIndices().get(0);
                    if (dataSheet.getDesign(centroidItemIndex) == null || dataSheet.getDesign(parallelogramItemIndex) == null) {
                        distancesToCentroids[i][j] = 1;
                    } else {
                        distancesToCentroids[i][j] = dataSheet.getDesign(centroidItemIndex).getDistance(dataSheet.getDesign(parallelogramItemIndex), priorities, dataSheet);
                    }
                }
            }
            for (int i = 0; i < terminalVertices.size(); i++) {
                for (int j = 0; j < centroids.length; j++) {
                    System.out.print(distancesToCentroids[j][i] + " ");
                }
                System.out.println("");
            }

            Parameter cluster = new Parameter("Cluster", dataSheet, false);
            for (int i = 0; i <= centroids.length; i++) {
                cluster.addNonNumericValue("c" + i);
            }
            cluster.addNonNumericValue(actionCommand);
            dataSheet.addParameter(cluster);

            System.out.println("The threshold is " + dialog.getThreshold());

            for (int i = 0; i < terminalVertices.size(); i++) {
                int clusterIndex = -1;
                double min = Double.MAX_VALUE;
                for (int j = 0; j < centroids.length; j++) {
                    if (distancesToCentroids[j][i] <= dialog.getThreshold() && min > distancesToCentroids[j][i]) {
                        min = distancesToCentroids[j][i];
                        clusterIndex = j;
                    }
                }
                if (clusterIndex == -1) {
                    for (Integer index : terminalVertices.get(i).getIndices()) {
                        dataSheet.getDesign(index).addParameter(cluster, "c_unassigned");
                    }
                } else {
                    for (Integer index : terminalVertices.get(i).getIndices()) {
                        dataSheet.getDesign(index).addParameter(cluster, "c" + clusterIndex);
                    }
                }
            }

            mainWindow.initialiseDataPanel();
            //---------------------------------------------------------------------------------------------------------------------------------------------------

            dialog.setVisible(false);
            dialog.dispose();
        }
    }

    /**
     * Prints debug information to stdout when printLog is set to true.
     *
     * @param message the message
     */
    private void log(String message) {
        if (ClusterUsingParallelogramsDialogActionListener.printLog && Main.isLoggingEnabled()) {
            System.out.println(this.getClass().getName() + "." + message);
        }
    }
}

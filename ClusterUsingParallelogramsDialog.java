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
package org.xdat.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.xdat.Main;
import org.xdat.actionListeners.clusteringParallelograms.ClusterUsingParallelogramsDialogActionListener;
import org.xdat.data.DataSheet;
import org.xdat.gui.WindowClosingAdapter;
import org.xdat.gui.panels.TitledSubPanel;
import org.xdat.gui.tables.GenericTableColumnModel;

/**
 * Dialog to edit the settings for file import, such as delimiters and default
 * browsing location.
 */
public class ClusterUsingParallelogramsDialog extends JDialog {

    /**
     * The version tracking unique identifier for Serialization.
     */
    static final long serialVersionUID = 0001;

    /**
     * Flag to enable debug message printing for this class.
     */
    private static final boolean printLog = false;

    /**
     * The main window.
     */
    private Main mainWindow;

    /**
     * The data sheet.
     */
    private DataSheet dataSheet;

    /**
     * The table to display parameters.
     */
    private JTable parametersTable = new JTable();

    private double threshold;

    private JSlider slider;

    /**
     * Instantiates a new file import settings dialog.
     *
     * @param mainWindow the main window
     * @throws HeadlessException the headless exception
     */
    public ClusterUsingParallelogramsDialog(Main mainWindow, DataSheet dataSheet) throws HeadlessException {
        super(mainWindow, "Cluster Using Parallelograms", true);
        threshold = 0;

        log("constructor called");
        this.addWindowListener(new WindowClosingAdapter(false));
        this.setResizable(false);
        this.mainWindow = mainWindow;
        this.dataSheet = dataSheet;

        this.setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel(new BorderLayout());
        TitledSubPanel sliderPanel = new TitledSubPanel("Set threshold");
        sliderPanel.setLayout(new BorderLayout());
        TitledSubPanel tablePanel = new TitledSubPanel("Set attribute priorities and select primary attribute");
        tablePanel.setLayout(new BorderLayout());
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2));

        slider = new JSlider( 0, 10);
        Hashtable labelTable = new Hashtable();
        labelTable.put(0, new JLabel("0.0"));
        labelTable.put(1, new JLabel("0.1"));
        labelTable.put(2, new JLabel("0.2"));
        labelTable.put(3, new JLabel("0.3"));
        labelTable.put(4, new JLabel("0.4"));
        labelTable.put(5, new JLabel("0.5"));
        labelTable.put(6, new JLabel("0.6"));
        labelTable.put(7, new JLabel("0.7"));
        labelTable.put(8, new JLabel("0.8"));
        labelTable.put(9, new JLabel("0.9"));
        labelTable.put(10, new JLabel("1.0"));
        slider.setLabelTable(labelTable);
        slider.setMajorTickSpacing(5);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setValue(0);

        GenericTableColumnModel cm = new GenericTableColumnModel();
        TableColumn paramCol = new TableColumn(0, 100);
        paramCol.setHeaderValue("Parameter");
        paramCol.setResizable(true);
        cm.addColumn(paramCol);

        TableColumn priorityCol = new TableColumn(1, 50);
        priorityCol.setHeaderValue("Priority");
        priorityCol.setResizable(true);
        cm.addColumn(priorityCol);

        TableColumn primaryCol = new TableColumn(2, 75);
        primaryCol.setHeaderValue("Primary Target");
        primaryCol.setResizable(true);
        cm.addColumn(primaryCol);


        this.parametersTable = new JTable(dataSheet.getParameterSet(), cm);
        JScrollPane scrollPane = new JScrollPane(this.parametersTable);

        JButton cancelButton = new JButton("Cancel");
        JButton okButton = new JButton("Ok");

        ClusterUsingParallelogramsDialogActionListener cmd = new ClusterUsingParallelogramsDialogActionListener(mainWindow, this, dataSheet);
        cancelButton.addActionListener(cmd);
        okButton.addActionListener(cmd);

        // create components
        // set Layouts
        this.add(mainPanel, BorderLayout.CENTER);
        mainPanel.add(sliderPanel, BorderLayout.NORTH);
        sliderPanel.add(slider);

        mainPanel.add(tablePanel, BorderLayout.CENTER);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(okButton);

        // pack
        this.pack();

        // set location and make visible
        int left = (int) (0.5 * this.mainWindow.getSize().width) - (int) (this.getSize().width * 0.5) + this.mainWindow.getLocation().x;
        int top = (int) (0.5 * this.mainWindow.getSize().height) - (int) (this.getSize().height * 0.5) + this.mainWindow.getLocation().y;
        this.setLocation(left, top);
        this.setVisible(true);

    }

    /**
     * Prints debug information to stdout when printLog is set to true.
     *
     * @param message the message
     */
    private static final void log(String message) {
        if (ClusterUsingParallelogramsDialog.printLog && Main.isLoggingEnabled()) {
            System.out.println("ChartFrameSettingsDialog" + message);
        }
    }

    public JTable getParametersTable() {
        return parametersTable;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold() {
        this.threshold = ((double) slider.getValue()) / 10;
    }

}

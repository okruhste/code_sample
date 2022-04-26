package org.xdat.actionListeners.mainMenu;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;

import org.xdat.Main;
import org.xdat.chart.ScatterChart2D;
import org.xdat.exceptions.NoParametersDefinedException;
import org.xdat.gui.dialogs.ClusterUsingParallelogramsDialog;
import org.xdat.gui.dialogs.FileImportSettingsDialog;
import org.xdat.gui.frames.ChartFrame;
import org.xdat.gui.menus.mainWIndow.MainChartMenu;
import org.xdat.workerThreads.ParallelCoordinatesChartCreationThread;
import org.xdat.workerThreads.ParallelSetsChartCreationThread;

/**
 * ActionListener for a {@link MainClusterMenu}.
 */
public class MainClusterMenuActionListener implements ActionListener {

	/** The main window. */
	private Main mainWindow;

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/**
	 * Instantiates a new main chart menu action listener.
	 * 
	 * @param mainWindow
	 *            the main window
	 */
	public MainClusterMenuActionListener(Main mainWindow) {
		log("constructor called");
		this.mainWindow = mainWindow;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Cluster Using Parallelograms")) {
			// log("Create: discrete level count of last parameter is: "+mainWindow.getDataSheet().getParameter(mainWindow.getDataSheet().getParameterCount()-1).getDiscreteLevelCount());
			if (mainWindow.getDataSheet() == null) {
				JOptionPane.showMessageDialog(mainWindow, "Please create a data sheet first by selecting Data->Import.", "Create Chart", JOptionPane.INFORMATION_MESSAGE);
			} else {
				new ClusterUsingParallelogramsDialog(this.mainWindow, this.mainWindow.getDataSheet());
			}
		} else {
			System.out.println(e.getActionCommand());
		}
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (MainClusterMenuActionListener.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}

}

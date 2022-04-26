package org.xdat.gui.menus.mainWIndow;

import java.awt.Event;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.xdat.Main;
import org.xdat.actionListeners.mainMenu.MainClusterMenuActionListener;

/**
 * Chart menu for the {@link org.xdat.Main} window.
 */
public class MainClusterMenu extends JMenu {
	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0001;

	/** The create parallel coordinates chart menu item. */
	private JMenuItem clusterUsingParallelograms = new JMenuItem("Cluster Using Parallelograms", 'p');


	/**
	 * Instantiates a new main chart menu.
	 * 
	 * @param mainWindow
	 *            the main window
	 */
	public MainClusterMenu(Main mainWindow) {
		super("Cluster");
		this.setMnemonic(KeyEvent.VK_C);
		MainClusterMenuActionListener cmd = new MainClusterMenuActionListener(mainWindow);
	
		clusterUsingParallelograms.setMnemonic(KeyEvent.VK_P);
		clusterUsingParallelograms.addActionListener(cmd);
		clusterUsingParallelograms.setEnabled(false);
		this.add(clusterUsingParallelograms);


		this.setItemsRequiringDataSheetEnabled(false);
	}

	/**
	 * Specifies whether the menu item createMenuItem is enabled. This is
	 * required because this item is only available when data is loaded.
	 * 
	 * @param enabled
	 *            specifies whether the menu item createMenuItem is enabled.
	 */
	public void setItemsRequiringDataSheetEnabled(boolean enabled) {
		this.clusterUsingParallelograms.setEnabled(enabled);
	}

	/**
	 * Sets the ctrl accelerator.
	 * 
	 * @param mi
	 *            the menu item
	 * @param acc
	 *            the accelerator
	 */
	private void setCtrlAccelerator(JMenuItem mi, char acc) {
		KeyStroke ks = KeyStroke.getKeyStroke(acc, Event.CTRL_MASK);
		mi.setAccelerator(ks);
	}

}

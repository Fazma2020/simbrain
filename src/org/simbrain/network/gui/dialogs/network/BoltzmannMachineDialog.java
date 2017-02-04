/*
 * Part of Simbrain--a java-based neural network kit
 * Copyright (C) 2005,2007 The Authors.  See http://www.simbrain.net/credits
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.simbrain.network.gui.dialogs.network;

import org.simbrain.network.gui.NetworkPanel;
import org.simbrain.network.gui.dialogs.layout.MainLayoutPanel;
import org.simbrain.network.subnetworks.BoltzmannMachine;
import org.simbrain.util.StandardDialog;
import org.simbrain.util.widgets.ShowHelpAction;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * <b>BoltzmannMachineDialog</b> is used as an assistant to create Boltzmann networks.
 */
public class BoltzmannMachineDialog extends StandardDialog {

    /** Tabbed pane. */
    private JTabbedPane tabbedPane = new JTabbedPane();

    /** Logic tab panel. */
    private JPanel tabLogic = new JPanel();

    /** Layout tab panel. */
    private JPanel tabLayout = new JPanel();

    /** Boltzmann properties panel. */
    private BoltzmannPropertiesPanel boltzmannPanel;

    /** Layout panel. */
    private MainLayoutPanel layoutPanel;

    /** Network Panel. */
    private NetworkPanel networkPanel;

    /**
     * This method is the default constructor.
     *
     * @param networkPanel Network panel
     */
    public BoltzmannMachineDialog(final NetworkPanel networkPanel) {
        this.networkPanel = networkPanel;
        layoutPanel = new MainLayoutPanel(false, this);
        init();
    }

    /**
     * Initializes all components used in dialog.
     */
    private void init() {

        setTitle("New Boltzmann Machine");

        boltzmannPanel = new BoltzmannPropertiesPanel(networkPanel);

        // Set up tab panels
        tabLogic.add(boltzmannPanel);
        layoutPanel = new MainLayoutPanel(false, this);
        //TODO: define layout
//        layoutPanel.setCurrentLayout(SOMGroup.DEFAULT_LAYOUT);
        tabLayout.add(layoutPanel);
        tabbedPane.addTab("Logic", tabLogic);
        tabbedPane.addTab("Layout", layoutPanel);
        setContentPane(tabbedPane);

        // Help action
        Action helpAction = new ShowHelpAction(boltzmannPanel.getHelpPath());
        addButton(new JButton(helpAction));

    }

    /**
     * Called when dialog closes.
     */
    @Override
    protected void closeDialogOk() {
        boltzmannPanel.commitChanges();
        BoltzmannMachine boltzmannMachine =  (BoltzmannMachine) boltzmannPanel.getGroup();
        layoutPanel.commitChanges();

        //TODO: do we need to set the layout?
//        som.setLayout(layoutPanel.getCurrentLayout());
//        som.applyLayout();
//        somNet.layoutNetwork(); // Must layout som first
        //networkPanel.getNetwork().addGroup(boltzmannMachine); //this gives NPE as BoltzmannPropertiesPanel.getGroup() returns null at present
        super.closeDialogOk();
    }

}

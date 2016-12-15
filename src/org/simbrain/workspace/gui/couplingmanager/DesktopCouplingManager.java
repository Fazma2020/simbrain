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
package org.simbrain.workspace.gui.couplingmanager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Type;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.simbrain.util.genericframe.GenericFrame;
import org.simbrain.util.genericframe.GenericJInternalFrame;
import org.simbrain.util.widgets.ShowHelpAction;
import org.simbrain.workspace.Consumer2;
import org.simbrain.workspace.MismatchedAttributesException;
import org.simbrain.workspace.Producer2;
import org.simbrain.workspace.WorkspaceComponent;
import org.simbrain.workspace.gui.CouplingListPanel;
import org.simbrain.workspace.gui.SimbrainDesktop;
import org.simbrain.workspace.gui.couplingmanager.PotentialAttributePanel.ProducerOrConsumer;

/**
 * GUI dialog for creating couplings.
 */
public class DesktopCouplingManager extends JPanel implements ActionListener {

    //TODO: Rename these fields
    /** List of producing attributes. */
    private PotentialAttributePanel producingAttributes;

    /** List of consuming attributes. */
    private PotentialAttributePanel consumingAttributes;

    /** Methods for making couplings. */
    private String[] tempStrings = { "One to one", "One to many" };

    /** Methods for making couplings. */
    private JComboBox couplingMethodComboBox = new JComboBox(tempStrings);

    /** Reference to desktop. */
    private SimbrainDesktop desktop;

    /** Reference of parent frame. */
    private final GenericFrame frame;

    /**
     * Creates and displays the coupling manager.
     *
     * @param desktop reference to parent desktop
     * @param frame reference to parent frame
     */
    public DesktopCouplingManager(final SimbrainDesktop desktop,
            final GenericJInternalFrame frame) {
        super(new BorderLayout());
        this.desktop = desktop;
        this.frame = frame;

        // Left Panel
        Border leftBorder = BorderFactory.createTitledBorder("Producers");
        producingAttributes = new PotentialAttributePanel(
                desktop.getWorkspace(), ProducerOrConsumer.Producing);
        producingAttributes.setBorder(leftBorder);

        // Right Panel
        Border rightBorder = BorderFactory.createTitledBorder("Consumers");
        consumingAttributes = new PotentialAttributePanel(
                desktop.getWorkspace(), ProducerOrConsumer.Consuming);
        consumingAttributes.setBorder(rightBorder);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        bottomPanel.add(new JButton(new ShowHelpAction(
                "Pages/Workspace/Couplings.html")));

        bottomPanel.add(couplingMethodComboBox);

        JButton addCouplingsButton = new JButton("Add Coupling(s)");
        addCouplingsButton.setActionCommand("addCouplings");
        addCouplingsButton.addActionListener(this);
        bottomPanel.add(addCouplingsButton);

        JButton okButton = new JButton("OK");
        okButton.setActionCommand("ok");
        okButton.addActionListener(this);
        bottomPanel.add(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(this);
        bottomPanel.add(cancelButton);

        JComponent couplingList = new CouplingListPanel(desktop,
                desktop.getWorkspace().getCouplings());
        couplingList.setBorder(BorderFactory.createTitledBorder("Couplings"));

        // Main Panel
        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        centerPanel.add(producingAttributes);
        centerPanel.add(couplingList);
        centerPanel.add(consumingAttributes);
        centerPanel.setPreferredSize(new Dimension(800, 400));
        this.add("Center", centerPanel);
        this.add("South", bottomPanel);

        frame.getRootPane().setDefaultButton(okButton);

        frame.pack();

    }

    /**
     * @see ActionListener
     * @param event to listen.
     */
    public void actionPerformed(final ActionEvent event) {

        // Refresh component lists
        if (event.getSource() instanceof JComboBox) {
            WorkspaceComponent component = (WorkspaceComponent) ((JComboBox) event
                    .getSource()).getSelectedItem();
        }

        // Handle Button Presses
        if (event.getSource() instanceof JButton) {
            JButton button = (JButton) event.getSource();
            if (button.getActionCommand().equalsIgnoreCase("addCouplings")) {
                addCouplings();
            } else if (button.getActionCommand().equalsIgnoreCase("ok")) {
                frame.dispose();
            } else if (button.getActionCommand().equalsIgnoreCase("cancel")) {
                frame.dispose();
            }
        }
    }

    /**
     * Add couplings using the selected method.
     */
    private void addCouplings() {

        List<Producer2> potentialProducers = (List<Producer2>) producingAttributes
                .getSelectedAttributes();
        List<Consumer2> potentialConsumers = (List<Consumer2>) consumingAttributes
                .getSelectedAttributes();

        if ((potentialProducers.size() == 0)
                || (potentialConsumers.size() == 0)) {
            JOptionPane
                    .showMessageDialog(
                            null,
                            "You must select at least one consuming and producing attribute \n in order to create couplings!",
                            "No Attributes Selected Warning",
                            JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            desktop.getWorkspace().coupleOneToOne(potentialProducers,
                    potentialConsumers);
//            if (((String) couplingMethodComboBox.getSelectedItem())
//                    .equalsIgnoreCase("One to one")) {
//                desktop.getWorkspace().coupleOneToOne(potentialProducers,
//                        potentialConsumers);
//            } else if (((String) couplingMethodComboBox.getSelectedItem())
//                    .equalsIgnoreCase("One to many")) {
//                desktop.getWorkspace().coupleOneToMany(potentialProducers,
//                        potentialConsumers);
//            }
        } catch (MismatchedAttributesException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(),
                    "Unmatched Attributes", JOptionPane.WARNING_MESSAGE, null);

        }
    }

    /**
     * Associates attribute and coupling data types (classes) with colors used
     * in displaying attributes and couplings.
     *
     * @param dataType the data type to associate with a color
     * @return the color associated with a data type
     */
    public static Color getColor(Type dataType) {
        if (dataType == double.class) {
            return Color.black;
        } else if (dataType == double[].class) {
            return Color.green.darker().darker();
        } else if (dataType == String.class) {
            return Color.blue.brighter();
        }
        return Color.black;
    }
}

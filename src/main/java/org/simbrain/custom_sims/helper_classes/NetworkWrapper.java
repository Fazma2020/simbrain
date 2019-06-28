package org.simbrain.custom_sims.helper_classes;

import org.simbrain.network.NetworkComponent;
import org.simbrain.network.connections.AllToAll;
import org.simbrain.network.connections.ConnectionStrategy;
import org.simbrain.network.connections.OneToOne;
import org.simbrain.network.core.*;
import org.simbrain.network.desktop.NetworkDesktopComponent;
import org.simbrain.network.desktop.NetworkPanelDesktop;
import org.simbrain.network.groups.Group;
import org.simbrain.network.groups.NeuronGroup;
import org.simbrain.network.groups.SynapseGroup;
import org.simbrain.network.gui.nodes.NeuronNode;
import org.simbrain.network.layouts.GridLayout;
import org.simbrain.network.layouts.LineLayout;
import org.simbrain.network.layouts.LineLayout.LineOrientation;
import org.simbrain.network.neuron_update_rules.LinearRule;
import org.simbrain.network.subnetworks.WinnerTakeAll;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A wrapper for a {@link Network}, with access to the {@link NetworkComponent}, and {@link
 * org.simbrain.network.gui.NetworkPanel} which can be used to set the position of a window in the Simbrain desktop.
 * Helper methods for creating neurons, neuron groups, etc. are also included.
 */
public class NetworkWrapper {

    /**
     * The desktop component with full graphical access.
     */
    private final NetworkDesktopComponent desktopComponent;

    /**
     * The network component.
     */
    private final NetworkComponent networkComponent;

    /**
     * The logical network.
     */
    private final Network network;

    /**
     * Grid spacing for methods that use a grid layout.
     */
    private double GRID_SPACE = 50; // todo; make this settable

    /**
     * Create an instance of the wrapper.
     */
    public NetworkWrapper(NetworkDesktopComponent desktopComponent) {
        this.desktopComponent = desktopComponent;
        this.networkComponent = desktopComponent.getWorkspaceComponent();
        this.network = networkComponent.getNetwork();
    }

    /**
     * Add a neuron at a specified location.
     */
    public Neuron addNeuron(int x, int y) {
        Neuron neuron = new Neuron(network, "LinearRule");
        neuron.setLocation(x, y);
        network.addNeuron(neuron);
        return neuron;
    }

    /**
     * Add neurons at a specified location with a specified layout.
     */
    public void addNeurons(int x, int y, int numNeurons, String layoutName, String type) {

        List<Neuron> newNeurons = new ArrayList();
        for (int i = 0; i < numNeurons; i++) {
            Neuron neuron = new Neuron(network, type);
            network.addNeuron(neuron);
            newNeurons.add(neuron);
        }

        // Lay out neurons
        if (layoutName.equalsIgnoreCase("none")) {
            return;
        }
        if (layoutName.toLowerCase().contains("line")) {
            if (layoutName.equalsIgnoreCase("vertical line")) {
                LineLayout lineLayout = new LineLayout(x, y, 50, LineOrientation.VERTICAL);
                lineLayout.layoutNeurons(newNeurons);

            } else {
                LineLayout lineLayout = new LineLayout(x, y, 50, LineOrientation.HORIZONTAL);
                lineLayout.layoutNeurons(newNeurons);
            }
        } else if (layoutName.equalsIgnoreCase("grid")) {
            GridLayout gridLayout = new GridLayout(GRID_SPACE, GRID_SPACE, (int) Math.sqrt(numNeurons));
            gridLayout.setInitialLocation(new Point(x, y));
            gridLayout.layoutNeurons(newNeurons);
        }
    }

    /**
     * Make a single source -> target neuron connection with specified upper and lower bounds for the synapses.
     */
    public void connect(Neuron source, Neuron target, double value, double lowerBound, double upperBound) {
        Synapse synapse = new Synapse(source, target);
        synapse.setStrength(value);
        synapse.setLowerBound(lowerBound);
        synapse.setUpperBound(upperBound);
        source.getNetwork().addSynapse(synapse);
    }

    /**
     * Make a single source -> target neuron connection.
     *
     * @param source the source neuron
     * @param target the target neuron
     */
    public Synapse connect(Neuron source, Neuron target, double value) {
        Synapse synapse = new Synapse(source, target);
        synapse.setStrength(value);
        source.getNetwork().addSynapse(synapse);
        return synapse;
    }

    /**
     * Connect source to target with a provided learning rule and value.
     *
     * @return the new synapse
     */
    public Synapse connect(Neuron source, Neuron target, SynapseUpdateRule rule, double value) {
        Synapse synapse = new Synapse(source, target, rule);
        synapse.setStrength(value);
        source.getNetwork().addSynapse(synapse);
        return synapse;
    }

    /**
     * Connect a source and neuron target group all to all
     *
     * @return the new synapses
     */
    public List<Synapse> connectAllToAll(NeuronGroup source, NeuronGroup target) {
        AllToAll connector = new AllToAll();
        return connector.connectAllToAll(source.getNeuronList(), target.getNeuronList());
    }

    /**
     * Connect a source neuron group to a single target neuron
     */
    public void connectAllToAll(NeuronGroup inputs, Neuron target) {
        AllToAll connector = new AllToAll();
        connector.connectAllToAll(inputs.getNeuronList(), Collections.singletonList(target));
    }

    /**
     * Connect source and target neuron groups with a provided connection strategy.
     *
     * @return the new synapses
     */
    public List<Synapse> connect(NeuronGroup source, NeuronGroup target, ConnectionStrategy connector) {
        return connector.connectNeurons(network, source.getNeuronList(), target.getNeuronList());
    }

    /**
     * Add a synapse group between a source and target neuron group
     *
     * @return the new synapse group
     */
    public SynapseGroup addSynapseGroup(NeuronGroup source, NeuronGroup target) {
        SynapseGroup sg = SynapseGroup.createSynapseGroup(source, target);
        network.addGroup(sg);
        return sg;
    }

    /**
     * Add a neuron group at the specified location, with a specified number of neurons, layout,
     * and update rule.
     *
     * @return the new neuron group
     */
    public NeuronGroup addNeuronGroup(double x, double y, int numNeurons, String layoutName, NeuronUpdateRule rule) {

        NeuronGroup ng;
        ng = new NeuronGroup(network, new Point2D.Double(x, y), numNeurons);
        ng.setNeuronType(rule);

        layoutNeuronGroup(ng, x, y, layoutName);
        network.addGroup(ng);
        return ng;

    }

    /**
     * Add a neuron group with a specified number of neurons, layout, and neuron update rule
     *
     * @return the new neuron group
     */
    public NeuronGroup addNeuronGroup(double x, double y, int numNeurons, String layoutName, String type) {
        return addNeuronGroup(x, y, numNeurons, layoutName, new LinearRule());
    }

    /**
     * Add a new neuron group at the specified location, with a default line layout and linear neurons.
     *
     * @return the new neuron group.
     */
    public NeuronGroup addNeuronGroup(double x, double y, int numNeurons) {
        //TODO: Setting location not always working
        return addNeuronGroup(x, y, numNeurons, "line", "LinearRule");
    }

    /**
     * Layout a neuron group.
     *
     * @param ng reference to the group
     * @param x reference x location (upper left)
     * @param y reference y location (upper left)
     * @param layoutName the type of layout to use: "line" (defaults to horizontal),
     *                   "vertical line", or "grid".  TODO: Add hex.
     */
    private void layoutNeuronGroup(NeuronGroup ng, double x, double y, String layoutName) {

        if (layoutName.toLowerCase().contains("line")) {
            if (layoutName.equalsIgnoreCase("vertical line")) {
                LineLayout lineLayout = new LineLayout(x, y, 50, LineOrientation.VERTICAL);
                ng.setLayout(lineLayout);
            } else {
                LineLayout lineLayout = new LineLayout(x, y, 50, LineOrientation.HORIZONTAL);
                ng.setLayout(lineLayout);
            }
        } else if (layoutName.equalsIgnoreCase("grid")) {
            GridLayout gridLayout = new GridLayout(GRID_SPACE, GRID_SPACE, (int) Math.sqrt(ng.size()));
            ng.setLayout(gridLayout);
        }
        ng.applyLayout();

    }

    /**
     * Add a {@link WinnerTakeAll} network at the specified location.
     *
     * @return the new network
     */
    public WinnerTakeAll addWTAGroup(double x, double y, int numNeurons) {
        WinnerTakeAll wta = new WinnerTakeAll(network, numNeurons);
        wta.setLocation(x, y);
        layoutNeuronGroup(wta, x, y, "line");
        return wta;
    }

    /**
     * Get a reference to a graphical {@link NeuronNode}.
     */
    public NeuronNode getNode(Neuron neuron) {
        return getNetworkPanel().getNode(neuron);
    }

    /**
     * Get a reference to the logical network object
     */
    public Network getNetwork() {
        return network;
    }

    /**
     * Get a reference to the network component.
     */
    public NetworkComponent getNetworkComponent() {
        return networkComponent;
    }

    /**
     * Get a reference to the network panel.
     */
    public NetworkPanelDesktop getNetworkPanel() {
        return (NetworkPanelDesktop) desktopComponent.getNetworkPanel();
    }


}
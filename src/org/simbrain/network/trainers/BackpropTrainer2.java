///*
// * Part of Simbrain--a java-based neural network kit
// * Copyright (C) 2005,2007 The Authors.  See http://www.simbrain.net/credits
// *
// * This program is free software; you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation; either version 2 of the License, or
// * (at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public License
// * along with this program; if not, write to the Free Software
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
// */
//package org.simbrain.network.trainers;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.concurrent.ThreadLocalRandom;
//
//import org.jblas.DoubleMatrix;
//import org.simbrain.network.groups.NeuronGroup;
//import org.simbrain.network.groups.SynapseGroup;
//import org.simbrain.network.neuron_update_rules.TransferFunction;
//import org.simbrain.network.subnetworks.BackpropNetwork;
//import org.simbrain.network.neuron_update_rules.interfaces.BiasedUpdateRule;
//import org.simbrain.util.math.ProbDistribution;
//import org.simbrain.util.propertyeditor.ComboBoxWrapper;
//import org.simbrain.util.randomizer.Randomizer;
//
///**
// * Array-backed backprop, currently using JBlas.
// *
// * 50-50 co-authors
// *
// * @author Jeff Yoshimi
// * @author Zoë Tosi
// */
//public class BackpropTrainer2 extends IterableTrainer {
//
//    /** Current error. */
//    private double mse;
//
//    /** Default learning rate. */
//    private static final double DEFAULT_LEARNING_RATE = .1;
//
//    /** Learning rate. */
//    private double learningRate = DEFAULT_LEARNING_RATE;
//
//    /** Default momentum. */
//    private static final double DEFAULT_MOMENTUM = .9;
//
//    /** Momentum. Must be between 0 and 1. */
//    private double momentum = DEFAULT_MOMENTUM;
//
//    /** The backprop network to be trained. */
//    private BackpropNetwork net;
//
//    /** Weight matrices ordered input to output. */
//    private List<DoubleMatrix> weightMatrices = new ArrayList<DoubleMatrix>();
//    private List<SynapseGroup> synGrps = new ArrayList<SynapseGroup>();
//
//    /** Memory of last weight updates for momentum. */
//    private List<DoubleMatrix> lastWeightUpdates = new ArrayList<DoubleMatrix>();
//
//    /** Memory of last bias updates for momentum. */
//    private List<DoubleMatrix> lastBiasUpdates = new ArrayList<DoubleMatrix>();
//
//    /** Activation vectors. */
//    private List<DoubleMatrix> layers = new ArrayList<DoubleMatrix>();
//    private List<NeuronGroup> ngroups = new ArrayList<NeuronGroup>();
//
//    /** Net inputs. */
//    private List<DoubleMatrix> netInputs = new ArrayList<DoubleMatrix>();
//
//    /** Biases. */
//    private List<DoubleMatrix> biases = new ArrayList<DoubleMatrix>();
//
//    /** Error. */
//    private List<DoubleMatrix> errors = new ArrayList<DoubleMatrix>();
//
//    /**
//     * Input layer. Holds current input vector from input dataset. Separate for
//     * simpler indexing on other lists.
//     */
//    private DoubleMatrix inputLayer;
//
//    /** Current target vector. */
//    private DoubleMatrix targetVector;
//
//    /** Inputs. */
//    private DoubleMatrix inputData;
//
//    /** Targets. */
//    private DoubleMatrix targetData;
//
//    /** Parameter randomizer. */
//    // TODO: Make proper GUI Link
//    public Randomizer rand = new Randomizer();
//
//    /** List of activation functions for easy reference. */
//    private List<TransferFunction> updateRules = new ArrayList<TransferFunction>();
//
//    /** Possible update methods. */
//    public static enum UpdateMethod {
//        EPOCH, BATCH, STOCHASTIC, MINI_BATCH;
//    }
//
//    /** Current update method. */
//    private UpdateMethod updateMethod = UpdateMethod.EPOCH;
//
//    /**
//     * Construct the trainer.
//     *
//     * @param network the network to train
//     */
//    public BackpropTrainer2(final Trainable network) {
//        super(network);
//
//        if (!(network instanceof BackpropNetwork)) {
//            throw new IllegalArgumentException(
//                    "Backprop trainer must be applied to backprop network");
//        }
//        net = (BackpropNetwork) network;
//
//        // Synapse group list is ordered from input to output layers
//        for (SynapseGroup sg : net.getSynapseGroupList()) {
//
//            DoubleMatrix weights = new DoubleMatrix(sg.getWeightMatrix())
//                    .transpose();
//            weightMatrices.add(weights);
//            lastWeightUpdates
//                    .add(DoubleMatrix.zeros(weights.rows, weights.columns));
//            synGrps.add(sg);
//        }
//
//        // Initialize layers
//        int ii = 0;
//        for (NeuronGroup ng : net.getNeuronGroupList()) {
//            if (ii > 0) {
//                layers.add(DoubleMatrix.zeros(ng.size()));
//                netInputs.add(DoubleMatrix.zeros(ng.size()));
//                errors.add(DoubleMatrix.zeros(ng.size()));
//                DoubleMatrix bs = new DoubleMatrix(ng.getBiases());
//                biases.add(bs);
//                lastBiasUpdates.add(DoubleMatrix.zeros(bs.rows, bs.columns));
//                updateRules.add((TransferFunction) ng.getNeuronList().get(0)
//                        .getUpdateRule());
//                ngroups.add(ng);
//            } else {
//                inputLayer = DoubleMatrix.zeros(ng.size());
//            }
//            ii++;
//        }
//
//        // Initialize randomizer
//        rand.setPdf(ProbDistribution.UNIFORM);
//        rand.setParam1(.9);
//        rand.setParam2(1);
//    }
//
//    @Override
//    public void apply() {
//
//        int numTrainingExamples = getMinimumNumRows(network);
//
//        // System.out.println("=== Before: ===\n");
//        // printDebugInfo();
//
//        // One "iteration" of the network according to some method
//        mse = 0;
//        if (updateMethod == UpdateMethod.EPOCH) {
//            for (int row = 0; row < numTrainingExamples; row++) {
//                mse += updateBackprop(row);
//            }
//            mse = mse / numTrainingExamples;
//        } else if (updateMethod == UpdateMethod.STOCHASTIC) {
//            int rowNum = ThreadLocalRandom.current()
//                    .nextInt(numTrainingExamples);
//            mse = updateBackprop(rowNum);
//        }
//        // TODO: Other update types
//
//        // System.out.println("\n\n=== After: ===\n");
//        // printDebugInfo();
//
//        incrementIteration();
//        fireErrorUpdated();
//    }
//
//    /**
//     * Backpropagate error on indicated row of dataset.
//     *
//     * @param rowNum row of dataset to update.
//     * @return mean squared error relative to outputs
//     */
//    private double updateBackprop(final int rowNum) {
//
//        inputLayer = inputData.getColumn(rowNum);
//
//        // Update network
//        updateNetwork();
//
//        // Backpropagate error
//        targetVector = targetData.getColumn(rowNum);
//        DoubleMatrix outputError = errors.get(errors.size() - 1);
//        targetVector.subi(getOutputLayer(), outputError);
//
//        // In place subtraction with the result being stored in outputError
//        // means that the DoubleMatrix object in the list represented by
//        // "outputError" does not have to be reset. It's still there, but
//        // now with different entries.
//        // errors.set(errors.size() - 1, outputError);
//
//        backpropagateError();
//
//        // Update weights and biases
//        updateParameters();
//
//        // TODO: Below has been modified for Mazur test
//
//        // Update MSE
//        // TODO: Settable error function
//        double error = 0;
//        for (int j = 0; j < outputError.length; j++) {
//            error += (outputError.get(j) * outputError.get(j));
//        }
//        mse = error / network.getOutputNeurons().size();
//        return mse;
//
//    }
//
//    /**
//     * Update the array-based "shadow" network.
//     */
//    private void updateNetwork() {
//
//        int ii = 0;
//        for (DoubleMatrix wm : weightMatrices) {
//
//            // Set up variables for easy reading
//            DoubleMatrix inputs;
//            if (ii == 0) {
//                inputs = inputLayer;
//            } else {
//                inputs = layers.get(ii - 1);
//            }
//            DoubleMatrix activations = layers.get(ii);
//            DoubleMatrix netInput = netInputs.get(ii);
//            DoubleMatrix biasVec = biases.get(ii);
//
//            // Take the inputs multiply them by the weight matrix
//            // get the netInput for the next layer
//            forwardPropagate(inputs, wm, netInput);
//            // Add biases to the net input
//            // before biases were not part of the net input...
//            netInput.addi(biasVec);
//            // Apply the transfer function to net input to get the
//            // activation values for the next layer and store that
//            // value in the activations vector
//            updateRules.get(ii).applyFunction(netInput, activations);
//
//            // Activations = actFunction(matrix * inputs + biases)
//            // wm.mmuli(inputs, netInput);
//            // activations.copy(netInput);
//            // activations.addi(biasVec);
//            // updateRules.get(ii).applyFunctionInPlace(activations);
//            ii++;
//        }
//
//    }
//
//    /**
//     * Set error values using backprop (roughly output errors times intervening
//     * weights, going "backwards" from output towards input).
//     */
//    private void backpropagateError() {
//
//        // From output weight layer backwards, not including the first weight
//        // layer
//        for (int ii = layers.size() - 1; ii > 0; ii--) {
//            // Multiply errors in the next layer by the weight matrix in the
//            // opposite direction
//            // to get error in the previous layer.
//            backwardPropagate(errors.get(ii), weightMatrices.get(ii),
//                    errors.get(ii - 1));
//            // errors.get(ii).transpose().mmuli(weightMatrices.get(ii),
//            // errors.get(ii - 1));
//        }
//
//    }
//
//    /**
//     * Apply weight and bias updates.
//     */
//    private void updateParameters() {
//
//        // Update weights: learning rate * (error * f'(netin) * last layer
//        // input)
//        // Update biases: learning rate * (error * f'(netinput))
//
//        int layerIndex = 0;
//        for (DoubleMatrix wm : weightMatrices) {
//            DoubleMatrix prevLayer;
//            DoubleMatrix lastDeltas = lastWeightUpdates.get(layerIndex);
//            DoubleMatrix lastBiasDeltas = lastBiasUpdates.get(layerIndex);
//
//            if (layerIndex == 0) {
//                prevLayer = inputLayer;
//            } else {
//                prevLayer = layers.get(layerIndex - 1);
//            }
//            DoubleMatrix error = errors.get(layerIndex);
//            DoubleMatrix biasVector = biases.get(layerIndex);
//
//            // TODO: Can't use activations for non-logistic
//
//            // TODO: Recode this!!!!!
//            // The derivative for the logistic function has been
//            // HARD CODED here!!!!
//            DoubleMatrix currentLayer = layers.get(layerIndex);
//            DoubleMatrix derivs = currentLayer.rsub(1);
//            derivs.muli(currentLayer);
//            // updateRules.get(layerIndex).getDerivative(currentLayer, derivs);
//
//            // System.out.println("Deriv: " + derivs);
//
//            // Update the weights
//            // Note: JBlas data laid out in a 1-d array
//            // TODO: Optimize with matrix operations
//            int kk = 0;
//            for (int ii = 0; ii < currentLayer.length; ii++) {
//                for (int jj = 0; jj < prevLayer.length; jj++) {
//                    double deltaVal = learningRate * error.data[ii]
//                            * derivs.data[ii] * prevLayer.data[jj]
//                           + (momentum * lastDeltas.data[ii]);
//
//                    wm.data[kk] += deltaVal;
//                    lastDeltas.data[kk] = deltaVal;
//                    kk++;
//                }
//            }
//            for (int ii = 0; ii < biasVector.length; ii++) {
//                double deltaVal = learningRate * error.data[ii]
//                        * derivs.data[ii]
//                        + (momentum * lastBiasDeltas.data[ii]);
//                // System.out.println(deltaVal);
//                biasVector.data[ii] += deltaVal;
//                lastBiasDeltas.data[ii] = deltaVal;
//            }
//            layerIndex++;
//        }
//
//    }
//
//    /**
//     * Helper to get output layer.
//     */
//    private DoubleMatrix getOutputLayer() {
//        return layers.get(layers.size() - 1);
//    }
//
//    @Override
//    public double getError() {
//        return mse;
//    }
//
//    @Override
//    public void randomize() {
//        for (int kk = 0; kk < weightMatrices.size(); ++kk) {
//            for (int ii = 0; ii < weightMatrices.get(kk).data.length; ii++) {
//                 weightMatrices.get(kk).data[ii] =  rand.getRandom();
//            }
//        }
//
//        for (int kk = 0; kk < biases.size(); ++kk) {
//            for (int ii = 0; ii < biases.get(kk).length; ii++) {
//                biases.get(kk).data[ii] = rand.getRandom();
//            }
//        }
//    }
//
//    /**
//     * Print debug info.
//     */
//    private void printDebugInfo() {
//        System.out.println("---------------------------");
//        System.out.println("Targets: " + targetVector);
//        System.out.println("Node Layer 1");
//        System.out.println("\tActivations:" + inputLayer);
//        for (int i = 0; i < layers.size(); i++) {
//            System.out.println("Weight Layer " + (i + 1) + " --> " + (i + 2));
//            System.out.println("\tWeights:" + weightMatrices.get(i));
//            System.out.println("Node Layer " + (i + 2));
//            System.out.println("\tActivations: " + layers.get(i));
//            System.out.println("\tBiases: " + biases.get(i));
//            System.out.println("\tNet inputs: " + netInputs.get(i));
//            System.out.println("\tErrors: " + errors.get(i));
//            DoubleMatrix derivs = DoubleMatrix.zeros(layers.get(i).length);
//            updateRules.get(i).getDerivative(layers.get(i), derivs);
//            System.out.println("\tDerivatives: " + derivs);
//        }
//
//    }
//
//    @Override
//    public void commitChanges() {
//        for (int ii = 0; ii < layers.size(); ++ii) {
//            for (int jj = 0; jj < ngroups.get(ii).size(); ++jj) {
//                ngroups.get(ii).getNeuron(jj)
//                        .forceSetActivation(layers.get(ii).data[jj]);
//                ((BiasedUpdateRule) ngroups.get(ii).getNeuron(jj)
//                        .getUpdateRule()).setBias(biases.get(ii).get(jj));
//            }
//        }
//        for (int kk = 0; kk < weightMatrices.size(); ++kk) {
//            DoubleMatrix wm = weightMatrices.get(kk);
//            NeuronGroup src = synGrps.get(kk).getSourceNeuronGroup();
//            NeuronGroup tar = synGrps.get(kk).getTargetNeuronGroup();
//            for (int ii = 0; ii < wm.rows; ++ii) {
//                for (int jj = 0; jj < wm.columns; ++jj) {
//                    src.getNeuron(jj).getFanOutUnsafe().get(tar.getNeuron(ii))
//                            .forceSetStrength(wm.get(ii, jj));
//                }
//            }
//        }
//    }
//
//    /**
//     * Initialize input and target datasets as JBlas arrays.
//     */
//    public void initData() {
//        // Store data as columns since that's what everything else deals with
//        // So no need to do tranposes later.
//        if (network.getTrainingSet().getInputData() != null) {
//            inputData = new DoubleMatrix(
//                    network.getTrainingSet().getInputData()).transpose();
//        }
//        if (network.getTrainingSet().getTargetData() != null) {
//            targetData = new DoubleMatrix(
//                    network.getTrainingSet().getTargetData()).transpose();
//
//        }
//    }
//
//    /**
//     * Convenience method for in place "Forward" matrix multiplication (forward
//     * if vectors are assumed to be column-major) e.g.: Ax=y Transposes x and/or
//     * y if needed to make this the specific operation that happens (right
//     * multiply), and then transposes them back afterward. Thus the operation is
//     * unambiguous and one does not have to care if x/y are rows or columns.
//     * That is, regardless of if x or y are rows/columns Ax=y is performed,
//     * which can be considered a "forward" propagation in a column-major
//     * paradigm.
//     *
//     * @param _x the right-hand vector MUST be a vector
//     * @param _A the matrix
//     * @param _y the result of a matrix-vector multiply MUST be a vector of the
//     *            same number of elements as _x, can be equal to _x.
//     */
//    public static void forwardPropagate(DoubleMatrix _x, DoubleMatrix _A,
//            DoubleMatrix _y) {
//        boolean wasRowX = false;
//        boolean wasRowY = false;
//        if (_x.isRowVector()) {
//            // Fast transpose
//            _x.rows = _x.columns;
//            _x.columns = 1;
//            wasRowX = true;
//        }
//        if (_x != _y && _y.isRowVector()) {
//            // Fast transpose
//            _y.rows = _y.columns;
//            _y.columns = 1;
//            wasRowY = true;
//        }
//
//        _A.mmuli(_x, _y);
//
//        if (wasRowX) {
//            // Fast transpose back
//            _x.columns = _x.rows;
//            _x.rows = 1;
//        }
//        if (wasRowY) {
//            // Fast transpose back
//            _y.columns = _y.rows;
//            _y.rows = 1;
//        }
//    }
//
//    /**
//     * Convenience method for in place "Backward" matrix multiplication
//     * (backward if vectors are assumed to be column-major) e.g.: x^TA=y^T
//     * Transposes x and/or y if needed to make this the specific operation that
//     * happens (left multiply), and then transposes them back afterward. Thus
//     * the operation is unambiguous and one does not have to care if x/y are
//     * rows or columns. That is, regardless of if x or y are rows/columns xA=y
//     * is performed, which can be considered a "backward" propagation in a
//     * column-major paradigm.
//     *
//     * @param _x the left-hand vector MUST be a vector
//     * @param _A the matrix
//     * @param _y the result of a matrix-vector multiply MUST be a vector of the
//     *            same number of elements as _x, can be equal to _x.
//     */
//    public static void backwardPropagate(DoubleMatrix _x, DoubleMatrix _A,
//            DoubleMatrix _y) {
//        boolean wasColX = false;
//        boolean wasColY = false;
//        if (_x.isColumnVector()) {
//            // Fast transpose
//            _x.columns = _x.rows;
//            _x.rows = 1;
//            wasColX = true;
//        }
//        if (_x != _y && _y.isColumnVector()) {
//            // Fast transpose
//            _y.columns = _y.rows;
//            _y.rows = 1;
//            wasColY = true;
//        }
//
//        _x.mmuli(_A, _y);
//
//        if (wasColX) {
//            // Fast transpose back
//            _x.rows = _x.columns;
//            _x.columns = 1;
//        }
//        if (wasColY) {
//            // Fast transpose back
//            _y.rows = _y.columns;
//            _y.columns = 1;
//        }
//    }
//
//    /**
//     * Returns the current solution type inside a comboboxwrapper. Used by
//     * preference dialog.
//     *
//     * @return the the comboBox
//     */
//    public ComboBoxWrapper getUpdateMethod() {
//        return new ComboBoxWrapper() {
//            public Object getCurrentObject() {
//                return updateMethod;
//            }
//
//            public Object[] getObjects() {
//                return UpdateMethod.values();
//            }
//        };
//    }
//
//    /**
//     * Set the current update method. Used by preference dialog.
//     *
//     * @param umw update method wrapper
//     */
//    public void setUpdateMethod(final ComboBoxWrapper umw) {
//        updateMethod = ((UpdateMethod) umw.getCurrentObject());
//    }
//
//    /**
//     * @return the learningRate
//     */
//    public double getLearningRate() {
//        return learningRate;
//    }
//
//    /**
//     * @param learningRate the learningRate to set
//     */
//    public void setLearningRate(double learningRate) {
//        this.learningRate = learningRate;
//    }
//
//    /**
//     * @return the momentum
//     */
//    public double getMomentum() {
//        return momentum;
//    }
//
//    /**
//     * @param momentum the momentum to set
//     */
//    public void setMomentum(double momentum) {
//        this.momentum = momentum;
//    }
//
//    //
//    // TODO: Temporarily exposing this stuff for quick testing
//    //
//
//    /**
//     * @return the weightMatrices
//     */
//    public List<DoubleMatrix> getWeightMatrices() {
//        return weightMatrices;
//    }
//
//    /**
//     * @param weightMatrices the weightMatrices to set
//     */
//    public void setWeightMatrices(List<DoubleMatrix> weightMatrices) {
//        this.weightMatrices = weightMatrices;
//    }
//
//    /**
//     * @return the biases
//     */
//    public List<DoubleMatrix> getBiases() {
//        return biases;
//    }
//
//    /**
//     * @param biases the biases to set
//     */
//    public void setBiases(List<DoubleMatrix> biases) {
//        this.biases = biases;
//    }
//
//    /**
//     * @param updateMethod the updateMethod to set
//     */
//    public void setUpdateMethod(UpdateMethod updateMethod) {
//        this.updateMethod = updateMethod;
//    }
//
//}

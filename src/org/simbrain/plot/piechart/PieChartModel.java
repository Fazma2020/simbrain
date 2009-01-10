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
package org.simbrain.plot.piechart;

import java.util.ArrayList;
import java.util.Collection;

import org.jfree.data.general.DefaultPieDataset;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Model for saving pie charts.
 * 
 */
public class PieChartModel {

    /** Initial Number of data sources. */
    private static final int INITIAL_DATA_SOURCES = 6;

    /** Parent Component. */
    private PieChartComponent parentComponent;

    /** Consumer list. */
    private ArrayList<PieDataConsumer> consumers = new ArrayList<PieDataConsumer>();

    /** JFreeChart dataset for pie charts. */
    private DefaultPieDataset dataset = new DefaultPieDataset();

    /**
     * Default constructor.
     * @param parent component
     */
    public PieChartModel(final PieChartComponent parent) {
        parentComponent = parent;
        defaultInit();
    }

    /**
     * Default initialization.
     */
    private void defaultInit() {
        addDataSources(INITIAL_DATA_SOURCES);
    }

    /**
     * @return parent component.
     */
    public PieChartComponent getParent() {
        return parentComponent;
    }

    /**
     * Set the parent component.
     * 
     * @param parent
     *            the parent
     */
    public void setParent(final PieChartComponent parent) {
        this.parentComponent = parent;
    }

    /**
     * {@inheritDoc}
     */
    public Collection<PieDataConsumer> getConsumers() {
        return consumers;
    }

    /**
     * Create specified number of set of data sources. Adds these two existing
     * data sources.
     * 
     * @param numDataSources
     *            number of data sources to initialize plot with
     */
    public void addDataSources(final int numDataSources) {
        for (int i = 0; i < numDataSources; i++) {
            addDataSource();
        }
    }

    /**
     * Adds a data source to the plot.
     */
    public void addDataSource() {
        int currentSize = consumers.size() + 1;
        PieDataConsumer newAttribute = new PieDataConsumer(this, "PieData"
                + (currentSize), currentSize);
        consumers.add(newAttribute);
        // dataset.setValue(dataset.getKey(currentSize), -1);
    }

    /**
     * Removes a data source from the plot.
     */
    public void removeDataSource() {
        int lastSeriesIndex = consumers.size() - 1;

        if (lastSeriesIndex >= 0) {
            consumers.remove(lastSeriesIndex);
        }
        clearChart();
    }

    /**
     * Clears data from the chart.
     */
    public void clearChart() {
        dataset.clear();
    }

    /**
     * @return the data set.
     */
    public DefaultPieDataset getDataset() {
        return dataset;
    }

    /**
     * Returns a properly initialized xstream object.
     * 
     * @return the XStream object
     */
    public static XStream getXStream() {
        XStream xstream = new XStream(new DomDriver());
        xstream.omitField(PieChartModel.class, "parentComponent");
        xstream.omitField(PieChartModel.class, "consumers");
        return xstream;
    }

    /**
     * Standard method call made to objects after they are deserialized. See:
     * http://java.sun.com/developer/JDCTechTips/2002/tt0205.html#tip2
     * http://xstream.codehaus.org/faq.html
     * 
     * @return Initialized object.
     */
    private Object readResolve() {
        consumers = new ArrayList<PieDataConsumer>();
        return this;
    }

}
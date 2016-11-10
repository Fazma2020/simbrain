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
package org.simbrain.workspace;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

/**
 * A coupling is an object that allows different objects in the Simbrain
 * workspace to communicate with each other. A coupling is essentially a pair
 * consisting of a <code>Producer</code> and a <code>Consumer</code>, where the
 * producer passes a value of type E to the consumer:
 * <p>
 * Producer --&#62;  E --&#62;  Consumer
 * <p>
 * Producers and Consumers are types of <code>Attribute</code>. They are usually
 * not created directly but are created from a <code>PotentialAttribute</code>.
 * When creating couplings, a lot of the work is in creating these
 * PotentialAttributes. Most users of the API will create Potential Attributes
 * using the <code>AttributeManager</code>.
 *
 * @author Matthew Watson
 * @author Jeff Yoshimi
 *
 * @param <E> coupling attribute value type
 *
 * @see Attribute
 * @see PotentialAttribute
 * @see AttributeManager
 */
public final class Coupling<E> {

    /** The static logger for this class. */
    private static final Logger LOGGER = Logger.getLogger(Coupling.class);

    /** An arbitrary prime for creating better hash distributions. */
    private static final int ARBITRARY_PRIME = 59;

    /** Producing attribute for this coupling. */
    private Producer<E> producer;

    /** Consuming attribute for this coupling. */
    private Consumer<E> consumer;

    /** Value of buffer. */
    public E buffer;

    /**
     * Create a coupling between a specified consuming attribute, without yet
     * specifying the corresponding producing attribute.
     *
     * @param Consumer the attribute that consumes.
     */
    public Coupling(final Consumer<E> Consumer) {
        super();
        this.consumer = Consumer;
    }

    /**
     * Create a coupling between a specified producing attribute, without yet
     * specifying the corresponding consuming attribute.
     *
     * @param Producer the attribute that produces.
     */
    public Coupling(final Producer<E> Producer) {
        super();
        this.producer = Producer;
    }

    /**
     * Create a new coupling between the specified producing attribute and
     * consuming attribute.
     *
     * @param Producer producing attribute for this coupling
     * @param Consumer consuming attribute for this coupling
     */
    public Coupling(final Producer<E> Producer, final Consumer<E> Consumer) {
        LOGGER.debug("new Coupling");
        // System.out.println("producing " +
        // Producer.getAttributeDescription());
        // System.out.println("consuming " +
        // Consumer.getAttributeDescription());

        this.producer = Producer;
        this.consumer = Consumer;
    }

    /**
     * Create a new coupling by actualizing a potential producer and a potential
     * consumer.
     *
     * @param producer producing attribute for this coupling
     * @param consumer consuming attribute for this coupling
     */
    public Coupling(final PotentialProducer producer,
            final PotentialConsumer consumer) {
        LOGGER.debug("new Coupling");
        this.producer = (Producer<E>) producer.createProducer();
        this.consumer = (Consumer<E>) consumer.createConsumer();
    }

    /**
     * Set value of buffer.
     */
    public void setBuffer() {
        final WorkspaceComponent producerComponent = producer
                .getParentComponent();
        
        try {
            synchronized(producerComponent) {
                buffer = producer.getValue();
            }
        } catch (Exception e) {
            // TODO exception service?
            e.printStackTrace();
        }

        LOGGER.debug("buffer set: " + buffer);
    }

    /**
     * Update this coupling.
     */
    public void update() {
        if ((consumer != null) && (producer != null)) {
            final WorkspaceComponent consumerComponent = consumer
                    .getParentComponent();
            try {
                synchronized (consumer) {
                    consumer.setValue(buffer);
                }
            } catch (Exception e) {
                // TODO exception service?
                e.printStackTrace();
            }
        }
    }

    /**
     * @return the Producer
     */
    public Producer<E> getProducer() {
        return producer;
    }

    /**
     * @return the Consumer
     */
    public Consumer<E> getConsumer() {
        return consumer;
    }

    /**
     * Returns the string representation of this coupling.
     *
     * @return The string representation of this coupling.
     */
    public String toString() {
        String producerString;
        String producerComponent = "";
        String consumerString;
        String consumerComponent = "";
        if (producer == null) {
            producerString = "Null";
        } else {
            producerComponent = "[" + producer.getParentComponent().getName()
                    + "]";
            producerString = producer.getDescription();
        }
        if (consumer == null) {
            consumerString = "Null";
        } else {
            consumerComponent = "[" + consumer.getParentComponent().getName()
                    + "]";
            consumerString = consumer.getDescription();
        }
        return producerComponent + " " + producerString + " --> "
                + consumerComponent + " " + consumerString;
    }

    /**
     * Returns an id used for persistence.
     *
     * @return id
     */
    public String getId() {
        String producerString;
        String consumerString;
        if (producer == null) {
            producerString = "Null";
        } else {
            producerString = producer.getParentComponent().getName()
                    + ":"
                    + producer.getParentComponent().getKeyFromObject(
                            producer.getBaseObject()) + ":"
                    + producer.getMethodName();
        }
        if (consumer == null) {
            consumerString = "Null";
        } else {
            consumerString = consumer.getParentComponent().getName()
                    + ":"
                    + consumer.getParentComponent().getKeyFromObject(
                            consumer.getBaseObject()) + ":"
                    + consumer.getMethodName();
        }
        return producerString + "-" + consumerString;
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(final Object o) {
        if (o instanceof Coupling) {
            Coupling<?> other = (Coupling<?>) o;

            return other.producer.equals(producer)
                    && other.consumer.equals(consumer);
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return producer.hashCode() + (ARBITRARY_PRIME * consumer.hashCode());
    }

    /**
     * Returns the datatype associated with this coupling.
     *
     * @return the data type associated with this coupling.
     */
    public Class<?> getDataType() {
        return consumer.getDataType();
    }

}

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
package org.simbrain.world.odorworld;

import java.io.InputStream;
import java.io.OutputStream;

import org.simbrain.workspace.WorkspaceComponent;
import org.simbrain.world.odorworld.effectors.Effector;
import org.simbrain.world.odorworld.entities.OdorWorldEntity;
import org.simbrain.world.odorworld.sensors.Sensor;

/**
 * <b>WorldPanel</b> is the container for the world component. Handles toolbar
 * buttons, and serializing of world data. The main environment codes is in
 * {@link OdorWorldPanel}.
 */
public class OdorWorldComponent extends WorkspaceComponent {

    /** Reference to model world. */
    private OdorWorld world = new OdorWorld();

    /** Attribute types. */
//    AttributeType xLocationType = (new AttributeType(this, "Location", "X",
//            double.class, false));
//    AttributeType yLocationType = (new AttributeType(this, "Location", "Y",
//            double.class, false));
//    AttributeType turningType = (new AttributeType(this, "Turning",
//            double.class, true));
//    AttributeType straightMovementType = (new AttributeType(this, "Straight",
//            double.class, true));
//    AttributeType absoluteMovementType = (new AttributeType(this,
//            "Absolute-movement", double.class, false));
//    AttributeType smellSensorScalars = (new AttributeType(this, "Smell Scalar",
//            double.class, true));
//    AttributeType smellSensorVectors = (new AttributeType(this, "Smell Vector",
//            double[].class, true));
//    AttributeType tileSensorType = (new AttributeType(this, "Tile",
//            double.class, true));
//    AttributeType speechEffectorType = (new AttributeType(this, "Speech",
//            double.class, true));
//    AttributeType hearingSensorType = (new AttributeType(this, "Hearing",
//            double.class, true));

    /**
     * Default constructor.
     * @param name
     */
    public OdorWorldComponent(final String name) {
        super(name);
        initializeAttributes();
        addListener();
    }

    /**
     * Constructor used in deserializing.
     *
     * @param name name of world
     * @param world model world
     */
    public OdorWorldComponent(final String name, final OdorWorld world) {
        super(name);
        this.world = world;
        initializeAttributes();
        addListener();
    }

    /**
     * Initialize odor world attributes.
     */
    private void initializeAttributes() {

//        addConsumerType(xLocationType);
//        addConsumerType(yLocationType);
//        addConsumerType(turningType);
//        addConsumerType(straightMovementType);
//        addConsumerType(absoluteMovementType);
//        addConsumerType(speechEffectorType);
//
//        addProducerType(xLocationType);
//        addProducerType(yLocationType);
//        addProducerType(smellSensorScalars);
//        addProducerType(smellSensorVectors);
//        addProducerType(tileSensorType);
//        addProducerType(hearingSensorType);
    }

//    @Override
//    public List<PotentialConsumer> getPotentialConsumers() {
//
//        List<PotentialConsumer> returnList = new ArrayList<PotentialConsumer>();
//
//        for (OdorWorldEntity entity : world.getObjectList()) {
//
//            // X, Y Locations
//            if (xLocationType.isVisible()) {
//                String description = entity.getName() + ":X";
//                PotentialConsumer consumer = getAttributeManager()
//                        .createPotentialConsumer(entity, "setX", double.class);
//                consumer.setCustomDescription(description);
//                returnList.add(consumer);
//            }
//            if (yLocationType.isVisible()) {
//                String description = entity.getName() + ":Y";
//                PotentialConsumer consumer = getAttributeManager()
//                        .createPotentialConsumer(entity, "setY", double.class);
//                consumer.setCustomDescription(description);
//                returnList.add(consumer);
//            }
//
//            // Absolute movement
//            if (absoluteMovementType.isVisible()) {
//
//                String description = entity.getName() + ":goNorth";
//                PotentialConsumer goNorth = getAttributeManager()
//                        .createPotentialConsumer(entity, "moveNorth",
//                                double.class);
//                goNorth.setCustomDescription(description);
//                returnList.add(goNorth);
//
//                description = entity.getName() + ":goSouth";
//                PotentialConsumer goSouth = getAttributeManager()
//                        .createPotentialConsumer(entity, "moveSouth",
//                                double.class);
//                goSouth.setCustomDescription(description);
//                returnList.add(goSouth);
//
//                description = entity.getName() + ":goEast";
//                PotentialConsumer goEast = getAttributeManager()
//                        .createPotentialConsumer(entity, "moveEast",
//                                double.class);
//                goEast.setCustomDescription(description);
//                returnList.add(goEast);
//
//                description = entity.getName() + ":goWest";
//                PotentialConsumer goWest = getAttributeManager()
//                        .createPotentialConsumer(entity, "moveWest",
//                                double.class);
//                goWest.setCustomDescription(description);
//                returnList.add(goWest);
//            }
//
//            // Turning and Going Straight
//            if (entity instanceof RotatingEntity) {
//                for (Effector effector : entity.getEffectors()) {
//                    if (effector instanceof StraightMovement) {
//                        if (straightMovementType.isVisible()) {
//                            String description = entity.getName()
//                                    + ":goStraight";
//                            PotentialConsumer consumer = getAttributeManager()
//                                    .createPotentialConsumer(effector,
//                                            "setAmount", double.class);
//                            consumer.setCustomDescription(description);
//                            returnList.add(consumer);
//                        }
//                    } else if (effector instanceof Turning) {
//                        if (turningType.isVisible()) {
//                            double direction = ((Turning) effector)
//                                    .getDirection();
//                            if (direction == Turning.LEFT) {
//                                String description = entity.getName()
//                                        + ":turnLeft";
//                                PotentialConsumer consumer = getAttributeManager()
//                                        .createPotentialConsumer(effector,
//                                                "setAmount", double.class);
//                                consumer.setCustomDescription(description);
//                                returnList.add(consumer);
//                            } else if (direction == Turning.RIGHT) {
//                                String description = entity.getName()
//                                        + ":turnRight";
//                                PotentialConsumer consumer = getAttributeManager()
//                                        .createPotentialConsumer(effector,
//                                                "setAmount", double.class);
//                                consumer.setCustomDescription(description);
//                                returnList.add(consumer);
//                            }
//                        }
//                    } else if (effector instanceof Speech) {
//                        if (speechEffectorType.isVisible()) {
//                            String description = entity.getName()
//                                    + ((Speech) effector).getLabel();
//                            PotentialConsumer consumer = getAttributeManager()
//                                    .createPotentialConsumer(effector,
//                                            "setAmount", double.class);
//                            consumer.setCustomDescription(description);
//                            returnList.add(consumer);
//                        }
//                    }
//
//                }
//
//            }
//        }
//        return returnList;
//    }
//
//    @Override
//    public List<PotentialProducer> getPotentialProducers() {
//
//        List<PotentialProducer> returnList = new ArrayList<PotentialProducer>();
//
//        for (OdorWorldEntity entity : world.getObjectList()) {
//
//            // X, Y Location of entities
//            if (xLocationType.isVisible()) {
//                String description = entity.getName() + ":X";
//                PotentialProducer producer = getAttributeManager()
//                        .createPotentialProducer(entity, "getX", double.class);
//                producer.setCustomDescription(description);
//                returnList.add(producer);
//            }
//            if (yLocationType.isVisible()) {
//                String description = entity.getName() + ":Y";
//                PotentialProducer producer = getAttributeManager()
//                        .createPotentialProducer(entity, "getY", double.class);
//                producer.setCustomDescription(description);
//                returnList.add(producer);
//            }
//
//            // Smell scalar sensors
//            if (smellSensorScalars.isVisible()) {
//                for (Sensor sensor : entity.getSensors()) {
//                    if (sensor instanceof SmellSensor) {
//                        SmellSensor smell = (SmellSensor) sensor;
//                        for (int i = 0; i < smell.getCurrentValue().length; i++) {
//                            String description = entity.getName() + ":"
//                                    + sensor.getLabel() + "-" + (i + 1);
//                            PotentialProducer producer = getAttributeManager()
//                                    .createPotentialProducer(smell,
//                                            "getCurrentValue", double.class,
//                                            new Class[] { int.class },
//                                            new Object[] { i });
//                            producer.setCustomDescription(description);
//                            returnList.add(producer);
//                        }
//                        // TODO: A way of indicating sensor location (relative
//                        // location in polar coordinates)
//                    }
//                }
//            }
//            if (smellSensorVectors.isVisible()) {
//                for (Sensor sensor : entity.getSensors()) {
//                    if (sensor instanceof SmellSensor) {
//                        SmellSensor smell = (SmellSensor) sensor;
//                        if (sensor instanceof SmellSensor) {
//                            PotentialProducer producer = getAttributeManager()
//                                    .createPotentialProducer(smell,
//                                            "getCurrentValue", double[].class);
//                            producer.setCustomDescription(entity.getName()
//                                    + ":" + sensor.getLabel());
//                            returnList.add(producer);
//                        }
//                    }
//                }
//            }
//
//            // Hearing Sensor
//            if (hearingSensorType.isVisible()) {
//                for (Sensor sensor : entity.getSensors()) {
//                    if (sensor instanceof Hearing) {
//                        String description = entity.getName()
//                                + ((Hearing) sensor).getLabel();
//                        PotentialProducer producer = getAttributeManager()
//                                .createPotentialProducer(sensor, "getValue",
//                                        double.class);
//                        producer.setCustomDescription(description);
//                        returnList.add(producer);
//                    }
//                }
//            }
//
//            // Tile sensor
//            if (tileSensorType.isVisible()) {
//                for (Sensor sensor : entity.getSensors()) {
//                    if (sensor instanceof TileSensor) {
//                        String description = entity.getName() + ":"
//                                + ((TileSensor) sensor).getLabel();
//                        PotentialProducer producer = getAttributeManager()
//                                .createPotentialProducer(sensor, "getValue",
//                                        double.class);
//                        producer.setCustomDescription(description);
//                        returnList.add(producer);
//                    }
//                }
//            }
//        }
//        return returnList;
//    }

    /**
     * Initialize this component.
     */
    private void addListener() {
        world.addListener(new WorldListener() {

            public void updated() {
                fireUpdateEvent();
            }

            public void effectorAdded(final Effector effector) {
                setChangedSinceLastSave(true);
//                firePotentialAttributesChanged();
            }

            public void effectorRemoved(final Effector effector) {
                setChangedSinceLastSave(true);
//                fireAttributeObjectRemoved(effector);
//                firePotentialAttributesChanged();
            }

            public void entityAdded(final OdorWorldEntity entity) {
                setChangedSinceLastSave(true);
//                firePotentialAttributesChanged();
            }

            public void entityRemoved(final OdorWorldEntity entity) {
                setChangedSinceLastSave(true);
//                fireAttributeObjectRemoved(entity);
//                firePotentialAttributesChanged();
            }

            public void sensorAdded(final Sensor sensor) {
                setChangedSinceLastSave(true);
//                firePotentialAttributesChanged();
            }

            public void sensorRemoved(Sensor sensor) {
                setChangedSinceLastSave(true);
//                fireAttributeObjectRemoved(sensor);
//                firePotentialAttributesChanged();
            }

            public void entityChanged(OdorWorldEntity entity) {
                setChangedSinceLastSave(true);
            }

            public void propertyChanged() {
                // TODO Auto-generated method stub

            }
        });
    }

    /**
     * Recreates an instance of this class from a saved component.
     *
     * @param input
     * @param name
     * @param format
     * @return
     */
    public static OdorWorldComponent open(InputStream input, String name,
            String format) {
        OdorWorld newWorld = (OdorWorld) OdorWorld.getXStream().fromXML(input);
        return new OdorWorldComponent(name, newWorld);
    }

    @Override
    public String getXML() {
        return OdorWorld.getXStream().toXML(world);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(final OutputStream output, final String format) {
        OdorWorld.getXStream().toXML(world, output);
    }

    @Override
    public String getKeyFromObject(Object object) {
        if (object instanceof OdorWorldEntity) {
            return ((OdorWorldEntity) object).getId();
        } else if (object instanceof Sensor) {
            String entityName = ((Sensor) object).getParent().getName();
            String sensorName = ((Sensor) object).getId();
            return entityName + ":sensor:" + sensorName;
        } else if (object instanceof Effector) {
            String entityName = ((Effector) object).getParent().getName();
            String effectorName = ((Effector) object).getId();
            return entityName + ":effector:" + effectorName;
        }

        return null;
    }

    @Override
    public Object getObjectFromKey(String objectKey) {
        String[] parsedKey = objectKey.split(":");
        String entityName = parsedKey[0];
        if (parsedKey.length == 1) {
            return getWorld().getEntity(entityName);
        } else {
            String secondString = parsedKey[1];
            if (secondString.equalsIgnoreCase("sensor")) {
                return getWorld().getSensor(entityName, parsedKey[2]);
            } else if (secondString.equalsIgnoreCase("effector")) {
                return getWorld().getEffector(entityName, parsedKey[2]);
            } else if (secondString.equalsIgnoreCase("smellSensorGetter")) {
                // Needed to read simulations created before 2/11; remove before
                // beta release
                int index = Integer.parseInt(parsedKey[3]);
                return getWorld().getSensor(entityName, parsedKey[2]);
            } else if (secondString.equalsIgnoreCase("smeller")) {
                int index = Integer.parseInt(parsedKey[3]);
                return getWorld().getSensor(entityName, parsedKey[2]);
            }
        }
        return null;
    }

    @Override
    public void closing() {
        // TODO Auto-generated method stub
    }

    @Override
    public void update() {
        world.update(this.getWorkspace().getTime());
    }

    /**
     * Returns a reference to the odor world.
     *
     * @return the odor world object.
     */
    public OdorWorld getWorld() {
        return world;
    }
}
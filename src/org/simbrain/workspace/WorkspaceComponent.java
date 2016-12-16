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

import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.simbrain.workspace.gui.ComponentPanel;
import org.simbrain.workspace.gui.GuiComponent;

/**
 * Represents a component in a Simbrain {@link org.simbrain.workspace.Workspace}
 * . Extend this class to create your own component type. Gui representations of
 * a workspace component should extend
 * {@link org.simbrain.workspace.gui.GuiComponent}.
 */
public abstract class WorkspaceComponent {

    /** The workspace that 'owns' this component. */
    private Workspace workspace;

    /** Log4j logger. */
    private Logger logger = Logger.getLogger(WorkspaceComponent.class);

    /** The set of all WorkspaceComponentListeners on this component. */
    private final Collection<WorkspaceComponentListener> workspaceComponentListeners;

    /** List of attribute listeners. */
    private final Collection<AttributeListener> attributeListeners;

    /** Whether this component has changed since last save. */
    private boolean changedSinceLastSave = false;

    // /** List of producer types. */
    // private final List<AttributeType> producerTypes = new
    // ArrayList<AttributeType>();
    //
    // /** List of consumer types. */
    // private final List<AttributeType> consumerTypes = new
    // ArrayList<AttributeType>();

    /**
     * Whether to display the GUI for this component (obviously only relevant
     * when Simbrain is run as a GUI). TODO: This should really be a property of
     * the GUI only, since we can imagine the gui is on or off for different
     * views of the component. This design is kind of hack, based on the fact
     * that {@link ComponentPanel} has no easy access to {@link GuiComponent}.
     */
    private Boolean guiOn = true;

    /** Whether to update this component. */
    private Boolean updateOn = true;

    /** The name of this component. Used in the title, in saving, etc. */
    private String name = "";

    /**
     * Current file. Used when "saving" a component. Subclasses can provide a
     * default value using User Preferences.
     */
    private File currentFile;

    /** Manage creation of attributes on this component. */
    // private final AttributeManager attributeManager;

    /**
     * If set to true, serialize this component before others. Possibly replace
     * with priority system later.
     * {@see org.simbrain.workspace.Workspace#preSerializationInit()}.
     */
    private int serializePriority = 0;

    /**
     * Initializer
     */
    {
        workspaceComponentListeners = new HashSet<WorkspaceComponentListener>();
        attributeListeners = new HashSet<AttributeListener>();
        // attributeManager = new AttributeManager(this);
    }

    /**
     * Construct a workspace component.
     *
     * @param name The name of the component.
     */
    public WorkspaceComponent(final String name) {
        this.name = name;
        logger.trace(
                this.getClass().getCanonicalName() + ": " + name + " created");
    }

    /**
     * Used when saving a workspace. All changed workspace components are saved
     * using this method.
     *
     * @param output the stream of data to write the data to.
     * @param format a key used to define the requested format.
     */
    public abstract void save(OutputStream output, String format);

    // TEMP. Override.
    public void save2(OutputStream output, String format) {
    };

    // TODO: TEMP
    public void open2() {
    }

    /**
     * Returns a list of the formats that this component supports.
     * <p>
     * The default behavior is to return an empty list. This means that there is
     * one format.
     *
     * @return a list of the formats that this component supports.
     */
    public List<? extends String> getFormats() {
        return Collections.singletonList(getDefaultFormat());
    }

    /**
     * Closes the WorkspaceComponent.
     */
    public void close() {
        closing();
        workspace.removeWorkspaceComponent(this);
    }

    /**
     * Perform cleanup after closing.
     */
    protected abstract void closing();

    /**
     * Called by Workspace to update the state of the component.
     */
    public void update() {
        /* no default implementation */
    }

    // TODO: This is more cruft to remove

    // /**
    // * Return the potential consumers associated with this component.
    // Subclasses
    // * should override this to make their consumers available.
    // *
    // * @return the consumer list.
    // */
    // public List<PotentialConsumer> getPotentialConsumers() {
    // return Collections.EMPTY_LIST;
    // }
    //
    // /**
    // * Return the potential producers associated with this component.
    // Subclasses
    // * should override this to make their producers available.
    // *
    // * @return the producer list.
    // */
    // public List<PotentialProducer> getPotentialProducers() {
    // return Collections.EMPTY_LIST;
    // }
    //
    /**
     * Fire attribute object removed event (when the base object of an attribute
     * is removed).
     *
     * @param object the object which was removed
     */
    public void fireAttributeObjectRemoved(Object object) {
        for (AttributeListener listener : attributeListeners) {
            listener.attributeObjectRemoved(object);
        }
    }

    /**
     * Fire potential attributes changed event.
     */
    // TODO: Rename to fireAttributesChanged
    public void firePotentialAttributesChanged() {
        for (AttributeListener listener : attributeListeners) {
            listener.potentialAttributesChanged();
        }
    }
    //
    // /**
    // * Fire attribute type visibility changed event.
    // *
    // * @param type the type whose visibility changed.
    // */
    // public void fireAttributeTypeVisibilityChanged(AttributeType type) {
    // for (AttributeListener listener : attributeListeners) {
    // listener.attributeTypeVisibilityChanged(type);
    // }
    //
    // }

    /**
     * Adds a AttributeListener to this component.
     *
     * @param listener the AttributeListener to add.
     */
    public void addAttributeListener(final AttributeListener listener) {
        attributeListeners.add(listener);
    }

    /**
     * Removes an AttributeListener from this component.
     *
     * @param listener the AttributeListener to remove.
     */
    public void removeAttributeListener(AttributeListener listener) {
        attributeListeners.remove(listener);
    }
    //
    // /**
    // * Add a new type of producer.
    // *
    // * @param type type to add
    // */
    // public void addProducerType(AttributeType type) {
    // if (!producerTypes.contains(type)) {
    // producerTypes.add(type);
    // }
    // }
    //
    // /**
    // * Add a new type of consumer.
    // *
    // * @param type type to add
    // */
    // public void addConsumerType(AttributeType type) {
    // if (!consumerTypes.contains(type)) {
    // consumerTypes.add(type);
    // }
    // }

    /**
     * Finds objects based on a key. Used in deserializing attributes. Any class
     * that produces attributes should override this for serialization.
     *
     * @param objectKey String key
     * @return the corresponding object
     */
    public Object getObjectFromKey(final String objectKey) {
        return null;
    }

    /**
     * Returns a unique key associated with an object. Used in serializing
     * attributes. Any class that produces attributes should override this for
     * serialization.
     *
     * @param object object which should be associated with a key
     * @return the key
     */
    public String getKeyFromObject(Object object) {
        return null;
    }

    /**
     * Called by Workspace to notify that updates have stopped.
     */
    protected void stopped() {
        /* no default implementation */
    }

    /**
     * Notify all workspaceComponentListeners of a componentUpdated event.
     */
    public final void fireUpdateEvent() {
        for (WorkspaceComponentListener listener : workspaceComponentListeners) {
            listener.componentUpdated();
        }
    }

    /**
     * Notify all workspaceComponentListeners that the gui has been turned on or
     * off.
     */
    public final void fireGuiToggleEvent() {
        for (WorkspaceComponentListener listener : workspaceComponentListeners) {
            listener.guiToggled();
        }
    }

    /**
     * Notify all workspaceComponentListeners of a component has been turned on
     * or off.
     */
    public final void fireComponentToggleEvent() {
        for (WorkspaceComponentListener listener : workspaceComponentListeners) {
            listener.componentOnOffToggled();
        }
    }

    /**
     * Called after a global update ends.
     */
    final void doStopped() {
        stopped();
    }

    /**
     * Returns the WorkspaceComponentListeners on this component.
     *
     * @return The WorkspaceComponentListeners on this component.
     */
    public Collection<WorkspaceComponentListener> getWorkspaceComponentListeners() {
        return Collections.unmodifiableCollection(workspaceComponentListeners);
    }

    /**
     * Adds a WorkspaceComponentListener to this component.
     *
     * @param listener the WorkspaceComponentListener to add.
     */
    public void addWorkspaceComponentListener(
            final WorkspaceComponentListener listener) {
        workspaceComponentListeners.add(listener);
    }

    /**
     * Adds a WorkspaceComponentListener to this component.
     *
     * @param listener the WorkspaceComponentListener to add.
     */
    public void removeWorkspaceComponentListener(
            final WorkspaceComponentListener listener) {
        workspaceComponentListeners.remove(listener);
        firePotentialAttributesChanged();
    }

    /**
     * Returns the name of this component.
     *
     * @return The name of this component.
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return name;
        // return this.getClass().getSimpleName() + ": " + name;
    }

    /**
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
        // TODO: Think about this
        // for (WorkspaceComponentListener listener : this.getListeners()) {
        // listener.setTitle(name);
        // }
    }

    /**
     * Retrieves a simple version of a component name from its class, e.g.
     * "Network" from "org.simbrain.network.NetworkComponent"/
     *
     * @return the simple name.
     */
    public String getSimpleName() {
        String simpleName = getClass().getSimpleName();
        if (simpleName.endsWith("Component")) {
            simpleName = simpleName.replaceFirst("Component", "");
        }
        return simpleName;
    }

    /**
     * Override for use with open service.
     *
     * @return xml string representing stored file.
     */
    public String getXML() {
        return null;
    }

    /**
     * Sets the workspace for this component. Called by the workspace right
     * after this component is created.
     *
     * @param workspace The workspace for this component.
     */
    public void setWorkspace(final Workspace workspace) {
        this.workspace = workspace;
    }

    /**
     * Returns the workspace associated with this component.
     *
     * @return The workspace associated with this component.
     */
    public Workspace getWorkspace() {
        return workspace;
    }
    //
    // /**
    // * Called when a coupling attached to this component is removed. This
    // method
    // * will only be called once if this component has both the source and the
    // * target.
    // *
    // * @param coupling The coupling that has been removed.
    // */
    // public void couplingRemoved(final Coupling<?> coupling) {
    // // No implementation.
    // }
    //
    // /**
    // * Called when a coupling is attached to this component.
    // *
    // * @param coupling The coupling that is being added
    // */
    // public void couplingAdded(Coupling<?> coupling) {
    // // Override is this function is needed in a component type
    // }

    /**
     * The file extension for a component type, e.g. By default, "xml".
     *
     * @return the file extension
     */
    public String getDefaultFormat() {
        return "xml";
    }

    /**
     * Set to true when a component changes, set to false after a component is
     * saved.
     *
     * @param changedSinceLastSave whether this component has changed since the
     *            last save.
     */
    public void setChangedSinceLastSave(final boolean changedSinceLastSave) {
        logger.debug("component changed");
        this.changedSinceLastSave = changedSinceLastSave;
    }

    /**
     * Returns true if it's changed since the last save.
     *
     * @return the changedSinceLastSave
     */
    public boolean hasChangedSinceLastSave() {
        return changedSinceLastSave;
    }

    /**
     * @return the currentFile
     */
    public File getCurrentFile() {
        return currentFile;
    }

    /**
     * @param currentFile the currentFile to set
     */
    public void setCurrentFile(final File currentFile) {
        this.currentFile = currentFile;
    }

    /**
     * @return the logger
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * @param logger the logger to set
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * @return the guiOn
     */
    public Boolean isGuiOn() {
        return guiOn;
    }

    /**
     * @param guiOn the guiOn to set
     */
    public void setGuiOn(Boolean guiOn) {
        this.guiOn = guiOn;
        this.fireGuiToggleEvent();
    }

    /**
     * @return the updateOn
     */
    public Boolean getUpdateOn() {
        return updateOn;
    }

    /**
     * @param updateOn the updateOn to set
     */
    public void setUpdateOn(Boolean updateOn) {
        this.updateOn = updateOn;
        this.fireComponentToggleEvent();
    }

    // /**
    // * @return the producerTypes
    // */
    // public List<AttributeType> getProducerTypes() {
    // return Collections.unmodifiableList(producerTypes);
    // }
    //
    // /**
    // * @return the consumerTypes
    // */
    // public List<AttributeType> getConsumerTypes() {
    // return Collections.unmodifiableList(consumerTypes);
    // }
    //
    // /**
    // * Return visible producer types.
    // *
    // * @return the visible producerTypes
    // */
    // public List<AttributeType> getVisibleProducerTypes() {
    // List<AttributeType> returnList = new ArrayList<AttributeType>();
    // for (AttributeType type : getProducerTypes()) {
    // if (type.isVisible()) {
    // returnList.add(type);
    // }
    // }
    // return returnList;
    // }
    //
    // /**
    // * Return visible consumer types.
    // *
    // * @return the visible consumerTypes
    // */
    // public List<AttributeType> getVisibleConsumerTypes() {
    // List<AttributeType> returnList = new ArrayList<AttributeType>();
    // for (AttributeType type : getConsumerTypes()) {
    // if (type.isVisible()) {
    // returnList.add(type);
    // }
    // }
    // return returnList;
    // }
    //
    // /**
    // * @return the attributeManager
    // */
    // public AttributeManager getAttributeManager() {
    // return attributeManager;
    // }

    /**
     * @return the serializePriority
     */
    protected int getSerializePriority() {
        return serializePriority;
    }

    /**
     * @param serializePriority the serializePriority to set
     */
    protected void setSerializePriority(int serializePriority) {
        this.serializePriority = serializePriority;
    }

    // TODO: Once annotation based coupling is done remove the next two
    // methods and as much other cruft as possible

    // /**
    // * Convenience method for making producers.
    // *
    // * @param baseObject the object to produce values
    // * @param methodName name of the method on that object
    // * @param dataType data type of the method
    // * @return the producer
    // */
    // public Producer<?> createProducer(Object baseObject, String methodName,
    // Class<?> dataType) {
    // PotentialProducer pp = attributeManager
    // .createPotentialProducer(baseObject, methodName, dataType);
    // return pp.createProducer();
    // }
    //
    // /**
    // * Convenience method for making consumers.
    // *
    // * @param baseObject the object to consume values
    // * @param methodName the method name on that object
    // * @param dataType data type of argument to that method
    // * @return the consumer
    // */
    // public Consumer<?> createConsumer(Object baseObject, String methodName,
    // Class<?> dataType) {
    // PotentialConsumer pc = attributeManager
    // .createPotentialConsumer(baseObject, methodName, dataType);
    // return pc.createConsumer();
    // }

    // /**
    // * Create a producer based on a method name assumed to exist at the
    // * workspace component level. Assumes the method has been annotated with a
    // * potential producer annotations.
    // *
    // * @param methodName the method name
    // * @return the the producer, or null if not such method could be found.
    // */
    // public Producer<?> createProducer(String methodName) {
    // for (PotentialProducer pp : getAnnotatedProducers()) {
    // if (pp.getMethodName().equalsIgnoreCase(methodName)) {
    // return pp.createProducer();
    // }
    // }
    // return null;
    //
    // }

    // /**
    // * Create a producer based on a method name assumed to exist on the
    // provided
    // * base object. Assumes the method has been annotated with a potential
    // * producer annotation.
    // *
    // * @param baseObject the base object
    // * @param methodName the method name
    // * @return the the producer, or null if not such method could be found.
    // */
    // public Producer<?> createProducer(Object baseObject, String methodName) {
    // for (PotentialProducer pp : getAnnotatedProducers(baseObject)) {
    // if (pp.getMethodName().equalsIgnoreCase(methodName)) {
    // return pp.createProducer();
    // }
    // }
    // return null;
    //
    // }
    //
    // // TODO: Example in javadoc below
    // /**
    // * Create a producer based on a method name assumed to exist on the
    // provided
    // * base object, accessed via a key (e.g. an index). Assumes the method has
    // * been annotated with a potential producer annotation.
    // *
    // * @param baseObject the base object
    // * @param methodName the method name
    // * @param key the key used to access to call the producer.
    // * @return the the producer, or null if not such method could be found.
    // */
    // public Producer<?> createProducer(Object baseObject, String methodName,
    // Object key) {
    // for (PotentialProducer pp : getAnnotatedProducers(baseObject, key)) {
    // if (pp.getArgumentValues().length > 0) {
    // if (pp.getMethodName().equalsIgnoreCase(methodName)) {
    // pp.setArgumentValues(new Object[] { key });
    // return pp.createProducer();
    // }
    // }
    // }
    // return null;
    //
    // }
    //
    // /**
    // * Create a consumer based on a method name assumed to exist at the
    // * workspace component level. Assumes the method has been annotated with a
    // * potential producer annotations.
    // *
    // * @param methodName the method name
    // * @return the the consumer, or null if not such method could be found.
    // */
    // public Consumer<?> createConsumer(String methodName) {
    // for (PotentialConsumer pp : getAnnotatedConsumers()) {
    // if (pp.getMethodName().equalsIgnoreCase(methodName)) {
    // return pp.createConsumer();
    // }
    // }
    // return null;
    // }
    //
    // /**
    // * Create a consumer based on a method name assumed to exist on the
    // provided
    // * base object. Assumes the method has been annotated with a potential
    // * producer annotations.
    // *
    // * @param baseObject the base object
    // * @param methodName the method name
    // * @return the the consumer, or null if not such method could be found.
    // */
    // public Consumer<?> createConsumer(Object baseObject, String methodName) {
    // for (PotentialConsumer pc : getAnnotatedConsumers(baseObject)) {
    // if (pc.getMethodName().equalsIgnoreCase(methodName)) {
    // return pc.createConsumer();
    // }
    // }
    // return null;
    // }

    // TODO: Consumer with key.
    // TODO: Change to getProducibles/Consumibles?

    // /**
    // * Returns all methods annotated as producible. Override this as in
    // * NetworkComponent.
    // * {@link org.simbrain.network.NetworkComponent#getAnnotatedProducers()}
    // *
    // * @return the resulting list of potential producers
    // */
    // public List<PotentialProducer> getAnnotatedProducers() {
    // return getAnnotatedProducers(this);
    // }
    //
    // /**
    // * Returns all methods annotated as producible on the provided base
    // object.
    // * Helper for getAnnotatedProducers.
    // *
    // * @param baseObject base object
    // * @return the resulting list of potential producers
    // */
    // public final List<PotentialProducer> getAnnotatedProducers(
    // Object baseObject) {
    // return getAnnotatedProducers(baseObject, null);
    // }
    //
    // /**
    // * Returns all methods annotated as producible on the provided base
    // object,
    // * using the provided key.
    // *
    // * @param baseObject base object
    // * @param key the key used to access the producer
    // * @return the resulting list of potential producers
    // */
    // public final List<PotentialProducer> getAnnotatedProducers(
    // Object baseObject, Object key) {
    // List<PotentialProducer> potentialProducers = new
    // ArrayList<PotentialProducer>();
    // for (Method method : baseObject.getClass().getMethods()) {
    // // Find methods annotated as producible
    // if (method.getAnnotation(Producible.class) != null) {
    // PotentialProducer pp;
    // if (key == null) {
    // if (method.getParameterTypes().length == 0) {
    // pp = getAttributeManager().createPotentialProducer(
    // baseObject, method.getName(),
    // method.getReturnType());
    // setCustomDescription(pp, baseObject, method);
    // potentialProducers.add(pp);
    // }
    // } else {
    // // Find the producible with the specified key
    // if (method.getParameterTypes().length == 1) {
    // pp = getAttributeManager().createPotentialProducer(
    // baseObject, method.getName(),
    // method.getReturnType(),
    // new Class[] { key.getClass() },
    // new Object[] { key });
    // setCustomDescription(pp, baseObject, method);
    // potentialProducers.add(pp);
    // }
    // }
    //
    // }
    // }
    // return potentialProducers;
    // }
    //
    // /**
    // * Returns all methods annotated as consumible.
    // *
    // * Override this
    // *
    // * @return the resulting list of potential consumers
    // */
    // public List<PotentialConsumer> getAnnotatedConsumers() {
    // return getAnnotatedConsumers(this);
    // }
    //
    // /**
    // * Returns all methods annotated as consumible on the provided base
    // object.
    // *
    // * @param baseObject base object
    // * @return the resulting list of potential consumers
    // */
    // public final List<PotentialConsumer> getAnnotatedConsumers(
    // Object baseObject) {
    //
    // List<PotentialConsumer> potentialConsumers = new
    // ArrayList<PotentialConsumer>();
    // for (Method method : baseObject.getClass().getMethods()) {
    // // Find methods annotated as consumible
    // if (method.getAnnotation(Consumible.class) != null) {
    // PotentialConsumer pc = getAttributeManager()
    // .createPotentialConsumer(baseObject, method.getName(),
    // method.getParameterTypes()[0]);
    // setCustomDescription(pc, baseObject, method);
    // potentialConsumers.add(pc);
    // }
    // }
    // return potentialConsumers;
    // }
    //
    // /**
    // * Sets a custom description on an annotated attribute, if it has been
    // * specified in the annotation "customDescriptionMethod" field.
    // *
    // * @param attribute the attribute to check
    // * @param baseObject the base object of the method
    // * @param method the producible or consumible method
    // */
    // private void setCustomDescription(PotentialAttribute attribute,
    // Object baseObject, Method method) {
    // String customDescriptionMethod = "";
    // if (attribute instanceof PotentialProducer) {
    // customDescriptionMethod = method.getAnnotation(Producible.class)
    // .customDescriptionMethod();
    //
    // } else {
    // customDescriptionMethod = method.getAnnotation(Consumible.class)
    // .customDescriptionMethod();
    // }
    // if (!customDescriptionMethod.isEmpty()) {
    // try {
    // // TODO: Think about below
    // Method descriptionMethod = baseObject.getClass()
    // .getMethod(customDescriptionMethod, null);
    // String customDescription = (String) descriptionMethod
    // .invoke(this, null);
    // attribute.setCustomDescription(customDescription);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    //
    // }
    // }
    //

    //// NEW STUFF. TODO. ////

    // To override
    public List<Producer2<?>> getProducers() {
        return Collections.EMPTY_LIST;
    }

    public List<Consumer2<?>> getConsumers() {
        return Collections.EMPTY_LIST;
    }

    // Helper
    // TODO: Rename to getProduersOnObject... to clarify it's a service / helper
    public static final List<Producer2<?>> getProducers(Object object) {
        List<Producer2<?>> returnList = new ArrayList<>();
        for (Method method : object.getClass().getMethods()) {
            if (method.getAnnotation(Producible.class) != null) {
                Producer2<?> producer = new Producer2<>(object, method);
                setCustomDescription(producer);
                returnList.add(producer);
            }
        }
        return returnList;
    }

    public static final List<Consumer2<?>> getConsumers(Object object) {
        List<Consumer2<?>> returnList = new ArrayList<>();
        for (Method method : object.getClass().getMethods()) {
            if (method.getAnnotation(Consumible.class) != null) {
                Consumer2<?> consumer = new Consumer2<>(object, method);
                setCustomDescription(consumer);
                returnList.add(consumer);
            }
        }
        return returnList;
    }

    /**
     * Sets a custom description on an annotated attribute, if it has been
     * specified in the annotation "customDescriptionMethod" field.
     *
     * @param attribute the attribute to check
     */
    private static void setCustomDescription(Attribute2 attribute) {
        String customDescriptionMethod = "";
        if (attribute instanceof Producer2) {
            customDescriptionMethod = attribute.getMethod()
                    .getAnnotation(Producible.class).customDescriptionMethod();

        } else {
            customDescriptionMethod = attribute.getMethod()
                    .getAnnotation(Consumible.class).customDescriptionMethod();
        }
        if (!customDescriptionMethod.isEmpty()) {
            try {
                // TODO: Think about below
                Method descriptionMethod = attribute.getBaseObject().getClass()
                        .getMethod(customDescriptionMethod, null);
                String customDescription = (String) descriptionMethod
                        .invoke(attribute.getBaseObject(), null);
                attribute.setDescription(customDescription);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    // Helpers to make consumers and producers

    // Create a consumer that using a method name and a parameter signature
    public static Consumer2<?> getConsumer(Object object, String methodName) {
        return getConsumers(object).stream().filter(
                c -> c.getMethod().getName().equalsIgnoreCase(methodName))
                .findFirst().get();
    }

    public static Producer2<?> getProducer(Object object, String methodName) {
        return getProducers(object).stream().filter(
                p -> p.getMethod().getName().equalsIgnoreCase(methodName))
                .findFirst().get();
    }

    // Get a consumer that uses parameters and a fixed key.
    public static Consumer2<?> getConsumer(Object object, String methodName,
            Object key) {
        Consumer2<?> consumer = getConsumer(object, methodName);
        consumer.key = key;
        return consumer;
    }

    // TODO: This one not tested yet
    public static Producer2<?> getProducer(Object object, String methodName,
            Object key) {
        Producer2<?> producer = getProducer(object, methodName);
        producer.key = key;
        return producer;
    }

}

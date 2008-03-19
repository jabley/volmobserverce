/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.common;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;

/**
 * An ObservableProperties is a Properties object that wraps an existing
 * {@link Properties} object and allows {@link PropertyChangeListener}s to be
 * registered to listen for named property changes on its Properties object.
 */
public class ObservableProperties extends Properties {

    /**
     * The Properties object that is being observed.
     */
    private final Properties properties;

    /**
     * The PropertyChangeListener support which manages registered listeners.
     */
    private final PropertyChangeSupport changeSupport;

    /**
     * Creates a new ObservableProperties for the specified properties.
     * @param properties the Properties to observe. Cannot be null.
     * @throws IllegalArgumentException if properties is null
     */
    public ObservableProperties(Properties properties) {
        if (properties == null) {
            throw new IllegalArgumentException("Cannot be null: properties.");
        }
        this.properties = properties;
        changeSupport = new PropertyChangeSupport(this);
    }

    /**
     * Fires a PropertyChangeEvent in response to a change in a bound property.
     * The event will be delivered to all registered PropertyChangeListeners.
     * No event will be delivered if oldValue and newValue are the same.
     *
     * @param propertyName the name of the property that has changed
     * @param oldValue the property's previous value
     * @param newValue the property's new value
     */
    protected void firePropertyChange(String propertyName, Object oldValue,
                                      Object newValue) {
        changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Add a PropertyChangeListener for all properties. The listener will
     * be notified if any property changes.
     * @param listener the PropertyChangeListener to be added
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove a registered PropertyChangeListener..
     * @param listener the PropertyChangeListener to be removed
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Add a PropertyChangeListener for a specific property.  The listener
     * will be notified only when a call on firePropertyChange names that
     * specific property.
     *
     * @param propertyName The name of the property
     * @param listener The PropertyChangeListener to be added
     */
    public void addPropertyChangeListener(String propertyName,
                                          PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * Remove a PropertyChangeListener for a specific property.
     *
     * @param propertyName The name of the property
     * @param listener The PropertyChangeListener to be removed
     */
    public void removePropertyChangeListener(String propertyName,
                                             PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(propertyName, listener);
    }

    /**
     * Sets the value of a specific property and informs listeners of the
     * change.
     */
    // rest of javadoc inherited
    public synchronized Object setProperty(String propertyName,
                                           String newValue) {
        Object oldValue = properties.getProperty(propertyName);
        properties.setProperty(propertyName, newValue);

        firePropertyChange(propertyName, oldValue, newValue);

        return oldValue;
    }

    // javadoc inherited
    public void load(InputStream inStream) throws IOException {
        properties.load(inStream);
    }

    // javadoc inherited
    public void store(OutputStream out, String header) throws IOException {
        properties.store(out, header);
    }

    // javadoc inherited
    public String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }

    // javadoc inherited
    public String getProperty(String propertyName, String defaultValue) {
        return properties.getProperty(propertyName, defaultValue);
    }

    // javadoc inherited
    public Enumeration propertyNames() {
        return properties.propertyNames();
    }

    // javadoc inherited
    public void list(PrintStream out) {
        properties.list(out);
    }

    // javadoc inherited
    public void list(PrintWriter out) {
        properties.list(out);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-May-04	4068/1	allan	VBM:2004032103 Structure page policies section.

 26-Mar-04	3568/4	pcameron	VBM:2004032105 Added test case for ObservableProperties PropertyChangeListeners

 25-Mar-04	3568/2	pcameron	VBM:2004032105 Added ObservableProperties and refactored XMLDeviceRepositoryAccessor and DeviceRepositoryAccessorManager to use Properties

 ===========================================================================
*/

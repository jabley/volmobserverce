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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.interaction.event;

import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.model.property.PropertyIdentifier;

/**
 * Fired when the proxy associate with a bean property is changed.
 */
public class BeanPropertyChangedEvent
        extends InteractionEvent {

    /**
     * The property that changed.
     */
    private final PropertyIdentifier property;

    /**
     * The old proxy, may be null.
     */
    private final Proxy oldProxy;

    /**
     * The new proxy, may be null.
     */
    private final Proxy newProxy;

    /**
     * Initialise.
     *
     * @param source   The bean proxy that was the source of the event.
     * @param property The property that changed.
     * @param oldProxy The old proxy associated with the property, may be null.
     * @param newProxy The new proxy associated with the property, may be null.
     * @param originator Indicates if this is the originating event or not.
     */
    public BeanPropertyChangedEvent(
            BeanProxy source, PropertyIdentifier property,
            Proxy oldProxy,
            Proxy newProxy,
            boolean originator) {

        super(source, originator);
        if (property == null) {
            throw new IllegalArgumentException("property cannot be null");
        }
        this.property = property;
        this.oldProxy = oldProxy;
        this.newProxy = newProxy;
    }

    /**
     * Get the bean proxy that was the source of the event.
     *
     * @return The bean proxy that was the source of the event.
     */
    public BeanProxy getBeanProxy() {
        return (BeanProxy) getSource();
    }

    /**
     * Get the property that changed.
     *
     * @return The property that changed.
     */
    public PropertyIdentifier getProperty() {
        return property;
    }

    /**
     * Get the old proxy.
     *
     * @return The old proxy.
     */
    public Proxy getOldProxy() {
        return oldProxy;
    }

    /**
     * Get the new proxy.
     *
     * @return The new proxy.
     */
    public Proxy getNewProxy() {
        return newProxy;
    }

    // Javadoc inherited.
    public void dispatch(InteractionEventListener listener) {
        listener.propertySet(this);
    }

    // Javadoc inherited.
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        if (!(obj instanceof BeanPropertyChangedEvent)) {
            return false;
        }

        BeanPropertyChangedEvent other = (BeanPropertyChangedEvent) obj;
        return other.getProperty().equals(getProperty())
                && other.getOldProxy() == getOldProxy()
                && other.getNewProxy() == getNewProxy();
    }

    // Javadoc inherited.
    public int hashCode() {
        int result = super.hashCode();
        result = result * 37 + getProperty().hashCode();
        result = result * 37 + hashCode(getOldProxy());
        result = result * 37 + hashCode(getNewProxy());
        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Oct-05	9961/5	pduffin	VBM:2005101811 Committing restructuring

 25-Oct-05	9961/3	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/

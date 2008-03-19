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

import com.volantis.mcs.interaction.Proxy;

/**
 * Fired when the underlying model data for a proxy has changed.
 */
public class ProxyModelChangedEvent
        extends InteractionEvent {

    /**
     * The old model object, may be null.
     */
    private final Object oldValue;

    /**
     * The new model object, may be null.
     */
    private final Object newValue;

    /**
     * Initialise.
     *
     * @param source   The proxy that was the source of the event.
     * @param oldValue The old model object, may be null.
     * @param newValue The new model object, may be null.
     * @param originator Indicates this is the originator event.
     */
    public ProxyModelChangedEvent(
            Proxy source,
            Object oldValue,
            Object newValue,
            boolean originator) {

        super(source, originator);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * Get the old model object.
     *
     * @return The old model object.
     */
    public Object getOldValue() {
        return oldValue;
    }

    /**
     * Get the new model object.
     *
     * @return The new model object.
     */
    public Object getNewValue() {
        return newValue;
    }

    // Javadoc inherited.
    public void dispatch(InteractionEventListener listener) {
        listener.proxyModelChanged(this);
    }

    // Javadoc inherited.
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        if (!(obj instanceof ProxyModelChangedEvent)) {
            return false;
        }

        ProxyModelChangedEvent other = (ProxyModelChangedEvent) obj;
        return equals(other.getOldValue(), getOldValue())
                && equals(other.getNewValue(), getNewValue());
    }

    // Javadoc inherited.
    public int hashCode() {
        int result = super.hashCode();
        result = result * 37 + hashCode(oldValue);
        result = result * 37 + hashCode(newValue);
        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Oct-05	9961/3	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/

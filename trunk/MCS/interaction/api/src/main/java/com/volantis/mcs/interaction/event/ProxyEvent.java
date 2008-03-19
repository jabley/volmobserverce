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

import java.util.EventObject;

public class ProxyEvent extends EventObject {

    /**
     * Indicates this is the original eventor.
     */
    private final boolean originator;

    public ProxyEvent(Proxy source, boolean originator) {
        super(source);
        this.originator = originator;
    }

    /**
     * Get the proxy that was the source of the event.
     *
     * @return The proxy that was the source of the event.
     */
    public Proxy getProxy() {
        return (Proxy) getSource();
    }

    /**
     * Is this an originating event.
     *
     * @return true if it is the originating event.
     */
    public boolean isOriginator() {
        return originator;
    }

    // Javadoc inherited.
    public boolean equals(Object obj) {
        // If the objects are the same then they are always equal.
        if (obj == this) {
            return true;
        }
        
        if (!(obj instanceof ProxyEvent)) {
            return false;
        }

        ProxyEvent other = (ProxyEvent) obj;
        return other.getProxy() == getProxy();
    }

    // Javadoc inherited.
    public int hashCode() {
        return getProxy().hashCode();
    }

    protected int hashCode(Object o) {
        return o == null ? 0 : o.hashCode();
    }

    protected boolean equals(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Oct-05	9961/3	pduffin	VBM:2005101811 Added basic list operations

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added diagnostic support and some commands

 ===========================================================================
*/

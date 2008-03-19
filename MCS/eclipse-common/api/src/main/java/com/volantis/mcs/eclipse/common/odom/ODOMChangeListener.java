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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.common.odom;

import java.util.EventListener;

/**
 * Implementations of this interface can be registered with {@link
 * ODOMObservable} implementations and will be notified of (qualified) change
 * events within the ODOMObservable hierarchy.
 */
public interface ODOMChangeListener extends EventListener {
    /**
     * This method is called when a (qualified or unqualified) change event
     * occurs within the {@link ODOMObservable} hierarchy for which this
     * listener has been registered. The <code>node</code> and the {@link
     * ODOMChangeEvent#source} will be different when the <code>event</code> is
     * observed at a higher point in the ODOMObservable hierarchy than the
     * point at which the <code>event</code> originated.
     *
     * @param node  the node reporting the event (this will be one against
     *              which the listener has been registered)
     * @param event the change event
     */
    void changed(ODOMObservable node,
                 ODOMChangeEvent event);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 04-Nov-03	1613/1	philws	VBM:2003102101 Observable DOM

 ===========================================================================
*/

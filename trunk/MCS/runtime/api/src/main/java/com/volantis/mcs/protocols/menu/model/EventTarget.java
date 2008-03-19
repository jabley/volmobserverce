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

package com.volantis.mcs.protocols.menu.model;

import com.volantis.mcs.protocols.assets.ScriptAssetReference;

/**
 * Represents entities that can be targetted by event handlers.
 */
public interface EventTarget {
    /**
     * Returns the handler registered on the entity for the given type of
     * event, or null if a handler has not been registered. Each event handler
     * can be a component ID or a piece of literal text.
     *
     * @param type the type of event for which the handler is to be obtained.
     *             May not be null
     * @return the event handler for the given type of event or null if one is
     *         not registered
     */
    ScriptAssetReference getEventHandler(EventType type);
}



/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3491/1	philws	VBM:2004031912 Make Menu Model conform to updated Architecture

 ===========================================================================
*/

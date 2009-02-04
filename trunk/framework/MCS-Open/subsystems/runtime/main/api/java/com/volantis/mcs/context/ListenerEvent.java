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
package com.volantis.mcs.context;

import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.xml.namespace.ExpandedName;

/**
 * Event information associated with the XDIME 2 'listener' element.
 */
public class ListenerEvent {

    /**
     * The type of the event.
     */
    private ExpandedName eventType;

    /**
     * The script which implements the event.
     */
    private ScriptAssetReference handlerScript;

    /**
     * Initialise.
     *
     * @param eventType the type of event.
     * @param handlerScript the script which implements the event.
     */
    public ListenerEvent(ExpandedName eventType,
            ScriptAssetReference handlerScript) {
        this.eventType = eventType;
        this.handlerScript = handlerScript;
    }

    /**
     * @see #eventType
     */
    public ExpandedName getEventType() {
        return eventType;
    }

    /**
     * @see #handlerScript
     */
    public ScriptAssetReference getHandlerScript() {
        return handlerScript;
    }

}

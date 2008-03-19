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

import java.util.Map;
import java.util.HashMap;

/**
 * Register of scripts created from 'handler' elements.
 * <p>
 * Scripts are registered by the id of the handler element which contained them.
 * <p>
 * Only one script may be associated with each id.
 *
 * @mock.generate 
 */
public class HandlerScriptRegistry {

    /**
     * Map of handler id to script asset references.
     */
    private Map handlerId2ScriptMap = new HashMap();

    /**
     * Register the supplied script with the supplied handler id.
     *
     * @param handlerId the handler id to register the script by.
     * @param script the script to register.
     * @throws IllegalArgumentException if an attempt to register more than one
     *      script with the same id is made.
     */
    public void addScriptById(String handlerId, ScriptAssetReference script) {

        Object old = handlerId2ScriptMap.put(handlerId, script);

        // Prevent the same handler being registered twice.
        if (old != null) {
            throw new IllegalArgumentException(
                    "Attempt to register handler " + handlerId + " twice");
        }
    }

    /**
     * Look up a script by the supplied handler id.
     *
     * @param handlerId the handler id of the script to look up.
     * @return the script associated with this id, or null if none is found.
     */
    public ScriptAssetReference getScriptById(String handlerId) {

        return (ScriptAssetReference) handlerId2ScriptMap.get(handlerId);
    }

}

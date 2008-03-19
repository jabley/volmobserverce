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

package com.volantis.xml.pipeline.sax.dynamic;

import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import org.xml.sax.SAXException;

/**
 * Encapsulates the action that should be taken after an end element is
 * received for a rule.
 */
public interface EndElementAction {

    /**
     * A standard action that does nothing.
     */
    public static final EndElementAction DO_NOTHING = new EndElementAction() {
        public void doAction(DynamicProcess dynamicProcess) {
        }
    };

    /**
     * Do the action.
     *
     * @param dynamicProcess The dynamic process that activated the rule.
     *
     * @throws SAXException If there was a problem performing the action.
     */
    public void doAction(DynamicProcess dynamicProcess)
            throws SAXException;

}

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
 * $Header: /src/voyager/com/volantis/mcs/runtime/configuration/xml/digester/EnabledObjectCreateRule.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; extension to 
 *                              ObjectCreateRule which handles Enabled objects. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml.digester;

import our.apache.commons.digester.ObjectCreateRule;
import org.xml.sax.Attributes;

/**
 * Extension to {@link ObjectCreateRule} which handles {@link Enabled} 
 * objects.
 */ 
public class EnabledObjectCreateRule extends ObjectCreateRule {

    /**
     * Flag for always enabled objects.
     */
    private final boolean alwaysEnabled;

    public EnabledObjectCreateRule(Class clazz, boolean alwaysEnabled) {
        super(clazz);
        this.alwaysEnabled = alwaysEnabled;
    }

    // Inherit javadoc.
    public void begin(Attributes attributes) throws Exception {
        if (alwaysEnabled) {
            super.begin(attributes);
        } else {
            String enabled = attributes.getValue("enabled");
            if ("true".equals(enabled)) {
                // This element has been enabled, so just act as normal.
                super.begin(attributes);
            } else {
                // This element has not been enabled, so we want to ignore it.
                // Push a dummy object onto the stack.
                // We then need to make sure every reference to digester.peek()
                // checks for this object and does nothing in this case.
                digester.push(this);
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/

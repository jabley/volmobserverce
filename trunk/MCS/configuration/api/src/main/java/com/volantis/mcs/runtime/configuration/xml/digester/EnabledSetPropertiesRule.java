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
 * $Header: /src/voyager/com/volantis/mcs/runtime/configuration/xml/digester/EnabledSetPropertiesRule.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; extension to 
 *                              SetPropertiesRule which handles Enabled 
 *                              objects. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml.digester;

import our.apache.commons.digester.SetPropertiesRule;
import org.xml.sax.Attributes;
import com.volantis.mcs.runtime.configuration.xml.digester.EnabledObjectCreateRule;

/**
 * Extension to (@link SetPropertiesRule} which handles {@link Enabled} 
 * objects. 
 */ 
public class EnabledSetPropertiesRule extends SetPropertiesRule {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    public EnabledSetPropertiesRule(String attributeName, 
            String propertyName) {
        super(attributeName, propertyName);
    }

    public EnabledSetPropertiesRule(String[] attributeNames, 
            String[] propertyNames) {
        super(attributeNames, propertyNames);
    }

    // Inherit javadoc.
    public void begin(Attributes attributes) throws Exception {
        Object o = digester.peek();
        if (! (o instanceof EnabledObjectCreateRule)) {
             super.begin(attributes);
        } 
        // else, we are ignoring the object "created"
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

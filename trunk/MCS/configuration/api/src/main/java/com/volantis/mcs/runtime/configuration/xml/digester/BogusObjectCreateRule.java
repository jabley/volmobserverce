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
 * $Header: /src/voyager/com/volantis/mcs/runtime/configuration/xml/digester/BogusObjectCreateRule.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 18-Mar-03    Geoff           VBM:2002112102 - Created; a bogus Rule 
 *                              implementation that creates a new object and 
 *                              pushes it onto the object stack, using a 
 *                              nominated "type" attribute to determine the
 *                              particular type of object to be created.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml.digester;

import our.apache.commons.digester.Rule;
import org.xml.sax.Attributes;

import java.util.HashMap;
import java.util.Map;

/**
 * A bogus Rule implementation that creates a new object and pushes it
 * onto the object stack, using a nominated "type" attribute to determine the
 * particular type of object to be created.  When the element is complete, the
 * object will be popped.
 * <p>
 * This is considered to be bogus because it subverts the overall design of 
 * the Digester, and is required to support something in our config file that 
 * is hard to understand and should never have been implemented, namely 
 * having separate channel definitions which share the same tag.
 * 
 * @deprecated Do not use this for new functionality; it is strictly to 
 * support the bogus existing format of mcs-config.dtd. 
 */ 
public class BogusObjectCreateRule extends Rule {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Attribute that contains the "logical" type we wish to create.  
     */ 
    private String typeAttribute;
    
    /**
     * Maps "logical" type names into class names.
     */ 
    private Map typeMap = new HashMap();

    /**
     * Creates an instance of this object, using a type attribute of the name
     * specified.
     * 
     * @param typeAttribute the name of the attribute to be used for the 
     *      "logical" type.
     */ 
    public BogusObjectCreateRule(String typeAttribute) {
        this.typeAttribute = typeAttribute;
    }

    /**
     * Add a lookup to translate from "logicial" type value to an actual
     * class name.
     *  
     * @param type the "logical type value.
     * @param clazz the class to use for the logical type value.
     */ 
    public void addBogusClassLookup(String type, Class clazz) {
        typeMap.put(type, clazz.getName());
    }

    /**
     * Process the beginning of this element.
     *
     * @param attributes The attribute list of this element
     */
    public void begin(Attributes attributes) throws Exception {

        // Identify the name of the class to instantiate
        String className;
        String type = attributes.getValue(typeAttribute);
        if (type == null) {
            // Can't find the type attribute.
            throw new Exception("Mandatory type attribute '" + 
                    typeAttribute + "' non-existant");
        }            
        className = (String) typeMap.get(type);
        if (className == null) {
            // Can't find a registered class name for this type.
            throw new Exception("Type attribute '" + typeAttribute + 
                    "' value '" + type + "' is invalid");
        } 
        
        if (digester.getLogger().isDebugEnabled()) {
            digester.getLogger().debug("[BogusObjectCreateRule]{" + 
                    digester.getMatch() + "} New " + className);
        }

        // Instantiate the new object and push it on the context stack
        Class clazz = digester.getClassLoader().loadClass(className);
        Object instance = clazz.newInstance();
        digester.push(instance);

    }


    /**
     * Process the end of this element.
     */
    public void end() throws Exception {

        Object top = digester.pop();
        if (digester.getLogger().isDebugEnabled()) {
            digester.getLogger().debug("[BogusObjectCreateRule]{" +
                    digester.getMatch() + "} Pop " + top.getClass().getName());
        }

    }


    /**
     * Render a printable version of this Rule.
     */
    public String toString() {

        StringBuffer sb = new StringBuffer("BogusObjectCreateRule[");
        sb.append("typeAttribute=");
        sb.append(typeAttribute);
        sb.append(", typeMap=");
        sb.append(typeMap);
        sb.append("]");
        return (sb.toString());

    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 ===========================================================================
*/

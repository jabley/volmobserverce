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
package com.volantis.xml.pipeline.sax.impl.dynamic;

import com.volantis.xml.pipeline.sax.dynamic.NamespaceRuleSet;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;

import java.util.Map;
import java.util.HashMap;

/**
 * Simple implementation of the {@link com.volantis.xml.pipeline.sax.dynamic.NamespaceRuleSet} interface
 */
public class SimpleNamespaceRuleSet implements NamespaceRuleSet {

    /**
     * The namespace URI associated with this rule set
     */
    private String namespaceURI;

    /**
     * Map in which to store the registerd DynamicElementRule objects
     */
    private Map rules;

    /**
     * Creates a new <code>SimpleNamespaceRuleSet</code> instance
     * @param namespaceURI the namespace URI associated with this rule set
     */
    public SimpleNamespaceRuleSet(String namespaceURI) {
        if (namespaceURI == null) {
            throw new IllegalArgumentException(
                    "namespaceURI argument cannnot be null");
        }
        this.namespaceURI = namespaceURI;
        this.rules = new HashMap();
    }

    // javadoc inherited
    public String getNamespaceURI() {
        return namespaceURI;
    }

    // javadoc inherited
    public void addRule(String localName,
                        DynamicElementRule rule) {
        // ensure that the localName parameter is valid
        assertLocalName(localName);
        // ensure the rule is not null
        if (rule == null) {
            throw new IllegalArgumentException(
                    "rule must be non null");
        }
        // store the rule in the map overwriting any existing rule that
        // may be registered under the same name.
        rules.put(localName, rule);
    }

    // javadoc inherited
    public DynamicElementRule getRule(String localName) {
        // ensure that the localName parameter is valid
        assertLocalName(localName);
        return (DynamicElementRule)rules.get(localName);
    }

    /**
     * Ensure that a localName string is valid.
     * @param localName The String representation of the localName that
     * is being check
     * @throws IllegalArgumentException if the localName is null or empty
     */
    private void assertLocalName(String localName) {
        if (localName == null) {
            throw new IllegalArgumentException(
                    "localName parameter cannot be null");
        }
        if ("".equals(localName)) {
            throw new IllegalArgumentException(
                    "localName parameter cannot be an empty string");
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 04-Aug-03	280/1	doug	VBM:2003080108 Provided a default implementation of the NamespaceRuleSet interface

 ===========================================================================
*/

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
package com.volantis.xml.pipeline.sax.config;

import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.NamespaceRuleSet;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;
import com.volantis.xml.pipeline.sax.impl.dynamic.SimpleNamespaceRuleSet;
import com.volantis.xml.namespace.ExpandedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Mock implementation of the {@link DynamicProcessConfiguration} class
 */ 
public class MockDynamicProcessConfiguration 
        implements DynamicProcessConfiguration {

    /**
     * Map for storing the rules in
     */ 
    Map ruleSets = new HashMap();
    
    // javadoc inherited        
    public NamespaceRuleSet getNamespaceRules(String namespaceURI,
                                              boolean create) {
        NamespaceRuleSet ruleSet = getNamespaceRules(namespaceURI);
        if (null == ruleSet && create) {
            ruleSet = new SimpleNamespaceRuleSet(namespaceURI);
            ruleSets.put(namespaceURI, ruleSet);
        }
        return ruleSet;
    }

    // javadoc inherited
    public NamespaceRuleSet getNamespaceRules(String namespaceURI) {
        return (NamespaceRuleSet)ruleSets.get(namespaceURI);
    }

    // javadoc inherited
    public DynamicElementRule getRule(ExpandedName element) {
        DynamicElementRule rule = null;
        NamespaceRuleSet ruleSet = 
                getNamespaceRules(element.getNamespaceURI());
        if (ruleSet != null) {
            rule = ruleSet.getRule(element.getLocalName());
        }
        return rule;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 06-Aug-03	301/1	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 ===========================================================================
*/

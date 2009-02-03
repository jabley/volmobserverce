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
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.impl.dynamic;

import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.config.Configuration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.NamespaceRuleSet;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple implementation of the {@link com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration} interface
 */
public class SimpleDynamicProcessConfiguration
        implements Configuration, DynamicProcessConfiguration {

    /**
     * Map for storing the <code>DynamicElementRule</code> instances that are
     * registered with this DynamicProcessConfiguration
     */
    private Map ruleSets;

    /**
     * Creates a new XDynamicPipelineProcessConfiguration instance
     */
    public SimpleDynamicProcessConfiguration() {
        ruleSets = new HashMap();
    }

    // javadoc inherited
    public NamespaceRuleSet getNamespaceRules(String namespaceURI,
                                              boolean create) {

        // see if we already have a rule set registered for the
        // given namespace. Calling this method will throw an
        // IllegalArgumentException if the namespaceURI is null.
        NamespaceRuleSet namespaceRules = getNamespaceRules(namespaceURI);

        if (namespaceRules == null && create) {
            // create the NamespaceRuleSet and add it to the map
            namespaceRules = new SimpleNamespaceRuleSet(namespaceURI);
            ruleSets.put(namespaceURI, namespaceRules);
        }
        return namespaceRules;
    }

    // javadoc inherited
    public NamespaceRuleSet getNamespaceRules(String namespaceURI) {
        // namespace URI must be non null
        if (namespaceURI == null) {
            throw new IllegalArgumentException(
                    "namespaceURI argument cannot be null");
        }
        return (NamespaceRuleSet)ruleSets.get(namespaceURI);
    }

    // javadoc inherited
    public DynamicElementRule getRule(ExpandedName element) {
        if (element == null) {
            throw new IllegalArgumentException(
                    "element argument cannot be null");
        }
        DynamicElementRule rule = null;
        // retrieve the NamespaceRuleSet for the given namespace
        NamespaceRuleSet namespaceRules =
                getNamespaceRules(element.getNamespaceURI());
        // if a rule set was found then look for a rule.
        if (namespaceRules != null) {
            rule = namespaceRules.getRule(element.getLocalName());
        }
        // return the rule - this may be null
        return rule;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 06-Aug-03	301/4	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 04-Aug-03	294/1	allan	VBM:2003070709 Fixed merge conflicts

 04-Aug-03	217/11	allan	VBM:2003071702 Removed conflicts in comments.

 04-Aug-03	217/9	allan	VBM:2003071702 Tidied two lines and fix merge conflicts

 04-Aug-03	217/6	allan	VBM:2003071702 Filter nested anchors. Fixed merge conflicts.

 31-Jul-03	217/3	allan	VBM:2003071702 Fixed javadoc issue with contains and containsIdentity.

 31-Jul-03	217/1	allan	VBM:2003071702 Made HTTPMessageEntities into a set.

 04-Aug-03	285/1	doug	VBM:2003080402 Renamed XMLProcessConfiguration interface to Configuration

 01-Aug-03	258/2	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 25-Jul-03	242/1	steve	VBM:2003072310 Implement namespace package and refactor exitsting code to fit it

 ===========================================================================
*/

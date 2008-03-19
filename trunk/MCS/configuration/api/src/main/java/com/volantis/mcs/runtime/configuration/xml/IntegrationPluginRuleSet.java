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

package com.volantis.mcs.runtime.configuration.xml;

import our.apache.commons.digester.Digester;
import com.volantis.mcs.runtime.configuration.MarkupPluginConfiguration;

/**
 * Base of all rule sets used to create integration plugin configurations.
 */
public abstract class IntegrationPluginRuleSet
        extends PrefixRuleSet {

    private final String elementName;
    private final Class configurationClass;
    private final String addMethodName;
    private final String[] attributeNames;
    private final String[] propertyNames;

    /**
     * @param prefix The prefix for all the rules.
     * @param elementName The name of the element that triggers the creation
     * of the configuration object.
     * @param configurationClass The class of the configuration object.
     * @param addMethodName The method name used to add the object to the
     * containing object.
     * @param attributeNames An optional array of attribute names.
     * @param propertyNames An optional array of property names.
     */
    public IntegrationPluginRuleSet(String prefix,
                                    String elementName,
                                    Class configurationClass,
                                    String addMethodName,
                                    String [] attributeNames,
                                    String [] propertyNames) {
        super(prefix);
        this.elementName = elementName;
        this.configurationClass = configurationClass;
        this.addMethodName = addMethodName;
        this.attributeNames = attributeNames;
        this.propertyNames = propertyNames;
    }

    // javadoc inherited
    public void addRuleInstances(Digester digester) {
        final String pattern = prefix + "/" + elementName;

        digester.addObjectCreate(pattern, configurationClass);
        digester.addSetNext(pattern, addMethodName);

        digester.addSetProperties(pattern,
                new String[] {"name", "class"},
                new String[] {"name", "className"});
        if (attributeNames != null && propertyNames != null) {
            digester.addSetProperties(pattern, attributeNames, propertyNames);
        }

        ArgumentRuleSet argumentRuleSet =
                new ArgumentRuleSet(pattern + "/initialize");
        argumentRuleSet.addRuleInstances(digester);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Jan-05	6712/1	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 ===========================================================================
*/

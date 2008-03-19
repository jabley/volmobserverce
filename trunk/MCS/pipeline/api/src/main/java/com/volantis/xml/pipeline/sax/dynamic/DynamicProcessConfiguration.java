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

package com.volantis.xml.pipeline.sax.dynamic;

import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.config.Configuration;

/**
 * Contains the configuration that is used by the dynamic pipeline's controlling
 * process.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation. User implementations of this interface are highly likely to
 * be incompatible with future releases of the product at both binary and source
 * levels.</strong></p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface DynamicProcessConfiguration
        extends Configuration {

    /**
     * The volantis copyright statement
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Get the set of rules associated with the specified namespace.
     * <p>If a set of rules already exists for the namespace then this simply
     * returns the existing set of rules.<p>
     * <p>If no set of rules exists for the namespace then the create flag
     * determines the behaviour of this method. If it is false then this method
     * returns null, otherwise this method creates a new namespace rule set,
     * adds it to this object and returns it.</p>
     * @param namespaceURI The identifier for the namespace.
     * @param create Controls whether a new namespace rule set is created if
     * one does not exist already.
     * @return The namespace rule set, or null if one was not available.
     */
    public NamespaceRuleSet getNamespaceRules(String namespaceURI,
                                              boolean create);

    /**
     * Get the set of rules associated with the specified namespace.
     * @param namespaceURI The identifier for the namespace.
     * @return The namespace rule set, or null if one was not available.
     */
    public NamespaceRuleSet getNamespaceRules(String namespaceURI);

    /**
     * Get the rule associated with the specified element.
     * @param element The ExpandedName that identifies the element.
     * @return The rule associated with the element, or null.
     */
    public DynamicElementRule getRule(ExpandedName element);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 16-Jul-03	208/1	doug	VBM:2003071603 Added as part of Pipeline public API

 ===========================================================================
*/

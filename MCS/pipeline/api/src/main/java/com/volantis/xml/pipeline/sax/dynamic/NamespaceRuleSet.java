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

/**
 * Encapsulates a set of dynamic rules that belong to a single namespace.
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
public interface NamespaceRuleSet {

    /**
     * Get the namespace that contains the rules.
     * @return The namespace that contains the rules.
     */
    public String getNamespaceURI();

    /**
     * Add a rule to the current set.
     * @param localName The name of the element within this namespace.
     * @param rule The rule.
     */
    public void addRule(String localName,
                        DynamicElementRule rule);

    /**
     * Get the rule associated with the specified element within the current
     * namespace.
     * @param localName The name of the element within this namespace.
     * @return The rule associated with the element, or null if no such rule
     * exists.
     */
    public DynamicElementRule getRule(String localName);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 04-Aug-03	294/1	allan	VBM:2003070709 Fixed merge conflicts

 31-Jul-03	217/5	allan	VBM:2003071702 Fixed javadoc issue with contains and containsIdentity.

 31-Jul-03	217/3	allan	VBM:2003071702 Made HTTPMessageEntities into a set.

 28-Jul-03	217/1	allan	VBM:2003071702 Renamed and repacked set classes to http classes

 23-Jul-03	230/1	allan	VBM:2003072101 Restructure cookies, headers and request parameters and their containers. Remove PossiblyImmutable and HeaderConversions. Rename HttpFactory to HTTPFactory.

 16-Jul-03	208/1	doug	VBM:2003071603 Added as part of Pipeline public API

 ===========================================================================
*/

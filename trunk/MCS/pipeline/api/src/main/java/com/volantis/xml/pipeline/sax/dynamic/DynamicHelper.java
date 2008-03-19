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
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;

/**
 * Provides helper methods for using a dynamic pipeline.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @todo this stub class needs to be implemented
 * @deprecated Currently does nothing.
 */
public class DynamicHelper {

    /**
     * Create a DynamicProcessConfiguration
     * @return a DynamicProcessConfiguration
     */
    public static DynamicProcessConfiguration createConfiguration() {
        return null;
    }

    /**
     * Add a rule to the specified configuration
     * @param configuration the DynamicProcessConfiguration
     * @param element the ExpandedName that the rule is associated with
     * @param rule the DynamicElementRule that is to be added
     */
    public static void addRule(DynamicProcessConfiguration configuration,
                               ExpandedName element, DynamicElementRule rule) {

    }

    /**
     * Add a rule to the specified configuration
     * @param configuration the DynamicProcessConfiguration
     * @param namespaceURI the namespace URI that the rule is associated with
     * @param localName the local name that the rule is associated with
     * @param rule the DynamicElementRule that is to be added
     */
    public static void addRule(DynamicProcessConfiguration configuration,
                               String namespaceURI, String localName,
                               DynamicElementRule rule) {

    }

    /**
     * Adds the rules encapsulated in the configurator class to the
     * configuration.
     * @param configurator An encapsulation of a set of rules.
     */
    public static void addConfiguratorRules(
            DynamicProcessConfiguration configuration,
            DynamicRuleConfigurator configurator) {

    }

    /**
     * Create a dynamic process.
     * <p>The behaviour of the dynamic process is determined by the
     * {@link com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration}
     * instance that is stored in the pipeline configuration.</p>
     * @param configuration The pipeline configuration.
     * @return A new dynamic process.
     */
    public DynamicProcess createDynamicProcess(
            XMLPipelineConfiguration configuration) {
        return null;
    }

    public XMLProcess createDynamicProcess(
            DynamicProcessConfiguration configuration) {
        return null;
    }
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

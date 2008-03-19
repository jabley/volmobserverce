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
package com.volantis.mcs.runtime.configuration.xml.pipeline;

import com.volantis.mcs.runtime.configuration.xml.PrefixRuleSet;
import com.volantis.mcs.runtime.configuration.xml.CacheOperationRuleSet;
import com.volantis.mcs.runtime.configuration.pipeline.PipelineConfiguration;
import com.volantis.mcs.runtime.configuration.extensions.ConfigurationRuleSetExtensionFactory;
import our.apache.commons.digester.Digester;

/**
 * The <code>PrefixRuleSet</code> for the pipeline-configuration element
 */
public class PipelineConfigurationRuleSet extends PrefixRuleSet {

    /**
     * Initializes a <code>PipelineConfigurationRuleSet</code> instance
     * @param prefix the prefix associated with the rule
     */
    public PipelineConfigurationRuleSet(String prefix) {
        super(prefix);
    }
    // javadoc inherited
    public void addRuleInstances(Digester digester) {
        // register the rule for the PipelineConfiguration instance
        // <pipeline-configuration>
        String pattern = prefix + "/pipeline-configuration";
        digester.addObjectCreate(pattern, PipelineConfiguration.class);
        digester.addSetNext(pattern, "setPipelineConfiguration");

        // register the debug output rule
        // <pipeline-configuration>/<debug-output>
        String debugPattern = pattern + "/debug-output";
        digester.addSetProperties(debugPattern,
                new String[] { "directory" },
                new String[] { "debugOutputDirectory" });

        // register the various child process configuration process
        addProcessConfigurationRules(pattern, digester);
    }


    private void addProcessConfigurationRules(String prefix,
                                              Digester digester) {

        // extension point for defining custom markup for the pipeline-config
        // section of the mcs-config
        ConfigurationRuleSetExtensionFactory.getDefaultInstance().
                extendProcessRuleSet(prefix, digester);

        // add the cache operation rule set for the
        //<pipeline-configuration>/<caching-operation>
        CacheOperationRuleSet cacheOperationRuleSet =
                    new CacheOperationRuleSet(prefix);
        cacheOperationRuleSet.addRuleInstances(digester);

        // add the web driver rule set for the
        // <pipeline-configuration>/<web-driver> element
        WebDriverRuleSet webDriverRuleSet = new WebDriverRuleSet(prefix);
        webDriverRuleSet.addRuleInstances(digester);

        // add the web services rule set for the                              
        // <pipeline-configuration>/<web-services-driver> element
        WSDriverConfigurationRuleSet wsDriverConfigurationRuleSet =
                    new WSDriverConfigurationRuleSet(prefix);
        wsDriverConfigurationRuleSet.addRuleInstances(digester);

        // add the transform rule set for the
        // <pipeline-configuration>/<transform> element
        TransformConfigurationRuleSet transformRuleSet =
                    new TransformConfigurationRuleSet(prefix);
        transformRuleSet.addRuleInstances(digester);

        // Add the connection configuration rule set of the
        // .../pipeline-configuration/connection element
        ConnectionConfigurationRuleSet connectionConfigurationRuleSet =
                new ConnectionConfigurationRuleSet(prefix);
        connectionConfigurationRuleSet.addRuleInstances(digester);

        // add the rules for the
        // <pipeline-configuration>/<markup-extensions> element
        MarkupExtensionsRuleSet markupExtensionsRuleSet =
                           new MarkupExtensionsRuleSet(prefix);
        markupExtensionsRuleSet.addRuleInstances(digester);


    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Oct-05	9724/1	philws	VBM:2005092810 Port forward of the generic pipeline connection timeout functionality

 04-Oct-05	9679/1	philws	VBM:2005092810 Provide a connection timeout mechanism and configuration for pipeline operations

 01-Apr-05	6798/1	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 ===========================================================================
*/

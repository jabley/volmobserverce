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

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.mcs.runtime.configuration.pipeline.MarkupExtensionsConfiguration;
import com.volantis.mcs.runtime.configuration.pipeline.MarkupExtensionConfiguration;
import com.volantis.mcs.runtime.configuration.pipeline.PipelinePluginConfiguration;
import com.volantis.mcs.runtime.configuration.pipeline.RuleConfiguration;
import com.volantis.mcs.runtime.configuration.pipeline.ProcessConfiguration;
import com.volantis.mcs.runtime.configuration.xml.TestXmlConfigurationBuilder;

import java.util.Iterator;

/**
 * TestCase for the {@link MarkupExtensionsRuleSet}
 */
public class MarkupExtensionsRuleSetTestCase extends TestCaseAbstract {

    /**
     * Create a subset of the mcs-config XML document
     * @param pluginNames array of element names. If null NO
     * markup-extenstions element will be generated
     * @param pluginClassnames array of element names. If null NO
     * markup-extenstions element will be generated
     * @return a MarinerConfiguration instance
     * @throws ConfigurationException if an error occurs
     */
    private MarinerConfiguration createMarinerConfiguration(
                String[] extensionElements,
                String[] extensionNamespaces,
                String[] pluginNames,
                String[] pluginClassnames)
            throws ConfigurationException {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<pipeline-configuration>");
        if (extensionElements != null && extensionNamespaces != null &&
            pluginNames != null && pluginClassnames != null) {
            buffer.append("<markup-extensions>");
            for (int i=0; i<pluginNames.length; i++) {
                buffer.append("<markup-extension local-name=\"")
                      .append(extensionElements[i]).append("\" namespace-uri=\"")
                      .append(extensionNamespaces[i]).append("\"><")
                      .append(pluginNames[i])
                      .append(" class-name=\"").append(pluginClassnames[i])
                      .append("\"/></markup-extension>");
            }
            buffer.append("</markup-extensions>");
        }
        buffer.append("</pipeline-configuration>");

        TestXmlConfigurationBuilder configBuilder =
                new TestXmlConfigurationBuilder(buffer.toString());
        MarinerConfiguration marinerConfig = configBuilder.buildConfiguration();
        assertNotNull("MarinerConfig should not be null", marinerConfig);

        return marinerConfig;
    }

    /**
     * Ensures that a {@link MarkupExtensionsConfiguration}
     * instance is created if the <pipeline-configuration>/<markup-extensions>
     * element is present.
     * @throws Exception if an error occurs.
     */
    public void testMarkupExtensionsIsExtracted() throws Exception {
        String[] localNames = new String[] {"element1", "element2"};
        String[] namespaces = new String[] {"namespace1", "namespace2"};
        String[] pluginNames = new String[] {"rule", "process"};
        String[] pluginClasses = new String[] {"com.volantis.Rule",
                                               "com.volantis.Process"};
        MarinerConfiguration config = createMarinerConfiguration(
                    localNames,
                    namespaces,
                    pluginNames,
                    pluginClasses);

        MarkupExtensionsConfiguration extensionsConfig =
                    config.getPipelineConfiguration().
                        getMarkupExtensionsConfiguration();
        assertNotNull("markup-extensions element should have been parsed",
                      extensionsConfig);


        int extensionCount = 0;
        for(Iterator i = extensionsConfig.getMarkupExtensionConfgurations();
            i.hasNext(); extensionCount++) {
            i.next();
        }
        assertEquals("Wrong number of markup-extension object",
                     2,
                     extensionCount);

        int count = 0;
        for(Iterator i = extensionsConfig.getMarkupExtensionConfgurations();
            i.hasNext();) {
            MarkupExtensionConfiguration extensionConfig =
                       (MarkupExtensionConfiguration) i.next();
            assertNotNull("MarkupExtensionConfiguration cannot be null",
                          extensionConfig);

            assertEquals("unepected local-name",
                         localNames[count],
                         extensionConfig.getLocalName());

            assertEquals("unepected namespace-uri",
                         namespaces[count],
                         extensionConfig.getNamespaceURI());

            PipelinePluginConfiguration ppc =
                        extensionConfig.getPipelinePluginConfiguration();

            assertNotNull("PipelinePluginConfiguration should not be null",
                          ppc);

            if ("rule".equals(pluginNames[count])) {
                assertTrue("Expected a RuleConfiguration",
                           ppc instanceof RuleConfiguration);
            } else {
                assertTrue("Expected a ProcessConfiguration",
                           ppc instanceof ProcessConfiguration);
            }
            assertEquals("Classname is incorrect",
                         pluginClasses[count],
                         ppc.getClassName());

            count++;
        }
    }

    /**
     * Ensures that a {@link MarkupExtensionsConfiguration}
     * instance is NOT created if the
     * <pipeline-configuration>/<markup-extensions> element is NOT present.
     * @throws Exception if an error occurs.
     */
    public void testMarkupExtensionsIsNotExtracted() throws Exception {
        MarinerConfiguration config = createMarinerConfiguration(null,
                                                                 null,
                                                                 null,
                                                                 null);
        MarkupExtensionsConfiguration extensionsConfig =
                    config.getPipelineConfiguration().
                        getMarkupExtensionsConfiguration();

        assertNull("markup-extensions element should NOT have been parsed",
                   extensionsConfig);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Jun-05	7418/1	doug	VBM:2005021505 Simplified pipeline initialization

 ===========================================================================
*/

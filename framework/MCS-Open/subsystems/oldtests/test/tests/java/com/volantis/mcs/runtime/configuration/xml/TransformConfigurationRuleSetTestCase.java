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
package com.volantis.mcs.runtime.configuration.xml;

import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.pipeline.sax.operations.transform.TransformConfiguration;

/**
 */
public class TransformConfigurationRuleSetTestCase extends TestCaseAbstract {

    /**
     * Default junit constructor
     * @param name
     */
    public TransformConfigurationRuleSetTestCase(String name) {
        super(name);
    }

    /**
     * Create a subset of the mcs-config XML document from the values
     * supplied, parse it into a {@link TransformConfiguration} object,
     * and ensure that the values supplied are match those found.
     *
     * @param compile True if the transform should be compiled
     * @param cache True if the transform should be cached
     * @throws ConfigurationException
     */
    private MarinerConfiguration getMarinerConfiguration(Boolean compile,
                                                         Boolean cache)
            throws ConfigurationException {
        String doc = "";
        doc += "  <pipeline-configuration> \n";
        if (compile != null || cache != null) {
            doc += "    <transform";
            if (compile != null) {
                doc += " compile=\"" + compile + "\"";
            }
            if (cache != null) {
                doc += " cache=\"" + cache + "\"";
            }
            doc += "  /> \n";
        }
        doc += "  </pipeline-configuration> \n";

        TestXmlConfigurationBuilder configBuilder =
                new TestXmlConfigurationBuilder(doc);
        MarinerConfiguration config = configBuilder.buildConfiguration();
        assertNotNull(config);

        return config;
    }

    public void testCompileTrue() throws ConfigurationException {
        MarinerConfiguration config = getMarinerConfiguration(
                                                        Boolean.TRUE, null);

        TransformConfiguration transform =
                    config.getPipelineConfiguration().
                        getTransformConfiguration();
        assertTrue("compile set wrong",
                   transform.isTemplateCompilationRequired());
    }

    public void testCompileFalse() throws ConfigurationException {
        MarinerConfiguration config = getMarinerConfiguration(
                                                        Boolean.FALSE, null);

        TransformConfiguration transform =
                    config.getPipelineConfiguration().
                        getTransformConfiguration();
        assertTrue("compile set wrong",
                   !transform.isTemplateCompilationRequired());
    }

    public void testCacheTrue() throws ConfigurationException {
        MarinerConfiguration config = getMarinerConfiguration(
                                                        null, Boolean.TRUE);

        TransformConfiguration transform =
                    config.getPipelineConfiguration().
                        getTransformConfiguration();
        assertTrue("cache set wrong",
                   transform.isTemplateCacheRequired());
    }

    public void testCacheFalse() throws ConfigurationException {
        MarinerConfiguration config = getMarinerConfiguration(
                                                        null, Boolean.FALSE);

        TransformConfiguration transform =
                    config.getPipelineConfiguration().
                        getTransformConfiguration();
        assertTrue("cache set wrong",
                   !transform.isTemplateCacheRequired());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Apr-05	6798/3	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 23-Jan-04	2714/2	claire	VBM:2004012203 Updated transform configuration to support template cache attribute

 06-Jan-04	2271/1	geoff	VBM:2003121716 Import/Export: Schemify configuration file: Enable schema validation

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 13-Oct-03	1517/1	pcameron	VBM:2003100706 Removed all traces of licensing from MCS

 07-Aug-03	906/3	chrisw	VBM:2003072905 Public API changed for transform configuration

 05-Aug-03	906/1	chrisw	VBM:2003072905 implemented compilable attribute on transform

 ===========================================================================
*/

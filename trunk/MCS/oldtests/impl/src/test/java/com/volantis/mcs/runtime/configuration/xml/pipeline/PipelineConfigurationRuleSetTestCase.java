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
import com.volantis.mcs.runtime.configuration.xml.TestXmlConfigurationBuilder;
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;

import java.io.File;

/**
 * Test Case for the {@link PipelineConfigurationRuleSet} class
 */
public class PipelineConfigurationRuleSetTestCase extends TestCaseAbstract {

    /**
     * Ensures that the <debug-output> element is parsed correctly when it ends
     * in File.seperator
     * @throws Exception if an error occurs
     */
    public void testDebugDirectory() throws Exception {
        String xml = "<pipeline-configuration>" +
                     "    <debug-output directory=\"dir" + File.separator + "\"/>" +
                     "</pipeline-configuration>";

        TestXmlConfigurationBuilder configBuilder =
            new TestXmlConfigurationBuilder(xml);

        MarinerConfiguration marinerConfig = configBuilder.buildConfiguration();
        assertEquals("debug directory is incorrect",
                     "dir" + File.separator,
                     marinerConfig.getPipelineConfiguration().
                        getDebugOutputDirectory());
    }

    /**
     * Ensures that the <debug-output> element is parsed correctly when it does
     * not end in File.seperator.
     *
     * @throws Exception if an error occurs
     */
    public void testDebugDirectoryWithoutSlash() throws Exception {
        String xml = "<pipeline-configuration>" +
                "    <debug-output directory=\"dir\"/>" +
                "</pipeline-configuration>";

        TestXmlConfigurationBuilder configBuilder =
                new TestXmlConfigurationBuilder(xml);

        MarinerConfiguration marinerConfig = configBuilder.buildConfiguration();
        // ensure that File.separator has not been appended to debug directory
        assertEquals("debug directory is incorrect",
                     "dir" ,
                     marinerConfig.getPipelineConfiguration().
                     getDebugOutputDirectory());
    }

    /**
     * Ensures that the <debug-output> element is handled correctly when it
     * is not specified
     * @throws Exception if an error occurs
     */
    public void testDebugDirectoryNotProvided() throws Exception {
        String xml = "<pipeline-configuration>" +
                     "</pipeline-configuration>";

        TestXmlConfigurationBuilder configBuilder =
            new TestXmlConfigurationBuilder(xml);

        MarinerConfiguration marinerConfig = configBuilder.buildConfiguration();
        assertNull("expected null debug directory",                       
                     marinerConfig.getPipelineConfiguration().
                        getDebugOutputDirectory());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-May-05	8135/1	matthew	VBM:2005050905 force debugOutputDirectory to end in File.seperator

 01-Apr-05	6798/1	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 ===========================================================================
*/

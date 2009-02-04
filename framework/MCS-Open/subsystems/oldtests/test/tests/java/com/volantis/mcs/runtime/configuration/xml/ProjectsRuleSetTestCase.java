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

import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.mcs.runtime.configuration.project.ProjectsConfiguration;
import com.volantis.mcs.runtime.configuration.project.AbstractPoliciesConfiguration;
import com.volantis.mcs.runtime.configuration.project.XmlPoliciesConfiguration;
import com.volantis.mcs.runtime.configuration.project.AssetsConfiguration;
import com.volantis.mcs.runtime.configuration.project.AssetConfiguration;
import com.volantis.mcs.runtime.configuration.project.JdbcPoliciesConfiguration;
import com.volantis.mcs.runtime.configuration.project.GeneratedResourcesConfiguration;
import com.volantis.mcs.runtime.configuration.project.RuntimeProjectConfiguration;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * A test case for {@link ProjectsRuleSetTestCase}.
 */ 
public class ProjectsRuleSetTestCase extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Test the basic functionality of the rule set.
     * <p>
     * This is deliberately minimal since we have a looming deadline.
     * 
     * @throws ConfigurationException
     */ 
    public void testTheBasics() throws ConfigurationException {
        String doc = ""; 
        doc += 
                "<projects> " +
                  "<default preload=\"true\"> " +
                    "<xml-policies directory=\"an xml dir\" /> " +
                    "<assets base-url=\"a/base/url\"> " +
                      "<audio-assets prefix-url=\"a/prefix/url\" /> " +
                      // "... " +
                    "</assets> " +
                    "<generated-resources base-dir=\"default-resources\" />" +
                  "</default> " +
                  "<project name=\"a/project\"> " +
                    "<jdbc-policies name=\"a jdbc policy\" /> " +
                    "<assets base-url=\"another/base/url\"> " +
                      "<dynamic-visual-assets prefix-url=\"another/prefix/url\" /> " +
                      // "... " +
                    "</assets> " +
                    "<generated-resources base-dir=\"custom-resources\" />" +
                  "</project> " +
                  // "... " +
                "</projects> ";
        
        // Parse the XML and create a configuration object tree from it.
        TestXmlConfigurationBuilder configBuilder = 
                new TestXmlConfigurationBuilder(doc);
        configBuilder.setAddDefaultProjects(false);
        MarinerConfiguration config = configBuilder.buildConfiguration();
        
        // Check we got back the top level objects.
        assertNotNull(config);
        ProjectsConfiguration projects = config.getProjects();
        assertNotNull(projects);

        // Test the default project.
        {
            RuntimeProjectConfiguration project = projects.getDefaultProject();
            assertNotNull(project);
            assertEquals("", Boolean.TRUE, project.getPreload());

            AbstractPoliciesConfiguration policies =
                    project.getPolicies();
            assertNotNull(policies);
            assertTrue(policies instanceof XmlPoliciesConfiguration);
            XmlPoliciesConfiguration xmlPolicies =
                    (XmlPoliciesConfiguration) policies;
            assertEquals("", "an xml dir", xmlPolicies.getDirectory());

            AssetsConfiguration assets = project.getAssets();
            assertNotNull(assets);
            AssetConfiguration audioAssets = assets.getAudioAssets();
            assertNotNull(audioAssets);
            assertEquals("", "a/prefix/url", audioAssets.getPrefixUrl());

            GeneratedResourcesConfiguration resources =
                    project.getGeneratedResources();
            assertNotNull(resources);
            assertEquals("", "default-resources", resources.getBaseDir());
        }

        // Test the single named project which was present.
        {
            RuntimeProjectConfiguration project = (RuntimeProjectConfiguration)
                    projects.getNamedProjects().values().iterator().next();
            assertNotNull(project);
            assertEquals("", "a/project", project.getName());

            AbstractPoliciesConfiguration policies = project.getPolicies();
            assertNotNull(policies);
            assertTrue(policies instanceof JdbcPoliciesConfiguration);
            JdbcPoliciesConfiguration jdbcPolicies =
                    (JdbcPoliciesConfiguration) policies;
            assertEquals("", "a jdbc policy", jdbcPolicies.getName());

            AssetsConfiguration assets = project.getAssets();
            assertNotNull(assets);
            AssetConfiguration dynvisAssets = assets.getDynamicVisualAssets();
            assertNotNull(dynvisAssets);
            assertEquals("", "another/prefix/url", dynvisAssets.getPrefixUrl());

            GeneratedResourcesConfiguration resources =
                    project.getGeneratedResources();
            assertNotNull(resources);
            assertEquals("", "custom-resources", resources.getBaseDir());
        }
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Oct-04	6027/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 28-Oct-04	5897/2	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 25-May-04	4507/1	geoff	VBM:2004051809 pre populate policy caches

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 26-Jan-04	2724/1	geoff	VBM:2004011911 Add projects to config

 ===========================================================================
*/

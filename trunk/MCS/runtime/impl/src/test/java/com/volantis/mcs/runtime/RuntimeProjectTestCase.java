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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime;

import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.project.InternalProjectFactory;
import com.volantis.mcs.project.PolicySource;
import com.volantis.mcs.project.ProjectFactory;
import com.volantis.mcs.project.remote.RemotePolicySource;
import com.volantis.mcs.runtime.configuration.project.AssetConfiguration;
import com.volantis.mcs.runtime.configuration.project.AssetsConfiguration;
import com.volantis.mcs.utilities.MarinerURL;
import junit.framework.TestCase;

/**
 * Tests RuntimeProject
 */
public class RuntimeProjectTestCase
        extends TestCase {

    /**
     * Base URL used for all of the tests
     */
    private String baseURLString = "/volantis/";

    /**
     * Audio prefix URL used for all of the tests
     */
    private String audioPrefixString = "/audio/";

    /**
     * Dynamic visual prefix URL used for all of the tests
     */
    private String dynamicVisualPrefixString = "/dynvis/";

    /**
     * Image prefix URL used for all of the tests
     */
    private String imagePrefixString = "/image/";

    /**
     * Exript prefix URL used for all of the tests
     */
    private String scriptPrefixString = "/script/";

    /**
     * Text prefix URL used for all of the tests
     */
    private String textPrefixString = "/text/";


    /**
     * Standard junit constructor
     *
     * @param name The name of the test
     */
    public RuntimeProjectTestCase(String name) {
        super(name);
    }

    /**
     * Test that the base URL is set correctly.
     *
     * @throws Exception If any test fails.
     */
    public void testBaseURL() throws Exception {
        RuntimeProject project = createNewProject(baseURLString, false);
        MarinerURL url = project.getAssetsBaseURL();
        assertNotNull("URL should exist", url);
        assertEquals("Project and test URLs should match",
                url, new MarinerURL(baseURLString));
    }

    /**
     * Test that the audio prefix URL is set correctly.
     *
     * @throws Exception If any test fails.
     */
    public void testAudioPrefixURL() throws Exception {
        RuntimeProject project = createNewProject(baseURLString, false);
        MarinerURL url = project.getPrefixURL(VariantType.AUDIO);
        assertNotNull("URL should exist", url);
        assertEquals("Project and test URLs should match",
                url, new MarinerURL(audioPrefixString));
    }

    /**
     * Test that the dynamic visual prefix URL is set correctly.
     *
     * @throws Exception If any test fails.
     */
    public void testDynamicVisualPrefixURL() throws Exception {
        RuntimeProject project = createNewProject(baseURLString, false);
        MarinerURL url = project.getPrefixURL(VariantType.VIDEO);
        assertNotNull("URL should exist", url);
        assertEquals("Project and test URLs should match",
                url, new MarinerURL(dynamicVisualPrefixString));
    }

    /**
     * Test that the image prefix URL is set correctly.
     *
     * @throws Exception If any test fails.
     */
    public void testImagePrefixURL() throws Exception {
        RuntimeProject project = createNewProject(baseURLString, false);
        MarinerURL url = project.getPrefixURL(VariantType.IMAGE);
        assertNotNull("URL should exist", url);
        assertEquals("Project and test URLs should match",
                url, new MarinerURL(imagePrefixString));
    }

    /**
     * Test that the exript prefix URL is set correctly.
     *
     * @throws Exception If any test fails.
     */
    public void testScriptPrefixURL() throws Exception {
        RuntimeProject project = createNewProject(baseURLString, false);
        MarinerURL url = project.getPrefixURL(VariantType.SCRIPT);
        assertNotNull("URL should exist", url);
        assertEquals("Project and test URLs should match",
                url, new MarinerURL(scriptPrefixString));
    }

    /**
     * Test that the text prefix URL is set correctly.
     *
     * @throws Exception If any test fails.
     */
    public void testTextPrefixURL() throws Exception {
        RuntimeProject project = createNewProject(baseURLString, false);
        MarinerURL url = project.getPrefixURL(VariantType.TEXT);
        assertNotNull("URL should exist", url);
        assertEquals("Project and test URLs should match",
                url, new MarinerURL(textPrefixString));
    }

    /**
     * Tests the policy retrieval method.
     *
     * @throws Exception If any test fails.
     */
    public void testGetPolicy() throws Exception {
        RuntimeProject project = createNewProject(baseURLString, false);
        PolicySource policySource = project.getPolicySource();
        assertNull("Should be a null policy", policySource);
    }

    /**
     * A utility method to create an instance of RuntimeProject.
     *
     * @param baseURL The baseURL to use for the project.
     * @param remote
     * @return An initialised instance of RuntimeProject.
     */
    private RuntimeProject createNewProject(String baseURL, boolean remote) {
        RuntimeProjectBuilder builder = new RuntimeProjectBuilder();
        // Addditional set-up of the project may go here
        builder.setAssetsConfiguration(createConfig(baseURL));
        builder.setPolicyRootAsString("file:/a/b/");
        builder.setRemote(remote);

        InternalProjectFactory factory = (InternalProjectFactory)
                ProjectFactory.getDefaultInstance();

        if (remote) {
            RemotePolicySource source = factory.createRemotePolicySource(
                    null, "file:/a/b/");
            builder.setPolicySource(source);
        }

        // Addditional set-up of the project may go here
        RuntimeProject project = builder.getProject();
        return project;
    }

    /**
     * A utility method to create an AssetsConfiguration with the
     * specified baseURL.
     *
     * @param baseURL The baseURL to use in the AssetsConfiguration.
     * @return An initialised instance of AssetsConfiguration.
     */
    private AssetsConfiguration createConfig(String baseURL) {
        // Base variables
        AssetsConfiguration assetConfig = new AssetsConfiguration();
        AssetConfiguration asset;

        // Setting the prefixes
        asset = new AssetConfiguration();
        asset.setPrefixUrl(audioPrefixString);
        assetConfig.setAudioAssets(asset);
        asset = new AssetConfiguration();
        asset.setPrefixUrl(dynamicVisualPrefixString);
        assetConfig.setDynamicVisualAssets(asset);
        asset = new AssetConfiguration();
        asset.setPrefixUrl(imagePrefixString);
        assetConfig.setImageAssets(asset);
        asset = new AssetConfiguration();
        asset.setPrefixUrl(scriptPrefixString);
        assetConfig.setScriptAssets(asset);
        asset = new AssetConfiguration();
        asset.setPrefixUrl(textPrefixString);
        assetConfig.setTextAssets(asset);

        // Setting the base URL
        assetConfig.setBaseUrl(baseURL);

        return assetConfig;
    }

    /**
     * Ensure that project can determine that it owns a policy.
     */
    public void testProjectContainsPolicy() {
        RuntimeProject project = createNewProject(baseURLString, false);
        assertTrue(project.containsPolicy("file:/a/b/c/d.mimg"));
    }

    /**
     * Ensure that project can determine that it does not own a policy.
     */
    public void testProjectDoesNotContainPolicy() {
        RuntimeProject project = createNewProject(baseURLString, false);
        assertFalse(project.containsPolicy("file:/b/c/d.mimg"));
    }

    /**
     * Ensure that the containsPolicy method is unsupported on the global
     * project.
     */
    public void testContainsPolicyUnsupportedOnGlobalProject() {
        RuntimeProject project = createGlobalProject();
        try {
            project.containsPolicy("file:/a/b/c.mimg");
            fail("Method should be unsupported on global project");
        } catch (UnsupportedOperationException expected) {
        }
    }

    private RuntimeProject createGlobalProject() {
        RuntimeProjectBuilder builder = new RuntimeProjectBuilder();
        builder.setContainsOrphans(true);
        RuntimeProject project = builder.getProject();
        return project;
    }

    /**
     * Ensure that making a project relative path works.
     */
    public void testMakeProjectRelative() {
        RuntimeProject project = createNewProject(baseURLString, false);
        assertEquals("/c/d.mimg", project.makeProjectRelativePath(
                new MarinerURL("file:/a/b/c/d.mimg"), true));
    }

    /**
     * Ensure that making a project relative path fails.
     */
    public void testMakeProjectRelativeFailed() {
        RuntimeProject project = createNewProject(baseURLString, false);
        try {
            project.makeProjectRelativePath(new MarinerURL("file:/b/c/d.mimg"), true);
            fail("Should fail to create project relative path");
        } catch (IllegalArgumentException expected) {
        }
    }

    /**
     * Ensure that making a project relative path on global fails if the URL is
     * not absolute.
     */
    public void testMakeProjectRelativeGlobalFails() {
        RuntimeProject project = createGlobalProject();
        try {
            project.makeProjectRelativePath(new MarinerURL("file:/b/c/d.mimg"), true);
            fail("Should fail to create project relative path");
        } catch (UnsupportedOperationException expected) {
        }
    }

    public void testMakeAbsolutePolicyURL() {
        RuntimeProject project = createNewProject(baseURLString, true);
        assertEquals("file:/a/b/c/d.mimg", project.makeAbsolutePolicyURL(
                "/c/d.mimg"));
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

 17-Feb-04	3041/1	claire	VBM:2004021208 Refactored RuntimeProject and added a unit test

 ===========================================================================
*/

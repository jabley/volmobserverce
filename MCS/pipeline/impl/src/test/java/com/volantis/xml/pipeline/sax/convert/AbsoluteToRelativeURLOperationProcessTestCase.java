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

package com.volantis.xml.pipeline.sax.convert;

import junit.framework.TestCase;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.IntegrationTestHelper;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.shared.environment.EnvironmentInteraction;

/**
 * Test the <code>AbsoluteToRelativeURLOperationProcess</code> object.
 */
public class AbsoluteToRelativeURLOperationProcessTestCase extends TestCase {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Test the convert from an absolute to relative url.
     */
    public void testConvertAbsoluteURLToRelativeURL() throws Exception {
        XMLPipelineFactory pipelineFactory =
                new IntegrationTestHelper().getPipelineFactory();

        XMLPipelineConfiguration pipelineConfiguration =
                createPipelineConfiguration();

        // Create a XMLPipelineContext
        XMLPipelineContext pipelineContext =
                pipelineFactory.createPipelineContext(
                        pipelineConfiguration,
                        (EnvironmentInteraction)null);

        // create a dynamic pipeine
        XMLPipeline pipeline = pipelineFactory.createDynamicPipeline(pipelineContext);

        AbsoluteToRelativeURLOperationProcess process =
            new AbsoluteToRelativeURLOperationProcess();
        process.setPipeline(pipeline);

        // Test 'ordinary' urls
        String relativeURL = "img.jpg";
        String baseURLNoMatch = "http://www.nomatch.com/test";
        String baseURLMatch = "http://www.test.com/testing";
        String absoluteURL = "http://www.test.com/testing/" + relativeURL;

        doTest(process, null, null, null, null);
        doTest(process, baseURLNoMatch, null, null, null);
        doTest(process, null, absoluteURL, null, absoluteURL);
        doTest(process, baseURLNoMatch, absoluteURL, null, absoluteURL);
        doTest(process, baseURLMatch, absoluteURL, null, relativeURL);
        doTest(process, baseURLMatch + "/", absoluteURL, null, relativeURL);

        String subPath = "images/";
        doTest(process, null, null, subPath, null);
        doTest(process, baseURLNoMatch, null, subPath, null);
        doTest(process, null, absoluteURL, subPath, absoluteURL);
        doTest(process, baseURLNoMatch, absoluteURL, subPath, absoluteURL);
        doTest(process, baseURLMatch, absoluteURL, subPath, subPath + relativeURL);
        doTest(process, baseURLMatch + "/", absoluteURL, subPath, subPath + relativeURL);


        // Test urls where the base has a port number
        baseURLNoMatch = "http://www.nomatch.com:80/test";
        baseURLMatch = "http://www.test.com:80/testing";
        absoluteURL = "http://www.test.com/testing/" + relativeURL;

        doTest(process, null, null, null, null);
        doTest(process, baseURLNoMatch, null, null, null);
        doTest(process, null, absoluteURL, null, absoluteURL);
        doTest(process, baseURLNoMatch, absoluteURL, null, absoluteURL);
        doTest(process, baseURLMatch, absoluteURL, null, relativeURL);
        doTest(process, baseURLMatch + "/", absoluteURL, null, relativeURL);

        subPath = "images/";
        doTest(process, null, null, subPath, null);
        doTest(process, baseURLNoMatch, null, subPath, null);
        doTest(process, null, absoluteURL, subPath, absoluteURL);
        doTest(process, baseURLNoMatch, absoluteURL, subPath, absoluteURL);
        doTest(process, baseURLMatch, absoluteURL, subPath, subPath + relativeURL);
        doTest(process, baseURLMatch + "/", absoluteURL, subPath, subPath + relativeURL);


        // test urls where the input has a port number
        baseURLNoMatch = "http://www.nomatch.com/test";
        baseURLMatch = "http://www.test.com/testing";
        absoluteURL = "http://www.test.com:80/testing/" + relativeURL;

        doTest(process, null, null, null, null);
        doTest(process, baseURLNoMatch, null, null, null);
        doTest(process, null, absoluteURL, null, absoluteURL);
        doTest(process, baseURLNoMatch, absoluteURL, null, absoluteURL);
        doTest(process, baseURLMatch, absoluteURL, null, relativeURL);
        doTest(process, baseURLMatch + "/", absoluteURL, null, relativeURL);

        subPath = "images/";
        doTest(process, null, null, subPath, null);
        doTest(process, baseURLNoMatch, null, subPath, null);
        doTest(process, null, absoluteURL, subPath, absoluteURL);
        doTest(process, baseURLNoMatch, absoluteURL, subPath, absoluteURL);
        doTest(process, baseURLMatch, absoluteURL, subPath, subPath + relativeURL);
        doTest(process, baseURLMatch + "/", absoluteURL, subPath, subPath + relativeURL);


        // test urls where both input and base have port numbers
        baseURLNoMatch = "http://www.nomatch.com:80/test";
        baseURLMatch = "http://www.test.com:80/testing";
        absoluteURL = "http://www.test.com:80/testing/" + relativeURL;

        doTest(process, null, null, null, null);
        doTest(process, baseURLNoMatch, null, null, null);
        doTest(process, null, absoluteURL, null, absoluteURL);
        doTest(process, baseURLNoMatch, absoluteURL, null, absoluteURL);
        doTest(process, baseURLMatch, absoluteURL, null, relativeURL);
        doTest(process, baseURLMatch + "/", absoluteURL, null, relativeURL);

        subPath = "images/";
        doTest(process, null, null, subPath, null);
        doTest(process, baseURLNoMatch, null, subPath, null);
        doTest(process, null, absoluteURL, subPath, absoluteURL);
        doTest(process, baseURLNoMatch, absoluteURL, subPath, absoluteURL);
        doTest(process, baseURLMatch, absoluteURL, subPath, subPath + relativeURL);
        doTest(process, baseURLMatch + "/", absoluteURL, subPath, subPath + relativeURL);
    }

    /**
     * Do the test with the input and expected output.
     *
     * @param process  the operation process.
     * @param baseURL  the base url.
     * @param url      the url.
     * @param expected the expected result.
     */
    private void doTest(AbsoluteToRelativeURLOperationProcess process,
                        String baseURL,
                        String url,
                        String substitution,
                        String expected) throws Exception {

        String result = process.
                convertAbsoluteURLToRelativeURL(baseURL, substitution, url);
        assertEquals("Result should match", expected, result);
    }

    /**
     * Factory method for creating an XMLPipelineConfiguration. This default
     * impelementation simple returns the configuration that
     * {@link IntegrationTestHelper#getPipelineConfiguration} returns
     *
     * @return An XMLPipelineConfiguration instance
     */
    protected XMLPipelineConfiguration createPipelineConfiguration() {
        XMLPipelineConfiguration configuration = null;
        try {
            configuration =
                    new IntegrationTestHelper().getPipelineConfiguration();
        } catch (Exception e) {
            fail("Unable to create XMLPipelineConfiguration for test");
        }
        return configuration;
    }

    /**
     * Test that getBaseURL returns values explicitly set on the operation
     * ahead of values set on the process configuration.
     */
    public void testGetBaseURL() throws Exception {
        XMLPipelineFactory pipelineFactory =
                new IntegrationTestHelper().getPipelineFactory();

        XMLPipelineConfiguration pipelineConfiguration =
                createPipelineConfiguration();
        AbsoluteToRelativeURLConfiguration config =
                (AbsoluteToRelativeURLConfiguration) pipelineConfiguration.
                retrieveConfiguration(AbsoluteToRelativeURLConfiguration.class);

        // Create a XMLPipelineContext
        XMLPipelineContext pipelineContext =
                pipelineFactory.createPipelineContext(
                        pipelineConfiguration,
                        (EnvironmentInteraction)null);

        // create a dynamic pipeine
        XMLPipeline pipeline = pipelineFactory.
                createDynamicPipeline(pipelineContext);

        AbsoluteToRelativeURLOperationProcess process =
            new AbsoluteToRelativeURLOperationProcess();
        process.setPipeline(pipeline);

        assertNull("Unexpected value for baseURL.", process.getBaseURL());

        String configURL = "http://www.setonconfig.com";
        config.setBaseURL(configURL);

        assertEquals("Unexpected value for baseURL.",
                configURL, process.getBaseURL());

        String attributeURL = "http://www.setonattribute.com";
        process.setBaseURL(attributeURL);

        assertEquals("Unexpected value for baseURL.",
                attributeURL, process.getBaseURL());
    }

    /**
     * Test that getSubstitutionPath returns values explicitly set on the
     * operation ahead of values set on the process configuration.
     */
    public void testGetSubstitutionPath() throws Exception {
        XMLPipelineFactory pipelineFactory =
                new IntegrationTestHelper().getPipelineFactory();

        XMLPipelineConfiguration pipelineConfiguration =
                createPipelineConfiguration();
        AbsoluteToRelativeURLConfiguration config =
                (AbsoluteToRelativeURLConfiguration) pipelineConfiguration.
                retrieveConfiguration(AbsoluteToRelativeURLConfiguration.class);

        // Create a XMLPipelineContext
        XMLPipelineContext pipelineContext =
                pipelineFactory.createPipelineContext(
                        pipelineConfiguration,
                        (EnvironmentInteraction)null);

        // create a dynamic pipeine
        XMLPipeline pipeline = pipelineFactory.
                createDynamicPipeline(pipelineContext);

        AbsoluteToRelativeURLOperationProcess process =
            new AbsoluteToRelativeURLOperationProcess();
        process.setPipeline(pipeline);

        assertNull("Unexpected value for SubstitutionPath.",
                process.getSubstitutionPath());

        String configURL = "setonconfig/";
        config.setSubstitutionPath(configURL);

        assertEquals("Unexpected value for SubstitutionPath.",
                configURL, process.getSubstitutionPath());

        String attributeURL = "setonattribute/";
        process.setSubstitutionPath(attributeURL);

        assertEquals("Unexpected value for SubstitutionPath.",
                attributeURL, process.getSubstitutionPath());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Mar-04	656/1	adrian	VBM:2004031602 Updated pipeline with DSB enhancement requests

 30-Mar-04	653/1	adrian	VBM:2004033005 Updated AbsoluteToRelativeURL process to use PS updated for DSB

 23-Mar-04	624/1	adrian	VBM:2004031904 Updated AbsoluteToRelativeURL process

 31-Jan-04	533/2	adrian	VBM:2004011906 updated AbsoluteToRelativeURL process to resolve against the base url in the pipeline context

 08-Aug-03	308/1	byron	VBM:2003080507 Provide ConvertAbsoluteToRelativeURL pipeline process

 ===========================================================================
*/

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
package com.volantis.mcs.integration.transcoder;

import java.io.IOException;

import com.volantis.mcs.context.MarinerContextException;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.integration.AssetTranscoderContext;
import com.volantis.mcs.integration.TranscoderURLParameterProvider;
import com.volantis.mcs.integration.TranscodingException;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import junitx.util.PrivateAccessor;

/**
 * Base test case for ParameterizedTranscoder specializations. Note that
 * {@link ParameterizedTranscoder#addDeviceParameters},
 * {@link ParameterizedTranscoder#addMaxSize},
 * {@link ParameterizedTranscoder#addWidth} and
 * {@link ParameterizedTranscoder#ruleToBeApplied} are tested indirectly by
 * the tests for {@link ParameterizedTranscoder#constructImageURL}.
 */
public abstract class ParameterizedTranscoderTestAbstract
    extends TestCaseAbstract {
    /**
     * The transcoder instance under test
     */
    protected ParameterizedTranscoder transcoder;

    /**
     * Returns the name of the width parameter.
     *
     * @return the name of the width parameter
     */
    protected abstract String getWidthParameter();

    /**
     * Returns the name of the height parameter.
     *
     * @return the name of the height parameter or null if there is no
     * height parameter
     */
    protected abstract String getHeightParameter();

    /**
     * Returns the name of the maxSize parameter.
     *
     * @return the name of the maxSize parameter
     */
    protected abstract String getMaxSizeParameter();

    /**
     * Returns the name of the extrasPolicy parameter.
     *
     * @return the name of the extrasPolicy parameter
     */
    protected abstract String getExtrasPolicyParameter();

    /**
     * Returns a new transcoder instance to be tested.
     *
     * @return a new transcoder instance to be tested
     */
    protected abstract ParameterizedTranscoder createTranscoder();

    /**
     * Creates a mock request context that will return the defined extras as
     * the value for any requested policy value.
     *
     * @param extras the policy value to be returned
     * @return a mock request context instance
     */
    protected MarinerRequestContext createContext(final String extras) {
        return new MarinerRequestContext() {
            public MarinerRequestContext createNestedContext()
                throws IOException,
                RepositoryException,
                MarinerContextException {
                return null;
            }

            public String getDeviceName() {
                return "Master";
            }

            public String getDevicePolicyValue(String policyName) {
                return extras;
            }
        };
    }

    // javadoc inherited
    protected void setUp() throws Exception {
        transcoder = createTranscoder();

        super.setUp();
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();

        transcoder = null;
    }

    /**
     * Test attribute initialization
     */
    public void testWidthParameter() throws Exception {
        assertEquals("Incorrect width parameter name",
                     getWidthParameter(),
                     transcoder.getWidthParameter());
    }

    /**
     * Test that the addWidth method works as expected when there is no
     * existing width parameter on the url.
     */
    public void testAddWidthNoParam() throws Exception {
        String string = "http://ahost/path";
        StringBuffer url = new StringBuffer(string);
        transcoder.addWidth(null, url, 5, null);

        assertEquals("There should be a single width param",
                (string + "&" + getWidthParameter() + "=" + 5),
                url.toString());

    }

    /**
     * Test that the addWidth method does not add the width parameter to a
     * url that already has a width parameter.
     */
    public void testAddWidthParam() throws Exception {
        String string = "http://ahost/path?" +
                getWidthParameter() + "=40%";
        StringBuffer url = new StringBuffer(string);
        transcoder.addWidth(null, url, 5, null);

        assertEquals("There should be a single width param",
                string, url.toString());


        string = "http://ahost/path?aparam=\"value\"&" +
                getWidthParameter() + "=40%";
        url = new StringBuffer(string);
        transcoder.addWidth(null, url, 5, null);

        assertEquals("There should be a single width param",
                string, url.toString());
    }

    /**
     * Test the max size parameter name
     */
    public void testMaxSizeParameter() throws Exception {
        assertEquals("Incorrect maxSize parameter name",
                     getMaxSizeParameter(),
                     transcoder.getMaxImageSizeParameter());
    }

    /**
     * Test the height parameter name.
     */
    public void testHeightParameter() throws Exception {
        String heightParameterName = getHeightParameterName();

        assertEquals("Incorrected height parameter name",
                     getHeightParameter(),
                     heightParameterName);
    }

    /**
     * Test extras policy parameter name.
     */
    public void testExtrasPolicyParameter() throws Exception {
        TranscoderURLParameterProvider tURLPP =
                (TranscoderURLParameterProvider)
                PrivateAccessor.getField(transcoder, "tURLPP");

        String extrasPolicyParameterName = tURLPP.getExtrasPolicyParameterName();

        assertEquals("Incorrect extras policy parameter name",
                     getExtrasPolicyParameter(),
                     extrasPolicyParameterName);
    }

    /**
     * Test for constructImageURL
     */
    public void testConstructImageURLNoMaxSizeNoExtras() throws Exception {
        String expected = "http://server/cp8/?" + getWidthParameter() + "=256";
        expected = addHeight(expected);

        assertEquals("URL with no maxSize/extras incorrect",
                     expected,
                     this.constructImageURL("http://server/",
                                                  "cp8",
                                                  256,
                                                  -1,
                                                  createContext(null)));
    }

    /**
     * Test for constructImageURL
     */
    public void testConstructImageURLNoExtras() throws Exception {
        String expected = "http://server/cp8/?" + getWidthParameter() + "=1024";
        expected = addHeight(expected);
        expected += "&" + getMaxSizeParameter() + "=65535";
        assertEquals("URL with no maxSize/extras incorrect",
                     expected,
                     this.constructImageURL("http://server/",
                                                  "cp8",
                                                  1024,
                                                  65535,
                                                  createContext(null)));
    }

    /**
     * Test for constructImageURL
     */
    public void testConstructImageURLNoMaxSize() throws Exception {
        String expected = "http://server/cp8/?" + getWidthParameter() + "=512";
        expected = addHeight(expected);
        expected += "&abc=123";

        assertEquals("URL with no maxSize incorrect",
                     expected,
                     this.constructImageURL("http://server/",
                                                  "cp8",
                                                  512,
                                                  -1,
                                                  createContext("&abc=123")));
    }

    /**
     * Test for constructImageURL
     */
    public void testConstructImageURLPNGMaxSizeOutput() throws Exception {
        String expected = "http://server/cp24/?" + getWidthParameter() + "=128";
        expected = addHeight(expected);
        expected += "&" + getMaxSizeParameter() + "=2048&abc=123";
        assertEquals("URL with PNG rule (maxSize should be output) incorrect",
                     expected,
                     this.constructImageURL("http://server/",
                                                  "cp24",
                                                  128,
                                                  2048,
                                                  createContext("abc=123")));
    }

    /**
     * Test for constructImageURL
     */
    public void testConstructImageURLJPEGMaxSizeOutput() throws Exception {
        String expected = "http://server/cj8/?" + getWidthParameter() + "=128";
        expected = addHeight(expected);
        expected += "&" + getMaxSizeParameter() + "=2048&abc=123";
        assertEquals("URL with JPEG rule (maxSize should be output) incorrect",
                     expected,
                     this.constructImageURL("http://server/",
                                                  "cj8",
                                                  128,
                                                  2048,
                                                  createContext("abc=123")));
    }

    /**
     * Test for constructImageURL
     */
    public void testConstructImageURLWBMPMaxSizeOutput() throws Exception {
        String expected = "http://server/gw1/?" + getWidthParameter() + "=128";
        expected = addHeight(expected);
        expected += "&" + getMaxSizeParameter() + "=2048&abc=123";
        assertEquals("URL with WBMP rule (maxSize should be output) incorrect",
                    expected,
                     this.constructImageURL("http://server/",
                                                  "gw1",
                                                  128,
                                                  2048,
                                                  createContext("abc=123")));
    }

    /**
     * Test for constructImageURL using a URL that has a servlet with parameters,
     * some of which have slashes in their values.
     */
    public void testConstructImageURLWithServletParametersAndSlashes()
            throws Exception {
        final String rule = "gw1";
        final String expectedRule = "gw1";

        final String servlet =
                "myServlet?tf.source.host=www.tf-nextmedia.de" +
                "&tf.source.port=80" +
                "&image=/cinema/shared/img/wml/cinemaWelcome3.jpg" +
                "&x=172&y=-1&mime=gif";

        final String actual = this.constructImageURL(
                "http://server/" + servlet,
                rule,
                10480,
                -1,
                createContext(null));

        String expected = "http://server/" + expectedRule + "/" +
                servlet + '&' + getWidthParameter() + "=10480";
        expected = addHeight(expected);
        assertEquals("Output for rule " + rule + " incorrect",
                expected, actual);
    }

    /**
     * Test for constructImageURL
     */
    public void testConstructImageURLWithExistingWidthHeight()
            throws Exception {
        String queryParams = getWidthParameter() + "=500&" +
                getHeightParameter() + "=9999";
        String expected = "http://server/gw1/?" + queryParams;
        expected += "&" + getMaxSizeParameter() + "=2048";
        assertEquals("URL with existing width/height should be output " +
                "correctly", expected,
                this.constructImageURL("http://server/?" + queryParams,
                        "gw1",
                        128,
                        2048,
                        createContext(null)));
    }

    /**
     * Add the height parameter if there is one to the given string.
     * @param s the String
     * @return s with the height parameter name and value if applicable
     */
    protected String addHeight(String s) throws Exception {
        String heightParameterName = getHeightParameterName();
        if(heightParameterName!=null) {
            s += "&" + heightParameterName + "=9999";
        }
        return s;
    }

    /**
     * Get the name of the height parameter for this test case.
     * @return the name of the height parameter for the transcoder in this
     * test - may be null
     */
    private String getHeightParameterName() throws NoSuchFieldException {
        TranscoderURLParameterProvider tURLPP =
                (TranscoderURLParameterProvider)
                PrivateAccessor.getField(transcoder, "tURLPP");

        String heightParameterName = tURLPP.getHeightParameterName();
        return heightParameterName;
    }
    
    /**
     * Helper method to call PlugableAssertthis.constructImageURL
     * @param url
     * @param ruleValue
     * @param width
     * @param maxSize
     * @param context
     * @return trancoded url
     */
    protected String constructImageURL(String url,
            String ruleValue,
            int width,
            int maxSize,
            MarinerRequestContext context)  throws TranscodingException
            {
        AssetTranscoderContext ctx = new AssetTranscoderContext(
                url,
                ruleValue,
                width,
                maxSize,
                context
        );
        return transcoder.constructImageURL(ctx);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Nov-05	10168/1	ianw	VBM:2005102504 port forward web clipping

 07-Nov-05	10170/1	ianw	VBM:2005102504 port forward web clipping

 04-Nov-05	9999/2	pszul	VBM:2005102504 preserver area implemented in ConvertibleImageAsset

 17-Feb-05	7015/1	adrianj	VBM:2005021508 Fix for parameters when creating image URLs

 17-Feb-05	7008/1	adrianj	VBM:2005021508 Fixes to creation of image URL

 18-Jan-05	6705/3	allan	VBM:2005011708 Remove the height from the width parameter

 18-Jan-05	6705/1	allan	VBM:2005011708 Remove the height from the width parameter

 05-Jan-05	6595/1	allan	VBM:2005010509 Only add a width if there is no width parameter

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Sep-04	5493/2	pcameron	VBM:2004091002 Fixed URL generation when parameter values have slashes

 26-Sep-03	1454/1	philws	VBM:2003092401 Provide asset transcoder plugin API and configuration-selectable standard implementations

 ===========================================================================
*/

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
import com.volantis.mcs.repository.RepositoryException;

/**
 * Test case for ICSwithGIF transcoder plugin.
 */
public class ICSWithGIFTestCase extends ParameterizedTranscoderTestAbstract {

    // javadoc inherited
    protected String getWidthParameter() {
        return "v.width";
    }

    // javadoc inherited
    protected String getHeightParameter() {
        return null;
    }

    // javadoc inherited
    protected String getMaxSizeParameter() {
        return "v.maxSize";
    }

    // javadoc inherited
    protected String getExtrasPolicyParameter() {
        return "ics.params";
    }

    // javadoc inherited
    protected ParameterizedTranscoder createTranscoder() {
        return new ICSWithGIF();
    }

    /**
     * The mock request context will return support for PNG and WBMP as well
     * as permitting extras to be returned.
     *
     * @param extras any extras parameters
     * @param supportsPNG true if PNG is supported by the device
     * @param supportsWBMP true if WBMP is supported by the device
     * @return the mock request context
     */
    protected MarinerRequestContext createContext(final String extras,
                                                  final boolean supportsPNG,
                                                  final boolean supportsWBMP) {
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
                String result = extras;

                if ("pnginpage".equals(policyName)) {
                    result = (supportsPNG ? "true" : null);
                } else if ("wbmpinpage".equals(policyName)) {
                    result = (supportsWBMP ? "true" : null);
                }

                return result;
            }
        };
    }

    /**
     * Test for constructImageURL targetting GIFs
     */
    public void testConstructImageURLTargetGIF() throws Exception {

        MarinerRequestContext context = createContext(null, false, false);
        doGIFTest("gg1", "gg2", context); // 1`-> 2 to avoid Image IO Tools bug
        doGIFTest("gg2", "gg2", context);
        doGIFTest("gg4", "gg4", context);
        doGIFTest("gg8", "gg8", context);
        doGIFTest("cg8", "cg8", context);

    }

    /**
     * Supporting helper method.
     */
    protected void doGIFTest(String rule,
            String expectedRule,
            MarinerRequestContext context) throws Exception {
        assertTrue("doGIFTest invoked with an invalid rule " + rule +
                "(not GIF targeted)",
                rule.charAt(1) == 'g');
        
        String expected = "http://server/" + expectedRule + "/?" +
        getWidthParameter() + "=10480";
        expected = addHeight(expected);
        
        AssetTranscoderContext ctx = new AssetTranscoderContext(
                "http://server/",
                rule,
                10480,
                -1,
                context);
        assertEquals("Output for rule " + rule + " not as",
                expected,
                transcoder.constructImageURL(ctx));
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

 18-Jan-05	6705/3	allan	VBM:2005011708 Remove the height from the width parameter

 18-Jan-05	6705/1	allan	VBM:2005011708 Remove the height from the width parameter

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 21-Sep-04	5559/1	geoff	VBM:2004091506 Support GIF as transcoded image type in MCS and ICS

 29-Jul-04	4991/4	byron	VBM:2004070510 VTS classes need renaming in MCS to ICS - removed constants usage in TC

 29-Jul-04	4991/2	byron	VBM:2004070510 VTS classes need renaming in MCS to ICS

 30-Apr-04	4128/1	ianw	VBM:2004042905 Changed v.maxsize to v.maxSize

 26-Sep-03	1454/1	philws	VBM:2003092401 Provide asset transcoder plugin API and configuration-selectable standard implementations

 ===========================================================================
*/

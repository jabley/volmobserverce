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
package com.volantis.mcs.integration.transcoder;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.integration.AssetTranscoderContext;
import com.volantis.mcs.integration.TranscodingException;

/**
 * Test case for ICSWithoutGIF transcoder plugin.
 */
public class ICSWithoutGIFTestCase extends ICSWithGIFTestCase {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    // javadoc inherited
    protected ParameterizedTranscoder createTranscoder() {

        return new ICSWithoutGIF();
    }

    /**
     * Test for constructImageURL targetting GIFs
     */
    public void testConstructImageURLTargetGIF() throws Exception {

        // Test requesting GIFs when we support PNG
        MarinerRequestContext context = createContext(null, true, true);
        doGIFTest("gg1", "gp1", context);
        doGIFTest("gg2", "gp2", context);
        doGIFTest("gg4", "gp4", context);
        doGIFTest("gg8", "gp8", context);
        doGIFTest("cg8", "cp8", context);

        // Test requesting GIFs when we support WBMP
        context = createContext(null, false, true);
        doGIFTest("gg1", "gw1", context);
        doGIFTest("gg2", "gw1", context);
        doGIFTest("gg4", "gw1", context);
        doGIFTest("gg8", "gw1", context);
        doGIFTest("cg8", "gw1", context);

        // Test requesting GIFs when we support neither PNG nor WBMP.
        context = createContext(null, false, false);
        try {
        	AssetTranscoderContext ctx = 
        		new AssetTranscoderContext(
        				"http://server/", "gg1", 1024, 32767, context);
            transcoder.constructImageURL(ctx);
            fail("Should have received a TranscodingException");
        } catch (TranscodingException e) {
            // Expected situation
        }

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Nov-05	10168/1	ianw	VBM:2005102504 port forward web clipping

 07-Nov-05	10170/1	ianw	VBM:2005102504 port forward web clipping

 04-Nov-05	9999/1	pszul	VBM:2005102504 preserver area implemented in ConvertibleImageAsset

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 21-Sep-04	5559/1	geoff	VBM:2004091506 Support GIF as transcoded image type in MCS and ICS

 ===========================================================================
*/

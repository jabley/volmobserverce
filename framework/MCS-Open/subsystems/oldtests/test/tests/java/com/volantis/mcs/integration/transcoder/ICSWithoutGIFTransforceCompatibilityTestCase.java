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

/**
 * Test case for ICSWithoutGifTransforceCompatibility transcoder plugin.
 */
public class ICSWithoutGIFTransforceCompatibilityTestCase
        extends ICSWithoutGIFTestCase {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    protected String getWidthParameter() {
        return "tf.width";
    }

    protected String getHeightParameter() {
        return "tf.height";
    }

    protected String getMaxSizeParameter() {
        return "tf.maxfilesize";
    }

    // javadoc inherited
    protected String getExtrasPolicyParameter() {
        return "transforce.params";
    }

    // javadoc inherited
    protected ParameterizedTranscoder createTranscoder() {
        return new ICSWithoutGIFTransforceCompatibility();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Jan-05	6705/3	allan	VBM:2005011708 Remove the height from the width parameter

 18-Jan-05	6705/1	allan	VBM:2005011708 Remove the height from the width parameter

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Dec-04	6356/1	geoff	VBM:2004112205 MCS Transcoder Transforce plugin

 21-Sep-04	5559/1	geoff	VBM:2004091506 Support GIF as transcoded image type in MCS and ICS

 ===========================================================================
*/

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
package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.context.MarinerRequestContext;

/**
 * WMLCDeviceSupport
 */
public final class WMLCDeviceSupport {

    /**
     * Whether or not device supports WMLC
     */
    private static final String WMLC_SUPPORTED = "wmlcsupport";

    private static final WMLCDeviceSupport USE_HEADERS =
            new WMLCDeviceSupport("headers");
    public static final WMLCDeviceSupport WML =
            new WMLCDeviceSupport("WML");
    public static final WMLCDeviceSupport WMLC =
            new WMLCDeviceSupport("WMLC");

    private final String type;

    private WMLCDeviceSupport(String type) {
        this.type = type;
    }

    /**
     * Returns the appropriate <code>WMLCDeviceSupport</code> for given
     * request context
     *
     * @param requestContext the MarinerRequestContext
     * @return a WMLCDeviceSupport instance
     */
    public static WMLCDeviceSupport getMode(
            MarinerRequestContext requestContext) {
        String type = requestContext.getDevicePolicyValue(WMLC_SUPPORTED);
        WMLCDeviceSupport wmlcSupported = USE_HEADERS;
        if (WML.type.equalsIgnoreCase(type)) {
            wmlcSupported = WML;
        } else if (WMLC.type.equalsIgnoreCase(type)) {
            wmlcSupported = WMLC;
        }
        return wmlcSupported;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 20-May-05	7762/1	doug	VBM:2005041916 Allow the MCSFilter cache to use post pipeline XDIME when calculating the cache key

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Apr-04	3712/1	steve	VBM:2004040103 Allow WML/WMLC at a device level

 ===========================================================================
*/

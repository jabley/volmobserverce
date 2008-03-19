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

import com.volantis.mcs.integration.TransforceURLParameterProvider;

/**
 * This specialist transcoder permits MCS to invoke the ICS transcoder in
 * TransForce compatibility mode (where ICS accepts TransForce parameters) but
 * GIF targeted images are not supported.
 * <p>
 * This plugin will run against an ICS running under JDK 1.3 (and without Image
 * IO Tools) as is does not request GIFs and thus does not require ICS to
 * support writing GIFs.
 */
public class ICSWithoutGIFTransforceCompatibility extends ICSWithoutGIF {

    /**
     * Initializes the new instance.
     */
    public ICSWithoutGIFTransforceCompatibility() {

        // Enable transforce compatibility.
        super(TransforceURLParameterProvider.SINGLETON);
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Jan-05	6705/1	allan	VBM:2005011708 Remove the height from the width parameter

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 21-Sep-04	5559/1	geoff	VBM:2004091506 Support GIF as transcoded image type in MCS and ICS

 ===========================================================================
*/

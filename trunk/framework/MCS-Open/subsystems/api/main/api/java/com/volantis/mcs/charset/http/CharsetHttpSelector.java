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
 * $Header: /cvs/architecture/architecture/api/src/java/com/volantis/mcs/charset/http/CharsetHttpSelector.java,v 1.2 2004/07/22 16:13:39 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Jul-04    Ian             Assignment:888 - Created.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.charset.http;

import com.volantis.mcs.devices.Device;

import com.volantis.mcs.http.HttpHeaders;

/**
 * This class is used to determine the charset that will be used to encode the
 * response to a device.
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface CharsetHttpSelector {
    /**
     * This method will return the character set that would be used my MCS to
     * render output to the Device.
     * 
     * @param headers
     *            The headers from which the accept-charset headers will be used
     *            in selecting the charset.
     * @param device
     *            The already identified {@link Device} whose policy values
     *            will be used in selecting the charset.
     * 
     * @return The name of the character set used in output processing.
     */
    public String getOutputCharset(HttpHeaders headers, Device device);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Nov-04	6098/1	byron	VBM:2004100715 No JavaDoc for com.volantis.mcs.charset public API

 02-Aug-04	5017/1	matthew	VBM:2004073003 CharsetHttpFactory and Selector

 ===========================================================================
*/

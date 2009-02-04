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
package com.volantis.mcs.runtime.packagers;

/**
 * Allows implementations to be written that perform URL encoding for packaged
 * URLs.
 */
public interface PackagedURLEncoder {
    /**
     * This method is used to mangle the URL given to generate an equivalent
     * URI. The behaviour of this method depends on the setting of the
     * protocol.mime.preserve.urls device policy value.
     *
     * @param plain the unmanged asset URL
     * @return the equivalent URI to be used as the asset URL in the package
     */
    String getEncodedURI(String plain);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Sep-04	5519/1	byron	VBM:2004091406 Multi-Part Mime: Does not handle url and urlc attributes on img tag

 ===========================================================================
*/

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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.servlet;

/**
 * A simple data object for storing the cached version of a rendered page
 * along with its content type.
 */
public class CachedRenderedPage {
    /**
     * The body data of the cached page, broken down into units separated by
     * session ID values.
     */
    private byte[][] bodyDataParts;

    /**
     * The content type of the cached page.
     */
    private String contentType;

    /**
     * Create a new cached rendered page with the specified body data and
     * content type.
     *
     * @param bodyDataParts The body data of the cached page
     * @param contentType The content type of the cached page
     */
    public CachedRenderedPage(byte[][] bodyDataParts, String contentType) {
        this.bodyDataParts = bodyDataParts;
        this.contentType = contentType;
    }

    /**
     * Retrieves the body data of the cached page as a byte array.
     * @return The body data of the cached page
     */
    public byte[][] getBodyDataParts() {
        return bodyDataParts;
    }

    /**
     * Retrieves the content type of the cached page.
     * @return The content type of the cached page
     */
    public String getContentType() {
        return contentType;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Feb-05	6786/5	adrianj	VBM:2005012506 Rendered page cache rework

 14-Feb-05	6786/3	adrianj	VBM:2005012506 Rendered page cache rework

 11-Feb-05	6786/1	adrianj	VBM:2005012506 Added rendered page cache

 ===========================================================================
*/

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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.xdime.xhtml2;

import java.util.HashMap;

/**
 * TODO: Fill in file header comment
 *
 * User: pabbott
 * Date: 29-Sep-2005
 * Time: 13:55:56
 */
public class ContentType {

    /**
     * Known Content Types
     */
    public final static ContentType XMCS_IMAGE =
        new ContentType("mimg", "application/xmcsimage");
    public final static ContentType XMCS_RESOURCE =
        new ContentType("mrsc", "application/xmcsresource");
    public final static ContentType XMCS_TEXT =
        new ContentType("mtxt", "application/xmcstext");

    public final static ContentType BMP_IMAGE =
        new ContentType("bmp", null);
    public final static ContentType GIF_IMAGE =
        new ContentType("gif", null);
    public final static ContentType JPG_IMAGE =
        new ContentType("jpg", null);
    public final static ContentType JPEG_IMAGE =
        new ContentType("jpeg", null);
    public final static ContentType PNG_IMAGE =
        new ContentType("png", null);
    public final static ContentType TIFF_IMAGE =
        new ContentType("tiff", null);
    public final static ContentType WBMP_IMAGE =
        new ContentType("wbmp", null);
//    public final static ContentType  =
//        new ContentType("", null);

    private static HashMap knownFileSuffix = new HashMap();
    private static HashMap knownMimeTypes = new HashMap();

    static {
        knownFileSuffix.put(XMCS_IMAGE.getPathSuffix(), XMCS_IMAGE);
        knownFileSuffix.put(XMCS_RESOURCE.getPathSuffix(), XMCS_RESOURCE);
        knownFileSuffix.put(XMCS_TEXT.getPathSuffix(), XMCS_TEXT);
        knownFileSuffix.put(BMP_IMAGE.getPathSuffix(), BMP_IMAGE);
        knownFileSuffix.put(GIF_IMAGE.getPathSuffix(), GIF_IMAGE);
        knownFileSuffix.put(JPG_IMAGE.getPathSuffix(), JPG_IMAGE);
        knownFileSuffix.put(JPEG_IMAGE.getPathSuffix(), JPEG_IMAGE);
        knownFileSuffix.put(PNG_IMAGE.getPathSuffix(), PNG_IMAGE);
        knownFileSuffix.put(TIFF_IMAGE.getPathSuffix(), TIFF_IMAGE);
        knownFileSuffix.put(WBMP_IMAGE.getPathSuffix(), WBMP_IMAGE);

        knownMimeTypes.put(XMCS_IMAGE.getMimeType(),XMCS_IMAGE);
        knownMimeTypes.put(XMCS_RESOURCE.getMimeType(),XMCS_RESOURCE);
        knownMimeTypes.put(XMCS_TEXT.getMimeType(),XMCS_TEXT);

    }

    static ContentType getContentTypeFromPath(String path) {
        int dotPos = path.lastIndexOf('.');

        ContentType type = null;

        if (dotPos>0) {
           type = (ContentType)knownFileSuffix.get(path.substring(dotPos+1));
        }

        return type;
    }

    static ContentType getContentTypeFromMime(String name) {
        return (ContentType)knownMimeTypes.get(name);
    }

    private String pathSuffix;
    private String mimeType;

    private ContentType(String pathSuffix, String mimeType) {
        this.pathSuffix = pathSuffix;
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }

    private String getPathSuffix() {
        return pathSuffix;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 30-Sep-05	9562/1	pabbott	VBM:2005092011 Add XHTML2 Object element

 ===========================================================================
*/

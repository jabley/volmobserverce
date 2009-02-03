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
package com.volantis.map.ics.configuration;

/**
 * This class is responsible for defining constants for the MIME types and
 * extensions used by file types that are currently supported by ICS.
 */
public final class ImageConstants {

    /**
     * Constants to to indicate that the images should be clipped from the left
     * side. Preserved area left bound at the left side of the image.
     */
    public final static int NO_CLIP_LEFT = -1;

    /**
     * Constants to to indicate that the images should be clipped from the
     * right side. Preserved area right bound at the right side of the image.
     */
    public final static int NO_CLIP_RIGHT = -1;

    /**
     * MIME Type used for PNG images.
     */
    public static final String PNG_MIME_TYPE = "image/png";

    /**
     * The extension used for PNG files.
     */
    public static final String PNG_EXTENSION = "PNG";

    /**
     * MIME type for GIF images.
     */
    public static final String GIF_MIME_TYPE = "image/gif";

    /**
     * Extension used for GIF images.
     */
    public static final String GIF_EXTENSION = "GIF";

    /**
     * The extension used for JPEG images.
     */
    public static final String JPEG_EXTENSION = "JPEG";

    /**
     * The MIME Type for JPEG images.
     */
    public static final String JPEG_MIME_TYPE = "image/jpeg";

    /**
     * MIME Type used for WBMP images.
     */
    public static final String WBMP_MIME_TYPE = "image/vnd.wap.wbmp";

    /**
     * Extension used for WBMP file types.
     */
    public static final String WBMP_EXTENSION = "WBMP";

    /**
     * The Mime type used for bitmap images.
     */
    public static final String BITMAP_MIME_TYPE = "image/bmp";

    /**
     * Extension used for BMP file types.
     */
    public static final String BMP_EXTENSION = "BMP";

    /**
     * MIME Type used for SVG images.
     */
    public static final String SVG_MIME_TYPE = "image/svg+xml";

    /**
     * The extension used for SVG images.
     */
    public static final String SVG_EXTENSION = "svg";

    /**
     * The MIME Type used for Tiff images.
     */
    public static final String TIF_MIME_TYPE = "image/tiff";

    /**
     * Extension used for TIFF file types.
     */
    public static final String TIFF_EXTENSION = "TIFF";

    /**
     * This specifies the method of scaling used by the servlet. This can take
     * the values nearest, bilinear or bicubic. The default is bilinear. This
     * can be overridden in native mode with the v.scaleMode parameter.
     */
    public static final int SCALE_MODE_BILINEAR = 1;

    public static final int SCALE_MODE_BICUBIC = 2;

    public static final int SCALE_MODE_NEAREST = 3;

    /**
     * Constant indicating that the image can be of any size.
     */
    public final static int NO_MAXIMUM_BYTE_SIZE_SET_FOR_IMAGE = -1;

    /**
     * These parameters define the preferred dither modes used when quantizing
     * an image to a lower bit size than the source image. The parameters
     * relate to the target bit size of the image. Valid values are
     * "ordered-dither", "floyd-steinberg", "jarvis" and "stucki". This can be
     * overridden in native mode by using the v.ditherMode parameter.
     */
    public final static int DITHER_MODE_NONE = 0;

    public final static int DITHER_MODE_FLOYD = 1;

    public final static int DITHER_MODE_JARVIS = 2;

    public final static int DITHER_MODE_STUCKI = 3;

    public final static int DITHER_MODE_PATTERNED = 4;

    /**
     * This specifies the mode in which a JPEG image will be rendered. Valid
     * values are baseline or progressive. The user may override this is native
     * mode by using the v.jpegFormat parameter.
     */
    public final static int JPEG_BASELINE = 1;

    public final static int JPEG_PROGRESSIVE = 2;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Mar-05	311/1	rgreenall	VBM:2005012701 Calling writeImage in concrete implementations of AbstractImageWriter no longer results in the image being converted twice.

 ===========================================================================
*/

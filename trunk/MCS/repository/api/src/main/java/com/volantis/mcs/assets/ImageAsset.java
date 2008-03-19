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
 * $Header: /src/voyager/com/volantis/mcs/assets/ImageAsset.java,v 1.26 2003/03/24 16:35:25 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Apr-01    Paul            Created.
 * 26-Jun-01    Paul            VBM:2001051103 - Added check to make sure that
 *                              the rendering is legal and added equals and
 *                              hashCode methods and updated toString value.
 * 09-Jul-01    Paul            VBM:2001070902 - Added encoding property which
 *                              has moved back from Asset.
 * 10-Jul-01    Paul            VBM:2001070513 - Modified equals to compare
 *                              localSrc, hashCode to take account of localSrc
 *                              when generating the hash code and paramString
 *                              to include the value in the objects string
 *                              representation.
 * 16-Jul-01    Paul            VBM:2001070508 - Added identityMatches method.
 * 10-Aug-01    Paul            VBM:2001072505 - Added invalid rendering and
 *                              encoding values, added some extra file
 *                              extensions for jpeg and pjpeg files, also
 *                              added a mapping from extension to encoding
 *                              and a method to perform the mapping.
 * 27-Sep-01    Allan           VBM:2001091104 - Javadoc
 * 24-Oct-01    Paul            VBM:2001092608 - Removed the identityMatches
 *                              method.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 04-Jan-02    Paul            VBM:2002010403 - Made encoding private.
 * 14-Jan-02    Paul            VBM:2002011414 - Added annotations to indicate
 *                              which of the properties of this class
 *                              constitute its identity.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Mar-02    Adrian          VBM:2002031503 - add annotations for auto
 *                              generated accessors and unDOSified the file.
 * 22-Mar-02    Adrian          VBM:2002031503 - re-added annotations for auto
 *                              generated accessors.
 * 26-Apr-02    Adrian          VBM:2002040811 - Changed WBMP encoding value
 *                              to 32 was incorrectly 30.
 * 08-May-02    Paul            VBM:2002050305 - Added constructor which takes
 *                              an identity and a default constructor.
 * 17-May-02    Paul            VBM:2002050101 - Changed the names of some
 *                              annotations to make them more meaningful.
 * 26-Jul-02    Allan           VBM:2002072508 - Changed to extend
 *                              SubstantiveAsset.
 * 27-Aug-02    Ian             VBM:2002081510 - Added support for TIFF
 *                              encoding.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.assets;

import com.volantis.mcs.objects.RepositoryObjectIdentity;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * The ImageAsset class is the parent of all types of image assets. It
 * provides common attributes and behaviour for all image assets.
 *
 * @mariner-object-null-is-empty-string-field value
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 *
 * @deprecated See {@link com.volantis.mcs.policies}.
 *             This was deprecated in version 3.5.1.
 */
public abstract class ImageAsset
        extends SubstantiveAsset {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(ImageAsset.class);

    /**
     * The number of images in this image asset.
     * @mariner-object-field-control-type Text
     */
    private int sequenceSize = 1;

    /**
     * Flag indicating whether or not this ImageAsset is a sequence of images.
     * @mariner-object-field-control-type CheckBox
     */
    private boolean sequence;

    /**
     * Whether this asset is locally sourced.
     *
     * @mariner-object-field-xml-mapping false "remote"
     * @mariner-object-field-xml-mapping true "local"
     */
    private boolean localSrc;

    /**
     * The width of the image asset in pixels.
     */
    private int pixelsX;

    /**
     * The height of the image asset in pixels.
     */
    private int pixelsY;

    /**
     * The number of bits used to represent each pixel of the image asset.
     */
    private int pixelDepth;

    /**
     * The rendering associated with the image.
     *
     * @mariner-object-field-xml-mapping ImageAsset.COLOR "color"
     * @mariner-object-field-xml-mapping ImageAsset.MONOCHROME "monochrome"
     */
    private int rendering;

    /**
     * The encoding of this attribute.
     *
     * @mariner-object-field-xml-mapping ImageAsset.BMP "bmp"
     * @mariner-object-field-xml-mapping ImageAsset.GIF "gif"
     * @mariner-object-field-xml-mapping ImageAsset.JPEG "jpeg"
     * @mariner-object-field-xml-mapping ImageAsset.PJPEG "pjpeg"
     * @mariner-object-field-xml-mapping ImageAsset.PNG "png"
     * @mariner-object-field-xml-mapping ImageAsset.TIFF "tiff"
     * @mariner-object-field-xml-mapping ImageAsset.WBMP "wbmp"
     * @mariner-object-field-xml-mapping ImageAsset.VIDEOTEX "videotex"
     */
    private int encoding;

    /**
     * The invalid rendering value
     */
    public static final int INVALID_RENDERING = 0;

    /**
     * The invalid rendering value
     */
    public static final Integer INVALID_RENDERING_WRAPPER
            = new Integer(INVALID_RENDERING);

    /**
     * The String placed into the value an image asset that is a sequence
     * of assets indicating the position of the index value when the
     * asset is expanded into its sequence of assets.
     */
    public static final String SEQUENCE_INDEX_EXPRESSION = "{$index}";

    /**
     * The value assigned to the rendering attribute to indicate that the image
     * is colour
     */
    public static final int COLOR = 2;

    /**
     * The value assigned to the rendering attribute to indicate that the image
     * is monochrome
     */
    public static final int MONOCHROME = 1;

    /**
     * The invalid encoding value
     */
    public static final int INVALID_ENCODING = 0;

    /**
     * The invalid encoding value
     */
    public static final Integer INVALID_ENCODING_WRAPPER
            = new Integer(INVALID_ENCODING);

    /**
     * The value assigned to the encoding to indicate that the image is a Windows
     * bitmap
     */
    public static final int BMP = 1;

    /**
     * The value assigned to the encoding to indicate that the image is a gif
     */
    public static final int GIF = 2;

    /**
     * The value assigned to the encoding to indicate that the image is a jpeg
     */
    public static final int JPEG = 4;

    /**
     * The value assigned to the encoding to indicate that the image is a
     * progressive jpeg
     */
    public static final int PJPEG = 8;

    /**
     * The value assigned to the encoding to indicate that the image is a png
     */
    public static final int PNG = 16;

    /**
     * The value assigned to the encoding to indicate that the image is a WAP
     * bitmap
     */
    public static final int WBMP = 32;

    /**
     * The value assigned to the encoding to indicate that the image is a TIFF
     * bitmap
     */
    public static final int TIFF = 64;

    /**
     * The ascii art encoding type.
     */
    public static final int VIDEOTEX = 128;

    /**
     * The extension used when creating files to hold images of this type
     */
    public static final String BMP_EXTENSION = "bmp";

    /**
     * The extension used when creating files to hold images of this type
     */
    public static final String GIF_EXTENSION = "gif";

    /**
     * The extension used when creating files to hold images of this type
     */
    public static final String JPG_EXTENSION = "jpg";

    /**
     * The extension used when creating files to hold images of this type
     */
    public static final String JPEG_EXTENSION = "jpeg";

    /**
     * The extension used when creating files to hold images of this type
     */
    public static final String PJPEG_EXTENSION = "pjpeg";

    /**
     * The extension used when creating files to hold images of this type
     */
    public static final String PNG_EXTENSION = "png";

    /**
     * The extension used when creating files to hold images of this type
     */
    public static final String TIF_EXTENSION = "tif";

    /**
     * The extension used when creating files to hold images of this type
     */
    public static final String TIFF_EXTENSION = "tiff";


    /**
     * The extension used when creating files to hold images of this type
     */
    public static final String WBMP_EXTENSION = "wbmp";

    /**
     * The extension used when creating files to hold images of this type
     */
    public static final String VIDEOTEX_EXTENSION = "cept";

    /**
     * The name of this image type
     */
    public static final String BMP_NAME = "BMP";

    /**
     * The name of this image type
     */
    public static final String GIF_NAME = "GIF";

    /**
     * The name of this image type
     */
    public static final String JPEG_NAME = "JPEG";

    /**
     * The name of this image type
     */
    public static final String PJPEG_NAME = "PJPEG";

    /**
     * The name of this image type
     */
    public static final String PNG_NAME = "PNG";

    /**
     * The name of this image type
     */
    public static final String TIFF_NAME = "TIFF";

    /**
     * The name of this image type
     */
    public static final String WBMP_NAME = "WBMP";

    /**
     * The name of this image type
     */
    public static final String VIDEOTEX_NAME = "VIDEOTEX";

    /**
     * Mime type for the bmp image encoding.
     */
    public static final String BMP_MIME_TYPE = "image/bmp";

    /**
     * Mime type for the gif image encoding.
     */
    public static final String GIF_MIME_TYPE = "image/gif";

    /**
     * Mime type for the jpeg image encoding.
     */
    public static final String JPEG_MIME_TYPE = "image/jpeg";

    /**
     * Mime type for the png image encoding.
     */
    public static final String PNG_MIME_TYPE = "image/png";

    /**
     * Mime type for the pjpeg image encoding.
     */
    public static final String PJPEG_MIME_TYPE = "image/pjpeg"; //this is a guess

    /**
     * Mime type for the tiff image encoding.
     */
    public static final String TIFF_MIME_TYPE = "image/tiff";

    /**
     * Mime type for the wbmp image encoding.
     */
    public static final String WBMP_MIME_TYPE = "image/vnd.wap.wbmp";

    /**
     * Mime type for the videotex image encoding.
     */
    public static final String VIDEOTEX_MIME_TYPE = "image/vnd.videotex";


    /**
     * Map from file extension to encoding.
     */
    private static Map extensionToEncoding;

    static {
        extensionToEncoding = new HashMap();
        extensionToEncoding.put(BMP_EXTENSION, new Integer(BMP));
        extensionToEncoding.put(GIF_EXTENSION, new Integer(GIF));
        extensionToEncoding.put(JPEG_EXTENSION, new Integer(JPEG));
        extensionToEncoding.put(JPG_EXTENSION, new Integer(JPEG));
        extensionToEncoding.put(PJPEG_EXTENSION, new Integer(PJPEG));
        extensionToEncoding.put(PNG_EXTENSION, new Integer(PNG));
        extensionToEncoding.put(TIFF_EXTENSION, new Integer(TIFF));
        extensionToEncoding.put(TIF_EXTENSION, new Integer(TIFF));
        extensionToEncoding.put(WBMP_EXTENSION, new Integer(WBMP));
        extensionToEncoding.put(VIDEOTEX_EXTENSION, new Integer(VIDEOTEX));
    }

    /**
     * Return the file extension corresponding to an encoding.
     *
     * @param encoding int encoding whose file extension to return
     * @return String file extenstion for encoding parameter
     */
    public static String fileExtension(int encoding) {
        if (encoding == PNG) {
            return PNG_EXTENSION;
        }
        if (encoding == GIF) {
            return GIF_EXTENSION;
        }
        if (encoding == JPEG) {
            return JPEG_EXTENSION;
        }
        if (encoding == BMP) {
            return BMP_EXTENSION;
        }
        if (encoding == WBMP) {
            return WBMP_EXTENSION;
        }
        if (encoding == PJPEG) {
            return PJPEG_EXTENSION;
        }
        if (encoding == TIFF) {
            return TIFF_EXTENSION;
        }
        if (encoding == VIDEOTEX) {
            return VIDEOTEX_EXTENSION;
        }

        return null;
    }

    /**
     * Return the encoding name corresponding to an encoding.
     *
     * @param encoding int encoding whose name to return
     * @return String name for given encoding
     */
    public static String encodingName(int encoding) {
        if (encoding == PNG) {
            return PNG_NAME;
        }
        if (encoding == GIF) {
            return GIF_NAME;
        }
        if (encoding == JPEG) {
            return JPEG_NAME;
        }
        if (encoding == BMP) {
            return BMP_NAME;
        }
        if (encoding == WBMP) {
            return WBMP_NAME;
        }
        if (encoding == PJPEG) {
            return PJPEG_NAME;
        }
        if (encoding == TIFF) {
            return TIFF_NAME;
        }
        if (encoding == VIDEOTEX) {
            return VIDEOTEX_NAME;
        }

        return null;
    }

    /**
     * Return the encoding name corresponding to an encoding.
     *
     * @param encoding int encoding whose name to return
     * @return int  encoding for given encoding name
     */
    public static int getEncodingFromEncodingName(String encoding) {
        if (encoding.equals(PNG_NAME)) {
            return PNG;
        }
        if (encoding.equals(GIF_NAME)) {
            return GIF;
        }
        if (encoding.equals(JPEG_NAME)) {
            return JPEG;
        }
        if (encoding.equals(BMP_NAME)) {
            return BMP;
        }
        if (encoding.equals(WBMP_NAME)) {
            return WBMP;
        }
        if (encoding.equals(PJPEG_NAME)) {
            return PJPEG;
        }
        if (encoding.equals(TIFF_NAME)) {
            return TIFF;
        }
        if (encoding.equals(VIDEOTEX_NAME)) {
            return VIDEOTEX;
        }

        logger.warn("unsupported-encoding", new Object[]{encoding});

        return 0;
    }

    /**
     * This returns the encoding for the extension.
     * @param extension The extension of the file.
     * @return The encoding for the particular extension, or -1.
     */
    public static int getEncodingForExtension(String extension) {
        Integer encoding = (Integer) extensionToEncoding.get(extension);
        if (encoding == null) {
            return INVALID_ENCODING;
        }

        return encoding.intValue();
    }

    /**
     * Return the prefered encoding
     * @param encodings the list of possible encodings
     * @return int prefered encoding for the encoding name
     */
    public static int getPreferredEncoding(Vector encodings) {
        if (encodings.contains(PNG_NAME)) {
            return PNG;
        } else if (encodings.contains(JPEG_NAME)) {
            return JPEG;
        } else if (encodings.contains(GIF_NAME)) {
            return GIF;
        } else if (encodings.contains(BMP_NAME)) {
            return BMP;
        } else if (encodings.contains(WBMP_NAME)) {
            return WBMP;
        } else if (encodings.contains(TIFF_NAME)) {
            return TIFF;
        } else if (encodings.contains(VIDEOTEX_NAME)) {
            return VIDEOTEX;
        } else {
            return 0;
        }
    }

    /**
     * Return the mime type corresponding to an encoding.
     *
     * @param encoding int encoding whose mime type to return
     * @return String mime type for encoding parameter
     */
    public static String mimeType(int encoding) {
        if (encoding == PNG) {
            return PNG_MIME_TYPE;
        }
        if (encoding == GIF) {
            return GIF_MIME_TYPE;
        }
        if (encoding == JPEG) {
            return JPEG_MIME_TYPE;
        }
        if (encoding == BMP) {
            return BMP_MIME_TYPE;
        }
        if (encoding == WBMP) {
            return WBMP_MIME_TYPE;
        }
        if (encoding == PJPEG) {
            return PJPEG_MIME_TYPE;
        }
        if (encoding == TIFF) {
            return TIFF_MIME_TYPE;
        }
        if (encoding == VIDEOTEX) {
            return VIDEOTEX_MIME_TYPE;
        }

        return null;
    }

    /**
     * Create a new <code>ImageAsset</code>.
     */
    public ImageAsset() {
    }

    /**
     * Create a new <code>ImageAsset</code>.
     * @param identity The identity to use.
     */
    public ImageAsset(RepositoryObjectIdentity identity) {
        super(identity);
    }

    /**
     * Return the string representing the extension part of the filename used to
     * hold images this type.
     * @return The string representing the extension part of the filename used to
     * hold images this type
     */
    public String fileExtension() {
        return fileExtension(encoding);
    }

    /**
     * Return the string representing the name of the encoding for the image
     * represented by this asset.
     * @return The string representing the name of the encoding for the image
     * represented by this asset
     */
    public String encodingName() {
        return encodingName(encoding);
    }

    /**
     * Return the string representing the MIME type for the image
     * represented by this asset.
     * @return The string representing the MIME type for the image
     * represented by this asset
     */
    public String mimeType() {
        return mimeType(encoding);
    }

    /**
     * Set the width of the asset in pixels
     * @param pixelsX The width of the asset in pixels
     */
    public void setPixelsX(int pixelsX) {
        this.pixelsX = pixelsX;
    }

    /**
     * Get the frame count for this image asset. This is the number of
     * image contained within this asset. The default and minimum value for
     * sequenceSize is 1.
     * @return the sequenceSize for this ImageAsset.
     */
    public int getSequenceSize() {
        return sequenceSize;
    }

    /**
     * Set the sequenceSize for this imageAsset.
     *
     * <p>This is the number of images that comprise this asset. The default
     * and minimum value for sequenceSize is 1.</p>
     * 
     * @param sequenceSize the sequenceSize
     */
    public void setSequenceSize(int sequenceSize) {
        this.sequenceSize = sequenceSize;
    }

    /**
     * Determine if this ImageAsset is a sequence of images.
     * @return true if this ImageAsset is a sequence; false otherwise.
     */
    public boolean isSequence() {
        return sequence;
    }

    /**
     * Set the sequence property of this ImageAsset
     * @param sequence true if this ImageAsset is sequence; false otherwise.
     */
    public void setSequence(boolean sequence) {
        this.sequence = sequence;
    }

    /**
     * Access method for the pixelsX property.
     *
     * @return the current value of the pixelsX property
     */
    public int getPixelsX() {
        return pixelsX;
    }

    /**
     * Set the height of the asset in pixels
     * @param pixelsY The height of the asset in pixels
     */
    public void setPixelsY(int pixelsY) {
        this.pixelsY = pixelsY;
    }

    /**
     * Get the height of the asset in pixels.
     * @return  the height of the asset in pixels
     */
    public int getPixelsY() {
        return pixelsY;
    }

    /**
     * Set the number of bits per pixel
     * @param pixelDepth The number of bits per pixel
     */
    public void setPixelDepth(int pixelDepth) {
        this.pixelDepth = pixelDepth;
    }

    /**
     * Get the number of bits per pixel
     * @return The number of bits per pixel
     */
    public int getPixelDepth() {
        return pixelDepth;
    }

    /**
     * Set the value of encoding.
     * @param encoding Value to assign to encoding.
     */
    public void setEncoding(int encoding) {
        this.encoding = encoding;
    }

    /**
     * Get the value of encoding.
     * @return value of encoding.
     */
    public int getEncoding() {
        return encoding;
    }

    /**
     * Set the rendering
     * @param rendering The rendering
     */
    public void setRendering(int rendering) {
        if (rendering != COLOR && rendering != MONOCHROME) {
            throw new IllegalArgumentException
                    ("Invalid rendering " + rendering
                    + ", must be either COLOR or MONOCHROME");
        }

        this.rendering = rendering;
    }

    /**
     * Get the rendering
     * @return The rendering
     */
    public int getRendering() {
        return rendering;
    }

    /**
     * Set the value of localSrc.
     * @param v  Value to assign to localSrc.
     * @deprecated Use an AssetGroup with a location type of ON_SERVER.
     */
    public void setLocalSrc(boolean v) {
        this.localSrc = v;
    }

    /**
     * Get the value of localSrc.
     * @return value of localSrc.
     * @deprecated Use an AssetGroup with a location type of ON_SERVER.
     */
    public boolean isLocalSrc() {
        return localSrc;
    }

    /** Compare this asset object with another asset object to see if
     * they are equivalent
     * @param object The object to compare with this object
     * @return True if the objects are equivalent and false otherwise
     */
    public boolean equals(Object object) {
        if (object instanceof ImageAsset) {
            ImageAsset o = (ImageAsset) object;
            return super.equals(o)
                    && localSrc == o.localSrc
                    && pixelsX == o.pixelsX
                    && pixelsY == o.pixelsY
                    && pixelDepth == o.pixelDepth
                    && rendering == o.rendering
                    && encoding == o.encoding
                    && sequence == o.sequence
                    && sequenceSize == o.sequenceSize;
        }

        return false;
    }

    /** Generate a hash code representing this asset.
     * @return The hash code that represents this object
     */
    public int hashCode() {
        return super.hashCode()
                + (localSrc ? 1 : 0)
                + pixelsX
                + pixelsY
                + pixelDepth
                + rendering
                + encoding
                + (sequence ? 1 : 0)
                + sequenceSize;
    }

    /** Generate a String from the parameters used to construct the asset
     * @return The generated String
     */
    protected String paramString() {
        return super.paramString()
                + "," + (localSrc ? "local" : "remote")
                + "," + pixelsX + "x" + pixelsY
                + "," + pixelDepth
                + "," + rendering
                + "," + encoding
                + "," + (sequence ? 1 : 0)
                + "," + sequenceSize;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Apr-05	7376/1	allan	VBM:2005031101 SmartClient bundler - commit for testing

 11-Mar-05	7308/3	tom	VBM:2005030702 Added XHTMLSmartClient and support for image sequences

 09-Mar-05	7315/1	allan	VBM:2005030711 Add sequences of image assets.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 27-May-04	4532/1	byron	VBM:2004052104 ASCII-Art Image Asset Encoding

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 06-Jan-04	2362/1	mat	VBM:2004010207 Enable fields to be tagged to output empty string values for attributes

 16-Dec-03	2213/1	allan	VBM:2003121401 More editors and fixes for presentable values.

 ===========================================================================
*/

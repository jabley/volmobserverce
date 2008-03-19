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

package com.volantis.mcs.policies.variants.image;

import com.volantis.mcs.policies.variants.metadata.Encoding;
import com.volantis.mcs.policies.variants.metadata.EncodingBuilder;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;
import com.volantis.mcs.policies.variants.metadata.EncodingCollectionFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Type safe enumeration of image encodings.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @see ImageMetaData
 * @since 3.5.1
 */
public final class ImageEncoding
        implements Encoding {

    /**
     * The encoding for a BMP image resource.
     */
    public static final ImageEncoding BMP;

    /**
     * The encoding for a GIF image resource.
     */
    public static final ImageEncoding GIF;

    /**
     * The encoding for a JPEG image resource.
     */
    public static final ImageEncoding JPEG;

    /**
     * The encoding for a Progressive JPEG image resource.
     */
    public static final ImageEncoding PJPEG;

    /**
     * The encoding for a PNG image resource.
     */
    public static final ImageEncoding PNG;

    /**
     * The encoding for a TIFF image resource.
     */
    public static final ImageEncoding TIFF;

    /**
     * The encoding for a VideoTex image resource.
     */
    public static final ImageEncoding VIDEOTEX;

    /**
     * The encoding for a WBMP image resource.
     */
    public static final ImageEncoding WBMP;

    /**
     * The collection of the encodings.
     *
     * <p>The ordering of the collection cannot be relied upon.</p>
     */
    public static final EncodingCollection COLLECTION;

    static {
        EncodingBuilder builder;

        List encodings = new ArrayList();

        builder = new EncodingBuilder("bmp");
        builder.addExtension("bmp");
        builder.addMimeType("image/bmp");
        BMP = add(builder, encodings);

        builder = new EncodingBuilder("gif");
        builder.addExtension("gif");
        builder.addMimeType("image/gif");
        GIF = add(builder, encodings);

        builder = new EncodingBuilder("jpeg");
        builder.addExtension("jpeg");
        builder.addExtension("jpg");
        builder.addMimeType("image/jpeg");
        JPEG = add(builder, encodings);

        builder = new EncodingBuilder("pjpeg");
        builder.addExtension("pjpeg");
        builder.addExtension("pjpg");
        builder.addMimeType("image/pjpeg");
        PJPEG = add(builder, encodings);

        builder = new EncodingBuilder("png");
        builder.addExtension("png");
        builder.addMimeType("image/png");
        PNG = add(builder, encodings);

        builder = new EncodingBuilder("tiff");
        builder.addExtension("tiff");
        builder.addExtension("tif");
        builder.addMimeType("image/tiff");
        TIFF = add(builder, encodings);

        builder = new EncodingBuilder("wbmp");
        builder.addExtension("wbmp");
        builder.addMimeType("image/vnd.wap.wbmp");
        WBMP = add(builder, encodings);

        builder = new EncodingBuilder("videotex");
        builder.addExtension("cept");
        builder.addMimeType("image/vnd.videotex");
        VIDEOTEX = add(builder, encodings);

        final EncodingCollectionFactory encodingCollectionFactory =
            EncodingCollectionFactory.getDefaultInstance();
        COLLECTION =
            encodingCollectionFactory.createEncodingCollection(encodings);
    }

    private static ImageEncoding add(EncodingBuilder builder,
                                     List encodings) {
        ImageEncoding encoding = new ImageEncoding(builder.getEncoding());
        encodings.add(encoding);
        return encoding;
    }

    private final Encoding encoding;

    private ImageEncoding(Encoding encoding) {
        this.encoding = encoding;
    }

    public String getName() {
        return encoding.getName();
    }

    public Iterator mimeTypes() {
        return encoding.mimeTypes();
    }

    public Iterator extensions() {
        return encoding.extensions();
    }
}

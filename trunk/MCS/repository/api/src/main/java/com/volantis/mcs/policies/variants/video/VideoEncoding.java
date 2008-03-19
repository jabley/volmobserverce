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

package com.volantis.mcs.policies.variants.video;

import com.volantis.mcs.policies.variants.metadata.Encoding;
import com.volantis.mcs.policies.variants.metadata.EncodingBuilder;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;
import com.volantis.mcs.policies.variants.metadata.EncodingCollectionFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Type safe enumeration of video encodings.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @see VideoMetaData
 * @since 3.5.1
 */
public final class VideoEncoding
        implements Encoding {

    /**
     * The encoding for an animated GIF resource.
     */
    public static final VideoEncoding ANIMATED_GIF;

    /**
     * The encoding for a Macromedia Flash resource.
     */
    public static final VideoEncoding MACROMEDIA_FLASH;

    /**
     * The encoding for a Macromedia Shockwave resource.
     */
    public static final VideoEncoding MACROMEDIA_SHOCKWAVE;

    /**
     * The encoding for an MPEG1 resource.
     */
    public static final VideoEncoding MPEG1;

    /**
     * The encoding for a MPEG4 resource.
     */
    public static final VideoEncoding MPEG4;

    /**
     * The encoding for a Quicktime video resource.
     */
    public static final VideoEncoding QUICKTIME;

    /**
     * The encoding for a Real Player video resource.
     */
    public static final VideoEncoding REAL;

    /**
     * The encoding for a 3GPP video resource.
     */
    public static final VideoEncoding THREE_GPP;

    /**
     * The encoding for a TV resource.
     */
    public static final VideoEncoding TV;

    /**
     * The encoding for a Windows Media Player video resource.
     */
    public static final VideoEncoding WINDOWS_MEDIA;

    /**
     * The collection of the encodings.
     *
     * <p>The ordering of the collection cannot be relied upon.</p>
     */
    public static final EncodingCollection COLLECTION;

    static {
        EncodingBuilder builder;

        List encodings = new ArrayList();

        builder = new EncodingBuilder("animated gif");
        builder.addExtension("gif");
        builder.addMimeType("image/gif");
        ANIMATED_GIF = add(builder, encodings);

        builder = new EncodingBuilder("macromedia flash");
        builder.addExtension("swf");
        builder.addMimeType("application/x-shockwave-flash");        
        MACROMEDIA_FLASH = add(builder, encodings);

        builder = new EncodingBuilder("macromedia shockwave");
        builder.addExtension("dcr");
        builder.addExtension("dir");
        builder.addExtension("dxr");
        builder.addMimeType("application/x-director");        
        MACROMEDIA_SHOCKWAVE = add(builder, encodings);

        builder = new EncodingBuilder("mpeg1");
        builder.addExtension("mpeg");
        builder.addExtension("mpg");
        builder.addExtension("mpe");
        builder.addMimeType("video/mpeg");
        MPEG1 = add(builder, encodings);

        builder = new EncodingBuilder("mpeg4");
        builder.addMimeType("video/mpeg");        
        MPEG4 = add(builder, encodings);

        builder = new EncodingBuilder("quicktime");
        builder.addExtension("qt");
        builder.addExtension("mov");                
        builder.addMimeType("video/quicktime");        
        QUICKTIME = add(builder, encodings);

        builder = new EncodingBuilder("real video");
        builder.addExtension("rm");
        builder.addMimeType("video/vnd.rn-realvideo");         
        builder.addMimeType("application/vnd.rn-realmedia"); 
        REAL = add(builder, encodings);

        builder = new EncodingBuilder("3gpp");
        builder.addExtension("3gpp");                
        builder.addMimeType("video/3gpp");                
        THREE_GPP = add(builder, encodings);

        builder = new EncodingBuilder("tv");
        TV = add(builder, encodings);

        builder = new EncodingBuilder("windows media");
        builder.addExtension("asf");
        builder.addExtension("asx");
        builder.addExtension("avi");
        builder.addMimeType("video/x-ms-asf"); 
        builder.addMimeType("video/x-msvideo");
        WINDOWS_MEDIA = add(builder, encodings);

        final EncodingCollectionFactory encodingCollectionFactory =
            EncodingCollectionFactory.getDefaultInstance();
        COLLECTION =
            encodingCollectionFactory.createEncodingCollection(encodings);
    }

    private static VideoEncoding add(EncodingBuilder builder,
                                     List encodings) {
        VideoEncoding encoding = new VideoEncoding(builder.getEncoding());
        encodings.add(encoding);
        return encoding;
    }

    private final Encoding encoding;

    private VideoEncoding(Encoding encoding) {
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

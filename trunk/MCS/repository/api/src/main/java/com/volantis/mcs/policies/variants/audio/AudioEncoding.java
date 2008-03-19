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

package com.volantis.mcs.policies.variants.audio;

import com.volantis.mcs.policies.variants.metadata.Encoding;
import com.volantis.mcs.policies.variants.metadata.EncodingBuilder;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;
import com.volantis.mcs.policies.variants.metadata.EncodingCollectionFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Type safe enumeration of audio encodings.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @see AudioMetaData
 * @since 3.5.1
 */
public final class AudioEncoding
        implements Encoding {

    /**
     * The encoding for an ADPCM32 audio resource.
     */
    public static final AudioEncoding ADPCM32;

    /**
     * The encoding for an AMR audio resource.
     */
    public static final AudioEncoding AMR;

    /**
     * The encoding for a basic audio resource.
     */
    public static final AudioEncoding BASIC;

    /**
     * The encoding for a GSM audio resource.
     */
    public static final AudioEncoding GSM;

    /**
     * The encoding for an iMelody audio resource.
     */
    public static final AudioEncoding IMELODY;

    /**
     * The encoding for a Midi audio resource.
     */
    public static final AudioEncoding MIDI;

    /**
     * The encoding for an MP3 audio resource.
     */
    public static final AudioEncoding MP3;

    /**
     * The encoding for a Nokia Ring Tone audio resource.
     */
    public static final AudioEncoding NOKIA_RING_TONE;

    /**
     * The encoding for a Real Player audio resource.
     */
    public static final AudioEncoding REAL;

    /**
     * The encoding for a Windows Media Player audio resource.
     */
    public static final AudioEncoding WINDOWS_MEDIA;

    /**
     * The encoding for an SP Midi audio resource.
     */
    public static final AudioEncoding SP_MIDI;

    /**
     * The encoding for a WAV audio resource.
     */
    public static final AudioEncoding WAV;

    /**
     * The encoding for an RMF audio resource.
     */
    public static final AudioEncoding RMF;

    /**
     * The encoding for an SMAF audio resource.
     */
    public static final AudioEncoding SMAF;

    /**
     * The collection of the encodings.
     *
     * <p>The ordering of the collection cannot be relied upon.</p>
     */
    public static final EncodingCollection COLLECTION;

    static {
        EncodingBuilder builder;

        List encodings = new ArrayList();

        builder = new EncodingBuilder("adpcm32");
        builder.addExtension("726");
        builder.addMimeType("audio/kadpcm32");
        ADPCM32 = add(builder, encodings);

        builder = new EncodingBuilder("amr");
        builder.addExtension("amr");
        builder.addMimeType("audio/amr");
        AMR = add(builder, encodings);

        builder = new EncodingBuilder("basic");
        builder.addExtension("au");
        builder.addExtension("snd");
        builder.addMimeType("audio/basic");
        BASIC = add(builder, encodings);

        builder = new EncodingBuilder("gsm");
        builder.addExtension("gsm");
        builder.addMimeType("audio/gsm");
        GSM = add(builder, encodings);

        builder = new EncodingBuilder("imelody");
        builder.addMimeType("audio/imelody");
        builder.addMimeType("audio/x-imelody");
        IMELODY = add(builder, encodings);

        builder = new EncodingBuilder("midi");
        builder.addExtension("mid");
        builder.addMimeType("audio/midi");
        MIDI = add(builder, encodings);

        builder = new EncodingBuilder("mp3");
        builder.addExtension("mp3");
        builder.addMimeType("audio/mp3");
        MP3 = add(builder, encodings);

        builder = new EncodingBuilder("nokia ring tone");
        builder.addMimeType("application/vnd.nokia.ringing-tone");
        NOKIA_RING_TONE = add(builder, encodings);

        builder = new EncodingBuilder("real audio");
        builder.addExtension("ra");
        builder.addExtension("ram");
        builder.addMimeType("audio/x-realaudio");
        builder.addMimeType("audio/x-pn-realaudio");                
        REAL = add(builder, encodings);

        builder = new EncodingBuilder("rmf");
        builder.addMimeType("audio/rmf");
        RMF = add(builder, encodings);

        builder = new EncodingBuilder("smaf");
        builder.addMimeType("audio/smaf");
        SMAF = add(builder, encodings);

        builder = new EncodingBuilder("sp midi");
        builder.addMimeType("audio/sp-midi");
        SP_MIDI = add(builder, encodings);

        builder = new EncodingBuilder("wav");
        builder.addExtension("wav");
        builder.addMimeType("audio/wav");
        WAV = add(builder, encodings);

        builder = new EncodingBuilder("windows media audio");
        builder.addExtension("wma");
        builder.addExtension("wax");        
        builder.addMimeType("audio/x-ms-wma");        
        builder.addMimeType("audio/x-ms-wax");        
        WINDOWS_MEDIA = add(builder, encodings);

        final EncodingCollectionFactory encodingCollectionFactory =
            EncodingCollectionFactory.getDefaultInstance();
        COLLECTION =
            encodingCollectionFactory.createEncodingCollection(encodings);
    }

    private static AudioEncoding add(
            EncodingBuilder builder,
            List encodings) {
        AudioEncoding encoding = new AudioEncoding(builder.getEncoding());
        encodings.add(encoding);
        return encoding;
    }

    private final Encoding encoding;

    private AudioEncoding(Encoding encoding) {
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

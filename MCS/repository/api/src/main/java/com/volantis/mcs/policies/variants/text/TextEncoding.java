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

package com.volantis.mcs.policies.variants.text;

import com.volantis.mcs.policies.variants.metadata.Encoding;
import com.volantis.mcs.policies.variants.metadata.EncodingBuilder;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;
import com.volantis.mcs.policies.variants.metadata.EncodingCollectionFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Type safe enumeration of text encodings.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @see TextMetaData
 * @since 3.5.1
 */
public final class TextEncoding
        implements Encoding {

    /**
     * The encoding for a plain text resource.
     */
    public static final TextEncoding PLAIN;

    /**
     * The encoding for a form validator text resource.
     */
    public static final TextEncoding FORM_VALIDATOR;

    /**
     * The encoding for a Voice XML Help text resource.
     */
    public static final TextEncoding VOICE_XML_HELP;

    /**
     * The encoding for a Voice XML Prompt text resource.
     */
    public static final TextEncoding VOICE_XML_PROMPT;

    /**
     * The encoding for a Voice XML Error text resource.
     */
    public static final TextEncoding VOICE_XML_ERROR;

    /**
     * The encoding for a Voice XML Nuance Grammar text resource.
     */
    public static final TextEncoding VOICE_XML_NUANCE_GRAMMAR;

    /**
     * The collection of the encodings.
     *
     * <p>The ordering of the collection cannot be relied upon.</p>
     */
    public static final EncodingCollection COLLECTION;

    static {
        EncodingBuilder builder;

        List encodings = new ArrayList();

        builder = new EncodingBuilder("plain");
        builder.addMimeType("text/plain");
        PLAIN = add(builder, encodings);

        builder = new EncodingBuilder("form validator");
        FORM_VALIDATOR = add(builder, encodings);

        builder = new EncodingBuilder("voice xml help");
        VOICE_XML_HELP = add(builder, encodings);

        builder = new EncodingBuilder("voice xml prompt");
        VOICE_XML_PROMPT = add(builder, encodings);

        builder = new EncodingBuilder("voice xml error");
        VOICE_XML_ERROR = add(builder, encodings);

        builder = new EncodingBuilder("voice xml nuance grammar");
        VOICE_XML_NUANCE_GRAMMAR = add(builder, encodings);


        final EncodingCollectionFactory encodingCollectionFactory =
            EncodingCollectionFactory.getDefaultInstance();
        COLLECTION =
            encodingCollectionFactory.createEncodingCollection(encodings);
    }

    private static TextEncoding add(
            EncodingBuilder builder,
            List encodings) {
        TextEncoding encoding = new TextEncoding(builder.getEncoding());
        encodings.add(encoding);
        return encoding;
    }

    private final Encoding encoding;

    private TextEncoding(Encoding encoding) {
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

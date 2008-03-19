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

package com.volantis.mcs.policies.variants.script;

import com.volantis.mcs.policies.variants.metadata.Encoding;
import com.volantis.mcs.policies.variants.metadata.EncodingBuilder;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;
import com.volantis.mcs.policies.variants.metadata.EncodingCollectionFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Type safe enumeration of script encodings.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @see ScriptMetaData
 * @since 3.5.1
 */
public final class ScriptEncoding
        implements Encoding {

    /**
     * The encoding for a JavaScript script resource.
     */
    public static final ScriptEncoding JAVASCRIPT;

    /**
     * The encoding for a JavaScript 1.0 script resource.
     */
    public static final ScriptEncoding JAVASCRIPT_1_0;

    /**
     * The encoding for a JavaScript 1.1 script resource.
     */
    public static final ScriptEncoding JAVASCRIPT_1_1;

    /**
     * The encoding for a JavaScript 1.2 script resource.
     */
    public static final ScriptEncoding JAVASCRIPT_1_2;

    /**
     * The encoding for a JavaScript 1.3 script resource.
     */
    public static final ScriptEncoding JAVASCRIPT_1_3;

    /**
     * The encoding for a JavaScript 1.4 script resource.
     */
    public static final ScriptEncoding JAVASCRIPT_1_4;

    /**
     * The encoding for a WML Task script resource.
     */
    public static final ScriptEncoding WML_TASK;

    /**
     * The collection of the encodings.
     *
     * <p>The ordering of the collection cannot be relied upon.</p>
     */
    public static final EncodingCollection COLLECTION;

    static {
        EncodingBuilder builder;

        List encodings = new ArrayList();

        builder = new EncodingBuilder("javascript");
        addJavaScriptInfo(builder);
        JAVASCRIPT = add(builder, encodings);

        builder = new EncodingBuilder("javascript1.0");
        addJavaScriptInfo(builder);
        JAVASCRIPT_1_0 = add(builder, encodings);

        builder = new EncodingBuilder("javascript1.1");
        addJavaScriptInfo(builder);
        JAVASCRIPT_1_1 = add(builder, encodings);

        builder = new EncodingBuilder("javascript1.2");
        addJavaScriptInfo(builder);
        JAVASCRIPT_1_2 = add(builder, encodings);

        builder = new EncodingBuilder("javascript1.3");
        addJavaScriptInfo(builder);
        JAVASCRIPT_1_3 = add(builder, encodings);

        builder = new EncodingBuilder("javascript1.4");
        addJavaScriptInfo(builder);
        JAVASCRIPT_1_4 = add(builder, encodings);

        builder = new EncodingBuilder("wml");
        builder.addExtension("wml");
        builder.addMimeType("text/vnd.wap.wml");
        WML_TASK = add(builder, encodings);


        final EncodingCollectionFactory encodingCollectionFactory =
            EncodingCollectionFactory.getDefaultInstance();
        COLLECTION =
            encodingCollectionFactory.createEncodingCollection(encodings);
    }

    private static void addJavaScriptInfo(EncodingBuilder builder) {
        builder.addExtension("js");
        builder.addMimeType("text/javascript");
    }

    private static ScriptEncoding add(
            EncodingBuilder builder,
            List encodings) {
        ScriptEncoding encoding = new ScriptEncoding(builder.getEncoding());
        encodings.add(encoding);
        return encoding;
    }

    private final Encoding encoding;

    private ScriptEncoding(Encoding encoding) {
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

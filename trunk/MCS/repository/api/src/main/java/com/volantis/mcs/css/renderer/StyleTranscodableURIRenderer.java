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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.css.renderer;

import com.volantis.mcs.themes.StyleTranscodableURI;

import java.io.IOException;
import java.io.Writer;

/**
 * Render a style value representing a transcodable URI.
 */
public class StyleTranscodableURIRenderer {

    /**
     * The reference to the single allowable instance of this class.
     */
    private static StyleTranscodableURIRenderer INSTANCE;

    // Initialise the static fields.
    static {
        // Always initialise to prevent a synchronization problem if we do it
        // lazily.
        INSTANCE = new StyleTranscodableURIRenderer();
    }

    /**
     * Get the single allowable instance of this class.
     *
     * @return The single allowable instance of this class.
     */
    public static StyleTranscodableURIRenderer getSingleton() {
        return INSTANCE;
    }

    /**
     * Protect the constructor to prevent any other instances being created.
     */
    protected StyleTranscodableURIRenderer() {
    }

    /**
     * Write out the value of a Mariner Transcodable URL. Note that this
     * kind of url is not valid css until a valid url is established
     * at run time. Before runtime, the value of a mariner transcodable
     * url is merely the base URI of the transcodable resource.
     *
     * @param context The RendererContext to obtain the Writer to write to.
     * @param uri     The value of the mariner transcodable uri (i.e. the URI
     * to transcode).
     */
    protected void writeMarinerTrancodeableURL(final RendererContext context,
                                               final String uri)
        throws IOException {

        final Writer writer = context.getWriter();
        writer.write(uri);
    }

    public void render(final StyleTranscodableURI value,
                       final RendererContext context)
            throws IOException {

        final String uri = value.getUri();
        writeMarinerTrancodeableURL(context, uri);
    }
}

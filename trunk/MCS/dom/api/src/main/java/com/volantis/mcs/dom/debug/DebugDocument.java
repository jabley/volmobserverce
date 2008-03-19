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
package com.volantis.mcs.dom.debug;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.output.AbstractCharacterEncoder;
import com.volantis.mcs.dom.output.DOMDocumentOutputter;
import com.volantis.mcs.dom.output.XMLDocumentWriter;
import com.volantis.shared.throwable.ExtendedRuntimeException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Output debugging information for a "plain" document, including whitespace
 * but not including styles.
 */
public class DebugDocument {

    /**
     * A trivial character encoder.
     */
    private static final AbstractCharacterEncoder encoder =
            new AbstractCharacterEncoder() {
                public void encode(int c, Writer out)
                        throws IOException {
                    // Just return the character as provided.
                    out.write(c);
                }
            };

    public String debug(final Document document) {
        return debug(new Executor() {
            public void execute(DOMDocumentOutputter outputter) throws IOException {
                outputter.output(document);
            }
        });
    }

    public String debug(final Element element) {
        return debug(new Executor() {
            public void execute(DOMDocumentOutputter outputter) throws IOException {
                outputter.output(element);
            }
        });
    }

    private String debug(Executor executor) {

        StringWriter writer = new StringWriter();
        DOMDocumentOutputter outputter = new DOMDocumentOutputter(
            new XMLDocumentWriter(writer), encoder);

        try {
            executor.execute(outputter);
        } catch (IOException e) {
            throw new ExtendedRuntimeException(e);
        }

        return writer.toString();
    }

    private interface Executor {
        void execute(DOMDocumentOutputter outputter) throws IOException;
    }

}

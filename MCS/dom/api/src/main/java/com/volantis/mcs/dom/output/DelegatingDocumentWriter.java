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

package com.volantis.mcs.dom.output;

import com.volantis.mcs.dom.Comment;
import com.volantis.mcs.dom.DocType;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.XMLDeclaration;

import java.io.IOException;
import java.io.Writer;

public class DelegatingDocumentWriter
        implements DocumentWriter {

    private final DocumentWriter delegate;

    protected final Writer writer;

    public DelegatingDocumentWriter(DocumentWriter delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException(
                "Wrapped document writer cannot be null.");
        }

        this.delegate = delegate;
        this.writer = delegate.getUnderlyingWriter();
    }

    public Writer getUnderlyingWriter() {
        return delegate.getUnderlyingWriter();
    }

    public void outputXMLDeclaration(XMLDeclaration declaration)
            throws IOException {
        delegate.outputXMLDeclaration(declaration);
    }

    public void outputDocType(DocType docType) throws IOException {
        delegate.outputDocType(docType);
    }

    public void outputText(Text text, CharacterEncoder encoder)
            throws IOException {
        delegate.outputText(text, encoder);
    }

    public void outputComment(Comment comment, CharacterEncoder encoder)
            throws IOException {
        delegate.outputComment(comment, encoder);
    }

    public boolean outputOpenTag(
            Element element, CharacterEncoder encoder) throws IOException {
        return delegate.outputOpenTag(element, encoder);
    }

    public void outputCloseTag(Element element) throws IOException {
        delegate.outputCloseTag(element);
    }

    public void flush() throws IOException {
        delegate.flush();
    }
}

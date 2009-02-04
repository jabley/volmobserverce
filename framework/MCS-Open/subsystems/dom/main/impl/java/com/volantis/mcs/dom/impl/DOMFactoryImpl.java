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

package com.volantis.mcs.dom.impl;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.DocType;
import com.volantis.mcs.dom.XMLDeclaration;
import com.volantis.mcs.dom.Comment;
import com.volantis.mcs.dom.MarkupFamily;
import com.volantis.styling.Styles;

public class DOMFactoryImpl
        extends DOMFactory {

    private boolean debug;
    private static final DOMFactoryImpl DEBUG_DOM_FACTORY = new DOMFactoryImpl(true);

    public DOMFactoryImpl(boolean debug) {
        this.debug = debug;
    }

    public DOMFactoryImpl() {
        this(false);
    }

    // Javadoc inherited.
    public Document createDocument() {
        return new DocumentImpl(this);
    }

    public DocType createDocType(
            String root, String publicId, String systemId, String internalDTD,
            MarkupFamily markupFamily) {
        return new DocTypeImpl(root, publicId, systemId, internalDTD,
                markupFamily);
    }

    public XMLDeclaration createXMLDeclaration() {
        return new XMLDeclarationImpl();
    }

    // Javadoc inherited.
    public Element createElement() {
        return new ElementImpl(this);
    }

    // Javadoc inherited.
    public Element createElement(String name) {
        ElementImpl element = new ElementImpl(this);
        element.setName(name);
        return element;
    }

    public Element createStyledElement(Styles styles) {
        ElementImpl element = new ElementImpl(this);
        element.setStyles(styles);
        return element;
    }

    public Element createStyledElement(String name, Styles styles) {
        ElementImpl element = new ElementImpl(this);
        element.setName(name);
        element.setStyles(styles);
        return element;
    }

    // Javadoc inherited.
    public Text createText() {
        return new TextImpl(this);
    }

    // Javadoc inherited.
    public Text createText(String contents) {
        Text text = new TextImpl(this);
        text.append(contents);
        return text;
    }

    // Javadoc inherited.
    public Comment createComment() {
        return new CommentImpl(this);
    }

    // Javadoc inherited.
    public Comment createComment(String contents) {
        Comment comment = new CommentImpl(this);
        comment.append(contents);
        return comment;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isDebug() {
        return debug;
    }

    public DOMFactory getDebugFactory() {
        return DEBUG_DOM_FACTORY;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Oct-05	9825/1	pduffin	VBM:2005091502 Corrected device name and made use of new stylistic property.

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 ===========================================================================
*/

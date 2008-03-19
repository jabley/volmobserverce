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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom.debug;

import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.compiler.InlineStyleSheetCompilerFactory;
import com.volantis.styling.compiler.StyleSheetCompilerFactory;
import com.volantis.styling.engine.Attributes;
import com.volantis.styling.engine.StylingEngine;
import com.volantis.mcs.dom.WalkingDOMVisitorStub;
import com.volantis.mcs.dom.DOMWalker;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Document;

/**
 * Apply styles to a document using the 'style' attributes.
 */
public class DocumentStyler
        extends WalkingDOMVisitorStub {

    private final StylingEngine stylingEngine;
    private final String namespace;

    public DocumentStyler(StylingEngine stylingEngine, final String namespace) {
        this.stylingEngine = stylingEngine;
        this.namespace = namespace;
    }

    public DocumentStyler(String namespace) {
        this.namespace = namespace;

        StylingFactory factory = StylingFactory.getDefaultInstance();

        StyleSheetCompilerFactory inlineCompilerFactory =
                new InlineStyleSheetCompilerFactory(null);

        stylingEngine = factory.createStylingEngine(inlineCompilerFactory);
    }

    public void style(Document document) {
        DOMWalker walker = new DOMWalker(this);
        walker.walk(document);
    }

    // Javadoc inherited.
    public void visit(Element element) {
        if (element.isEmpty()) {
            // This will never be styled properly because the before and
            // after children methods are never called so do it in here.
            startStyling(element);
            endStyling(element);
        }
    }

    // Javadoc inherited.
    public void beforeChildren(final Element element) {
        startStyling(element);
    }

    private void startStyling(final Element element) {
        Attributes attributes = new Attributes() {
            public String getAttributeValue(String namespace, String localName) {
                return element.getAttributeValue(localName);
            }
        };

        stylingEngine.startElement(namespace,
                element.getName(), attributes);
        Styles styles = stylingEngine.getStyles();
        element.setStyles(styles);

        // Clear the class and style attributes.
        element.removeAttribute("class");
        element.removeAttribute("style");
    }

    // Javadoc inherited.
    public void afterChildren(Element element) {
        endStyling(element);
    }

    private void endStyling(Element element) {
        stylingEngine.endElement(namespace,
                element.getName());
    }
}

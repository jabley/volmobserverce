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
package com.volantis.mcs.protocols.css;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.NodeIteratee;
import com.volantis.mcs.dom.RecursingDOMVisitor;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DOMTransformer;
import com.volantis.styling.PseudoElement;
import com.volantis.styling.PseudoElements;
import com.volantis.styling.Styles;

/**
 * Parses the document tree and removes before and after pseudo elements,
 * creating new elements in their place.
 */
public class PseudoElementDOMTransformer implements DOMTransformer {

    /**
     * The factory for elements replacing pseudo elements.
     */
    private final ReplacementPseudoElementFactory replacementFactory;

    /**
     * Creates the DOM transformer with the given replacement factory.
     * @param replacementFactory
     */
    public PseudoElementDOMTransformer
            (ReplacementPseudoElementFactory replacementFactory) {
        this.replacementFactory = replacementFactory;
    }

    // javadoc inherited
    public Document transform(DOMProtocol protocol, Document document) {

        BeforeReplacer beforeReplacer = new BeforeReplacer();
        beforeReplacer.replacePseudoElements(document);

        AfterReplacer afterReplacer = new AfterReplacer();
        afterReplacer.replacePseudoElements(document);

        return document;
    }

    /**
     * Abstract replacer that takes pseudo elements and replaces them with real
     * elements.
     *
     * This is similar to but simpler than the
     * {@link com.volantis.mcs.protocols.trans.TransMapper} system as it doesn't
     * need configuring.
     */
    private abstract class AbstractReplacer extends RecursingDOMVisitor {

        /**
         * This method removes any :before pseudo elements and inserts elements
         * to effect the pseudo element.
         *
         * @param document from which to remove null named elements
         */
        void replacePseudoElements(Document document) {
            document.forEachChild(this);
        }

        // Javadoc inherited.
        public void visit(Element element) {

            Styles styles = element.getStyles();
            if (styles != null) {
                Styles pseudoStyles =
                        styles.removeNestedStyles(getPseudoElement());
                if (pseudoStyles != null) {
                    Element e = replacementFactory
                            .createElement(element, pseudoStyles);
                    if (e != null) {
                        insertElement(element, e);
                    }
                }
            }

            element.forEachChild(this);
        }

        /**
         * Gets the pseudo element required for the replacement.
         * @return the pseudo element.
         */
        abstract PseudoElement getPseudoElement();

        /**
         * Insert the child into the parent.
         * @param parent the parent element.
         * @param child the child element.
         */
        abstract void insertElement(Element parent, Element child);
    }

    /**
     * {@link NodeIteratee} implementation which removes all :before pseduo
     * elements from a {@link Document}and inserts markup to effect the
     * pseudo element.
     */
    private class BeforeReplacer extends AbstractReplacer {

        // javadoc inherited
        PseudoElement getPseudoElement() {
            return PseudoElements.BEFORE;
        }

        // javadoc inherited
        void insertElement(Element parent, Element child) {
            parent.addHead(child);
        }
    }

    /**
     * {@link NodeIteratee} implementation which removes all :after pseduo
     * elements from a {@link Document}and inserts markup to effect the
     * pseudo element.
     */
    private class AfterReplacer extends AbstractReplacer {

        // javadoc inherited
        PseudoElement getPseudoElement() {
            return PseudoElements.AFTER;
        }

        // javadoc inherited
        void insertElement(Element parent, Element child) {
            parent.addTail(child);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 07-Sep-05	9413/3	schaloner	VBM:2005070406 Changed style property iteration to direct access

 06-Sep-05	9413/1	schaloner	VBM:2005070406 Implemented before and after pseudo-element support

 ===========================================================================
*/

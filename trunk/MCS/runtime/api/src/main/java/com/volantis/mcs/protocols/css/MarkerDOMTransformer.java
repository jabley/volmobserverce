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
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.RecursingDOMVisitor;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DOMTransformer;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.ContentKeywords;
import com.volantis.mcs.themes.properties.ListStylePositionKeywords;
import com.volantis.styling.PseudoElements;
import com.volantis.styling.Styles;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.shared.iteration.IterationAction;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MarkerDOMTransformer
        implements DOMTransformer {

    /**
     * The "marker content" finder.
     */
    private final MarkerContentFinder markerContentFinder =
            new MarkerContentFinder();

    // javadoc inherited
    public Document transform(
            DOMProtocol protocol,
            Document document) {

        // get the lists in the document
        ListFinder listFinder = new ListFinder();
        Element[] listElements = listFinder.getListElements(document);

        // select the transform to use for each list element.
        for (int i = 0; i < listElements.length; i++) {

            Element listElement = listElements[i];
            MarkerProcessor markerProcessor = getMarkerProcessor(listElement);

            if (markerProcessor != null) {
                markerProcessor.process(listElement, protocol.getInserter());
            } else {
                throw new IllegalStateException(
                        "Could not determine required marker processor");
            }
        }

        return document;
    }

    /**
     * Gets the necessary marker processor based on the style values set on
     * the list and list items.
     *
     * @param element the list element.
     * @return the appropriate marker processor.
     */
    private MarkerProcessor getMarkerProcessor(Element element) {
        MarkerProcessor markerProcessor = null;
        if (isDefaultMarkerContent(element)) {
            // no custom content is set
            // the browser's inbuilt list renderer can be used.
            markerProcessor = new DefaultMarkerProcessor();
        } else {
            Styles styles = element.getStyles();
            MutablePropertyValues propertyValues = styles.getPropertyValues();
            StyleValue listStylePosition =
                    propertyValues.getComputedValue(
                            StylePropertyDetails.LIST_STYLE_POSITION);

            if (listStylePosition == ListStylePositionKeywords.OUTSIDE) {
                // table-based emulation deals with the outside position
                markerProcessor = new TableMarkerProcessor();
            } else if (listStylePosition == ListStylePositionKeywords.INSIDE) {
                // block and inline-based emulation deals with the inside
                // position
                markerProcessor = new BlockInlineMarkerProcessor();
            } else {
                throw new IllegalStateException(
                        "Unknown list-style-position value: " +
                                listStylePosition);
            }
        }

        return markerProcessor;
    }

    /**
     * Checks if the tree whose root is element contains a marker pseudo style
     * that defines a content value.
     *
     * @param element the root of the tree to check.
     * @return true iff no marker was found with a content value.
     */
    private boolean isDefaultMarkerContent(Element element) {
        // if marker content was found, the marker does not use the default
        // symbol.
        return !markerContentFinder.isMarkerContentFound(element);
    }

    /**
     * Examines each node and stores it if it is a support list element.
     */
    private class ListFinder
            extends RecursingDOMVisitor {

        private final List lists = new ArrayList();

        public void visit(Element element) {
            if (CSSConstants.isSupportedList(element)) {
                lists.add(element);
            }
            element.forEachChild(this);
        }

        Element[] getListElements(Document document) {

            document.forEachChild(this);

            Element[] elements = new Element[lists.size()];
            return (Element[]) lists.toArray(elements);
        }
    }

    /**
     * Checks nodes for marker pseudo elements containing content values.
     */
    private static class MarkerContentFinder
            extends RecursingDOMVisitor {

        /**
         * Flag indicating the discovery of a marker's content.
         */
        private boolean markerContentFound = false;


        public void visit(Element element) {
            Styles styles = element.getStyles();
            if (styles != null) {
                Styles markerStyles =
                        styles.findNestedStyles(PseudoElements.MARKER);
                if (markerStyles != null) {
                    MutablePropertyValues propValues =
                            markerStyles.getPropertyValues();
                    StyleValue content =
                            propValues.getComputedValue(
                                    StylePropertyDetails.CONTENT);
                    markerContentFound =
                            content != ContentKeywords.NORMAL &&
                                    content != ContentKeywords.NONE;
                }
            }
        }

        /**
         * Checks the node's styles for marker pseudo elements.  If found,
         * and a content value is specified the markerContentFlag is set to true
         * and the the remainder of elements are skipped over.
         *
         * @param node
         */
        public IterationAction next(Node node) {
            // don't bother checking further if marker content has already
            // been found
            if (!markerContentFound) {
                node.accept(this);
            }
            
            return IterationAction.CONTINUE;
        }

        /**
         * Gets the marker's content discovery flag.
         *
         * @param element
         * @return true iff a marker with content was found.
         */
        public boolean isMarkerContentFound(Element element) {
            markerContentFound = false;
            element.forEachChild(this);
            return markerContentFound;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	9440/2	pduffin	VBM:2005070711 Merged changes from main trunk

 05-Oct-05	9440/1	schaloner	VBM:2005070711 Added marker pseudo-element support

 ===========================================================================
*/

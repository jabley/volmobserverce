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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/XHTMLBasic_MIB2_1Transformer.java,v 1.12 2003/03/20 15:15:33 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Sep-02    Byron           VBM:2002091904 - Created
 * 16-Oct-02    Byron           VBM:2002101605 - Added transformTableCell(),
 *                              eligibleForTransformation(). Removed
 *                              floatImageToLeft() and isFloatableImageTable().
 *                              Modified transformTableElement()
 * 18-Oct-02    Phil W-S        VBM:2002081322 - Ensure that the table cell's
 *                              class is preserved.
 * 23-Oct-02    Byron           VBM:2002101404 - Moved transformTableCell to
 *                              superclass and updated transformTableElement()
 *                              to use correct parameters for flattenTable.
 *                              Removed redundant type casts.
 * 29-Oct-02    Byron           VBM:2002101404 - Added method logDOM() and
 *                              called it from the transform() method().
 * 05-Nov-02    Byron           VBM:2002103106 - Removed method logDOM()
 *                              which is now accessible from super class
 * 11-Dec-02    Byron           VBM:2002121126 - Added getFirstChildElement()
 *                              and modified transformTableElement() to use new
 *                              method. Updated matches to check for null
 *                              nodes.
 * 12-Dec-02    Byron           VBM:2002121126 - Modified eligibleFor-
 *                              Transformation() to correctly handle Text nodes
 *                              and updated comments and logger output in
 *                              transform() method..
 * 24-Jan-03    Byron           VBM:2003012404 - Added isElementDisposable
 *                              and enclosesTableOrDivMarkup.
 * 28-Jan-03    Byron           VBM:2003012404 - Modified the constructor to  
 *                              set supportsSingleColumnTableReduction to true.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.TransformingVisitor;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.synergetics.log.LogDispatcher;


/**
 * Used to transform the dom tree for XHTMLBasic_MIB2_1, if necessary. This
 * specifically looks for nested one-row, two-column tables where the
 * left-hand cell contains an image. If this case is found, the table is
 * replaced by a div and the image is set to float left.
 */
public class XHTMLBasic_MIB2_1Transformer
        extends XHTMLBasicTransformer {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory
                    .createLogger(XHTMLBasic_MIB2_1Transformer.class);


    protected TransformingVisitor getDOMVisitor(DOMProtocol protocol) {
        return new MIB2_1TreeVisitor(protocol);
    }

    /**
     * Override the inner class in XHTMLBasicTransformer to modify the manner
     * of the transformation (floating images in 2x1 tables)
     */
    class MIB2_1TreeVisitor
            extends TreeVisitor {

        /**
         * Create the visitor by calling the super class' constructor.
         *
         * @param protocol the DOMProtocol
         */
        public MIB2_1TreeVisitor(DOMProtocol protocol) {
            super(protocol);
            supportsSingleColumnTableReduction = true;
        }

        /**
         * Find the first <code>Element</code> for the element passed in.
         *
         * @param element the element to find
         * @return the element if it is an <code>Element</code> or null if
         *         none found
         * @todo better this should use getNextChild()...as in the super class. In
         * fact this method may be moved to the super class
         */
        protected Element getFirstChildElement(Element element) {
            if (element == null) {
                return null;
            }
            Node node = element.getHead();
            while ((node != null) && !(node instanceof Element)) {
                node = node.getNext();
            }
            // Either null or Element
            return (Element) node;
        }

        // javadoc inherited
        protected void transformTableElement(
                Element tableElement, int rows,
                int columns) {

            if (columns == 1) {

                if (logger.isDebugEnabled()) {
                    logger.debug("Flattening table as it only has one column");
                }
                tableElement = flattenTable(tableElement, rows, columns);
                tableElement.forEachChild(this);

            } else if (insideTable) {

                if (logger.isDebugEnabled()) {
                    logger.debug("Flattening table as it is inside another " +
                            "table");
                }

                if (eligibleForTransformation(tableElement, rows, columns)) {

                    Element e = getFirstChildElement(tableElement);
                    if (matches(e, "tr")) {
                        e = getFirstChildElement(e);

                        if (matches(e, "td")) {
                            e = getFirstChildElement(e);

                            if (matches(e, "table")) {
                                setDisplayProperty(e.getStyles(),
                                        DisplayKeywords.INLINE);
                                if (logger.isDebugEnabled()) {
                                    logger.debug(
                                            "Special table identified and" +
                                                    " tagged with nobreak style");
                                }
                            }
                        }
                    }
                }

                tableElement = flattenTable(tableElement, rows, columns);
                tableElement.forEachChild(this);

            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Top level multi column table does not need " +
                            "flattening");
                }
                insideTable = true;
                tableElement.forEachChild(this);
                insideTable = false;
            }
        }

        /**
         * Return true if the node is an element an it's name matches name
         *
         * @param node the node which should be an element
         * @param name the name always non-null
         * @return true if the node is an element an it's name matches name
         */
        protected boolean matches(Node node, String name) {
            return ((node != null) &&
                    (node instanceof Element) &&
                    (name.equals(((Element) node).getName())));
        }


        /**
         * Find the single img node amongst this node and its siblings.
         *
         * @return the image node or null if no img element is found, the
         *         image's siblings are not whitespace or there is more than one img.
         */
        protected Node findImageNode(Node node) {
            Node imageNode = null;

            do {
                if (node instanceof Text) {
                    Text text = (Text) node;

                    if (!text.isWhitespace()) {
                        char chars[] = text.getContents();

                        if (chars.length > 0) {
                            String value = new String(chars).trim();

                            if (value.length() > 1) {
                                return null;
                            }
                        }
                    }
                } else if (node instanceof Element &&
                        "img".equals(((Element) node).getName()) &&
                        (imageNode == null)) {
                    imageNode = node;
                } else {
                    // If the node is any other type of element or if more than one
                    // image has been found
                    return null;
                }
            } while ((node = node.getNext()) != null);

            return imageNode;
        }

        /**
         * Return true if the table may be transformed, false otherwise. The table
         * may be transformed if:
         * <ul>
         * <li>the table has 1 row and 2 columns.</li>
         * <li>the 2nd column contains 1 image only</li>
         * <li>there are no <code>Text</code> node(s) before the tr and each td.
         * </ul>
         *
         * @param tableElement the element that will be examined to see if it
         *                     contains meets the transformation criteria.
         * @param rows         the number of rows in the table
         * @param columns      the number of columns in the able
         * @return true if this table meets the criteria for
         *         transformation, false otherwise
         */
        protected boolean eligibleForTransformation(
                Element tableElement,
                int rows,
                int columns) {
            if ((columns == 2) && (rows == 1)) {
                Node node = tableElement.getHead();

                if (matches(node, "tr")) {

                    // See if the 2nd column contains an image.
                    node = ((Element) node).getHead();

                    // 1st column
                    if (matches(node, "td")) {
                        node = node.getNext();
                        // 2nd column
                        if (matches(node, "td")) {
                            node = ((Element) node).getHead();
                            if (matches(node, "div")) {
                                node = ((Element) node).getHead();
                            }
                            return (findImageNode(node) != null);
                        }
                    }
                }
            }
            return false;
        }

        /**
         * Return true is the specified element encloses any combination of div and
         * table elements. Return false if it encloses any other tags (e.g. text or
         * form elements).
         *
         * @param element the element to see what it encloses
         * @return true if the specified element encloses and combination
         *         of div and table elements, false otherwise.
         */
        protected boolean enclosesTableOrDivMarkup(Element element) {
            boolean enclosesTableOrDiv = true;
            Node child = element.getHead();
            while (enclosesTableOrDiv && child != null) {
                if (child instanceof Text) {
                    enclosesTableOrDiv = false;
                } else {
                    String childName = ((Element) child).getName();
                    if (!("table".equalsIgnoreCase(childName) ||
                            ("div".equalsIgnoreCase(childName)))) {
                        enclosesTableOrDiv = false;
                    }
                }
                child = child.getNext();
            }
            return enclosesTableOrDiv;
        }

        /**
         * Return true if this element is marked for preservation, false
         * otherwise.
         *
         * @param element the element that will be examined to see if it is
         *                marked for preservation, or not.
         * @return true if this element is marked for preservation,
         *         false otherwise.
         */
        protected boolean isElementDisposable(Element element) {
            if ((PRESERVE_OBJECT == element.getObject()) &&
                    !enclosesTableOrDivMarkup(element)) {
                return false;
            }
            return true;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 22-Aug-05	9184/5	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 22-Aug-05	9184/2	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 22-Aug-05	9223/2	emma	VBM:2005080403 Remove style class from within protocols and transformers

 19-Aug-05	9289/1	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 ===========================================================================
*/

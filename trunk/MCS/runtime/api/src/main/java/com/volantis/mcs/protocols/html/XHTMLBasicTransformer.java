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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/XHTMLBasicTransformer.java,v 1.21 2003/04/28 09:53:20 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 26-Apr-02    Paul            VBM:2002042205 - Created to fix up the XHTML
 *                              Basic markup.
 * 05-Jun-02    Adrian          VBM:2002021103 - Added methods getNextChild and
 *                              getRealNode to get the next node with
 *                              KEEPTOGETHER_ELEMENT nodes invisible - ie the
 *                              child of the KEEPTOGETHER_ELEMENT is returned
 *                              instead.  Also modified method flattenTable to
 *                              restore KEEPTOGETHER_ELEMENT nodes where they
 *                              previously existed as parents of tr tags
 * 10-Jun-02    Adrian          VBM:2002021103 - Changed keeptogether to
 *                              keepTogether.
 * 25-Sep-02    Byron           VBM:2002091904 - Added transformTableElement()
 *                              so that derived classes may override this
 *                              method, if necessary
 * 16-Oct-02    Byron           VBM:2002101605 - Added transformTableCell() and
 *                              removed VP_FLOATLEFT attribute
 * 17-Oct-02    Phil W-S        VBM:2002081322 - Make sure that style classes
 *                              on table cells are preserved during the
 *                              table flattening.
 * 23-Oct-02    Byron           VBM:2002101404 - Modified visit() to remove
 *                              empty divs, flattentable() to take additional
 *                              parameters and internal refactoring, transform-
 *                              TableCell to use no break according it's new
 *                              parameter. Added method removeInnerDivTag()
 * 28-Oct-02    Byron           VBM:2002101404 - Modified removeInnerDivTag()
 *                              to remove divs that are contained in outer divs
 *                              and preserve their class/id attributes
 * 01-Nov-02    Byron           VBM:2002103106 - Added findInnerDiv() method
 *                              and modified removeInnerDivTag() to use the new
 *                              method. Added allocateElement() method to
 *                              allocateElements without requiring the dom
 *                              pool.
 * 05-Nov-02    Byron           VBM:2002103106 - Added method logDOM(),
 *                              getChildrenCount(), modified transform() to
 *                              call logDOM(). Renamed findInnerDiv to
 *                              findInnerElement generalized this method.
 * 08-Nov-02    Byron           VBM:2002110508 - Added parentTagMatches() and
 *                              modified visit to use parentTagMatches not to
 *                              remove div tags that have 'form' tags as a
 *                              direct parent of the div tag
 * 22-Nov-02    Phil W-S        VBM:2002112006 - Fix retention of class and ID
 *                              attributes.
 * 27-Nov-02    Byron           VBM:2002112005 - Added processDivElement as a
 *                              result of refactoring. Renamed findInnerElement
 *                              to findOnlyChild and improved functionality
 *                              to perform a recursive search. Renamed get-
 *                              ChildrenCount to countChildren. Modified
 *                              flattenTable to use forceLinebreak. Renamed
 *                              cols and rows to totalColumns and totalRows.
 * 05-Dec-02    Byron           VBM:2002112210 - Modified parentMatches() hard-
 *                              coded string, processDivElement() and
 *                              flattenTable() to check for body tag as parent
 *                              before removing any div tags.
 * 06-Dec-02    Byron           VBM:2002112002 - Added findSibling(),
 *                              canRemoveElement(). Modified
 *                              processDivElement().
 * 13-Dec-02    Byron           VBM:2002112002 - Added isNodeEligibleForRemoval
 *                              and modified processDivElement to use it.
 * 24-Jan-03    Byron           VBM:2003012404 - Modified inner class
 *                              TreeVisitor to have PRESERVE_OBJECT attribute.
 *                              Renamed isNodeEligibleForRemoval. Modified
 *                              flattenTable and transformTableCell and added
 *                              isElementDisposable.
 * 28-Jan-03    Byron           VBM:2003012404 - Added the member variable
 *                              supportsSingleColumnTableReduction. Modified
 *                              flattenTable to use abovementioned variable.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 24-Apr-03    Allan           VBM:2003042401 - Made into an
 *                              VisitorBasedDOMTransformer.
 *                              TreeVisitor.allocateElement() defers to
 *                              allocateElement() in VisitorBasedDOMTransformer
 * 24-Apr-03    Allan           VBM:2003042401 - Calls to logDOM now use the
 *                              VisitorBasedDOMTransformer version.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DOMVisitorBasedTransformer;
import com.volantis.mcs.protocols.DescendantStylesMerger;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.TransformingVisitor;
import com.volantis.mcs.protocols.dissection.DissectionConstants;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesMerger;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * This class fixes up the dom so it is valid XHTML Basic.
 */
public class XHTMLBasicTransformer extends DOMVisitorBasedTransformer
        implements HTMLConstants, DissectionConstants {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(XHTMLBasicTransformer.class);


    protected TransformingVisitor getDOMVisitor(DOMProtocol protocol) {
        return new TreeVisitor(protocol);
    }

    /**
     * Inner class to permit visit's to the DOM tree.
     */
    protected class TreeVisitor extends AbstractDivRemover {

        private final ProtocolConfiguration configuration;

        private final DOMFactory factory;

        /**
         * A flag which indicates that we are inside a table.
         */
        protected boolean insideTable;

        /**
         * Use this constant to flag a particular node to be preserved.
         */
        protected final Object PRESERVE_OBJECT = new Object();

        /**
         * Set this to true in MIB2_1 transformer's implementation
         */
        protected boolean supportsSingleColumnTableReduction = false;
        
        private DescendantStylesMerger descendantStylesMerger;

        public TreeVisitor(DOMProtocol protocol) {
            configuration = protocol.getProtocolConfiguration();
            factory = protocol.getDOMFactory();
        }


        public void visit(Element element) {
            String name = element.getName();

            if ("table".equals(name)) {
                transformTable(element);
            } else {
                super.visit(element);
            }
        }

        /**
         * Return true if the element has null attributes for class and id,
         * and no important style properties are set, false otherwise.
         *
         * @param element the element to be processed
         * @return true if the element has null attributes for class and id,
         *         and no important style properties are set, false otherwise
         */
        protected boolean isDivEligibleForRemoval(Element element) {
            return (element != null && element.getAttributeValue("id") == null
                    && !configuration.
                    optimisationWouldLoseImportantStyles(element));
        }


        /**
         * If the outer tag does not contain an id attribute and contains one
         * element which is a div tag, then this inner element may be removed
         * (care should be taken to preserve the inner tag's attribute(s) by
         * promoting these attributes to the parent tag).
         * <p/>
         * For example, if we have:
         * <pre>
         * &lt;div class="VE-pane VP-nobreak"&gt; &lt;!-- outer div --&gt;
         *   &lt;div class="VE-pane VE-test" id="111"&gt &lt;!-- inner div --&gt
         * ...
         *   &lt;/div&gt;
         * &lt;/div&gt
         * </pre>
         * then the inner div tag may be removed and any class and id
         * attributes not contained in the outer div tag are moved to the
         * parent div tag. In this example, 'VE-test' and id="111" will be
         * moved to the parent tag.
         * <p/>
         * We assume that the style class attribute values of the received
         * element will be valid (i.e. having multiple classes only if multiple
         * classes are supported by the device).
         *
         * @param outerDiv the outer div tag as an <code>Element</code> object.
         */
        protected void removeInnerDivTag(Element outerDiv) {

            // If the outer div tag does not have an id and its child is an
            //  element, we possibly may be able to remove the inner div tag
            if ((outerDiv.getAttributeValue("id") == null)
                    && (outerDiv.getHead() instanceof Element)) {

                // Find the inner div tag that is contained by the outerdiv
                Element innerDiv = findOnlyChild(outerDiv, "div");

                if (innerDiv == MULTIPLE_CHILDREN_FOUND) {
                    innerDiv = null;
                }

                // This div contains just one nested inner div tag
                if ((innerDiv != null) && isElementDisposable(innerDiv)) {

                    promoteIDAttributeAsNeeded(innerDiv, outerDiv);
                    promoteStylesAsNeeded(innerDiv, outerDiv);

                    // Remove the div tag
                    innerDiv.setName(null);
                }
            }
        }

        /**
         * Promote the leaf element id attribute if required from the
         * <code>innerDiv</code> to the <code>outerDiv</code>.
         *
         * @param innerDiv the inner (leaf) div element. Not <code>null</code>.
         * @param outerDiv the containing parent div element. Not
         *                 <code>null</code>. This should not have a
         *                 <code>id</code> attribute itself.
         */
        private void promoteIDAttributeAsNeeded(Element innerDiv,
                                                Element outerDiv) {
            if (innerDiv.getAttributeValue("id") != null) {
                outerDiv.setAttribute("id",
                        innerDiv.getAttributeValue("id"));
            }
        }

        /**
         * Merge all styles (if any) from the <code>innerDiv</code> to the
         * <code>outerDiv</code>.
         *
         * @param innerDiv the inner (leaf) div element. Not <code>null</code>.
         * @param outerDiv the containing parent div element. Not
         *                 <code>null</code>.
         */
        private void promoteStylesAsNeeded(Element innerDiv,
                                           Element outerDiv) {
            if (innerDiv.getStyles() != null) {
                StylesMerger merger = StylingFactory.
                        getDefaultInstance().getStylesMerger();
                outerDiv.setStyles(merger.merge(innerDiv.getStyles(),
                        outerDiv.getStyles()));
            }
        }

        /**
         * Get the next sibling of the specified node unless the node is a
         * <code>DissectionConstants.KEEPTOGETHER_ELEMENT</code> in which case
         * get the first child of the node.
         *
         * @param node The <code>Node</code> to get the next sibling.
         * @param stop The <code>Node</code>
         */
        protected Node getNextChild(Node node, Node stop) {

            if (node == null) {
                return null;
            } else if (node == stop) {
                if (node instanceof Element) {
                    Element element = (Element) node;
                    Node next = element.getHead();
                    if (next instanceof Element) {
                        Element nextElement = (Element) next;
                        if (KEEPTOGETHER_ELEMENT.equals(
                                nextElement.getName())) {
                            return getRealNode(nextElement.getHead());
                        }
                    }
                    return next;
                } else {
                    return null;
                }
            }

            if (node instanceof Element) {
                Element element = (Element) node;
                if (KEEPTOGETHER_ELEMENT.equals(element.getName())) {
                    return getRealNode(element.getHead());
                }
            }

            Node next = node.getNext();
            if (next == null) {
                Node parent = node.getParent();
                if (parent != null) {
                    if (parent.equals(stop)) {
                        return null;
                    } else {
                        return getRealNode(parent.getNext());
                    }
                } else {
                    return null;
                }
            } else if (next instanceof Element) {
                Element element = (Element) next;
                if (KEEPTOGETHER_ELEMENT.equals(element.getName())) {
                    return getRealNode(element.getHead());
                }
            }
            return next;
        }

        /**
         * if the specified node is a KeepTogether Element then return the
         * first descendant node that is not a KeepTogether Element.
         *
         * @param node The node to test.
         * @return either the originally specified node or the first
         *         descendant which is not a KeepTogether Element.
         */
        protected Node getRealNode(Node node) {
            if (node == null) {
                return null;
            }

            if (node instanceof Element) {
                Element element = (Element) node;
                if (KEEPTOGETHER_ELEMENT.equals(element.getName())) {
                    return getRealNode(element.getHead());
                }
            }
            return node;
        }

        /**
         * Transform the given table element and its rows and cells by first
         * determining the size (rows and columns count) of the table and then
         * calling {@link #transformTableElement} to convert the table markup
         * to non-table markup.
         *
         * @param tableElement the element that is a table to be transformed
         */
        protected void transformTable(Element tableElement) {

            int totalColumns = 0;
            int totalRows = 0;

            // Count the rows and columns.
            for (Node r = tableElement.getHead(); r != null;
                 r = getNextChild(r, tableElement)) {
                if (!(r instanceof Element)) {
                    continue;
                }

                Element row = (Element) r;
                if (!row.getName().equals("tr")) {
                    continue;
                }

                int cells = 0;
                for (Node c = row.getHead(); c != null; c = c.getNext()) {
                    if (!(c instanceof Element)) {
                        continue;
                    }

                    Element column = (Element) c;
                    String columnName = column.getName();
                    if (!columnName.equals("td")) {
                        continue;
                    }

                    cells += 1;
                }

                // Increment the number of rows.
                totalRows += 1;

                // If the number of cells in this row exceed the current column count
                // then update the column count.
                if (cells > totalColumns) {
                    totalColumns = cells;
                }
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Table has " + totalRows + " rows and "
                        + totalColumns + " columns");
            }

            // If the table only has one column then we can flatten it without
            // greatly affecting the layout.
            transformTableElement(tableElement, totalRows, totalColumns);
        }

        /**
         * Transform the table by 'flattening it out', replacing the table
         * markup with non-table markup.
         *
         * @param tableElement the table that will be transformed
         * @param totalRows    the number of rows in the table
         * @param totalColumns the number of columns in the table
         */
        protected void transformTableElement(Element tableElement,
                                             int totalRows,
                                             int totalColumns) {
            if (totalColumns == 1) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Flattening table as it only has one column");
                }
                tableElement = flattenTable(tableElement, totalRows,
                        totalColumns);
                tableElement.forEachChild(this);
            } else if (insideTable) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Flattening table as it is inside another " +
                            "table");
                }
                tableElement = flattenTable(tableElement, totalRows,
                        totalColumns);
                tableElement.forEachChild(this);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Top level multi column table does not " +
                            "need flattening");
                }
                insideTable = true;
                tableElement.forEachChild(this);
                insideTable = false;
            }
        }

        /**
         * Replace the table with a "div" and remove the table and separate the
         * contents of the cells with line breaks.
         *
         * @param tableElement the element representing the table
         * @param totalRows    the total number of rows in this table
         * @param totalColumns the total number of columns in this table
         * @return the root element after the table has been flattened
         */
        protected Element flattenTable(Element tableElement, int totalRows,
                                       int totalColumns) {

            Element tableDivElement = allocateElement();
            tableDivElement.setName("div");
            final Styles divStyles = tableElement.getStyles();
            tableDivElement.setStyles(divStyles);
            setDisplayProperty(divStyles, DisplayKeywords.BLOCK);

            boolean useDiv = false;
            String value;

            // Copy the id attribute and Styles .
            if ((value = tableElement.getAttributeValue("id")) != null) {
                tableDivElement.setAttribute("id", value);
                useDiv = true;
            }

            if (configuration.optimisationWouldLoseImportantStyles(tableElement)) {
                useDiv = true;
            }

            if (parentTagMatches(tableElement, "body")) {
                useDiv = true;
            }

            if (!useDiv) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Removing table's div tag: " + tableElement);
                }
                tableDivElement.setName(null);
            }

            boolean insideKeepTogether = false;

            Element newParent = null;

            // Iterate over the elements.
            for (Node r = tableElement.getHead();
                 r != null;
                 r = getNextChild(r, tableElement)) {

                if (!(r instanceof Element)) {
                    continue;
                }

                Element row = (Element) r;
                if (!row.getName().equals("tr")) {
                    continue;
                }
                if (row.getParent().getName().equals(KEEPTOGETHER_ELEMENT)) {
                    if (!insideKeepTogether) {
                        insideKeepTogether = true;
                        newParent = allocateElement();
                        newParent.setName(KEEPTOGETHER_ELEMENT);
                        newParent.addToTail(tableDivElement);
                    }
                } else {
                    insideKeepTogether = false;
                    newParent = tableDivElement;
                }
                // Iterate over the column elements
                int currentColumn = 0;
                for (Node c = row.getHead(); c != null; c = c.getNext()) {
                    if (!(c instanceof Element)) {
                        continue;
                    }

                    Element column = (Element) c;
                    String columnName = column.getName();
                    if (!"td".equals(columnName) && !"tr".equals(columnName)) {
                        continue;
                    }

                    // Force a line break if the current column is the last
                    // column (row break) and this column's tail child element
                    // is not a table element (in order to prevent duplicate
                    // <br> tags from being generated from nested tables).
                    // AND if it is not the last row
                    boolean forceLineBreak =
                            (currentColumn == (totalColumns - 1))
                                    && !((column.getTail() instanceof Element)
                                    && "table".equals(
                                    ((Element) column.getTail()).getName()));

                    // Always use VP-nobreak (default value).
                    boolean doInlineDiv = true;

                    // Don't use VP-nobreak or line breaks if we are in a
                    // single column table.
                    if (supportsSingleColumnTableReduction
                            && totalColumns == 1) {
                        forceLineBreak = false;
                        doInlineDiv = false;
                    }

                    transformTableCell(column, newParent, forceLineBreak,
                            doInlineDiv);
                    ++currentColumn;
                }
            }

            // Replace the table with the div element.
            tableDivElement.replace(tableElement);

            return tableDivElement;
        }

        private DescendantStylesMerger getDescendantStylesMerger() {
            if (descendantStylesMerger == null) {
                descendantStylesMerger = new DescendantStylesMerger();
            }
            return descendantStylesMerger;
        }

        /**
         * Transform the table's cell into a div tag. Add this div tag to
         * parent's list of children. The parent should be the table (div)
         * itself.
         * <p/>
         * Eventually the styleClass will not be used at this point because the
         * new StylingEngine will have already used it to pre-calculate the
         * Styles. Currently, there is duplication of information between
         * styleClass and Styles, and this method will be simplified when this
         * is resolved.
         *
         * @param cell           the element representing the column.
         * @param newParent      the new parent for this element
         * @param forceLineBreak true if the line requires a break, false
         *                       otherwise
         * @param doInlineDiv    true if we should generate a VP_NO_BREAK,
         *                       false otherwise
         */
        protected void transformTableCell(Element cell,
                                          Element newParent,
                                          boolean forceLineBreak,
                                          boolean doInlineDiv) {

            Styles styles = cell.getStyles();

            // If doInlineDiv is false then we need to insert a new div element
            // and move the children and styles of the 'column' element to the
            // div; if true we can just move them to the newParent element.
            if (doInlineDiv) {
                // set the display property to inline
                setDisplayProperty(styles, DisplayKeywords.INLINE);
                if (styles != null) {
                    if (newParent.getName() != null) {
                        // new parent is not null named, and will appear in the
                        // output, therefore the styles can be pushed up
                        StylesMerger merger = StylingFactory.
                                getDefaultInstance().getStylesMerger();
                        newParent.setStyles(merger.merge(styles,
                                newParent.getStyles()));
                    } else {
                        // the new parent is null named, and will not appear in
                        // the output, so the styles should be pushed down to
                        // be preserved. merge the element's styles with those
                        // of its children
                        DescendantStylesMerger merger =
                                getDescendantStylesMerger();
                        merger.pushStylesDown(cell);
                    }
                }
                cell.setName(null);
                cell.addChildrenToTail(newParent);
            } else {
                Element cellDivElement = allocateElement();
                cellDivElement.setName("div");
                cellDivElement.setObject(PRESERVE_OBJECT);
                cellDivElement.setStyles(styles);

                // Change the cell to a block display.
                setDisplayProperty(styles, DisplayKeywords.BLOCK);

                // Add the children of the column to the cell div element.
                cell.addChildrenToTail(cellDivElement);
                cellDivElement.addToTail(newParent);
            }

            if (forceLineBreak) {
                Element lineBreak = allocateElement();
                lineBreak.setName("br");
                lineBreak.addToTail(newParent);
            }
        }

        /**
         * A helper method used to allocate a new element. This utilizes the
         * protocol's DOM pool, if available.
         *
         * @return a newly allocated DOM element
         */
        protected Element allocateElement() {
            return factory.createElement();
        }

        /**
         * Convenience method which sets the display property on the Styles.
         *
         * @param styles  on which to set the display property to inline. May
         *                be null.
         * @param display
         */
        protected void setDisplayProperty(
                Styles styles, final StyleKeyword display) {

            if (styles != null) {
                // Set the computed and specified value as otherwise the
                // display:inline property will be removed when generating CSS
                // as it is the initial value.
                MutablePropertyValues propertyValues = styles.getPropertyValues();
                propertyValues.setComputedAndSpecifiedValue(
                        StylePropertyDetails.DISPLAY, display);
            }
        }

        // javadoc inherited
        protected boolean isBlockyElement(final Node node) {
            if (!(node instanceof Element)) {
                return false;
            }

            final Element element = (Element) node;
            String name = element.getName();
            return name != null && configuration.isElementBlocky(name);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/2	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 22-Aug-05	9184/5	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 22-Aug-05	9184/2	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 22-Aug-05	9223/5	emma	VBM:2005080403 Remove style class from within protocols and transformers

 19-Aug-05	9289/1	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 22-Jul-05	8859/2	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 13-May-05	8233/1	emma	VBM:2005042706 Merge from 3.3.0 - multiple style classes generated when device didn't support it

 13-May-05	8192/1	emma	VBM:2005042706 Merge from 3.2.3 - multiple style classes generated when device didn't support it

 31-Mar-05	7527/3	geoff	VBM:2005032304 XHTML - No style is applied as style classes are written without a space.

 31-Mar-05	7527/1	geoff	VBM:2005032304 XHTML - No style is applied as style classes are written without a space.

 31-Mar-05	7523/1	geoff	VBM:2005032304 XHTML - No style is applied as style classes are written without a space.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 26-Aug-04	5323/17	pcameron	VBM:2004082015 Fixed XHTMLBasicTransformer's tree visitor inner class's findOnlyChild

 26-Aug-04	5287/12	pcameron	VBM:2004082015 Fixed XHTMLBasicTransformer's tree visitor inner class's findOnlyChild

 26-Aug-04	5287/10	pcameron	VBM:2004082015 Fixed XHTMLBasicTransformer's tree visitor inner class's findOnlyChild

 ===========================================================================
*/

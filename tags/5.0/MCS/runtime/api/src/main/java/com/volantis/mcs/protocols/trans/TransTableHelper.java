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
 * $Header: /src/voyager/com/volantis/mcs/protocols/trans/TransTableHelper.java,v 1.2 2003/01/15 12:42:10 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 10-Jan-03    Phil W-S        VBM:2002110402 - Created. Provides various
 *                              supporting methods for use in the specialist
 *                              TransTables (specifically related to the
 *                              canOptimizeStyle method).
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.RecursingDOMVisitor;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StylePercentage;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.BorderStyleKeywords;
import com.volantis.mcs.themes.properties.HeightKeywords;
import com.volantis.mcs.themes.properties.WidthKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.PropertyValues;

/**
 * Provides various supporting methods for use in the specialist TransTables
 * (specifically related to the canOptimizeStyle method) that are in the
 * HTML protocol family. Implements the singleton pattern.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public final class TransTableHelper {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    /**
     * Constant values used for validating input data.
     */
    private static final StylePercentage HUNDRED_PERCENT =
        STYLE_VALUE_FACTORY.getPercentage(null, 100);
    private static final StylePercentage ZERO_PERCENT =
        STYLE_VALUE_FACTORY.getPercentage(null, 0);
    private static final StyleLength ZERO_PIXELS =
        STYLE_VALUE_FACTORY.getLength(null, 0, LengthUnit.PX);

    /**
     * An instance of this class must be passed into the visitor to
     * control the search mechanism.
     */
    protected static abstract class ContextFinder
            extends RecursingDOMVisitor {

        public boolean includeCells;
        public String searchName;
        protected ElementHelper helper;
        public boolean visitChildren;

        protected boolean find(
                boolean includeCells, String searchName,
                ElementHelper helper, Element element) {

            resetRecursingState();

            this.includeCells = includeCells;
            this.searchName = searchName;
            this.helper = helper;
            visitChildren = true;
            
            visit(element);

            return isSkippingRemainder();
        }
    }

    /**
     * This helper class traverses a table (ignoring row or cell content's
     * content depending on the cell inclusion flag) looking for any elements
     * with a name equal to the search name. If one is found, the traversal
     * will return true, otherwise it will return false. Note that the
     * extended visitor pattern context object must be a Context instance.
     */
    protected static class ElementFinder extends ContextFinder {

        /**
         * Find the element with the specified name.
         *
         * @param includeCells whether the search should include the table's
         *                     cells as well as rows
         * @param searchName   the type of element to be searched for
         * @param helper       the ElementHelper needed by the visitors
         * @param table        the table from which the search will start.
         * @return True if the element was found, false otherwise.
         */
        public boolean findElement(
                boolean includeCells, String searchName, ElementHelper helper,
                Element table) {

            return find(includeCells, searchName, helper, table);
        }

        // Javadoc inherited.
        public void visit(Element element) {
            boolean found = searchName.equals(element.getName());
            if (found) {
                skipRemainder();
            } else {
                boolean visitChildren = this.visitChildren;

                if (includeCells && helper.isCell(element)) {
                    this.visitChildren = false;
                } else if (!includeCells && helper.isRow(element)) {
                    this.visitChildren = false;
                }

                if (visitChildren) {
                    element.forEachChild(this);
                }

                this.visitChildren = visitChildren;
            }
        }
    }

    /**
     * This helper class traverses a table (ignoring row or cell content
     * depending on the cell inclusion flag) looking for any elements
     * with a name attribute with the given search name. If one is found, the
     * traversal will return true, otherwise it will return false. Note that
     * the extended visitor pattern context object must be a Context instance.
     */
    protected static class AttributeFinder extends ContextFinder {

        /**
         * Find an element with an attribute with the specified name.
         *
         * @param includeCells whether the search should include the table's
         *                     cells as well as rows
         * @param searchName   the type of attribute to be searched for
         * @param helper       the ElementHelper needed by the visitors
         * @param table        the table from which the search will start.
         * @return True if the element was found, false otherwise.
         */
        public boolean findAttribute(
                boolean includeCells, String searchName, ElementHelper helper,
                Element table) {
            return find(includeCells, searchName, helper, table);
        }

        public void visit(Element element) {
            boolean found = (element.getAttributeValue(this.searchName) != null);
            if (found) {
                skipRemainder();
            } else {
                if ((includeCells && helper.isRow(element)) ||
                    !helper.isCell(element)) {
                    element.forEachChild(this);
                }
            }
        }
    }

    /**
     * The singleton instance.
     */
    private static final TransTableHelper instance = new TransTableHelper();

    /**
     * Helper object used to find specifically named elements within a
     * table (looking in row or cell content depending on the cell inclusion
     * flag given on search execution.
     * This can be static as no state data is held.
     */
    private final ElementFinder elementFinder =
        new ElementFinder();

    /**
     * Helper object used to find specifically named attributes within a
     * table (looking on tables, table sections and table rows; table cells
     * will also be examined if the cell inclusion flag is set true when the
     * search is executed).
     * This can be static as no state data is held.
     */
    private final AttributeFinder attributeFinder =
        new AttributeFinder();

    /**
     * Initializes the new instance. Protected to enforce the singleton
     * pattern.
     */
    protected TransTableHelper() {
    }

    /**
     * Returns the singleton instance of this class.
     *
     * @return the singleton instance of this class
     */
    public static TransTableHelper getInstance() {
        return instance;
    }

    /**
     * Helper method that returns true if the value is null or "0".
     *
     * @param value the string to test
     * @return true if the value is null or "0"
     */
    public boolean isZero(String value) {
        return (value == null) || "0".equals(value);
    }

    /**
     * Helper method that returns true if the value is a StyleLength of zero
     * pixels, a StylePercentage of 0% or null.
     *
     * @param value the StyleValue to test
     * @return true if the value is null, 0px or 0%
     */
    public boolean isStyleValueZero(StyleValue value) {
        return (value == null) ||
                value.equals(TransTable.zeroPixels) ||
                value.equals(TransTable.zeroPercent);
    }

    /**
     * Helper method that returns true if the table or one of its elements has
     * an id attribute (only covers the table, (optional) table sections, rows
     * and cells - the latter only if requested).
     *
     * @param table        the table to be examined
     * @param factory      the TransFactory used to obtain the ElementHelper
     *                     needed by the visitors used
     * @param includeCells true if cells should be examined for id attributes
     * @return true if the table or one of its elements has an id attribute
     */
    public boolean tableHasId(Element table,
                              TransFactory factory,
                              boolean includeCells) {
        return tableHasAttribute(table, factory, "id", includeCells);
    }

    /**
     * Helper method that returns true if the table or one of its elements has
     * the specified attribute (only covers the table, (optional) table
     * sections, rows and cells - the latter only if requested).
     *
     * @param table        the table to be examined
     * @param factory      the TransFactory used to obtain the ElementHelper
     *                     needed by the visitors used
     * @param attribute    the name of the attribute to look for
     * @param includeCells true if cells should be examined for id attributes
     * @return true if the table or one of its elements has an id attribute
     */
    public boolean tableHasAttribute(Element table,
                                     TransFactory factory,
                                     String attribute,
                                     boolean includeCells) {
        return attributeFinder.findAttribute(
                includeCells, attribute, factory.getElementHelper(), table);
    }

    /**
     * Helper method that returns true if the table or one of its elements,
     * down to the rows or cells (if includeCells is true), contain elements
     * with the given element type (tag).
     *
     * @param table        the table to be examined
     * @param factory      the TransFactory used to obtain the ElementHelper
     *                     needed by the visitors used
     * @param tag          the type of element to be searched for
     * @param includeCells whether the search should include the table's cells
     *                     as well as rows
     * @return true if at least one element with the specified tag can be
     *         found
     */
    public boolean tableContains(Element table,
                                 TransFactory factory,
                                 String tag,
                                 boolean includeCells) {
        return elementFinder.findElement(
                includeCells, tag, factory.getElementHelper(), table);
    }

    /**
     * Check if the border edge is significant.
     *
     * @param styleProperty The property for the border style.
     * @param widthProperty The property for the border width.
     *
     * @return True if the border has a non 0 width and a non none style.
     */
    private boolean isBorderEdgeSignificant(PropertyValues values,
                                           StyleProperty styleProperty,
                                           StyleProperty widthProperty) {
        StyleValue style = values.getComputedValue(styleProperty);
        StyleValue width = values.getComputedValue(widthProperty);

        return style != BorderStyleKeywords.NONE && !isStyleValueZero(width);
    }

    public boolean isBorderInsignificant(PropertyValues values) {
        return !isBorderSignificant(values);
    }

    public boolean isBorderSignificant(PropertyValues values) {
        boolean result = false;
        if (values != null) {
            result = isBorderEdgeSignificant(values,
                    StylePropertyDetails.BORDER_TOP_STYLE,
                    StylePropertyDetails.BORDER_TOP_WIDTH) ||

                    isBorderEdgeSignificant(values,
                    StylePropertyDetails.BORDER_RIGHT_STYLE,
                    StylePropertyDetails.BORDER_RIGHT_WIDTH) ||

                    isBorderEdgeSignificant(values,
                    StylePropertyDetails.BORDER_BOTTOM_STYLE,
                    StylePropertyDetails.BORDER_BOTTOM_WIDTH) ||

                    isBorderEdgeSignificant(values,
                    StylePropertyDetails.BORDER_LEFT_STYLE,
                    StylePropertyDetails.BORDER_LEFT_WIDTH);
        }
        return result;
    }

    public boolean isTableWidthSignificant(PropertyValues values) {
        StyleValue width = values.getComputedValue(StylePropertyDetails.WIDTH);
        return width != null && width != WidthKeywords.AUTO &&
                !width.equals(HUNDRED_PERCENT);
    }

    public boolean isCellWidthSignificant(PropertyValues values) {
        StyleValue width = values.getComputedValue(StylePropertyDetails.WIDTH);
        return width != null && width != WidthKeywords.AUTO &&
                !width.equals(ZERO_PERCENT) && !width.equals(ZERO_PIXELS);
    }

    public boolean isCellHeightSignificant(PropertyValues values) {
        StyleValue height = values.getComputedValue(StylePropertyDetails.HEIGHT);
        return height != null && height != HeightKeywords.AUTO &&
                !height.equals(ZERO_PERCENT) && !height.equals(ZERO_PIXELS);
    }

    /**
     * Helper method that returns true if the table and the containing table
     * either do not have the given attribute or both have the same value for
     * the given attribute.
     *
     * @param left the first table element to check
     * @param right the other table element to check
     * @param attribute the name of the attribute to be checked
     * @return true if the attributes match in both tables
     */
    public boolean match(Element left,
                         Element right,
                         String attribute) {
        String leftAttr = left.getAttributeValue(attribute);
        String rightAttr = right.getAttributeValue(attribute);

        return match(leftAttr, rightAttr);
    }

    /**
     * Helper method that returns true if the table and the containing table
     * either do not have a value set for the given property or both have the
     * same value.
     *
     * @param left      the first table's property values to check
     * @param right     the other table's property values to check
     * @param property the property to be checked
     * @return true if the property values match in both tables
     */
    private boolean match(PropertyValues left,
                         PropertyValues right,
                         StyleProperty property) {

        StyleValue leftValue =
                (left == null ? null : left.getComputedValue(property));
        StyleValue rightValue =
                (right == null ? null : right.getComputedValue(property));

        return match(leftValue, rightValue);
    }

    /**
     * Helper method that returns true if the two objects are considered to be
     * equal using #equals, or if they are actually the same object.
     *
     * @param left
     * @param right
     * @return true if the objects match
     */
    private boolean match(Object left, Object right) {
        return (left == right) || ((left != null) && left.equals(right));
    }

    /**
     * Helper method that returns true if the PropertyValues either do not
     * have values set for padding, or both have the same value.
     *
     * @param left      the first table's property values to check
     * @param right     the other table's property values to check
     * @return true if padding values match in both tables
     */
    public boolean paddingMatches(PropertyValues left,
                                  PropertyValues right) {
        return match(left, right, StylePropertyDetails.PADDING_TOP) &&
               match(left, right, StylePropertyDetails.PADDING_BOTTOM) &&
               match(left, right, StylePropertyDetails.PADDING_LEFT) &&
               match(left, right, StylePropertyDetails.PADDING_RIGHT);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 22-Aug-05	9223/1	emma	VBM:2005080403 Remove style class from within protocols and transformers

 22-Jul-05	8859/2	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 11-May-05	8167/1	allan	VBM:2005040701 Prevent iMode table optimization when td has style

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 ===========================================================================
*/


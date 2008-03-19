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
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Nov-00    Paul            Created.
 * 27-Jun-01    Paul            VBM:2001062704 - Renamed from GridFormat and
 *                              added visit method.
 * 09-Jul-01    Paul            VBM:2001062810 - Sorted out the background
 *                              image retrieval and changed the visit method
 *                              to return a boolean.
 * 23-Jul-01    Paul            VBM:2001070507 - Renamed setBorder method in
 *                              GridAttributes to setBorderWidth so that it
 *                              more closely matches its use.
 * 24-Jul-01    Paul            VBM:2001061904 - Added support for width unit
 *                              format attributes.
 * 30-Jul-01    Paul            VBM:2001071609 - Removed some unnecessary code.
 * 02-Aug-01    Allan           VBM:2001072604 - Replaced openComment() -
 *                              comment - closeComment() with
 *                              doComment(comment).
 * 10-Aug-01    Paul            VBM:2001072505 - Allow subclasses to modify
 *                              the created rows and columns, this is mainly
 *                              to allow SegmentGrid to change the default
 *                              setting for the height units attribute in
 *                              row from pixels to percent.
 * 14-Aug-01    Payal           VBM:2001080803 - Height property added to Grid,
 *                              added HEIGHT_ATTRIBUTE and getHeight method
 *                              to get the Height attribute.
 * 17-Aug-01    Allan           VBM:2001081612 - Added set units for height
 *                              and width to writeChildPreamble()
 * 14-Sep-01    Allan           VBM:2001091103 - Override setParent() to
 *                              set the hasGridChildren property in the parent.
 *                              Modified the write...Preamble and
 *                              write...Postamble methods to set the parent
 *                              format on the attributes in use.
 * 18-Sep-01    Allan           VBM:2001091103 - No need for hasGridChildren
 *                              so removing the reference to it
 * 01-Oct-01    Doug            VBM:2001092501 - now use the Format.java
 *                              method getBestBackgroundImage to calculate the
 *                              background image url. Removed the methods
 *                              getBackgroundImage and setBackroundImage. Added
 *                              Format.BACKGROUND_COMPONENT_TYPE_ATTRIBUTE
 *                              to the userAttributes and defaultAttributes
 *                              arrays
 * 29-Oct-01    Paul            VBM:2001102901 - Layout has been renamed
 *                              DeviceLayout and all methods relating to the
 *                              runtime generation of layouts have been
 *                              moved into protocols.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 28-Jan-02    Steve           VBM:2002011412 - Added name property so grids
 *                              can be accessed by replicas.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 26-Apr-02    Paul            VBM:2002042205 - Made sure that Grid
 *                              initialised width properly.
 * 21-May-02    Allan           VBM:2002052302 - Removed previous fix to
 *                              widths that have no value being set to 100%.
 * 09-Dec-02    Allan           VBM:2002120615 - Replaced String version of
 *                              Format type with FormatType where possible.
 * 03-Jan-03    Phil W-S        VBM:2002122404 - Add the OPTIMIZATION_LEVEL
 *                              attribute.
 * 20-Feb-03    Allan           VBM:2003021803 - Modified hasMoreElements() in
 *                              getSubComponentInfo() to check for null
 *                              rows/columns before trying to use them.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 27-Mar-03    Allan           VBM:2003030603 - Added clone().
 * 16-Apr-03    Geoff           VBM:2003041603 - Add FormatVisitorException to
 *                              declaration of visit() method.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.layouts;

import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.model.validation.ValidationContext;

/**
 * A format that provides a grid.
 *
 * @mock.generate base="AbstractGrid"
 */
public class Grid
        extends AbstractGrid implements DirectionAttribute {

    private static String[] userAttributes = new String[]{
        FormatConstants.BACKGROUND_COLOUR_ATTRIBUTE,
        FormatConstants.BACKGROUND_COMPONENT_ATTRIBUTE,
        FormatConstants.BACKGROUND_COMPONENT_TYPE_ATTRIBUTE,
        FormatConstants.BORDER_WIDTH_ATTRIBUTE,
        FormatConstants.CELL_PADDING_ATTRIBUTE,
        FormatConstants.CELL_SPACING_ATTRIBUTE,
        FormatConstants.DIRECTION_ATTRIBUTE,
        FormatConstants.VERTICAL_ALIGNMENT_ATTRIBUTE,
        FormatConstants.HORIZONTAL_ALIGNMENT_ATTRIBUTE,
        FormatConstants.WIDTH_ATTRIBUTE,
        FormatConstants.WIDTH_UNITS_ATTRIBUTE,
        FormatConstants.HEIGHT_ATTRIBUTE,
        FormatConstants.OPTIMIZATION_LEVEL_ATTRIBUTE,
        FormatConstants.NAME_ATTRIBUTE,
        FormatConstants.STYLE_CLASS
    };

    private static String[] persistentAttributes = userAttributes;

    private static String[] defaultAttributes = new String[]{
        FormatConstants.BACKGROUND_COMPONENT_TYPE_ATTRIBUTE,
        FormatConstants.BORDER_WIDTH_ATTRIBUTE,
        FormatConstants.CELL_PADDING_ATTRIBUTE,
        FormatConstants.CELL_SPACING_ATTRIBUTE,
        FormatConstants.VERTICAL_ALIGNMENT_ATTRIBUTE,
        FormatConstants.WIDTH_UNITS_ATTRIBUTE,
        FormatConstants.OPTIMIZATION_LEVEL_ATTRIBUTE
    };

    /**
     * Create a new Grid format.
     *
     * @param canvasLayout The Layout to which this format belongs
     */
    public Grid(CanvasLayout canvasLayout) {
        super(canvasLayout);
    }

    // Javadoc inherited from super class.
    public boolean visit(FormatVisitor visitor, Object object)
            throws FormatVisitorException {
        return visitor.visit(this, object);
    }

    // Javadoc inherited.
    public String getDirectionality() {
        String myDirectionality = (String)
                getAttribute(FormatConstants.DIRECTION_ATTRIBUTE);

        if (myDirectionality == null) {
            myDirectionality = getDirectionalityFromParent();

            // Store the value locally
            setDirectionality(myDirectionality);
        }

        return myDirectionality;
    }

    /**
     * Search up the hierachy for a layout element with a directionality
     * attribute. Stop when we find an element with a direction attribute
     * or we reach the root of the layout tree.
     *
     * @return "l2r", "r2l" or null from an ancesstor
     */
    private String getDirectionalityFromParent() {
        Format theParent = getParent();

        while (theParent != null &&
            !(theParent instanceof DirectionAttribute)) {
            theParent = theParent.getParent();
        }

        String directionality = null;

        if (theParent != null) {
             directionality = ((DirectionAttribute)theParent).getDirectionality();
        }

        return directionality;
    }

    // Javadoc inherited.
    public void setDirectionality(String direction) {
        setAttribute(FormatConstants.DIRECTION_ATTRIBUTE, direction);
    }

    // javadoc inherited
    public String[] getUserAttributes() {
        return userAttributes;
    }

    // javadoc inherited
    public String[] getPersistentAttributes() {
        return persistentAttributes;
    }

    // javadoc inherited
    public String[] getDefaultAttributes() {
        return defaultAttributes;
    }

    // javadoc inherited
    public FormatType getFormatType() {
        return FormatType.GRID;
    }

    // Javadoc inherited.
    public void validate(ValidationContext context) {

        String elementName = "gridFormat";

        validateGridAttributes(context);

        Step step = context.pushPropertyStep("columns");
        validateGridColumns(context, elementName);
        context.popStep(step);

        step = context.pushPropertyStep("rows");
        validateGridRows(context);
        context.popStep(step);

        validateChildren(context);
    }

    private void validateGridColumns(ValidationContext context,
            String elementName) {
        if (getColumns() > 0) {
            for (int i = 0; i < getColumns(); i++) {
                Column column = getColumn(i);

                Step step = context.pushIndexedStep(i);
                validateGridColumn(context, elementName, column);
                context.popStep(step);
            }
        }
    }

    private void validateGridColumn(ValidationContext context,
            String elementName, Column column) {

        validateWidthPixelsOrPercentAttributes(context, elementName, column);
        validateStyleClassAttribute(context, column);
    }

    private void validateGridRows(ValidationContext context) {
        if (getRows() > 0) {
            for (int i = 0; i < getRows(); i++) {
                Row row = getRow(i);

                Step step = context.pushIndexedStep(i);
                validateGridRow(context, row);
                context.popStep(step);
            }
        }
    }

    private void validateGridRow(ValidationContext context,
            Row row) {

        validateHeightPixelsOnlyAttribute(context, row);
        validateStyleClassAttribute(context, row);
    }

    private void validateGridAttributes(ValidationContext context) {
        String elementName = "gridFormat";
        validateOptionalName(context);
        validateGridDimensionAttributes(context);
        validateAllPaneAndGridAndIteratorAttributes(context, elementName);
        validateAdditionalNonDissectablePaneAndGridAttributes(
            context, elementName);
        validateStyleClassAttribute(context);
        validateDirectionAttribute(context);
    }

    private void validateGridDimensionAttributes(ValidationContext context) {

        Step step = context.pushPropertyStep("rows");
        String rows = getRowsString();
        if (rows != null) {
            if (!LayoutTypeValidator.isUnsigned(rows)) {
                addErrorDiagnostic(context, context.createMessage(
                        LayoutMessages.ROWS_ILLEGAL, rows));
            }
        } else {
            addErrorDiagnostic(context, context.createMessage(
                    LayoutMessages.ROWS_UNSPECIFIED));
        }
        context.popStep(step);

        step = context.pushPropertyStep("columns");
        String columns = getColumnsString();
        if (columns != null) {
            if (!LayoutTypeValidator.isUnsigned(columns)) {
                addErrorDiagnostic(context, context.createMessage(
                        LayoutMessages.COLUMNS_ILLEGAL, columns));
            }
        } else {
            addErrorDiagnostic(context, context.createMessage(
                    LayoutMessages.COLUMNS_UNSPECIFIED));
        }
        context.popStep(step);

    }

    private void validateDirectionAttribute(ValidationContext context) {
        Step step = context.pushPropertyStep("directionality");
        final String direction = getDirectionality();
        if (direction != null) {
            if (!LayoutTypeValidator.isDirectionType(direction)) {
                addErrorDiagnostic(context, context.createMessage(
                    LayoutMessages.DIRECTIONALITY_ILLEGAL,
                    direction));
            }
        }
        context.popStep(step);

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9652/1	gkoch	VBM:2005092204 Initial marshaller/unmarshaller for layoutFormat

 29-Sep-05	9590/3	schaloner	VBM:2005092204 Added width, height, and style accessor interfaces derived from CoreAttributes interface

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 29-Apr-05	7946/1	pduffin	VBM:2005042821 Moved code out of repository, in preparation for some device work

 18-Feb-05	7037/1	pcameron	VBM:2005021704 Width units default to percent if not present

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Oct-04	5816/1	byron	VBM:2004101318 Support style classes: Runtime XHTMLBasic

 08-Oct-04	5758/1	byron	VBM:2004100804 Support style classes on grids and spatial format iterators: Common

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/

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
 * $Header: /src/voyager/com/volantis/mcs/layouts/TemporalFormatIterator.java,v 1.7 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-OCt-02    Allan           VBM:2002101805 - An abstract Format for
 *                              TemporalFormatIteator formats.
 * 07-Nov-02    Allan           VBM:2002110506 - Removed the max. cell and
 *                              constants and attributes. Removed cell choices
 *                              contant.
 * 11-Nov-02    Sumit           VBM:2002101801 - Added visit method impl
 * 03-Dec-02    Adrian          VBM:2002111805 - removed the attributeGroupings
 *                              as these are no longer required to format
 *                              the AttributeViews.
 * 09-Dec-02    Allan           VBM:2002120615 - Replaced String version of
 *                              Format type with FormatType where possible.
 * 14-Jan-03    Allan           VBM:2002112703 - Added cell count to defaults
 *                              list. The default value is based on the
 *                              cells default value of variable.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add FormatVisitorException to
 *                              declaration of visit() method.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.layouts;

import com.volantis.mcs.layouts.iterators.IteratorSizeConstraint;
import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.model.validation.ValidationContext;

import java.util.ArrayList;
import java.util.Set;

/**
 * A Format for TemporalFormatIteator formats. FormatIterators only have
 * a single child of any Format type.
 *
 * @mock.generate base="FormatIterator"
 */
public class TemporalFormatIterator extends FormatIterator {

    /**
     * Constant representing the name the cells property. This property
     * defines whether the number of cells are fixed or variable.
     */
    public static String TEMPORAL_ITERATOR_CELLS = "TemporalIteratorCells";

    /**
     * Constant representing the name of the property whose value is the
     * number of cells in this format.
     */
    public static String TEMPORAL_ITERATOR_CELL_COUNT =
        "TemporalIteratorCellCount";

    /**
     * Constant representing the indexing direction for temporal format
     * iterators that have 2 dimensions.
     */
    public static String TEMPORAL_ITERATOR_CLOCK_VALUES =
        "TemporalIteratorClockValues";

    /**
     * Attributes associated with this Format.
     */
    protected static String [] userAttributes;

    /**
     * Attributes that have default values (a.k.a defaultAttributes).
     */
    protected static String [] attributesWithDefaults;

    /**
     * Attributes that are saved to the repository.
     */
    protected static String [] persistentAttributes;

    /**
     * The default FormatProperties for this Format.
     */
    protected static FormatProperties defaults;

    /**
     * A constraint on the number of cells used within an instance of this
     * format.
     *
     * <strong>This is only available after activation.</strong>
     */
    private IteratorSizeConstraint maxCellConstraint;

    /**
     * Initialize the property lists.
     */
    static {
        ArrayList attributesList =
            new ArrayList(FormatIterator.attributesList);
        attributesList.add(TEMPORAL_ITERATOR_CLOCK_VALUES);
        attributesList.add(TEMPORAL_ITERATOR_CELLS);
        attributesList.add(TEMPORAL_ITERATOR_CELL_COUNT);

        ArrayList attributesListWithDefaults =
            new ArrayList(FormatIterator.attributesWithDefaultsList);
        attributesListWithDefaults.add(TEMPORAL_ITERATOR_CELLS);
        attributesListWithDefaults.add(TEMPORAL_ITERATOR_CELL_COUNT);

        userAttributes = new String[attributesList.size()];

        userAttributes = (String []) attributesList.toArray(userAttributes);

        attributesWithDefaults = new String[attributesListWithDefaults.size()];

        attributesWithDefaults = (String [])
            attributesListWithDefaults.toArray(attributesWithDefaults);

        persistentAttributes = userAttributes;

        defaults = new FormatProperties();
  }

    /**
     * Construct a new TemporalFormatIterator for the specified Layout.
     * @param canvasLayout The Layout associated with this Format.
     */
    public TemporalFormatIterator(CanvasLayout canvasLayout) {
        super(canvasLayout, defaults);
    }

    /**
     * Retrieve the format type
     * @return The type of this format.
     */
    public FormatType getFormatType() {
        return FormatType.TEMPORAL_FORMAT_ITERATOR;
    }

    /**
     * Retrieve the set of attributes which can be changed by the user.
     * @return An array of attributes names.
     */
    public String [] getUserAttributes() {
        return userAttributes;
    }

    /**
     * Retrieve the set of attributes which are persistent.
     * @return An array of attributes names.
     */
    public String [] getPersistentAttributes() {
        return persistentAttributes;
    }

    /**
     * Retrieve the set of attributes which have sensible defaults.
     * @return An array of attributes names.
     */
    public String[] getDefaultAttributes() {
        return attributesWithDefaults;
    }

    public boolean visit(FormatVisitor visitor, Object object)
            throws FormatVisitorException {
        return visitor.visit(this,object);
    }

    /**
     * <strong>This is only available after activation.</strong>
     */
    public IteratorSizeConstraint getMaxCellConstraint() {
        if (maxCellConstraint == null) {
            throw new IllegalStateException("Layout has not been activated");
        }
        return maxCellConstraint;
    }

    /**
     * <strong>This should only be used by activation.</strong>
     */
    public void setMaxCellConstraint(IteratorSizeConstraint maxCellConstraint) {
        this.maxCellConstraint = maxCellConstraint;
    }

    /**
     * Sets whether the effective layout created by the temporal format
     * iterator has a fixed or variable number of cells.
     * Possible values are: "fixed" and "variable"
     *
     * @param cells the nature of the number of cells
     */
    public void setCells(final String cells) {
        setAttribute(TEMPORAL_ITERATOR_CELLS, cells);
    }

    /**
     * Returns whether the effective layout created by the temporal format
     * iterator has a fixed or variable number of cells.
     * @return the nature of the number of cells
     */
    public String getCells() {
        return (String) getAttribute(TEMPORAL_ITERATOR_CELLS);
    }

    /**
     * Sets cell count.
     * @param cellCount the new cell count
     */
    public void setCellCount(final int cellCount) {
        // Convert from int to String since internal form must always be String
        String value = String.valueOf(cellCount);
        setCellCount(value);
    }

    public void setCellCount(final String cellCount) {
        setAttribute(TEMPORAL_ITERATOR_CELL_COUNT, cellCount);
    }

    /**
     * Returns cell count.
     * @return cell count
     */
    public int getCellCount() {
        // Convert from String to int since internal form must always be String
        String value = getCellCountString();
        return Integer.parseInt(value);
    }

    public String getCellCountString() {
        return (String) getAttribute(TEMPORAL_ITERATOR_CELL_COUNT);
    }


    /**
     * Sets the durations for each cell of the temporal format iterator.
     * @param clockValues the new clock values
     */
    public void setClockValues(String clockValues) {
        setAttribute(TEMPORAL_ITERATOR_CLOCK_VALUES, clockValues);
    }

    /**
     * Sets the durations for each cell of the temporal format iterator.
     * @return the new clock values
     */
    public String getClockValues() {
        return (String) getAttribute(TEMPORAL_ITERATOR_CLOCK_VALUES);
    }

    // Javadoc inherited.
    public void validate(ValidationContext context) {

        validateTemporalFormatIteratorAttributes(context);

        validateChildren(context);

    }

    private void validateTemporalFormatIteratorAttributes(
            final ValidationContext context) {

        String element = "temporalFormatIterator";

        validateFormatIteratorAttributes(context, element);

        Step step = context.pushPropertyStep("clockValues");
        // Clock values require conversion. So we convert the old value back to
        // the new form before validating it.
        String oldClockValues = getClockValues();
        if (oldClockValues != null) {
            String clockValues = LayoutTypeValidator.newValue(element, "clockValues",
                    oldClockValues);
            if (!LayoutTypeValidator.isClockValueTypeList(clockValues)) {
                addErrorDiagnostic(context, context.createMessage(
                        LayoutMessages.CLOCK_VALUES_ILLEGAL,
                        clockValues));
            }
        } else {
            addErrorDiagnostic(context, context.createMessage(
                    LayoutMessages.CLOCK_VALUES_UNSPECIFIED));
        }
        context.popStep(step);

        step = context.pushPropertyStep("cells");
        final String cells = getCells();
        if (cells != null) {
            Set keywords = LayoutTypeValidator.getOldKeywords(element, "cells",
                    new String[] {"fixed", "variable"});
            if (!keywords.contains(cells)) {
                addErrorDiagnostic(context, context.createMessage(
                        LayoutMessages.CELLS_ILLEGAL, cells));
            }
        } else {
            addErrorDiagnostic(context, context.createMessage(
                    LayoutMessages.CELLS_UNSPECIFIED));
        }
        context.popStep(step);

        step = context.pushPropertyStep("cellCount");
        final String cellCount = getCellCountString();
        if (cellCount != null) {
            if (!LayoutTypeValidator.isUnsigned(cellCount)) {
                addErrorDiagnostic(context, context.createMessage(
                        LayoutMessages.CELL_COUNT_ILLEGAL, cellCount));
            }
        } else {
            addErrorDiagnostic(context, context.createMessage(
                    LayoutMessages.CELL_COUNT_UNSPECIFIED));
        }
        context.popStep(step);
    }

    // javadoc inherited
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        if (!super.equals(other)) return false;

        final TemporalFormatIterator iterator = (TemporalFormatIterator) other;

        return maxCellConstraint == null ?
            iterator.maxCellConstraint == null :
            maxCellConstraint.equals(iterator.maxCellConstraint);

    }

    // javadoc inherited
    public int hashCode() {
        return super.hashCode() * 31 +
            (maxCellConstraint != null ? maxCellConstraint.hashCode() : 0);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Oct-05	9986/1	geoff	VBM:2005102512 MCS35: Investigate and fix any JDBC repository import/export problems

 30-Sep-05	9652/1	gkoch	VBM:2005092204 Initial marshaller/unmarshaller for layoutFormat

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 ===========================================================================
*/

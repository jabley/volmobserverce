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
 * $Header: /src/voyager/com/volantis/mcs/layouts/SpatialFormatIterator.java,v 1.8 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Oct-02    Allan           VBM:2002101805 - An abstract Format for
 *                              SpatialFormatIteator formats.
 * 07-Nov-02    Allan           VBM:2002110506 - Removed the max. row and
 *                              column count constants and attributes. Removed
 *                              rowcolumn choices constant.
 * 11-Nov-02    Sumit           VBM:2002101801 - Added visit method impl
 * 25-Nov-02    Payal           VBM:2002111804 - Removed constant for
 *                              SPATIAL_ITERATOR_2D_DIRECTION_CHOICES and moved
 *                              it into FormatPropertiesDialog.
 * 03-Dec-02    Adrian          VBM:2002111805 - removed the attributeGroupings
 *                              as these are no longer required to format
 *                              the AttributeViews.
 * 09-Dec-02    Allan           VBM:2002120615 - Replaced String version of
 *                              Format type with FormatType where possible.
 * 14-Jan-03    Allan           VBM:2002112703 - Added row and column counts
 *                              to the list of defaults. The default is based
 *                              on the rows/columns default of variable.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add FormatVisitorException to
 *                              declaration of visit() method.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.layouts;

import com.volantis.mcs.layouts.spatial.CoordinateConverterChooser;
import com.volantis.mcs.layouts.spatial.EndlessStringArray;
import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.model.validation.ValidationContext;

import java.util.ArrayList;
import java.util.Set;

/**
 * A Format for SpatialFormatIteator formats. FormatIterators only have
 * a single child of any Format type.
 *
 * @mock.generate base="FormatIterator"
 */
public class SpatialFormatIterator extends FormatIterator
    implements StyleAttributes, OptimizationLevelAttribute,
               DirectionAttribute {

    /**
     * Constant representing the name the rows property. This property
     * defines whether the number of rows are fixed or variable.
     */
    public static String SPATIAL_ITERATOR_ROWS = "SpatialIteratorRows";

    /**
     * Constant representing the name of the property whose value is the
     * number of rows in this format.
     */
    public static String SPATIAL_ITERATOR_ROW_COUNT =
        "SpatialIteratorRowCount";

    /**
     * Constant representing the name the columns property. This property
     * defines whether the number of columns are fixed or variable.
     */
    public static String SPATIAL_ITERATOR_COLUMNS = "SpatialIteratorColumns";

    /**
     * Constant representing the name of the property whose value is the
     * number of columns in this format.
     */
    public static String SPATIAL_ITERATOR_COLUMN_COUNT =
        "SpatialIteratorColumnCount";

    /**
     * Constant representing the name of the property that determines whether
     * grid-based content of the spatial iterator should be aligned.
     */
    public static String SPATIAL_ITERATOR_ALIGN_CONTENT =
            "AlignContent";

    /**
     * Constant representing the indexing direction for spatial format
     * iterators that have 2 dimensions.
     */
    public static String SPATIAL_ITERATOR_2D_INDEXING_DIRECTION =
        "SpatialIterator2DIndexingDir";

    /**
     * The style class constant for Spatial Format iterator rows.
     */
    public static final String ROW_STYLE_CLASSES = "RowStyleClasses";

    /**
     * The style class constant for Spatial Format iterator columns.
     */
    public static final String COLUMN_STYLE_CLASSES = "ColumnStyleClasses";


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
     * Object responsible for creating objects to convert coordinates to and
     * from instance positions for this iterator.
     *
     * <strong>This is only available after activation.</strong>
     */
    private CoordinateConverterChooser coordinateConverterChooser;

    /**
     * An endless array of the row style classes.
     *
     * <strong>This is only available after activation.</strong>
     */
    private EndlessStringArray rowStyleClasses;

    /**
     * An endless array of the column style classes.
     *
     * <strong>This is only available after activation.</strong>
     */
    private EndlessStringArray columnStyleClasses;

    /**
     * Initialize the property lists.
     */
    static {
        ArrayList attributesList =
            new ArrayList(FormatIterator.attributesList);
        attributesList.add(SPATIAL_ITERATOR_2D_INDEXING_DIRECTION);
        attributesList.add(SPATIAL_ITERATOR_ROWS);
        attributesList.add(SPATIAL_ITERATOR_ROW_COUNT);
        attributesList.add(SPATIAL_ITERATOR_COLUMNS);
        attributesList.add(SPATIAL_ITERATOR_COLUMN_COUNT);
        attributesList.add(FormatConstants.OPTIMIZATION_LEVEL_ATTRIBUTE);
        attributesList.add(SPATIAL_ITERATOR_ALIGN_CONTENT);
        attributesList.add(FormatConstants.STYLE_CLASS);
        attributesList.add(ROW_STYLE_CLASSES);
        attributesList.add(COLUMN_STYLE_CLASSES);
        attributesList.add(FormatConstants.DIRECTION_ATTRIBUTE);

        ArrayList attributesListWithDefaults =
            new ArrayList(FormatIterator.attributesWithDefaultsList);
        attributesListWithDefaults.add(SPATIAL_ITERATOR_ROWS);
        attributesListWithDefaults.add(SPATIAL_ITERATOR_COLUMNS);
        attributesListWithDefaults.add(SPATIAL_ITERATOR_ROW_COUNT);
        attributesListWithDefaults.add(SPATIAL_ITERATOR_COLUMN_COUNT);
        attributesListWithDefaults.add(SPATIAL_ITERATOR_2D_INDEXING_DIRECTION);

        userAttributes = new String[attributesList.size()];

        userAttributes = (String []) attributesList.toArray(userAttributes);

        attributesWithDefaults = new String[attributesListWithDefaults.size()];

        attributesWithDefaults = (String [])
            attributesListWithDefaults.toArray(attributesWithDefaults);

        persistentAttributes = userAttributes;

        defaults = new FormatProperties();
        defaults.setAttribute(SPATIAL_ITERATOR_2D_INDEXING_DIRECTION,
                              "AcrossDown");
    }

    /**
     * Construct a new SpatialFormatIterator for the specified Layout.
     * @param canvasLayout The Layout associated with this Format.
     */
    public SpatialFormatIterator(CanvasLayout canvasLayout) {
        super(canvasLayout, defaults);
    }

    /**
     * Retrieve the format type
     * @return The type of this format.
     */
    public FormatType getFormatType() {
        return FormatType.SPATIAL_FORMAT_ITERATOR;
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
    public String [] getDefaultAttributes() {
        return attributesWithDefaults;
    }

    public boolean visit(FormatVisitor visitor, Object object)
            throws FormatVisitorException {
        return visitor.visit(this,object);
    }

    /**
     * Return the style class associated with this <code>SpatialFormatIterator</code>.
     *
     * @return the style class associated with this <code>SpatialFormatIterator</code>.
     */
    public String getStyleClass() {
        return (String) getAttribute(FormatConstants.STYLE_CLASS);
    }

    /**
     * Sets the style class associated with this <code>SpatialFormatIterator</code>.
     *
     * @param styleClass the style class associated with this
     * <code>SpatialFormatIterator</code>.
     */
    public void setStyleClass(final String styleClass) {
        setAttribute(FormatConstants.STYLE_CLASS, styleClass);
    }

    /**
     * Returns true if grid content of this spatial iterator is to be aligned,
     * false otherwise.
     *
     * @return True if content is aligned
     */
    public boolean isContentAligned() {
        String attributeValue = getAlignContent();
        return Boolean.valueOf(attributeValue).booleanValue();
    }

    public String getAlignContent() {
        return (String) getAttribute(SPATIAL_ITERATOR_ALIGN_CONTENT);
    }

    /**
     * <strong>This should only be used by activation.</strong>
     */
    public void setCoordinateConverterChooser(CoordinateConverterChooser chooser) {
        this.coordinateConverterChooser = chooser;
    }

    /**
     * <strong>This is only available after activation.</strong>
     */
    public CoordinateConverterChooser getCoordinateConverterChooser() {
        if (coordinateConverterChooser == null) {
            throw new IllegalStateException("Layout has not been activated");
        }
        return coordinateConverterChooser;
    }

    /**
     * <strong>This should only be used by activation.</strong>
     */
    public void setRowStyleClasses(EndlessStringArray classes) {
        rowStyleClasses = classes;
    }

    /**
     * <strong>This is only available after activation.</strong>
     */
    public EndlessStringArray getRowStyleClasses() {
        if (rowStyleClasses == null) {
            throw new IllegalStateException("Layout has not been activated");
        }
        return rowStyleClasses;
    }

    /**
     * Sets the row style classes attribute
     * @param rowStyleClasses - the row style classes
     */
    public void setRowStyleClassesAttribute(final String rowStyleClasses) {
        setAttribute(ROW_STYLE_CLASSES, rowStyleClasses);
    }

    /**
     * Returns the row style classes attribute
     * @return the row style classes
     */
    public String getRowStyleClassesAttribute() {
        return (String) getAttribute(ROW_STYLE_CLASSES);
    }

    /**
     * Sets the column style classes attribute
     * @param columnStyleClasses - the column style classes
     */
    public void setColumnStyleClassesAttribute(final String columnStyleClasses) {
        setAttribute(COLUMN_STYLE_CLASSES, columnStyleClasses);
    }

    /**
     * Returns the column style classes attribute
     * @return the column style classes
     */
    public String getColumnStyleClassesAttribute() {
        return (String) getAttribute(COLUMN_STYLE_CLASSES);
    }

    /**
     * <strong>This should only be used by activation.</strong>
     */
    public void setColumnStyleClasses(EndlessStringArray classes) {
        columnStyleClasses = classes;
    }

    /**
     * <strong>This is only available after activation.</strong>
     */
    public EndlessStringArray getColumnStyleClasses() {
        if (columnStyleClasses == null) {
            throw new IllegalStateException("Layout has not been activated");
        }
        return columnStyleClasses;
    }

    /**
     * Sets the indexing direction. Possible values are: "across-down" and
     * "down-across".
     *
     * @param indexingDirection the indexing direction
     */
    public void setIndexingDirection(final String indexingDirection) {
        setAttribute(SPATIAL_ITERATOR_2D_INDEXING_DIRECTION, indexingDirection);
    }

    /**
     * Returns the indexing direction. Possible values are: "across-down" and
     * "down-across".
     *
     * @return the indexing direction
     */
    public String getIndexingDirection() {
        return (String) getAttribute(SPATIAL_ITERATOR_2D_INDEXING_DIRECTION);
    }

    /**
     * Specifies whether the effective layout created by the spatial format
     * iterator has a fixed or variable number of rows.
     * Possible values are: "fixed" and "variable"
     *
     * @param rows the nature of the number of rows
     */
    public void setRowsFlexibility(final String rows) {
        setAttribute(SPATIAL_ITERATOR_ROWS, rows);
    }

    /**
     * Returns if the effective layout created by the spatial format
     * iterator has a fixed or variable number of rows.
     * @return the nature of the number of rows
     */
    public String getRowsFlexibility() {
        return (String) getAttribute(SPATIAL_ITERATOR_ROWS);
    }

    /**
     * Specifies the number of rows in the effective layout created by the
     * spatial format iterator.
     * @param rowCount the number of rows
     */
    public void setRows(final String rowCount) {
        setAttribute(SPATIAL_ITERATOR_ROW_COUNT, rowCount);
    }

    /**
     * Return the number of rows
     * @return the number of rows
     */
    public int getRows() {
        return Integer.parseInt(
                getRowCount());
    }

    public String getRowCount() {
        return (String) getAttribute(SPATIAL_ITERATOR_ROW_COUNT);
    }

    /**
     * Specifies whether the effective layout created by the spatial format
     * iterator has a fixed or variable number of columns.
     * Possible values are: "fixed" and "variable"
     *
     * @param columns the nature of the number of columns
     */
    public void setColumnsFlexibility(final String columns) {
        setAttribute(SPATIAL_ITERATOR_COLUMNS, columns);
    }

    /**
     * Returns if the effective layout created by the spatial format
     * iterator has a fixed or variable number of columns.
     * @return the nature of the number of columns
     */
    public String getColumnsFlexibility() {
        return (String) getAttribute(SPATIAL_ITERATOR_COLUMNS);
    }

    public void setColumns(final String columnCount) {
        setAttribute(SPATIAL_ITERATOR_COLUMN_COUNT, columnCount);
    }

    // javadoc inherited
    public void setColumns(final int columnCount) {
        setColumns(Integer.toString(columnCount));
    }

    // javadoc inherited
    public int getColumns() {
        return
            Integer.parseInt(getColumnCount());
    }

    public String getColumnCount() {
        return (String) getAttribute(SPATIAL_ITERATOR_COLUMN_COUNT);
    }

    /**
     * Sets whether content should be aligned or not.
     * @param alignContent the new alignment
     */
    public void setAlignContent(final boolean alignContent) {
        setAlignContent(Boolean.toString(alignContent));
    }

    /**
     * Sets whether content should be aligned or not.
     * @param alignContent the new alignment
     */
    public void setAlignContent(final String alignContent) {
        setAttribute(SPATIAL_ITERATOR_ALIGN_CONTENT,
                     alignContent);
    }

    // javadoc inherited
    public String getOptimizationLevel() {
        return (String) getAttribute(FormatConstants.OPTIMIZATION_LEVEL_ATTRIBUTE);
    }

    // javadoc inherited
    public void setOptimizationLevel(final String optimizationLevel) {
        setAttribute(FormatConstants.OPTIMIZATION_LEVEL_ATTRIBUTE,
            optimizationLevel);
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

        // Default left-to-right
        String directionality = null;

        if (theParent != null) {
             directionality = ((DirectionAttribute)theParent).getDirectionality();
        }

        return directionality;
    }

    // Javadoc inherited.
    public void setDirectionality(String direction) {
        setAttribute(FormatConstants.DIRECTION_ATTRIBUTE,
                     direction);
    }

    // Javadoc inherited.
    public void validate(ValidationContext context) {

        validateSpatialFormatIteratorAttributes(context);

        validateChildren(context);

    }

    private void validateSpatialFormatIteratorAttributes(
            final ValidationContext context) {

        String element = "spatialFormatIterator";

        validateFormatIteratorAttributes(context, element);
        validateOptimizationLevelAttribute(context, element);
        validateStyleClassAttribute(context);

        Step step = context.pushPropertyStep("indexingDirection");
        final String indexingDirection = getIndexingDirection();
        if (indexingDirection != null) {
            Set keywords = LayoutTypeValidator.getOldKeywords(element, "indexingDirection",
                    new String[] {"across-down", "down-across"});
            if (!keywords.contains(indexingDirection)) {
                addErrorDiagnostic(context, context.createMessage(
                        LayoutMessages.INDEXING_DIRECTION_ILLEGAL,
                        indexingDirection));
            }
        }
        context.popStep(step);

        step = context.pushPropertyStep("rows");
        final String rows = getRowsFlexibility();
        if (rows != null) {
            Set keywords = LayoutTypeValidator.getOldKeywords(element, "rows",
                    new String[] {"fixed", "variable"});
            if (!keywords.contains(rows)) {
                addErrorDiagnostic(context, context.createMessage(
                        LayoutMessages.ROWS_ILLEGAL, rows));
            }
        } else {
            addErrorDiagnostic(context, context.createMessage(
                    LayoutMessages.ROWS_UNSPECIFIED));
        }
        context.popStep(step);

        step = context.pushPropertyStep("rowCount");
        final String rowCount = getRowCount();
        if (rowCount != null) {
            if (!LayoutTypeValidator.isUnsigned(rowCount)) {
                addErrorDiagnostic(context, context.createMessage(
                        LayoutMessages.ROW_COUNT_ILLEGAL, rowCount));
            }
        } else {
            addErrorDiagnostic(context, context.createMessage(
                    LayoutMessages.ROW_COUNT_UNSPECIFIED));
        }
        context.popStep(step);

        step = context.pushPropertyStep("rowStyleClasses");
        final String rowStyleClasses = getRowStyleClassesAttribute();
        if (rowStyleClasses != null) {
            if (!LayoutTypeValidator.isThemeClassNameList(rowStyleClasses)) {
                addErrorDiagnostic(context, context.createMessage(
                        LayoutMessages.ROW_STYLE_CLASSES_ILLEGAL,
                        rowStyleClasses));
            }
        }
        context.popStep(step);

        step = context.pushPropertyStep("columns");
        final String columns = getColumnsFlexibility();
        if (columns != null) {
            Set keywords = LayoutTypeValidator.getOldKeywords(element, "columns",
                    new String[] {"fixed", "variable"});
            if (!keywords.contains(columns)) {
                addErrorDiagnostic(context, context.createMessage(
                        LayoutMessages.COLUMNS_ILLEGAL, columns));
            }
        } else {
            addErrorDiagnostic(context, context.createMessage(
                    LayoutMessages.COLUMNS_UNSPECIFIED));
        }
        context.popStep(step);

        step = context.pushPropertyStep("columnCount");
        final String columnCount = getColumnCount();
        if (columnCount != null) {
            if (!LayoutTypeValidator.isUnsigned(columnCount)) {
                addErrorDiagnostic(context, context.createMessage(
                        LayoutMessages.COLUMN_COUNT_ILLEGAL, columnCount));
            }
        } else {
            addErrorDiagnostic(context, context.createMessage(
                    LayoutMessages.COLUMN_COUNT_UNSPECIFIED));
        }
        context.popStep(step);

        step = context.pushPropertyStep("columnStyleClasses");
        final String columnStyleClasses = getColumnStyleClassesAttribute();
        if (columnStyleClasses != null) {
            if (!LayoutTypeValidator.isThemeClassNameList(columnStyleClasses)) {
                addErrorDiagnostic(context, context.createMessage(
                        LayoutMessages.COLUMN_STYLE_CLASSES_ILLEGAL,
                        columnStyleClasses));
            }
        }
        context.popStep(step);

        step = context.pushPropertyStep("alignContent");
        final String alignContent = getAlignContent();
        if (alignContent != null) {
            if (!LayoutTypeValidator.isBoolean(alignContent)) {
                addErrorDiagnostic(context, context.createMessage(
                        LayoutMessages.ALIGN_CONTENT_ILLEGAL,
                        alignContent));
            }
        }
        context.popStep(step);

        validateDirectionAttribute(context);
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

 02-Oct-05	9652/3	gkoch	VBM:2005092204 Tests for layoutFormat marshaller/unmarshaller

 30-Sep-05	9652/1	gkoch	VBM:2005092204 Initial marshaller/unmarshaller for layoutFormat

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 20-Dec-04	6402/1	philws	Promoting rebuilt jar files

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Dec-04	6387/3	adrianj	VBM:2004120205 Added content align attribute to Spatial Iterator format (rework)

 06-Dec-04	6387/1	adrianj	VBM:2004120205 Added content align attribute to Spatial Iterator format

 19-Oct-04	5816/1	byron	VBM:2004101318 Support style classes: Runtime XHTMLBasic

 08-Oct-04	5758/1	byron	VBM:2004100804 Support style classes on grids and spatial format iterators: Common

 14-Jul-04	4871/1	adrianj	VBM:2003112107 Added optimisation attribute to spatial format iterators

 ===========================================================================
*/

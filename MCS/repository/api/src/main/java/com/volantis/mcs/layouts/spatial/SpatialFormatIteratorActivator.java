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

package com.volantis.mcs.layouts.spatial;

import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.SpatialFormatIterator;
import com.volantis.mcs.layouts.activator.FormatActivator;
import com.volantis.mcs.layouts.iterators.IteratorConstraintFactory;
import com.volantis.mcs.layouts.iterators.IteratorSizeConstraint;

/**
 * Activates the spatial format iterator.
 *
 * <p>Activation is the process whereby objects are initialised for use within
 * the runtime. Although this is not part of the runtime this is doing part of
 * its job and in fact should be moved into the runtime when runtime formats
 * are created.</p>
 *
 * @todo This does a number of different orthogonal jobs and these should be split into separate classes to make it possible to test them separately.
 */
public class SpatialFormatIteratorActivator
        implements FormatActivator {

    /**
     * The object to use to create constraints.
     */
    private final IteratorConstraintFactory constraintFactory;

    /**
     * The object to use to create {@link CoordinateConverter}s.
     */
    private final CoordinateChooserFactory coordinateChooserFactory;

    /**
     * The object to use to tokenize a space separate list into a
     * String array of tokens.
     */ 
    private final ListTokenizer listTokenizer;

    /**
     * Initialise.
     *
     * @param coordinateChooserFactory
     * @param constraintFactory
     * @param listTokenizer
     */
    public SpatialFormatIteratorActivator(
            CoordinateChooserFactory coordinateChooserFactory,
            IteratorConstraintFactory constraintFactory,
            ListTokenizer listTokenizer) {

        this.coordinateChooserFactory = coordinateChooserFactory;
        this.constraintFactory = constraintFactory;
        this.listTokenizer = listTokenizer;
    }

    // Javadoc inherited.
    public void activate(Format format) {

        SpatialFormatIterator spatial = (SpatialFormatIterator) format;
        setCoordinateConverterFactory(spatial);
        setRowStyleClasses(spatial);
        setColumnStyleClasses(spatial);
    }

    /**
     * Return a constraint on the maximum number of rows that can be rendered
     * by this format.
     *
     * @return A constraint on the maximum number of rows that can be rendered.
     */
    private IteratorSizeConstraint getMaxRowsConstraint(SpatialFormatIterator spatial) {
        int rows = spatial.getAttributeAsInt(
                SpatialFormatIterator.SPATIAL_ITERATOR_ROW_COUNT,
                "render-spatial-iterator-missing-rows");

        final boolean fixedRows = spatial.attributeHasValue(
                SpatialFormatIterator.SPATIAL_ITERATOR_ROWS,
                "fixed");

        return constraintFactory.createConstraint(rows, fixedRows);
    }

    /**
     * Return a constraint on the maximum number of columns that can be rendered
     * by this format.
     *
     * @return A constraint on the maximum number of columns that can be
     * rendered.
     */
    private IteratorSizeConstraint getMaxColsConstraint(SpatialFormatIterator spatial) {
        int columns = spatial.getAttributeAsInt(
                SpatialFormatIterator.SPATIAL_ITERATOR_COLUMN_COUNT,
                "render-spatial-iterator-missing-columns");

        final boolean fixedColumns = spatial.attributeHasValue(
                SpatialFormatIterator.SPATIAL_ITERATOR_COLUMNS,
                "fixed");

        return constraintFactory.createConstraint(columns, fixedColumns);
    }

    /**
     * Set the {@link CoordinateChooserFactory} property on the spatial format.
     *
     * @param spatial The spatial format being activated.
     */
    private void setCoordinateConverterFactory(SpatialFormatIterator spatial) {

        IteratorSizeConstraint rowsConstraint = getMaxRowsConstraint(spatial);
        IteratorSizeConstraint colsConstraint = getMaxColsConstraint(spatial);

        final boolean acrossDown = spatial.attributeHasValue(
                SpatialFormatIterator.SPATIAL_ITERATOR_2D_INDEXING_DIRECTION,
                "AcrossDown");

        // If we have a fixed number of rows and fixed number of
        // columns we already know how many cells to process
        CoordinateConverterChooser chooser;
        if (acrossDown) {
            chooser = coordinateChooserFactory.createHorizontalConverterChooser(
                    colsConstraint, rowsConstraint);
        } else {
            chooser = coordinateChooserFactory.createVerticalConverterChooser(
                    colsConstraint, rowsConstraint);
        }

        spatial.setCoordinateConverterChooser(chooser);
    }

    /**
     * Set the rows style classes property on the spatial format.
     *
     * @param spatial The spatial format being activated.
     */
    private void setRowStyleClasses(SpatialFormatIterator spatial) {

        // Initialize the style class arrays for later use
        String classList = (String) spatial.getAttribute(
                SpatialFormatIterator.ROW_STYLE_CLASSES);

        EndlessStringArray classArray = createEndlessArray(classList);

        spatial.setRowStyleClasses(classArray);
    }

    /**
     * Set the columns style classes property on the spatial format.
     *
     * @param spatial The spatial format being activated.
     */
    private void setColumnStyleClasses(SpatialFormatIterator spatial) {

        // Initialize the style class arrays for later use
        String classList = (String) spatial.getAttribute(
                SpatialFormatIterator.COLUMN_STYLE_CLASSES);

        EndlessStringArray classArray = createEndlessArray(classList);

        spatial.setColumnStyleClasses(classArray);
    }

    /**
     * Create an {@link EndlessStringArray} from a space separated list of
     * classes.
     *
     * @param classList The space separated list of classes.
     *
     * @return An {@link EndlessStringArray}.
     */
    private EndlessStringArray createEndlessArray(String classList) {
        String[] classes = listTokenizer.extractTokens(classList);
        EndlessStringArray classArray = new EndlessStringArray(classes);
        return classArray;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 ===========================================================================
*/

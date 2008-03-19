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

import com.volantis.mcs.layouts.iterators.IteratorSizeConstraint;

/**
 * Implementation of {@link CoordinateChooserFactory}.
 */
public class CoordinateChooserFactoryImpl
        implements CoordinateChooserFactory {

    // Javadoc inherited.
    public CoordinateConverterChooser createHorizontalConverterChooser(
            IteratorSizeConstraint columnsConstraint,
            IteratorSizeConstraint rowsConstraint) {

        // If both the column and row sizes are fixed then the resulting
        // chooser ignores the actual number of instances. Otherwise, it
        // uses the constraints to produce a converter whose coordinate
        // space is dependent on the number of instances.
        CoordinateConverterChooser chooser;
        if (columnsConstraint.isFixed() && rowsConstraint.isFixed()) {
            chooser = new FixedConverterChooser(
                columnsConstraint, rowsConstraint);
        } else {
            chooser = new VariableHorizontalConverterChooser(
                    columnsConstraint, rowsConstraint);
        }

        return chooser;
    }

    // Javadoc inherited.
    public CoordinateConverterChooser createVerticalConverterChooser(
            IteratorSizeConstraint columnsConstraint,
            IteratorSizeConstraint rowsConstraint) {


        // If both the column and row sizes are fixed then the resulting
        // chooser ignores the actual number of instances. Otherwise, it
        // uses the constraints to produce a converter whose coordinate
        // space is dependent on the number of instances.
        CoordinateConverterChooser chooser;
        if (columnsConstraint.isFixed() && rowsConstraint.isFixed()) {
            chooser = new FixedVerticalConverterChooser(
                columnsConstraint,rowsConstraint);
        } else {
            chooser = new VariableVerticalConverterChooser(
                    columnsConstraint, rowsConstraint);
        }

        return chooser;
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

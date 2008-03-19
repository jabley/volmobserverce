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
 * A {@link CoordinateConverterChooser} that always returns the same
 * {@link CoordinateConverter} irrespective of the number of instances it has
 * to support.
 */
public class FixedConverterChooser extends AbstractConverterChooser {

    public FixedConverterChooser(IteratorSizeConstraint columnsConstraint,
        IteratorSizeConstraint rowsConstraint) {
        super(columnsConstraint, rowsConstraint);
    }

    // Javadoc inherited.
    public CoordinateConverter chooseCoordinateConverter(int instanceCount, boolean reverseColumns) {
        int width = colsConstraint.getMaximumValue();
        int height = rowsConstraint.getMaximumValue();

        if (reverseColumns) {
            return new HorizontalCoordinateConverterRightToLeft(width,  height);
        } else {
            return new HorizontalCoordinateConverter(width, height);
        }
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

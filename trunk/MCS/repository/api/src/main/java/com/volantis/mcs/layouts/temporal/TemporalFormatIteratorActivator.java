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

package com.volantis.mcs.layouts.temporal;

import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.TemporalFormatIterator;
import com.volantis.mcs.layouts.activator.FormatActivator;
import com.volantis.mcs.layouts.iterators.IteratorConstraintFactory;
import com.volantis.mcs.layouts.iterators.IteratorSizeConstraint;

/**
 * Activates the temporal format iterator for use in the runtime.
 */
public class TemporalFormatIteratorActivator
        implements FormatActivator {

    /**
     * The object to use to create constraints.
     */
    private final IteratorConstraintFactory constraintFactory;

    /**
     * Initialise.
     *
     * @param constraintFactory
     */
    public TemporalFormatIteratorActivator(
            IteratorConstraintFactory constraintFactory) {
        this.constraintFactory = constraintFactory;
    }

    // Javadoc inherited.
    public void activate(Format format) {

        TemporalFormatIterator temporal = (TemporalFormatIterator) format;
        setMaxCellConstraint(temporal);
    }

    /**
     * Set the max cell constraint.
     *
     * @param temporal The {@link TemporalFormatIterator} to activate.
     */
    private void setMaxCellConstraint(TemporalFormatIterator temporal) {
        int count = temporal.getAttributeAsInt(
                TemporalFormatIterator.TEMPORAL_ITERATOR_CELL_COUNT,
                "render-temporal-iterator-missing-cells");

        boolean fixed = temporal.attributeHasValue(
                TemporalFormatIterator.TEMPORAL_ITERATOR_CELLS,
                "fixed");

        IteratorSizeConstraint constraint = constraintFactory.createConstraint(
                count, fixed);
        temporal.setMaxCellConstraint(constraint);
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

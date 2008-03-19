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

package com.volantis.mcs.layouts.activator;

import com.volantis.mcs.layouts.spatial.SpatialFormatIteratorActivator;
import com.volantis.mcs.layouts.spatial.CoordinateChooserFactoryImpl;
import com.volantis.mcs.layouts.spatial.ListTokenizer;
import com.volantis.mcs.layouts.spatial.CoordinateChooserFactory;
import com.volantis.mcs.layouts.spatial.CoordinateConverter;
import com.volantis.mcs.layouts.iterators.IteratorConstraintFactory;
import com.volantis.mcs.layouts.iterators.IteratorSizeConstraint;
import com.volantis.mcs.layouts.temporal.TemporalFormatIteratorActivator;

/**
 * Implementation of {@link FormatActivatorFactory}.
 */
public class FormatActivatorFactoryImpl
        implements FormatActivatorFactory {

    /**
     * Object for choosing the {@link CoordinateConverter}.
     */
    private static final CoordinateChooserFactory COORDINATE_CHOOSER_FACTORY
            = new CoordinateChooserFactoryImpl();

    /**
     * Object for creating {@link IteratorSizeConstraint}s
     */
    private static final IteratorConstraintFactory CONSTRAINT_FACTORY =
            new IteratorConstraintFactory();

    /**
     * Object for tokenizing a space separated list into an array of tokens.
     */
    private static final ListTokenizer LIST_TOKENIZER = new ListTokenizer();

    /**
     * The activator for spatial formats.
     */
    private static final FormatActivator SPATIAL_ITERATOR_ACTIVATOR =
            new SpatialFormatIteratorActivator(
                    COORDINATE_CHOOSER_FACTORY,
                    CONSTRAINT_FACTORY,
                    LIST_TOKENIZER);

    /**
     * The activator for temporal formats.
     */
    private static final FormatActivator TEMPORAL_ITERATOR_ACTIVATOR =
            new TemporalFormatIteratorActivator(CONSTRAINT_FACTORY);

    // Javadoc inherited.
    public FormatActivator createSpatialIteratorActivator() {
        return SPATIAL_ITERATOR_ACTIVATOR;
    }

    // Javadoc inherited.
    public FormatActivator createTemporalIteratorActivator() {
        return TEMPORAL_ITERATOR_ACTIVATOR;
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

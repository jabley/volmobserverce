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

import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.FormatVisitorAdapter;
import com.volantis.mcs.layouts.FormatVisitorException;
import com.volantis.mcs.layouts.SpatialFormatIterator;
import com.volantis.mcs.layouts.TemporalFormatIterator;
import com.volantis.mcs.repository.RepositoryException;

/**
 * A {@link FormatActivator} that will recurse over a tree of {@link Format}s
 * activating them all.
 */
public class RecursingFormatActivatorImpl
        extends FormatVisitorAdapter
        implements FormatActivator {

    /**
     * The activator for spatial formats.
     */
    private final FormatActivator spatialIteratorActivator;

    /**
     * The activator for temporal formats.
     */
    private final FormatActivator temporalIteratorActivator;

    /**
     * Initialise.
     *
     * @param factory The factory for creating {@link FormatActivator}s.
     */
    public RecursingFormatActivatorImpl(FormatActivatorFactory factory) {
        spatialIteratorActivator = factory.createSpatialIteratorActivator();
        temporalIteratorActivator = factory.createTemporalIteratorActivator();
    }

    // Javadoc inherited.
    public void activate(Format format) {

        // Return if there is nothing to do.
        if (format == null) {
            return;
        }
        
        format.visit(this, null);
    }

    // Javadoc inherited.
    public boolean visit(SpatialFormatIterator spatial, Object object)
            throws FormatVisitorException {

        activate(spatialIteratorActivator, spatial);

        return super.visit(spatial, object);
    }

    // Javadoc inherited.
    public boolean visit(TemporalFormatIterator temporal, Object object)
            throws FormatVisitorException {

        activate(temporalIteratorActivator, temporal);

        return super.visit(temporal, object);
    }

    /**
     * Invoke the activator on the format and wrap any exceptions.
     *
     * @param activator The activator.
     * @param format The format to be activated.
     *
     * @throws FormatVisitorException If there was a problem activating.
     */
    private void activate(final FormatActivator activator, Format format)
            throws FormatVisitorException {

        activator.activate(format);
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

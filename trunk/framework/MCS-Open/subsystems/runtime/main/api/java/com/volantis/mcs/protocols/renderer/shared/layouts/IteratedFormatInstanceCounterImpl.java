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

package com.volantis.mcs.protocols.renderer.shared.layouts;

import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.FormatVisitorAdapter;
import com.volantis.mcs.layouts.FormatVisitorException;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.layouts.Region;
import com.volantis.mcs.protocols.NDimensionalContainer;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.synergetics.UndeclaredThrowableException;

/**
 * This class visits each format in the layout in order to find the
 * maximum number of iterated format instances at the current format index.
 * <p>
 * This allows us to determine the number of rows and columns to render
 * when a format iterator is defined with a variable number of rows or
 * columns.
 */
public class IteratedFormatInstanceCounterImpl
        extends FormatVisitorAdapter
        implements IteratedFormatInstanceCounter {

    /**
     * The context within which this executes.
     */
    private final FormatRendererContext context;

    /**
     * The index of the instance whose contents are being counted.
     */
    private NDimensionalIndex index;

    /**
     * The calculated maximum number of instances for any iterated format
     * type at the format index specified on construction.
     */
    private int maxInstances;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param context the context within which this counter is being executed
     */
    public IteratedFormatInstanceCounterImpl(FormatRendererContext context) {
        this.context = context;
    }

    // Javadoc inherited.
    public int getMaxInstances(Format format, NDimensionalIndex index) {
        this.index = index;
        this.maxInstances = 0;
        try {
            format.visit(this, null);
        } catch (FormatVisitorException e) {
            // This should never occur.
            throw new UndeclaredThrowableException(e);
        }
        return maxInstances;
    }

    // javadoc inherited
    public boolean visit(Pane pane, Object object) {
        countInstances(pane);
        return false;
    }

    // javadoc inherited
    public boolean visit(Region region, Object object) {
        countInstances(region);
        return false;
    }

    /**
     * Counts the instances of a format at the current index and saves
     * this value if it is greater than the current value.
     *
     * @param format the format to count instances for
     */
    private void countInstances(Format format) {
        NDimensionalContainer container =
                context.getFormatInstancesContainer(format);
        int instances = container.getNumCellsInDimension(index);
        maxInstances = Math.max(maxInstances, instances);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 ===========================================================================
*/

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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.protocols.renderer.layouts.TableLayers;

/**
 * @mock.generate base="FormatAttributes"
 */
public class BaseGridAttributes
        extends FormatAttributes {

    private int columns;

    private TableLayers layers;

    public BaseGridAttributes() {
        initialise();
    }

    /**
     * This method should reset the state of this object back to its
     * state immediately after it was constructed.
     */
    public void resetAttributes() {
        super.resetAttributes();

        // Call this after calling super.resetAttributes to allow initialise to
        // override any inherited attributes.
        initialise();
    }

    /**
     * Initialise all the data members. This is called from the constructor
     * and also from resetAttributes.
     */
    private void initialise() {
        columns = 0;
    }

    /**
     * Set the columns property.
     *
     * @param columns The new value of the columns property.
     */
    public void setColumns(int columns) {
        this.columns = columns;
    }

    /**
     * Get the value of the columns property.
     *
     * @return The value of the columns property.
     */
    public int getColumns() {
        return columns;
    }

    public TableLayers getLayers() {
        return layers;
    }

    public void setLayers(TableLayers layers) {
        this.layers = layers;
    }
}

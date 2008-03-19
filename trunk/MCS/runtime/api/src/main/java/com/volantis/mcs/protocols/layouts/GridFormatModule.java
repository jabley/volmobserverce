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

package com.volantis.mcs.protocols.layouts;

import com.volantis.mcs.protocols.GridAttributes;
import com.volantis.mcs.protocols.GridChildAttributes;
import com.volantis.mcs.protocols.GridRowAttributes;

/**
 * <p> <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong> </p>
 */
public interface GridFormatModule {
    /**
     * Write the open grid markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    void writeOpenGrid(GridAttributes attributes);

    /**
     * Write the close grid markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    void writeCloseGrid(GridAttributes attributes);

    /**
     * Write the open grid child markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    void writeOpenGridChild(GridChildAttributes attributes);

    /**
     * Write the close grid child markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    void writeCloseGridChild(GridChildAttributes attributes);

    /**
     * Write the open grid row markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    void writeOpenGridRow(GridRowAttributes attributes);

    /**
     * Write the close grid row markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    void writeCloseGridRow(GridRowAttributes attributes);
}

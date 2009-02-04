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

package com.volantis.mcs.protocols.layouts;

import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.OutputBuffer;

/**
 * <p/>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate 
 */
public interface ContainerInstance {

    /**
     * Get the format.
     * 
     * @return The format.
     */
    public Format getFormat();

    /**
     * Get the index.
     *
     * @return The index.
     */
    public NDimensionalIndex getIndex();

    /**
     * Get the current OutputBuffer which contains the content of the Container.
     * Creates a buffer if it does not exist.
     *
     * @return The OutputBuffer which contains the content of the Container.
     */
    OutputBuffer getCurrentBuffer();

    /**
     * End the current buffer - nothing required for normal containers.
     */
    void endCurrentBuffer();

    /**
     * Check to see whether anything should be written to the container in the
     * current instance, or whether the container should be ignored.
     *
     * @return True if the container should be ignored and false otherwise.
     */
    boolean ignore();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9637/3	emma	VBM:2005092807 Adding tests for XForms emulation

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 ===========================================================================
*/

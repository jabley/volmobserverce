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

package com.volantis.mcs.protocols.renderer.layouts.spatial.aligned;

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.OutputBufferFactory;
import com.volantis.mcs.protocols.layouts.LayoutModule;

import java.util.Arrays;

/**
 * A collection of temporary output buffers used by the AlignedSpatialIterator
 * to get separate rendering of the aggregate rows.
 */
class RowOutputBuffers {

    // Array of output buffers, one per row
    private final OutputBuffer[] buffers;

    // Factory used to create new buffers
    private final OutputBufferFactory factory;

    /**
     * Create a new set of row output buffers with a size based on the
     * GridInstance information provided.
     *
     * @param gridRows The number of rows in the grid.
     * @param module   used to create new output buffers
     */
    public RowOutputBuffers(int gridRows, LayoutModule module) {
        buffers = new OutputBuffer[gridRows];
        factory = module.getOutputBufferFactory();
    }

    /**
     * Get buffer for a given row position.
     *
     * @param position of Row to get buffer for.
     * @return OutputBuffer for given row position.
     */
    OutputBuffer getBuffer(int position) {

        if (buffers[position] == null) {
            buffers[position] = factory.createOutputBuffer();
        }
        return buffers[position];
    }

    /**
     * Return true if the buffer at the given position is not null and not
     * empty.
     *
     * @param position of buffer to test
     * @return true if buffer not null and not empty
     */
    boolean bufferNotEmpty(int position) {

        if (buffers[position] == null) {
            return false;
        }

        return !buffers[position].isEmpty();
    }

    /**
     * Clear out all the buffers.
     */
    void reset() {
        Arrays.fill(buffers, null);
    }

    /**
     * Return the maximum number of buffers.
     *
     * @return number of buffers.
     */
    public int size() {
        return buffers.length;
    }
}

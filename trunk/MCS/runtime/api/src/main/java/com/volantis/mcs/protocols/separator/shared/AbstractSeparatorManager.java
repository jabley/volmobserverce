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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.separator.shared;

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.separator.SeparatorArbitrator;
import com.volantis.mcs.protocols.separator.SeparatorManager;

public abstract class AbstractSeparatorManager
        implements SeparatorManager {

    /**
     * The output buffer.
     */
    protected final OutputBuffer outputBuffer;

    /**
     * The arbitrator.
     */
    protected final SeparatorArbitrator arbitrator;

    /**
     * The arbitrator's decision.
     */
    protected final DefaultArbitratorDecision decision;



    protected AbstractSeparatorManager(OutputBuffer outputBuffer,
                                    SeparatorArbitrator arbitrator) {

        if (outputBuffer == null) {
            throw new IllegalArgumentException("outputBuffer may not be null");
        }

        if (arbitrator == null) {
            throw new IllegalArgumentException("arbitrator may not be null");
        }

        this.outputBuffer = outputBuffer;
        this.arbitrator = arbitrator;
        this.decision = new DefaultArbitratorDecision();
    }

    // Javadoc inherited
    public OutputBuffer getOutputBuffer() {
        return outputBuffer;
    }

    // Javadoc inherited
    public SeparatorArbitrator getArbitrator() {
        return arbitrator;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 ===========================================================================
*/

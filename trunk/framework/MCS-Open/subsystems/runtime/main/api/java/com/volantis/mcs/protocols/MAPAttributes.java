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
 * (c) Copyright Volantis Systems Ltd. 2008.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.protocols.ElementFinalizer;

/**
 * Attributes that cooperate with MAPResponseCallbacks and store finalizers
 */
public class MAPAttributes extends MCSAttributes {

    private ElementFinalizer finalizer;
    private boolean needsFinalizer;

    // Indicate if attribute contains finalizer
    protected boolean needsFinalizer() {
        return needsFinalizer;
    }

    /**
     * Marks element described by this attributes as needing finalizer. Passing
     * marked attributes to protocol writeOpenObject() method, the protocol CAN
     * provide a finalizer, which can be accessed using getFinalizer() method.
     */
    public void setNeedsFinalizer() {
        needsFinalizer = true;
    }

    /**
     * Returns element finalizer provided by the protocol.
     *
     * @returns element finalizer
     */
    public ElementFinalizer getFinalizer() {
        return finalizer;
    }

    /**
     * Set Finalizer
     * @param finalizer
     */
    protected void setFinalizer(ElementFinalizer finalizer) {
        this.finalizer = finalizer;
    }

    /**
     * Reset MAP specific attributes
     */
    public void resetAttributes() {
        super.resetAttributes();

        needsFinalizer = false;
        finalizer = null;
    }
}

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

package com.volantis.devrep.repository.impl.devices.policy.values;

import com.volantis.mcs.devices.policy.values.CompositePolicyValue;

/**
 * This class provides an abstract realisation of a Composite Policy Value.
 */
public abstract class DefaultCompositePolicyValue
        extends DefaultPolicyValue
        implements CompositePolicyValue {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * Flag to indicate if initialisation is complete.
     */
    protected boolean complete = false;

    /**
     * Mark the structure as having had it's initialisation completed.
     * <p>
     * <string>Note:</strong> This method must be called after all addition
     * methods and before retrieving the completed Map or List instance.
     */
    protected abstract void complete();

    /**
     * A method that tests whether the class has been completed by a call
     * to {@link #complete}.  If it is not when this is called an
     * IllegalStateException will be thrown.
     */
    protected void ensureComplete() {
        if (!complete) {
            throw new IllegalStateException("Value must be complete");
        }
    }

    /**
     * A method that tests whether the class has not been completed; i.e.
     * that {@link #complete} has not been called.  If it is when this is
     * called an IllegalStateException will be thrown.
     */
    protected void ensureIncomplete() {
        if (complete) {
            throw new IllegalStateException("Value must not be incomplete");
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 28-Jul-04	4952/1	claire	VBM:2004072301 Public API for Device Repository: Provide PolicyValue implementations

 ===========================================================================
*/

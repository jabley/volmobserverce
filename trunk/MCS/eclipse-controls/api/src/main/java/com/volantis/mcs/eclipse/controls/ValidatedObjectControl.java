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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.eclipse.controls;

import com.volantis.mcs.eclipse.validation.Validated;
import com.volantis.mcs.eclipse.controls.events.StateChangeListener;

/**
 * An object control that supports validation.
 */
public interface ValidatedObjectControl extends ObjectControl, Validated {
    /**
     * Determine if the validated object control is able to provide an object
     * @return true iff an object can be provided
     */
    public boolean canProvideObject();

    /**
     * add a state change listener to listen for changes in the object that
     * can be provided
     * @param listener
     */
    public void addStateChangeListener(StateChangeListener listener);

    /**
     * remove the state change listener
     */
    public void removeStateChangeListener(StateChangeListener listener);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-05	10708/1	ibush	VBM:2005120209 Disable new style wizard add button if all fields are empty

 08-Dec-05	10666/2	ibush	VBM:2005120209 Disable new style wizard add button if all fields are empty

 21-Jul-05	8713/2	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/

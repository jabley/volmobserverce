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
package com.volantis.mcs.eclipse.controls.events;

/**
 * Listener Interface which defines a callback function to notify an
 * object of a state change
 */
public interface StateChangeListener {
    /**
     * call back function
     */
    public void stateChanged();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-05	10666/1	ibush	VBM:2005120209 Disable new style wizard add button if all fields are empty

 ===========================================================================
*/

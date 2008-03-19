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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.editors.dom.validation;

/**
 * Interface defining the generic behaviour required of XML validators.
 */
public interface XMLValidator {
    /**
     * Invoke validation.
     */
    public void validate();

    /**
     * Add a listener that will be notified when validation has occurred.
     * If the listener is already listening this method takes no action.
     * @param listener The ValidationListener to add as a listener.
     * @throws IllegalArgumentException If listener is null.
     */
    public void addValidationListener(ValidationListener listener);

    /**
     * Remove a listener that has been added.
     * @param listener The ValidationListener to remove.
     * @throws IllegalStateException If the specified ValidationListener
     * is not listening to this validator.
     */
    public void removeValidationListener(ValidationListener listener);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-Jan-04	2447/1	philws	VBM:2004010609 Initial code for revised validation mechanism

 ===========================================================================
*/

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
package com.volantis.mcs.eclipse.controls;

import org.eclipse.swt.widgets.Composite;

/**
 * Factory for creating TimeProvider controls.
 */
public class TimeProviderFactory implements ValidatedTextControlFactory {
    
    /**
     * Builds a TimeProvider control.
     * @param parent the parent composite for the control
     * @param style the style for the control
     * @return a TimeProvider control
     */
    public ValidatedTextControl buildValidatedTextControl(Composite parent, int style) {
        return new TimeProvider(parent, style);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Jan-04	2215/1	pcameron	VBM:2003112405 TimeSelectionDialog, ListBuilder and refactoring

 ===========================================================================
*/

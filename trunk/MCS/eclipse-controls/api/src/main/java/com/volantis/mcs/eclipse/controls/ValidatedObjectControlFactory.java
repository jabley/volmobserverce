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

import org.eclipse.swt.widgets.Composite;

/**
 * An object that creates {@link ValidatedObjectControl}s.
 */
public interface ValidatedObjectControlFactory {
    /**
     * Creates a validated object control with the specified parent and style.
     *
     * @param parent The parent composite for the validated control
     * @param style The style of the validated control
     * @return An appropriate validated object control
     */
    public abstract ValidatedObjectControl buildValidatedObjectControl(
            Composite parent, int style);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jul-05	8713/2	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/

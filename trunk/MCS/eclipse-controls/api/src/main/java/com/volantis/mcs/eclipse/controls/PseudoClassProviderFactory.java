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
 * Factory for creating {@link PseudoClassProvider}s.
 */
public class PseudoClassProviderFactory implements ValidatedObjectControlFactory {
    /**
     * An array of names for the pseudo classes supported by this factory.
     */
    private String[] templates;

    /**
     * An array of display values for the pseudo classes supported by this
     * factory.
     */
    private String[] display;

    /**
     * An array of names for the pseudo classes supported by this factory
     * that can have parameters associated with them.
     */
    private String[] parameterised;

    /**
     * Create a pseudo class provider factory.
     *
     * @param templates The names for the pseudo classes
     * @param display The display names for the pseudo classes
     * @param parameterised The names of the pseudo classes that can be
     *                      parameterised
     */
    public PseudoClassProviderFactory(String[] templates, String[] display, String[] parameterised) {
        this.templates = templates;
        this.display = display;
        this.parameterised = parameterised;
    }

    // Javadoc inherited
    public ValidatedObjectControl buildValidatedObjectControl(Composite parent, int style) {
        return new PseudoClassProvider(parent, style, templates, display, parameterised);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Sep-05	9380/3	adrianj	VBM:2005082401 Tidy up and javadoc nth-child support

 14-Sep-05	9380/1	adrianj	VBM:2005082401 GUI support for nth-child

 ===========================================================================
*/

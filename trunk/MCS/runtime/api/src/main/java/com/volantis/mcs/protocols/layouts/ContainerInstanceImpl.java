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
package com.volantis.mcs.protocols.layouts;

import com.volantis.mcs.layouts.NDimensionalIndex;

/**
 * Can be either a region or pane.
 *
 * @mock.generate base="FormatInstance"
 */
public abstract class ContainerInstanceImpl
        extends FormatInstance
        implements ContainerInstance {

    /**
     * A flag which indicates whether we have already checked whether this
     * container should be ignored.
     */
    private boolean ignoreChecked;

    /**
     * A flag which indicates whether this container should be ignored or not.
     * It is only valid when ignoreChecked is true.
     */
    private boolean ignore;

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param index         of this context within its containing N dimensional
     *                      container.
     */
    protected ContainerInstanceImpl(NDimensionalIndex index) {
        super(index);
    }

    public void endCurrentBuffer() {
    }

    public boolean ignore() {
        if (!ignoreChecked) {
            ignore = ignoreImpl();
            ignoreChecked = true;
        }

        return ignore;
    }

    /**
     * Check to see whether anything should be written to the container in the
     * current instance, or whether the container should be ignored.
     *
     * @return True if the container should be ignored and false otherwise.
     */
    protected abstract boolean ignoreImpl();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 15-Sep-05	9524/1	emma	VBM:2005091503 Added ContainerInstance to allow regions and panes to be treated in the same way

 ===========================================================================
*/

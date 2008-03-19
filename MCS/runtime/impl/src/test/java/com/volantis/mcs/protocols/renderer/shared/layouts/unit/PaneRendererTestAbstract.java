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

package com.volantis.mcs.protocols.renderer.shared.layouts.unit;

import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.layouts.PaneMock;
import com.volantis.mcs.protocols.PaneAttributesMock;
import com.volantis.mcs.protocols.renderer.shared.layouts.unit.AbstractFormatRendererTestAbstract;

/**
 * Base for all test cases that test classes for rendering panes.
 */
public abstract class PaneRendererTestAbstract
        extends AbstractFormatRendererTestAbstract {

    /**
     * Initialise the mock with default attribute values.
     *
     * @param expects The mock whose default attribute values need
     * initialising.
     */
    protected void expectGetDefaultPaneAttributes(PaneMock.Expects expects) {
        expectGetDefaultPaneGridAttributes(expects);
    }

    /**
     * Prepare the attributes mock for being initialise.
     *
     * @param paneAttributesMock The attribvutes mock.
     * @param paneMock The pane.
     */
    protected void expectSetPaneAttributes(
            PaneAttributesMock paneAttributesMock, Pane paneMock) {

        expectSetPaneGridAttributes(paneAttributesMock.expects, null);

        paneAttributesMock.expects.setPane(paneMock).atLeast(1);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 ===========================================================================
*/

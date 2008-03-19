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
package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.renderer.RendererException;


/**
 * Implementations of this are responsible for rendering shortcut labels.
 * <p>Where possible implementations should not contain any information that
 * would prevent it being used by multiple threads.</p>
 */
public interface ShortcutLabelRenderer {


    /**
     * Adds markup for the shortcut label to the specified buffer.
     * @param buffer The output buffer in which we will render.
     * @param item The menu item to render.
     */
    public void render(OutputBuffer buffer,
                       MenuItem item)
            throws RendererException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Jan-05	6129/3	matthew	VBM:2004102019 supermerge required

 23-Nov-04	6129/1	matthew	VBM:2004102019 Enable shortcut menu link rendering

 ===========================================================================
*/

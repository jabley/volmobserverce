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
package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.ProtocolException;

/**
 * An interface to allow the output of an image. 
 * <p>
 * <strong>
 * Note that this is deprecated because we will hopefully be replacing this 
 * simple "existing protocol api callback" with a properly designed system for
 * rendering protocol elements outside of the protocol classes in future.   
 * </strong>
 */ 
public interface DeprecatedImageOutput {

    /**
     * Render the protocol image element represented by the image 
     * attributes provided into the output buffer provided.
     * 
     * @param dom the output buffer to render to
     * @param attributes the attributes of the image to write out.
     */ 
    void outputImage(DOMOutputBuffer dom, ImageAttributes attributes)
            throws ProtocolException;
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-May-04	4174/3	claire	VBM:2004050501 Enhanced Menus: (X)HTML menu renderer selectors and protocol integration

 06-May-04	4153/2	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 26-Apr-04	3920/7	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers (review comments)

 26-Apr-04	3920/5	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 20-Apr-04	3715/1	claire	VBM:2004040201 Improving WML menu item renderers

 15-Apr-04	3645/1	geoff	VBM:2004032904 Enhance Menu Support: Open Wave Menu Renderer

 ===========================================================================
*/

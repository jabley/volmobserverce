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

import com.volantis.mcs.protocols.AnchorAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;

/**
 * An interface to allow the output of an anchor link from protocol anchor
 * attributes.
 * <p>
 * <strong>
 * Note that this is deprecated because we will hopefully be replacing this 
 * simple "existing protocol api callback" with a properly designed system for
 * rendering protocol elements outside of the protocol classes in future.   
 * </strong>
 */
public interface DeprecatedAnchorOutput {

    /**
     * Starting the rendering the anchor represented by the anchor attributes
     * to the output buffer provided.  This may produce either an &lt;a... or
     * an &lt;anchor as output depending on protocol values at the time.
     *
     * @param dom        The output buffer to render to
     * @param attributes The attributes of the element to render
     */
    public void openAnchor(DOMOutputBuffer dom, AnchorAttributes attributes)
            throws ProtocolException;

    /**
     * Finishing the rendering of the anchor.
     *
     * @param dom        The output buffer to render to
     * @param attributes The attributes of the element to render
     */
    public void closeAnchor(DOMOutputBuffer dom, AnchorAttributes attributes);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-May-04	4174/1	claire	VBM:2004050501 Enhanced Menus: (X)HTML menu renderer selectors and protocol integration

 06-May-04	4153/2	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 26-Apr-04	3920/3	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers (review comments)

 26-Apr-04	3920/1	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 20-Apr-04	3715/3	claire	VBM:2004040201 Improving WML menu item renderers

 16-Apr-04	3715/1	claire	VBM:2004040201 Enhanced Menu: WML Menu Item Renderers

 ===========================================================================
*/

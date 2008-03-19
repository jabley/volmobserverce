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
import com.volantis.mcs.protocols.DivAttributes;
import com.volantis.mcs.protocols.ProtocolException;

/**
 * An interface to allow the output of a div.
 * <p>
 * <strong>
 * Note that this is deprecated because we will hopefully be replacing this
 * simple "existing protocol api callback" with a properly designed system for
 * rendering protocol elements outside of the protocol classes in future.
 * </strong>
 *
 * @mock.generate 
 */
public interface DeprecatedDivOutput {

    /**
     * Add the open div markup to the specified DOMOutputBuffer.
     *
     * @param dom The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    void openDiv(DOMOutputBuffer dom, DivAttributes attributes)
            throws ProtocolException;

    /**
     * Add the close div markup to the specified DOMOutputBuffer.
     *
     * @param dom The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    void closeDiv(DOMOutputBuffer dom, DivAttributes attributes);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-May-04	4217/1	geoff	VBM:2004042807 Enhance Menu Support: Renderers: Menu Markup

 06-May-04	4174/1	claire	VBM:2004050501 Enhanced Menus: (X)HTML menu renderer selectors and protocol integration

 06-May-04	4153/2	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 ===========================================================================
*/

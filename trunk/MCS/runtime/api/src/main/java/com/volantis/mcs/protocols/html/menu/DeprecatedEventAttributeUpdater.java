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
package com.volantis.mcs.protocols.html.menu;

import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.renderer.RendererException;

/**
 * An interface to allow the rendering of the event related attributes for an 
 * element. 
 * <p>
 * Note that this is deprecated because we will hopefully be replacing this 
 * simple "existing protocol api callback" with a properly designed system for
 * rendering event attributes outside of the protocol classes in future.
 * 
 * @todo rename this to something more sensible.   
 */ 
public interface DeprecatedEventAttributeUpdater {

    /**
     * Merge an optional user specified script with the internal one and store
     * the result in the event attributes.
     * 
     * @param attributes the attributes to have script added.
     * @param event the code for the event attribute to add.
     * @param script the script to add as the event.
     * 
     * @throws RendererException if there was a problem.
     */ 
    void mergeEventAttribute(MCSAttributes attributes, int event, String script)
            throws RendererException;
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 12-May-04	4279/2	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 11-May-04	4217/1	geoff	VBM:2004042807 Enhance Menu Support: Renderers: Menu Markup

 06-May-04	4153/1	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 26-Apr-04	3920/3	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers (review comments)

 26-Apr-04	3920/1	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 ===========================================================================
*/

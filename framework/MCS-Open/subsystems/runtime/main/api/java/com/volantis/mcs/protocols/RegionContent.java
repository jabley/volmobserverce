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
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 */
 
package com.volantis.mcs.protocols;

import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;

/**
 * RegionContent
 * 
 * @mock.generate
 */
public interface RegionContent {

    
    /** 
     * Render the content. 
     * 
     * @param context the page context to render with.
     */
    public abstract void render(FormatRendererContext context);
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/2	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Jun-04	4698/1	geoff	VBM:2003061912 RegionContent should not store a MarinerPageContext

 19-Jun-03	407/1	steve	VBM:2002121215 Flow elements and PCData in regions

 ===========================================================================
*/

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

package com.volantis.mcs.protocols.css.renderer;

import com.volantis.mcs.protocols.ValidationHelper;

/**
 * This interface provides configuration items from the protocol to the
 * (@link RuntimeCSSStyleSheetRenderer}.
 */
public interface RuntimeRendererProtocolConfiguration {

    /**
     * Retrieve the protocol specific ValidationHelper.
     * @return The {@link ValidationHelper}
     */
    public ValidationHelper getValidationHelper();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10527/5	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10480/1	pduffin	VBM:2005070711 Merged changes from main trunk

 05-Oct-05	9440/1	schaloner	VBM:2005070711 Added marker pseudo-element support

 05-Oct-05	9440/1	schaloner	VBM:2005070711 Added marker pseudo-element support

 22-Aug-05	9324/1	ianw	VBM:2005080202 Move validation for WapCSS into styling

 ===========================================================================
*/

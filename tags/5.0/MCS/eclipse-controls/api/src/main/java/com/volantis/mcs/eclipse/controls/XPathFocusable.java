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

package com.volantis.mcs.eclipse.controls;

import com.volantis.mcs.xml.xpath.XPath;

/**
 * XPath Focusable interface definition. 
 */
public interface XPathFocusable {
    /**
     * Set the focus to the control with that XPath.
     * @param path
     * @return true If focus was accepted; false otherwise.
     */
    public boolean setFocus(XPath path);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Nov-03	2013/1	allan	VBM:2003112501 Support multi-attribute controls and XPathFocusable.

 15-Nov-03	1825/1	byron	VBM:2003092601 Create generic policy property composite

 ===========================================================================
*/

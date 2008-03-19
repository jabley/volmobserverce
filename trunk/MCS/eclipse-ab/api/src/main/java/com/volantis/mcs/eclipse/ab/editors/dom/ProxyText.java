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
package com.volantis.mcs.eclipse.ab.editors.dom;

import com.volantis.mcs.eclipse.common.odom.ODOMText;

/**
 * Used as a marker to indicate that a given text node is part of a proxy
 * structure. No additional functionality is provided in and of itself, as the
 * text updating is handled by the parent ProxyElement, but only ProxyText
 * elements can be added to a parent ProxyElement.
 *
 * @see ProxyElement
 */
public class ProxyText extends ODOMText {
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Oct-05	9734/2	adrianj	VBM:2005100510 Allow text in ProxyElements

 ===========================================================================
*/

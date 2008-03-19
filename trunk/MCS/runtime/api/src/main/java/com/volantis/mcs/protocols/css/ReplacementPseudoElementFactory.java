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
package com.volantis.mcs.protocols.css;

import com.volantis.mcs.dom.Element;
import com.volantis.styling.Styles;

/**
 * Detects the replacement element required for a pseudo element, based on
 * the context and protocol.
 */
public interface ReplacementPseudoElementFactory {

    /**
     * Creates the element required to effect a pseudo element.
     * @param element the element
     * @param styles the nested pseudo style from the element's styles.
     * @return the element required to effect the pseudo element.
     */
    Element createElement(Element element,
                          Styles styles);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Oct-05	9440/1	schaloner	VBM:2005070711 Added marker pseudo-element support

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 07-Sep-05	9413/3	schaloner	VBM:2005070406 Changed style property iteration to direct access

 06-Sep-05	9413/1	schaloner	VBM:2005070406 Implemented before and after pseudo-element support

 ===========================================================================
*/

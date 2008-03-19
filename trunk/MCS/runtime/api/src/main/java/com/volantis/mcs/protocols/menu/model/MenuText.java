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

package com.volantis.mcs.protocols.menu.model;

import com.volantis.mcs.protocols.OutputBuffer;

/**
 * Represents the text used in a menu label, supporting two deprecated
 * stylistic properties for covering text colouration.
 */
public interface MenuText extends ModelElement {
    /**
     * Returns the text associated with the menu label, as an
     * <code>OutputBuffer</code>.
     *
     * @return the text for the menu label. Will not be null
     */
    OutputBuffer getText();
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Sep-05	9609/1	ibush	VBM:2005082215 Move on/off color values for menu items

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Apr-04	3429/1	philws	VBM:2004031502 MenuLabelElement implementation

 10-Mar-04	3306/1	claire	VBM:2004022706 Implementation of the menu model

 03-Mar-04	3288/1	philws	VBM:2004022702 Add Menu Model API

 ===========================================================================
*/

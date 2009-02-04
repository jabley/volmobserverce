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

import com.volantis.mcs.protocols.assets.ImageAssetReference;

/**
 * Represents an optionally two-state icon for use in a menu label.
 */
public interface MenuIcon extends ModelElement {
    /**
     * Returns the primary image URL for this icon.
     *
     * @return the primary image URL for the icon. Will not be null
     */
    ImageAssetReference getNormalURL();

    /**
     * Returns the optional secondary image URL for this icon.
     *
     * @return the secondary image URL for the icon. May be null
     */
    ImageAssetReference getOverURL();
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-Mar-04	3500/1	claire	VBM:2004031806 Initial implementation of abstract component image references

 10-Mar-04	3306/1	claire	VBM:2004022706 Implementation of the menu model

 03-Mar-04	3288/1	philws	VBM:2004022702 Add Menu Model API

 ===========================================================================
*/

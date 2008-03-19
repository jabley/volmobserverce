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
package com.volantis.mcs.layouts;

/**
 * Defines accessors for style attributes.
 */
public interface StyleAttributes {

    /**
     * Sets the style class.
     * @param styleClass the style class.
     */
    public void setStyleClass(String styleClass);

    /**
     * Gets the style class.
     * @return the style class.
     */
    public String getStyleClass();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Added width, height, and style accessor interfaces derived from CoreAttributes interface

 ===========================================================================
*/

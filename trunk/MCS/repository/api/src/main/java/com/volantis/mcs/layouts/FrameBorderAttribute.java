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
 * Defines accessors for frame border attribute.
 */
public interface FrameBorderAttribute {

    /**
     * Sets the border colour.
     * @param frameBorder the border colour.
     */
    public void setFrameBorder(String frameBorder);

    /**
     * Gets the frame border.
     * @return the frame border.
     */
    public String getFrameBorder();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Oct-05	9652/1	gkoch	VBM:2005092204 completely custom marshalling/unmarshalling of layoutFormat

 ===========================================================================
*/

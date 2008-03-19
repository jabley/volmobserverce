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
package com.volantis.mcs.integration;

/**
 * This class defines objects that are expected to modify the url of an image
 * in some way where the url provided is in StringBuffer form.
 */
public interface ImageURLModifier {
    /**
     * Modify the url specified by urlStringBuffer.
     * @param urlStringBuffer the StringBuffer containing the url to be
     * modified
     * @param details the ImageURLModifierDetails containing properties that
     * may be useful for doing the modification.
     */
    public void modifyImageURL(StringBuffer urlStringBuffer,
                               ImageURLModifierDetails details);
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6693/3	allan	VBM:2005011403 Remove MPS specific image url parameters

 17-Jan-05	6693/1	allan	VBM:2005011403 Remove MPS specific image url parameters

 ===========================================================================
*/

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
package com.volantis.styling;

/**
 * This interface defines the functionality required by any class which
 * contains style information. 
 */
public interface StyleContainer {

    /**
     * Get the Styles of this StyleContainer.
     *
     * @return The Styles of this StyleContainer. May be null.
     */
    public Styles getStyles();

    /**
     * Set the Styles for this StyleContainer.
     *
     * @param styles The new Styles for this StyleContainer. May be null.
     */
    public void setStyles(Styles styles);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 22-Jun-05	8483/3	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 09-Jun-05	8665/1	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 ===========================================================================
*/

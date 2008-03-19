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

import com.volantis.mcs.protocols.OutputBuffer;

/**
 * A protocol module that provides CSS support.
 */
public interface CSSModule {

    /**
     * Add a place holder for inline CSS into the buffer.
     *
     * @param buffer The buffer into which the place holder should be added.
     *
     * @return The place holder.
     */
    PlaceHolder addInlinePlaceHolder(OutputBuffer buffer);

    /**
     * Update the contents of the place holder with the CSS.
     *
     * @param placeHolder The place holder to update.
     * @param css The CSS to add.
     */
    void updateInlinePlaceHolder(PlaceHolder placeHolder, String css);

    /**
     * Add a place holder for external CSS into the buffer.
     *
     * @param buffer The buffer into which the place holder should be added.
     *
     * @return The place holder.
     */
    PlaceHolder addExternalPlaceHolder(OutputBuffer buffer);

    /**
     * Update the contents of the place holder with the specified URL.
     *
     * @param placeHolder The place holder to update.
     * @param url The URL to the external CSS.
     */
    void updateExternalPlaceHolder(PlaceHolder placeHolder, String url);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 ===========================================================================
*/

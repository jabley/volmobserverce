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

package com.volantis.mcs.protocols.css.reference;

import com.volantis.mcs.protocols.OutputBuffer;

/**
 * A reference to some CSS, either stored internally or externally.
 *
 * <p>Instances of these are added to the page head (and generate their markup)
 * before the CSS has actually been generated. When the CSS has been generated
 * then they update the markup with the appropriate information.</p>
 */
public interface CssReference {

    /**
     * Write the markup that will act as a placeholder for the real markup.
     *
     * @param buffer The buffer into which the markup will be added.
     */
    public void writePlaceHolderMarkup(OutputBuffer buffer);

    /**
     * Update the markup with the CSS from the specified style sheet.
     *
     * @param css The style sheet from which the CSS should be
     */
    public void updateMarkup(String css);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 ===========================================================================
*/

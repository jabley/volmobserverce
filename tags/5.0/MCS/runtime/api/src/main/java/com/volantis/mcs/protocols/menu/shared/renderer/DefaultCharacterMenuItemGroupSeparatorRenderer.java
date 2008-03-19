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
package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;

/**
 * This class provides a default means of rendering a character separator
 * between menu item groups to a given output buffer.  This allows the various
 * protocols and renderers to support character group separators rendering
 * without repeating the same code.  In most cases for most protocols the
 * output will be as in this class.  If specialised behaviour is required
 * another SeparatorRenderer can be dropped in in place of this class.
 */
public class DefaultCharacterMenuItemGroupSeparatorRenderer
        implements SeparatorRenderer {

    /**
     * The character string to use as the character separator.
     */
    private String charString;

    /**
     * The number of times the character string should be repeated for each
     * separator that is rendered.
     */
    private int repeats;

    /**
     * Initialises a new instance of the character menu item group separator
     * renderer using the given parameters.
     *
     * @param charString The character string to use as the separator.  May
     *                   not be null and may not be an empty string.
     * @param repeats    The number of times that the character string
     *                   separator should be repeated.  This may not be less
     *                   than one.  Whilst a zero repeat has a special meaning
     *                   if encounted in the theme this should have been
     *                   resolved once this renderer is called and the
     *                   correct number of real repeats calculated.  <strong>
     *                   This repeat value produces exactly that number of
     *                   repeats of the character string in the output buffer.
     *                   It does not default to repeat value plus one as
     *                   previous character separator code did (erroneously).
     *                   </strong>
     */
    public DefaultCharacterMenuItemGroupSeparatorRenderer(String charString,
                                                          int repeats) {
        if (charString == null || charString.equals("")) {
            throw new IllegalArgumentException(
                "Null or empty strings not permitted as a character separator");
        } else if (repeats <= 0) {
            throw new IllegalArgumentException(
                "Zero or less not allowed as repeat on a character separator");
        }
        this.charString = charString;
        this.repeats = repeats;
    }

    // JavaDoc inherited
    public void render(OutputBuffer buffer)
            throws RendererException {
        // Get the writer
        DOMOutputBuffer domBuffer = (DOMOutputBuffer) buffer;

        // Output the separator character string an appropriate number of times
        for (int i = 0; i < repeats; i++) {
            domBuffer.appendEncoded(charString);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 22-Apr-04	3986/1	claire	VBM:2004042106 Creating a default menu item group character separator

 ===========================================================================
*/

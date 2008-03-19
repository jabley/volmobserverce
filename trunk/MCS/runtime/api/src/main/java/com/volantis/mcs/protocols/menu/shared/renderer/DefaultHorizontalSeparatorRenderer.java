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
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.MCSMenuHorizontalSeparatorKeywords;

/**
 * This class provides a default means of rendering a horizontal separator to
 * a given output buffer.  This allows the various protocols and renderers to
 * support horizontal rendering without repeating the same code.  In most
 * cases for most protocols the output will be as in this class.  If specialised
 * behaviour is required another SeparatorRenderer can be dropped in in place of
 * this class.
 */
public class DefaultHorizontalSeparatorRenderer
        implements HorizontalSeparatorRenderer {

    /**
     * The property type that governs what this renderer outputs.  This will be
     * one of {@link MCSMenuHorizontalSeparatorKeywords}.
     */
    private StyleValue property;


    /**
     * This creates a new instance of a horizontal separator renderer.  It is
     * initialised with the value required to indicate how it renders its
     * contents.
     *
     * @param property       The type of separator that should be output.  This
     *                       value should be from the enumeration
     *                       {@link MCSMenuHorizontalSeparatorKeywords}.
     */
    public DefaultHorizontalSeparatorRenderer(StyleValue property) {
        if (property == null) {
            throw new IllegalArgumentException("property cannot be null");
        }
        this.property = property;
    }

    // JavaDoc inherited
    public void render(OutputBuffer buffer)
            throws RendererException {

        // Get the writer
        DOMOutputBuffer domBuffer = (DOMOutputBuffer) buffer;

        // Get the information to output
        String text = null;

        if (property == MCSMenuHorizontalSeparatorKeywords.NONE) {
        } else if (property == MCSMenuHorizontalSeparatorKeywords.SPACE) {
            text = " ";
        } else if (property ==
                MCSMenuHorizontalSeparatorKeywords.NON_BREAKING_SPACE) {
            // The conversion of this to a named entity, a numeric entity,
            // a space character, or leaving this as the unicode
            // representation is made at the buffer level for each protocol.
            text = VolantisProtocol.NBSP;
        } else {
            throw new IllegalArgumentException(
                    "Unknown separator type " + property);
        }
        // Render the information using the writer
        if (text != null) {
            // @todo: DOMBuffer needs fixing to use mixed content here
            // This method is called to preserve whitespace in the DOM element
            // because otherwise any " " would get removed.  It is set here and
            // not in the space separator above in case any protocol also
            // converts NBSP to " ".
            domBuffer.setElementIsPreFormatted(true);
            domBuffer.writeText(text);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 26-Apr-04	3920/1	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 21-Apr-04	3956/3	claire	VBM:2004042008 Provision of Default Horizontal Separator Renderer

 21-Apr-04	3956/1	claire	VBM:2004042008 Provision of Default Horizontal Separator Renderer

 ===========================================================================
*/

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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DivAttributes;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.renderer.RendererException;

/**
 * A default bracketing renderer for menus.
 * <p>
 * By default we write out a div. This choice is fairly arbitrary as 
 * previously we had either a div hacked to be inline or a span, and we are
 * really supposed to output a block level element for a menu but that hasn't
 * been thought through yet. Hopefully this will get us through for now until
 * we have time to revisit this properly.
 */ 
public class DefaultMenuBracketingRenderer implements MenuBracketingRenderer {

    /**
     * Reference to an object that is capable of generating div markup based
     * on protocol "knowledge"
     */
    private final DeprecatedDivOutput divOutput;
    
    /**
     * The div attributes used during the rendering of {@link #open} and
     * {@link #close}.
     */
    private DivAttributes attributes;

    /**
     * Create a new initialised instance of this renderer.
     *
     * @param divOutput A protocol specific means of rendering an div.
     */
    public DefaultMenuBracketingRenderer(DeprecatedDivOutput divOutput) {
        this.divOutput = divOutput;
    }

    // Javadoc inherited.
    public void open(OutputBuffer buffer, Menu menu)
            throws RendererException {
        
        // Extract the output buffer for use in createElement
        DOMOutputBuffer outputBuffer = (DOMOutputBuffer) buffer;
        
        // Create the div attributes
        attributes = new DivAttributes();
        // Stylistic properties
        ElementDetails elementDetails = menu.getElementDetails();
        if (elementDetails != null) {
            attributes.setElementDetails(elementDetails);
        }

        // Create the actual element
        try {
            divOutput.openDiv(outputBuffer, attributes);
        } catch (ProtocolException e) {
            throw new RendererException(e);
        }
    }

    // Javadoc inherited.
    public void close(OutputBuffer buffer, Menu menu)
            throws RendererException {
        
        // Extract the output buffer for use in createElement
        DOMOutputBuffer outputBuffer = (DOMOutputBuffer) buffer;

        // Close the div rendering
        divOutput.closeDiv(outputBuffer, attributes);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 11-May-04	4217/2	geoff	VBM:2004042807 Enhance Menu Support: Renderers: Menu Markup

 ===========================================================================
*/

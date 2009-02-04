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
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.renderer.RendererException;

/**
 * Abstract class for text menu item renderers which applies the menu label
 * text style via a span element wrapped around the text being written.
 */
public abstract class AbstractStyledTextMenuItemRenderer
        implements MenuItemComponentRenderer {

    /**
     * The outputter used when style is to be applied to the text.
     */
    private final DeprecatedSpanOutput spanOutput;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param spanOutput the outputter for span markup required when style is
     *                   applied to the text
     */
    protected AbstractStyledTextMenuItemRenderer(DeprecatedSpanOutput spanOutput) {

        if (spanOutput == null) {
            throw new IllegalArgumentException("spanOutput must not be null");
        }

        this.spanOutput = spanOutput;
    }

    // JavaDoc inherited
    public MenuItemRenderedContent render(OutputBuffer buffer, MenuItem item)
            throws RendererException {

        try {
            // Get the information from the menu item to output
            SpanAttributes attributes = null;
            final ElementDetails elementDetails =
                    item.getLabel().getText().getElementDetails();
            // If the style information exists we will need to output a "span"
            // around the text in order to apply style
            if (elementDetails != null) {
                // Set up the span attributes to be applied here and after the
                // text output
                attributes = new SpanAttributes();
                attributes.setElementDetails(elementDetails);

                spanOutput.openSpan((DOMOutputBuffer)buffer, attributes);
            }

            // Write the text for the menu item into the output buffer.
            writeMenuTextToBuffer(buffer, item);

            if (elementDetails != null) {
                spanOutput.closeSpan((DOMOutputBuffer)buffer, attributes);
            }
        } catch (ProtocolException e) {
            throw new RendererException(e);
        }

        return MenuItemRenderedContent.TEXT;
    }

    /**
     * Writes out the text of the menu into the output buffer.
     *
     * @param output the buffer to output the text to.
     * @param item the menu item who's text is to be rendered.
     */
    protected abstract void writeMenuTextToBuffer(OutputBuffer output,
            MenuItem item);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Oct-04	5635/2	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 ===========================================================================
*/

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
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.shared.EventSupport;
import com.volantis.mcs.protocols.renderer.RendererException;

/**
 * Renders a protocol specific span (by calling back on the protocol),
 * adding style and event information associated with the menu item.
 */ 
public class DefaultSpanMenuItemRenderer 
        implements MenuItemBracketingRenderer {

    /**
     * Reference to an object that is capable of generating span markup based
     * on protocol "knowledge"
     */
    private final DeprecatedSpanOutput spanOutput;

    /**
     * The span attributes used during the rendering of {@link #open} and
     * {@link #close}.
     */
    private SpanAttributes attributes;

    /**
     * Create a new initialised instance of this renderer.
     *
     * @param spanOutput A protocol specific means of rendering an span.
     */
    public DefaultSpanMenuItemRenderer(DeprecatedSpanOutput spanOutput) {
        this.spanOutput = spanOutput;
    }

    // JavaDoc inherited
    public boolean open(OutputBuffer buffer, MenuItem item)
            throws RendererException {

        try {
            // Extract the output buffer for use in createElement
            DOMOutputBuffer outputBuffer = (DOMOutputBuffer) buffer;

            // Create the span attributes
            attributes = new SpanAttributes();

            // Stylistic properties
            ElementDetails elementDetails = item.getElementDetails();
            if (elementDetails != null) {
                attributes.setElementDetails(elementDetails);
            }

            // Events
            EventSupport.setEvents(item, attributes);
            
            // Create the actual element
            spanOutput.openSpan(outputBuffer, attributes);
        } catch (ProtocolException pe) {
            throw new RendererException(pe);
        }
        return true;
    }

    // JavaDoc inherited
    public void close(OutputBuffer buffer, MenuItem item)
            throws RendererException {

        // Extract the output buffer for use in createElement
        DOMOutputBuffer outputBuffer = (DOMOutputBuffer) buffer;

        // Close the span rendering
        spanOutput.closeSpan(outputBuffer, attributes);
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

 01-Oct-04	5635/1	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 14-May-04	4388/1	philws	VBM:2004050405 Handle events on menu items

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 11-May-04	4217/1	geoff	VBM:2004042807 Enhance Menu Support: Renderers: Menu Markup

 06-May-04	4174/1	claire	VBM:2004050501 Enhanced Menus: (X)HTML menu renderer selectors and protocol integration

 06-May-04	4153/2	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 ===========================================================================
*/

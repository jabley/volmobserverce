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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.renderer.shared.layouts;

import com.volantis.mcs.layouts.ColumnIteratorPane;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.ColumnIteratorPaneAttributes;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.layouts.AbstractPaneInstance;
import com.volantis.mcs.protocols.layouts.ColumnIteratorPaneInstance;
import com.volantis.mcs.protocols.layouts.LayoutModule;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.util.Iterator;

/**
 * A format renderer that is used to render column iterator panes.
 */
public class ColumnIteratorPaneRenderer extends AbstractPaneRenderer {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(ColumnIteratorPaneRenderer.class);

    // Javadoc inherited.
    protected void renderPaneInstance(
            FormatRendererContext context,
            AbstractPaneInstance abstractPaneInstance) throws IOException {

        ColumnIteratorPane pane = (ColumnIteratorPane)
                abstractPaneInstance.getFormat();
        ColumnIteratorPaneInstance paneInstance = (ColumnIteratorPaneInstance)
                abstractPaneInstance;

        // Get the attributes.
        ColumnIteratorPaneAttributes attributes = (ColumnIteratorPaneAttributes)
                paneInstance.getAttributes();

        if (logger.isDebugEnabled()) {
            logger.debug("ColumnIteratorFormat.writeOutput() for "
                         + pane.getName());
        }

        // Get the module.
        LayoutModule module = context.getLayoutModule();

        // Write out our pane preamble
        module.writeOpenColumnIteratorPane(attributes);

        Iterator itr =
                paneInstance.getBufferIterator();
        while (itr.hasNext()) {
            Object o = itr.next();
            if (o instanceof OutputBuffer) {
                OutputBuffer contentBuffer = (OutputBuffer) o;
                if (!contentBuffer.isEmpty()) {

                    // Copy the attributes because the rendering process
                    // destroys the styles.
                    ColumnIteratorPaneAttributes paneElementAttributes =
                            new ColumnIteratorPaneAttributes();
                    paneElementAttributes.copy(attributes);

                    // Write out the element preamble
                    module.writeOpenColumnIteratorPaneElement(
                            attributes);

                    // Write out the contents of this buffer, trim off
                    // any leading or trailing white space.
                    module.writeColumnIteratorPaneElementContents(
                            contentBuffer);

                    // Write out the element postamble
                    module.writeCloseColumnIteratorPaneElement(
                            attributes);
                }
            }
        }

        // Write out our pane postamble
        module.writeCloseColumnIteratorPane(attributes);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Nov-05	10282/1	emma	VBM:2005110902 Forward port: fixing two layout rendering bugs

 11-Nov-05	10253/1	emma	VBM:2005110902 Fixing two layout rendering bugs

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 11-Jul-05	8862/2	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 23-Feb-05	7114/1	geoff	VBM:2005021402 Exception when addressing row/column iterator panes using pane element

 23-Feb-05	7079/1	geoff	VBM:2005021402 Exception when addressing row/column iterator panes using pane element

 06-Jan-05	6391/5	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 21-Dec-04	6402/1	philws	VBM:2004120208 Added aligning spatial format iterator renderer

 10-Dec-04	6391/1	adrianj	VBM:2004120207 Refactoring DeviceLayoutRenderer

 ===========================================================================
*/

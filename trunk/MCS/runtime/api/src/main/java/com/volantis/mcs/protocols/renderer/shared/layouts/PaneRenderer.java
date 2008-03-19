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

import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.layouts.AbstractPaneInstance;
import com.volantis.mcs.protocols.layouts.LayoutModule;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;

/**
 * A format renderer that is used to render panes.
 */
public class PaneRenderer extends AbstractPaneRenderer {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(PaneRenderer.class);

    // Javadoc inherited.
    protected void renderPaneInstance(
            FormatRendererContext context,
            AbstractPaneInstance abstractPaneInstance)
            throws IOException, ProtocolException {

        Pane pane = (Pane) abstractPaneInstance.getFormat();

        // Get the attributes.
        PaneAttributes attributes = abstractPaneInstance.getAttributes();
        attributes.setPane(pane);
        attributes.setFormat(pane.getParent());

        // Get the module.
        LayoutModule module = context.getLayoutModule();

        module.writeOpenPane(attributes);

        if (logger.isDebugEnabled()) {
            logger.debug("Pane.writeOutput() for " + pane.getName());
        }

        // Write out the contents of the content buffer
        OutputBuffer contentBuffer =
                abstractPaneInstance.getCurrentBuffer(false);

        // Write out the contents of this buffer, trim off any leading
        // or trailing white space.
        if (contentBuffer != null) {
            module.writePaneContents(contentBuffer);
        }

        // Write out our pane postamble
        attributes.setFormat(pane.getParent());
        module.writeClosePane(attributes);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/6	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 11-Jul-05	8862/2	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 06-Jan-05	6391/5	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 21-Dec-04	6402/3	philws	VBM:2004120208 Added aligning spatial format iterator renderer

 20-Dec-04	6402/1	philws	Promoting rebuilt jar files

 10-Dec-04	6391/1	adrianj	VBM:2004120207 Refactoring DeviceLayoutRenderer

 ===========================================================================
*/

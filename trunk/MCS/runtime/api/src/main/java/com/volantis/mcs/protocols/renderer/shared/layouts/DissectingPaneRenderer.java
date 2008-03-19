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

import com.volantis.mcs.layouts.DissectingPane;
import com.volantis.mcs.protocols.DissectingPaneAttributes;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.layouts.AbstractPaneInstance;
import com.volantis.mcs.protocols.layouts.DissectingPaneInstance;
import com.volantis.mcs.protocols.layouts.LayoutAttributesFactory;
import com.volantis.mcs.protocols.layouts.LayoutModule;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;

import java.io.IOException;

/**
 * A format renderer that is used to render dissecting panes.
 */
public class DissectingPaneRenderer extends PaneRenderer {

    /**
     * The factory to use to create attributes classes.
     */
    private final LayoutAttributesFactory factory;

    /**
     * Initialise.
     *
     * @param factory The factory to use to construct any attributes classes
     * used internally.
     */
    public DissectingPaneRenderer(LayoutAttributesFactory factory) {
        this.factory = factory;
    }

    // Javadoc inherited.
    protected void renderPaneInstance(
            FormatRendererContext context,
            AbstractPaneInstance abstractPaneInstance)
            throws IOException, ProtocolException {

        DissectingPane pane = (DissectingPane) abstractPaneInstance.getFormat();
        String inclusionPath = context.getInclusionPath();
        DissectingPaneInstance paneInstance = (DissectingPaneInstance)
                abstractPaneInstance;

        // Initialise the attributes.
        DissectingPaneAttributes attributes =
                factory.createDissectingPaneAttributes();

        // Copy the tagname and style class from the old attributes
        // Dont just throw them away.
        PaneAttributes oldattr = paneInstance.getAttributes();
        attributes.setStyles(oldattr.getStyles());

        attributes.setInclusionPath(inclusionPath);
        attributes.setDissectingPane(pane);
        attributes.setIsNextLinkFirst(pane.isNextLinkFirst());
        attributes.setLinkText(paneInstance.getLinkToText());
        attributes.setBackLinkText(paneInstance.getLinkFromText());

        // If the output is going to be dissected then write a protocol
        // specific marker in the text which the protocol parser will
        // recognise.
        LayoutModule module = context.getLayoutModule();
        module.writeOpenDissectingPane(attributes);
        super.renderPaneInstance(context, abstractPaneInstance);
        module.writeCloseDissectingPane(attributes);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 11-Jul-05	8862/3	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 22-Jun-05	8483/1	emma	VBM:2005052410 Modifications after review

 06-Jan-05	6391/5	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 21-Dec-04	6402/1	philws	VBM:2004120208 Added aligning spatial format iterator renderer

 10-Dec-04	6391/1	adrianj	VBM:2004120207 Refactoring DeviceLayoutRenderer

 ===========================================================================
*/

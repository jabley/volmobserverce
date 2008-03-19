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

package com.volantis.mcs.protocols.renderer.shared.layouts;

import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.layouts.AbstractPaneInstance;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.mcs.runtime.layouts.styling.FormatStylingEngine;
import com.volantis.styling.Styles;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.io.IOException;

public abstract class AbstractPaneRenderer extends AbstractFormatRenderer {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(
                        AbstractPaneRenderer.class);

    /**
     * Render a pane.
     *
     * @param context  the context that provides access to the state data
     *                 required for the rendering
     * @param instance The format instance for the pane to be rendered
     * @throws com.volantis.mcs.protocols.renderer.RendererException
     *          if an error occurs while rendering
     */
    public void render(
            final FormatRendererContext context,
            final FormatInstance instance)
            throws RendererException {
        try {
            if (!instance.isEmpty()) {
                Pane pane = (Pane) instance.getFormat();
                AbstractPaneInstance paneInstance = (AbstractPaneInstance) instance;

                // Initialise the attributes.
                PaneAttributes attributes = paneInstance.getAttributes();

                // Style the format.
                String styleClass = paneInstance.getStyleClass();
                FormatStylingEngine formatStylingEngine =
                        context.getFormatStylingEngine();
                Styles formatStyles = formatStylingEngine.startStyleable(
                        pane, styleClass);
                attributes.setStyles(formatStyles);
                attributes.setPane(pane);

                renderPaneInstance(context, paneInstance);

                // Finished styling the pane and its contents.
                formatStylingEngine.endStyleable(pane);
            }
        } catch (IOException e) {
            throw new RendererException(exceptionLocalizer.format(
                    "renderer-error",
                    instance.getFormat()), e);
        } catch (ProtocolException e) {
            throw new RendererException(exceptionLocalizer.format(
                    "renderer-error",
                    instance.getFormat()), e);
        }
    }

    protected abstract void renderPaneInstance(
            FormatRendererContext context,
            AbstractPaneInstance abstractPaneInstance)
            throws IOException, ProtocolException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/

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

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.AnchorAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.renderer.layouts.SegmentLinkWriter;
import com.volantis.mcs.runtime.URLConstants;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.io.IOException;

/**
 * Default implementation of {@link SegmentLinkWriter}.
 */
public class DefaultSegmentLinkWriter
        implements SegmentLinkWriter {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(
                        DefaultSegmentLinkWriter.class);

    private final MarinerPageContext pageContext;

    /**
     * Initialise.
     *
     * @param pageContext The context within which this is written.
     */
    public DefaultSegmentLinkWriter(MarinerPageContext pageContext) {
        this.pageContext = pageContext;
    }

    public void writeDefaultSegmentLink()
            throws RendererException, IOException {

        // Check whether this layout needs to provide a link back to the
        // default segment.
        MarinerURL url = pageContext.getRequestURL(false);
        String defaultSegmentURL
                = url.getParameterValue(URLConstants.SEGMENTATION_PARAMETER);
        if (defaultSegmentURL != null) {
            writeDefaultSegmentLink(pageContext, defaultSegmentURL);
        }
    }

    /**
     * Write the markup for a link back to the default segment.
     * @param pageContext The pageContext for which this markup is being created.
     * @param defaultSegmentURL The URL to the default segment.
     */
    private void writeDefaultSegmentLink(MarinerPageContext pageContext,
                                           String defaultSegmentURL)
            throws IOException, RendererException {

        AnchorAttributes a = new AnchorAttributes();

        a.setHref(defaultSegmentURL);

        if (pageContext.getPageTagId() != null) {
            a.setId(pageContext.getPageTagId());
        }

        VolantisProtocol protocol = pageContext.getProtocol();
        try {
            protocol.writeDefaultSegmentLink(a);
        } catch (ProtocolException e) {
            throw new RendererException(
                        exceptionLocalizer.format(
                                    "default-segment-link-rendering-error"),
                        e);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 ===========================================================================
*/

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

import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.layouts.Segment;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.SegmentAttributes;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.layouts.LayoutModule;
import com.volantis.mcs.protocols.layouts.SegmentInstance;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;

/**
 * A format renderer that is used to render segments.
 */
public class SegmentRenderer extends AbstractFormatRenderer {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(SegmentRenderer.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(
                        SegmentRenderer.class);

    // javadoc inherited
    public void render(final FormatRendererContext context, final FormatInstance instance)
            throws RendererException {
        try {
            if (!instance.isEmpty()) {
                Segment segment = (Segment)instance.getFormat();
                SegmentInstance segmentInstance = (SegmentInstance) instance;

                SegmentAttributes attributes = segmentInstance.getAttributes();

                if (logger.isDebugEnabled()) {
                    logger.debug("Segment.writeOutput() for "
                                 + attributes.getName());
                }

                String value;
                int ivalue;

                value = (String) segment.getAttribute(
                        FormatConstants.BORDER_COLOUR_ATTRIBUTE);
                attributes.setBorderColor(value);

                value = (String) segment.getAttribute(
                        FormatConstants.FRAME_BORDER_ATTRIBUTE);
                attributes.setFrameBorder("true".equalsIgnoreCase(value));

                value =
                (String)segment.getAttribute(FormatConstants.RESIZE_ATTRIBUTE);
                attributes.setResize("true".equalsIgnoreCase(value));

                value = (String) segment.getAttribute(
                        FormatConstants.SCROLLING_ATTRIBUTE);
                if (value == null ||
                        value.equals(FormatConstants.SCROLLING_VALUE_AUTOMATIC)) {
                    attributes.setScrolling(
                            SegmentAttributes.SCROLLING_AUTOMATIC);
                } else if (value.equals(FormatConstants.SCROLLING_VALUE_NO)) {
                    attributes.setScrolling(SegmentAttributes.SCROLLING_NO);
                } else if (value.equals(FormatConstants.SCROLLING_VALUE_YES)) {
                    attributes.setScrolling(SegmentAttributes.SCROLLING_YES);
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Unknown scrolling type of " + value);
                    }
                }

                value = (String) segment.getAttribute(
                        FormatConstants.MARGIN_HEIGHT_ATTRIBUTE);
                try {
                    ivalue = Integer.parseInt(value);
                } catch (NumberFormatException nfe) {
                    ivalue = 0;
                }
                attributes.setMarginHeight(ivalue);

                value = (String) segment.getAttribute(
                        FormatConstants.MARGIN_WIDTH_ATTRIBUTE);
                try {
                    ivalue = Integer.parseInt(value);
                } catch (NumberFormatException nfe) {
                    ivalue = 0;
                }
                attributes.setMarginWidth(ivalue);

                // Get the module.
                LayoutModule module = context.getLayoutModule();

                module.writeOpenSegment(attributes);
                module.writeCloseSegment(attributes);
            }
        } catch (IOException e) {
            throw new RendererException(
                    exceptionLocalizer.format("renderer-error",
                            instance.getFormat()), e);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 06-Jan-05	6391/5	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 21-Dec-04	6402/1	philws	VBM:2004120208 Added aligning spatial format iterator renderer

 10-Dec-04	6391/1	adrianj	VBM:2004120207 Refactoring DeviceLayoutRenderer

 ===========================================================================
*/

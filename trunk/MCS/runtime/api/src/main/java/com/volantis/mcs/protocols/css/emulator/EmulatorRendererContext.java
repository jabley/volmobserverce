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
 * $Header: /src/voyager/com/volantis/mcs/protocols/css/emulator/EmulatorRendererContext.java,v 1.1 2002/06/29 01:04:52 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Apr-02    Adrian          VBM:2002040808 - Created. The context in which
 *                              the rendering of style sheet and its component
 *                              parts takes place.
 * 10-May-02    Adrian          VBM:2002040808 - Do not attempt to render a
 *                              value if the StyleValue is inherited.
 * 28-Jun-02    Paul            VBM:2002051302 - Moved from css.emulator
 *                              package as it is specific to runtime. Also,
 *                              made it extend the RuntimeCSSRendererContext
 *                              to properly handle resolution of components.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.css.emulator;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.css.renderer.StyleSheetRenderer;
import com.volantis.mcs.css.version.CSSVersion;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.css.renderer.RuntimeRendererContext;
import com.volantis.mcs.themes.Selector;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.utilities.ReusableStringBuffer;
import com.volantis.mcs.utilities.ReusableStringWriter;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;

/**
 * The context in which the rendering of style sheet and its component
 * parts takes place.
 */
public class EmulatorRendererContext
        extends RuntimeRendererContext {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(EmulatorRendererContext.class);

    /**
     * Construct a RendererContext that will write the renderer style sheet
     * to a given Writer utilizing the given StyleSheetRenderer.
     *
     * @param requestContext
     * @param renderer       the StyleSheetRenderer
     * @param cssVersion
     */
    public EmulatorRendererContext(
            MarinerRequestContext requestContext,
            StyleSheetRenderer renderer,
            VolantisProtocol protocol,
            CSSVersion cssVersion) {
        super(null, renderer, protocol, cssVersion);

        // Wrap the ValuesRenderer in an EmulatorValuesRenderer which will render
        // colors and uris differently to css.
        this.valuesRenderer = new EmulatorValuesRenderer(this.valuesRenderer);

    }

    /**
     * Write out the contents of the writer that is used to write values into
     * the RendererContext to the writer that the RendererContext writes out
     * to(i.e. write the proxy writer to the real writer).
     *
     * @throws IOException if a problem is encountered in the write operation
     */
    public void flushStyleSheet() throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Render a Selector.
     *
     * @param selector the Selector to render
     * @throws IOException if there is a problem rendering to the writer.
     */
    public void renderSelector(Selector selector)
            throws IOException {
        throw new UnsupportedOperationException();
    }

    public ReusableStringBuffer getRenderValue(StyleValue value) {
        if (value == null ||
                value.getStyleValueType() == StyleValueType.INHERIT) {
            return null;
        }

        try {
            ReusableStringWriter stringWriter =
                    (ReusableStringWriter) getWriter();
            ReusableStringBuffer buffer = stringWriter.getBuffer();
            buffer.setLength(0);
            renderValue(value);
            if (buffer.length() > 0) {
                return buffer;
            } else {
                return null;
            }
        }
        catch (IOException ioe) {
            logger.warn("style-value-retrieval-error");
            return null;
        }
    }

    public void renderValue(
            StyleValue value, Element element,
            String attributeName) {
        ReusableStringBuffer buffer = getRenderValue(value);
        if (buffer != null) {
            element.setAttribute(attributeName, buffer.toString());
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 22-Aug-05	9324/2	ianw	VBM:2005080202 Move validation for WapCSS into styling

 22-Aug-05	9184/1	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 19-Nov-04	5733/3	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/

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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.xdime.xhtml2.meta.datatype;

import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.xhtml2.MetaInformationElement;
import com.volantis.mcs.xdime.xhtml2.ElementUtils;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.protocols.OutputBuffer;

/**
 * Abstract MetaContentProcessor that either uses the value of the content
 * attribute or store the element content. The output buffer that contains the
 * meta content can be returned by calling the {@link #getBodyContentBuffer()}.
 */
public abstract class AbstractMetaContentProcessor
        implements MetaContentProcessor {

    /**
     * Name of the content attribute.
     */
    private static final String ATTR_NAME_CONTENT = "content";

    /**
     * Output buffer for the content.
     */
    private OutputBuffer bodyContentBuffer;

    /**
     * True, iff the content buffer was pushed onto the output buffer stack to
     * collect the content of the meta element.
     */
    private boolean outputBufferPushed;

    // javadoc inherited
    public void startProcess(final XDIMEContextInternal context,
                             final MetaInformationElement metaElement,
                             final XDIMEAttributes attributes) {
        bodyContentBuffer = metaElement.createOutputBuffer(context);
        final String content = ElementUtils.getAttributeValue(context,
            attributes, XDIMESchemata.XHTML2_NAMESPACE, ATTR_NAME_CONTENT);
        if (content != null && content.length() > 0) {
            bodyContentBuffer.writeText(content);
            outputBufferPushed = false;
        } else {
            final MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(
                    context.getInitialRequestContext());
            // add the output buffer to the page context's output buffer stack
            pageContext.pushOutputBuffer(bodyContentBuffer);
            outputBufferPushed = true;
        }
    }

    // javadoc inherited
    public void endProcess(final XDIMEContextInternal context,
                           final MetaInformationElement metaElement)
            throws XDIMEException {

        if (outputBufferPushed) {
            final MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(
                    context.getInitialRequestContext());
            pageContext.popOutputBuffer(bodyContentBuffer);
        }
    }

    /**
     * Returns the output buffer that contains the content. This method should
     * only be called after {@link #endProcess} was executed.
     *
     * @return the buffer that contains the content, never returns null
     */
    protected OutputBuffer getBodyContentBuffer() {
        return bodyContentBuffer;
    }
}

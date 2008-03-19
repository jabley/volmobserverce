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
package com.volantis.mcs.xdime;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.xdime.xforms.XFGroupElementImpl;
import com.volantis.mcs.xdime.xforms.model.XFormModel;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Concrete implementation of {@link DataHandlingStrategy} which stores any
 * label data (character data or markup) encountered while processing.
 * <p/>
 * If the label applies to a group, the current group (from the context) is
 * updated with the label data when {@link #stopHandlingData} is called, and
 * the buffer is cleared.
 * <p/>
 * If the label does not apply to a group, the label data remains accessible
 * via {@link #getCharacterData()}.
 */
public class LabelStrategy implements DataHandlingStrategy {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(LabelStrategy.class);

    /**
     * The output buffer to which content will be directed.
     */
    private DOMOutputBuffer content = null;

    // Javadoc inherited.
    public void handleData(XDIMEContextInternal context) {
        MarinerPageContext pageContext =
                 ContextInternals.getMarinerPageContext(
                         context.getInitialRequestContext());
        // Create new output buffer to capture the content of the label element.
        content = (DOMOutputBuffer) pageContext.getProtocol().
            getOutputBufferFactory().createOutputBuffer();
        pageContext.pushOutputBuffer(content);
    }

    // Javadoc inherited.
    public String getCharacterData() {
        return content.getPCDATAValue();
    }

    // Javadoc inherited.
    public void stopHandlingData(XDIMEContextInternal context) {
        MarinerPageContext pageContext =
                 ContextInternals.getMarinerPageContext(
                         context.getInitialRequestContext());
        pageContext.popOutputBuffer(content);

        // by this point the current element is this element's parent...
        StylableXDIMEElement labelParent =
                (StylableXDIMEElement)context.getCurrentElement();

        if (labelParent instanceof XFGroupElementImpl) {
            // this is the label for a group - needs to be added the model even
            // if the model is inactive at the moment.
            XFormModel model = context.getXFormBuilder().getCurrentModel();
            if (model != null) {
                final String labelText = getCharacterData();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("XForms group label: " + labelText);
                }
                model.setGroupLabel(labelText);
                content.clear();
            }
        }
    }
}

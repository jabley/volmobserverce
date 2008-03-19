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
package com.volantis.mcs.xdime.xhtml2;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.xdime.UnstyledStrategy;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextImpl;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Stack;

/**
 * XHTML V2 Title element object.
 */
public class TitleElement extends XHTML2Element {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(TitleElement.class);

    public TitleElement(XDIMEContextInternal context) {
        super(XHTML2Elements.TITLE, UnstyledStrategy.STRATEGY, context);
    }

    /**
     * Buffer to hold the content seen inside this object.
     */
    private DOMOutputBuffer bodyContentBuffer;

    // Javadoc inherited
    protected XDIMEResult callOpenOnProtocol(
            XDIMEContextInternal context, XDIMEAttributes attributes)
        throws XDIMEException {

        // Create an output buffer for the content of the object element.
        // This will enable us to grab it easily when the element ends and
        // either throw it away if the src attribute can be used or use it
        // instead if not.
        bodyContentBuffer = (DOMOutputBuffer) createOutputBuffer(context);
        getPageContext(context).pushOutputBuffer(bodyContentBuffer);
        return XDIMEResult.PROCESS_ELEMENT_BODY;        
    }

    // Javadoc inherited
    protected void callCloseOnProtocol(XDIMEContextInternal context) {

        final MarinerPageContext pageContext = getPageContext(context);
        pageContext.popOutputBuffer(bodyContentBuffer);

        // We store the canvas initialisation data on the grandparent html
        // element. So get a reference to the grandparent html element.
        Stack elementStack = ((XDIMEContextImpl)context).getStack();
        int stackSize = elementStack.size();
        if (stackSize < 2) {
            throw new IllegalStateException("No html element found");
        }
        Object grandparent = elementStack.get(elementStack.size() - 2);
        if (!(grandparent instanceof HtmlElement)) {
            throw new IllegalStateException("No html element found");
        }
        HtmlElement htmlElement = (HtmlElement) grandparent;

        // Get the textual title. Should be easy as title is PCDATA.
        String title = bodyContentBuffer.getPCDATAValue();
        if (title != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Title: " + title);
            }
            // Add the title into the html element to be processed later.
            htmlElement.setTitle(title);
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05      10523/7 ianw    VBM:2005112406 Make XDIMECP Meta tag noop for now until we process it properly

 01-Dec-05      10523/1 ianw    VBM:2005112406 Fix uo XDIMCP Title element

 01-Dec-05      10514/1 ianw    VBM:2005112406 Fixed XDIMECP Title elemement

 02-Dec-05      10514/3 ianw    VBM:2005112406 Make XDIMECP Meta tag noop for now until we process it properly

 01-Dec-05      10514/1 ianw    VBM:2005112406 Fixed XDIMECP Title elemement

 12-Oct-05      9673/5  pduffin VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05      9673/3  pduffin VBM:2005092906 Improved validation and fixed layout formatting

 22-Sep-05      9128/5  pabbott VBM:2005071114 Review feedback for XHTML2 elements

 21-Sep-05      9128/3  pabbott VBM:2005071114 Review feedback for XHTML2 elements

 20-Sep-05      9128/1  pabbott VBM:2005071114 Add XHTML 2 elements

 ===========================================================================
*/

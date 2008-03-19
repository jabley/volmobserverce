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

import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.xdime.StylableXDIMEElement;
import com.volantis.mcs.xdime.StyledStrategy;
import com.volantis.mcs.xdime.StylingStrategy;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEElementInternal;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Abstract superclass for all XHTML2 element classes.
 */
public abstract class XHTML2Element extends StylableXDIMEElement {

    /**
     * Initialize a new instance.
     */
    protected XHTML2Element(ElementType type, XDIMEContextInternal context) {
        this(type, StyledStrategy.STRATEGY, context);
    }

    /**
     * Initialize a new instance.
     */
    protected XHTML2Element(
            ElementType type,
            StylingStrategy stylingStrategy,
            XDIMEContextInternal context) {

        super(type, stylingStrategy, context);
    }

    /**
     * Generate a string which contains the tag name if known. If
     * the tag name is not known then put the class name in the string.
     *
     * @return string representation of this element
     */
    public String toString() {
        String text = "XHTML2 element ";

        String tag = getTagName();

        if (tag != null) {
            text = text + "tag='" + tag + "'";
        } else {
            text = text + "tag name not known, class=" + getClass();
        }

        MCSAttributes attributes = getProtocolAttributes();
        if (attributes != null) {
            String id = attributes.getId();
            if (id != null) {
                text = text + " id="+id;
            }
            String title = attributes.getTitle();
            if (title != null) {
                text = text + " title="+title;
            }
            LinkAssetReference href = attributes.getHref();
            if (href != null) {
                text = text + " href="+href.getURL();
            }
        }
        return text;
    }

    /**
     * Utility to dump the current element stack for debugging.
     *
     * <p>Stack is dumped from top down.</p>
     *
     * @param log logger to which to dump the stack
     */
    protected void dumpElementStack(LogDispatcher log) {
        log.error("element-stack-dump");
        dumpElementStack(log, this);
    }

    /**
     * Dump the stack above the specified element then the element.
     *
     * @param log     The dispatcher to which the log messages are sent.
     * @param element The element to dump.
     */
    private void dumpElementStack(
            LogDispatcher log, XDIMEElementInternal element) {
        dumpElementStack(log, element.getParent());
        log.error("element-line", element);
    }

    protected OutputBuffer createOutputBuffer(XDIMEContextInternal context) {
        return getProtocol(context).
            getOutputBufferFactory().createOutputBuffer();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/8	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 03-Oct-05	9673/3	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 30-Sep-05	9562/5	pabbott	VBM:2005092011 Add XHTML2 Object element

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 22-Sep-05	9128/6	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 21-Sep-05	9128/4	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 20-Sep-05	9128/2	pabbott	VBM:2005071114 Add XHTML 2 elements

 09-Sep-05	9415/1	emma	VBM:2005072710 Add mappings for DISelect Set XPath Functions

 ===========================================================================
*/

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

package com.volantis.mcs.xdime.widgets;

import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.protocols.widgets.attributes.LoadAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEElementInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;

/**
 * Class for XDIME2 widget:load element, used to load ajax content
 */
public class LoadElement extends WidgetElement {

    public LoadElement(XDIMEContextInternal context) {
        super(WidgetElements.LOAD, context);
        protocolAttributes = new LoadAttributes();
    }

    public XDIMEResult callOpenOnProtocol(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {

        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    public void callCloseOnProtocol(XDIMEContextInternal context)
            throws XDIMEException {
        // Parent is a TabElement
        if (parent instanceof Loadable) {
            ((Loadable) parent).setLoadAttributes(getLoadAttributes());
        }
        // Parent is a DetailElement, grand parent is FoldingItemElement.
        XDIMEElementInternal grandParent = parent.getParent();
        if (grandParent instanceof Loadable) {
            ((Loadable) grandParent).setLoadAttributes(getLoadAttributes());
        }
    }

    public LoadAttributes getLoadAttributes() {
        return (LoadAttributes) protocolAttributes;
    }

    /**
     * src - specifies of content to load
     * when - specifies when the content should be load
     */
    protected void initialiseElementSpecificAttributes(
            XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {
        ((LoadAttributes) protocolAttributes).setSrc(getSrcAttributeValue(context, attributes));
        ((LoadAttributes) protocolAttributes).setWhen(attributes.getValue("",
                "when"));
    }

    /**
     * Retrieves the value of the 'src' attribute and processes it to be
     * passed to protocol attributes.
     *
     * @param context The XDIME context
     * @param attributes The XDIME attributes to read the 'src' attribute value
     * @return The attribute value ready to be passed to protocol attributes.
     * @throws XDIMEException
     */
    protected String getSrcAttributeValue(XDIMEContextInternal context, XDIMEAttributes attributes) throws XDIMEException {
        String src = attributes.getValue("","src");

        if (src != null) {
            src = rewriteURLWithPageURLRewriter(context, src, PageURLType.WIDGET);
        }
        return src;
    }
}

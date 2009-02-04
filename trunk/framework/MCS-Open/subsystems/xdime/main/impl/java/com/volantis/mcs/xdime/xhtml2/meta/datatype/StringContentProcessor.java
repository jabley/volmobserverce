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

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.mcs.xdime.xhtml2.MetaInformationElement;
import com.volantis.mcs.xdime.xhtml2.ElementUtils;
import com.volantis.synergetics.localization.MessageLocalizer;
import com.volantis.xml.namespace.ImmutableExpandedName;

/**
 * Content processor for Text contents. Stores the result in a String.
 */
public class StringContentProcessor extends AbstractMetaContentProcessor {

    /**
     * Used to obtain localized messages
     */
    private static final MessageLocalizer LOCALIZER =
        LocalizationFactory.createMessageLocalizer(
            StringContentProcessor.class);

    public static final ImmutableExpandedName EXPANDED_NAME_STRING =
        new ImmutableExpandedName(NAMESPACE_XML_SCHEMA, "string");

    public static final DataType STRING_TYPE = new DataType(EXPANDED_NAME_STRING);

    // javadoc inherited
    public DataType getType() {
        return STRING_TYPE;
    }

    // javadoc inherited
    public void endProcess(final XDIMEContextInternal context,
                           final MetaInformationElement metaElement)
            throws XDIMEException {

        super.endProcess(context, metaElement);

        // check the content
        final DOMOutputBuffer contentBuffer =
            ((DOMOutputBuffer) getBodyContentBuffer());
        for (Node node = contentBuffer.getRoot().getHead(); node != null;
             node = node.getNext()) {

            if (!(node instanceof Text)) {
                throw new XDIMEException(LOCALIZER.format(
                    "invalid-meta-content-type",
                    new Object[]{"string", node.getClass().getName()}));
            }
        }
    }

    // javadoc inherited
    public Object getResult() throws XDIMEException {

        // append the content
        final StringBuffer buffer = new StringBuffer();
        final DOMOutputBuffer contentBuffer =
            ((DOMOutputBuffer) getBodyContentBuffer());
        for (Node node = contentBuffer.getRoot().getHead(); node != null;
             node = node.getNext()) {

            final Text text = (Text) node;
            buffer.append(text.getContents(), 0, text.getLength());
        }
        return buffer.toString();
    }
}

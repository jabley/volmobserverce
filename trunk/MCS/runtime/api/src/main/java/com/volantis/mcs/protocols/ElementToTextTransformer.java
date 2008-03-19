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

package com.volantis.mcs.protocols;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.output.DocumentOutputter;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Transforms all marked element nodes to the text nodes. The content of the
 * text node will euqal to the serialized representation of an element's XML.
 */
public class ElementToTextTransformer extends DOMVisitorBasedTransformer {


    protected TransformingVisitor getDOMVisitor(final DOMProtocol protocol) {

        return new AbstractTransformingVisitor() {

            public void visit(Element element) {

                // Check, if the element is to be textualized.
                if (protocol.getTransformToTextMarker(element)) {
                    // Remove the attribute which indicated that element
                    // needed to be textualized.
                    protocol.setTransformToTextMarker(element, false);

                    // Serialize the element to the string.
                    StringWriter writer = new StringWriter();

                    DocumentOutputter outputter = protocol.createDocumentOutputter(writer);

                    try {
                        outputter.output(element);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    // Create text node with serialized string.
                    Text text = protocol.getDOMFactory().createText();

                    text.append(writer.toString());

                    // Replace the element with its textualized form.
                    text.replace(element);
                } else {
                    element.forEachChild(this);
                }
            }
        };
    }
}

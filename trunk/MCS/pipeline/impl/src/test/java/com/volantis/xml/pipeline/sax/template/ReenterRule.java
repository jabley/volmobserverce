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

package com.volantis.xml.pipeline.sax.template;

import com.volantis.xml.pipeline.sax.dynamic.rules.DynamicElementRuleImpl;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.namespace.ExpandedName;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * A rule that generates markup that will cause this rule to be reentered if
 * there is a dynamic element process downstream.
 *
 * <p>This keeps count of the number of times it has been evaluated by adding
 * 1 to a use count attribute.</p>
 */
public class ReenterRule
        extends DynamicElementRuleImpl {


    public Object startElement(
            DynamicProcess dynamicProcess, ExpandedName element,
            Attributes attributes) throws SAXException {

        String countValue = attributes.getValue("count");
        int count = 0;
        if (countValue != null) {
            count = Integer.parseInt(countValue);
        }
        count += 1;

        AttributesImpl next = new AttributesImpl();
        next.addAttribute("", "count", "count", "CDATA",
                String.valueOf(count));

        // Get the process after the dynamic process so it does not
        // reenter itself otherwise that would cause an infinite loop.
        XMLProcess process = getTargetProcess(dynamicProcess);
        process.startElement(element.getNamespaceURI(),
                element.getLocalName(), "", next);

        return null;
    }

    public void endElement(
            DynamicProcess dynamicProcess, ExpandedName element, Object object)
            throws SAXException {

        // Get the process after the dynamic process so it does not
        // reenter itself otherwise that would cause an infinite loop.
        XMLProcess process = getTargetProcess(dynamicProcess);
        process.endElement(element.getNamespaceURI(),
                element.getLocalName(), "");
    }
}

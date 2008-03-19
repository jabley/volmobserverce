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
 * A rule that alternates between generating simple markup or complex.
 */
public class AlternateComplexityRule
        extends DynamicElementRuleImpl {

    /**
     * TESTING ONLY.
     *
     * <p>DO NOT COPY THIS PRACTICE OF HAVING STATE IN YOUR RULE, IT WILL LEAD
     * TO THREADING ISSUES IN PRODUCTION ENVIRONMENTS. IF YOU NEED STATE IT
     * SHOULD BE IN THE PIPELINE PROCESS.</p>
     */
    private int counter;
    private static final char[] SIMPLE = "simple".toCharArray();
    private static final int SIMPLE_LENGTH = "simple".length();

    public Object startElement(
            DynamicProcess dynamicProcess, ExpandedName element,
            Attributes attributes) throws SAXException {

        XMLProcess process = getTargetProcess(dynamicProcess);
        if ((counter & 1) == 0) {
            process.characters(SIMPLE, 0, SIMPLE_LENGTH);
        } else {
            process.startElement(element.getNamespaceURI(), "complex", "",
                    attributes);
            process.endElement(element.getNamespaceURI(), "complex", "");
        }

        counter += 1;

        return null;
    }

    public void endElement(
            DynamicProcess dynamicProcess, ExpandedName element, Object object)
            throws SAXException {
    }
}

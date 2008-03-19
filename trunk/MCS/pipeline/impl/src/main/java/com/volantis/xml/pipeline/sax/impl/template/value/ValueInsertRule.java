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

package com.volantis.xml.pipeline.sax.impl.template.value;

import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.impl.template.AbstractTemplateRule;
import com.volantis.xml.pipeline.sax.impl.template.TValue;
import com.volantis.xml.pipeline.sax.impl.template.TemplateModel;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Rule for the template:value element.
 */
public class ValueInsertRule
        extends AbstractTemplateRule {

    // Javadoc inherited.
    public Object startElement(
            DynamicProcess dynamicProcess, ExpandedName element,
            Attributes attributes) throws SAXException {

        TemplateModel model = getModel(dynamicProcess);

        String ref = attributes.getValue("ref");
        if (ref == null) {
            throw forwardFatalError(dynamicProcess,
                    "A ref must be provided for a value");
        }

        TValue value = model.getParameterBlock().query(ref);
        if (value == null) {
            throw forwardFatalError(dynamicProcess,
                    "Parameter " + ref + " not defined in the current scope");
        }

        value.insert(dynamicProcess.getPipeline(), ref);

        return null;
    }

    // Javadoc inherited.
    protected void endElementImpl(
            DynamicProcess dynamicProcess, ExpandedName element, Object object)
            throws SAXException {
    }
}

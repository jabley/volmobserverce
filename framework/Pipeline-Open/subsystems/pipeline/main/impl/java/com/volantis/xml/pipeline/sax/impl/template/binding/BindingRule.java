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

package com.volantis.xml.pipeline.sax.impl.template.binding;

import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.impl.template.AbstractTemplateRule;
import com.volantis.xml.pipeline.sax.impl.template.TValue;
import com.volantis.xml.pipeline.sax.impl.template.TemplateModel;
import com.volantis.xml.pipeline.sax.impl.template.TemplateSchema;
import com.volantis.xml.pipeline.sax.impl.template.TemplateValueSetter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class BindingRule
        extends AbstractTemplateRule {

    public Object startElement(
            DynamicProcess dynamicProcess, ExpandedName element,
            Attributes attributes) throws SAXException {

        final TemplateModel model = getModel(dynamicProcess);
        model.startElement(TemplateSchema.BINDING);

        final String name = attributes.getValue("name");
        if (name == null) {
            throw forwardFatalError(dynamicProcess,
                    "A name must be provided for a binding");
        }

        String value = attributes.getValue("value");
        String ref = attributes.getValue("ref");

        TValue tValue;
        if (value != null && ref != null) {
            throw forwardFatalError(dynamicProcess,
                    "Only one of 'value' or 'ref' may be specified");
        } else if (value != null) {
            tValue = model.createSimpleValue(value);
            model.addBindingValue(name, tValue);
        } else if (ref != null) {
            tValue = model.createReferenceValue(ref);
            model.addBindingValue(name, tValue);
        } else {
            model.setValueSetter(new TemplateValueSetter() {

                public void setTemplateValue(TValue value) {
                    model.addBindingValue(name, value);
                }
            });
            tValue = null;
        }

        return null;
    }

    protected void endElementImpl(
            DynamicProcess dynamicProcess, ExpandedName element, Object object)
            throws SAXException {

        final TemplateModel model = getModel(dynamicProcess);

        model.endElement(TemplateSchema.BINDING);
    }
}

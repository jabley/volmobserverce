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

package com.volantis.xml.pipeline.sax.impl.template;

import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.impl.validation.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * The base class for those rules that simply update the template model state.
 */
public class StateUpdatingRule
        extends AbstractTemplateRule {

    /**
     * The element for which the state will be updated.
     */
    private final Element element;

    /**
     * Initialise.
     *
     * @param element The element for which the state will be updated.
     */
    protected StateUpdatingRule(Element element) {
        this.element = element;
    }

    // Javadoc inherited.
    public Object startElement(
            DynamicProcess dynamicProcess, ExpandedName element,
            Attributes attributes) throws SAXException {

        TemplateModel model = getModel(dynamicProcess);
        model.startElement(this.element);
        return model;
    }

    // Javadoc inherited.
    protected void endElementImpl(
            DynamicProcess dynamicProcess, ExpandedName element, Object object)
            throws SAXException {

        TemplateModel model = (TemplateModel) object;
        model.endElement(this.element);
    }
}

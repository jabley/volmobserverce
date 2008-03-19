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

package com.volantis.xml.pipeline.sax.impl.template.parameter;

import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.EndElementAction;
import com.volantis.xml.pipeline.sax.impl.template.TemplateModel;
import com.volantis.xml.pipeline.sax.impl.template.TemplateSchema;
import org.xml.sax.SAXException;

/**
 * Action that is always performed at the end of the template:parameter element.
 */
class EndParameterAction
        implements EndElementAction {

    /**
     * The template model.
     */
    protected final TemplateModel model;

    /**
     * Initialise.
     *
     * @param model The template model.
     */
    public EndParameterAction(TemplateModel model) {
        this.model = model;
    }

    // Javadoc inherited.
    public void doAction(DynamicProcess dynamicProcess)
            throws SAXException {
        model.endElement(TemplateSchema.PARAMETER);
    }
}

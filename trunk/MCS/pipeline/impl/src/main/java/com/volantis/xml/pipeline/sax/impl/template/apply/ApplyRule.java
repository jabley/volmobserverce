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

package com.volantis.xml.pipeline.sax.impl.template.apply;

import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.EndElementAction;
import com.volantis.xml.pipeline.sax.dynamic.rules.DynamicElementRuleImpl;
import com.volantis.xml.pipeline.sax.impl.template.TemplateModel;
import com.volantis.xml.pipeline.sax.impl.template.TemplateProcess;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Rule for the template:apply element.
 */
public class ApplyRule
        extends DynamicElementRuleImpl {

    // Javadoc inherited.
    public Object startElement(
            DynamicProcess dynamicProcess, ExpandedName element,
            Attributes attributes) throws SAXException {

        XMLPipelineContext pipelineContext =
                dynamicProcess.getPipelineContext();

        // Get the model for containing template for bindings that reference
        // values in containing templates, may be null.
        TemplateModel containing = (TemplateModel) pipelineContext.findObject(
                TemplateModel.class);

        // Create the template process and add it to the pipeline.
        TemplateProcess process = new TemplateProcess(containing);
        dynamicProcess.addProcess(process);

        // Push the template model onto the stack so it can be picked up by
        // nested elements.
        TemplateModel model = process;
        pipelineContext.pushObject(model, false);

        // Start the template.
        model.startTemplate();

        // The presence or absence of the href attribute determines the gross
        // structure of the template:apply.
        String href = attributes.getValue("href");
        EndElementAction action;
        if (href == null) {

            // Template must be inline so a template:bindings element is
            // expected.
            action = new EndTemplateAction(model, process);
        } else {

            // Pretend that we have received a template:bindings element.
            model.startBindings();

            // Template definition needs including at the end
            // of the operation.
            action = new IncludeDefinitionAction(model, process, href);
        }

        return action;
    }

    // Javadoc inherited.
    public void endElement(
            DynamicProcess dynamicProcess, ExpandedName element, Object object)
            throws SAXException {

        EndElementAction action = (EndElementAction) object;
        action.doAction(dynamicProcess);
    }
}

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

import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.EndElementAction;
import com.volantis.xml.pipeline.sax.impl.template.TemplateModel;
import org.xml.sax.SAXException;

/**
 * The action that is performed at the end of the template.
 */
public class EndTemplateAction
        implements EndElementAction {

    /**
     * The model for the template.
     */
    protected final TemplateModel model;

    /**
     * The process that needs removing.
     */
    private final XMLProcess process;

    /**
     * Initialise.
     *
     * @param model   The model for the template.
     * @param process The process that needs removing.
     */
    public EndTemplateAction(TemplateModel model, XMLProcess process) {
        this.model = model;
        this.process = process;
    }

    // Javadoc inherited.
    public void doAction(DynamicProcess dynamicProcess)
            throws SAXException {

        XMLPipelineContext context = dynamicProcess.getPipelineContext();
        if (!context.inErrorRecoveryMode()) {
            model.endTemplate();
        }

        // Remove the process.
        dynamicProcess.removeProcess(process);

        if (!context.inErrorRecoveryMode()) {
            Object popped = context.popObject();
            if (popped != model) {
                XMLPipelineException error = new XMLPipelineException(
                        "Expected to remove object " + model +
                        "from the pipeline, but actually removed " + popped,
                        context.getCurrentLocator());
                // send the error down the pipeline
                dynamicProcess.fatalError(error);
            }
        }
    }
}

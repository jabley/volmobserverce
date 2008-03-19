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

import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.drivers.uri.Fetcher;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.impl.template.TemplateModel;
import org.xml.sax.SAXException;

/**
 * The action that is performed at the end of the template where the definition
 * is referenced using the href attribute.
 */
public class IncludeDefinitionAction
        extends EndTemplateAction {

    /**
     * The reference to the template definition.
     */
    private final String href;

    /**
     * Initialise.
     *
     * @param model   The model for the template.
     * @param process The process that needs removing.
     * @param href    The reference to the template definition.
     */
    public IncludeDefinitionAction(
            TemplateModel model, XMLProcess process, String href) {
        super(model, process);

        this.href = href;
    }

    // Javadoc inherited.
    public void doAction(DynamicProcess dynamicProcess)
            throws SAXException {

        if (!dynamicProcess.getPipelineContext().inErrorRecoveryMode()) {

            // The bindings have finished.
            model.endBindings();

            XMLPipeline pipeline = dynamicProcess.getPipeline();

            // In order to get the required template definition to be processed
            // we need to invoke a URI inclusion based on the given href.
            Fetcher fetcher = new Fetcher(pipeline);
            fetcher.setHref(href);

            fetcher.doInclude();
        }

        super.doAction(dynamicProcess);
    }
}

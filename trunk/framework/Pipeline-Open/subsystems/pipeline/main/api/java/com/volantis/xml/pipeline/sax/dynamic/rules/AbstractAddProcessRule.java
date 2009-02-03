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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.dynamic.rules;

import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * An abstract class that makes it easy to create a rule to add a process to
 * the pipeline.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public abstract class AbstractAddProcessRule
        extends DynamicElementRuleImpl {

    /**
     * Called to create a process to add to the pipeline.
     *
     * @param dynamicProcess The process that invoked this rule.
     * @param elementName    The name of the element for which the rule was
     *                       invoked.
     * @param attributes     The attributes of the element for which the rule
     *                       was invoked.
     * @return The XMLProcess to add, or null if no process should be added.
     * @throws SAXException If there was a problem creating the process.
     */
    protected abstract XMLProcess createProcess(DynamicProcess dynamicProcess,
                                                ExpandedName elementName,
                                                Attributes attributes)
            throws SAXException;

    /**
     * Called immediately after the process has been added to the pipeline.
     *
     * @param dynamicProcess The process that invoked this rule.
     * @param elementName    The name of the element for which the rule was
     *                       invoked.
     * @param attributes     The attributes of the element for which the rule
     *                       was invoked.
     * @param process        The process that was returned from
     *                       {@link #createProcess} .
     */
    protected void postAddProcess(
            DynamicProcess dynamicProcess,
            ExpandedName elementName,
            Attributes attributes,
            XMLProcess process)
        throws SAXException {
    }

    /**
     * Called immediately before the process will be removed from the pipeline.
     *
     * @param dynamicProcess The process that invoked this rule.
     * @param elementName    The name of the element for which the rule was
     *                       invoked.
     * @param process        The process that was returned from
     *                       {@link #createProcess} .
     */
    protected void preRemoveProcess(
            DynamicProcess dynamicProcess,
            ExpandedName elementName,
            XMLProcess process)
            throws SAXException {
    }

    /**
     * @TODO ALTHOUGH IN THE PUBLIC API THIS METHOD HAS NEVER BEEN INCLUDED
     * AND AS SUCH HAS NOT BEEN IMPLEMENTED.  THIS SHOULD BE ADDRESSED AS
     * A PART OF VBM:2004012006
     *
     * Release the process that has been removed from the pipeline.
     *
     * <p>This is called after the process has been removed from the pipeline
     * and so must not use methods that rely on the process being in the
     * pipeline.
     *
     * @param dynamicProcess The process that invoked this rule.
     * @param elementName The name of the element that the rule was invoked for.
     * @param process The process that was removed.
     * @throws SAXException If there was a problem releasing the process.
     * @deprecated will be removed.
     */
    protected void releaseProcess(DynamicProcess dynamicProcess,
                                  ExpandedName elementName,
                                  XMLProcess process)
            throws SAXException {
        throw new UnsupportedOperationException(
                "This method is currently unsupported.");
    }

    /**
     * Creates and adds a process to the pipeline.
     * <p>Process creation is delegated to the {@link #createProcess} method.
     * </p>
     * @param dynamicProcess The dynamic process that invoked this rule.
     * @param element The name of the element that triggered the rule to
     * be invoked.
     * @param attributes The attributes associated with the above element.
     * @return The {@link XMLProcess} that was added to the pipeline if one
     * was successfully created.  Otherwise null.
     * @throws SAXException If there was a problem creating the process, or
     * adding it to the pipeline.
     */
    public Object startElement(DynamicProcess dynamicProcess,
                               ExpandedName element,
                               Attributes attributes)
            throws SAXException {

        // factor the XMLProcess that should be added to the pipeline
        XMLProcess process = createProcess(dynamicProcess,
                                           element,
                                           attributes);
        if (process != null) {
            // add the process to the pipeline
            dynamicProcess.addProcess(process);
            postAddProcess(dynamicProcess, element, attributes, process);
        }
        // return the process that was added
        return process;
    }

    /**
     * Removes a process from the pipeline and releases its resources.
     * @param dynamicProcess The dynamic process that invoked this rule.
     * @param element The name of the element that triggered the rule to
     * be invoked.
     * @param object The object returned by the matching call to
     * {@link #startElement}.
     * @throws SAXException If there was a problem removing the process from the
     * pipeline, or releasing its resources.
     */
    public void endElement(DynamicProcess dynamicProcess,
                           ExpandedName element,
                           Object object)
            throws SAXException {
        XMLProcess process = (XMLProcess)object;

        preRemoveProcess(dynamicProcess, element, process);


        // Remove the process that was added via the startElement
        // method of this rule.
        if (null != process) {
            // This checks that the correct process was removed
            dynamicProcess.removeProcess(process);
        } else {
            // If the process is null and we are in Error Recovery Mode,
            // it's a correct situation - it means we are recovering from an
            // error in our own start element
            XMLPipelineContext context = dynamicProcess.getPipelineContext();
            if (context.inErrorRecoveryMode()) {
                dynamicProcess.removeProcess();
            }
        }
    }
}

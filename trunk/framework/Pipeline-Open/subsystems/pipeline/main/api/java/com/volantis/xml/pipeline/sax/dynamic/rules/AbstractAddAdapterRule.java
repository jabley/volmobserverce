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

import com.volantis.xml.pipeline.sax.adapter.AdapterProcess;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.XMLPipelineProcessImpl;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.namespace.NamespacePrefixTracker;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Abstract implementation of the {@link DynamicElementRule} interface
 * that adds {@link AdapterProcess} instances to the pipeline
 */
public abstract class AbstractAddAdapterRule
        implements DynamicElementRule {

    /**
     * Factory method for creating the <code>AdapterProcess</code> that
     * this rule wishes to add to the pipeline.
     * @param dynamicProcess A DynamicProcess implementation
     * @return An AdapterProcess of null if one should not be added.
     */
    public abstract AdapterProcess createAdapterProcess(
            DynamicProcess dynamicProcess);

    // javadoc inherited
    public Object startElement(DynamicProcess dynamicProcess,
                               ExpandedName element,
                               Attributes attributes)
            throws SAXException {
        // factor the adapter that should be added to the pipeline
        AdapterProcess process = createAdapterProcess(dynamicProcess);
        if (process != null) {
            XMLPipeline pipeline = dynamicProcess.getPipeline();
            if (pipeline instanceof XMLPipelineProcessImpl) {
                // pass the details of the element to the process
                process.setElementDetails(element.getNamespaceURI(),
                                          element.getLocalName(),
                                          calculateQName(dynamicProcess,
                                                         element));

                // add the AdapterProcess to the pipeline.
                ((XMLPipelineProcessImpl)pipeline).addHeadProcess(process,
                                                                  attributes);

            } else {
                // Need the pipeline to be an instance of
                // XMLPipelineProcessImpl but wasn't. Deliver a fatal error
                // down the pipeline.
                XMLPipelineContext context = pipeline.getPipelineContext();
                XMLPipelineException error = new XMLPipelineException(
                        "Expected pipeline to be instance of " +
                        "XMLPipelineProcessImpl, but was " +
                        pipeline.getClass(),
                        context.getCurrentLocator());
                // send the error down the pipeline
                pipeline.getPipelineProcess().fatalError(error);
            }
        }
        // return the process that was added
        return process;
    }

    // javadoc inherited
    public void endElement(DynamicProcess dynamicProcess,
                           ExpandedName element,
                           Object object)
            throws SAXException {
        XMLProcess process = (XMLProcess)object;
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

    /**
     * Method that returns a String representation of a qname for
     * a given expanded name
     * @param dynamicProcess the DynamicProcess
     * @param expandedName the expanded name
     * @return the qname as a string
     */
    private String calculateQName(DynamicProcess dynamicProcess,
                                  ExpandedName expandedName) {
        // retrieve the PipelineContext
        XMLPipelineContext context =
                dynamicProcess.getPipeline().getPipelineContext();
        // get hold of the NamespacePrefixTracker so that we can
        // access the registered prefixes
        NamespacePrefixTracker prefixTracker =
                context.getNamespacePrefixTracker();
        // obtain the prefix for the specified namespace URI
        String prefix = prefixTracker.getNamespacePrefix(
                expandedName.getNamespaceURI());
        // local name
        String qName = expandedName.getLocalName();
        if (prefix != null) {
            StringBuffer sb = new StringBuffer(
                    prefix.length() + qName.length() + 1);
            // construct the qName
            sb.append(prefix).append(':').append(qName);
            qName = sb.toString();
        }
        return qName;
    }
}

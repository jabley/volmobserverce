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
package com.volantis.xml.pipeline.sax.impl.drivers.webservice;

import com.volantis.shared.net.url.URLContentManager;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.xml.namespace.NamespacePrefixTracker;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.drivers.webservice.WSDLCatalog;
import com.volantis.xml.pipeline.sax.drivers.webservice.WSDLEntry;
import com.volantis.xml.pipeline.sax.drivers.webservice.WSDriverConfiguration;
import com.volantis.xml.pipeline.sax.drivers.webservice.WSDriverUtils;
import com.volantis.xml.pipeline.sax.impl.drivers.webservice.wsif.WebServiceInvoker;
import com.volantis.xml.pipeline.sax.impl.dynamic.ContextManagerProcess;
import com.volantis.xml.pipeline.sax.operation.AbstractOperationProcess;
import com.volantis.xml.pipeline.sax.url.PipelineURLContentManager;
import com.volantis.xml.utilities.sax.SAXUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import java.io.IOException;
import java.net.URL;

/**
 * The OperationProcess for the WebService Connector request element.
 */
public class RequestOperationProcess extends AbstractOperationProcess {

    /**
     * A property key to access the namespace uri for response message parts
     * that are stored in the Pipeline context.
     */
    static String PART_NAMESPACE_URI_KEY = "PNURI";

    /**
     * A property key to access the prefix for response message parts that
     * are stored in the Pipeline context.
     */
    static String PART_PREFIX_KEY = "PPK";

    /**
     * The name the reponse element.
     */
    private final String RESPONSE = "response";

    /**
     * The name of the message element.
     */
    private final String MESSAGE = "message";

    /**
     * The configuration for this operation process.
     */
    private WSDriverConfiguration configuration;

    /**
     * Construct a new RequestOperationProcess.
     * @throws IllegalArgumentException if configuration is null.
     */
    public RequestOperationProcess() {
    }

    // javadoc inherited
    public void setPipeline(XMLPipeline pipeline) {
        super.setPipeline(pipeline);

        // get hold of the pipeline context
        XMLPipelineContext context = getPipelineContext();

        // get hold of the pipeline configuration
        XMLPipelineConfiguration pipelineConfiguration =
                context.getPipelineConfiguration();

        configuration = (WSDriverConfiguration)pipelineConfiguration.
                retrieveConfiguration(WSDriverConfiguration.class);

        if (configuration == null) {
            // cannot get hold of the configuration. As this is fatal
            // deliver a fatal error down the pipeline
            XMLPipelineException error = new XMLPipelineException(
                    "Could not retrieve the Web Service configuration",
                    context.getCurrentLocator());

            try {
                pipeline.getPipelineProcess().fatalError(error);
            } catch (SAXException e) {
                // cannot continue so throw a runtime exception
                throw new ExtendedRuntimeException(e);
            }
        }
    }

    // javadoc inherited from interface
    public void stopProcess() throws SAXException {
        // Each request has a single operation and a single message. Both
        // of these should have been set as properties in the pipeline context
        // by the time we get here.
        XMLPipelineContext context = getPipelineContext();
        if (!context.inErrorRecoveryMode()) {
            Locator locator = context.getCurrentLocator();

            Operation operation =
                    (Operation)context.getProperty(Operation.class);
            if (operation == null) {
                String errorMessage =
                        "Could not find the operation for this request";
                fatalError(new XMLPipelineException(errorMessage, locator));
            }

            Message message = (Message)context.getProperty(Message.class);
            if (message == null) {
                String errorMessage =
                        "Could not find the message for this request";
                fatalError(new XMLPipelineException(errorMessage, locator));
            }

            InputSource wsdlInputSource = null;
            try {
                wsdlInputSource = acquireWSDLInputSource(operation);
            } catch (IOException e) {
                e.printStackTrace();
                fatalError(new XMLPipelineException(
                        e.getMessage(), locator, e));
            }

            if (wsdlInputSource == null) {
                // It should usually not be the case that wsdlInputSource is
                // null, though can happen if the WSDL acquisition times out
                // for example.
                // @todo later for some reason this can cause a flow control failure if the operation is surrounded by a try operation
                fatalError(new XMLPipelineException(
                        "wsdlInputSource is null", locator));
            }

            try {
                Message result =
                        WebServiceInvoker.invokeWebService(wsdlInputSource,
                                                           operation,
                                                           message);
                forwardMessageAsXML(result);
            } catch (Exception e) {
                fatalError(new XMLPipelineException(
                        e.getMessage(), locator, e));
            }
        }
    }

    /**
     * Forward a Message as XML to the next process in the pipeline.
     * @param message The Message to forward.
     */
    private void forwardMessageAsXML(Message message)
            throws SAXException, IOException {

        XMLProcess consumer = getNextProcess();

        XMLPipelineContext context = getPipelineContext();

        String partNamespace = (String)
                context.getProperty(PART_NAMESPACE_URI_KEY);
        String partPrefix = (String)context.getProperty(PART_PREFIX_KEY);

        String responseNamespace = configuration.getResponseNamespaceURI();
        String responsePrefix = configuration.getResponseDefaultPrefixURI();

        NamespacePrefixTracker namespaceManager =
                context.getNamespacePrefixTracker();
        namespaceManager.startPrefixMapping(responsePrefix,
                                            responseNamespace);

        consumer.startPrefixMapping(responsePrefix, responseNamespace);

        // Get the qnames that are constant for this operation process
        String responseQName = SAXUtils.createPrefixedName(responsePrefix,
                                                           RESPONSE);
        String messageQName = SAXUtils.createPrefixedName(responsePrefix,
                                                          MESSAGE);

        consumer.startElement(responseNamespace, RESPONSE, responseQName,
                              new AttributesImpl());

        consumer.startElement(responseNamespace, MESSAGE, messageQName,
                              new AttributesImpl());

        // If the parts have a prefix we need to map them now.
        if (partPrefix != null) {
            namespaceManager.startPrefixMapping(partPrefix,
                                                partNamespace);

            consumer.startPrefixMapping(partPrefix, partNamespace);

        }

        for (int i = 0; i < message.size(); i++) {
            Part part = message.retrievePart(i);
            String partQName = SAXUtils.createPrefixedName(partPrefix,
                                                           part.getName());
            consumer.startElement(partNamespace, part.getName(), partQName,
                                  new AttributesImpl());

            Object value = part.getValue();
            if (value instanceof Element) {
                // This is a DOM Element. Use DOMToSAX.
                Node node = (Node)value;
                DOMToSAX dom2SAX = new DOMToSAX(node);

                ContextManagerProcess cup = new ContextManagerProcess(true);
                cup.setPipeline(getPipeline());
                cup.setNextProcess(consumer);
                dom2SAX.setContentHandler(cup);

                dom2SAX.parse();
            } else {
                // Just a bunch of characters.
                String chars = value.toString();
                consumer.characters(chars.toCharArray(), 0, chars.length());
            }
            consumer.endElement(partNamespace, part.getName(), partQName);
        }

        // If the parts have a prefix we need to unmap them now.
        if (partPrefix != null) {
            consumer.endPrefixMapping(partPrefix);
            namespaceManager.endPrefixMapping(partPrefix);
        }

        consumer.endElement(responseNamespace, MESSAGE, messageQName);
        consumer.endElement(responseNamespace, RESPONSE, responseQName);

        consumer.endPrefixMapping(responsePrefix);
        namespaceManager.endPrefixMapping(responsePrefix);
    }

    /**
     * Using an operation and the configuration, return an InputSource
     * based on the WSDL for the operation.
     */
    private InputSource acquireWSDLInputSource(Operation operation)
            throws IOException {
        String wsdlURI = operation.retrieveWSDLURI();
        InputSource wsdlInputSource = null;

        URLContentManager manager = PipelineURLContentManager.retrieve(
                getPipelineContext());

        if (configuration != null) {
            // Look in the configuration for an alternative source for the
            // wsdl...
            WSDLCatalog catalog = configuration.getWSDLCatalog();

            if (catalog != null) {
                WSDLEntry entry = catalog.retrieveWSDLEntry(wsdlURI);

                if (entry != null) {
                    XMLPipelineContext context = getPipelineContext();
                    wsdlInputSource = entry.provideAlternativeInputSource(
                            context, manager);
                }
            }
        }

        if (wsdlInputSource == null) {
            // No alternative input source for the WSDL could be found so
            // try the uri directly.

            // retrieve the URL that should be used to resolve any
            // relative URIS.
            URL base = getPipelineContext().getCurrentBaseURI();

            // ensure that we have a fully resolved URI.
            URL resolved = (null == base) ? new URL(wsdlURI) :
                    new URL(base, wsdlURI);

            wsdlInputSource = WSDriverUtils.createURLInputSource(
                    resolved, manager, getPipelineContext());
        }

        return wsdlInputSource;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Oct-05	9724/1	philws	VBM:2005092810 Port forward of the generic pipeline connection timeout functionality

 04-Oct-05	9679/1	philws	VBM:2005092810 Provide a connection timeout mechanism and configuration for pipeline operations

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 18-Mar-04	614/1	adrian	VBM:2004030307 Fixed jsp integration tests

 17-Mar-04	608/1	adrian	VBM:2004030307 Fixed jsp integration tests

 19-Dec-03	489/1	doug	VBM:2003120807 Ensured that our current xml processes are recoverable when inside a try op

 06-Aug-03	301/1	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 28-Jul-03	232/1	doug	VBM:2003071804 Refactored XMLPipelineContext to reflect new public API

 25-Jul-03	242/1	steve	VBM:2003072310 Implement namespace package and refactor exitsting code to fit it

 18-Jul-03	213/2	doug	VBM:2003071615 Refactored XMLProcess interface

 30-Jun-03	98/21	allan	VBM:2003022822 Commit for implementation review

 29-Jun-03	98/19	allan	VBM:2003022822 Added some tests for RequestOperationProcess - could do with more though. Added some possibly final touches

 27-Jun-03	98/12	allan	VBM:2003022822 Intermediate commit for jsp testing

 20-Jun-03	98/6	allan	VBM:2003022822 Add new classes to make them available to others

 19-Jun-03	98/4	allan	VBM:2003022822 WS Connector renamed to WS Driver

 18-Jun-03	98/1	allan	VBM:2003022822 Promote prelimary classes so that other can access them

 ===========================================================================
*/

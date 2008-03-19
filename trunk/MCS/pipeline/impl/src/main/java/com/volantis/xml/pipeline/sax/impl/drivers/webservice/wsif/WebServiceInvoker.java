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
package com.volantis.xml.pipeline.sax.impl.drivers.webservice.wsif;

import com.ibm.wsdl.Constants;
import com.volantis.wsif.WSIFConstants;
import com.volantis.wsif.WSIFException;
import com.volantis.wsif.WSIFMessage;
import com.volantis.wsif.WSIFOperation;
import com.volantis.wsif.WSIFPort;
import com.volantis.wsif.WSIFService;
import com.volantis.wsif.WSIFServiceFactory;
import com.volantis.wsif.logging.Trc;
import com.volantis.wsif.providers.soap.apacheaxis.WSIFAXISConstants;
import com.volantis.wsif.providers.soap.apacheaxis.WSIFDynamicProvider_ApacheAxis;
import com.volantis.wsif.util.TypeSerializerInfo;
import com.volantis.wsif.util.WSIFPluggableProviders;
import com.volantis.wsif.util.WSIFUtils;
import com.volantis.xml.pipeline.sax.impl.drivers.webservice.Message;
import com.volantis.xml.pipeline.sax.impl.drivers.webservice.Operation;
import com.volantis.xml.pipeline.sax.impl.drivers.webservice.Part;
import com.volantis.xml.pipeline.sax.impl.drivers.webservice.wsif.soap.PipelineAXISStringSerializerFactory;
import com.volantis.xml.pipeline.sax.impl.drivers.webservice.Message;
import com.volantis.xml.pipeline.sax.impl.drivers.webservice.Part;
import org.apache.axis.AxisFault;
import org.apache.axis.encoding.ser.ElementDeserializerFactory;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.wsdl.Definition;
import javax.wsdl.Input;
import javax.wsdl.Output;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Utility class for invoking WebServices. This class delegates the task
 * of selecting the service, binding and part to WSIF. Multiple bindings are
 * supported via WSIFProviders. WSIF has several built in providers including
 * EJB, Java, and Soap via both ApacheSOAP and Axis. However, this
 * implementation has only ever been tested with the Axis provider - though
 * originally it was based on ApacheSOAP. WSIF sits on top of WSDL4J.
 *
 * Both WSIF and Axis are relatively new and at the time of writing are
 * significantly lacking in documentation. It is likely that they will improve
 * in quality over both in terms of their code and documentation. We should
 * try to take advantage of such improvements where possible.
 *
 * @todo later make independent of Axis
 */
public class WebServiceInvoker {

    static {
        Trc.ON = false; // disable commons-logging

        // Register WSIF Providers for Pipeline.
        WSIFPluggableProviders.overrideDefaultProvider(
                "http://schemas.xmlsoap.org/wsdl/soap/",
                new WSIFDynamicProvider_ApacheAxis());

    }

    /**
     * Used for formatting output of fault messages.
     */
    private static String EXAMPLE_SOAP_ENV_ELEMENT = "soap-env:envelope";

    /**
     * Used for formatting output of fault messages.
     */
    private static String EXAMPLE_SOAP_BODY_ELEMENT = "soap-env:body";

    /**
     * Used for formatting output of fault messages.
     */
    private static String EXAMPLE_SOAP_ENVELOPE_START = '<' +
            EXAMPLE_SOAP_ENV_ELEMENT + ">\n";

    /**
     * Used for formatting output of fault messages.
     */
    private static String EXAMPLE_SOAP_ENVELOPE_END = "</" +
            EXAMPLE_SOAP_ENV_ELEMENT + ">\n";

    /**
     * Used for formatting output of fault messages.
     */
    private static String EXAMPLE_SOAP_BODY_START = '<' +
            EXAMPLE_SOAP_BODY_ELEMENT + ">\n";

    /**
     * Used for formatting output of fault messages.
     */
    private static String EXAMPLE_SOAP_BODY_END = "</" +
            EXAMPLE_SOAP_BODY_ELEMENT + ">\n";

    /**
     * Used for formatting output of fault messages.
     */
    private static String FAKE_HEADERS = EXAMPLE_SOAP_ENVELOPE_START +
            EXAMPLE_SOAP_BODY_START;

    /**
     * Used for formatting output of fault messages.
     */
    private static String FAKE_FOOTERS = EXAMPLE_SOAP_BODY_END +
            EXAMPLE_SOAP_ENVELOPE_END;

    /**
     * Invoke a WebService based on the given parameters and return the output
     * if any as a Message.
     *
     * @param wsdlInputSource An InputSource derived from the WSDL document for
     *                        the WebService to invoke.
     * @param operation       The WebService Operation to invoke.
     * @param message         The Message (parameters) for the requested
     *                        operation.
     * @return A Message containing the response from the WebService
     * @throws WSDLException If there is a problem related to WSDL.
     * @throws WSIFException If there is a WSIF problem.
     */
    public static Message invokeWebService(
            InputSource wsdlInputSource,
                                           Operation operation,
            Message message)
            throws WSDLException, WSIFException {

        Message result = null;

        Definition def = createDefinition(wsdlInputSource);

        Service service = WSIFUtils.selectService(def, null, null);

        PortType portType = WSIFUtils.selectPortType(def,
                                                     def.getTargetNamespace(),
                                                     operation.getPortType());

        WSIFServiceFactory factory = WSIFServiceFactory.newInstance();
        WSIFService wsifService = factory.getService(def, service, portType);
        WSIFPort wsifPort = wsifService.getPort();

        String operationName = operation.getOperationName();
        javax.wsdl.Operation wsdlOperation =
                retrieveWSDLOperation(operationName, portType);

        // Get the Input and Output objects to create the WSIFOperation and
        // messages.
        Input wsdlInput = wsdlOperation.getInput();
        String inputName = (wsdlInput.getName() == null) ? null :
                wsdlInput.getName();
        Output wsdlOutput = wsdlOperation.getOutput();
        String outputName = (wsdlOutput.getName() == null) ? null :
                wsdlOutput.getName();

        WSIFOperation wsifOperation =
                wsifPort.createOperation(operationName, inputName, outputName);
        WSIFMessage input = wsifOperation.createInputMessage();
        WSIFMessage output = wsifOperation.createOutputMessage();
        WSIFMessage fault = wsifOperation.createFaultMessage();

        initializeInputParts(wsdlInput.getMessage(), message, input);

        createTypeMappings(input, output, wsifService, wsifOperation);

        try {
            wsifOperation.executeRequestResponseOperation(
                    input, output, fault);
        } catch (IOException e) {
            throw new WSIFException(e.getMessage(), e);
        }

        checkForFault(operation, message, fault);

        result = buildOutputMessage(wsdlOutput.getMessage(), output);

        return result;
    }

    /**
     * Given a WSIFMessage that is a fault message received from a WebService,
     * check if it has any parts. If it does there was a fault. In this case
     * throw an exception and attempt to provide a useful message.
     *
     * NOTE: There has been very little experience of faults. It is quite
     * possible that there are many different kinds of faults that are not
     * handled well by this method.
     *
     * Only AXIS faults are currently handled with an degree if adequacy.
     *
     * @param operation The operation that generated the fault.
     * @param input The input that resulted in the fault response.
     * @param fault The fault message.
     * @throws WSIFException If there was a fault.
     */
    private static void checkForFault(Operation operation, Message input,
                                      WSIFMessage fault)
            throws WSIFException {

        Iterator faultParts = fault.getParts();
        String faultString = null;
        Object faultPart = null;
        while (faultParts.hasNext()) {
            faultPart = faultParts.next();
            if (faultPart instanceof AxisFault) {
                AxisFault axisFault = (AxisFault)faultPart;
                faultString = axisFault.dumpToString();
            }
        }

        if (faultPart != null) {
            String message = null;
            if (faultPart instanceof AxisFault) {

                StringBuffer msgBuffer = new StringBuffer();
                String qualifier = "\n\nThe soap request envelope would have" +
                        " looked something like this....\n\n";
                msgBuffer.append(qualifier);
                addFakeHeadersToBuffer(msgBuffer);
                msgBuffer.append('<').append(operation.getOperationName()).
                        append(">\n");
                addMessageToBuffer(input, msgBuffer);
                addFakeFootersToBuffer(msgBuffer);
                msgBuffer.append("</").append(operation.getOperationName()).
                        append(">\n");
                msgBuffer.append("\n\nHere are the fault details:\n\n");
                msgBuffer.append(faultString);
                message = msgBuffer.toString();
            } else {
                message = "WebService server reported a fault." +
                        "Sorry, don't know any more details.";
            }

            throw new WSIFException(message);
        }
    }

    /**
     * Fault messages sometimes specify the line number of the fault. This
     * line number will (at least in the single case I have experienced) be
     * 2 lines more than the lines in the operation/message - presumably
     * accounting for the soap body and soap envelop elements. Of course this
     * is quite subjective but I think it is preferable to add a couple of
     * 'header' lines to the fault message that we provide so that users at
     * least have a chance to find the line. That is what this method does.
     * @param buffer The buffer.
     */
    private static void addFakeHeadersToBuffer(StringBuffer buffer) {
        buffer.append(FAKE_HEADERS);
    }

    /**
     * @see #addFakeHeadersToBuffer
     */
    private static void addFakeFootersToBuffer(StringBuffer buffer) {
        buffer.append(FAKE_FOOTERS);
    }

    /**
     * This method puts a Message into a StringBuffer formatted so that each
     * part is on a new line. Each part value is treated as a String.
     * @param input
     * @param buffer
     */
    private static void addMessageToBuffer(Message input,
                                           StringBuffer buffer) {
        for (int i = 0; i < input.size(); i++) {
            Part part = input.retrievePart(i);
            buffer.append('<').append(part.getName()).append(">\n");
            buffer.append(part.getValue());
            buffer.append("</").append(part.getName()).append(">\n");
        }
    }

    /**
     * Build a WSDriver Message that describes a WSIFMessage output.
     * @param format The WSDL Message i.e. the abstract message format.
     * @param output The WSIFMessage that contains the actual output from
     * the WebService.
     */
    private static Message buildOutputMessage(javax.wsdl.Message format,
                                              WSIFMessage output)
            throws WSIFException {

        Message result = new Message();

        List parts = format.getOrderedParts(null);

        for (int i = 0; i < parts.size(); i++) {
            javax.wsdl.Part wsdlPart = (javax.wsdl.Part)parts.get(i);
            String partName = wsdlPart.getName();
            Object value = output.getObjectPart(partName);
            Part wsDriverPart = new Part();
            wsDriverPart.setName(partName);
            wsDriverPart.setValue(value);
            result.addPart(wsDriverPart);
        }

        return result;
    }

    /**
     * Map part types in the input and output messages to java classes that
     * can be handled by Axis and provide serializers to ensure that all
     * complex types are Deserialized as Elements and Serialized using
     * a PipelineAXIStringSerializer.
     * @param input The WSIF input message.
     * @param output The WSIF output message.
     * @param service The WSIF service in use.
     * @param operation The WSIF operation to be invoked.
     * @throws WSIFException If something goes wrong.
     */
    private static void createTypeMappings(WSIFMessage input,
                                           WSIFMessage output,
                                           WSIFService service,
                                           WSIFOperation operation)
            throws WSIFException {

        List mappingInfo = new ArrayList();

        javax.wsdl.Message wsdlInput = input.getMessageDefinition();
        javax.wsdl.Message wsdlOutput = output.getMessageDefinition();

        List inputParts = wsdlInput.getOrderedParts(null);
        List outputParts = wsdlOutput.getOrderedParts(null);
        buildMappingInfo(inputParts, service, mappingInfo, String.class);
        buildMappingInfo(outputParts, service, mappingInfo, Element.class);

        WSIFMessage context = operation.getContext();
        context.setObjectPart(WSIFAXISConstants.CONTEXT_SOAP_TYPE_SERIALIZERS,
                              mappingInfo);
        operation.setContext(context);
    }

    /**
     * Build a list of TypeSerializerInfo and add type mappings to the
     * WSIFSerice. The TypeSerializerInfo tells the WSIFProvider how to
     * serialize and de-serialize parts. We avoid having the WSIFProvider
     * work this out for itself because of complex types. The WSIFProvider
     * will by default try to serialize an Element with a SimpleSerializer.
     * A SimpleSerializer will encode every < and > character (among others)
     * thereby breaking the element. The WSIFProvider will attempt to
     * de-serialize an Element by looking for the Class of the same type.
     * Typically this Class will not exist so it fails. Here we overide this
     * behaviour to map every complex part type to an Element and ensure that
     * an ElementDeserializer is registered for de-serialization. We force
     * elements to be serialized with our own serializer that does not encode
     * characters.
     * @param parts A list of WSDL parts to build mapping info for.
     * @param service The WSIFService in use.
     * @param mappingInfo A List in which to store mapping info.
     * @param mapType The kind of Class elements should map to and have
     * their Serializer/Deserializer associated with.
     * @throws WSIFException If something goes wrong.
     * @todo later make independent of Axis
     */
    private static void buildMappingInfo(List parts, WSIFService service,
                                         List mappingInfo, Class mapType)
            throws WSIFException {

        for (int i = 0; i < parts.size(); i++) {
            javax.wsdl.Part wsdlPart = (javax.wsdl.Part)parts.get(i);
            QName element = getElementName(wsdlPart);
            if (element != null) {

                service.mapType(element, mapType);

                TypeSerializerInfo info = null;
                if (mapType == Element.class) {
                    info = new TypeSerializerInfo(element,
                                                  mapType, null,
                                                  new ElementDeserializerFactory());

                } else {
                    info = new TypeSerializerInfo(element,
                                                  mapType,
                                                  new PipelineAXISStringSerializerFactory(String.class,
                                                                                          element), null);
                }
                mappingInfo.add(info);
            }
        }
    }

    /**
     * Some parts have an element name. These are always complex
     * types. In these cases we just provide wsdlPart.getElementName().
     * Some parts specify a type name instead. In these cases sometimes the
     * type will be a complex type and sometimes not. If there is no
     * element name and the part type name is not in the namespace of one of
     * the supported w3c XMLSchemas (listed below) then we consider the part to
     * be complex and return the type name as the element name. If there is no
     * element name and the type is in an XMLSchema namespace this this is a
     * simple type so null is returned.
     *
     * Supported w3c XMLSchemas:
     * http://www.w3.org/1999/XMLSchema
     * http://www.w3.org/2000/10/XMLSchema
     * http://www.w3.org/2001/XMLSchema
     *
     * @param wsdlPart The WSDL Part containing the part details.
     * @return A QName representing an Element if the part represents a
     * complext type; null otherwise.
     */
    private static QName getElementName(javax.wsdl.Part wsdlPart)
            throws WSIFException {

        QName elementName = wsdlPart.getElementName();

        if (elementName == null) {
            QName typeName = wsdlPart.getTypeName();
            if (typeName == null) {
                throw new WSIFException("Part \"" + wsdlPart.getName() +
                                        "\" has neither type nor element definition.");
            }
            String namespace = typeName.getNamespaceURI();
            if (!namespace.equals(Constants.NS_URI_XSD_1999) &&
                    !namespace.equals(Constants.NS_URI_XSD_2000) &&
                    !namespace.equals(Constants.NS_URI_XSD_2001)) {
                elementName = typeName;
            }
        }

        return elementName;
    }

    /**
     * Initialize the input parts associated with a message.
     * @param wsdlMessage The abstract Message format.
     * @param wsDriverMessage The Message containing the Parts obtained
     * from the pipeline.
     * @param wsifMessage The WSIFMessage is will contain the actual input
     * having been initialized using a combination of the other two
     * Message parameters.
     */
    private static void initializeInputParts(javax.wsdl.Message wsdlMessage,
                                             Message wsDriverMessage,
                                             WSIFMessage wsifMessage
                                             )
            throws WSIFException {

        List parts = wsdlMessage.getOrderedParts(null);

        if (parts.size() != wsDriverMessage.size()) {
            // Both sets of parts should be the same size.
            throw new WSIFException("Wrong number of parts. " +
                                    "WSDL message " + wsdlMessage.getQName() +
                                    " has " + parts.size() + " parts. The wsdriver.Message" +
                                    " equivalent has " + wsDriverMessage.size());
        }

        for (int i = 0; i < parts.size(); i++) {
            javax.wsdl.Part wsdlPart = (javax.wsdl.Part)parts.get(i);
            Part wsDriverPart = wsDriverMessage.retrievePart(i);

            if (!wsDriverPart.getName().equals(wsdlPart.getName())) {
                // Both sets of parts should be in the same order and have the
                // same set of part names.
                throw new WSIFException("Unexpected part name. Expected \"" +
                                        wsdlPart.getName() + "\" but was \"" +
                                        wsDriverPart.getName() + "\".");
            }

            wsifMessage.setObjectPart(wsDriverPart.getName(),
                                      wsDriverPart.getValue());

        }
    }

    /**
     * Retrieve the WSDL Operation with the given name from the specified
     * PortType.
     * @param name The name of the Operation.
     * @param portType The PortType containing the Operation.
     * @throws WSIFException If the requested Operation could not be found.
     */
    private static javax.wsdl.Operation retrieveWSDLOperation(String name,
                                                              PortType portType)
            throws WSIFException {

        // retrieve list of operations
        List operationList = portType.getOperations();

        // try to find input and output names for the operation specified
        javax.wsdl.Operation operation = null;
        for (int i = 0; i < operationList.size() && operation == null; i++) {
            javax.wsdl.Operation op =
                    (javax.wsdl.Operation)operationList.get(i);
            if (op.getName().equals(name)) {
                operation = op;
            }
        }

        if (operation == null) {
            throw new WSIFException("Could not find operation \"" +
                                    name + "\".");
        }

        return operation;
    }

    /**
     * Create the Definition object for a WSDL.
     * @param wsdl The InputSource to the WSDL.
     * @return The Definition for the WSDL.
     * @throws javax.wsdl.WSDLException If something goes wrong.
     */
    private static Definition createDefinition(InputSource wsdl)
            throws WSDLException {

        WSIFUtils.initializeProviders();

        WSDLFactory factory = WSDLFactory.newInstance(
                WSIFConstants.WSIF_WSDLFACTORY);
        WSDLReader wsdlReader = factory.newWSDLReader();
        wsdlReader.setFeature(Constants.FEATURE_VERBOSE, false);
        Definition def =
                wsdlReader.readWSDL(null, wsdl);

        return def;
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

 04-Aug-03	294/1	allan	VBM:2003070709 Fixed merge conflicts

 31-Jul-03	217/3	allan	VBM:2003071702 Fixed javadoc issue with contains and containsIdentity.

 31-Jul-03	217/1	allan	VBM:2003071702 Made HTTPMessageEntities into a set.

 29-Jun-03	98/11	allan	VBM:2003022822 Added some tests for RequestOperationProcess - could do with more though. Added some possibly final touches

 27-Jun-03	98/5	allan	VBM:2003022822 Intermediate commit for jsp testing

 ===========================================================================
*/

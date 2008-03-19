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

import com.volantis.xml.pipeline.sax.impl.drivers.webservice.Message;
import com.volantis.xml.pipeline.sax.impl.drivers.webservice.Part;
import com.volantis.xml.pipeline.sax.impl.drivers.webservice.WSDLOperation;

/**
 * A class of test helper methods for WSDriver tests.
 */
public class WSDriverTestHelpers {
    /**
     * Take an operation and a message and set them up for a request
     * that operates on only simple types. This case uses the BabelFish
     * WebService.
     * @param operation The operation.
     * @param message The message.
     * @param translation The "from" and "to" languages e.g. en_fr is
     * English to French.
     * @param text The String to translate.
     */
    public static void setupBabelFishOperationMessage(WSDLOperation operation,
                                                      Message message,
                                                      String translation,
                                                      String text) {

        // Set up the operation
        String wsdlURI =
                "http://www.xmethods.net/sd/2001/BabelFishService.wsdl";
        String portType = "BabelFishPortType";
        String operationName = "BabelFish";

        operation.setWsdlURI(wsdlURI);
        operation.setPortType(portType);
        operation.setOperationName(operationName);

        // Set up the message
        Part part = new Part();
        part.setName("translationmode");
        part.setValue(translation);
        message.addPart(part);

        part = new Part();
        part.setName("sourcedata");
        part.setValue(text);
        message.addPart(part);
    }

    /**
     * Take an operation and a message and set them up for a request
     * that operates on only complex types declared as elements. This
     * case uses a W3Coder WebService that simply reverses a string.
     * @param operation The operation.
     * @param message The message
     */
    public static void
            setupReverseStringOperationMessage(WSDLOperation operation,
                                               Message message,
                                               String string) {

        // Set up the operation
        String wsdlURI = "http://www.w3coder.com/ws/email/SerVice.asmx?WSDL";
        String portType = "ServiceSoap";
        String operationName = "RevString";

        operation.setWsdlURI(wsdlURI);
        operation.setPortType(portType);
        operation.setOperationName(operationName);

        // Set up the message
        Part part = new Part();
        part.setName("parameters");
        part.setValue("<ReverseString>"+string+"</ReverseString>");
        message.addPart(part);

    }

    /**
     * Take an operation and a message and set them up for a request
     * that operates on complex types that are declared as types as
     * oppose to elements. This case uses the Amazon WebService and
     * its KeywordSerachRequest operation.
     * @param operation The operation.
     * @param message The message.
     * @param author The name of an author to use in the search.
     */
    public static void setupAmazonOperationMessage(WSDLOperation operation,
                                                   Message message,
                                                   String author) {
        // Set up the operation
        String wsdlURI =
                "http://soap.amazon.com/schemas/AmazonWebServices.wsdl";
        String portType = "AmazonSearchPort";
        String operationName = "KeywordSearchRequest";

        operation.setWsdlURI(wsdlURI);
        operation.setPortType(portType);
        operation.setOperationName(operationName);

        // Set up the message
        Part part = new Part();
        part.setName("KeywordSearchRequest");

        StringBuffer buffer = new StringBuffer();
        buffer.append("<keyword>" + author + "</keyword>\n");
        buffer.append("<page>1</page>\n");
        buffer.append("<mode>books</mode>\n");
        buffer.append("<tag>webservices-20</tag>\n");
        buffer.append("<type>lite</type>\n");
        buffer.append("<devtag>*******</devtag>\n");
        buffer.append("<version>1.0</version>\n");

        part.setValue(buffer.toString());

        message.addPart(part);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 29-Jun-03	98/1	allan	VBM:2003022822 Added some tests for RequestOperationProcess - could do with more though. Added some possibly final touches

 ===========================================================================
*/

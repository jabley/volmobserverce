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

import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLStreamingException;
import com.volantis.xml.pipeline.sax.operation.AbstractOperationProcess;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * An OperationProcess that builds Message objects from streamed XML.
 */
public class MessageOperationProcess extends AbstractOperationProcess {

    /**
     * In order to distinguish between the startElement and endElement for a
     * part rather than some element within the part we keep track of the
     * level of nesting of elements. When the nested element level is zero
     * we know that element start and end events will be for part elements.
     * When the nested element level is more than zero we know that element
     * start and end events are for elements that are within a part (part of the
     * part).
     */
    private int nestedElementLevel;

    /**
     * The current Part of the Message being built.
     */
    private Part currentPart;

    /**
     * A buffer for building the value of a part.
     */
    private StringBuffer partValueBuffer;

    /**
     * The message that is built by this operation process.
     */
    private Message message;

    /**
     * The namesapce uri for the message. We use this because we are only
     * interested in contents that are not in this namespace.
     */
    private String processNamespaceURI;

    /**
     * Get the processNamespaceURI.
     * @return The namespace uri for this process.
     */
    public String getProcessNamespaceURI() {
        return processNamespaceURI;
    }

    /**
     * Set the processNamespaceURI.
     * @param processNamespaceURI
     */
    public void setProcessNamespaceURI(String processNamespaceURI) {
        this.processNamespaceURI = processNamespaceURI;
    }

    // javadoc inherited from interface
    public void startProcess() throws SAXException {
        message = new Message();
    }

    // javadoc inherited from interface
    public void stopProcess() throws SAXException {

        XMLPipelineContext context = getPipelineContext();

        context.setProperty(Message.class, message, false);

        message = null;
    }


    /**
     * Override startElement() to populate the message and its parts.
     */
    // rest of javadoc inherited
    public void startElement(String namespace, String localName,
                             String qName, Attributes attributes)
            throws SAXException {

        XMLPipelineContext context = getPipelineContext();

        // All elements should be in a different namespace from the
        // message.
        if (getProcessNamespaceURI().equals(namespace)) {
            Locator locator = context.getCurrentLocator();
            String message = "Message parts must be in a different " +
                    "namespace from the message";
            fatalError(new XMLStreamingException(message, locator));
        }

        if (nestedElementLevel == 0) {
            // This must be the start of a part.();
            currentPart = new Part();
            currentPart.setName(localName);

            // If part namespace is not in the pipeline context then store it.
            Object test = context.getProperty(RequestOperationProcess.
                                              PART_NAMESPACE_URI_KEY);
            if (test == null) {
                context.setProperty(
                        RequestOperationProcess.PART_NAMESPACE_URI_KEY,
                        namespace, false);
            }

            // If part prefix is not in the pipeline context then store it.
            // The namespace and prefix are used to supply the response
            // message parts with namespaces and prefixes.
            if (qName != null) {
                test = context.
                        getProperty(RequestOperationProcess.PART_PREFIX_KEY);

                int colonIndex = qName.indexOf(':');
                if (test == null && colonIndex != -1) {
                    String partPrefix = qName.substring(0, colonIndex);
                    context.setProperty(
                            RequestOperationProcess.PART_PREFIX_KEY,
                            partPrefix, false);
                }
            }
            partValueBuffer = new StringBuffer();
        } else {
            // This element must be part of the current part.
            partValueBuffer.append('<').append(localName).append('>');
        }

        nestedElementLevel++;
    }

    /**
     * Override endElement() to populate the message and its parts.
     */
    // rest of javadoc inherited
    public void endElement(String namespace, String localName,
                           String qName)
            throws SAXException {

        // All elements should be in a different namespace from the
        // message.
        if (getProcessNamespaceURI().equals(namespace)) {
            XMLPipelineContext context = getPipelineContext();
            Locator locator = context.getCurrentLocator();
            String message = "Message parts must be in a different " +
                    "namespace from the message";
            fatalError(new XMLStreamingException(message, locator));
        }

        nestedElementLevel--;

        if (nestedElementLevel == 0) {
            // This must be the end of the current part.
            if (!currentPart.getName().equals(localName)) {
                // This should never happen because the document is supposed
                // to be valid before it reaches this process....But just in
                // case.
                XMLPipelineContext context = getPipelineContext();
                Locator locator = context.getCurrentLocator();
                String message = "Parts out of sync. Current part is " +
                        currentPart.getName() + " current endElement is " +
                        localName;
                fatalError(new XMLStreamingException(message, locator));
            }

            currentPart.setValue(partValueBuffer.toString());
            message.addPart(currentPart);
            partValueBuffer = null;
        } else {
            // This element must be part of the current part.
            partValueBuffer.append("</").append(localName).append('>');
        }
    }

    /**
     * Override characters() to populate the message and its parts.
     * process.
     */
    // rest of javadoc inherited
    public void characters(char[] buffer, int start, int offset)
            throws SAXException {

        // todo encode the characters
        if (nestedElementLevel == 0 && !isWhitespace(buffer, start, offset)) {
            XMLPipelineContext context = getPipelineContext();
            Locator locator = context.getCurrentLocator();
            String message = "No part seems to be associated with these " +
                    "characters since nestedElementLevel is 0";
            fatalError(new XMLStreamingException(message, locator));
        } else if (nestedElementLevel != 0) {
            partValueBuffer.append(buffer, start, offset);
        }
    }

    /**
     * Tests if a char array contains only whitespace at given region.
     * @param chars The char array.
     * @param start the index at which to start looking
     * @param offset the number of characters to check
     * @return true if every character in chars is whitespace; false
     * otherwise.
     */
    private boolean isWhitespace(char[] chars, int start, int offset) {

        boolean isWhitespace = true;
        for (int i = start;
             i < (offset + start) && isWhitespace;
             i++) {
            isWhitespace = Character.isWhitespace(chars[i]);
        }

        return isWhitespace;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Oct-04	5900/1	philws	VBM:2004101107 Avoid null pointer issue with leading whitespace in web services message part handling

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 04-Nov-03	438/1	doug	VBM:2003091803 Added parameter value processes

 10-Aug-03	264/1	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 04-Aug-03	294/1	allan	VBM:2003070709 Fixed merge conflicts

 31-Jul-03	217/3	allan	VBM:2003071702 Fixed javadoc issue with contains and containsIdentity.

 31-Jul-03	217/1	allan	VBM:2003071702 Made HTTPMessageEntities into a set.

 01-Jul-03	165/1	allan	VBM:2003070101 Fix bug in MessageOperationProcess.startElement()

 30-Jun-03	137/9	byron	VBM:2003022823 Support web service integration within a JSP page - update

 29-Jun-03	98/5	allan	VBM:2003022822 Added some tests for RequestOperationProcess - could do with more though. Added some possibly final touches

 27-Jun-03	137/6	byron	VBM:2003022823 Update test cases

 27-Jun-03	98/2	allan	VBM:2003022822 Intermediate commit for jsp testing

 ===========================================================================
*/

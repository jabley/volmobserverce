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
package com.volantis.xml.pipeline.sax.impl.operations.debug;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.namespace.DefaultNamespacePrefixTracker;
import com.volantis.xml.namespace.NamespacePrefixTracker;
import com.volantis.xml.pipeline.sax.XMLHandlerAdapter;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.XMLProcessImpl;
import com.volantis.xml.pipeline.sax.impl.dynamic.ContextAnnotatingProcess;
import com.volantis.xml.sax.ExtendedSAXException;
import com.volantis.xml.xml.serialize.OutputFormat;
import com.volantis.xml.xml.serialize.XMLSerializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * A Pipeline Process that serializes the sax event that it recieves to disk.
 */
public class SerializeProcess
        extends XMLProcessImpl implements TryCatchFinallyProcess {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(SerializeProcess.class);

    /**
     * Will be used to serialize the SAX events that this process receives.
     */
    protected XMLProcess serializer;


    /**
     * The path to the file that the XML will be serialized to.
     */
    private String debugOutputFilePath;

    /**
     * Will be used to write out the serialized
     */
    private Writer writer;

    /**
     * Used to track namespace declarations that this process has had to
     * declare explicitly as they had been declared out side of the scope of
     * this process.
     */
    private Stack namespaceDetails = new Stack();

    /**
     * Used to determine what namespaces are declared within the scope
     * of this process
     */
    private NamespacePrefixTracker namespacePrefixTracker =
            new DefaultNamespacePrefixTracker();


    // Javadoc inherited
    public void characters(char ch[],
                           int start,
                           int length) throws SAXException {
        serializer.characters(ch, start, length);
        super.characters(ch, start, length);
    }

    // javadoc inherited
    public void endDocument() throws SAXException {
        serializer.endDocument();
        super.endDocument();
    }

    // Javadoc inherited
    public void endElement(String namespaceURI,
                           String localName,
                           String qName) throws SAXException {
        serializer.endElement(namespaceURI, localName, qName);
        super.endElement(namespaceURI, localName, qName);
        String prefixDeclared = (String) namespaceDetails.pop();
        if (prefixDeclared != null) {
            serializer.endPrefixMapping(prefixDeclared);
            namespacePrefixTracker.endPrefixMapping(prefixDeclared);
        }
    }

    // Javadoc inherited
    public void endPrefixMapping(String prefix) throws SAXException {
        namespacePrefixTracker.endPrefixMapping(prefix);
        serializer.endPrefixMapping(prefix);
        super.endPrefixMapping(prefix);
    }

    // Javadoc inherited
    public void ignorableWhitespace(char ch[],
                                    int start,
                                    int length) throws SAXException {
        serializer.ignorableWhitespace(ch, start, length);
        super.ignorableWhitespace(ch, start, length);
    }

    // Javadoc inherited
    public void processingInstruction(String target, String data)
            throws SAXException {
        serializer.processingInstruction(target, data);
        super.processingInstruction(target, data);
    }

    // javadoc inherited
    public void setDocumentLocator(Locator locator) {
        serializer.setDocumentLocator(locator);
        super.setDocumentLocator(locator);
    }

    // javadoc inherited
    public void skippedEntity(String entity) throws SAXException {
        serializer.skippedEntity(entity);
        super.skippedEntity(entity);
    }

    // javadoc inherited
    public void startDocument() throws SAXException {
        serializer.startDocument();
        super.startDocument();
    }

    // Javadoc inherited
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes atts) throws SAXException {
        String prefix = cacluatePrefix(qName);
        if (prefix != null) {
            if (namespacePrefixTracker.getNamespaceURI(prefix) == null) {
                // this namespace binding must have been declared outside the
                // scope of this process. We need to declare the binding in the
                // log file
                serializer.startPrefixMapping(prefix, namespaceURI);
                namespacePrefixTracker.startPrefixMapping(prefix, namespaceURI);
            } else {
                prefix = null;
            }
        }
        namespaceDetails.push(prefix);
        serializer.startElement(namespaceURI, localName, qName, atts);
        super.startElement(namespaceURI, localName, qName, atts);
    }

    // Javadoc inherited
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        namespacePrefixTracker.startPrefixMapping(prefix, uri);
        serializer.startPrefixMapping(prefix, uri);
        super.startPrefixMapping(prefix, uri);
    }


    // javadoc inherited
    public void error(SAXParseException exception) throws SAXException {
        super.error(exception);
    }

    // javadoc inherited
    public void fatalError(SAXParseException exception) throws SAXException {
        super.fatalError(exception);
    }

    // javadoc inherited
    public void warning(SAXParseException exception) throws SAXException {
        super.warning(exception);
    }

    // javadoc inherited
    protected XMLProcess getConsumerProcess() {
        return super.getConsumerProcess();
    }

    // javadoc inherited
    public XMLProcess getNextProcess() {
        return super.getNextProcess();
    }

    // javadoc inherited
    public XMLPipeline getPipeline() {
        return super.getPipeline();
    }

    // javadoc inherited
    public XMLPipelineContext getPipelineContext() {
        return super.getPipelineContext();
    }

    // javadoc inherited
    public void setNextProcess(XMLProcess next) {
        super.setNextProcess(next);
    }

    // javadoc inherited
    public void setPipeline(XMLPipeline pipeline) {
        super.setPipeline(pipeline);
    }

    // javadoc inherited
    public void startProcess() throws SAXException {
        // create the xmlSerializer
        try {

            XMLSerializer xmlSerializer = getSerializer();
            if (xmlSerializer == null) {
                // There is no debug file so use a serializer that does
                // nothing. JSP tags will use this when no debug file is set.
                serializer = new NoOpProcess();
            } else {
                // ensure that the serializer is set up to operate in SAX mode.
                XMLHandlerAdapter adapter = new XMLHandlerAdapter();
                adapter.setContentHandler(xmlSerializer.asContentHandler());

                serializer = new ContextAnnotatingProcess(true);
                serializer.setNextProcess(adapter);
                serializer.setPipeline(getPipeline());
                serializer.startProcess();
            }
        } catch (IOException e) {
            throw new ExtendedSAXException(e);
        }
    }

    protected XMLSerializer getSerializer() throws IOException {
        XMLSerializer xmlSerializer = null;
        if (debugOutputFilePath != null) {
            File debugOutputFile = new File(debugOutputFilePath);
            File parentDir = debugOutputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                // ensure that the parent directory if any exists
                parentDir.mkdirs();
            }

            writer = new FileWriter(debugOutputFile);

            OutputFormat format = new OutputFormat();
            format.setPreserveSpace(true);
            format.setOmitXMLDeclaration(true);
            xmlSerializer = new XMLSerializer(writer, format);
        }
        return xmlSerializer;
    }


    // javadoc inherited
    public void stopProcess() throws SAXException {
        try {
            closeSerializer();
        } catch (IOException e) {
            throw new ExtendedSAXException(e);
        }
    }

    /**
     * Set the path to the file that this process will serialize the
     * SAX events to.
     * @param debugOutputFilePath the path that the SAX events will be
     * serialized to.
     */
    public void setDebugOutputFilePath(String debugOutputFilePath) {
        this.debugOutputFilePath = debugOutputFilePath;
    }

    /**
     * Closes the XMLSerializer ensuring that all input has been written out.
     * @throws IOException if an error occurs
     * @throws SAXException if an error occurs
     */
    protected void closeSerializer() throws IOException, SAXException {
        if (writer != null) {
            try {
                // we need to fake up an endDocument event in order to
                // get the serializer to clean up.
                serializer.stopProcess();
                // flush the writer
                writer.flush();
                writer.close();
            } finally {
                // set the writer to null;
                writer = null;
            }
        }
    }

    /**
     * Strips the prefix of a qualified element name
     * @param qName the qualified element name
     * @return the prefix of null if the qName is not prefixed.
     */
    private String cacluatePrefix(String qName) {
        String prefix = null;
        int index = -1;
        if (qName != null && (index = qName.indexOf(':')) != -1) {
            prefix = qName.substring(0, index);
        }
        return prefix;
    }

    // javadoc inherited
    public void doCatch(Throwable throwable) {
        // log the fact that an eror has occured. Note we are making no effort
        // to forward the error down the pipeline.
        logger.error("serialize-process-caught-throwable", throwable);

        try {
            // close the serializer
            closeSerializer();
        } catch (IOException e) {
            throw new ExtendedRuntimeException(e);
        } catch (SAXException e) {
            throw new ExtendedRuntimeException(e);
        }
    }

    // javadoc inherited
    public void doFinally() {
        // nothing to do.
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 23-May-05	8403/2	pcameron	VBM:2005052006 Added a no-op process serialiser to Pipeline for JSP tags

 11-May-05	8151/1	matthew	VBM:2005051002 add JSP Tag support for Serialize pipeline operation

 01-Apr-05	6798/1	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 ===========================================================================
*/

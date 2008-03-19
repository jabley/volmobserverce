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

import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLWrappingProcess;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.Locator;
import org.xml.sax.Attributes;

/**
 * This {@link XMLProcess} manages a {@link TryCatchFinallyProcess} by
 * delegating to the TryCatchFinallyProcess ensuring that if an exception
 * occurs the {@link TryCatchFinallyProcess#doCatch} and
 * {@link TryCatchFinallyProcess#doFinally} methods are invoked. 
 */
public class TryCatchFinallyManagerProcess extends XMLWrappingProcess {

    /**
     * Will delegate to in order to perform any "catch" or "finally" processing
     * that may be required in the {@link org.xml.sax.ContentHandler}
     * implemetation.
     */
    private TryCatchFinallyProcess tryCatchFinallyProcess;

    /**
     * Constructor for <code>TryCatchFinallyManagerProcess</code>
     * @param tryCatchFinallyProcess the {@link TryCatchFinallyProcess} that
     * will be delegated to in order to provide the XML processing. In addition
     * the {@link TryCatchFinallyProcess#doCatch} and
     * {@link TryCatchFinallyProcess#doFinally} implementations will be used to
     * perform any processing for caught exceptions as well as the finally
     * block processing.
     */
    public TryCatchFinallyManagerProcess(
                TryCatchFinallyProcess tryCatchFinallyProcess) {
        super(tryCatchFinallyProcess);
        this.tryCatchFinallyProcess = tryCatchFinallyProcess;
    }

    // Javadoc inherited
    public void characters(char ch[],
                           int start,
                           int length) throws SAXException {
        try {
            // delegate to the TryCatchFinallyProcess
            super.characters(ch, start, length);
        } catch (Throwable throwble) {
            // delegate to the TryCatchFinallyProcess
            tryCatchFinallyProcess.doCatch(throwble);
        } finally {
            // delegate to the TryCatchFinallyProcess
            tryCatchFinallyProcess.doFinally();
        }
    }

    // Javadoc inherited
    public void endDocument() throws SAXException {
        try {
            // delegate to the TryCatchFinallyProcess
            super.endDocument();
        } catch (Throwable throwble) {
            // delegate to the TryCatchFinallyProcess
            tryCatchFinallyProcess.doCatch(throwble);
        } finally {
            // delegate to the TryCatchFinallyProcess
            tryCatchFinallyProcess.doFinally();
        }
    }

    // Javadoc inherited
    public void endElement(String namespaceURI,
                           String localName,
                           String qName) throws SAXException {
        try {
            // delegate to the TryCatchFinallyProcess
            super.endElement(namespaceURI, localName, qName);
        } catch (Throwable throwble) {
            // delegate to the TryCatchFinallyProcess
            tryCatchFinallyProcess.doCatch(throwble);
        } finally {
            // delegate to the TryCatchFinallyProcess
            tryCatchFinallyProcess.doFinally();
        }
    }

    // Javadoc inherited
    public void endPrefixMapping(String prefix) throws SAXException {
        try {
            // delegate to the TryCatchFinallyProcess
            super.endPrefixMapping(prefix);
        } catch (Throwable throwble) {
            // delegate to the TryCatchFinallyProcess
            tryCatchFinallyProcess.doCatch(throwble);
        } finally {
            // delegate to the TryCatchFinallyProcess
            tryCatchFinallyProcess.doFinally();
        }
    }

    // Javadoc inherited
    public void ignorableWhitespace(char ch[],
                                    int start,
                                    int length) throws SAXException {
        try {
            // delegate to the TryCatchFinallyProcess
            super.ignorableWhitespace(ch, start, length);
        } catch (Throwable throwble) {
            // delegate to the TryCatchFinallyProcess
            tryCatchFinallyProcess.doCatch(throwble);
        } finally {
            // delegate to the TryCatchFinallyProcess
            tryCatchFinallyProcess.doFinally();
        }
    }

    // Javadoc inherited
    public void processingInstruction(String target, String data)
                throws SAXException {
        try {
            // delegate to the TryCatchFinallyProcess
            super.processingInstruction(target, data);
        } catch (Throwable throwble) {
            // delegate to the TryCatchFinallyProcess
            tryCatchFinallyProcess.doCatch(throwble);
        } finally {
            // delegate to the TryCatchFinallyProcess
            tryCatchFinallyProcess.doFinally();
        }
    }


    // Javadoc inherited
    public void setDocumentLocator(Locator locator) {
        try {
            // delegate to the TryCatchFinallyProcess
            super.setDocumentLocator(locator);
        } catch (Throwable throwble) {
            // delegate to the TryCatchFinallyProcess
            tryCatchFinallyProcess.doCatch(throwble);
        } finally {
            // delegate to the TryCatchFinallyProcess
            tryCatchFinallyProcess.doFinally();
        }
    }

    // Javadoc inherited
    public void skippedEntity(String s) throws SAXException {
        try {
            // delegate to the TryCatchFinallyProcess
            super.skippedEntity(s);
        } catch (Throwable throwble) {
            // delegate to the TryCatchFinallyProcess
            tryCatchFinallyProcess.doCatch(throwble);
        } finally {
            // delegate to the TryCatchFinallyProcess
            tryCatchFinallyProcess.doFinally();
        }
    }

    // Javadoc inherited
    public void startDocument() throws SAXException {
        try {
            // delegate to the TryCatchFinallyProcess
            super.startDocument();
        } catch (Throwable throwble) {
            // delegate to the TryCatchFinallyProcess
            tryCatchFinallyProcess.doCatch(throwble);
        } finally {
            // delegate to the TryCatchFinallyProcess
            tryCatchFinallyProcess.doFinally();
        }
    }

    // Javadoc inherited
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes atts) throws SAXException {
        try {
            // delegate to the TryCatchFinallyProcess
            super.startElement(namespaceURI, localName, qName, atts);
        } catch (Throwable throwble) {
            // delegate to the TryCatchFinallyProcess
            tryCatchFinallyProcess.doCatch(throwble);
        } finally {
            // delegate to the TryCatchFinallyProcess
            tryCatchFinallyProcess.doFinally();
        }
    }

    // Javadoc inherited
    public void startPrefixMapping(String prefix, String uri)
                throws SAXException {
        try {
            // delegate to the TryCatchFinallyProcess
            super.startPrefixMapping(prefix, uri);
        } catch (Throwable throwble) {
            // delegate to the TryCatchFinallyProcess
            tryCatchFinallyProcess.doCatch(throwble);
        } finally {
            // delegate to the TryCatchFinallyProcess
            tryCatchFinallyProcess.doFinally();
        }
    }

    // javadoc inherited
    public void setNextProcess(XMLProcess next) {
        super.setNextProcess(next);
    }

    // javadoc inherited
    public void setPipeline(XMLPipeline pipeline) {
        super.setPipeline(pipeline);
        tryCatchFinallyProcess.setPipeline(pipeline);
    }

    // javadoc inherited
    public void startProcess() throws SAXException {
        super.startProcess();
        tryCatchFinallyProcess.startProcess();
    }

    // javadoc inherited
    public void stopProcess() throws SAXException {
        super.stopProcess();
        tryCatchFinallyProcess.stopProcess();
    }

    // Javadoc inherited
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

    // Javadoc inherited
    public XMLPipelineContext getPipelineContext() {
        return super.getPipelineContext();
    }

    // javadoc inherited
    public void release() {
        super.release();
    }

    // Javadoc inherited
    public void warning(SAXParseException exception) throws SAXException {
        super.warning(exception);
    }

        // Javadoc inherited
    public void error(SAXParseException exception) throws SAXException {
        super.error(exception);
    }

    // Javadoc inherited
    public void fatalError(SAXParseException exception) throws SAXException {
        super.fatalError(exception);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Apr-05	6798/1	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 ===========================================================================
*/

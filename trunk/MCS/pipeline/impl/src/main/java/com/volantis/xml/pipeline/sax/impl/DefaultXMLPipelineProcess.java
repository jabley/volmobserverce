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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.impl;

import org.xml.sax.SAXException;
import org.xml.sax.Locator;
import com.volantis.xml.pipeline.sax.XMLPipelineProcessImpl;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineProcess;
import com.volantis.xml.pipeline.sax.XMLProcess;

/**
 * An extension of XMLPipelineProcessImpl which processes the
 * {@link org.xml.sax.ContentHandler} methods:
 * <ul>
 * <li> {@link org.xml.sax.ContentHandler#startDocument}
 * <li> {@link org.xml.sax.ContentHandler#endDocument}
 * <li> {@link org.xml.sax.ContentHandler#setDocumentLocator}
 * <li> {@link org.xml.sax.ContentHandler#startPrefixMapping}
 * <li> {@link org.xml.sax.ContentHandler#endPrefixMapping}
 * </ul>
 *
 * All {@link org.xml.sax.ContentHandler} methods are processed such that the
 * events are forwarded to the consumer process of this pipeline.  It is
 * intended that the head process (and therefore the consumer process) will be
 * an instance of
 * {@link com.volantis.xml.pipeline.sax.impl.dynamic.ContextManagerProcess}
 *
 * The ContextManagerProcess will update the {@link com.volantis.xml.pipeline.sax.XMLPipelineContext} as
 * document in the javadoc for that class.
 *
 * It is further intended that this XMLPipelineProcess should have a
 * {@link com.volantis.xml.pipeline.sax.impl.dynamic.ContextAnnotatingProcess} as
 * the tail process.  This will restore the contextual information as
 * SAXEvents.  This behaviour is as per the documentation for
 * {@link com.volantis.xml.pipeline.sax.impl.dynamic.ContextAnnotatingProcess}
 */
public class DefaultXMLPipelineProcess extends XMLPipelineProcessImpl {

    /**
     * Create a new DefaultXMLPipelineProcess instance. This default pipeline
     * will forward "nested" startDocument() and endDocument() events
     * to any process that has been set as the head/consumer process.
     * @param pipelineContext the associated XMLPipelineContext
     */
    public DefaultXMLPipelineProcess(XMLPipelineContext pipelineContext) {
        super(pipelineContext);
    }

    // javadoc inherited
    public void addTailXMLPipelineProcess(XMLPipelineProcess process)
            throws SAXException {
        // Cannot add a null process
        if (null == process) {
            throw new NullPointerException("Cannot add a null process");
        }
        // add the process to the pipeline
        addTailProcessImpl(process);
    }

    // javadoc inherited
    public void startProcess() throws SAXException {
        throw new UnsupportedOperationException("As the top level " +
                                                "XMLPipelineProcess the startProcess() method should " +
                                                "never be called.");
    }

    // javadoc inherited
    public void stopProcess() throws SAXException {
        throw new UnsupportedOperationException("As the top level " +
                                                "XMLPipelineProcess the stopProcess() method should " +
                                                "never be called.");
    }

    // javadoc inherited from ContentHandler interface
    public void startDocument() throws SAXException {
        XMLProcess consumer = getConsumerProcess();
        if (null != consumer) {
            consumer.startDocument();
        }
    }

    // javadoc inherited from ContentHandler interface
    public void endDocument() throws SAXException {
        XMLProcess consumer = getConsumerProcess();
        if (null != consumer) {
            consumer.endDocument();
        }
    }

    // javadoc inherited from ContentHandler interface
    public void setDocumentLocator(Locator locator) {
        XMLProcess consumer = getConsumerProcess();
        if (null != consumer) {
            consumer.setDocumentLocator(locator);
        }
    }

    // javadoc inherited from ContentHandler interface
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        XMLProcess consumer = getConsumerProcess();
        if (null != consumer) {
            consumer.startPrefixMapping(prefix, uri);
        }
    }

    // javadoc inherited from ContentHandler interface
    public void endPrefixMapping(String prefix) throws SAXException {
        XMLProcess consumer = getConsumerProcess();
        if (null != consumer) {
            consumer.endPrefixMapping(prefix);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 30-Jan-04	531/1	adrian	VBM:2004011905 added context updating and context annotation support to pipeline processes

 ===========================================================================
*/

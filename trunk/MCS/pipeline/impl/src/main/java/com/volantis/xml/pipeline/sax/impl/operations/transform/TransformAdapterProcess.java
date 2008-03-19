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
 * $Header: /src/voyager/com/volantis/mcs/protocols/XHTMLBasic.java,v 1.7 2001/10/30 15:16:05 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-May-03    Doug            VBM:2   002121803 - Created. The adapter process
 *                              that manages a TransformOperationProcess
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.impl.operations.transform;

import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLStreamingException;
import com.volantis.xml.pipeline.sax.adapter.AbstractAdapterProcess;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.util.Stack;

/**
 * Adapter process for the transform operation.
 * This process will process the following transformatin
 * markup in order to configure the operation.
 *
 * <pre>
 * <transform href="A.xsl">
 *   ........
 *   ........
 * </transform>
 *
 *
 *
 * <transform>
 *   [<parameters>
 *      <parameter name="x" value="y"/>
 *      <parameter name="a">
 *          ........
 *          ........
 *      </parameter>
 *    </parameters>]
 *   <transformation href="A.xsl"/>
 *     ........
 *     ........
 * </transform>
 *
 *
 *
 * <transform>
 *    [<parameters>
 *      <parameter name="x" value="y"/>
 *      <parameter name="a">
 *          ........
 *          ........
 *      </parameter>
 *    </parameters>]
 *   <transformations>
 *     <transformation href="A.xsl"/>
 *     <transfromation href="B.xsl"/>
 *   </transformations/>
 *   .........
 *   .........
 * </transform>
 *
 *
 *
 * <transform>
 *    [<parameters>
 *      <parameter name="x" value="y"/>
 *      <parameter name="a">
 *          ........
 *          ........
 *      </parameter>
 *    </parameters>]
 *   <transformations>
 *     <transformation>
 *       <xsl:stylesheet>
 *         // template definition
 *       </xsl:stylesheet>
 *     </transformation>
 *   </transformations>
 *   .........
 *   .........
 * </transform>
 * </pre>
 *
 */
public class TransformAdapterProcess extends AbstractAdapterProcess {

    /**
     * Stack used to store away the various Transformation states that this
     * process can be in
     */
    private Stack states = new Stack();

    /**
     * The operation process that this adapter is delegating to.
     */
    TransformOperationProcess operation;

    /**
     * The attributes that are passed in via the processAttributes method
     */
    private Attributes attributes;

    /**
     * Creates a new TransformAdapterProcess instance
     */
    public TransformAdapterProcess() {
        operation = new TransformOperationProcess();
        setDelegate(operation);
    }

    /**
     * Get the URI of the namespace associated with this process
     * @return the URI of the namespace associated with this process
     */
    String getProcessNamespaceURI() {
        return processNamespaceURI;
    }

    // javadoc inherited
    public void stopProcess() throws SAXException {
        operation.endTransform();
        operation.stopProcess();
    }

    // javadoc inherited
    public void startProcess() throws SAXException {
        // move to the intial transform state
        changeState(new Transform(this),
                    processNamespaceURI,
                    processLocalName,
                    processPrefixedName,
                    attributes);
    }

    // javadoc inherited
    public void processAttributes(Attributes attributes) throws SAXException {
        this.attributes = attributes;
    }

    // javadoc inherited
    public void setPipeline(XMLPipeline pipeline) {
        super.setPipeline(pipeline);
        // initialize the operation process
        operation.setPipeline(pipeline);
    }

    // Javadoc inherited
    public void characters(char ch[],
                           int start,
                           int length) throws SAXException {
        getCurrentState().characters(ch, start, length);
    }

    // Javadoc inherited
    public void endDocument() throws SAXException {
        getCurrentState().endDocument();
    }

    // Javadoc inherited
    public void endElement(String namespaceURI,
                           String localName,
                           String qName) throws SAXException {
        getCurrentState().endElement(namespaceURI, localName, qName);
    }

    // Javadoc inherited
    public void endPrefixMapping(String prefix) throws SAXException {
        getCurrentState().endPrefixMapping(prefix);
    }

    // Javadoc inherited
    public void ignorableWhitespace(char ch[],
                                    int start,
                                    int length) throws SAXException {
        getCurrentState().ignorableWhitespace(ch, start, length);
    }

    // Javadoc inherited
    public void processingInstruction(String target, String data)
            throws SAXException {
        getCurrentState().processingInstruction(target, data);
    }

    // Javadoc inherited
    public void setDocumentLocator(Locator locator) {
        try {
            getCurrentState().setDocumentLocator(locator);
        } catch (SAXException e) {
            throw new ExtendedRuntimeException(
                    "Failed to forward a setDocument locator event", e);
        }
    }

    // Javadoc inherited
    public void skippedEntity(String name) throws SAXException {
        getCurrentState().skippedEntity(name);
    }

    // Javadoc inherited
    public void startDocument() throws SAXException {
        getCurrentState().startDocument();
    }

    // Javadoc inherited
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes atts) throws SAXException {
        getCurrentState().startElement(namespaceURI, localName, qName, atts);
    }

    // Javadoc inherited
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        getCurrentState().startPrefixMapping(prefix, uri);
    }

    /**
     * Change the state of this process. The new TransformationState will be
     * content handler that receives the xml content for processing
     * @param state the TransformationState to change to.
     * @param namespaceURI the namespace URI that the state is associated with
     * @param localName the local name associated with the new state
     * @param qName the qualified name associated with the new state
     * @param atts the Attributes associated with the new state.
     * @throws SAXException if an error occurs
     */
    void changeState(TransformationState state, String namespaceURI,
                     String localName, String qName, Attributes atts)
            throws SAXException {
        states.push(state);
        state.initialise(namespaceURI, localName, qName, atts);
    }

    /**
     * Pop the current TransformationState and revert back to the previous
     * state.
     * @param expected the Transformation state that is expected to be popped
     * @throws SAXException if the expected state was not at the top of the
     * stack
     */
    void popState(TransformationState expected) throws SAXException {
        TransformationState popped = (TransformationState)states.pop();
        if (popped != expected) {
            fatalError(new XMLStreamingException(
                    "Unexpected processing state. Transformation markup " +
                    "invalid", getPipelineContext().getCurrentLocator()));
        }
    }

    /**
     * Returns the current processing state.
     * @return the current transformation state.
     */
    private TransformationState getCurrentState() throws SAXException {
        TransformationState state = (TransformationState)states.peek();
        if (null == state) {
            fatalError(new XMLStreamingException(
                    "Transformation markup is not well formed",
                    getPipelineContext().getCurrentLocator()));
        }
        return state;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 30-Apr-04	692/1	adrian	VBM:2004042603 Add parameter passing functionality to transform process

 30-Apr-04	686/1	adrian	VBM:2004042802 Add parameter support for transforms

 27-Apr-04	683/1	adrian	VBM:2004042607 Refactored states out of transform adapter process

 08-Aug-03	268/6	chrisw	VBM:2003072905 Made pushing compilable attribute consistent for both jsp and xml markup

 07-Aug-03	268/4	chrisw	VBM:2003072905 Public API changed for transform configuration

 05-Aug-03	268/2	chrisw	VBM:2003072905 implemented compilable attribute on transform

 22-Jul-03	225/1	doug	VBM:2003071805 Refactored the XMLPipeline interface to reflect the new public API

 18-Jul-03	213/2	doug	VBM:2003071615 Refactored XMLProcess interface

 14-Jul-03	185/1	steve	VBM:2003071402 Refactor exceptions into throwable package

 23-Jun-03	107/5	doug	VBM:2002121803 Removed commented out code

 23-Jun-03	107/3	doug	VBM:2002121803 Transform Adapter tidy up

 ===========================================================================
*/

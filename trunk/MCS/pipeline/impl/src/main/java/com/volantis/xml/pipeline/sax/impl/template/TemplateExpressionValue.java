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
package com.volantis.xml.pipeline.sax.impl.template;

import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.atomic.AtomicSequence;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.pipeline.sax.XMLHandlerAdapter;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.XMLProcessImpl;
import com.volantis.xml.sax.ExtendedSAXParseException;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Provides a means of generating an expression result from a template
 * parameter value. This must be passed a pipeline factory capable of creating
 * preprocessing pipeline processes in order for this mechanism to work.
 */
public class TemplateExpressionValue
        extends AtomicSequence
        implements StringValue {

    /**
     * The name of the parameter whose value this represents.
     */
    private final String parameter;

    /**
     * The value from which the result should be generated. It must be
     * noted that this value has an evaluation mode and therefore must
     * be managed much the same as done in the
     * {@link com.volantis.xml.pipeline.sax.impl.template.value.ValueInsertRule}.
     */
    private final TValue value;


    /**
     * The factory that must be used to create the pipeline through which
     * non-immediate mode markup must be pumped in order to ensure that it
     * is correctly preprocessed.
     */
    private final XMLPipelineFactory pipelineFactory;

    /**
     * The context within which this value will be used.
     */
    private final XMLPipelineContext context;

    /**
     * Initializes the new instance with the given parameters.
     *
     * @param value   the template parameter value
     * @param context
     */
    public TemplateExpressionValue(
            String parameter, TValue value, XMLPipelineContext context) {
        super(context.getExpressionContext().getFactory());

        this.parameter = parameter;
        this.value = value;
        this.context = context;
        this.pipelineFactory = context.getPipelineFactory();
    }

    // javadoc inherited
    public StringValue stringValue() throws ExpressionException {
        return factory.createStringValue(contentAsString());
    }

    // javadoc inherited
    public void streamContents(ContentHandler contentHandler)
            throws ExpressionException, SAXException {

        // Create a process wrapper around the content handler.
        XMLHandlerAdapter adapter = new XMLHandlerAdapter();
        adapter.setContentHandler(contentHandler);

        // Stream the contents to the process.
        streamContents(adapter);
    }

    /**
     * Stream the contents of this value to the specified process.
     *
     * @param process The process to which the contents are to be streamed.
     */
    private void streamContents(XMLProcess process)
            throws SAXException {

        XMLPipeline pipeline;

        if (value.requiresDynamicPipeline()) {
            // The value requires an external dynamic pipeline so create one.
            pipeline = pipelineFactory.createDynamicPipeline(context);
        } else {
            // The value has already been processed, so we don't need a
            // dynamic pipeline.
            pipeline = pipelineFactory.createPipeline(context);
        }

        pipeline.addTailProcess(process);

        value.insert(pipeline, parameter);
    }

    /**
     * Converts the template binding value into a string. This utilizes a
     * special content handler that throws exceptions if complex markup is
     * found.
     *
     * @return a string representation of the template binding value
     * @throws ExpressionException if the content contains complex markup
     */
    private String contentAsString() throws ExpressionException {
        // Collect the string value into an output buffer
        final StringBuffer output = new StringBuffer();

        try {
            // This handler allows a string value to be collated from the value's
            // SAX events. An exception will be thrown by this handler if any
            // non-simple events are encountered
            XMLProcess handler = new XMLProcessImpl() {
                /**
                 * Used to generate a standardized fatal error message.
                 *
                 * @param event the name of the SAX event in which the error
                 *              occurred
                 * @throws SAXException representing the fatal error
                 */
                protected void reportFatalError(String event)
                        throws SAXException {
                    throw new ExtendedSAXParseException("Received invalid " +
                            event +
                            " event while " +
                            "processing Value.stringValue()",
                            getPipelineContext().getCurrentLocator());
                }

                // javadoc inherited
                public void setDocumentLocator(Locator locator) {
                    // Has no impact on final markup so can be ignored
                }

                // javadoc inherited
                public void startDocument() {
                    // Has no impact on final markup so can be ignored
                }

                // javadoc inherited
                public void endDocument() {
                    // Has no impact on final markup so can be ignored
                }

                // javadoc inherited
                public void startPrefixMapping(
                        String prefix,
                        String uri) {
                    // Specifically does nothing. This is needed because
                    // prefix mapping events may propagate even when the
                    // element(s) to which they belong have been consumed
                }

                // javadoc inherited
                public void endPrefixMapping(String prefix) {
                    // Specifically does nothing. This is needed because
                    // prefix mapping events may propagate even when the
                    // element(s) to which they belong have been consumed
                }

                // javadoc inherited
                public void startElement(
                        String namespaceURI,
                        String localName,
                        String qName,
                        Attributes attrs)
                        throws SAXException {
                    reportFatalError("startElement");
                }

                // javadoc inherited
                public void endElement(
                        String namespaceURI,
                        String localName,
                        String qName) throws SAXException {
                    reportFatalError("endElement");
                }

                // javadoc inherited
                public void characters(
                        char[] chars,
                        int start,
                        int length) {
                    output.append(chars, start, length);
                }

                // javadoc inherited
                public void ignorableWhitespace(
                        char[] chars,
                        int start,
                        int length) {
                }

                // javadoc inherited
                public void processingInstruction(
                        String target,
                        String data)
                        throws SAXException {
                    reportFatalError("processingInstruction");
                }

                // javadoc inherited
                public void skippedEntity(String name) throws SAXException {
                    reportFatalError("skippedEntity");
                }
            };

            streamContents(handler);

        } catch (SAXException e) {
            throw new ExpressionException(e);
        }

        return output.toString();
    }

    // javadoc inherited
    public String asJavaString() {
        try {
            return contentAsString();
        } catch (ExpressionException e) {
            throw new ExtendedRuntimeException(e);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Mar-05	7294/1	geoff	VBM:2005022311 Remote Repository Exceptions

 04-Mar-05	7247/1	geoff	VBM:2005022311 Remote Repository Exceptions

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Aug-04	828/3	pcameron	VBM:2004072012 Fixed layout:getPaneInstance with a parameter and nested contexts

 16-Aug-04	828/1	pcameron	VBM:2004072012 Fixed layout:getPaneInstance with a parameter and nested contexts

 16-Aug-04	826/1	pcameron	VBM:2004072012 Fixed layout:getPaneInstance with a parameter and nested contexts

 09-Aug-04	796/2	pcameron	VBM:2004072012 Fixed layout:getPaneInstance with a parameter

 11-Aug-03	275/2	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 01-Aug-03	258/4	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 31-Jul-03	222/3	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 10-Jun-03	13/1	philws	VBM:2003030610 Integrate with Template Model Expression facilities

 ===========================================================================
*/

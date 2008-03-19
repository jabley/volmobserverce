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

import com.volantis.shared.dependency.Dependency;
import com.volantis.shared.dependency.DependencyContext;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.operation.AbstractOperationProcess;
import com.volantis.xml.pipeline.sax.recorder.PipelinePlayer;
import com.volantis.xml.pipeline.sax.recorder.PipelineRecorder;
import com.volantis.xml.pipeline.sax.recorder.PipelineRecording;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Represents a named parameter for which a value can be substituted that
 * has its own content.
 * <p/>
 * This implementation permits direct management of a SAXEventRecorder into
 * which SAX events are recorded for playback (which is invoked via
 * {@link TValue#insert}).
 * <p/>
 * It is required that the SAX events recorded were gathered from a pipeline
 * in an appropriate processing mode based on the value's
 * {@link #evaluationMode}.
 */
public class DirectValue
        implements TValue {

    /**
     * Identifies how the value should be evaluated.
     */
    private transient EvaluationMode evaluationMode;

    private final Complexity complexity;

    private final Dependency dependency;

    /**
     * Used to hold the SAX events for the value for replay when {@link
     * #insert} is called.
     */
    private PipelineRecording recording;

    /**
     * Holds the verification status for the value.
     */
    private boolean verified = false;

    /**
     * Initializes the new instance using the parameters given.
     *
     * @param recording  the source of the recorded SAX events
     * @param complexity
     * @param evaluationMode
     * @param dependency
     */
    public DirectValue(
            PipelineRecording recording, Complexity complexity,
            EvaluationMode evaluationMode, Dependency dependency) {
        this.recording = recording;
        this.complexity = complexity;
        this.evaluationMode = evaluationMode;
        this.dependency = dependency;
    }

    // javadoc inherited
    public void insert(XMLPipeline target, String parameter)
            throws SAXException {

        XMLPipelineContext context = target.getPipelineContext();

        if (evaluationMode == EvaluationMode.DEFERRED) {

            // Get a new event recorder to be populated with the required
            // events. This is needed because we only want to preprocess
            // the events once, on first use of this value, in DEFERRED mode,
            // thereafter simply replaying these preprocessed events
            XMLPipelineFactory factory = context.getPipelineFactory();
            PipelineRecorder processedEventRecorder =
                    factory.createPipelineRecorder();
            processedEventRecorder.startRecording(target);

            // The following process collects the preprocessed events
            // without permitting the events to be replayed to the rest
            // of the pipeline. This allows the preprocessing to be
            // performed before checking the complexity of the SAX
            // events
            XMLProcess process = processedEventRecorder.getRecordingProcess();

            // Make sure that the pipeline will direct all preprocessed
            // SAX events to the new event recorder
            target.addHeadProcess(process);

            // This will cause the original event recorder to send the
            // original SAX events to the target pipeline which will
            // then preprocess them (we assume) and send them to the
            // event collection process
            try {
                PipelinePlayer player = recording.createPlayer();
                player.play(target.getPipelineProcess());
                recording = processedEventRecorder.stopRecording();
            } finally {
                // Must always remove the collection process so that the
                // pipeline isn't messed up
                target.removeHeadProcess();
            }

            evaluationMode = EvaluationMode.IMMEDIATE;
        }

        // Verify that the event recorder contains compatible markup
        if (complexity != Complexity.COMPLEX) {
            if (evaluationMode == EvaluationMode.REPEATED) {
                // Add a process that will only allow characters and ignorable
                // whitespace events through to perform the complexity testing
                addComplexityChecker(target, parameter);
            } else if (recording.isComplex()) {
                throw new XMLPipelineException("The " + parameter +
                        " value's type is simple but complex markup " +
                        "has been found", null);
            }
        }

        try {
            // Add the dependency information for this value to the pipeline.
            DependencyContext dependencyContext =
                    context.getDependencyContext();
            dependencyContext.addDependency(dependency);

            XMLProcess process;

            // Select the process to which the content should be sent based
            // on whether it needs to be processed through the dynamic pipeline
            // or not.
            if (evaluationMode == EvaluationMode.REPEATED) {
                // The evaluation mode is repeated so each time the content
                // is played back it needs to be processed through the dynamic
                // process which is accessed through the pipeline process.
                process = target.getPipelineProcess();
            } else {
                // The content has already been processed so send it to the
                // process after the dynamic process. That is either the
                // head process, or if that is null the next process.
                process = target.getHeadProcess();
                if (process == null) {
                    process = target.getPipelineProcess().getNextProcess();
                }
            }

            PipelinePlayer player = recording.createPlayer();
            player.play(process);
        } finally {
            if (complexity != Complexity.COMPLEX &&
                    evaluationMode == EvaluationMode.REPEATED) {
                // Remove the temporary process added above (this was added for
                // complexity testing purposes)
                target.removeHeadProcess();
            }
        }
    }

    // Javadoc inherited.
    public Complexity getComplexity() {
        return complexity;
    }

    // javadoc inherited
    public boolean requiresDynamicPipeline() {
        // Only values with an evaluation mode of immediate do not require a
        // dynamic pipeline to work in.
        return (evaluationMode != EvaluationMode.IMMEDIATE);
    }

    // javadoc inherited
    public void verify() {
        verified = true;
    }

    // javadoc inherited
    public boolean isVerified() {
        return verified;
    }

    /**
     * Creates and adds a process onto the target pipeline which will only
     * permit characters and ignorableWhitespace events to propagate down the
     * pipeline.
     *
     * @param target the pipeline onto which the process should be added
     * @param parameter
     * @throws org.xml.sax.SAXException if there is a problem adding the process to the
     *                                  pipeline
     */
    private void addComplexityChecker(
            XMLPipeline target, final String parameter)
            throws SAXException {

        // @todo could have standard process that does this
        XMLProcess process =
                new AbstractOperationProcess() {
                    // javadoc inherited
                    public void setDocumentLocator(Locator locator) {
                        super.setDocumentLocator(locator);
                    }

                    // javadoc inherited
                    public void startPrefixMapping(
                            String prefix,
                            String uri) {
                        // Quietly consume these events as they can come through
                        // from a pipeline that used to contain complex markup
                        // but does no longer
                        // @todo later re-introduce the error if updates allow it
                    }

                    // javadoc inherited
                    public void endPrefixMapping(String prefix) {
                        // Quietly consume these events as they can come through
                        // from a pipeline that used to contain complex markup
                        // but does no longer
                        // @todo later re-introduce the error if updates allow it
                    }

                    // javadoc inherited
                    public void startElement(
                            String namespaceURI,
                            String localName,
                            String qName,
                            Attributes attrs)
                            throws SAXException {
                        error();
                    }

                    // javadoc inherited
                    public void endElement(
                            String namespaceURI,
                            String localName,
                            String qName)
                            throws SAXException {
                        error();
                    }

                    // javadoc inherited
                    public void characters(
                            char[] chars,
                            int start,
                            int length)
                            throws SAXException {
                        super.characters(chars, start, length);
                    }

                    // javadoc inherited
                    public void ignorableWhitespace(
                            char[] chars,
                            int start,
                            int length)
                            throws SAXException {
                        super.ignorableWhitespace(chars, start, length);
                    }

                    // javadoc inherited
                    public void processingInstruction(
                            String target,
                            String data)
                            throws SAXException {
                        error();
                    }

                    // javadoc inherited
                    public void skippedEntity(String name)
                            throws SAXException {
                        error();
                    }

                    private void error() throws SAXException {
                        throw new XMLPipelineException("The '" + parameter +
                                "' parameter was declared to be simple " +
                                "but complex markup was produced during " +
                                "evaluation",
                                null);
                    }
                };

        target.addHeadProcess(process);
    }

    /**
     * In order to ensure that the EvaluationMode is handled in a serialization
     * safe manner the internal name representation is explicitly serialized to
     * the output stream instead of the referenced object (note that the evaluationMode
     * attribute is marked transient to prevent it from being serialized).
     * <p>
     * This method must be balanced by an equivalent implementation of
     * {@link #readObject}.
     */
    private void writeObject(ObjectOutputStream out)
            throws IOException {
        out.defaultWriteObject();

        out.writeUTF(evaluationMode.toString());
    }

    /**
     * In order to ensure that the EvaluationMode is handled in a serialization
     * safe manner the internal name representation is explicitly de-serialized
     * from the input stream and is converted back into the required type-safe
     * enumeration literal.
     * <p>
     * This method balances the equivalent implementation of
     * {@link #writeObject}.
     */
    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {

        in.defaultReadObject();

        evaluationMode = EvaluationMode.literal(in.readUTF());
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

 04-Aug-03	294/1	allan	VBM:2003070709 Fixed merge conflicts

 31-Jul-03	217/6	allan	VBM:2003071702 Fixed javadoc issue with contains and containsIdentity.

 31-Jul-03	217/3	allan	VBM:2003071702 Fixed javadoc issue with contains and containsIdentity.

 31-Jul-03	217/1	allan	VBM:2003071702 Made HTTPMessageEntities into a set.

 31-Jul-03	222/2	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 18-Jul-03	213/1	doug	VBM:2003071615 Refactored XMLProcess interface

 23-Jun-03	95/1	doug	VBM:2003061605 Document Event Filtering changes

 16-Jun-03	78/2	philws	VBM:2003061205 Add JSP test cases and debug some issues

 10-Jun-03	13/2	philws	VBM:2003030610 Integrate with Template Model Expression facilities

 ===========================================================================
*/

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

package com.volantis.xml.pipeline.sax.impl.operations.tryop;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.shared.dependency.DependencyContext;
import com.volantis.shared.dependency.Tracking;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.pipeline.sax.CompositeXMLPipelineException;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.XMLPipelineProcess;
import com.volantis.xml.pipeline.sax.impl.validation.Element;
import com.volantis.xml.pipeline.sax.impl.validation.ValidationProcess;
import com.volantis.xml.pipeline.sax.recorder.PipelinePlayer;
import com.volantis.xml.pipeline.sax.recorder.PipelineRecorder;
import com.volantis.xml.pipeline.sax.recorder.PipelineRecording;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the list of exceptions collated by the various attempts.
 */
public class TryProcess
        extends ValidationProcess
        implements TryModel {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(TryProcess.class);

    /**
     * Error message to provide to a Composite exception if the preferred and
     * alternative child operations all failed.
     */
    private static final String ERROR_MSG =
            "The preferred operation and all alternative operations failed.";

    /**
     * The client to inform that error recovery should begin if an error occurs
     */
    private final ErrorListener listener;

    /**
     * The list of exceptions collated by the various attempts.
     */
    private final List exceptions;

    private XMLPipelineContext.RecoveryPoint recoveryPoint;

    private PipelineRecorder recorder;
    private boolean inErrorRecoveryMode;
    private DependencyContext dependencyContext;


    /**
     * Initialise.
     *
     * @param errorListener
     */
    public TryProcess(ErrorListener errorListener) {
        super(TrySchema.EXPECT_TRY_START, 2, "try");

        this.listener = errorListener;
        this.exceptions = new ArrayList();
    }

    // javadoc inherited
    public void setPipeline(XMLPipeline pipeline) {
        super.setPipeline(pipeline);

        XMLPipelineContext context = getPipelineContext();
        dependencyContext = context.getDependencyContext();
    }

    // Javadoc inherited.
    public void startTry() {
        startElement(TrySchema.TRY);

        // Push a dependency tracker on the stack to track the dependencies
        // within the separate blocks of content.
        dependencyContext.pushDependencyTracker(Tracking.INHERIT);
    }

    // Javadoc inherited.
    public void endTry() {

        // Pop the dependency tracker, any dependencies that it may have
        // contained have either been propagated up to the parent, or are not
        // needed.
        dependencyContext.popDependencyTracker();

        endElement(TrySchema.TRY);
    }

    // Javadoc inherited.
    public void addException(SAXParseException exception) {
        exceptions.add(exception);
    }

    // Javadoc inherited.
    public List getExceptions() {
        return exceptions;
    }

    // Javadoc inherited.
    public void clearExceptions() {
        exceptions.clear();
    }

    // Javadoc inherited.
    public SAXParseException getException(XMLPipelineContext context) {
        SAXParseException exception = null;
        if (exceptions.size() != 0) {
            Throwable[] throwables = new Throwable[exceptions.size()];
            exceptions.toArray(throwables);
            exception = new CompositeXMLPipelineException(ERROR_MSG,
                    context.getCurrentLocator(), throwables);
        }
        return exception;
    }

    // Javadoc inherited.
    public void startBlock(Element element)
            throws SAXException {

        startElement(element);

        XMLPipelineContext context = getPipelineContext();
        recoveryPoint = context.addRecoveryPoint();
        XMLPipelineProcess pipeline =
                (XMLPipelineProcess) getPipeline().getPipelineProcess();
        XMLPipelineFactory factory = context.getPipelineFactory();
        recorder = factory.createPipelineRecorder();
        recorder.startRecording(pipeline);
        contentHandler = recorder.getRecordingHandler();
    }

    // Javadoc inherited.
    public void endBlock(Element element) throws SAXException {
        XMLPipelineContext context = getPipelineContext();
        if (!inErrorRecoveryMode) {

            // There was no error encountered during the processing of this
            // block of content.
            //
            // Propagate the dependencies upwards.
            dependencyContext.propagateDependencies();

            // Play the content back.
            PipelineRecording recording = recorder.stopRecording();
            PipelinePlayer player = recording.createPlayer();
            player.play(getNextProcess());

            // Remove the recovery point and keep the changes that have been
            // made to the pipeline context.
            context.removeRecoveryPoint(recoveryPoint, true);

            clearExceptions();

        } else {

            // An error occurred in this block.
            //
            // Discard all the changes made to the context since the recovery
            // point was created.
            context.removeRecoveryPoint(recoveryPoint, false);

            // Exit error recovery mode so that the next block can be tried.
            context.exitErrorRecoveryMode();

            // There was an error in the content so discard the dependencies.
            dependencyContext.clearDependencies();
        }

        inErrorRecoveryMode = false;
        contentHandler = getNextProcess();
        recorder = null;
        recoveryPoint = null;

        endElement(element);
    }

    // Javadoc inherited.
    public void error(SAXParseException exception) throws SAXException {
        recoverError(exception);
    }

    // Javadoc inherited.
    public void fatalError(SAXParseException exception) throws SAXException {
        recoverError(exception);
    }

    /**
     * Start error recovery mode by informing the listener and pipeline context
     *
     * @param exception The error from which we want to recover.
     * @throws SAXException If an error occured whilst attempt to recover from
     *                      an error!
     */
    protected void recoverError(SAXParseException exception)
            throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("An error was encountered. " +
                    "Invoking error recovery", exception);
        }
        inErrorRecoveryMode = true;
        listener.startErrorRecovery();
        XMLPipelineContext context = getPipelineContext();
        context.enterErrorRecoveryMode();
        if (logger.isDebugEnabled()) {
            logger.debug("Attempt failed ", exception);
        }

        addException(exception);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 13-Aug-03	331/1	adrian	VBM:2003081001 implemented try operation

 ===========================================================================
*/

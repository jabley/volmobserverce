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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-May-03    Doug            VBM:2003030405 - Created. An XMLProcess to use
 *                              as the preprocessing XMLProcess in a pipeline
 *                              of AdapterProcess instances. A
 *                              SimpleDynamicProcess is responsible
 *                              for identify pipeline markup, creating the
 *                              appropriate AdapterProcess and then adding
 *                              the process to the pipeline.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.impl.dynamic;

import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.pipeline.sax.XMLPipelineProcess;
import com.volantis.xml.pipeline.sax.XMLPipelineProcessImpl;
import com.volantis.xml.pipeline.sax.XMLPreprocessingPipelineProcessImpl;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.PassThroughController;
import com.volantis.xml.pipeline.sax.flow.FlowControlManager;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Stack;

/**
 * Basic implementation of the {@link DynamicProcess} interface
 */
public class SimpleDynamicProcess
        extends XMLPreprocessingPipelineProcessImpl
        implements DynamicProcess {

    /**
     * Counter used to record the element nesting count. Every time this
     * process revieves a {@link #startElement} or {@link #startDocument}
     * event, this count is incremented. Every {@link #endElement} or
     * {@link #endDocument} event causes this count to be decremented
     */
    private int nesting;

    /**
     * Used to record that nesting count when the last call to
     * {@link #addErrorRecoveryPoint} was made
     */
    private int lastRecoveryPoint;

    /**
     * Used to record the level at which the nesting count needs to fall to
     * for pass through to be completed. When this variable is -1 pass
     * through is not being performed
     */
    private int passThroughExitLevel;

    /**
     * Array of PassThroughControllers that help provide the "pass through"
     * functionality
     */
    private PassThroughController[] passThroughControllers;

    /**
     * The DynamicProcessConfiguration for this pipeline process
     */
    private DynamicProcessConfiguration configuration;

    /**
     * Stack record error recovery points in the markup
     */
    private Stack recoveryPoints;

    /**
     * Creates a new SimpleDynamicProcess instance
     */
    private SimpleDynamicProcess(XMLPipelineContext context,
                                 DynamicProcessConfiguration configuration) {
        super(context);

        if (configuration == null) {
            if (context == null) {
                throw new IllegalArgumentException("At least one of context " +
                        "and configuration must not be null");
            }

            configuration = getDynamicConfiguration(context);
        }

        this.configuration = configuration;
        this.nesting = 0;
        this.lastRecoveryPoint = -1;
        this.passThroughExitLevel = -1;
        this.recoveryPoints = new Stack();
    }

    /**
     * Creates a new SimpleDynamicProcess instance
     */
    public SimpleDynamicProcess(DynamicProcessConfiguration configuration) {
        this(null, configuration);
    }

    /**
     * Creates and starts a new SimpleDynamicProcess instance.
     * @param context The context within which the process will run.
     */
    public SimpleDynamicProcess(XMLPipelineContext context)
            throws SAXException {
        this(context, null);
        startProcess();
    }



    public static DynamicProcessConfiguration getDynamicConfiguration(
            XMLPipelineContext context) {

        XMLPipelineConfiguration configuration =
                context.getPipelineConfiguration();

        return getDynamicConfiguration(configuration);

    }

    public static DynamicProcessConfiguration getDynamicConfiguration(
            XMLPipelineConfiguration configuration) {
        // retrieve the DynamicProcessConfiguration from the pipeline
        // configuration
        DynamicProcessConfiguration dynamicConfig =
                (DynamicProcessConfiguration)
                configuration.retrieveConfiguration(
                        DynamicProcessConfiguration.class);

        if (null == dynamicConfig) {
            // Dynamic process configuration could not be retieved
            throw new IllegalStateException(
                    "Could not retrieve the DynamicProcessConfiguration " +
                    "from the XMLPipelineConfiguratin");
        }

        return dynamicConfig;
    }

    // javadoc inherited
    public void startProcess() throws SAXException {
        super.startProcess();

        // create a process that executes rules
        DynamicRuleProcess dynamicRuleProcess
                = new DynamicRuleProcess(this, configuration);

        // create a process that can evaluate expressions
        ExpressionProcess expressionProcess = createExpressionProcess();

        // store away these process as references to them are need in order
        // to perform pass through
        passThroughControllers = new PassThroughController[2];
        passThroughControllers[0] = dynamicRuleProcess;
        passThroughControllers[1] = expressionProcess;

        // create a pipeline process that will contain the individual
        // process that together provide the functionality of this dynamic
        // process
        XMLPipelineProcess preprocessor = new XMLPipelineProcessImpl();

        // add the processes
        preprocessor.addHeadProcess(dynamicRuleProcess);
        preprocessor.addHeadProcess(expressionProcess);
        preprocessor.addHeadProcess(new FlowControlProcess());

        // set the pipeline as the preprocessor
        this.initialisePreprocessor(preprocessor);
    }

    /**
     * Creates an expression process.
     * @return an expression process.
     */
    protected ExpressionProcess createExpressionProcess() {
        return new ExpressionProcess();
    }

    // Javadoc inherited
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes atts) throws SAXException {
        // increment the element counter
        nesting++;

        // forward the event
        super.startElement(namespaceURI, localName, qName, atts);
    }

    // Javadoc inherited
    public void endElement(String namespaceURI,
                           String localName,
                           String qName) throws SAXException {
        // decrement the element counter
        nesting--;
        if (inPassThroughMode() && (passThroughExitLevel == nesting + 1)) {
            stopPassThrough();
        }
        if (lastRecoveryPoint != -1 && (lastRecoveryPoint == (nesting + 1))) {
            popErrorRecoveryPoint();
        }
        // forward the event
        super.endElement(namespaceURI, localName, qName);
    }

    // Javadoc inherited
    public void endDocument() throws SAXException {
        // decrement the element counter
        nesting--;

        if (inPassThroughMode() && (passThroughExitLevel == (nesting + 1))) {
            stopPassThrough();
        }
        if (lastRecoveryPoint != -1 && (lastRecoveryPoint == (nesting + 1))) {
            popErrorRecoveryPoint();
        }
        // forward the event
        super.endDocument();
    }

    // Javadoc inherited
    public void startDocument() throws SAXException {
        // increment the element counter
        nesting++;

        // forward the event
        super.startDocument();
    }

    // javadoc inherited
    public void addProcess(XMLProcess process)
            throws SAXException {
        getPipeline().addHeadProcess(process);
    }

    // javadoc inherited
    public XMLProcess removeProcess()
            throws SAXException {
        return getPipeline().removeHeadProcess();
    }

    // Javadoc inherited.
    public void removeProcess(XMLProcess expected)
            throws SAXException {

        XMLProcess removed = removeProcess();
        // check that the correct process was removed.
        if (expected != removed) {
            XMLPipelineContext context = getPipelineContext();
            XMLPipelineException error = new XMLPipelineException(
                    "Expected to remove process " + expected +
                    "from the pipeline, but actually removed " + removed,
                    context.getCurrentLocator());
            // send the error down the pipeline
            fatalError(error);
        }
    }

    // javadoc inherited
    public boolean inPassThroughMode() {
        // when passThroughExitLevel = -1 then we cannot be in passthrough
        // mode. It does not make sense for passThroughExitLevel to be equal
        // to 0 as this level means that all the elements are balanced.
        return (passThroughExitLevel > 0);
    }

    // javadoc inherited
    public void passThroughElementContents()
            throws IllegalStateException {
        if (inPassThroughMode()) {
            throw new IllegalStateException(
                    "Cannot perform pass through as this dynamic process " +
                    "is already in pass through mode");
        }
        startPassThrough();
    }

    // javadoc inherited
    public void addErrorRecoveryPoint() {
        recoveryPoints.push(new Integer(nesting));
        lastRecoveryPoint = nesting;
    }

    // javadoc inherited
    public void skipToErrorRecoveryPoint()
            throws IllegalStateException {

        // work out the element levels that need to be skipped
        int levelsToSkip = nesting - lastRecoveryPoint + 1;

        FlowControlManager flowControlManager =
                getPipelineContext().getFlowControlManager();

        // skip the required nesting levels
        flowControlManager.exitNestingLevels(levelsToSkip);
    }

    // javadoc inherited
    public void cleanupProcesses(XMLProcess process) {
        while (getProcessCount() > 0 && getHeadProcess() != process) {
            try {
                removeHeadProcess();
            } catch (SAXException e) {
                throw new ExtendedRuntimeException(e);
            }
        }
    }

    /**
     * Method used to start pass through
     */
    private void startPassThrough() {
        passThroughExitLevel = nesting;
        // inform all the PassThroughControllers that pass through is starting
        for (int i = 0; i < passThroughControllers.length; i++) {
            passThroughControllers[i].startPassThrough();
        }
    }

    /**
     * Method used to stop pass through
     */
    private void stopPassThrough() {
        passThroughExitLevel = -1;
        // inform all the PassThroughControllers that pass through is stoping
        for (int i = 0; i < passThroughControllers.length; i++) {
            passThroughControllers[i].stopPassThrough();
        }
    }

    /**
     * Method that pops the top recovery point off the stack of recovery
     * points. It also updates the lastRecoveryPoint member so that is
     * reflects the new recovery point at the top of the stack.
     */
    private void popErrorRecoveryPoint() {
        // pop the top recovery point off of the stack
        recoveryPoints.pop();

        if (!recoveryPoints.isEmpty()) {
            // see if there is another recovery point
            Integer recoveryPoint = (Integer)recoveryPoints.peek();
            lastRecoveryPoint = recoveryPoint.intValue();
        } else {
            lastRecoveryPoint = -1;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Jun-05	8751/3	schaloner	VBM:2005060711 Updated code style

 15-Jun-05	8751/1	schaloner	VBM:2005060711 ExpressionProcess and PipelineExpressionHelper can now support multiple expression declaration markup

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 30-Jan-04	531/1	adrian	VBM:2004011905 added context updating and context annotation support to pipeline processes

 31-Oct-03	440/5	doug	VBM:2003102911 Fixed problem with non standard characters

 31-Oct-03	440/3	doug	VBM:2003102911 Added Flow control process to tail of all pipelines

 13-Aug-03	331/1	adrian	VBM:2003081001 implemented try operation

 11-Aug-03	275/6	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 06-Aug-03	301/3	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 05-Aug-03	290/1	doug	VBM:2003080412 Provided DynamicElementRule implementation for adding Adapters to a pipeline

 04-Aug-03	285/1	doug	VBM:2003080402 Renamed XMLProcessConfiguration interface to Configuration

 01-Aug-03	258/1	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 28-Jul-03	232/1	doug	VBM:2003071804 Refactored XMLPipelineContext to reflect new public API

 25-Jul-03	242/1	steve	VBM:2003072310 Implement namespace package and refactor exitsting code to fit it

 22-Jul-03	225/2	doug	VBM:2003071805 Refactored the XMLPipeline interface to reflect the new public API

 18-Jul-03	213/1	doug	VBM:2003071615 Refactored XMLProcess interface

 06-Jun-03	26/1	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/

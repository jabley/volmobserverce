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

package com.volantis.xml.pipeline.sax.dynamic;

import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import org.xml.sax.SAXException;

/**
 * A dynamic process modifies the set of processes within the pipeline based
 * on the events it receives.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation. User implementations of this interface are highly likely to
 * be incompatible with future releases of the product at both binary and source
 * levels.</strong></p>
 *
 * <p>It looks for element events for special elements and when it finds them
 * it applies a set of preconfigured rules. These rules can do all sorts of
 * different things, including but not limited to adding and removing processes
 * to and from the pipeline, recovering from errors, activating flow control.
 * </p>
 * <p>In addition to the above a dynamic process also supports the following.<p>
 * <ul>
 *   <li>Error recovery</li>
 *   <li>Flow control</li>
 *   <li>Attribute Expression Evaluation (this is done before invoking any of
 * the rules).</li>
 *   <li>XML Base handling. (This is done after attribute expression evaluation
 * so it is possible for it to be specified using an expression)</li>
 * </ul>
 * <h1>Pass Through Mode</h1>
 * <p>While in pass through mode the dynamic process does not perform any
 * processing of the events it simply passes them straight through. Among other
 * possible uses the template model uses it to defer the evaluation of pipeline
 * content until it is used within the template body.</p>
 * <p>The {@link #passThroughElementContents} method can be used to control the
 * pass through mode.</p>
 *
 * <h1>Error Recovery</h1>
 * <p>If an error occurs in a process within the pipeline then it is important
 * to be able to recover from it. Error recovery must ensure that after it has
 * finished the pipeline is at a well known point within the source infoset and
 * in a well known state.</p>
 * <p>The main problem with achieving this goal is that when the error occurs
 * most processes within the pipeline will have received an unbalanced set of
 * events, some of which have come from external documents and some of which
 * have come from the source document. In order to recover from errors it is
 * necessary to separate the two sets of events but this is impossible for most
 * processes within the pipeline.</p>
 * <p>The one process that can distinguish between them is this process (as it
 * only sees events from the source infoset) and hence the reason why this
 * process supports error recovery.</p>
 * <p>It is not possible for a dynamic process to recover from errors if it has
 * received a fatal error in its input infoset. Therefore, if it does detect one
 * then it ignores any attempt at error recovery.</p>
 * <p>An operation/process that supports recovering from errors, e.g. the
 * &lt;try&gt; operation, has to perform the following steps in order to
 * recover from errors.</p>
 * <ol>
 * <li>When the operation starts it must add an error recovery point.</li>
 * <li>If it receives an error event and it can recover from the error then it
 * must ask the pipeline to start error recovery.</li>
 * <li>If either the operation or this process cannot perform error recovery
 * for some reason then the operation must forward the event on as normal.</li>
 * </ol>
 *
 * <h2>Error Recovery Point</h2>
 * <p>Setting an error recovery point is done in a very similar way to setting
 * a flow control destination point. In fact the error recovery point is used
 * as the destination for flow control.</p>
 *
 * <h2>Activating Error Recovery</h2>
 * <p>When error recovery is activated this process uses the flow control
 * mechanism to advance to the error recovery point. It also uses the facilities
 * provided by XMLPipelineContext to recover the state.</p>
 *
 * <h2>Error Recovery Mode</h2>
 * <p>Being in error recovery mode has the following effects on this process.
 * </p>
 * <p>In error recovery mode all events are ignored until the error recovery
 * point is reached at which point error recovery is removed. In addition this
 * process removes all processes from the pipeline up until the point.</p>
 *
 * <h2>Error Cleanup</h2>
 * <p>If this process detects an error on its input, or is asked to begin error
 * recovery then it cleans up the pipeline as follows.</p>
 * <p>Starting with the first process it removes it from the pipeline, releases
 * its resources (by invoking {@link XMLProcess#release}) and discards it. It
 * does this until it either reaches the end of the processes that it added to
 * the pipeline, or reaches the process that initiated error recovery.</p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @mock.generate 
 */
public interface DynamicProcess
        extends XMLProcess {

    /**
     * The volantis copyright statement
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Get the pipeline context.
     *
     * @return The pipeline context, or null if this process is not in a
     *         pipeline.
     */
    public XMLPipelineContext getPipelineContext();

    /**
     * Adds the process after the dynamic process.
     *
     * @param process The process to add.
     * @throws SAXException If an error occurred while adding the process.
     */
    public void addProcess(XMLProcess process)
            throws SAXException;

    /**
     * Removes the process after the dynamic process.
     *
     * @return The process removed.
     * @throws SAXException If an error occurred while removing the process.
     */
    public XMLProcess removeProcess()
            throws SAXException;

    /**
     * Removes the process after the dynamic process.
     *
     * <p>If the expected process is not the one after the dynamic process
     * then this forwards a fatal error.</p>
     *
     * @param expected The expected process.
     * @throws SAXException If an error occurred while removing the process.
     */
    public void removeProcess(XMLProcess expected)
            throws SAXException;

    /**
     * Check whether this process is currently in pass through mode.
     * @return True if this process is currently in pass through mode and false
     * otherwise.
     */
    public boolean inPassThroughMode();

    /**
     * Causes the current elements contents to be passed through unprocessed by
     * this dynamic process.
     *
     * <p>All events received after calling this method but before the end
     * element event for the current element are passed through unprocessed.</p>
     * <p>Unprocessed means that no attribute expressions are evaluated and no
     * dynamic element rules are invoked.</p>
     * <p>This method puts this process in pass through mode. If it is called
     * while it is already in pass through mode then this method will throw an
     * {@link IllegalStateException}.</p>
     *
     * @throws IllegalStateException If this process is already in pass through
     * mode.
     */
    public void passThroughElementContents()
            throws IllegalStateException;

    /**
     * Add an error recovery point.
     * <p>The point is in the same location as the flow control point would be
     * if the {@link com.volantis.xml.pipeline.sax.flow.FlowControlManager#exitCurrentElement}
     * method was called.</p>
     * <p>The error recovery point is removed automatically when it is reached,
     * whether it was used or not.</p>
     */
    public void addErrorRecoveryPoint();

    /**
     * Skip to the error recovery point.
     * <p>This uses flow control to skip to the error recovery point.</p>
     * @see com.volantis.xml.pipeline.sax.flow.FlowControlManager#exitCurrentElement
     * @throws IllegalStateException If there is no suitable error recovery
     * point.
     */
    public void skipToErrorRecoveryPoint()
            throws IllegalStateException;

    /**
     * Cleanup the processes that were added by this dynamic process.
     * <p>This methods cleans up the processes that were added by this dynamic
     * process. It starts with the last one added and continues until it reaches
     * the process specified below at which point it stops. The specified
     * process is not removed. It removes each process, invokes the
     * {@link XMLProcess#release} method and then discards it.</p>
     * @param process The process that causes this method to stop its cleanup.
     */
    public void cleanupProcesses(XMLProcess process);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 16-Jul-03	208/1	doug	VBM:2003071603 Added as part of Pipeline public API

 ===========================================================================
*/

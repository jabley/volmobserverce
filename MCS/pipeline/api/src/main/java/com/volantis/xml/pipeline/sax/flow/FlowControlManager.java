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

package com.volantis.xml.pipeline.sax.flow;

/**
 * Responsible for managing flow control within the pipeline.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation. User implementations of this interface are highly likely to
 * be incompatible with future releases of the product at both binary and source
 * levels.</strong></p>
 *
 * <h2>Managing Flow Control Mode</h2>
 * <p>This class automatically manages the flow control mode and any state
 * associated with it. On entry to flow control mode the
 * {@link FlowController#beginFlowControl} method of all the registered
 * {@link FlowController}s are invoked. On exit from flow control mode the
 * {@link FlowController#endFlowControl} method of all the registered
 * {@link FlowController}s are invoked.</p>
 * <p>The following sections describe how two different flow controllers, one
 * based on events and one on a DOM would support flow control.</p>
 * <h3>Event Based Flow Control</h3>
 * <p>While in flow control mode the event based flow controller must do the
 * following.</p>
 * <ul>
 *   <li>For each startElement event it receives it must call
 * {@link #handleStartElementEvent} and consume the event.</li>
 *   <li>For each endElement event it receives it must call the
 * {@link #handleEndElementEvent}. If it returned true then it must forward the
 * event on, otherwise it should consume it.</li>
 * </ul>
 * <h3>DOM Based Flow Control</h3>
 * <p>While in flow control mode the DOM based flow controller must do the
 * following.</p>
 * <ul>
 *   <li>Move to the parent element.</li>
 *   <li>Call {@link #handleEndElementEvent} method (which must return true).
 * </li>
 *   <li>Generate an endElement event for the element.</li>
 *   <li>Generate any endPrefixMapping events for the element.</li>
 *   <li>Repeat the above steps while in flow control mode.</li>
 * </ul>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface FlowControlManager {

    /**
     * The volantis copyright statement
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Add a flow controller to this manager.
     * @param flowController The flow controller to add.
     */
    public void addFlowController(FlowController flowController);

    /**
     * Remove a flow controller from this manager.
     * @param flowController The flow controller to remove.
     */
    public void removeFlowController(FlowController flowController);

    /**
     * Exits the current element skipping any remaining contents.
     * <p>This is equivalent to calling {@link #exitNestingLevels} with an
     * argument of 1 although operations must not call that method directly.</p>
     */
    public void exitCurrentElement();

    /**
     * Exit the specified number of nesting levels skipping any intermediate
     * content.
     *
     * <p><strong>This method is provided for error recovery purposes only and
     * should not be called by anything other than
     * {@link com.volantis.xml.pipeline.sax.dynamic.DynamicProcess}.</strong>
     * It is possible that future releases may change this method in
     * incompatible ways.</p>
     *
     * <p>If the number of levels to exit is 0 then this has no effect, the
     * pipeline does not enter flow control mode and no events are ignored.</p>
     * <p>If there are no active flow controllers (added to the context but not
     * yet removed) then this throws an {@link java.lang.IllegalStateException}.</p>
     * <p>In the following example if this method is called at point 1 then
     * none of the events for elements b, c, d, or e are allowed through by the
     * flow controller. If this method is called at point 2 then none of the
     * events for elements d or e are allowed through by the flow controller.
     * </p>
     * <pre>
     *     &lt;a&gt;
     *         Point 1
     *         &lt;b&gt;
     *             &lt;c/&gt;
     *         &lt;/b&gt;
     *         Point 2
     *         &lt;d&gt;
     *             &lt;e/&gt;
     *         &lt;/d&gt;
     *         Point 3
     *     &lt;/a&gt;
     * </pre>
     * <p>This method performs the following actions.</p>
     * <ul>
     *   <li>Sets the flow control mode to skip.</li>
     *   <li>Informs all the flow controllers that they should begin flow
     *       control.</li>
     * </ul>
     * @param levels The number of levels to skip, must be 0 or higher.
     * @throws IllegalArgumentException If the levels argument is less than 0.
     */
    public void exitNestingLevels(int levels);

    /**
     * Updates the state assuming that a startElement event has been received.
     * <strong>This method must only be called while in flow control mode.
     * </strong>
     * @throws IllegalStateException If this is called while not in flow control
     * mode.
     */
    public void handleStartElementEvent();

    /**
     * Check whether the pipeline is currently in flow control mode.
     * @return True if in flow control mode and false if it is not.
     */
    public boolean inFlowControlMode();

    /**
     * Check whether the current level needs exiting.
     *
     * @return True if this is in flow control mode and the current level needs
     *         exiting, false otherwise.
     */
    public boolean shouldExitCurrentLevel();

    /**
     * Called to indicate that an endElement event has been received.
     * <strong>This method must only be called while in flow control mode.
     * </strong>
     * <p>This method will update the internal state of this object and
     * depending on that state could cause the pipeline to exit flow control
     * mode which will in turn cause the {@link FlowController#endFlowControl}
     * method of all the registered {@link FlowController}s to be invoked.</p>
     * <p>If this does cause the pipeline to exit flow control mode then
     * the event must be forwarded so this method will return true.</p>
     * @return True if the event must be forwarded in order to maintain well
     * formed set of events and false if it should be ignored.
     * @throws IllegalStateException If this is called while not in flow control
     * mode.
     */
    public boolean handleEndElementEvent();

    /**
     * Checks to see whether in the current state an endPrefixMapping event
     * should be forwarded.
     * <strong>This method must only be called while in flow control mode.
     * </strong>
     * <p>Unlike the {@link #handleEndElementEvent} method this method will
     * not cause the pipeline to exit flow control mode.</p>
     * @return True if the event must be forwarded in order to maintain well
     * formed set of events and false if it should be ignored.
     * @throws IllegalStateException If this is called while not in flow control
     * mode.
     */
    public boolean forwardEndPrefixMappingEvent();
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

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
package com.volantis.xml.pipeline.sax.impl.flow;

import com.volantis.xml.pipeline.sax.flow.FlowControlManager;
import com.volantis.xml.pipeline.sax.flow.FlowController;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Basic implementation of the {@link FlowControlManager} interface
 *
 * <p>There are two main values within this that determine the behaviour of
 * the flow control. The {@link #flowControlDepth} is the depth of the nesting
 * of element events, e.g. it is 0 at start, after seeing one startElement
 * event it is 1, after seeing an endElement event it is 0. The
 * {@link #flowControlThreshold} is the depth above which endElement events
 * must be sent in order to ensure a well balanced event stream.</p>
 */
public class SimpleFlowControlManager implements FlowControlManager {

    /**
     * Flag used to determine whether flow control is currently being
     * performed
     */
    private boolean inFlowControlMode;

    /**
     * Set used to store away all registered <code>FlowControllers</code>
     */
    private Set flowControllers;

    /**
     * Counter that is set to the number of element levels that are being
     * controlled.
     */
    private int flowControlDepth;

    /**
     * Counter used to record the lowest level at which the flowControlLevel
     * counter has reached.
     */
    private int flowControlThreshold;

    /**
     * Flag used to determine whether <code>FlowController</code> objects
     * need to handle endPrefixMapping events
     */
    private boolean forwardEndPrefixMappingEvents;

    /**
     * Creates a new <code>SimpleFlowControlManager</code> instance
     */
    public SimpleFlowControlManager() {
        this.inFlowControlMode = false;
        this.flowControllers = new HashSet();
        this.flowControlDepth = 0;
        this.flowControlThreshold = 0;
        this.forwardEndPrefixMappingEvents = false;
    }

    // javadoc inherited
    public void addFlowController(FlowController flowController) {
        // add the flow controller to the collection of controllers
        flowControllers.add(flowController);
    }

    // javadoc inherited
    public void removeFlowController(FlowController flowController) {
        //   add the flow controller to the collection of controllers
        flowControllers.remove(flowController);
    }

    // javadoc inherited
    public void exitCurrentElement() {
        // to exit the current element we need to exit one nesting level
        exitNestingLevels(1);
    }

    public void exitNestingLevels(int levels) {
        if (levels < 0) {
            throw new IllegalStateException(
                    "nesting level must be greater than or equal to zero");
        } else if (levels > 0) {
            // initialize the
            flowControlDepth = flowControlThreshold = levels;
            beginFlowControl();
        }
    }

    // javadoc inherited
    public void handleStartElementEvent() {
        // ensure that this manager is in flow control mode
        assertInFlowControlMode();

        // increment the nesting level
        flowControlDepth++;

        forwardEndPrefixMappingEvents = false;
    }

    // javadoc inherited
    public boolean handleEndElementEvent() {
        // ensure that this manager is in flow control mode
        assertInFlowControlMode();
        flowControlDepth--;
        boolean handleElement = false;
        if (flowControlDepth == 0) {
            handleElement = true;
            endFlowControl();
        } else if (flowControlDepth < flowControlThreshold) {
            handleElement = true;
            flowControlThreshold = flowControlDepth;
        }
        forwardEndPrefixMappingEvents = handleElement;
        return handleElement;
    }

    // javadoc inherited
    public boolean inFlowControlMode() {
        return inFlowControlMode;
    }

    // javadoc inherited
    public boolean shouldExitCurrentLevel() {
        return inFlowControlMode && flowControlDepth > 0;
    }

    // javadoc inherited
    public boolean forwardEndPrefixMappingEvent() {
        return forwardEndPrefixMappingEvents;
    }

    /**
     * Method that starts all <code>FlowCntrollers</code>
     */
    private void beginFlowControl() {

        // ensure that this manager is not already in flow control mode
        assertNotInFlowControlMode();

        // inform all flow controllers that flow controll has begun
        FlowController flowController;
        for (Iterator i = flowControllers.iterator(); i.hasNext();) {
            flowController = (FlowController)i.next();
            flowController.beginFlowControl();
        }
        inFlowControlMode = true;
    }

    /**
     * Method that stops all <code>FlowControllers</code>
     */
    private void endFlowControl() {
        // ensure that this manager is in flow control mode
        assertInFlowControlMode();

        // inform all flow controllers that flow control has ended
        FlowController flowController;
        for (Iterator i = flowControllers.iterator(); i.hasNext();) {
            flowController = (FlowController)i.next();
            flowController.endFlowControl();
        }
        inFlowControlMode = false;
    }

    /**
     * Method that throws an <code>IllegalStateException</code> if this
     * manager is currently <b>NOT</b> performing flow control
     * @throws IllegalStateException if not in flow control mode
     */
    private void assertInFlowControlMode() {
        if (!inFlowControlMode) {
            throw new IllegalStateException(
                    "cannot perform operation as flow control manager is " +
                    "not currently performing flow control");
        }
    }

    /**
     * Method that throws an <code>IllegalStateException</code> if this
     * manager is currently performing flow control
     * @throws IllegalStateException if in flow control mode
     */
    private void assertNotInFlowControlMode() {
        if (inFlowControlMode) {
            throw new IllegalStateException(
                    "cannot perform operation as flow control manager is " +
                    "already currently performing flow control");

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

 11-Aug-03	275/3	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 11-Aug-03	275/1	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 ===========================================================================
*/

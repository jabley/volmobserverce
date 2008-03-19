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
package com.volantis.shared.environment;

import com.volantis.shared.recovery.RecoverableTransactionStack;

/**
 * Simple implementation of the {@link EnvironmentInteractionTracker}
 * interface
 */
public class SimpleEnvironmentInteractionTracker
        implements RecoverableEnvironmentInteractionTracker {

    /**
     * Stack for storing the EnvironmentInteractions
     */
    private RecoverableTransactionStack environmentInteractions;

    /**
     * Creates a new SimpleEnvironmentInteractionTracker
     * @param rootEnvironmentInteraction the root EnvironmentInteraction
     */
    public SimpleEnvironmentInteractionTracker(
            EnvironmentInteraction rootEnvironmentInteraction) {

        environmentInteractions = new RecoverableTransactionStack();
        if (rootEnvironmentInteraction != null) {
            pushEnvironmentInteraction(rootEnvironmentInteraction);
        }
    }

    /**
     * Creates a new SimpleEnvironmentInteractionTracker
     */
    public SimpleEnvironmentInteractionTracker() {
        this(null);
    }

    // javadoc inherited
    public void pushEnvironmentInteraction(
            EnvironmentInteraction environmentInteraction) {
        if (environmentInteraction == null) {
            throw new IllegalArgumentException(
                    "Cannot push a null EnvironmentInteraction");
        }
        environmentInteractions.push(environmentInteraction);
    }

    // javadoc inherited
    public EnvironmentInteraction popEnvironmentInteraction() {
        if (environmentInteractions.empty()) {
            throw new IllegalStateException("Cannot pop an empty stack");
        }
        return (EnvironmentInteraction)environmentInteractions.pop();
    }

    // javadoc inherited
    public EnvironmentInteraction getCurrentEnvironmentInteraction() {
        return (environmentInteractions.empty()) ? null :
                (EnvironmentInteraction)environmentInteractions.peek();
    }

    // javadoc inherited
    public EnvironmentInteraction getRootEnvironmentInteraction() {
        return (environmentInteractions.empty()) ? null :
                (EnvironmentInteraction)environmentInteractions.get(0);
    }

    // javadoc inherited from RecoverableTransaction interface
    public void startTransaction() {
        environmentInteractions.startTransaction();
    }

    // javadoc inherited from RecoverableTransaction interface
    public void commitTransaction() {
        environmentInteractions.commitTransaction();
    }

    // javadoc inherited from RecoverableTransaction interface
    public void rollbackTransaction() {
        environmentInteractions.rollbackTransaction();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 10-Aug-03	264/1	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 01-Aug-03	258/1	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 24-Jul-03	252/1	doug	VBM:2003072403 Implemented the EnvironmentInteractionTracker interface

 ===========================================================================
*/

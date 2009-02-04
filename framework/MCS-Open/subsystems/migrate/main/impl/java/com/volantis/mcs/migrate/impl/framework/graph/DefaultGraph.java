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
package com.volantis.mcs.migrate.impl.framework.graph;

import com.volantis.mcs.migrate.api.framework.Version;
import com.volantis.mcs.migrate.impl.framework.identification.Step;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Default implementation of {@link Graph}.
 * <p>
 * NOTE: This implementation is currently very limited. It only supports a
 * single path to the target version. Proper graph functionality will be added
 * later when we actually need it.
 */
public class DefaultGraph implements Graph {

//    /**
//     * Used for logging.
//     */
//     private static final LogDispatcher logger =
//             LocalizationFactory.createLogger(DefaultGraph.class);

    /**
     * The version of the product that this graph targets.
     * <p>
     * This version will be the latest/current version of the product. All
     * paths in this graph must end in this version in order to be valid.
     */
    private Version targetVersion;

    /**
     * The collection of steps which this graph knows about.
     */
    private Collection steps = new ArrayList();

    /**
     * Initialise.
     *
     * @param targetVersion the version of the product that this graph targets.
     */
    public DefaultGraph(Version targetVersion) {

        this.targetVersion = targetVersion;
    }

    /**
     * Add a step to the graph.
     * <p>
     * NOTE: due to the currently very limited implementaton, all steps added
     * must form a single path to the target version. They do not have to be
     * added in order though ;-).
     *
     * @param step the step to add.
     */
    public void addStep(Step step) {

        steps.add(step);
    }

    // Javadoc inherited.
    public Iterator getSequence(Version version) {

        if (steps.size() == 0) {
            throw new IllegalStateException(
                    "cannot get a sequence of a graph with no steps");
        }

        // Build up the sequence of steps from the specified version to the
        // target version.
        List path = new ArrayList();

        // Loop around over the steps until no more steps can be added or the
        // path from the specified version to the target version is complete.
        Version inputVersion = version;
        Step addedStep;
        do {
            Version nextVersion = null;
            addedStep = null;
            for (Iterator i = steps.iterator(); i.hasNext();) {
                Step step = (Step) i.next();
                if (step.getInput() == inputVersion) {
                    if (addedStep != null) {
                        throw new IllegalStateException("Multiple steps from " +
                                inputVersion + ", " + addedStep +
                                " and " + step);
                    } else {
                        path.add(step);
                        nextVersion = step.getOutput();
                        addedStep = step;
                    }
                }
            }

            inputVersion = nextVersion;
        }
        while (addedStep != null && inputVersion != targetVersion);

        if (path.size() == 0) {
            throw new IllegalStateException(
                    "version requested not present in graph");
        }

        // Ensure the sequence ends with the target version.
        Step finalStep = (Step) path.get(path.size() - 1);
        if (finalStep.getOutput() != targetVersion) {
            throw new IllegalStateException("Step sequence ends with " +
                    finalStep.getOutput() + " instead of target " +
                    targetVersion);
        }

//        if (logger.isDebugEnabled()) {
//            for (int i = 0; i < path.size(); i++) {
//                Step step = (Step) path.get(i);
//                logger.debug("Step " + i + ": " + step.getInput() + " -> " +
//                        step.getOutput());
//            }
//        }
        return path.iterator();
    }

    // Javadoc inherited.
    public Version getTargetVersion() {
        return targetVersion;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-May-05	8036/9	geoff	VBM:2005050505 XDIMECP: Migration Framework

 18-May-05	8036/7	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/5	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/3	geoff	VBM:2005050505 XDIMECP: Migration Framework

 11-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/

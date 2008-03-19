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
package com.volantis.mcs.migrate.impl.framework;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.migrate.api.framework.InputMetadata;
import com.volantis.mcs.migrate.api.framework.OutputCreator;
import com.volantis.mcs.migrate.api.framework.ResourceMigrationException;
import com.volantis.mcs.migrate.api.framework.ResourceMigrator;
import com.volantis.mcs.migrate.api.framework.StepType;
import com.volantis.mcs.migrate.api.notification.NotificationType;
import com.volantis.mcs.migrate.impl.framework.identification.Match;
import com.volantis.mcs.migrate.impl.framework.identification.ResourceIdentifier;
import com.volantis.mcs.migrate.impl.framework.identification.Step;
import com.volantis.mcs.migrate.impl.framework.io.RestartInputStream;
import com.volantis.mcs.migrate.impl.framework.io.StreamBuffer;
import com.volantis.mcs.migrate.impl.framework.io.StreamBufferFactory;
import com.volantis.mcs.migrate.notification.NotificationFactory;
import com.volantis.mcs.migrate.api.notification.NotificationReporter;
import com.volantis.synergetics.io.IOUtils;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

/**
 * Default implementation of {@link ResourceMigrator}.
 * <p>
 * This is implemented to delegate all the work of identification off to
 * a resource identifier, and then take the migration steps it returns and
 * iterate over them, connecting any intermedieate inputs and outputs together
 * where nececssary, and processing each one to migrate30 the input version to
 * the target version via all the intermediate versions (if any).
 *
 * todo: later: allow debug logging of intermediate version content.
 */
public class DefaultResourceMigrator implements ResourceMigrator {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(DefaultResourceMigrator.class);

    NotificationFactory notificationFactory =
            NotificationFactory.getDefaultInstance();

    /**
     * A factory to create stream buffers if we need to have intermediate
     * versions.
     */
    private StreamBufferFactory streamBufferFactory;

    /**
     * The resource identifier that does all the hard work of identifying the
     * resource and deciding which steps are required to migrate30 it.
     */
    private ResourceIdentifier resourceIdentifier;

    /**
     * Used to report user messages.
     */
    private NotificationReporter reporter;

    /**
     * Initialise.
     *
     * @param streamBufferFactory creates stream buffers used for intermediate
     *      versions as required.
     * @param resourceIdentifier does all the heavy lifting of identification.
     * @param reporter used to report notifications to the user.
     */
    public DefaultResourceMigrator(StreamBufferFactory streamBufferFactory,
            ResourceIdentifier resourceIdentifier,
            NotificationReporter reporter) {

        this.streamBufferFactory = streamBufferFactory;
        this.resourceIdentifier = resourceIdentifier;
        this.reporter = reporter;
    }

    // Javadoc inherited.
    public void migrate(InputMetadata meta, InputStream input,
            OutputCreator outputCreator)
                    throws IOException, ResourceMigrationException {

        // Create a version of the input which we can read multiple times.
        // This is required because the content recognisers and the first step
        // may all need to read the input data. Note that currently the
        // implementation is very simple and reads the entire input into a
        // byte array. Improving this implementation may speed the performance.
        RestartInputStream restartInput = new RestartInputStream(input);

        // Ask the resource recogniser to see if can recognise the input
        // resource. This will throw an exception if we get duplicate matches
        // as that indicates an error in the configuration which must be solved
        // before we migrate30 the resource in question.
        Match match = resourceIdentifier.identifyResource(meta,
                restartInput);

        // Restart the input since the recogniser will have already
        // consumed it.
        restartInput.restart();

        // If we found a recognition match...
        if (match != null) {
            // ... then try and run the migration using the match.

            reporter.reportNotification(
                    notificationFactory.createLocalizedNotification(
                            NotificationType.INFO, "migration-resource-migrating",
                            new Object[]{meta.getURI(), match.getTypeName(),
                                         match.getVersionName()}));
            processMigrationSteps(match, restartInput, outputCreator);
        } else {
            // ... else, we have no match for this resource.
            // In this case, we assume the content does not require migration
            // and just copy it across to the output.

            reporter.reportNotification(
                    notificationFactory.createLocalizedNotification(
                            NotificationType.INFO, "migration-resource-copying",
                            new Object[]{meta.getURI()}));
            IOUtils.copyAndClose(restartInput,
                                 outputCreator.createOutputStream());
        }

    }

    /**
     * Given a sequence of migration steps, process each in turn in order to
     * translate the input version into the target version via any intermediate
     * versions required.
     *
     * @param match the identification match containing the sequence of steps
     *      to perform.
     * @param inputStream the input data.
     * @param outputCreator an object which allows us to create the output
     *      when required.
     * @throws ResourceMigrationException if a migration error occurs.
     */
    private void processMigrationSteps(Match match,
            InputStream inputStream, OutputCreator outputCreator)
            throws ResourceMigrationException {

        // Extract the sequence of steps that we
        // e.g. if the graph does not have a step registered for the
        // identified version (actually this is an config error).
        Iterator steps = match.getSequence();
        if (steps.hasNext()) {
            // we have at least one step.

            // Loop over the steps, processing each in turn.
            StreamBuffer buffer = null;
            InputStream stepInput;
            OutputStream stepOutput;

            // Type of validation to be performed by each step
            StepType stepType = null;

            while (steps.hasNext()) {
                final Step step = (Step) steps.next();

                if (logger.isDebugEnabled()) {
                    logger.debug("Invoking migration step :"+step);
                }

                // Calculate the input stream for this step.
                // If we have a previous output, then that must be the
                // input for this step, otherwise use the original
                // input.
                if (buffer != null) {
                    stepInput = buffer.getInput();
                } else {
                    stepInput = inputStream;
                }
                // Calculate the output stream for this step.
                // If we have another step to process, then we must
                // buffer the output of the step, otherwise use the
                // original output.
                if (steps.hasNext()) {
                    buffer = streamBufferFactory.create();
                    stepOutput = buffer.getOutput();
                } else {
                    stepOutput = outputCreator.createOutputStream();
                }

                // Workout which step this is
                stepType = getCurrentStepType(stepType, steps);

                // Do the migration.
                Exception firstException = null;
                try {
                    step.migrate(stepInput, stepOutput, stepType);
                } catch (ResourceMigrationException e) {
                    firstException = e;
                } finally {
                    try {
                        stepInput.close();
                    } catch (IOException e) {
                        if (firstException == null) {
                            firstException = e;
                        }
                    }
                    try {
                        stepOutput.close();
                    } catch (IOException e) {
                        if (firstException == null) {
                            firstException = e;
                        }
                    }
                }
                if (firstException != null) {
                    throw new ResourceMigrationException(
                            "Error processing type " + match.getTypeName() +
                            ", step " + step, firstException);
                }
            }

        } else {
            // no steps? must be a bug!
            throw new IllegalStateException("No steps found!");
        }
    }

    /**
     * Determine the type of the current step.
     *
     * @param oldStepType of the last step
     * @param steps iterator
     * @return type of the current step
     */
    private StepType getCurrentStepType(StepType oldStepType, Iterator steps) {

        final boolean firstStep = (oldStepType == null);
        final boolean lastStep = !steps.hasNext();

        return StepType.getType(firstStep, lastStep);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10098/1	phussain	VBM:2005110209 Migration Framework for Repository Parser - ready for review

 27-Oct-05	9986/1	geoff	VBM:2005102512 MCS35: Investigate and fix any JDBC repository import/export problems

 13-Jul-05	9033/1	allan	VBM:2005071312 Move IOUtils.java that is in cornerstone into Synergetics

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 19-May-05	8036/15	geoff	VBM:2005050505 XDIMECP: Migration Framework

 18-May-05	8036/11	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/9	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/6	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/4	geoff	VBM:2005050505 XDIMECP: Migration Framework

 11-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/

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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.migrate.integration.framework;

import com.volantis.mcs.migrate.api.framework.InputMetadata;
import com.volantis.mcs.migrate.api.framework.OutputCreator;
import com.volantis.mcs.migrate.api.framework.ResourceMigrationException;
import com.volantis.mcs.migrate.impl.framework.DefaultResourceMigrator;
import com.volantis.mcs.migrate.impl.framework.DefaultVersion;
import com.volantis.mcs.migrate.impl.framework.graph.DefaultGraph;
import com.volantis.mcs.migrate.impl.framework.identification.DefaultContentIdentifier;
import com.volantis.mcs.migrate.impl.framework.identification.DefaultIdentificationFactory;
import com.volantis.mcs.migrate.impl.framework.identification.DefaultResourceIdentifier;
import com.volantis.mcs.migrate.impl.framework.identification.DefaultStep;
import com.volantis.mcs.migrate.impl.framework.identification.DefaultTypeIdentifier;
import com.volantis.mcs.migrate.impl.framework.identification.Step;
import com.volantis.mcs.migrate.impl.framework.identification.DefaultInputMetadata;
import com.volantis.mcs.migrate.impl.framework.io.DefaultStreamBufferFactory;
import com.volantis.mcs.migrate.impl.framework.io.StreamBufferFactory;
import com.volantis.mcs.migrate.impl.framework.recogniser.RegexpContentRecogniser;
import com.volantis.mcs.migrate.impl.framework.recogniser.RegexpPathRecogniser;
import com.volantis.mcs.migrate.impl.notification.reporter.SimpleCLINotificationReporter;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * An integration test case for the migration framework which creates all the
 * objects manually rather than using the factory and builder classes.
 */
public class FrameworkManualIntegrationTestCase extends TestCaseAbstract {

    /**
     * Test a very simple configuration of a single type with a single custom
     * step.
     *
     * @throws ResourceMigrationException
     * @throws IOException
     */
    public void testIntegrationWithManualCreation()
            throws IOException, ResourceMigrationException {

        String filename = "blah.ext";
        final String inputData = "the old input";
        final String outputData = "the new output";

        SimpleCLINotificationReporter notificationReporter =
                new SimpleCLINotificationReporter();

        DefaultVersion version1 = new DefaultVersion("version 1");
        DefaultVersion version2 = new DefaultVersion("version 2");
        StreamBufferFactory streamBufferFactory =
                new DefaultStreamBufferFactory();
        DefaultResourceIdentifier resourceRecogniser =
                new DefaultResourceIdentifier();
        DefaultIdentificationFactory typeFactory =
                new DefaultIdentificationFactory();
        DefaultTypeIdentifier type = new DefaultTypeIdentifier(typeFactory,
                "type");
        type.setPathRecogniser(new RegexpPathRecogniser(filename));
        type.addContentIdentifier(
                new DefaultContentIdentifier(version1,
                        new RegexpContentRecogniser(inputData)));
        DefaultGraph graph = new DefaultGraph(version2);
        Step step = new DefaultStep(version1, version2,
                new TestStreamMigrator(inputData, outputData));
        graph.addStep(step);
        type.setGraph(graph);
        resourceRecogniser.addType(type);
        DefaultResourceMigrator resourceMigrator =
                new DefaultResourceMigrator(streamBufferFactory,
                        resourceRecogniser, notificationReporter);

        ByteArrayInputStream bais = new ByteArrayInputStream(
                inputData.getBytes());
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputMetadata meta = new DefaultInputMetadata(filename, true);
        resourceMigrator.migrate(meta, bais, new OutputCreator() {
            public OutputStream createOutputStream() {
                return baos;
            }
        });
        assertEquals("", outputData, new String(baos.toByteArray()));
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10098/1	phussain	VBM:2005110209 Migration Framework for Repository Parser - ready for review

 19-May-05	8036/3	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/

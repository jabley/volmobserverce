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

import com.volantis.mcs.migrate.api.framework.FrameworkFactory;
import com.volantis.mcs.migrate.api.framework.InputMetadata;
import com.volantis.mcs.migrate.api.framework.ResourceMigratorBuilder;
import com.volantis.mcs.migrate.api.framework.Version;
import com.volantis.mcs.migrate.impl.framework.graph.DefaultGraphFactory;
import com.volantis.mcs.migrate.impl.framework.graph.GraphFactory;
import com.volantis.mcs.migrate.impl.framework.identification.DefaultIdentificationFactory;
import com.volantis.mcs.migrate.impl.framework.identification.DefaultInputMetadata;
import com.volantis.mcs.migrate.impl.framework.identification.IdentificationFactory;
import com.volantis.mcs.migrate.impl.framework.io.DefaultStreamBufferFactory;
import com.volantis.mcs.migrate.impl.framework.io.StreamBufferFactory;
import com.volantis.mcs.migrate.impl.framework.recogniser.DefaultRecogniserFactory;
import com.volantis.mcs.migrate.impl.framework.recogniser.RecogniserFactory;
import com.volantis.mcs.migrate.impl.notification.reporter.LogDispatcherNotificationReporter;
import com.volantis.mcs.migrate.impl.notification.reporter.SimpleCLINotificationReporter;
import com.volantis.mcs.migrate.impl.framework.stream.DefaultStreamMigratorFactory;
import com.volantis.mcs.migrate.impl.framework.stream.StreamMigratorFactory;
import com.volantis.mcs.migrate.api.notification.NotificationReporter;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Default implementation of {@link FrameworkFactory}.
 */
public class DefaultFrameworkFactory extends FrameworkFactory {
    NotificationReporter reporter;

    // Javadoc inherited.
    public ResourceMigratorBuilder createResourceMigratorBuilder(
            NotificationReporter reporter) {

        // Create all the dependent factories.
        CoreFactory coreFactory = new DefaultCoreFactory();

        IdentificationFactory identificationFactory =
                new DefaultIdentificationFactory();
        GraphFactory graphFactory = new DefaultGraphFactory();
        StreamMigratorFactory streamMigratorFactory =
                new DefaultStreamMigratorFactory();
        RecogniserFactory recogniserFactory = new DefaultRecogniserFactory();
        StreamBufferFactory streamBufferFactory =
                new DefaultStreamBufferFactory();
        // Create the default implementation of the builder using the created
        // dependent factories.
        return new DefaultResourceMigratorBuilder(coreFactory,
                identificationFactory, streamMigratorFactory, graphFactory,
                recogniserFactory, streamBufferFactory, reporter);
        // todo: later: perhaps reporter should be part of the builder API
        // rather than this factory?
    }

    // Javadoc inherited.
    public Version createVersion(String name) {

        // todo: later: to support XML configuration we would need to ensure
        // that identical names generate identical objects since we use ==
        // rather than .equals() to compare versions. For now since the
        // configuration objects are created by hand we don't need to bother.

        return new DefaultVersion(name);
    }

    public InputMetadata createInputMetadata(final String name,
            final boolean applyPathRecognition) {
        return new DefaultInputMetadata(name, applyPathRecognition);
    }

    // Javadoc inherited
    public NotificationReporter createCLINotificationReporter() {
        return new SimpleCLINotificationReporter();
    }

    // Javadoc inherited
    public NotificationReporter createLogDispatcherNotificationReporter(
            LogDispatcher logger) {
        return new LogDispatcherNotificationReporter(logger);
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Nov-05	10098/7	phussain	VBM:2005110209 Migration Framework for Repository Parser - post-review amendments: new reporter type

 15-Nov-05	10098/3	phussain	VBM:2005110209 Migration Framework for Repository Parser - ready for review

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 17-Oct-05	9722/1	ianw	VBM:2005100308 Fix up building of RelMCS by including XML policies

 11-Oct-05	9729/2	geoff	VBM:2005100507 Mariner Export fails with NPE

 19-May-05	8036/17	geoff	VBM:2005050505 XDIMECP: Migration Framework

 18-May-05	8181/5	adrianj	VBM:2005050505 XDIME/CP migration CLI

 18-May-05	8181/3	adrianj	VBM:2005050505 XDIME/CP Migration CLI

 18-May-05	8036/10	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/8	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/5	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/3	geoff	VBM:2005050505 XDIMECP: Migration Framework

 11-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/

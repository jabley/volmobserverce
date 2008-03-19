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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.migrate.unit.framework.stream;

import com.volantis.mcs.migrate.impl.framework.stream.DefaultStreamMigratorFactory;
import com.volantis.mcs.migrate.impl.framework.stream.StreamMigratorFactory;
import com.volantis.mcs.migrate.api.notification.NotificationReporter;
import com.volantis.mcs.migrate.notification.NotificationFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import mock.org.xml.sax.EntityResolverMock;

/**
 * Tests for the XSL stream migrator factory.
 */
public class StreamMigratorFactoryTestCase extends TestCaseAbstract {

    NotificationReporter reporter =
            NotificationFactory.getDefaultInstance().createCLIReporter();

    private EntityResolverMock mockEntityResolver;

    private StreamMigratorFactory factory = new DefaultStreamMigratorFactory();

    private final String xslPath = "path";


    protected void setUp() throws Exception {
        super.setUp();

        mockEntityResolver = new EntityResolverMock(
                "mockEntityResolver", expectations);
    }

    // TODO: test creation of xsl builder.

//    /**
//     * Tests the creation an XSLStreamMigrator which requires an Entity Resolver.
//     *
//     * @throws Exception if an error occurs
//     */
//    public void testCreateXSLStreamMigratorWithEntityResolver() throws Exception {
//        // ==================================================================
//        // Create mocks.
//        // ==================================================================
//
//        // ==================================================================
//        // Create expectations.
//        // ==================================================================
//
//        // ==================================================================
//        // Do the test.
//        // ==================================================================
//        XSLStreamMigrator migrator = (XSLStreamMigrator) factory.createXSLStreamMigrator(xslPath,mockEntityResolver, reporter);
//        assertEquals("xslPath initialized",xslPath,migrator.getXslResourcePath());
//        assertEquals("xslPath initialized",mockEntityResolver,migrator.getEntityResolver());
//    }

    public void testDummy() {
        
    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10098/1	phussain	VBM:2005110209 Migration Framework for Repository Parser - ready for review

 ===========================================================================
*/

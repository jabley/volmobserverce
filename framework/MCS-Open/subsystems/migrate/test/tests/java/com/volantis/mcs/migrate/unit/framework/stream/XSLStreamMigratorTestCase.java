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

import com.volantis.mcs.migrate.impl.framework.stream.XSLStreamMigrator;
import com.volantis.mcs.migrate.notification.NotificationFactory;
import com.volantis.mcs.migrate.api.notification.NotificationReporter;
import com.volantis.mcs.migrate.api.framework.StepType;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.MCSTransformerMetaFactory;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.TransformerMetaFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import mock.org.xml.sax.EntityResolverMock;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

/**
 * Tests for the XSL stream migrator.
 */
public class XSLStreamMigratorTestCase extends TestCaseAbstract {

    NotificationReporter reporter =
            NotificationFactory.getDefaultInstance().createCLIReporter();

    /**
     * Location of a simple test stylesheet.
     */
    private static final String SIMPLE_STYLESHEET_LOCATION =
            "/com/volantis/mcs/migrate/unit/framework/stream/xsl/" +
            "simpleStylesheet.xsl";

    /**
     * Location of an even simpler test stylesheet.
     */
    private static final String SIMPLE_STYLESHEET2_LOCATION =
            "/com/volantis/mcs/migrate/unit/framework/stream/xsl/" +
            "simpleStylesheet2.xsl";

    private static final String inputData="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<!DOCTYPE elem1 SYSTEM \"file:/input.dtd\">\n" +
            "<elem1>\n" +
            "    <elem2/>\n" +
            "</elem1>\n";
    private static final String inputDtd=
            "<?xml encoding=\"UTF-8\"?>\n" +
            "<!ELEMENT elem1 (elem2)>\n" +
            "<!ATTLIST elem1 xmlns CDATA #FIXED ''>\n" +
            "<!ELEMENT elem2 EMPTY>\n" +
            "<!ATTLIST elem2 xmlns CDATA #FIXED ''>\n";
    private static final String output="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<elem1a>\n" +
            "    <elem2a/>\n" +
            "</elem1a>\n";

    private static TransformerMetaFactory transformerMetaFactory =
            new MCSTransformerMetaFactory();

    private EntityResolverMock mockEntityResolver;


    protected void setUp() throws Exception {
        super.setUp();

        mockEntityResolver = new EntityResolverMock(
                "entityResolverMock", expectations);
    }

    /**
     * Tests the migration of simple XML through a basic stylesheet. Note that
     * the stylesheets used for migration should be tested in their own right.
     *
     * @throws Exception if an error occurs
     */
    public void testMigrateXML() throws Exception {
        // Preserve the whitespace ignore setting
        boolean previousWhitespaceSetting = XMLUnit.getIgnoreWhitespace();

        InputStream input =
                getClass().getResourceAsStream("xml/simpleInput.xml");
        InputStream expected =
                getClass().getResourceAsStream("xml/simpleExpectedOutput.xml");
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        XSLStreamMigrator migrator =
                new XSLStreamMigrator(SIMPLE_STYLESHEET_LOCATION, reporter);
        migrator.migrate(input, output, StepType.INTERMEDIATE);
        String outputString = new String(output.toByteArray());

        XMLUnit.setIgnoreWhitespace(true);
        XMLAssert.assertXMLEqual("Result should be as expected",
                                 new InputStreamReader(expected),
                                 new StringReader(outputString));

        // Reset whitespace ignore setting
        XMLUnit.setIgnoreWhitespace(previousWhitespaceSetting);
    }
    
    
    // TODO: test with validation on as well, requires schemas

//    public void testMigrateWithEntityResolver() throws IOException, ParserConfigurationException, SAXException, TransformerException, ResourceMigrationException {
//
//        InputSource externalEntity = new InputSource(new ByteArrayInputStream(inputDtd.getBytes()));
//
//        // ==================================================================
//        // Create mocks.
//        // ==================================================================
//
//        // ==================================================================
//        // Create expectations.
//        // ==================================================================
//        mockEntityResolver.expects.
//                resolveEntity(null,"file:/input.dtd").
//                returns(externalEntity);
//
//        // ==================================================================
//        // Do the test.
//        // ==================================================================
//
//        boolean previousWhitespaceSetting = XMLUnit.getIgnoreWhitespace();
//        XMLUnit.setIgnoreWhitespace(true);
//
//        ByteArrayInputStream in = new ByteArrayInputStream(inputData.getBytes());
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//        StreamMigrator migrator =
//                new XSLStreamMigrator(SIMPLE_STYLESHEET2_LOCATION, mockEntityResolver, reporter);
//        migrator.migrate30(in, out);
//        System.out.println(out);
//
//
//        XMLAssert.assertXMLEqual(output, out.toString());
//        XMLUnit.setIgnoreWhitespace(previousWhitespaceSetting);
//    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10098/1	phussain	VBM:2005110209 Migration Framework for Repository Parser - ready for review

 10-Aug-05	9211/1	pabbott	VBM:2005080902 End to End CSS emulation test

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 18-May-05	8181/1	adrianj	VBM:2005050505 XDIME/CP Migration CLI

 ===========================================================================
*/

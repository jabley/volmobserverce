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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 18-May-03    Geoff           VBM:2003042904 - Created; abstract base class 
 *                              for the test cases for WBSAX which use example 
 *                              data from the various specification documents. 
 * 20-May-03    Geoff           VBM:2003052102 - Add and use new Codec object.
 * 21-May-03    Steve           VBM:2003042908 - Use the supplied test data class
 * 23-May-03    Steve           VBM:2003042908 - Test data has moved packages
 * 29-May-03    Geoff           VBM:2003042905 - Updated to handle the new 
 *                              WBSAX test structure.
 * 30-May-03    Geoff           VBM:2003042918 - Implement dissection 
 *                              WBSAX serialiser.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

import com.volantis.mcs.dom.output.CharacterEncoder;
import com.volantis.mcs.protocols.EncodingWriter;
import com.volantis.mcs.protocols.ProtocolCharacterEncoder;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.ProtocolRegistry;
import com.volantis.mcs.wbsax.io.WBXMLProducer;
import com.volantis.mcs.wbsax.io.WMLProducer;
import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.synergetics.testtools.ArrayObject;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.apache.log4j.Category;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Abstract base class for test cases which use WBSAX example data
 * from the various specification documents, as defined in 
 * {@link com.volantis.mcs.wbsax.WBSAXTestData}.
 * <p>
 * This class defines various test methods which test the conversion from one 
 * representation of test data to another. 
 * <p>
 * Currently the test data has three different representations which this 
 * class uses for testing:
 * <ul>
 *   <li>Events (generated)
 *   <li>WBXML / WMLC
 *   <li>XML / WML
 * </ul>
 * Theoretically, this allows it to test the following combinations
 * <ol>
 *   <li>Events to XML
 *   <li>Events to WBXML
 *   <li>XML to XML
 *   <li>XML to WBXML
 *   <li>WBXML to XML
 *   <li>WBXML to WBXML
 * </ol>
 * However, only the 1 and 2 are currently fully implemented. These each have 
 * two variants, one which tests string table buffering on the input side,
 * and one which tests string table buffering on the output side.
 * <p>
 * 3 is implemented but disabled in the tests which require a string table
 * since {@link com.volantis.mcs.wbsax.TestSAXConsumer} does not currently handle $ variable
 * references in it's input. 
 * <p>
 * 4 requires that we annotate the XML format with indications of where the
 * string references are, similar to what Paul did for the dissector test
 * cases. 
 * <p>
 * 5 and 6 require the ability to parse WBXML which we do not have a 
 * requirement for, and which I have not had time to create an example parser 
 * for.
 * 
 * @todo extend ExampleSAXConsumer to parse variables and enable 3 in children
 * @todo implement string reference annotation in XML and enable 4.
 */ 
public abstract class WBSAXTestCaseAbstract extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The log4j object to log to.
     */
    private static Category logger = Category.getInstance("com.volantis.mcs." +
            "wbsax.WBSAXTestCaseAbstract");
    
    protected WBSAXTestData testData;
    
    public WBSAXTestCaseAbstract(String s) {
        super(s);
    }

    // todo: this should be moved into synergetics ArrayObject to match metis
    // todo: current ArrayObject is broken wrt Format, or at least bogus. 
    // It actually compares the formatted values rather than the values in
    // equals(), which is non-obvious (but useful for this usage, since we
    // try and compare Integers and Bytes which doesn't work otherwise...)
    public static ArrayObject.Format hexByteFormat = new ArrayObject.Format() {
        public String format(Object o) {
            int uint = ((Number) o).byteValue() & 0xFF;
            String s = Integer.toHexString(uint);
            if (s.length() == 1)
                return "0x0" + s;
            else
                return "0x" + s;
        }
    };

    
    /**
     * Test serialising events to WBXML, with buffering on the input side.
     * 
     * @throws Exception
     */ 
    public void testEventsToWBXMLBufferBefore() 
            throws Exception {
        checkEventsToWBXML(true);
    }

    /**
     * Test serialising events to WBXML, with buffering on the output side.
     * 
     * @throws Exception
     */ 
    public void testEventsToWBXMLBufferAfter() 
            throws Exception {
        checkEventsToWBXML(false);
    }

    /**
     * Test serialising the events to XML, with buffering on the input side.
     * 
     * @throws Exception
     */ 
    public void testEventsToXMLBufferBefore() 
            throws Exception {
        checkEventsToXML(true);
    }

    /**
     * Test serialising the events to XML, with buffering on the output side.
     * 
     * @throws Exception
     */ 
    public void testEventsToXMLBufferAfter() 
            throws Exception {
        checkEventsToXML(false);
    }
    
    /**
     * Test parsing XML into events and then serialising back to XML.
     * 
     * @throws Exception
     */ 
    public void testXMLToXML() throws Exception {
        checkXMLToXML();
    }

    // Commented out cos it doesn't work yet, but it will be useful
    // if we implement the to do above.
//    /**
//     * Test parsing XML into events and then serialising back to WBXML.
//     * 
//     * @throws Exception
//     */ 
//    public void testXMLToWBXML() throws Exception {
//        checkXMLToWBXML();
//    }

    protected void checkEventsToWBXML(boolean bufferBefore) throws Exception {
        testData = createTestData();
        int[] expected = testData.getBytes();
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        WBSAXContentHandler producer = createWBXMLProducer(baos);
        if (logger.isDebugEnabled()) {
            producer = new WBSAXDisassembler(producer);
        }
        testData.fireEvents(producer, bufferBefore);
        byte[] actual = baos.toByteArray();

        //System.out.println(new ArrayObject(expected, hexByteFormat));
        //System.out.println(new ArrayObject(actual, hexByteFormat));
        assertEquals(new ArrayObject(expected, hexByteFormat),
                     new ArrayObject(actual, hexByteFormat));
    }

    protected void checkEventsToXML(boolean bufferBefore) throws Exception {
        testData = createTestData();
        String expected = testData.getXML();

        StringWriter sw = new StringWriter();
        WBSAXContentHandler producer = createXMLProducer(sw);
        if (logger.isDebugEnabled()) {
            producer = new WBSAXDisassembler(producer);
        }
        testData.fireEvents(producer, bufferBefore);
        String actual = sw.toString();
        
        //System.out.println(expected);
        //System.out.println(actual);
        assertEquals(expected, actual);
    }

    public void checkXMLToXML() throws Exception {
        testData = createTestData();
        String expected = testData.getXML();

        StringWriter sw = new StringWriter();
        WBSAXContentHandler producer = createXMLProducer(sw);
        if (logger.isDebugEnabled()) {
            producer = new WBSAXDisassembler(producer);
        }
        parseXML(producer);
        String actual = sw.toString();
        
        //System.out.println(expected);
        //System.out.println(actual);
        assertEquals(expected, actual);
    }

    // Commented out cos it doesn't work yet, but it will be useful
    // if we implement the to do above.
//    public void checkXMLToWBXML() throws Exception {
//        testData = createTestData();
//        int[] expected = testData.getBytes();
//        
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        WBSAXContentHandler producer = createWBXMLProducer(baos);
//        WBSAXDisassembler disassembler = new WBSAXDisassembler(producer);
//        parseXML(disassembler);
//        byte[] actual = baos.toByteArray();
//
//        //System.out.println(new ArrayObject(expected, hexByteFormat));
//        //System.out.println(new ArrayObject(actual, hexByteFormat));
//        assertEquals(new ArrayObject(expected, ArrayObject.hexByteFormat),
//                     new ArrayObject(actual, ArrayObject.hexByteFormat));
//    }

    private void parseXML(WBSAXContentHandler producer) throws Exception {
        String input = testData.getXML();
        // Build up the pipeline to convert XML -> producer
        // This will convert SAX -> WBSAX (-> producer)
        StringTable stringTable = null;
        if (testData.requiresStringTable()) {
            stringTable = new StringTable();
        }
        TestSAXConsumer consumer = new TestSAXConsumer(producer,
                testData.getElements(), testData.getAttrStarts(), 
                stringTable, testData.getCodec(), testData.getStrings());
        // This will convert XML -> SAX (-> WBSAX -> producer) 
        XMLReader reader = new com.volantis.xml.xerces.parsers.SAXParser();
        // Before we do so, lets collect the public id info without going
        // out to the internet for the DTD. We do this by parsing with
        // just an entity resolver registered.
        TestSAXPublicIdCollector publicIdCollector = new TestSAXPublicIdCollector(
                testData.getPublicIds());
        reader.setEntityResolver(publicIdCollector);
        reader.parse(createInputSource(input));
        consumer.setPublicId(publicIdCollector.getPublicIdCode());
        // Then parse for real.
        // We leave the public id collector registered as we still want to 
        // prevent the parser trying to resolve the DTD.
        reader.setContentHandler(consumer);
        reader.parse(createInputSource(input));
    }

    private InputSource createInputSource(String xml) {
        StringReader stringReader = new StringReader(xml);
        InputSource source = new InputSource(stringReader);
        return source;
    }
    
    /**
     * Create the test data we will use for this test case.
     * 
     * @throws Exception
     */ 
    protected abstract WBSAXTestData createTestData() 
            throws Exception;

    /**
     * Create a content handler which generates WBXML / WMLC.
     * 
     * @return the created content handler.
     */ 
    protected WBSAXContentHandler createWBXMLProducer(OutputStream output) {
        return new WBXMLProducer(output);
    }

    /**
     * Create a content handler which generates XML / WML.
     * 
     * @return the created content handler.
     */ 
    protected WBSAXContentHandler createXMLProducer(Writer output) {

        // Create a proper protocol character encoder rather than a test one
        // since we rely on encoding rules which are defined in the protocol.
        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new ProtocolRegistry.WMLVersion1_3Factory(),
                InternalDeviceTestHelper.createTestDevice());
        CharacterEncoder pce = new ProtocolCharacterEncoder(protocol);

        EncodingWriter enc = new EncodingWriter(output, pce);
        return new WMLProducer(output, enc);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Oct-03	1599/1	geoff	VBM:2003101501 Support Device access in the new XMLDeviceRepositoryAccessor

 09-Sep-03	1336/1	geoff	VBM:2003090301 Provide support for StringLiteral in WMLC

 14-Jul-03	790/1	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/1	geoff	VBM:2003070404 clean up WBSAX

 10-Jul-03	774/1	geoff	VBM:2003070703 merge from mimas and fix renames manually

 10-Jul-03	770/1	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 04-Jul-03	709/4	geoff	VBM:2003070209 hack together a forward port from Metis

 03-Jul-03	709/2	geoff	VBM:2003070209 hacked port from metis, without synergetics changes

 03-Jul-03	696/7	geoff	VBM:2003070209 clean up WBSAX test cases

 ===========================================================================
*/

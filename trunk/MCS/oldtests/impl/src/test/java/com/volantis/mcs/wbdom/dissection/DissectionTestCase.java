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
 * 30-May-03    Geoff           VBM:2003042918 - Implement dissection
 *                              WBSAX serialiser.
 * 31-May-03    Geoff           VBM:2003042906 - Add opaque value support and
 *                              support for using the fixed WMLProducer.
 * 01-Jun-03    Geoff           VBM:2003042906 - More improvements.
 * 02-Jun-03    Geoff           VBM:2003042906 - More improvements after some
 *                              input from Paul.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbdom.dissection;

import com.volantis.mcs.dissection.DissectedDocument;
import com.volantis.mcs.dissection.DissectionCharacteristicsImpl;
import com.volantis.mcs.dissection.DissectionContext;
import com.volantis.mcs.dissection.DissectionTestCaseHelper;
import com.volantis.mcs.dissection.DissectionURLManager;
import com.volantis.mcs.dissection.Dissector;
import com.volantis.mcs.dissection.DocumentInformationImpl;
import com.volantis.mcs.dissection.MyDissectionContext;
import com.volantis.mcs.dissection.MyDissectionURLManager;
import com.volantis.mcs.dissection.RequestedShards;
import com.volantis.mcs.dissection.ShardIterator;
import com.volantis.mcs.dissection.XMLTestCaseHelper;
import com.volantis.mcs.dissection.dom.DissectableDocumentBuilder;
import com.volantis.mcs.dissection.dom.TestDocumentDetails;
import com.volantis.mcs.dom.output.CharacterEncoder;
import com.volantis.mcs.dom.output.SerialisationURLListener;
import com.volantis.mcs.dom.debug.DebugCharacterEncoder;
import com.volantis.mcs.protocols.EncodingWriter;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.wbdom.TestURLListener;
import com.volantis.mcs.wbdom.dissection.io.DissectionWBSAXParser;
import com.volantis.mcs.wbdom.dissection.io.WBDOMDissectedContentHandler;
import com.volantis.mcs.wbsax.AttributeStartFactory;
import com.volantis.mcs.wbsax.CharsetCode;
import com.volantis.mcs.wbsax.Codec;
import com.volantis.mcs.wbsax.ElementNameFactory;
import com.volantis.mcs.wbsax.PublicIdFactory;
import com.volantis.mcs.wbsax.StringFactory;
import com.volantis.mcs.wbsax.StringReferenceFactory;
import com.volantis.mcs.wbsax.StringTable;
import com.volantis.mcs.wbsax.TestSAXPublicIdCollector;
import com.volantis.mcs.wbsax.WBSAXContentHandler;
import com.volantis.mcs.wbsax.WBSAXDisassembler;
import com.volantis.mcs.wbsax.WBSAXTeeHandler;
import com.volantis.mcs.wbsax.io.WBXMLProducer;
import com.volantis.mcs.wbsax.io.WMLProducer;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.net.URL;

/**
 * An extremely bodgy test case which integration tests the dissectable WBDOM
 * with the dissector by running some test content through it. Currently all
 * it does is ensure that the whole thing doesn't fall over. 
 * 
 * @todo this test case needs a LOT more work. 
 */ 
public class DissectionTestCase extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The log4j object to log to.
     */
    private static Category logger = Category.getInstance("com.volantis.mcs." +
            "wbdom.dissection.DissectionTestCase");
    
    public DissectionTestCase(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        BasicConfigurator.configure();
    }

    protected void tearDown() throws Exception {
        Category.shutdown();
    }

    /**
     * This test currently just exercises as many code paths as I can get it
     * to, in order to check that it doesn't fall over completely. 
     * <p>
     * This is obviously not good, but it was the best I could do given the
     * time available. 
     * <p>
     * We should use this test as the basis of many similar tests that check 
     * the generated output to ensure that the WBDOM dissector works correctly. 
     * 
     * @throws Exception
     */ 
    public void testMostOfTheCodePathsHopefully() throws Exception {

        // Currently we have not implemented breakpoints/string reference 
        // support for dissectable strings. Thus, we cannot have string 
        // references in element content (since it may be dissected, unlike
        // attribute content, which currently does allow string references).
        // This means that we have had to change this test back to using a 
        // simpler input file without shared references "temporarily" until we 
        // have implemented support for string references in element content.
        // todo: reenable this when we support references in element content
        
        // Declare what we know about the test data to use
        String name = "input-string.xml";
        int pageSize = 550;
//        String name = "input.xml";
//        // for input.xml, divide input into 3
//        // Without shared content
////      int fixedCost = 171; // the fixed stuff, not counting DA or SLG/DO
//        // With duplicates allowed shared content
//        //   "Term" to reference = 3 
//        // * 3 bytes per string  = 9
//        int fixedCost = 180; // but it prints out 170!
//        // @todo investigate why fixed cost incorrect using shared references
//        // note this might be easier once we remove the duplicates
//        int dissectableCost = 483; // no longer output?
//        // With and without shared content, this does not vary
//        int dissectableOverhead = 79; // shard link stuff
//        int pageSize = fixedCost + dissectableOverhead +
//                (dissectableCost / 3);
//        int textSizes[] = new int [] { 436, 498, 488, 394 };
//        // Without shared content        
////      int binarySizes[] = new int[] { 338, 389, 379, 299};
//        // With duplicates allowed shared content        
////               "Term" strings refs +    3    3    3    3     
////              * 3 bytes per string =    9    9    9    9
////               "some" strings refs +    3    2    3    2     
////              * 5 bytes per string =   15   10   15   10
////                total bytes       +=   24   19   24   19
////      int binarySizes[] = new int[] { 362, 408, 403, 318 };
//        // With duplicates not allowed shared content              
////         duplicate "Term"s deleted +    2    2    2    2     
////              * 5 bytes per string =   10   10   10   10
////         duplicate "some"s deleted +    2    1    2    1     
////              * 5 bytes per string =   10    5   10    5
////                total bytes       -=   20   15   20   15
//        int binarySizes[] = new int[] { 342, 393, 383, 303 };
                               
//        int pageSize = 9999; // too big for dissection.

        TestDissectionConfiguration conf = 
                new TestDissectionConfiguration();
        
        // Handler to parse WBSAX events into WBDOM nodes.
        DissectableWBDOMFactory wbdomFactory = 
                new DissectableWBDOMFactory(conf);
        DissectionWBSAXParser parser =
                new DissectionWBSAXParser(wbdomFactory, conf);

        // Register the codes used in the input file
        ElementNameFactory elementNames = new ElementNameFactory();
        elementNames.registerElement(0, "root");
        elementNames.registerElement(1, "p");
        elementNames.registerElement(2, "div");
        elementNames.registerElement(4, "dl");
        elementNames.registerElement(5, "dt");
        elementNames.registerElement(6, "dd");
        elementNames.registerElement(7, "a");
        conf.registerAtomicElement(  7);
        conf.registerUrlAttribute(      "a", "href");
        elementNames.registerElement(8, "br");
        conf.registerEmptyElement(   8, "br");
        elementNames.registerElement(9, "address"); // for testing empty element type
        AttributeStartFactory attrStarts = new AttributeStartFactory();
        attrStarts.registerAttributeStart(20, "href");

        SerialisationURLListener urlListener = new TestURLListener();
        
        CharsetCode charset = new CharsetCode(0x6A, "UTF-8");
        Codec codec = new Codec(charset);
        StringTable stringTable = new StringTable();
        StringFactory strings = new StringFactory(codec);
        StringReferenceFactory references = 
                new StringReferenceFactory(stringTable, strings);
        
        // SAX handler to consume SAX events, generating WBSAX events
        DissectionTestSAXConsumer consumer = new DissectionTestSAXConsumer(parser,
                elementNames, attrStarts, stringTable, codec, strings);
        // Wrapper for SAX->WBSAX layer to handle the dissection stuff.
        DissectableDocumentBuilder builder = 
                new WBDOMDissectableDocumentBuilder(parser, consumer, references);

        // Parser to parse XML and generate SAX events
        XMLReader reader = XMLTestCaseHelper.getXMLReader();

        // Before we do so, lets collect the public id info without going
        // out to the internet for the DTD. We do this by parsing with
        // just an entity resolver registered.
        TestSAXPublicIdCollector publicIdCollector = new TestSAXPublicIdCollector(
                new PublicIdFactory());
        reader.setEntityResolver(publicIdCollector);
        reader.parse(new InputSource(getJarUrl(name)));
        consumer.setPublicId(publicIdCollector.getPublicIdCode());

        // Parse in the input file, using the builder, creating a dissectable 
        // document.
        TestDocumentDetails details = 
                DissectionTestCaseHelper.createDissectableDocument(builder, 
                this.getClass(), name);
        WBDOMDissectableDocument dissectionDocument = (WBDOMDissectableDocument) 
                details.getDocument();
        
        Dissector dissector = new Dissector();
        DissectionContext context = new MyDissectionContext();
        DissectionCharacteristicsImpl characteristics =
                new DissectionCharacteristicsImpl();
        DissectionURLManager urlManager = new MyDissectionURLManager();
        DocumentInformationImpl information = new DocumentInformationImpl();
        information.setDocumentURL(new MarinerURL("document-url.xml"));
        System.out.println("Page Size=" + pageSize);
        characteristics.setMaxPageSize(pageSize);
        DissectedDocument dissectedDocument =
                dissector.createDissectedDocument(context, characteristics,
                dissectionDocument, urlManager, information);

        // Interate over the shards.
        RequestedShards shards = dissectedDocument.createRequestedShards();
        int dissectableArea = 0;
        ShardIterator iterator =
                dissectedDocument.getShardIterator(context, dissectableArea);
        int count = 0;
        while (iterator.hasMoreShards()) {
            iterator.populateNextShard();
            count += 1;
        }
        for (int i = 0; i < count; i += 1) {
            System.out.println();
            System.out.println("Shard " + (i+1) + " of " + count +
                    " of dissectable area " + dissectableArea);
            shards.setShard(0, i);

            // Create a producer for XML.
            CharArrayWriter textBuffer = new CharArrayWriter();
            CharacterEncoder pce = new DebugCharacterEncoder();
            EncodingWriter enc = new EncodingWriter(textBuffer, pce);
            WBSAXContentHandler textProducer = new WMLProducer(textBuffer, enc);

            if (logger.isDebugEnabled()) {
                textProducer = new WBSAXDisassembler(textProducer);
            }
            
            // Create a producer for WBXML.
            ByteArrayOutputStream binaryBuffer = new ByteArrayOutputStream();
            WBXMLProducer binaryProducer = new WBXMLProducer(binaryBuffer);
            // Create a "tee" producer which will generate the XML and WBXML
            // simultaneously.
            WBSAXContentHandler producer = new WBSAXTeeHandler(
                    textProducer, binaryProducer);
            // Create content handler used by the dissector to serialise with.
            WBDOMDissectedContentHandler dissectedContentHandler =
                    new WBDOMDissectedContentHandler(producer, conf, urlListener);
            
            // And finally, create the XML and WBXML simultaneously.
            dissector.serialize(context, dissectedDocument, shards,
                    dissectedContentHandler);

            String text = textBuffer.toString();
            byte[] binary = binaryBuffer.toByteArray();

            System.out.println(text);
            System.out.println("XML size=" + text.length());
            System.out.write(binary);
            System.out.println();
            System.out.println("WBXML size=" + binary.length);

            assertTrue("Output page size too large",
                    binary.length <= pageSize);
//            assertEquals("shard " + i + " text size mismatch", 
//                    textSizes[i], text.length());
//            assertEquals("shard " + i + " binary size mismatch", 
//                    binarySizes[i], binary.length);
        }
    }

    private String getJarUrl(String name) {
        URL url = getClass().getResource(name);
        if (url != null)
            return url.toExternalForm();
        else {
            throw new RuntimeException("Can't find input jar url for " + name);
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Oct-05	9729/1	geoff	VBM:2005100507 Mariner Export fails with NPE

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Oct-03	1599/1	geoff	VBM:2003101501 Support Device access in the new XMLDeviceRepositoryAccessor

 09-Sep-03	1336/1	geoff	VBM:2003090301 Provide support for StringLiteral in WMLC

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/3	geoff	VBM:2003082101 Clean up wbdom.dissection

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/2	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 14-Jul-03	790/1	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/1	geoff	VBM:2003070404 clean up WBSAX

 10-Jul-03	751/5	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 04-Jul-03	722/1	geoff	VBM:2003070403 merge from mimas; fix rename failures manually

 04-Jul-03	724/3	geoff	VBM:2003070403 first take at cleanup

 03-Jul-03	696/4	geoff	VBM:2003070209 clean up WBSAX test cases

 30-Jun-03	559/4	geoff	VBM:2003060607 changes to test atomic elements

 27-Jun-03	559/2	geoff	VBM:2003060607 make WML use protocol configuration again

 24-Jun-03	365/3	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 13-Jun-03	372/1	chrisw	VBM:2003060609 Implement wmlc url optimiser

 12-Jun-03	388/3	mat	VBM:2003061101 Improve WMLC debugging and tidy up WMLRoot

 12-Jun-03	368/7	geoff	VBM:2003061006 Enhance WBDOM to support string references

 10-Jun-03	309/6	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 10-Jun-03	309/4	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/2	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/

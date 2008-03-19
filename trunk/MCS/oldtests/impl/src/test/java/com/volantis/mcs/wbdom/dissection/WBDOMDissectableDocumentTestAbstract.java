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
package com.volantis.mcs.wbdom.dissection;

import com.volantis.mcs.dissection.dom.DissectableDocumentTestAbstract;
import com.volantis.mcs.dissection.dom.DissectableDocumentBuilder;
import com.volantis.mcs.dissection.dom.DissectedContentHandler;
import com.volantis.mcs.dissection.dom.OutputDocument;
import com.volantis.mcs.dissection.dom.DocumentStats;
import com.volantis.mcs.dissection.dom.DissectableContentHandler;
import com.volantis.mcs.wbdom.dissection.io.DissectionWBSAXParser;
import com.volantis.mcs.wbdom.dissection.io.WBDOMDissectedContentHandler;
import com.volantis.mcs.dom.output.SerialisationURLListener;
import com.volantis.mcs.wbdom.TestURLListener;
import com.volantis.mcs.wbsax.ElementNameFactory;
import com.volantis.mcs.wbsax.AttributeStartFactory;
import com.volantis.mcs.wbsax.Codec;
import com.volantis.mcs.wbsax.CharsetCode;
import com.volantis.mcs.wbsax.StringTable;
import com.volantis.mcs.wbsax.StringReferenceFactory;
import com.volantis.mcs.wbsax.TestSAXConsumer;
import com.volantis.mcs.wbsax.WBSAXContentHandler;
import com.volantis.mcs.wbsax.WBSAXDisassembler;
import com.volantis.mcs.wbsax.StringFactory;
import org.apache.log4j.Category;

/**
 * An abstract WBDOM implementation of {@link DissectableDocumentTestAbstract}.
 * <p>
 * This class "oversees" the testing of the WBDOM against the generic test 
 * infrastructure provided by the "accurate" dissector.
 * <p>
 * This includes creation of WBDOM content from the test input files, and 
 * sizing information, but does not include output generation and checking. 
 * Output generation and checking is done in the two concrete subclasses, one
 * for WML (text) and one for WMLC (binary). 
 */ 
public abstract class WBDOMDissectableDocumentTestAbstract 
        extends DissectableDocumentTestAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The log4j object to log to.
     */
    private static Category logger = Category.getInstance("com.volantis.mcs." +
            "wbdom.dissection.WBDOMDissectableDocumentTestAbstract");
    
    private TestDissectionConfiguration conf;
    private SerialisationURLListener urlListener;
    private DissectableWBDOMFactory wbdomFactory;
    private ElementNameFactory elementNames;
    private AttributeStartFactory attrStarts;
    private Codec codec;
    private CharsetCode charset;
    private StringFactory strings;
    
    public WBDOMDissectableDocumentTestAbstract(String name) {
        super(name);

        conf = new TestDissectionConfiguration();
        urlListener = new TestURLListener();
        
        wbdomFactory = new DissectableWBDOMFactory(conf);

        // Register the codes used in the input files
        elementNames = new ElementNameFactory();
        elementNames.registerElement( 0, "root");
        elementNames.registerElement( 1, "b");
        elementNames.registerElement( 2, "br");
        conf.registerEmptyElement(    2, "br");    // set BR as empty
        elementNames.registerElement( 3, "p");
        elementNames.registerElement( 4, "element");
        elementNames.registerElement( 5, "subelement");
        elementNames.registerElement( 6, "a");
        conf.registerUrlAttribute(       "a", "href"); // register as url attr
        // todo: can we make some tests for atomic elements?
        // yes we can but we need to use the same method that the text test
        // uses which is to provide a maximum size, which will be output 
        // dependent.
        conf.registerAtomicElement(   6);          // set A as atomic
        elementNames.registerElement( 7, "div");
        elementNames.registerElement( 8, "dl");
        elementNames.registerElement( 9, "dt");
        elementNames.registerElement(10, "dd");

        attrStarts = new AttributeStartFactory();
        attrStarts.registerAttributeStart(20, "attribute");
        attrStarts.registerAttributeStart(21, "href");

        charset = new CharsetCode(0x6A, "UTF-8");
        codec = new Codec(charset);
        strings = new StringFactory(codec);
    }

    protected DissectableDocumentBuilder createBuilder() throws Exception {
        StringTable stringTable = new StringTable();
        StringReferenceFactory references = 
                new StringReferenceFactory(stringTable, strings);
        
        // Handler to parse WBSAX events into dissection WBDOM nodes.
        DissectionWBSAXParser parser =
                new DissectionWBSAXParser(wbdomFactory, conf);
        // SAX handler to consume SAX events, generating WBSAX events
        DissectionTestSAXConsumer consumer = new DissectionTestSAXConsumer(
                parser, elementNames, attrStarts, stringTable, codec, strings);
        // Wrapper for SAX->WBSAX layer to handle the dissection stuff.
        DissectableDocumentBuilder builder = 
                new WBDOMDissectableDocumentBuilder(parser, consumer,
                        references);

        return builder;
    }

    protected DissectedContentHandler createDissectedContentHandler(
            OutputDocument output)
            throws Exception {

        // Create either a binary or text producer.
        WBSAXContentHandler producer = createProducer(output);

        if (logger.isDebugEnabled()) {
            // Wrap it with a debug filter.
            producer = new WBSAXDisassembler(producer);
        }
        
        // Create content handler used by the dissector to serialise with.
        WBDOMDissectedContentHandler dissectedContentHandler =
                new WBDOMDissectedContentHandler(producer, conf, urlListener);
        
        return dissectedContentHandler;
    }

    protected abstract WBSAXContentHandler createProducer(
            OutputDocument output);

    //
    // These tests have sizes I have hand-validated.
    // Note these sizes were done on Metis, the whitespace is different
    // on Mimas and this means the sizes have to be "adjusted".
    // 
    
    public void checkSimple1(DocumentStats statistics) throws Exception {
        /*
          . version
          . public id
          . charset
          . string table length = 0
          . root with content
          . end root content.
          6
         */
        statistics.totalCost = 6;
        statistics.fixedCost = 6;
        super.checkSimple1(statistics);
    }

    public void checkSimple2(DocumentStats statistics) throws Exception {
        /*
          . version
          . public id
          . charset
          . string table length = 0
          . root with content
            . str_i
            . <cr>
              "  Some text." = 12
            . <cr>
            . <nul>
          . end root content
            
          10 + 12 = 22
         */ 
        statistics.totalCost = 22;
        statistics.fixedCost = 22;
        super.checkSimple2(statistics);
    }

    public void checkSimple3(DocumentStats statistics) throws Exception {
        /*
          . version
          . public id
          . charset
          . string table length = 0
          . root with content
            . str_i
            . <cr>
              "  Some text " = 12
              " split into two sections that should still be" = 45
            . <cr>
              "  treated as a single text node." = 32
            . <cr>
            . <nul>
          . end root content
            
          11 + 12 + 45 + 32 = 100        
        */
        statistics.totalCost = 100;
        statistics.fixedCost = 100;
        super.checkSimple3(statistics);
    }

    public void checkSimple4(DocumentStats statistics) throws Exception {
        /*
          . version
          . public id
          . charset
          . string table length = 0
          . root with content
            . str_i
            . <cr>
            "  Some text, some " = 18
            . <nul>
            . b with content
              . str_i
              "bold text" = 9
              . <nul>
            . end b content
            . str_i
            " and then some more text." = 25
            . <cr>
            . <nul>
          . end root content
            
          16 + 18 + 9 + 25 = 68
         */ 
        statistics.totalCost = 68;
        statistics.fixedCost = 68;
        super.checkSimple4(statistics);
    }

    public void checkSimple5(DocumentStats statistics) throws Exception {
        /*
          . version
          . public id
          . charset
          . string table length = 0
          . root with content
            . str_i
            . <cr>
            "  Some text, then a break " = 26
            . <nul>
            . br - empty
            . str_i
            " and then some more text." = 25
            . <cr>
            . <nul>
          . end root content
            
          13 + 26 + 25 = 64
         */ 
        statistics.totalCost = 64;
        statistics.fixedCost = 64;
        super.checkSimple5(statistics);
    }

    // todo: ask paul what the point of 6 and 7 are.
    
    public void checkSimple6(DocumentStats statistics) throws Exception {
        /*
          . version
          . public id
          . charset
          . string table length = 0
          . root with content
            . p with content
              . str_i
              "Some text." = 10
              . <nul>
              //. str_i
              //. <cr>
              //. <nul>
            . end p content
          . end root content
            
          10 + 10 = 20
         */ 
        statistics.totalCost = 20;
        statistics.fixedCost = 20;
        super.checkSimple6(statistics);
    }

    public void checkSimple7(DocumentStats statistics) throws Exception {
        /*
          . version
          . public id
          . charset
          . string table length = 0
          . root with content
            //. str_i
            //. <cr>
            //. <nul>
            . p with content
              . str_i
              "Some text." = 10
              . <nul>
            . end p content
          . end root content
            
          10 + 10 = 20
         */ 
        statistics.totalCost = 20;
        statistics.fixedCost = 20;
        super.checkSimple7(statistics);
    }

    public void checkSimple8(DocumentStats statistics) throws Exception {
        /*
          . version
          . public id
          . charset
          . string table length = 0
          . root with content
            //. str_i
            //. <cr>
            //. <nul>
            . p with content
              . str_i
              "Some text." = 10
              . <nul>
            . end p content
            . p with content
              . str_i
              "More text." = 10
              . <nul>
            . end p content
            //. str_i
            //. <cr>
            //. <nul>
          . end root content
            
          14 + 10 + 10 = 34
         */ 
        statistics.totalCost = 34;
        statistics.fixedCost = 34;
        super.checkSimple8(statistics);
    }

    protected void checkCommon1(DocumentStats statistics)
            throws Exception {
        /*
          . version
          . public id
          . charset
          . string table length = 17 (1 byte)
          "[shared content]" = 16
          . <nul>
          . root with content
            //. str_i
            //. <cr>
            //. <nul>
            . p with content
              . str_i
              "This paragraph contains common content " = 39
              . <nul>
              . str_t
              . offset 0 (-> [shared content])
              . str_i
              " that is repeated" = 17
              . <cr>
              "       " = 7
              . <nul>
              . str_t
              . offset 0 (-> [shared content])
              . str_i
              " in a number !sharedContent; of locations." = 42
              . <cr>
              "    " = 4
              . <nul>
            . end p content
            //. str_i
            //. <cr>
            //. <nul>
          . end root content
          
          21 + 16 + 39 + 17 + 7 + 42 + 4 = 146 
         */
        statistics.totalCost = 146;
        statistics.fixedCost = 146;
        super.checkCommon1(statistics);
    }

    //
    // These test have sizes I simply copied from the failure messages, 
    // as their content was too big to attempt sizing it by hand.
    // 
    
    protected void checkCommon2(DocumentStats statistics)
            throws Exception {
        statistics.totalCost = 927;
        statistics.fixedCost = 519;
        super.checkCommon2(statistics);
    }

    protected void checkCommon3(DocumentStats statistics)
            throws Exception {
        statistics.totalCost = 1414;
        statistics.fixedCost = 727;
        super.checkCommon3(statistics);
    }

    protected void checkCommon4(DocumentStats statistics)
            throws Exception {
        statistics.totalCost = 1574;
        statistics.fixedCost = 727;
        super.checkCommon4(statistics);
    }

    protected void checkCommon5(DocumentStats statistics)
            throws Exception {
        /*
          . version
          . public id
          . charset
          . string table length = 2 (1 byte)
          "b" = 1
          . <nul>
          . root with content
            //. str_i
            //. <cr>
            //. <nul>
            . literal with content
              . ref to b:0 = 1 byte
              . str_i
              "an element with a shared string name" = 37
              . <nul>
            . end b content
            //. str_i
            //. <cr>
            //. <nul>
          . end root content
          
          8 + 2 + 39 = 49 
         */
        statistics.totalCost = 49;
        statistics.fixedCost = 49;
        super.checkCommon5(statistics);
    }
    
    protected void checkCommon6(DocumentStats statistics)
            throws Exception {
        // Common6 input is exactly equal to Common3 input with string 
        // references transformed into literal named elements. Each string 
        // reference is a "STR_T <ref>", each Literal named element is a 
        // "LITERAL_C <ref> END", and there are nine occurrences, so you would
        // expect the WBXML size of Common6 to be exactly 9 bytes larger than 
        // Common3. The math works out ... 1323 - (1414 - 100) = 9.   
        statistics.totalCost = 1323;
        statistics.fixedCost = 691;
        super.checkCommon6(statistics);
    }

    protected void checkComplex1(DocumentStats statistics)
            throws Exception {
        statistics.totalCost = 331;
        statistics.fixedCost = 165;
        super.checkComplex1(statistics);
    }

    protected void checkText1(DocumentStats statistics)
            throws Exception {
        statistics.totalCost = 544;
        statistics.fixedCost = 6;
        super.checkText1(statistics);
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Oct-03	1599/1	geoff	VBM:2003101501 Support Device access in the new XMLDeviceRepositoryAccessor

 09-Sep-03	1336/1	geoff	VBM:2003090301 Provide support for StringLiteral in WMLC

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/2	geoff	VBM:2003082101 Clean up wbdom.dissection

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 23-Jul-03	807/2	geoff	VBM:2003071405 works and tested but no design review yet

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/2	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 14-Jul-03	790/1	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/1	geoff	VBM:2003070404 clean up WBSAX

 10-Jul-03	770/2	geoff	VBM:2003070703 merge from metis, and rename files manually, and fix up sizes for whitespace differences

 10-Jul-03	751/6	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 ===========================================================================
*/

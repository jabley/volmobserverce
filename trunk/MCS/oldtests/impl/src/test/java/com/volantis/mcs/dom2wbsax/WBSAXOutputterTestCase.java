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
 * 23-May-03    Mat             VBM:2003042907 - Created to test WBSAXDocumentOutputter.
 * 30-May-03    Mat             VBM:2003042911 - Changed to add the ADD_CONTENT
 *                              and END_CONTENT events.
 * 29-May-03    Geoff           VBM:2003042905 - Update after implementing
 *                              WBDOM.
 * 01-Jun-03    Chris W         VBM:2003042906 - testOutputTextNodeLiteralDollar
 *                              fixed.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.dom2wbsax;

import com.volantis.charset.BitSetEncoding;
import com.volantis.charset.Encoding;
import com.volantis.mcs.dissection.dom.DissectionElementTypes;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.MCSDOMContentHandler;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.NodeAnnotation;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.wml.WBSAXAccesskeyAnnotationElementProcessor;
import com.volantis.mcs.protocols.wml.WBSAXDissectionElementProcessor;
import com.volantis.mcs.protocols.wml.WBSAXShardLinkElementProcessor;
import com.volantis.mcs.protocols.wml.WMLVariable;
import com.volantis.mcs.protocols.wml.WMLVersion1_3Configuration;
import com.volantis.mcs.wbsax.AttributeStartFactory;
import com.volantis.mcs.wbsax.CharsetCode;
import com.volantis.mcs.wbsax.Codec;
import com.volantis.mcs.wbsax.ElementNameFactory;
import com.volantis.mcs.wbsax.EntityCode;
import com.volantis.mcs.wbsax.EnumeratedWBSAXContentHandler;
import com.volantis.mcs.wbsax.Extension;
import com.volantis.mcs.wbsax.StringFactory;
import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.wbsax.StringReferenceFactory;
import com.volantis.mcs.wbsax.StringTable;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.WBSAXString;
import com.volantis.mcs.wbsax.io.TestDebugProducer;
import junit.framework.TestCase;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * @author mat
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class WBSAXOutputterTestCase extends TestCase {

    // Slow to create so just do it once
    private static Encoding encoding;
    static {
        try {
            encoding = new BitSetEncoding("iso-8859-1", 4);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    private WBSAXProcessorContext context;

    /**
     * The WBSAXDocumentOutputter to test.
     */
    private WBSAXDocumentOutputter outputter;
    
    /**
     * The special content handler to use
     */
    private EnumeratedWBSAXContentHandler enumeratedWBSAXContentHandler =
        new EnumeratedWBSAXContentHandler();
        
    /**
     * The protocol configuration
     */
    WMLVersion1_3Configuration configuration;
    
    private ElementNameFactory fac;

    private AttributeStartFactory asf;

    /**
     * Constructor for WBSAXOutputterTestCase.
     * @param arg0
     */
    public WBSAXOutputterTestCase(String arg0) {
        super(arg0);
    }
    
    public void setUp() {
        // @todo later fix this dependency on non-common code (this comes from core)
        configuration = new WMLVersion1_3Configuration();
        CharsetCode charsetCode = new CharsetCode(encoding.getMIBEnum(),
                        "iso-8859-1");
        fac = configuration.getElementNameFactory();
        asf = configuration.getAttributeStartFactory();
        
        StringTable stringTable = new StringTable();
        StringFactory strings = new StringFactory(new Codec(charsetCode));
        StringReferenceFactory references =
                new StringReferenceFactory(stringTable, strings);
        
        context = new WBSAXProcessorContext(fac, asf, strings, references,
                        encoding, stringTable, configuration);
        context.setContentHandler(enumeratedWBSAXContentHandler);

        outputter = new WBSAXDocumentOutputter(context);
        
    }

    /*
     * Test for void output(Document)
     * Disabled at the moment due to time constraints.
     */
    public void noTestOutputDocument() {
//        String wml = "<wml>" +
//                    "<card id=\"abc\" ordered=\"true\">" +
//                        "<p>" +
//                            "<do type=\"accept\">" + 
//                                "<go href=\"http://xyz.org/s\"/>" +
//                            "</do>" +
//                            " Enter name: <input type=\"text\" name=\"N\"/>" +
//                        "</p>" +
//                    "</card>" +
//                "</wml>";
       // try {
//            document = null;
            //document = parseXml(wml);
            //outputter.output(document);
//        } catch (IOException e) {
//            e.printStackTrace();
//            fail("Cannot parse XML");
//        } catch (SAXException e) {
//            e.printStackTrace();
//            fail("Cannot parse XML");
//        }
        
        
    }

    /**
     * Method to create an MCSDOM from some xml and output it using 
     * WBSAXDocumentOutputter
     * @param xml Well formed xml to output.
     */
    public void doOutputNode(String xml) {
        
        try {
            outputter.output((Element)parseXml(xml));
        } catch (IOException e) {
            e.printStackTrace();
            fail("Cannot parse XML");
        } catch (SAXException e) {
            e.printStackTrace();
            fail("Cannot parse XML");
        }
    }

    /**
     * Test the output of a simple paragraph tag
     *
     */
    public void testOutputNodeSimpleParagraph() throws WBSAXException {
        doOutputNode("<p>Testing</p>");
        
        ArrayList parameterList;
        ArrayList events = enumeratedWBSAXContentHandler.getEvents();
        ArrayList parameters = enumeratedWBSAXContentHandler.getParameters();
        // Expecting:
        // 0 - START_ELEMENT_ELEMENT_CODE               60
        //                                              false
        //                                              false
        // 1 - START_CONTENT
        // 2 - ADD_CONTENT_VALUE_SAX_STRING             Testing
        // 3 - END_CONTENT
        // 4 - END_ELEMENT
        
        // list of expected parameters.
        ArrayList ep = new ArrayList();
        String result;
        
        assertEquals("Wrong number of events", 5, events.size());
        
        int event = 0;
        ep.add(fac.create("p"));
        ep.add(new Boolean(false));
        ep.add(new Boolean(true));
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.START_ELEMENT_ELEMENT_CODE, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.START_CONTENT, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        ep.add("Testing");
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.ADD_CONTENT_VALUE_SAX_STRING, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.END_CONTENT, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.END_ELEMENT, ep)) != null) {
            fail(result);
        }
        
        
    }
    
    /**
     * Test the output of a simple paragraph tag with an unrepresentable character
     *
     */
    public void testOutputNodeSimpleParagraphUnRepresentableContent() throws WBSAXException{
        doOutputNode("<p>Testing \uaaff character</p>");
    
        ArrayList parameterList;
        ArrayList events = enumeratedWBSAXContentHandler.getEvents();
        ArrayList parameters = enumeratedWBSAXContentHandler.getParameters();
        // Expecting:
        // 0 - START_ELEMENT_ELEMENT_CODE               60
        //                                              false
        //                                              false
        // 1 - START_CONTENT
        // 2 - ADD_CONTENT_VALUE_SAX_STRING             Testing
        // 3 - ADD_ATTRIBUTE_VALUE_ENTITY_ENTITY_CODE   Entity for \uaaff
        // 4 - ADD_CONTENT_VALUE_SAX_STRING             character
        // 5 - END_CONTENT
        // 6 - END_ELEMENT
    
        // list of expected parameters.
        ArrayList ep = new ArrayList();
        String result;
    
        assertEquals("Wrong number of events", 7, events.size());
    
        int event = 0;
        ep.add(fac.create("p"));
        ep.add(new Boolean(false));
        ep.add(new Boolean(true));
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.START_ELEMENT_ELEMENT_CODE, ep)) != null) {
            fail(result);
        }
        event++;
    
        ep.clear();
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.START_CONTENT, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        ep.add("Testing ");
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.ADD_CONTENT_VALUE_SAX_STRING, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        ep.add(new EntityCode('\uaaff'));
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.ADD_CONTENT_VALUE_ENTITY_ENTITY_CODE, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        ep.add(" character");
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.ADD_CONTENT_VALUE_SAX_STRING, ep)) != null) {
            fail(result);
        }
        event++;
    
        ep.clear();
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.END_CONTENT, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.END_ELEMENT, ep)) != null) {
            fail(result);
        }
    }
    
    /**
     * Test the output of a variable
     *
     */
    public void testOutputTextNodeBracketedVariable() throws WBSAXException{
        Text textNode = domFactory.createText();
        textNode.append(WMLVariable.WMLV_BRACKETED + "var" +
        WMLVariable.WMLV_BRACKETED);
        textNode.accept(outputter);

        ArrayList parameterList;
        ArrayList events = enumeratedWBSAXContentHandler.getEvents();
        ArrayList parameters = enumeratedWBSAXContentHandler.getParameters();
        // Expecting:
        // 0 - ADD_ATTRIBUTE_VALUE_ENTITY_ENTITY_CODE   Extension.TWO
    
        // list of expected parameters.
        ArrayList ep = new ArrayList();
        String result;
    
        assertEquals("Wrong number of events", 1, events.size());
    
        int event = 0;
        
        ep.clear();
        ep.add(Extension.TWO);
        ep.add("var");
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.ADD_CONTENT_VALUE_EXTENSION_SAX_STRING, ep)) != null) {
            fail(result);
        }
        event++;
        
    }
    
    /**
     * Test the output of an escaped variable
     *
     */
    public void testOutputTextNodeEscapedVariable() throws WBSAXException{
        Text textNode = domFactory.createText();
        textNode.append(WMLVariable.WMLV_BRACKETED + "var" +
        WMLVariable.WMLV_ESCAPE);
        textNode.accept(outputter);

        ArrayList parameterList;
        ArrayList events = enumeratedWBSAXContentHandler.getEvents();
        ArrayList parameters = enumeratedWBSAXContentHandler.getParameters();
        // Expecting:
        // 1 - ADD_ATTRIBUTE_VALUE_ENTITY_ENTITY_CODE   Extension.ZERO
    
        // list of expected parameters.
        ArrayList ep = new ArrayList();
        String result;
    
        assertEquals("Wrong number of events", 1, events.size());
    
        int event = 0;
        
        ep.clear();
        ep.add(Extension.ZERO);
        ep.add("var");
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.ADD_CONTENT_VALUE_EXTENSION_SAX_STRING, ep)) != null) {
            fail(result);
        }
        event++;
        
    }
    
    /**
     * Test the output of an unescaped variable
     *
     */
    public void testOutputTextNodeUnEscapedVariable() throws WBSAXException{
        Text textNode = domFactory.createText();
        textNode.append(WMLVariable.WMLV_BRACKETED + "var" +
        WMLVariable.WMLV_UNESC);
        textNode.accept(outputter);

        ArrayList parameterList;
        ArrayList events = enumeratedWBSAXContentHandler.getEvents();
        ArrayList parameters = enumeratedWBSAXContentHandler.getParameters();
        // Expecting:
        // 0 - ADD_ATTRIBUTE_VALUE_ENTITY_ENTITY_CODE   Extension.ONE
    
        // list of expected parameters.
        ArrayList ep = new ArrayList();
        String result;
    
        assertEquals("Wrong number of events", 1, events.size());
    
        int event = 0;
        
        ep.clear();
        ep.add(Extension.ONE);
        ep.add("var");
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.ADD_CONTENT_VALUE_EXTENSION_SAX_STRING, ep)) != null) {
            fail(result);
        }
        event++;
    }
    
    /**
     * Test the output of an literal dollare
     *
     */
    public void testOutputTextNodeLiteralDollar() throws WBSAXException{
        Text textNode = domFactory.createText();
        textNode.append('$' + "text");
        textNode.accept(outputter);
    
        ArrayList parameterList;
        ArrayList events = enumeratedWBSAXContentHandler.getEvents();
        ArrayList parameters = enumeratedWBSAXContentHandler.getParameters();
        // Expecting:
        // 1 - ADD_CONTENT_VALUE_SAX_STRING             $$
        // 2 - ADD_CONTENT_VALUE_SAX_STRING             text
    
        // list of expected parameters.
        ArrayList ep = new ArrayList();
        String result;
    
        assertEquals("Wrong number of events", 2, events.size());
    
        int event = 0;
        
        ep.clear();
        ep.add("$");
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.ADD_CONTENT_VALUE_SAX_STRING, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        ep.add("text");
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.ADD_CONTENT_VALUE_SAX_STRING, ep)) != null) {
            fail(result);
        }
        event++;
    
    }
    
    /**
     * Test the output of a tag with an attribute that has a prefix 
     * specified in the token table.  In this case "href", "http://"
     *
     */
    public void testOutputNodeAttributeStart() throws WBSAXException{
        doOutputNode("<a href=\"http://my.test.com\">Link</a>");
        ArrayList parameterList;
        ArrayList events = enumeratedWBSAXContentHandler.getEvents();
        ArrayList parameters = enumeratedWBSAXContentHandler.getParameters();
        // Expecting:
        // 0 - START_ELEMENT_ELEMENT_CODE               a
        //                                              true
        //                                              true
        // 1 - START_ATTRIBUTES
        // 2 - ADD_ATTRIBUTE_START_CODE                 code for href=http://
        // 3 - ADD_ATTRIBUTE_VALUE_SAX_STRING           my.test.com
        // 4 - START_CONTENT
        // 5 - ADD_CONTENT_VALUE_SAX_STRING             Link
        // 6 - END_CONTENT  
        // 7 - END_ATTRIBUTES
        // 8 - END_ELEMENT
    
        // list of expected parameters.
        ArrayList ep = new ArrayList();
        String result;
    
        assertEquals("Wrong number of events", 9, events.size());
    
        int event = 0;        
        ep.add(fac.create("a"));
        ep.add(new Boolean(true));
        ep.add(new Boolean(true));
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.START_ELEMENT_ELEMENT_CODE, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.START_ATTRIBUTES, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        ep.add(asf.create("href", "http://"));
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.ADD_ATTRIBUTE_START_CODE, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        ep.add("my.test.com");
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.ADD_ATTRIBUTE_VALUE_SAX_STRING, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.END_ATTRIBUTES, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.START_CONTENT, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        ep.add("Link");
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.ADD_CONTENT_VALUE_SAX_STRING, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.END_CONTENT, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.END_ELEMENT, ep)) != null) {
            fail(result);
        }
        event++;
    }
        
    /**
     *  Test for output of an attribute that doesn't have a matching prefix
     * in the token table.
     *
     */
    public void testOutputNodeSimpleAttribute() throws WBSAXException{
        doOutputNode("<img src=\"mypic\"/>");
        ArrayList parameterList;
        ArrayList events = enumeratedWBSAXContentHandler.getEvents();
        ArrayList parameters = enumeratedWBSAXContentHandler.getParameters();
        // Expecting:
        // 0 - START_ELEMENT_ELEMENT_CODE               img
        //                                              true
        //                                              false
        // 1 - START_ATTRIBUTES
        // 1 - ADD_ATTRIBUTE_START_CODE                 code for img
        // 2 - ADD_ATTRIBUTE_VALUE_SAX_STRING           my.test.com
        // 3 - END_ATTRIBUTES
        // 4 - END_ELEMENT

        // list of expected parameters.
        ArrayList ep = new ArrayList();
        String result;

        assertEquals("Wrong number of events", 6, events.size());

        int event = 0;        
        ep.add(fac.create("img"));
        ep.add(new Boolean(true));
        ep.add(new Boolean(false));
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.START_ELEMENT_ELEMENT_CODE, ep)) != null) {
            fail(result);
        }
        event++;
    
        ep.clear();
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.START_ATTRIBUTES, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        ep.add(asf.create("src", null));
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.ADD_ATTRIBUTE_START_CODE, ep)) != null) {
            fail(result);
        }
        event++;
    
        ep.clear();
        ep.add("mypic");
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.ADD_ATTRIBUTE_VALUE_SAX_STRING, ep)) != null) {
            fail(result);
        }
        event++;
    
        ep.clear();
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.END_ATTRIBUTES, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.END_ELEMENT, ep)) != null) {
            fail(result);
        }
        event++;
    }
    
    /**
     *  Test for output of an attribute that doesn't have a matching prefix
     * in the token table.
     *
     */
    public void testOutputNodeSimpleAttributeUnrepresentableChar() throws WBSAXException{
        doOutputNode("<img src=\"my" + "\uaaff" + "pic\"/>");
        ArrayList parameterList;
        ArrayList events = enumeratedWBSAXContentHandler.getEvents();
        ArrayList parameters = enumeratedWBSAXContentHandler.getParameters();
        // Expecting:
        // 0 - START_ELEMENT_ELEMENT_CODE               img
        //                                              true
        //                                              false
        // 1 - START_ATTRIBUTES
        // 2 - ADD_ATTRIBUTE_START_CODE                 code for img
        // 3 - ADD_ATTRIBUTE_VALUE_SAX_STRING           my
        // 4 - ADD_ATTRIBUTE_VALUE_ENTITY_ENTITY_CODE   Entity for \uaaff
        // 5 - ADD_ATTRIBUTE_VALUE_SAX_STRING           pic
        // 6 - END_ATTRIBUTES
        // 7 - END_ELEMENT

        // list of expected parameters.
        ArrayList ep = new ArrayList();
        String result;

        assertEquals("Wrong number of events", 8, events.size());

        int event = 0;        
        ep.add(fac.create("img"));
        ep.add(new Boolean(true));
        ep.add(new Boolean(false));
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.START_ELEMENT_ELEMENT_CODE, ep)) != null) {
            fail(result);
        }
        event++;
    
        ep.clear();
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.START_ATTRIBUTES, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        ep.add(asf.create("src", null));
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.ADD_ATTRIBUTE_START_CODE, ep)) != null) {
            fail(result);
        }
        event++;
    
        ep.clear();
        ep.add("my");
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.ADD_ATTRIBUTE_VALUE_SAX_STRING, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        ep.add(new EntityCode('\uaaff'));
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.ADD_ATTRIBUTE_VALUE_ENTITY_ENTITY_CODE, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        ep.add("pic");
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.ADD_ATTRIBUTE_VALUE_SAX_STRING, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.END_ATTRIBUTES, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.END_ELEMENT, ep)) != null) {
            fail(result);
        }
        event++;
    }
    
    /**
     *  Test the output of multiple attributes.
     *
     */
    public void testOutputNodeMultipleAttributes() throws WBSAXException {
        doOutputNode("<img src=\"mypic\" alt=\"Alt\"/>");
        ArrayList parameterList;
        ArrayList events = enumeratedWBSAXContentHandler.getEvents();
        ArrayList parameters = enumeratedWBSAXContentHandler.getParameters();
        // Expecting:
        // 0 - START_ELEMENT_ELEMENT_CODE               img
        //                                              true
        //                                              false
        // 1 - START_ATTRIBUTES
        // 2 - ADD_ATTRIBUTE_START_CODE                 code for src
        // 3 - ADD_ATTRIBUTE_VALUE_SAX_STRING           my.test.com
        // 4 - ADD_ATTRIBUTE_START_CODE                 code for alt
        // 5 - ADD_ATTRIBUTE_VALUE_SAX_STRING           Alt
        // 6 - END_ATTRIBUTES
        // 7 - END_ELEMENT

        // list of expected parameters.
        ArrayList ep = new ArrayList();
        String result;

        assertEquals("Wrong number of events", 8, events.size());

        int event = 0;        
        ep.add(fac.create("img"));
        ep.add(new Boolean(true));
        ep.add(new Boolean(false));
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.START_ELEMENT_ELEMENT_CODE, ep)) != null) {
            fail(result);
        }
        event++;

        ep.clear();
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.START_ATTRIBUTES, ep)) != null) {
            fail(result);
        }
        event++;
    
        ep.clear();
        ep.add(asf.create("alt", null));
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.ADD_ATTRIBUTE_START_CODE, ep)) != null) {
            fail(result);
        }
        event++;

        ep.clear();
        ep.add("Alt");
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.ADD_ATTRIBUTE_VALUE_SAX_STRING, ep)) != null) {
            fail(result);
        }
        event++;
                
        ep.clear();
        ep.add(asf.create("src", null));
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.ADD_ATTRIBUTE_START_CODE, ep)) != null) {
            fail(result);
        }
        event++;

        ep.clear();
        ep.add("mypic");
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.ADD_ATTRIBUTE_VALUE_SAX_STRING, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.END_ATTRIBUTES, ep)) != null) {
            fail(result);
        }
        event++;
    
        ep.clear();
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.END_ELEMENT, ep)) != null) {
            fail(result);
        }
        event++;
    }
       
    /**
     * Test an element with a literal name, and a simple attribute and content.
     */
    public void testOutputNodeLiteralElement() throws WBSAXException {
        doOutputNode("<unknown src=\"mypic\">Testing</unknown>");
        ArrayList parameterList;
        ArrayList events = enumeratedWBSAXContentHandler.getEvents();
        ArrayList parameters = enumeratedWBSAXContentHandler.getParameters();
        // Expecting:
        // 0 - START_ELEMENT_STRING_REF                 unknown
        //                                              true
        //                                              true
        // 1 - START_ATTRIBUTES
        // 2 - ADD_ATTRIBUTE_START_CODE                 literal for src
        // 3 - ADD_ATTRIBUTE_VALUE_SAX_STRING           mypic
        // 4 - END_ATTRIBUTES
        // 5 - START_CONTENT
        // 6 - ADD_CONTENT_VALUE_SAX_STRING             Testing
        // 7 - END_CONTENT
        // 8 - END_ELEMENT

        // list of expected parameters.
        ArrayList ep = new ArrayList();
        String result;

        assertEquals("Wrong number of events", 9, events.size());

        int event = 0;        
        ep.add(new TestStringReference(0,0,"unknown"));
        ep.add(new Boolean(true));
        ep.add(new Boolean(true));
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.START_ELEMENT_STRING_REF, ep)) != null) {
            fail(result);
        }
        event++;
    
        ep.clear();
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.START_ATTRIBUTES, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        ep.add(asf.create("src", null));
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.ADD_ATTRIBUTE_START_CODE, ep)) != null) {
            fail(result);
        }
        event++;
    
        ep.clear();
        ep.add("mypic");
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.ADD_ATTRIBUTE_VALUE_SAX_STRING, ep)) != null) {
            fail(result);
        }
        event++;
    
        ep.clear();
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.END_ATTRIBUTES, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.START_CONTENT, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        ep.add("Testing");
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.ADD_CONTENT_VALUE_SAX_STRING, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.END_CONTENT, ep)) != null) {
            fail(result);
        }
        event++;
        
        ep.clear();
        parameterList = getParameterList(event, parameters);
        if ((result = checkEvent((Integer) events.get(event), parameterList,
            EnumeratedWBSAXContentHandler.END_ELEMENT, ep)) != null) {
            fail(result);
        }
        event++;
    }
    
    /**
     * Get the parameter list from the parameters.
     * 
     * @param index The index into the parameters list
     * @param parameters The list of parameter containers
     * @return The parameter list for this index.
     */ 
    private ArrayList getParameterList(int index, ArrayList parameters) {
        EnumeratedWBSAXContentHandler.ParameterContainer pc = (EnumeratedWBSAXContentHandler.ParameterContainer) parameters.get(index);
        // Return sn empty array list if there are no parameters.
        return (pc != null) ? pc.getParameters() : new ArrayList();
    }

    /**
     * Parse a string containing an XML snippet into a tree of DOM nodes.
     * 
     * @param xml the xml snippet.
     * @return a {@link com.volantis.mcs.dom.Node} containing a tree of DOM Nodes.
     * 
     * @throws java.io.IOException
     * @throws org.xml.sax.SAXException
     */ 
    private static Node parseXml(String xml)
            throws IOException, SAXException {
        
        MCSDOMContentHandler domParser = new MCSDOMContentHandler();
        XMLReader saxParser =
            new com.volantis.xml.xerces.parsers.SAXParser();
        saxParser.setContentHandler(domParser);
        StringReader stringReader = new StringReader(xml);
        InputSource source = new InputSource(stringReader);
        saxParser.parse(source);
        return domParser.getDocument().getRootElement();
    }
    
    /**
     * Check an event against some expected results.
     * @param eventIndex The event we are testing.
     * @param parameters The parameters for the event
     * @param expectedEvent The event we are expecting
     * @param expectedParameters The expected parameters for the event.
     * @return A message indicating a problem, or null if there were no problems.
     */
    private String checkEvent(Integer eventIndex, ArrayList parameters,
                              int expectedEvent, ArrayList expectedParameters) throws WBSAXException{
                                  
          int event = eventIndex.intValue();
          if(event != expectedEvent) {
              return "Expected event " + expectedEvent + ", got event " + event;                        
          }
          if(parameters.size() != expectedParameters.size()) {
              return "Expected " + expectedParameters.size() + " parameters, received " + parameters.size();
          }
          for(int i = 0; i < parameters.size(); i++) {
              Object p1 = parameters.get(i);
              Object p2 = expectedParameters.get(i);
              
              if(p1 instanceof WBSAXString) {
                try {
                    String s1 = ((WBSAXString) p1).getString();
                    // p2 should be a string to test against.
                    if(!s1.equals((String) p2)) {
                        return 
                            "Parameter " + i + " does not match (Got " + s1 + ", expected " + p2 + ")";
                    }
                } catch (WBSAXException e) {
                    e.printStackTrace();
                    fail("Unable to get string from WBSAXString");
                }
              } else if (p1 instanceof StringReference) {
                  StringReference r1 = (StringReference) p1;
                  TestStringReference r2 = (TestStringReference) p2;
                  if (r1.resolveLogicalIndex() != r2.getLogicalOffset() ||
                          r1.resolvePhysicalIndex().getInteger() != r2.getPhysicalOffset() || 
                          !r1.resolveString().getString().equals(r2.getContents())) {
                      return 
                          "Parameter " + i + " does not match (Got " + p1 + ", expected " + p2 + ")";
                  }
              } else {
                  if(!(p1.equals(p2))) {
                      return "Parameter " + i + " does not match (Got " + p1 + ", expected " + p2 + ")";
                  }
              }
          }
          return null;
    }
    
    /**
     * Testing version of a string reference.
     * <p>
     * This stores expected results for comparison purposes.
     */ 
    private static class TestStringReference {
        private int logicalOffset;
        private int physicalOffset;
        private String contents;

        public TestStringReference(int logicalOffset, int physicalOffset, 
                String contents) {
            this.logicalOffset = logicalOffset;
            this.physicalOffset = physicalOffset;
            this.contents = contents;
        }

        public int getLogicalOffset() {
            return logicalOffset;
        }

        public int getPhysicalOffset() {
            return physicalOffset;
        }

        public String getContents() {
            return contents;
        }
        
        public String toString() {
            return "[tsr:logical=" + logicalOffset + 
                    ":physical=" + physicalOffset + 
                    ":contents=" + contents + "]";
        }
    }

    
    // We should really split these tests out into their own testcases, and
    // we should have a few for normal elements as well, but we rely on
    // the original tests above for that at the moment.
    
    /**
     * Test the basic behaviour of the WBSAXIgnoreElementProcessor.
     */ 
    public void testIgnoreElement() throws Exception {
        Node node = parseXml("<p><ignore><br/></ignore></p>");
        
        StringWriter out = new StringWriter();
        context.setContentHandler(new TestDebugProducer(out));
        outputter.addSpecialElementProcessor("ignore", 
                new WBSAXIgnoreElementProcessor(context));
        node.accept(outputter);
        // Check for <p><br/></p>
        assertEquals("", 
                "[se:(c:p),false,true]" +
                    "[sc]" +
                        "[se:(c:br),false,false]" +
                    "[ec]" +
                "[ee]", 
                out.toString());
    }

    /**
     * Test the basic behaviour of the WBSAXDissectionElementProcessor.
     */ 
    public void testDissectionElement() throws Exception {
        doTestDissectionElement(new WBSAXDissectionElementProcessor(context,
                DissectionElementTypes.getDissectableAreaType()),
                null);
    }
    
    /**
     * Test the basic behaviour of the WBSAXShardLinkElementProcessor.
     */ 
    public void testDissectionShardLinkElement() throws Exception {
        // The shard link dissection element turns href="[url]" into
        // href="{opaque}", but you can't call getString() on it until
        // you configure it. For now we just check for the exception in it's
        // place as we are not testing the opaque value's behaviour.
        doTestDissectionElement(new WBSAXShardLinkElementProcessor(context,
                DissectionElementTypes.getDissectableAreaType()),
                "[av:(o:{exception})]");
    }

    public void doTestDissectionElement(WBSAXDissectionElementProcessor
            processor, String attributeValue) throws Exception {
        // Create <p><da {na}><a href="[url]"/></da></p>
        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();
        Element p = buffer.openElement("p");
        Element da = domFactory.createElement();
        da.setName("da");
        NodeAnnotation na = new NodeAnnotation() {};
        da.setObject(na);
        buffer.openElement(da);
        Element a = buffer.openElement("a");
        a.setAttribute("href", "[url]");
        buffer.closeElement(a);
        buffer.closeElement(da);
        buffer.closeElement(p);
        
        StringWriter out = new StringWriter();
        context.setContentHandler(new TestDebugProducer(out));
        outputter.addSpecialElementProcessor("da", processor);
        p.accept(outputter);
        // Check for <p><{opaque}><a href="[url]|{opaque}"/></{opaque}></p>
        if (attributeValue == null) {
            // default behaviour is just render it normally.
            attributeValue = "[av:(s:[url])]";
        }
        assertEquals("", 
                "[se:(c:p),false,true]" +
                    "[sc]" +
                        "[se:(o:DISSECTABLE-AREA),true]" +
                            "[sc]" +
                                "[se:(c:a),true,false]" +
                                    "[sa]" +
                                        "[aa:(c:href,null)]" +
                                        attributeValue +
                                    "[ea]" +
                                "[ee]"+
                            "[ec]" +
                        "[ee]"+
                    "[ec]" +
                "[ee]", 
                out.toString());
    }
    
    /**
     * Test the basic behaviour of WBSAXAccesskeyAnnotationElementProcessor.
     *
     * todo: later: split into it's own test case class.
     */ 
    public void testAccesskeyAnnotationElement() throws Exception {
        Node node = parseXml("<p><ak><a accesskey=\"x\">x abc</a></ak></p>");
        
        StringWriter out = new StringWriter();
        context.setContentHandler(new TestDebugProducer(out));
        outputter.addSpecialElementProcessor("ak", 
                new WBSAXAccesskeyAnnotationElementProcessor(context, true));
        node.accept(outputter);
        // Check for <p><a accesskey="{opaque}">{opaque}abc</a></p>
        // The access key opaque values created by above cannot be rendered
        // until they have a value. For now we just check for the exception in
        // it's place as we are not testing the opaque value's behaviour.
        assertEquals("",
                "[se:(c:p),false,true]" +
                    "[sc]" +
                        "[se:(c:a),true,true]" +
                            "[sa]" +
                                "[aa:(c:accesskey,null)]" +
                                "[av:(o:{exception})]" +
                            "[ea]" +
                            "[sc]" +
                                "[cv:(o:{exception})]" +
                                "[cv:(s:abc)]" +
                            "[ec]" +
                        "[ee]"+
                    "[ec]" +
                "[ee]", 
                out.toString());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9522/1	ibush	VBM:2005091502 no_save on images

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 05-May-05	8005/2	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 02-Mar-05	7243/5	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 02-Mar-05	7120/1	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Jun-04	4720/1	byron	VBM:2004061604 Core Emulation Facilities

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 17-Feb-04	2974/1	steve	VBM:2004020608 SGML Quote handling

 05-Feb-04	2794/1	steve	VBM:2004012613 HTML Quote handling
 02-Oct-03	1469/5	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 09-Sep-03	1336/2	geoff	VBM:2003090301 Provide support for StringLiteral in WMLC

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 23-Jul-03	807/1	geoff	VBM:2003071405 works and tested but no design review yet

 14-Jul-03	790/1	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/1	geoff	VBM:2003070404 clean up WBSAX

 04-Jul-03	733/1	geoff	VBM:2003070403 port from mimas and fix renames manually

 04-Jul-03	724/1	geoff	VBM:2003070403 first take at cleanup

 30-Jun-03	605/1	geoff	VBM:2003060607 port from metis to mimas

 06-Jun-03	335/1	mat	VBM:2003042906 Merged changes to MCS

 05-Jun-03	285/6	mat	VBM:2003042911 Merged with MCS

 ===========================================================================
*/

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
package com.volantis.xml.utilties.sax;

import java.io.ByteArrayInputStream;
import java.io.StringReader;

import junit.framework.TestCase;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;
import com.volantis.xml.utilities.sax.XMLReaderFactory;

/**
 * Tests the DocumentFragmentFilter
 */
public class DocumentFragmentFilterTestCase extends TestCase {

    /**
     * Constructor for DocumentFragmentParserTest.
     */
    public DocumentFragmentFilterTestCase(String name) {
        super(name);
    }

    /**
     * Test void parse(InputSource) to be valid
     */
    public void testParseInputSourceByteStream() throws Exception {
        String xmlFragment = "<one/>\n<two>text goes here</two>";
        try {
            XMLReader subject = XMLReaderFactory.createXMLReader(true);
            subject.parse(new InputSource(new ByteArrayInputStream(
                                  xmlFragment.getBytes())));
        } catch (SAXParseException e) {
            e.printStackTrace();
            fail(e.toString()+":"+e.getMessage()+":"
                    +e.getLineNumber()+","+e.getColumnNumber());
        }
    }
    
    /**
     * Test parse(InputSource) 
     */
    public void testParseInputSourceCharacterStream() throws Exception {
        String xmlFragment = "<one/>\n<two>text goes here</two>";
        try {
            XMLReader subject = XMLReaderFactory.createXMLReader(true);
            StringReader reader = new StringReader(xmlFragment);
            subject.parse(new InputSource(reader));
        } catch (SAXParseException e) {
            e.printStackTrace();
            fail(e.toString()+":"+e.getMessage()+":"
                    +e.getLineNumber()+","+e.getColumnNumber());
        }
    }

    /**
     * Test to see that the line number information is fixed up to exclude
     * the wrapped <fragment> element
     */
    public void testFailedParseLineNumber() throws Exception {
        String xmlFragment="<no-error>This line should be fine</no-error>\n"+
                           "<error>Expected error on line 2</rror>\n"+
                           "<no-error>This lie should be fine</no-error>";                    
        try {
            XMLReader subject = XMLReaderFactory.createXMLReader(true);
            subject.parse(new InputSource(new ByteArrayInputStream(
                                  xmlFragment.getBytes())));
        } catch (SAXParseException e) {
            if(e.getLineNumber()!=2) {
                fail("Error should be on line 2 and not on "+e.getLineNumber());
            }
        }
    }
    
    /**
     * Test to make sure the ContentHandler does not get startElement or 
     * endElement events for the <fragment> 
     */
    public void testFixElement() throws Exception {
        String xmlFragment = "<one/>\n<two>text goes here</two>";
        try {
            XMLReader subject = XMLReaderFactory.createXMLReader(true);
            TestFragmentEventXMLFilter filter = new TestFragmentEventXMLFilter();
            subject.setContentHandler(filter);   
            subject.parse(new InputSource(new ByteArrayInputStream(
                                  xmlFragment.getBytes())));
        } catch (SAXParseException e) {
            e.printStackTrace();
            fail(e.toString()+":"+e.getMessage()+":"
                    +e.getLineNumber()+","+e.getColumnNumber());
        }
    }

    /**
     * Test to make sure the ContentHandler does not get leading whitespace
     *
     */
    public void testWhitespaceRemoval() {
        String xmlFragment = "           <one/><two><three></three></two>";
        try {
            XMLReader subject = XMLReaderFactory.createXMLReader(true);
            subject.setFeature(
                "http://www.volantis.com/DocumentFragmentParser/trimWhitespace",
                true);
            TestTrimWhitespaceXMLFilter filter = new TestTrimWhitespaceXMLFilter();
            subject.setContentHandler(filter);   
            subject.parse(new InputSource(new ByteArrayInputStream(
                                  xmlFragment.getBytes())));
        } catch (SAXParseException e) {
            e.printStackTrace();
            fail(e.toString()+":"+e.getMessage()+":"
                    +e.getLineNumber()+","+e.getColumnNumber());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }        
    }

    /**
     * Test the parser with a large xml input stream.
     */
    public void testBigXMLParsing() throws Exception {
        String xmlFragment =
            "<catalog>" +
            "    <cd>" +
            "        <title>Empire Burlesque</title>" +
            "        <artist>Bob Dylan</artist>" +
            "        <country>USA</country>" +
            "        <company>Columbia</company>" +
            "        <price>10.90</price>" +
            "        <year>1985</year>" +
            "    </cd>" +
            "    <cd>" +
            "        <title>Hide your heart</title>" +
            "        <artist>Bonnie Tyler</artist>" +
            "        <country>UK</country>" +
            "        <company>CBS Records</company>" +
            "        <price>9.90</price>" +
            "        <year>1988</year>" +
            "    </cd>" +
            "    <cd>" +
            "        <title>Greatest Hits</title>" +
            "        <artist>Dolly Parton</artist>" +
            "        <country>USA</country>" +
            "        <company>RCA</company>" +
            "        <price>9.90</price>" +
            "        <year>1982</year>" +
            "    </cd>" +
            "    <cd>" +
            "        <title>Still got the blues</title>" +
            "        <artist>Gary Moore</artist>" +
            "        <country>UK</country>" +
            "        <company>Virgin records</company>" +
            "        <price>10.20</price>" +
            "        <year>1990</year>" +
            "    </cd>" +
            "    <cd>" +
            "        <title>Eros</title>" +
            "        <artist>Eros Ramazzotti</artist>" +
            "        <country>EU</country>" +
            "        <company>BMG</company>" +
            "        <price>9.90</price>" +
            "        <year>1997</year>" +
            "    </cd>" +
            "    <cd>" +
            "        <title>One night only</title>" +
            "        <artist>Bee Gees</artist>" +
            "        <country>UK</country>" +
            "        <company>Polydor</company>" +
            "        <price>10.90</price>" +
            "        <year>1998</year>" +
            "    </cd>" +
            "    <cd>" +
            "        <title>Sylvias Mother</title>" +
            "        <artist>Dr.Hook</artist>" +
            "        <country>UK</country>" +
            "        <company>CBS</company>" +
            "        <price>8.10</price>" +
            "        <year>1973</year>" +
            "    </cd>" +
            "    <cd>" +
            "        <title>Maggie May</title>" +
            "        <artist>Rod Stewart</artist>" +
            "        <country>UK</country>" +
            "        <company>Pickwick</company>" +
            "        <price>8.50</price>" +
            "        <year>1990</year>" +
            "    </cd>" +
            "    <cd>" +
            "        <title>Romanza</title>" +
            "        <artist>Andrea Bocelli</artist>" +
            "        <country>EU</country>" +
            "        <company>Polydor</company>" +
            "        <price>10.80</price>" +
            "        <year>1996</year>" +
            "    </cd>" +
            "    <cd>" +
            "        <title>When a man loves a woman</title>" +
            "        <artist>Percy Sledge</artist>" +
            "        <country>USA</country>" +
            "        <company>Atlantic</company>" +
            "        <price>8.70</price>" +
            "        <year>1987</year>" +
            "    </cd>" +
            "    <cd>" +
            "        <title>Black angel</title>" +
            "        <artist>Savage Rose</artist>" +
            "        <country>EU</country>" +
            "        <company>Mega</company>" +
            "        <price>10.90</price>" +
            "        <year>1995</year>" +
            "    </cd>" +
            "    <cd>" +
            "        <title>1999 Grammy Nominees</title>" +
            "        <artist>Many</artist>" +
            "        <country>USA</country>" +
            "        <company>Grammy</company>" +
            "        <price>10.20</price>" +
            "        <year>1999</year>" +
            "    </cd>" +
            "    <cd>" +
            "        <title>For the good times</title>" +
            "        <artist>Kenny Rogers</artist>" +
            "        <country>UK</country>" +
            "        <company>Mucik Master</company>" +
            "        <price>8.70</price>" +
            "        <year>1995</year>" +
            "    </cd>" +
            "    <cd>" +
            "        <title>Big Willie style</title>" +
            "        <artist>Will Smith</artist>" +
            "        <country>USA</country>" +
            "        <company>Columbia</company>" +
            "        <price>9.90</price>" +
            "        <year>1997</year>" +
            "    </cd>" +
            "    <cd>" +
            "        <title>Tupelo Honey</title>" +
            "        <artist>Van Morrison</artist>" +
            "        <country>UK</country>" +
            "        <company>Polydor</company>" +
            "        <price>8.20</price>" +
            "        <year>1971</year>" +
            "    </cd>" +
            "    <cd>" +
            "        <title>Soulsville</title>" +
            "        <artist>Jorn Hoel</artist>" +
            "        <country>Norway</country>" +
            "        <company>WEA</company>" +
            "        <price>7.90</price>" +
            "        <year>1996</year>" +
            "    </cd>" +
            "    <cd>" +
            "        <title>The very best of</title>" +
            "        <artist>Cat Stevens</artist>" +
            "        <country>UK</country>" +
            "        <company>Island</company>" +
            "        <price>8.90</price>" +
            "        <year>1990</year>" +
            "    </cd>" +
            "    <cd>" +
            "        <title>Stop</title>" +
            "        <artist>Sam Brown</artist>" +
            "        <country>UK</country>" +
            "        <company>A and M</company>" +
            "        <price>8.90</price>" +
            "        <year>1988</year>" +
            "    </cd>" +
            "    <cd>" +
            "        <title>Bridge of Spies</title>" +
            "        <artist>T`Pau</artist>" +
            "        <country>UK</country>" +
            "        <company>Siren</company>" +
            "        <price>7.90</price>" +
            "        <year>1987</year>" +
            "    </cd>" +
            "    <cd>" +
            "        <title>Private Dancer</title>" +
            "        <artist>Tina Turner</artist>" +
            "        <country>UK</country>" +
            "        <company>Capitol</company>" +
            "        <price>8.90</price>" +
            "        <year>1983</year>" +
            "    </cd>" +
            "    <cd>" +
            "        <title>Midt om natten</title>" +
            "        <artist>Kim Larsen</artist>" +
            "        <country>EU</country>" +
            "        <company>Medley</company>" +
            "        <price>7.80</price>" +
            "        <year>1983</year>" +
            "    </cd>" +
            "    <cd>" +
            "        <title>Pavarotti Gala Concert</title>" +
            "        <artist>Luciano Pavarotti</artist>" +
            "        <country>UK</country>" +
            "        <company>DECCA</company>" +
            "        <price>9.90</price>" +
            "        <year>1991</year>" +
            "    </cd>" +
            "    <cd>" +
            "        <title>The dock of the bay</title>" +
            "        <artist>Otis Redding</artist>" +
            "        <country>USA</country>" +
            "        <company>Atlantic</company>" +
            "        <price>7.90</price>" +
            "        <year>1987</year>" +
            "    </cd>" +
            "    <cd>" +
            "        <title>Picture book</title>" +
            "        <artist>Simply Red</artist>" +
            "        <country>EU</country>" +
            "        <company>Elektra</company>" +
            "        <price>7.20</price>" +
            "        <year>1985</year>" +
            "    </cd>" +
            "    <cd>" +
            "        <title>Red</title>" +
            "        <artist>The Communards</artist>" +
            "        <country>UK</country>" +
            "        <company>London</company>" +
            "        <price>7.80</price>" +
            "        <year>1987</year>" +
            "    </cd>" +
            "    <cd>" +
            "        <title>Unchain my heart</title>" +
            "        <artist>Joe Cocker</artist>" +
            "        <country>USA</country>" +
            "        <company>EMI</company>" +
            "        <price>8.20</price>" +
            "        <year>1987</year>" +
            "    </cd>" +
            "</catalog>";
        try {
            XMLReader subject = XMLReaderFactory.createXMLReader(true);
            subject.parse(new InputSource(new ByteArrayInputStream(
                xmlFragment.getBytes())));
        } catch (SAXParseException e) {
            e.printStackTrace();
            fail(e.toString() + ":" + e.getMessage() + ":"
                 + e.getLineNumber() + "," + e.getColumnNumber());
        }
    }

    /**
     * Test the xsl parsing
     */
    public void testXSLParsing() throws Exception {
        String xmlFragment =
            "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n" +
            "    <xsl:output method=\"html\" encoding=\"iso-8859-1\"/>\n" +
            "\n" +
            "    <xsl:template match=\"/list\">\n" +
            "        <HTML>\n" +
            "            <HEAD>\n" +
            "                <TITLE>Title</TITLE>\n" +
            "            </HEAD>\n" +
            "            <BODY>\n" +
            "                <UL>\n" +
            "                    <xsl:apply-templates/>\n" +
            "                </UL>\n" +
            "            </BODY>\n" +
            "        </HTML>\n" +
            "    </xsl:template>\n" +
            "\n" +
            "    <xsl:template match=\"person\">\n" +
            "        <LI>\n" +
            "            <I>\n" +
            "                <xsl:value-of select=\"@number\"/>\n" +
            "            </I> -\n" +
            "            <B>\n" +
            "            <xsl:value-of select=\"lastname\"/>,\n" +
            "            </B>\n" +
            "            <xsl:value-of select=\"firstname\"/>\n" +
            "        </LI>\n" +
            "    </xsl:template>\n" +
            "</xsl:stylesheet>\n";
        try {
            XMLReader subject = XMLReaderFactory.createXMLReader(true);
            subject.parse(new InputSource(new ByteArrayInputStream(
                xmlFragment.getBytes())));
        } catch (SAXParseException e) {
            e.printStackTrace();
            fail(e.toString() + ":" + e.getMessage() + ":"
                 + e.getLineNumber() + "," + e.getColumnNumber());
        }
    }

    /**
     * Test to make sure the ContentHandler does not get leading whitespace
     *
     */
    public void testWhitespaceRemovalToCharacters() throws Exception {
        String xmlFragment =
            "           non-whitespace<one/><two><three></three></two>";
        try {
            XMLReader subject = XMLReaderFactory.createXMLReader(true);
            subject.setFeature(
                "http://www.volantis.com/DocumentFragmentParser/trimWhitespace",
                true);
            TestTrimNonWhitespaceXMLFilter filter = new TestTrimNonWhitespaceXMLFilter();
            subject.setContentHandler(filter);   
            subject.parse(new InputSource(new ByteArrayInputStream(
                                  xmlFragment.getBytes())));
        } catch (SAXParseException e) {
            e.printStackTrace();
            fail(e.toString()+":"+e.getMessage()+":"
                    +e.getLineNumber()+","+e.getColumnNumber());
        }
    }

    public void testPrefixInfixAndTrailingWhitespace() throws Exception {
        String INPUT =
            "   <one> \n text\n\n<two>This is some text </two>with " +
            "prefix, infix and trailing</one> values  \n";
        String EXPECTED =
            "<one>text <two>This is some text</two> with " +
            "prefix, infix and trailing</one> values";

        doTest(INPUT, EXPECTED);
    }

    public void testMoreWhitespaceGlobbing() throws Exception {
        String INPUT =
            "<one> little\nbit of \n   fluff</one> can cause\n<two> weeks " +
            "\n\n\nof chaos <in>bet</in><out>ween</out>\n</two>you and me    \n\n \n";
        String EXPECTED =
            "<one>little\nbit of fluff</one> can cause <two>weeks" +
            "\nof chaos <in>bet</in><out>ween</out></two> you and me";

        doTest(INPUT, EXPECTED);
    }

    protected void doTest(String input, String expected) throws Exception {
        try {
            XMLReader subject = XMLReaderFactory.createXMLReader(true);
            subject.setFeature(
                "http://www.volantis.com/DocumentFragmentParser/trimWhitespace",
                true);
            OutputCollector filter = new OutputCollector();
            subject.setContentHandler(filter);
            subject.parse(new InputSource(new ByteArrayInputStream(
                input.getBytes())));
            String actual = filter.getOutput();

            System.out.println("INPUT:    *" + input + "*");
            System.out.println("EXPECTED: *" + expected + "*");
            System.out.println("ACTUAL:   *" + actual + "*");

            assertEquals("Trimming not applied as",
                         expected,
                         actual);
        } catch (SAXParseException e) {
            e.printStackTrace();
            fail(e.toString() + ":" + e.getMessage() + ":"
                 + e.getLineNumber() + "," + e.getColumnNumber());
        }
    }

    /**
     * A private content handler to check if  <fragment> elements
     * reach it although they shouldn't
     */
    private class TestFragmentEventXMLFilter extends XMLFilterImpl {
        
        public void endElement(String uri, String localName, String qName)
            throws SAXException {
            if(localName.equals("fragment")) {
                fail("endElement() got <fragment> element");
            }
        }

        public void startElement(String uri, String localName, String qName,
                                 Attributes atts)
            throws SAXException {
            if(localName.equals("fragment")) {
                fail("startElement() got </fragment> element");
            }
        }
    }

    /**
     * A private content handler to check if whitespace leading characters are
     * being passed to the ContentHandler
     */
    private class TestTrimWhitespaceXMLFilter extends XMLFilterImpl {
        public void characters(char[] arg0, int off, int len)
            throws SAXException {
                fail("Got characters: "+new String(arg0, off, len)); 
        }
    }
    
    /**
     * A private content handler to check if non-whitespace leading characters are
     * being passed to the ContentHandler
     */
    private class TestTrimNonWhitespaceXMLFilter extends XMLFilterImpl {
        public void characters(char[] arg0, int off, int len)
            throws SAXException {
                assertTrue("Got characters: "+new String(arg0, off, len),
                    new String(arg0, off, len).equals("non-whitespace")); 
        }
    }

    /**
     * Helper that reconstructs an XML string from element and characters
     * events. Note that the short-hand, empty body form &lt;element .../&gt;
     * is not supported: this will always be output as
     * &lt;element ...&gt;&lt;/element&gt;
     */
    private static class OutputCollector extends XMLFilterImpl {
        /**
         * The output XML string.
         */
        private StringBuffer output = new StringBuffer();

        /**
         * Returns the output XML string.
         * @return
         */
        public String getOutput() {
            return output.toString();
        }

        // javadoc inherited
        public void startElement(String namespaceURI,
                                 String localName,
                                 String qName,
                                 Attributes attributes) throws SAXException {
            output.append('<').append(localName);

            for (int i = 0;
                 i < attributes.getLength();
                 i++) {
                output.append(' ').append(attributes.getLocalName(i));
                output.append(' ').append(attributes.getValue(i));
            }

            output.append('>');
        }

        // javadoc inherited
        public void endElement(String namespaceURI,
                               String localName,
                               String qName) throws SAXException {
            output.append("</").append(localName).append('>');
        }

        // javadoc inherited
        public void characters(char[] chars,
                               int start,
                               int length) throws SAXException {
            output.append(chars, start, length);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 27-Jun-03	143/1	philws	VBM:2003062610 Fix the document fragment filter's whitespace trimming

 16-Jun-03	23/4	byron	VBM:2003022819 Update to get jsp TLD files with correct merge

 13-Jun-03	23/2	byron	VBM:2003022819 Integration complete

 ===========================================================================
*/

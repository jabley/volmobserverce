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
package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.wbsax.AttributeStartCode;
import com.volantis.mcs.wbsax.AttributeStartFactory;
import com.volantis.mcs.wbsax.CharsetCode;
import com.volantis.mcs.wbsax.Codec;
import com.volantis.mcs.wbsax.ElementNameCode;
import com.volantis.mcs.wbsax.ElementNameFactory;
import com.volantis.mcs.wbsax.StringFactory;
import com.volantis.mcs.wbsax.WBSAXContentHandler;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.WBSAXString;
import com.volantis.mcs.wbsax.io.TestDebugProducer;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.StringWriter;

/**
 */
public class AccesskeyWBSAXFilterTestCase extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private Codec codec;

    private StringFactory strings;
    private WBSAXString s_x;
    private WBSAXString s_y;

    private ElementNameFactory elements;
    private ElementNameCode e;

    private AttributeStartFactory attributes;
    private AttributeStartCode a;
    private AttributeStartCode b;
    private AttributeStartCode accesskey;

    public void setUp() {
        codec = new Codec(new CharsetCode(1, "US-ASCII"));

        strings = new StringFactory(codec);
        s_x = strings.create("s x");
        s_y = strings.create("s y");

        elements = new ElementNameFactory();
        elements.registerElement(2, "e");
        e = elements.create("e");

        attributes = new AttributeStartFactory();
        attributes.registerAttributeStart(3, "a");
        attributes.registerAttributeStart(4, "b");
        attributes.registerAttributeStart(5, "accesskey"); // filter needs this
        a = attributes.create("a", null);
        b = attributes.create("b", null);
        accesskey = attributes.create("accesskey", null);
    }

    public void testEmptyAttributesNone() throws WBSAXException {
        StringWriter out = new StringWriter();
        WBSAXContentHandler handler = new TestDebugProducer(out);
        AccesskeyWBSAXFilter filter = new AccesskeyWBSAXFilter(codec,
                handler, attributes);

        // Check an element without attributes
        filter.startElement(e, false, false);
        // </e>
        assertEquals("", "[se:(c:e),false,false]", out.toString());
    }

    public void testEmptyAttributeOne() throws WBSAXException {
        StringWriter out = new StringWriter();
        WBSAXContentHandler handler = new TestDebugProducer(out);
        AccesskeyWBSAXFilter filter = new AccesskeyWBSAXFilter(codec,
                handler, attributes);

        // Check an element with a single attribute
        filter.startElement(e, true, false);
        filter.startAttributes();
        filter.addAttribute(a);
        filter.addAttributeValue(s_x);
        filter.addAttributeValue(s_y);
        filter.endAttributes();
        filter.endElement();
        // <e a="s xs y"/>
        assertEquals("",
                "[se:(c:e),true,false]" +
                    "[sa]" +
                        "[aa:(c:a,null)][av:(s:s x)][av:(s:s y)]" +
                    "[ea]" +
                "[ee]",
                out.toString());
    }

    public void testEmptyAttributesTwo() throws WBSAXException {
        StringWriter out = new StringWriter();
        WBSAXContentHandler handler = new TestDebugProducer(out);
        AccesskeyWBSAXFilter filter = new AccesskeyWBSAXFilter(codec,
                handler, attributes);

        // Check an element with two attributes
        filter.startElement(e, true, false);
        filter.startAttributes();
        filter.addAttribute(a);
        filter.addAttributeValue(s_x);
        filter.addAttributeValue(s_y);
        filter.addAttribute(b);
        filter.addAttributeValue(s_y);
        filter.addAttributeValue(s_x);
        filter.endAttributes();
        filter.endElement();
        // "<e a="s xs y" b="s ys x"/>"
        assertEquals("",
                "[se:(c:e),true,false]" +
                    "[sa]" +
                        "[aa:(c:a,null)][av:(s:s x)][av:(s:s y)]" +
                        "[aa:(c:b,null)][av:(s:s y)][av:(s:s x)]" +
                    "[ea]" +
                "[ee]",
                out.toString());
    }

    public void testEmptyAccesskeyNormal() throws WBSAXException {
        StringWriter out = new StringWriter();
        WBSAXContentHandler handler = new TestDebugProducer(out);
        AccesskeyWBSAXFilter filter = new AccesskeyWBSAXFilter(codec,
                handler, attributes);

        // Check one element with an normal accesskey element
        filter.startElement(e, true, false);
        filter.startAttributes();
        filter.addAttribute(accesskey);
        filter.addAttributeValue(s_x);
        filter.addAttributeValue(s_y);
        filter.endAttributes();
        filter.endElement();
        // <e accesskey="s xs y"/>
        assertEquals("",
                "[se:(c:e),true,false]" +
                    "[sa]" +
                        "[aa:(c:accesskey,null)][av:(s:s x)][av:(s:s y)]" +
                    "[ea]" +
                "[ee]", out.toString());
    }

    public void testEmptyAccesskeyOpaque() throws WBSAXException {
        StringWriter out = new StringWriter();
        WBSAXContentHandler handler = new TestDebugProducer(out);
        AccesskeyWBSAXFilter filter = new AccesskeyWBSAXFilter(codec,
                handler, attributes);

        // Check one element with an opaque value accesskey element
        filter.startElement(e, true, false);
        filter.startAttributes();
        filter.addAttribute(accesskey);
        filter.addAttributeValueOpaque(new AccesskeyOpaqueValue());
        filter.endAttributes();
        filter.endElement();
        // <e accesskey="1"/>
        assertEquals("",
                "[se:(c:e),true,false]" +
                    "[sa]" +
                        "[aa:(c:accesskey,null)][av:(o:1)]" +
                    "[ea]" +
                "[ee]",
                out.toString());
    }

    public void testEmptyAccesskeyOpaqueRemovalOnly() throws WBSAXException {
        StringWriter out = new StringWriter();
        WBSAXContentHandler handler = new TestDebugProducer(out);
        AccesskeyWBSAXFilter filter = new AccesskeyWBSAXFilter(codec,
                handler, attributes);

        // Check 10 elements with an opaque value accesskey element
        // Do the first 9, they will work normally
        StringBuffer result = new StringBuffer();
        for (int i = 1; i <= 9; i++ ) {
            filter.startElement(e, true, false);
            filter.startAttributes();
            filter.addAttribute(accesskey);
            filter.addAttributeValueOpaque(new AccesskeyOpaqueValue());
            filter.endAttributes();
            filter.endElement();
            // <e accesskey="{i}"/>
            result.append(
                "[se:(c:e),true,false]" +
                    "[sa]" +
                        "[aa:(c:accesskey,null)][av:(o:" + i + ")]" +
                    "[ea]" +
                "[ee]");
        }
        // Do the last one, it should be removed.
        filter.startElement(e, true, false);
        filter.startAttributes();
        filter.addAttribute(accesskey);
        filter.addAttributeValueOpaque(new AccesskeyOpaqueValue());
        filter.endAttributes();
        filter.endElement();
        // <e/>
        result.append("[se:(c:e),false,false]");
        assertEquals("", result.toString(), out.toString());
    }

    public void testEmptyAccesskeyOpaqueRemovalFirst() throws WBSAXException {
        StringWriter out = new StringWriter();
        WBSAXContentHandler handler = new TestDebugProducer(out);
        AccesskeyWBSAXFilter filter = new AccesskeyWBSAXFilter(codec,
                handler, attributes);

        // Check 10 elements with an opaque value accesskey element
        // Do the first 9, they will work normally
        StringBuffer result = new StringBuffer();
        for (int i = 1; i <= 9; i++ ) {
            filter.startElement(e, true, false);
            filter.startAttributes();
            filter.addAttribute(accesskey);
            filter.addAttributeValueOpaque(new AccesskeyOpaqueValue());
            filter.addAttribute(b);
            filter.addAttributeValue(s_y);
            filter.endAttributes();
            filter.endElement();
            // <e accesskey="{i}" b="s y"/>
            result.append(
                "[se:(c:e),true,false]" +
                    "[sa]" +
                        "[aa:(c:accesskey,null)][av:(o:" + i + ")]" +
                        "[aa:(c:b,null)][av:(s:s y)]" +
                    "[ea]" +
                "[ee]"
            );
        }
        // Do the last one, it should be removed.
        filter.startElement(e, true, false);
        filter.startAttributes();
        filter.addAttribute(accesskey);
        filter.addAttributeValueOpaque(new AccesskeyOpaqueValue());
        filter.addAttribute(b);
        filter.addAttributeValue(s_y);
        filter.endAttributes();
        filter.endElement();
        // <e b="s y"/>
        result.append(
                "[se:(c:e),true,false]" +
                    "[sa]" +
                        "[aa:(c:b,null)][av:(s:s y)]" +
                    "[ea]" +
                "[ee]"
        );
        assertEquals("", result.toString(), out.toString());
    }

    public void testEmptyAccesskeyOpaqueRemovalLast() throws WBSAXException {
        StringWriter out = new StringWriter();
        WBSAXContentHandler handler = new TestDebugProducer(out);
        AccesskeyWBSAXFilter filter = new AccesskeyWBSAXFilter(codec,
                handler, attributes);

        // Check 10 elements with an opaque value accesskey element
        // Do the first 9, they will work normally
        StringBuffer result = new StringBuffer();
        for (int i = 1; i <= 9; i++ ) {
            filter.startElement(e, true, false);
            filter.startAttributes();
            filter.addAttribute(b);
            filter.addAttributeValue(s_y);
            filter.addAttribute(accesskey);
            filter.addAttributeValueOpaque(new AccesskeyOpaqueValue());
            filter.endAttributes();
            filter.endElement();
            // <e b="s y" accesskey="{i}"/>
            result.append(
                    "[se:(c:e),true,false]" +
                        "[sa]" +
                            "[aa:(c:b,null)][av:(s:s y)]" +
                            "[aa:(c:accesskey,null)][av:(o:" + i + ")]" +
                        "[ea]" +
                    "[ee]"
            );
        }
        // Do the last one, it should be removed.
        filter.startElement(e, true, false);
        filter.startAttributes();
        filter.addAttribute(accesskey);
        filter.addAttributeValueOpaque(new AccesskeyOpaqueValue());
        filter.addAttribute(b);
        filter.addAttributeValue(s_y);
        filter.endAttributes();
        filter.endElement();
        // <e b="s y"/>
        result.append(
                "[se:(c:e),true,false]" +
                    "[sa]" +
                        "[aa:(c:b,null)][av:(s:s y)]" +
                    "[ea]" +
                "[ee]"
        );
        assertEquals("", result.toString(), out.toString());
    }

    public void testContentNormalAccesskeyOpaque() throws WBSAXException {
        StringWriter out = new StringWriter();
        WBSAXContentHandler handler = new TestDebugProducer(out);
        AccesskeyWBSAXFilter filter = new AccesskeyWBSAXFilter(codec,
                handler, attributes);

        // Check one element with an opaque value accesskey element
        filter.startElement(e, true, true);
        filter.startAttributes();
        filter.addAttribute(accesskey);
        filter.addAttributeValueOpaque(new AccesskeyOpaqueValue());
        filter.endAttributes();
        filter.startContent();
        filter.addContentValue(s_x);
        filter.addContentValue(s_y);
        filter.endContent();
        filter.endElement();
        // <e accesskey="1">s xs y</e>
        assertEquals("",
                "[se:(c:e),true,true]" +
                    "[sa]" +
                        "[aa:(c:accesskey,null)][av:(o:1)]" +
                    "[ea]" +
                    "[sc]" +
                        "[cv:(s:s x)][cv:(s:s y)]" +
                    "[ec]" +
                "[ee]",
                out.toString());
    }

    public void testContentOpaqueAccesskeyOpaque() throws WBSAXException {
        StringWriter out = new StringWriter();
        WBSAXContentHandler handler = new TestDebugProducer(out);
        AccesskeyWBSAXFilter filter = new AccesskeyWBSAXFilter(codec,
                handler, attributes);

        // Check one element with an opaque value accesskey element
        filter.startElement(e, true, true);
        filter.startAttributes();
        filter.addAttribute(accesskey);
        filter.addAttributeValueOpaque(new AccesskeyOpaqueValue());
        filter.endAttributes();
        filter.startContent();
        filter.addContentValueOpaque(new AccesskeyOpaqueValue());
        filter.addContentValue(s_y);
        filter.endContent();
        filter.endElement();
        // <e accesskey="1">1 xs y</e>
        assertEquals("",
                "[se:(c:e),true,true]" +
                    "[sa]" +
                        "[aa:(c:accesskey,null)][av:(o:1)]" +
                    "[ea]" +
                    "[sc]" +
                        "[cv:(o:1 )][cv:(s:s y)]" +
                    "[ec]" +
                "[ee]",
                out.toString());
    }

    public void testContentOpaqueAccesskeyOpaqueRemoval()
            throws WBSAXException {
        StringWriter out = new StringWriter();
        WBSAXContentHandler handler = new TestDebugProducer(out);
        AccesskeyWBSAXFilter filter = new AccesskeyWBSAXFilter(codec,
                handler, attributes);

        // Check 10 elements with an opaque value accesskey element
        // Do the first 9, they will work normally
        StringBuffer result = new StringBuffer();
        for (int i = 1; i <= 9; i++ ) {
            filter.startElement(e, true, true);
            filter.startAttributes();
            filter.addAttribute(accesskey);
            filter.addAttributeValueOpaque(new AccesskeyOpaqueValue());
            filter.endAttributes();
            filter.startContent();
            AccesskeyOpaqueValue opaqueValue = new AccesskeyOpaqueValue();
            filter.addContentValueOpaque(opaqueValue);
            filter.addContentValue(s_y);
            filter.endContent();
            filter.endElement();
            // <e accesskey="{i}">{i} s y</e>
            result.append(
                "[se:(c:e),true,true]" +
                    "[sa]" +
                        "[aa:(c:accesskey,null)][av:(o:" + i + ")]" +
                    "[ea]" +
                    "[sc]" +
                        "[cv:(o:" + i + " )][cv:(s:s y)]" +
                    "[ec]" +
                "[ee]");
        }
        // Do the last one, it should be removed.
        filter.startElement(e, true, true);
        filter.startAttributes();
        filter.addAttribute(accesskey);
        filter.addAttributeValueOpaque(new AccesskeyOpaqueValue());
        filter.endAttributes();
        filter.startContent();
        AccesskeyOpaqueValue opaqueValue = new AccesskeyOpaqueValue();
        filter.addContentValueOpaque(opaqueValue);
        filter.addContentValue(s_y);
        filter.endContent();
        filter.endElement();
        // <e>s y</e>
        result.append(
                "[se:(c:e),false,true]" +
                    "[sc]" +
                        "[cv:(s:s y)]" +
                    "[ec]" +
                "[ee]");
        assertEquals("", result.toString(), out.toString());
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Mar-05	7243/9	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 02-Mar-05	7120/3	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 02-Mar-05	7120/1	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Oct-03	1469/4	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols (fix rework stuff from phil)

 02-Oct-03	1469/2	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 ===========================================================================
*/

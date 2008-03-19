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

package com.volantis.mcs.runtime.dissection;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.runtime.TestableVolantis;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.testtools.application.AppContext;
import com.volantis.mcs.testtools.application.AppExecutor;
import com.volantis.mcs.testtools.application.AppManager;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.wbsax.AttributeStartCode;
import com.volantis.mcs.wbsax.AttributeStartFactory;
import com.volantis.mcs.wbsax.CharsetCode;
import com.volantis.mcs.wbsax.Codec;
import com.volantis.mcs.wbsax.PublicIdCode;
import com.volantis.mcs.wbsax.PublicIdFactory;
import com.volantis.mcs.wbsax.StringFactory;
import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.wbsax.StringTable;
import com.volantis.mcs.wbsax.TestWBSAXContentHandler;
import com.volantis.mcs.wbsax.TokenTable;
import com.volantis.mcs.wbsax.VersionCode;
import com.volantis.mcs.wbsax.WBSAXString;
import com.volantis.mcs.wbsax.wml.WMLVersion1_1TokenTable;
import com.volantis.testtools.stubs.ServletContextStub;
import junit.framework.TestCase;

import java.util.Iterator;

/**
 * This class tests the URLOptimiser to ensure that jsessionids are written to
 * the string table.
 * 
 * @todo stop using {@link TestWBSAXContentHandler}, it is a duplicate of
 *      {@link com.volantis.mcs.wbsax.EnumeratedWBSAXContentHandler}.
 */
public class URLOptimiserTestCase extends TestCase {

    /**
     * Default junit constructor
     * @param name
     */
    public URLOptimiserTestCase(String name) {
        super(name);        
    }

    /**
     * Test that jsessionids in url are added to the string table
     */
    public void testAddAttributeValue() throws Exception {
        TestWBSAXContentHandler contentHandler = new TestWBSAXContentHandler();
        URLOptimiser optimiser = new URLOptimiser(contentHandler);
            
        // Start the document
        VersionCode versionCode = VersionCode.V1_1;
        PublicIdCode publicIdCode = PublicIdFactory.WML_1_1;
        Codec codec = new Codec(new CharsetCode(4, "iso-8859-1"));
        StringFactory strings = new StringFactory(codec);
        StringTable stringTable = new StringTable();
        optimiser.setPageContext(getPageContext());
        optimiser.startDocument(versionCode, publicIdCode, codec, stringTable, 
                strings);
        
        // Send a href event to optimiser
        AttributeStartFactory attributeStartFactory = new AttributeStartFactory();
        TokenTable tokenTable = new WMLVersion1_1TokenTable();
        tokenTable.registerAttrStarts(attributeStartFactory);
        AttributeStartCode hrefStartCode
             = attributeStartFactory.create("href", null);
        optimiser.addAttribute(hrefStartCode);
        
        // send url to optimiser
        optimiser.addAttributeValue(new WBSAXString(codec, 
            "http://www.my.com/page.jsp;jsessionid=123?parm=value#ref"));
        
        // Send a title event to optimiser
        AttributeStartCode titleStartCode
            = attributeStartFactory.create("title", null);
        optimiser.addAttribute(titleStartCode);
        
        // Send the value of the title to optimiser
        optimiser.addAttributeValue(new WBSAXString(codec, "my title"));

        // Send another href to optimiser 
        optimiser.addAttribute(hrefStartCode);
        
        // send url to optimiser
        optimiser.addAttributeValue(new WBSAXString(codec,
            "http://www.my.com/page2.jsp;jsessionid=123?parm=value#ref2"));
        
        // Get the events       
        Iterator events = contentHandler.getEvents();
        
        // Check startDocument
        assertEquals("Wrong versionCode", versionCode, 
                     (VersionCode)events.next());
        assertEquals("Wrong publicIdCode", publicIdCode, 
                     (PublicIdCode)events.next());
        assertEquals("Wrong codec", codec, (Codec)events.next());        

        // Skip past the StringTable created by the optimiser 
        assertTrue("StringTable should be next",
                   events.next() instanceof StringTable);
        
        // Check href
        AttributeStartCode aStartCode = (AttributeStartCode)events.next();
        assertEquals("Wrong AttributeStartCode integer", 0x4A, 
                     aStartCode.getInteger());
        assertEquals("Wrong AttributeStartCode name", "href",
                     aStartCode.getName());
        assertNull("Wrong AttributeStartCode value prefix",
                   aStartCode.getValuePrefix());
        
        // Check prefix
        assertEquals("Wrong url prefix",
                     new WBSAXString(codec, "http://www.my.com/page.jsp"),
                     (WBSAXString)events.next());
        
        // Check jsessionid
        StringReference stringReference1 = (StringReference)events.next();        
        assertEquals("Wrong url jsessionid",
                     new WBSAXString(codec, ";jsessionid=123"),
                     stringReference1.resolveString());
        
        // Check suffix
        assertEquals("Wrong url suffix",
                     new WBSAXString(codec, "?parm=value#ref"),
                     (WBSAXString)events.next());
        
        // Check title
        aStartCode = (AttributeStartCode)events.next();
        assertEquals("Wrong AttributeStartCode integer", 0x36,
                     aStartCode.getInteger());
        assertEquals("Wrong AttributeStartCode name", "title",
                     aStartCode.getName());
        assertNull("Wrong AttributeStartCode value prefix",
                   aStartCode.getValuePrefix());
        
        // Check title's value        
        assertEquals("Wrong title", 
                     new WBSAXString(codec, "my title"),
                     (WBSAXString)events.next());
        
        // Check href
        aStartCode = (AttributeStartCode)events.next();
        assertEquals("Wrong AttributeStartCode integer", 0x4A,
                     aStartCode.getInteger());
        assertEquals("Wrong AttributeStartCode name", "href",
                     aStartCode.getName());
        assertNull("Wrong AttributeStartCode value prefix",
                   aStartCode.getValuePrefix());
        
        // Check prefix
        assertEquals("Wrong url prefix",
                     new WBSAXString(codec, "http://www.my.com/page2.jsp"),
                     (WBSAXString)events.next());
        
        // Check jsessionid, can now check logical and physical refs to ensure
        // jsession id was added only once to string table
        StringReference stringReference2 = (StringReference)events.next();        
        assertEquals("Wrong logical ref", 
                     stringReference1.resolveLogicalIndex(),
                     stringReference2.resolveLogicalIndex());
        assertEquals("Wrong physical ref",
                     stringReference1.resolvePhysicalIndex(),
                     stringReference2.resolvePhysicalIndex());
        assertEquals("Wrong url jsessionid",
                     new WBSAXString(codec, ";jsessionid=123"),
                     stringReference2.resolveString());
        
        // Check suffix
        assertEquals("Wrong url suffix", 
                     new WBSAXString(codec, "?parm=value#ref2"),
                events.next());

        optimiser.endDocument();
    }

    /**
     * Returns TestMarinerPageContext
     * @return the MarinerPageContext
     */
    private MarinerPageContext getPageContext() throws Exception {
        Volantis volantis = new TestableVolantis();
        ServletContextStub servletContext = new ServletContextStub();
        AppManager appManager = new AppManager(volantis, servletContext);        
        appManager.useAppWith(new AppExecutor() {
            public void execute(AppContext context) throws Exception {
                String err = context.getConsoleOutput().getErr();
                // Note: this is deliberately as specific as possible to 
                // ensure that ANY changes in the way that volantis 
                // initialisation affects System.err are at least found here; 
                // even if we get false positive matches this is better than 
                // the alternative.
                assertEquals("No error message", "", err);
            }
        });
        
        TestMarinerRequestContext requestContext
            = new TestMarinerRequestContext();
        TestMarinerPageContext pageContext = new TestMarinerPageContext();
        pageContext.pushRequestContext(requestContext);
        pageContext.setRequestURL(
            new MarinerURL("http://www.my.com/page2.jsp;jsessionid=123"));
        pageContext.setVolantis(volantis);
        return pageContext;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Aug-05	9391/1	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 28-Jun-04	4733/1	allan	VBM:2004062105 Convert Volantis to use the new PageURLRewriter

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 23-Jul-03	807/1	geoff	VBM:2003071405 works and tested but no design review yet

 14-Jul-03	790/1	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/1	geoff	VBM:2003070404 clean up WBSAX

 03-Jul-03	709/1	geoff	VBM:2003070209 hacked port from metis, without synergetics changes

 03-Jul-03	696/1	geoff	VBM:2003070209 clean up WBSAX test cases

 16-Jun-03	372/2	geoff	VBM:2003060609 avoid merge hell by committing on geoffs machine

 13-Jun-03	372/1	chrisw	VBM:2003060609 Implement wmlc url optimiser

 ===========================================================================
*/

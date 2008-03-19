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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/dissection/DissectorTestCase.java,v 1.8 2003/04/23 13:08:09 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 21-Jan-03    Adrian          VBM:2003011605 - created this testcase to test
 *                              the keeptogether hint
 * 30-Jan-03    Geoff           VBM:2003012101 - Add to do comment.
 * 12-Feb-03    Geoff           VBM:2003021110 - Add code to
 *                              testWriteShard() to generate a test license.
 * 12-Feb-03    Geoff           VBM:2003021110 - Remove references to
 *                              ConfigFileBuilder.getSingleton().
 * 18-Feb-03    Byron           VBM:2003020610 - Refactored test case to use
 *                              TestMarinerPageContext and fixed null pointer
 *                              exception.
 * 21-Feb-03    Geoff           VBM:2003022004 - Use new AppManager rather 
 *                              than less capable LicenseManager.
 * 25-Mar-03    Geoff           VBM:2003042306 - Use new AppExecutor.
 * 27-May-03    Allan           VBM:2003052207 - Modified doKeepTogetherTest() 
 *                              to set the DeviceLayoutContext in the 
 *                              MarinerPageContext. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.dissection;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.DefaultApplicationContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.DefaultProtocolSupportFactory;
import com.volantis.mcs.protocols.wml.WMLRoot;
import com.volantis.mcs.protocols.wml.WMLRootConfiguration;
import com.volantis.mcs.runtime.FragmentationState;
import com.volantis.mcs.runtime.PageGenerationCache;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.testtools.application.AppContext;
import com.volantis.mcs.testtools.application.AppExecutor;
import com.volantis.mcs.testtools.application.AppManager;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.testtools.stubs.ServletContextStub;
import junit.framework.TestCase;

import java.io.StringWriter;

/**
 * This class unit test the Dissectorclass.
 */
public class DissectorTestCase
    extends TestCase {

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    private TestMarinerPageContext marinerPageContext;

    private MyFragmentationState fragmentationState;

    private ServletContextStub servletContext;

    private Volantis volantis;

    public DissectorTestCase(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        volantis = new Volantis();

        // This anonymous inner class may be eligible for reuse if someone
        // intends to use it elsewhere. Currently no-one is requires it and
        // therefore will be 'lazily re-used' and refactored out in the future.
        PageGenerationCache pageGenerationCache = new PageGenerationCache() {
            public int getFragmentationStateChangeIndex(
                    FragmentationState.Change change) {
                return 0;
            }
        };
        fragmentationState = new MyFragmentationState();

        marinerPageContext = new TestMarinerPageContext();
        marinerPageContext.setVolantis(volantis);
        marinerPageContext.setRootPageURL(
                new MarinerURL("http://www.volantis.com/mypage.jsp"));
        marinerPageContext.setFragmentationState(fragmentationState);
        marinerPageContext.setPageGenerationCache(pageGenerationCache);
        servletContext = new ServletContextStub();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        marinerPageContext = null;
    }

    public void testDummy() {
        // Dummy test to avoid having to delete the file as we have disabled the
        // only test for the time being.
    }
    
    /**
     * This method tests the method public void writeShard ( DOMProtocol,Document,Writer )
     * for the com.volantis.mcs.protocols.dissection.Dissector class.
     * Disabled until new dissector implemented.
     */
    public void noTestWriteShard() throws Exception {
        AppManager mgr = new AppManager(volantis, servletContext); 
        mgr.useAppWith(new AppExecutor() {
            public void execute(AppContext context) throws Exception {
                doKeepTogetherTest();
            }
        });
    }

    /**
     * This method tests that KeepTogether elements are correctly handled
     */
    public void doKeepTogetherTest() throws Exception {

        Dissector dissector = new Dissector(
                new WMLRootConfiguration());

        Document document = domFactory.createDocument();
        Element root = domFactory.createElement();
        root.setName("wml");
        document.addNode(root);
        Element child = addChildElement(root, "card");
        child = addChildElement(child, "DISSECTABLE-CONTENTS");
        child.setAttribute("'DISSECTABLE PANE NAME'", "dissecting");
        child.setAttribute("'NEXT SHARD LINK TEXT'", "Next");
        child.setAttribute("'PREVIOUS SHARD LINK TEXT'", "Previous");

        Element parent = child;
        child = addChildElement(parent, "p");
        Text text = domFactory.createText();
        text.append("The Golden Globe winners for 2003 have been announced. " +
                "Here are the winners for film and TV. Best dramatic film:" +
                " The Hours.  Also nominated: About Schmidt, " +
                "Gangs of new York, The Pianist, " +
                "The Lord of the Rings: The Two Towers");


       // "  Meryl Streep - The Hours Diane Lane - Unfaithful"
        child.addHead(text);

        parent = addChildElement(parent, "KEEP-TOGETHER");
        child = addChildElement(parent, "p");
        text = domFactory.createText();
        text.append("Best dramatic actress: Nicole Kidman - The Hours ");
        child.addHead(text);
        child = addChildElement(parent, "p");
        text = domFactory.createText();
        text.append("Also nominated: Salma Hayek - Frida");
        child.addHead(text);
        child = addChildElement(parent, "p");
        text = domFactory.createText();
        text.append("Julianne Moore - Far From Heaven");
        child.addHead(text);
        child = addChildElement(parent, "p");
        text = domFactory.createText();
        text.append("Meryl Streep - The Hours");
        child.addHead(text);


        MyWMLRoot protocol = new MyWMLRoot();
        TestMarinerRequestContext requestContext = 
                new TestMarinerRequestContext();
        marinerPageContext.pushRequestContext(requestContext);
        marinerPageContext.pushDeviceLayoutContext(new DeviceLayoutContext());
        DefaultApplicationContext context = 
                new DefaultApplicationContext(requestContext);
        ContextInternals.setApplicationContext(requestContext, context);

        protocol.setMarinerPageContext(marinerPageContext);
        protocol.setDocument(document);
        protocol.setMaxPageSize(400);

        fragmentationState.setShardIndex(null, null, 0);
        dissector.annotateDocument(protocol, document);

        StringWriter writer = new StringWriter();
        dissector.writeShard(document, writer);

        String expected =
        "<wml><card><p>The Golden Globe winners for 2003 have been" +
        " announced. Here are the winners for film and TV. Best dramatic " +
        "film: The Hours.  Also nominated: About Schmidt, Gangs of new York" +
        ", The Pianist, The Lord of the Rings: The Two Towers</p><p>" +
        "<a href=\"mypage.jsp?vfrag=s0\">Next</a></p></card></wml>";

        assertEquals("Unexpected result from dissection.", expected,
                writer.toString());

        // do shard 1
        fragmentationState.setShardIndex(null, null, 1);
        dissector.annotateDocument(protocol, document);

        writer = new StringWriter();
        dissector.writeShard(document, writer);

        expected =
        "<wml><card><p>Best dramatic actress: Nicole Kidman - The Hours " +
        "</p><p>Also nominated: Salma Hayek - Frida</p><p>Julianne Moore - " +
        "Far From Heaven</p><p>Meryl Streep - The Hours</p><p>" +
        "<a href=\"mypage.jsp?vfrag=s0\">Previous</a></p></card></wml>";

        assertEquals("Unexpected result from dissection.", expected,
                writer.toString());

        // Reproduce a scenario where the content before the keeptogether just
        // splits onto two shards.  The keeptogether content should still be
        // to big to fit and will end up on the third shard
        protocol.setMaxPageSize(300);

        fragmentationState.setShardIndex(null, null, 0);
        dissector.annotateDocument(protocol, document);

        writer = new StringWriter();
        dissector.writeShard(document, writer);

        expected =
                "<wml><card><p>The Golden Globe winners for 2003 have been " +
                "announced. Here are the winners for film and TV. " +
                "Best dramatic film: The Hours.  Also nominated: About " +
                "...</p><p><a href=\"mypage.jsp?vfrag=s0\">Next</a>" +
                "</p></card></wml>";

        assertEquals("Unexpected result from dissection.", expected,
                writer.toString());

        fragmentationState.setShardIndex(null, null, 1);
        dissector.annotateDocument(protocol, document);

        writer = new StringWriter();
        dissector.writeShard(document, writer);

        expected =
                "<wml><card><p>... Schmidt, Gangs of new York, The Pianist," +
                " The Lord of the Rings: The Two Towers</p><p>" +
                "<a href=\"mypage.jsp?vfrag=s0\">Previous</a>" +
                "<a href=\"mypage.jsp?vfrag=s0\">Next</a></p></card></wml>";

        assertEquals("Unexpected result from dissection.", expected,
                writer.toString());

        fragmentationState.setShardIndex(null, null, 2);
        dissector.annotateDocument(protocol, document);

        writer = new StringWriter();
        dissector.writeShard(document, writer);

        expected =
                "<wml><card><p>Best dramatic actress: Nicole Kidman - The " +
                "Hours </p><p>Also nominated: Salma Hayek - Frida</p><p>" +
                "Julianne Moore - Far From Heaven</p><p>Meryl Streep - " +
                "The Hours</p><p><a href=\"mypage.jsp?vfrag=s0\">" +
                "Previous</a></p></card></wml>";

        assertEquals("Unexpected result from dissection.", expected,
                writer.toString());


        // here the page size means that the content of the keeptogether is
        // too big to fit onto one shard so will end up on both on the third
        // and fourth shards
        protocol.setMaxPageSize(275);

        fragmentationState.setShardIndex(null, null, 0);
        dissector.annotateDocument(protocol, document);

        writer = new StringWriter();
        dissector.writeShard(document, writer);

        expected =
                "<wml><card><p>The Golden Globe winners for 2003 have been " +
                "announced. Here are the winners for film and TV. " +
                "Best dramatic film: The Hours.  Also nominated: About " +
                "...</p><p><a href=\"mypage.jsp?vfrag=s0\">Next</a>" +
                "</p></card></wml>";

        assertEquals("Unexpected result from dissection.", expected,
                writer.toString());

        fragmentationState.setShardIndex(null, null, 1);
        dissector.annotateDocument(protocol, document);

        writer = new StringWriter();
        dissector.writeShard(document, writer);

        expected =
                "<wml><card><p>... Schmidt, Gangs of new York, The Pianist," +
                " The Lord of the Rings: The Two Towers</p><p>" +
                "<a href=\"mypage.jsp?vfrag=s0\">Previous</a>" +
                "<a href=\"mypage.jsp?vfrag=s0\">Next</a></p></card></wml>";

        assertEquals("Unexpected result from dissection.", expected,
                writer.toString());

        fragmentationState.setShardIndex(null, null, 2);
        dissector.annotateDocument(protocol, document);

        writer = new StringWriter();
        dissector.writeShard(document, writer);

        expected =
                "<wml><card><p>Best dramatic actress: Nicole Kidman - The" +
                " Hours </p><p>Also nominated: Salma Hayek - Frida</p><p>" +
                "Julianne Moore - Far From Heaven</p><p>Meryl Streep - ..." +
                "</p><p><a href=\"mypage.jsp?vfrag=s0\">Previous</a>" +
                "<a href=\"mypage.jsp?vfrag=s0\">Next</a></p></card></wml>";

        assertEquals("Unexpected result from dissection.", expected,
                writer.toString());

        fragmentationState.setShardIndex(null, null, 3);
        dissector.annotateDocument(protocol, document);

        writer = new StringWriter();
        dissector.writeShard(document, writer);

        expected =
                "<wml><card><p>... The Hours</p><p><a href=\"mypage.jsp?" +
                "vfrag=s0\">Previous</a></p></card></wml>";

        assertEquals("Unexpected result from dissection.", expected,
                writer.toString());


        // Finally reproduce a situation where the whole content fits on the
        // first shard.
        protocol.setMaxPageSize(500);
        fragmentationState.setShardIndex(null, null, 0);
        dissector.annotateDocument(protocol, document);

        writer = new StringWriter();
        dissector.writeShard(document, writer);

        expected =
                "<wml><card><p>The Golden Globe winners for 2003 have been " +
                "announced. Here are the winners for film and TV. Best " +
                "dramatic film: The Hours.  Also nominated: About Schmidt, " +
                "Gangs of new York, The Pianist, The Lord of the Rings: " +
                "The Two Towers</p><p>Best dramatic actress: Nicole Kidman" +
                " - The Hours </p><p>Also nominated: Salma Hayek - Frida" +
                "</p><p>Julianne Moore - Far From Heaven</p>" +
                "<p>Meryl Streep - The Hours</p></card></wml>";

        assertEquals("Unexpected result from dissection.", expected,
                writer.toString());

        fragmentationState.setShardIndex(null, null, 1);
        dissector.annotateDocument(protocol, document);
        writer = new StringWriter();

        boolean failed = false;
        try {
            dissector.writeShard(document, writer);
        } catch (ProtocolException e) {
            failed = true;
        }

        if (!failed) {
            fail("Requested shard should not have been available as all of" +
                    "the content was written in the first shard.");
        }
    }

    /**
     * Utility method to add a new Element with the given name to the given
     * Element.
     * @param parent The parent element to add the new element to.
     * @param name The name of the new element
     * @return The new element.
     */
    protected Element addChildElement(Element parent, String name) {
        Element element = domFactory.createElement();
        element.setName(name);
        element.addToTail(parent);
        return element;
    }

    /**
     * Protocol for use in doKeeptogetherTest
     */
    private class MyWMLRoot extends WMLRoot {
        public MyWMLRoot() {
            super(new DefaultProtocolSupportFactory(),
                    new WMLRootConfiguration());
        }

        public void setMaxPageSize(int max) {
            this.maxPageSize = max;
        }

        public void setDocument(Document document) {
            this.document = document;
        }
    }


    /**
     * We want to be able to set the shard that we want to generate so use our
     * own sub-class of FragmentationState.
     * <p>
     * This inner class may be eligible for reuse if someone intends to use it
     * elsewhere. Currently no-one uses it elsewhere and therefore should be
     * 'lazily re-used' and refactored out in the future but that someone else.
     */
    public class MyFragmentationState extends FragmentationState {
        private int index;

        public int getShardIndex(String inclusionPath,
                                 String paneName) {
            return index;
        }

        public void setShardIndex(String inclusionPath,
                                  String paneName,
                                  int shardIndex) {
            index = shardIndex;
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10333/1	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 04-Oct-05	9522/4	ibush	VBM:2005091502 no_save on images

 03-Oct-05	9522/1	ibush	VBM:2005091502 no_save on images

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 29-Jun-04	4713/2	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 06-Jun-03	335/1	mat	VBM:2003042906 Merged changes to MCS

 ===========================================================================
*/

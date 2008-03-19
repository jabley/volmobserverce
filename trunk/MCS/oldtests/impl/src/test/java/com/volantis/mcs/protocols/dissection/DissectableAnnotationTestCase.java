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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/dissection/DissectableAnnotationTestCase.java,v 1.2 2003/04/16 10:23:22 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Mar-03    Doug            VBM:2003030409 - Created to test the
 *                              DissectableAnnotation class.
 * 17-Apr-03    Geoff           VBM:2003041505 - Commented out System.out
 *                              calls which clutter the JUnit console output.
 * 23-May-03    Allan           VBM:2003052207 - Modified doLinkNavTest() to be
 *                              compatible with shard links generated as menus.
 *                              Added some more tests.
 * 27-May-03    Allan           VBM:2003052207 - Remove the br from the
 *                              expected results in doLinkNavTest().
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.dissection;

import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.DefaultProtocolSupportFactory;
import com.volantis.mcs.protocols.wml.WMLVersion1_3;
import com.volantis.mcs.protocols.wml.WMLVersion1_3Configuration;
import com.volantis.mcs.runtime.PageGenerationCache;
import com.volantis.mcs.utilities.ReusableStringBuffer;
import junit.framework.TestCase;
import junitx.util.PrivateAccessor;

/**
 * Class to test the DissectableAnnotation
 */
public class DissectableAnnotationTestCase
        extends TestCase implements DissectionConstants {

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    private static final String NEXT_TEXT = "next";
    private static final String PREV_TEXT = "prev";
    private static final String HREF = "href";

    public DissectableAnnotationTestCase(String name) {
        super(name);
    }

    /**
     * Test createStandAloneNextLink.
     * Disabled until new dissector implemented.
     */
    public void noTestCreateStandAloneNextLink() throws Exception {
        DissectableAnnotation annotation =
                createTestableAnnotation();
        StringBuffer nextLink =
                annotation.createStandAloneNextLink();
        StringBuffer expected = new StringBuffer();
        expected.append("<p><a href=\"" + HREF + "\">").append(NEXT_TEXT).
                append("</a></p>");
        assertEquals(expected.toString(), nextLink.toString());
    }

    /**
     * Test createStandAlonePreviousLink.
     * Disabled until new dissector implemented.
     */
    public void noTestCreateStandAlonePreviousLink() throws Exception {
        DissectableAnnotation annotation =
                createTestableAnnotation();
        StringBuffer nextLink =
                annotation.createStandAlonePreviousLink();
        StringBuffer expected = new StringBuffer();
        expected.append("<p><a href=\"" + HREF + "\">").append(PREV_TEXT).
                append("</a></p>");
        assertEquals(expected.toString(), nextLink.toString());
    }

    /**
     * Fixture to test the next/prev link ordering of the
     * generateDissectedContents() method
     * @exception ProtocolException if an error occurs
     */
    public void testNavLinkOrdering() throws ProtocolException {
        // test prev then next
        doLinkNavTest(true);
        doLinkNavTest(false);
    }

    /**
     * Tests that the correct shardLinkMarkup is output for 
     * the first, last and middle shards.
     */
    public void testShardLinkMarkup() throws ProtocolException,
            NoSuchFieldException {

        final StringBuffer previousLinkMarkup = new StringBuffer("previous");
        final StringBuffer bothLinksMarkup = new StringBuffer("both");
        final StringBuffer nextLinkMarkup = new StringBuffer("next");

        DissectableAnnotation annotation = new DissectableAnnotation() {
            protected StringBuffer createShardLinkMenu() throws ProtocolException {
                return bothLinksMarkup;
            }
            protected StringBuffer createStandAloneNextLink() throws ProtocolException {
                return nextLinkMarkup;
            }
            protected StringBuffer createStandAlonePreviousLink() throws ProtocolException {
                return previousLinkMarkup;
            }
            public boolean generateShardContentsImpl(ReusableStringBuffer buffer,
                                                     int shardNumber,
                                                     boolean all) {
                return true;
            }
        };

        annotation.setRequestedShard(0);
        annotation.firstShard = 0;
        annotation.lastShard = 9;
        PrivateAccessor.setField(annotation, "shardCount", new Integer(10));
        
        ReusableStringBuffer rsb = new ReusableStringBuffer();
        annotation.generateDissectedContents(rsb);
        assertEquals(nextLinkMarkup.toString(), rsb.toString());

        annotation.setRequestedShard(5);
        rsb = new ReusableStringBuffer();
        annotation.generateDissectedContents(rsb);
        assertEquals(bothLinksMarkup.toString(), rsb.toString());

        annotation.setRequestedShard(10);
        rsb = new ReusableStringBuffer();
        annotation.generateDissectedContents(rsb);
        assertEquals(previousLinkMarkup.toString(), rsb.toString());
    }

    /**
     * Helper method for the testNavLinkOrdering() fixture
     * @param nextFirst should the next link come first
     * @exception ProtocolException if an error occurs
     */
    private void doLinkNavTest(boolean nextFirst)
            throws ProtocolException {

        DissectableAnnotation annotation =
                createUninitialisedTestableAnnotation();

        Element element = annotation.getElement();

        element.setAttribute(GENERATE_NEXT_LINK_FIRST_ATTRIBUTE,
                             String.valueOf(nextFirst));

        annotation.initialise();

        annotation.selectRequestedShard(1);
        annotation.getOverheadSize();

        ReusableStringBuffer buffer = new ReusableStringBuffer();
        annotation.generateDissectedContents(buffer);

        StringBuffer expected = new StringBuffer();
        String first = (nextFirst) ? NEXT_TEXT : PREV_TEXT;
        String second = (nextFirst) ? PREV_TEXT : NEXT_TEXT;

        expected.append("<p>")
                .append("<a href=\"" + HREF + "\">")
                .append(first).append("</a>")
                .append("<a href=\"" + HREF + "\">")
                .append(second).append("</a>")
                .append("</p>");

        assertEquals("Navigation link order incorrect",
                     expected.toString(),
                     buffer.toString());


    }

    /**
     * Create a DissectableAnnotation whose initialise() method has not yet
     * been called that can be used for testing.
     * @return a DissectableAnnotation.
     */
    protected DissectableAnnotation createUninitialisedTestableAnnotation() {
        DissectableAnnotation annotation = new DissectableAnnotation() {
            protected String createShardLinkHRef(int shardIndex) {
                return HREF;
            }

            public int getContentsSize() {
                return Integer.MAX_VALUE;
            }

            protected int markShardNodesImpl(int shardNumber, int limit) {
                return SHARD_COMPLETE;
            }

            public boolean generateShardContentsImpl(ReusableStringBuffer buffer,
                                                     int shardNumber,
                                                     boolean all) {
                return true;
            }
        };

        // ensures that a previous link is generated
        annotation.setRequestedShard(1);

        TestMarinerPageContext context = new TestMarinerPageContext();
        context.pushDeviceLayoutContext(new DeviceLayoutContext());
        context.setPageGenerationCache(new PageGenerationCache());

        class TestableWMLVersion1_3 extends WMLVersion1_3 {

            public TestableWMLVersion1_3() {
                super(new DefaultProtocolSupportFactory(),
                        new WMLVersion1_3Configuration());
            }

            public void setDocument(Document document) {
                this.document = document;
            }
        }

        TestableWMLVersion1_3 protocol = new TestableWMLVersion1_3();
        protocol.setMarinerPageContext(context);

        Document document = domFactory.createDocument();
        document.setObject(new DocumentAnnotation());
        protocol.setDocument(document);

        Element element = domFactory.createElement();
        element.setAttribute(NEXT_SHARD_LINK_TEXT_ATTRIBUTE, NEXT_TEXT);
        element.setAttribute(PREVIOUS_SHARD_LINK_TEXT_ATTRIBUTE, PREV_TEXT);
        element.setObject(annotation);
        annotation.setElement(element);

        annotation.setProtocol(protocol);

        return annotation;
    }

    /**
     * Create a DissectableAnnotation that can be used for testing.
     * @return a DissectableAnnotation.
     */
    protected DissectableAnnotation createTestableAnnotation()
            throws ProtocolException {

        DissectableAnnotation annotation =
                createUninitialisedTestableAnnotation();
        annotation.initialise();

        return annotation;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Oct-05	7445/2	emma	VBM:2005031701 Forward port: Fix dissecting pane problems with small max page size

 04-Oct-05	9522/4	ibush	VBM:2005091502 no_save on images

 03-Oct-05	9522/1	ibush	VBM:2005091502 no_save on images

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 22-Mar-05	7441/1	emma	VBM:2005031701 Candidate fix for dissecting pane problems with small max page size

 15-Mar-05	7414/2	emma	VBM:2005031108 Merged from MCS 3.3.0 - fix for problem with dissecting panes with small max size

 15-Mar-05	7396/1	emma	VBM:2005031108 Fixing problem with dissecting panes with small max size

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Jun-04	4713/2	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 06-Jun-03	335/1	mat	VBM:2003042906 Merged changes to MCS

 ===========================================================================
*/

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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.vdxml;

import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.AnchorAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.FraglinkAttributes;
import com.volantis.mcs.protocols.LineBreakAttributes;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.properties.MCSFragmentListOrientationKeywords;
import com.volantis.styling.StylesBuilder;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * A test case for {@link VDXMLFragmentLinkRenderer}.
 */
public class VDXMLFragmentLinkRendererTestCase extends TestCaseAbstract {

    /**
     * Test the rendering of a "child" fragment link ... i.e. a link from
     * a fragment to a contained fragment. The text for these links appears
     * inline in the content.
     */
    public void testChildLink() throws Exception {

        FraglinkAttributes attrs = new FraglinkAttributes();
        attrs.setHref("href");
        attrs.setName("name");
        final DOMOutputBuffer outputBuffer = new DOMOutputBuffer();
        outputBuffer.writeText("text");
        attrs.setLinkText(outputBuffer);
        attrs.setInList(false);

        String expectedLink = "<link shortcut=\"name\" url=\"href\">";
        String expectedChildText = "<pane props=\"done\">text</pane>";
        String expectedListText = "";

        checkRendering(null, attrs, expectedLink, expectedChildText,
                expectedListText);
    }

    /**
     * Test the rendering of a "peer/parent" fragment link ... i.e. a link
     * from a fragment to a peer or parent fragment. The text for these links
     * appears in a special fragment link list pane separate from the normal
     * content.
     *
     * @throws Exception
     */
    public void testListLink() throws Exception {

        FraglinkAttributes attrs = new FraglinkAttributes();
        attrs.setHref("href");
        attrs.setName("name");
        final DOMOutputBuffer outputBuffer = new DOMOutputBuffer();
        outputBuffer.writeText("text");
        attrs.setLinkText(outputBuffer);
        attrs.setInList(true);

        String expectedLink = "<link shortcut=\"name\" url=\"href\">";
        String expectedChildText = "";
        String expectedListText = "text<br>";

        checkRendering(null, attrs, expectedLink, expectedChildText,
                expectedListText);
    }

    /**
     * Test the rendering of the mariner fragment list orientation style
     * property for a parent/peer fragment link.
     */
    public void testOrientation() throws Exception {

        FraglinkAttributes attrs = new FraglinkAttributes();
        attrs.setHref("href");
        attrs.setName("name");
        DOMOutputBuffer outputBuffer = new DOMOutputBuffer();
        outputBuffer.writeText("text");
        attrs.setLinkText(outputBuffer);
        attrs.setInList(true);

        String expectedLink = "<link shortcut=\"name\" url=\"href\">";
        String expectedChildText = "";
        String expectedListText = "text<br>";

        // Test with default orientation - this is vertical (ie with <br>).
        checkRendering(null, attrs, expectedLink, expectedChildText,
                expectedListText);

        outputBuffer = new DOMOutputBuffer();
        outputBuffer.writeText("text");
        attrs.setLinkText(outputBuffer);

        // Test with vertical orientation ... this is the same as the default
        checkRendering(MCSFragmentListOrientationKeywords.VERTICAL, attrs,
                expectedLink, expectedChildText,
                expectedListText);

        // Test with horizontal orientation (i.e. with " ").
        expectedListText = "text ";
        attrs.setStyles(StylesBuilder.getStyles(
                "mcs-fragment-list-orientation: horizontal"));

        outputBuffer = new DOMOutputBuffer();
        outputBuffer.writeText("text");
        attrs.setLinkText(outputBuffer);

        checkRendering(MCSFragmentListOrientationKeywords.HORIZONTAL, attrs,
                expectedLink, expectedChildText,
                expectedListText);
    }

    /**
     * Helper method to render a fragment link and check the expected results
     * against the actual results.
     *
     * @param orientation the mariner fragment list orientation to use.
     * @param attrs the fragment link attributes to use.
     * @param expectedLink the expected value of the active part of the link
     *      rendered (i.e. the shortcut and href).
     * @param expectedChildText the expected value of any link text rendered as
     *      a child link - may be "" if none expected.
     * @param expectedListText the expected value of the link text rendered as
     *      a entry in the peer/parent link list - rendered - may be "" if none
     *      expected.
     * @throws Exception
     */
    private void checkRendering(final StyleKeyword orientation,
                                FraglinkAttributes attrs,
            final String expectedLink, final String expectedChildText,
            final String expectedListText) throws Exception {

        TestDOMOutputBuffer linkDom = new TestDOMOutputBuffer();
        TestDOMOutputBuffer childTextDom = new TestDOMOutputBuffer();
        TestDOMOutputBuffer listTextDom = new TestDOMOutputBuffer();

        TestVDXMLFragmentLinkRendererContext context =
                new TestVDXMLFragmentLinkRendererContext(orientation, linkDom,
                        listTextDom);

        VDXMLFragmentLinkRenderer renderer = new VDXMLFragmentLinkRenderer(
                context);
        renderer.doFragmentLink(childTextDom, attrs);

        String link = DOMUtilities.toString(linkDom.getRoot());
        //System.out.println("link='" + link + "'");
        assertEquals("", expectedLink, link);

        String childText = DOMUtilities.toString(childTextDom.getRoot());
        //System.out.println("child text='" + childText + "'");
        assertEquals("", expectedChildText, childText);

        String listText = DOMUtilities.toString(listTextDom.getRoot());
        //System.out.println("list text='" + listText + "'");
        assertEquals("", expectedListText, listText);
    }

    /**
     * A test implementation of VDXMLFragmentLinkRendererContext.
     */
    private static class TestVDXMLFragmentLinkRendererContext
            extends VDXMLFragmentLinkRendererContext {

        /**
         * The mariner fragment list orientation value to provide to the
         * renderer.
         */
        private final StyleKeyword orientation;

        /**
         * The buffer to render links into.
         */
        private final DOMOutputBuffer linkBuffer;

        /**
         * The buffer to render the fragment link list text into.
         */
        private final DOMOutputBuffer listTextBuffer;

        /**
         * Initialise.
         *
         * @param orientation the mariner fragment list orientation value to
         *      provide to the renderer.
         * @param linkBuffer the buffer to render links into.
         * @param listTextBuffer the buffer to render the fragment link list
         *      text into.
         */
        public TestVDXMLFragmentLinkRendererContext(StyleKeyword orientation,
                DOMOutputBuffer linkBuffer,
                DOMOutputBuffer listTextBuffer) {
            super(null);
            this.orientation = orientation;
            this.linkBuffer = linkBuffer;
            this.listTextBuffer = listTextBuffer;
        }

        // Javadoc inherited.
        public void outputExternalLink(String shortcut, String url) {

            linkBuffer.writeText("<link shortcut=\"" + shortcut + "\" " +
                    "url=\"" + url + "\">");
        }

        // Javadoc inherited.
        public DOMOutputBuffer getFragmentLinksBuffer(String fragmentName)
                throws ProtocolException {

            return listTextBuffer;
        }

        // Javadoc inherited.
        public void addFakePaneAttributes(Element element, String formatName,
                MutablePropertyValues propertyValues) {

            element.setAttribute("props", "done");
        }

        // Javadoc inherited.
        public void doAnchor(OutputBuffer outputBuffer,
                AnchorAttributes attributes) throws ProtocolException {

            throw new IllegalStateException("should never be called");
        }

        // Javadoc inherited.
        public void doLineBreak(OutputBuffer outputBuffer,
                LineBreakAttributes attributes) throws ProtocolException {

            outputBuffer.writeText("<br>");
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 30-Jun-05	8893/2	emma	VBM:2005062406 Annotate DOM elements generated from VDXML with styles

 22-Jun-05	8483/3	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 01-Oct-04	5635/1	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 24-Sep-04	5613/2	geoff	VBM:2004092215 Port VDXML to MCS: update fragment link support

 ===========================================================================
*/

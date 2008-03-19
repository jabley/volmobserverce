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
package com.volantis.mcs.protocols;

import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.styling.StylesBuilder;

/**
 * A test case for the default fragment link renderer.
 */ 
public class DefaultFragmentLinkRendererTestCase extends TestCaseAbstract {

    /**
     * Typesafe Enum to represent the different types of mariner fragment list
     * orientations we can have.
     */ 
    private static class Orientation {
        private String value;

        private Orientation(String value) {
            this.value = value;
        }

        static Orientation None = new Orientation(null);
        static Orientation Vertical = new Orientation("vertical");
        static Orientation Horizontal = new Orientation("horizontal");
        
        public String toString() {
            return value;
        }
    }
    
    /**
     * Test the doFragmentLink method with a simple attributes.
     *  
     * This should generate a single select option with the fraglink 
     * attributes contained in it.
     */ 
    public void testDoFragmentLinkSimple() throws Exception {

        FraglinkAttributes attrs = new FraglinkAttributes();
        attrs.setHref("href");
        final DOMOutputBuffer outputBuffer = new DOMOutputBuffer();
        outputBuffer.writeText("text");
        attrs.setLinkText(outputBuffer);
        checkDoFragmentLink(attrs, Orientation.None);

    }

    /**
     * Test the doFragmentLink method with simple attributes and which is in a
     * "list".
     *  
     * This should generate a single select option with the fraglink 
     * attributes contained in it, with the default/vertical separator after.
     *
     * todo fix this up when the initial value for the mcs-fragment-list-orientation is corrected.
     */ 
    public void notestDoFragmentLinkInListDefault() throws Exception {

        checkDoFragmentLinkInList(Orientation.None);

    }

    /**
     * Test the doFragmentLink method with simple attributes and which is in a
     * "list", where the list style is vertical.
     *  
     * This should generate a single select option with the fraglink 
     * attributes contained in it, with the default/vertical separator after.
     */ 
    public void testDoFragmentLinkInListVertical() throws Exception {

        checkDoFragmentLinkInList(Orientation.Vertical);

    }

    /**
     * Test the doFragmentLink method with simple attributes and which is in a
     * "list", where the list style is horizontal.
     *  
     * This should generate a single select option with the fraglink 
     * attributes contained in it, with the horizontal separator after.
     */ 
    public void testDoFragmentLinkInListHorizontal() throws Exception {

        checkDoFragmentLinkInList(Orientation.Horizontal);

    }

    /**
     * A helper method to test doFragmentLink where frag link is in a list,
     * and the MarinerFragmentListOrientation is as provided, and the other
     * attributes are hardcoded.
     * 
     * @param orientation the orientation of the list (i.e. the separator)
     * @throws Exception
     */ 
    private void checkDoFragmentLinkInList(Orientation orientation) 
            throws Exception {
        
        FraglinkAttributes attrs = new FraglinkAttributes();
        attrs.setStyles(StylesBuilder.getCompleteStyles(
                "mcs-fragment-list-orientation: " + orientation));
        attrs.setHref("href");
        final DOMOutputBuffer outputBuffer = new DOMOutputBuffer();
        outputBuffer.writeText("text");
        attrs.setLinkText(outputBuffer);
        attrs.setInList(true);
        checkDoFragmentLink(attrs, orientation);
        
    }

    /**
     * A helper method to test doFragmentLink with the attributes provided,
     * and MarinerFragmentListOrientation style emulation as provided.
     * 
     * @param orientation the orientation of the list to be emulated
     * @throws Exception
     */ 
    private void checkDoFragmentLink(FraglinkAttributes attrs, 
            final Orientation orientation) throws Exception {
        
        // Implement any methods of the renderer context we need. 
        // This means that this test is independent of the Protocol.
        FragmentLinkRendererContext context = 
                new FragmentLinkRendererContext(null) {

                    public void doAnchor(OutputBuffer outputBuffer,
                            AnchorAttributes attributes) {
                        outputBuffer.writeText(
                                "<anchor " + 
                                        // style class is representative of Core attrs
                                        "href='" + attributes.getHref() + "'" + 
                                    ">" + 
                                    attributes.getContent() + 
                                "</anchor>");
                    }

                    public void doLineBreak(OutputBuffer outputBuffer,
                            LineBreakAttributes attributes) {
                        outputBuffer.writeText("<linebreak/>");
                    }
                };
        // Create the renderer under test with the testing context.
        DefaultFragmentLinkRenderer renderer = 
                new DefaultFragmentLinkRenderer(context);
        
        DOMOutputBuffer buffer = new TestDOMOutputBuffer();
        
        // Run the method under test.
        renderer.doFragmentLink(buffer, attrs);

        // Construct the expected result based on the inputs.
        String expected = 
                "<anchor " + 
                        "href='" + attrs.getHref() + "'" +
                        ">" + 
                    attrs.getLinkText() + 
                "</anchor>";
        if (attrs.isInList()) {
            if (orientation == Orientation.Horizontal) {
                expected += " ";
            } else {
                // default to vertical
                expected += "<linebreak/>";
            }
        }
        
        String actual = DOMUtilities.toString(buffer.getRoot());
        
        assertEquals("Default Fragment Link not rendering correctly", 
                expected, actual);
        
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 17-Sep-03	1412/1	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 ===========================================================================
*/

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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.trans.NullRemovingDOMTransformer;

/**
 * Test that the {@link HTML_iModeDivRemover} works as expected..
 */
public class HTML_iModeDivRemoverTestCase
        extends AbstractDivRemoverTestAbstract {

    // Javadoc inherited.
    public AbstractDivRemover getDivRemover() {
        return new HTML_iModeDivRemover();
    }

    /**
     * Test that the inner div tag is not removed if the outer div tag has an
     * id attribute.
     *
     * @throws Exception if there was a problem.
     */
    public void testRemoveInnerDivTagWhenOuterDivHasIDAttribute()
            throws Exception {

        String input =
                "<div id=\"1\">" +
                "<div foo=\"a\"/>" +
                "</div>";

        checkRemoveInnerDivTag(input, input);
    }

    /**
     * Test that outer and inner div classes are added together properly when
     * the inner div has an ID attribute.
     *
     * @throws Exception if there was a problem.
     */
    public void testRemoveInnerDivTagWhenInnerDivOnlyHasIDAttribute()
            throws Exception {

        String input =
                "<div align=\"left\">" +
                "<div id=\"innerID\">test</div>" +
                "</div>";
        String expected =
                "<div align=\"left\" id=\"innerID\">test</div>";

        checkRemoveInnerDivTag(input, expected);
    }

    /**
     * Test that nothing happens if the outer div tag has no div children.
     *
     * @throws Exception if there was a problem.
     */
    public void testRemoveInnerDivTagWhenOuterDivHasNoDivChildren()
            throws Exception {

        String input = "<div><p>test</p></div>";

        checkRemoveInnerDivTag(input, input);
    }

    /**
     * Test that the inner div tag is not removed if the outer div tag has
     * multiple children (div or otherwise).
     *
     * @throws Exception if there was a problem.
     */
    public void testRemoveInnerDivTagWhenOuterDivHasMultipleChildren()
            throws Exception {

        String input =
                "<div>" +
                "<div foo=\"a\"/>" +
                "<p>test</p>" +
                "<div>test</div>" +
                "</div>";

        checkRemoveInnerDivTag(input, input);
    }

    /**
     * Test that the inner div tag is removed if the outer div tag has an align
     * attribute but the inner doesn't.
     *
     * @throws Exception if there was a problem.
     */
    public void testRemoveInnerDivTagWhenOnlyOuterDivHasAlignAttribute()
            throws Exception {

        String input =
                "<div align=\"left\">" +
                "<div>" +
                "<p>test</p>" +
                "</div>" +
                "</div>";
        String expected = "<div align=\"left\"><p>test</p></div>";

        checkRemoveInnerDivTag(input, expected);
    }

    /**
     * Test that the inner div tag is removed if the inner div tag has an align
     * attribute but the outer doesn't.
     *
     * @throws Exception if there was a problem.
     */
    public void testRemoveInnerDivTagWhenOnlyInnerDivHasAlignAttribute()
            throws Exception {

        String input =
                "<div>" +
                "<div align=\"left\">" +
                "<p>test</p>" +
                "</div>" +
                "</div>";

        String expected = "<div align=\"left\"><p>test</p></div>";

        checkRemoveInnerDivTag(input, expected);
    }

    /**
     * Test that the inner div tag is removed if neither the inner or the outer
     * div tag has an align attribute.
     *
     * @throws Exception if there was a problem.
     */
    public void testRemoveInnerDivTagWhenNeitherDivHasAlignAttribute()
            throws Exception {

        String input =
                "<div>" +
                "<div>" +
                "<p>test</p>" +
                "</div>" +
                "</div>";
        String expected = "<div><p>test</p></div>";

        checkRemoveInnerDivTag(input, expected);
    }

    /**
     * Test that the inner div tag is removed if both the inner and the outer
     * div tag have the same value for their align attribute.
     *
     * @throws Exception if there was a problem.
     */
    public void testRemoveInnerDivTagWhenBothDivsHaveSameAlignAttribute()
            throws Exception {

        String input =
                "<div align=\"left\">" +
                "<div align=\"left\">" +
                "<p>test</p>" +
                "</div>" +
                "</div>";

        String expected = "<div align=\"left\"><p>test</p></div>";

        checkRemoveInnerDivTag(input, expected);
    }

    /**
     * Test that the inner div tag is not removed if the outer div tag has an
     * align attribute with a different value to the inner.
     *
     * @throws Exception if there was a problem.
     */
    public void testRemoveInnerDivTagWhenBothDivsHaveDifferentAlignAttribute()
            throws Exception {

        String input =
                "<div align=\"right\">" +
                "<div align=\"left\">" +
                "<p>test</p>" +
                "</div>" +
                "</div>";

        checkRemoveInnerDivTag(input, input);
    }

    /**
     * Utility method to test the removeInnerDivTag method.
     *
     * @param input    the input DOM containing an outer and inner div.
     * @param expected the expected DOM containing the resultant merged div.
     * @throws Exception if there was a problem.
     */
    private void checkRemoveInnerDivTag(String input, String expected)
            throws Exception {

        Document dom = getStrictStyledDOMHelper().parse(input);
        Document expectedDOM = getStrictStyledDOMHelper().parse(expected);

        Element outerDiv = dom.getRootElement();

        HTML_iModeDivRemover divRemover = new HTML_iModeDivRemover();
        divRemover.removeInnerDivTag(outerDiv);

        // Remove any null elements added by the remapping before rendering.
        // In normal operation this is done at the DOMTransformer level.
        // @todo I would rather use the NullRemovingDOMTransformer, but as
        // mocks are currently not accessible from integration tests, and
        // StyledDOMTester is not accessible from unit tests, it's too awkward
        // at the moment...
        NullRemovingDOMTransformer.NullRemover remover =
                new NullRemovingDOMTransformer.NullRemover();
        remover.removeNullElements(dom);

        String actual = getStrictStyledDOMHelper().render(dom);
        String canonicalExpected = getStrictStyledDOMHelper().render(expectedDOM);

        assertEquals(canonicalExpected, actual);
    }

    /**
     * Verify that an element with a non null id attribute cannot be removed.
     */
    public void testIsElementEligibleForRemovalElementWithId() {
        DOMFactory factory = DOMFactory.getDefaultInstance();
        Element div = factory.createElement("div");
        div.setAttribute("id", "myID");

        HTML_iModeDivRemover divRemover = new HTML_iModeDivRemover();
        assertEquals(false, divRemover.isDivEligibleForRemoval(div));
    }

    /**
     * Verify that a null element cannot be removed.
     */
    public void testIsElementEligibleForRemovalNullElement() {
        HTML_iModeDivRemover divRemover = new HTML_iModeDivRemover();
        assertEquals(false, divRemover.isDivEligibleForRemoval(null));
    }

    /**
     * Verify that an element with a non null align attribute cannot be
     * removed, unless it has the same value as the parent align attribute.
     */
    public void testIsElementEligibleForRemovalElementWithAlign() {
        DOMFactory factory = DOMFactory.getDefaultInstance();
        Element div = factory.createElement("div");
        div.setAttribute("align", "left");
        Element innerDiv = factory.createElement("div");
        innerDiv.setAttribute("align", "left");
        div.addHead(innerDiv);

        HTML_iModeDivRemover divRemover = new HTML_iModeDivRemover();
        assertEquals(true, divRemover.isDivEligibleForRemoval(innerDiv));

        innerDiv.setAttribute("align", "right");
        assertEquals(false, divRemover.isDivEligibleForRemoval(innerDiv));
    }

    /**
     * Verify that a non null element with a null id and align attribute can
     * be removed.
     */
    public void testIsElementEligibleForRemovalElement() {
        DOMFactory factory = DOMFactory.getDefaultInstance();
        Element div = factory.createElement("div");

        HTML_iModeDivRemover divRemover = new HTML_iModeDivRemover();
        assertEquals(true, divRemover.isDivEligibleForRemoval(div));
    }
}

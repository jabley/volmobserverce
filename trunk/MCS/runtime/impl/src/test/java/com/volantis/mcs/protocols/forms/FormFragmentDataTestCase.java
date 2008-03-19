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
package com.volantis.mcs.protocols.forms;

import com.volantis.mcs.runtime.URLConstants;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.MCSBreakAfterKeywords;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.styling.PseudoElements;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import com.volantis.styling.StylesMock;
import com.volantis.styling.values.MutablePropertyValuesMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.List;

/**
 * Test class for FormFragmentData
 */
public class FormFragmentDataTestCase extends TestCaseAbstract {

    /**
     * form fragment identifier
     */
    private static final String NAME = "fragmentOne";

    /**
     * previous link text
     */
    private static final String PREVIOUS_TEXT = "previousText";

    /**
     * next link text
     */
    private static final String NEXT_TEXT = "nextText";

    //constructor
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Test the FormFragmentData when all the Pseudo styles have been specified
     */
    public void testConstructionWithFullyPopulatedData() {

        // Set up test objects.
        String css = "::before::mcs-next{content:none} " +
                "::before::mcs-previous{content:normal} " +
                "::before::mcs-reset{content:\"Test label\"} " +
                "::after::mcs-reset{content:none} " +
                "::after::mcs-previous{content: \"Back to \" contents} " +
                "::after::mcs-next{content:contents} ";
        Styles styles = StylesBuilder.getCompleteStyles(css, true);

        // Run test.        
        XFormGroup group = new XFormGroup(styles, null);
        FormFragmentData formFragmentData = new FormFragmentData(NAME, group);
        XFormGroup previousGroup = new XFormGroup(styles, null);
        previousGroup.setLabel(PREVIOUS_TEXT);
        FormFragmentData previous = new FormFragmentData(NAME, previousGroup);
        XFormGroup nextGroup = new XFormGroup(styles, null);
        nextGroup.setLabel(NEXT_TEXT);
        FormFragmentData next = new FormFragmentData(NAME, nextGroup);
        assertNotNull(formFragmentData);
        assertEquals(NAME, formFragmentData.getName());
        assertEquals(styles, formFragmentData.getStyles());

        List after = formFragmentData.getAfterFragmentLinks(previous, next);
        assertNotNull(after);
        assertEquals(2, after.size());
        Link previousAfter = (Link) after.get(0);
        Link nextAfter = (Link) after.get(1);
        assertEquals(URLConstants.NEXT_FORM_FRAGMENT, nextAfter.getLinkName());
        assertEquals(URLConstants.PREV_FORM_FRAGMENT, previousAfter.getLinkName());
        assertEquals(NEXT_TEXT, nextAfter.getLinkText());
        assertEquals("Back to " + PREVIOUS_TEXT, previousAfter.getLinkText());

        List before = formFragmentData.getBeforeFragmentLinks(previous, next);
        assertNotNull(before);
        assertEquals(3, before.size());
        Link nextBefore = (Link) before.get(0);
        Link previousBefore = (Link) before.get(1);
        Link resetBefore = (Link) before.get(2);
        assertEquals(URLConstants.NEXT_FORM_FRAGMENT, nextBefore.getLinkName());
        assertEquals(URLConstants.PREV_FORM_FRAGMENT, previousBefore.getLinkName());
        assertEquals(URLConstants.RESET_FORM_FRAGMENT, resetBefore.getLinkName());
        assertEquals(NEXT_TEXT, nextBefore.getLinkText());
        assertEquals(PREVIOUS_TEXT, previousBefore.getLinkText());
        assertEquals("Test label", resetBefore.getLinkText());
    }

    /**
     * Test the FormFragmentData when all the Pseudo styles have been specified
     */
    public void testConstructionWithFullyPopulatedDataPt2() {

        // Set up test objects.
        String css =
            "::before::mcs-next{content: \"\" contents contents} " +
            "::before::mcs-previous{content: \"Go to \" none \"Prev. page\"} " +
            "::before::mcs-reset{content:\"Reset page: \" contents} " +
            "::after::mcs-reset{content: \"reset page\" contents normal}" +
            "::after::mcs-previous{content: \"Prev. \" \"page\"} " +
            "::after::mcs-next{content:\"This is the real Text\"} ";
        Styles styles = StylesBuilder.getCompleteStyles(css, true);

        // Run test.
        XFormGroup group = new XFormGroup(styles, null);
        group.setLabel("Middle group");
        FormFragmentData formFragmentData = new FormFragmentData(NAME, group);
        XFormGroup previousGroup = new XFormGroup(styles, null);
        previousGroup.setLabel(PREVIOUS_TEXT);
        FormFragmentData previous = new FormFragmentData(NAME, previousGroup);
        XFormGroup nextGroup = new XFormGroup(styles, null);
        nextGroup.setLabel(null);
        FormFragmentData next = new FormFragmentData(NAME, nextGroup);
        assertNotNull(formFragmentData);
        assertEquals(NAME, formFragmentData.getName());
        assertEquals(styles, formFragmentData.getStyles());

        List after = formFragmentData.getAfterFragmentLinks(previous, next);
        assertNotNull(after);
        assertEquals(3, after.size());
        Link resetAfter = (Link) after.get(0);
        Link previousAfter = (Link) after.get(1);
        Link nextAfter = (Link) after.get(2);
        assertEquals(URLConstants.RESET_FORM_FRAGMENT, resetAfter.getLinkName());
        assertEquals(URLConstants.NEXT_FORM_FRAGMENT, nextAfter.getLinkName());
        assertEquals(URLConstants.PREV_FORM_FRAGMENT, previousAfter.getLinkName());
        assertEquals("reset pageMiddle groupMiddle group",
            resetAfter.getLinkText());
        assertEquals("This is the real Text", nextAfter.getLinkText());
        assertEquals("Prev. page", previousAfter.getLinkText());

        List before = formFragmentData.getBeforeFragmentLinks(previous, next);
        assertNotNull(before);
        assertEquals(3, before.size());
        Link nextBefore = (Link) before.get(0);
        Link previousBefore = (Link) before.get(1);
        Link resetBefore = (Link) before.get(2);
        assertEquals(URLConstants.NEXT_FORM_FRAGMENT, nextBefore.getLinkName());
        assertEquals(URLConstants.PREV_FORM_FRAGMENT, previousBefore.getLinkName());
        assertEquals(URLConstants.RESET_FORM_FRAGMENT, resetBefore.getLinkName());
        assertEquals(
            AbstractFormFragment.NEXT_TEXT + AbstractFormFragment.NEXT_TEXT,
            nextBefore.getLinkText());
        assertEquals("Go to Prev. page", previousBefore.getLinkText());
        assertEquals("Reset page: Middle group", resetBefore.getLinkText());
    }

    /**
     * Test the FormFragmentData when only the Pseudo style before has been
     * specified using the next link
     */
    public void testConstructionWithNextLinkPopulatedData() {

        // Set up test objects.
        String css = "::before::mcs-next{content:none} ";
        Styles styles = StylesBuilder.getCompleteStyles(css, true);

        // Run test.
        XFormGroup group = new XFormGroup(styles, null);
        FormFragmentData formFragmentData = new FormFragmentData(NAME, group);
        XFormGroup previousGroup = new XFormGroup(styles, null);
        previousGroup.setLabel(PREVIOUS_TEXT);
        FormFragmentData previous = new FormFragmentData(NAME, previousGroup);
        XFormGroup nextGroup = new XFormGroup(styles, null);
        nextGroup.setLabel(NEXT_TEXT);
        FormFragmentData next = new FormFragmentData(NAME, nextGroup);

        assertNotNull(formFragmentData);
        assertEquals(NAME, formFragmentData.getName());
        assertEquals(styles, formFragmentData.getStyles());

        //check that the default previous link was created.
        List after = formFragmentData.getAfterFragmentLinks(previous, next);
        assertNotNull(after);
        assertEquals(1, after.size());
        Link prevAfter = (Link) after.get(0);
        assertEquals(URLConstants.PREV_FORM_FRAGMENT, prevAfter.getLinkName());

        //check that the specified next link was created.
        List before = formFragmentData.getBeforeFragmentLinks(previous, next);
        assertNotNull(before);
        assertEquals(1, before.size());

        Link nextBefore = (Link) before.get(0);
        assertEquals(URLConstants.NEXT_FORM_FRAGMENT, nextBefore.getLinkName());
        assertEquals(NEXT_TEXT, nextBefore.getLinkText());
    }

    /**
     * Test the FormFragmentData when only the Pseudo style before has been
     * specified using the previous link
     */
    public void testConstructionWithPreviousLinkPopulatedData() {

        // Set up test objects.
        String css = "::before::mcs-previous{content:none} ";
        Styles styles = StylesBuilder.getCompleteStyles(css, true);

        // Run test.
        XFormGroup group = new XFormGroup(styles, null);
        FormFragmentData formFragmentData = new FormFragmentData(NAME, group);
        XFormGroup previousGroup = new XFormGroup(styles, null);
        previousGroup.setLabel(PREVIOUS_TEXT);
        FormFragmentData previous = new FormFragmentData(NAME, previousGroup);
        XFormGroup nextGroup = new XFormGroup(styles, null);
        nextGroup.setLabel(NEXT_TEXT);
        FormFragmentData next = new FormFragmentData(NAME, nextGroup);
        assertNotNull(formFragmentData);
        assertEquals(NAME, formFragmentData.getName());
        assertEquals(styles, formFragmentData.getStyles());

        //check that the default previous link was created.
        List after = formFragmentData.getAfterFragmentLinks(previous, next);
        assertNotNull(after);
        assertEquals(1, after.size());
        Link nextAfter = (Link) after.get(0);
        assertEquals(URLConstants.NEXT_FORM_FRAGMENT, nextAfter.getLinkName());

        //check that the specified next link was created.
        List before = formFragmentData.getBeforeFragmentLinks(previous, next);
        assertNotNull(before);
        assertEquals(1, before.size());
        Link nextBefore = (Link) before.get(0);
        assertEquals(URLConstants.PREV_FORM_FRAGMENT, nextBefore.getLinkName());
        assertEquals(PREVIOUS_TEXT, nextBefore.getLinkText());
    }

    /**
     * Test the FormFragmentData when no Pseudo styles have been specified
     */
    public void testConstructionWithDataWithoutFragmentLinks() {
        // Create test objects.
        String name = "fragmentOne";
        StylesMock styles = new StylesMock("styles", expectations);
        MutablePropertyValuesMock values =
                new MutablePropertyValuesMock("values", expectations);

        // Set expectations.
        styles.expects.removeNestedStyles(PseudoElements.BEFORE).returns(null);
        styles.expects.removeNestedStyles(PseudoElements.AFTER).returns(null);
        styles.expects.getPropertyValues().returns(values).fixed(5);
        values.expects.getSpecifiedValue(StylePropertyDetails.MCS_BREAK_AFTER).
                returns(MCSBreakAfterKeywords.ALWAYS).fixed(3);
        values.expects.getSpecifiedValue(StylePropertyDetails.CONTENT).
                returns(StyleKeywords.NORMAL).fixed(2);

        // Run test.
        XFormGroup group = new XFormGroup(styles, null);
        FormFragmentData formFragmentData = new FormFragmentData(NAME, group);
        XFormGroup previousGroup = new XFormGroup(styles, null);
        previousGroup.setLabel(PREVIOUS_TEXT);
        FormFragmentData previous = new FormFragmentData(NAME, previousGroup);
        XFormGroup nextGroup = new XFormGroup(styles, null);
        nextGroup.setLabel(NEXT_TEXT);
        FormFragmentData next = new FormFragmentData(NAME, nextGroup);
        assertNotNull(formFragmentData);
        assertEquals(name, formFragmentData.getName());
        assertEquals(styles, formFragmentData.getStyles());

        List after = formFragmentData.getAfterFragmentLinks(previous, next);
        assertNotNull(after);
        assertEquals(2, after.size());
        Link nextAfter = (Link) after.get(0);
        assertEquals(URLConstants.NEXT_FORM_FRAGMENT, nextAfter.getLinkName());
        Link prevAfter = (Link) after.get(1);
        assertEquals(URLConstants.PREV_FORM_FRAGMENT, prevAfter.getLinkName());
    }
}

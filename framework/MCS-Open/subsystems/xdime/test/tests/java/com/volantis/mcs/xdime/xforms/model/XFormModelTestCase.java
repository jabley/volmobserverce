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
package com.volantis.mcs.xdime.xforms.model;

import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.protocols.forms.FormFragmentData;
import com.volantis.mcs.protocols.forms.EmulatedXFormDescriptor;
import com.volantis.mcs.protocols.DeviceLayoutContextMock;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.MCSBreakAfterKeywords;
import com.volantis.styling.PseudoElements;
import com.volantis.styling.StylesMock;
import com.volantis.styling.values.MutablePropertyValuesMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.List;

public class XFormModelTestCase extends TestCaseAbstract {

    private XFormModelImpl model;
    StylesMock styles;
    MutablePropertyValuesMock values;

    protected void setUp() throws Exception {
        super.setUp();
        model = new XFormModelImpl("ModelID", new EmulatedXFormDescriptor());
        styles = new StylesMock("styles", expectations);
        values = new MutablePropertyValuesMock("values", expectations);
    }

    private void setMCSBreakAfterExpectation(StylesMock styles,
            StyleKeyword value, int count) {
        styles.expects.getPropertyValues().returns(values).fixed(count);
        values.expects.getSpecifiedValue(
                StylePropertyDetails.MCS_BREAK_AFTER).returns(value).fixed(count);
    }

    public void testUpdateFormFragmentationStateOnFirstFragment() {
        MarinerPageContextMock pageContext = new MarinerPageContextMock(
                "pageContext", expectations);
        pageContext.expects.updateFormFragmentationState(model.getID()).returns(null);
        assertEquals(-1, model.getRequestedFragmentIndex());
        model.updateFormFragmentationState(pageContext);
        assertEquals(0, model.getRequestedFragmentIndex());
    }

    public void testUpdateFormFragmentationStateOnSubsequentFragments() {
        MarinerPageContextMock pageContext = new MarinerPageContextMock(
                "pageContext", expectations);
        pageContext.expects.updateFormFragmentationState(model.getID()).returns("ff1");
        assertEquals(-1, model.getRequestedFragmentIndex());
        model.updateFormFragmentationState(pageContext);
        assertEquals(1, model.getRequestedFragmentIndex());
    }

    public void testUpdateFormFragmentationStateWhenKnowReqFragIndex() {
        MarinerPageContextMock pageContext = new MarinerPageContextMock(
                "pageContext", expectations);
        pageContext.expects.updateFormFragmentationState(model.getID()).returns(null);
        assertEquals(-1, model.getRequestedFragmentIndex());
        model.updateFormFragmentationState(pageContext);
        assertEquals(0, model.getRequestedFragmentIndex());
        model.updateFormFragmentationState(pageContext);
        assertEquals(0, model.getRequestedFragmentIndex());
    }

    public void testPushFirstGroup() {
        MarinerPageContextMock pageContext = new MarinerPageContextMock(
                "pageContext", expectations);
        DeviceLayoutContextMock dlc =
                    new DeviceLayoutContextMock("dlc", expectations);
        StylesMock styles = new StylesMock("styles", expectations);

        setMCSBreakAfterExpectation(styles, MCSBreakAfterKeywords.NEVER, 1);
        pageContext.expects.getDeviceLayoutContext().returns(dlc);
            dlc.expects.getInclusionPath().returns(null);
        pageContext.expects.updateFormFragmentationState(model.getID()).returns(null);

        assertNull(model.getCurrentFormFragment());
        assertEquals(0, model.getNestingDepth());
        model.pushGroup(styles, pageContext);

        final FormFragmentData currentFormFragment = model.getCurrentFormFragment();
        assertNotNull(currentFormFragment);
        assertEquals(1, model.getNestingDepth());
    }

    public void testPushSecondOuterGroup() {
        MarinerPageContextMock pageContext = new MarinerPageContextMock(
                "pageContext", expectations);
        DeviceLayoutContextMock dlc =
                    new DeviceLayoutContextMock("dlc", expectations);
        StylesMock styles = new StylesMock("styles", expectations);

        setMCSBreakAfterExpectation(styles, MCSBreakAfterKeywords.ALWAYS, 2);
        pageContext.expects.getDeviceLayoutContext().returns(dlc).fixed(2);
        dlc.expects.getInclusionPath().returns(null).fixed(2);
        pageContext.expects.updateFormFragmentationState(model.getID()).returns(null);

        assertNull(model.getCurrentFormFragment());
        assertEquals(0, model.getNestingDepth());
        model.pushGroup(styles, pageContext);

        final FormFragmentData currentFormFragment = model.getCurrentFormFragment();
        assertNotNull(currentFormFragment);
        assertEquals(1, model.getNestingDepth());

        model.popGroup();
        assertEquals(currentFormFragment, model.getCurrentFormFragment());
        assertEquals(0, model.getNestingDepth());

        model.pushGroup(styles, pageContext);
        final FormFragmentData newFormFragment = model.getCurrentFormFragment();
        assertNotNull(currentFormFragment);
        assertNotEquals(currentFormFragment, newFormFragment);
        assertEquals(1, model.getNestingDepth());
    }

    public void testPushNestedGroupWhenLastCausesFragmentation() {
        MarinerPageContextMock pageContext = new MarinerPageContextMock(
                "pageContext", expectations);
        DeviceLayoutContextMock dlc =
                    new DeviceLayoutContextMock("dlc", expectations);
        StylesMock styles = new StylesMock("styles", expectations);

        setMCSBreakAfterExpectation(styles, MCSBreakAfterKeywords.ALWAYS, 2);
        pageContext.expects.getDeviceLayoutContext().returns(dlc).fixed(2);
        dlc.expects.getInclusionPath().returns(null).fixed(2);
        pageContext.expects.updateFormFragmentationState(model.getID()).returns(null);

        assertNull(model.getCurrentFormFragment());
        assertEquals(0, model.getNestingDepth());
        model.pushGroup(styles, pageContext);

        final FormFragmentData currentFormFragment = model.getCurrentFormFragment();
        assertNotNull(currentFormFragment);
        assertEquals(1, model.getNestingDepth());

        model.pushGroup(styles, pageContext);
        final FormFragmentData newFormFragment = model.getCurrentFormFragment();
        assertNotNull(newFormFragment);
        assertEquals(currentFormFragment, newFormFragment);
        assertEquals(2, model.getNestingDepth());
    }

    public void testPushNestedGroupWhenLastDoesNotCauseFragmentation() {
        MarinerPageContextMock pageContext = new MarinerPageContextMock(
                "pageContext", expectations);
        DeviceLayoutContextMock dlc =
                    new DeviceLayoutContextMock("dlc", expectations);
        StylesMock styles = new StylesMock("styles", expectations);

        setMCSBreakAfterExpectation(styles, MCSBreakAfterKeywords.NEVER, 2);
        pageContext.expects.getDeviceLayoutContext().returns(dlc).fixed(2);
        dlc.expects.getInclusionPath().returns(null).fixed(2);
        pageContext.expects.updateFormFragmentationState(model.getID()).returns(null);

        assertNull(model.getCurrentFormFragment());
        assertEquals(0, model.getNestingDepth());
        model.pushGroup(styles, pageContext);

        final FormFragmentData currentFormFragment = model.getCurrentFormFragment();
        assertNotNull(currentFormFragment);
        assertEquals(1, model.getNestingDepth());

        model.pushGroup(styles, pageContext);
        final FormFragmentData newFormFragment = model.getCurrentFormFragment();
        assertNotNull(newFormFragment);
        assertEquals(currentFormFragment, newFormFragment);
        assertEquals(2, model.getNestingDepth());
        assertEquals(1, model.getFormFragments().size());
    }

    public void testPopGroupWhenNotNested() {
        MarinerPageContextMock pageContext = new MarinerPageContextMock(
                "pageContext", expectations);
        DeviceLayoutContextMock dlc =
                    new DeviceLayoutContextMock("dlc", expectations);
        StylesMock styles = new StylesMock("styles", expectations);

        setMCSBreakAfterExpectation(styles, MCSBreakAfterKeywords.NEVER, 1);
        pageContext.expects.getDeviceLayoutContext().returns(dlc);
        dlc.expects.getInclusionPath().returns(null);
        pageContext.expects.updateFormFragmentationState(model.getID()).returns(null);

        // Push the group on.
        assertNull(model.getCurrentFormFragment());
        assertEquals(0, model.getNestingDepth());
        model.pushGroup(styles, pageContext);
        final FormFragmentData currentFormFragment = model.getCurrentFormFragment();
        assertNotNull(currentFormFragment);
        assertEquals(1, model.getNestingDepth());

        // Pop the group off.
        model.popGroup();
        assertEquals(currentFormFragment, model.getCurrentFormFragment());
        assertEquals(0, model.getNestingDepth());
    }

    public void testPopGroupWhenNestedAndDoesNotFragment() {
        // Create test objects.
        MarinerPageContextMock pageContext = new MarinerPageContextMock(
                "pageContext", expectations);
        DeviceLayoutContextMock dlc =
                    new DeviceLayoutContextMock("dlc", expectations);
        StylesMock styles = new StylesMock("styles", expectations);
        StylesMock innerStyles = new StylesMock("innerStyles", expectations);

        // Set expectations.
        pageContext.expects.updateFormFragmentationState(model.getID()).returns(null);
        pageContext.expects.getDeviceLayoutContext().returns(dlc).fixed(2);
        dlc.expects.getInclusionPath().returns(null).fixed(2);
        setMCSBreakAfterExpectation(styles, MCSBreakAfterKeywords.ALWAYS, 1);
        setMCSBreakAfterExpectation(innerStyles, MCSBreakAfterKeywords.NEVER, 1);

        // Get model in the correct state.
        // Push the group on.
        assertNull(model.getCurrentFormFragment());
        assertEquals(0, model.getNestingDepth());
        model.pushGroup(styles, pageContext);
        final FormFragmentData currentFormFragment = model.getCurrentFormFragment();
        assertNotNull(currentFormFragment);
        assertEquals(1, model.getNestingDepth());
        model.pushGroup(innerStyles, pageContext);
        final FormFragmentData newFormFragment = model.getCurrentFormFragment();
        assertEquals(currentFormFragment, newFormFragment);
        // Verify that it is nested.
        assertEquals(2, model.getNestingDepth());

        // Run the test - pop the group off.
        model.popGroup();
        assertEquals(currentFormFragment, model.getCurrentFormFragment());
        assertEquals(1, model.getNestingDepth());
    }

    public void testPopGroupWhenNestedAndDoesFragment() {
        MarinerPageContextMock pageContext = new MarinerPageContextMock(
                "pageContext", expectations);
        DeviceLayoutContextMock dlc =
                    new DeviceLayoutContextMock("dlc", expectations);
        StylesMock styles = new StylesMock("styles", expectations);

        setMCSBreakAfterExpectation(styles, MCSBreakAfterKeywords.ALWAYS, 2);
        pageContext.expects.getDeviceLayoutContext().returns(dlc).fixed(2);
        dlc.expects.getInclusionPath().returns(null).fixed(2);
        pageContext.expects.updateFormFragmentationState(model.getID()).returns(null);

        // Push the group on.
        assertNull(model.getCurrentFormFragment());
        assertEquals(0, model.getNestingDepth());
        model.pushGroup(styles, pageContext);
        final FormFragmentData currentFormFragment = model.getCurrentFormFragment();
        assertNotNull(currentFormFragment);
        assertEquals(1, model.getNestingDepth());
        model.pushGroup(styles, pageContext);
        final FormFragmentData newFormFragment = model.getCurrentFormFragment();
        assertEquals(currentFormFragment, newFormFragment);
        // Verify that it is nested.
        assertEquals(2, model.getNestingDepth());

        // Pop the group off.
        model.popGroup();
        assertNotEquals(currentFormFragment, model.getCurrentFormFragment());
        assertEquals(1, model.getNestingDepth());
    }

    /**
     * Test that the first form fragment is created properly.
     */
    public void testCreateFormFragment() {
        MarinerPageContextMock pageContext = new MarinerPageContextMock(
                "pageContext", expectations);
        DeviceLayoutContextMock dlc =
                    new DeviceLayoutContextMock("dlc", expectations);

        // Set expectations.
        setMCSBreakAfterExpectation(styles, MCSBreakAfterKeywords.NEVER, 1);
        pageContext.expects.getDeviceLayoutContext().returns(dlc);
        dlc.expects.getInclusionPath().returns(null);
        pageContext.expects.updateFormFragmentationState(model.getID()).returns(null);

        assertNull(model.getCurrentFormFragment());
        assertEquals(0, model.getNestingDepth());
        assertTrue(model.getFormFragments().isEmpty());
        model.pushGroup(styles, pageContext);
        final FormFragmentData currentFormFragment = model.getCurrentFormFragment();
        assertNotNull(currentFormFragment);
        List fragments = model.getFormFragments();
        assertNotNull(fragments);
        assertEquals(1, fragments.size());
        assertEquals(currentFormFragment, fragments.get(0));
    }
}

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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.renderer.shared.layouts.unit;

import com.volantis.mcs.context.EnvironmentContextMock;
import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.context.ResponseCachingDirectives;
import com.volantis.mcs.layouts.FormatMock;
import com.volantis.mcs.layouts.FragmentMock;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.DeviceLayoutContextMock;
import com.volantis.mcs.protocols.layouts.FormatInstanceMock;
import com.volantis.mcs.protocols.layouts.FragmentInstanceMock;
import com.volantis.mcs.protocols.renderer.layouts.FormatRenderer;
import com.volantis.mcs.protocols.renderer.shared.layouts.FragmentRenderer;
import com.volantis.mcs.unit.layouts.LayoutTestHelper;
import com.volantis.shared.system.SystemClock;
import com.volantis.testtools.mock.expectations.OrderedExpectations;


/**
 * Tests rendering functionality of default fragment renderer.
 */
public class FragmentRendererTestCase
        extends AbstractFormatRendererTestAbstract {

    private FragmentInstanceMock fragmentInstanceMock;
    private FragmentMock fragmentMock;
    private FormatInstanceMock childInstanceMock;
    private ResponseCachingDirectives cachingDirectives;

    // Javadoc inherited from superclass
    public void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        fragmentInstanceMock = new FragmentInstanceMock("fragmentInstanceMock",
            expectations, NDimensionalIndex.ZERO_DIMENSIONS);

        fragmentMock = LayoutTestHelper.createFragmentMock("fragmentMock",
                                                           expectations,
                                                           canvasLayoutMock);

        childInstanceMock = new FormatInstanceMock("childInstanceMock",
            expectations, NDimensionalIndex.ZERO_DIMENSIONS);

        final FormatMock childMock = LayoutTestHelper.createFormatMock(
            "childMock", expectations, canvasLayoutMock);

        LayoutTestHelper.addChildren(fragmentMock.expects,
                                     new FormatMock.Expects[]{
                                         childMock.expects
                                     });

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // Initialise the instance.
        fragmentInstanceMock.expects.isEmpty().returns(false).any();
        fragmentInstanceMock.expects.getFormat().returns(fragmentMock).any();
        fragmentInstanceMock.expects.getIndex().returns(
            NDimensionalIndex.ZERO_DIMENSIONS).any();

        // Initialise the format renderer context.

        final DeviceLayoutContextMock deviceLayoutContextMock =
            new DeviceLayoutContextMock("deviceLayoutContextMock", expectations);
        final MarinerPageContextMock marinerPageContextMock =
            new MarinerPageContextMock("marinerPageContextMock", expectations);
        final EnvironmentContextMock environmentContextMock =
            new EnvironmentContextMock("environmentContextMock", expectations);
        cachingDirectives =
            new ResponseCachingDirectives(SystemClock.getDefaultInstance());
        cachingDirectives.enable();
        assertTrue(cachingDirectives.isEnabled());

        environmentContextMock.expects.getCachingDirectives().returns(
            cachingDirectives).any();
        marinerPageContextMock.expects.getEnvironmentContext().returns(
            environmentContextMock).any();
        deviceLayoutContextMock.expects.getMarinerPageContext().returns(
            marinerPageContextMock).any();
        formatRendererContextMock.expects.getDeviceLayoutContext().returns(
            deviceLayoutContextMock).any();
        formatRendererContextMock.expects.isFragmentationSupported()
                .returns(true).any();

        // Create an association between the child instance and its format.
        FormatRendererTestHelper.connectFormatInstanceToFormat(
            formatRendererContextMock, childInstanceMock.expects, childMock,
            NDimensionalIndex.ZERO_DIMENSIONS);
    }

    /**
     * Tests that fragment contents are rendered if there is no current
     * fragment.
     */
    public void testNoCurrentFragment() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // No current fragment.
        formatRendererContextMock.expects.getCurrentFragment()
                .returns(null).any();

        // Make sure that the layout module is invoked properly.
        expectations.add(new OrderedExpectations() {
            public void add() {
                formatRendererContextMock.expects
                        .renderFormat(childInstanceMock);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        FormatRenderer renderer = new FragmentRenderer();
        renderer.render(formatRendererContextMock, fragmentInstanceMock);
        assertFalse(cachingDirectives.isEnabled());
    }

    /**
     * Test that the fragment contents are rendered when it is the current one.
     */
    public void testIsCurrentFragment() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // The fragment is the current one.
        formatRendererContextMock.expects.getCurrentFragment()
                .returns(fragmentMock).any();

        // Make sure that the layout module is invoked properly.
        expectations.add(new OrderedExpectations() {
            public void add() {
                formatRendererContextMock.expects
                        .renderFormat(childInstanceMock);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        FormatRenderer renderer = new FragmentRenderer();
        renderer.render(formatRendererContextMock, fragmentInstanceMock);
        assertFalse(cachingDirectives.isEnabled());
    }

    /**
     * Test that a fragment link is rendered when it is not the current one.
     */
    public void testIsNotCurrentFragment() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final FragmentMock otherFragmentMock =
                LayoutTestHelper.createFragmentMock(
                        "otherFragmentMock", expectations, canvasLayoutMock);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // The fragment is the current one.
        formatRendererContextMock.expects.getCurrentFragment()
                .returns(otherFragmentMock).any();

        // Make sure that the layout module is invoked properly.
        expectations.add(new OrderedExpectations() {
            public void add() {
                fragmentLinkWriterMock.expects.writeFragmentLink(
                        formatRendererContextMock,
                        otherFragmentMock, fragmentMock,
                        false, false);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        FormatRenderer renderer = new FragmentRenderer();
        renderer.render(formatRendererContextMock, fragmentInstanceMock);
        assertFalse(cachingDirectives.isEnabled());
    }
}

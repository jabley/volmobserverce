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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.layouts;

import com.volantis.mcs.layouts.CanvasLayoutMock;
import com.volantis.mcs.layouts.DeviceLayoutUpdaterMock;
import com.volantis.mcs.layouts.FormatMock;
import com.volantis.mcs.layouts.FragmentMock;
import com.volantis.mcs.layouts.PaneMock;
import com.volantis.mcs.policies.variants.layout.InternalLayoutContentMock;
import com.volantis.mcs.runtime.layouts.styling.LayoutStyleSheetBuilderMock;
import com.volantis.mcs.runtime.policies.theme.StyleSheetActivatorMock;
import com.volantis.mcs.themes.StyleSheetMock;
import com.volantis.mcs.unit.layouts.LayoutTestHelper;
import com.volantis.styling.compiler.StyleSheetCompilerFactoryMock;
import com.volantis.styling.compiler.StyleSheetCompilerMock;
import com.volantis.styling.sheet.CompiledStyleSheetMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.List;

/**
 * Test that the runtime activation of a device layout works correctly.
 */
public class DeviceLayoutActivatorTestCase
        extends TestCaseAbstract {

    protected CanvasLayoutMock canvasLayoutMock;
    private LayoutContentActivator activator;
    private LayoutStyleSheetBuilderMock styleSheetBuilderMock;
    protected StyleSheetCompilerFactoryMock styleSheetCompilerFactoryMock;

    protected DeviceLayoutUpdaterMock deviceLayoutUpdaterMock;

    protected void setUp() throws Exception {
        super.setUp();

        canvasLayoutMock = new CanvasLayoutMock("canvasLayoutMock", expectations);

        styleSheetBuilderMock =
                new LayoutStyleSheetBuilderMock("styleSheetBuilderMock",
                                                expectations);

        styleSheetCompilerFactoryMock = new StyleSheetCompilerFactoryMock(
                "styleSheetCompilerFactoryMock", expectations);

        deviceLayoutUpdaterMock =
                new DeviceLayoutUpdaterMock("deviceLayoutUpdaterMock",
                        expectations);

        activator = new LayoutContentActivator(
                styleSheetBuilderMock, styleSheetCompilerFactoryMock,
                deviceLayoutUpdaterMock);
    }

    /**
     * Test that activation  
     *
     * @throws Exception
     */
    public void notestActivate() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final PaneMock paneMock = LayoutTestHelper.createPaneMock(
                "paneMock", expectations, canvasLayoutMock);

        final StyleSheetMock styleSheetMock =
                new StyleSheetMock("styleSheetMock", expectations);

        final StyleSheetCompilerMock styleSheetCompilerMock =
                new StyleSheetCompilerMock("styleSheetCompilerMock",
                                           expectations);

        final CompiledStyleSheetMock compiledStyleSheetMock =
                new CompiledStyleSheetMock("compiledStyleSheetMock",
                                           expectations);

        final FragmentMock fragmentMock = LayoutTestHelper.createFragmentMock(
                "fragmentMock", expectations, canvasLayoutMock);

        final InternalLayoutContentMock layoutContentMock =
                new InternalLayoutContentMock("layoutContentMock",
                        expectations);

        final StyleSheetActivatorMock styleSheetActivatorMock =
                new StyleSheetActivatorMock("styleSheetActivatorMock",
                        expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        canvasLayoutMock.expects.getDefaultFragmentName()
                .returns("DEFAULT FRAGMENT").any();

        paneMock.expects.getEnclosingFragment().returns(fragmentMock).any();

        canvasLayoutMock.expects.getRootFormat().returns(paneMock).any();

        styleSheetBuilderMock.expects.build(canvasLayoutMock)
                .returns(styleSheetMock);

        styleSheetCompilerFactoryMock.expects.createStyleSheetCompiler()
                .returns(styleSheetCompilerMock);

        styleSheetCompilerMock.expects.compileStyleSheet(styleSheetMock).
                returns(compiledStyleSheetMock);

        deviceLayoutUpdaterMock.expects.update(canvasLayoutMock);

        layoutContentMock.expects.getLayout().returns(canvasLayoutMock).any();

        styleSheetActivatorMock.expects.activate(styleSheetMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ActivatedLayoutContent activatedLayoutContent = (ActivatedLayoutContent)
                activator.activateLayoutContent(styleSheetActivatorMock,
                        layoutContentMock);

        assertEquals("Compiled Style Sheet", compiledStyleSheetMock,
                     activatedLayoutContent.getCompiledStyleSheet());

        RuntimeDeviceLayout runtimeDeviceLayout = new RuntimeLayoutAdapter(
                null, canvasLayoutMock, compiledStyleSheetMock,
                activatedLayoutContent.getContainerNameToFragments());

        final List fragments =
            runtimeDeviceLayout.getEnclosingFragments("paneMock");
        assertEquals(1, fragments.size());
        final LayoutContentActivator.ContainerPosition containerPosition =
            (LayoutContentActivator.ContainerPosition) fragments.get(0);

        assertEquals(paneMock.getEnclosingFragment(),
            containerPosition.getFragment());
        assertEquals(0, containerPosition.getIndex());
    }

    /**
     * Tests that the <code>getFirstFragment</code> method returns null
     * when a null format is passed in
     */
    public void testGetFirstFragmentLinkNullFormat() throws Exception {

        String frag = activator.getFirstFragment(null);
        assertNull("getFirstFragment() should return null for null format",
                   frag);
    }

    /**
     * Tests that the <code>getFirstFragment</code> method returns the
     * name of the format passed in if the Format is a Fragment
     */
    public void testGetFirstFragmentLinkWithFragmentArg() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final String name = "fragmentMock";
        final FragmentMock fragmentMock = LayoutTestHelper.createFragmentMock(
                "fragmentMock", expectations, canvasLayoutMock);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        fragmentMock.expects.getName().returns(name).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        String actualName = activator.getFirstFragment(fragmentMock);
        assertEquals("getFirstFragment() should return the name of the " +
                     " fragment if a fragment is passed in",
                     name, actualName);
    }

    /**
     * Tests that the <code>getFirstFragment</code> method returns the
     * name of the first fragment child if a format is passed in that is not
     * a fragment
     */
    public void testGetFirstFragmentLinkWithFormatArg() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final String name = "fragmentMock";
        final FormatMock formatMock = LayoutTestHelper.createFormatMock(
                "formatMock", expectations, canvasLayoutMock);
        final FragmentMock fragmentMock = LayoutTestHelper.createFragmentMock(
                "fragmentMock", expectations, canvasLayoutMock);

        LayoutTestHelper.addChildren(formatMock.expects,
                                     new FormatMock.Expects[]{
                                         fragmentMock.expects
                                     });

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        fragmentMock.expects.getName().returns(name).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        String actualName = activator.getFirstFragment(formatMock);
        assertEquals("getFirstFragment() should return the name of the " +
                     " fragment if a fragment is passed in",
                     name, actualName);
    }

    /**
     * Tests that the <code>getFirstFragment</code> method returns null
     * if a format is passed in that is not a fragment and does not contain
     * any Fragment children/grandchildren etc.
     */
    public void testGetFirstFragmentLinkWithNoFragChildren() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final FormatMock formatMock = LayoutTestHelper.createFormatMock(
                "formatMock", expectations, canvasLayoutMock);
        final FormatMock childMock = LayoutTestHelper.createFormatMock(
                "childMock", expectations, canvasLayoutMock);

        LayoutTestHelper.addChildren(formatMock.expects,
                                     new FormatMock.Expects[]{
                                         childMock.expects
                                     });

        // The child mock has no children of its own.
        childMock.expects.getNumChildren().returns(0).any();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        String actualName = activator.getFirstFragment(formatMock);
        assertNull("getFirstFragment() should return null if the format " +
                   "passed in does not contain any fragments", actualName);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10641/1	geoff	VBM:2005113024 Pagination page rendering issues

 06-Dec-05	10621/1	geoff	VBM:2005113024 Pagination page rendering issues

 27-Oct-05	9986/1	geoff	VBM:2005102512 MCS35: Investigate and fix any JDBC repository import/export problems

 13-Oct-05	9727/5	ianw	VBM:2005100506 Fixed remote repository issues

 10-Oct-05	9727/3	ianw	VBM:2005100506 Fixed up remote repositories layout issues

 10-Oct-05	9727/1	ianw	VBM:2005100506 Fixed up remote repositories layout issues

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 ===========================================================================
*/

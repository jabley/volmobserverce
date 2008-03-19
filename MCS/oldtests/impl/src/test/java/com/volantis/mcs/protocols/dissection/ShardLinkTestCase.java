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
package com.volantis.mcs.protocols.dissection;

import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.DissectingPane;
import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.protocols.DissectingPaneAttributes;
import com.volantis.mcs.protocols.OutputBufferFactory;
import com.volantis.mcs.protocols.TestDOMOutputBufferFactory;
import com.volantis.mcs.protocols.assets.implementation.AssetResolverMock;
import com.volantis.mcs.protocols.css.DefaultStylePropertyResolver;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultMenuItemRendererSelector;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultMenuSeparatorRendererSelector;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemRendererFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemRendererSelector;
import com.volantis.mcs.protocols.menu.shared.renderer.TestMenuBufferLocator;
import com.volantis.mcs.protocols.menu.shared.renderer.TestMenuItemRendererFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.TestMenuRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.TestSimpleMenuSeparatorRendererFactory;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.styling.StylesBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public class ShardLinkTestCase extends TestCaseAbstract {

    /**
     * Test that we can create build and render a shard link menu.
     * <p>
     * This tests pretty much everything apart from the style properties.
     *  
     * @throws Exception
     */ 
    public void testTheBasics() throws Exception {
        // Initialise the shared test data we use in input and check in output.
        String inclusionPath = "TestInclusionPath";
        String paneName = "TestPaneName";
        String nextShortcut = "TestNextShardShortcut";
        String nextLink = "TestNextShardLink";
        String nextClass = "TestNextShardLinkClass";
        String prevShortcut = "TestPreviousShardShortcut";
        String prevLink = "TestPreviousShardLink";
        String prevClass = "TestPreviousShardLinkClass";

        // Create the DissectingPaneAttributes from the test data.
        CanvasLayout canvasLayout = new CanvasLayout();
        DissectingPane pane = new DissectingPane(canvasLayout);
        pane.setName(paneName);
        pane.setAttribute(FormatConstants.NEXT_SHARD_SHORTCUT_ATTRIBUTE,
                          nextShortcut);
        pane.setAttribute(FormatConstants.NEXT_SHARD_LINK_CLASS_ATTRIBUTE,
                          nextClass);
        pane.setAttribute(FormatConstants.PREVIOUS_SHARD_SHORTCUT_ATTRIBUTE,
                          prevShortcut);
        pane.setAttribute(FormatConstants.PREVIOUS_SHARD_LINK_CLASS_ATTRIBUTE,
                          prevClass);
        DissectingPaneAttributes attributes = new DissectingPaneAttributes();
        attributes.setLinkText(nextLink);
        attributes.setBackLinkText(prevLink);
        attributes.setDissectingPane(pane);
        attributes.setInclusionPath(inclusionPath);
        attributes.setIsNextLinkFirst(true);
        attributes.setStyles(StylesBuilder.getCompleteStyles(
                "mcs-menu-orientation: horizontal"));

        // Build the Menu from the DissectingPaneAttributes.
        OutputBufferFactory outputBufferFactory =
                new TestDOMOutputBufferFactory();
        ShardLinkMenuModelBuilder builder = new ShardLinkMenuModelBuilder(
                outputBufferFactory);
        ShardLinkMenu menu = builder.buildShardLinkMenuModel(attributes);
        
        MenuItemRendererFactory menuItemRendererFactory =
                new ShardLinkMenuItemRendererFactory(
                        new TestMenuItemRendererFactory(outputBufferFactory));

        DefaultMenuSeparatorRendererSelector menuSeparatorRendererSelector =
                new DefaultMenuSeparatorRendererSelector(
                        new ShardLinkMenuSeparatorRendererFactory(
                                new TestSimpleMenuSeparatorRendererFactory()),
                        new AssetResolverMock("assetResolverMock", expectations),
                        new DefaultStylePropertyResolver(null, null));

        // Create the Renderers which will render the shard link Menu
        SeparatorRenderer orientationSeparator
                = menuSeparatorRendererSelector.selectMenuSeparator(menu);
        TestMenuBufferLocator locator
                = new TestMenuBufferLocator(orientationSeparator);

        MenuItemRendererSelector menuItemRendererSelector =
                new DefaultMenuItemRendererSelector(menuItemRendererFactory, 
                        menuSeparatorRendererSelector);
        // @todo delete ShardLinkMenuRenderer and TestMenuRenderer 
        // and replace with ShardLinkMenuBracketingRenderer and 
        // DefaultMenuRenderer.
        ShardLinkMenuRenderer menuRenderer = 
                new ShardLinkMenuRenderer(locator, 
                        new TestMenuRenderer(menuItemRendererSelector,
                                menuSeparatorRendererSelector, locator));

        // Render the menu.
        menuRenderer.render(menu);
        String actual = locator.getOutput();
        //System.out.println(actual);

        // Create the expected results from the test data.
        String expected =
            "<SHARD-LINK-GROUP " +
                    "annotation=\"[ShardLinkGroupAttributes:dissectableArea=" +
                        "[DissectableAreaIdentity:inclusionPath=" + inclusionPath + ",name=" + paneName + "]" +
                        "]\"" +
                    ">" +
                "<menu>" +
                    "<SHARD-LINK " +
                            "annotation=\"[ShardLinkAttributes:action=NEXT]\"" +
                            ">" +
                        "<link " +
//                                "class=\"" + nextClass + "\" " +
                                "href=\"" + DissectionConstants.URL_MAGIC_CHAR + "\" " +
                                "shortcut=\"" + nextShortcut + "\"" +
                                ">" +
                            "<plain-text>" + nextLink + "</plain-text>" +
                        "</link>" +
                    "</SHARD-LINK>" +
                    "<SHARD-LINK-CONDITIONAL " +
                            "annotation=\"[ShardLinkConditionalAttributes:contentRule=[SeparatorRule]]\"" +
                            ">" +
                        "[separator-menu-horizontal:non-breaking-space]" +
                    "</SHARD-LINK-CONDITIONAL>" +
                    "<SHARD-LINK " +
                            "annotation=\"[ShardLinkAttributes:action=PREVIOUS]\"" +
                            ">" +
                        "<link " +
//                                "class=\"" + prevClass + "\" " +
                                "href=\"[url]\" " +
                                "shortcut=\"" + prevShortcut + "\"" +
                                ">" +
                            "<plain-text>" + prevLink + "</plain-text>" +
                        "</link>" +
                    "</SHARD-LINK>" +
                "</menu>" +
            "</SHARD-LINK-GROUP>";
        //System.out.println(expected);

        // Finally compare the expected with actual results.
        assertEquals("shard link markup not as expected", expected, actual);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 17-Nov-05	10356/1	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 15-Nov-05	10333/1	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 15-Nov-05	10333/1	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 30-Aug-05	9353/2	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 09-Aug-05	9151/4	pduffin	VBM:2005080205 Recommitted after super merge

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 11-May-05	8123/2	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 09-Mar-05	7022/2	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 28-Feb-05	7149/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 28-Feb-05	7127/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 20-Sep-04	5575/1	tom	VBM:2004091402 Previous and Next work with correct names and are the right way around

 17-Sep-04	5527/1	tom	VBM:2004091402 Previous and Next work with correct names and are the correct way around

 14-May-04	4318/3	pduffin	VBM:2004051207 Integrated separators into menu rendering

 13-May-04	4315/1	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 10-May-04	4164/1	pduffin	VBM:2004050404 Fixed some problems with shard link test cases and some mock test cases

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 28-Apr-04	4048/4	geoff	VBM:2004042606 Enhance Menu Support: WML Dissection

 28-Apr-04	4048/2	geoff	VBM:2004042606 Enhance Menu Support: WML Dissection

 ===========================================================================
*/

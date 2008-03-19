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
package com.volantis.mcs.protocols.wml.menu;

import com.volantis.mcs.dom.StyledDOMTester;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.implementation.LiteralLinkAssetReference;
import com.volantis.mcs.protocols.assets.implementation.TestNormalImageAssetReference;
import com.volantis.mcs.protocols.assets.implementation.AssetResolver;
import com.volantis.mcs.protocols.assets.implementation.AssetResolverMock;
import com.volantis.mcs.protocols.css.DefaultStylePropertyResolver;
import com.volantis.mcs.protocols.css.StylePropertyResolver;
import com.volantis.mcs.protocols.menu.builder.BuilderException;
import com.volantis.mcs.protocols.menu.builder.MenuModelBuilder;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.shared.builder.ConcreteMenuModelBuilder;
import com.volantis.mcs.protocols.menu.shared.renderer.ConcreteMenuBufferFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultMenuBufferLocator;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultMenuItemRendererSelector;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultMenuSeparatorRendererSelector;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuBufferFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.TestDeprecatedImageOutput;
import com.volantis.mcs.protocols.menu.shared.renderer.TestDeprecatedOutputLocator;
import com.volantis.mcs.protocols.renderer.TestRendererContext;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.MCSMenuImageStyleKeywords;
import com.volantis.mcs.themes.properties.MCSMenuItemOrderKeywords;
import com.volantis.mcs.themes.properties.MCSMenuTextStyleKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import com.volantis.styling.properties.MutableStylePropertySet;
import com.volantis.styling.properties.MutableStylePropertySetImpl;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * A test case for {@link OpenwaveMenuRenderer}.
 */ 
public class OpenwaveMenuRendererTestCase extends TestCaseAbstract {

    private StyledDOMTester styledDOMTester;

    public OpenwaveMenuRendererTestCase(String name) {
        super(name);
        // This is required for Pipeline namespace support in IDEA. Sigh!
        // todo: fix this.
        new Volantis();

        MutableStylePropertySet interestingProperties =
                new MutableStylePropertySetImpl();
        interestingProperties.add(StylePropertyDetails.WHITE_SPACE);
        styledDOMTester = new StyledDOMTester(
                null, interestingProperties, true, false);
    }

    /**
     * Test that a simple menu renders correctly when no styles are provided.
     * 
     * @throws Exception
     */ 
    public void testNoStyles() throws Exception {
        checkMenuStyles(null, null, null);
    }

    /**
     * Test that a simple menu renders correctly with only item images enabled.
     * 
     * @throws Exception
     */ 
    public void testImageOnlyStyle() throws Exception {
        checkMenuStyles(
                MCSMenuImageStyleKeywords.PLAIN,
                MCSMenuTextStyleKeywords.NONE,
                null);
    }

    /**
     * Test that a simple menu renders correctly with only item text enabled.
     * 
     * @throws Exception
     */ 
    public void testTextOnlyStyle() throws Exception {
        checkMenuStyles(
                MCSMenuImageStyleKeywords.NONE,
                MCSMenuTextStyleKeywords.PLAIN,
                null);
    }
    
    /**
     * Test that a simple menu renders correctly with both item text and 
     * images enabled and ordering set to image first.
     * 
     * @throws Exception
     */ 
    public void testBothImageFirstStyle() throws Exception {
        checkMenuStyles(
                MCSMenuImageStyleKeywords.PLAIN,
                MCSMenuTextStyleKeywords.PLAIN,
                MCSMenuItemOrderKeywords.IMAGE_FIRST);
    }

    /**
     * Test that a simple menu renders correctly with both item text and 
     * images enabled and ordering set to text first.
     * 
     * @throws Exception
     */ 
    public void testBothTextFirstStyle() throws Exception {
        checkMenuStyles(
                MCSMenuImageStyleKeywords.PLAIN,
                MCSMenuTextStyleKeywords.PLAIN,
                MCSMenuItemOrderKeywords.TEXT_FIRST);
    }

    /**
     * Test that a simple menu renders correctly with both item text and 
     * images disabled.
     * 
     * @throws Exception
     */ 
    public void testBothNoneStyle() throws Exception {
        StyleKeyword imageStyle = MCSMenuImageStyleKeywords.NONE;
        StyleKeyword textStyle = MCSMenuTextStyleKeywords.NONE;
        StyleKeyword orderStyle = null;
        checkMenuStyles(imageStyle, textStyle, orderStyle);
    }

    /**
     * Helper method to test a simple menu with differing style values.
     *  
     * @param imageStyle the style keyword value for MCS_MENU_IMAGE_STYLE, or 
     *      null to indicate no value.
     * @param textStyle the style keyword value for MCS_MENU_TEXT_STYLE, or
     *      null to indicate no value.
     * @param orderStyle the style keyword value for MCS_MENU_ITEM_ORDER, or
     *      null to indicate no value.
     * @throws Exception if there was a problem testing the menu rendering.
     */ 
    private void checkMenuStyles(StyleKeyword imageStyle,
                                 StyleKeyword textStyle,
                                 StyleKeyword orderStyle)
            throws Exception {
        // Declare the fixed data items which will be used in the menu.
        String menuTitle = "menu title";
        // Test the menu.
        checkSimpleMenu(imageStyle, textStyle, orderStyle, 
                menuTitle);
    }

    /**
     * Test a simple menu with minimal menu content.
     * 
     * @throws Exception
     */ 
    public void testMinimalContent() throws Exception {
        checkMenuContent(null);
    }

    /**
     * Test a simple menu with maximal menu content.
     * 
     * @throws Exception
     */ 
    public void testMaximalContent() throws Exception {
        checkMenuContent("menu title");
    }
    
    /**
     * A helper method to test a simple menu with differing menu content.
     *  
     * @param menuTitle the title of the menu to use, or null for none.
     * @throws Exception if there was a problem testing the menu rendering.
     */ 
    private void checkMenuContent(String menuTitle) 
            throws Exception {
        // Declare the fixed data items which will be used in the menu.
        StyleKeyword imageStyle = MCSMenuImageStyleKeywords.PLAIN;
        StyleKeyword textStyle = MCSMenuTextStyleKeywords.PLAIN;
        StyleKeyword orderStyle = MCSMenuItemOrderKeywords.IMAGE_FIRST;
        // Test the menu.
        checkSimpleMenu(imageStyle, textStyle, orderStyle, menuTitle);
    }
    
    /**
     * A helper method to test a simple menu; this allows setting of all the
     * values which affect the menu's rendering.
     * 
     * @param imageStyle the style keyword value for MCS_MENU_IMAGE_STYLE, or 
     *      null to indicate no value.
     * @param textStyle the style keyword value for MCS_MENU_TEXT_STYLE, or
     *      null to indicate no value.
     * @param orderStyle the style keyword value for MCS_MENU_ITEM_ORDER, or
     *      null to indicate no value.
     * @param menuTitle the title of the menu to use, or null for none.
     * @throws Exception if there was a problem testing the menu rendering.
     */ 
    private void checkSimpleMenu(
            StyleKeyword imageStyle, StyleKeyword textStyle,
            StyleKeyword orderStyle,
            String menuTitle) throws Exception {

        // Declare the data items which will be used in the menu items.
        // These are fixed as they are tested in the menu item rendering tests.
        String itemTitle = "item title";
        String itemHref = "item href";
        String itemText = "item text";
        String itemImageComponent = "item image component";

        //
        // Calculate the actual value of a menu rendering.
        //

        final AssetResolverMock assetResolverMock =
                new AssetResolverMock("assetResolverMock", expectations);

        // Create the related test/mock objects.
        TestRendererContext context =
                new TestRendererContext(assetResolverMock);
        
        TestDeprecatedOutputLocator outputLocator
                = new TestDeprecatedOutputLocator();
        DOMOutputBuffer buffer = context.getBuffer();
        TestDeprecatedImageOutput imageOutput
                = (TestDeprecatedImageOutput) outputLocator.getImageOutput();

        // Create the renderer to test, using the test objects created above.
        OpenwaveMenuItemRendererFactory rendererFactory =
                new OpenwaveMenuItemRendererFactory(context, outputLocator);
        final StylePropertyResolver stylePropertyResolver =
                new DefaultStylePropertyResolver(null, null);
        final DefaultMenuSeparatorRendererSelector rendererSelector =
                new DefaultMenuSeparatorRendererSelector(
                        new OpenwaveMenuSeparatorRendererFactory(),
                        context.getAssetResolver(),
                        stylePropertyResolver);
        DefaultMenuItemRendererSelector selector =
                new DefaultMenuItemRendererSelector(
                        rendererFactory, rendererSelector);

        MenuBufferFactory bufferFactory
                = new ConcreteMenuBufferFactory(SeparatorRenderer.NULL);

        DefaultMenuBufferLocator bufferLocator
                = new DefaultMenuBufferLocator(context.getOutputBufferResolver(),
                        bufferFactory);

        OpenwaveMenuRenderer menuRenderer =
                new OpenwaveMenuRenderer(selector, bufferLocator);

        // Build a simple menu model we can exercise the renderer with.
        Menu menu = createSimpleMenu(
                imageStyle, textStyle, orderStyle,
                menuTitle, itemTitle, itemHref, itemText, itemImageComponent);

        // Render the menu model out to the buffer.
        menuRenderer.render(menu);

        // Extract the output from the menu rendering as a string.
        String actual = styledDOMTester.render(buffer.getRoot());
        //System.out.println(actual);

        // Calculate the image part of the menu items rendered from the
        // related mariner menu style property setting, if any.
        String imageValue = null;
        if (imageStyle == null || imageStyle == MCSMenuImageStyleKeywords.NONE) {
            // No image.
        } else if (imageStyle == MCSMenuImageStyleKeywords.PLAIN
                || imageStyle == MCSMenuImageStyleKeywords.ROLLOVER) {
            imageValue = imageOutput.getLastRenderedText();
        } else {
            throw new IllegalStateException();
        }

        // Calculate the text part of the menu items rendered from the
        // related mariner menu style property setting, if any.
        String textValue = null;
        if (textStyle == MCSMenuTextStyleKeywords.NONE) {
            // no text.
        } else if (textStyle == null ||
                textStyle == MCSMenuTextStyleKeywords.PLAIN) {
            textValue = itemText;
        } else {
            throw new IllegalStateException();
        }

        //
        // Calculate the expected value for the menu rendering.
        //

        // Assemble the expected value (rendered menu).
        String expected = "";

        // If we managed to create a value for the image or text part of the
        // menu item...
        if (imageValue != null || textValue != null) {
            // ... then we can render the menu.

            expected +=
                "<BLOCK style='white-space: nowrap'>" +
                  "<select" + attr("title", menuTitle) + ">" +
                    "<option " +
                            "onpick=\"" + itemHref + "\" " +
                            "title=\"" + itemTitle +
                        "\">";

            // use default horizontal separator for now.
            String separator = VolantisProtocol.NBSP;

            // Assemble the contents of the option in the correct order
            // depending on the related mariner menu style property.
            if (orderStyle == null ||
                    orderStyle == MCSMenuItemOrderKeywords.IMAGE_FIRST) {
                // image first.
                if (imageValue != null) {
                    expected += imageValue;
                }
                if (imageValue != null && textValue != null) {
                    expected += separator;
                }
                if (textValue != null) {
                    expected += textValue;
                }
            } else if (orderStyle == MCSMenuItemOrderKeywords.TEXT_FIRST) {
                // text first
                if (textValue != null) {
                    expected += textValue;
                }
                if (imageValue != null && textValue != null) {
                    expected += separator;
                }
                if (imageValue != null) {
                    expected += imageValue;
                }
            } else {
                throw new IllegalStateException();
            }

            expected +=
                    "</option>" +
                  "</select>" +
                "</BLOCK>";
        }
        // ... else, we can't render a sensible menu with menu items without
        // any content, so don't bother.
        expected = styledDOMTester.normalize(expected);

        //
        // Finally, compare the expected value we calculated with the actual
        // value which was rendered.
        //

        assertEquals("Simple menu not as expected", expected, actual);
    }

    /**
     * Create a simple menu which can be used to exercise a menu renderer.
     * 
     * @param imageStyle the style keyword value for MCS_MENU_IMAGE_STYLE, or 
     *      null to indicate no value.
     * @param textStyle the style keyword value for MCS_MENU_TEXT_STYLE, or
     *      null to indicate no value.
     * @param orderStyle the style keyword value for MCS_MENU_ITEM_ORDER, or
     *      null to indicate no value.
     * @param menuTitle the title of the menu.
     * @param itemTitle the title of each menu item.
     * @param href the href of each menu item.
     * @param text the text of each menu item.
     * @param imageComponent the image component name of each menu item's icon.
     * @return a completed menu.
     * @throws BuilderException if there was a problem building the menu.
     */ 
    private Menu createSimpleMenu(
            StyleKeyword imageStyle, StyleKeyword textStyle, StyleKeyword orderStyle,
            String menuTitle, String itemTitle, String href, String text, 
            String imageComponent) throws BuilderException {
        // Build a very simple menu with a single item which contains a label
        // with some text and an icon.
        MenuModelBuilder builder = new ConcreteMenuModelBuilder();
        builder.startMenu();

        Styles menuStyles = StylesBuilder.getCompleteStyles(
                (imageStyle == null ? "" :
                "mcs-menu-image-style: " + imageStyle + "; ") +
                (textStyle == null ? "" :
                "mcs-menu-text-style: " + textStyle + "; ") +
                (orderStyle == null ? "" :
                "mcs-menu-item-order: " + orderStyle + "; "));

        builder.setElementDetails("menu", null, menuStyles);
        builder.setTitle(menuTitle);

        builder.startMenuItem();
        builder.setTitle(itemTitle);
        builder.setHref(new LiteralLinkAssetReference(href));

        builder.startLabel();

        builder.startText();
        DOMOutputBuffer dom = new TestDOMOutputBuffer();
        dom.writeText(text);
        builder.setText(dom);
        builder.endText();

        builder.startIcon();
        // this is required or we crash trying to extract the element name in
        // the renderer
        builder.setElementDetails("menu-icon", null,
                StylesBuilder.getInitialValueStyles());
        builder.setNormalImageURL(new TestNormalImageAssetReference(
                imageComponent));
        builder.endIcon();

        builder.endLabel();

        builder.endMenuItem();

        builder.endMenu();

        return builder.getCompletedMenuModel();
    }

    /**
     * Create an attribute name=value pair, if the value is not null.
     *
     * @param name name of the attribute
     * @param value value of the attribute, may be null.
     * @return the name=value pair, or empty string if the value is null.
     * @todo this was cut and pasted from 
     * {@link com.volantis.testtools.config.ConfigFileBuilder}. Maybe we should
     * factor this out somewhere?
     */
    private String attr(String name, Object value) {
        if (value != null) {
            return " " + name + "=\"" + String.valueOf(value) + "\"";
        } else {
            return "";
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

 29-Sep-05	9600/4	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 23-Jun-05	8483/5	emma	VBM:2005052410 Modifications after review

 22-Jun-05	8483/3	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 09-Mar-05	7022/2	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 14-May-04	4318/2	pduffin	VBM:2004051207 Integrated separators into menu rendering

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 11-May-04	4246/1	philws	VBM:2004050709 Fix StyleInfo handling for MenuIcon, MenuText and MenuLabel

 06-May-04	4153/1	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 29-Apr-04	4013/3	pduffin	VBM:2004042210 Restructure menu item renderers

 29-Apr-04	4091/1	claire	VBM:2004042804 Enhanced menus: protocol integration and Openwave end to end

 26-Apr-04	3920/5	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 22-Apr-04	4004/3	claire	VBM:2004042204 Implemented remaining required WML renderers

 21-Apr-04	3681/3	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 15-Apr-04	3645/1	geoff	VBM:2004032904 Enhance Menu Support: Open Wave Menu Renderer

 ===========================================================================
*/

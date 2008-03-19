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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.protocols.DOMOutputBufferMock;
import com.volantis.mcs.protocols.ShortcutPropertiesMock;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.InserterMock;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.assets.TextAssetReferenceMock;
import com.volantis.mcs.protocols.menu.model.ElementDetailsMock;
import com.volantis.mcs.protocols.menu.model.MenuMock;
import com.volantis.mcs.protocols.menu.model.MenuItemMock;
import com.volantis.mcs.protocols.menu.MenuModuleCustomisationMock;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValueMock;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.mcs.dom.ElementMock;
import com.volantis.styling.StylesMock;
import com.volantis.styling.values.MutablePropertyValuesMock;
import com.volantis.testtools.mock.test.MockTestCaseAbstract;
import com.volantis.testtools.mock.value.ExpectedValue;

public class DefaultShortcutLabelRendererTestCase extends MockTestCaseAbstract {

    private static final String SHORTCUT_TEXT = "shortcutText";
    private static final String ELEMENT_ID = "elementID";

    DeprecatedSpanOutputMock spanOutput;
    MenuModuleCustomisationMock customisation;
    InserterMock inserter;
    DefaultShortcutLabelRenderer renderer;
    DOMOutputBufferMock buffer;
    MenuItemMock item;
    MenuMock menu;
    TextAssetReferenceMock shortcutValue;
    ShortcutPropertiesMock properties;
    ElementDetailsMock elementDetails;
    ElementMock currentElement;
    StyleValueMock styleValue;

    protected void setUp() throws Exception {
        super.setUp();

        // Create test objects.
        spanOutput = new DeprecatedSpanOutputMock("spanOutput", expectations);
        customisation = new MenuModuleCustomisationMock("customisation",
                expectations);
        inserter = new InserterMock("inserter", expectations);
        renderer = new DefaultShortcutLabelRenderer(spanOutput, customisation,
                inserter);
        buffer = new DOMOutputBufferMock("buffer", expectations);
        item = new MenuItemMock("item", expectations);
        menu = new MenuMock("menu", expectations);
        shortcutValue = new TextAssetReferenceMock("shortcutValue", expectations);
        properties = new ShortcutPropertiesMock("properties", expectations);
        elementDetails = new ElementDetailsMock("elementDetails", expectations);
        currentElement = new ElementMock("currentElement", expectations);
        styleValue = new StyleValueMock("styleValue", expectations);
    }

    public void testRenderWhenSupportsSpan() throws RendererException {
        // Create test objects.
        StylesMock styles = new StylesMock("styles", expectations);
        StylesMock stylesCopy = new StylesMock("stylesCopy", expectations);
        MutablePropertyValuesMock propertyValues =
                new MutablePropertyValuesMock("propertyValues", expectations);
        final ExpectedValue attributes =
                mockFactory.expectsInstanceOf(SpanAttributes.class);

        // Set expectations.
        item.expects.getMenu().returns(menu);
        item.expects.getShortcut().returns(shortcutValue);
        customisation.expects.supportsAccessKeyAttribute().returns(true);
        shortcutValue.expects.getText(TextEncoding.PLAIN).returns(SHORTCUT_TEXT);
        menu.expects.getShortcutProperties().returns(properties);
        menu.expects.getElementDetails().returns(elementDetails);
        properties.expects.supportsSpan().returns(true);
        elementDetails.expects.getId().returns(ELEMENT_ID);
        elementDetails.expects.getStyles().returns(styles);
        styles.expects.copy().returns(stylesCopy);
        stylesCopy.expects.getPropertyValues().returns(propertyValues);
        propertyValues.expects.setSpecifiedValue(StylePropertyDetails.DISPLAY,
                StyleKeywords.INLINE);
        customisation.expects.supportsStyleSheets().returns(true);
        spanOutput.fuzzy.openSpan(buffer, attributes);
        buffer.expects.setElementIsPreFormatted(true);
        buffer.expects.writeText(SHORTCUT_TEXT);
        buffer.expects.getCurrentElement().returns(currentElement);
        properties.expects.getSeparatorStyleValue().returns(styleValue);
        inserter.expects.insertPreservingExistingContent(currentElement, styleValue);
        properties.expects.supportsSpan().returns(true);
        spanOutput.fuzzy.closeSpan(buffer, attributes);

        // Run test.
        renderer.render(buffer, item);
    }

    public void testRenderWhenDoesNotSupportsSpan() throws RendererException {
        // Set expectations.
        item.expects.getMenu().returns(menu);
        item.expects.getShortcut().returns(shortcutValue);
        customisation.expects.supportsAccessKeyAttribute().returns(true);
        shortcutValue.expects.getText(TextEncoding.PLAIN).returns(SHORTCUT_TEXT);
        menu.expects.getShortcutProperties().returns(properties);
        menu.expects.getElementDetails().returns(elementDetails);
        properties.expects.supportsSpan().returns(false);
        buffer.expects.setElementIsPreFormatted(true);
        buffer.expects.writeText(SHORTCUT_TEXT);
        buffer.expects.getCurrentElement().returns(currentElement);
        properties.expects.getSeparatorStyleValue().returns(styleValue);
        inserter.expects.insertPreservingExistingContent(currentElement, styleValue);
        properties.expects.supportsSpan().returns(false);

        // Run test.
        renderer.render(buffer, item);
    }
}

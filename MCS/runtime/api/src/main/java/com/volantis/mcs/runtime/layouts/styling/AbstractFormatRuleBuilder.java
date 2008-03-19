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

package com.volantis.mcs.runtime.layouts.styling;

import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.layouts.StyleableFormat;
import com.volantis.mcs.themes.AttributeSelector;
import com.volantis.mcs.themes.AttributeSelectorActionEnum;
import com.volantis.mcs.themes.PropertyGroups;
import com.volantis.mcs.themes.Rule;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.StyleComponentURI;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.properties.BorderStyleKeywords;
import com.volantis.styling.properties.StyleProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all classes that build rules for format.
 */
public abstract class AbstractFormatRuleBuilder
    implements FormatRuleBuilder {

    /**
     * The object that can convert style properties represented as strings in
     * the syntax used by formats into style values.
     */
    protected final FormatAttributeToStyleValueConverter converter;
    protected final StyleSheetFactory factory;

    /**
     * Initialise.
     *
     * @param converter The converter to use.
     * @param factory
     */
    public AbstractFormatRuleBuilder(
            FormatAttributeToStyleValueConverter converter,
            StyleSheetFactory factory) {

        this.converter = converter;
        this.factory = factory;
    }

    /**
     * Create the rule for the specified container ignoring the .
     *
     * @param format
     * @return
     */
    protected Rule createRuleWithoutProperties(StyleableFormat format) {

        Rule rule = ThemeFactory.getDefaultInstance().createRule();

        SelectorSequence sequence = createSelectorSequence(format);
        List selectors = new ArrayList();
        selectors.add(sequence);

        rule.setSelectors(selectors);

        return rule;
    }

    protected SelectorSequence createSelectorSequence(
            StyleableFormat format) {

        SelectorSequence sequence = factory.createSelectorSequence();

        initialiseSelectorSequence(format, sequence);

        return sequence;
    }

    protected SelectorSequence createContextualSelectorSequence(
            StyleableFormat format) {

        SelectorSequence sequence = factory.createSelectorSequence();

        initialiseSelectorSequence(format, sequence);

        return sequence;
    }

    private void initialiseSelectorSequence(
            StyleableFormat format, SelectorSequence sequence) {

        int instance = System.identityHashCode(format);
        AttributeSelector attributeSelector = factory.createAttributeSelector();
        attributeSelector.setName(FormatStylingConstants.FORMAT_IDENTIFIER);
        attributeSelector.setConstraint(AttributeSelectorActionEnum.EQUALS,
                String.valueOf(instance));

//        sequence.setElementSelector(typeSelector);
        sequence.addSelector(attributeSelector);
    }

    protected void setHorizontalAlign(
            MutableStyleProperties styleProperties, Format format) {

        String align = format.getHorizontalAlignment();
        StyleValue value = converter.getHorizontalAlign(align);
        if (value != null) {
            styleProperties.setStyleValue(StylePropertyDetails.TEXT_ALIGN,
                    value);
        }
    }

    protected void setVerticalAlign(
            MutableStyleProperties styleProperties, Format format) {

        String align = format.getVerticalAlignment();
        StyleValue value = converter.getVerticalAlign(align);
        if (value != null) {
            styleProperties.setStyleValue(StylePropertyDetails.VERTICAL_ALIGN,
                    value);
        }
    }

    protected void setBackgroundComponent(
            MutableStyleProperties styleProperties, Format format) {

        String backgroundComponentType = format.getBackgroundComponentType();
        String backgroundComponentName = format.getBackgroundComponent();
        if (backgroundComponentName == null) {
            return;
        }

        StyleProperty property;
        if (backgroundComponentType.equals(
                FormatConstants.BACKGROUND_COMPONENT_TYPE_DYNAMIC_VISUAL)) {
            property = StylePropertyDetails.MCS_BACKGROUND_DYNAMIC_VISUAL;
        } else {
            property = StylePropertyDetails.BACKGROUND_IMAGE;
        }

        StyleComponentURI value =
                converter.getComponentURI(backgroundComponentName);
        if (value != null) {
            styleProperties.setStyleValue(property, value);
        }
    }

    protected void setHeight(
            MutableStyleProperties styleProperties, Format format) {

        StyleValue value = converter.getDimensionValue(format.getHeight(),
                null);
        if (value != null) {
            styleProperties.setStyleValue(StylePropertyDetails.HEIGHT, value);
        }
    }

    protected void setWidth(
            MutableStyleProperties styleProperties, Format format) {

        StyleValue value = converter.getDimensionValue(format.getWidth(),
                format.getWidthUnits());

        if (value != null) {
            styleProperties.setStyleValue(StylePropertyDetails.WIDTH, value);
        }
    }

    protected void setBorderSpacing(
            MutableStyleProperties styleProperties, Format format) {

        // A border spacing of zero should not be considered a default value,
        // but should be honoured (i.e. not overwritten by it's parents value). 
        StyleValue value =
                converter.getLengthValue(format.getCellSpacing(), true);
        if (value != null) {
            value = converter.getPairValue(value);

            styleProperties.setStyleValue(StylePropertyDetails.BORDER_SPACING,
                    value);
        }
    }

    protected void setPadding(
            MutableStyleProperties styleProperties, Format format) {

        StyleValue value =
                converter.getLengthValue(format.getCellPadding(), false);
        if (value != null) {
            setEdgeValues(styleProperties, PropertyGroups.PADDING_PROPERTIES,
                    value);
        }
    }

    protected void setBorder(
            MutableStyleProperties styleProperties, Format format) {

        StyleValue value =
                converter.getLengthValue(format.getBorderWidth(), false);
        if (value != null) {
            setEdgeValues(styleProperties,
                    PropertyGroups.BORDER_WIDTH_PROPERTIES, value);
            setEdgeValues(styleProperties,
                          PropertyGroups.BORDER_STYLE_PROPERTIES,
                    BorderStyleKeywords.SOLID);
        }
    }

    protected void setBackgroundColor(
            MutableStyleProperties properties, Format format) {

        StyleValue value = converter.getColorValue(
                format.getBackgroundColour());
        if (value != null) {
            properties.setStyleValue(StylePropertyDetails.BACKGROUND_COLOR,
                    value);
        }
    }

    protected void setEdgeValues(
            MutableStyleProperties styleProperties,
            StyleProperty[] properties,
            StyleValue value) {
        for (int i = 0; i < properties.length; i++) {
            StyleProperty property = properties[i];

            styleProperties.setStyleValue(property, value);
        }
    }

    protected void initialiseCommonProperties(
            MutableStyleProperties styleProperties, Format format,
            StyleKeyword display) {

        styleProperties.setStyleValue(StylePropertyDetails.DISPLAY, display);

        setBackgroundColor(styleProperties, format);
        setBackgroundComponent(styleProperties, format);
        setBorder(styleProperties, format);
        setBorderSpacing(styleProperties, format);
        setWidth(styleProperties, format);
        setHeight(styleProperties, format);
        setVerticalAlign(styleProperties, format);
        setHorizontalAlign(styleProperties, format);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/4	emma	VBM:2005111705 Interim commit

 12-Dec-05	10374/2	emma	VBM:2005111705 Interim commit

 06-Dec-05	10641/1	geoff	VBM:2005113024 Pagination page rendering issues

 06-Dec-05	10621/1	geoff	VBM:2005113024 Pagination page rendering issues

 02-Dec-05	10567/1	emma	VBM:2005112901 Forward port of bug fix: problems targetting styles by setting a class on the parent

 02-Dec-05	10544/1	emma	VBM:2005112901 Bug fix: problems targetting styles by setting a class on the parent

 02-Dec-05	10542/1	emma	VBM:2005112308 Forward port: Many bug fixes: xforms, GUI and pane styling

 01-Dec-05	10447/3	emma	VBM:2005112308 Many bug fixes: xforms, GUI and pane styling

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 28-Nov-05	10394/3	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 25-Nov-05	10443/1	ianw	VBM:2005111812 interim commit for IB

 09-Nov-05	10201/3	emma	VBM:2005102606 Fixing various styling bugs

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 28-Nov-05	10394/3	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 25-Nov-05	10443/1	ianw	VBM:2005111812 interim commit for IB

 09-Nov-05	10221/1	emma	VBM:2005102606 Forward port: fixing various styling bugs

 09-Nov-05	10201/3	emma	VBM:2005102606 Fixing various styling bugs

 27-Sep-05	9487/2	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/5	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/3	pduffin	VBM:2005083007 Changed layout style sheet builder over to using the new model, added support for nth child

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Added support and tests for immediately preceding sibling selectors and multiple pseudo element selectors in the styling engine

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/

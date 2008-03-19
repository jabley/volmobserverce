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

package com.volantis.styling.impl;

import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.css.parser.CSSParserFactory;
import com.volantis.mcs.themes.CSSParserMode;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValues;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesMerger;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.compiler.CSSCompiler;
import com.volantis.styling.compiler.CompilerConfiguration;
import com.volantis.styling.compiler.FunctionResolverBuilder;
import com.volantis.styling.compiler.SpecificityCalculator;
import com.volantis.styling.compiler.StyleSheetCompiler;
import com.volantis.styling.compiler.StyleSheetCompilerFactory;
import com.volantis.styling.device.DeviceOutlook;
import com.volantis.styling.device.DeviceStylingEngine;
import com.volantis.styling.engine.StylingEngine;
import com.volantis.styling.impl.compiler.CompilerConfigurationImpl;
import com.volantis.styling.impl.compiler.InlineStylesSpecificityCalculator;
import com.volantis.styling.impl.compiler.SpecificityCalculatorImpl;
import com.volantis.styling.impl.compiler.StandardStyleSheetCompiler;
import com.volantis.styling.impl.device.DeviceCSSCompiler;
import com.volantis.styling.impl.device.DeviceExtensionHandler;
import com.volantis.styling.impl.device.DeviceStylingEngineImpl;
import com.volantis.styling.impl.engine.StylingEngineImpl;
import com.volantis.styling.impl.expressions.FunctionResolverBuilderImpl;
import com.volantis.styling.impl.sheet.StyleSheetInternal;
import com.volantis.styling.impl.values.MutablePropertyValuesImpl;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertyDefinitions;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.styling.values.InitialValueFinder;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.styling.values.PropertyValues;

import java.util.Iterator;

/**
 * Implementation of {@link StylingFactory}.
 */
public class StylingFactoryImpl
    extends StylingFactory {

    /**
     * Definitions of all supported properties.
     */
    private static final StylePropertyDefinitions DEFINITIONS =
            StylePropertyDetails.getDefinitions();

    private final DefaultStylesMerger defaultStylesMerger;

    public StylingFactoryImpl() {
        this.defaultStylesMerger = new DefaultStylesMerger(this);
    }

    // Javadoc inherited.
    public CompilerConfiguration createCompilerConfiguration() {
        return new CompilerConfigurationImpl();
    }

    // Javadoc inherited.
    public StyleSheetCompiler createStyleSheetCompiler(
            CompilerConfiguration configuration) {

        return new StandardStyleSheetCompiler(configuration);
    }

    public StylingEngine createStylingEngine() {
        return new StylingEngineImpl();
    }

    // Javadoc inherited.
    public StylingEngine createStylingEngine(
            StyleSheetCompilerFactory inlineCompilerFactory) {
        return new StylingEngineImpl(inlineCompilerFactory);
    }

    public MutablePropertyValues createPropertyValues() {
        return new MutablePropertyValuesImpl(DEFINITIONS);
    }

    // Javadoc inherited.
    public Styles createStyles(PropertyValues propertyValues) {
        return new StylesImpl(null, null, propertyValues);
    }

    // Javadoc inherited.
    public MutablePropertyValues createPropertyValues(
            StylePropertyDefinitions definitions) {

        return new MutablePropertyValuesImpl(definitions);
    }

    // Javadoc inherited.
    public SpecificityCalculator createSpecificityCalculator() {
        return new SpecificityCalculatorImpl();
    }

    // Javadoc inherited.
    public Styles createInheritedStyles(Styles parent, StyleKeyword display) {
        StyleValues parentValues;
        Iterator iterator;
        if (parent == null) {
            iterator = StylePropertyDetails.getDefinitions()
                    .stylePropertyIterator();
            parentValues = StylePropertyDetails.getDefinitions()
                    .getStandardDetailsSet().getRootStyleValues();
        } else {
            MutablePropertyValues parentPropertyValues =
                    parent.getPropertyValues();
            iterator = parentPropertyValues.stylePropertyIterator();
            parentValues = parentPropertyValues;
        }

        Styles styles = createInheritedStyles(iterator, parentValues, display);

        return styles;
    }

    private Styles createInheritedStyles(
            Iterator iterator, StyleValues parentValues,
            StyleKeyword display) {

        InitialValueFinder initialValueFinder = new InitialValueFinder();
        Styles styles = new StylesImpl();
        MutablePropertyValues propertyValues = styles.getPropertyValues();
        while (iterator.hasNext()) {
            StyleProperty property = (StyleProperty) iterator.next();
            StyleValue value;
            if (property.getStandardDetails().isInherited()) {
                value = parentValues.getStyleValue(property);
            } else  {
                value = initialValueFinder.getInitialValue(
                        propertyValues, property.getStandardDetails());
            }

            propertyValues.setComputedValue(property, value);
        }

        propertyValues.setComputedValue(StylePropertyDetails.DISPLAY, display);
        return styles;
    }

    // Javadoc inherited.
    public Styles createInheritedStyles(
            StyleValues parent, StyleKeyword display) {
        return createInheritedStyles(
                StylePropertyDetails.getDefinitions().stylePropertyIterator(),
                parent, display);
    }

    // Javadoc inherited.
    public FunctionResolverBuilder createFunctionResolverBuilder() {
        return new FunctionResolverBuilderImpl();
    }

    // Javadoc inherited.
    public StylesMerger getStylesMerger() {
        return defaultStylesMerger;
    }

    // Javadoc inherited
    public SpecificityCalculator createInlineSpecificityCalculator() {
        return new InlineStylesSpecificityCalculator();
    }

    // Javadoc inherited.
    public CSSCompiler createDeviceCSSCompiler(DeviceOutlook outlook) {
        return createDeviceCSSCompiler(createDeviceCSSParser(), outlook);
    }

    // Javadoc inherited.
    public CSSCompiler createDeviceCSSCompiler(
            CSSParser parser, DeviceOutlook outlook) {
        return new DeviceCSSCompiler(parser, outlook);
    }

    // Javadoc inherited.
    public DeviceStylingEngine createDeviceStylingEngine(
            CompiledStyleSheet compiledStyleSheet) {
        return new DeviceStylingEngineImpl(
                (StyleSheetInternal) compiledStyleSheet);
    }

    // Javadoc inherited.
    public CSSParser createDeviceCSSParser() {
        return CSSParserFactory.getDefaultInstance().createExtensibleParser(
                CSSParserMode.LAX,
                DeviceExtensionHandler.getDefaultInstance());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/4	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/5	pduffin	VBM:2005111405 Massive changes for performance

 18-Nov-05	10347/3	pduffin	VBM:2005111405 Stopped copying style values in order to change whether they were explicitly specified or not

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Removed some unnecessary usages of setSpecifiedValue

 30-Sep-05	9635/1	adrianj	VBM:2005092817 Counter functions for CSS

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/4	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 22-Jul-05	8859/3	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 18-Jul-05	9029/3	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 30-Jun-05	8893/1	emma	VBM:2005062406 Annotate DOM elements generated from VDXML with styles

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/

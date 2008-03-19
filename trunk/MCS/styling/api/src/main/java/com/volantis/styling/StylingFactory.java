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

package com.volantis.styling;

import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleValues;
import com.volantis.styling.compiler.CSSCompiler;
import com.volantis.styling.compiler.CompilerConfiguration;
import com.volantis.styling.compiler.FunctionResolverBuilder;
import com.volantis.styling.compiler.SpecificityCalculator;
import com.volantis.styling.compiler.StyleSheetCompiler;
import com.volantis.styling.compiler.StyleSheetCompilerFactory;
import com.volantis.styling.device.DeviceOutlook;
import com.volantis.styling.device.DeviceStylingEngine;
import com.volantis.styling.engine.StylingEngine;
import com.volantis.styling.properties.StylePropertyDefinitions;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.styling.values.PropertyValues;
import com.volantis.synergetics.factory.MetaDefaultFactory;

/**
 * Entry point for the styling infrastructure.
 *
 * @mock.generate
 */
public abstract class StylingFactory {

    /**
     * Obtain a reference to the default factory implementation.
     */
    protected static final MetaDefaultFactory metaDefaultFactory;
    static {
        metaDefaultFactory =
            new MetaDefaultFactory(
                    "com.volantis.styling.impl.StylingFactoryImpl",
                    StylingFactory.class.getClassLoader());
    }

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static StylingFactory getDefaultInstance() {
//        System.out.println("Getting default factory instance - ");
//        new Exception().printStackTrace(System.out);
        StylingFactory stylingFactory = (StylingFactory)
                metaDefaultFactory.getDefaultFactoryInstance();
//        System.out.println("Got default factory instance");
        return stylingFactory;
    }

    /**
     * Create a new {@link CompilerConfiguration}.
     *
     * @return A newly created {@link CompilerConfiguration}.
     */
    public abstract CompilerConfiguration createCompilerConfiguration();

    /**
     * Create a new {@link StyleSheetCompiler} based on the supplied
     * configuration.
     *
     * @param configuration The configuration that determines the
     * characteristics of the compiler.
     *
     * @return A newly created {@link StyleSheetCompiler}.
     */
    public abstract StyleSheetCompiler createStyleSheetCompiler(
            CompilerConfiguration configuration);

    /**
     * Create a new {@link StylingEngine} that will apply the styles contained
     * within the specified sheet.
     *
     * @return A newly created {@link StylingEngine}.
     */
    public abstract StylingEngine createStylingEngine();

    /**
     * Create a new {@link StylingEngine} that will apply the styles contained
     * within the specified sheet.
     *
     * @param inlineCompilerFactory The factory used to create style sheet
     *                              compilers for CSS embedded in 'style'
     *                              attributes.
     * @return A newly created {@link StylingEngine}.
     */
    public abstract StylingEngine createStylingEngine(
            StyleSheetCompilerFactory inlineCompilerFactory);

    /**
     * Create a new {@link MutablePropertyValues} that can support the
     * specified set of definitions.
     *
     * @return A newly created {@link MutablePropertyValues}.
     */
    public abstract MutablePropertyValues createPropertyValues();

    /**
     * Create a new {@link MutablePropertyValues} that can support the
     * specified set of definitions.
     *
     * @param definitions The definitions.
     *
     * @return A newly created {@link MutablePropertyValues}.
     */
    public abstract MutablePropertyValues createPropertyValues(
            StylePropertyDefinitions definitions);

    /**
     * Create a new {@link Styles}. This should usually not be used, and
     * should never be used for testing; if Styles are required in tests, then
     * mock objects should be used.
     *
     * @param propertyValues to use to initialise the styles. May be null.
     * @return new Styles.
     */
    public abstract Styles createStyles(PropertyValues propertyValues);

    /**
     * Create and return a new {@link StylesMerger} using the default styling
     * factory.
     *
     * @return newly created styles merger
     */
    public abstract StylesMerger getStylesMerger();

    /**
     * Create an object that will calculate the specificity of selectors.
     *
     * @return An object that will calculate the specificity of selectors.
     */
    public abstract SpecificityCalculator createSpecificityCalculator();

    /**
     * Create a styles whose properties are set to either the inherited, or the
     * initial values depending on whether the property is automatically
     * inherited or not.
     *
     * @param parent The parent styles. This MUST NOT be null.
     *
     * @param display
     * @return The inherited styles.
     */
    public abstract Styles createInheritedStyles(
            Styles parent, StyleKeyword display);

    /**
     * Create a styles whose properties are set to either the inherited, or the
     * initial values depending on whether the property is automatically
     * inherited or not.
     *
     * @param parent The parent values.
     *
     * @param display
     * @return The inherited styles.
     */
    public abstract Styles createInheritedStyles(
            StyleValues parent, StyleKeyword display);

    /**
     * Creates a function resolver builder.
     *
     * @return A function resolver builder.
     */
    public abstract FunctionResolverBuilder createFunctionResolverBuilder();

    /**
     * Creates an SpecificityCalculator which calculates specificity assuming
     * the styles have been specified through the style attribute.
     *
     * @return The SpecificityCalculator
     */
    public abstract SpecificityCalculator createInlineSpecificityCalculator();

    /**
     * Create a CSS compiler for a device specific style sheet.
     *
     * @param outlook The outlook on the device information.
     * @return The newly constructed compiler.
     */
    public abstract CSSCompiler createDeviceCSSCompiler(DeviceOutlook outlook);

    /**
     * Create a CSS compiler for a device specific style sheet.
     *
     * @param parser  The CSS parser to use, must have been constructed by
     *                {@link #createDeviceCSSParser()}.
     * @param outlook The outlook on the device information.
     * @return The newly constructed compiler.
     */
    public abstract CSSCompiler createDeviceCSSCompiler(
            CSSParser parser, DeviceOutlook outlook);

    /**
     * Create a styling engine to use to apply device specific styles sheets.
     *
     * @param compiledStyleSheet The style sheet that must have been compiled
     *                           by a {@link CSSCompiler} returned by
     *                           {@link #createDeviceCSSCompiler}.
     * @return The styling engine.
     */
    public abstract DeviceStylingEngine createDeviceStylingEngine(
            CompiledStyleSheet compiledStyleSheet);

    /**
     * Create a parser for device CSS.
     *
     * @return The newly created parser.
     */ 
    public abstract CSSParser createDeviceCSSParser();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 30-Sep-05	9635/1	adrianj	VBM:2005092817 Counter functions for CSS

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/5	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 05-Aug-05	8859/7	emma	VBM:2005062006 Fixing merge conflicts

 22-Jul-05	8859/4	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 01-Aug-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 18-Jul-05	9029/3	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 30-Jun-05	8893/1	emma	VBM:2005062406 Annotate DOM elements generated from VDXML with styles

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Jun-05	7997/3	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 02-Jun-05	7997/1	pduffin	VBM:2005050324 Added styling API

 ===========================================================================
*/

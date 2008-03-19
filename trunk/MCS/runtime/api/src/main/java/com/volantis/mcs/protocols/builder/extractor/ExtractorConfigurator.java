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

package com.volantis.mcs.protocols.builder.extractor;

import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.css.renderer.BorderRadiusHelper;
import com.volantis.mcs.css.version.CSSProperty;
import com.volantis.mcs.css.version.CSSPropertyIteratee;
import com.volantis.mcs.css.version.CSSVersion;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom2theme.StyledDOMThemeExtractorFactory;
import com.volantis.mcs.dom2theme.extractor.ExtractorConfiguration;
import com.volantis.mcs.dom2theme.extractor.ExtractorConfigurationBuilder;
import com.volantis.mcs.themes.MutableShorthandSet;
import com.volantis.mcs.themes.StyleShorthand;
import com.volantis.mcs.themes.StyleShorthands;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.compiler.CSSCompiler;
import com.volantis.styling.device.DeviceOutlook;
import com.volantis.styling.properties.InitialValueAccuracy;
import com.volantis.styling.properties.PropertyDetailsBuilder;
import com.volantis.styling.properties.PropertyDetailsSet;
import com.volantis.styling.properties.PropertyDetailsSetBuilder;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.styling.values.FixedInitialValue;
import com.volantis.styling.values.InitialValueSource;

import java.io.StringReader;
import java.util.Iterator;

/**
 * Create a {@link ExtractorConfigurator} from a device and {@link CSSVersion}.
 */
public class ExtractorConfigurator implements CSSPropertyIteratee {

    /**
     * The factory for creating the objects needed by the extractor.
     */
    private final StyledDOMThemeExtractorFactory factory;

    /**
     * The lax extended CSS parser.
     */
    private final CSSParser cssParser;

    /**
     * The device being processed.
     */
    private InternalDevice device;

    /**
     * The builder for the {@link PropertyDetailsSet}.
     */
    private PropertyDetailsSetBuilder detailsSetBuilder;

    /**
     * Initialise.
     */
    public ExtractorConfigurator() {
        // Create a CSS parser for processing the device repository values.
        StylingFactory stylingFactory = StylingFactory.getDefaultInstance();

        cssParser = stylingFactory.createDeviceCSSParser();

        factory = StyledDOMThemeExtractorFactory.getDefaultInstance();
    }

    /**
     * Create the configuration for the CSS extractor.
     *
     * @param device     The device for which the CSS will be generated.
     * @param cssVersion The version of CSS supported by the device.
     * @return The configuration.
     */
    public ExtractorConfiguration createConfiguration(
            final InternalDevice device, CSSVersion cssVersion) {

        this.device = device;

        detailsSetBuilder = new PropertyDetailsSetBuilder();

        // Create a definition of which style properties we will work on.
        // We need to minimise this set of style properties as much as
        // possible as processing each style property is *very* processor
        // intensive. For example, there is not much use extracing most of
        // the mcs-* properties as they are not understood by any device.
        cssVersion.iterate(this);

        ExtractorConfigurationBuilder configurationBuilder =
                factory.createConfigurationBuilder();

        // Populate the set of supported shorthands.
        MutableShorthandSet supportedShorthands = new MutableShorthandSet();

        Iterator i = StyleShorthands.getDefinitions().iterator();
        while (i.hasNext()) {
            StyleShorthand shorthand = (StyleShorthand) i.next();
            if (cssVersion.supportsShorthand(shorthand)) {
                supportedShorthands.add(shorthand);
            }
        }
        
        String internalShorthand = BorderRadiusHelper.supportShorthand(cssVersion);        
        if(internalShorthand != null) {
            supportedShorthands.add(StyleShorthands.getDefinitions().getShorthand("mcs-border-radius"));            
        }

        configurationBuilder.setSupportedShorthands(supportedShorthands);

        PropertyDetailsSet detailsSet = detailsSetBuilder.getDetailsSet();

        String defaultCSS = device.getPolicyValue(
                DevicePolicyConstants.DEFAULT_CSS);
        if (defaultCSS == null) {
            defaultCSS = "";
        }
        
        String sourceLocation = device.getName() + "/" +
                DevicePolicyConstants.DEFAULT_CSS;

        CompiledStyleSheet compiledStyleSheet = compileDeviceStyleSheet(
                defaultCSS, sourceLocation);

        configurationBuilder.setDeviceStyleSheet(compiledStyleSheet);

        configurationBuilder.setDetailsSet(detailsSet);

        return configurationBuilder.buildConfiguration();
    }

    /**
     * Compile the device style sheet.
     *
     * @param defaultCSS        The default CSS.
     * @param sourceDescription A description of the source of the CSS.
     * @return The compiled device style sheet.
     */
    private CompiledStyleSheet compileDeviceStyleSheet(
            String defaultCSS, String sourceDescription) {

        // The default display values used by the device.
        defaultCSS = DevicePolicyConstants.DEFAULT_DISPLAY_CSS + defaultCSS;

        StylingFactory factory = StylingFactory.getDefaultInstance();
        CSSCompiler compiler = factory.createDeviceCSSCompiler(
                DeviceOutlook.REALISTIC);

        return compiler.compile(new StringReader(defaultCSS), sourceDescription);
    }

    // Javadoc inherited.
    public IterationAction next(CSSProperty cssProperty) {
        final StyleProperty property = cssProperty.getStyleProperty();

        // Create a details builder that defaults to the standard details.
        PropertyDetailsBuilder builder = new PropertyDetailsBuilder(property);

        String policy = "x-css.properties." + property.getName() +
                    ".initial-value";


        // Get the standard initial value source.
        InitialValueSource initialValueSource =
                property.getStandardDetails().getInitialValueSource();
        InitialValueAccuracy accuracy = InitialValueAccuracy.ASSUMED;

        String value = device.getPolicyValue(policy);

        if (value == null || value.equals("") || value.equals("<default>")) {
            accuracy = InitialValueAccuracy.ASSUMED;
        } else if (value.equals("<unknown>")) {
            accuracy = InitialValueAccuracy.UNKNOWN;
            initialValueSource = null;
        } else if (value.equals("<standard>")) {
            accuracy = InitialValueAccuracy.KNOWN;
        } else {

            // Parse the property.
            StyleValue initial = cssParser.parseStyleValue(property, value);
            if (initial != null) {
                initialValueSource = new FixedInitialValue(initial);
                accuracy = InitialValueAccuracy.KNOWN;
            }
        }

        builder.setInitialValueSource(initialValueSource);
        builder.setInitialValueAccuracy(accuracy);

        detailsSetBuilder.addBuilder(builder);

        return IterationAction.CONTINUE;
    }
}

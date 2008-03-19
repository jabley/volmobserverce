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
package com.volantis.mcs.protocols.builder;

import com.volantis.mcs.css.version.DefaultCSSProperty;
import com.volantis.mcs.css.version.DefaultCSSVersion;
import com.volantis.mcs.css.version.ManualCSS1VersionFactory;
import com.volantis.mcs.css.version.ManualCSS2VersionFactory;
import com.volantis.mcs.css.version.ManualCSSMobileVersionFactory;
import com.volantis.mcs.css.version.ManualCSSWAPVersionFactory;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.ProtocolConfigurationImpl;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleShorthand;
import com.volantis.mcs.themes.StyleShorthandIteratee;
import com.volantis.mcs.themes.StyleShorthands;
import com.volantis.mcs.themes.StyleSyntax;
import com.volantis.mcs.themes.StyleSyntaxIteratee;
import com.volantis.mcs.themes.StyleSyntaxes;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.mappers.CSSPropertyNameMapper;
import com.volantis.mcs.themes.properties.AllowableKeywords;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertyAlias;
import com.volantis.styling.properties.StylePropertyAliasIteratee;
import com.volantis.styling.properties.StylePropertyAliases;
import com.volantis.styling.properties.StylePropertyIteratee;
import com.volantis.styling.properties.StyleShorthandAlias;
import com.volantis.styling.properties.StyleShorthandAliasIteratee;
import com.volantis.styling.properties.StyleShorthandAliases;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Initialises a protocol configuration with the CSS data from a device.
 * <p>
 * For now, this works by using the style sheet version ("ssversion" device
 * policy) as a template for which properties the device supports and then adds
 * and/or removes individual properties from that template (using device
 * policies like "x-css.properties.{propertyname}.support" = "full"|"none").
 * <p>
 * Note that individual properties are only inherited from parent devices which
 * share the same style sheet version to avoid invalid data creeping in.
 * <p>
 * Hopefully this structure will be quite similar to how the modular device
 * repository will represent CSS information.
 * <p>
 * See R1102 (UseCase.Themes.CSSparams.FilterRedundant) for more details.
 */
public class CSSConfigurator implements ProtocolDeviceConfigurator {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(CSSConfigurator.class);

    /**
     * Provides a mapping from internal style property names to their external
     * css representations.
     */
    private static final CSSPropertyNameMapper CSS_NAME_MAPPER  =
            CSSPropertyNameMapper.getDefaultInstance();

    /**
     * The list of ancestor devices which have the same CSS version.
     */
    private final List cssMatchAncestors = new ArrayList();

    // Javadoc inherited.
    public void initialise(ProtocolConfigurationImpl configuration,
            final InternalDevice device) {

        if (logger.isDebugEnabled()) {
            logger.debug("Configuring CSS for device '" +
                    device.getName() + "'");
        }

        final DefaultCSSVersion cssVersion;
        // Initialise the CSS version from the device.
        String styleSheetVersion = device.getStyleSheetVersion();
        // If the device has a style sheet version.
        if (styleSheetVersion != null) {
            // Then use it.

            // Find the ancestor devices which share the same style sheet
            // version. These are the ones that we may inherit individual
            // property definitions from.
            calculateAncestors(device, styleSheetVersion);

            if (DevicePolicyConstants.CSS_MOBILE_PROFILE1.equals(
                    styleSheetVersion)) {
                cssVersion =
                        new ManualCSSMobileVersionFactory().createCSSVersion();
            } else if (DevicePolicyConstants.CSS1.equals(
                    styleSheetVersion)) {
                cssVersion =
                        new ManualCSS1VersionFactory().createCSSVersion();
            } else if (DevicePolicyConstants.CSS_WAP.equals(styleSheetVersion)) {
                cssVersion =
                        new ManualCSSWAPVersionFactory().createCSSVersion();
            } else if (DevicePolicyConstants.CSS2.equals(styleSheetVersion)) {
                cssVersion =
                        new ManualCSS2VersionFactory().createCSSVersion();
            } else {
                // Should never happen, as getStyleSheetVersion constrains the
                // results to ones that we know about.
                throw new IllegalStateException("Unexpected device style " +
                        "sheet version '" + styleSheetVersion + "'");
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Using style sheet version '" +
                        styleSheetVersion + "'");
            }

            initialiseStyleProperties(device, cssVersion);

            initialiseStyleShorthands(device, cssVersion);

            initialiseAliases(device, cssVersion);

            initialiseStyleSyntaxes(device, cssVersion);

            CSSSupportConfigurator cssSupport =
                    new CSSSupportConfigurator(device);
            cssVersion.setRemappableElements(
                    cssSupport.getFallbackAttributeExpressions());

            cssVersion.markImmutable();
            configuration.setCssVersion(cssVersion);
        }
        // else the device had no style sheet version set, or had it set to
        // "None". We can just leave the css version as null in this case,
        // as it only affects rendering at this stage, which we assume will
        // never happen.

    }

    /**
     * Iterate over the style properties (defined in
     * {@link StylePropertyDetails}) and update the cssVersion with any values
     * specified for the individual property by the device.
     *
     * @param device        which may specify different levels of support for
     *                      individual style properties
     * @param cssVersion    to update with the device specific information
     */
    private void initialiseStyleProperties(final InternalDevice device,
                                           final DefaultCSSVersion cssVersion) {
        // Add or subtract any individual css properties from the base
        // version as specified in the device.

        // Iterate over the individual property names, checking for each
        // one in the device.
        StylePropertyDetails.getDefinitions().iterateStyleProperties(
                new StylePropertyIteratee() {
            public IterationAction next(StyleProperty property) {

                final String externalName =
                        CSS_NAME_MAPPER.getExternalString(property);
                String policy = "x-css.properties." + externalName + ".support";

                String value = getAncestorPolicyValue(device, policy);

                if ("full".equals(value)) {
                    // Add the property definition to the css version.
                    cssVersion.addPermissiveProperty(property);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Adding device CSS property '" +
                                property.getName() + "'");
                    }
                } else if ("none".equals(value)) {
                    // Remove the property definiton from the css version.
                    cssVersion.removeProperty(property);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Removing device CSS property '" +
                                property.getName() + "'");
                    }
                } else if (value == null || "default".equals(value)) {
                    // Leave the original definition, but modify the
                    // according to any lower level properties.

                    // Extract the CSS property for this property.
                    DefaultCSSProperty cssProperty = (DefaultCSSProperty)
                            cssVersion.getProperty(property);

                    if (cssProperty != null) {
                        // Iterate over the keywords for this property.
                        if (cssProperty.supportsValueType(
                                StyleValueType.KEYWORD)) {
                            initialisePropertyKeywords(device, cssProperty);
                        }
                    } else {
                        // default may be that the property is undefined.
                        // So in order to add keywords to an undefined
                        // property we would need to create it first.
                        // However that is slightly non-trivial to do and
                        // really we should refactor the CSS version
                        // creation stuff to be more flexible to support it,
                        // i.e add some builders. For now we just ignore
                        // this case.
                        // TODO: create empty property to have keywords
                        // (and values?) added
                    }

                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Ignoring unknown value: " + value);
                    }
                }

                return IterationAction.CONTINUE;
            }
        });
    }

    /**
     * Iterate over the style shorthands (defined in {@link StyleShorthands})
     * and update the cssVersion with any values specified for the individual
     * shorthand by the device.
     *
     * @param device        which may specify different levels of support for
     *                      individual style shorthands
     * @param cssVersion    to update with the device specific information
     */
    private void initialiseStyleShorthands(final InternalDevice device,
                                           final DefaultCSSVersion cssVersion) {
        // Iterate over the shorthands property names, checking for each
        // one in the device.
        StyleShorthands.getDefinitions().iterate(new StyleShorthandIteratee() {
            public IterationAction iterate(StyleShorthand shorthand) {

                final String externalName =
                        CSS_NAME_MAPPER.getExternalString(shorthand);
                String policy =
                        "x-css.shorthands." + externalName + ".support";

                String value = getAncestorPolicyValue(device, policy);

                if ("full".equals(value)) {
                    // Add the property definition to the css version.
                    cssVersion.addShorthandProperty(shorthand);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Adding device CSS shorthand '" +
                                shorthand.getName() + "'");
                    }
                } else if ("none".equals(value)) {
                    // Remove the property definiton from the css version.
                    cssVersion.removeShorthand(shorthand);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Removing device CSS shorthand '" +
                                shorthand.getName() + "'");
                    }
                } else if (value == null || "default".equals(value)) {
                    // Leave the original definition.

                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Ignoring unknown value: " + value);
                    }
                }

                return IterationAction.CONTINUE;
            }
        });
    }

    /**
     * Iterate over the style property and shorthand aliases (defined in
     * {@link StylePropertyAliases} and {@link StyleShorthandAliases}) and
     * update the cssVersion with any values specified for the individual
     * alias by the device.
     *
     * @param device        which may specify different levels of support for
     *                      individual style aliases
     * @param cssVersion    to update with the device specific information
     */
    private void initialiseAliases(final InternalDevice device,
                                   final DefaultCSSVersion cssVersion) {
        // Iterate over the alias property names, checking for each
        // one in the device and save it to cssVersion
        StylePropertyAliases.iterate(new StylePropertyAliasIteratee() {
            public IterationAction visit(StylePropertyAlias similarity) {

                // Iterate over the list of similar properties, in order of
                // preference.
                Iterator i = similarity.getAliasProperties().iterator();
                while (i.hasNext()) {
                    String similarPropertyName = (String)i.next();

                    String policy = "x-css.properties." + similarPropertyName +
                            ".support";

                    String value = getAncestorPolicyValue(device, policy);

                    if (value != null && "full".equals(value)) {
                        // Add the property definition to the css version.
                        cssVersion.addPermissiveProperty(similarPropertyName,
                                similarity.getProperty());
                        // Use the first found similarity
                        break;
                    }
                    // TODO: add handling for other possible values that is "default"
                    // and "none", when needed.
                }
                return IterationAction.CONTINUE;
            }
        });

        // Iterate over the alias shorthand property names, checking for each
        // one in the device and save it to cssVersion
        StyleShorthandAliases.iterate(new StyleShorthandAliasIteratee() {
            public IterationAction visit(StyleShorthandAlias alias) {

                // Iterate over the list of similar properties, in order
                // of preference.
                Iterator i = alias.getAliasShorthands().iterator();
                while (i.hasNext()) {
                    String aliasShorthandName = (String)i.next();

                    String policy = "x-css.shorthands." + aliasShorthandName +
                            ".support";

                    String value = getAncestorPolicyValue(device, policy);

                    if (value != null && "full".equals(value)) {
                        // Add the property definition to the css version.
                        cssVersion.addShorthandProperty(aliasShorthandName);
                        // Use the first found similarity
                        break;
                    }
                    // TODO: add handling for other possible values that is "default"
                    // and "none", when needed.
                }
                return IterationAction.CONTINUE;
            }
        });
    }

    /**
     * Iterate over the style syntaxes defined in {@link StyleSyntaxes} and
     * update the cssVersion with any values specified for the individual
     * syntax by the device.
     *
     * @param device        which may specify different levels of support for
     *                      individual style syntaxes
     * @param cssVersion    to update with the device specific information
     */
    private void initialiseStyleSyntaxes(final InternalDevice device,
                                         final DefaultCSSVersion cssVersion) {
        // Iterate over the syntaxes, checking for each one in the device
        // and saving it to cssVersion.
        StyleSyntaxes.getDefinitions().iterate(new StyleSyntaxIteratee() {
            // Javadoc inherited.
            public IterationAction iterate(StyleSyntax syntax) {
                final String name = syntax.getName();
                String policy ="x-css.syntax.supports." + name;

                String value = getAncestorPolicyValue(device, policy);

                if ("full".equals(value)) {
                    // Add the syntax definition to the css version.
                    cssVersion.addSyntax(name);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Adding device CSS syntax '" +
                                name + "'");
                    }
                } else if ("none".equals(value)) {
                    // Remove the syntax definiton from the css version.
                    cssVersion.removeSyntax(name);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Removing device CSS syntax '" +
                                name + "'");
                    }
                } else if (value == null || "default".equals(value)) {
                    // Leave the original definition.

                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Ignoring unknown value: " + value);
                    }
                }

                return IterationAction.CONTINUE;
            }
        });
    }

    private void initialisePropertyKeywords(InternalDevice device,
            DefaultCSSProperty cssProperty) {

        StyleProperty property = cssProperty.getStyleProperty();

        final AllowableKeywords allowableKeywords =
                property.getStandardDetails().getAllowableKeywords();
        if (allowableKeywords != null) {
            List keywords = allowableKeywords.getKeywords();
            for (int i = 0; i < keywords.size(); i++) {
                StyleKeyword keyword = (StyleKeyword) keywords.get(i);

                final String externalName =
                            CSS_NAME_MAPPER.getExternalString(property);
                String externalKeywordName =
                        CSS_NAME_MAPPER.getExternalString(keyword);
                String policy = "x-css.properties." + externalName +
                        ".keyword." + externalKeywordName + ".support";

                String value = getAncestorPolicyValue(device, policy);

                if ("full".equals(value)) {
                    // Add the property definition to the css version.
                    cssProperty.addKeyword(keyword);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Adding device CSS property '" +
                                property.getName() + "' keyword '" +
                                keyword.getName() + "'");
                    }
                } else if ("none".equals(value)) {
                    // Remove the property definiton from the css version.
                    cssProperty.removeKeyword(keyword);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Removing device CSS property '" +
                                property.getName() + "' keyword '" +
                                keyword.getName() + "'");
                    }
                } else if (value == null || "default".equals(value)) {
                    // Leave the original definition.

                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Ignoring unknown value: " + value);
                    }
                }
            }
        }
    }

    private void calculateAncestors(final InternalDevice device,
            String styleSheetVersion) {

        InternalDevice ancestor = device.getFallbackDevice();
        while (ancestor != null && ancestor.getStyleSheetVersion() != null &&
                ancestor.getStyleSheetVersion().equals(styleSheetVersion)) {
            cssMatchAncestors.add(ancestor);
            ancestor = ancestor.getFallbackDevice();
        }

        if (logger.isDebugEnabled()) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < cssMatchAncestors.size(); i++) {
                InternalDevice dev = (InternalDevice)
                        cssMatchAncestors.get(i);
                buffer.append(dev.getName()).append(" ");
            }
            logger.debug("CSS match ancestors: " + buffer);
        }
    }

    private String getAncestorPolicyValue(
            final InternalDevice device, String policy) {

        String value = device.getSpecifiedPolicyValue(policy);
        if (value == null) {
            int depth = cssMatchAncestors.size() - 1;
            while (value == null && depth >= 0) {
                InternalDevice ancestor = (InternalDevice)
                        cssMatchAncestors.get(depth);
                value = ancestor.getSpecifiedPolicyValue(policy);
                depth--;
                if (logger.isDebugEnabled()) {
                    if (value != null) {
                        logger.debug("Using ancestor '" +
                                ancestor.getName() + "' specified: " +
                                policy + "=" + value);
                    }
                }
            }
        } else {

            if (logger.isDebugEnabled()) {
                logger.debug("Using device '" + device.getName() +
                        "' specified: " + policy + "=" + value);
            }
        }
        return value;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10835/1	geoff	VBM:2005121405 P900; superscript does not work with CssMobleProfile

 14-Dec-05	10829/1	geoff	VBM:2005121405 P900; superscript does not work with CssMobleProfile

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 ===========================================================================
*/

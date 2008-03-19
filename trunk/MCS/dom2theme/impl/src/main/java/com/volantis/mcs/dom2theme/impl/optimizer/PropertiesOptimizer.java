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
package com.volantis.mcs.dom2theme.impl.optimizer;

import com.volantis.mcs.dom2theme.AssetResolver;
import com.volantis.mcs.dom2theme.ExtractorContext;
import com.volantis.mcs.dom2theme.impl.model.PseudoStylePath;
import com.volantis.mcs.dom2theme.impl.normalizer.BackgroundNormalizer;
import com.volantis.mcs.dom2theme.impl.normalizer.BackgroundResolver;
import com.volantis.mcs.dom2theme.impl.normalizer.BorderNormalizer;
import com.volantis.mcs.dom2theme.impl.normalizer.ListStyleResolver;
import com.volantis.mcs.dom2theme.impl.normalizer.MarqueeNormalizer;
import com.volantis.mcs.dom2theme.impl.normalizer.PropertiesNormalizer;
import com.volantis.mcs.dom2theme.impl.normalizer.TextAlignNormalizer;
import com.volantis.mcs.dom2theme.impl.optimizer.border.BorderOptimizer;
import com.volantis.mcs.dom2theme.impl.optimizer.font.FontOptimizer;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.ShorthandSet;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleShorthands;
import com.volantis.mcs.themes.StyleValues;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.styling.device.DeviceValues;
import com.volantis.styling.properties.ImmutableStylePropertySet;
import com.volantis.styling.properties.MutableStylePropertySet;
import com.volantis.styling.properties.PropertyDetailsSet;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * Optimise the properties.
 */
public class PropertiesOptimizer
        implements InputPropertiesOptimizer {

    /**
     * The object responsible for checking the status of individual properties.
     */
    private final PropertyClearerCheckerImpl propertyClearerChecker;

    /**
     * The set of objects that are responsible for resolving any policy
     * references within the properties to URLs.
     */
    private final PropertiesNormalizer[] resolvers;

    /**
     * The set of objects that are responsible for normalizing those properties
     * that are dependent upon one another.
     */
    private final PropertiesNormalizer[] normalizers;

    /**
     * The set of objects that are responsible for optimizing groups of
     * properties by making use of shorthands where possible.
     */
    private final ShorthandOptimizer[] optimizers;

    /**
     * The optimizer for all properties that are handled individually.
     */
    private final PropertyGroupOptimizer individualOptimizer;

    /**
     * The set of properties supported by the target device.
     *
     * <p>Any properties not in this set are ignored.</p>
     */
    private final ImmutableStylePropertySet supportedProperties;

    /**
     * The output values that have been created but not used yet.
     *
     * <p>If this is not already set this is initialised before optimizing the
     * properties. If afterwards if it is empty then it will be used again next
     * time, otherwise this field is cleared and the values returned.
     */
    private MutableStyleProperties savedOutputValues;

    /**
     * Initialise.
     *
     * @param detailsSet          Details for all the properties to be output.
     * @param context             The contextual information needed to optimize.
     * @param supportedShorthands The set of supported shorthands.
     */
    public PropertiesOptimizer(
            PropertyDetailsSet detailsSet,
            ExtractorContext context,
            ShorthandSet supportedShorthands) {

        propertyClearerChecker =
                new PropertyClearerCheckerImpl(detailsSet);

        AssetResolver assetResolver = context.getAssetResolver();

        supportedProperties = getSupportedProperties(detailsSet);

        resolvers = new PropertiesNormalizer[]{
            new BackgroundResolver(supportedProperties, assetResolver),
            new ListStyleResolver(supportedProperties, assetResolver),
        };

        normalizers = new PropertiesNormalizer[]{
            new BackgroundNormalizer(supportedProperties),
            new BorderNormalizer(supportedProperties),
            new TextAlignNormalizer(supportedProperties),
            new MarqueeNormalizer(supportedProperties),
        };

        optimizers = new ShorthandOptimizer[]{
            new HeterogeneousShorthandOptimizer(StyleShorthands.BACKGROUND,
                    propertyClearerChecker, supportedShorthands),

            new BorderOptimizer(propertyClearerChecker,
                    supportedShorthands),

            new EdgeShorthandOptimizer(StyleShorthands.MARGIN,
                    propertyClearerChecker, supportedShorthands),

            new EdgeShorthandOptimizer(StyleShorthands.PADDING,
                    propertyClearerChecker, supportedShorthands),

            new CornerShorthandOptimizer(StyleShorthands.MCS_BORDER_RADIUS,
                    propertyClearerChecker, supportedShorthands),
                                        
            new HeterogeneousShorthandOptimizer(StyleShorthands.MARQUEE,
                    propertyClearerChecker, supportedShorthands),

            new FontOptimizer(propertyClearerChecker, supportedShorthands),
        };

        // Remove all those properties that are handled by shorthand
        // optimizers from the set of individual properties.
        MutableStylePropertySet individualProperties =
                supportedProperties.createMutableStylePropertySet();
        for (int i = 0; i < optimizers.length; i++) {
            ShorthandOptimizer optimizer = optimizers[i];
            optimizer.removeProperties(individualProperties);
        }

        StyleProperty[] orderedPropertyArray =
                detailsSet.getOrderedPropertyArray(individualProperties);

        individualOptimizer = new IndividualPropertyOptimizer(
                propertyClearerChecker, orderedPropertyArray);
    }

    /**
     * Get the set of properties supported by the target device.
     *
     * @param detailsSet The details for the target device.
     * @return The set of supported properties.
     */
    private ImmutableStylePropertySet getSupportedProperties(
            PropertyDetailsSet detailsSet) {

        ImmutableStylePropertySet supportedProperties =
                detailsSet.getSupportedProperties();
        MutableStylePropertySet propertySet =
                supportedProperties.createMutableStylePropertySet();

        // CSS 2.1 only supports the content property for ::before and ::after.
        // CSS 3 provides additional support for most selectors but we only
        // support for li::marker from that additonal set.
        //
        // We currently assume that no browsers support content natively and do
        // emulation for the above usages of content well before this point.
        //
        // We assume that any content properties which still exist at this
        // point have been set on unsupported selectors, so we simply remove
        // them. This is required for consistency with CSS 2.1 but also because
        // any content properties which contain mcs-component-uri() will cause
        // MCS to crash during style sheet extraction.
        propertySet.remove(StylePropertyDetails.CONTENT);

        supportedProperties = propertySet.createImmutableStylePropertySet();
        return supportedProperties;
    }

    // Javadoc inherited.
    public MutableStyleProperties calculateOutputProperties(
            String elementName, PseudoStylePath pseudoPath,
            final MutablePropertyValues inputValues,
            StyleValues parentValues, DeviceValues deviceValues) {

        if (elementName == null) {
            throw new IllegalArgumentException("elementName cannot be null");
        }
        if (pseudoPath == null) {
            throw new IllegalArgumentException("pseudoPath cannot be null");
        }
        if (inputValues == null) {
            throw new IllegalArgumentException("inputValues cannot be null");
        }
        if (parentValues == null) {
            throw new IllegalArgumentException("parentValues cannot be null");
        }

        TargetEntity target;
        if (pseudoPath.containsPseudoClass()) {
            // The pseudo path contains pseudo classes so do not check for
            // defaults (e.g. inherit and initial), otherwise values may be
            // cleared. This is a problem because the styles associated with
            // pseudo classes are combined dynamically on the client depending
            // on the state of the element and hence the 'default' value may
            // be necessary in order to override values on other pseudo
            // classes.
            target = TargetEntity.PSEUDO_CLASS;
        } else {
            target = TargetEntity.ELEMENT;
        }

        // Resolve any policy references, this is done for elements, pseudo
        // elements and pseudo classes.
        for (int i = 0; i < resolvers.length; i++) {
            PropertiesNormalizer resolver = resolvers[i];
            resolver.normalize(inputValues);
        }

        // Only normalize the properties when there is no pseudo class present.
        // This is because while a property may have no effect on its own, it
        // may have an effect when combined with other pseudo classes. e.g.
        // :link {border-top-color: green}
        // :hover {border-top-width: thick}
        // :hover:link {border-top-style: solid}
        if (!pseudoPath.containsPseudoClass()) {
            // Normalize the output properties.
            for (int i = 0; i < normalizers.length; i++) {
                PropertiesNormalizer normalizer = normalizers[i];
                normalizer.normalize(inputValues);
            }
        }

        propertyClearerChecker.prepare(parentValues, target);

        // Create a new output properties if needed, or use the saved one.
        if (savedOutputValues == null) {
            savedOutputValues =
                ThemeFactory.getDefaultInstance().createMutableStyleProperties();
        }
        MutableStyleProperties outputValues = savedOutputValues;

        // Optimize the shorthand based properties.
        for (int i = 0; i < optimizers.length; i++) {
            ShorthandOptimizer optimizer = optimizers[i];
            optimizer.optimize(target, inputValues, outputValues,
                    deviceValues);
        }

        // Optimize the individual properties.
        individualOptimizer.optimize(target, inputValues, outputValues,
                deviceValues);

        // If there are no properties in the output then return null, otherwise
        // make sure that the saved values is not used again.
        if (outputValues.isEmpty()) {
            outputValues = null;
        } else {
            savedOutputValues = null;
        }

        return outputValues;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/2	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 18-Nov-05	10370/1	geoff	VBM:2005111405 MCS stability. Requesting pages over a 48 hour period lead to Errors

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 05-Aug-05	9153/1	ianw	VBM:2005072216 Fix style normalizer issue with pseudo paths

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 18-Jul-05	8668/5	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/

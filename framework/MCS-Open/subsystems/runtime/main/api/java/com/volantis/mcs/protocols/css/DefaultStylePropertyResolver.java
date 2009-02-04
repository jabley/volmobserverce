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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.css;

import com.volantis.mcs.context.TranscodableUrlResolver;
import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.AssetResolver;
import com.volantis.mcs.protocols.assets.implementation.DefaultComponentImageAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralImageAssetReference;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.themes.StyleComponentURI;
import com.volantis.mcs.themes.StyleInteger;
import com.volantis.mcs.themes.StyleString;
import com.volantis.mcs.themes.StyleTranscodableURI;
import com.volantis.mcs.themes.StyleURI;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.PropertyValues;

/**
 * Provides some methods to help manage style properties.
 *
 * <p>The main purpose of this class is to provide a single location for
 * methods that hide some nastinesses of the existing style implementation.</p>
 *
 * <h3>No Support For Initial Values</h3>
 *
 * <p>Every piece of code that gets a StyleValue from a StyleProperties that
 * has been created as part of the CSS emulation has to check for null before
 * it can do anything with it. If it is null then the code has to hard code the
 * initial value. The proper way to solve this is to build the initial values
 * for every property into the StyleProperties created by the CSS emulation.
 * This means that it will never return null and the code that is calling it
 * does not need to know the initial value.</p>
 *
 * <p>Unfortunately a simplistic approach to this will lead to the following
 * problems:</p>
 *
 * <dl>
 *
 * <dt>Increased page weight</dt>
 *
 * <dd><p>This is because many pieces of code assume that a null value
 * indicates that it is the device default and as such does not need to set the
 * attribute. By always providing an initial value this code will always write
 * out an attribute hence increasing the page weight.</p>
 * <p>The assumption on which this code is based is wrong because many devices
 * use default attribute values that do not match the protocol specification.
 * Therefore, the correct approach is to store what the device defaults are and
 * compare the resulting value against that. If they are the same then no
 * attribute is written. If done properly this could reduce the page weight in
 * the case that the author has specified an explicit value that matches the
 * device default.</p></dd>
 *
 * </dl>
 *
 * <h3>No Inheritance</h3>
 *
 * <p>The majority of stylistic properties only have a single value type,
 * e.g. keyword, or integer, apart from inherit. If inheritance was supported
 * by the CSS emulator properly then the inherit value would be replaced with
 * the value of the property from the enclosing element and so would never
 * appear in code that deals with StyleProperties created by CSS emulation.</p>
 * <p>A simplistic approach to this would have similar problems to those above
 * and would be addressed by the same solutions.</p>
 *
 * <h3>Hiding Issues</h3>
 *
 * <p>This class provides methods that serve two functions, the first is to hide
 * the above issues from the code using it and the second is to provide a
 * convenient way of identifying those areas that will need fixing in future
 * once the above issues have been resolved.</p>
 */
public class DefaultStylePropertyResolver
        implements StylePropertyResolver {

    private final PolicyReferenceResolver resolver;
    private final TranscodableUrlResolver transcodableUrlResolver;

    /**
     * Initialise.
     *
     * @param resolver
     */
    public DefaultStylePropertyResolver(PolicyReferenceResolver resolver,
            final TranscodableUrlResolver transcodableUrlResolver) {

        this.resolver = resolver;
        this.transcodableUrlResolver = transcodableUrlResolver;
    }

    // Javadoc inherited.
    public String getStringValue(
            PropertyValues properties,
            StyleProperty property) {

        StyleValue value = properties.getComputedValue(property);
        if (value == null) {
            throw new IllegalStateException(
                    "No value defined for " + property.getName());
        }

        return ((StyleString) value).getString();
    }

    // Javadoc inherited.
    public int getIntValue(
            PropertyValues properties,
            StyleProperty property) {

        StyleValue value = properties.getComputedValue(property);
        if (value == null) {
            throw new IllegalStateException(
                    "No value defined for " + property.getName());
        }

        return ((StyleInteger) value).getInteger();
    }

    // Javadoc inherited.
    public ImageAssetReference getImageAssetReferenceValue(
            PropertyValues properties, StyleProperty property,
            AssetResolver assetResolver) {

        StyleValue value = properties.getComputedValue(property);
        if (value == null) {
            throw new IllegalStateException(
                    "No value defined for " + property.getName());
        }

        final ImageAssetReference imageAssetReference;
        if (value instanceof StyleURI) {
            String uri = ((StyleURI) value).getURI();
            imageAssetReference = new LiteralImageAssetReference(uri);
        } else if (value instanceof StyleComponentURI) {
            StyleComponentURI styleComponentURI = (StyleComponentURI) value;

            // Resolve the identity against the project associated with the
            // value.
            RuntimePolicyReference reference =
                    resolver.resolvePolicyExpression(
                            styleComponentURI.getExpression());
            imageAssetReference = new DefaultComponentImageAssetReference(
                    reference, assetResolver);
        } else if (value instanceof StyleTranscodableURI) {
            final StyleTranscodableURI transcodableUri =
                (StyleTranscodableURI) value;
            try {
                // resolve the transcodable URL
                final String imageUrl = transcodableUrlResolver.resolve(
                    transcodableUri.getUri());

                return new ImageAssetReference() {
                    public String getURL() {
                        return imageUrl;
                    }
                    public TextAssetReference getTextFallback() {
                        return null;
                    }
                };
            } catch (RepositoryException e) {
                throw new ExtendedRuntimeException(e);
            }
        } else {
            throw new IllegalStateException("The value is of the wrong type (" +
                    value.getClass().getName() + ") for " +
                    property.getName());
        }

        return imageAssetReference;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 07-Jul-05	8967/1	pduffin	VBM:2005070702 Refactored resolving of expressions into component identities

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 09-Mar-05	7022/3	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 17-Feb-05	6957/2	geoff	VBM:2005021103 R821: Branding using Projects: Prerequisites: use current project in PAPI phase

 11-Feb-05	6931/2	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 21-Apr-04	3681/3	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 25-Mar-04	3550/2	pduffin	VBM:2004032306 Improved theme generation code, reducing the number of automatically generated classes and added support for initial value

 ===========================================================================
*/

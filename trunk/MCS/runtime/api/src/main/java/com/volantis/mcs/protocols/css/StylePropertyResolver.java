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

import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.protocols.assets.implementation.AssetResolver;
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
 *     <dt>Increased page weight</dt>
 *
 *     <dd><p>This is because many pieces of code assume that a null value
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
 *
 * @mock.generate 
 */
public interface StylePropertyResolver {

    /**
     * This method must only be called for those properties that only support
     * a string value in addition to inherit.
     *
     * <p>If either no value has been set, or it is inherit then this returns
     * the initial value which must of course be a StyleString.</p>
     *
     * @param properties The properties that contain the value that needs
     *                   retrieving.
     * @param property   The property whose value is requested.
     * @return The value of the specified property.
     */
    String getStringValue(PropertyValues properties,
            StyleProperty property);

    /**
     * This method must only be called for those properties that only support
     * an integer value in addition to inherit.
     *
     * <p>If either no value has been set, or it is inherit then this returns
     * the initial value which must of course be a StyleInteger.</p>
     *
     * @param properties The properties that contain the value that needs
     *                   retrieving.
     * @param property   The property whose value is requested.
     * @return The value of the specified property.
     */
    int getIntValue(PropertyValues properties,
            StyleProperty property);

    /**
     * This method must only be called for those properties that only support
     * a URI or a Component URI in addition to inherit.
     *
     * <p>If either no value has been set, or it is inherit that this returns
     * the initial value which must of course be a StyleURI or
     * StyleComponentURI.</p>
     *
     * @param properties The properties that contain the value that needs
     *                   retrieving.
     * @param property   The property whose value is requested.
     * @return The value of the specified property.
     */
    ImageAssetReference getImageAssetReferenceValue(
            PropertyValues properties, StyleProperty property,
            AssetResolver assetResolver);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 09-Mar-05	7022/1	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 ===========================================================================
*/

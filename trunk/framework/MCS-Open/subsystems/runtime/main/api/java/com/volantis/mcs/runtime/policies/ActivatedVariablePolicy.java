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

package com.volantis.mcs.runtime.policies;

import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.policies.variants.metadata.Encoding;
import com.volantis.mcs.policies.VariablePolicy;
import com.volantis.mcs.runtime.RuntimeProject;

import java.util.List;

/**
 * Wrapper around a {@link VariablePolicy} that provides some additional
 * runtime specific methods for performing efficient variant selection.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate base="VariablePolicy"
 */
public interface ActivatedVariablePolicy
        extends VariablePolicy, ActivatedPolicy {

    /**
     * Get the variant targeted at the specified device.
     *
     * @param deviceName The name of the device.
     * @return The variant targeted at the device, or null if there isn't one.
     */
    Variant getDeviceTargetedVariant(String deviceName);

    /**
     * Get the variant targeted at the specified category.
     *
     * @param categoryName The name of the category.
     * @return The variant targeted at the category, or null if there isn't one.
     */
    Variant getCategoryTargetedVariant(String categoryName);

    /**
     * Get the default variant.
     *
     * @return The default variant, or null if there isn't one.
     */
    Variant getDefaultVariant();

    /**
     * Get the variant with the specified encoding.
     *
     * @param encoding The encodig.
     * @return The variant with the specified encoding, or null if there isn't
     *         one.
     */
    Variant getVariantWithEncoding(Encoding encoding);

    /**
     * Get the list of generic image variants.
     *
     * @return The list of generic image variants, or null if there aren't any.
     */
    List getGenericImages();

    /**
     * Get the selected object.
     *
     * <p>This method must only be called if the calling thread has
     * synchronized on this object.</p>
     *
     * @param key The key to the selected object.
     *
     * @return The previously selected object, or null.
     */
    SelectedVariant getSelected(Object key);

    /**
     * Put the selected object with the specified key.
     *
     * <p>This method must only be called if the calling thread has
     * synchronized on this object.</p>
     *
     * @param key The key to the selected object.
     * @param object The selected object.
     */
    void putSelected(Object key, SelectedVariant object);

}

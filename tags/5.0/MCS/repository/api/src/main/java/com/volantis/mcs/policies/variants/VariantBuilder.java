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
package com.volantis.mcs.policies.variants;

import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.variants.content.ContentBuilder;
import com.volantis.mcs.policies.variants.metadata.MetaDataBuilder;
import com.volantis.mcs.policies.variants.selection.SelectionBuilder;

/**
 * Builder of {@link Variant} instances.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with current and future releases of the
 * product at binary and source levels.</strong>
 * </p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @see Variant
 * @see SelectionBuilder
 * @see MetaDataBuilder
 * @see ContentBuilder
 * @see PolicyFactory#createVariantBuilder(VariantType)
 * @since 3.5.1
 * @mock.generate 
 */
public interface VariantBuilder {

    /**
     * Get the built {@link Variant}.
     *
     * <p>Returns a newly created instance the first time it is called and
     * if the state has changed since the last time this method was called,
     * otherwise it returns the same instance as the previous call.</p>
     *
     * @return The built {@link Variant}.
     */
    Variant getVariant();

    /**
     * Setter for the <a href="Variant.html#variantType">variant type</a> property.
     *
     * @param variantType New value of the
     *                    <a href="Variant.html#variantType">variant type</a> property.
     */
    void setVariantType(VariantType variantType);

    /**
     * Getter for the <a href="Variant.html#variantType">variant type</a> property.
     *
     * @return Value of the <a href="Variant.html#variantType">variant type</a>
     *         property.
     */
    VariantType getVariantType();

    /**
     * Setter for the builder of the
     * <a href="Variant.html#selection">selection</a> property.
     *
     * @param selectionBuilder New builder of the
     *                         <a href="Variant.html#selection">selection</a>
     *                         property.
     */
    void setSelectionBuilder(SelectionBuilder selectionBuilder);

    /**
     * Getter for the builder of the
     * <a href="Variant.html#selection">selection</a> property.
     *
     * @return Builder of the <a href="Variant.html#selection">selection</a>
     *         property.
     */
    SelectionBuilder getSelectionBuilder();

    /**
     * Setter for the builder of the
     * <a href="Variant.html#metaData">meta data</a> property.
     *
     * @param metaDataBuilder New builder of the
     *                        <a href="Variant.html#metaData">meta data</a>
     *                        property.
     */
    void setMetaDataBuilder(MetaDataBuilder metaDataBuilder);

    /**
     * Getter for the builder of the
     * <a href="Variant.html#metaData">meta data</a> property.
     *
     * @return Builder of the <a href="Variant.html#metaData">meta data</a>
     *         property.
     */
    MetaDataBuilder getMetaDataBuilder();

    /**
     * Setter for the builder of the
     * <a href="Variant.html#content">content</a> property.
     *
     * @param contentBuilder New builder of the
     *                       <a href="Variant.html#content">content</a>
     *                       property.
     */
    void setContentBuilder(ContentBuilder contentBuilder);

    /**
     * Getter for the builder of the
     * <a href="Variant.html#content">content</a> property.
     *
     * @return Builder of the <a href="Variant.html#content">content</a>
     *         property.
     */
    ContentBuilder getContentBuilder();
}

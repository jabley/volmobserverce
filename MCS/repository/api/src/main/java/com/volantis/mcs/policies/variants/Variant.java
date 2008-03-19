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

import com.volantis.mcs.policies.VariablePolicy;
import com.volantis.mcs.policies.variants.content.Content;
import com.volantis.mcs.policies.variants.content.EmbeddedContent;
import com.volantis.mcs.policies.variants.metadata.MetaData;
import com.volantis.mcs.policies.variants.selection.Selection;

/**
 * A device specific variant of the {@link VariablePolicy}.
 *
 * <p>A variant is an abstract representation of a resource. In many cases the
 * resource is real, e.g. a real image, and in other cases it is just notional
 * and the contents of the resource are {@link EmbeddedContent} in the
 * variant itself.</p>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with current and future releases of the
 * product at binary and source levels.</strong>
 * </p>
 *
 *
 * <table border="1" cellpadding="3" cellspacing="0" width="100%">
 *
 * <tr bgcolor="#ccccff" class="TableHeadingColor">
 * <td colspan="2"><font size="+2">
 * <b>Property Summary</b></font></td>
 * </tr>
 *
 * <tr id="variantType">
 * <td align="right" valign="top" width="1%"><b>variant&nbsp;type</b></td>
 * <td>the type of the variant. This determines which {@link Selection},
 * {@link MetaData} and {@link Content} specialisations are valid.</td>
 * </tr>
 *
 * <tr id="selection">
 * <td align="right" valign="top" width="1%"><b>selection</b></td>
 * <td>the selection criteria for the variant.</td>
 * </tr>
 *
 * <tr id="metaData">
 * <td align="right" valign="top" width="1%"><b>meta&nbsp;data</b></td>
 * <td>meta data about the variant.</td>
 * </tr>
 *
 * <tr id="content">
 * <td align="right" valign="top" width="1%"><b>content</b></td>
 * <td>the content of the variant.</td>
 * </tr>
 *
 * </table>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @mock.generate
 * @see VariantBuilder
 * @see VariantType
 * @see Selection
 * @see MetaData
 * @see Content
 * @since 3.5.1
 */
public interface Variant {

    /**
     * Get a new builder instance for {@link Variant}.
     *
     * <p>The returned builder has been initialised with the values of this
     * object and will return this object from its
     * {@link VariantBuilder#getVariant()} until its state is
     * changed.</p>
     *
     * @return A new builder instance.
     */
    VariantBuilder getVariantBuilder();

    /**
     * Getter for the <a href="#variantType">variant type</a> property.
     *
     * @return Value of the <a href="#variantType">variant type</a>
     *         property.
     */
    VariantType getVariantType();

    /**
     * Getter for the <a href="#selection">selection</a> property.
     *
     * @return Value of the <a href="#selection">selection</a>
     *         property.
     */
    Selection getSelection();

    /**
     * Getter for the <a href="#metaData">meta data</a> property.
     *
     * @return Value of the <a href="#metaData">meta data</a>
     *         property.
     */
    MetaData getMetaData();

    /**
     * Getter for the <a href="#content">content</a> property.
     *
     * @return Value of the <a href="#content">content</a>
     *         property.
     */
    Content getContent();

}

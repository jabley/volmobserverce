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

import com.volantis.mcs.model.validation.Validatable;

/**
 * Internal API to a {@link VariantBuilder}.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * <table border="1" cellpadding="3" cellspacing="0" width="100%">
 *
 * <tr bgcolor="#ccccff" class="TableHeadingColor">
 * <td colspan="2"><font size="+2">
 * <b>Property Summary</b></font></td>
 * </tr>
 *
 * <tr id="variantIdentifier">
 * <td align="right" valign="top" width="1%"><b>variant identifier</b></td>
 * <td>A field that uniquely identifies the variant within the containing
 * policy. This identifier is not copied into the {@link Variant} constructed
 * from this builder as it is only used within the collaboration framework
 * of the Eclipse user interface and that never uses {@link Variant Variants}.
 * </td>
 * </tr>
 *
 * </table>
 *
 * @mock.generate
 */
public interface InternalVariantBuilder
        extends VariantBuilder, Validatable {

    /**
     * Getter for the <a href="#variantIdentifier">variant identifier</a>
     * property.
     * @return Value of the <a href="#variantIdentifier">variant identifier</a>
     * property.
     */
    String getVariantIdentifier();

    /**
     * Setter for the <a href="#variantIdentifier">variant identifier</a>
     * property.
     *
     * @param variantIdentifier New value of the
     *                          <a href="#variantIdentifier">variant
     */
    void setVariantIdentifier(String variantIdentifier);
}

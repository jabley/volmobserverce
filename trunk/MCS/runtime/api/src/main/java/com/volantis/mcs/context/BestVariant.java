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

package com.volantis.mcs.context;

import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.VariablePolicy;
import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.policies.variants.metadata.Encoding;

/**
 * Encapsulates information about the best variant.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with current and future releases of the
 * product at binary and source levels.</strong>
 * </p>
 *
 * <table border="1" cellpadding="3" cellspacing="0" width="100%">
 *
 * <tr bgcolor="#ccccff" class="TableHeadingColor">
 * <td colspan="2"><font size="+2">
 * <b>Property Summary</b></font></td>
 * </tr>
 *
 * <tr id="variablePolicy">
 * <td align="right" valign="top" width="1%"><b>variable&nbsp;policy</b></td>
 * <td>the {@link VariablePolicy} that contained the {@link Variant}, or the
 * referenced policy if no acceptable {@link Variant} could be found.</td>
 * </tr>
 *
 * <tr id="variant">
 * <td align="right" valign="top" width="1%"><b>variant</b></td>
 * <td>the best {@link Variant} for the current device, or null if no
 * acceptable {@link Variant} could be found.</td>
 * </tr>
 *
 * </table>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @see MarinerRequestContext#selectBestVariant(PolicyReference, Encoding)
 * @since 3.5.1
 */
public interface BestVariant {

    /**
     * Getter for the <a href="#variablePolicy">variable policy</a> property.
     *
     * @return Value of the <a href="#variablePolicy">variable policy</a>
     *         property.
     */
    VariablePolicy getVariablePolicy();

    /**
     * Getter for the <a href="#variant">variant</a> property.
     *
     * @return Value of the <a href="#variant">variant</a>
     *         property.
     */
    Variant getVariant();
}

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
package com.volantis.mcs.policies;

import com.volantis.mcs.policies.variants.Variant;

import java.util.Iterator;
import java.util.List;

/**
 * A variable policy is a policy that can contain device dependent variants.
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
 * <tr id="categorizationScheme">
 * <td align="right" valign="top" width="1%"><b>categorization&nbsp;schema</b></td>
 * <td>the optional name of a device policy that categorizes devices according
 * to some criteria. This must be specified if any of the
 * {@link Variant variants} are targeted at a specific category.</td>
 * </tr>
 *
 * <tr id="variants">
 * <td align="right" valign="top" width="1%"><b>variants</b></td>
 * <td>the unmodifiable list of {@link Variant} instances.</td>
 * </tr>
 *
 * </table>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @mock.generate base="ConcretePolicy"
 * @see VariablePolicyBuilder
 * @see Variant
 * @since 3.5.1
 * @mock.generate
 */
public interface VariablePolicy
        extends ConcretePolicy {

    /**
     * Get a new builder instance for {@link VariablePolicy}.
     *
     * <p>The returned builder has been initialised with the values of this
     * object and will return this object from its
     * {@link VariablePolicyBuilder#getVariablePolicy()} until its state is
     * changed.</p>
     *
     * @return A new builder instance.
     */
    VariablePolicyBuilder getVariablePolicyBuilder();

    /**
     * Get the type of variable policy.
     *
     * <p>The type of policy determines the variants that can be contained
     * within it. See {@link VariablePolicyType} for more details.</p>
     *
     * @return The type of variable policy.
     */
    VariablePolicyType getVariablePolicyType();

    /**
     * Getter for the <a href="#categorizationScheme">categorization schema</a>
     * property.
     *
     * @return Value of the
     *         <a href="#categorizationScheme">categorization schema</a>
     *         property.
     */
    String getCategorizationScheme();

    /**
     * Getter for the <a href="#variants">variants</a> property.
     *
     * @return Value of the <a href="#variants">variants</a>
     *         property.
     */
    List getVariants();

    /**
     * Get an iterator over the contents of the <a href="#variants">variants</a>
     * property.
     *
     * @return An iterator over the contents of the
     *         <a href="#variants">variants</a> property.
     */
    Iterator variantIterator();
}

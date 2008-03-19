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

import com.volantis.mcs.policies.variants.VariantBuilder;

import java.util.Iterator;
import java.util.List;

/**
 * Builder of {@link VariablePolicy} instances.
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
 * <tr id="variantBuilders">
 * <td align="right" valign="top" width="1%"><b>variant&nbsp;builders</b></td>
 * <td>the modifiable list of {@link VariantBuilder} objects that are used to
 * create the <a href="VariablePolicy.html#variants">variants</a>
 * property.</td>
 * </tr>
 *
 * </table>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @see VariablePolicy
 * @see VariantBuilder
 * @see PolicyFactory#createVariablePolicyBuilder(VariablePolicyType)
 * @since 3.5.1
 * @mock.generate
 */
public interface VariablePolicyBuilder
        extends ConcretePolicyBuilder {

    /**
     * Get the built {@link VariablePolicy}.
     *
     * <p>Returns a newly created instance the first time it is called and
     * if the state has changed since the last time this method was called,
     * otherwise it returns the same instance as the previous call.</p>
     *
     * @return The built {@link VariablePolicy}.
     */
    VariablePolicy getVariablePolicy();

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
     * Set the type of variable policy.
     *
     * @param policyType The type of variable policy.
     */
    void setVariablePolicyType(VariablePolicyType policyType);

    /**
     * Setter for the <a href="VariablePolicy.html#categorizationScheme">categorization schema</a> property.
     *
     * @param categorizationScheme New value of the
     *                             <a href="VariablePolicy.html#categorizationScheme">categorization schema</a> property.
     */
    void setCategorizationScheme(String categorizationScheme);

    /**
     * Getter for the <a href="VariablePolicy.html#categorizationScheme">categorization schema</a> property.
     *
     * @return Value of the <a href="VariablePolicy.html#categorizationScheme">categorization schema</a>
     *         property.
     */
    String getCategorizationScheme();

    /**
     * Add a builder to the end of the
     * <a href="#variantBuilders">variant builders</a> property.
     *
     * @param variantBuilder The {@link VariantBuilder} to add.
     */
    void addVariantBuilder(VariantBuilder variantBuilder);

    /**
     * Getter for the <a href="#variantBuilders">variant builders</a> property.
     *
     * @return Value of the <a href="#variantBuilders">variant builders</a>
     *         property.
     */
    List getVariantBuilders();

    /**
     * Get an iterator over the contents of the
     * <a href="#variantBuilders">variant builders</a> property.
     *
     * @return An iterator over the contents of the
     *         <a href="#variantBuilders">variant builders</a> property.
     */
    Iterator variantBuilderIterator();
}

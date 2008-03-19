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

/**
 * The RolloverImage component represents a set of two images used to
 * implement an image rollover.
 *
 * <p>One image forms the state of the button when the pointer is not over it
 * while the second forms the state when the pointer is over the rollover.</p>
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
 * <tr id="normalPolicy">
 * <td align="right" valign="top" width="1%"><b>normal&nbsp;policy</b></td>
 * <td>the image that is used when the mouse is not over the rollover.</td>
 * </tr>
 *
 * <tr id="overPolicy">
 * <td align="right" valign="top" width="1%"><b>over&nbsp;policy</b></td>
 * <td>the image that is used when the mouse is over the rollover.</td>
 * </tr>
 *
 * </table>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @mock.generate base="ConcretePolicy"
 * @see RolloverImagePolicyBuilder
 * @since 3.5.1
 */
public interface RolloverImagePolicy
        extends ConcretePolicy {

    /**
     * Get a new builder instance for {@link RolloverImagePolicy}.
     *
     * <p>The returned builder has been initialised with the values of this
     * object and will return this object from its
     * {@link RolloverImagePolicyBuilder#getRolloverImagePolicy()} until its state is
     * changed.</p>
     *
     * @return A new builder instance.
     */
    RolloverImagePolicyBuilder getRolloverImagePolicyBuilder();

    /**
     * Getter for the <a href="#normalPolicy">normal policy</a> property.
     * @return Value of the <a href="#normalPolicy">normal policy</a>
     * property.
     */
    PolicyReference getNormalPolicy();

    /**
     * Getter for the <a href="#overPolicy">over policy</a> property.
     * @return Value of the <a href="#overPolicy">over policy</a>
     * property.
     */
    PolicyReference getOverPolicy();
}

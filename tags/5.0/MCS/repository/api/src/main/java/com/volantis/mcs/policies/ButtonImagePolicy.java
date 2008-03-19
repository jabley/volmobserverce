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
 * Represents a set of three images used to represent a button.
 *
 * <p>Consists of three images, up, down and over of which only one is
 * displayed at a time depending on the state of the 'button'. The up image is
 * displayed when the button is in its up position. The over image is displayed
 * when the mouse moves over the button. The down image is displayed when the
 * mouse button is clicked (or it is activated in some other way).</p>
 *
 * <p>Instances of this type can be constructed using a
 * {@link ButtonImagePolicyBuilder}.</p>
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
 * <tr id="upPolicy">
 * <td align="right" valign="top" width="1%"><b>up&nbsp;policy</b></td>
 * <td>the image that is used when the button is in its up state.</td>
 * </tr>
 *
 * <tr id="downPolicy">
 * <td align="right" valign="top" width="1%"><b>down&nbsp;policy</b></td>
 * <td>the image that is used when the button is in the down state.</td>
 * </tr>
 *
 * <tr id="overPolicy">
 * <td align="right" valign="top" width="1%"><b>over&nbsp;policy</b></td>
 * <td>the image that is used when the mouse is over the button.</td>
 * </tr>
 *
 * </table>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @see ButtonImagePolicyBuilder
 * @since 3.5.1
 */
public interface ButtonImagePolicy
        extends ConcretePolicy {

    /**
     * Get a new builder instance for {@link ButtonImagePolicy}.
     *
     * <p>The returned builder has been initialised with the values of this
     * object and will return this object from its
     * {@link ButtonImagePolicyBuilder#getButtonImagePolicy()} until its state is
     * changed.</p>
     *
     * @return A new builder instance.
     */
    ButtonImagePolicyBuilder getButtonImagePolicyBuilder();

    /**
     * Getter for the <a href="#upPolicy">up policy</a> property.
     * @return Value of the <a href="#upPolicy">up policy</a>
     * property.
     */
    PolicyReference getUpPolicy();

    /**
     * Getter for the <a href="#downPolicy">down policy</a> property.
     * @return Value of the <a href="#downPolicy">down policy</a>
     * property.
     */
    PolicyReference getDownPolicy();

    /**
     * Getter for the <a href="#overPolicy">over policy</a> property.
     * @return Value of the <a href="#overPolicy">over policy</a>
     * property.
     */
    PolicyReference getOverPolicy();
}

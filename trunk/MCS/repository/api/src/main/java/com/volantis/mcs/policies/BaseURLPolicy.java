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

import com.volantis.mcs.policies.variants.content.BaseLocation;
import com.volantis.mcs.policies.variants.content.BaseURLRelative;

/**
 * A policy that provides a base URL for references to resources.
 *
 * <p>Instances of this type can be constructed using a
 * {@link BaseURLPolicyBuilder}.</p>
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
 * <tr id="baseURL">
 * <td align="right" valign="top" width="1%"><b>base&nbsp;URL</b></td>
 * <td>the base URL against which any related resource URLs are resolved.</td>
 * </tr>
 *
 * <tr id="baseLocation">
 * <td align="right" valign="top" width="1%"><b>base&nbsp;location</b></td>
 * <td>the base location of the resources, may be on the servlet, or a device.</td>
 * </tr>
 *
 * </table>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @see BaseURLPolicyBuilder
 * @see BaseURLRelative
 * @since 3.5.1
 */
public interface BaseURLPolicy
        extends Policy {

    /**
     * Get a new builder instance for {@link BaseURLPolicy}.
     *
     * <p>The returned builder has been initialised with the values of this
     * object and will return this object from its
     * {@link BaseURLPolicyBuilder#getBaseURLPolicy()} until its state is
     * changed.</p>
     *
     * @return A new builder instance.
     */
    BaseURLPolicyBuilder getBaseURLPolicyBuilder();

    /**
     * Getter for the <a href="#baseURL">base URL</a> property.
     *
     * @return Value of the <a href="#baseURL">base URL</a>
     *         property.
     */
    String getBaseURL();

    /**
     * Getter for the <a href="#baseLocation">base location</a> property.
     *
     * @return Value of the <a href="#baseLocation">base location</a>
     *         property.
     */
    BaseLocation getBaseLocation();

}

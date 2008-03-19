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

package com.volantis.mcs.policies.variants.content;

import com.volantis.mcs.policies.PolicyReference;

/**
 * Base for builders of {@link BaseURLRelative} instances.
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
 * @see BaseURLRelative
 * @since 3.5.1
 */
public interface BaseURLRelativeBuilder {

    /**
     * Getter for the
     * <a href="BaseURLRelative.html#baseURLPolicyReference">base URL policy
     * reference</a> property.
     *
     * @return Value of the
     *         <a href="BaseURLRelative.html#baseURLPolicyReference">base URL
     *         policy reference</a> property.
     */
    PolicyReference getBaseURLPolicyReference();

    /**
     * Setter for the
     * <a href="BaseURLRelative.html#baseURLPolicyReference">base URL policy
     * reference</a> property.
     *
     * @param baseURLPolicyReference New value of the
     *                               <a href="BaseURLRelative.html#baseURLPolicyReference">base URL policy reference</a>
     *                               property.
     */
    void setBaseURLPolicyReference(PolicyReference baseURLPolicyReference);

    /**
     * Getter for the
     * <a href="BaseURLRelative.html#baseLocation">base location</a> property.
     *
     * @return Value of the
     *         <a href="BaseURLRelative.html#baseLocation">base location</a>
     *         property.
     */
    BaseLocation getBaseLocation();

    /**
     * Setter for the
     * <a href="BaseURLRelative.html#baseLocation">base location</a> property.
     *
     * @param baseLocation New value of the
     *                     <a href="BaseURLRelative.html#baseLocation">base
     *                     location</a> property.
     */
    void setBaseLocation(BaseLocation baseLocation);
}

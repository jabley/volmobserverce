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
 * Base for all policy builders.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @mock.generate
 * @see Policy
 * @see CacheControlBuilder
 * @since 3.5.1
 */
public interface PolicyBuilder {

    /**
     * Get the built {@link Policy}.
     *
     * <p>Returns a newly created instance the first time it is called and
     * if the state has changed since the last time this method was called,
     * otherwise it returns the same instance as the previous call.</p>
     *
     * @return The built {@link Policy}.
     */
    Policy getPolicy();

    /**
     * Get the policy type.
     *
     * @return The policy typ.
     */
    PolicyType getPolicyType();

    /**
     * Setter for the <a href="Policy.html#name">name</a> property.
     *
     * @param name New value of the
     *             <a href="Policy.html#name">name</a> property.
     */
    void setName(String name);

    /**
     * Getter for the <a href="Policy.html#name">name</a> property.
     *
     * @return Builder of the <a href="Policy.html#name">name</a>
     *         property.
     */
    String getName();

    /**
     * Setter for the builder of the
     * <a href="Policy.html#cacheControl">cache control</a> property.
     *
     * @param cacheControl New builder of the
     *                     <a href="Policy.html#cacheControl">cache control</a>
     *                     property.
     */
    void setCacheControlBuilder(CacheControlBuilder cacheControl);

    /**
     * Getter for the builder of the
     * <a href="Policy.html#cacheControl">cache control</a> property.
     *
     * @return Value of the <a href="Policy.html#cacheControl">cache control</a>
     *         property.
     */
    CacheControlBuilder getCacheControlBuilder();
}

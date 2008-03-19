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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.shared.dependency;

import com.volantis.shared.time.Period;

/**
 * Represents a dependency on an original source.
 *
 * <p>Implementations of this can be stateful but must be thread safe. They
 * must also satisfy the constraints placed on elements in a
 * {@link java.util.Set} and also on keys added to a
 * {@link java.util.Map}.</p>
 *
 * <p>
 * <strong>Warning: This is intended to be implemented by user code but in
 * order to ensure that enhancements can be made to this interface without
 * breaking existing code it is recommended that users extend the
 * {@link DependencyImpl} class rather than implementing this
 * directly. Direct implementations of this interface are highly likely to be
 * incompatible with future releases of the product at both binary and source
 * levels. </strong>
 * </p>
 *
 * @mock.generate
 * @see com.volantis.shared.dependency
 * @see DependencyImpl
 */
public interface Dependency {

    /**
     * Determines whether the dependency is cacheable.
     *
     * @return An instance of {@link Cacheability}, must not be null.
     */
    Cacheability getCacheability();

    /**
     * Get the maximum time to live for the dependency.
     *
     * @return The maximum time to live.
     */
    Period getTimeToLive();

    /**
     * Determine the freshness of the dependency.
     *
     * <p><strong>Note:</strong> This method must not be called directly via
     * user code, it is intended solely for use by the
     * {@link DependencyContext#checkValidity(Dependency)} method to
     * check the validity of a depedency.</p>
     *
     * <p>This is intended to be a very quick operation that can be performed
     * without attempting to access external resources. If it is not possible
     * to determine the freshness within this method then it must return
     * {@link Freshness#REVALIDATE}. In that case the
     * {@link #revalidate(DependencyContext)} method will be called unless
     * another dependency indicates that it is {@link Freshness#STALE}.</p>
     *
     * @param context The context within which this dependency is being
     *                checked.
     * @return The freshness of the dependency, may not be null.
     */
    Freshness freshness(DependencyContext context);

    /**
     * Revalidate the dependency.
     *
     * <p><strong>Note:</strong> This method must not be called directly via
     * user code, it is intended solely for use by the
     * {@link DependencyContext#checkValidity(Dependency)} method to
     * check the validity of a depedency.</p>
     *
     * <p>This method will only be called if a previous call to
     * {@link #freshness(DependencyContext)} returned
     * {@link Freshness#REVALIDATE}.</p>
     *
     * @param context The context within which this dependency is being
     *                checked.
     * @return The freshness of the dependency, may not be null. A return value
     *         of {@link Freshness#REVALIDATE} will be treated as
     *         {@link Freshness#STALE}.
     */
    Freshness revalidate(DependencyContext context);

}

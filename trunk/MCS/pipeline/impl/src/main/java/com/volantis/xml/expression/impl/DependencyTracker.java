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

package com.volantis.xml.expression.impl;

import com.volantis.shared.dependency.Dependency;

/**
 * Keeps track of dependencies.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @see com.volantis.shared.dependency.DependencyContext
 */
public interface DependencyTracker {

    /**
     * Indicates whether the tracker is tracking dependencies or not.
     *
     * @return True if it is, false otherwise.
     */
    boolean isTracking();

    /**
     * Add the dependency to the tracker.
     *
     * @param dependency The dependency.
     */
    void addDependency(Dependency dependency);

    /**
     * Clear the dependencies.
     */
    void clearDependencies();

    /**
     * Extract the dependencies as a single possible composite dependency.
     *
     * <p>A side effect of this call is to remove all the dependencies from the
     * tracker.</p>
     *
     * @return The dependency.
     */
    Dependency extractDependency();

    /**
     * Create a nested tracker.
     *
     * @return The nested tracker.
     */
    DependencyTracker createNestedTracker();

    /**
     * Propagate the dependencies to the containing tracker.
     *
     * <p>A side effect of this call is to remove all the dependencies from the
     * tracker.</p>
     */
    void propagateDependencies();
}

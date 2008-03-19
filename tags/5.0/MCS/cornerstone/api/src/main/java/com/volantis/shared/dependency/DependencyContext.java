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

/**
 * Provides contextual information for managing
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate
 */
public interface DependencyContext {

    /**
     * Sets the value of a custom property.
     *
     * @param key             The key that identifies the property.
     * @param value           The value of the property.
     */
    public void setProperty(Object key, Object value);

    /**
     * Gets the value of a custom property.
     * @param key The key that identifies the property.
     * @return The value of the property, or null if the property has not been
     *         set.
     */
    public Object getProperty(Object key);

    /**
     * Remove the property that has the specified key. <p>This is equivalent to
     * calling <code>setProperty</code> with the same key and a null
     * value.</p>
     * @param key The key that identifies the property.
     * @return The value of the property, of null if the property has not been
     *         set.
     */
    public Object removeProperty(Object key);

    /**
     * Push a newly created dependency tracker.
     * @param tracking Indicates how the created tracker will behave.
     */
    void pushDependencyTracker(Tracking tracking);

    /**
     * Add a dependency to the tracker.
     * @param dependency The dependency to add.
     */
    void addDependency(Dependency dependency);

    /**
     * Indicates whether the current dependency tracker is tracking
     * dependencies.
     *
     * <p>This method should be used in order to determine whether it is
     * necessary to create a {@link Dependency} or not.</p>
     *
     * @return True if the tracker is tracking dependencies and false otherwise.
     */
    boolean isTrackingDependencies();

    /**
     * Propagate the dependencies of the current tracker to the parent.
     */
    void propagateDependencies();

    /**
     * Clear the dependencies of the current tracker.
     */
    void clearDependencies();

    /**
     * Pop the current tracker and discard any dependencies.
     */
    void popDependencyTracker();

    /**
     * Extract the dependencies from the current tracker as a single possibly
     * composite dependency.
     *
     * <p>A side effect of this call is to remove all the dependencies from the
     * tracker.</p>
     *
     * @return The dependency.
     */
    Dependency extractDependency();

    /**
     * Check to see whether the dependency is valid.
     *
     * <p>Calls to {@link #checkFreshness(Dependency)} must only be made while
     * a call to this is in progress, e.g. during a call to
     * {@link Dependency#freshness(DependencyContext)} that was made by this
     * method.</p>
     *
     * @param dependency The dependency to check.
     * @return An instance of {@link Validity}.
     */
    Validity checkValidity(Dependency dependency);

    /**
     * Check the freshness of the dependency by calling
     * {@link Dependency#freshness(DependencyContext)}
     *
     * <p>If the freshness of the dependency has already been checked during
     * the current call to {@link #checkValidity(Dependency)} then the same
     * value will be returned as previously and the
     * {@link Dependency#freshness(DependencyContext)} method will not be called.
     *
     * @param dependency The dependency.
     * @return The freshness.
     */
    Freshness checkFreshness(Dependency dependency);
}

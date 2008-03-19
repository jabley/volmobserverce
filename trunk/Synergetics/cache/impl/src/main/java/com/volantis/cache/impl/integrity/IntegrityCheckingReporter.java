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

package com.volantis.cache.impl.integrity;

/**
 * Reports issues relating to the cache integrity.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface IntegrityCheckingReporter {

    /**
     * Begin checking an object.
     *
     * @param name A descriptive name of the object.
     */
    void beginChecking(String name);

    /**
     * Report an issue that was discovered during the integrity check.
     *
     * @param issue A description of the issue.
     */
    void reportIssue(String issue);

    /**
     * Begin checking an object.
     *
     * @param name A descriptive name of the object, must be the same as that
     *             supplied on the match {@link #beginChecking(String)}
     *             method call.
     */
    void endChecking(String name);
}

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

package com.volantis.shared.net.impl.url;

import com.volantis.shared.dependency.Cacheability;
import com.volantis.shared.dependency.Dependency;
import com.volantis.shared.dependency.DependencyContext;
import com.volantis.shared.dependency.Freshness;
import com.volantis.shared.net.url.URLContent;
import com.volantis.shared.net.url.URLContentImpl;
import com.volantis.shared.time.Period;

import java.net.URL;

/**
 * Base class for all {@link URLContent} implementations.
 */
public abstract class AbstractURLContent
        extends URLContentImpl {

    /**
     * The URL whose content this represents.
     */
    private final URL url;
    
    /**
     * Initialise.
     *
     * @param url The URL whose content this represents.
     */
    public AbstractURLContent(final URL url) {
        if (url == null) {
            throw new IllegalArgumentException("url cannot be null");
        }
        this.url = url;
    }

    // Javadoc inherited.
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode()) +
                " [" + url.toExternalForm() + "]";
    }

    /**
     * Returns true, iff the content is still the same as it was when the
     * content object was created.
     *
     * <p>Implementations should err on the safe side and return false if they
     * are not sure that the content is fresh.</p>
     *
     * @return true iff the content is still fresh
     */
    public abstract boolean isFresh();

    // javadoc inherited
    public Dependency getDependency() {
        return new Dependency() {

            // javadoc inherited
            public Cacheability getCacheability() {
                return Cacheability.CACHEABLE;
            }

            // javadoc inherited
            public Period getTimeToLive() {
                return Period.ZERO;
            }

            // javadoc inherited
            public Freshness freshness(final DependencyContext context) {
                return isFresh()? Freshness.FRESH : Freshness.STALE;
            }

            // javadoc inherited
            public Freshness revalidate(DependencyContext context) {
                return Freshness.STALE;
            }
        };
    }
   
}

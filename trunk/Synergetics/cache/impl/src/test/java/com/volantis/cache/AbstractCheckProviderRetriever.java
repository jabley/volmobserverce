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

package com.volantis.cache;

import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import com.volantis.cache.provider.CacheableObjectProvider;
import junit.framework.Assert;

/**
 * Base class for actions used in expectations of the
 *  {@link CacheableObjectProvider#retrieve} method.
 */
public class AbstractCheckProviderRetriever
        implements MethodAction {

    /**
     * Indicates whether the provider expects to be given an entry.
     */
    private final boolean hasEntry;

    /**
     * The expected key.
     */
    private final Object expectedKey;

    /**
     * The expected value.
     */
    private final Object expectedValue;

    /**
     * Initialise.
     *
     * @param expectedKey   The expected key in the entry.
     * @param expectedValue The expected value in the entry.
     */
    protected AbstractCheckProviderRetriever(
            Object expectedKey, Object expectedValue) {

        this.hasEntry = true;
        this.expectedKey = expectedKey;
        this.expectedValue = expectedValue;
    }

    /**
     * Initialise.
     *
     * <p>Does not expect an entry.</p>
     */
    public AbstractCheckProviderRetriever() {
        this.hasEntry = false;
        this.expectedKey = null;
        this.expectedValue = null;
    }

    // Javadoc inherited.
    public Object perform(MethodActionEvent event)
            throws Throwable {

        CacheEntry entry = (CacheEntry)
                event.getArgument(CacheEntry.class);
        if (hasEntry) {
            Assert.assertSame(expectedKey, entry.getKey());
            Assert.assertSame(expectedValue, entry.getValue());
        } else {
            Assert.assertNull(entry);
        }

        return null;
    }
}

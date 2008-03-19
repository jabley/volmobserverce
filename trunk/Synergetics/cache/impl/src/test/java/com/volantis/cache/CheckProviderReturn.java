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

import com.volantis.cache.provider.ProviderResult;
import com.volantis.testtools.mock.method.MethodActionEvent;

/**
 * An action that returns a supplied result after checking to make sure that
 * the state of the entry supplied matches what is expected.
 */
public class CheckProviderReturn
        extends AbstractCheckProviderRetriever {

    /**
     * The result to return.
     */
    private final ProviderResult result;

    /**
     * Initialise.
     *
     * @param expectedKey   The expected key in the entry.
     * @param expectedValue The expected value in the entry.
     * @param result        The result to return.
     */
    public CheckProviderReturn(
            final String expectedKey, final Object expectedValue,
            final ProviderResult result) {

        super(expectedKey, expectedValue);

        this.result = result;
    }

    /**
     * Initialise.
     *
     * <p>Does not expect an entry.</p>
     *
     * @param result The result to return.
     */
    public CheckProviderReturn(ProviderResult result) {
        this.result = result;
    }

    // Javadoc inherited.
    public Object perform(MethodActionEvent event) throws Throwable {
        super.perform(event);
        return result;
    }
}

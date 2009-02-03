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

import com.volantis.testtools.mock.method.MethodActionEvent;

/**
 * An action that throws a supplied throwable after checking to make sure that
 * the state of the entry supplied matches what is expected.
 */
public class CheckProviderThrow
        extends AbstractCheckProviderRetriever {

    private final Throwable throwable;

    /**
     * Initialise.
     *
     * @param expectedKey   The expected key in the entry.
     * @param expectedValue The expected value in the entry.
     * @param throwable     The throwable to throw.
     */
    public CheckProviderThrow(
            final String expectedKey, final Object expectedValue,
            final Throwable throwable) {

        super(expectedKey, expectedValue);

        this.throwable = throwable;
    }

    /**
     * Initialise.
     *
     * <p>Does not expect an entry.</p>
     *
     * @param throwable The throwable to throw.
     */
    public CheckProviderThrow(Throwable throwable) {
        super();

        this.throwable = throwable;
    }

    // Javadoc inherited.
    public Object perform(MethodActionEvent event) throws Throwable {
        super.perform(event);
        throwable.fillInStackTrace();
        throw throwable;
    }
}

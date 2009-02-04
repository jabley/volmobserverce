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
package com.volantis.mcs.protocols.separator.shared;

import com.volantis.testtools.mock.MockFactory;
import com.volantis.testtools.mock.value.ExpectedValue;

import java.lang.reflect.InvocationTargetException;

/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

/**
 * Matches when a method on the supplied object returns the specified value.
 */
public class ExpectedReturnValue implements ExpectedValue {

    /**
     * Anonymous interface instance pointing to the method to invoke
     */
    private Object invoker;

    /**
     * The expected returned value.
     */
    private ExpectedValue expectedReturnValue;

    /**
     * Initialise.
     *
     * @param invoker The identifier of the methodIdentifier to be
     * invoked.
     * @param expectedReturnValue The expected return value.
     * @param mockFactory
     */
    public ExpectedReturnValue(Object invoker,
                               Object expectedReturnValue,
                               MockFactory mockFactory) {
        this.invoker = invoker;
        if (expectedReturnValue instanceof ExpectedValue) {
            this.expectedReturnValue = (ExpectedValue) expectedReturnValue;
        } else {
            this.expectedReturnValue =
                    mockFactory.expectsEqual(expectedReturnValue);
        }

    }

    /**
     * Matches if the value returned from the invoked function matches the
     * expected return value.
     * @param object
     * @return
     */
    public boolean matches(Object object) {

        try {
            Object returnValue;
            if( invoker instanceof SeparatedContentInvoker) {
                SeparatedContentInvoker ivk = (SeparatedContentInvoker)invoker;
                returnValue = ivk.invoke(object);
            } else {
                SeparatorRendererInvoker ivk = (SeparatorRendererInvoker)invoker;
                returnValue = ivk.invoke(object);
            }
            return expectedReturnValue.matches(returnValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return false;
    }
}

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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.shared.throwable;



/**
 * ExtendedExceptionTestCase
 *
 * @author steve
 */
public class ExtendedRuntimeExceptionTestCase
        extends ExtendedThrowableTestAbstract {

    public ExtendedRuntimeExceptionTestCase() {
        super(ExtendedRuntimeException.class);
    }

    /**
     * Throw an exception with no message.
     *
     * @throws ExtendedRuntimeException Always
     */
    protected Throwable createException() {
        return new ExtendedRuntimeException();
    }

    /**
     * Throw an exception with a message.
     *
     * @throws ExtendedRuntimeException Always
     */
    protected Throwable createException(String s)
            throws ExtendedRuntimeException {
        return new ExtendedRuntimeException(s);
    }

    /**
     * Throw an exception with a message and a cause.
     *
     * @throws ExtendedRuntimeException Always
     */
    protected Throwable createCausedException(String s)
            throws ExtendedRuntimeException {
        return new ExtendedRuntimeException(s, createExceptionCause());
    }

    protected Throwable createCausedException(String s, Throwable t) {
        return new ExtendedRuntimeException(s, t);
    }

    /**
     * Throw an exception with no message but with a cause.
     *
     * @throws ExtendedRuntimeException Always
     */
    protected Throwable createCausedException() throws ExtendedRuntimeException {
        return new ExtendedRuntimeException(createExceptionCause());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-May-05	8007/1	philws	VBM:2005050311 Port improved wrapped exception message handling from 3.3

 04-May-05	8000/1	philws	VBM:2005050311 Correct exception message handling in wrapping exceptions

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	222/1	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 14-Jul-03	185/1	steve	VBM:2003071402 Refactor exceptions into throwable package

 14-Jul-03	183/3	steve	VBM:2003071102 Refactor WrappingException

 14-Jul-03	183/1	steve	VBM:2003071102 implement throwable package

 ===========================================================================
*/

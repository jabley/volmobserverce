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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.management.tracking;

/**
 * Base class for all Exceptions thrown in the
 * com.volantis.mcs.management.tracking package.
 */
public class PageTrackerException extends Exception {
                                          
    /**
     * Create a new <code>WrappingException</code> with no message.
     */
    public PageTrackerException() {
    }

    /**
     * Create a new <code>WrappingException</code> with the specified message.
     * @param message The message.
     */
    public PageTrackerException(String message) {
        super(message);
    }

    /**
     * Create a new <code>WrappingException</code> with the specified message
     * which was caused by the specified Throwable.
     * @param message The message.
     * @param cause The cause of this exception being thrown.
     */
    public PageTrackerException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Create a new <code>WrappingException</code> which was caused by the
     * specified Throwable.
     * @param cause The cause of this exception being thrown.
     */
    public PageTrackerException(Throwable cause) {
        super(cause);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 24-Jun-04	4702/1	matthew	VBM:2004061402 rework JMXPageTrackerFactory error handling

 ===========================================================================
*/

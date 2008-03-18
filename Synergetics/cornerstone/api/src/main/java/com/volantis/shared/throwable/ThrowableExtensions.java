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
 * Add some extra methods that should be implemented by Throwable.
 * <p>This is compatible with the JDK 1.4 extensions to Throwable.</p>
 *
 * <strong>This interface is a facade provided for use by user code and as such
 * must not be implemented by user code.</strong>
 */
public interface ThrowableExtensions {

    /**
     * The copyright statement.
     */
    public static String mark = "(c) Volantis Systems Ltd 2002.";

    /**
     * Get the Throwable object that was the root cause for this exception
     * being thrown.
     * @return The Throwable object that was the root cause for this exception
     * being thrown, or null if this throwable was not thrown in response to
     * another Throwable.
     */
    public Throwable getCause();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 14-Jul-03	185/1	steve	VBM:2003071402 Refactor exceptions into throwable package

 ===========================================================================
*/

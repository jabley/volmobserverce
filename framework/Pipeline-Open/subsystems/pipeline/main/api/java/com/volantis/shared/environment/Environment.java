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

package com.volantis.shared.environment;

/**
 * This interface provides an abstraction of the various environments within
 * which volantis products can run.
 * <p>At the moment it is simply a marker interface and it needs to be converted
 * to the appropriate environment specific instance of this interface in order
 * to be used. Where it is possible and makes sense this interface will be
 * expanded to provide access to abstractions of functionality common across the
 * different environments.</p>
 *
 * <strong>This interface is a facade provided for use by user code and as such
 * must not be implemented by user code.</strong>
 */
public interface Environment {
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 15-Jul-03	197/1	steve	VBM:2003071501 Cookie and environment implementation

 11-Jul-03	181/1	steve	VBM:2003070802 Environment implementation

 ===========================================================================
*/

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
package com.volantis.vdp.scs.authentication;

/**
 * This interface defines the interface for authenticating and
 * returning everything needed for the Secure Connection Server
 * to function.
 */
public interface AuthenticationProvider {
    
    /**
     * Authenticates the userid and password combination against the
     * AuthenticationPRovider.
     *
     * @param user The userid to authenticate.
     * @param password The password for the specified user.
     * @return An AuthenticationToken indicating the login status.
     */
    public AuthenticationToken authenticate(String user, String password);
} 

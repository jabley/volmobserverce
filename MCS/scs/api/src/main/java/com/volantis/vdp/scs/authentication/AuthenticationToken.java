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

import java.net.URL;

public class AuthenticationToken {

    /**
     * Indicates a succesful login.
     */
    public static int LOGIN_SUCCESFUL = 0;

    /**
     * Indicates a failed login because the user does not exist.
     */
    public static int LOGIN_FAILED_INVALID_USER = 1;

    /**
     * Indicates a failed login because the password is
     * incorrect for the specified user.
     */
    public static int LOGIN_FAILED_INVALID_PASSWORD = 2;


    /**
     * Indicates a failed login because the password has
     * expired.
     */
    public static int LOGIN_FAILED_PASSWORD_EXPIRED = 3;


    /**
     * The userid that this token is associated with.
     */ 
    private String user;

    /**
     * The validation status of this user.
     * <p>May be one of {@link LOGIN_SUCCESFUL}, 
     * {@link LOGIN_FAILED_INVALID_USER},
     * {@link LOGIN_FAILED_INVALID_PASSWORD} or 
     * {@link LOGIN_FAILED_PASSWORD_EXPIRED}.
     * </p>
     */ 
    private int status;

    /**
     * The URL associated with this user.
     */ 
    private URL url;


    /**
     * Create a new Authentication with the given status. 
     * <p>This form of the constructor is typically used for failed login 
     * attempts as no URL is set.
     * 
     * @param user The user who this token belogs too.
     * @param status The login status. May be one of {@link LOGIN_SUCCESFUL}, 
     * {@link LOGIN_FAILED_INVALID_USER},
     * {@link LOGIN_FAILED_INVALID_PASSWORD} or 
     * {@link LOGIN_FAILED_PASSWORD_EXPIRED}.
     */ 
    public AuthenticationToken(String user, int status) {
       this(user, status, null);
    }

    /**
     * Create a new Authentication with the given status. 
     * <p>This form of the constructor is typically used for failed login 
     * attempts as no URL is set.
     * 
     * @param user The user who this token belogs too.
     * @param status The login status. May be one of {@link LOGIN_SUCCESFUL}, 
     * {@link LOGIN_FAILED_INVALID_USER},
     * {@link LOGIN_FAILED_INVALID_PASSWORD} or 
     * {@link LOGIN_FAILED_PASSWORD_EXPIRED}.
     * @param url The URL asociated with this user.
     */
    public AuthenticationToken(String user, int status,URL url) {
        this.user = user;
        this.status = status;
        this.url = url;
    }

    /**
     * Return the user who this token belongs too.
     * 
     * @return The user.
     */ 
    public String getUser() {
        return user;    
    }

    /**
     * Return the status of the login attempt.
     * 
     * @return The status. May be one of {@link LOGIN_SUCCESFUL}, 
     * {@link LOGIN_FAILED_INVALID_USER},
     * {@link LOGIN_FAILED_INVALID_PASSWORD} or 
     * {@link LOGIN_FAILED_PASSWORD_EXPIRED}.
     */ 
    public int getStatus() {
        return status;    
    }

    /**
     * Return the URL associated with this user.
     * @return The url.
     */ 
    public URL getUrl() {
        return url;
    }
}

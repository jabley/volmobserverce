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
 * $Header: /src/mps/com/volantis/mps/channels/DefaultAuthenticator.java,v 1.1 2002/11/14 12:42:43 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * dd-mmm-yy    ianw        VBM:200nnnnnnn - Created
 * ----------------------------------------------------------------------------
 */
package com.volantis.mps.channels;


import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * This class provides a basic <CODE>Authenticator</CODE> for 
 * <CODE>ChannelAdapters</CODE> implementing <CODE>javax.mail.session</CODE> 
 * so that user and password can be read from an external source.
 */
public class DefaultAuthenticator extends Authenticator {
    
    private static final String mark = "(c) Volantis Systems Ltd 2002.";
    
    /**
     * The authentication user.
     */
    private String user;
    
    /**
     * The authentication password.
     */
    private String password;
    
    /** Creates a new instance of SMTPAuthenticator */
    public DefaultAuthenticator() {
    }
    
    /**
     * Set the user to use for authentication.
     * @param user The user.
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Set the password to use for authentication.
     * @param password The password.
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Get the password authentication for use in the session.
     * @return The <CODE>PasswordAuthentication</CODE> object.
     */
    public PasswordAuthentication getPasswordAuthentication() {
        PasswordAuthentication auth = 
            new PasswordAuthentication(user, password);
        return auth;
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-04	243/1	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 10-Jun-04	121/1	ianw	VBM:2004060111 Made to work with main 3.2 MCS stream

 ===========================================================================
*/

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

import java.net.MalformedURLException;
import java.net.URL;

public class SampleAuthenticationProvider implements AuthenticationProvider {

    private String user;
    private String passwd;
    private URL url;

    public SampleAuthenticationProvider() {
        this.user = "developer1";
        this.passwd = "secret";
        try {
            this.url = new URL("http://localhost/");
        } catch(MalformedURLException mue) {

        }
    }

    public AuthenticationToken authenticate(String userid, String password) {

        AuthenticationToken token = null;

        if(user.equals(userid)&&passwd.equals(password)) {
            token = new AuthenticationToken(userid,
                        AuthenticationToken.LOGIN_SUCCESFUL,
                        url
                    );
        } else if(!user.equals(userid)&&passwd.equals(password)) {
            token = new AuthenticationToken(userid,
                    AuthenticationToken.LOGIN_FAILED_INVALID_USER
                );

        } else if(user.equals(userid)&&!passwd.equals(password)) {
            token = new AuthenticationToken(userid,
                    AuthenticationToken.LOGIN_FAILED_INVALID_PASSWORD
                );
        } else if(!user.equals(userid)&&!passwd.equals(password)) {
            token = new AuthenticationToken(userid,
                    AuthenticationToken.LOGIN_FAILED_PASSWORD_EXPIRED
                );
        }

        return token;
    }
    
}

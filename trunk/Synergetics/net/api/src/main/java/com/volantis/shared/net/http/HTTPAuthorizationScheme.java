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
/**
 * (c) Copyright Volantis Systems Ltd. 2005. 
 */
package com.volantis.shared.net.http;


import com.volantis.synergetics.utilities.Base64;
import com.volantis.shared.net.proxy.Proxy;

import java.util.Map;
import java.util.HashMap;
import java.util.Locale;

/**
 * A type safe enumeration of mechanisms by which a client may authenticate
 * with a proxy
 */
public abstract class HTTPAuthorizationScheme {

    /**
     * The header used to send basci authentication information to a proxy.
     */
    public static final String PROXY_AUTHORIZATION_HEADER =
                "Proxy-Authorization";

    /**
     * This string can be used to generate a challenge response. It is easier
     * to just assume the challenge will be made and initialliy send the
     * response without having to ask the proxy for a page and then handle the
     * 407 response with the challenge header and then request the page again
     * with the generated response.
     *
     * @todo later this approach will not work if other authorization schemes
     * are implemented.
     */
    public static final String MOCK_CHALLENGE_BASIC =
        "Basic everything after the initial 'Basic ' is not "
        + "relevent to the reponse";
    
    /**
     * Store the set of Maps agains thier scheme name.
     */
    private static final Map SCHEMES = new HashMap();

    /**
     * "Basic" authorization is base64 encoded clear-text user name and
     * password.
     */
    public static final HTTPAuthorizationScheme BASIC =
        new HTTPAuthorizationScheme("basic") {
            public String createResponseForChallenge(String challenge, Proxy proxy) {
                String result = null;
                if (proxy.useAuthorization()) {
                    result =  "basic " + Base64.encodeString(
                        proxy.getUser() + ":" + proxy.getPassword(), false);
                }
                return result;
            }
        };

    /**
     * This method can be used to find an authentication scheme for a given
     * challenge.
     *
     * @param challenge the challenge whose scheme you wish to find. The
     *                  challenge is the value of the chellenge header (without
     *                  the header name)
     * @return the AuthorizationScheme appropriate for the specified challenge
     *         or null if an appropriate scheme could not be found
     */
    public static HTTPAuthorizationScheme getAuthorization(String challenge) {
        String tmp = challenge.trim();
        int i = tmp.indexOf(' ');
        if (i >= 0) {
            // if we found a space then find substring otherwise assume that
            // the challenge only contains the scheme name
            tmp = tmp.substring(0, i);
        }
        return literal(tmp);
    }

    /**
     * Return a the authorization object corresponding to the scheme name
     *
     * @param scheme the name of the authroization scheme
     * @return the authroization scheme or null if one does not exist
     */
    public static HTTPAuthorizationScheme literal(String scheme) {
        return (HTTPAuthorizationScheme)
            HTTPAuthorizationScheme.SCHEMES.get(
                scheme.trim().toLowerCase(Locale.ENGLISH));
    }

    /**
     * The name of the authorization scheme to use
     */
    private String scheme;

    /**
     * Construct a Authorization type with the given scheme name. scheme
     * names are not case sensitive
     * @param scheme the name of the authroization scheme
     */
    private HTTPAuthorizationScheme(String scheme) {
        if (scheme==null || "".equals(scheme.trim())) {
            throw new IllegalArgumentException(
                "Scheme must not be null or an empty string");
        }
        // schemes are all english names
        String tmp = scheme.toLowerCase(Locale.ENGLISH);
        this.scheme = tmp;
        if (HTTPAuthorizationScheme.SCHEMES.containsKey(tmp)) {
            throw new IllegalArgumentException("A Scheme with the name '" +
                                               tmp + "' already exists");
        }
        HTTPAuthorizationScheme.SCHEMES.put(tmp, this);
    }

    /**
     * @return the name of the authorization scheme
     */
    public String getName() {
        return scheme;
    }

    /**
     * Authorization normally consists of a number of challenges and responses.
     * The proxy will provide a challenge String to which a response must be
     * generated. Passing the challenge to this method will cause a correct
     * response for the challege to be produced.
     *
     * @param challenge the challenge that must be responded to. This is the
     *                  entire value of the "Proxy-Authorization" response
     *                  header. Note the name "Proxy-Authenticate:" is not part
     *                  of the value. eg "Proxy-Authenticate: Basic" for basic
     *                  authorization
     * @param proxy     the proxy information from which the response is
     *                  generated
     * @return the string to send to the proxy to authorize the connection.
     *         This does not include the header name. or null if the proxy is
     *         not able to provide authentication information
     */
    public abstract String createResponseForChallenge(
        String challenge, Proxy proxy);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9649/3	matthew	VBM:2005092809 Allow proxy configuration via system properties

 30-Sep-05	9649/1	matthew	VBM:2005092809 Allow proxy configuration via system properties

 ===========================================================================
*/

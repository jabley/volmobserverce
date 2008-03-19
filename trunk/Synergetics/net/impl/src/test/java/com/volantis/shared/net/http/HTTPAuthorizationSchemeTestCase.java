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

import com.volantis.shared.net.proxy.ProxyMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test the HTTPAuthorizationScheme
 */
public class HTTPAuthorizationSchemeTestCase
        extends TestCaseAbstract {

    /**
     * Ensure the "literal" method works as expected and returns the corret
     * Scheme
     *
     * @throws Exception
     */
    public void testLiteral() throws Exception {
        // check that the literal method is not case sentitive
        {
            HTTPAuthorizationScheme scheme = HTTPAuthorizationScheme.literal(
                "basic");
            assertSame("Should return the BASIC enumerated type object",
                       HTTPAuthorizationScheme.BASIC, scheme);
        }
        {
            HTTPAuthorizationScheme scheme = HTTPAuthorizationScheme.literal(
                "BASIC");
            assertSame("Should return the BASIC enumerated type object",
                       HTTPAuthorizationScheme.BASIC, scheme);
        }
        {
            HTTPAuthorizationScheme scheme = HTTPAuthorizationScheme.literal(
                "bAsIc");
            assertSame("Should return the BASIC enumerated type object",
                       HTTPAuthorizationScheme.BASIC, scheme);
        }
        {   // does it return null for an unknown scheme name
            HTTPAuthorizationScheme scheme = HTTPAuthorizationScheme.literal(
                "gibberish");
            assertNull("Should return null for an unrecognized scheme",
                       scheme);
        }
    }

    /**
     * Ensure the getScheme method returns the correct scheme for a given
     * challenge
     *
     * @throws Exception
     */
    public void testGetScheme() throws Exception {
        {
            // single space after basic is necessary for identification of the
            // scheme
            HTTPAuthorizationScheme scheme = HTTPAuthorizationScheme.
                getAuthorization("Basic ");
            assertSame("Expect basic authorization",
                       HTTPAuthorizationScheme.BASIC, scheme);
        }
        {
            // ensure all other stuff for Basic scheme is ignored.
            HTTPAuthorizationScheme scheme = HTTPAuthorizationScheme.
                getAuthorization("Basic gibberish=false, please ignore everything "
                                 + "after the first space as it is not relevent to "
                                 + "basic auth");
            assertSame("Expect basic authorization",
                       HTTPAuthorizationScheme.BASIC, scheme);
        }
        {
            // case insensitive
            HTTPAuthorizationScheme scheme = HTTPAuthorizationScheme.
                getAuthorization("BaSic ");
            assertSame("Expect basic authorization",
                       HTTPAuthorizationScheme.BASIC, scheme);
        }
        {
            // preceeding whitepace
            HTTPAuthorizationScheme scheme = HTTPAuthorizationScheme.
                getAuthorization("       Basic ");
            assertSame("Expect basic authorization",
                       HTTPAuthorizationScheme.BASIC, scheme);
        }
    }

    /**
     * Ensure the Basic Auth scheme produces something sensible in response to
     * a correct challenge
     *
     * @throws Exception
     */
    public void testCreateResponseToChallengeBasic() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final ProxyMock proxyMock =
                new ProxyMock("proxyMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        proxyMock.expects.useAuthorization().returns(true).any();
        proxyMock.expects.getUser().returns("Aladdin").any();
        proxyMock.expects.getPassword().returns("open sesame").any();

//        Proxy proxy = new MockProxy() {
//            public String getUser() {
//                return "Aladdin";
//            }
//            public String getPassword() {
//                return "open sesame";
//            }
//            public boolean useAuthorization() {
//                return true;
//            }
//        };
        String response = HTTPAuthorizationScheme.BASIC.
            createResponseForChallenge("Basic ", proxyMock);
        assertEquals("This test is an example from RFC 2617 page 5",
                    "basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==", response);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9649/1	matthew	VBM:2005092809 Allow proxy configuration via system properties

 ===========================================================================
*/

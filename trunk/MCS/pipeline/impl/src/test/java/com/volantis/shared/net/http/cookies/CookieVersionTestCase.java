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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.shared.net.http.cookies;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Test the functionality of the CookieVersion type safe enumeration.
 */
public class CookieVersionTestCase extends TestCaseAbstract {

    /**
     * Standard constructor
     * @param name
     */
    public CookieVersionTestCase(String name) {
        super(name);
    }

    /**
     * Simple tests to ensure the CookieVersions are actually singletons
     * @throws Exception
     */
    public void testEnumsAreSingletons() throws Exception {

        CookieVersion cv0 = CookieVersion.NETSCAPE;
        assertSame("Ensure the getCookieVersion method returns the " +
                   "NETSCAPE CookieVersion",
                   cv0, CookieVersion.getCookieVersion(cv0.getName()));

        CookieVersion cv1 = CookieVersion.RFC2109;
        assertSame("Ensure the getCookieVersion method returns the " +
                   "RFC2109 CookieVersion",
                   cv1, CookieVersion.getCookieVersion(cv1.getName()));

        assertNotSame("Ensure the CookieVersion are not the same",
                      cv0, cv1);

        assertNull("Ensure the CookieVersion handles null parameter correctly",
                   CookieVersion.getCookieVersion(null));
        assertNull("Ensure the CookieVersion handles junk paramter correctly",
                   CookieVersion.getCookieVersion("junk"));
    }

    /**
     * Test the getName and getNumber methods work.
     * @throws Exception
     */
    public void testMemberMethods() throws Exception {
        assertEquals("test getName netscape",
                     "netscape", CookieVersion.NETSCAPE.getName());
        assertEquals("test getName rfc2109",
                     "rfc2109", CookieVersion.RFC2109.getName());

        assertEquals("test getNumber netscape",
                     0, CookieVersion.NETSCAPE.getNumber());
        assertEquals("test getNumber rfc2109",
                     1, CookieVersion.RFC2109.getNumber());
    }


    /**
     * Make sure that the CookieVersion class is properly serializable.
     * @throws Exception
     */
    public void testSerialization() throws Exception {

        Object o = checkIdentity(CookieVersion.getCookieVersion(0));
        assertNotNull("name should not be null", ((CookieVersion)o).getName());


        CookieVersion cv0 = CookieVersion.NETSCAPE;
        CookieVersion cv1 = CookieVersion.RFC2109;
        checkIdentity(cv0);
        checkIdentity(cv1);


    }


    /**
     * Utility method that serializes the parameter and then deserializes it
     * and returns the result. Any error in serialization
     * or deserialization will cause an Exception to be thrown.
     *
     * @param entity the object whose serialisation you wish to test
     * @return the deserialized copy of the object.
     * @see java.io.ObjectInputStream, java.io.ObjectOutputStream for the
     * exceptions that can be thrown
     */
    public static Serializable checkSerialization(
           Serializable entity) throws Exception {

        if (entity == null) {
            throw new IllegalArgumentException("Argument must not be null");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(entity);
        oos.flush();
        byte[] data = baos.toByteArray();
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(data));
        Serializable result = (Serializable) ois.readObject();
        return result;
    }

    /**
     * Utility method that serializes the parameter then deserializes it. It
     * then asserts that the deserialized version is equal to the original.
     *
     * @param entity the object to test.
     * @return the deserialized object.
     */
    public static Serializable checkIdentity(Serializable entity)
            throws Exception {
        Object result = checkSerialization(entity);
        assertEquals("The deserialized object is not the same as the object " +
                     "before serialization", entity, result);
        return (Serializable) result;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Mar-05	7337/1	matthew	VBM:2005030809 Add a CookieVersion typesafe enum and use it in the Cookie interface and its implementations

 ===========================================================================
*/

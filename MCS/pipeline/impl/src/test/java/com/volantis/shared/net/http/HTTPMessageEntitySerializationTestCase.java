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
package com.volantis.shared.net.http;

import com.volantis.shared.net.http.cookies.CookieImpl;
import com.volantis.shared.net.http.headers.HeaderImpl;
import com.volantis.shared.net.http.parameters.RequestParameterImpl;
import com.volantis.shared.servlet.http.HTTPServletCookie;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.pipeline.sax.drivers.web.WebRequestCookie;
import com.volantis.xml.pipeline.sax.drivers.web.WebRequestHeader;
import com.volantis.xml.pipeline.sax.drivers.web.WebRequestParameter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Base class that can be used to check that implementors of the
 * HTTPMessageEntity interface.
 */
public class HTTPMessageEntitySerializationTestCase
        extends TestCaseAbstract {

    /**
     * Standard terst case constructor.
     * @param name
     */
    public HTTPMessageEntitySerializationTestCase(String name) {
        super(name);
    }


    /**
     * Utility method that serializes the <code>entity</code> parameter and
     * then deserializes it and returns the result. Any error in serialization
     * or deserialization will cause an Exception to be thrown.
     *
     * @param entity the HTTPMessageEntity whose serialisation you wish to test
     * @return the deserialized copy of the <code>entity</code> implementation.
     * @see java.io.ObjectInputStream, java.io.ObjectOutputStream for the
     * exceptions that can be thrown
     */
    public static HTTPMessageEntity checkSerialization(
            HTTPMessageEntity entity) throws Exception {

        if(entity == null) {
            throw new IllegalArgumentException("Argument must not be null");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(entity);
        oos.flush();
        byte[] data = baos.toByteArray();
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(data));
        HTTPMessageEntity result = (HTTPMessageEntity)ois.readObject();
        return result;
    }


    /**
     * Utility method that serializes the <code>entity</code> parameter
     * then deserializes it. It then asserts that the deserialized version
     * is equal to the original.
     * @param entity the HTTPMessageEntity to test
     *
     */
    public static void checkEquality(HTTPMessageEntity entity)
            throws Exception {
        HTTPMessageEntity result = checkSerialization(entity);
        assertEquals("The deserialized object is not equal to the object " +
                     "before serialization", entity, result);
    }


    /**
     * Test the implementors or the HTTPMessageEntity interface for
     * correct Serialization behaviour.
     * @throws java.lang.Exception
     */
    public void testHTTPSerialization() throws Exception {

        HTTPFactory factory = HTTPFactory.getDefaultInstance();

        {
            // test request parameters
            HTTPMessageEntity entity = factory.createRequestParameter("TEST");
            checkEquality(entity);
        }
        {
            // test headers
            HTTPMessageEntity entity = factory.createHeader("TEST");
            checkEquality(entity);
        }
        {
            // test cookies
            HTTPMessageEntity entity = factory.createCookie(
                    "TEST", "test.domain", "/testdir");
            checkEquality(entity);
        }
        {
            // test ServletCookies
            HTTPMessageEntity entity = new HTTPServletCookie(
                    "TEST", "test.domain", "/testdir");
            checkEquality(entity);
        }
        {
            // test WebRequestCookie
            CookieImpl entity = new WebRequestCookie();
            entity.setName("TEST");
            entity.setMaxAge(1);
            entity.setComment("comment");
            entity.setValue("test");
            checkEquality(entity);
        }
        {
            // test WebRequestHeader
            HeaderImpl entity = new WebRequestHeader();
            entity.setName("TEST");
            entity.setValue("test");
            checkEquality(entity);
        }
        {
            // test WebRequestParameter
            RequestParameterImpl entity = new WebRequestParameter();
            entity.setName("TEST");
            entity.setValue("test");
            checkEquality(entity);
        }

    }




}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Oct-04	5990/1	matthew	VBM:2004102621 Mark HTTPMessageEntity as Serializable and modify its javadoc

 ===========================================================================
*/

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

package com.volantis.mcs.protocols;

import com.volantis.testtools.mock.ExpectationContainer;
import com.volantis.testtools.mock.MockFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Provides helper methods for creating mock attributes classes.
 */
public class AttributesTestHelper {

    /**
     * The types of parameters for constructor.
     */
    private static final Class[] MOCK_ATTRIBUTES_CTOR_PARAMETER_TYPES =
            new Class[]{
                String.class,
                ExpectationContainer.class
            };

    private static final Class[] PROXY_GET_PARAMETER_TYPES =
            new Class[]{
                String.class
            };

    private static final MockFactory mockFactory =
            MockFactory.getDefaultInstance();

    /**
     * Create a new mock attributes class.
     * <p/>
     * <p>This handles the problem whereby the
     *
     * @param mockClass
     * @param expectedName
     * @param identifier
     * @param expectations
     * @return
     */
    public static MCSAttributes createMockAttributes(
            Class mockClass, String expectedName, String identifier,
            ExpectationContainer expectations)
            throws Exception {

        Class proxyClass = mockClass.getClassLoader().loadClass(
                mockClass.getName() + "$MockProxy");

        Method method = proxyClass.getMethod("get", PROXY_GET_PARAMETER_TYPES);
        Object[] args;

        args = new Object[]{identifier};
        Object proxy = method.invoke(null, args);

        // Get the expects field from the proxy object.
        Field proxyExpectsField = proxyClass.getField("expects");
        MCSAttributesMock.Expects proxyExpects = (MCSAttributesMock.Expects)
                proxyExpectsField.get(proxy);

        // Expect any tag name, simplifies callers at the slight expense of
        // not checking the actual tag name.
        if (expectedName != null) {
            proxyExpects.setTagName(expectedName).any();
        }

        // Construct an instance. This should consume all the expectations.
        Constructor ctor = mockClass.getConstructor(
                MOCK_ATTRIBUTES_CTOR_PARAMETER_TYPES);
        args = new Object[]{identifier, expectations};
        MCSAttributes mock = (MCSAttributes) ctor.newInstance(args);

        // Get the expects field from the mock object.
        Field mockExpectsField = mockClass.getField("expects");
        MCSAttributesMock.Expects mockExpects = (MCSAttributesMock.Expects)
                mockExpectsField.get(mock);

        // Set up additional expectations. These assume that the container is
        // an unordered one.
        mockExpects.getTagName().returns(expectedName).any();

        return mock;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9888/3	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 ===========================================================================
*/

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
package com.volantis.mcs.runtime;

/**
 * This class should be subclassed by test cases to provide specific behaviour
 * for particular tests. Ideally the tests would use the mock framework, but
 * this should be used until the mock framework is available to integration
 * tests. (If all anonymous subclasses subclass a test class (rather than
 * subclassing Volantis directly) it makes it much easier to see the hierarchy
 * in Idea).
 * <P/>
 * NB: This test class does not set up the
 * {@link com.volantis.mcs.marlin.sax.NamespaceSwitchContentHandlerMap} because
 * it causes an exception to be thrown for tests in all subsystems except for
 * xdime. Tests that require this can override the empty initialisation method
 * here and call the superclass' method.
 *
 */
public class TestableVolantis extends Volantis {

    /**
     * See inherited javadoc in
     * {@link Volantis#initializeNamespaceSwitchContentHandlerMap()} for the
     * original behaviour.
     *
     * This method overrides the superclass method in order to do nothing. The
     * reason for this is that this method will throw an exception in tests in
     * all subsystems other than xdime. This is because one of the content
     * handler factory classes is not accessible during the test. Ideally that
     * would be fixed, but this is a workaround that should be used by tests
     * that do not use the
     * {@link com.volantis.mcs.marlin.sax.NamespaceSwitchContentHandlerMap}.
     */

    protected void initializeNamespaceSwitchContentHandlerMap()
            throws ClassNotFoundException, IllegalAccessException,
            InstantiationException {
        // Do nothing.
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Aug-05	9391/1	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 ===========================================================================
*/

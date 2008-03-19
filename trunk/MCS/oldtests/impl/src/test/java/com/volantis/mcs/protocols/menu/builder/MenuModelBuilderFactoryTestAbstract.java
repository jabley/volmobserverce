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
package com.volantis.mcs.protocols.menu.builder;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Base test case for the testing of concrete menu model builder factory
 * implementations.
 */
public abstract class MenuModelBuilderFactoryTestAbstract
    extends TestCaseAbstract {
    /**
     * Factory method used to create the factory instance under test. This
     * allows the default instance to be automatically set to the required
     * concrete type.
     *
     * @return a factory instance to be tested
     */
    protected abstract MenuModelBuilderFactory createFactory();

    /**
     * Used to restore the default instance after tests have been run
     */
    private MenuModelBuilderFactory previous;

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();

        previous = MenuModelBuilderFactory.setDefaultInstance(createFactory());
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        MenuModelBuilderFactory.setDefaultInstance(previous);

        super.tearDown();
    }

    /**
     * Tests that {@link MenuModelBuilderFactory#setDefaultInstance} and
     * {@link MenuModelBuilderFactory#getDefaultInstance} behave as expected
     * in non-failure conditions.
     */
    public void testDefaultFactoryManagementSuccess() {
        MenuModelBuilderFactory factoryA = createFactory();
        MenuModelBuilderFactory factoryB = createFactory();

        // Set the default instance explicitly to isolate this test
        MenuModelBuilderFactory.setDefaultInstance(factoryA);

        assertSame("Default instance not as",
                   factoryA,
                   MenuModelBuilderFactory.getDefaultInstance());

        assertSame("Previous default instance not as",
                   factoryA,
                   MenuModelBuilderFactory.setDefaultInstance(factoryB));

        assertSame("Updated default instance not as",
                   factoryB,
                   MenuModelBuilderFactory.getDefaultInstance());
    }

    /**
     * Tests that {@link MenuModelBuilderFactory#setDefaultInstance} behaves
     * as expected in failure conditions.
     */
    public void testDefaultFactoryManagementFailure() {
        try {
            MenuModelBuilderFactory.setDefaultInstance(null);

            fail("Should not have been able to register a null " +
                 "default instance");
        } catch (IllegalArgumentException e) {
            // expected condition
        }
    }

    /**
     * Checks that the factory correctly returns a non-null builder.
     */
    public void testCreateBuilderNotNull() {
        assertNotNull("The builder instance should not be null",
                      MenuModelBuilderFactory.getDefaultInstance().
                      createBuilder());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Mar-04	3292/1	philws	VBM:2004022703 Added Menu Model Builder API

 ===========================================================================
*/

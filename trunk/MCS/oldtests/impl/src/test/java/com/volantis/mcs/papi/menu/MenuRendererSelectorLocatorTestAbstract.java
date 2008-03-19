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

package com.volantis.mcs.papi.menu;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * The base class for all MenuRendererSelectorLocator test cases.
 */
public abstract class MenuRendererSelectorLocatorTestAbstract
    extends TestCaseAbstract {
    
    /**
     * Factory method used to create the MenuRendererSelectorLocator instance
     * under test.
     *
     * @return The MenuRendererSelectorLocator instance to be tested
     */
    protected abstract MenuRendererSelectorLocator createMenuRendererSelectorLocator();

    /**
     * Used to restore the default instance after tests have been run
     */
    private MenuRendererSelectorLocator previous;

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();

        MenuRendererSelectorLocator locator = createMenuRendererSelectorLocator();
        previous = MenuRendererSelectorLocator.setDefaultInstance(locator);
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        MenuRendererSelectorLocator.setDefaultInstance(previous);

        super.tearDown();
    }

    /**
     * Tests that {@link MenuRendererSelectorLocator#setDefaultInstance} and
     * {@link MenuRendererSelectorLocator#getDefaultInstance} behave as expected
     * in non-failure conditions.
     */
    public void testDefaultLocatorManagementSuccess() {
        MenuRendererSelectorLocator locatorA = createMenuRendererSelectorLocator();
        MenuRendererSelectorLocator locatorB = createMenuRendererSelectorLocator();

        // Set the default instance explicitly to isolate this test
        MenuRendererSelectorLocator.setDefaultInstance(locatorA);

        assertSame("Default instance not as",
                   locatorA,
                   MenuRendererSelectorLocator.getDefaultInstance());

        assertSame("Previous default instance not as",
                   locatorA,
                   MenuRendererSelectorLocator.setDefaultInstance(locatorB));

        assertSame("Updated default instance not as",
                   locatorB,
                   MenuRendererSelectorLocator.getDefaultInstance());
    }

    /**
     * Tests that {@link MenuRendererSelectorLocator#setDefaultInstance} behaves
     * as expected in failure conditions.
     */
    public void testDefaultLocatorManagementFailure() {
        try {
            MenuRendererSelectorLocator.setDefaultInstance(null);

            fail("Should not have been able to register a null " +
                 "default instance");
        } catch (IllegalArgumentException e) {
            // expected condition
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-Mar-04	3274/1	pduffin	VBM:2004022704 Initial API for menu renderers and markup generators

 ===========================================================================
*/

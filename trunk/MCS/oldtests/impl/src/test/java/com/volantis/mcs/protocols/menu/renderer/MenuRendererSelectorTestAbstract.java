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

package com.volantis.mcs.protocols.menu.renderer;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * The base class for all MenuRendererSelector test cases.
 */
public abstract class MenuRendererSelectorTestAbstract
    extends TestCaseAbstract {

    /**
     * Create the MenuRendererSelector instance that is being tested.
     * @return The MenuRendererSelector instance to test, may not be null.
     */
    protected abstract MenuRendererSelector createMenuRendererSelector();

    // =========================================================================
    // Tests for selectMenuRenderer method.
    // =========================================================================

    /**
     * Test that the select method throws an IllegalArgumentException for
     * null Menu.
     */
    public void testSelectMethodChecksForNullMenu()
        throws Exception {

        MenuRendererSelector selector = createMenuRendererSelector();

        try {
            selector.selectMenuRenderer(null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // Success.
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

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 07-Apr-04	3735/1	geoff	VBM:2004033102 Enhance Menu Support: Address some issues with asset references

 11-Mar-04	3274/2	pduffin	VBM:2004022704 Initial API for menu renderers and markup generators

 ===========================================================================
*/

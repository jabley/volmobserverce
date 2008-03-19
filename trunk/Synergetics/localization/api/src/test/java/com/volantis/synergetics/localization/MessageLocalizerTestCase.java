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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.localization;


import junit.framework.TestCase;

/**
 * Test Case for the {@link MessageLocalizer} class
 */
public class MessageLocalizerTestCase extends TestCase {

    /**
     * Instance of the class being tested
     */
    private MessageLocalizer messageLocalizer;

    protected void setUp() throws Exception {
        super.setUp();
        messageLocalizer =
            MessageLocalizer.getLocalizer(MessageLocalizer.class,
                                          "synergetics");

    }

    protected void tearDown() throws Exception {
        messageLocalizer = null;
        super.tearDown();
    }

    /**
     * Tests that the format method that only takes a key returns the expected
     * localized message.
     *
     * @throws Exception if an error occurs
     */
    public void testFormatNoCategory() throws Exception {
        String key = "unexpected-illegal-state-exception";
        assertEquals("Unexpected localized message for key " + key,
                     "Unexpected IllegalStateException",
                     messageLocalizer.format(key));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Dec-04	358/1	doug	VBM:2004122005 Enhancements to the MessageLocalizer interface

 ===========================================================================
*/

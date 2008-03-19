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
package com.volantis.mcs.eclipse.common;

/**
 * Tests {@link URIFragmentExchange}.
 */
public class URIFragmentExchangeTestCase
        extends AttributeValueExchangeTestAbstract {
    /**
     * Factory method used to create the testable instance.
     *
     * @return a testable instance
     */
    protected AttributeValueExchange createTestableInstance() {
        return new URIFragmentExchange();
    }

    public void testToModelFormEncoding() throws Exception {
        AttributeValueExchange instance = createTestableInstance();

        assertEquals("Encoding failed",
                     "this%20is%20a%20test%20of%20%25",
                     instance.toModelForm("this is a test of %"));

        assertEquals("Encoding failed (2)",
                     "%20",
                     instance.toModelForm(" "));

        assertEquals("Encoding failed (3)",
                     "%2520",
                     instance.toModelForm("%20"));
    }

    public void testToModelFormPassthrough() throws Exception {
        AttributeValueExchange instance = createTestableInstance();
        String expected = "thisIsAPassthrough";
        assertSame("Passthrough failed",
                   expected,
                   instance.toModelForm(expected));
    }

    public void testToControlFormDecoding() throws Exception {
        AttributeValueExchange instance = createTestableInstance();

        assertEquals("Decoding failed",
                     "this is a test of %",
                     instance.toControlForm(
                             "this%20is%20a%20test%20of%20%25"));

        assertEquals("Decoding failed (2)",
                     " ",
                     instance.toControlForm("%20"));

        assertEquals("Decoding failed (3)",
                     "%20",
                     instance.toControlForm("%2520"));
    }

    public void testToControlFormPassthrough() throws Exception {
        AttributeValueExchange instance = createTestableInstance();
        String expected = "thisIsAPassthrough";
        assertSame("Passthrough failed",
                   expected,
                   instance.toControlForm(expected));
    }

    public void testToControlFormBadlyFormed() throws Exception {
        AttributeValueExchange instance = createTestableInstance();

        try {
            instance.toControlForm("this%20is%20bad%");

            fail("Expected exception (1)");
        } catch (IllegalStateException e) {
            // Expected condition
        }

        try {
            instance.toControlForm("this%20is%20bad%2Xstuff");

            fail("Expected exception (2)");
        } catch (NumberFormatException e) {
            // Expected condition
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Mar-05	7374/4	philws	VBM:2004121405 Allow asset values to contain space characters via URI encoding

 ===========================================================================
*/

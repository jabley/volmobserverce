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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom;

import com.volantis.mcs.dom.impl.DocTypeImpl;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link DocType}.
 */
public class DocTypeTestCase
        extends TestCaseAbstract {

    public void testEmpty() throws Exception {

        try {
            doCreationTest(null, null, null, MarkupFamily.SGML);
            fail("Did not detect invalid arguments");
        } catch (IllegalArgumentException e) {
            assertEquals("At least one of public id, system id " +
                    "and internal DTD must be set", 
                    e.getMessage());
        }
    }

    private void doCreationTest(
            String publicId, String systemId, String internalDTD,
            MarkupFamily markupFamily) {

        DocType docType = new DocTypeImpl(
                "html", publicId, systemId, internalDTD,
                markupFamily);

        assertEquals(publicId, docType.getPublicId());
        assertEquals(systemId, docType.getSystemId());
        assertEquals(internalDTD, docType.getInternalDTD());
    }

    public void testNoMarkupFamily() throws Exception {
        try {
            doCreationTest("public", null, null, null);
            fail("Did not detect invalid arguments");
        } catch (IllegalArgumentException e) {
            assertEquals("markupFamily cannot be null",
                    e.getMessage());
        }
    }

    public void testPublicIdOnly() throws Exception {
        doCreationTest("public", null, null, MarkupFamily.SGML);
    }

    public void testSystemIdOnly() throws Exception {
        doCreationTest(null, "system", null, MarkupFamily.XML);
    }

    public void testInternalDTDOnly() throws Exception {
        doCreationTest(null, null, "dtd", MarkupFamily.SGML);
    }

    public void testPublicAndSystemIds() throws Exception {
        doCreationTest("public", "system", null, MarkupFamily.XML);
    }
}

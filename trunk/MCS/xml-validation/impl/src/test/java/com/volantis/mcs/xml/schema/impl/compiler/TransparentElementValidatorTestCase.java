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

package com.volantis.mcs.xml.schema.impl.compiler;

import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.mcs.xml.schema.model.Content;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link TransparentElementValidator}.
 */
public class TransparentElementValidatorTestCase
        extends TestCaseAbstract {

    /**
     * Ensure that a transparent validator at the root of a document works
     * correctly.
     */
    public void testTransparentAtRoot() throws Exception {

        ElementType e = new ElementType("", "e", "e");
        ElementType c = new ElementType("", "c", "c");

        TransparentElementValidator elementValidator =
                new TransparentElementValidator(e, null, true);

        elementValidator.open(null);

        // If the content is character data that is not required then it
        // shouldn't be written out at the root.
        assertFalse(elementValidator.content(Content.PCDATA, false));

        // If the content is required (and so must be element) then it should
        // be written out.
        assertTrue(elementValidator.content(c, true));

        elementValidator.close();
    }
}

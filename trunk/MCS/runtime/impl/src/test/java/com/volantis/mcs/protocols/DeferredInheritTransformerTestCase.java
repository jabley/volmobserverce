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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.runtime.debug.StrictStyledDOMHelper;
import com.volantis.mcs.dom.Document;

/**
 * Test cases for {@link DeferredInheritTransformer}.
 */
public class DeferredInheritTransformerTestCase
        extends TestCaseAbstract {

    /**
     * Ensure that the -internal-deferred-inherit correctly causes the value
     * to be inherited from the parent.
     */
    public void testDeferredInherit() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        DOMTransformer transformer = new DeferredInheritTransformer();

        StrictStyledDOMHelper helper = new StrictStyledDOMHelper(null);

        Document document = helper.parse(
                "<div style='text-align: -internal-deferred-inherit'>" +
                "<div style='text-align: -internal-deferred-inherit'>" +
                "<div style='text-align: right'>" +
                "<div style='text-align: -internal-deferred-inherit'/>" +
                "</div>" +
                "</div>" +
                "</div>" +
                "");
        Document transformed = transformer.transform(null, document);
        assertSame(document, transformed);

        String actual = helper.render(document);
        String expected = "<div>" +
                "<div>" +
                "<div style='text-align: right'>" +
                "<div/>" +
                "</div>" +
                "</div>" +
                "</div>" +
                "";
        assertEquals(expected, actual);
    }

}

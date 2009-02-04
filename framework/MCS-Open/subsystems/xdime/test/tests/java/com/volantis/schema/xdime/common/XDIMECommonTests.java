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

package com.volantis.schema.xdime.common;

import com.volantis.schema.ContentNotCompleteOneChild;
import com.volantis.schema.SchemaTestBuilder;
import com.volantis.xml.schema.Schemata;

/**
 * Contains schema tests that are common to both XDIME 2 and XDIME CP.
 */
public class XDIMECommonTests {

    /**
     * Get a test builder with the common tests in it.
     *
     * <p>The caller must use {@link SchemaTestBuilder#useClass(Class)}
     * to make sure that any additional test resources are found relative
     * to the calling class.</p>
     *
     * @param schemata The schemata that are to be tested.
     * @return The test builder.
     */
    public static SchemaTestBuilder getSchemaTestBuilder(
            final Schemata schemata) {

        SchemaTestBuilder builder = new SchemaTestBuilder(
                XDIMECommonTests.class, schemata);
        builder.addValidDocument("xml/simple.xml");
        builder.addValidDocument("xml/meta-in-body.xml");
        builder.addValidDocument("xml/meta-in-head.xml");
        builder.addValidDocument("xml/di-select-in-body.xml");
        builder.addInvalidDocument("xml/no-title.xml",
                new ContentNotCompleteOneChild("head",
                        "http://www.w3.org/2002/06/xhtml2", "title"));

        return builder;
    }
}

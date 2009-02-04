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

package com.volantis.mcs.xml.schema.validation;

import com.volantis.mcs.xml.schema.impl.validation.ValidationMessages;
import com.volantis.mcs.xml.schema.model.AbstractSchema;
import com.volantis.mcs.xml.schema.model.CompositeModel;
import com.volantis.mcs.xml.schema.model.ElementSchema;

/**
 * Test that exclusion mechanism works correctly.
 */
public class ExclusionTestCase
        extends ModelValidationTestAbstract {

    public ExclusionTestCase() {
        super(new ExclusionSchema());
    }

    private static class ExclusionSchema
            extends AbstractSchema {

        ElementSchema a = createElementSchema(TestElements.A);
        ElementSchema b = createElementSchema(TestElements.B);
        ElementSchema c = createElementSchema(TestElements.C);

        public ExclusionSchema() {

            // c is the root element and it can contain any number of a or b.
            c.setContentModel(bounded(choice().add(a).add(b)));

            // b can contain any amount of a, c or PCDATA.
            b.setContentModel(bounded(choice().add(a).add(b).add(PCDATA)));

            // a can contain any amount of b or PCDATA but excludes itself from
            // all nested elements.
            CompositeModel a_content = choice().add(b).add(PCDATA);
            a_content.exclude(choice().add(a));
            a.setContentModel(bounded(a_content));
        }
    }

    public void testExclusionOk()
            throws Exception {

        checkValidationFromFile("xml/exclusion-ok.xml");
    }

    public void testExclusionError()
            throws Exception {

        checkValidationFailsFromFile("xml/exclusion-error.xml",
                ValidationMessages.EXCLUDED_CONTENT,
                new Object[] {
                        TestElements.A,
                        TestElements.A,
                });
    }
}

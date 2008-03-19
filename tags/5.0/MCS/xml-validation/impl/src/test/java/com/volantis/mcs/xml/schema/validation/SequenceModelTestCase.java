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

import com.volantis.mcs.xml.schema.model.CompositeModel;

/**
 * Test that bounded model works correctly.
 */
public class SequenceModelTestCase
        extends ModelValidationTestAbstract {

    public SequenceModelTestCase() {
        super(new SequenceSchema());
    }

    private static class SequenceSchema
            extends TestSchema {

        public SequenceSchema() {
            a.setContentModel(
                    sequence()
                            .add(b)
                            .add(choice().add(c).add(d))
                            .add(bounded(e).optional())
                            .add(bounded(f).atLeastOne()));

            CompositeModel MIXED = choice().add(j).add(g).add(PCDATA);
            g.setContentModel(sequence()
                                   .add(bounded(h).optional())
                                   .add(bounded(i))
                                   .add(bounded(MIXED)));
        }
    }

    public void testSequence1Ok()
            throws Exception {

        checkValidationFromFile("xml/sequence1-ok.xml");
    }

    public void testSequence2Ok()
            throws Exception {

        checkValidationFromFile("xml/sequence2-ok.xml");
    }

    public void testSequence3Ok()
            throws Exception {

        checkValidationFromFile("xml/sequence3-ok.xml");
    }

    public void testSequence4Ok()
            throws Exception {

        checkValidationFromFile("xml/sequence4-ok.xml");
    }
}

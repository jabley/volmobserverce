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

package com.volantis.xml.pipeline.sax.operations.value;

import com.volantis.xml.pipeline.sax.PipelineTestAbstract;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;

/**
 * Test cases for pipeline:value-of operation.
 */
public class ValueOfTestCase
        extends PipelineTestAbstract {

    /**
     * Ensure that it can handle string values.
     */
    public void testStringValue() throws Exception {

        doTest(XMLPipelineFactory.getDefaultInstance(),
                "string-input.xml", "string-expected.xml");
    }

    /**
     * Ensure that it can handle number values.
     */
    public void testNumberValue() throws Exception {

        doTest(XMLPipelineFactory.getDefaultInstance(),
                "number-input.xml", "number-expected.xml");
    }

    /**
     * Ensure that it can handle template variables.
     */
    public void testTemplateVariables() throws Exception {

        doTest(XMLPipelineFactory.getDefaultInstance(),
                "template-variable-input.xml",
                "template-variable-expected.xml");
    }
}

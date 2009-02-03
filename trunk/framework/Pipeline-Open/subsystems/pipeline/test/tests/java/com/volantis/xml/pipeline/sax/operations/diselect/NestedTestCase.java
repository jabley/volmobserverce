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
package com.volantis.xml.pipeline.sax.operations.diselect;

import com.volantis.xml.pipeline.sax.PipelineTestAbstract;
import com.volantis.xml.pipeline.sax.IntegrationTestHelper;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;
import com.volantis.xml.pipeline.sax.ContentWriter;
import com.volantis.xml.pipeline.sax.TestPipelineFactory;

import java.io.Writer;

/**
 * Integration test for flow control related markup
 */
public class NestedTestCase extends PipelineTestAbstract {

    /**
     * Ensure that if there are two dynamic pipelines nested within each other
     * that the DISelect attributes still work properly.
     */
    public void testNestedPipelines() throws Exception {
        doTest(new TestPipelineFactory(),
                "nested-pipeline-input.xml",
                "nested-pipeline-expected.xml");
    }
}
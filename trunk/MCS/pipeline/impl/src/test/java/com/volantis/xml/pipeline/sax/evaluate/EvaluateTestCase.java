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
package com.volantis.xml.pipeline.sax.evaluate;

import com.volantis.xml.pipeline.sax.PipelineTestAbstract;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.IntegrationTestHelper;

/**
 * Test Case for the
 * {@link com.volantis.xml.pipeline.sax.operations.evaluate.EvaluateRule}
 */
public class EvaluateTestCase extends PipelineTestAbstract {

    /**
     * Factory for creating pipeline objects
     */
    protected XMLPipelineFactory pipelineFactory;

    /**
     * Creates a new <code>EvaluateTestCase</code> instance
     * @param name the name of the test case
     */
    public EvaluateTestCase(String name) {
        super(name);
    }

    // javadoc inherited
    public void setUp() throws Exception {
        super.setUp();

        pipelineFactory = new IntegrationTestHelper().getPipelineFactory();
    }

   /**
     * Defencive test to ensure that pipeline markup that is retrieved from
     * from an external source via an XML process is not evaluated when
     *  the XML process is NOT enclosed in an evaluate tag.
     */
    public void testWhenNoEvaluate() throws Exception {
        doTest(pipelineFactory,
                "NoEvaluateTestCase.input.xml",
                "NoEvaluateTestCase.expected.xml");
    }

    /**
     * Ensures that the evaluate tag results in nested markup being processed
     */
    public void testSimpleEvaluate() throws Exception {
        doTest(pipelineFactory,
                "SimpleEvaluateTestCase.input.xml",
                "SimpleEvaluateTestCase.expected.xml");
    }

    /**
     * Ensures that the evaluate tag can be used in a nested fashion
     */
    public void testNestedEvaluate() throws Exception {
        doTest(pipelineFactory,
                "NestedEvaluateTestCase.input.xml",
                "NestedEvaluateTestCase.expected.xml");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 18-Oct-04	906/1	doug	VBM:2004101313 Added an Evaluate pipeline process

 ===========================================================================
*/

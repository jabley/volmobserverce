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
 * $Header: /src/voyager/com/volantis/mcs/protocols/XHTMLBasic.java,v 1.7 2001/10/30 15:16:05 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-May-03    Doug            VBM:2002091803 - Created. Integration test that
 *                              tests the content XML process.
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.content;

import com.volantis.xml.pipeline.sax.IntegrationTestHelper;
import com.volantis.xml.pipeline.sax.PipelineTestAbstract;

/**
 * Test case for the Content process
 */ 
public class SimpleContentTestCase
        extends PipelineTestAbstract {

    /**
     * Creates a new SimpleContentTestCase instance
     * @param name
     */ 
    public SimpleContentTestCase(String name) {
        super(name);
    }
    
    /**
     * Tests a simple content process 
     * @throws Exception if an error occurs
     */ 
    public void testSimpleContent() throws Exception {
        doTest(new IntegrationTestHelper().getPipelineFactory(),
               "SimpleContentTestCase.input.xml", 
               "SimpleContentTestCase.expected.xml");               
    }
    
    /**
     * Ensures that nested pipeline markup is processed
     * @throws Exception if an error occurs
     */ 
    public void testNestedContent()
            throws Exception {

        doTest(new IntegrationTestHelper().getPipelineFactory(),
               "SimpleContentTestCase.nested_input.xml", 
               "SimpleContentTestCase.nested_expected.xml");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 01-Aug-03	258/1	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 12-Jun-03	53/1	doug	VBM:2003050603 JSP ContentTag refactoring

 ===========================================================================
*/

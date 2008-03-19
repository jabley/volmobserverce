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
 * 12-May-03    Doug           VBM:2002091803 - Created. Base class for 
 *                             integration tests that wish to test the 
 *                             fetch process.
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.drivers.uri;

import com.volantis.xml.pipeline.sax.IntegrationTestHelper;
import com.volantis.xml.pipeline.sax.PipelineTestAbstract;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;

/**
 * Test case for the fetch process
 */ 
public class FetchTestCase
        extends PipelineTestAbstract {

    /**
     * Factory used to create pipeline objects
     */ 
    protected XMLPipelineFactory pipelineFactory;

    // javadoc inherited
    public void setUp() throws Exception {
        super.setUp();

        pipelineFactory = new IntegrationTestHelper().getPipelineFactory();
    }

    /**
     * Tests a simple xml inclusion
     * @throws Exception
     */ 
    public void testSimpleXMLInclusion() throws Exception {
        doTest(pipelineFactory,
               "SimpleXMLIncludeTestCase.input.xml",
               "SimpleXMLIncludeTestCase.expected.xml");        
    }
    
    /**
     * Test that a text file can be included.
     * @throws Exception if an error occurs
     */ 
    public void testSimpleTextInclusion() throws Exception {
        doTest(pipelineFactory,
               "SimpleTextIncludeTestCase.input.xml",
               "SimpleTextIncludeTestCase.expected.xml");                
    }
    
    /**
     * Ensures that nested inclusion can be processed
     * @throws Exception if an error occurs
     */ 
    public void testNestedXMLInclude() throws Exception {
        doTest(pipelineFactory,
               "NestedXMLIncludeTestCase.input.xml",
               "NestedXMLIncludeTestCase.expected.xml");        
    }
    
    /**
     * Ensures that cyclic inclusions generate an error.
     * @throws Exception if an error occurs
     */ 
    public void testInclusionLoop() throws Exception {
        CyclicInclusionException exception = null;
        try {
            doTest(pipelineFactory, 
                   "InclusionLoopTestCaseInvalid.input.xml", 
                   "InclusionLoopTestCase.expected.xml");
        } catch(CyclicInclusionException e) {
            exception = e;
        }        
        assertNotNull(exception);        
    }
    
    public void test() throws Exception {
        
    }
    
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 01-Aug-03	258/1	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 24-Jun-03	109/4	philws	VBM:2003061913 Change pipeline:includeURI to urid:fetch and add new TLD for it

 12-Jun-03	53/2	doug	VBM:2003050603 JSP ContentTag refactoring

 06-Jun-03	26/2	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/

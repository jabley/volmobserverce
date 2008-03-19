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
 * $Header: $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who         Description
 * -----------  ----------- -------------------------------------------------
 * 29-May-2003  Sumit       VBM:2003030612
 * ----------------------------------------------------------------------------
 */
 
package com.volantis.xml.pipeline.sax.include;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.volantis.servlet.HttpServletRequestMock;
import com.volantis.servlet.HttpServletResponseStub;
import com.volantis.servlet.RequestDispatcherStub;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.TestPipelineFactory;
import com.volantis.xml.pipeline.sax.XMLPipelineProcess;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.shared.environment.EnvironmentInteraction;

/**
 * Test case for the <code>IncludeSrvResOperationProcess</code> class
 */
public class IncludeSrvResOperationProcessTestCase extends TestCaseAbstract {

    /**
     * XMLPipelineFactor that this test requires
     */ 
    protected XMLPipelineFactory factory;
    
    /**
     * An XMLPipelineContext that this test requires
     */ 
    protected XMLPipelineContext context;
    
    /**
     * Constructor for IncludeSrvResOperationProcessTestCase.
     * @param arg0
     */
    public IncludeSrvResOperationProcessTestCase(String arg0) {
        super(arg0);         
    }

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();
        factory = new TestPipelineFactory();
        // create an empty pipeline configuration
        XMLPipelineConfiguration pipelineConfig =
                factory.createPipelineConfiguration();
        // ok to use null interaction for this test
        EnvironmentInteraction rootInteraction = null;
                
        context = factory.createPipelineContext(pipelineConfig, 
                                                rootInteraction);    
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
        factory = null;
        context = null;
    }

    public void testSetPath() {
        IncludeSrvResOperationProcess process = new IncludeSrvResOperationProcess();
        process.setPath("/testcase");
        assertTrue("/testcase".equals(process.getPath()));
    }

    public void testGetPath() {
        IncludeSrvResOperationProcess process = new IncludeSrvResOperationProcess();
        process.setPath("/testcase");
        assertTrue("/testcase".equals(process.getPath()));
    }

    public void testSetParameter() {
        IncludeSrvResOperationProcess process = new IncludeSrvResOperationProcess();
        process.setParameter("param1", "value1");
        assertTrue(process.getParameter("param1").equals("value1"));
    }

    public void testGetParameter() {
        IncludeSrvResOperationProcess process = new IncludeSrvResOperationProcess();
        process.setParameter("param1", "value1");
        assertTrue(process.getParameter("param1").equals("value1"));
    }

    public void testDoIncludeServerResourceWithParam() throws Exception {
        IncludeSrvResOperationProcess process = new IncludeSrvResOperationProcess();
        process.setPath("/testcase");
        process.setParameter("param1", "value1");
        IncludeSrvResRequest request = new IncludeSrvResRequest();
        request.setExpectedURI("/testcase?param1=value1");
        context.setProperty(ServletRequest.class, request, false);
        context.setProperty(ServletResponse.class, 
                new HttpServletResponseStub(), false);
        XMLPipeline pipeline = factory.createPipeline(context); 
        process.setPipeline(pipeline);
        process.doIncludeServerResource();
        
    }
    
    public void testDoIncludeServerResourceWithDuplicateParam() throws Exception {
        IncludeSrvResOperationProcess process = new IncludeSrvResOperationProcess();
        process.setPath("/testcase?param1=value2");
        process.setParameter("param1", "value1");
        IncludeSrvResRequest request = new IncludeSrvResRequest();
        request.setExpectedURI("/testcase?param1=value1");
        context.setProperty(ServletRequest.class, request, false);
        context.setProperty(ServletResponse.class, 
                new HttpServletResponseStub(), false);
        XMLPipeline pipeline = factory.createPipeline(context);        
        process.setPipeline(pipeline);
        process.doIncludeServerResource();
    
    }
    
    public void testDoIncludeServerResourceWithParentURI() throws Exception {
        IncludeSrvResOperationProcess process = new IncludeSrvResOperationProcess();
        process.setPath("../testcase");
        process.setParameter("param1", "value1");
        IncludeSrvResRequest request = new IncludeSrvResRequest();
        request.setAttribute("javax.servlet.include.request_uri","/parent");
        request.setExpectedURI("/parent/../testcase?param1=value1");
        context.setProperty(ServletRequest.class, request, false);
        context.setProperty(ServletResponse.class, 
                new HttpServletResponseStub(), false);
        XMLPipeline pipeline = factory.createPipeline(context);        
        process.setPipeline(pipeline);
        process.doIncludeServerResource();
    }
    
    private class IncludeSrvResRequest extends HttpServletRequestMock {
        
        private String expectedURI;

        public RequestDispatcher getRequestDispatcher(String arg0) {
            assertTrue("The expected URI " + expectedURI 
                + " does not match the incoming URI "+arg0, expectedURI.equals(arg0));
            return new RequestDispatcherStub();
        }
        
        public void setExpectedURI(String expected) {
            expectedURI = expected;
        }
    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 10-Aug-03	264/1	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 01-Aug-03	258/2	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 18-Jul-03	213/2	doug	VBM:2003071615 Refactored XMLProcess interface

 10-Jul-03	179/1	sumit	VBM:2003070407 Fixed IncludeSrvRes process to handle params

 06-Jun-03	26/1	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/

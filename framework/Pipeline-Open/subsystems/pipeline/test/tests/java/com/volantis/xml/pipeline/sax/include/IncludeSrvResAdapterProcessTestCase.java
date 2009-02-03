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
 * 29-May-2003  Sumit       VBM:2003030612 - Created
 * ----------------------------------------------------------------------------
 */
 
package com.volantis.xml.pipeline.sax.include;

import org.xml.sax.helpers.AttributesImpl;

import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineProcessImpl;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.TestPipelineFactory;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.adapter.AbstractAdapterProcessTestAbstract;
import com.volantis.shared.environment.EnvironmentInteraction;

/**
 * Tests the functionality of the IncludeSrvResAdapterProcess
 */
public class IncludeSrvResAdapterProcessTestCase 
                    extends AbstractAdapterProcessTestAbstract {

    /**
     * XMLPipelineFactor that this test requires
     */ 
    protected XMLPipelineFactory factory;
    
    /**
     * An XMLPipelineContext that this test requires
     */ 
    protected XMLPipelineContext context;
    
    /**
     * Constructor for IncludeSrvResAdapterProcessTestCase.
     * @param arg0
     */
    public IncludeSrvResAdapterProcessTestCase(String arg0) {
        super(arg0);
    }
    
    /**
     * Tests the processAttributes method
     * @throws Exception if an error occurs
     */ 
    public void testProcessAttributes() throws Exception {
        IncludeSrvResAdapterProcess process = 
                        (IncludeSrvResAdapterProcess)createTestableProcess(); 
                
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute("","path","path","","/testcase?param1=value1");
        process.processAttributes(atts);
        IncludeSrvResOperationProcess op = (IncludeSrvResOperationProcess) 
                                            process.getDelegate();
        assertTrue("/testcase?param1=value1".equals(op.getPath()));                                            
                                                    
    }

    public XMLProcess createTestableProcess() {
        return new IncludeSrvResAdapterProcess();
    }
    
    public void testStopProcess() throws Exception {
        // Don't test this
    }
    
    public void testStartElement() throws Exception {
        super.testStartElement();
        IncludeSrvResAdapterProcess process = (IncludeSrvResAdapterProcess) 
                                            createTestableProcess();
        process.setElementDetails("testURI", null, null);                                             
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute("","name","name","","param1");                                                    
        atts.addAttribute("","value","value","","value1");
        process.startElement(URI,"param","param",atts);
        IncludeSrvResOperationProcess op = (IncludeSrvResOperationProcess) 
                                            process.getDelegate();
        String value =  op.getParameter("param1");                                               
        assertTrue("Parameter value for param1 must be value1 not "+value, 
                        "value1".equals(value));                                                
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 11-Aug-03	275/1	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 01-Aug-03	258/2	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 18-Jul-03	213/2	doug	VBM:2003071615 Refactored XMLProcess interface

 06-Jun-03	26/1	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/

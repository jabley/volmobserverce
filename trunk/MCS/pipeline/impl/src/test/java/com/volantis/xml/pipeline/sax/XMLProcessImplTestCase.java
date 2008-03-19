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
 * 12-May-03    Doug            VBM:2003030405 - Created. TestCase for the
 *                              XMLProcessImpl class 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax;

import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.shared.environment.EnvironmentInteraction;

/**
 * TestCase for the XMLProcessImpl class. 
 */ 
public class XMLProcessImplTestCase extends XMLProcessTestAbstract {

    /**
     * Created a new XMLProcessTestAbstract instance
     * @param name the name
     * @deprecated
     */
    public XMLProcessImplTestCase(String name) {
        setName(name);
    }

    public XMLProcessImplTestCase() {
    }

    /**
     * Factory method that returns an instance of the class being tested.
     * Sub classes can override this to return an instance of the class
     * that they wish to test.
     * @return
     */ 
    protected XMLProcess createTestableProcess() {
        return new XMLProcessImpl();
    }


    /**
     * Method to test the setPipeline(PipelineProcess) method.
     * @throws Exception if an error occurs
     */ 
    public void testSetPipeline() throws Exception {
        XMLProcessImpl testable = (XMLProcessImpl)createTestableProcess();
        initializeProcess(testable);
        
        XMLProcessTestable next = new XMLProcessTestable();
        testable.setNextProcess(next);        
                
        next.assertSetPipelineNotInvoked();
    }

    /**
     * Initialize a process so that subsequent (post setPipeline methods) can
     * be tested with an expected setup.
     * @param testable
     */ 
    protected void initializeProcess(XMLProcessImpl testable) {
        XMLPipelineConfiguration pipelineConfig =
                factory.createPipelineConfiguration();
        
        // allow sublcasses to register configurations
        registerConfiguration(pipelineConfig);
        
        EnvironmentInteraction rooInteraction = null;
        
        XMLPipelineContext context =  
                factory.createPipelineContext(pipelineConfig, rooInteraction);
        
        XMLPipelineProcess pipelineProcess = 
                new XMLPipelineProcessImpl(context);
        testable.setPipeline(pipelineProcess);
        assertSame("setPipeline() should set the pipelineProcess ", 
                   pipelineProcess, testable.getPipeline());

    }

    /**
     * Sublcasses should override this method to register the processes
     * configuration if the process being tested require a configuration
     * 
     * @param configuration the XMLPipelineConfiguration in which the 
     * processes configuration should be stored
     */ 
    protected void registerConfiguration(
            XMLPipelineConfiguration configuration) {            
    }
    /**
     * Method to test the release() method.
     * @throws Exception if an error occurs
     */ 
    public void testRelease() throws Exception {
        XMLProcessImpl testable = (XMLProcessImpl)createTestableProcess();
        
        testable.release();
        
        assertNull("release() should null the pipeline", 
                   testable.getPipeline());
        
        assertNull("release() should null the next member", 
                   testable.getNextProcess());
                                                           
        testable = (XMLProcessImpl)createTestableProcess();
        XMLProcessTestable next = new XMLProcessTestable();
        testable.setNextProcess(next);        
        testable.release();
        
        next.assertReleaseNotInvoked();        
    }

    
    /**
     * Method to test the startProcess method.
     * @throws Exception if an error occurs
     */ 
    public void testStartProcess() throws Exception {
        XMLProcessImpl testable = (XMLProcessImpl)createTestableProcess();

        XMLProcessTestable next = new XMLProcessTestable();
        testable.setNextProcess(next);
        
        testable.startProcess();        
        next.assertStartProcessNotInvoked(); 
    }

    public void testStopProcess() throws Exception {
        XMLProcessImpl testable = (XMLProcessImpl)createTestableProcess();
        
        XMLProcessTestable next = new XMLProcessTestable();
        testable.setNextProcess(next);
        
        testable.stopProcess();        
        next.assertStopProcessNotInvoked();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 11-Aug-03	275/1	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 06-Aug-03	301/1	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 01-Aug-03	258/2	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 22-Jul-03	225/1	doug	VBM:2003071805 Refactored the XMLPipeline interface to reflect the new public API

 18-Jul-03	213/2	doug	VBM:2003071615 Refactored XMLProcess interface

 11-Jun-03	34/3	allan	VBM:2003022820 Fix problem caused by poor test case design unrelated to SQLConnector.

 11-Jun-03	34/1	allan	VBM:2003022820 SQL Connector

 06-Jun-03	26/1	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/

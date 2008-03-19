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
 *                              XMLProcessImpl class.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax;

/**
 * TestCase for the XMLProcessImpl class. 
 */ 
public class XMLPipelineProcessImplTestCase extends XMLProcessImplTestCase {


    protected void setUp() throws Exception {
        super.setUp();


    }

    // javadoc inherited from superclass
    protected XMLProcess createTestableProcess() {
        return new XMLPipelineProcessImpl(createPipelineContext());
    }

    /**
     * Creates an XMLPipelineProcess that is nestable
     * @return an XMLPipelineProcess
     */ 
    protected XMLPipelineProcess createNestableTestableProcess() {
        return new XMLPipelineProcessImpl();                        
    }

    /**
     * Test that ensures the getHeadProcess() method throws an 
     * IllegalStateException if the pipeline is empty.
     * @throws Exception if an error occurs
     */ 
    public void testGetHeadProcessEmptyPipeline() throws Exception {
        // get hold of a XMLPipelineProcess
        XMLPipelineProcess pipeline = 
                (XMLPipelineProcessImpl)createTestableProcess();
        
        IllegalStateException ise = null;
        try {
            pipeline.getHeadProcess();
        } catch (IllegalStateException e) {
            ise = e;
        }
        
        assertNull("getHeadProcess() should throw an IllegalStateException" +
                   " if the pipeline is empty", ise);
    }
    
    /**
     * Test the getHeadProcess() method
     * @throws Exception if an error occurs
     */ 
    public void testGetHeadProcess() throws Exception {
        // create some processes
        XMLProcess p1 = new XMLProcessTestable();
        XMLProcess p2 = new XMLProcessTestable();
        XMLProcess p3 = new XMLProcessTestable();
        
        // get hold of a XMLPipelineProcess
        XMLPipelineProcess pipeline = 
                (XMLPipelineProcessImpl)createTestableProcess();
         
        // add a process to the head of the pipeline
        pipeline.addHeadProcess(p1);
        
        // ensure the getHeadProcess() method returns the process that
        // was just added
        assertEquals("getHeadProcess() should return process p1",
                     p1, pipeline.getHeadProcess());
        
        // add another process to the head of the pipeline
        pipeline.addHeadProcess(p2);
        
        // ensure the getHeadProcess() method returns the second process 
        // that was just added 
        assertEquals("getHeadProcess() should return process p2",
                     p2, pipeline.getHeadProcess());
        
        // add a third process to the TAIL of the pipeline
        pipeline.addTailProcess(p3);
        // ensure the getHeadProcess() method returns the second process 
        // that was added 
        assertEquals("getHeadProcess() should return process p2 as p3 was " +
                     "added to the tail", p2, pipeline.getHeadProcess());        
        
        
    }
        
    /**
     * Test that ensures the getTailProcess() method throws an 
     * IllegalStateException if the pipeline is empty.
     * @throws Exception if an error occurs
     */    
    public void testGetTailProcessEmptyPipeline() throws Exception {
        // get hold of a XMLPipelineProcess
        XMLPipelineProcess pipeline = 
                (XMLPipelineProcessImpl)createTestableProcess();
        
        IllegalStateException ise = null;
        try {
            pipeline.getTailProcess();
        } catch (IllegalStateException e) {
            ise = e;
        }
        
        assertNull("getTailProcess() should throw an IllegalStateException" +
                   " if the pipeline is empty", ise);
    }
    
    /**
     * Test the getHeadProcess() method
     * @throws Exception if an error occurs
     */
    public void testGetTailProcess() throws Exception {
        // create some processes
        XMLProcess p1 = new XMLProcessTestable();
        XMLProcess p2 = new XMLProcessTestable();
        XMLProcess p3 = new XMLProcessTestable();
        
        // get hold of a XMLPipelineProcess
        XMLPipelineProcess pipeline = 
                (XMLPipelineProcessImpl)createTestableProcess();
         
        // add a process to the head of the pipeline
        pipeline.addHeadProcess(p1);
        
        // ensure the getTailProcess() method returns the process that
        // was just added
        assertEquals("getTailProcess() should return process p1",
                     p1, pipeline.getTailProcess());
        
        // add another process to the TAIL of the pipeline
        pipeline.addTailProcess(p2);
        
        // ensure the getTailProcess() method returns the second process 
        // that was just added 
        assertEquals("getTailProcess() should return process p2",
                     p2, pipeline.getTailProcess());
        
        // add a third process to the HEAD of the pipeline
        pipeline.addHeadProcess(p3);
        // ensure the getTailProcess() method returns the second process 
        // that was added 
        assertEquals("getTailProcess() should return process p2 as p3 was " +
                     "added to the head", p2, pipeline.getTailProcess());        
        
        
    }
    
    /**
     * Method that tests the getAddHeadProcess() method
     * @throws Exception if an error occurs
     */ 
    public void testAddHeadProcess() throws Exception {
        // create some processes
        XMLProcessTestable p1 = new XMLProcessTestable("p1");
        XMLProcessTestable p2 = new XMLProcessTestable("p2");
        XMLProcessTestable next = new XMLProcessTestable("next");
        
        XMLPipelineProcess pipeline = 
                (XMLPipelineProcessImpl)createTestableProcess();        
        
        // add the process to the pipeline
        pipeline.setNextProcess(next);
        pipeline.addHeadProcess(p2);
        // ensure that the correct methods were invoked.
        p2.assertSetPipelineInvoked(pipeline);
        p2.assertStartProcessInvoked();
        
        pipeline.addHeadProcess(p1);
        // ensure that the correct methods were invoked.
        p1.assertSetPipelineInvoked(pipeline);
        p1.assertStartProcessInvoked();
        
        char[] chars = "characters".toCharArray();
        pipeline.characters(chars, 0, chars.length -1);
        p1.assertCharactersInvoked(chars, 0, chars.length -1);
        p2.assertCharactersInvoked(chars, 0, chars.length -1);
        next.assertCharactersInvoked(chars, 0, chars.length -1);
    }
    
    /**
     * Tests the addHeadProcess with a nestable XMLPipelineProcess that
     * hasn't yet been nested inside another pipeline
     * @throws Exception if an error occurs
     */ 
    public void testAddHeadProcessNestableButNotNested() throws Exception {
        
        // create some processes
        XMLProcessTestable p1 = new XMLProcessTestable("p1");
        XMLProcessTestable p2 = new XMLProcessTestable("p2");
        XMLProcessTestable next = new XMLProcessTestable("next");
        
        // create a nestable process 
        XMLPipelineProcess pipeline = createNestableTestableProcess();
        
        // add some process to the pipeline
        pipeline.setNextProcess(next);
        next.assertSetPipelineNotInvoked();
        next.assertStartProcessNotInvoked();        
        
        // add a process to the pipeline
        pipeline.addHeadProcess(p2);
        
        // ensure that the correct methods were invoked.
        p2.assertSetPipelineInvoked(pipeline);
        p2.assertStartProcessNotInvoked();
        
        // add another process to the pipeline
        pipeline.addHeadProcess(p1);
        
        // ensure that the correct methods were invoked.
        p1.assertSetPipelineInvoked(pipeline);
        p1.assertStartProcessNotInvoked();
        
        // nest the pipeline process in a parent pipeline
        XMLPipelineProcess parentPipeline = 
                (XMLPipelineProcessImpl)createTestableProcess();
        
        parentPipeline.addHeadProcess(pipeline);
        
        // ensure the processes where started and pas        
        p2.assertStartProcessInvoked();                
        p1.assertStartProcessInvoked();

        char[] chars = "characters".toCharArray();
        pipeline.characters(chars, 0, chars.length -1);
        p1.assertCharactersInvoked(chars, 0, chars.length -1);
        p2.assertCharactersInvoked(chars, 0, chars.length -1);
        next.assertCharactersInvoked(chars, 0, chars.length -1);
    }
    
    /**
     * Tests the addHeadProcess with a nestable XMLPipelineProcess that
     * has been nested inside another pipeline
     * @throws Exception if an error occurs
     */
    public void testAddHeadProcessNestableAndNested() throws Exception {
        
        // create some processes
        XMLProcessTestable p1 = new XMLProcessTestable("p1");
        XMLProcessTestable p2 = new XMLProcessTestable("p2");
        XMLProcessTestable next = new XMLProcessTestable("next");
        
        // create a nestable process 
        XMLPipelineProcess pipeline = createNestableTestableProcess();
        
        XMLPipelineProcess parentPipeline = 
                (XMLPipelineProcessImpl)createTestableProcess();
        
        // nest the pipeline process in the parent pipeline        
        parentPipeline.addHeadProcess(pipeline);
        
        // add some process to the pipeline
        pipeline.setNextProcess(next);        
        next.assertStartProcessNotInvoked();        
        
        // add a process to the pipeline
        pipeline.addHeadProcess(p2);
        
        p2.assertSetPipelineInvoked(pipeline);
        p2.assertStartProcessInvoked();
        
        // add another process to the pipeline
        pipeline.addHeadProcess(p1);
        
        p1.assertSetPipelineInvoked(pipeline);
        p1.assertStartProcessInvoked();
        
        char[] chars = "characters".toCharArray();
        pipeline.characters(chars, 0, chars.length -1);
        p1.assertCharactersInvoked(chars, 0, chars.length -1);
        p2.assertCharactersInvoked(chars, 0, chars.length -1);
        next.assertCharactersInvoked(chars, 0, chars.length -1);
    }
    
    /**
     * Tests the addTailProcess with a nestable XMLPipelineProcess that
     * hasn't yet been nested inside another pipeline
     * @throws Exception if an error occurs
     */ 
    public void testAddTailProcessNestableButNotNested() throws Exception {
        
        // create some processes
        XMLProcessTestable p1 = new XMLProcessTestable("p1");
        XMLProcessTestable p2 = new XMLProcessTestable("p2");
        XMLProcessTestable next = new XMLProcessTestable("next");
        
        // create a nestable process 
        XMLPipelineProcess pipeline = createNestableTestableProcess();
        
        // add some process to the pipeline
        pipeline.setNextProcess(next);
        next.assertSetPipelineNotInvoked();
        next.assertStartProcessNotInvoked();        
        
        // add a process to the pipeline
        pipeline.addTailProcess(p2);
        
        // ensure that the correct methods were invoked.
        p2.assertSetPipelineInvoked(pipeline);
        p2.assertStartProcessNotInvoked();
        
        // add another process to the pipeline
        pipeline.addTailProcess(p1);
        
        // ensure that the correct methods were invoked.
        p1.assertSetPipelineInvoked(pipeline);
        p1.assertStartProcessNotInvoked();
        
        // nest the pipeline process in a parent pipeline
        XMLPipelineProcess parentPipeline = 
                (XMLPipelineProcessImpl)createTestableProcess();
        
        parentPipeline.addTailProcess(pipeline);
        
        // ensure the processes where started
        p2.assertStartProcessInvoked();                       
        p1.assertStartProcessInvoked();        
        
        char[] chars = "characters".toCharArray();
        pipeline.characters(chars, 0, chars.length -1);
        p1.assertCharactersInvoked(chars, 0, chars.length -1);
        p2.assertCharactersInvoked(chars, 0, chars.length -1);
        next.assertCharactersInvoked(chars, 0, chars.length -1);
    }
    
    /**
     * Tests the addTailProcess with a nestable XMLPipelineProcess that
     * has been nested inside another pipeline
     * @throws Exception if an error occurs
     */
    public void testAddTailProcessNestableAndNested() throws Exception {
        // create some processes
        XMLProcessTestable p1 = new XMLProcessTestable("p1");
        XMLProcessTestable p2 = new XMLProcessTestable("p2");
        XMLProcessTestable next = new XMLProcessTestable("next");
        
        // create a nestable process 
        XMLPipelineProcess pipeline = createNestableTestableProcess();
        
        XMLPipelineProcess parentPipeline = 
                (XMLPipelineProcessImpl)createTestableProcess();
        
        // nest the pipeline process in the parent pipeline        
        parentPipeline.addTailProcess(pipeline);
        
        // add some process to the pipeline
        pipeline.setNextProcess(next);
        next.assertSetPipelineNotInvoked();
        next.assertStartProcessNotInvoked();        
        
        // add a process to the pipeline
        pipeline.addTailProcess(p2);
        
        p2.assertSetPipelineInvoked(pipeline);
        p2.assertStartProcessInvoked();
        
        // add another process to the pipeline
        pipeline.addTailProcess(p1);
        
        p1.assertSetPipelineInvoked(pipeline);
        p1.assertStartProcessInvoked();
        
        char[] chars = "characters".toCharArray();
        pipeline.characters(chars, 0, chars.length -1);
        p1.assertCharactersInvoked(chars, 0, chars.length -1);
        p2.assertCharactersInvoked(chars, 0, chars.length -1);
        next.assertCharactersInvoked(chars, 0, chars.length -1);
    }
    
    /**
     * Method that tests the addTailProcess()  
     * @throws Exception
     */    
    public void testAddTailProcess() throws Exception {
        // create some processes
        XMLProcessTestable p1 = new XMLProcessTestable("p1");
        XMLProcessTestable p2 = new XMLProcessTestable("p2");
        XMLProcessTestable next = new XMLProcessTestable("next");
        
        XMLPipelineProcess pipeline = 
                (XMLPipelineProcessImpl)createTestableProcess();        
        
        // add the process to the pipeline
        pipeline.setNextProcess(next);
        pipeline.addTailProcess(p2);
        // ensure that the correct methods were invoked.
        p2.assertSetPipelineInvoked(pipeline);
        p2.assertStartProcessInvoked();
        
        pipeline.addTailProcess(p1);
        // ensure that the correct methods were invoked.
        p1.assertSetPipelineInvoked(pipeline);
        p1.assertStartProcessInvoked();
        
        char[] chars = "characters".toCharArray();
        pipeline.characters(chars, 0, chars.length -1);
        p1.assertCharactersInvoked(chars, 0, chars.length -1);
        p2.assertCharactersInvoked(chars, 0, chars.length -1);
        next.assertCharactersInvoked(chars, 0, chars.length -1);
    }

    /**
     * Tests the removeHeadProcess() method. 
     * @throws Exception if an error occurs
     */ 
    public void testRemoveHeadProcessProcess() throws Exception {
        
        // create some processes
        XMLProcessTestable p1 = new XMLProcessTestable("p1");
        XMLProcessTestable p2 = new XMLProcessTestable("p2");
        XMLProcessTestable next = new XMLProcessTestable("next");
        
        XMLPipelineProcess pipeline = 
                (XMLPipelineProcessImpl)createTestableProcess();        
        
        // add the process to the pipeline
        pipeline.setNextProcess(next);
        pipeline.addHeadProcess(p2);        
        pipeline.addHeadProcess(p1);
        
        char[] chars = "characters".toCharArray();
        pipeline.characters(chars, 0, chars.length -1);
        p1.assertCharactersInvoked(chars, 0, chars.length -1);
        p2.assertCharactersInvoked(chars, 0, chars.length -1);
        next.assertCharactersInvoked(chars, 0, chars.length -1);
        
        // remove the head process
        XMLProcessTestable removed = 
                (XMLProcessTestable)pipeline.removeHeadProcess();
        removed.assertStopProcessInvoked();
        assertEquals("Process p1 should have been removed", p1, removed);

        p1.reset();
        chars = "characters2".toCharArray();
        pipeline.characters(chars, 0, chars.length -1);
        p1.assertCharactersNotInvoked();
        p2.assertCharactersInvoked(chars, 0, chars.length -1);
        next.assertCharactersInvoked(chars, 0, chars.length -1);
        
        // remove the next head process
        removed = (XMLProcessTestable)pipeline.removeHeadProcess();
        removed.assertStopProcessInvoked();
        assertEquals("Process p2 should have been removed", p2, removed);

        p2.reset();
        chars = "characters3".toCharArray();
        pipeline.characters(chars, 0, chars.length -1);
        p1.assertCharactersNotInvoked();
        p2.assertCharactersNotInvoked();
        next.assertCharactersInvoked(chars, 0, chars.length -1);
        
        // ensure that an IllegalStateException is thrown if you
        // try to remove a process from an empty pipeline
        IllegalStateException ise = null;
        try {
            pipeline.removeHeadProcess();
        } catch (IllegalStateException e) {
            ise = e;
        }
        assertNotNull("Exception should be thrown if you try to remove " +
                      "a process from an empty pipeline", ise);            
        
    }    
     
    /**
     * Tests the removeTailProcess() method. 
     * @throws Exception if an error occurs
     */
    public void testRemoveTailProcessProcess() throws Exception {    
        // create some processes
        XMLProcessTestable p1 = new XMLProcessTestable("p1");
        XMLProcessTestable p2 = new XMLProcessTestable("p2");
        XMLProcessTestable next = new XMLProcessTestable("next");
        
        XMLPipelineProcess pipeline = 
                (XMLPipelineProcessImpl)createTestableProcess();        
        
        // add the process to the pipeline
        pipeline.setNextProcess(next);
        pipeline.addHeadProcess(p2);        
        pipeline.addHeadProcess(p1);
        
        char[] chars = "characters".toCharArray();
        pipeline.characters(chars, 0, chars.length -1);
        p1.assertCharactersInvoked(chars, 0, chars.length -1);
        p2.assertCharactersInvoked(chars, 0, chars.length -1);
        next.assertCharactersInvoked(chars, 0, chars.length -1);

        // remove the head process
        XMLProcessTestable removed =
                (XMLProcessTestable)pipeline.removeHeadProcess();
        removed.assertStopProcessInvoked();
        assertEquals("Process p1 should have been removed", p1, removed);

        p1.reset();
        chars = "characters2".toCharArray();
        pipeline.characters(chars, 0, chars.length -1);
        p1.assertCharactersNotInvoked();
        p2.assertCharactersInvoked(chars, 0, chars.length -1);
        next.assertCharactersInvoked(chars, 0, chars.length -1);

        // remove the next head process
        removed = (XMLProcessTestable)pipeline.removeHeadProcess();
        removed.assertStopProcessInvoked();
        assertEquals("Process p2 should have been removed", p2, removed);

        p2.reset();
        chars = "characters3".toCharArray();
        pipeline.characters(chars, 0, chars.length -1);
        p1.assertCharactersNotInvoked();
        p2.assertCharactersNotInvoked();
        next.assertCharactersInvoked(chars, 0, chars.length -1);
            
        // ensure that an IllegalStateException is thrown if you
        // try to remove a process from an empty pipeline
        IllegalStateException ise = null;
        try {
            pipeline.removeTailProcess();
        } catch (IllegalStateException e) {
            ise = e;
        }
        assertNotNull("Exception should be thrown if you try to remove " +
                      "a process from an empty pipeline", ise);            

    }
    
    // javadoc inherited
    public void testStartProcess() throws Exception {
        // create a testable pipeline
        XMLPipelineProcess pipeline = 
                (XMLPipelineProcessImpl)createTestableProcess();
        
        // create a process and add it to the pipeline
        XMLProcessTestable p1 = new XMLProcessTestable();        
        pipeline.addHeadProcess(p1);
        
        // p1 should have been started
        p1.assertStartProcessInvoked();
        
        // reset p1
        p1.reset();
        
        // start the pipeline process and test that the process was not 
        // started
        p1.assertStartProcessNotInvoked();
        pipeline.startProcess();
        p1.assertStartProcessNotInvoked();                        
    }
    
    /**
     * Test the startProcess method with a nestable pipeline.
     * @throws Exception if an error occurs.
     */ 
    public void testStartProcessWithNestable() throws Exception {
        // create a testable pipeline
        XMLPipelineProcess nestable = createNestableTestableProcess();
        
        // create a process and add it to the pipeline
        final XMLProcessMock processMock =
                new XMLProcessMock("processMock", expectations);

        // When the process is added to a pipeline then the process is
        // associated with the pipeline but as the pipeline does not have a
        // context it is not started.
        processMock.expects.setPipeline(nestable);
        nestable.addHeadProcess(processMock);

        // Create a pipeline into which the nestable pipeline is added.
        XMLPipelineProcess pipeline =
                (XMLPipelineProcessImpl) createTestableProcess();

        // When the pipeline process is added to another process that does
        // have a context then the pipline process and all processes that
        // were previously added to it are started.
        processMock.expects.startProcess();

        pipeline.addHeadProcess(nestable);
    }
 
    // javadoc inherited
    public void testStopProcess() throws Exception {
        // create a testable pipeline
        XMLPipelineProcess pipeline = 
                (XMLPipelineProcessImpl)createTestableProcess();

        final XMLProcessMock processMock =
                new XMLProcessMock("processMock", expectations);

        // create a process and add it to the pipeline
        processMock.expects.setPipeline(pipeline);
        processMock.expects.startProcess();
        pipeline.addHeadProcess(processMock);

        pipeline.startProcess();

        processMock.expects.stopProcess();
        pipeline.stopProcess();
    }

    // javadoc inherited
    public void testCharacters() throws Exception {
        super.testCharacters();

        XMLPipelineProcess testable = (XMLPipelineProcess) createTestableProcess();
        
        // create some XMLProcess to push onto the pipeline        
        XMLProcessTestable p1 = new XMLProcessTestable();
        XMLProcessTestable p2 = new XMLProcessTestable();
        
        // push the Processes
        testable.addHeadProcess(p1);
        testable.addHeadProcess(p2);
                
        //  set up  a next process for the pipeline
        XMLProcessTestable next = new XMLProcessTestable();
        testable.setNextProcess(next);
        
        // defensive checks
        p1.assertCharactersNotInvoked();
        p2.assertCharactersNotInvoked();
        next.assertCharactersNotInvoked();        
        
        // send the appropriate event down the pipeline.
        testCharacters(testable);
        
        // check that each process received the event 
        next.assertCharactersInvoked(CH, START, LENGTH);
        p1.assertCharactersInvoked(CH, START, LENGTH);
        p2.assertCharactersInvoked(CH, START, LENGTH);
    }
     
    // javadoc inherited
    public void testEndDocument() throws Exception {
        super.testEndDocument();

        XMLPipelineProcess testable = (XMLPipelineProcess) createTestableProcess();
        
        // create some XMLProcess to push onto the pipeline        
        XMLProcessTestable p1 = new XMLProcessTestable();
        XMLProcessTestable p2 = new XMLProcessTestable();
        
        // push the Processes
        testable.addHeadProcess(p1);
        testable.addHeadProcess(p2);
                
        //  set up  a next process for the pipeline
        XMLProcessTestable next = new XMLProcessTestable();
        testable.setNextProcess(next);
        
        // defensive checks
        p1.assertEndDocumentNotInvoked();
        p2.assertEndDocumentNotInvoked();
        next.assertEndDocumentNotInvoked();
        
        // send the appropriate event down the pipeline.
        testEndDocument(testable);
        
        // check that each process does not received the event
        p1.assertEndDocumentNotInvoked();
        p2.assertEndDocumentNotInvoked();
        next.assertEndDocumentNotInvoked();
    }

    // javadoc inherited
    public void testEndElement() throws Exception {
        super.testEndElement();

        XMLPipelineProcess testable = (XMLPipelineProcess) createTestableProcess();
        
        // create some XMLProcess to push onto the pipeline        
        XMLProcessTestable p1 = new XMLProcessTestable();
        XMLProcessTestable p2 = new XMLProcessTestable();
        
        // push the Processes
        testable.addHeadProcess(p1);
        testable.addHeadProcess(p2);
                
        //  set up  a next process for the pipeline
        XMLProcessTestable next = new XMLProcessTestable();
        testable.setNextProcess(next);
        
        // defensive checks
        p1.assertEndElementNotInvoked();
        p2.assertEndElementNotInvoked();
        next.assertEndElementNotInvoked();
        
        // send the appropriate event down the pipeline.
        testEndElement(testable);
        
        // check that each process received the event 
        next.assertEndElementInvoked(NAMESPACE_URI, LOCAL_NAME, Q_NAME);
        p1.assertEndElementInvoked(NAMESPACE_URI, LOCAL_NAME, Q_NAME);
        p2.assertEndElementInvoked(NAMESPACE_URI, LOCAL_NAME, Q_NAME);
    }

    // javadoc inherited 
    public void testEndPrefixMapping() throws Exception {
        super.testEndPrefixMapping();

        XMLPipelineProcess testable = (XMLPipelineProcess) createTestableProcess();
        
        // create some XMLProcess to push onto the pipeline        
        XMLProcessTestable p1 = new XMLProcessTestable();
        XMLProcessTestable p2 = new XMLProcessTestable();
        
        // push the Processes
        testable.addHeadProcess(p1);
        testable.addHeadProcess(p2);
                
        //  set up  a next process for the pipeline
        XMLProcessTestable next = new XMLProcessTestable();
        testable.setNextProcess(next);
        
        // defensive checks
        p1.assertEndPrefixMappingNotInvoked();
        p2.assertEndPrefixMappingNotInvoked();
        next.assertEndPrefixMappingNotInvoked();        
        
        // send the appropriate event down the pipeline.
        testEndPrefixMapping(testable);
        
        // check that each process received the event 
        next.assertEndPrefixMappingInvoked(PREFIX);
        p1.assertEndPrefixMappingInvoked(PREFIX);
        p2.assertEndPrefixMappingInvoked(PREFIX);
    }

    // javadoc inherited
    public void testIgnorableWhitespace() throws Exception {
        super.testIgnorableWhitespace();

        XMLPipelineProcess testable = (XMLPipelineProcess) createTestableProcess();
        
        // create some XMLProcess to push onto the pipeline        
        XMLProcessTestable p1 = new XMLProcessTestable();
        XMLProcessTestable p2 = new XMLProcessTestable();
        
        // push the Processes
        testable.addHeadProcess(p1);
        testable.addHeadProcess(p2);
                
        //  set up  a next process for the pipeline
        XMLProcessTestable next = new XMLProcessTestable();
        testable.setNextProcess(next);
        
        // defensive checks
        p1.assertIgnorableWhitespaceNotInvoked();
        p2.assertIgnorableWhitespaceNotInvoked();
        next.assertIgnorableWhitespaceNotInvoked();       
        
        // send the appropriate event down the pipeline.
        testIgnorableWhitespace(testable);
        
        // check that each process received the event 
        next.assertIgnorableWhitespaceInvoked(CH, START, LENGTH);
        p1.assertIgnorableWhitespaceInvoked(CH, START, LENGTH);
        p2.assertIgnorableWhitespaceInvoked(CH, START, LENGTH);
    }

    // javadoc inherited
    public void testProcessingInstruction() throws Exception {
        super.testProcessingInstruction();

        XMLPipelineProcess testable = (XMLPipelineProcess) createTestableProcess();
        
        // create some XMLProcess to push onto the pipeline        
        XMLProcessTestable p1 = new XMLProcessTestable();
        XMLProcessTestable p2 = new XMLProcessTestable();
        
        // push the Processes
        testable.addHeadProcess(p1);
        testable.addHeadProcess(p2);
                
        //  set up  a next process for the pipeline
        XMLProcessTestable next = new XMLProcessTestable();
        testable.setNextProcess(next);
        
        // defensive checks
        p1.assertProcessingInstructionNotInvoked();
        p2.assertProcessingInstructionNotInvoked();
        next.assertProcessingInstructionNotInvoked();       
        
        // send the appropriate event down the pipeline.
        testProcessingInstruction(testable);
        
        // check that each process received the event 
        next.assertProcessingInstructionInvoked(TARGET, DATA);
        p1.assertProcessingInstructionInvoked(TARGET, DATA);
        p2.assertProcessingInstructionInvoked(TARGET, DATA);
    }

    // javadoc inherited
    public void testSetDocumentLocator() throws Exception {
        super.testSetDocumentLocator();

        XMLPipelineProcess testable = (XMLPipelineProcess) createTestableProcess();
        
        // create some XMLProcess to push onto the pipeline        
        XMLProcessTestable p1 = new XMLProcessTestable();
        XMLProcessTestable p2 = new XMLProcessTestable();
        
        // push the Processes
        testable.addHeadProcess(p1);
        testable.addHeadProcess(p2);
                
        //  set up  a next process for the pipeline
        XMLProcessTestable next = new XMLProcessTestable();
        testable.setNextProcess(next);
        
        // defensive checks
        p1.assertSetDocumentLocatorNotInvoked();
        p2.assertSetDocumentLocatorNotInvoked();
        next.assertSetDocumentLocatorNotInvoked();       
        
        // send the appropriate event down the pipeline.
        testSetDocumentLocator(testable);
        
        // check that each process did not received the event
        p1.assertSetDocumentLocatorNotInvoked();
        p2.assertSetDocumentLocatorNotInvoked();
        next.assertSetDocumentLocatorNotInvoked();
    }

    // javadoc inherited
    public void testSkippedEntity() throws Exception {
        super.testSkippedEntity();

        XMLPipelineProcess testable = (XMLPipelineProcess) createTestableProcess();
        
        // create some XMLProcess to push onto the pipeline        
        XMLProcessTestable p1 = new XMLProcessTestable();
        XMLProcessTestable p2 = new XMLProcessTestable();
        
        // push the Processes
        testable.addHeadProcess(p1);
        testable.addHeadProcess(p2);
                
        //  set up  a next process for the pipeline
        XMLProcessTestable next = new XMLProcessTestable();
        testable.setNextProcess(next);
        
        // defensive checks
        p1.assertSkippedEntityNotInvoked();
        p2.assertSkippedEntityNotInvoked();
        next.assertSkippedEntityNotInvoked();       
        
        // send the appropriate event down the pipeline.
        testSkippedEntity(testable);
        
        // check that each process did not receive the event
        p1.assertSkippedEntityNotInvoked();
        p2.assertSkippedEntityNotInvoked();
        next.assertSkippedEntityNotInvoked();
    }

    // javadoc inherited
    public void testStartDocument() throws Exception {
        super.testStartDocument();

        XMLPipelineProcess testable = (XMLPipelineProcess) createTestableProcess();
        
        // create some XMLProcess to push onto the pipeline        
        XMLProcessTestable p1 = new XMLProcessTestable();
        XMLProcessTestable p2 = new XMLProcessTestable();
        
        // push the Processes
        testable.addHeadProcess(p1);
        testable.addHeadProcess(p2);
                
        //  set up  a next process for the pipeline
        XMLProcessTestable next = new XMLProcessTestable();
        testable.setNextProcess(next);
        
        // defensive checks
        p1.assertStartDocumentNotInvoked();
        p2.assertStartDocumentNotInvoked();
        next.assertStartDocumentNotInvoked();       
        
        // send the appropriate event down the pipeline.
        testStartDocument(testable);
        
        // check that each process did not receive the event
        p1.assertStartDocumentNotInvoked();
        p2.assertStartDocumentNotInvoked();
        next.assertStartDocumentNotInvoked();
    }

    // javadoc inherited
    public void testStartElement() throws Exception {
        super.testStartElement();

        XMLPipelineProcess testable = (XMLPipelineProcess) createTestableProcess();
        
        // create some XMLProcess to push onto the pipeline        
        XMLProcessTestable p1 = new XMLProcessTestable();
        XMLProcessTestable p2 = new XMLProcessTestable();
        
        // push the Processes
        testable.addHeadProcess(p1);
        testable.addHeadProcess(p2);
                
        //  set up  a next process for the pipeline
        XMLProcessTestable next = new XMLProcessTestable();
        testable.setNextProcess(next);
        
        // defensive checks
        p1.assertStartElementNotInvoked();
        p2.assertStartElementNotInvoked();
        next.assertStartElementNotInvoked();       
        
        // send the appropriate event down the pipeline.
        testStartElement(testable);
        
        // check that each process received the event 
        next.assertStartElementInvoked(NAMESPACE_URI, LOCAL_NAME,
                                       Q_NAME, ATTS);
        p1.assertStartElementInvoked(NAMESPACE_URI, LOCAL_NAME,
                                     Q_NAME, ATTS);
        p2.assertStartElementInvoked(NAMESPACE_URI, LOCAL_NAME,
                                     Q_NAME, ATTS);
    }

    // javadoc inherited
    public void testStartPrefixMapping() throws Exception {
        super.testStartPrefixMapping();

        XMLPipelineProcess testable = (XMLPipelineProcess) createTestableProcess();
        
        // create some XMLProcess to push onto the pipeline        
        XMLProcessTestable p1 = new XMLProcessTestable();
        XMLProcessTestable p2 = new XMLProcessTestable();
        
        // push the Processes
        testable.addHeadProcess(p1);
        testable.addHeadProcess(p2);
                
        //  set up  a next process for the pipeline
        XMLProcessTestable next = new XMLProcessTestable();
        testable.setNextProcess(next);
        
        // defensive checks
        p1.assertStartPrefixMappingNotInvoked();
        p2.assertStartPrefixMappingNotInvoked();
        next.assertStartPrefixMappingNotInvoked();       
        
        // send the appropriate event down the pipeline.
        testStartPrefixMapping(testable);
        
        // check that each process received the event 
        next.assertStartPrefixMappingInvoked(PREFIX, URI);
        p1.assertStartPrefixMappingInvoked(PREFIX, URI);
        p2.assertStartPrefixMappingInvoked(PREFIX, URI);
    }

    // javadoc inherited
    public void testError() throws Exception {
        super.testError();

        XMLPipelineProcess testable = (XMLPipelineProcess) createTestableProcess();
        
        // create some XMLProcess to push onto the pipeline        
        XMLProcessTestable p1 = new XMLProcessTestable();
        XMLProcessTestable p2 = new XMLProcessTestable();
        
        // push the Processes
        testable.addHeadProcess(p1);
        testable.addHeadProcess(p2);
                
        //  set up  a next process for the pipeline
        XMLProcessTestable next = new XMLProcessTestable();
        testable.setNextProcess(next);
        
        // defensive checks
        p1.assertErrorNotInvoked();
        p2.assertErrorNotInvoked();
        next.assertErrorNotInvoked();       
        
        // send the appropriate event down the pipeline.
        testError(testable);
        
        // check that each process received the event 
        next.assertErrorInvoked(EXCEPTION);
        p1.assertErrorInvoked(EXCEPTION);
        p2.assertErrorInvoked(EXCEPTION);
    }

    // javadoc inherited
    public void testFatalError() throws Exception {
        super.testFatalError();

        XMLPipelineProcess testable = (XMLPipelineProcess) createTestableProcess();
        
        // create some XMLProcess to push onto the pipeline        
        XMLProcessTestable p1 = new XMLProcessTestable();
        XMLProcessTestable p2 = new XMLProcessTestable();
        
        // push the Processes
        testable.addHeadProcess(p1);
        testable.addHeadProcess(p2);
                
        //  set up  a next process for the pipeline
        XMLProcessTestable next = new XMLProcessTestable();
        testable.setNextProcess(next);
        
        // defensive checks
        p1.assertFatalErrorNotInvoked();
        p2.assertFatalErrorNotInvoked();
        next.assertFatalErrorNotInvoked();       
        
        // send the appropriate event down the pipeline.
        testFatalError(testable);
        
        // check that each process received the event 
        next.assertFatalErrorInvoked(EXCEPTION);
        p1.assertFatalErrorInvoked(EXCEPTION);
        p2.assertFatalErrorInvoked(EXCEPTION);
    }

    // javadoc inherited
    public void testWarning() throws Exception {
        super.testWarning();

        XMLPipelineProcess testable = (XMLPipelineProcess) createTestableProcess();
        
        // create some XMLProcess to push onto the pipeline        
        XMLProcessTestable p1 = new XMLProcessTestable();
        XMLProcessTestable p2 = new XMLProcessTestable();
        
        // push the Processes
        testable.addHeadProcess(p1);
        testable.addHeadProcess(p2);
                
        //  set up  a next process for the pipeline
        XMLProcessTestable next = new XMLProcessTestable();
        testable.setNextProcess(next);
        
        // defensive checks
        p1.assertWarningNotInvoked();
        p2.assertWarningNotInvoked();
        next.assertWarningNotInvoked();       
        
        // send the appropriate event down the pipeline.
        testWarning(testable);
        
        // check that each process received the event 
        next.assertWarningInvoked(EXCEPTION);
        p1.assertWarningInvoked(EXCEPTION);
        p2.assertWarningInvoked(EXCEPTION);
    }


    // javadoc inherited
    public void testSetPipeline() throws Exception {
        XMLPipelineProcessImpl testable = 
                ( XMLPipelineProcessImpl)createTestableProcess();
        
        XMLPipelineProcess parent = 
                (XMLPipelineProcessImpl)createTestableProcess();
        
        IllegalArgumentException iae = null;
        try {
            testable.setPipeline(parent);
        } catch (IllegalArgumentException e) {
            iae = e;            
        }
        assertNotNull("calling setPipeline() on a unnestable pipeline " +
                      "should throw an exception", iae);
        
        XMLPipelineProcessImpl nestable = 
                (XMLPipelineProcessImpl)createNestableTestableProcess();
        
        XMLProcessTestable p = new XMLProcessTestable();
        p.assertSetPipelineNotInvoked();
        
        nestable.addHeadProcess(p);
        
        p.assertSetPipelineInvoked(nestable);
        
        nestable.setPipeline(parent);
                                                    
    }

    // javadoc inherited
    public void testRelease() throws Exception {
        XMLProcessImpl testable = (XMLProcessImpl)createTestableProcess();
        
        testable.release();
        
        assertNull("release() should null the pipeline context", 
                   testable.getPipelineContext());
        
        assertNull("release() should null the next member", 
                   testable.getNextProcess());
                                                           
        testable = (XMLProcessImpl)createTestableProcess();
        XMLProcessTestable next = new XMLProcessTestable();
        testable.setNextProcess(next);        
        testable.release();
        
        next.assertReleaseNotInvoked();        
    }
}    
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 30-Jan-04	531/1	adrian	VBM:2004011905 added context updating and context annotation support to pipeline processes

 11-Aug-03	275/2	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 06-Aug-03	301/1	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 01-Aug-03	258/2	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 22-Jul-03	225/1	doug	VBM:2003071805 Refactored the XMLPipeline interface to reflect the new public API

 18-Jul-03	213/2	doug	VBM:2003071615 Refactored XMLProcess interface

 23-Jun-03	95/5	doug	VBM:2003061605 Document Event Filtering changes

 06-Jun-03	26/1	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/

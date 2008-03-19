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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-May-03    Doug            VBM:2003030405 - Created. TestCase for the
 *                              XMLPreprocessingPipelineProcess class
 * 27-May-03   Doug             VBM:2003030405 - Added methods to test the 
 *                              various processing modes.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax;



/**
 * TestCase for the XMLPreprocessingPipelineProcess class 
 */ 
public class XMLPreprocessingPipelineProcessImplTestCase 
        extends XMLPipelineProcessImplTestCase {

    // javadoc inherited from superclass 
    protected XMLProcess createTestableProcess() {
        return new XMLPreprocessingPipelineProcessImpl(createPipelineContext());
    }    
    
    // javadoc inherited from superclass
    protected XMLPipelineProcess createNestableTestableProcess() {
        return new XMLPreprocessingPipelineProcessImpl();
    }
    
    /**
     * Ensures that events are directed through the preprocessor first
     * @throws Exception if an error occurs.
     */  
    public void testEventFlow() throws Exception {
        XMLProcessTestable preProcessor = 
                new XMLProcessTestable("a");

        XMLProcessTestable process1 = 
                new XMLProcessTestable("b");
        
        XMLProcessTestable process2 = 
                new XMLProcessTestable("c");
        
        XMLProcessTestable next =
                new XMLProcessTestable("d");
        
        XMLPreprocessingPipelineProcessImpl pipeline =
                (XMLPreprocessingPipelineProcessImpl) createTestableProcess();
        
        pipeline.initialisePreprocessor(preProcessor);
        pipeline.addHeadProcess(process2);
        pipeline.addHeadProcess(process1);
        pipeline.setNextProcess(next);

//        pipeline.setProcessingMode(XMLProcessingMode.NORMAL);
        
        // defensive checks
        preProcessor.assertSkippedEntityNotInvoked();
        process1.assertSkippedEntityNotInvoked();
        process2.assertSkippedEntityNotInvoked();
        next.assertSkippedEntityNotInvoked();
        
        
        char[] chars = "characters".toCharArray();
        pipeline.characters(chars, 0, chars.length -1);
        process1.assertCharactersInvoked(chars, 0, chars.length -1);
        process2.assertCharactersInvoked(chars, 0, chars.length -1);
        next.assertCharactersInvoked(chars, 0, chars.length -1);
    }
    
//    /**
//     * Test basic functionality of the "suppress" processing mode
//     * @throws Exception if an error occurs
//     */ 
//    public void testSuppressProcessingMode() throws Exception {
//        
//        // test the suppression mode of processing the markup
//        // the following is the structure of the markup that
//        // is being tested
//        // 
//        // <a>                         mode == normal
//        //          switch suppression on here
//        //   <b>                       mode == suppressed 
//        //     <c>                     mode == suppressed
//        //     </c>                    mode == suppressed
//        //   </b>                      mode == suppressed
//        //  </a>                       mode == normal
//        
//        XMLProcessTestable preProcessor = 
//                new XMLProcessTestable("a");
//
//        XMLProcessTestable process1 = 
//                new XMLProcessTestable("b");
//        
//        XMLProcessTestable next =
//                new XMLProcessTestable("c");
//        
//        XMLPreprocessingPipelineProcess pipeline = 
//            (XMLPreprocessingPipelineProcess)createTestableProcess();
//        
//        pipeline.setPreprocessor(preProcessor);
//        pipeline.addHeadProcess(process1);
//        pipeline.setNextProcess(next);
//
//        pipeline.setProcessingMode(XMLProcessingMode.NORMAL);
//        
//        // send a startElement event down the pipeline
//        pipeline.startElement(NAMESPACE_URI, LOCAL_NAME, Q_NAME, ATTS);
//        
//        // make sure all the processes received the event
//        preProcessor.assertStartElementInvoked(NAMESPACE_URI, LOCAL_NAME, 
//                                               Q_NAME, ATTS);
//        process1.assertStartElementInvoked(NAMESPACE_URI, LOCAL_NAME, 
//                                               Q_NAME, ATTS);
//        next.assertStartElementInvoked(NAMESPACE_URI, LOCAL_NAME, 
//                                               Q_NAME, ATTS);
//                
//        // send a skippedEntity event down the pipeline
//        pipeline.skippedEntity("s");
//        
//        // make sure all the processes received the event in the 
//        // correct sequence
//        preProcessor.assertSkippedEntityInvoked("s");
//        process1.assertSkippedEntityInvoked("sa");
//        next.assertSkippedEntityInvoked("sab");        
//        
//        preProcessor.reset();
//        process1.reset();
//        next.reset();
//        
//        // switch on suppression
//        pipeline.setProcessingMode(XMLProcessingMode.SUPPRESSED);
//       
//        // send a startElement event down the pipeline
//        pipeline.startElement(NAMESPACE_URI, LOCAL_NAME, Q_NAME, ATTS);
//        
//        // ensure the process did not receive this event        
//        preProcessor.assertStartElementNotInvoked();                                                
//        process1.assertStartElementNotInvoked();                                                
//        next.assertStartElementNotInvoked();
//                
//        // send a skippedEntity event down the pipeline
//        pipeline.skippedEntity("s");
//        
//        // ensure the processes did not receive this event
//        preProcessor.assertSkippedEntityNotInvoked();
//        process1.assertSkippedEntityNotInvoked();
//        next.assertSkippedEntityNotInvoked();
//        
//        preProcessor.reset();
//        process1.reset();
//        next.reset();
//        
//        // send a startElement event down the pipeline - this will
//        // be the second suppressed startElement() event
//        pipeline.startElement(NAMESPACE_URI, LOCAL_NAME, Q_NAME, ATTS);
//
//        // ensure the processes did not receive this event
//        preProcessor.assertStartElementNotInvoked();
//        process1.assertStartElementNotInvoked();
//        next.assertStartElementNotInvoked();
//
//        // send a skippedEntity event down the pipeline
//        pipeline.skippedEntity("s");
//
//        // ensure the processes did not receive this event
//        preProcessor.assertSkippedEntityNotInvoked();
//        process1.assertSkippedEntityNotInvoked();
//        next.assertSkippedEntityNotInvoked();
//        
//        // send an endElement event down the pipeline - this will
//        // close off the second startElement event that was generated. 
//        // we should still be suppressing
//        pipeline.endElement(NAMESPACE_URI, LOCAL_NAME, Q_NAME);
//
//        // ensure the processes did not receive this event
//        preProcessor.assertEndElementNotInvoked();
//        process1.assertEndElementNotInvoked();
//        next.assertEndElementNotInvoked();
//
//        // send a skippedEntity event down the pipeline
//        pipeline.skippedEntity("s");
//
//        // ensure the processes did not receive this event
//        preProcessor.assertSkippedEntityNotInvoked();
//        process1.assertSkippedEntityNotInvoked();
//        next.assertSkippedEntityNotInvoked();
//        
//        preProcessor.reset();
//        process1.reset();
//        next.reset();
//        
//        // send an endElement event down the pipeline - this will
//        // close off the first startElement event that was generated. 
//        // we should still be suppressing.
//        pipeline.endElement(NAMESPACE_URI, LOCAL_NAME, Q_NAME);
//
//        // ensure the processes did not receive this event
//        preProcessor.assertEndElementNotInvoked();
//        process1.assertEndElementNotInvoked();
//        next.assertEndElementNotInvoked();
//
//        // send a skippedEntity event down the pipeline
//        pipeline.skippedEntity("s");
//
//        // ensure the processes did not receive this event
//        preProcessor.assertSkippedEntityNotInvoked();
//        process1.assertSkippedEntityNotInvoked();
//        next.assertSkippedEntityNotInvoked();        
//        
//        preProcessor.reset();
//        process1.reset();
//        next.reset();
//        
//        // send an endElement event down the pipeline - this should
//        // signal the end of suppression. This event should be seen
//        // by processes in the pipeline
//        pipeline.endElement(NAMESPACE_URI, LOCAL_NAME, Q_NAME);
//
//        // ensure the processes received this event        
//        preProcessor.assertEndElementInvoked(NAMESPACE_URI, LOCAL_NAME,
//                                             Q_NAME);
//        process1.assertEndElementInvoked(NAMESPACE_URI, LOCAL_NAME, Q_NAME);
//        next.assertEndElementInvoked(NAMESPACE_URI, LOCAL_NAME, Q_NAME);
//        
//        preProcessor.reset();
//        process1.reset();
//        next.reset();
//        
//        // send a startElement event down the pipeline. 
//        pipeline.startElement(NAMESPACE_URI, LOCAL_NAME, Q_NAME, ATTS);
//        
//        // ensure the processes received this event        
//        preProcessor.assertStartElementInvoked(NAMESPACE_URI, LOCAL_NAME, 
//                                               Q_NAME, ATTS);
//        process1.assertStartElementInvoked(NAMESPACE_URI, LOCAL_NAME, 
//                                           Q_NAME, ATTS);
//        next.assertStartElementInvoked(NAMESPACE_URI, LOCAL_NAME, 
//                                       Q_NAME, ATTS);
//                
//        // send a skippedEntity event down the pipeline        
//        pipeline.skippedEntity("s");
//        
//        // ensure the processes received this event
//        preProcessor.assertSkippedEntityInvoked("s");
//        process1.assertSkippedEntityInvoked("sa");
//        next.assertSkippedEntityInvoked("sab");        
//                
//    }
    
//    /**
//     * Test that mark up can be suppressed immediately after a startDocument() 
//     * event
//     * @throws Exception if an error occurs.
//     */
//    public void testSuppressProcessingModeWithFragment() throws Exception {
//
//        // test the suppression mode of processing the markup
//        // the following is the structure of the markup that
//        // is being tested
//        // 
//        // <a>                         mode == normal
//        //          switch suppression on here
//        //   <b>                       mode == suppressed
//        //   </b>                      mode == suppressed
//        //   <c>                       mode == suppressed
//        //   </c>                      mode == suppressed
//        //  </a>                       mode == normal
//        
//        XMLProcessTestable preProcessor =
//                new XMLProcessTestable("a");
//
//        XMLProcessTestable process1 =
//                new XMLProcessTestable("b");
//
//        XMLProcessTestable next =
//                new XMLProcessTestable("c");
//
//        XMLPreprocessingPipelineProcess pipeline =
//                (XMLPreprocessingPipelineProcess) createTestableProcess();
//
//        pipeline.setPreprocessor(preProcessor);
//        pipeline.addHeadProcess(process1);
//        pipeline.setNextProcess(next);
//
//        pipeline.setProcessingMode(XMLProcessingMode.NORMAL);
//        
//        // send a startElement event down the pipeline
//        pipeline.startElement(NAMESPACE_URI, LOCAL_NAME, Q_NAME, ATTS);
//        
//        // make sure all the processes received the event
//        preProcessor.assertStartElementInvoked(NAMESPACE_URI, LOCAL_NAME,
//                                               Q_NAME, ATTS);
//        process1.assertStartElementInvoked(NAMESPACE_URI, LOCAL_NAME,
//                                           Q_NAME, ATTS);
//        next.assertStartElementInvoked(NAMESPACE_URI, LOCAL_NAME,
//                                       Q_NAME, ATTS);
//                
//        // send a skippedEntity event down the pipeline
//        pipeline.skippedEntity("s");
//        
//        // make sure all the processes received the event in the 
//        // correct sequence
//        preProcessor.assertSkippedEntityInvoked("s");
//        process1.assertSkippedEntityInvoked("sa");
//        next.assertSkippedEntityInvoked("sab");
//
//        preProcessor.reset();
//        process1.reset();
//        next.reset();
//        
//        // switch on suppression
//        pipeline.setProcessingMode(XMLProcessingMode.SUPPRESSED);
//       
//        // send a startElement event down the pipeline
//        pipeline.startElement(NAMESPACE_URI, LOCAL_NAME, Q_NAME, ATTS);
//        
//        // ensure the process did not receive this event        
//        preProcessor.assertStartElementNotInvoked();
//        process1.assertStartElementNotInvoked();
//        next.assertStartElementNotInvoked();
//                
//        // send a skippedEntity event down the pipeline
//        pipeline.skippedEntity("s");
//        
//        // ensure the processes did not receive this event
//        preProcessor.assertSkippedEntityNotInvoked();
//        process1.assertSkippedEntityNotInvoked();
//        next.assertSkippedEntityNotInvoked();
//
//        preProcessor.reset();
//        process1.reset();
//        next.reset();
//        
//        // send an endElement event down the pipeline - this will
//        // close off the startElement event that was generated. 
//        // we should still be suppressing
//        pipeline.endElement(NAMESPACE_URI, LOCAL_NAME, Q_NAME);
//
//        // ensure the processes did not receive this event
//        preProcessor.assertEndElementNotInvoked();
//        process1.assertEndElementNotInvoked();
//        next.assertEndElementNotInvoked();
//
//        // send a skippedEntity event down the pipeline
//        pipeline.skippedEntity("s");
//
//        // ensure the processes did not receive this event
//        preProcessor.assertSkippedEntityNotInvoked();
//        process1.assertSkippedEntityNotInvoked();
//        next.assertSkippedEntityNotInvoked();
//
//        preProcessor.reset();
//        process1.reset();
//        next.reset();
//                    
//            
//            
//        // send a startElement event down the pipeline - this will
//        // be the second suppressed startElement() event
//        pipeline.startElement(NAMESPACE_URI, LOCAL_NAME, Q_NAME, ATTS);
//
//        // ensure the processes did not receive this event
//        preProcessor.assertStartElementNotInvoked();
//        process1.assertStartElementNotInvoked();
//        next.assertStartElementNotInvoked();
//
//        // send a skippedEntity event down the pipeline
//        pipeline.skippedEntity("s");
//
//        // ensure the processes did not receive this event
//        preProcessor.assertSkippedEntityNotInvoked();
//        process1.assertSkippedEntityNotInvoked();
//        next.assertSkippedEntityNotInvoked();
//        
//        
//        // send an endElement event down the pipeline - this will
//        // close off the  startElement event that was generated. 
//        // we should still be suppressing.
//        pipeline.endElement(NAMESPACE_URI, LOCAL_NAME, Q_NAME);
//
//        // ensure the processes did not receive this event
//        preProcessor.assertEndElementNotInvoked();
//        process1.assertEndElementNotInvoked();
//        next.assertEndElementNotInvoked();
//
//        // send a skippedEntity event down the pipeline
//        pipeline.skippedEntity("s");
//
//        // ensure the processes did not receive this event
//        preProcessor.assertSkippedEntityNotInvoked();
//        process1.assertSkippedEntityNotInvoked();
//        next.assertSkippedEntityNotInvoked();
//
//        preProcessor.reset();
//        process1.reset();
//        next.reset();
//        
//        // send an endElement event down the pipeline - this should
//        // signal the end of suppression. This event should be seen
//        // by processes in the pipeline
//        pipeline.endElement(NAMESPACE_URI, LOCAL_NAME, Q_NAME);
//
//        // ensure the processes received this event        
//        preProcessor.assertEndElementInvoked(NAMESPACE_URI, LOCAL_NAME,
//                                             Q_NAME);
//        process1.assertEndElementInvoked(NAMESPACE_URI, LOCAL_NAME, Q_NAME);
//        next.assertEndElementInvoked(NAMESPACE_URI, LOCAL_NAME, Q_NAME);
//
//        preProcessor.reset();
//        process1.reset();
//        next.reset();
//        
//        // send a startElement event down the pipeline. 
//        pipeline.startElement(NAMESPACE_URI, LOCAL_NAME, Q_NAME, ATTS);
//        
//        // ensure the processes received this event        
//        preProcessor.assertStartElementInvoked(NAMESPACE_URI, LOCAL_NAME,
//                                               Q_NAME, ATTS);
//        process1.assertStartElementInvoked(NAMESPACE_URI, LOCAL_NAME,
//                                           Q_NAME, ATTS);
//        next.assertStartElementInvoked(NAMESPACE_URI, LOCAL_NAME,
//                                       Q_NAME, ATTS);
//                
//        // send a skippedEntity event down the pipeline        
//        pipeline.skippedEntity("s");
//        
//        // ensure the processes received this event
//        preProcessor.assertSkippedEntityInvoked("s");
//        process1.assertSkippedEntityInvoked("sa");
//        next.assertSkippedEntityInvoked("sab");
//
//    }
//
//    /**
//     * Test that mark up can be suppressed immediately after a startDocument() 
//     * event
//     * @throws Exception if an error occurs.
//     */ 
//    public void testSuppressProcessingModeAfterStartDocument() 
//            throws Exception {
//
//        // test the suppression mode of processing the markup
//        // the following is the structure of the markup that
//        // is being tested
//        
//        // startDocument()             mode == normal
//        //                switch on suppression here
//        // <a>                         mode == suppressed
//        //  </a>                       mode == suppressed
//        // endDocument()               mode == normal
//        
//        XMLProcessTestable preProcessor =
//                new XMLProcessTestable("a");
//
//        XMLProcessTestable process1 =
//                new XMLProcessTestable("b");
//
//        XMLProcessTestable next =
//                new XMLProcessTestable("c");
//
//        XMLPreprocessingPipelineProcess pipeline =
//                (XMLPreprocessingPipelineProcess) createTestableProcess();
//
//        pipeline.setPreprocessor(preProcessor);
//        pipeline.addHeadProcess(process1);
//        pipeline.setNextProcess(next);
//
//        pipeline.setProcessingMode(XMLProcessingMode.NORMAL);
//        
//        // send a startDocument event down the pipeline
//        pipeline.startDocument();
//        
//        // make sure all the processes received the event
//        preProcessor.assertStartDocumentInvoked();
//        process1.assertStartDocumentInvoked();
//        next.assertStartDocumentInvoked();
//                
//        preProcessor.reset();
//        process1.reset();
//        next.reset();
//        
//        // switch on suppression
//        pipeline.setProcessingMode(XMLProcessingMode.SUPPRESSED);
//       
//        // send a startElement event down the pipeline
//        pipeline.startElement(NAMESPACE_URI, LOCAL_NAME, Q_NAME, ATTS);
//        
//        // ensure the process did not receive this event        
//        preProcessor.assertStartElementNotInvoked();
//        process1.assertStartElementNotInvoked();
//        next.assertStartElementNotInvoked();
//                
//        // send a skippedEntity event down the pipeline
//        pipeline.skippedEntity("s");
//        
//        // ensure the processes did not receive this event
//        preProcessor.assertSkippedEntityNotInvoked();
//        process1.assertSkippedEntityNotInvoked();
//        next.assertSkippedEntityNotInvoked();
//
//        preProcessor.reset();
//        process1.reset();
//        next.reset();
//        
//        // send an endElement event down the pipeline - this will
//        // close off the startElement event that was generated. 
//        // we should still be suppressing
//        pipeline.endElement(NAMESPACE_URI, LOCAL_NAME, Q_NAME);
//
//        // ensure the processes did not receive this event
//        preProcessor.assertEndElementNotInvoked();
//        process1.assertEndElementNotInvoked();
//        next.assertEndElementNotInvoked();
//
//        // send a skippedEntity event down the pipeline
//        pipeline.skippedEntity("s");
//
//        // ensure the processes did not receive this event
//        preProcessor.assertSkippedEntityNotInvoked();
//        process1.assertSkippedEntityNotInvoked();
//        next.assertSkippedEntityNotInvoked();
//
//        preProcessor.reset();
//        process1.reset();
//        next.reset();
//                    
//        // send a endDocument event down the pipeline.
//        // suppression should be switched off by this
//        pipeline.endDocument();
//        
//        // ensure the processes received this event        
//        preProcessor.assertEndDocumentInvoked();
//        process1.assertEndDocumentInvoked();
//        next.assertEndDocumentInvoked();
//               
//    }
       
//    /**
//     * Test the "pass through" processing mode
//     * @throws Exception if error ocurrs
//     */ 
//    public void testPassThrough() throws Exception {
//        // when pass through mode is switched on the proprocessor is skipped
//        XMLProcessTestable preProcessor =
//                new XMLProcessTestable("a");
//
//        XMLProcessTestable process1 =
//                new XMLProcessTestable("b");
//
//        XMLProcessTestable next =
//                new XMLProcessTestable("c");
//
//        XMLPreprocessingPipelineProcess pipeline =
//                (XMLPreprocessingPipelineProcess) createTestableProcess();
//
//        pipeline.setPreprocessor(preProcessor);
//        pipeline.addHeadProcess(process1);
//        pipeline.setNextProcess(next);
//
//        pipeline.setProcessingMode(XMLProcessingMode.NORMAL);
//        
//        // send a skippedEntity event down the pipeline
//        pipeline.skippedEntity("s");
//        
//         // ensure the processes received this event
//        preProcessor.assertSkippedEntityInvoked("s");
//        process1.assertSkippedEntityInvoked("sa");
//        next.assertSkippedEntityInvoked("sab");     
//        
//        // flush out any state information
//        preProcessor.reset();
//        process1.reset();
//        next.reset();
//        
//        // switch to pass through mode
//        pipeline.setProcessingMode(XMLProcessingMode.PASS_THROUGH);
//        
//        // send a skippedEntity event down the pipeline
//        pipeline.skippedEntity("s");
//        
//         // ensure all processes except the preProcessor received this event
//        preProcessor.assertSkippedEntityNotInvoked();
//        process1.assertSkippedEntityInvoked("s");
//        next.assertSkippedEntityInvoked("sb");
//        
//        // flush out any state information
//        preProcessor.reset();
//        process1.reset();
//        next.reset();
//        
//        // switch back to normal and test again
//        pipeline.setProcessingMode(XMLProcessingMode.NORMAL);
//        
//        // send a skippedEntity event down the pipeline
//        pipeline.skippedEntity("s");
//        
//         // ensure the processes received this event
//        preProcessor.assertSkippedEntityInvoked("s");
//        process1.assertSkippedEntityInvoked("sa");
//        next.assertSkippedEntityInvoked("sab");     
//                
//    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 30-Jan-04	531/1	adrian	VBM:2004011905 added context updating and context annotation support to pipeline processes

 11-Aug-03	275/2	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 01-Aug-03	258/2	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 18-Jul-03	213/2	doug	VBM:2003071615 Refactored XMLProcess interface

 23-Jun-03	95/3	doug	VBM:2003061605 Document Event Filtering changes

 06-Jun-03	26/1	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/

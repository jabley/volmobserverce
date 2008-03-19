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
package com.volantis.xml.pipeline.sax.impl.drivers.webservice;

import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.drivers.webservice.WSDriverConfiguration;
import com.volantis.xml.pipeline.sax.impl.drivers.webservice.RequestOperationProcess;
import com.volantis.xml.pipeline.sax.impl.drivers.webservice.Message;
import com.volantis.xml.pipeline.sax.impl.drivers.webservice.Operation;
import com.volantis.xml.pipeline.sax.impl.drivers.webservice.WSDLOperation;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.operation.AbstractOperationProcessTestAbstract;
import com.volantis.xml.pipeline.testtools.XMLHelpers;
import org.jdom.Document;
import org.jdom.input.SAXHandler;
import org.jdom.output.XMLOutputter;

/**
 * Excluding WebService testcases as they currently rely on external
 * resources (and currently always fail).
 */
public class RequestOperationProcessTestCaseDISABLED
        extends AbstractOperationProcessTestAbstract {

    /**
     * Construct a new RequestOperationTestCase.
     * @param name The name of the test.
     */
    public RequestOperationProcessTestCaseDISABLED(String name) {
        super(name);
    }

    /**
     * Create an WSDriverConfiguration that can be used in a
     * RequestOperationProcess.
     * @return An WSDriverConfiguration.
     */
    private WSDriverConfiguration creatWSDriverConfiguration() {
        WSDriverConfiguration configuration =
                new WSDriverConfiguration();

        return configuration;
    }

    protected void registerConfiguration(
            XMLPipelineConfiguration configuration) {
        // register the Configuration that the process being tested requires
        configuration.storeConfiguration(WSDriverConfiguration.class,
                                         creatWSDriverConfiguration());
    }

    // javadoc inherited
    protected XMLProcess createTestableProcess() {

        RequestOperationProcess process =
                new RequestOperationProcess();

        initializeProcess(process);

        return process;
    }

    /**
     * Override testStopProcess() since it is not possible for the parent
     * class to run stopProcess() successfully since it does not know how
     * to set up the RequestOperationProcess.
     */
    public void testStopProcess() {
        // Do nothing. stopProcess() is tested by several different methods.
    }

    /*
      This is commented out because the babelfish service is currently broken

     * Test stopProcess() with a setup that uses simple types for
     * both request and response. This test also tests part namespace and
     * prefix handling in the response.
     * @throws Exception If something goes wrong.
    public void testStopProcessBabelFish() throws Exception {

        RequestOperationProcess process =
                (RequestOperationProcess) createTestableProcess();

        XMLPipelineContext context = process.getPipelineContext();

        SAXHandler saxHandler = new SAXHandler();

        XMLProcess next = XMLHelpers.createSAXHandlerProcess(saxHandler);

        process.setNextProcess(next);

        // Setup the operation and message.
        WSDLOperation operation = new WSDLOperation();
        Message message = new Message();
        WSDriverTestHelpers.setupBabelFishOperationMessage(operation,
                message, "en_fr", "the cat sat on the mat");

        context.setProperty(Operation.class, operation, false);
        context.setProperty(Message.class, message, false);

        context.setProperty(RequestOperationProcess.PART_NAMESPACE_URI_KEY,
                "http://partnamespace", false);
        context.setProperty(RequestOperationProcess.PART_PREFIX_KEY,
                "pre", false);

        process.stopProcess();

        Document document = saxHandler.getDocument();

        System.out.println(new XMLOutputter("  ", true).
                outputString(document));

        char[] c = {(char)0xe9};
        String eaccute = new String(c);
        String expected = "<wsr:response xmlns:wsr=" +
                "\"http://www.volantis.com/xmlns/marlin-web-service-response\">" +
                "<wsr:message><pre:return xmlns:pre=\"http://partnamespace\">" +
                "le chat repos" + eaccute + " sur la natte </pre:return>" +
                "</wsr:message></wsr:response>";

        XMLHelpers.assertEquals(expected, document);
    }
     */

    /**
     * Test stopProcess() with a setup that uses simple types for
     * both request and response.
     * @throws Exception If something goes wrong.
     */
    public void testStopProcessAmazon() throws Exception {
        RequestOperationProcess process =
                (RequestOperationProcess) createTestableProcess();

        XMLPipelineContext context = process.getPipelineContext();

        SAXHandler saxHandler = new SAXHandler();

        XMLProcess next = XMLHelpers.createSAXHandlerProcess(saxHandler);

        process.setNextProcess(next);

        // Setup the operation and message.
        WSDLOperation operation = new WSDLOperation();
        Message message = new Message();
        WSDriverTestHelpers.setupAmazonOperationMessage(operation,
                message, "Douglas Adams");

        context.setProperty(Operation.class, operation, false);
        context.setProperty(Message.class, message, false);

        process.stopProcess();

        Document document = saxHandler.getDocument();

        String documentString = new XMLOutputter("  ", true).
                outputString(document);        

        assertTrue(documentString.
                indexOf("The Hitchhiker's Guide to the Galaxy") != -1);
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 07-Sep-04	860/2	byron	VBM:2004090705 Prepare Pipeline source for move to MCS

 12-Nov-03	456/1	geoff	VBM:2003111103 jaxrpc.jar clashes with IBM Websphere Portal Server

 31-Oct-03	440/1	doug	VBM:2003102911 Added Flow control process to tail of all pipelines

 10-Aug-03	264/1	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 06-Aug-03	301/1	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 28-Jul-03	232/1	doug	VBM:2003071804 Refactored XMLPipelineContext to reflect new public API

 25-Jul-03	242/1	steve	VBM:2003072310 Implement namespace package and refactor exitsting code to fit it

 18-Jul-03	213/2	doug	VBM:2003071615 Refactored XMLProcess interface

 29-Jun-03	98/3	allan	VBM:2003022822 Added some tests for RequestOperationProcess - could do with more though. Added some near final touches

 29-Jun-03	98/1	allan	VBM:2003022822 Added some tests for RequestOperationProcess - could do with more though. Added some possibly final touches

 ===========================================================================
*/

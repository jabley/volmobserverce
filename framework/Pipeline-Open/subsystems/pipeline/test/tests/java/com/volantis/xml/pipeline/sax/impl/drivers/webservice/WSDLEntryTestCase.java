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
 
package com.volantis.xml.pipeline.sax.impl.drivers.webservice;

import com.volantis.shared.net.url.URLContentManager;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.pipeline.sax.TestPipelineFactory;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.drivers.webservice.URIResource;
import com.volantis.xml.pipeline.sax.drivers.webservice.WSDLEntry;
import com.volantis.xml.pipeline.sax.drivers.webservice.WSDLResource;
import com.volantis.xml.pipeline.sax.url.PipelineURLContentManager;
import org.xml.sax.InputSource;
import org.custommonkey.xmlunit.XMLUnit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This class tests WSDLEntry
 */
public class WSDLEntryTestCase extends TestCaseAbstract {

    protected void setup() throws Exception {
        super.setUp();

	    XMLUnit.setControlParser(
            "com.volantis.xml.xerces.jaxp.DocumentBuilderFactoryImpl");
        // this next line is strictly not required - if no test parser is
        // explicitly specified then the same factory class will be used for
        // both test and control
        XMLUnit.setTestParser(
            "com.volantis.xml.xerces.jaxp.DocumentBuilderFactoryImpl");

        XMLUnit.setSAXParserFactory(
            "com.volantis.xml.xerces.jaxp.SAXParserFactoryImpl");
        XMLUnit.setTransformerFactory(
            "com.volantis.xml.xalan.processor.TransformerFactoryImpl");
    }
    
    /**
     * Test that the method provideAlternativeInputSource iterates over the
     * list of WSDLResource until a non-null InputSource is retrieved.     
     */ 
    public void testProvideAlternativeInputSource() throws Exception {
        // Create a temporary file
        File tempFile =
                File.createTempFile("URIResourceTestCaseResource", "wsdl");
        // Make sure the file is deleted after the test.
        tempFile.deleteOnExit();        
        // The writers to populate the file.
        FileWriter fw = new FileWriter(tempFile);
        BufferedWriter bw = new BufferedWriter(fw);
        
        // Get hold of the file in the jar.
        String path = getClass().getPackage().getName().replace('.', '/');
        InputStream is =
                getResourceURL(path + "/" + "URIResourceTestCase.wsdl");
        InputStreamReader isr = new InputStreamReader(is);

        // Copy the content of the file in the jar to the temporary file.
        BufferedReader br = new BufferedReader(isr);
        String input = null;
        StringBuffer expectedResult = new StringBuffer();
        while ((input = br.readLine()) != null) {
            expectedResult.append(input);
            bw.write(input);
        }
        bw.flush();
        
        // Create the URIResource
        URIResource resourceA = new URIResource("file:///does/not/exist");
        
        String filePath = "file://" + tempFile.getPath();
        URIResource resourceB = new URIResource(filePath);
        
        TestPipelineFactory factory = new TestPipelineFactory(); 
        
        final XMLPipelineContext pipelineContext = 
                factory.createPipelineContext(); 
                
        
        WSDLResource testResource = new WSDLResource() {
            // javadoc inherited
            public InputSource provideInputSource(XMLPipelineContext context) {
                assertSame("PipelineContext should have been passed to this " +
                        "WSDLResource.", pipelineContext, context);
                return null;
            }
        };
        
        WSDLEntry entry = new WSDLEntry();
        entry.addWSDLResource(testResource);
        entry.addWSDLResource(resourceA);
        entry.addWSDLResource(resourceB);
        
        URLContentManager pipelineManager =
                PipelineURLContentManager.retrieve(pipelineContext);
//        pipelineManager.store(pipelineContext.getPipelineConfiguration());

        // Get the InputSource from the WSDLEntry
        InputSource source = entry.provideAlternativeInputSource(
                pipelineContext, pipelineManager);

        is = source.getByteStream();
        isr = new InputStreamReader(is);
        br = new BufferedReader(isr);

        input = null;
        StringBuffer actualResult = new StringBuffer();
        while ((input = br.readLine()) != null) {
            actualResult.append(input);
        }

        XMLUnit.compareXML(expectedResult.toString(), actualResult.toString());
    }

    /**
     * Returns an InputStream to a given named resource.
     * @param resource the resource to look for.
     * @return the InputStream to the resource
     */
    protected InputStream getResourceURL(String resource) {
        ClassLoader cl;
        InputStream inputStream = null;

        // Use the context class loader first if any.
        cl = Thread.currentThread().getContextClassLoader();
        if (cl != null) {
            inputStream = cl.getResourceAsStream(resource);
        }

        // If that did not work then use the current class's class loader.
        if (inputStream == null) {
            cl = getClass().getClassLoader();
            inputStream = cl.getResourceAsStream(resource);
        }

        // If that did not work then use the system class loader.
        if (inputStream == null) {
            cl = ClassLoader.getSystemClassLoader();
            inputStream = cl.getResourceAsStream(resource);
        }

        return inputStream;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Oct-05	9724/1	philws	VBM:2005092810 Port forward of the generic pipeline connection timeout functionality

 04-Oct-05	9679/1	philws	VBM:2005092810 Provide a connection timeout mechanism and configuration for pipeline operations

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 01-Aug-03	258/1	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 28-Jul-03	232/1	doug	VBM:2003071804 Refactored XMLPipelineContext to reflect new public API

 24-Jun-03	124/1	adrian	VBM:2003061902 Implemented provideInputSource method for ServletRequestResource

 23-Jun-03	111/3	adrian	VBM:2003061903 updated testcase to extend testcase abstract

 23-Jun-03	111/1	adrian	VBM:2003061903 Implemented URIResource provideInputSource method

 ===========================================================================
*/

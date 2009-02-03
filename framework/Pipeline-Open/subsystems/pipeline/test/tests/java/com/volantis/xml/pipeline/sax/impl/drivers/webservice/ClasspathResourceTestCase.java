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

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineContextMock;
import com.volantis.xml.pipeline.sax.drivers.webservice.ClasspathResource;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This class test ClasspathResource.
 */
public class ClasspathResourceTestCase
        extends WSDLResourceTestAbstract {

    /**
     * Test the method provideInputSource.  We need to verify that the method
     * correctly creates an InputSource which gives access to the content of
     * the specified file on the classpath.
     */ 
    public void testProvideInputSource() throws Exception {

        // Get hold of the file in the jar.
        String path = getClass().getPackage().getName().replace('.', '/');
        String filePath = path + "/" + "URIResourceTestCase.wsdl";
        InputStream is = getResourceStream(filePath);
        InputStreamReader isr = new InputStreamReader(is);

        // Copy the content of the file in the jar to a stringbuffer
        BufferedReader br = new BufferedReader(isr);
        String input = null;
        StringBuffer expectedResult = new StringBuffer();
        while ((input = br.readLine()) != null) {
            expectedResult.append(input);            
        }        
        
        // Create the URIResource
        ClasspathResource resource = new ClasspathResource(filePath);
        
        // Get the InputSource from the URIResource.
        InputSource source = resource.provideInputSource(contextMock);

        is = source.getByteStream();
        isr = new InputStreamReader(is);
        br = new BufferedReader(isr);

        input = null;
        StringBuffer actualResult = new StringBuffer();
        while ((input = br.readLine()) != null) {
            actualResult.append(input);
        }
                
        // Check that the expected and actual results are the same.
        assertEquals("Unexpected result from URIResource.",
                expectedResult.toString(), actualResult.toString());
    }

    /**
     * Returns an InputStream to a given named resource.
     * @param resource the resource to look for.
     * @return the InputStream to the resource
     */
    protected InputStream getResourceStream(String resource) {
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
    
    /**
     * Test the method getResourceURL
     */ 
    public void testGetResourceURL() throws Exception {
        // Get hold of the file in the jar.
        String path = getClass().getPackage().getName().replace('.', '/');
        String filePath = path + "/" + 
                "ClasspathResourceTestCase_TestGetResourceURL.txt";
        InputStream is = getResourceStream(filePath);
        InputStreamReader isr = new InputStreamReader(is);

        // Copy the content of the file in the jar to a stringbuffer
        BufferedReader br = new BufferedReader(isr);
        String input = null;
        StringBuffer actualResult = new StringBuffer();
        while ((input = br.readLine()) != null) {
            actualResult.append(input);
        }    
        
        assertEquals("Unexpected content in resource file.", 
                "Some text in resource file.", actualResult.toString());
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

 24-Jun-03	124/1	adrian	VBM:2003061902 Implemented provideInputSource method for ServletRequestResource

 23-Jun-03	118/2	adrian	VBM:2003061901 implemented provideInputSource for ClasspathResource

 ===========================================================================
*/

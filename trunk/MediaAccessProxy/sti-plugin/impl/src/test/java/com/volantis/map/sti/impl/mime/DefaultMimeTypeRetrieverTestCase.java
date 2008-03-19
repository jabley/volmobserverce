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
package com.volantis.map.sti.impl.mime;

import com.volantis.map.common.param.ParametersMock;
import com.volantis.map.common.param.ParameterNames;
import com.volantis.map.operation.ObjectParametersMock;
import com.volantis.map.operation.ResourceDescriptorMock;
import com.volantis.map.retriever.RepresentationMock;
import com.volantis.map.retriever.ResourceRetrieverException;
import com.volantis.map.retriever.ResourceRetrieverMock;
import com.volantis.map.retriever.ResponseHandler;
import com.volantis.map.sti.impl.STIOperation;
import com.volantis.map.sti.mime.MimeTypeRetrieverException;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import com.volantis.testtools.mock.test.MockTestCaseAbstract;

import java.io.IOException;
import java.net.URL;

/**
 * A test case for {@link STIOperation}.
 */
public class DefaultMimeTypeRetrieverTestCase extends MockTestCaseAbstract {

    private DefaultMimeTypeRetriever mimeTypeRetriever;
    private ResourceRetrieverMock resourceRetrieverMock;
    private ResourceDescriptorMock resourceDescriptorMock;
    private ObjectParametersMock inputParametersMock;
    private ParametersMock outputParametersMock;
    private RepresentationMock representationMock;

    // Javadoc inhertited
    public void setUp() throws Exception {
        super.setUp();
        
        // ==================================================================
        // Create mocks.
        // ==================================================================
        resourceDescriptorMock = 
            new ResourceDescriptorMock("resource descriptor", expectations);

        inputParametersMock =
            new ObjectParametersMock("input parameters", expectations);

        outputParametersMock =
            new ParametersMock("output parameters", expectations);
        
        resourceRetrieverMock = 
            new ResourceRetrieverMock("resource retriever", expectations);

        representationMock = 
            new RepresentationMock("representation", expectations);

        // ==================================================================
        // Setup expectations
        // ==================================================================
        resourceDescriptorMock.expects.getInputParameters()
            .returns(inputParametersMock).any();

        resourceDescriptorMock.expects.getOutputParameters()
            .returns(outputParametersMock).any();
        
        // ==================================================================
        // Create object to test.
        // ==================================================================
        mimeTypeRetriever =
            new DefaultMimeTypeRetriever(resourceRetrieverMock);
    }
    
    /**
     * Tests, that if resource URL is missing, MimeTypeRetrieverException is thrown. 
     */
    public void testMissingURL() {
        // ==================================================================
        // Prepare expectations.
        // ==================================================================
        inputParametersMock.fuzzy.getParameterValue(mockFactory.expectsEqual(ParameterNames.SOURCE_URL))
            .returns(null);
        
        // ==================================================================
        // Do the test.
        // ==================================================================
        try {
            mimeTypeRetriever.retrieveMIMEType(resourceDescriptorMock);
            
            fail("MimeTypeRetrieverException expected.");
        } catch (MimeTypeRetrieverException e) {
            // It's OK that MimeTypeRetrieverException is thrown.
        }        
    }

    /**
     * Tests, that if resource URL is invalid, MimeTypeRetrieverException is thrown. 
     */
    public void testInvalidURL() {
        // ==================================================================
        // Prepare expectations.
        // ==================================================================
        inputParametersMock.fuzzy.getParameterValue(mockFactory.expectsEqual(ParameterNames.SOURCE_URL))
            .returns("^*&$@)*&#@");
        
        // ==================================================================
        // Do the test.
        // ==================================================================
        try {
            mimeTypeRetriever.retrieveMIMEType(resourceDescriptorMock);
            
            fail("MimeTypeRetrieverException expected.");
        } catch (MimeTypeRetrieverException e) {
            // It's OK that MimeTypeRetrieverException is thrown.
        }        
    }
    
    /**
     * Tests, that if resourceRetriever throws IOException, MimeTypeRetrieverException is thrown. 
     */
    public void testResourceRetrieverThrowsIOException() throws Exception {
        // ==================================================================
        // Prepare expectations.
        // ==================================================================
        String urlString = "http://www.google.com";
        
        URL url = new URL(urlString);
        
        inputParametersMock.fuzzy.getParameterValue(mockFactory.expectsEqual(ParameterNames.SOURCE_URL))
            .returns(urlString);
        
        resourceRetrieverMock.fuzzy
            .execute(
                    mockFactory.expectsEqual(url),
                    mockFactory.expectsAny())                                            
            .fails(new IOException());
        
        // ==================================================================
        // Do the test.
        // ==================================================================
        try {
            mimeTypeRetriever.retrieveMIMEType(resourceDescriptorMock);
            
            fail("MimeTypeRetrieverException expected.");
        } catch (MimeTypeRetrieverException e) {
            // It's OK that MimeTypeRetrieverException is thrown.
        }        
    }

    /**
     * Tests, that if resourceRetriever throws IOException, MimeTypeRetrieverException is thrown. 
     */
    public void testResourceRetrieverThrowsResourceRetrieverException() throws Exception {
        // ==================================================================
        // Prepare expectations.
        // ==================================================================
        String urlString = "http://www.google.com";
        
        URL url = new URL(urlString);
        
        inputParametersMock.fuzzy.getParameterValue(mockFactory.expectsEqual(ParameterNames.SOURCE_URL))
            .returns(urlString);
        
        resourceRetrieverMock.fuzzy
            .execute(
                    mockFactory.expectsEqual(url),
                    mockFactory.expectsAny())
            .fails(new ResourceRetrieverException("error"));
        
        // ==================================================================
        // Do the test.
        // ==================================================================
        try {
            mimeTypeRetriever.retrieveMIMEType(resourceDescriptorMock);
            
            fail("MimeTypeRetrieverException expected.");
        } catch (MimeTypeRetrieverException e) {
            // It's OK that MimeTypeRetrieverException is thrown.
        }        
    }

    /**
     * Expects the mime type to be returned from the Representation.
     * 
     * @throws Exception
     */
    public void testExpectedMimeType() throws Exception {
        // ==================================================================
        // Prepare expectations.
        // ==================================================================
        String urlString = "http://www.google.com";
        
        String mimeType = "blah-blah-blah";
        
        URL url = new URL(urlString);
        
        inputParametersMock.fuzzy.getParameterValue(mockFactory.expectsEqual(ParameterNames.SOURCE_URL))
            .returns(urlString);
        
        representationMock.expects.getFileType().returns(mimeType);
        
        resourceRetrieverMock.fuzzy
            .execute(
                    mockFactory.expectsEqual(url),
                    mockFactory.expectsAny())
            .returns(representationMock).any();
        
        // ==================================================================
        // Do the test.
        // ==================================================================
        try {
            assertEquals(mimeType, mimeTypeRetriever
                    .retrieveMIMEType(resourceDescriptorMock));
            
        } catch (MimeTypeRetrieverException e) {
            fail("MimeTypeRetrieverException not expected.");
        }        
    }
}

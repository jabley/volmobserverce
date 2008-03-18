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
package com.volantis.map.sti.impl;

import java.io.IOException;

import mock.javax.servlet.http.HttpServletRequestMock;
import mock.javax.servlet.http.HttpServletResponseMock;

import com.volantis.map.operation.ResourceDescriptorMock;
import com.volantis.map.operation.Result;
import com.volantis.map.sti.transcoder.TranscoderException;
import com.volantis.map.sti.transcoder.TranscoderFactoryMock;
import com.volantis.map.sti.transcoder.TranscoderMock;
import com.volantis.testtools.mock.test.MockTestCaseAbstract;

/**
 * A test case for {@link STIOperation}.
 */
public class DefaultOperationTestCase extends MockTestCaseAbstract {

    private TranscoderFactoryMock transcoderFactoryMock;
    private TranscoderMock transcoderMock;
    private HttpServletRequestMock httpServletRequestMock;
    private HttpServletResponseMock httpServletResponseMock;
    private ResourceDescriptorMock resourceDescriptorMock;
    private DefaultOperation stiOperation;

    // Javadoc inhertited
    public void setUp() throws Exception {
        super.setUp();
        
        // ==================================================================
        // Create mocks.
        // ==================================================================

        transcoderFactoryMock = 
            new TranscoderFactoryMock("transcoder factory", expectations);

        transcoderMock = 
            new TranscoderMock("transcoder", expectations);

        httpServletRequestMock =
            new HttpServletRequestMock("HTTP servlet request", expectations);

        httpServletResponseMock =
            new HttpServletResponseMock("HTTP servlet response", expectations);

        resourceDescriptorMock = 
            new ResourceDescriptorMock("resource descriptor", expectations);

        // ==================================================================
        // Create expectations.
        // ==================================================================
        transcoderFactoryMock.expects.createTranscoder()
            .returns(transcoderMock);

        // ==================================================================
        // Create object to test.
        // ==================================================================
        stiOperation =
            new DefaultOperation(transcoderFactoryMock);
    }
    
    /**
     * Tests, that if Transcoder does not throw TranscoderException, the
     * STIOperation is successful.
     */
    public void testSuccess() {
        // ==================================================================
        // Prepare expectations.
        // ==================================================================
        transcoderMock
            .expects.transcode(resourceDescriptorMock, httpServletRequestMock, httpServletResponseMock);

        // ==================================================================
        // Do the test.
        // ==================================================================
        try {
            Result result = stiOperation.execute(resourceDescriptorMock, 
                    httpServletRequestMock, 
                    httpServletResponseMock);
            
            assertEquals(result, Result.SUCCESS);
            
        } catch (Exception e) {
            fail("Exception not expected.");
        }
    }

    /**
     * Tests, that if Transcoder throws TranscoderException, the
     * STIOperation is unsupported.
     */
    public void testUnsupported() {
        // ==================================================================
        // Prepare expectations.
        // ==================================================================
        transcoderMock
            .expects.transcode(resourceDescriptorMock, httpServletRequestMock, httpServletResponseMock)
            .fails(new TranscoderException(null));

        // ==================================================================
        // Do the test.
        // ==================================================================
        try {
            Result result = stiOperation.execute(resourceDescriptorMock, 
                    httpServletRequestMock, 
                    httpServletResponseMock);
            
            assertEquals(result, Result.UNSUPPORTED);
            
        } catch (Exception e) {
            fail("Exception not expected.");
        }        
    }
}

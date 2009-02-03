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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.ics.imageprocessor.parameters.impl;

import junit.framework.TestCase;

import com.volantis.map.common.param.ParameterNames;
import com.volantis.map.common.param.Parameters;
import com.volantis.map.common.param.MutableParameters;
import com.volantis.map.common.param.ParameterNames;
import com.volantis.map.operation.ResourceDescriptor;
import com.volantis.map.operation.ResourceDescriptorFactory;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

/**
 * Tests PreservationParamBuilder
 */
public class PreservationParamsBuilderTestCase extends TestCase {

// @todo uncomment these when the preservation stuff is working again
//    public void testUnsupportedFileFormat() throws Exception {
//
//        PreservationParamsBuilder ppb = new PreservationParamsBuilder();
//
//        ResourceDescriptor descriptor = createResourceDescriptor("image/tiff", 2000);
//        Parameters params = descriptor.getInputParameters();
//        HttpServletRequest request = createHttpRequest("TestDevice/");
//
//        ppb.process(request, descriptor);
//
//        assertEquals("Wrong destination format rule, it shouldn't be "
//                     + params.getParameterValue(ParameterNames.DESTINATION_FORMAT_RULE),
//                     OutputImageRules.COLOURJPEG24,
//                     params.getParameterValue(ParameterNames.DESTINATION_FORMAT_RULE));
//    }
//
//    public void testLargeFileSize() throws Exception {
//
//        PreservationParamsBuilder ppb = new PreservationParamsBuilder();
//
//        ResourceDescriptor descriptor = createResourceDescriptor("image/jpeg", 1000000);
//        Parameters params = descriptor.getInputParameters();
//        HttpServletRequest request = createHttpRequest("TestDevice/");
//
//        ppb.process(request, descriptor);
//
//        int outputImageSize = params.getInteger(ParameterNames.IMAGE_SIZE);
//
//        //check if output image size is bigger than maximum supported size
//        // 30000 is the maximum supported size of the image
//        boolean sizeIsOK = outputImageSize <= 30000;
//        assertTrue("Image is too large", sizeIsOK);
//    }


    public void testNotUsingPreservation() throws Exception {

        PreservationParamsBuilder ppb = new PreservationParamsBuilder();

        // override the default repository url with nothing
        ResourceDescriptor descriptor = createResourceDescriptor("image/jpeg",
                                                                 2000);
        Parameters params = descriptor.getInputParameters();
        HttpServletRequest request = createHttpRequest("TestDevice/");

        ppb.process(request, descriptor);

        assertFalse("MaxImageSize shouldn't have been changed ",
                    params.containsName(ParameterNames.MAX_IMAGE_SIZE));
        assertFalse("DestinationFormatRule shouldn't have been changed",
                    params.containsName(ParameterNames.DESTINATION_FORMAT_RULE));
    }

    /**
     * Creates HTTP servlet request with pointed device as user agent.
     *
     * @param device - device name.
     * @return HttpServletRequest with pointed device as user agent.
     */
    private HttpServletRequest createHttpRequest(String device) {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setHeaderField("User-Agent", device);

        return request;
    }

    /**
     * Creates parameter block and sets parameters needed for testing.
     *
     * @param ImageMIMEType - input image mimetype.
     * @param ImageSize     - input image size.
     * @return Params - parameter block.
     *
     * @throws Exception
     */
    private ResourceDescriptor createResourceDescriptor(String ImageMIMEType,
                                                        int ImageSize)
        throws Exception {

        ResourceDescriptor descriptor = ResourceDescriptorFactory.getInstance().
            createDescriptor("an-external-id", "a-resource-type");
        MutableParameters params = descriptor.getInputParameters();
        params.setParameterValue(ParameterNames.SOURCE_IMAGE_MIME_TYPE, ImageMIMEType);
        params.setParameterValue("InputImageSize",
                                 Integer.toString(ImageSize));
        params.setParameterValue(ParameterNames.DEVICE_REPOSITORY_URL,
                                 getRepositoryURL());

        return descriptor;
    }

    private String getRepositoryURL()
        throws Exception {

        URL url = PreservationParamsBuilderTestCase.class
            .getResource("rep/repository_test.mdpr");

        return url.toExternalForm();
    }
}

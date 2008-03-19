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

package com.volantis.map.ics.imageprocessor.tool.impl;

import junit.framework.TestCase;

import com.volantis.map.common.param.ParameterNames;
import com.volantis.map.ics.configuration.ImageConstants;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertor;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertorFactory;
import com.volantis.map.ics.imageprocessor.convertor.ImageRule;
import com.volantis.map.operation.ObjectParameters;
import com.volantis.map.operation.ResourceDescriptorFactory;

import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

/**
 * Tests WatermarkingTool
 */
public class WatermarkingToolTestCase extends TestCase {

    public void testWatermark() throws Exception {

        WatermarkingTool tool = new WatermarkingTool();

        ObjectParameters params = createParams();
        URL input =
            WatermarkingToolTestCase.class.getResource(
                "images/circusshow4256.bmp");
        RenderedOp sourceImage = JAI.create("url", input);
        ImageConvertor cnv =
            ImageConvertorFactory.getInstance().
            getImageConvertor(ImageRule.TRUECOLOUR);
        sourceImage = cnv.convert(sourceImage, params);
        RenderedOp[] watermarkedImage =
            tool.process(new RenderedOp[]{sourceImage}, params);

        assertNotNull("The RenderedOp shouldn't be null", watermarkedImage);

    }

    private ObjectParameters createParams() throws Exception {

        ObjectParameters params = ResourceDescriptorFactory.getInstance()
            .createParameters();
        URL input = WatermarkingToolTestCase.class.getResource(
            "images/trans-test.gif");
        params.setParameterValue(ParameterNames.WATERMARK_URL, input.toString());
        params.setParameterValue(ParameterNames.IMAGE_WIDTH, "1419");
        params.setParameterValue(ParameterNames.SCALE_MODE,
                                 Integer.toString(
                                     ImageConstants.SCALE_MODE_BILINEAR));
        params.setParameterValue(ParameterNames.SCALE_LARGER, "true");

        ImageInputStream iis = ImageIO.createImageInputStream(input.openStream());
        params.setObject(ParameterNames.WATERMARK_INPUT_STREAM, iis);

        return params;
    }


}

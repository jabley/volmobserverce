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
package com.volantis.map.ics.configuration;


import com.volantis.map.common.param.ParameterNames;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


/**
 * ConfigurationParserTestCase
 */
public class ConfigurationParserTestCase extends TestCase {

    /**
     * Constructor for ConfigurationParserTestCase.
     *
     * @param arg0
     */
    public ConfigurationParserTestCase(String arg0) {
        super(arg0);
    }

    public void testWatermark() throws Exception {
        String xml =
            "<?xml version='1.0'?>" +
            "<!DOCTYPE transcodingServer>" +
            "<transcodingServer>" +
            "<image jpegFormat='progressive' gifSupport='true' >" +
            "<environment serverMode='native' imgHost='penguin' " +
            "imgPort='5000' useInMemoryIOCache='true' />" +
            "<scaling scaleMode='bicubic' scaleLarger='true' />" +
            "<dithering>" + "<dither bitDepth='1' mode='floyd-steinberg' />" +
            "<dither bitDepth='2' mode='ordered-dither' />" +
            "<dither bitDepth='4' mode='stucki' />" +
            "<dither bitDepth='8' mode='jarvis' />" +
            "<dither bitDepth='16' mode='floyd-steinberg' />" +
            "</dithering>" +
            "<compression qualityMin='30' paletteMin='4' />" +
            "<watermarking watermarkURL='eeh by gum, eckie thump' />" +
            "</image>" +
            "</transcodingServer>";

        Configuration conf = null;

        InputStream stream = new ByteArrayInputStream(xml.getBytes());
        conf = ConfigurationParserFactory.getInstance().
            createConfigurationParser().unmarshal(stream);

        assertEquals("JPEG Mode", conf.getJpegMode(),
                     ImageConstants.JPEG_PROGRESSIVE);
        assertEquals("GIF Support", conf.isGifEnabled(), true);
        assertEquals("Native Mode", conf.isNativeMode(), true);
        assertEquals("Image Host", conf.getHost(), "penguin");
        assertEquals("Host Port", conf.getHostPort(), 5000);
        assertEquals("Watermark", conf.getWatermarkURL(), "eeh by gum, eckie thump");

    }

    public void testHost() {
        String xml =
            "<?xml version='1.0'?>" +
            "<!DOCTYPE transcodingServer>" +
            "<transcodingServer>" +
            "<image jpegFormat='progressive' gifSupport='true' >" +
            "<environment serverMode='native' imgHost='' " +
            "imgPort='5000' proxyhost='' useInMemoryIOCache='true' />" +
            "<scaling scaleMode='bicubic' scaleLarger='true' />" +
            "<dithering>" + "<dither bitDepth='1' mode='floyd-steinberg' />" +
            "<dither bitDepth='2' mode='ordered-dither' />" +
            "<dither bitDepth='4' mode='stucki' />" +
            "<dither bitDepth='8' mode='jarvis' />" +
            "<dither bitDepth='16' mode='floyd-steinberg' />" +
            "</dithering>" +
            "<compression qualityMin='30' paletteMin='4' />" +
            "<watermarking watermarkURL='eeh by gum, eckie thump' />" +
            "</image>" +
            "</transcodingServer>";

         
        InputStream stream = new ByteArrayInputStream(xml.getBytes());
        Configuration conf = ConfigurationParserFactory.getInstance().createConfigurationParser().unmarshal(stream);

        assertNull("host is not specified so should be null", conf.getHost());
        assertNull("Proxy host is not specified so should be null", conf.getProxy());

    }

    /**
     * Test for all elements
     */
    public void testDefaults() {
        String xml =
            "<?xml version='1.0'?>" +
            "<!DOCTYPE transcodingServer>" +
            "<transcodingServer />";

        Configuration conf = null;

        InputStream stream = new ByteArrayInputStream(xml.getBytes());
        conf = ConfigurationParserFactory.getInstance().createConfigurationParser().unmarshal(stream);

        assertEquals("JPEG Mode", conf.getJpegMode(),
                     ImageConstants.JPEG_BASELINE);
        assertEquals("GIF Support", conf.isGifEnabled(), false);
        assertEquals("Native Mode", conf.isNativeMode(), true);
        assertEquals("Image Host", conf.getHost(), null);
        assertEquals("Host Port", conf.getHostPort(), 80);
        assertEquals("Scale Mode", conf.getScaleMode(),
                     ImageConstants.SCALE_MODE_BILINEAR);
        assertEquals("Scale Larger", conf.canScaleLarger(), false);
        assertEquals("1 Bit Dither", conf.getDitherMode(1),
                     DitherMode.FLOYD);
        assertEquals("2 Bit Dither", conf.getDitherMode(2),
                     DitherMode.FLOYD);
        assertEquals("4 Bit Dither", conf.getDitherMode(4),
                     DitherMode.FLOYD);
        assertEquals("8 Bit Dither", conf.getDitherMode(8),
                     DitherMode.FLOYD);
        assertEquals("16 Bit Dither", conf.getDitherMode(16),
                     DitherMode.FLOYD);
        assertEquals("JPEG Quality", conf.getMinimumJPEGQuality(), 30);
        assertEquals("Min Palette", conf.getMinimumBitDepth(), 8);
    }

    /**
     * Test for all elements
     */
    public void testAllElements() {
        String xml =
            "<?xml version='1.0'?>" +
            "<!DOCTYPE transcodingServer>" +
            "<transcodingServer>" +
            "<image jpegFormat='progressive' gifSupport='true' >" +
            "<environment serverMode='native' imgHost='penguin' " +
            "imgPort='5000' useInMemoryIOCache='true' />" +
            "<scaling scaleMode='bicubic' scaleLarger='true' />" +
            "<dithering>" + "<dither bitDepth='1' mode='floyd-steinberg' />" +
            "<dither bitDepth='2' mode='ordered-dither' />" +
            "<dither bitDepth='4' mode='stucki' />" +
            "<dither bitDepth='8' mode='jarvis' />" +
            "<dither bitDepth='16' mode='floyd-steinberg' />" +
            "</dithering>" +
            "<compression qualityMin='30' paletteMin='4' />" +
            "<watermarking watermarkURL='eeh by gum, eckie thump' />" +
            "</image>" +
            "</transcodingServer>";

        Configuration conf = null;

        InputStream stream = new ByteArrayInputStream(xml.getBytes());
        conf = ConfigurationParserFactory.getInstance().createConfigurationParser().unmarshal(stream);

        assertEquals("JPEG Mode", conf.getJpegMode(),
                     ImageConstants.JPEG_PROGRESSIVE);
        assertEquals("GIF Support", conf.isGifEnabled(), true);
        assertEquals("Native Mode", conf.isNativeMode(), true);
        assertEquals("Image Host", conf.getHost(), "penguin");
        assertEquals("Host Port", conf.getHostPort(), 5000);
        assertEquals("Scale Mode", conf.getScaleMode(),
                     ImageConstants.SCALE_MODE_BICUBIC);
        assertEquals("Scale Larger", conf.canScaleLarger(), true);
        assertEquals("1 Bit Dither", conf.getDitherMode(1),
                     DitherMode.FLOYD);
        assertEquals("2 Bit Dither", conf.getDitherMode(2),
                     DitherMode.ORDERED);
        assertEquals("4 Bit Dither", conf.getDitherMode(4),
                     DitherMode.STUCKI);
        assertEquals("8 Bit Dither", conf.getDitherMode(8),
                     DitherMode.JARVIS);
        assertEquals("16 Bit Dither", conf.getDitherMode(16),
                     DitherMode.FLOYD);
        assertEquals("JPEG Quality", conf.getMinimumJPEGQuality(), 30);
        assertEquals("Min Palette", conf.getMinimumBitDepth(), 4);
        assertEquals(ParameterNames.WATERMARK_URL, conf.getWatermarkURL(),
                     "eeh by gum, eckie thump");
    }

    /**
     * Test for compression element
     */
    public void testCompression() {
        {
            String xml =
                "<?xml version='1.0'?>" +
                "<!DOCTYPE transcodingServer>" +
                "<transcodingServer>" +
                "<image>" +
                "<compression qualityMin='90' paletteMin='4' />" +
                "</image>" +
                "</transcodingServer>";

            Configuration conf = null;

            InputStream stream = new ByteArrayInputStream(xml.getBytes());
            conf = ConfigurationParserFactory.getInstance().createConfigurationParser().unmarshal(stream);

            assertEquals("JPEG Quality", conf.getMinimumJPEGQuality(), 90);
            assertEquals("Min Palette", conf.getMinimumBitDepth(), 4);
        }
        {
            String xml =
                "<?xml version='1.0'?>" +
                "<!DOCTYPE transcodingServer>" +
                "<transcodingServer>" +
                "<image>" +
                "<compression />" +
                "</image>" +
                "</transcodingServer>";

            InputStream stream = new ByteArrayInputStream(xml.getBytes());
            Configuration conf = ConfigurationParserFactory.getInstance().createConfigurationParser().unmarshal(stream);

            assertEquals("JPEG Quality", conf.getMinimumJPEGQuality(), 30);
            assertEquals("Min Palette", conf.getMinimumBitDepth(), 8);
        }
    }

    /**
     * Test for dithering element
     */
    public void testDithering() {

        {
            String xml =

            "<?xml version='1.0'?>" +
            "<!DOCTYPE transcodingServer>" +
            "<transcodingServer>" +
            "<image jpegFormat='progressive' gifSupport='true' >" +
            "<dithering>" + "<dither bitDepth='1' mode='floyd-steinberg' />" +
            "<dither bitDepth='2' mode='patterned' />" +
            "<dither bitDepth='8' mode='jarvis' />" +
            "<dither bitDepth='16' mode='floyd-steinberg' />" +
            "</dithering>" +
            "</image>" +
            "</transcodingServer>";

            InputStream stream = new ByteArrayInputStream(xml.getBytes());
            Configuration conf = ConfigurationParserFactory.getInstance().createConfigurationParser().unmarshal(stream);

            assertEquals("1 Bit Dither", conf.getDitherMode(1),
                         DitherMode.FLOYD);
            assertEquals("2 Bit Dither", conf.getDitherMode(2),
                         DitherMode.PATTERNED);
            assertEquals("4 Bit Dither", conf.getDitherMode(4),
                         DitherMode.FLOYD);
            assertEquals("8 Bit Dither", conf.getDitherMode(8),
                         DitherMode.JARVIS);
            assertEquals("16 Bit Dither", conf.getDitherMode(16),
                         DitherMode.FLOYD);
        }
        {
            String xml =
                "<?xml version='1.0'?>" +
                "<!DOCTYPE transcodingServer>" +
                "<transcodingServer>" +
                "<image><dithering /></image>" +
                "</transcodingServer>";

            InputStream stream = new ByteArrayInputStream(xml.getBytes());
            Configuration conf = ConfigurationParserFactory.getInstance().createConfigurationParser().unmarshal(stream);

            assertEquals("1 Bit Dither", conf.getDitherMode(1),
                         DitherMode.FLOYD);
            assertEquals("2Bit Dither", conf.getDitherMode(2),
                         DitherMode.FLOYD);
            assertEquals("4 Bit Dither", conf.getDitherMode(4),
                         DitherMode.FLOYD);
            assertEquals("8 Bit Dither", conf.getDitherMode(8),
                         DitherMode.FLOYD);
            assertEquals("16 Bit Dither", conf.getDitherMode(16),
                         DitherMode.FLOYD);
        }
    }

    /**
     * Test the scaling element
     */
    public void testScaling() {
        {
            String xml =
                "<?xml version='1.0'?>" +
                "<!DOCTYPE transcodingServer>" +
                "<transcodingServer>" +
                "<image>" +
                "<scaling scaleMode='bicubic' scaleLarger='true' />" +
                "</image>" +
                "</transcodingServer>";

            Configuration conf = null;

            InputStream stream = new ByteArrayInputStream(xml.getBytes());
            conf = ConfigurationParserFactory.getInstance().createConfigurationParser().unmarshal(stream);

            assertEquals("Scale Mode", conf.getScaleMode(),
                         ImageConstants.SCALE_MODE_BICUBIC);
            assertEquals("Scale Larger", conf.canScaleLarger(), true);
        }
        {
            String xml =
                "<?xml version='1.0'?>" +
                "<!DOCTYPE transcodingServer>" +
                "<transcodingServer>" +
                "<image>" +
                "<scaling scaleMode='bilinear' />" +
                "</image>" +
                "</transcodingServer>";

            InputStream stream = new ByteArrayInputStream(xml.getBytes());
            Configuration conf = ConfigurationParserFactory.getInstance().createConfigurationParser().unmarshal(stream);

            assertEquals("Scale Mode", conf.getScaleMode(),
                         ImageConstants.SCALE_MODE_BILINEAR);
            assertEquals("Scale Larger", conf.canScaleLarger(), false);
        }
        {
            String xml =
                "<?xml version='1.0'?>" +
                "<!DOCTYPE transcodingServer>" +
                "<transcodingServer>" +
                "<image>" +
                "<scaling scaleMode='nearest' />" +
                "</image>" +
                "</transcodingServer>";

            InputStream stream = new ByteArrayInputStream(xml.getBytes());
            Configuration conf = ConfigurationParserFactory.getInstance().createConfigurationParser().unmarshal(stream);

            assertEquals("Scale Mode", conf.getScaleMode(),
                         ImageConstants.SCALE_MODE_NEAREST);
            assertEquals("Scale Larger", conf.canScaleLarger(), false);
        }
    }

    /**
     * Test the environment element
     */
    public void testEnvironment() {
        {
            String xml =
                "<?xml version='1.0'?>" +
                "<!DOCTYPE transcodingServer>" +
                "<transcodingServer><image>" +
                "<environment serverMode='native' imgHost='penguin' " +
                "imgPort='5000'/>" +
                "</image></transcodingServer>";

            Configuration conf = null;

            InputStream stream = new ByteArrayInputStream(xml.getBytes());
            conf = ConfigurationParserFactory.getInstance().createConfigurationParser().unmarshal(stream);

            assertEquals("Native Mode", conf.isNativeMode(), true);
            assertEquals("Image Host", conf.getHost(), "penguin");
            assertEquals("Host Port", conf.getHostPort(), 5000);
        }
        {
            String xml =
                "<?xml version='1.0'?>" +
                "<!DOCTYPE transcodingServer>" +
                "<transcodingServer><image>" +
                "<environment serverMode='transforce' />" +
                "</image></transcodingServer>";

            InputStream stream = new ByteArrayInputStream(xml.getBytes());
            Configuration conf = ConfigurationParserFactory.getInstance().createConfigurationParser().unmarshal(stream);

            assertEquals("Native Mode", conf.isNativeMode(), false);
            assertEquals("Image Host", conf.getHost(), null);
            assertEquals("Host Port", conf.getHostPort(), 80);
        }
    }

    /**
     * Test the image element
     */
    public void testImage() {
        {
            String xml =
                "<?xml version='1.0'?>" +
                "<!DOCTYPE transcodingServer>" +
                "<transcodingServer>" +
                "<image jpegFormat='progressive' gifSupport='true' />" +
                "</transcodingServer>";

            Configuration conf = null;

            InputStream stream = new ByteArrayInputStream(xml.getBytes());
            conf = ConfigurationParserFactory.getInstance().createConfigurationParser().unmarshal(stream);

            assertEquals("JPEG Mode", conf.getJpegMode(),
                         ImageConstants.JPEG_PROGRESSIVE);
            assertEquals("GIF Support", conf.isGifEnabled(), true);
        }
        {
            String xml =
                "<?xml version='1.0'?>" +
                "<!DOCTYPE transcodingServer>" +
                "<transcodingServer>" +
                "<image jpegFormat='baseline' gifSupport='false' />" +
                "</transcodingServer>";

            InputStream stream = new ByteArrayInputStream(xml.getBytes());
            Configuration conf = ConfigurationParserFactory.getInstance().createConfigurationParser().unmarshal(stream);

            assertEquals("JPEG Mode", conf.getJpegMode(),
                         ImageConstants.JPEG_BASELINE);
            assertEquals("GIF Support", conf.isGifEnabled(), false);
        }
    }
}



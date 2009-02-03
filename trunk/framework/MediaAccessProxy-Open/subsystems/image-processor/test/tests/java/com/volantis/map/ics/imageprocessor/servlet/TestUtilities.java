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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.ics.imageprocessor.servlet;

import com.volantis.map.common.param.ParameterNames;
import com.volantis.map.ics.imageprocessor.impl.ICSOperation;
import com.volantis.map.ics.imageprocessor.parameters.ICSParamBuilder;
import com.volantis.map.ics.configuration.OutputImageRules;
import com.volantis.map.operation.ResourceDescriptor;
import com.volantis.map.operation.ResourceDescriptorFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.synergetics.osgi.boot.BootConstants;
import com.volantis.synergetics.path.Path;
import com.volantis.testtools.mock.ExpectationContainer;
import com.volantis.testtools.mock.MockFactory;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import mock.javax.servlet.http.HttpServletRequestMock;
import mock.javax.servlet.http.HttpServletResponseMock;
import mock.org.osgi.service.component.ComponentContextMock;
import mock.org.osgi.framework.BundleContextMock;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;
import javax.media.jai.ParameterBlockJAI;
import javax.servlet.ServletOutputStream;
import java.awt.image.ColorModel;
import java.awt.image.SampleModel;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A utility class contianing a number of methods that are used by the ICS test
 * framework.
 */
public class TestUtilities {

    /**
     * Stop it being instantiated
     */
    private TestUtilities() {
    }

    /**
     * Load the named image given a rule and perform a conversion as specified
     * by the parameters using the rule.
     *
     * @param expectationContainer expectations container to use
     * @param rule                 the transcode rule to use (OutputImageRules.GREYSCALEGIF8 for
     *                             example)
     * @param fileName             the filename to load
     * @param parameters           String[n][2] containing the parameter name
     *                             and value for each parameter used
     * @param enableGif            true if gif support should be enabled, false
     *                             otherwise.
     * @return a FileCacheSeekableStream containing the new image.
     */
    public static InputStream doImageTranscodeTest(
        ExpectationContainer expectationContainer,
        String rule,
        String fileName,
        String[][] parameters,
        boolean enableGif) throws Exception {

        ResourceDescriptor descriptor = ResourceDescriptorFactory.getInstance()
            .createDescriptor("ics", "image.");
        URL sourceURL = TestUtilities.class.getResource("images/" + fileName);
        descriptor.getInputParameters().setParameterValue(ParameterNames.SOURCE_URL, sourceURL.toExternalForm());

        HttpServletRequestMock request = new HttpServletRequestMock(
            "test-request", expectationContainer);
        String pathInfo = "/" + rule + "/" + fileName;
        request.expects.getPathInfo().returns(pathInfo).any();
        final MockFactory mf = MockFactory.getDefaultInstance();

        // use a vector as we need to return an enumeration for calls to
        // getHeaderNames()
        final Map params = new HashMap();
        request.fuzzy.getParameter(mf.expectsAny()).does(new MethodAction(){
            public Object perform(MethodActionEvent event) throws Throwable {
                return params.get(event.getArguments()[0]);
            }
        }).any();

        StringBuffer url = new StringBuffer(pathInfo);
        char c = '?';
        if (parameters != null) {
            url.append('/');
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i].length != 2) {
                    throw new IllegalArgumentException(
                        "Supplied parameter " +
                        i +
                        " has incorrect " +
                        "length: expected 2 got " +
                        parameters[i].length);
                }
                url.append(c).append(parameters[i][0]).append("=").append(parameters[i][1]);
                c = '&';
                params.put(parameters[i][0], parameters[i][1]);
            }
        }

        // Hack to enable gif support without having a config file.
        if (enableGif) {
            params.put("v.gifEnabled", "true");
            url.append(c).append("v.gifEnabled=true");
        }

        request.expects.getParameterNames().does(new MethodAction() {
            public Object perform(MethodActionEvent event) throws Throwable {
                return Collections.enumeration(params.keySet());
            }
        }).any();

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ServletOutputStream os = new ServletOutputStream() {
            public void write(int b) {
                outputStream.write(b);
            }
        };
        HttpServletResponseMock response = new HttpServletResponseMock(
            "test-response", expectationContainer);
        response.expects.getOutputStream().returns(os).any();

        // content type should only be set once
        response.fuzzy.setContentType(mf.expectsAny()).returns().any();
        response.fuzzy.setDateHeader(mf.expectsAny(), mf.expectsAny()).returns().any();
        response.fuzzy.addDateHeader(mf.expectsAny(), mf.expectsAny()).returns().any();
        response.fuzzy.addHeader(mf.expectsAny(), mf.expectsAny()).returns().any();
        response.fuzzy.addIntHeader(mf.expectsAny(), mf.expectsAny()).returns().any();

        response.fuzzy.setContentLength(mf.expectsAny()).returns().any();

        ICSParamBuilder builder = new ICSParamBuilder();
        builder.build(new URI(url.toString()),
                      descriptor.getInputParameters());

        ComponentContextMock componentCtx
                = new ComponentContextMock("componentCtx", expectationContainer);
        ICSOperation op = new ICSOperation();

        executeICSOperation(expectationContainer, descriptor, request, response);

        ByteArrayInputStream bais = new ByteArrayInputStream(
            outputStream.toByteArray());

        return bais;
    }

    public static RenderedOp transcodeToImage(
        ExpectationContainer expectations,
        String rule,
        String fileName,
        String[][] parameters,
        boolean enableGif)
            throws Exception {

        InputStream stream = TestUtilities.doImageTranscodeTest(
                expectations, rule, fileName, parameters, enableGif);

        ImageReader reader = null;
        ParameterBlockJAI pblock = new ParameterBlockJAI("ImageRead");
        pblock.setParameter("Input", ImageIO.createImageInputStream(stream));
        if (rule.equals(OutputImageRules.WBMP)) {
            // wbmps do not have a magic number so
            // ImageRead operation cannot recognize them.
            pblock.setParameter("Reader",
                ImageIO.getImageReadersByFormatName("wbmp").next());
        }
        RenderedOp image = JAI.create("ImageRead", pblock);

        return image;
    }

    /**
     * Determine if an image is a 24 bit true colour image
     */
    public static boolean isTrueColour(RenderedOp image) {
        ColorModel cm = image.getColorModel();
        SampleModel sm = image.getSampleModel();

        return ((cm.getPixelSize() == 24
                 || cm.getPixelSize() == 32) &&
            (cm.getNumColorComponents() == 3)
            && (sm.getNumBands() == 3 || sm.getNumBands() == 4)) ? true : false;
    }

    /**
     * Determine if an image is a greyscale image
     *
     * Note: current implementation is equivalent to isGreyscale8 
     */
    public static boolean isGreyscale(RenderedOp image) {
        return isGreyscale8(image);
    }

    /**
      * Determine if an image is a greyscale image
      */
     public static boolean isGreyscale16(PlanarImage image) {
         ColorModel cm = image.getColorModel();
         SampleModel sm = image.getSampleModel();
         return ((cm.getPixelSize() == 16) &&
             ((cm.getNumColorComponents() == 1)
             || (cm.getNumColorComponents() == 3)) &&
             (sm.getNumBands() == 1)) ? true : false;
     }

     /**
      * Determine if an image is a greyscale image
      */
     public static boolean isGreyscale8(PlanarImage image) {
         ColorModel cm = image.getColorModel();
         SampleModel sm = image.getSampleModel();
         return ((cm.getPixelSize() == 8) &&
             ((cm.getNumColorComponents() == 1)
             || (cm.getNumColorComponents() == 3)) &&
             (sm.getNumBands() == 1)) ? true : false;
     }

    /**
     * Determine if an image is a greyscale image
     */
    public static boolean isGreyscale4(RenderedOp image) {
        ColorModel cm = image.getColorModel();
        SampleModel sm = image.getSampleModel();

        return ((cm.getPixelSize() == 4) &&
            ((cm.getNumColorComponents() == 1)
            || (cm.getNumColorComponents() == 3)) &&
            (sm.getNumBands() == 1)) ? true : false;
    }

    /**
     * Determine if an image is a greyscale image
     */
    public static boolean isGreyscale2(RenderedOp image) {
        ColorModel cm = image.getColorModel();
        SampleModel sm = image.getSampleModel();

        return ((cm.getPixelSize() == 2) &&
            ((cm.getNumColorComponents() == 1)
            || (cm.getNumColorComponents() == 3)) &&
            (sm.getNumBands() == 1)) ? true : false;
    }

    /**
     * Determine if an image is an indexed image
     */
    public static boolean isIndexed(RenderedOp image) {
        ColorModel cm = image.getColorModel();
        SampleModel sm = image.getSampleModel();

        return ((cm.getPixelSize() == 8) && (cm.getNumColorComponents() == 3)
            && (sm.getNumBands() == 1)) ? true : false;
    }

    /**
     * Determine if an image is an monochrome image
     */
    public static boolean isMonochrome(RenderedOp image) {
        ColorModel cm = image.getColorModel();
        SampleModel sm = image.getSampleModel();

        return ((cm.getPixelSize() == 1) &&
            ((cm.getNumColorComponents() == 1)
            || (cm.getNumColorComponents() == 3)) &&
            (sm.getNumBands() == 1)) ? true : false;
    }

    public static void checkConversionResult(String rule, RenderedOp img){

        if (rule.startsWith("c") && rule.endsWith("24")) {
            TestCaseAbstract.assertEquals("Rule was " + rule + " but result is not truecolour",
                    true, TestUtilities.isTrueColour(img));
        } else if (rule.startsWith("c") && rule.endsWith("8")) {
            TestCaseAbstract.assertEquals("Rule was " + rule + " but result is not indexed",
                    true, TestUtilities.isIndexed(img));
        } else if (rule.startsWith("gp") && !rule.endsWith("1")) {
            // Note: apparently rendering operation from JAI
            // renders greyscale PNG always as 8-bit greyscale
            // so we check it this special way
            TestCaseAbstract.assertEquals("Rule was " + rule + " but result is not greyscale",
                    true, TestUtilities.isGreyscale(img));
        } else if (rule.startsWith("g") && rule.endsWith("16")) {
            TestCaseAbstract.assertEquals("Rule was " + rule + " but result is not greyscale16",
                    true, TestUtilities.isGreyscale16(img));
        } else if (rule.startsWith("g") && rule.endsWith("8")) {
            TestCaseAbstract.assertEquals("Rule was " + rule + " but result is not greyscale8",
                    true, TestUtilities.isGreyscale8(img));
        } else if (rule.startsWith("g") && rule.endsWith("4")) {
            TestCaseAbstract.assertEquals("Rule was " + rule + " but result is not greyscale4",
                    true, TestUtilities.isGreyscale4(img));
        } else if (rule.startsWith("g") && rule.endsWith("2")) {
            TestCaseAbstract.assertEquals("Rule was " + rule + " but result is not greyscale2",
                    true, TestUtilities.isGreyscale2(img));
        } else if (rule.startsWith("g") && rule.endsWith("1")) {
            TestCaseAbstract.assertEquals("Rule was " + rule + " but result  is not monochrome",
                    true, TestUtilities.isMonochrome(img));
        }
    }

    /**
     * Creates a temporary directory in the <code>java.io.tmpdir</code>
     * directory with a name based on the specified prefix, plus a random
     * number. This directory (and its content) must be deleted by the caller
     * when no longer required.
     *
     * @param prefix the directory's name's prefix
     * @return a file representing the temporary directory that was created
     */
    public static File createTempDir(String prefix) {
        String tmpdir = System.getProperty("java.io.tmpdir");
        File dir = null;
        int count = new Random().nextInt() & 0xffff;

        do {
            dir = new File(new StringBuffer(tmpdir).
                           append(File.separator).
                           append(prefix).
                           append(count).toString());
        } while (!dir.mkdir());

        return dir;
    }

    /**
     *
     *
     * @param expectations
     * @param descriptor
     * @param request
     * @param response
     */
    private static void executeICSOperation(ExpectationContainer expectations,
            ResourceDescriptor descriptor,
            HttpServletRequestMock request,
            HttpServletResponseMock response) throws Exception {

        // @todo: in future we could get ICSOperation by way of OSGI mechanism
        // for now we just use hacks and mocks

        // We need to find a path to the directory were WEB-INF
        // of our fake webapp is located. This directory will
        // be the context area of our app.
        URL webinf = TestUtilities.class.getResource("WEB-INF");
        String contextArea = Path.parse(webinf.getPath()).getParent().asAbsoluteString();

        // Prepare the mock of OSGI environment
        ComponentContextMock componentCtx
                = new ComponentContextMock("componentCtx", expectations);

        BundleContextMock bundleCtx = new BundleContextMock("bundleCtx", expectations);
        componentCtx.expects.getBundleContext()
                    .returns(bundleCtx).any();
        bundleCtx.expects.getProperty(BootConstants.CONTEXT_AREA)
                 .returns(contextArea).any();

        // Lame simulation of creation by OSGi framework
        ICSOperation op = new ICSOperation();
        op.activate(componentCtx);
        try {
            op.execute(descriptor, request, response);
        } finally {
            op.deactivate(componentCtx);
        }
    }
}
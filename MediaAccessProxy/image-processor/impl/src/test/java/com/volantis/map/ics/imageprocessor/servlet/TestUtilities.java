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
import com.volantis.map.operation.ResourceDescriptor;
import com.volantis.map.operation.ResourceDescriptorFactory;
import com.volantis.testtools.mock.ExpectationContainer;
import com.volantis.testtools.mock.MockFactory;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import mock.javax.servlet.http.HttpServletRequestMock;
import mock.javax.servlet.http.HttpServletResponseMock;

import javax.media.jai.RenderedOp;
import javax.servlet.ServletOutputStream;
import java.awt.image.ColorModel;
import java.awt.image.SampleModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    };

    /**
     * Load the named image given a rule and perform a conversion as specified
     * by the parameters using the rule.
     *
     * @param expectationContainer the web .xml file to be used.
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
        boolean enableGif)
        throws Throwable {

//        ConfigurationParserFactory factory = ConfigurationParserFactory.getInstance();
//        Configuration config = factory.createConfigurationParser()
//            .createDefaultConfiguration();

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
                url.append(c).append(parameters[i][0] + "=" + parameters[i][1]);
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

        ICSOperation op = new ICSOperation();              
        op.execute(descriptor, request, response);

        ByteArrayInputStream bais = new ByteArrayInputStream(
            outputStream.toByteArray());

        return bais;
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
     */
    public static boolean isGreyscale(RenderedOp image) {
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

//        System.out.println("Pixel Size:" + cm.getPixelSize());
//        System.out.println("Num Color Components:" + cm.getNumColorComponents());
//        System.out.println("Num Bands:" + sm.getNumBands());

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
     * Can be used to delete a file or a directory and all its content.
     *
     * @param file the file or directory to be deleted. Doesn't have to exist
     */
    public static void delete(File file) throws Exception {
        if (file.isDirectory()) {
            File[] files = file.listFiles();

            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    delete(files[i]);
                }
            } else {
                throw new Exception(
                    "Could not scan directory " + file.getPath());
            }
        }

        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Copy bytes from an InputStream to an OutputStream and close both streams
     * at the end of the copy or if an exception is thrown during the copy
     * attempt.
     *
     * @param in  the source InputStream
     * @param out the target OutputStream
     * @throws IOException if there is a problem with the copy
     */
    public static void copyAndClose(InputStream in, OutputStream out)
        throws IOException {

        if (in == null) {
            throw new IllegalArgumentException("Input stream cannot be null");
        }
        if (out == null) {
            throw new IllegalArgumentException("Output stream cannot be null");
        }
        IOException copyException = null;
        RuntimeException runtimeException = null;
        IOException closeException = null;
        try {
            copy(in, out);
        } catch (IOException e) {
            copyException = e;
        } catch (RuntimeException e) {
            runtimeException = e;
        } finally {
            // Ensure we close the input.
            try {
                in.close();
            } catch (IOException e) {
                // Don't throw this exception to avoid masking a possible
                // exception from the copy.
                //logger.error("unexpected-ioexception", e);
                if (closeException == null) {
                    closeException = e;
                }
            }
            // Ensure we close the output.
            try {
                out.close();
            } catch (IOException e) {
                // Don't throw this exception to avoid masking a possible
                // exception from the copy.
                //logger.error("unexpected-ioexception", e);
                if (closeException == null) {
                    closeException = e;
                }
            }
            // Finally throw the first exception we encountered, if any.
            if (copyException != null) {
                throw copyException;
            }
            if (runtimeException != null) {
                throw runtimeException;
            }
            if (closeException != null) {
                throw closeException;
            }
        }
    }

    /**
     * Copy bytes from an InputStream an OutputStream.
     *
     * @param in  the source InputStream.
     * @param out the target OutputStream.
     * @throws IOException If there was a problem with the copy.
     */
    public static void copy(InputStream in, OutputStream out)
        throws IOException {

        byte buf[] = new byte[1024];
        int len;
        while ((len = in.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
        out.flush();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Feb-05	311/1	rgreenall	VBM:2005012701 Resolved conflicts

 27-Jan-05	289/1	matthew	VBM:2005012617 Fix problems with transcoding gif/png with transparency and refactor test case

 24-Jan-05	276/4	matthew	VBM:2004121009 Allow transcoding of indexed images that have a transparency

 19-Jan-05	270/1	matthew	VBM:2004121009 Allow correct handling of gif images with transparency

 ===========================================================================
*/

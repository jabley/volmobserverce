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
package com.volantis.xml.pipeline.sax.impl.operations.debug;

import com.volantis.shared.environment.EnvironmentInteraction;
import com.volantis.xml.pipeline.sax.AttributeNormalizingContentHandler;
import com.volantis.xml.pipeline.sax.PipelineTestAbstract;
import com.volantis.xml.pipeline.sax.TestPipelineFactory;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.utilities.sax.XMLReaderFactory;
import com.volantis.xml.xml.serialize.OutputFormat;
import com.volantis.xml.xml.serialize.XMLSerializer;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Test case for the {@link SerializeProcess}
 */
public class SerializeTestCase extends PipelineTestAbstract {

    /**
     * Will be used to reference the location of the Systems TMP dir.
     */
    private String tmpDir;

    /**
     * Creates a new <code>SerializeTestCase</code> instance
     */
    public SerializeTestCase() {
        File tmp = null;
        try {
            tmp = File.createTempFile("myFile", "tmp");
            tmpDir = tmp.getParent();
            if (!tmpDir.endsWith(File.separator)) {
                tmpDir += File.separator;
            }
            System.out.println("TEMP DIR IS " + tmpDir);
        } catch (IOException e) {
            fail("Could not determine the system tmp directory: " +
                 e.getMessage() );
            e.printStackTrace();
        } finally {
            tmp.delete();
        }
    }

    /**
     * Test a single "serialize" element.
     */
    public void testSimpleSerialize() throws Exception {
        try {
            doTest(createPipelineFactory(tmpDir, true),
                   "SimpleSerializeTestCase.input.xml",
                   "SimpleSerializeTestCase.expected.xml");
        } finally {
            File logFile = new File(tmpDir + "SimpleTest.txt");
            if (logFile.exists()) {
                assertSerialization(logFile,
                                    "SimpleSerializeTestCase.serialized.xml");
            } else {
                fail("The seriaize process did not write to the expected log file");
            }
        }
    }

    /**
     * Ensure that no content is serialized when the serialize process is not
     * "Active"
     * @throws Exception if an error occurs
     */
    public void testSerializeNotActive() throws Exception {
        try {
            doTest(createPipelineFactory(tmpDir, true),
                   "SerializeNotActiveTestCase.input.xml",
                   "SerializeNotActiveTestCase.expected.xml");
        } finally {
            File logFile = new File(tmpDir + "SimpleTest.txt");
            if (logFile.exists()) {
                fail("The serialize process should not have serialized to disk");
            }
        }
    }

    /**
     * Ensure that no content is serialized when the
     * {@link XMLPipelineContext#setDebugOutputFilesPrefix} has not been set
     * @throws Exception if an error occurs
     */
    public void testSerializeNotActiveWhenNoSerializeConfiguration()
                throws Exception {
        try {
            doTest(createPipelineFactory(tmpDir, false),
                   "SimpleSerializeTestCase.input.xml",
                   "SimpleSerializeTestCase.expected.xml");
        } finally {
            File logFile = new File(tmpDir + "SimpleTest.txt");
            if (logFile.exists()) {
                fail("The serialize process should not have serialized to disk");
            }
        }
    }

    /**
     * Ensure that no content is serialized when the serialize element does
     * not provide a "filesSuffix" attribute
     * @throws Exception if an error occurs
     */
    public void testSerializeNotActiveWhenNoFileSuffixAttribute() throws Exception {
        try {
            doTest(createPipelineFactory(tmpDir, false),
                   "SerializeNotActiveWhenNoSuffixTestCase.input.xml",
                   "SerializeNotActiveWhenNoSuffixTestCase.expected.xml");
        } finally {
            File logFile = new File(tmpDir + "SimpleTest.txt");
            if (logFile.exists()) {
                fail("The serialize process should not have serialized to disk");
            }
        }
    }

    /**
     * Test a single "serialize" element.
     */
    public void testSerializeCreatesDir() throws Exception {
        String outputDir = tmpDir + "debugOutput" + File.separator;
        // check that the dir does not exist
        File outputDirFile = new File(outputDir);
        if (outputDirFile.exists()) {
            fail("the directory " + outputDir + "should not exist");
        }
        try {
            doTest(createPipelineFactory(outputDir, true),
                   "SimpleSerializeTestCase.input.xml",
                   "SimpleSerializeTestCase.expected.xml");
        } finally {
            File logFile = new File(outputDir + "SimpleTest.txt");
            if (logFile.exists()) {
                assertSerialization(logFile,
                                    "SimpleSerializeTestCase.serialized.xml");
                // delete the debugOutput dir
                File debugOutput = new File(outputDir);
                if (debugOutput.exists()) {
                    debugOutput.delete();
                }
            } else {
                fail("The seriaize process did not write to the expected log file");
            }
        }
    }

    /**
     * Checks that the SerializeProcess has generated the expected markup.
     * @param serializedFile the output from the serialize process
     * @param pathToExpected path to the a file that contains the expected
     * output
     * @throws Exception if an error occurs
     */
    private void assertSerialization(File serializedFile,
                                     String pathToExpected) throws Exception {
        try {

            // create a ContentHandler that will write the content into a buffer.
            CharArrayWriter outputWriter = new CharArrayWriter();

            OutputFormat format = new OutputFormat();
            format.setPreserveSpace(true);
            format.setOmitXMLDeclaration(true);
            XMLSerializer serializer = new XMLSerializer(format);
            serializer.setOutputCharStream(outputWriter);
            ContentHandler sHandler = serializer.asContentHandler();

            // The AtributeNoramilizingContentHandler modifies the base attribute
            // so that it is relative to the working dir. If we didn't do this
            // the test case would be dependant on and absolute file path.
            ContentHandler handler =
                        new AttributeNormalizingContentHandler(sHandler);


            // create the actual parser
            XMLReader parser = XMLReaderFactory.createXMLReader(false);
            parser.setContentHandler(handler);

            // Create the required input source
            InputSource inputSource = new InputSource(new FileReader(serializedFile));


            // send the input file down the pipeline
            parser.parse(inputSource);
            outputWriter.flush();

            URL inputURL = getResourceURL(packagePath + '/' + pathToExpected);
            URLConnection inputConnection = inputURL.openConnection();
            InputStream inputStream = inputConnection.getInputStream();
            Reader expected = new InputStreamReader(inputStream);
            Reader actual = new CharArrayReader(outputWriter.toCharArray());

            assertXMLEqual(expected, actual);
        } finally {
            // whatever happens ensure the file is deleted
            serializedFile.delete();
        }
    }
    /**
     * Factory method for creating {@link XMLPipelineFactory} instances
     * @param setDebugOutputFilesPrefix iff true the
     * {@link XMLPipelineContext#setDebugOutputFilesPrefix} property will be
     * set to the tmp dir.
     * @return a <code>XMLPipelineFactory</code> instance
     */
    private XMLPipelineFactory createPipelineFactory(
                final String debugOutputFilesPrefix,
                final boolean setDebugOutputFilesPrefix) {
        return new TestPipelineFactory() {
            // javadoc inherited
            public XMLPipelineContext createPipelineContext(
                        XMLPipelineConfiguration configuration,
                        EnvironmentInteraction environmentInteraction) {
                XMLPipelineContext context =
                            super.createPipelineContext(configuration,
                                                        environmentInteraction);
                if (setDebugOutputFilesPrefix) {
                    context.setDebugOutputFilesPrefix(debugOutputFilesPrefix);
                }
                return context;
            }

            // javadoc inherited
            public XMLPipelineContext createPipelineContext() {
                XMLPipelineContext context = super.createPipelineContext();
                if (setDebugOutputFilesPrefix) {
                    context.setDebugOutputFilesPrefix(debugOutputFilesPrefix);
                }
                return context;
            }
        };
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Apr-05	6798/3	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 01-Apr-05	6798/1	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 ===========================================================================
*/

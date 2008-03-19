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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.migrate.unit.config.lpdm.xsl;

import com.volantis.mcs.migrate.api.config.ConfigFactory;
import com.volantis.mcs.repository.xml.PolicySchemas;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.MCSTransformerMetaFactory;
import com.volantis.shared.content.StringContentInput;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.schema.validator.SchemaValidator;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Provides a means of testing XSL transformations.
 */
public abstract class XSLTestAbstract extends TestCaseAbstract {

    /**
     * The meta factory to use for creating JAXP XSL transformer factories.
     * <p>
     * Since we are a test tool we always use the repackaged MCS version of
     * Xalan which we ship rather than relying on the standard JAXP dynamic
     * lookup process.
     */
    private static MCSTransformerMetaFactory transformerMetaFactory =
            new MCSTransformerMetaFactory();

    /**
     * Tests that the input is transformed into the output using the given
     * stylesheet.
     *
     * @param input         input XML
     * @param expected      expected result of transforming the input XML
     * @param stylesheet    stylesheet to use to transform
     * @throws Exception    if there was a problem running the test
     */
    protected void doTransformation(Source input, InputStream expected,
                                      Source stylesheet)
            throws Exception {

        // Preserve the whitespace ignore setting
        boolean previousWhitespaceSetting = XMLUnit.getIgnoreWhitespace();
        try {
            StreamSource streamSource = (StreamSource) input;
            String inputString =
                    readAsString(streamSource.getInputStream());
            input = new StreamSource(new StringReader(inputString),
                    streamSource.getSystemId());

            EntityResolver resolver = ConfigFactory.getDefaultInstance().
                    createRepositoryEntityResolver();

            SAXTransformerFactory factory = (SAXTransformerFactory)
                    transformerMetaFactory.createTransformerFactory();
            Source xslSource = stylesheet;
            TransformerHandler robotInDisguise =
                    factory.newTransformerHandler(xslSource);

            XMLReader reader = new com.volantis.xml.xerces.parsers.SAXParser();
            reader.setEntityResolver(resolver);
            reader.setContentHandler(robotInDisguise);
            reader.setDTDHandler(robotInDisguise);
            reader.setEntityResolver(resolver);
            reader.setProperty("http://xml.org/sax/properties/lexical-handler",
                    robotInDisguise);

            InputSource inputSource;
            if (input instanceof StreamSource) {
                StreamSource inputStreamSource = (StreamSource) input;
                InputStream inputStream = inputStreamSource.getInputStream();
                Reader inputReader = inputStreamSource.getReader();
                if (inputReader == null && inputStream == null) {
                    throw new IllegalArgumentException(
                            "Input source has neither an " +
                            "InputStream or Reader");
                } else if (inputReader != null && inputStream != null) {
                    throw new IllegalArgumentException(
                            "Input source has both an " +
                            "InputStream or Reader");
                } else if (inputReader != null) {
                    inputSource = new InputSource(inputReader);
                } else {
                    inputSource = new InputSource(inputStream);
                }
                inputSource.setSystemId(inputSource.getSystemId());
            } else {
                throw new IllegalArgumentException(
                        "Unknown input source type: " + input);
            }

            StringWriter output = new StringWriter();
            Result result = new StreamResult(output);
            robotInDisguise.setResult(result);

            reader.parse(inputSource);

            // Make sure that whitespace is not ignored. This is extremely
            // important as there are some elements within the document where
            // whitespace is significant, even if it is the only characters
            // within the document.
            XMLUnit.setIgnoreWhitespace(false);

            String expectedString = readAsString(expected);
            String actualString = output.toString();

            // Create the validator.
            SchemaValidator schemaValidator = new SchemaValidator();
            schemaValidator.addSchema(PolicySchemas.MARLIN_LPDM_V3_0);
            schemaValidator.addSchemata(PolicySchemas.MARLIN_RPDM_DTDS);
            schemaValidator.addSchemata(PolicySchemas.REPOSITORY_2005_09);
            schemaValidator.addSchemata(PolicySchemas.REPOSITORY_2005_12);
            schemaValidator.addSchemata(PolicySchemas.REPOSITORY_2006_02);

            // Make sure that the input result is valid before comparing.
            schemaValidator.validate(new StringContentInput(inputString));

            // Make sure that the expected result is valid before comparing.
            schemaValidator.validate(new StringContentInput(expectedString));

            boolean worked = false;
            try {
                XMLAssert.assertXMLEqual("Result should be as expected",
                        new StringReader(expectedString),
                        new StringReader(actualString));
                worked = true;
            } finally {
                if (!worked) {
                    System.out.println("Expected: " + expectedString);
                    System.out.println("Actual: " + actualString);
                }
            }
        } finally {
            // Reset whitespace ignore setting
            XMLUnit.setIgnoreWhitespace(previousWhitespaceSetting);
        }
    }

    private String readAsString(InputStream stream) throws IOException {
        StringBuffer buffer = new StringBuffer();
        Reader reader = new InputStreamReader(stream);
        int read = 0;
        while ((read = reader.read()) != -1) {
            buffer.append((char) read);
        }
        return buffer.toString();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Dec-05	10845/1	pduffin	VBM:2005121511 Moved architecture files from CVS to MCS Depot

 24-Nov-05	10369/1	emma	VBM:2005111604 Forward port: refactor migration tests

 17-Nov-05	10349/1	emma	VBM:2005111604 Refactor migration tests

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 14-Sep-05	9496/3	pduffin	VBM:2005091211 Addressing review comments

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 08-Jun-05	7997/3	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 06-Jun-05	8613/1	geoff	VBM:2005052404 Holding VBM for XDIME CP prior to 3.3.1 release

 18-May-05	8181/3	adrianj	VBM:2005050505 XDIME/CP Migration CLI

 13-May-05	8181/1	adrianj	VBM:2005050505 XSL for theme migration

 ===========================================================================
*/

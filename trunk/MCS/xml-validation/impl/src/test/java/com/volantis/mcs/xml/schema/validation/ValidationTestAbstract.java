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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xml.schema.validation;

import com.volantis.mcs.xml.schema.compiler.CompiledSchema;
import com.volantis.mcs.xml.schema.impl.validation.InternationalizedValidationException;
import com.volantis.mcs.xml.schema.model.SchemaNamespaces;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.sax.VolantisXMLReaderFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

import java.io.StringReader;
import java.net.URL;
import java.util.Arrays;

/**
 * Base for all test classes that perform validation of real XML.
 */
public abstract class ValidationTestAbstract
        extends TestCaseAbstract {

    protected void checkValidationFromFile(URL url)
            throws Exception {

        InputSource is = new InputSource(url.openStream());
        is.setSystemId(url.toExternalForm());

        checkValidation(is);
    }

    protected void checkValidationFromFile(String relativePath)
            throws Exception {

        URL url = getClass().getResource(relativePath);
        InputSource is = new InputSource(url.openStream());
        is.setSystemId(url.toExternalForm());

        checkValidation(is);
    }

    private void checkValidation(InputSource is)
            throws Exception {

        SchemaValidationFactory factory =
                SchemaValidationFactory.getDefaultInstance();

        final DocumentValidator validator =
                factory.createDocumentValidator(getCompiledSchema());

        VolantisXMLReaderFactory readerFactory = new VolantisXMLReaderFactory();
        readerFactory.disableValidation();
        XMLReader reader = readerFactory.create();

        XMLFilter filter;


        filter = createFilter();
        if (filter != null) {
            filter.setParent(reader);
            reader = filter;

        }

        filter = new XMLFilterImpl() {

            public void startElement(
                    String uri, String localName, String qName, Attributes atts)
            throws SAXException {
                System.out.println("<" + qName + ">");
                super.startElement(uri, localName, qName, atts);
            }


            public void endElement(String uri, String localName, String qName)
            throws SAXException {
                System.out.println("</" + qName + ">");
                super.endElement(uri, localName, qName);
            }
        };
        filter.setParent(reader);
        reader = filter;

        reader.setContentHandler(new ValidatingHandler(validator,
                getSchemaNamespaces()));
        
        reader.parse(is);
    }

    protected XMLFilter createFilter() throws Exception {
        return null;
    }

    protected void checkValidationFromString(String content)
            throws Exception {

        InputSource is = new InputSource(new StringReader(content));

        checkValidation(is);
    }

    protected abstract SchemaNamespaces getSchemaNamespaces();

    protected abstract CompiledSchema getCompiledSchema();

    protected void checkValidationFailsFromFile(
            String relativePath, String expectedMessageKey,
            Object[] expectedMessageArguments) throws Exception {
        try {
            checkValidationFromFile(relativePath);
            fail("Did not detect validation error");
        } catch (InternationalizedValidationException e) {
            assertEquals(expectedMessageKey, e.getMessageKey());
            if (expectedMessageArguments != null) {
                assertEquals(Arrays.asList(expectedMessageArguments),
                        Arrays.asList(e.getArguments()));
            }
        }
    }

    protected void checkValidationFailsFromString(
            String content, String expectedMessageKey,
            Object[] expectedMessageArguments) throws Exception {
        try {
            checkValidationFromString(content);
            fail("Did not detect validation error");
        } catch (InternationalizedValidationException e) {
            assertEquals(expectedMessageKey, e.getMessageKey());
            if (expectedMessageArguments != null) {
                assertEquals(Arrays.asList(expectedMessageArguments),
                        Arrays.asList(e.getArguments()));
            }
        }
    }

}

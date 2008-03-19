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

package com.volantis.mcs.policies.impl.io;

import com.volantis.mcs.accessors.common.ComponentWriter;
import com.volantis.mcs.accessors.xml.jibx.JiBXReader;
import com.volantis.mcs.accessors.xml.jibx.JiBXWriter;
import com.volantis.mcs.model.ModelFactory;
import com.volantis.mcs.model.validation.StrictValidator;
import com.volantis.mcs.model.validation.Validatable;
import com.volantis.mcs.policies.InternalPolicyFactory;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.xml.PolicySchemas;
import com.volantis.shared.content.StringContentInput;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.schema.validator.SchemaValidator;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringWriter;

public abstract class JIBXTestAbstract
        extends TestCaseAbstract {

    protected static final InternalPolicyFactory FACTORY =
            InternalPolicyFactory.getInternalInstance();

    protected final ResourceLoader RESOURCE_LOADER =
            new ResourceLoader(getClass());

    protected boolean validateModel;

    protected void setUp() throws Exception {
        super.setUp();
        
        validateModel = true;
    }

    protected void doRoundTrip(Object sourceObject)
            throws Exception {

        String createdXML = writeStructure(sourceObject);
        Object readObject = unmarshallFromString(createdXML, null);

        assertEquals("Round trip of object failed", sourceObject, readObject);
    }

    /**
     * Write(marshall) a Theme structue to a StringWriter and and return the
     * resulting string.
     *
     * @param object object tree to marshall
     * @return String representation of marshalled object tree.
     * @throws com.volantis.mcs.repository.RepositoryException
     *
     */
    protected String writeStructure(final Object object)
            throws RepositoryException {
        StringWriter outputWriter = new StringWriter();

        ComponentWriter jibxWriter = new JiBXWriter(true);

        jibxWriter.write(outputWriter, object);

        String result = outputWriter.toString();

        return result;
    }

    protected void writeAndCompare(Object object, String resourceName)
            throws Exception {

        String expected = RESOURCE_LOADER.getResourceAsString(resourceName);
        String actual = writeStructure(object);
        assertXMLEquals("", expected, actual);
    }

    public void assertXMLEquals(String s, String expected, String actual)
            throws SAXException, ParserConfigurationException, IOException {


        boolean passed = false;
        try {
            // Validate the expected result.
            SchemaValidator schemaValidator = new SchemaValidator();
            schemaValidator.addSchemata(PolicySchemas.REMOTE_REPOSITORY_CURRENT);

            schemaValidator.validate(new StringContentInput(expected));

            super.assertXMLEquals(s, expected, actual);
            passed = true;
        } finally {
            if (!passed) {
                System.out.println("Expected: " + expected);
                System.out.println("Actual  : " + actual);
            }
        }
    }

    protected Object doRoundTrip(String sourceXML, String name)
            throws Exception {

        Object readObject = unmarshallFromString(sourceXML, name);
        String returnedXML = marshallToString(readObject);

        System.out.println("Expected: " + sourceXML);
        System.out.println("Actual: " + returnedXML);
        assertXMLEquals("Round trip of XML Theme failed", sourceXML,
                        returnedXML);

        return readObject;
    }

    /**
     * Read/unmarshall a Theme structure from a provided string.
     *
     * @param sourceXML
     * @param name
     * @return
     * @throws IOException
     */
    protected Object unmarshallFromString(String sourceXML, String name)
            throws IOException {

        StringContentInput content = new StringContentInput(sourceXML);

        JiBXReader jibxReader = FACTORY.createPolicyReader();

        Object object = jibxReader.read(content, name);

        assertNotNull("Object returned cannot be null", object);

        if (validateModel && object instanceof Validatable) {
            // Do the model validation explicitly here as well since it is no
            // longer done in JiBXReader.
            StrictValidator validator =
                    ModelFactory.getDefaultInstance().createStrictValidator();
            validator.validate((Validatable) object);
        }

        return object;
    }

    /**
     * Write/marshall a Theme structue to a StringWriter and and return the
     * resulting string.
     *
     * @param object object tree to marshall
     * @return String representation of marshalled object tree.
     * @throws com.volantis.mcs.repository.RepositoryException
     */
    private String marshallToString(Object object)
        throws RepositoryException {

        StringWriter outputWriter = new StringWriter();

        ComponentWriter jibxWriter = FACTORY.createPolicyWriter();

        jibxWriter.write(outputWriter, object);

        String result = outputWriter.toString();

        return result;
    }
}

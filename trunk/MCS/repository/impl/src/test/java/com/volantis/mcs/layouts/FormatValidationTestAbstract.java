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
package com.volantis.mcs.layouts;

import com.volantis.mcs.accessors.common.ComponentWriter;
import com.volantis.mcs.accessors.xml.jibx.JiBXReader;
import com.volantis.mcs.model.TestValidator;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.Validatable;
import com.volantis.mcs.policies.InternalPolicyFactory;
import com.volantis.mcs.policies.impl.io.ResourceLoader;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.xml.PolicySchemas;
import com.volantis.shared.content.StringContentInput;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.schema.validator.SchemaValidator;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringWriter;

public abstract class FormatValidationTestAbstract extends TestCaseAbstract {

    private static final ResourceLoader resourceLoader =
            new ResourceLoader(LayoutValidationTestCase.class);

    private static final InternalPolicyFactory policyFactory =
            InternalPolicyFactory.getInternalInstance();

    private Validatable unmarshall(final String testFileName)
            throws IOException {

        String sourceXML = resourceLoader.getResourceAsString(testFileName);
        final StringContentInput content = new StringContentInput(sourceXML);
        JiBXReader jibxReader = policyFactory.createPolicyReader();
        Validatable policy = (Validatable) jibxReader.read(content, null);
        return policy;
    }

    private void marshall(Object object, String resourceName)
            throws Exception {

        String expected = resourceLoader.getResourceAsString(resourceName);
        StringWriter outputWriter = new StringWriter();
        ComponentWriter jibxWriter = policyFactory.createPolicyWriter();
        jibxWriter.write(outputWriter, object);
        String actual = outputWriter.toString();
        assertXMLEquals("", expected, actual);
    }

    /**
     * Unmarshall the contents of the file and validate it. Do not bother to
     * marshall it and check against the input as we know it will be different.
     * <p>
     * This should be used for test cases which check lots of
     * required/unspecified fields which have default values.
     *
     * @param validator
     * @param testFileName
     * @return
     * @throws IOException
     * @throws RepositoryException
     */
    protected Validatable doRead(TestValidator validator,
            final String testFileName) throws IOException, RepositoryException {

        Validatable policy = unmarshall(testFileName);
        validator.validate(policy);
        return policy;
    }

    /**
     * Unmarshall the contents of the file, validate it, marshall it back again
     * and finally ensure that the input file matched the output.
     *
     * @param validator
     * @param testFileName
     * @throws Exception
     */
    protected void doReadAndWrite(TestValidator validator,
            final String testFileName) throws Exception {

        Validatable policy = doRead(validator, testFileName);
        marshall(policy, testFileName);

    }

    public void assertXMLEquals(String s, String expected, String actual)
            throws SAXException, ParserConfigurationException, IOException {

        boolean passed = false;
        try {
            // Validate the expected result.
            SchemaValidator schemaValidator = new SchemaValidator();
            schemaValidator.addSchemata(PolicySchemas.LOCAL_REPOSITORY_CURRENT);
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


    static void expectRootError(TestValidator validator, String pathExtension,
            String messageKey, String param) {

        expectRootError(validator, pathExtension, messageKey, new Object[] {param});
    }

    static void expectRootError(TestValidator validator, String pathExtension,
            String messageKey) {

        expectRootError(validator, pathExtension, messageKey, (Object[]) null);
    }

    static void expectRootError(TestValidator validator, String pathExtension,
            String messageKey, Object[] params) {

        String path = "/variants/0/content/layout/rootFormat";
        if (pathExtension != null) {
            path += "/" + pathExtension;
        }

        validator.expectDiagnostic(DiagnosticLevel.ERROR,
                path, messageKey, params);
    }

    protected void addAllPaneAndGridAndIteratorInvalidExpectations(
            TestValidator validator) {
        expectRootError(validator, "backgroundColor",
                LayoutMessages.BACKGROUND_COLOR_ILLEGAL, "grungy");
        expectRootError(validator, "backgroundComponentType",
                LayoutMessages.BACKGROUND_COMPONENT_TYPE_ILLEGAL, "evil");
        expectRootError(validator, "borderWidth",
                LayoutMessages.BORDER_WIDTH_ILLEGAL, "-1");
        expectRootError(validator, "cellPadding",
                LayoutMessages.CELL_PADDING_ILLEGAL, "-2");
        expectRootError(validator, "cellSpacing",
                LayoutMessages.CELL_SPACING_ILLEGAL, "-3");
        expectRootError(validator, "height",
                LayoutMessages.HEIGHT_ILLEGAL, "-4");
        expectRootError(validator, "horizontalAlignment",
                LayoutMessages.HORIZONTAL_ALIGNMENT_ILLEGAL, "asleep");
        expectRootError(validator, "verticalAlignment",
                LayoutMessages.VERTICAL_ALIGNMENT_ILLEGAL, "awake");
        expectRootError(validator, "width",
                LayoutMessages.WIDTH_ILLEGAL, "minus five");
        expectRootError(validator, "widthUnits",
                LayoutMessages.WIDTH_UNITS_ILLEGAL, "dress-size");
    }

    protected void addAdditionalNonDissectablePaneAndGridInvalidExpectations(
            TestValidator validator) {

        addOptimizationLevelInvalidExpectations(validator);
    }

    protected void addOptimizationLevelInvalidExpectations(
            TestValidator validator) {
        expectRootError(validator, "optimizationLevel",
                LayoutMessages.OPTIMIZATION_LEVEL_ILLEGAL, "insane");
    }

    protected void addAllPaneInvalidExpectations(TestValidator validator) {

        addFormatNameInvalidExpectations(validator);

        addAllPaneAndGridAndIteratorInvalidExpectations(validator);

        expectRootError(validator, "filterOnKeyboardUsability",
                LayoutMessages.FILTER_KEYBOARD_USABILITY_ILLEGAL, "minus 6");
    }

    protected void addFormatNameInvalidExpectations(TestValidator validator) {
        expectRootError(validator, "name",
                LayoutMessages.FORMAT_NAME_ILLEGAL, "1format");
    }

    protected void addFormatNameRequiredExpectations(TestValidator validator) {
        expectRootError(validator, "name",
                LayoutMessages.FORMAT_NAME_UNSPECIFIED, (Object[]) null);
    }

    protected void addFormatIteratorInvalidExpectations(TestValidator validator) {
        addFormatNameInvalidExpectations(validator);
        addAllPaneAndGridAndIteratorInvalidExpectations(validator);
    }

    protected void addStyleClassInvalidExpectations(TestValidator validator) {
        expectRootError(validator, "styleClass",
                LayoutMessages.STYLE_CLASS_ILLEGAL, "2style");
    }

}

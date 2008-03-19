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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.eclipse.ab.editors.dom.validation;

import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.xml.validation.ErrorReporter;
import com.volantis.mcs.xml.validation.ErrorDetails;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.utilities.FaultTypes;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Test the duplicate name validator.
 */
public class DuplicateNameValidatorTestCase
        extends DOMConstraintTestAbstract {

    /**
     * Error reporter that is used to track the errors reported so we
     * can verify that those reported are as expected (content and quantity).
     */
    private static final DuplicateNameErrorReporter ERROR_REPORTER =
            new DuplicateNameErrorReporter();

    /**
     * Store the namespace.
     */
    private static final String NS = MCSNamespace.LPDM.getPrefix() + ":";

    /**
     * Provide an implementation of the ErrorReporter that will track the full
     * xpath for each element that has an error.
     */
    private static class DuplicateNameErrorReporter implements ErrorReporter {
        private boolean started;
        private boolean completed;
        private List errors = new ArrayList();

        // javadoc iherited
        public void reportError(ErrorDetails details) {
            XPath xpath = details.getXPath();
            String key = details.getKey();

            assertEquals("Key should match", FaultTypes.DUPLICATE_NAME, key);
            errors.add(xpath.getExternalForm());
        }

        // javadoc iherited
        public void validationStarted(Element root, XPath xpath) {
            if(started || completed) {
                throw new IllegalArgumentException("Bad start state");
            }
            started = true;
        }

        // javadoc iherited
        public void validationCompleted(XPath xpath) {
            if(!started || completed) {
                throw new IllegalArgumentException("Bad completed state");
            }
            completed = true;
        }

        // javadoc iherited
        public ErrorReporter reset() {
            started = false;
            completed = false;
            errors.clear();
            return this;
        }

        /**
         * Getter for the list of errors.
         * @return the list of errors.
         */
        public List getErrors() {
            return errors;
        }
    }

    // javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();
        ERROR_REPORTER.reset();
    }

    /**
     * Create a DOM with various names.
     * @param names the names to use.
     * @return the root JDOM element
     */
    private Element createDOM(String[] names) throws Exception {
        String xml =
            "<layout>" +
                "<canvasLayout deviceName=\"Master\">" +
                    "<gridFormat rows=\"1\" columns=\"2\">" +
                        "<gridFormatColumns>" +
                            "<gridFormatColumn />" +
                            "<gridFormatColumn />" +
                        "</gridFormatColumns>" +
                        "<gridFormatRow>" +
                            "<gridFormat rows=\"1\" columns=\"2\">" +
                                "<gridFormatColumns>" +
                                    "<gridFormatColumn />" +
                                    "<gridFormatColumn />" +
                                "</gridFormatColumns>" +
                                "<gridFormatRow>" +
                                    "<fragmentFormat name=\"fragmentA\">" +
                                        "<gridFormat rows=\"1\" columns=\"3\">" +
                                            "<gridFormatColumns>" +
                                                "<gridFormatColumn />" +
                                                "<gridFormatColumn />" +
                                                "<gridFormatColumn />" +
                                            "</gridFormatColumns>" +
                                            "<gridFormatRow>" +
                                                "<fragmentFormat name=\"fragmentB\">" +
                                                    "<paneFormat name=\"C\" />" +
                                                "</fragmentFormat>" +
                                                "<paneFormat name=\"" + names[0] + "\" />" +
                                                "<paneFormat name=\"" + names[1] + "\" />" +
                                            "</gridFormatRow>" +
                                        "</gridFormat>" +
                                    "</fragmentFormat>" +
                                    "<paneFormat name=\"" + names[4] + "\" />" +
                                "</gridFormatRow>" +
                            "</gridFormat>" +
                            "<gridFormat rows=\"1\" columns=\"2\">" +
                                "<gridFormatColumns>" +
                                    "<gridFormatColumn />" +
                                    "<gridFormatColumn />" +
                                "</gridFormatColumns>" +
                                "<gridFormatRow>" +
                                    "<paneFormat name=\"" + names[5] + "\" />" +
                                    "<fragmentFormat name=\"fragmentD\">" +
                                        "<gridFormat rows=\"1\" columns=\"2\">" +
                                            "<gridFormatColumns>" +
                                                "<gridFormatColumn />" +
                                                "<gridFormatColumn />" +
                                            "</gridFormatColumns>" +
                                            "<gridFormatRow>" +
                                                "<paneFormat name=\"" + names[2] + "\" />" +
                                                "<paneFormat name=\"" + names[3] + "\" />" +
                                            "</gridFormatRow>" +
                                        "</gridFormat>" +
                                    "</fragmentFormat>" +
                                "</gridFormatRow>" +
                            "</gridFormat>" +
                        "</gridFormatRow>" +
                    "</gridFormat>" +
                "</canvasLayout>" +
                "<canvasLayout deviceName=\"PC\">" +
                    "<gridFormat rows=\"2\" columns=\"1\">" +
                        "<gridFormatColumns>" +
                            "gridFormatColumn />" +
                        "</gridFormatColumns>" +
                        "<gridFormatRow>" +
                            "<paneFormat name=\"" + names[6] + "\" />" +
                        "</gridFormatRow>" +
                        "<gridFormatRow>" +
                            "<paneFormat name=\"" + names[7] + "\" />" +
                        "</gridFormatRow>" +
                    "</gridFormat>" +
                "</canvasLayout>" +
            "</layout>";

        return buildLPDMODOM(xml, "//" + NS + "layout");
    }

    /**
     * Check that validation works with duplicates.
     */
    public void testWithDuplicates() throws Exception {
        DuplicateNameValidator validator = new DuplicateNameValidator();
        String names[] = {"B", "B", "D", "D", "A", "A", "PC1", "PC2"};

        Element root = createDOM(names);
        validator.validate(root, ERROR_REPORTER);

        verifyValidationResult(getExpectedDuplicates());
    }

    /**
     * Test with no duplicates
     */
    public void testWithNoDuplicates() throws Exception {

        DuplicateNameValidator validator = new DuplicateNameValidator();

        String names[] = {"B1", "B2", "D1", "D2", "A1", "A2", "PC1", "PC2"};
        Element root = createDOM(names);
        validator.validate(root, ERROR_REPORTER);

        String expectedDuplicates[] = {};

        verifyValidationResult(expectedDuplicates);
    }

    /**
     * Test with many duplicates
     */
    public void testWithManyDuplicates() throws Exception {

        DuplicateNameValidator validator = new DuplicateNameValidator();

        String names[] = {"M", "M", "M", "M", "M", "M", "M", "M1", "M2"};
        Element root = createDOM(names);
        validator.validate(root, ERROR_REPORTER);

        verifyValidationResult(getExpectedDuplicates());
    }

    /**
     * Test with one set of duplicates.
     */
    public void testWithOneSetOfDuplicates() throws Exception {

        DuplicateNameValidator validator = new DuplicateNameValidator();

        String names[] = {"ABC", "DEF", "GHI", "KLM", "DUPLICATE", "DUPLICATE",
                          "PC1", "PC2"};
        Element root = createDOM(names);
        validator.validate(root, ERROR_REPORTER);

        String base = "/" + NS + "layout/" + NS + "canvasLayout[1]/" +
                NS + "gridFormat/" + NS + "gridFormatRow/" + NS + "gridFormat";

        String expectedDuplicates[] = {
            base + "[1]/" + NS + "gridFormatRow/" + NS + "paneFormat",
            base + "[2]/" + NS + "gridFormatRow/" + NS + "paneFormat"
        };
        verifyValidationResult(expectedDuplicates);
    }

    /**
     * Test with one set of duplicates.
     */
    public void testWithOneSetOfDuplicatesSecondCanvas() throws Exception {

        DuplicateNameValidator validator = new DuplicateNameValidator();

        String names[] = {"ABC", "DEF", "GHI", "KLM", "DUPLICATE", "DUPLICATE",
                          "PC1", "PC1"};
        Element root = createDOM(names);
        validator.validate(root, ERROR_REPORTER);

        String base1 = "/" + NS + "layout/" + NS + "canvasLayout[1]/" +
                NS + "gridFormat/" + NS + "gridFormatRow/" + NS + "gridFormat";

        String base2 = "/" + NS + "layout/" + NS + "canvasLayout[2]/";

        String expectedDuplicates[] = {
            base1 + "[1]/" + NS + "gridFormatRow/" + NS + "paneFormat",
            base1 + "[2]/" + NS + "gridFormatRow/" + NS + "paneFormat",
            base2 + NS + "gridFormat/" + NS + "gridFormatRow[1]/" + NS + "paneFormat",
            base2 + NS + "gridFormat/" + NS + "gridFormatRow[2]/" + NS + "paneFormat"
        };
        verifyValidationResult(expectedDuplicates);
    }


    /**
     * Helper method for returning duplicates for all names.
     */
    private String[] getExpectedDuplicates() {
        String base = "/" + NS + "layout/" + NS + "canvasLayout[1]/" +
                NS + "gridFormat/" + NS + "gridFormatRow/" + NS + "gridFormat";

        String other = "/" + NS + "gridFormatRow/" + NS + "fragmentFormat/" +
                NS + "gridFormat/" + NS + "gridFormatRow/" + NS + "paneFormat";

        String expectedDuplicates[] = {
            base + "[1]" + other + "[1]",
            base + "[1]" + other + "[2]",
            base + "[2]" + other + "[1]",
            base + "[2]" + other + "[2]",
            base + "[1]/" + NS + "gridFormatRow/" + NS + "paneFormat",
            base + "[2]/" + NS + "gridFormatRow/" + NS + "paneFormat"   
        };
        return expectedDuplicates;
    }

    /**
     * Helper method that verifies the errors reported are as expected.
     */
    private void verifyValidationResult(String[] expectedDuplicates) {
        assertNotNull("Errors should not be null", ERROR_REPORTER.getErrors());
        assertEquals("Error count should match", expectedDuplicates.length,
                ERROR_REPORTER.getErrors().size());

        for (int i = 0; i < expectedDuplicates.length; i++) {
            String expectedDuplicate = expectedDuplicates[i];
            assertTrue("Error should contain: " + i + ")" + expectedDuplicate,
                    ERROR_REPORTER.getErrors().contains(expectedDuplicate));
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Dec-04	6524/3	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Dec-04	6354/2	adrianj	VBM:2004112605 Refactor XML validation error reporting

 10-Sep-04	5488/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 19-Aug-04	5264/1	allan	VBM:2004081008 Remove invalid plugin dependencies

 10-Sep-04	5432/4	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 08-Sep-04	5432/2	allan	VBM:2004081803 Validation for range min and max values

 06-May-04	4068/2	allan	VBM:2004032103 Structure page policies section.

 27-Apr-04	4059/1	byron	VBM:2004042205 Format names incorrectly flagged as duplicate across multiple device layouts

 30-Mar-04	3614/1	byron	VBM:2004022404 Layout: Panes are allowed same names

 ===========================================================================
*/

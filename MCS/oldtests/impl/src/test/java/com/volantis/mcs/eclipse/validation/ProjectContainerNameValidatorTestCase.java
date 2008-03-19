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
package com.volantis.mcs.eclipse.validation;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.File;

import org.eclipse.core.runtime.Status;

/**
 * This class is responsible for testing the behaviour of
 * {@link ProjectContainerNameValidator}.
 */
public class ProjectContainerNameValidatorTestCase extends TestCaseAbstract {

    public void testValidDirectoryPath() {

        String directoryPath = "WebContent" + File.separatorChar + "policy";
        testValidityOfPath(directoryPath, "Path should be valid.", Status.OK);
    }

    public void testInvalidDirectoryPath() {

        String invalidPath = "@@ \\\\";
        testValidityOfPath(invalidPath, "Path should be invalid.", Status.ERROR);
    }

    public void testInvalidPolicyPathContainingColon() {
        String invalidPath = "Webcontent" + File.separatorChar + ":";
        testValidityOfPath(invalidPath, "Path should be invalid.", Status.ERROR);
    }

    public void testInvalidPolicyPathContainingAsterix() {
        String invalidPath = "Webcontent" + File.separatorChar + "*";
        testValidityOfPath(invalidPath, "Path should be invalid.", Status.ERROR);
    }

    public void testInvalidPolicyPathContainingQuestionMark() {
        String invalidPath = "Webcontent" + File.separatorChar + "?";
        testValidityOfPath(invalidPath, "Path should be invalid.", Status.ERROR);
    }

    public void testInvalidPolicyPathContainingQuote() {
        String invalidPath = "Webcontent" + File.separatorChar + "\"";
        testValidityOfPath(invalidPath, "Path should be invalid.", Status.ERROR);
    }

    public void testInvalidPolicyPathContainingGreaterThan() {
        String invalidPath = "Webcontent" + File.separatorChar + ">";
        testValidityOfPath(invalidPath, "Path should be invalid.", Status.ERROR);
    }

    public void testInvalidPolicyPathContainingLessThan() {
        String invalidPath = "Webcontent" + File.separatorChar + "<";
        testValidityOfPath(invalidPath, "Path should be invalid.", Status.ERROR);
    }

    public void testInvalidPolicyPathContainingVerticalLine() {
        String invalidPath = "Webcontent" + File.separatorChar + "|";
        testValidityOfPath(invalidPath, "Path should be invalid.", Status.ERROR);
    }

    public void testEmptyPolicyPath() {
        final String invalidPath = "";
        testValidityOfPath(
            invalidPath, "Path should not be empty.", Status.ERROR);
    }

    /**
     * Tests the validity of the supplied path.
     *
     * @param path the path to be tested for validity.
     * @param failureMessage message to be displayed in the case of test failure.
     * @param expectedStatus the expeceted status to be returned afer validation
     * has taken place.
     */
    private void testValidityOfPath(String path, String failureMessage,
                                    int expectedStatus) {

        Validator projectContainerNameValidator =
                new ProjectContainerNameValidator(null);
        ValidationStatus status = projectContainerNameValidator.validate(path,
                new ValidationMessageBuilder(null, null, null));

        assertEquals(failureMessage, expectedStatus, status.getSeverity());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9614/1	rgreenall	VBM:2005092606 Fixed issue where absolute paths were always considered to be invalid.

 ===========================================================================
*/

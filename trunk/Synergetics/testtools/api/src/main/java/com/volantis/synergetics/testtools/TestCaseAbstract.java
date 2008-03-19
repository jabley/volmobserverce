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
 * $Header: /src/voyager/testsuite/unit/com/volantis/TestCaseAbstract.java,v 1.4 2003/04/23 09:44:19 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; super class for all
 *                              our test cases (eventually).
 * 24-Mar-03    Allan           VBM:2003030603 - Added override of assertEquals
 * 22-Apr-03    Phil W-S        VBM:2003041709 - Ensure debug logging is
 *                              disabled by default (unless system property
 *                              defined). Provide programmatic means to
 *                              explicitly enable debug for a test.
 * 22-Apr-03    Geoff           VBM:2003040305 - Add assertNotEquals().
 * 18-May-03    Geoff           VBM:2003042904 - added int[] version of
 *                              assertEquals.
 * 03-Jun-03    Allan           VBM:2003060301 - ObjectHelper moved to 
 *                              Synergetics along with ArrayObject.
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.testtools;

import com.volantis.synergetics.ObjectHelper;
import com.volantis.testtools.mock.test.MockTestCaseAbstract;
import junit.framework.TestCase;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.jdom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * Super class for all our test cases (eventually); provides common services
 * that all testcases need.
 *
 * Subclasses must ensure that if they override {@link #setUp} and/or {@link
 * #tearDown} methods, those methods must call their superclass versions. Pity
 * there is no way to enforce this in Java...
 *
 * This class ensures that Log4j is configured for INFO priority and above
 * (i.e. DEBUG priority is disabled) by default unless the junit.debug.enabled
 * system property is set. Specific tests that require debug output should call
 * the provided helper method {@link #enableLog4jDebug}.
 *
 * @todo add array versions of other types besides int.
 */
public abstract class TestCaseAbstract extends MockTestCaseAbstract {

    /**
     * The set of temporary directories to delete on tear down.
     */
    private Collection temporaryDirectories;

    /**
     * Construct a new TestCaseAbstract with the given name.
     *
     * @param name The name.
     * @deprecated
     */
    public TestCaseAbstract(String name) {
        super(name);
    }

    /**
     * Construct a new TestCaseAbstract with a default name.
     */
    public TestCaseAbstract() {
    }

    protected void setUp() throws Exception {
        super.setUp();
        // Initialise Log4j for the test.
        BasicConfigurator.configure();

        // Suppress debug output by default unless the system property is
        // defined (the value is irrelevant)
        if (System.getProperty("junit.debug.enabled") == null) {
            Category.getDefaultHierarchy().setThreshold(Level.INFO);
        }
    }

    protected void tearDown() throws Exception {
        Category.shutdown();

        if (temporaryDirectories != null) {
            for (Iterator i = temporaryDirectories.iterator(); i.hasNext();) {
                File dir = (File) i.next();
                deleteDirectory(dir);
            }
        }
    }

    /**
     * Helper method that can be called by specializations to enable the output
     * of debug priority messages if needed.
     */
    protected void enableLog4jDebug() {
        Category.getDefaultHierarchy().setThreshold(Level.ALL);
    }

    /**
     * Create a temporary directory for use in test cases.
     *
     * <p>Directories created by this will be automatically deleted along with
     * their contents when the test case is torn down.</p>
     *
     * @param prefix The prefix for the temporary directory.
     * @return The File for the temporary directory.
     * @throws IOException If there was a problem creating the directory.
     */
    protected File createTempDir(String prefix) throws IOException {

        File tempDir;
        while (true) {
            tempDir = File.createTempFile(prefix, "");
            tempDir.delete();
            if (tempDir.mkdirs()) {
                break;
            }
        }

        if (temporaryDirectories == null) {
            temporaryDirectories = new ArrayList();
        }
        temporaryDirectories.add(tempDir);
        return tempDir;
    }

    /**
     * Recursively delete the directory and all its contents.
     *
     * @param dir The directory to delete.
     */
    private void deleteDirectory(File dir) {
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                deleteDirectory(file);
            } else {
                file.delete();
            }
        }

        dir.delete();
    }

    /**
     * Asserts that two int arrays are equal.
     *
     * @param expected the expected value.
     * @param actual   the actual value.
     */
    public static void assertEquals(int[] expected,
                                    int[] actual) {
        assertEquals(new ArrayObject(expected), new ArrayObject(actual));
    }

    /**
     * Asserts that two object arrays are equal.
     *
     * @param expected the expected value.
     * @param actual   the actual value.
     */
    public static void assertEquals(
            Object[] expected, Object[] actual) {
        if (expected == null) {
            assertNull(actual);
        } else {
            assertEquals(Arrays.asList(expected),
                    Arrays.asList(actual));
        }
    }
    /**
     * Asserts that two int arrays are equal. If they are not, an
     * AssertionFailedError is thrown with the given message.
     *
     * @param message  the message to use if the assertion fails.
     * @param expected the expected value.
     * @param actual   the actual value.
     */
    public static void assertEquals(String message, int[] expected,
                                    int[] actual) {
        assertEquals(message, new ArrayObject(expected),
                     new ArrayObject(actual));
    }

    /**
     * Test that two objects that are supposed to be equal adhere to the
     * equals() contract.
     *
     * @param o1
     * @param o2
     */
    public static void assertEquals(Object o1, Object o2) {
        TestCase.assertEquals("o1 does not equal o2",
                              o1, o2);
        TestCase.assertEquals("o2 does not equal o1",
                              o2, o1);
        TestCase.assertEquals("Hashcodes for equal objects o1 and o2" +
                              " are not equal",
                              ObjectHelper.hashCode(o1),
                              ObjectHelper.hashCode(o2));
    }

    /**
     * Test that two objects that are supposed to be not equal adhere to the
     * equals() contract.
     *
     * @param o1
     * @param o2
     */
    public static void assertNotEquals(Object o1, Object o2) {
        assertTrue("o1 equals o2", !o1.equals(o2));
        assertTrue("o2 equals o1", !o2.equals(o1));
    }

    /**
     * Test that two jdom elements are equal (ignores whitespace)
     *
     * @param message  the message to use if the assertion fails.
     * @param expected the expected jdom element
     * @param actual   the actual jdom element
     * @throws SAXException                 if an error occurs
     * @throws ParserConfigurationException if an error occurs
     * @throws IOException                  if an error occurs
     */
    public void assertXMLEquals(String message,
                                Element expected,
                                Element actual)
        throws SAXException, ParserConfigurationException, IOException {
        XMLUnit.setIgnoreWhitespace(true);
        XMLAssert.assertXMLEqual(message,
                                 com.volantis.synergetics.testtools.JDOMUtils.convertToString(
                                     expected),
                                 com.volantis.synergetics.testtools.JDOMUtils.convertToString(
                                     actual));
    }

    /**
     * Test that a jdom elements is equal to some XML text (ignores
     * whitespace)
     *
     * @param message  the message to use if the assertion fails.
     * @param expected the expected XML as a String
     * @param actual   the actual jdom element to compare.
     * @throws SAXException                 if an error occurs
     * @throws ParserConfigurationException if an error occurs
     * @throws IOException                  if an occurs
     */
    public void assertXMLEquals(String message,
                                String expected,
                                Element actual)
        throws SAXException, ParserConfigurationException, IOException {
        XMLUnit.setIgnoreWhitespace(true);
        XMLAssert.assertXMLEqual(message,
                                 expected, JDOMUtils.convertToString(actual));
    }

    /**
     * Test that some XML text is equal to a jdom element (ignores whitespace)
     *
     * @param message  the message to use if the assertion fails.
     * @param expected the expected jdom element
     * @param actual   the actual XML text.
     * @throws SAXException                 if an error occurs
     * @throws ParserConfigurationException if an error occurs
     * @throws IOException                  if an occurs
     */
    public void assertXMLEquals(String message,
                                Element expected,
                                String actual)
        throws SAXException, ParserConfigurationException, IOException {
        XMLUnit.setIgnoreWhitespace(true);
        XMLAssert.assertXMLEqual(message, JDOMUtils.convertToString(expected),
                                 actual);
    }

    /**
     * Test that two XML strings are equal (ignores whitespace)
     *
     * @param message  the message to use if the assertion fails.
     * @param expected the expected XML string
     * @param actual   that actual XML string
     * @throws SAXException                 if an error occurs
     * @throws ParserConfigurationException if an error occurs
     * @throws IOException                  if an error occurs
     */
    public void assertXMLEquals(String message,
                                String expected,
                                String actual)
        throws SAXException, ParserConfigurationException, IOException {
        XMLUnit.setIgnoreWhitespace(true);
        XMLAssert.assertXMLEqual(message, expected, actual);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Jun-05	491/1	allan	VBM:2005062308 Move ArrayUtils into Synergetics and add a new toString

 04-May-04	199/2	doug	VBM:2004042906 Fixed migration problem with the device repository

 16-Oct-03	93/1	steve	VBM:2003100601 Use Category.setThreshold rather than Category.disable

 07-Aug-03	42/1	allan	VBM:2003080502 Add timeToLive, do some refactoring, make Clock more accurate

 ===========================================================================
*/

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
package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.menu.TestMenuModuleCustomisation;
import com.volantis.mcs.protocols.wml.AccesskeyConstants;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * This class tests Tests {@link DefaultNumericShortcutEmulationRenderer}.
 */
public class DefaultNumericEmulationRendererTestCase extends TestCaseAbstract {

    private TestMenuModuleCustomisation customisation;

    /**
     * Initialise a new instance of this test case.
     */
    public DefaultNumericEmulationRendererTestCase() {
    }

    /**
     * Initialise a new named instance of this test case.
     *
     * @param s The name of the test case.
     */
    public DefaultNumericEmulationRendererTestCase(String s) {
        super(s);
    }

    // JavaDoc inherited
    protected void setUp() throws Exception {
        super.setUp();

        customisation = new TestMenuModuleCustomisation();
    }

    // JavaDoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * This tests the start and end methods with no content between them to
     * ensure correct output generation.
     */
    public void testEmptyStartAndEnd() throws Exception {
        // Create the test buffer
        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();

        // Create the renderer
        DefaultNumericShortcutEmulationRenderer renderer =
                new DefaultNumericShortcutEmulationRenderer(customisation);

        // Test the start call
        renderer.start(buffer);

        // Test the end call
        renderer.end(buffer);

        // Create expected
        String required = "<" + AccesskeyConstants.ACCESSKEY_ANNOTATION_ELEMENT + "/>";
        String expected = DOMUtilities.provideDOMNormalizedString(required);

        // Check state of buffer
        assertFalse("Should be contents in the buffer", buffer.isEmpty());

        // Extract the output from the menu rendering as a string.
        String actual = DOMUtilities.toString(buffer.getRoot());
        assertNotNull("The actual string should exist", actual);

        // Compare state and expected
        assertEquals("Strings should match", expected, actual);
    }

    /**
     * This tests the start and end methods with some valud content between
     * them to ensure correct output generation.  It encloses an &lt;a href...
     * link to also test setting the accesskey attribute correctly.
     */
    public void testContentStartAndEnd() throws Exception {
        // Content string
        final String testContent = "click here";

        // Href string
        final String testHref = "/path/to/stuff_here";

        // Create the test buffer
        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();

        // Create the renderer
        DefaultNumericShortcutEmulationRenderer renderer =
                new DefaultNumericShortcutEmulationRenderer(customisation);

        // Start the call
        renderer.start(buffer);

        // Add some content...
        Element element = buffer.openElement("a");
        element.setAttribute("href", testHref);
        element.setAttribute("accesskey", renderer.getShortcut().getText(TextEncoding.PLAIN));
        buffer.appendEncoded(testContent);
        buffer.closeElement(element);

        // End the call
        renderer.end(buffer);

        // Create expected
        String required = "<" + AccesskeyConstants.ACCESSKEY_ANNOTATION_ELEMENT +
                          "><a href=\"" + testHref + "\" accesskey=\"" +
                          renderer.getShortcut().getText(TextEncoding.PLAIN) + "\">" + testContent +
                          "</a></" + AccesskeyConstants.ACCESSKEY_ANNOTATION_ELEMENT + ">";

        String expected = DOMUtilities.provideDOMNormalizedString(required);

        // Check state of buffer
        assertFalse("Should be contents in the buffer", buffer.isEmpty());

        // Extract the output from the menu rendering as a string.
        String actual = DOMUtilities.toString(buffer.getRoot());
        assertNotNull("The actual string should exist", actual);

        // Compare state and expected
        assertEquals("Strings should match", expected, actual);
    }

    /**
     * This tests the output prefix functionality when the device will
     * automatically add an access key.
     */
    public void testOutputPrefixAuto() throws Exception {
        checkAccessKeyPrefix(true);
    }

    /**
     * This tests the output prefix functionality when the device will
     * not automatically add an access key and the object under test has to do
     * it manually.
     */
    public void testOutputPrefixManual() throws Exception {
        checkAccessKeyPrefix(false);
    }

    private void checkAccessKeyPrefix(boolean autoAccessKeyPrefix)
            throws Exception {

        // Create the test buffer
        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();

        customisation.setAutomaticallyDisplaysAccessKey(autoAccessKeyPrefix);

        // Create the renderer
        DefaultNumericShortcutEmulationRenderer renderer =
                new DefaultNumericShortcutEmulationRenderer(customisation);

        // Start the call
        renderer.start(buffer);

        // Add some content...
        renderer.outputPrefix(buffer);

        // End the call
        renderer.end(buffer);

        String prefix;
        if (autoAccessKeyPrefix) {
            prefix = "";
        } else {
            prefix = "x";
        }

        // Create expected (note whitespace after prefix will be eaten)
        String required = "<" + AccesskeyConstants.ACCESSKEY_ANNOTATION_ELEMENT +
                          ">" + prefix +
                          "</" + AccesskeyConstants.ACCESSKEY_ANNOTATION_ELEMENT + ">";

        String expected = DOMUtilities.provideDOMNormalizedString(required);

        // Extract the output from the menu rendering as a string.
        String actual = DOMUtilities.toString(buffer.getRoot());

        assertEquals("Access key prefix mismatch", expected, actual);
    }

    /**
     * This tests retrieving the dummy value.
     */
    public void testAccessKey() {
        // Create the renderer
        DefaultNumericShortcutEmulationRenderer renderer =
                new DefaultNumericShortcutEmulationRenderer(customisation);

        // Get the dummy value
        String dummy = renderer.getShortcut().getText(TextEncoding.PLAIN);

        // Compare retrieved value and constant
        assertEquals("Strings should match",
                     dummy, AccesskeyConstants.DUMMY_ACCESSKEY_VALUE_STRING);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Mar-05	7243/5	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 02-Mar-05	7120/3	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 02-Mar-05	7120/1	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-May-04	4440/2	geoff	VBM:2004051703 Enhanced Menus: WML11 doesn't remove accesskey annotations

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 30-Apr-04	4124/1	claire	VBM:2004042805 Openwave and WML menu renderer selectors

 27-Apr-04	4025/3	claire	VBM:2004042302 Refining numeric emulation interface

 27-Apr-04	4025/1	claire	VBM:2004042302 Enhance Menu Support: Numeric shortcut rendering and and emulation

 ===========================================================================
*/

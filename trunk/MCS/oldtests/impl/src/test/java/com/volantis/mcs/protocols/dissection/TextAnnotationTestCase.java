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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/dissection/TextAnnotationTestCase.java,v 1.2 2002/11/13 15:43:05 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Nov-02    Phil W-S        VBM:2002110507 - Created to test whitespace
 *                              handling.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.dissection;

import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.utilities.WhitespaceUtilities;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * This class unit test the TextAnnotation class.
 *
 * @author <a href="phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public class TextAnnotationTestCase extends TestCase {

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    public TextAnnotationTestCase(String name) {
        super(name);
    }

    /**
     * This method tests the constructors.
     */
    public void notestConstructors() {
        Assert.fail("public TextAnnotation() not tested.");
    }

    /**
     * This method tests the method public void initialise().
     */
    public void notestInitialise()
        throws Exception {
        Assert.fail("public void initialise() not tested.");
    }

    /**
     * This method tests the method public void
     * generateContents(ReusableStringBuffer).
     */
    public void notestGenerateContents()
        throws Exception {
        Assert.fail("public void generateContents(ReusableStringBuffer) not tested.");
    }

    /**
     * This method tests the method public void
     * generateFixedContents(ReusableStringBuffer).
     */
    public void notestGenerateFixedContents()
        throws Exception {
        Assert.fail("public void generateFixedContents(ReusableStringBuffer) not tested.");
    }

    /**
     * This method tests the method public void
     * generateDissectedContents(ReusableStringBuffer).
     */
    public void notestGenerateDissectedContents()
        throws Exception {
        Assert.fail("public void generateDissectedContents(ReusableStringBuffer) not tested.");
    }

    /**
     * This method tests the method public boolean
     * generateShardContentsImpl(ReusableStringBuffer, int, boolean).
     */
    public void notestGenerateShardContentsImpl()
        throws Exception {
        Assert.fail("public boolean generateShardContentsImpl(ReusableStringBuffer, int, boolean) not tested.");
    }

    /**
     * This method tests the method public void setText(Text).
     */
    public void notestSetText()
        throws Exception {
        Assert.fail("public void setText(Text) not tested.");
    }

    /**
     * This method tests the method public Text getText().
     */
    public void notestGetText()
        throws Exception {
        Assert.fail("public Text getText() not tested.");
    }

    /**
     * This method tests the whitespace handling in the method public
     * int calculateContentsSize().
     */
    public void testCalculateContentsSizeWhitespace()
        throws Exception {
        doCalculateContentsSizeTest("no",
                                    null,
                                    "This is the string value",
                                    null);
        doCalculateContentsSizeTest("preamble",
                                    "     \n",
                                    "This is the string value",
                                    null);
        doCalculateContentsSizeTest("trailing",
                                    null,
                                    "This is the string value",
                                    "     \n");
        doCalculateContentsSizeTest("surrounding",
                                    " \n    ",
                                    "This is the string value",
                                    "     \n");
    }

    /**
     * Helper method used to simplify content size testing with or without
     * whitespace.
     *
     * @param preambleWhitespace
     * @param value
     * @param trailingWhitespace
     * @throws Exception
     */
    protected void doCalculateContentsSizeTest(String callDescription,
                                               String preambleWhitespace,
                                               String value,
                                               String trailingWhitespace)
        throws Exception {

        InternalDevice internalDevice = InternalDeviceTestHelper.createTestDevice();

        TextAnnotation annotation = new TextAnnotation();
        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(),
                internalDevice);
        annotation.setProtocol((DOMProtocol)protocol);

        Text text = domFactory.createText();
        int size;
        int expectedSize = value.length();

        if (preambleWhitespace != null) {
            if (!WhitespaceUtilities.isWhitespace(preambleWhitespace,
                                                  0,
                                                  preambleWhitespace.
                                                  length())) {
                fail("Should only be whitespace in the preamble");
            }

            text.append(preambleWhitespace);
            expectedSize++;
        }

        text.append(value);

        if (trailingWhitespace != null) {
            if (!WhitespaceUtilities.isWhitespace(trailingWhitespace,
                                                  0,
                                                  trailingWhitespace.
                                                  length())) {
                fail("Should only be whitespace in the trailer");
            }

            text.append(trailingWhitespace);
            expectedSize++;
        }

        annotation.setText(text);
        annotation.initialise();
        size = annotation.calculateContentsSize();

        assertEquals("calcuateContentsSize " + callDescription +
                     " whitespace text length not as",
                     expectedSize,
                     size);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05      8005/1  pduffin VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04      6416/3  ianw    VBM:2004120703 New Build

 08-Dec-04      6416/1  ianw    VBM:2004120703 New Build

 20-Aug-03      1207/1  adrian  VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/

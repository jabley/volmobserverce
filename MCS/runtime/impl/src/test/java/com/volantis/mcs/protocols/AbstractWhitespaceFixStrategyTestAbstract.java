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
package com.volantis.mcs.protocols;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Base class for testing the various {@link WhiteSpaceFixStrategy}
 * implementations.
 */
public abstract class AbstractWhitespaceFixStrategyTestAbstract
        extends TestCaseAbstract {

    /**
     * Element that will be used for testing
     */
    private Element strongElement;

    /**
     * Root element of the document that will be used for testing
     */
    private Element rootElement;

    // javadoc inherted
    protected void tearDown() throws Exception {
        strongElement =null;
    }

    /**
     * Factorys an instance of the strategy that is to be tested
     * @return a {@link WhiteSpaceFixStrategy} instance.
     */
    protected abstract WhiteSpaceFixStrategy createStrategy();

    /**
     * Test the {@link WhiteSpaceFixStrategy#fixUpSpace} method on
     * open elements with a single space.
     *
     * @throws Exception if an error occurs
     */
    public void testOpenElementSingleSpace() throws Exception {
        WhiteSpaceFixStrategy strategy = createStrategy();
        createTestDocument("This is ",
                           "some strong text",
                           " with text after");

        strategy.fixUpSpace(strongElement, true);

        String testDocAfterWhiteSpaceFix = DOMUtilities.toString(rootElement);

        assertEquals(getExpectedOpenElementSingleSpaceResult(),
                     testDocAfterWhiteSpaceFix);
    }

    /**
     * Allow subclasses to provide the expected result for the
     * {@link #testOpenElementSingleSpace()}  method.
     *
     * @return the expected result
     */
    protected abstract String getExpectedOpenElementSingleSpaceResult();

    /**
     * Test the {@link WhiteSpaceFixStrategy#fixUpSpace} method on
     * close elements with a single space.
     *
     * @throws Exception if an error occurs
     */
    public void testCloseElementSingleSpace() throws Exception {
        WhiteSpaceFixStrategy strategy = createStrategy();
        createTestDocument("This is ",
                           "some strong text",
                           " with text after");

        strategy.fixUpSpace(strongElement, false);

        String testDocAfterWhiteSpaceFix = DOMUtilities.toString(rootElement);

        assertEquals(getExpectedCloseElementSingleSpaceResult(),
                     testDocAfterWhiteSpaceFix);
    }

    /**
     * Allow subclasses to provide the expected result for the
     * {@link #testCloseElementSingleSpace()}  method.
     *
     * @return the expected result
     */
    protected abstract String getExpectedCloseElementSingleSpaceResult();

    /**
     * Creates a testable Document.
     * 
     * @param textBeforeStrongElement the text to use before the strong element
     * @param textWithinStrongElement the text to use within the strong element
     * @param textAfterStrongElement the text to use after the strong element
     */
    private void createTestDocument(String textBeforeStrongElement,
                                    String textWithinStrongElement,
                                    String textAfterStrongElement) {
        DOMFactory factory = DOMFactory.getDefaultInstance();

        rootElement = factory.createElement("body");

        Text text = factory.createText();
        text.append(textBeforeStrongElement);

        rootElement.addTail(text);

        strongElement = factory.createElement("strong");

        Text strongText = factory.createText();
        strongText.append(textWithinStrongElement);
        strongElement.addTail(strongText);

        rootElement.addTail(strongElement);

        Text textNodeAfterStrongElement = factory.createText();
        textNodeAfterStrongElement.append(textAfterStrongElement);

        rootElement.addTail(textNodeAfterStrongElement);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10803/2	pabbott	VBM:2005120901 Fix white space problem on LG-C1100

 08-Dec-05	10675/7	pduffin	VBM:2005110905 Ported forward some white space fixes from 3.2.3

 02-Aug-05	9139/1	doug	VBM:2005071403 Finished off whitespace fixes

 ===========================================================================
*/

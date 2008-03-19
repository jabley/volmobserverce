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

package com.volantis.styling;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test the {@link StatefulPseudoClass} pseudo class works properly.
 */
public class StatefulPseudoClassSetTestCase
        extends TestCaseAbstract {

    /**
     * Test that combining does not return null.
     */
    public void testCombineDoesNotReturnNull() {
        assertNotNull("Combining two pseudo classes should not return null",
                      StatefulPseudoClasses.LINK.getSet().add(StatefulPseudoClasses.HOVER));
    }

    /**
     * Test that :link cannot be combined with :visited.
     */
    public void testLinkCannotBeCombinedWithVisited() {
        try {
            StatefulPseudoClasses.LINK.getSet().add(StatefulPseudoClasses.VISITED);
            fail("Combine of :link and :visited should fail");
        } catch (IllegalArgumentException expected) {
            assertEquals("Exception message incorrect",
                         "Cannot combine :link and :visited due to conflict" +
                         " between :visited and :link",
                         expected.getMessage());
        }
    }

    /**
     * Test that :visited cannot be combined with :link.
     */
    public void testVisitedCannotBeCombinedWithLink() {
        try {
            StatefulPseudoClasses.VISITED.getSet().add(StatefulPseudoClasses.LINK);
            fail("Combine of :visited and :link should fail");
        } catch (IllegalArgumentException expected) {
            assertEquals("Exception message incorrect",
                         "Cannot combine :visited and :link due to conflict" +
                         " between :link and :visited",
                         expected.getMessage());
        }
    }

    /**
     * Test that :visited:hover cannot be combined with :link.
     */
    public void testVisitedHoverCannotBeCombinedWithLink() {

        // Do this outside the try so that if it fails it does not cause this
        // test to accidentally complete.
        StatefulPseudoClassSet visitedHover =
                StatefulPseudoClasses.VISITED.getSet().add(
                        StatefulPseudoClasses.HOVER);
        try {
            visitedHover.add(StatefulPseudoClasses.LINK);
            fail("Combine of :visited:hover and :link should fail");
        } catch (IllegalArgumentException expected) {

            // Split the message to make sure that the test is not dependent
            // on ordering of CSS representation for sets containing more than
            // one pseudo class.
            String message = expected.getMessage();
            final String START = "Cannot combine ";
            final String END = " and :link due to conflict between :link and :visited";
            if (message.startsWith(START) && message.endsWith(END)) {

                String css = message.substring(
                        START.length(), message.length() - END.length());
                checkCSSRepresentationMatches(css, new String[]{
                    ":visited",
                    ":hover"
                });
            }
            assertEquals("Exception message incorrect",
                         "Cannot combine :hover:visited and :link due to " +
                         "conflict between :link and :visited",
                         expected.getMessage());
        }
    }

    /**
     * Test that combining an object with itself returns the object.
     */
    public void testCombineWithSelfReturnsSelf() {
        StatefulPseudoClassSet focusSet = StatefulPseudoClasses.FOCUS.getSet();
        assertSame("Combining with self should return self",
                focusSet, focusSet.add(StatefulPseudoClasses.FOCUS));
    }

    /**
     * Test that combining the same classes always results in the same objects.
     */
    public void testCombineResultIsSame() {
        StatefulPseudoClassSet c1 = StatefulPseudoClasses.HOVER.getSet().add(
                StatefulPseudoClasses.FOCUS);
        StatefulPseudoClassSet c2 = StatefulPseudoClasses.HOVER.getSet().add(
                StatefulPseudoClasses.FOCUS);

        assertEquals("Combining the same classes must result in equivalent objects",
                   c1, c2);
    }

    /**
     * Test that combine order is irrelevant.
     */
    public void testCombineOrderIrrelevant() {
        StatefulPseudoClassSet c1 = StatefulPseudoClasses.HOVER.getSet().add(
                StatefulPseudoClasses.FOCUS);
        StatefulPseudoClassSet c2 = StatefulPseudoClasses.FOCUS.getSet().add(
                StatefulPseudoClasses.HOVER);

        assertEquals("Combine order must be irrelevant", c1, c2);
    }

    /**
     * Test the CSS representation of a single pseudo class.
     */
    public void testCSSRepresentationSinglePseudoClass() {
        assertEquals("CSS representation incorrect",
                     ":focus", StatefulPseudoClasses.FOCUS.getSet()
                .getCSSRepresentation());
    }


    /**
     * Test the CSS representation of a combination pseudo class.
     */
    public void testCSSRepresentationOfCombinationPseudoClass() {
        StatefulPseudoClassSet combination =
                StatefulPseudoClasses.FOCUS.getSet().add(StatefulPseudoClasses.LINK);
        String css = combination.getCSSRepresentation();

        checkCSSRepresentationMatches(css, new String[]{
            ":focus", ":link"
        });
    }

    /**
     * Check that the CSS representation contains all the specified pseudo
     * classes.
     *
     * @param css The CSS representation to check.
     * @param pseudoClasses The pseudo classes that it should contain.
     */
    private void checkCSSRepresentationMatches(String css, String[] pseudoClasses) {
        int totalLength = 0;
        for (int i = 0; i < pseudoClasses.length; i++) {
            String pseudoClass = pseudoClasses[i];
            totalLength += pseudoClass.length();

            assertTrue("CSS representation '" + css + "' does not contain " +
                       pseudoClass, css.indexOf(pseudoClass) != -1);
        }
        assertEquals("CSS representation '" + css + "' contains too much",
                     css.length(), totalLength);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 18-Jul-05	9029/2	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/

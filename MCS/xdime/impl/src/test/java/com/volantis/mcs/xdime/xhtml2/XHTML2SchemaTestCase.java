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

package com.volantis.mcs.xdime.xhtml2;

import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.mcs.xdime.XDIMEValidationTestAbstract;
import junit.framework.Test;
import junit.framework.TestSuite;

public class XHTML2SchemaTestCase
        extends XDIMEValidationTestAbstract {

    public static Test suite() {
        TestSuite suite = new TestSuite();

        String[] mixedElements = new String[]{
                "a",
                "abbr",
                "address",
                "blockquote",
                "caption",
                "cite",
                "code",
                "dd",
                "dfn",
                "div",
                "dt",
                "em",
                "h1",
                "h2",
                "h3",
                "h4",
                "h5",
                "h6",
                "kbd",
                "label",
                "li",
                "meta",
                "object",
                "p",
                "pre",
                "quote",
                "samp",
                "span",
                "strong",
                "style",
                "sub",
                "sup",
                "td",
                "th",
                "title",
                "var",
        };

        for (int i = 0; i < mixedElements.length; i++) {
            String element = mixedElements[i];
            String content = "<" + element + " xmlns=\"" +
                    XDIMESchemata.XHTML2_NAMESPACE + "\">PCDATA</" + element + ">";
            Test test = new XDIMESuccessfulTest(
                    "mixed with character data - " + element, content);
            suite.addTest(test);
        };

        for (int i = 0; i < mixedElements.length; i++) {
            String element = mixedElements[i];
            String content = "<" + element + " xmlns=\"" +
                    XDIMESchemata.XHTML2_NAMESPACE + "\">    </" + element + ">";
            Test test = new XDIMESuccessfulTest(
                    "mixed with whitespace - " + element, content);
            suite.addTest(test);
        };

        String[] notMixedElements = new String[]{
                "access",
                "body",
                "dl",
                "head",
                "html",
                "link",
                "nl",
                "ol",
                "param",
                "table",
                "tbody",
                "tfoot",
                "thead",
                "tr",
                "ul",
        };

        for (int i = 0; i < notMixedElements.length; i++) {
            String element = notMixedElements[i];
            String content = "<" + element + " xmlns=\"" +
                    XDIMESchemata.XHTML2_NAMESPACE + "\">PCDATA</" + element + ">";
            Test test = new XDIMEFailedTest(
                    "not mixed with character data - " + element, content,
                    "validation-error-invalid-content", null);
            suite.addTest(test);
        };

        String[] notMixedUnstructuredElements = new String[]{
                "access",
                "body",
                "link",
                "param",
        };

        for (int i = 0; i < notMixedUnstructuredElements.length; i++) {
            String element = notMixedUnstructuredElements[i];
            String content = "<" + element + " xmlns=\"" +
                    XDIMESchemata.XHTML2_NAMESPACE + "\">    </" + element + ">";
            Test test = new XDIMESuccessfulTest(
                    "not mixed with whitespace - " + element, content);
            suite.addTest(test);
        };

        suite.addTestSuite(XHTML2SchemaTestCase.class);

        return suite;
    }

    public void testOLWithMultipleLabelsFails() throws Exception {
        checkValidationFailsFromFile(
                "validation-xml/ol-with-multiple-labels-fails.xml",
                "validation-error-invalid-content", new Object[] {
                XHTML2Elements.OL, XHTML2Elements.LI.toString(),
                XHTML2Elements.LABEL
        });
    }

    public void testOLWithNoItemsFails() throws Exception {
        
        // todo this should actually indicate that either label, or li is
        // todo allowed as label is optional but there is a bug in the
        // todo BoundedContentValidator.
        checkValidationFailsFromFile(
                "validation-xml/ol-with-no-items-fails.xml",
                "validation-error-missing-content", new Object[] {
                XHTML2Elements.OL, XHTML2Elements.LABEL.toString()
        });
    }

    public void testBodyWithTextElementFails() throws Exception {
        checkValidationFailsFromFile(
                "validation-xml/body-with-text-element-fails.xml",
                "validation-error-invalid-content", null);
    }

    public void testPInsidePFails() throws Exception {
        checkValidationFailsFromFile(
                "validation-xml/p-inside-p-fails.xml",
                "validation-error-invalid-content", null);
    }
    private static class XDIMESuccessfulTest
            extends XDIMEValidationTestAbstract {

        private final String content;

        public XDIMESuccessfulTest(String name, String content) {
            this.content = content;
            setName(name);
        }

        public void runBare()
                throws Exception {

            checkValidationFromString(content);
        }
    }

    private static class XDIMEFailedTest
            extends XDIMEValidationTestAbstract {

        private final String content;
        private final String expectedMessageKey;
        private final Object[] expectedMessageArguments;

        public XDIMEFailedTest(
                String name, String content,
                String expectedMessageKey,
                Object[] expectedMessageArguments) {
            this.content = content;
            this.expectedMessageKey = expectedMessageKey;
            this.expectedMessageArguments = expectedMessageArguments;
            setName(name);
        }

        public void runBare()
                throws Exception {

            checkValidationFailsFromString(
                    content, expectedMessageKey, expectedMessageArguments);
        }
    }

}

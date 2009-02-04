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

import com.volantis.mcs.eclipse.ab.editors.dom.LPDMODOMFactory;
import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import java.io.StringReader;

/**
 * Base for Constraint test cases.
 */
public abstract class DOMConstraintTestAbstract extends TestCaseAbstract {

    /**
     * Performs testing using the given constraint against the given element.
     * Null should be given as the error key if no violation is expected.
     *
     * @param constraint       the constraint to be tested
     * @param element          the element against which the constraint should
     *                         be actioned
     * @param expectedErrorKey the error key for which a violation should be
     *                         reported, or null if no error is expected
     */
    protected void doTest(DOMConstraint constraint,
                          ODOMElement element,
                          String expectedErrorKey) {
        CheckingErrorReporter reporter = null;
        boolean result;

        if (expectedErrorKey != null) {
            reporter = new CheckingErrorReporter(element, expectedErrorKey);
        }

        result = constraint.violated(element, reporter);

        if (expectedErrorKey != null) {
            assertTrue("The constraint should have reported a violation",
                       result);

            assertTrue("The error reporter should have been invoked",
                       reporter.isReported());
        }
    }

    /**
     * Supporting method that can construct an LPDM ODOM from the given XML
     * string and return an element identified by a given XPath. Throws an
     * exception of the XPath isn't appropriate or the XML string is not well
     * formed.
     *
     * @param xml                  the XML document
     * @param requiredElementXPath the path to the element to be returned. This
     *                             must use the 'lpdm' namespace prefix
     * @return the required element
     */
    protected ODOMElement buildLPDMODOM(String xml,
                                        String requiredElementXPath)
        throws Exception {
        XPath xpath = new XPath(requiredElementXPath,
                                new Namespace[] {MCSNamespace.LPDM});
        SAXBuilder builder = new SAXBuilder();

        // Make sure we create the DOM with default LPDM namespace
        builder.setFactory(new LPDMODOMFactory());

        return (ODOMElement)xpath.selectSingleNode(
            builder.build(new StringReader(xml)).getRootElement());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Sep-04	5488/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 19-Aug-04	5264/1	allan	VBM:2004081008 Remove invalid plugin dependencies

 09-Aug-04	5130/1	doug	VBM:2004080310 MCS

 10-Sep-04	5432/4	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 08-Sep-04	5432/2	allan	VBM:2004081803 Validation for range min and max values

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 06-May-04	4068/1	allan	VBM:2004032103 Structure page policies section.

 30-Mar-04	3614/3	byron	VBM:2004022404 Layout: Panes are allowed same names - remove erroneously added method

 30-Mar-04	3614/1	byron	VBM:2004022404 Layout: Panes are allowed same names

 15-Jan-04	2583/1	philws	VBM:2003121512 Add layout constraints

 ===========================================================================
*/

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

import org.jdom.Element;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.AncestorContainment;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.DOMConstraint;
import com.volantis.mcs.xml.xpath.XPath;

/**
 * Tests {@link AncestorContainment}.
 */
public class AncestorContainmentTestCase
    extends DOMConstraintTestAbstract {
    /**
     * Tests that {@link AncestorContainment#getAncestor} operates as expected.
     *
     * @param constraint the constraint to be tested
     * @param element the start-point element
     * @param expectedAncestor the expected ancestor element
     */
    protected void doGetAncestorTest(AncestorContainment constraint,
                                     Element element,
                                     Element expectedAncestor) {
        Element actualAncestor = constraint.getAncestor(element);

        if (expectedAncestor == null) {
            assertNull("Ancestor should be null",
                       actualAncestor);
        } else if (actualAncestor == null) {
            fail("Expected ancestor (" +
                 new XPath(expectedAncestor).getExternalForm() +
                 ") not found");
        } else {
            assertSame("Expected ancestor (" +
                       new XPath(expectedAncestor).getExternalForm() +
                       ") not the same as the actual (" +
                       new XPath(actualAncestor).getExternalForm() + ")",
                       expectedAncestor,
                       actualAncestor);
        }
    }

    /**
     * Check that getAncestor finds the existing ancestor.
     */
    public void testGetAncestorExists() throws Exception {
        AncestorContainment constraint =
            createConstraint("ancestor",
                             "unused",
                             true);

        Element element = buildLPDMODOM(
            "<root>" +
            "    <ancestor>" +
            "        <sub>" +
            "            <target/>" +
            "            <alternative/>" +
            "        </sub>" +
            "        <other/>" +
            "    </ancestor>" +
            "</root>",
            "lpdm:ancestor/lpdm:sub/lpdm:target");

        doGetAncestorTest(constraint,
                          element,
                          element.getParent().getParent());
    }

    /**
     * Check that getAncestor fails to find the non-existent ancestor.
     */
    public void testGetAncestorDoesNotExist() throws Exception {
        AncestorContainment constraint =
            createConstraint("ancestor",
                             "unused",
                             true);

        Element element = buildLPDMODOM(
            "<root>" +
            "    <something>" +
            "        <sub>" +
            "            <target/>" +
            "            <alternative/>" +
            "        </sub>" +
            "        <other/>" +
            "    </something>" +
            "</root>",
            "lpdm:something/lpdm:sub/lpdm:target");

        doGetAncestorTest(constraint,
                          element,
                          null);
    }

    /**
     * Check that the constraint is not violated when the not required ancestor
     * doesn't exist.
     */
    public void testViolatedNotRequiredNotExists() throws Exception {
        doViolatedNotExistsTest(false, null);
    }

    /**
     * Check that the constraint is violated when the not required ancestor
     * does exist.
     */
    public void testViolatedNotRequiredExists() throws Exception {
        doViolatedExistsTest(false, "key");
    }

    /**
     * Check that the constraint is violated when the required ancestor doesn't
     * exist.
     */
    public void testViolatedRequiredNotExists() throws Exception {
        doViolatedNotExistsTest(true, "key");
    }

    /**
     * Check that the constraint is not violated when the required ancestor
     * does exist.
     */
    public void testViolatedRequiredExists() throws Exception {
        doViolatedExistsTest(true, null);
    }

    /**
     * Supporting method.
     *
     * @param required whether the ancestor must exist or not
     * @param key      the error key expected, or null if no error expected
     */
    protected void doViolatedNotExistsTest(boolean required,
                                           String key) throws Exception {
        DOMConstraint constraint = createConstraint("ancestor",
                                                    key,
                                                    required);

        ODOMElement element = buildLPDMODOM(
            "<root>" +
            "    <something>" +
            "        <sub>" +
            "            <target/>" +
            "            <target/>" +
            "        </sub>" +
            "    </something>" +
            "</root>",
            "lpdm:something/lpdm:sub/lpdm:target[1]");

        doTest(constraint,
               element,
               key);
    }

    /**
     * Supporting method.
     *
     * @param required whether the ancestor must exist or not
     * @param key      the error key expected, or null if no error expected
     */
    protected void doViolatedExistsTest(boolean required,
                                        String key) throws Exception {
        DOMConstraint constraint = createConstraint("ancestor",
                                                 key,
                                                 required);

        ODOMElement element = buildLPDMODOM(
            "<root>" +
            "    <ancestor>" +
            "        <sub>" +
            "            <target/>" +
            "            <target/>" +
            "        </sub>" +
            "    </ancestor>" +
            "</root>",
            "lpdm:ancestor/lpdm:sub/lpdm:target[2]");

        doTest(constraint,
               element,
               key);
    }

    /**
     * Supporting method that can be overridden in specialization test cases.
     *
     * @param ancestor the identified container
     * @param key the error key to be reported
     * @param required whether the ancestor must or must not exist
     * @return the constraint
     */
    protected AncestorContainment createConstraint(String ancestor,
                                                   String key,
                                                   boolean required) {
        return new AncestorContainment(ancestor,
                                       MCSNamespace.LPDM.getURI(),
                                       (key == null ? "dummy" : key),
                                       required);
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

 19-Aug-04	5264/1	allan	VBM:2004081008 Remove invalid plugin dependencies

 06-May-04	4068/1	allan	VBM:2004032103 Structure page policies section.

 15-Jan-04	2583/1	philws	VBM:2003121512 Add layout constraints

 ===========================================================================
*/

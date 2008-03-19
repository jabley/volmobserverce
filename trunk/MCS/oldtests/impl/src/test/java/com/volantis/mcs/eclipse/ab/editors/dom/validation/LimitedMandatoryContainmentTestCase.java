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
import com.volantis.mcs.eclipse.ab.editors.dom.validation.DOMConstraint;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.LimitedMandatoryContainment;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.DOMConstraintTestAbstract;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import org.jdom.filter.ElementFilter;

/**
 * Test for {@link LimitedMandatoryContainment} constraint.
 */
public class LimitedMandatoryContainmentTestCase
    extends DOMConstraintTestAbstract {
    /**
     * Check that the constraint is violated when the ancestor does not exist.
     */
    public void testViolatedByMissingContainer() throws Exception {
        DOMConstraint constraint = createConstraint("ancestor",
                                                    "containerKey",
                                                    1,
                                                    "countKey");

        ODOMElement element = buildLPDMODOM(
            "<root>" +
            "    <something>" +
            "        <sub>" +
            "            <target/>" +
            "            <target/>" +
            "        </sub>" +
            "    </something>" +
            "</root>",
            "lpdm:something/lpdm:sub/lpdm:target[2]");

        doTest(constraint,
               element,
               "containerKey");
    }

    /**
     * Check that the constraint is violated when the ancestor does not exist.
     */
    public void testViolatedByCount() throws Exception {
        DOMConstraint constraint = createConstraint("ancestor",
                                                 "containerKey",
                                                 1,
                                                 "countKey");

        ODOMElement element = buildLPDMODOM(
            "<root>" +
            "    <ancestor>" +
            "        <sub>" +
            "            <target/>" +
            "        </sub>" +
            "        <other>" +
            "            <nested>" +
            "               <target/>" +
            "            </nested>" +
            "        </other>" +
            "        <sub/>" +
            "    </ancestor>" +
            "</root>",
            "lpdm:ancestor/lpdm:sub/lpdm:target");

        doTest(constraint,
               element,
               "countKey");

        // Check the XPath cache
        doTest(constraint,
               element,
               "countKey");

        // Check against "sub"
        element = (ODOMElement)element.getParent();

        // Check the XPath cache
        doTest(constraint,
               element,
               "countKey");

        // Check against "other"
        element = (ODOMElement)element.getParent().getContent(
            new ElementFilter()).get(1);

        // Check the XPath cache
        doTest(constraint,
               element,
               null);
    }

    /**
     * Supporting method that can be overridden in specialization test cases.
     *
     * @param container    the mandatory container
     * @param containerKey the error key to be reported if container constraint
     *                     is violated
     * @param maxCount     the max number of the given element that can exist
     *                     in the container
     * @param countKey     the error key to be reported if count constraint is
     *                     violated
     * @return the constraint
     */
    protected LimitedMandatoryContainment createConstraint(String container,
                                                           String containerKey,
                                                           int maxCount,
                                                           String countKey) {
        return new LimitedMandatoryContainment(
            container,
            MCSNamespace.LPDM.getURI(),
            containerKey,
            maxCount,
            countKey);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Aug-04	5264/1	allan	VBM:2004081008 Remove invalid plugin dependencies

 06-May-04	4068/1	allan	VBM:2004032103 Structure page policies section.

 15-Jan-04	2583/1	philws	VBM:2003121512 Add layout constraints

 ===========================================================================
*/

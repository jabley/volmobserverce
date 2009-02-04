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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/trans/GenericContainerValidatorTestCase.java,v 1.3 2003/01/17 12:03:40 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 07-Jan-03    Phil W-S        VBM:2003010709 - Created to test the new class.
 * 17-Jan-03    Phil W-S        VBM:2003010606 - Rework: Refactored to test the
 *                              refactored class.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.dissection.DissectionConstants;
import junit.framework.TestCase;

/**
 * Tests the GenericContainerValidator class.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public class GenericContainerValidatorTestCase extends TestCase
    implements ContainerActions {

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    public GenericContainerValidatorTestCase(String name) {
        super(name);
    }

    /**
     * This method tests the constructors for
     * com.volantis.mcs.protocols.trans.GenericContainerValidator.
     */
    public void testConstructors() {
        Element form = domFactory.createElement();
        Element div = domFactory.createElement();
        Element dissectableContents = domFactory.createElement();
        Element blockquote = domFactory.createElement();
        Element p = domFactory.createElement();
        Element table = domFactory.createElement();

        form.setName("form");
        div.setName("div");
        dissectableContents.setName(DissectionConstants.
                                    DISSECTABLE_CONTENTS_ELEMENT);
        blockquote.setName("blockquote");
        p.setName("p");
        table.setName("table");

        //
        // Test public GenericContainerValidator(int) constructor
        //
        ContainerValidator cv =
            new GenericContainerValidator(RETAIN) {
                protected void initialize() {
                    containerActionMap.put(
                        "form", new Integer(REMAP));
                    containerActionMap.put(
                        "div", new Integer(PROMOTE));
                    containerActionMap.put(
                        DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT,
                        new Integer(INVERSE_REMAP));
                    containerActionMap.put(
                        "blockquote", new Integer(RETAIN));
                }
            };

        assertEquals("defaultAction variant did not return as",
                     RETAIN,
                     cv.getAction(p, table));

        assertEquals("defaultAction variant did not return as",
                     REMAP,
                     cv.getAction(form, table));

        assertEquals("defaultAction variant did not return as",
                     PROMOTE,
                     cv.getAction(div, table));

        assertEquals("defaultAction variant did not return as",
                     INVERSE_REMAP,
                     cv.getAction(dissectableContents, table));

        assertEquals("defaultAction variant did not return as",
                     RETAIN,
                     cv.getAction(blockquote, table));

        //
        // Test public GenericContainerValidator() constructor
        //
        cv = new GenericContainerValidator() {
                protected void initialize() {
                    containerActionMap.put(
                        "form", new Integer(REMAP));
                    containerActionMap.put(
                        "div", new Integer(PROMOTE));
                    containerActionMap.put(
                        DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT,
                        new Integer(INVERSE_REMAP));
                    containerActionMap.put(
                        "blockquote", new Integer(RETAIN));
                }
            };

        assertEquals("Simple variant did not return as",
                     PROMOTE,
                     cv.getAction(p, table));

        assertEquals("Simple variant did not return as",
                     REMAP,
                     cv.getAction(form, table));

        assertEquals("Simple variant did not return as",
                     PROMOTE,
                     cv.getAction(div, table));

        assertEquals("Simple variant did not return as",
                     INVERSE_REMAP,
                     cv.getAction(dissectableContents, table));

        assertEquals("Simple variant did not return as",
                     RETAIN,
                     cv.getAction(blockquote, table));

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/

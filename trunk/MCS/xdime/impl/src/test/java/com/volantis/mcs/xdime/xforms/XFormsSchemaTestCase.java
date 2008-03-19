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

package com.volantis.mcs.xdime.xforms;

import com.volantis.mcs.xdime.XDIMEValidationTestAbstract;
import com.volantis.mcs.xdime.xhtml2.XHTML2Elements;

public class XFormsSchemaTestCase
        extends XDIMEValidationTestAbstract {

    /**
     * Ensure that model is integrated with XHTML 2 markup and has the correct
     * content model.
     */
    public void testModelInHeadOk() throws Exception {
        checkValidationFromFile("validation-xml/model-in-head-ok.xml");
    }

    /**
     * Ensure that model cannot be used within the body of an XHTML 2 document.
     */
    public void testModelInBodyFails() throws Exception {
        checkValidationFailsFromFile("validation-xml/model-in-body-fails.xml",
                "validation-error-invalid-content", null);
    }

    /**
     * Ensure that models with all the different sort of simple initialisation
     * instances work correctly.
     */
    public void testModelInstancesOk() throws Exception {
        checkValidationFromFile("validation-xml/model-instances-ok.xml");
    }

    /**
     * Ensure that group can be used within the body of an XHTML 2 document.
     */
    public void testGroupInBodyOk() throws Exception {
        checkValidationFromFile("validation-xml/group-in-body-ok.xml");
    }

    /**
     * Ensure that group with XHTML 2 structural content works.
     */
    public void testGroupWithStructuralContent() throws Exception {
        checkValidationFromFile(
            "validation-xml/group-with-structural-content.xml");
    }

    /**
     * Ensure that label cannot contain mixed content.
     */
    public void testLabelWithMixedContentFails() throws Exception {
        checkValidationFailsFromFile("validation-xml/label-with-mixed-content-fails.xml",
                "validation-error-invalid-content", new Object[] {
                XFormElements.LABEL, "((PCDATA))?", XHTML2Elements.SPAN
        });
    }

    /**
     * Ensure that form controls can be used within the body of an XHTML 2
     * document.
     */
    public void testControlsInBodyOk() throws Exception {
        checkValidationFromFile("validation-xml/controls-in-body-ok.xml");
    }

    /**
     * Ensure that submit without a label fails.
     */
    public void testSubmitWithoutLabelFails() throws Exception {
        checkValidationFailsFromFile("validation-xml/submit-without-label-fails.xml",
                "validation-error-missing-content", new Object[] {
                XFormElements.SUBMIT, XFormElements.LABEL.toString()
        });
    }

    /**
     * Ensure that submit with a label (and nothing else) works as expected.
     */
    public void testSubmitWithLabelOk() throws Exception {
        checkValidationFromFile("validation-xml/submit-with-label-ok.xml");
    }

    /**
     * Ensure that a submit with a setvalue works as expected.
     * 
     * @throws Exception if there was a problem running the test
     */
    public void testSubmitWithSetvalueOk() throws Exception {
        checkValidationFromFile("validation-xml/submit-with-setvalue-ok.xml");
    }

    /**
     * Ensure that select without an item fails.
     */
    public void testSelectWithoutItemFails() throws Exception {
        checkValidationFailsFromFile("validation-xml/select-without-item-fails.xml",
                "validation-error-missing-content", new Object[] {
                XFormElements.SELECT, XFormElements.ITEM.toString()
        });
    }

    /**
     * Ensure that select with many items is ok.
     */
    public void testSelectWithManyItemsOk() throws Exception {
        checkValidationFromFile("validation-xml/select-with-many-items-ok.xml");
    }
}

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

package com.volantis.mcs.xdime.mcs;

import com.volantis.mcs.xdime.XDIMEValidationTestAbstract;

public class MCSSchemaTestCase
        extends XDIMEValidationTestAbstract {

    /**
     * Ensure that unit does not allow PCDATA if the parent element does not
     * allow it.
     */
    public void testUnitWithinBodyContainingPCDATAFails()
            throws Exception {
        
         checkValidationFailsFromFile(
                 "validation-xml/unit-within-body-containing-pcdata-fails.xml",
                 "validation-error-invalid-content", null);
    }

    /**
     * Ensure that unit does not allow PCDATA if the parent element does not
     * allow it.
     *
     * todo commented out because validator has no support for elements that
     * todo can appear anywhere.
     */
    public void testUnitWithinDivContainingPCDATAOk()
            throws Exception {

         checkValidationFromFile(
                 "validation-xml/unit-within-div-containing-pcdata-ok.xml");
    }

    /**
     * Ensure that br is not allowed directly inside body
     */
    public void testBreakInBodyFails()
            throws Exception {

        checkValidationFailsFromFile(
                "validation-xml/br-in-body-fails.xml",
                "validation-error-invalid-content", null);
    }

    /**
     * Ensure that br is not allowed anywhere in the head
     */
    public void testBreakInHeadFails()
            throws Exception {

        checkValidationFailsFromFile(
                "validation-xml/br-in-head-fails.xml",
                "validation-error-invalid-content", null);
    }

    /**
     * Ensure that br must be empty
     */
    public void testBreakNonEmptyFails()
            throws Exception {

        checkValidationFailsFromFile(
                "validation-xml/br-non-empty-fails.xml",
                "validation-error-invalid-content", null);
    }

    /**
     * Ensure that br can be used wherever text content can be used
     */
    public void testBreakWhereAllowedOK()
            throws Exception {

        checkValidationFromFile(
                "validation-xml/br-where-allowed-ok.xml");
    }
}

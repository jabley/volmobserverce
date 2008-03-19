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

import com.volantis.mcs.xdime.XDIMEValidationTestAbstract;

/**
 * Some integration level tests for the schema validation.
 */
public class XHTML2ObjectValidationTestCase
        extends XDIMEValidationTestAbstract {

    public void testObjectWithParamOk() throws Exception {
        checkValidationFromFile(
                "validation-xml/object-with-param-ok.xml");
    }

    public void testObjectWithCaptionAndParamOk() throws Exception {
        checkValidationFromFile(
                "validation-xml/object-with-caption-and-param-ok.xml");
    }

    public void testObjectWithFlowOk() throws Exception {
        checkValidationFromFile(
                "validation-xml/object-with-flow-ok.xml");
    }

    public void testObjectWithPCDATAOk() throws Exception {
        checkValidationFromFile(
                "validation-xml/object-with-pcdata-ok.xml");
    }
}

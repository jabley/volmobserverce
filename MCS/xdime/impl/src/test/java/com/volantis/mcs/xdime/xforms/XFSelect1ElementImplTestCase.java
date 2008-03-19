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
package com.volantis.mcs.xdime.xforms;

import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.forms.FieldType;
import com.volantis.mcs.xdime.XDIMEContext;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xml.schema.model.ElementType;

/**
 * Tests the concrete class {@link XFSelect1ElementImpl}.
 */
public class XFSelect1ElementImplTestCase extends
        AbstractXFSelectElementImplTestAbstract {

    // Javadoc inherited.
    protected AbstractXFSelectElementImpl getSelectElementImpl(
            XDIMEContextInternal context) {
        return new XFSelect1ElementImpl(context) {
            // Javadoc inherited.
            protected FieldType getFieldType() {
                return mockFieldType;
            }
            // Javadoc inherited.
            protected VolantisProtocol getProtocol(XDIMEContext context) {
                return protocol;
            }
        };
    }

    /**
     * Verify that if multiple initial values are set an exception is thrown.
     */
    public void testFailsWithMultipleInitialValues() {

        try {
            doTest(new String[] {OPTION1_VALUE, OPTION2_VALUE});
            fail("should object to multiple initial values on a single select");
        } catch (XDIMEException e) {
            // do nothing, correct behaviour
        }
    }

    // Javadoc inherited.
    protected boolean getMultiple() {
        return false;
    }

    // Javadoc inherited.
    protected String getTagName() {
        return "xfsiselect";
    }

    // Javadoc inherited.
    protected ElementType getElementType() {
        return XFormElements.SELECT1;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/2	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05	9637/1	emma	VBM:2005092807 Adding tests for XForms emulation

 ===========================================================================
*/

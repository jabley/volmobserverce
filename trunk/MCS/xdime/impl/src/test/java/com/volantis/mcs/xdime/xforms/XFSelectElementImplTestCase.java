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

import com.volantis.mcs.protocols.SelectOption;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.forms.FieldType;
import com.volantis.mcs.xdime.XDIMEContext;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xml.schema.model.ElementType;

import java.util.List;

/**
 * Tests the concrete class {@link XFSelectElementImpl}.
 */
public class XFSelectElementImplTestCase extends
        AbstractXFSelectElementImplTestAbstract {

    // Javadoc inherited.
    protected AbstractXFSelectElementImpl getSelectElementImpl(
            XDIMEContextInternal context) {
        return new XFSelectElementImpl(context) {
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
     * Verify that if multiple initial values are set, all of which map to
     * selector item values, the corresponding {@link SelectOption} instances
     * are selected on the generated protocol attributes.
     */
    public void testWithMultipleNonNullMatchingInitialValues()
            throws XDIMEException {

        final String[] initialValues = new String[] {
            OPTION1_VALUE, OPTION2_VALUE};
        List options = doTest(initialValues);

        assertTrue(((SelectOption)options.get(0)).isSelected());
        assertTrue(((SelectOption)options.get(1)).isSelected());
    }

    /**
     * Verify that if multiple initial values are set, not all of which map to
     * selector item values, only the matching ones' corresponding
     * {@link SelectOption}s are selected on the generated protocol attributes.
     */
    public void testWithMultipleNonNullMatchingInitialValues2()
            throws XDIMEException {

        final String[] initialValues = new String[] {
            OPTION1_VALUE, NON_MATCHING_VALUE};
        List options = doTest(initialValues);

        assertTrue(((SelectOption)options.get(0)).isSelected());
        assertFalse(((SelectOption)options.get(1)).isSelected());
    }

    // Javadoc inherited.
    protected boolean getMultiple() {
        return true;
    }

    // Javadoc inherited.
    protected String getTagName() {
        return "xfmuselect";
    }

    // Javadoc inherited.
    protected ElementType getElementType() {
        return XFormElements.SELECT;
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

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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/papi/XFTextInputElementImplTestCase.java,v 1.5 2003/04/22 10:59:03 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 03-Mar-03    Byron           VBM:2003022813 - Created.
 * 04-Mar-03    Byron           VBM:2003022813 - Removed privateSetup method.
 * 16-Apr-03    Allan           VBM:2003041604 - Removed setTestableElement() 
 *                              and modified all methods that used the element 
 *                              member to create a local element using 
 *                              createTestablePAPIElement(). Modified 
 *                              createTestablePAPIElement() to return a 
 *                              PAPIElement. Now extends 
 *                              AbstractElementImplTestAbstract.
 * 17-Apr-03    Allan           VBM:2003041506 - Uses of createTestableElement 
 *                              updated to createTestablePAPIElement in light 
 *                              of this rename. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.papi.PAPIConstants;
import com.volantis.mcs.papi.PAPIElement;
import com.volantis.mcs.papi.XFTextInputAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolStub;
import com.volantis.mcs.protocols.forms.FieldType;
import com.volantis.mcs.protocols.forms.TextInputFieldType;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolverTestHelper;

/**
 * This class tests the XFTextInputElement class.
 */
public class XFTextInputElementImplTestCase extends XFFormFieldElementImplTestAbstract {

    // javdoc inherited
    protected PAPIElement createTestablePAPIElement() {
        return new XFTextInputElementImpl();
    }

    /**
     * Test the elementStartImpl method. In particular any state changes
     * performed by this method. Note any changes made by calls to other
     * methods are assumed to be tested already.
     */
    public void testElementStartImpl() throws Exception {
        PAPIElement element = createTestablePAPIElement();
        TestMarinerPageContext pageContext = new TestMarinerPageContext();
        MarinerRequestContext requestContext = new TestMarinerRequestContext();
        VolantisProtocol protocol = new VolantisProtocolStub();
        pageContext.pushRequestContext(requestContext);
        pageContext.setProtocol(protocol);
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        protocol.setMarinerPageContext(pageContext);

        pageContext.setPolicyReferenceResolver(
                PolicyReferenceResolverTestHelper.getCommonExpectations(
                        expectations, mockFactory));

        XFTextInputAttributes attributes = new XFTextInputAttributes();
        XFTextInputElementImpl xfElement = (XFTextInputElementImpl)element;
        int result = xfElement.elementStartImpl(requestContext, attributes);
        assertEquals(PAPIConstants.PROCESS_ELEMENT_BODY, result);
        assertNull(attributes.getErrmsg());

        final String errorMsg = "Error msg";
        attributes.setErrmsg(errorMsg);
        result = xfElement.elementStartImpl(requestContext, attributes);
        assertEquals(PAPIConstants.PROCESS_ELEMENT_BODY, result);
        assertEquals(attributes.getErrmsg(), errorMsg);

    }

    /**
     * Test the getting of the initial value. Not much testing required here.
     * Note that the method tested does not use the pageContext parameter and
     * the PAPIAttributes parameter is always assumed to be of the type
     * XFTextInputAttributes.
     */
    public void testGetInitialValue() throws Exception {
        PAPIElement element = createTestablePAPIElement();        
        XFTextInputAttributes attributes = new XFTextInputAttributes();
        assertNull(attributes.getInitial());
        XFTextInputElementImpl xfElement = (XFTextInputElementImpl)element;
        String result = xfElement.getInitialValue(null, attributes);
        assertNull(result);

        final String initialValue = "Initial Value";
        attributes.setInitial(initialValue);
        result = xfElement.getInitialValue(null, attributes);
        assertEquals(result, initialValue);
    }

    /**
     * Test the getting of the field type. This is simple test to ensure the
     * result returned is never null and the type is TextInputFieldType.
     */
    public void testGetFieldType() throws Exception {
        PAPIElement element = createTestablePAPIElement();
        FieldType type = null;
        XFTextInputElementImpl xfElement = (XFTextInputElementImpl)element;
        type = xfElement.getFieldType(null);
        assertNotNull(type);
        assertTrue(type instanceof TextInputFieldType);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 01-Aug-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 08-Aug-03	958/1	chrisw	VBM:2003070704 half way through changes to existing PAPI

 31-Jul-03	868/1	mat	VBM:2003070704 Initial work on this task

 ===========================================================================
*/

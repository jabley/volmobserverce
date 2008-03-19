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

import com.volantis.mcs.context.EnvironmentContextMock;
import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.context.MarinerRequestContextMock;
import com.volantis.mcs.protocols.XFFormFieldAttributes;
import com.volantis.mcs.protocols.XFFormFieldAttributesMock;
import com.volantis.mcs.protocols.forms.EmulatedXFormDescriptor;
import com.volantis.mcs.protocols.forms.FieldType;
import com.volantis.mcs.protocols.forms.FieldTypeMock;
import com.volantis.mcs.xdime.XDIMEContextImpl;
import com.volantis.mcs.xdime.XDIMEContextInternalMock;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.xforms.model.SIItem;
import com.volantis.mcs.xdime.xforms.model.XFormBuilder;
import com.volantis.mcs.xdime.xforms.model.XFormBuilderImpl;
import com.volantis.mcs.xdime.xforms.model.XFormBuilderMock;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import junitx.util.PrivateAccessor;

/**
 * Verifies that {@link XFormsControlElement} works as expected.
 * @todo finish writing tests
 */
public class XFormsControlElementTestCase extends TestCaseAbstract {

    private static final ElementType ELEMENT_TYPE = new ElementType(
            "", "xform-control");
    private static final String REF = "REF";
    private static final String MODEL_ID = "MODEL_ID";
    private static final String CONTENT = "initial value";

    private XFormBuilder builder;
    private XFormsControlElement element;
    private FieldTypeMock fieldType;
    private XFFormFieldAttributesMock attributes;
//    private XDIMEContextInternal context;

    // Javadoc inherited.
    public void setUp() throws Exception {
        super.setUp();

//        context = new XDIMEContextImpl();
        final XDIMEContextInternalMock contextMock =
            new XDIMEContextInternalMock("contextMock", expectations);
        final MarinerRequestContextMock requestContext =
            new MarinerRequestContextMock("requestContext", expectations);
        contextMock.expects.getCurrentElement().returns(null);
        contextMock.expects.getInitialRequestContext().returns(requestContext);
        
        final EnvironmentContextMock environmentContextMock =
            new EnvironmentContextMock("environmentContextMock", expectations);
        environmentContextMock.expects.getCachingDirectives().returns(null).
            any();
        requestContext.expects.getEnvironmentContext().returns(
            environmentContextMock).any();

        builder = new XFormBuilderImpl();
        fieldType = new FieldTypeMock("fieldType", expectations);
        attributes = new XFFormFieldAttributesMock("attributes", expectations);

        element = new XFormsControlElement(ELEMENT_TYPE, contextMock) {
            protected FieldType getFieldType() {
                return fieldType;
            }
        };
        PrivateAccessor.setField(element, "protocolAttributes", attributes);
    }

//    /**
//     * Verifies that calling {@link XFormsControlElement#setInitialValue} with
//     * no ref causes an exception to be thrown
//     */
//    public void testSetInitialValueWithNoRef() {
//
//        try {
//            element.setInitialValue(builder, null, null);
//            fail("Should throw an XDIMEException if attempting to set an " +
//                    "initial value with a null ref");
//        } catch (XDIMEException e) {
//            // do nothing - correct behaviour
//        }
//    }

    /**
     * Verifies that calling {@link XFormsControlElement#setInitialValue} with
     * a value for ref that does not exist in any model and a no model succeeds
     * and the initial value is not set.
     */
    public void testSetInitialValueWithNonExistentRefAndNoModel()
            throws XDIMEException {

        assertNull(builder.getItem(REF, null));
        element.setInitialValue(builder, REF, null);
    }

    /**
     * Verifies that calling {@link XFormsControlElement#setInitialValue} with
     * a value for ref that does not exist in any model and a non existent
     * model causes an exception to be thrown.
     */
    public void testSetInitialValueWithNonExistentRefAndModel()
            throws XDIMEException {

        // confirm that neither the item or the model exist
        assertNull(builder.getItem(REF, null));
        assertNull(builder.getModel((String)MODEL_ID));
        try {
            element.setInitialValue(builder, REF, MODEL_ID);
            fail("Referencing a non existent item in a non existent item with" +
                    "no default model should cause an exception to be thrown");
        } catch (XDIMEException e) {
            // do nothing, correct behaviour
        }

    }

    /**
     * Verifies that calling {@link XFormsControlElement#setInitialValue} with
     * a value for ref that does exist in the model suceeds and the initial
     * value is set to the value of the item referenced.
     */
    public void testSetInitialValueWithRealRef() throws XDIMEException {

        builder.addModel(MODEL_ID, new EmulatedXFormDescriptor());
        builder.addItem(REF, CONTENT);

        // verify that the item exists, is not referenced and the specified
        // model exists
        SIItem item = builder.getItem(REF, MODEL_ID);
        assertNotNull(item);
        assertFalse(item.isReferenced());
        assertNotNull(builder.getModel(MODEL_ID));
        // set the expectation that the setInitial method will be called
        attributes.expects.setInitial(CONTENT);

        element.setInitialValue(builder, REF, MODEL_ID);

        item = builder.getItem(REF, MODEL_ID);
        assertNotNull(item);
        assertTrue(item.isReferenced());

        // make sure that the field descriptor also has the initial value set
        assertEquals(CONTENT, element.fieldDescriptor.getInitialValue());
    }

    /**
     * Verifies that calling {@link XFormsControlElement#setInitialValue} with
     * a value for ref that does exist in the model succeeds and the initial
     * value is set to the value of the item referenced.
     * The initial value contains commas, but it shouldn't make a difference.
     */
    public void testSetInitialValueWithComma() throws XDIMEException {

        builder.addModel(MODEL_ID, new EmulatedXFormDescriptor());
        builder.addItem(REF, "a, b, c");

        // verify that the item exists, is not referenced and the specified
        // model exists
        SIItem item = builder.getItem(REF, MODEL_ID);
        assertNotNull(item);
        assertFalse(item.isReferenced());
        assertNotNull(builder.getModel(MODEL_ID));
        // set the expectation that the setInitial method will be called
        attributes.expects.setInitial("a, b, c");

        element.setInitialValue(builder, REF, MODEL_ID);

        item = builder.getItem(REF, MODEL_ID);
        assertNotNull(item);
        assertTrue(item.isReferenced());

        // make sure that the field descriptor also has the initial value set
        assertEquals("a, b, c", element.fieldDescriptor.getInitialValue());
    }

    public void testCallCloseOnProtocol() throws XDIMEException,
            NoSuchFieldException {

        // Create test objects.
        XFormBuilderMock builderMock =
                new XFormBuilderMock("builder", expectations);
        XDIMEContextImpl context = new XDIMEContextImpl();
        MarinerRequestContextMock requestContext =
                new MarinerRequestContextMock("requestContext", expectations);
        MarinerPageContextMock pageContext =
                new MarinerPageContextMock("pageContext", expectations);

        // Set expectations.
        context.setInitialRequestContext(requestContext);
        requestContext.expects.getMarinerPageContext().returns(pageContext);
        pageContext.expects.getProtocol().returns(null);

        PrivateAccessor.setField(context, "xformBuilder", builderMock);
        assertTrue(element.getProtocolAttributes() instanceof
                XFFormFieldAttributes);
        XFFormFieldAttributes attributes =
                (XFFormFieldAttributes) element.getProtocolAttributes();
        builderMock.expects.updateControl(element.fieldDescriptor, attributes);
        fieldType.expects.doField(null, attributes);

        element.callCloseOnProtocol(context);
    }   
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/3	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05	9637/1	emma	VBM:2005092807 Adding tests for XForms emulation

 ===========================================================================
*/

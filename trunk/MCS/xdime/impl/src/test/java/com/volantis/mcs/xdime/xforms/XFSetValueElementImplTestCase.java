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

import com.volantis.mcs.context.EnvironmentContextMock;
import com.volantis.mcs.context.MarinerRequestContextMock;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.protocols.EventAttributes;
import com.volantis.mcs.protocols.XFActionAttributes;
import com.volantis.mcs.protocols.forms.EmulatedXFormDescriptor;
import com.volantis.mcs.protocols.forms.FieldDescriptor;
import com.volantis.mcs.xdime.DataHandlingStrategyMock;
import com.volantis.mcs.xdime.XDIMEAttribute;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEAttributesImpl;
import com.volantis.mcs.xdime.XDIMEContextImpl;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.mcs.xdime.xforms.model.XFormBuilder;
import com.volantis.mcs.xdime.xforms.model.XFormBuilderMock;
import com.volantis.mcs.xdime.xforms.model.XFormModelMock;
import com.volantis.testtools.mock.test.MockTestCaseAbstract;

/**
 * Verify that {@link XFSetValueElementImpl} behaves as expected.
 */
public class XFSetValueElementImplTestCase extends MockTestCaseAbstract {
    private MarinerRequestContextMock requestContext;
    private EnvironmentContextMock envContext;
    XDIMEAttributes attributes;
    XFormBuilderMock builder;
    XFormModelMock model;

    private static final String VALUE_FROM_ATTRIBUTE = "value from attribute";
    private static final String VALUE_FROM_BODY = "value from body";
    private static final String MODEL_ID = "modelID";
    private static final String submissionID = "subID";
    private static final String action = "http://www.a.com/";
    private static final String method = "GET";

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();
        requestContext = new MarinerRequestContextMock("requestContext",
                expectations);
        envContext = new EnvironmentContextMock("envContext", expectations);
        builder = new XFormBuilderMock("builder", expectations);
        model = new XFormModelMock("model", expectations);

        // Set common expectations.
        requestContext.expects.getEnvironmentContext().returns(envContext);
        envContext.expects.getCachingDirectives().returns(null);
    }

    /**
     * Verify that an exception will be thrown if an event of the wrong type
     * is encountered.
     *
     * @throws XDIMEException if there was a problem running the test.
     */
    public void testCallOpenOnProtocolWithInvalidEvent() throws XDIMEException {

        // Create test objects.
        XDIMEContextInternal context =
                prepareEnvironment("domActivate", submissionID);

        // Run test.
        XFSetValueElementImpl setValue = new XFSetValueElementImpl(context);
        try {
            setValue.callOpenOnProtocol(context, attributes);
            fail("Should throw an exception if processing an event of " +
                    "the wrong type");
        } catch (XDIMEException e) {
            // do nothing, correct behaviour.
        }
    }

    /**
     * Verify that a non existent element can be referenced.
     *
     * @throws XDIMEException if there was a problem running the test.
     */
    public void testCallOpenOnProtocolWithNonExistentReferee()
            throws XDIMEException {

        // Create test objects.
        XDIMEContextInternal context = prepareEnvironment("DOMActivate", "ref1");

        // Run test.
        XFSetValueElementImpl setValue = new XFSetValueElementImpl(context);
        setValue.callOpenOnProtocol(context, attributes);
    }

    /**
     * Verify that {@link XFSetValueElementImpl#callOpenOnProtocol} behaves
     * as expected.
     *
     * @throws XDIMEException if there was a problem running the test.
     */
    public void testCallOpenOnProtocol() throws XDIMEException {

        // Create test objects.
        XDIMEContextInternal context =
                prepareEnvironment("DOMActivate", submissionID);

        // Run test.
        XFSetValueElementImpl setValue = new XFSetValueElementImpl(context);
        setValue.callOpenOnProtocol(context, attributes);
    }

    /**
     * Verify that {@link XFSetValueElementImpl#callCloseOnProtocol} behaves
     * as expected.
     *
     * @throws XDIMEException if there was a problem running the test.
     */
    public void testCallCloseOnProtocol() throws XDIMEException {
        // Create test objects.
        DataHandlingStrategyMock strategy =
                new DataHandlingStrategyMock("strategy", expectations);
        XDIMEContextInternal context =
                prepareEnvironment("DOMActivate", submissionID);

        // Set expectations.
        requestContext.expects.getEnvironmentContext().returns(envContext);
        envContext.expects.getCachingDirectives().returns(null);
        strategy.expects.getCharacterData().returns(VALUE_FROM_BODY);

        XFSubmitElementImpl submitElement = new XFSubmitElementImpl(context);
        XFActionAttributes attributes =
                (XFActionAttributes) submitElement.getProtocolAttributes();
        FieldDescriptor fd = new FieldDescriptor();
        attributes.setFieldDescriptor(fd);
        context.pushElement(submitElement);

        // Run test.
        XFSetValueElementImpl setValue =
                new XFSetValueElementImpl(context, strategy);
        setValue.callCloseOnProtocol(context);
    }

    /**
     * Verify that if a value is specified both via the setvalue value attribute
     * and element body, then the element body data is used.
     *
     * @throws XDIMEException if there was a problem running the test.
     */
    public void testBodyValueOverridesAttributeValue() throws XDIMEException {
        // Create test objects.
        DataHandlingStrategyMock strategy =
                new DataHandlingStrategyMock("strategy", expectations);
        XDIMEContextInternal context =
                prepareEnvironment("DOMActivate", submissionID);

        // Set expectations.
        requestContext.expects.getEnvironmentContext().returns(envContext);
        envContext.expects.getCachingDirectives().returns(null);
        strategy.expects.getCharacterData().returns(VALUE_FROM_BODY);

        XFSubmitElementImpl submitElement = new XFSubmitElementImpl(context);
        XFActionAttributes actionAttributes =
                (XFActionAttributes) submitElement.getProtocolAttributes();
        FieldDescriptor fd = new FieldDescriptor();
        actionAttributes.setFieldDescriptor(fd);
        context.pushElement(submitElement);

        // Run test.
        assertNull(actionAttributes.getInitial());
        assertNull(actionAttributes.getValue());
        assertNull(actionAttributes.getFieldDescriptor().getInitialValue());
        XFSetValueElementImpl setValue =
                new XFSetValueElementImpl(context, strategy);
        setValue.callOpenOnProtocol(context, attributes);
        setValue.callCloseOnProtocol(context);

        assertEquals(VALUE_FROM_BODY, actionAttributes.getInitial().getText(
                TextEncoding.PLAIN));
        assertEquals(VALUE_FROM_BODY, actionAttributes.getValue());
        assertEquals(VALUE_FROM_BODY,
                actionAttributes.getFieldDescriptor().getInitialValue());
    }

    /**
     * Verify that if only an attribute value is specified on the setvalue
     * element (and no body content), then that attribute value is used.
     *
     * @throws XDIMEException if there was a problem running the test.
     */
    public void testAttributeValueUsedWhenNoBodyValue() throws XDIMEException {
        // Create test objects.
        DataHandlingStrategyMock strategy =
                new DataHandlingStrategyMock("strategy", expectations);
        XDIMEContextInternal context =
                prepareEnvironment("DOMActivate", submissionID);

        // Set expectations.
        requestContext.expects.getEnvironmentContext().returns(envContext);
        envContext.expects.getCachingDirectives().returns(null);
        strategy.expects.getCharacterData().returns(null);

        XFSubmitElementImpl submitElement = new XFSubmitElementImpl(context);
        XFActionAttributes actionAttributes =
                (XFActionAttributes) submitElement.getProtocolAttributes();
        FieldDescriptor fd = new FieldDescriptor();
        actionAttributes.setFieldDescriptor(fd);
        context.pushElement(submitElement);

        // Run test.
        assertNull(actionAttributes.getInitial());
        assertNull(actionAttributes.getValue());
        assertNull(actionAttributes.getFieldDescriptor().getInitialValue());
        XFSetValueElementImpl setValue =
                new XFSetValueElementImpl(context, strategy);
        setValue.callOpenOnProtocol(context, attributes);
        setValue.callCloseOnProtocol(context);

        assertEquals(VALUE_FROM_ATTRIBUTE, actionAttributes.getInitial().getText(
                TextEncoding.PLAIN));
        assertEquals(VALUE_FROM_ATTRIBUTE, actionAttributes.getValue());
        assertEquals(VALUE_FROM_ATTRIBUTE,
                actionAttributes.getFieldDescriptor().getInitialValue());
    }

    /**
     * Verify that an exception is thrown if
     * {@link XFSetValueElementImpl#callCloseOnProtocol} is called when the
     * parent element is not a submit element.
     *
     * @throws XDIMEException if there was a problem running the test.
     */
    public void testCallCloseOnProtocolWithInvalidParent()
            throws XDIMEException {
        // Create test objects.
        DataHandlingStrategyMock strategy =
                new DataHandlingStrategyMock("strategy", expectations);
        XDIMEContextInternal context =
                prepareEnvironment("DOMActivate", submissionID);

        // Set expectations.
        requestContext.expects.getEnvironmentContext().returns(envContext);
        envContext.expects.getCachingDirectives().returns(null);
        strategy.expects.getCharacterData().returns(VALUE_FROM_BODY);

        XFormsControlElement parent = new XFSecretElementImpl(context);
        context.pushElement(parent);

        // Run test.
        XFSetValueElementImpl setValue = 
                new XFSetValueElementImpl(context, strategy);
        try {
            setValue.callCloseOnProtocol(context);
            fail("setvalue elements should only appear as children of submit elements");
        } catch (XDIMEException e) {
            // do nothing, correct behaviour
        }
    }

    /**
     * callCloseOnProtocol assumes that callOpenOnProtocol and
     * initialiseAttributes will have been called first, so duplicate the
     * necessary state.
     *
     * @return an XDIMEContext that is ready for callCloseOnProtocol to be
     * called
     * @throws XDIMEException if there was a problem preparing the environment.
     */
    protected XDIMEContextInternal prepareEnvironment(
            String eventName,
            String referencedSubmission)
            throws XDIMEException {

        XDIMEContextInternal context = new XDIMEContextImpl();
        final XFormBuilder xFormBuilder = context.getXFormBuilder();
        EmulatedXFormDescriptor fd = new EmulatedXFormDescriptor();
        xFormBuilder.addModel(MODEL_ID, fd);
        context.setInitialRequestContext(requestContext);

        attributes = new XDIMEAttributesImpl(XFormElements.SETVALUE);
        attributes.setValue(XDIMESchemata.XML_EVENTS_NAMESPACE, "event", eventName);
        attributes.setValue("", XDIMEAttribute.REF.toString(), referencedSubmission);
        attributes.setValue("", XDIMEAttribute.VALUE.toString(), VALUE_FROM_ATTRIBUTE);
        xFormBuilder.registerControl(attributes);
        xFormBuilder.addSubmission(submissionID, new EventAttributes(), action,
                method);

        return context;
    }
}

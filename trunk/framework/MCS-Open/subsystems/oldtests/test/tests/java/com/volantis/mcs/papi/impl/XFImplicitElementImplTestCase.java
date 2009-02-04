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
/*
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/papi/XFImplicitElementImplTestCase.java,v 1.4 2003/04/22 10:59:03 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 21-Mar-2003  Chris W         VBM:2003031909 - Created
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
import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.MarinerRequestContextMock;
import com.volantis.mcs.context.TestEnvironmentContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.Form;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.papi.PAPIConstants;
import com.volantis.mcs.papi.PAPIElement;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.XFFormAttributes;
import com.volantis.mcs.papi.XFImplicitAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolStub;
import com.volantis.mcs.protocols.forms.FieldDescriptor;
import com.volantis.mcs.protocols.forms.FormDescriptorMock;
import com.volantis.mcs.protocols.forms.ImplicitFieldType;
import com.volantis.mcs.protocols.layouts.FormInstance;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.PageGenerationCache;
import com.volantis.mcs.runtime.RuntimeProjectMock;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolverTestHelper;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolverMock;
import com.volantis.mcs.utilities.MarinerURL;

import java.util.List;

/**
 * Tests XFImplicitElement papi element. Currently the test only covers the
 * clientVariableName
 */
public class XFImplicitElementImplTestCase extends AbstractElementImplTestAbstract {

    /**
     * The xfimplicit element's papi attributes
     */
    private XFImplicitAttributes papiAttributes;

    /**
     * The xfimplicit element
     */
    private XFImplicitElementImpl implicitElement;

    /**
     * The xfform element's papi attributes
     */
    private XFFormAttributes formAttributes;

    /**
     * The xfform element
     */
    private XFFormElementImpl formElement;

    /**
     * The request context
     */
    private MarinerRequestContext requestContext;
    protected TestMarinerPageContext pageContext;

    //  JavaDoc inherited from superclass
    protected PAPIElement createTestablePAPIElement() {
        return new XFImplicitElementImpl();
    }

    /**
     * Setups testing.
     */
    private void privateSetUp() throws RepositoryException {
        pageContext = new TestMarinerPageContext();
        requestContext = new TestMarinerRequestContext();
        // Register a dummy EnvironmentContext against the request context
        ContextInternals.setEnvironmentContext(requestContext,
                                               new TestEnvironmentContext());
        VolantisProtocol protocol = new VolantisProtocolStub();        
        pageContext.setProtocol(protocol);
        pageContext.setPageGenerationCache(new PageGenerationCache());
        pageContext.setRequestURL(
                new MarinerURL("http://server:8080/volantis/test.xdime"));
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        protocol.setMarinerPageContext(pageContext);

        // Create the xfform form element surrounding the xfimplicit. This is
        // required as XFImplicitElement.elementStart gets the current element
        // from the page context.
        formElement = new XFFormElementImpl();
        formAttributes = new XFFormAttributes();
        formAttributes.setAction("action");
        formAttributes.setMethod("post");
        formAttributes.setName("form");

        pageContext.setPolicyReferenceResolver(
                PolicyReferenceResolverTestHelper.getCommonExpectations(
                        expectations, mockFactory));

        // Create Form layout object
        CanvasLayout canvasLayout = new CanvasLayout();
        Form form = new Form(canvasLayout);
        form.setName("form");

        // Activate the device layout.
        RuntimeDeviceLayout runtimeDeviceLayout =
                RuntimeDeviceLayoutTestHelper.activate(canvasLayout);

        pageContext.setDeviceLayout(runtimeDeviceLayout);
        pageContext.setForm(form);

        // Create a project to push onto the stack.
        final RuntimeProjectMock runtimeProjectMock =
                new RuntimeProjectMock("runtimeProjectMock", expectations);

        requestContext.pushProject(runtimeProjectMock);
        pageContext.pushRequestContext(requestContext);

        // Create the XFImplicit element and attributes
        implicitElement = new XFImplicitElementImpl();
        papiAttributes = new XFImplicitAttributes();
    }
    
    /**
     * Tests elementStart to ensure that the protocol attribue receives the
     * correct value from the papi attribute when the papi attribute is a
     * String.
     */
    public void testElementStartPAPIAttributeIsString() throws Exception {
        privateSetUp();
        
        // set the papi xfimplicit attributes        
        papiAttributes.setName("name");
        papiAttributes.setClientVariableName("clientVariableName");        
               
        assertEquals("wrong value returned by formElement", 
            PAPIConstants.PROCESS_ELEMENT_BODY,
            formElement.elementStart(requestContext, formAttributes));
        assertEquals("wrong value returned by xfimplicit element",
            PAPIConstants.PROCESS_ELEMENT_BODY,
            implicitElement.elementStart(requestContext, papiAttributes));            
        
        // Check that the protocol attributes received the correct value for
        // client variable name
        com.volantis.mcs.protocols.XFImplicitAttributes pAttributes = 
           (com.volantis.mcs.protocols.XFImplicitAttributes)
                    formElement.getProtocolAttributes().getFields().get(0);
        assertEquals("wrong client variable name", "clientVariableName",
            pAttributes.getClientVariableName());            
    }
    
    /**
     * Tests elementStart to ensure that the protocol attribue receives the
     * correct value from the papi attribute when the papi attribute is a
     * Mariner attribute expression.
     */
    public void testElementStartPAPIAttributeIsExpression() 
            throws Exception {
        privateSetUp();
        
        // set the papi xfimplicit attributes        
        papiAttributes.setName("name");
        papiAttributes.setClientVariableName("{clientVariableName.ext}");

        assertEquals("wrong value returned by formElement",
            PAPIConstants.PROCESS_ELEMENT_BODY,
            formElement.elementStart(requestContext, formAttributes));
        assertEquals("wrong value returned by xfimplicit element",
            PAPIConstants.PROCESS_ELEMENT_BODY,
            implicitElement.elementStart(requestContext, papiAttributes));            
        
        // Check that the protocol attributes received the correct value for
        // client variable name
        com.volantis.mcs.protocols.XFImplicitAttributes pAttributes = 
           (com.volantis.mcs.protocols.XFImplicitAttributes)
                    formElement.getProtocolAttributes().getFields().get(0);                                    
        assertEquals("wrong client variable name", "clientVariableName.ext",
            pAttributes.getClientVariableName());            
    }

    /**
     * Tests elementStart to ensure that a PAPIException is thrown when
     * the papi attribute contains neither a value or clientVariableName
     * attribute.
     */
    public void testElementStartPAPIAttributeIsNull() throws Exception {
        privateSetUp();
        
        // set the papi xfimplicit attributes        
        papiAttributes.setName("name");              
        
        assertEquals("wrong value returned by formElement", 
            PAPIConstants.PROCESS_ELEMENT_BODY,
            formElement.elementStart(requestContext, formAttributes));

        try {
            assertEquals("wrong value returned by xfimplicit element",
                PAPIConstants.PROCESS_ELEMENT_BODY,
                implicitElement.elementStart(requestContext, papiAttributes));
            fail("should have thrown PAPIException when value and " +
                 "clientVariableName attributes absent");            
        } catch (PAPIException e) {
            // Nothing needs to be done here. This is the expected behaviour
        }
    }

    /**
     * Verifies that {@link XFImplicitElementImpl#exprElementStart} fully
     * populates the protocol attributes. For example, later form processing
     * code requires that the form field attributes contain a reference to both
     * the form attributes and the form instance itself. The purpose of this
     * test is to flag where changing the behaviour will impact other parts of
     * MCS.
     *
     * @throws PAPIException        if there was a problem running the test
     * @throws RepositoryException  if there was a problem running the test
     */
    public void testExprElementStartConfiguresProtocolAttributesCorrectly()
            throws PAPIException, RepositoryException {

        // Create test objects.
        XFFormElementImplMock formElement =
                new XFFormElementImplMock("formElement", expectations);
        MarinerRequestContextMock requestContext =
                new MarinerRequestContextMock("requestContext", expectations);
        MarinerPageContextMock pageContext =
                new MarinerPageContextMock("requestContext", expectations);
        FormDescriptorMock fd = new FormDescriptorMock("descriptor", expectations);

        privateSetUp();
        FormInstance form = new FormInstance(NDimensionalIndex.ZERO_DIMENSIONS);

        com.volantis.mcs.protocols.XFFormAttributes formAttributes =
                new com.volantis.mcs.protocols.XFFormAttributes();
        formAttributes.setFormData(form);

        final String name = "testForm";
        papiAttributes.setName(name);
        final String value = "VALUE";
        papiAttributes.setValue(value);
        String clientVariableName = "CVName";
        papiAttributes.setClientVariableName(clientVariableName);

        // Set expectations.
        requestContext.expects.setMarinerPageContext(pageContext);
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        requestContext.expects.getMarinerPageContext().returns(pageContext);
        pageContext.expects.getCurrentElement().returns(formElement);
        formElement.expects.getProtocolAttributes().returns(formAttributes).max(2);
        formElement.expects.getFormDescriptor().returns(fd).any();
        PolicyReferenceResolverMock policyResolver = PolicyReferenceResolverTestHelper.
                getCommonExpectations(expectations, mockFactory);
        pageContext.expects.getPolicyReferenceResolver().returns(policyResolver);
        fd.fuzzy.addField(mockFactory.expectsInstanceOf(FieldDescriptor.class));

        // Run the test.
        implicitElement.exprElementStart(requestContext, papiAttributes);

        List fields = formElement.getProtocolAttributes().getFields();
        assertEquals(1, fields.size());
        com.volantis.mcs.protocols.XFImplicitAttributes attributes =
                (com.volantis.mcs.protocols.XFImplicitAttributes) fields.get(0);
        assertEquals(formAttributes, attributes.getFormAttributes());
        assertEquals(form, attributes.getFormData());
        assertEquals(name, attributes.getName());
        assertEquals(value, attributes.getValue());
        assertEquals(clientVariableName, attributes.getClientVariableName());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 01-Aug-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 21-Feb-05	6986/2	emma	VBM:2005021411 Changes merged from MCS3.3

 15-Feb-05	6974/1	emma	VBM:2005021411 Allowing relative paths to devices.mdpr and xml repository

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6135/1	byron	VBM:2004081726 Allow spatial format iterators within forms

 29-Oct-04	6027/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 28-Oct-04	5897/2	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 13-Feb-04	2966/1	ianw	VBM:2004011923 Added mcsi:policy function

 11-Feb-04	2761/1	mat	VBM:2004011910 Add Project repository

 ===========================================================================
*/

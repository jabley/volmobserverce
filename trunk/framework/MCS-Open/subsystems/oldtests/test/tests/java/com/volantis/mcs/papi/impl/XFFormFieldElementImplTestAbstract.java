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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/papi/XFFormFieldElementImplTestAbstract.java,v 1.4 2003/04/22 10:59:03 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Feb-03    Byron           VBM:2003022813 - Created
 * 16-Apr-03    Allan           VBM:2003041604 - Removed setTestableElement() 
 *                              and modified all methods that used the element 
 *                              member to create a local element using 
 *                              createTestablePAPIElement(). Modified 
 *                              createTestablePAPIElement() to return a 
 *                              PAPIElement. 
 * 17-Apr-03    Allan           VBM:2003041506 - Uses of createTestableElement 
 *                              updated to createTestablePAPIElement in light 
 *                              of this rename. 
 * 07-May-03    Byron           VBM:2003042208 - Updated testDoField() to work
 *                              with correlating modified protocol method.
 * 08-May-03    Byron           VBM:2003050705 - Moved MyVolantisProtocolStub
 *                              inner class to be within test method that uses
 *                              it.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestEnvironmentContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.expression.ExpressionSupport;
import com.volantis.mcs.expression.MCSExpressionHelper;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.Form;
import com.volantis.mcs.layouts.TestPane;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.papi.PAPIConstants;
import com.volantis.mcs.papi.PAPIElement;
import com.volantis.mcs.papi.XFTextInputAttributes;
import com.volantis.mcs.protocols.CanvasAttributes;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.VolantisProtocolStub;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.forms.FormDescriptor;
import com.volantis.mcs.protocols.forms.AbstractForm;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.protocols.layouts.TestPaneInstance;
import com.volantis.mcs.protocols.layouts.FormInstance;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolverTestHelper;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.styling.StylesBuilder;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.namespace.NamespaceFactory;
import junitx.util.PrivateAccessor;

/**
 * Test case for XFFormFieldElement.
 */
public abstract class XFFormFieldElementImplTestAbstract
        extends AbstractElementImplTestAbstract {

    /**
     * This method is difficult to test because it takes the input (parameters
     * and creates a new object (
     */
    public void testDoField() throws Exception {
        
        /**
         * This class is used by testDoField (the only method here so far) an is no
         * longer anonymous in order to check to see if the writeInitialFocus
         * method has been called.
         */
        class MyVolantisProtocolStub extends VolantisProtocolStub {
            boolean writeInitialFocusCalled = false;
            private CanvasAttributes myCanvasAttributes = null;

            // javadoc inherited from superclass
            public CanvasAttributes getCanvasAttributes() {
                if (myCanvasAttributes == null) {
                    myCanvasAttributes = new CanvasAttributes();
                    myCanvasAttributes.setStyles(StylesBuilder.getDeprecatedStyles());
                    myCanvasAttributes.setInitialFocus("ID");
                }
                return myCanvasAttributes;
            }

            public void writeInitialFocus(String tabindex) {
                writeInitialFocusCalled = true;
            }
        };
        PAPIElement element = createTestablePAPIElement();
        TestMarinerRequestContext requestContext = new TestMarinerRequestContext();
        TestMarinerPageContext pageContext = new TestMarinerPageContext();
        XFTextInputAttributes attributes = new XFTextInputAttributes();
        com.volantis.mcs.protocols.XFTextInputAttributes pattributes =
                new com.volantis.mcs.protocols.XFTextInputAttributes();
        attributes.setId("ID");
        attributes.setStyleClass("StyleClass");
        attributes.setTitle("Title");
        attributes.setCaption("Caption");
        attributes.setHelp("Help");
        attributes.setName("Name");
        attributes.setPrompt("Prompt");
        attributes.setShortcut("Shortcut");
        attributes.setTabindex("1");
        XFFormFieldElementImpl xfElement = (XFFormFieldElementImpl) element;

        pageContext.setPolicyReferenceResolver(
                PolicyReferenceResolverTestHelper.getCommonExpectations(
                        expectations, mockFactory));

        MyVolantisProtocolStub protocol = new MyVolantisProtocolStub();
        pageContext.setProtocol(protocol);
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        pageContext.pushRequestContext(requestContext);

        protocol.setMarinerPageContext(pageContext);

        // Subset of initial values should be null
        assertNotNull(pattributes);
        assertNull(pattributes.getCaptionContainerInstance());
        assertNull(pattributes.getEntryContainerInstance());
        assertNull(pattributes.getId());
        assertNull(pattributes.getTitle());

        assertEquals(protocol.writeInitialFocusCalled, false);
        xfElement.doField(pageContext, attributes, pattributes);
        assertEquals(protocol.writeInitialFocusCalled, true);

        // After some values should be match the values set.
        assertNotNull(pattributes);
        assertNull(pattributes.getCaptionContainerInstance());
        assertNull(pattributes.getEntryContainerInstance());
        assertEquals(pattributes.getId(), attributes.getId());
        assertEquals(pattributes.getTitle(), attributes.getTitle());
        assertEquals(pattributes.getCaption().getText(TextEncoding.PLAIN),
                attributes.getCaption());
        assertEquals(pattributes.getHelp().getText(TextEncoding.PLAIN),
                attributes.getHelp());
        assertEquals(pattributes.getName(), attributes.getName());
        assertEquals(pattributes.getPrompt().getText(TextEncoding.PLAIN),
                attributes.getPrompt());
        assertEquals(pattributes.getShortcut().getText(TextEncoding.PLAIN),
                attributes.getShortcut());
        assertNull(pattributes.getFieldDescriptor());
        assertEquals(pattributes.getTabindex(), attributes.getTabindex());
    }

    /**
     * Test the method: checkPaneInstances
     */
    public void testCheckPanes() throws Exception {
        XFFormFieldElementImpl element =
                (XFFormFieldElementImpl)createTestablePAPIElement();

        TestMarinerPageContext pageContext = new TestMarinerPageContext();
        TestMarinerRequestContext requestContext = new TestMarinerRequestContext();
        TestEnvironmentContext environmentContext = new TestEnvironmentContext();
        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(),
                InternalDeviceTestHelper.createTestDevice());

        ExpressionContext expressionContext =
                ExpressionFactory.getDefaultInstance().createExpressionContext(
                        null,
                        NamespaceFactory.getDefaultInstance().createPrefixTracker());

        ContextInternals.setEnvironmentContext(requestContext, environmentContext);
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        MCSExpressionHelper.setExpressionContext(requestContext, expressionContext);

        pageContext.pushRequestContext(requestContext);
        pageContext.setProtocol(protocol);

        ExpressionSupport.registerFunctions(expressionContext);
        expressionContext.setProperty(MarinerRequestContext.class,
                                      requestContext,
                                      true);

        protocol.setMarinerPageContext(pageContext);


        // Now can actually do the test.
        String paneName = "caption-pane";
        CanvasLayout canvasLayout =
                new CanvasLayout();
        TestPane pane = new TestPane(canvasLayout);
        pane.setName(paneName);

        // Activate the device layout.
        RuntimeDeviceLayout runtimeDeviceLayout =
                RuntimeDeviceLayoutTestHelper.activate(canvasLayout);

        pageContext.addPaneMapping(pane);
        pageContext.setDeviceLayout(runtimeDeviceLayout);
        TestPaneInstance fContext = new TestPaneInstance();
        pageContext.setFormatInstance(fContext);

        int result = element.checkPaneInstances(pageContext, null, null);
        assertEquals(result, PAPIConstants.SKIP_ELEMENT_BODY);

        String value = "{layout:getPaneInstance('" + paneName + "',0)}";
        result = element.checkPaneInstances(pageContext, value, null);
        assertEquals(result, PAPIConstants.PROCESS_ELEMENT_BODY);
        PaneInstance captionPaneInstance = (PaneInstance) PrivateAccessor.getField(
                element, "captionPaneInstance");

        PaneInstance entryPaneInstance = (PaneInstance) PrivateAccessor.getField(
                element, "entryPaneInstance");

        assertNotNull(entryPaneInstance);
        assertNotNull(captionPaneInstance);
        assertTrue("Panes should be exactly the same",
                   entryPaneInstance == captionPaneInstance);

        FormDescriptor descriptor = new FormDescriptor();
        XFFormElementImpl formElement = new XFFormElementImpl();
        Form form = new Form(new CanvasLayout());
        form.setName("form-name");
        FormInstance formInstance = new FormInstance(
                NDimensionalIndex.ZERO_DIMENSIONS);
        formInstance.setFormat(form);

        formElement.getProtocolAttributes().setFormData(formInstance);
        PrivateAccessor.setField(formElement, "formDescriptor", descriptor);

        pageContext.setCurrentElement(formElement);

        result = element.checkPaneInstances(pageContext, value, null);
        assertEquals(result, PAPIConstants.PROCESS_ELEMENT_BODY);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Sep-05	9524/1	emma	VBM:2005091503 Added ContainerInstance to allow regions and panes to be treated in the same way

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 30-Aug-05	9353/2	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Aug-05	9298/3	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 01-Aug-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 17-Feb-05	6957/1	geoff	VBM:2005021103 R821: Branding using Projects: Prerequisites: use current project in PAPI phase

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6135/6	byron	VBM:2004081726 Allow spatial format iterators within forms

 12-Nov-04	6135/1	byron	VBM:2004081726 Allow spatial format iterators within forms

 ===========================================================================
*/

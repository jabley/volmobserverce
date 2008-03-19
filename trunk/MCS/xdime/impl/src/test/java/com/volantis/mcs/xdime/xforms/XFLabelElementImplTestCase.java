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
import com.volantis.mcs.protocols.SelectOptionMock;
import com.volantis.mcs.protocols.XFFormFieldAttributesMock;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.layouts.ContainerInstanceMock;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolverMock;
import com.volantis.mcs.xdime.DataHandlingStrategyMock;
import com.volantis.mcs.xdime.XDIMEContextInternalMock;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.xforms.model.XFormBuilderMock;
import com.volantis.mcs.xdime.xforms.model.XFormModelMock;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.styling.StylesMock;
import com.volantis.styling.engine.StylingEngineMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import junitx.util.PrivateAccessor;

/**
 * Verifies that {@link XFLabelElementImpl} behaves as required.
 */
public class XFLabelElementImplTestCase extends TestCaseAbstract {

    private static final String CONTENT = "Some content";
    private static final ElementType ELEMENT_TYPE = new ElementType("", "xform-control");
    private XDIMEContextInternalMock contextMock;

    protected void setUp() throws Exception {
        super.setUp();

        contextMock = new XDIMEContextInternalMock("contextMock", expectations);
    }

    /**
     * Verify that the label should not be written out either immediately or by
     * the label's parent (even though the label's parent can have a label)
     * because there is no current model and therefore nowhere to write to.
     *
     * @throws XDIMEException if there was a problem running the test
     */
    public void testCaptionShouldNotBeWrittenOutBecauseNoCurrentModel()
            throws XDIMEException, NoSuchFieldException {

        contextMock.expects.getCurrentElement().returns(null);
        final MarinerRequestContextMock requestContext =
            new MarinerRequestContextMock("requestContext", expectations);
        contextMock.expects.getInitialRequestContext().returns(
            requestContext).any();
        final EnvironmentContextMock environmentContextMock =
            new EnvironmentContextMock("environmentContextMock", expectations);
        environmentContextMock.expects.getCachingDirectives().returns(null).
            any();
        requestContext.expects.getEnvironmentContext().returns(
            environmentContextMock).any();

        XFormsControlElementMock control = new XFormsControlElementMock(
                "control", expectations, ELEMENT_TYPE, contextMock);

        contextMock.expects.getCurrentElement().returns(control);
        XFLabelElementImpl label = new XFLabelElementImpl(contextMock);
        XFormBuilderMock builder = new XFormBuilderMock("builder", expectations);

        contextMock.expects.getXFormBuilder().returns(builder);
        builder.expects.getCurrentModel().returns(null);

        label.callCloseOnProtocol(contextMock);
    }

    /**
     * Verify that the label should not be written out either immediately or by
     * the label's parent (even though the label's parent can have a label)
     * because the current model is inactive and there is therefore nowhere to
     * write to.
     *
     * @throws XDIMEException if there was a problem running the test
     */
    public void testCaptionShouldNotBeWrittenOutBecauseModelInactive()
            throws XDIMEException, NoSuchFieldException {

        contextMock.expects.getCurrentElement().returns(null);
        final MarinerRequestContextMock requestContext =
            new MarinerRequestContextMock("requestContext", expectations);
        contextMock.expects.getInitialRequestContext().returns(
            requestContext).any();
        final EnvironmentContextMock environmentContextMock =
            new EnvironmentContextMock("environmentContextMock", expectations);
        environmentContextMock.expects.getCachingDirectives().returns(null).
            any();
        requestContext.expects.getEnvironmentContext().returns(
            environmentContextMock).any();

        XFormsControlElementMock control = new XFormsControlElementMock(
                "control", expectations, ELEMENT_TYPE, contextMock);

        contextMock.expects.getCurrentElement().returns(control);
        XFLabelElementImpl label = new XFLabelElementImpl(contextMock);

        XFormBuilderMock builder = new XFormBuilderMock("builder", expectations);
        XFormModelMock model = new XFormModelMock("model", expectations);

        contextMock.expects.getXFormBuilder().returns(builder);
        builder.expects.getCurrentModel().returns(model);
        model.expects.isActive().returns(false);

        label.callCloseOnProtocol(contextMock);
    }

    /**
     * Verify that the label should not be written out either immediately or by
     * the label's parent (even though the label's parent can have a label)
     * because the label applies to a group element.
     *
     * @throws XDIMEException if there was a problem running the test
     */
    public void testCaptionShouldNotBeWrittenOutByGroupParent()
            throws XDIMEException, NoSuchFieldException {

        contextMock.expects.getCurrentElement().returns(null);
        final MarinerRequestContextMock requestContext =
            new MarinerRequestContextMock("requestContext", expectations);
        contextMock.expects.getInitialRequestContext().returns(
            requestContext).any();
        final EnvironmentContextMock environmentContextMock =
            new EnvironmentContextMock("environmentContextMock", expectations);
        environmentContextMock.expects.getCachingDirectives().returns(null).
            any();
        requestContext.expects.getEnvironmentContext().returns(
            environmentContextMock).any();

        XFGroupElementImplMock group = new XFGroupElementImplMock(
                "group", expectations, contextMock);

        contextMock.expects.getCurrentElement().returns(group);
        XFLabelElementImpl label = new XFLabelElementImpl(contextMock);

        XFormBuilderMock builder = new XFormBuilderMock("builder", expectations);
        XFormModelMock model = new XFormModelMock("model", expectations);

        contextMock.expects.getXFormBuilder().returns(builder);
        builder.expects.getCurrentModel().returns(model);

        label.callCloseOnProtocol(contextMock);
    }

    /**
     * Verify that labels whose parent is an {@link XFItemElementImpl} are
     * written out by their parent (assuming the model is non null and active).
     *
     * @throws XDIMEException if there was a problem running the test
     * @throws NoSuchFieldException if there was a problem running the test
     */
    public void testCaptionShouldBeWrittenOutByItemParent()
            throws XDIMEException, NoSuchFieldException {

        ContainerInstanceMock container =
                new ContainerInstanceMock("container", expectations);

        // This expectation is required in order to create the item element
        // with no parent element.
        contextMock.expects.getCurrentElement().returns(null);
        final MarinerRequestContextMock requestContext =
            new MarinerRequestContextMock("requestContext", expectations);
        contextMock.expects.getInitialRequestContext().returns(
            requestContext).any();
        final EnvironmentContextMock environmentContextMock =
            new EnvironmentContextMock("environmentContextMock", expectations);
        environmentContextMock.expects.getCachingDirectives().returns(null).
            any();
        requestContext.expects.getEnvironmentContext().returns(
            environmentContextMock).any();

        XFItemElementImplMock item =
                new XFItemElementImplMock("item", expectations, contextMock);

        XFormBuilderMock builder = new XFormBuilderMock("builder", expectations);
        XFormModelMock model = new XFormModelMock("model", expectations);
        MarinerPageContextMock pageContext =
                new MarinerPageContextMock("pageContext", expectations);
        PolicyReferenceResolverMock policyRefResolver =
                new PolicyReferenceResolverMock("policyRefResolver", expectations);
        SelectOptionMock attributes =
                new SelectOptionMock("attributes", expectations);
        TextAssetReference textAssetRef = new LiteralTextAssetReference("");
        DataHandlingStrategyMock strategy =
                new DataHandlingStrategyMock("strategy", expectations);
        StylesMock labelStyles = new StylesMock("labelStyles", expectations);
        StylingEngineMock stylingEngine =
                new StylingEngineMock("stylingEngine", expectations);

        contextMock.expects.getXFormBuilder().returns(builder);
        builder.expects.getCurrentModel().returns(model);
        model.expects.isActive().returns(true);

        item.expects.getProtocolAttributes().returns(attributes);
        requestContext.expects.getMarinerPageContext().returns(pageContext);
        pageContext.expects.getStylingEngine().returns(stylingEngine);
        stylingEngine.expects.getStyles().returns(labelStyles);
        attributes.expects.setCaptionStyles(labelStyles);

        pageContext.expects.getPolicyReferenceResolver().
                returns(policyRefResolver);
        strategy.expects.getCharacterData().returns(CONTENT);
        policyRefResolver.fuzzy.resolveQuotedTextExpression(
                mockFactory.expectsInstanceOf(String.class)).
                returns(textAssetRef);
        attributes.expects.setCaption(textAssetRef);
        pageContext.expects.getCurrentContainerInstance().returns(container);
        attributes.expects.setCaptionContainerInstance(container);

        // This expectation is required in order to create the label element
        // with an item parent element.
        contextMock.expects.getCurrentElement().returns(item);
        XFLabelElementImpl label = new XFLabelElementImpl(contextMock);

        PrivateAccessor.setField(label, "dataHandlingStrategy", strategy);
        label.callCloseOnProtocol(contextMock);
    }

    /**
     * Verify that labels whose parent is an {@link XFormsControlElement} are
     * written out by their parent (assuming the model is non null and active).
     *
     * @throws XDIMEException if there was a problem running the test
     * @throws NoSuchFieldException if there was a problem running the test
     */
    public void testCaptionShouldBeWrittenOutByControlParent()
            throws XDIMEException, NoSuchFieldException {

        ContainerInstanceMock container =
                new ContainerInstanceMock("container", expectations);

        // This expectation is required in order to create the control element
        // with no parent element.
        contextMock.expects.getCurrentElement().returns(null);
        final MarinerRequestContextMock requestContext =
            new MarinerRequestContextMock("requestContext", expectations);
        contextMock.expects.getInitialRequestContext().returns(
            requestContext).any();
        final EnvironmentContextMock environmentContextMock =
            new EnvironmentContextMock("environmentContextMock", expectations);
        environmentContextMock.expects.getCachingDirectives().returns(null).
            any();
        requestContext.expects.getEnvironmentContext().returns(
            environmentContextMock).any();

        XFormsControlElementMock control = new XFormsControlElementMock(
                "control", expectations, ELEMENT_TYPE, contextMock);

        XFormBuilderMock builder = new XFormBuilderMock("builder", expectations);
        XFormModelMock model = new XFormModelMock("model", expectations);
        MarinerPageContextMock pageContext =
                new MarinerPageContextMock("pageContext", expectations);
        PolicyReferenceResolverMock policyRefResolver =
                new PolicyReferenceResolverMock("policyRefResolver", expectations);
        XFFormFieldAttributesMock attributes =
                new XFFormFieldAttributesMock("attributes", expectations);
        TextAssetReference textAssetRef = new LiteralTextAssetReference("");
        StylesMock labelStyles = new StylesMock("labelStyles", expectations);
        DataHandlingStrategyMock strategy =
                new DataHandlingStrategyMock("strategy", expectations);
        StylingEngineMock stylingEngine =
                new StylingEngineMock("stylingEngine", expectations);

        contextMock.expects.getXFormBuilder().returns(builder);
        builder.expects.getCurrentModel().returns(model);
        model.expects.isActive().returns(true);

        control.expects.getProtocolAttributes().returns(attributes);
        requestContext.expects.getMarinerPageContext().returns(pageContext);
        pageContext.expects.getStylingEngine().returns(stylingEngine);
        stylingEngine.expects.getStyles().returns(labelStyles);
        attributes.expects.setCaptionStyles(labelStyles);

        pageContext.expects.getPolicyReferenceResolver().
                returns(policyRefResolver);
        strategy.expects.getCharacterData().returns(CONTENT);
        policyRefResolver.fuzzy.resolveQuotedTextExpression(
                mockFactory.expectsInstanceOf(String.class)).
                returns(textAssetRef);
        attributes.expects.setCaption(textAssetRef);
        pageContext.expects.getCurrentContainerInstance().returns(container);
        attributes.expects.setCaptionContainerInstance(container);

        // This expectation is required in order to create the label element
        // with an control parent element.
        contextMock.expects.getCurrentElement().returns(control);
        XFLabelElementImpl label = new XFLabelElementImpl(contextMock);

        PrivateAccessor.setField(label, "dataHandlingStrategy", strategy);
        label.callCloseOnProtocol(contextMock);
    }
}

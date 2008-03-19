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
 * $Header$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.Form;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.papi.PAPIElement;
import com.volantis.mcs.papi.XFFormAttributes;
import com.volantis.mcs.papi.XFOptionAttributes;
import com.volantis.mcs.papi.XFSingleSelectAttributes;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.SelectOption;
import com.volantis.mcs.protocols.TestDeviceLayoutContext;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.layouts.FormInstance;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.protocols.layouts.TestPaneInstance;
import com.volantis.mcs.runtime.PageGenerationCache;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolverMock;
import com.volantis.styling.compiler.CSSCompiler;
import com.volantis.mcs.runtime.styling.CSSCompilerBuilder;
import com.volantis.mcs.runtime.themes.ThemeStyleSheetCompilerFactory;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.FontSizeKeywords;
import com.volantis.mcs.themes.properties.FontStyleKeywords;
import com.volantis.mcs.themes.properties.FontWeightKeywords;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.styling.values.MutablePropertyValues;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

/**
 * This class tests the PAPIElement XFOptionElement
 */
public class XFOptionElementImplTestCase extends AbstractElementImplTestAbstract {

    // javadoc inherited from superclass
    protected PAPIElement createTestablePAPIElement() {
        return new XFOptionElementImpl();
    }

    /**
     * Test the method elementStart.
     */
    public void noTestElementStartAddsStyles() throws Exception {
        // configure MCS
        final MarinerRequestContext requestContext =
                new TestMarinerRequestContext();
        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(), null);
        final TestMarinerPageContext pageContext = new TestMarinerPageContext();

        pageContext.pushRequestContext(requestContext);
        pageContext.setProtocol(protocol);
        pageContext.setPageGenerationCache(new PageGenerationCache());
        pageContext.setRequestURL(
                new MarinerURL("http://server:8080/volantis/test.xdime"));

        final PolicyReferenceResolverMock referenceResolverMock =
                new PolicyReferenceResolverMock("referenceResolverMock",
                        expectations);
        pageContext.setPolicyReferenceResolver(referenceResolverMock);
        referenceResolverMock.expects
                .resolveQuotedLinkExpression(null, PageURLType.FORM)
                .returns(null).any();
        referenceResolverMock.expects.resolveQuotedTextExpression(null)
                .returns(null).any();
        referenceResolverMock.expects
                .resolveQuotedTextExpression("testCaption")
                .returns(new LiteralTextAssetReference("testCaption"))
                .any();

        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        protocol.setMarinerPageContext(pageContext);

        loadStyleSheet(pageContext);

        // a form with a pane in it
        final CanvasLayout canvasLayout =
                new CanvasLayout();
        // Activate the device layout.
        RuntimeDeviceLayout runtimeDeviceLayout =
                RuntimeDeviceLayoutTestHelper.activate(canvasLayout);
        pageContext.setDeviceLayout(runtimeDeviceLayout);

        final Form form = new Form(canvasLayout);
        form.setName("form");
        pageContext.setForm(form);
        FormInstance formInstance = new FormInstance(
                NDimensionalIndex.ZERO_DIMENSIONS);
        formInstance.setFormat(form);
        pageContext.setFormatInstance(formInstance);

        final Pane testPane = new Pane(canvasLayout);
        final String paneName = "testPane";
        testPane.setName(paneName);
        testPane.setParent(form);
        final PaneInstance paneInstance = new TestPaneInstance();
        paneInstance.setFormat(testPane);

        final TestDeviceLayoutContext deviceLayoutContext =
                new TestDeviceLayoutContext();
        paneInstance.setDeviceLayoutContext(deviceLayoutContext);
        pageContext.setFormatInstance(paneInstance);
        pageContext.addPaneMapping(testPane);

        // set up xfform element
        final XFFormAttributes xfFormAttributes = new XFFormAttributes();
        final XFFormElementImpl xfFormElement = new XFFormElementImpl();
        xfFormElement.elementStart(requestContext, xfFormAttributes);
        pageContext.setCurrentElement(xfFormElement);

        // set up xfsiselect element
        final XFSingleSelectElementImpl xfSingleSelectElement =
                new XFSingleSelectElementImpl();
        final XFSingleSelectAttributes xfSingleSelectAttributes =
                new XFSingleSelectAttributes();
        xfSingleSelectAttributes.setCaptionPane(paneName);
        xfSingleSelectElement.elementStart(requestContext,
                xfSingleSelectAttributes);
        pageContext.setCurrentElement(xfSingleSelectElement);

        // set up xfoption element
        XFOptionAttributes xfOptionsAttrs = new XFOptionAttributes();
        final String styleClass = "styleClass";
        xfOptionsAttrs.setStyleClass(styleClass);
        final String id = "id";
        xfOptionsAttrs.setId(id);
        xfOptionsAttrs.setCaption("testCaption");
        xfOptionsAttrs.setCaptionClass("testCaptionClass");
        final XFOptionElementImpl element =
                (XFOptionElementImpl) createTestablePAPIElement();

        final int result = element.elementStart(requestContext, xfOptionsAttrs);

        assertTrue("Unexpected value returned from XFOptionElement. Should have been" +
                " PROCESS_ELEMENT_BODY.",
                result == PAPIElement.PROCESS_ELEMENT_BODY);

        final List options =
                xfSingleSelectElement.getProtocolAttributes().getOptions();
        assertEquals(1, options.size());

        final SelectOption option = (SelectOption) options.get(0);
        final MutablePropertyValues propertyValues =
                option.getCaptionStyles().getPropertyValues();
        assertEquals(FontWeightKeywords.BOLD,
                propertyValues.getComputedValue(StylePropertyDetails.FONT_WEIGHT));
        assertEquals(FontSizeKeywords.LARGE,
                propertyValues.getComputedValue(StylePropertyDetails.FONT_SIZE));
        assertEquals(FontStyleKeywords.ITALIC,
                propertyValues.getComputedValue(StylePropertyDetails.FONT_STYLE));
    }

    private void loadStyleSheet(final TestMarinerPageContext pageContext) {
        final InputStream inputStream =
            XFOptionElementImplTestCase.class.getResourceAsStream(
                    "XFOptionElementImplTestCase.css");

        final Reader reader = new InputStreamReader(inputStream);

        // Create a CSS compiler.
        CSSCompilerBuilder builder = new CSSCompilerBuilder();
        builder.setStyleSheetCompilerFactory(
                ThemeStyleSheetCompilerFactory.getDefaultInstance());
        CSSCompiler cssCompiler = builder.getCSSCompiler();

        final CompiledStyleSheet compiledStyleSheet =
            cssCompiler.compile(reader, null);

        pageContext.getStylingEngine().pushStyleSheet(compiledStyleSheet);
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10641/1	geoff	VBM:2005113024 Pagination page rendering issues

 06-Dec-05	10621/1	geoff	VBM:2005113024 Pagination page rendering issues

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 27-Sep-05	9487/2	pduffin	VBM:2005091203 Committing new CSS Parser

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 25-Aug-05	9370/2	gkoch	VBM:2005070507 xform select option to store caption styles instead of caption (style) class

 ===========================================================================
*/

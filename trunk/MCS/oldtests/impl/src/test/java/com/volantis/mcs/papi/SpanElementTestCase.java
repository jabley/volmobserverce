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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/papi/SpanElementTestCase.java,v 1.3 2003/04/25 10:26:08 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 17-Apr-03    Allan           VBM:2003041506 - Test case for SpanElement.
 * 22-Apr-03    Allan           VBM:2003041710 - Added
 *                              testElementStartSrcAttribute and
 *                              testElementStartSkipBody methods.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.papi;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.papi.impl.AttrsElementImplTestAbstract;
import com.volantis.mcs.papi.impl.TestableSpanElementImpl;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.VolantisProtocolStub;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReferenceMock;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolverMock;
import com.volantis.styling.Styles;

/**
 * Test case for SpanElement.
 */
public class SpanElementTestCase extends AttrsElementImplTestAbstract {
    private PolicyReferenceResolverMock referenceResolverMock;

    public void setUp() throws Exception {
        super.setUp();

        referenceResolverMock = new PolicyReferenceResolverMock(
                "referenceResolverMock", expectations);
        pageContext.setPolicyReferenceResolver(referenceResolverMock);

        referenceResolverMock.expects.resolveQuotedTextExpression(null)
                .returns(null).any();
    }

    public void testElementStartSrcExpressionAttribute() throws Exception {

        final TextAssetReferenceMock textAssetReferenceMock =
                new TextAssetReferenceMock("textAssetReferenceMock",
                        expectations);

        referenceResolverMock.expects
                .resolveQuotedTextExpression("{expression.ext}")
                .returns(textAssetReferenceMock).any();

        textAssetReferenceMock.expects.isPolicy().returns(false).any();

        SpanAttributes attrsAttributes =
                (SpanAttributes) createTestableAttrsAttributes();
        attrsAttributes.setSrc("{expression.ext}");

        TestableSpanElementImpl element = (TestableSpanElementImpl)
                createTestablePAPIElement();

        com.volantis.mcs.protocols.SpanAttributes protocolAttributes =
                new com.volantis.mcs.protocols.SpanAttributes();

        element.setVolantisAttributes(protocolAttributes);

        element.elementStart(requestContext, attrsAttributes);
    }

    public void testElementStartSrcNonExpressionAttribute() throws Exception {

        referenceResolverMock.expects
                .resolveQuotedTextExpression("not an epxression")
                .returns(new LiteralTextAssetReference("not an epxression"))
                .any();

        SpanAttributes attrsAttributes =
                (SpanAttributes) createTestableAttrsAttributes();
        attrsAttributes.setSrc("not an epxression");

        TestableSpanElementImpl element = (TestableSpanElementImpl)
                createTestablePAPIElement();

        com.volantis.mcs.protocols.SpanAttributes protocolAttributes =
                new com.volantis.mcs.protocols.SpanAttributes();

        element.setVolantisAttributes(protocolAttributes);

        element.elementStart(requestContext, attrsAttributes);

        assertTrue("src property in protocol attributes should not be" +
                   " equal to src property on papi attributes",
                   !attrsAttributes.getSrc().
                    equals(protocolAttributes.getSrc()));

        assertEquals("protocol src attribute should be null",
                     null, protocolAttributes.getSrc());
    }

    public void testElementStartSkipBody() throws Exception {
        VolantisProtocolStub protocol = new VolantisProtocolStub() {
            public boolean skipElementBody() {
                return true;
            }
        };

        pageContext.setProtocol(protocol);

        SpanElement element = new SpanElement();

        int result = element.elementStart(requestContext, attrsAttributes);

        assertEquals("Expected SKIP_ELEMENT_BODY from elementStart",
                     PAPIConstants.SKIP_ELEMENT_BODY, result);
    }

    /**
     * This tests that elementStart adds a {@link Styles} to the
     * {@link MCSAttributes}.
     * @throws PAPIException
     */
    public void testElementStartAddsStyles() throws PAPIException {
        SpanAttributes attrsAttributes =
                (SpanAttributes) createTestableAttrsAttributes();
        attrsAttributes.setSrc("{expression.ext}");

        TestableSpanElementImpl element = (TestableSpanElementImpl)
                createTestablePAPIElement();

        final TextAssetReferenceMock textAssetReferenceMock =
                new TextAssetReferenceMock("textAssetReferenceMock",
                        expectations);

        referenceResolverMock.expects
                .resolveQuotedTextExpression("{expression.ext}")
                .returns(textAssetReferenceMock).any();

        textAssetReferenceMock.expects.isPolicy().returns(false).any();

        com.volantis.mcs.protocols.SpanAttributes protocolAttributes =
                new com.volantis.mcs.protocols.SpanAttributes();

        element.setVolantisAttributes(protocolAttributes);

        element.elementStart(requestContext, attrsAttributes);

        // ensure that there is a CSP Container in the protocol attributes
        Styles styles = protocolAttributes.getStyles();
        assertNotNull("The protocol attributes should have a Styles after" +
                "elementStart has been called", styles);
    }

    // javadoc inherited
    protected MCSAttributes createTestableProtocolAttributes() {
        return new com.volantis.mcs.protocols.SpanAttributes();
    }

    protected AttrsAttributes createTestableAttrsAttributes() {
        return new SpanAttributes();
    }

    // javadoc inherited
    protected PAPIElement createTestablePAPIElement() {
        return new TestableSpanElementImpl();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 01-Aug-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 20-Jun-05	8483/2	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 11-May-05	8123/2	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 24-May-05	8123/5	ianw	VBM:2005050906 Fix merge conflicts

 11-May-05	8123/2	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 18-May-05	8196/1	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 09-Mar-05	7022/2	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 09-Feb-05	6914/1	geoff	VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

 28-Feb-05	7149/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 28-Feb-05	7127/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	6183/3	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 18-Nov-04	6135/1	byron	VBM:2004081726 Allow spatial format iterators within forms

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 13-Feb-04	2966/1	ianw	VBM:2004011923 Added mcsi:policy function

 ===========================================================================
*/

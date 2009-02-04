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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/VolantisProtocolTestAbstract.java,v 1.4 2003/04/24 16:42:23 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 02-Dec-02    Phil W-S        VBM:2002112701 - Created to test the
 *                              new external/internal stylesheet generation
 *                              options.
 * 30-Jan-03    Geoff           VBM:2003012101 - Refactor so that it uses
 *                              inheritance to mirror the structure of the
 *                              Protocol heirarchy it is testing, and so it
 *                              uses the new TestMariner...Context classes
 *                              rather than a "cut & paste" inner classes
 *                              which extend Mariner...Context.
 * 12-Feb-03    Phil W-S        VBM:2003021206 - Update VolantisProtocol
 *                              specialization.
 * 03-Mar-03    Byron           VBM:2003022813 - Added testGetInitialValue().
 * 05-Mar-03    Chris W         VBM:2003030523 - Added testSetMultipleSelected
 * 21-Mar-03    Phil W-S        VBM:2003031910 - Update the test for
 *                              renderMode, add a test for initialization of
 *                              preferredLocationFor* members in initialise
 *                              and updated testMemberConstants.
 * 28-Mar-03    Geoff           VBM:2003031711 - Refactored from
 *                              VolantisProtocolTestCase so that this class is
 *                              abstract, required to add the new abstract
 *                              testRenderAltText.
 * 01-Apr-03    Phil W-S        VBM:2002111502 - Added testing of dialling link
 *                              initialization and usage.
 * 24-Apr-03    Byron           VBM:2003040903 - Modified
 *                              testResolveQualifiedFullNumber and
 *                              testResolvePhoneNumberAttributes.
 * 27-May-03    Byron           VBM:2003051904 - Added getDOMGeneratedString.
 * 30-May-03    Mat             VBM:2003042911 - Change writeCanvasContent()
 *                              & writeMontageContent() to accept a
 *                              PackageBodyOutput instead of a Writer
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;


import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.EnvironmentContextMock;
import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestEnvironmentContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.css.renderer.CSSStyleSheetRenderer;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.devices.InternalDeviceMock;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.layouts.Form;
import com.volantis.mcs.layouts.FormFragment;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralLinkAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.forms.FormDataManagerMock;
import com.volantis.mcs.protocols.forms.FormDescriptor;
import com.volantis.mcs.protocols.forms.SessionFormDataMock;
import com.volantis.mcs.protocols.layouts.FormInstance;
import com.volantis.mcs.runtime.RuntimePageURLRewriter;
import com.volantis.mcs.runtime.StyleSheetConfiguration;
import com.volantis.mcs.runtime.URLConstants;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.configuration.ProtocolsConfiguration;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.styling.StylesBuilder;
import junitx.util.PrivateAccessor;
import org.xml.sax.XMLReader;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for the {@link VolantisProtocol} class.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public abstract class VolantisProtocolTestAbstract extends ProtocolTestAbstract {

    /**
     * The object to use to create style values.
     */
    protected StyleValueFactory styleValueFactory;

    /**
     * Constant available for phone number related testing.
     */
    protected final String phoneNumberPrefix = "phoneNumberPrefix:";

    /**
     * Constant available for phone number related testing.
     */
    protected final String phoneNumber = "+441483739739";

    /**
     * Constant available for phone number related testing.
     */
    protected final TextAssetReference phoneNumberReference =
            new LiteralTextAssetReference(phoneNumber);

    /**
     * Constant available for phone number related testing.
     */
    protected final TextAssetReference objectNumber =
            new LiteralTextAssetReference("+441483739000");

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    private VolantisProtocol protocol;
    private VolantisProtocolTestable testable;

    /**
     * Initialize the new test case instance.
     *
     * @param name the name of the test case
     */
    public VolantisProtocolTestAbstract(String name) {
        super(name);
    }

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();

        styleValueFactory = StyleValueFactory.getDefaultInstance();
    }

    protected VolantisProtocol createTestableProtocol(
            InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.TestVolantisProtocolFactory(),
                internalDevice);
        return protocol;
    }

    protected void setTestableProtocol(VolantisProtocol protocol,
            VolantisProtocolTestable testable) {
        this.protocol = protocol;
        this.testable = testable;
    }

    /**
     * Tests the member constants.
     *
     * @throws Exception
     * @todo later cover all constants
     */
    public void testMemberConstants() throws Exception {
        assertEquals("Constant StylesheetRenderMode.DEFAULT not as",
                     "default",
                     VolantisProtocol.StylesheetRenderMode.DEFAULT.toString());

        assertEquals("Constant StylesheetRenderMode.INTERNAL not as",
                     "internal",
                     VolantisProtocol.StylesheetRenderMode.INTERNAL.
                     toString());

        assertEquals("Constant StylesheetRenderMode.EXTERNAL not as",
                     "external",
                     VolantisProtocol.StylesheetRenderMode.EXTERNAL.
                     toString());

        assertEquals("Constant StylesheetRenderMode.IMPORT not as",
                     "import",
                     VolantisProtocol.StylesheetRenderMode.IMPORT.toString());

        assertEquals("Constant DiallingLinkInfoType.NONE not as",
                     "none",
                     VolantisProtocol.DiallingLinkInfoType.NONE.toString());

        assertEquals("Constant DiallingLinkInfoType.PREFIX not as",
                     "prefix",
                     VolantisProtocol.DiallingLinkInfoType.PREFIX.toString());
    }

    /**
     * Test the getting of the initial value with a valid form and:
     * a) attributes without an initial value set
     * b) attributes with an initial value set as a string
     * c) attributes with an initial value set as a TextComponentIdentity
     * d) fragmented form with nitial value set as a TextComponentIdentity
     */
    public void testGetInitialValue() throws Exception {
        // a) Expect null as the result
        XFTextInputAttributes attributes = new XFTextInputAttributes();
        FormInstance formInstance = new FormInstance(NDimensionalIndex.ZERO_DIMENSIONS);
        final Form form = new Form(null);
        formInstance.setFormat(form);
        attributes.setFormData(formInstance);

        TestMarinerRequestContext requestContext = new TestMarinerRequestContext();
        ContextInternals.setEnvironmentContext(requestContext,
                new TestEnvironmentContext());

        TestMarinerPageContext pageContext = new TestMarinerPageContext();
        FormDescriptor fd = new FormDescriptor();
        String formSpecifier =
                pageContext.getFormDataManager().getFormSpecifier(fd);
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        pageContext.pushRequestContext(requestContext);
        protocol.setMarinerPageContext(pageContext);

        String result = protocol.getInitialValue(attributes);
        assertEquals(null, result);

        // b) Expect 'Test' as the result
        attributes.setInitial("Test");
        result = protocol.getInitialValue(attributes);
        assertEquals("Test", result);

        // c) Expect value of textComponentID as the result.
        attributes.setInitial(new LiteralTextAssetReference("foobar"));
        result = protocol.getInitialValue(attributes);
        assertEquals("foobar", result);

        // d) Expect the value of textComponentID (since sessionContext.
        // getAttribute  return null).
        attributes.setInitial(new LiteralTextAssetReference("foobar"));
        final XFFormAttributes formAttributes = new XFFormAttributes();
        formAttributes.setFormSpecifier(formSpecifier);
        attributes.setFormAttributes(formAttributes);        
        form.addFormFragment(new FormFragment(null));
        result = protocol.getInitialValue(attributes);
        assertEquals("foobar", result);
    }

    public void testSetMultipleSelected() {
        // Create the list of options on the form
        SelectOption a = new SelectOption();
        a.setValue("a");
        SelectOption b = new SelectOption();
        b.setValue("b");
        SelectOption c = new SelectOption();
        c.setValue("c");
        SelectOption d = new SelectOption();
        d.setValue("d");
        List options = new ArrayList();
        options.add(a);
        options.add(b);
        options.add(c);
        options.add(d);

        // The options that the user selected on the form
        ArrayList initialValues = new ArrayList();
        initialValues.add("b");
        initialValues.add("c");

        // Run the test
        protocol.setSelectedOptions(options, initialValues);

        // Check the results
        assertTrue("option a should not be selected", !a.isSelected());
        assertTrue("option b should be selected", b.isSelected());
        assertTrue("option c should be selected", c.isSelected());
        assertTrue("option d should not be selected", !d.isSelected());
    }

    public void testSetSingleWithNullValueOption() {
        // Create the list of options on the form
        SelectOption a = new SelectOption();
        a.setValue(null);
        SelectOption b = new SelectOption();
        b.setValue("b");
        SelectOption c = new SelectOption();
        c.setValue("c");
        SelectOption d = new SelectOption();
        d.setValue("d");
        List options = new ArrayList();
        options.add(a);
        options.add(b);
        options.add(c);
        options.add(d);

        // The options that the user selected on the form
        ArrayList initialValues = new ArrayList();
        initialValues.add("b");

        // Run the test
        protocol.setSelectedOptions(options, initialValues);

        // Check the results
        assertTrue("option a should not be selected", !a.isSelected());
        assertTrue("option b should be selected", b.isSelected());
        assertTrue("option c should be selected", !c.isSelected());
        assertTrue("option d should not be selected", !d.isSelected());
    }
    /**
     * todo XDIME-CP
     * @throws Exception
     */
    public void notestInitialisePreferredLocationFor() throws Exception {
        Volantis volantis = getVolantis();

        TestMarinerPageContext context = new TestMarinerPageContext();
        context.pushRequestContext(new TestMarinerRequestContext());
        context.setVolantis(volantis);
        protocol.setMarinerPageContext(context);
        context.setProtocol(protocol);
        context.setDevice(InternalDeviceTestHelper.createTestDevice());
        context.setDevicePolicyValue(
            DevicePolicyConstants.STYLESHEET_LOCATION_DEVICE,
            null);

        context.setDevicePolicyValue(
            DevicePolicyConstants.STYLESHEET_LOCATION_LAYOUT,
            DevicePolicyConstants.STYLESHEET_RENDER_INTERNAL);

        context.setDevicePolicyValue(
            DevicePolicyConstants.STYLESHEET_LOCATION_THEME,
            DevicePolicyConstants.STYLESHEET_RENDER_EXTERNAL);

        protocol.initialise();

        assertEquals(protocol.preferredLocationForThemeStylesheets,
                     VolantisProtocol.StylesheetRenderMode.EXTERNAL);
    }

    public void testInitialiseAccesskeySupportNull() throws Exception {
        doAccesskeySupportTest(null,
                               protocol.supportsAccessKeyAttribute);
    }

    public void testInitialiseAccesskeySupportDefault() throws Exception {
        doAccesskeySupportTest(DevicePolicyConstants.ACCESSKEY_SUPPORT_DEFAULT,
                               protocol.supportsAccessKeyAttribute);
    }

    public void testInitialiseAccesskeySupportTrue() throws Exception {
        doAccesskeySupportTest(Boolean.TRUE.toString(),
                               true);
    }

    public void testInitialiseAccesskeySupportFalse() throws Exception {
        doAccesskeySupportTest(Boolean.FALSE.toString(),
                               false);
    }

    public void testInitialiseAccesskeySupportInvalid() throws Exception {
        doAccesskeySupportTest("twaddle",
                               false);
    }

    /**
     * Verifies that the correct behaviour is seen for initialization of the
     * accesskey support set up in {@link VolantisProtocol#initialise}.
     *
     * @param accesskeyValue the device policy's value
     * @param expected the expected support setting
     */
    protected void doAccesskeySupportTest(String accesskeyValue,
                                        boolean expected) {
        Volantis volantis = getVolantis();
        TestMarinerPageContext context = new TestMarinerPageContext();
        context.pushRequestContext(new TestMarinerRequestContext());
        context.setVolantis(volantis);
        protocol.setMarinerPageContext(context);
        context.setProtocol(protocol);
        context.setDevice(InternalDeviceTestHelper.createTestDevice());

        context.setDevicePolicyValue(
                DevicePolicyConstants.ACCESSKEY_SUPPORTED,
                accesskeyValue);


        protocol.initialiseAccessKeySupport();

        assertEquals(protocol.supportsAccessKeyAttribute,
                     expected);
    }

    public void testInitialiseDiallingLinkWithPrefix() throws Exception {
        final String prefix = "prefix:";

        Volantis volantis = getVolantis();
        TestMarinerPageContext context = new TestMarinerPageContext();
        context.pushRequestContext(new TestMarinerRequestContext());

        context.setVolantis(volantis);
        protocol.setMarinerPageContext(context);
        context.setProtocol(protocol);
        context.setDevice(InternalDeviceTestHelper.createTestDevice());

        context.setBooleanDevicePolicyValue(
            DevicePolicyConstants.SUPPORTS_LINK_DIALLING,
            true);

        context.setDevicePolicyValue(
            DevicePolicyConstants.LINK_DIALLING_INFO_TYPE,
            DevicePolicyConstants.LINK_DIALLING_INFO_TYPE_PREFIX);

        context.setDevicePolicyValue(
            DevicePolicyConstants.LINK_DIALLING_INFO,
            prefix);

        protocol.initialiseDiallingLink();

        assertEquals(protocol.supportsDiallingLinks,
                     true);
        assertEquals(protocol.diallingLinkInfoType,
                     VolantisProtocol.DiallingLinkInfoType.PREFIX);
        assertEquals(protocol.diallingLinkInfo,
                     prefix);
    }

    public void testResolveQualifiedFullNumber() throws Exception {
        protocol.supportsDiallingLinks = true;

        protocol.diallingLinkInfoType =
            VolantisProtocol.DiallingLinkInfoType.NONE;

        protocol.diallingLinkInfo = phoneNumberPrefix;

        assertEquals("resolution with none dialling link info type not as",
                     expectedQualifiedFullNumber(null, phoneNumber),
                     protocol.resolveQualifiedFullNumber(phoneNumber));

        protocol.diallingLinkInfoType =
            VolantisProtocol.DiallingLinkInfoType.PREFIX;

        assertEquals("resolution with prefix dialling link info not as",
                     expectedQualifiedFullNumber(phoneNumberPrefix,
                                                 phoneNumber),
                     protocol.resolveQualifiedFullNumber(phoneNumber));

        protocol.diallingLinkInfo = null;

        assertEquals("resolution with null prefix dialling link info not as",
                     expectedQualifiedFullNumber(null, phoneNumber),
                     protocol.resolveQualifiedFullNumber(phoneNumber));

        protocol.diallingLinkInfo = null;

        assertEquals("resolution with empty prefix dialling link info not as",
                     expectedQualifiedFullNumber(null, phoneNumber),
                     protocol.resolveQualifiedFullNumber(phoneNumber));

        protocol.supportsDiallingLinks = false;

        assertNull("resolution with no support should return null",
                   protocol.resolveQualifiedFullNumber(phoneNumber));
    }

    public void testResolvePhoneNumberAttributes() throws Exception {
        TestMarinerPageContext context = new TestMarinerPageContext();

        protocol.setMarinerPageContext(context);
        context.setProtocol(protocol);

        PhoneNumberAttributes attributes =
            createPhoneNumberAttributes(phoneNumberReference);

        protocol.supportsDiallingLinks = true;

        protocol.diallingLinkInfoType =
            VolantisProtocol.DiallingLinkInfoType.PREFIX;

        protocol.diallingLinkInfo = phoneNumberPrefix;

        protocol.resolvePhoneNumberAttributes(attributes);

        assertEquals("resolution of qualified full number with prefix " +
                     "dialling link info not as",
                     expectedQualifiedFullNumber(phoneNumberPrefix,
                                                 phoneNumber),
                     attributes.getQualifiedFullNumber());
    }

    /**
     * Permits the expected qualified full number to be generated differently
     * in different specializations of this test case. Default behaviour is
     * appropriate to most protocols. Prefix may be null.
     *
     * @param prefix the qualified number prefix
     * @param fullNumber the full number required
     * @return the qualified full number
     */
    protected String expectedQualifiedFullNumber(String prefix,
                                                 String fullNumber) {
        String result = fullNumber;

        if (prefix != null) {
            result = prefix + fullNumber;
        }

        return result;
    }

    /**
     * Returns a newly created {@link PhoneNumberAttributes} instance populated
     * with the given full number (String or Object). Additional attributes
     * may be populated within the attributes after return of the instance or
     * by augmenting this method in specializations.
     *
     * @param fullNumber the full number to be set in the attributes returned
     * @return pre-defined phone number attributes
     */
    protected PhoneNumberAttributes createPhoneNumberAttributes(
        TextAssetReference fullNumber) {
        PhoneNumberAttributes attributes = new PhoneNumberAttributes();

        attributes.setFullNumber(fullNumber);
        attributes.setStyles(StylesBuilder.getInitialValueStyles());

        return attributes;
    }

    /**
     * Test the renderAltText method; abstract at this level since
     * we can't test it until we have a proper protocol with proper output
     * buffers to collect the output.
     *
     * @throws Exception
     */
    public abstract void testRenderAltText() throws Exception;

    /**
     * Test that the protocol can provide a fragment link renderer.
     */
    public void testGetFragmentLinkRenderer() {

        if (!protocol.supportsFragmentation) {
            return;
        }

        // Set up the required contexts
        MarinerRequestContext requestContext = new TestMarinerRequestContext();
        TestMarinerPageContext context = new TestMarinerPageContext();
        context.pushRequestContext(requestContext);
        ContextInternals.setMarinerPageContext(requestContext, context);
        testable.setStyleSheetRenderer(CSSStyleSheetRenderer.getSingleton());
        context.setDeviceName("PC-Win32-IE5.5");
        protocol.setMarinerPageContext(context);
        context.setDevice(InternalDeviceTestHelper.createTestDevice());

        FraglinkAttributes attrs = new FraglinkAttributes();
        attrs.setStyles(StylesBuilder.getEmptyStyles());
        FragmentLinkRenderer renderer = null;

        // Get renderer for null style.
        renderer = protocol.getFragmentLinkRenderer(attrs);
        // This should be the default renderer.
        checkFragmentLinkRenderer(renderer);

        // Then, check the numeric shortcut renderer.
        // But, we add the ability to render openlink style menus when the user
        // defines a theme with a mariner-link-style of numeric-shortcut.

        // Get renderer for button style.
        attrs.setStyles(StylesBuilder.getStyles("mcs-link-style: button"));
        renderer = protocol.getFragmentLinkRenderer(attrs);
        // This should always be same as the default for now.
        checkFragmentLinkRenderer(renderer);

        // Get renderer for numeric shortcut style.
        attrs.setStyles(StylesBuilder.getStyles("mcs-link-style: numeric-shortcut"));
        renderer = protocol.getFragmentLinkRenderer(attrs);
        // This should be the numeric-shortcut renderer if one was defined.
        checkNumericShortcutFragmentLinkRenderer(renderer);
    }

    /**
     * Override this if your protocol supports fragments and uses a different
     * default fragment link renderer than the normal one.
     *
     * @param renderer the default fragment link renderer to check.
     */
    protected void checkFragmentLinkRenderer(FragmentLinkRenderer renderer) {

        assertTrue(renderer instanceof DefaultFragmentLinkRenderer);

    }

    /**
     * Override this if your protocol supports fragments and provides a
     * numeric-shortcut fragment link renderer.
     *
     * @param renderer the numeric-shortcut fragment link renderer to check.
     */
    protected void checkNumericShortcutFragmentLinkRenderer(
            FragmentLinkRenderer renderer) {

        // At this level we don't have a numeric shortcut renderer so we will
        // have fallen back to the default renderer.
        assertTrue(renderer instanceof DefaultFragmentLinkRenderer);

    }

    /**
     * Test that the protocol can provide a fragment link renderer context.
     * Protocols test cases which do not support fragments will need to
     * override this to do nothing.
     */
    public void testGetFragmentLinkRendererContext() {
        if (protocol.supportsFragmentation) {
            FragmentLinkRendererContext context =
                    protocol.getFragmentLinkRendererContext();
            assertNotNull(context);
            checkFragmentLinkRendererContext(context);
        }
    }

    /**
     * Tests that the default mime type for the protocol is returned if the
     * device doesn't specify an overriding policy value.
     */
    public void testMimeTypeDevicePolicyValueNull() throws Exception {
        TestMarinerPageContext context = new TestMarinerPageContext();

        protocol.setMarinerPageContext(context);
        context.setProtocol(protocol);
        context.setDevicePolicyValue(DevicePolicyConstants.PROTOCOL_MIME_TYPE,
                                     null);

        assertEquals("mime type not as",
                     protocol.defaultMimeType(),
                     protocol.mimeType());
    }

    /**
     * Test that getLinkFromReference works when the Object is a String.
     * This method also shows one of the weaknesses in the
     * getLinkFromReference method in that the String can be any string yet
     * the returned value is according to the method supposed to be a link.
     */
    public void testGetLinkFromReference() {
        LinkAssetReference s = new LiteralLinkAssetReference("string");

        String link = protocol.getLinkFromReference(s);

        assertEquals(s.getURL(), link);
    }


    /**
     * Test that getLinkFromReference works when it is null.
     */
    public void testGetLinkFromNullReference() throws Throwable {
        String link = protocol.getLinkFromReference(null);

        assertNull(link);
    }


    /**
     * Test that getLinkFromReference works when the object is a
     * LinkComponentIdentity and encodeSegmentURL is true.
     */
    public void testGetLinkFromReferenceEncodeSegmentURLTrue()
            throws Throwable {

        final String assetURL = "http://test.url";
        final String defaultSegment = "defaultSegment";

        final MarinerPageContextMock pageContextMock =
                new MarinerPageContextMock("pageContextMock",
                        expectations);
        protocol.setMarinerPageContext(pageContextMock);

        LinkAssetReference reference =
                new LiteralLinkAssetReference(assetURL);

        final InternalDeviceMock deviceMock =
                new InternalDeviceMock("deviceMock", expectations);

        pageContextMock.expects.getRequestURL(false)
                .returns(new MarinerURL("defaultSegment"));
        pageContextMock.expects.getDevice().returns(deviceMock).any();
        deviceMock.expects.getPolicyValue("aggregation").returns("false").any();

        String link = protocol.getRewrittenLinkFromObject(reference, true);

        assertEquals(assetURL + "?defseg=" + defaultSegment, link);
    }

    /**
     * Test that getLinkFromReference works when the object is a
     * LinkComponentIdentity and encodeSegmentURL is false.
     */
    public void testGetLinkFromReferenceEncodeSegmentURLFalse()
            throws Throwable {

        final String assetURL = "http://test.url";

        LinkAssetReference reference =
                new LiteralLinkAssetReference(assetURL);

        String link = protocol.getRewrittenLinkFromObject(reference, false);

        assertEquals(assetURL, link);
    }

    /**
     * Tests that the default mime type for the protocol is returned if the
     * device doesn't specify an overriding policy value.
     */
    public void testMimeTypeDevicePolicyValueEmpty() throws Exception {
        TestMarinerPageContext context = new TestMarinerPageContext();

        protocol.setMarinerPageContext(context);
        context.setProtocol(protocol);
        context.setDevicePolicyValue(DevicePolicyConstants.PROTOCOL_MIME_TYPE,
                                     "");

        assertEquals("mime type not as",
                     protocol.defaultMimeType(),
                     protocol.mimeType());
    }

    /**
     * Tests that the default mime type for the protocol is returned if the
     * device doesn't specify a valid overriding policy value.
     */
    public void testMimeTypeDevicePolicyValueNoSlash() throws Exception {
        TestMarinerPageContext context = new TestMarinerPageContext();

        protocol.setMarinerPageContext(context);
        context.setProtocol(protocol);
        context.setDevicePolicyValue(DevicePolicyConstants.PROTOCOL_MIME_TYPE,
                                     "invalidMimeType");

        assertEquals("mime type not as",
                     protocol.defaultMimeType(),
                     protocol.mimeType());
    }

    /**
     * Tests that the device-specific mime type for the protocol is returned if
     * the device specifies a valid overriding policy value.
     */
    public void testMimeTypeDevicePolicyValueValid() throws Exception {
        TestMarinerPageContext context = new TestMarinerPageContext();

        protocol.setMarinerPageContext(context);
        context.setProtocol(protocol);
        context.setDevicePolicyValue(DevicePolicyConstants.PROTOCOL_MIME_TYPE,
                                     "valid/mimeType");

        assertEquals("mime type not as",
                     "valid/mimeType",
                     protocol.mimeType());
    }

    public void testDeviceHonoursSpacingForStylingOpenElementsWhenTrue() {
        TestMarinerPageContext context = new TestMarinerPageContext();

        protocol.setMarinerPageContext(context);
        context.setProtocol(protocol);
        context.setDevicePolicyValue(
                DevicePolicyConstants.FIX_FOR_OPEN_INLINE_STYLING_ELEMENTS,
                DevicePolicyConstants.NO_WHITESPACE_FIXING);

        boolean deviceHonoursSpacing = protocol.
                deviceHonoursSpacingForInlineStylingOpenElements();

        assertTrue("Device should honour spacing.", deviceHonoursSpacing);
    }

    public void testDeviceHonoursSpacingForStylingOpenElementsWhenFalse() {
        TestMarinerPageContext context = new TestMarinerPageContext();

        protocol.setMarinerPageContext(context);
        context.setProtocol(protocol);
        context.setDevicePolicyValue(
                DevicePolicyConstants.FIX_FOR_OPEN_INLINE_STYLING_ELEMENTS,
                DevicePolicyConstants.WHITESPACE_INSIDE);

        boolean deviceHonoursSpacing = protocol.
                deviceHonoursSpacingForInlineStylingOpenElements();

        assertFalse("Device should honour spacing.", deviceHonoursSpacing);
    }

    /**
     * Checks that the default mime type is as expected.
     */
    public void testDefaultMimeType() throws Exception {
        String expected = expectedDefaultMimeType();

        if (expected == null) {
            assertNull("default mime type not null (was " +
                       protocol.defaultMimeType() + " instead)",
                       protocol.defaultMimeType());
        } else {
            assertEquals("default mime type not as",
                         expected,
                         protocol.defaultMimeType());
        }
    }

    /**
     * This tests the creation of a menu module
     */

/* todo uncomment this once protocol tests are improved so that overriding
   todo a method in VolantisProtocol does not require that you override that
   todo method in each test version of the protocol.

    public void testGetMenuModule() throws Exception {

        // Get the menu module from the protocol
        MenuModule module = protocol.getMenuModule();

        // A null module is not allowed.
        assertNotNull("Menu module must not be null", module);
    }
*/

    /**
     * Verify that if {@link VolantisProtocol#getSpecifiedRenderMode} is called
     * when the device specifies a valid stylesheet rendering mode, then that
     * mode is used.
     */
    public void testGetSpecifiedRenderMode() {

        VolantisProtocol.StylesheetRenderMode renderMode =
                protocol.getSpecifiedRenderMode(
                        VolantisProtocol.StylesheetRenderMode.EXTERNAL,
                        VolantisProtocol.StylesheetRenderMode.INTERNAL,
                        StyleSheetConfiguration.EXTERNAL);
        assertEquals(VolantisProtocol.StylesheetRenderMode.EXTERNAL, renderMode);
    }

    /**
     * Verify that if {@link VolantisProtocol#getSpecifiedRenderMode} is called
     * when the device specifies that the default stylesheet rendering mode
     * should be used, it falls back to use the value specified by the protocol.
     */
    public void testGetSpecifiedRenderModeWhenDeviceSpecifiesDefault() {

        VolantisProtocol.StylesheetRenderMode renderMode =
                protocol.getSpecifiedRenderMode(
                        VolantisProtocol.StylesheetRenderMode.DEFAULT,
                        VolantisProtocol.StylesheetRenderMode.INTERNAL,
                        StyleSheetConfiguration.EXTERNAL);
        assertEquals(VolantisProtocol.StylesheetRenderMode.INTERNAL, renderMode);
    }

    /**
     * Verify that if {@link VolantisProtocol#getSpecifiedRenderMode} is called
     * when the device and protocol specify that the default stylesheet
     * rendering mode should be used, it falls back to use the value specified
     * in the MCS configuration file.
     */
    public void testGetSpecifiedRenderModeWhenDeviceAndProtocolSpecifyDefault() {

        VolantisProtocol.StylesheetRenderMode renderMode =
                protocol.getSpecifiedRenderMode(
                        VolantisProtocol.StylesheetRenderMode.DEFAULT,
                        VolantisProtocol.StylesheetRenderMode.DEFAULT,
                        StyleSheetConfiguration.EXTERNAL);
        assertEquals(VolantisProtocol.StylesheetRenderMode.EXTERNAL, renderMode);
    }

    /**
     * Verify that if {@link VolantisProtocol#determineActualRenderMode} is
     * called with a specified stylesheet render mode of INTERNAL and the
     * device supports internal stylesheets, the rendering mode is INTERNAL.
     */
    public void testDetermineActualRenderModeWhenInternalAndInternalSupported() {

        VolantisProtocol.StylesheetRenderMode renderMode =
                protocol.determineActualRenderMode(
                        VolantisProtocol.StylesheetRenderMode.INTERNAL,
                        false,
                        true);
        assertEquals(VolantisProtocol.StylesheetRenderMode.INTERNAL, renderMode);

        renderMode = protocol.determineActualRenderMode(
                VolantisProtocol.StylesheetRenderMode.INTERNAL,
                true,
                true);
        assertEquals(VolantisProtocol.StylesheetRenderMode.INTERNAL, renderMode);
    }

    /**
     * Verify that if {@link VolantisProtocol#determineActualRenderMode} is
     * called with a specified stylesheet render mode of INTERNAL and the
     * device does not supports internal stylesheets, the rendering mode is
     * EXTERNAL if that is supported.
     */
    public void testDetermineActualRenderModeWhenInternalAndInternalNotSupported() {

        VolantisProtocol.StylesheetRenderMode renderMode =
                protocol.determineActualRenderMode(
                        VolantisProtocol.StylesheetRenderMode.INTERNAL,
                        true,
                        false);
        assertEquals(VolantisProtocol.StylesheetRenderMode.EXTERNAL, renderMode);

        try {
            renderMode = protocol.determineActualRenderMode(
                    VolantisProtocol.StylesheetRenderMode.INTERNAL,
                    false,
                    false);
            fail("Should throw an exception because no stylesheet " +
                    "rendering options are enabled");
        } catch (IllegalStateException e) {
            // do nothing, correct behaviour.
        }
    }

    /**
     * Verify that if {@link VolantisProtocol#determineActualRenderMode} is
     * called with a specified stylesheet render mode of EXTERNAL and the
     * device supports external stylesheets, the rendering mode is EXTERNAL.
     */
    public void testDetermineActualRenderModeWhenExternalAndExternalSupported() {

        VolantisProtocol.StylesheetRenderMode renderMode =
                protocol.determineActualRenderMode(
                        VolantisProtocol.StylesheetRenderMode.EXTERNAL,
                        true,
                        false);
        assertEquals(VolantisProtocol.StylesheetRenderMode.EXTERNAL, renderMode);

        renderMode = protocol.determineActualRenderMode(
                VolantisProtocol.StylesheetRenderMode.EXTERNAL,
                true,
                true);
        assertEquals(VolantisProtocol.StylesheetRenderMode.EXTERNAL, renderMode);
    }

    /**
     * Verify that if {@link VolantisProtocol#determineActualRenderMode} is
     * called with a specified stylesheet render mode of EXTERNAL and the
     * device does not supports external stylesheets, the rendering mode is
     * INTERNAL if that is supported.
     */
    public void testDetermineActualRenderModeWhenExternalAndExternalNotSupported() {

        VolantisProtocol.StylesheetRenderMode renderMode =
                protocol.determineActualRenderMode(
                        VolantisProtocol.StylesheetRenderMode.EXTERNAL,
                        false,
                        true);
        assertEquals(VolantisProtocol.StylesheetRenderMode.INTERNAL, renderMode);

        try {
            renderMode = protocol.determineActualRenderMode(
                    VolantisProtocol.StylesheetRenderMode.EXTERNAL,
                    false,
                    false);
            fail("Should throw an exception because no stylesheet " +
                    "rendering options are enabled");
        } catch (IllegalStateException e) {
            // do nothing, correct behaviour.
        }
    }

    /**
     * Verify that {@link VolantisProtocol#storeFragmentedFormURL} resolves the
     * supplied link correctly and stores it in the session context for later
     * use.
     *
     * @param linkToResolve     URL which should be resolved and stored
     * @param resolvedLink      the URL which is expected to be stored
     * @param requestPath
     */
    public void doTestStoreFragmentedFormURL(String linkToResolve,
                                             String resolvedLink,
                                             String requestPath) {

        // Create test objects.
        final String formSpecifier = "s0";
        final String contextPathString = "/context/";
        final FormDescriptor fd = new FormDescriptor();

        MarinerPageContextMock pageContext =
                new MarinerPageContextMock("pageContext", expectations);
        EnvironmentContextMock envContext =
                new EnvironmentContextMock("envContext", expectations);
        MarinerURL contextPathURL = new MarinerURL(contextPathString);
        protocol.setMarinerPageContext(pageContext);
        MarinerURL requestURL = new MarinerURL(requestPath);
        FormDataManagerMock fdm = new FormDataManagerMock("fdm", expectations);
        SessionFormDataMock formData = new SessionFormDataMock(
                "formData", expectations, formSpecifier, fd);

        // Set expectations.
        pageContext.expects.getEnvironmentContext().returns(envContext);
        envContext.expects.getContextPathURL().returns(contextPathURL);
        if (requestPath != null) {
            pageContext.expects.getRequestURL(true).returns(requestURL);
        }
        pageContext.expects.getFormDataManager().returns(fdm);
        fdm.expects.getSessionFormData(formSpecifier).returns(formData);
        formData.expects.setFieldValue(URLConstants.ACTION_FORM_FRAGMENT,
                resolvedLink);

        // Run test.
        protocol.storeFragmentedFormURL(linkToResolve, formSpecifier);
    }

    /**
     * Verify that {@link VolantisProtocol#storeFragmentedFormURL} correctly
     * resolves simple page relative URLs and stores them in the session
     * context.
     */
    public void testStoreFragmentedFormURLWithPageRelativeFormURL() {
        // Create test objects.
        final String linkToResolve = "testPage.jsp";
        final String resolvedPath = "/abc/testPage.jsp";
        final String requestPath =
                "http://localhost:8080/context/abc/firstPage.xdime";
        doTestStoreFragmentedFormURL(linkToResolve, resolvedPath, requestPath);
    }

    /**
     * Verify that {@link VolantisProtocol#storeFragmentedFormURL} correctly
     * resolves more complex page relative URLs and stores them in the session
     * context.
     */
    public void testStoreFragmentedFormURLWithPageRelativeFormURL2() {
        final String linkToResolve = "abc/testPage.jsp";
        final String resolvedPath = "/abc/abc/testPage.jsp";
        final String requestPath =
                "http://localhost:8080/context/abc/firstPage.xdime";
        doTestStoreFragmentedFormURL(linkToResolve, resolvedPath, requestPath);
    }

    /**
     * Verify that {@link VolantisProtocol#storeFragmentedFormURL} correctly
     * resolves more complex page relative URLs and stores them in the session
     * context.
     */
    public void testStoreFragmentedFormURLWithPageRelativeFormURL3() {
        final String linkToResolve = "abc/testPage.jsp";
        final String resolvedPath = "/abc/efg/abc/testPage.jsp";
        final String requestPath =
                "http://localhost:8080/context/abc/efg/";
        doTestStoreFragmentedFormURL(linkToResolve, resolvedPath, requestPath);
    }

    /**
     * Verify that {@link VolantisProtocol#storeFragmentedFormURL} correctly
     * identifies servlet context relative URLs and stores them in the session
     * context unchanged.
     */
    public void testStoreFragmentedFormURLWithServletContextRelativeFormURL() {
        final String linkToResolve = "/abc/testPage.jsp";
        doTestStoreFragmentedFormURL(linkToResolve, linkToResolve, null);
    }

    /**
     * Verify that {@link VolantisProtocol#storeFragmentedFormURL} correctly
     * resolves app server relative URLs and stores them in the session context.
     */
    public void testStoreFragmentedFormURLWithWebappRelativeFormURL() {
        final String linkToResolve = "/context/abc/testPage.jsp";
        final String resolvedPath = "/abc/testPage.jsp";
        doTestStoreFragmentedFormURL(linkToResolve, resolvedPath, null);
    }

    /**
     * Verify that {@link VolantisProtocol#storeFragmentedFormURL} correctly
     * identifies absolute URLs and stores them in the session context
     * unchanged.
     * <p/>
     * NB: Absolute URLs are not supported as fragmented form submission URLs.
     * This is because they cannot be made relative to the servlet context and
     * so cannot be passed to {@link javax.servlet.RequestDispatcher#forward}.
     * However, because it is a valid form submission URL, and we need to be
     * backwards compatible, we just log it and continue.
     */
    public void testStoreFragmentedFormURLWithAbsoluteFormURL() {
        // Create test objects.
        final String linkToResolve = "http://www.a.com/c/t.jsp";
        doTestStoreFragmentedFormURL(linkToResolve, linkToResolve, null);
    }

    /**
     * Verify that {@link VolantisProtocol#storeFragmentedFormURL} correctly
     * identifies absolute URLs thatand stores them in the session context
     * unchanged.
     * <p/>
     * NB: Absolute URLs are not supported as fragmented form submission URLs.
     * This is because they cannot be made relative to the servlet context and
     * so cannot be passed to {@link javax.servlet.RequestDispatcher#forward}.
     * However, because it is a valid form submission URL, and we need to be
     * backwards compatible, we just log it and continue.
     */
    public void testDangerousStoreFragmentedFormURLWithAbsoluteFormURLThatCanBeMadeRelative() {
        // Create test objects.
        final String linkToResolve = "http://www.a.com/context/c/t.jsp";
        final String resolvedPath = "/c/t.jsp";
        doTestStoreFragmentedFormURL(linkToResolve, resolvedPath, null);
    }

    /**
     * Return an instance of Volantis, with the protocols
     * configuration set.  The DOMProtocol needs the configuration to
     * check whether object pooling is enabled, so all protocol test cases
     * that need Volantis, will need a protocols configuration.
     *
     * This should probably be done by a 'test framework' that configures
     * Volantis for you. That is out of scope for this VBM.
     * But as a lot of methods just use new Volantis() and
     * they all need the protocol configuration, I added this method to avoid
     * cutting and pasting.
     *
     * @return An instance of Volantis
     */
    protected Volantis getVolantis() {
        Volantis volantis = new Volantis();
        ProtocolsConfiguration config = new ProtocolsConfiguration();
        config.setWmlPreferredOutputFormat("wmlc");
        volantis.setProtocolsConfiguration(config);

        try {
            PrivateAccessor.setField(volantis, "pageURLRewriter",
                new RuntimePageURLRewriter(null, null));
        } catch (Exception e) {
            throw new UndeclaredThrowableException(e);
        }
        return volantis;
    }

    /**
     * To be overridden by all specializations where the tested protocol
     * overrides the {@link VolantisProtocol#defaultMimeType} method.
     *
     * @return the expected default mime type
     */
    protected String expectedDefaultMimeType() {
        return "";
    }

    /**
     * Override this if your protocol supports fragments and you want to check
     * the fragment link renderer context that it creates.
     *
     * @param context the created context.
     */
    protected void checkFragmentLinkRendererContext(
            FragmentLinkRendererContext context) {

    }

    /**
     * Utility method that takes the input dom string, reads it and returns
     * another string representing the dom. This method ensures a string
     * containing <xml><node></node></xml> is returned as <xml><node/></xml>
     * which makes it safer to compare expected with actual results in test
     * cases.
     *
     * @param  inputDOM a string representing a dom tree.
     * @return          a new parsed/processed dom tree which may be identical
     *                  to the original string passed in.
     */
    protected String getDOMGeneratedString(String inputDOM) throws Exception {
        XMLReader reader = DOMUtilities.getReader();
        Document actualDom = DOMUtilities.read(reader, inputDOM);
        return DOMUtilities.toString(actualDom, protocol.getCharacterEncoder());
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-05	10701/1	pduffin	VBM:2005110905 Porting forward changes from 3.5

 08-Dec-05	10675/3	pduffin	VBM:2005110905 Ported forward some white space fixes from 3.2.3

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10505/7	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (6)

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 02-Sep-05	9407/3	pduffin	VBM:2005083007 Committing resolved conflicts

 01-Sep-05	9375/5	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9363/5	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/2	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/7	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 22-Aug-05	9324/1	ianw	VBM:2005080202 Move validation for WapCSS into styling

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 12-Jul-05	9011/1	pduffin	VBM:2005071214 Refactored StyleValueFactory to change static methods to non static

 22-Jun-05	8483/3	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 09-Jun-05	8665/1	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 25-May-05	8517/2	pduffin	VBM:2005052404 Commiting changes from supermerge

 24-May-05	8123/4	ianw	VBM:2005050906 Fix merge conflicts

 11-May-05	8123/1	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 24-May-05	7762/1	doug	VBM:2005041916 Allow the MCSFilter cache to use post pipeline XDIME when calculating the cache key

 24-May-05	8462/1	philws	VBM:2005052310 Port device access key override from 3.3

 24-May-05	8430/1	philws	VBM:2005052310 Allow the device to override a protocol's access key support

 28-Feb-05	7149/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 28-Feb-05	7127/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 26-Nov-04	6076/5	tom	VBM:2004101509 Modified protocols to get their styles from MCSAttributes

 19-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 11-Oct-04	5744/1	claire	VBM:2004092801 mergevbm: Encoding of style class names for inclusions

 11-Oct-04	5742/1	claire	VBM:2004092801 Encoding of style class names for inclusions

 07-Oct-04	5729/3	claire	VBM:2004092801 Encoding of style class names for inclusions

 08-Sep-04	5449/1	claire	VBM:2004090809 New Build Mechanism: Remove the use of utilities.UndeclaredThrowableException

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 01-Jul-04	4778/6	allan	VBM:2004062912 Use the Volantis.pageURLRewriter to rewrite page urls

 01-Jul-04	4778/4	allan	VBM:2004062912 Use the Volantis.pageURLRewriter to rewrite page urls

 01-Jul-04	4775/6	claire	VBM:2004062911 Tidied testcase for caching up

 01-Jul-04	4775/3	claire	VBM:2004062911 Caching of inline stylesheets internally

 30-Jun-04	4781/3	adrianj	VBM:2002111405 Created SMS test case and added check for null/empty mime types in protocols

 30-Jun-04	4781/1	adrianj	VBM:2002111405 VolantisProtocol.defaultMimeType() and DOMProtocol made abstract

 12-May-04	4279/2	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 06-May-04	4174/1	claire	VBM:2004050501 Enhanced Menus: (X)HTML menu renderer selectors and protocol integration

 30-Apr-04	3910/2	byron	VBM:2004021117 Fixed merge conflicts

 29-Apr-04	4091/3	claire	VBM:2004042804 Enhanced menus: protocol integration and Openwave end to end

 29-Apr-04	4091/1	claire	VBM:2004042804 Enhanced menus: protocol integration and Openwave end to end

 29-Apr-04	4098/1	mat	VBM:2004042809 Made pooling of objects in the DOMProtocol configurable

 12-Feb-04	2958/2	philws	VBM:2004012715 Add protocol.content.type device policy

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 02-Oct-03	1469/1	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 25-Sep-03	1412/3	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links (sigh, rework as per dougs request)

 17-Sep-03	1412/1	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 05-Jun-03	285/3	mat	VBM:2003042911 Merged with MCS

 ===========================================================================
*/

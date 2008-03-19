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
import com.volantis.mcs.context.ResponseCachingDirectives;
import com.volantis.mcs.protocols.SelectOption;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolMock;
import com.volantis.mcs.protocols.XFSelectAttributes;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.forms.EmulatedXFormDescriptor;
import com.volantis.mcs.protocols.forms.FieldTypeMock;
import com.volantis.mcs.xdime.XDIMEAttribute;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEAttributesImpl;
import com.volantis.mcs.xdime.XDIMEContextImpl;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.xforms.model.XFormBuilder;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.shared.system.SystemClock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.List;

/**
 * Tests behaviour which is common to both single and multiple select elements.
 */
public abstract class AbstractXFSelectElementImplTestAbstract
        extends TestCaseAbstract{

    protected static SelectOption OPTION1;
    protected static SelectOption OPTION2;
    protected static final String OPTION1_VALUE = "Option one value";
    protected static final String OPTION2_VALUE = "Option two value";
    protected static final String NON_MATCHING_VALUE =
            "non matching initial value";
    protected FieldTypeMock mockFieldType;
    protected VolantisProtocolMock protocol;
    private MarinerRequestContextMock marinerRequestContextMock;
    private ResponseCachingDirectives cachingDirectives;

    // Javadoc inherited.
    public void setUp() throws Exception {
        super.setUp();

        OPTION1 = new SelectOption();
        OPTION1.setCaption(new LiteralTextAssetReference("Option one caption"));
        OPTION1.setValue(OPTION1_VALUE);

        OPTION2 = new SelectOption();
        OPTION2.setCaption(new LiteralTextAssetReference("Option two caption"));
        OPTION2.setValue(OPTION2_VALUE);
        mockFieldType = new FieldTypeMock(
            "mockFieldType", expectations);
        protocol = new VolantisProtocolMock("protocol", expectations, null);
        mockFieldType.fuzzy.doField(
                mockFactory.expectsInstanceOf(VolantisProtocol.class),
                mockFactory.expectsInstanceOf(XFSelectAttributes.class)).returns().min(0).max(1);

        marinerRequestContextMock = new MarinerRequestContextMock(
                "marinerRequestContextMock", expectations);
        final MarinerPageContextMock marinerPageContextMock =
            new MarinerPageContextMock("marinerPageContextMock", expectations);

        marinerRequestContextMock.expects.getMarinerPageContext()
                .returns(marinerPageContextMock).any();
        final EnvironmentContextMock environmentContextMock =
            new EnvironmentContextMock("environmentContextMock", expectations);
        cachingDirectives = new ResponseCachingDirectives(SystemClock.getDefaultInstance());
        cachingDirectives.enable();
        assertTrue(cachingDirectives.isEnabled());

        environmentContextMock.expects.getCachingDirectives().returns(
            cachingDirectives).any();
        marinerRequestContextMock.expects.getEnvironmentContext().returns(
            environmentContextMock).any();

        marinerPageContextMock.expects.getProtocol().returns(protocol).any();
    }

    /**
     * Verify that if a selector is declared with no options, processing does
     * not fail.
     */
    public void testWithNoSelectOptions() throws XDIMEException {

        // callCloseOnProtocol assumes that callOpenOnProtocol and
        // initialiseAttributes will have been called first, so duplicate this
        XDIMEContextInternal context = prepareForCallCloseOnProtocol();

        AbstractXFSelectElementImpl selector = getSelectElementImpl(context);
        XFSelectAttributes attributes =
                (XFSelectAttributes)selector.getProtocolAttributes();
        assertEquals(0, attributes.getOptions().size());
        assertNull(selector.initialValues);

        selector.callCloseOnProtocol(context);
    }

    /**
     * Verify that a null initial value does not cause processing to fail, and
     * that no items are selected on the {@link SelectOption} on the generated
     * protocol attributes.
     */
    public void testWithNullInitialValue() throws XDIMEException {
        List options = doTest(null);

        final int length = options.size();
        for (int i = 0; i < length; i++) {
            final SelectOption option = (SelectOption) options.get(i);
            assertFalse(option.isSelected());
        }
    }

    /**
     * Verify that if an initial value is set which does not map to any of the
     * selector item values, no {@link SelectOption} is selected on the
     * generated protocol attributes.
     */
    public void testWithNonNullButNonMatchingInitialValue() throws XDIMEException {

        final String[] unmatchedValue = new String[] {NON_MATCHING_VALUE};
        List options = doTest(unmatchedValue);

        final int length = options.size();
        for (int i = 0; i < length; i++) {
            final SelectOption option = (SelectOption) options.get(i);
            assertFalse(option.isSelected());
        }
    }

    /**
     * Verify that if an initial value is set which does map to one of the
     * selector item values, the corresponding {@link SelectOption} is selected
     * on the generated protocol attributes.
     */
    public void testWithNonNullMatchingInitialValue() throws XDIMEException {

        final String[] initialValue = new String[] {OPTION1_VALUE};
        List options = doTest(initialValue);

        assertTrue(((SelectOption)options.get(0)).isSelected());
        assertFalse(((SelectOption)options.get(1)).isSelected());
    }

    /**
     * Call {@link com.volantis.mcs.xdime.StylableXDIMEElement#callCloseOnProtocol} with the
     * given parameters and return the resulting selector element.
     *
     * @param initialValues used to initialise the selector options
     * @return {@link AbstractXFSelectElementImpl}
     * @throws XDIMEException if there was a problem running the test
     */
    protected List doTest(String[] initialValues)
            throws XDIMEException {

        // callCloseOnProtocol assumes that callOpenOnProtocol and
        // initialiseAttributes will have been called first, so duplicate this
        XDIMEContextInternal context = prepareForCallCloseOnProtocol();

        AbstractXFSelectElementImpl selector = getSelectElementImpl(context);
        assertFalse(cachingDirectives.isEnabled());
        XFSelectAttributes attributes =
                (XFSelectAttributes)selector.getProtocolAttributes();
        attributes.addOption(OPTION1);
        attributes.addOption(OPTION2);

        selector.initialValues = initialValues;
        assertEquals(initialValues, selector.initialValues);

        selector.callCloseOnProtocol(context);

        List options = ((XFSelectAttributes)selector.getProtocolAttributes()).
                getOptions();

        return options;
    }

    /**
     * Verify that the other attribute values (e.g. multiple and tag name) have
     * been set correctly for the single or multiple select element.
     *
     * @throws XDIMEException if there was a problem running the test
     */
    public void testCommonAttributes() throws XDIMEException {

        // callCloseOnProtocol assumes that callOpenOnProtocol and
        // initialiseAttributes will have been called first, so duplicate this
        XDIMEContextInternal context = prepareForCallCloseOnProtocol();

        String expectedTagName = getTagName();
        boolean expectedMultiple = getMultiple();
        AbstractXFSelectElementImpl selector = getSelectElementImpl(context);
        assertEquals(null, selector.initialValues);

        selector.callCloseOnProtocol(context);

        XFSelectAttributes pattributes =
                (XFSelectAttributes)selector.getProtocolAttributes();
        assertEquals(expectedTagName, pattributes.getTagName());
        assertEquals(expectedMultiple, pattributes.isMultiple());
    }

    /**
     * Returns the concrete instance of {@link AbstractXFSelectElementImpl}
     * that should be tested.
     *
     * @return the concrete instance of {@link AbstractXFSelectElementImpl}
     * that should be tested.
     * @param context
     */
    protected abstract AbstractXFSelectElementImpl getSelectElementImpl(
            XDIMEContextInternal context);

    /**
     * Return a boolean which should be true if the element under test is a
     * multiple selector, and false otherwise.
     *
     * @return boolean true if the element under test is a multiple selector
     * and false otherwise
     */
    protected abstract boolean getMultiple();

    /**
     * Return the tag name which should be set for the element under test.
     *
     * @return String tag name
     */
    protected abstract String getTagName();

    /**
     * callCloseOnProtocol assumes that callOpenOnProtocol and
     * initialiseAttributes will have been called first, so duplicate the
     * necessary state.
     *
     * @return an XDIMEContext that is ready for callCloseOnProtocol to be
     * called
     * @throws XDIMEException if there was a problem preparing the context
     */
    protected XDIMEContextInternal prepareForCallCloseOnProtocol()
            throws XDIMEException {

        // callCloseOnProtocol assumes that callOpenOnProtocol and
        // initialiseAttributes will have been called first, so duplicate this
        XDIMEContextInternal context = new XDIMEContextImpl();
        final XFormBuilder xFormBuilder = context.getXFormBuilder();
        final String modelID = "modelID";
        EmulatedXFormDescriptor fd = new EmulatedXFormDescriptor();
        xFormBuilder.addModel(modelID, fd);
        XDIMEAttributes attributes = new XDIMEAttributesImpl(getElementType());
        attributes.setValue("", XDIMEAttribute.ID.toString(), "controlID");
        attributes.setValue("", XDIMEAttribute.MODEL.toString(), modelID);
        xFormBuilder.registerControl(attributes);

        context.setInitialRequestContext(marinerRequestContextMock);

        return context;
    }

    /**
     * Get the type of element being tested.
     *
     * @return The type of element being tested.
     */
    protected abstract ElementType getElementType();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Oct-05	9673/5	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05	9673/3	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05	9637/1	emma	VBM:2005092807 Adding tests for XForms emulation

 ===========================================================================
*/

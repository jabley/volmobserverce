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
package com.volantis.mcs.policies.impl.io;

import com.volantis.mcs.model.impl.validation.ValidationException;
import com.volantis.mcs.policies.BaseURLPolicyBuilder;
import com.volantis.mcs.policies.CacheControlBuilder;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.VariablePolicyBuilder;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.theme.ThemeContentBuilder;
import com.volantis.mcs.policies.variants.theme.InternalThemeContentBuilder;
import com.volantis.mcs.themes.CSSStyleSheet;
import com.volantis.mcs.themes.CSSParserMode;
import com.volantis.shared.content.StringContentInput;

import java.io.IOException;
import java.util.List;

/**
 * Test that Theme trees read from supplied XML are correct.
 *
 * @todo this is not a test case for JiBXReader. Rename! This is really
 * more of a theme validation test case. Currently it includes both schema
 * and model validation, perhaps they should be split.
 */
public class JIBXReaderTestCase extends JIBXTestAbstract {

    protected void setUp() throws Exception {
        super.setUp();

        validateModel = true;
    }

    public void testReadingThemeWithTypeAndNoChildStyle()
            throws Exception {

        String name = "theme/themeWithSubjectSelectorTypeAndNoChildStyle.xml";
        String sourceXML = RESOURCE_LOADER.getResourceAsString(name);
        doRoundTrip(sourceXML, name);
    }

    public void testReadingThemeWithMarker()
            throws Exception {

        // To check vbm 2006021719
        String name = "theme/themeWithMarker.xml";
        String sourceXML = RESOURCE_LOADER.getResourceAsString(name);
        doRoundTrip(sourceXML, name);
    }

    public void testReadingThemeWithAttributeSelectorAndNoChildStyle()
            throws Exception {

        String name = "theme/themeWithAttributeSelectorAndNoChildStyle.xml";
        String sourceXML = RESOURCE_LOADER.getResourceAsString(name);
        doRoundTrip(sourceXML, name);
    }

    public void testReadingThemeWithFullyPopulatedAttributeSelector()
            throws Exception {

        String name = "theme/themeWithFullyPopulatedAttributeSelector.xml";
        String sourceXML = RESOURCE_LOADER.getResourceAsString(name);
        doRoundTrip(sourceXML, name);
    }

    public void testReadingThemeWithInvalidAttributeSelector()
            throws Exception {

        String name = "theme/themeWithInvalidAttributeSelector.xml";
        String sourceXML = RESOURCE_LOADER.getResourceAsString(name);

        try {
            unmarshallFromString(sourceXML, name);
            fail("A theme with a missing constraint value for a constraint " +
                    "which requires one should fail to unmarshall");
        } catch (ValidationException e) {
            // do nothing - correct behaviour.
        }
    }

    public void testReadingThemeWithInvalidAttributeSelector2()
            throws Exception {

        String name = "theme/themeSetAttributeSelectorWithValue.xml";
        String sourceXML = RESOURCE_LOADER.getResourceAsString(name);

        try {
            unmarshallFromString(sourceXML, name);
            fail("A theme with a constraint value for a constraint which " +
                    "doesn't require one should fail to unmarshall");
        } catch (IOException e) {
            // do nothing - correct behaviour.
        }
    }

    public void testReadingThemeWithClassAndNoChildStyle()
            throws Exception {

        String name = "theme/themeWithSubjectSelectorClassAndNoChildStyle.xml";
        String sourceXML = RESOURCE_LOADER.getResourceAsString(name);
        doRoundTrip(sourceXML, name);
    }

    public void testReadingThemeWithIDAndNoChildStyle()
            throws Exception {

        String name = "theme/themeWithSubjectSelectorIDAndNoChildStyle.xml";
        String sourceXML = RESOURCE_LOADER.getResourceAsString(name);
        doRoundTrip(sourceXML, name);
    }

    public void testReadingThemeWithUniversalAndNoChildStyle()
            throws Exception {

        String name = "theme/themeWithSubjectSelectorUniversalAndNoChildStyle.xml";
        String sourceXML = RESOURCE_LOADER.getResourceAsString(name);
        doRoundTrip(sourceXML, name);
    }

    public void testReadingThemeWithCombinedSelectorAndNoChildStyle()
            throws Exception {

        String name = "theme/themeWithCombinedSelectorAndNoChildStyle.xml";
        String sourceXML = RESOURCE_LOADER.getResourceAsString(name);
        doRoundTrip(sourceXML, name);
    }

    public void testReadingThemeWithUniversalAndFirstSecondChildStyles()
            throws Exception {

        // NOTE: file name cut down to fit in accurev 63 char limit.
        String name = "theme/themeWithSubjectSelectorUniversalAndFirstSecond.xml";
        String sourceXML = RESOURCE_LOADER.getResourceAsString(name);
        doRoundTrip(sourceXML, name);
    }

    public void testReadingThemeWithUniversalAndListChildStyles()
            throws Exception {

        // NOTE: file name cut down to fit in accurev 63 char limit.
        String name = "theme/themeWithSubjectSelectorUniversalAndList.xml";
        String sourceXML = RESOURCE_LOADER.getResourceAsString(name);
        doRoundTrip(sourceXML, name);
    }

    public void testReadingThemeMultipleDeviceThemesAndRules()
            throws Exception {

        String name = "theme/themeMultipleDeviceThemesAndRules.xml";
        String sourceXML = RESOURCE_LOADER.getResourceAsString(name);
        doRoundTrip(sourceXML, name);
    }

    public void testReadingThemeWithMultiplePseudoClassSelectorsAndNoChildStyles()
            throws Exception {

        String name = "theme/themeWithMultiplePseudoClassSelectors.xml";
        String sourceXML = RESOURCE_LOADER.getResourceAsString(name);
        doRoundTrip(sourceXML, name);
    }

    public void testReadingThemeWithInvalidPseudoClassSelectors()
            throws Exception {

        String name = "theme/themeWithInvalidPseudoClassSelectors.xml";
        String sourceXML = RESOURCE_LOADER.getResourceAsString(name);

        try {
            unmarshallFromString(sourceXML, name);
            fail("A theme with both link and visited pseudo class selectors " +
                    "defined on the same selector sequence should fail to " +
                    "unmarshall");
        } catch (ValidationException e) {
            // do nothing - correct behaviour.
        }
    }

    public void testReadingThemeWithMultiplePseudoElementSelectorsAndNoChildStyles()
            throws Exception {

        String name = "theme/themeWithMultiplePseudoElementSelectors.xml";
        String sourceXML = RESOURCE_LOADER.getResourceAsString(name);
        doRoundTrip(sourceXML, name);
    }

    public void testReadingThemeWithNthChild()
            throws Exception {

        String name = "theme/themeWithNthChild.xml";
        String sourceXML = RESOURCE_LOADER.getResourceAsString(name);
        doRoundTrip(sourceXML, name);
    }

    public void testReadingThemeWithNewStructure() throws Exception {

        String name = "theme/themeWithNewStructure.xml";
        String sourceXML = RESOURCE_LOADER.getResourceAsString(name);

        doRoundTrip(sourceXML, name);
    }

    public void testReadingThemeWithMultipleSelectorsAndNoChildStyles()
            throws Exception {

        String name = "theme/themeWithMultipleSelectorsAndNoChildStyle.xml";
        String sourceXML = RESOURCE_LOADER.getResourceAsString(name);

        doRoundTrip(sourceXML, name);
    }

    public void testStyleValues()
            throws Exception {

        String name = "theme/themeWithMultipleStyleValues.xml";
        String sourceXML = RESOURCE_LOADER.getResourceAsString(name);
        doRoundTrip(sourceXML, name);
    }

    public void testStyleValuesInvalid()
            throws Exception {

        // Turn off model validation just for this test, as we know the content
        // is invalid. The runtime and cli tools cannot handle invalid content
        // but the GUI can.
        validateModel = false;

        String name = "theme/themeWithInvalidStyleValues.xml";
        String sourceXML = RESOURCE_LOADER.getResourceAsString(name);
        doRoundTrip(sourceXML, name);
    }

    public void testBaseURLPolicy() throws Exception {

        String name = "testReadingBaseURLPolicy.xml";
        String xml = RESOURCE_LOADER.getResourceAsString(name);

        final BaseURLPolicyBuilder baseURLPolicyBuilder =
                (BaseURLPolicyBuilder) readStructure(xml, name);

        checkCacheControl(baseURLPolicyBuilder);

        assertEquals("/welcome/images/",
            baseURLPolicyBuilder.getBaseURL());
    }

    private void checkCacheControl(
            final PolicyBuilder builder) {
        CacheControlBuilder cacheControl =
                builder.getCacheControlBuilder();

        // check the values
        assertTrue(cacheControl.getCacheThisPolicy());
        assertEquals(100, cacheControl.getTimeToLive());
        assertEquals(10, cacheControl.getRetryInterval());
        assertEquals(5, cacheControl.getRetryMaxCount());
        assertFalse(cacheControl.getRetryFailedRetrieval());
        assertTrue(cacheControl.getRetainDuringRetry());
    }


    public void testReadingCSSDevicePolicies()
            throws Exception {

        String name = "testReadingCSSDevicePolicies.xml";
        String sourceXML = RESOURCE_LOADER.getResourceAsString(name);
        VariablePolicyBuilder policy = (VariablePolicyBuilder)
                doRoundTrip(sourceXML, name);
        List variants = policy.getVariantBuilders();
        CSSStyleSheet sheet;

        sheet = getCSSStyleSheet(variants, 0);
        assertEquals(null, sheet.getParserMode());

        sheet = getCSSStyleSheet(variants, 1);
        assertEquals(CSSParserMode.LAX, sheet.getParserMode());

        sheet = getCSSStyleSheet(variants, 2);
        assertEquals(CSSParserMode.STRICT, sheet.getParserMode());
    }


    private CSSStyleSheet getCSSStyleSheet(List variants, final int index) {
        VariantBuilder variant = (VariantBuilder) variants.get(index);
        InternalThemeContentBuilder content = (InternalThemeContentBuilder)
                variant.getContentBuilder();
        CSSStyleSheet sheet = (CSSStyleSheet) content.getStyleSheet();
        return sheet;
    }

    public void testReadingFormFragmentElements() throws Exception {
        String name = "theme/themeWithFormFragmentElements.xml";
        String sourceXML = RESOURCE_LOADER.getResourceAsString(name);
        doRoundTrip(sourceXML, name);
    }

    private Object readStructure(final String sourceXML, String name)
            throws IOException {

        StringContentInput content = new StringContentInput(sourceXML);
        return FACTORY.createPolicyReader().read(content, name);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/2	emma	VBM:2005111705 Interim commit

 09-Dec-05	10756/1	geoff	VBM:2005120813 JiBX is reading XML using system default encoding

 09-Dec-05	10738/1	geoff	VBM:2005120813 JiBX is reading XML using system default encoding

 16-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.
 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 31-Oct-05	9961/1	pduffin	VBM:2005101811 Committing restructuring

 11-Oct-05	9729/4	geoff	VBM:2005100507 Mariner Export fails with NPE

 03-Oct-05	9500/6	ianw	VBM:2005091308 Rationalise RPDM and LPDM

 30-Sep-05	9500/4	ianw	VBM:2005091308 Interim commit for George

 29-Sep-05	9500/1	ianw	VBM:2005091308 Interim commit for Ian B

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 28-Sep-05	9445/16	gkoch	VBM:2005090603 Introduced ComponentContainers to bring components and their assets together

 16-Sep-05	9512/3	pduffin	VBM:2005091408 Added support for invalid style values

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 12-Sep-05	9372/1	ianw	VBM:2005082221 Allow only one instance of MarinerPageContext for a page

 09-Sep-05	9479/1	ianw	VBM:2005090816 implement new JIBX bindings

 02-Sep-05	9408/1	pabbott	VBM:2005083007 Move over to using JiBX accessor

 29-Jun-05	8552/6	pabbott	VBM:2005051902 JIBX Theme accessors


 ===========================================================================
*/

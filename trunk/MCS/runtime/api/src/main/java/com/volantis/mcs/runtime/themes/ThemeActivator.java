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
 * $Header: /src/voyager/com/volantis/mcs/runtime/themes/DeviceThemeActivator.java,v 1.2 2002/11/11 16:28:52 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 07-Nov-02    Phil W-S        VBM:2002102306 - Created. Allows post-load
 *                              initialization of DeviceTheme instances in
 *                              the runtime application configuration.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.themes;

import com.volantis.mcs.css.parser.CSSParserFactory;
import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.policies.InternalVariablePolicyBuilder;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.VariablePolicyBuilder;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.theme.InternalThemeContent;
import com.volantis.mcs.policies.variants.theme.InternalThemeContentBuilder;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.policies.AbstractConcretePolicyActivator;
import com.volantis.mcs.runtime.policies.ActivatedPolicy;
import com.volantis.mcs.runtime.policies.ActivatedVariablePolicy;
import com.volantis.mcs.runtime.policies.ActivatedVariablePolicyImpl;
import com.volantis.mcs.runtime.policies.FixedContentBuilder;
import com.volantis.mcs.runtime.policies.PolicyReferenceFactory;
import com.volantis.mcs.runtime.policies.theme.ActivatedThemeContent;
import com.volantis.mcs.runtime.policies.theme.StyleSheetActivator;
import com.volantis.mcs.runtime.policies.theme.StyleSheetActivatorImpl;
import com.volantis.mcs.themes.BaseStyleSheet;
import com.volantis.mcs.themes.CSSStyleSheet;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.CSSParserMode;
import com.volantis.mcs.utilities.MarinerURL;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

/**
 * Activator for {@link InternalThemeContent}.
 */
public class ThemeActivator
        extends AbstractConcretePolicyActivator {

    /**
     * Initialise.
     *
     * @param referenceFactory The reference factory.
     */
    public ThemeActivator(PolicyReferenceFactory referenceFactory) {
        super(referenceFactory);
    }


    // Javadoc inherited.
    protected ActivatedPolicy activateImpl(
            RuntimeProject actualProject, PolicyBuilder policyBuilder,
            RuntimeProject logicalProject) {

        VariablePolicyBuilder variablePolicyBuilder =
                (VariablePolicyBuilder) policyBuilder;

        // Ensure we prune any invalid variants before we start activating.
        // We must do it now as the activation process will trigger lots
        // of redundant(!) validation which will fail otherwise.
        // TODO: avoid rendundant validation and consequent...
        // TODO: avoid redundant logging of warnings
        InternalVariablePolicyBuilder internalVariablePolicyBuilder =
                (InternalVariablePolicyBuilder) variablePolicyBuilder;
        internalVariablePolicyBuilder.validateAndPrune();
        MarinerURL baseURL = getBaseURL(actualProject, policyBuilder,
                logicalProject);

        activateAlternateReferences(logicalProject, variablePolicyBuilder,
                baseURL);

        StyleSheetActivator styleSheetActivator =
                new StyleSheetActivatorImpl(logicalProject, baseURL);

        List variantBuilders = variablePolicyBuilder.getVariantBuilders();
        for (int i = 0; i < variantBuilders.size(); i++) {
            VariantBuilder variantBuilder = (VariantBuilder)
                    variantBuilders.get(i);

            InternalThemeContentBuilder themeContentBuilder =
                    (InternalThemeContentBuilder)
                    variantBuilder.getContentBuilder();
            InternalThemeContent theme =
                    themeContentBuilder.getInternalThemeContent();

            ActivatedThemeContent activated = activateThemeContent(
                    styleSheetActivator, theme);
            variantBuilder.setContentBuilder(
                    new FixedContentBuilder(activated));
        }

        // Convert the builder back into a policy. This will trigger another
        // full validation of the policy with consequent redundant logging.
        ActivatedVariablePolicy activatedVariablePolicy =
                new ActivatedVariablePolicyImpl(
                        variablePolicyBuilder.getVariablePolicy(),
                        actualProject, logicalProject);

        return activatedVariablePolicy;
    }

    /**
     * Activate the theme content.
     *
     * @param styleSheetActivator The activator for a {@link StyleSheet}.
     * @param theme               The theme being activated.
     * @return The activated theme content.
     */
    private ActivatedThemeContent activateThemeContent(
            StyleSheetActivator styleSheetActivator,
            InternalThemeContent theme) {

        BaseStyleSheet baseStyleSheet = theme.getStyleSheet();

        StyleSheet styleSheet;
        if (baseStyleSheet instanceof StyleSheet) {

            // Compile the style sheet.
            styleSheet = (StyleSheet) baseStyleSheet;

        } else if (baseStyleSheet instanceof CSSStyleSheet) {

            CSSStyleSheet cssStyleSheet = (CSSStyleSheet) baseStyleSheet;
            CSSParserMode parserMode = cssStyleSheet.getParserMode();
            String css = cssStyleSheet.getCSS();

            // Prefix sufficient blank lines and white space to the front of
            // the CSS to ensure that any line and column numbers that the
            // parser reports are correct relative to the source XML.
            String url = cssStyleSheet.getSourceDocumentName();
            int line = cssStyleSheet.getSourceLineNumber();
            int column = cssStyleSheet.getSourceColumnNumber();
            StringBuffer buffer = new StringBuffer(
                    css.length() + line + column);

            for (int i = 1; i < line; i += 1) {
                buffer.append('\n');
            }
            for (int i = 1; i < column; i += 1) {
                buffer.append(' ');
            }
            buffer.append(css);

            Reader reader = new StringReader(css);

            CSSParserFactory parserFactory =
                    CSSParserFactory.getDefaultInstance();
            CSSParser parser = parserFactory.createParser(parserMode);

            styleSheet = parser.parseStyleSheet(reader, url);

        } else {
            throw new IllegalStateException(
                    "Unknown style sheet " + baseStyleSheet);
        }

        styleSheetActivator.activate(styleSheet);

        return new ActivatedThemeContent(styleSheet, theme.getImportParent());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10641/1	geoff	VBM:2005113024 Pagination page rendering issues

 06-Dec-05	10621/1	geoff	VBM:2005113024 Pagination page rendering issues

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Fixed some issues with layout and theme selection policies not caching properly

 30-Sep-05	9500/3	ianw	VBM:2005091308 Interim commit for George

 29-Sep-05	9660/1	ibush	VBM:2005091308 Activate CSSDeviceThemes

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 02-Sep-05	9408/4	pabbott	VBM:2005083007 Move over to using JiBX accessor

 01-Sep-05	9407/1	pduffin	VBM:2005083007 Changed MIB2_1 and Netfront3 configuration to remove device specific theme, and replaced it with a new initial value finder that is device aware

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 18-Jul-05	9029/2	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 11-Jul-05	8862/2	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 11-May-05	8123/2	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 04-Mar-05	7271/1	geoff	VBM:2005011202 log4j configuration extremely slow

 03-Mar-05	7250/1	geoff	VBM:2005011202 log4j configuration extremely slow

 28-Feb-05	7149/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 28-Feb-05	7127/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 11-Nov-04	6094/1	tom	VBM:2004101510 Created CSSEmulator based StyleEngine

 19-Oct-04	5818/1	tom	VBM:2004082510 added rule merging as part of DeviceTheme normalization

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 12-Aug-04	5111/7	doug	VBM:2004080405 Added Theme overlay support

 12-Aug-04	5111/4	doug	VBM:2004080405 Added Theme overlay support

 23-Mar-04	3555/1	allan	VBM:2004032205 Patch performance fixes from MCS 3.0GA

 23-Mar-04	3512/5	allan	VBM:2004032205 MCS performance enhancements.

 22-Mar-04	3512/3	allan	VBM:2004032205 MCS performance enhancements.

 22-Mar-04	3512/1	allan	VBM:2004032205 MCS performance enhancements.

 ===========================================================================
*/

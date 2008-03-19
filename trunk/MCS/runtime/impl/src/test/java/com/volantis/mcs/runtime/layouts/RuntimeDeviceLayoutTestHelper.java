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

package com.volantis.mcs.runtime.layouts;

import com.volantis.mcs.layouts.Layout;
import com.volantis.mcs.policies.InternalPolicyFactory;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.VariablePolicy;
import com.volantis.mcs.policies.VariablePolicyBuilder;
import com.volantis.mcs.policies.VariablePolicyType;
import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.content.Content;
import com.volantis.mcs.policies.variants.content.ContentBuilder;
import com.volantis.mcs.policies.variants.layout.InternalLayoutContentBuilder;
import com.volantis.mcs.policies.variants.theme.InternalThemeContentBuilder;
import com.volantis.mcs.runtime.RuntimeProjectMock;
import com.volantis.mcs.runtime.policies.PolicyActivator;
import com.volantis.mcs.runtime.policies.PolicyReferenceFactory;
import com.volantis.mcs.runtime.policies.PolicyReferenceFactoryImpl;
import com.volantis.mcs.runtime.policies.cache.CacheControlConstraints;
import com.volantis.mcs.runtime.policies.cache.CacheControlConstraintsMap;
import com.volantis.mcs.runtime.policies.cache.SingleCacheControlConstraintsMap;
import com.volantis.mcs.runtime.policies.theme.ActivatedThemeContent;
import com.volantis.mcs.runtime.policies.theme.RuntimeDeviceTheme;
import com.volantis.mcs.runtime.policies.theme.RuntimeThemeAdapter;
import com.volantis.mcs.runtime.project.ProjectManagerMock;
import com.volantis.mcs.runtime.themes.ThemeActivator;
import com.volantis.mcs.runtime.themes.ThemeStyleSheetCompilerFactory;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.styling.compiler.StyleSheetCompiler;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.testtools.mock.MockFactory;

/**
 * Provides support for testing using runtime device layouts.
 */
public class RuntimeDeviceLayoutTestHelper {

    private static InternalPolicyFactory policyFactory =
            (InternalPolicyFactory) PolicyFactory.getDefaultInstance();

    /**
     * Activate the device layout for use within the runtime.
     *
     * @param layout The device layout to activate.
     *
     * @return The runtime device layout.
     */
    public static RuntimeDeviceLayout activate(Layout layout) {

        InternalLayoutContentBuilder layoutContent =
                policyFactory.createLayoutContentBuilder();
        layoutContent.setLayout(layout);

        PolicyReferenceFactory referenceFactory = createReferenceFactory();

        LayoutActivator layoutActivator = new LayoutActivator(referenceFactory);

        Content activatedContent = activateContent(layoutContent,
                layoutActivator, "policy.mlyt", PolicyType.LAYOUT,
                VariantType.LAYOUT);

        ActivatedLayoutContent activatedLayoutContent =
                (ActivatedLayoutContent) activatedContent;

        return new RuntimeLayoutAdapter("Layout",
                activatedLayoutContent.getLayout(),
                activatedLayoutContent.getCompiledStyleSheet(),
                activatedLayoutContent.getContainerNameToFragments());
    }

    private static Content activateContent(
            ContentBuilder contentBuilder,
            PolicyActivator activator,
            final String name,
            final VariablePolicyType policyType,
            final VariantType variantType) {

        CacheControlConstraints constraints =
                CacheControlConstraints.getDefaultConstraints();
        CacheControlConstraintsMap constraintsMap =
                new SingleCacheControlConstraintsMap(constraints);

        MockFactory factory = MockFactory.getDefaultInstance();
        ExpectationBuilder localExpectations = factory.createUnorderedBuilder();

        RuntimeProjectMock projectMock = new RuntimeProjectMock("test",
                localExpectations);

        projectMock.expects.getCacheControlConstraintsMap()
                .returns(constraintsMap);

        VariablePolicyBuilder policyBuilder =
                policyFactory.createVariablePolicyBuilder(policyType);

        policyBuilder.setName(name);

        VariantBuilder variantBuilder =
                policyFactory.createVariantBuilder(variantType);

        variantBuilder.setSelectionBuilder(
                policyFactory.createDefaultSelectionBuilder());

        variantBuilder.setContentBuilder(contentBuilder);

        policyBuilder.addVariantBuilder(variantBuilder);

        // Activate the policyBuilder.
        VariablePolicy policy = (VariablePolicy) activator.activate(
                projectMock, policyBuilder, projectMock);

        // Get the activated variantBuilder.
        Variant activatedVariant = (Variant) policy.getVariants().get(0);

        return activatedVariant.getContent();
    }

    public static RuntimeDeviceTheme activate(StyleSheet styleSheet) {

        InternalThemeContentBuilder themeContent =
                policyFactory.createThemeContentBuilder();
        themeContent.setStyleSheet(styleSheet);

        PolicyReferenceFactory referenceFactory = createReferenceFactory();

        ThemeActivator themeActivator = new ThemeActivator(referenceFactory);

        // Get the activated variant.
        Content activatedContent = activateContent(themeContent, themeActivator,
                "policy.mthm", PolicyType.THEME, VariantType.THEME);

        // Get the content.
        ActivatedThemeContent activatedThemeContent =
                (ActivatedThemeContent) activatedContent;

        StyleSheetCompiler styleSheetCompiler = ThemeStyleSheetCompilerFactory.
                getDefaultInstance().createStyleSheetCompiler();

        CompiledStyleSheet compiledStyleSheet =
                styleSheetCompiler.compileStyleSheet(
                        activatedThemeContent.getStyleSheet());

        return new RuntimeThemeAdapter(null, compiledStyleSheet);
    }

    private static PolicyReferenceFactory createReferenceFactory() {
        final ProjectManagerMock projectManagerMock =
                new ProjectManagerMock("projectManagerMock", null);

        PolicyReferenceFactory referenceFactory =
                new PolicyReferenceFactoryImpl(projectManagerMock);
        return referenceFactory;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Nov-05	9789/4	emma	VBM:2005101113 Supermerge: Migrate JDBC Accessors to use chunked accessors

 23-Oct-05	9789/1	emma	VBM:2005101113 Migrate JDBC Accessors to chunked accessors

 27-Oct-05	9986/1	geoff	VBM:2005102512 MCS35: Investigate and fix any JDBC repository import/export problems

 13-Oct-05	9727/1	ianw	VBM:2005100506 Fixed remote repository issues

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 ===========================================================================
*/

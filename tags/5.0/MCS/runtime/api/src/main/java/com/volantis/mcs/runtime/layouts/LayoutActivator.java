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

import com.volantis.mcs.layouts.DeviceLayoutUpdater;
import com.volantis.mcs.policies.InternalVariablePolicyBuilder;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.VariablePolicyBuilder;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.layout.InternalLayoutContent;
import com.volantis.mcs.policies.variants.layout.InternalLayoutContentBuilder;
import com.volantis.mcs.policies.variants.layout.LayoutContentBuilder;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.layouts.styling.LayoutStyleSheetBuilderImpl;
import com.volantis.mcs.runtime.layouts.styling.LayoutStyleSheetCompilerFactory;
import com.volantis.mcs.runtime.policies.AbstractConcretePolicyActivator;
import com.volantis.mcs.runtime.policies.ActivatedPolicy;
import com.volantis.mcs.runtime.policies.ActivatedVariablePolicy;
import com.volantis.mcs.runtime.policies.ActivatedVariablePolicyImpl;
import com.volantis.mcs.runtime.policies.FixedContentBuilder;
import com.volantis.mcs.runtime.policies.PolicyReferenceFactory;
import com.volantis.mcs.runtime.policies.theme.StyleSheetActivator;
import com.volantis.mcs.runtime.policies.theme.StyleSheetActivatorImpl;
import com.volantis.mcs.utilities.MarinerURL;

import java.util.List;

/**
 * Activator for {@link LayoutContentBuilder}.
 *
 * <p>Activating a layout involves:</p>
 * <ul>
 * <li>Activating any alternate references.</li>
 * <li>Creating a style sheet that contains the stylistic information from the
 * layout.</li>
 * <li>Activating any policy references within the style sheet.</li>
 * <li>Activating any other policy references within the layout.</li>
 * <li>Performing some format specific activation to convert the information
 * into a more useful runtime version.</li>
 * </ul>
 *
 */
public class LayoutActivator
        extends AbstractConcretePolicyActivator {

    /**
     * The activator for the {@link LayoutContentBuilder}.
     */
    private final LayoutContentActivator contentActivator;

    /**
     * Initialise.
     *
     * @param referenceFactory The reference factory.
     */
    public LayoutActivator(PolicyReferenceFactory referenceFactory) {
        this(new LayoutContentActivator(new LayoutStyleSheetBuilderImpl(),
                LayoutStyleSheetCompilerFactory.getDefaultInstance(),
                new DeviceLayoutUpdater()),
                referenceFactory);
    }

    /**
     * Initialise.
     *
     * <p>Provided for testing.</p>
     *
     * @param referenceFactory The reference factory.
     * @param contentActivator The object that will activate the layout content.
     */
    public LayoutActivator(
            final LayoutContentActivator contentActivator,
            PolicyReferenceFactory referenceFactory) {
        super(referenceFactory);

        this.contentActivator = contentActivator;
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

            InternalLayoutContentBuilder layoutContentBuilder =
                    (InternalLayoutContentBuilder)
                    variantBuilder.getContentBuilder();
            // Convert the content builder back into a content. This will
            // trigger a partial validation of the policy with consequent
            // redundant logging.
            InternalLayoutContent layout = layoutContentBuilder
                    .getInternalLayoutContent();

            final ActivatedLayoutContent activated =
                    contentActivator.activateLayoutContent(
                            styleSheetActivator, layout);
            variantBuilder.setContentBuilder(new FixedContentBuilder(
                    activated));
        }

        // Convert the builder back into a policy. This will trigger another
        // full validation with consequent redundant logging.
        ActivatedVariablePolicy activatedVariablePolicy =
                new ActivatedVariablePolicyImpl(
                        variablePolicyBuilder.getVariablePolicy(),
                        actualProject, logicalProject);

        return activatedVariablePolicy;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Fixed some issues with layout and theme selection policies not caching properly

 27-Oct-05	9986/1	geoff	VBM:2005102512 MCS35: Investigate and fix any JDBC repository import/export problems

 ===========================================================================
*/

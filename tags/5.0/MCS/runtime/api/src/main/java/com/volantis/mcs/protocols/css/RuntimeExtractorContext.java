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

package com.volantis.mcs.protocols.css;

import com.volantis.mcs.context.TranscodableUrlResolver;
import com.volantis.mcs.dom2theme.AssetResolver;
import com.volantis.mcs.dom2theme.ExtractorContext;
import com.volantis.mcs.expression.PolicyExpression;
import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.policies.ActivatedVariablePolicy;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.runtime.policies.SelectedVariant;
import com.volantis.shared.throwable.ExtendedRuntimeException;

public class RuntimeExtractorContext
        implements ExtractorContext, AssetResolver {

    private final PolicyReferenceResolver resolver;
    private final com.volantis.mcs.protocols.assets.implementation.AssetResolver assetResolver;
    private final TranscodableUrlResolver transcodableUrlResolver;

    /**
     * Indicates whether type rules are to be generated. 
     */
    private boolean generateTypeRules;

    public RuntimeExtractorContext(
            final PolicyReferenceResolver policyReferenceResolver,
            final com.volantis.mcs.protocols.assets.implementation.AssetResolver assetResolver,
            final TranscodableUrlResolver transcodableUrlResolver) {
        resolver = policyReferenceResolver;
        this.assetResolver = assetResolver;
        this.transcodableUrlResolver = transcodableUrlResolver;

        // By default, type rules should be generated.
        this.generateTypeRules = true;
    }

    public AssetResolver getAssetResolver() {
        return this;
    }

    // javadoc inherited
    public PolicyReference evaluateExpression(PolicyExpression expression) {
        return resolver.resolvePolicyExpression(expression);
    }

    // javadoc inherited
    public String resolveImage(PolicyReference reference) {
        RuntimePolicyReference runtimeReference =
                (RuntimePolicyReference) reference;

        String url = assetResolver.retrieveVariantURLAsString(runtimeReference, null);
        return assetResolver.rewriteURLWithPageURLRewriter(url, PageURLType.IMAGE);
    }

    // javadoc inherited
    public String resolveTranscodableImage(final String transcodableUrl) {
        try {
            return transcodableUrlResolver.resolve(transcodableUrl);
        } catch (RepositoryException e) {
            throw new ExtendedRuntimeException(e);
        }
    }

    // javadoc inherited
    public String resolveVideo(PolicyReference reference) {
        SelectedVariant selected = retrieveBestDynamicVisualAsset(
                (RuntimePolicyReference) reference);
        return assetResolver.computeURLAsString(selected);
    }

    // Javadoc inherited.
    public String resolveText(
            PolicyReference reference, EncodingCollection requiredEncodings) {
        SelectedVariant variant = assetResolver.selectBestVariant(
                (RuntimePolicyReference) reference,
                requiredEncodings);
        return assetResolver.getContentsFromVariant(variant);
    }

    private SelectedVariant retrieveBestDynamicVisualAsset(
            final RuntimePolicyReference reference) {

        SelectedVariant selected = assetResolver.selectBestVariant(reference, null);
        if (selected == null) {
            return null;
        } else if (selected.getVariant() != null) {
            return selected;
        }

        ActivatedVariablePolicy policy = selected.getPolicy();
        RuntimePolicyReference fallback = (RuntimePolicyReference)
                policy.getAlternatePolicy(PolicyType.IMAGE);
        if (fallback != null) {
            selected = assetResolver.selectBestVariant(fallback, null);
            if (selected == null) {
                return null;
            } else if (selected.getVariant() != null) {
                return selected;
            }
        }

        // No suitable asset could be found for the background dynamic visual.
        return null;
    }

    // Javadoc inherited
    public boolean generateTypeRules() {
        return generateTypeRules;
    }

    /**
     * Enables or disables generation of type rules.
     * 
     * @param generate The enabled flag.
     */
    public void setGenerateTypeRules(boolean generate) {
        this.generateTypeRules = generate;
    }
}

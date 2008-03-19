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

package com.volantis.mcs.protocols.styles;

import com.volantis.mcs.context.TranscodableUrlResolver;
import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.protocols.assets.implementation.AssetResolver;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.themes.StyleComponentURI;
import com.volantis.mcs.themes.StyleTranscodableURI;
import com.volantis.mcs.themes.StyleURI;
import com.volantis.shared.throwable.ExtendedRuntimeException;

public class BackgroundImageHandler
    extends ValueRendererChecker {

    private final PolicyReferenceResolver policyReferenceResolver;

    private final AssetResolver assetResolver;
    private final TranscodableUrlResolver transcodableUrlResolver;

    public BackgroundImageHandler(
        PolicyReferenceResolver policyReferenceResolver,
        AssetResolver assetResolver, TranscodableUrlResolver transcodableUrlResolver) {
        this.policyReferenceResolver = policyReferenceResolver;
        this.assetResolver = assetResolver;
        this.transcodableUrlResolver = transcodableUrlResolver;
    }

    // javadoc inherited
    public void visit(StyleComponentURI value, Object object) {

        RuntimePolicyReference reference =
                policyReferenceResolver.resolvePolicyExpression(
                        value.getExpression());

        string = assetResolver.retrieveVariantURLAsString(reference, null);
        string = assetResolver.rewriteURLWithPageURLRewriter(string, PageURLType.IMAGE);
    }

    // javadoc inherited
    public void visit(final StyleTranscodableURI value, final Object object) {
        try {
            string = transcodableUrlResolver.resolve(value.getUri());
        } catch (RepositoryException e) {
            throw new ExtendedRuntimeException(e);
        }
    }

    // javadoc inherited
    public void visit(StyleURI value, Object object) {
        string = value.getURI();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/

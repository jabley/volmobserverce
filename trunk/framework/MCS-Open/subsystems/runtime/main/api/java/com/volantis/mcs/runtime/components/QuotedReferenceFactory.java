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

package com.volantis.mcs.runtime.components;

import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.protocols.assets.AssetReference;
import com.volantis.mcs.protocols.assets.implementation.AssetResolver;
import com.volantis.mcs.protocols.assets.implementation.DefaultComponentLinkAssetReference;
import com.volantis.mcs.protocols.assets.implementation.DefaultComponentScriptAssetReference;
import com.volantis.mcs.protocols.assets.implementation.DefaultComponentTextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralLinkAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralScriptAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;

/**
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface QuotedReferenceFactory {

    public static final QuotedReferenceFactory LINK_REFERENCE_FACTORY =
            new QuotedReferenceFactory() {
                public AssetReference createExpressionReference(
                        RuntimePolicyReference reference, AssetResolver resolver,
                        PageURLType urlType) {

                    return new DefaultComponentLinkAssetReference(reference,
                            resolver, urlType);
                }

                public AssetReference createLiteralReference(String literal) {
                    return new LiteralLinkAssetReference(literal);
                }

                public PolicyType getPolicyType() {
                    return PolicyType.LINK;
                }
            };

    public static final QuotedReferenceFactory SCRIPT_REFERENCE_FACTORY =
            new QuotedReferenceFactory() {
                public AssetReference createExpressionReference(
                        RuntimePolicyReference reference, AssetResolver resolver,
                        PageURLType urlType) {

                    return new DefaultComponentScriptAssetReference(reference,
                            resolver);
                }

                public AssetReference createLiteralReference(String literal) {
                    return new LiteralScriptAssetReference(literal);
                }

                public PolicyType getPolicyType() {
                    return PolicyType.SCRIPT;
                }
            };

    public static final QuotedReferenceFactory TEXT_REFERENCE_FACTORY =
            new QuotedReferenceFactory() {
                public AssetReference createExpressionReference(
                        RuntimePolicyReference reference, AssetResolver resolver,
                        PageURLType urlType) {

                    return new DefaultComponentTextAssetReference(reference,
                            resolver);
                }

                public AssetReference createLiteralReference(String literal) {
                    return new LiteralTextAssetReference(literal);
                }

                public PolicyType getPolicyType() {
                    return PolicyType.TEXT;
                }
            };

    AssetReference createExpressionReference(
            RuntimePolicyReference reference,
            AssetResolver resolver,
            PageURLType urlType);

    AssetReference createLiteralReference(String literal);

    PolicyType getPolicyType();
}

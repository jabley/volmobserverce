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

package com.volantis.mcs.dom2theme;

import com.volantis.mcs.expression.PolicyExpression;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;

/**
 * Yet another asset resolver.
 *
 * <p>We really need to rationalise them all but at the moment they all have
 * slightly different usages and dependencies so for now we cannot. However,
 * hopefully the restructuring of the components / layouts / themes / assets
 * into consistent policies should allow this to happen eventually.</p>
 *
 * @mock.generate 
 */
public interface AssetResolver {

    /**
     * Resolve the named image referenced from within the specified
     * project to a URL.
     *
     * @param reference The reference to the policy.
     * @return The string to the variant asset URL.
     */
    String resolveImage(PolicyReference reference);

    /**
     * Resolves a transcodable url.
     *
     * @param transcodableUrl the url to resolve
     * @return the resolved url
     */
    String resolveTranscodableImage(String transcodableUrl);

    /**
     * Resolve the named video referenced from within the specified
     * project to a URL.
     *
     * @param reference The reference to the policy.
     * @return The string to the variant asset URL.
     */
    String resolveVideo(PolicyReference reference);

    /**
     * Resolve the named text policy referenced from within the specified
     * project to its string contents..
     *
     * @param reference The reference to the policy.
     * @param requiredEncodings
     * @return The string to the variant asset URL.
     */
    String resolveText(PolicyReference reference, EncodingCollection requiredEncodings);

    PolicyReference evaluateExpression(PolicyExpression expression);
}

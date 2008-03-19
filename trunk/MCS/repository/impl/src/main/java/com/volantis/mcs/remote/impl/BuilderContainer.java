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


package com.volantis.mcs.remote.impl;

import com.volantis.mcs.policies.PolicyBuilder;

/**
 * The RPDM container for a Policy (Component/Layout or Theme).
 */
public class BuilderContainer {

    /**
     * The URL by which this Policy can be accessed.
     */
    private String url;

    /**
     * The Policy.
     */
    private PolicyBuilder policyBuilder;

    /**
     * Protect our consdtructer
     */
    BuilderContainer() {
    }

    /**
     * Create a new PolicyBuilderContainer instance.#
     *
     * @param url           The URL that points to this Policy.
     * @param policyBuilder The policy builder.
     */
    public BuilderContainer(String url, PolicyBuilder policyBuilder) {
        this.url = url;
        this.policyBuilder = policyBuilder;
    }

    /**
     * Get the URL associated with this policy as a string.
     *
     * @return The URL of the policy.
     */
    public String getUrl() {
        return url;
    }

    public PolicyBuilder getPolicyBuilder() {
        return policyBuilder;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9500/3	ianw	VBM:2005091308 Rationalise RPDM and LPDM

 29-Sep-05	9500/1	ianw	VBM:2005091308 Interim commit for Ian B

 ===========================================================================
*/

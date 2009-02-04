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
 * $Header: /src/voyager/com/volantis/mcs/imdapi/AbstractIMDAPIElement.java,v 1.2 2003/03/24 16:35:26 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 *
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Mar-02    Mat            VBM:2002022009 - Created
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.imdapi;

import com.volantis.mcs.accessors.xml.jibx.ComponentContainer;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.compatibility.Old2NewConverter;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.repository.imd.IMDPolicyFetcher;


/**
 * The abstract class from which all IMDAPI elements derive.
 */
public abstract class AbstractIMDAPIElement {

    private Old2NewConverter converter = new Old2NewConverter();

    void addPolicy(MarinerRequestContext mrc, ComponentContainer container)
            throws RepositoryException {

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(mrc);

        PolicyBuilder policyBuilder =
                converter.oldContainer2NewPolicyBuilder(container);

        IMDPolicyFetcher fetcher = pageContext.getPagePolicyFetcher();
        fetcher.addInlinePolicyBuilder(policyBuilder);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 ===========================================================================
*/

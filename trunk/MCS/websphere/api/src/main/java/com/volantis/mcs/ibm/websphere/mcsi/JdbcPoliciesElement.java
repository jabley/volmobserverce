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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.ibm.websphere.mcsi;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.project.InternalProjectFactory;
import com.volantis.mcs.project.jdbc.JDBCPolicySource;
import com.volantis.mcs.repository.LocalRepository;

/**
 * The JdbcPoliciesElement MCSIElement
 */
public class JdbcPoliciesElement extends AbstractPortletContextChildElement {

    /**
     * The name of the element in an array for use in error messages.
     */
    private static final Object[] elementName = new Object[] {"jdbc-policies"};

    private JDBCPolicySource policySource;

    /*
     * Parent PortletContextElement
     */
    private PortletContextElement parent;

    // Javadoc inherited from MCSIElement interface
    public int elementStart(MarinerRequestContext context,
                            PAPIAttributes mcsiAttributes)
            throws PAPIException {

        JdbcPoliciesAttributes attrs = (JdbcPoliciesAttributes) mcsiAttributes;
        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(context);

        LocalRepository jdbcRepository =
                pageContext.getVolantisBean().getJDBCRepository();

        InternalProjectFactory factory =
                InternalProjectFactory.getInternalInstance();

        policySource = factory.createJDBCPolicySource(jdbcRepository,
                attrs.getName());

        parent = findParent(pageContext, elementName);

        pageContext.pushMCSIElement(this);

        return PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited from MCSIElement interface
    public int elementEnd(MarinerRequestContext context,
                          PAPIAttributes mcsiAttributes)
            throws PAPIException {
        if (parent != null) {
            parent.setPolicySource(policySource);
            // only pop ourselves of the stack if there was a parent.  if there
            // wasn't we would not have pushed ourselves onto the stack.
            MarinerPageContext pageContext =
                    ContextInternals.getMarinerPageContext(context);
            pageContext.popMCSIElement();
        }

        return CONTINUE_PROCESSING;
    }


    // Javadoc inherited from MCSIElement interface
    public void elementReset(MarinerRequestContext context) {
        parent = null;
        policySource = null;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 07-Dec-04	5800/4	ianw	VBM:2004090605 New Build system

 29-Oct-04	6027/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 28-Oct-04	5897/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/1	tony	VBM:2004012601 update localisation services

 06-Feb-04	2828/3	ianw	VBM:2004011922 corrected logging issues

 04-Feb-04	2828/1	ianw	VBM:2004011922 Added MCSI content handler

 ===========================================================================
*/

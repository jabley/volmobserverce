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
package com.volantis.mcs.context;

import com.volantis.mcs.project.Project;
import com.volantis.mcs.repository.RepositoryException;

import java.io.IOException;
import java.util.Stack;

/**
 * Test Mariner Request Context.
 */
public class RuntimeTestMarinerRequestContext extends MarinerRequestContext {

    /**
     * The wrapped MarinerPageContext.
     */
    private MarinerPageContext marinerPageContext;

    private String devicePolicyValue;

    // Javadoc inherited
    public MarinerRequestContext createNestedContext()
        throws IOException,
        RepositoryException,
        MarinerContextException {
        return null;
    }

    // Javadoc inherited
    public String getDevicePolicyValue(String policyName) {
        return devicePolicyValue;
    }

    private Stack projects = new Stack();

    // Javadoc inherited
    public void pushProject(Project project) {
        if (project == null) {
            throw new IllegalArgumentException("Project cannot be null");
        }
        projects.push(project);
    }

    // Javadoc inherited
    public Project popProject(Project expected) {
        return (Project)projects.pop();
    }

    // Javadoc inherited
    public Project getCurrentProject() {
        if (projects.isEmpty()) {
            return null;
        } else {
            return (Project)projects.peek();
        }
    }

    // Javadoc inherited
    public void setDevicePolicyValue(String value) {
        devicePolicyValue = value;
    }

    /**
     * Set the MarinerPageContext in this TestMarinerRequestContext.
     *
     * @param pageContext The MarinerPageContext.
     */
    public void setMarinerPageContext(MarinerPageContext pageContext) {
        marinerPageContext = pageContext;
    }

    /**
     * Return locally stored page context as we are unable to get to the
     * one in the parent.
     *
     * @return Stored MarinerPageContext
     */
    public MarinerPageContext getMarinerPageContext() {
        return marinerPageContext;
    }

    // Javadoc inherited
    public String getCharacterEncoding() {
       return null;
    }

    // Javadoc inherited
    public void setCharacterEncoding(String characterEncoding)
        throws IllegalArgumentException {
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 09-Aug-05	9211/1	pabbott	VBM:2005080902 End to End CSS emulation test

 ===========================================================================
*/

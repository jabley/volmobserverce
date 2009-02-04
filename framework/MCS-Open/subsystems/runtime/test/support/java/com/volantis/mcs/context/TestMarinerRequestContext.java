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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/context/TestMarinerRequestContext.java,v 1.3 2003/04/22 10:59:03 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-Jan-03    Geoff           VBM:2003012101 - Created. 
 * 17-Apr-03    Allan           VBM:2003041506 - Added set/get methods for 
 *                              MarinerPageContext. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.context;

import com.volantis.mcs.project.Project;
import com.volantis.mcs.repository.RepositoryException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * A MarinerRequestContext which is "designed" for use with test cases.
 * <p>
 * Note that the original "design" for this was simply to collect together all
 * the existing usages demonstrated in the various inner classes which had 
 * previously extended MarinerRequestContext before the existance of this 
 * class, so the code is not necessarily the best at the moment. However, with 
 * continued use and more refactoring, this should evolve into something which 
 * is useful for all test cases.
 * <p> 
 * Make sure you run ALL the test cases if you modify this class! 
 */ 
public class TestMarinerRequestContext extends MarinerRequestContext {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";
    
    private String devicePolicyValue;

    // Implement the abstract method so we can instantiate this class.
    public MarinerRequestContext createNestedContext()
            throws IOException,
            RepositoryException,
            MarinerContextException {
        return null;
    }
    
    public String getDevicePolicyValue(String policyName) {
        // This currently returns just a simple hardcoded value, regardless of
        // the name passed in.
        // if you need to be more flexible, change the impl to a Map and the
        // set method to take the policy name as well.
        return devicePolicyValue;
    }

    private Stack projects = new Stack();

    public void pushProject(Project project) {
        projects.push(project);
    }

    public Project popProject(Project expected) {
        return (Project) projects.pop();
    }

    public Project getCurrentProject() {
        // test cases don't usually require a project, and if we call the
        // parent version without one set we die, so instead handle empty
        // in a way which is likely to be compatible with test cases.
        if (projects.isEmpty()) {
            return null;
        } else {
            return (Project) projects.peek();
        }
    }

    public void setDevicePolicyValue(String value) {
        devicePolicyValue = value;
    }
    
    /**
     * Set the MarinerPageContext in this TestMarinerRequestContext.
     * @param pageContext The MarinerPageContext.
     */ 
    public void setMarinerPageContext(MarinerPageContext pageContext) {
        super.setMarinerPageContext(pageContext);
    }
    
    /** 
     * @return The MarinerPageContext associated with this 
     * TestMarinerRequestContext.
     */ 
    public MarinerPageContext getMarinerPageContext() {
        return super.getMarinerPageContext();
    }

    private Map parameters = new HashMap();

    public void resetParameters() {
        parameters.clear();
    }

    public void setParameters(Map parameters) {
        this.parameters = parameters;
    }

    public String getParameter(String name) {
        return (String)parameters.get(name);
    }

    public Map getParameterMap() {
        return parameters;
    }

    public void setParameter(String name, String value) {
        parameters.put(name, value);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-Aug-04	5139/1	geoff	VBM:2004080311 Implement Null Assets: ObjectSelectionPolicys

 24-Jun-04	4726/1	claire	VBM:2004060803 Implementation of internal style sheet caching

 25-Mar-04	3386/1	steve	VBM:2004030901 Supermerged and merged back with Proteus

 12-Mar-04	3370/1	steve	VBM:2004030901 Null exception if protocols element is missing in config

 23-Jan-04	2736/1	steve	VBM:2003121104 Configurable WMLC and dollar encoding

 23-Jan-04	2685/1	steve	VBM:2003121104 Configurable WMLC and dollar encoding

 ===========================================================================
*/

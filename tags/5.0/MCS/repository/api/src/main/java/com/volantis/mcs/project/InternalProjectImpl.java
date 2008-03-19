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
package com.volantis.mcs.project;

import com.volantis.mcs.utilities.MarinerURL;

/**
 * Internal representation of a project.
 */
public class InternalProjectImpl
        implements InternalProject {

    /**
     * The PolicySource for this project
     */
    protected PolicySource policySource;

    /**
     * True if the policies associated with this project should be preloaded.
     */
    private boolean preloaded;

    /**
     * Create a new instance of <code>DefaultProject</code>.
     */
    public InternalProjectImpl(PolicySource policysource) {
        this.policySource = policysource;
    }

    // Javadoc inherited.
    public InternalProject getInternalBaseProject() {
        return null;
    }

    public PolicySource getPolicySource() {
        return policySource;
    }

    public PolicyManager createPolicyManager() {
        // PolicyManagers are not much use, PolicyBuilderManagers are generally
        // much more useful. So we implement PolicyManager in terms of
        // PolicyBuilderManager to simplify the underlying layers. I would like
        // to deprecate PolicyManager but PD says no.
        return new PolicyManagerImpl(createPolicyBuilderManager());
    }

    public PolicyBuilderManager createPolicyBuilderManager() {
        return policySource.createPolicyBuilderManager(this);
    }

//    // JavaDoc inherited
//    public boolean equals(Object object) {
//        if(object == null ||
//            !(object instanceof InternalProjectImpl)) {
//                return false;
//        }
//
//        InternalProject project = (InternalProject) object;
//
//        return policySource.equals(project.getPolicySource());
//    }
//
//    // JavaDoc inherited
//    public int hashCode() {
//        return policySource.hashCode() + getClass().hashCode();
//    }
//
//    // JavaDoc inherited
//    public int compareTo(Object object) {
//        InternalProject project = (InternalProject) object;
//
//        return policySource.compareTo(project.getPolicySource());
//    }
//
    // JavaDoc inherited
    public String toString() {
        return getClass().getName() + "@" +
                Integer.toHexString(System.identityHashCode(this)) +
                "{policySource = " + policySource + "}";
    }

    public boolean isPreloaded() {
        return preloaded;
    }

    /**
     * @see {@link #preloaded}.
     */
    public void setPreloaded(boolean preloaded) {
        this.preloaded = preloaded;
    }

    public MarinerURL makeProjectRelative(MarinerURL resolved) {
        return resolved;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-May-04	4507/1	geoff	VBM:2004051809 pre populate policy caches

 04-Feb-04	2828/1	ianw	VBM:2004011922 Added MCSI content handler

 01-Feb-04	2821/1	mat	VBM:2004012701 Change tests and generate scripts for Projects

 ===========================================================================
*/

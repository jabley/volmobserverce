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
/** (c) Volantis Systems Ltd 2004.  */
package com.volantis.mcs.project.impl.jdbc;

import com.volantis.mcs.accessors.PolicyBuilderAccessor;
import com.volantis.mcs.policies.InternalPolicyFactory;
import com.volantis.mcs.policies.impl.jdbc.JDBCPolicyBuilderAccessor;
import com.volantis.mcs.policies.impl.jdbc.JDBCPolicyBuilderReader;
import com.volantis.mcs.project.InternalProject;
import com.volantis.mcs.project.PolicyBuilderManager;
import com.volantis.mcs.project.impl.LocalPolicySourceImpl;
import com.volantis.mcs.project.jdbc.JDBCPolicySource;
import com.volantis.mcs.repository.LocalRepository;


/**
 * A PolicySource for a JDBC policy.  Defines the project name for the 
 * policy.
 */
public class JDBCPolicySourceImpl
        extends LocalPolicySourceImpl
        implements JDBCPolicySource {

    /**
     * The accessor to use for accessing policies in any JDBC source.
     */
    private static final PolicyBuilderAccessor ACCESSOR =
            new JDBCPolicyBuilderAccessor();

    /**
     * The reader to use for accessing policies in any JDBC source.
     */
    private static final JDBCPolicyBuilderReader READER =
            new JDBCPolicyBuilderReader(ACCESSOR);


    /**
     * The name for this JDBC policy source.
     */
    private final String name;

    /**
     * Construct the JDBCPolicySource
     * 
     * @param repository
     * @param name The name of the project, used in the project column.
     */
    public JDBCPolicySourceImpl(LocalRepository repository, String name) {
        super(repository, ACCESSOR, READER);
        this.name = name;
    }

    // Javadoc inherited
    public boolean equals(Object obj) {
        if(!(obj instanceof JDBCPolicySource)) {
            return false;
        }
        return getName().equals(((JDBCPolicySource) obj).getName());
    }

    // Javadoc inherited
    public int hashCode() {
        return getClass().hashCode() + getName().hashCode();
    }

    public String getName() {
        return name;
    }

    // Javadoc inherited.
    public PolicyBuilderManager createPolicyBuilderManager(InternalProject project) {
        InternalPolicyFactory factory =
                InternalPolicyFactory.getInternalInstance();
        return factory.createJDBCPolicyBuilderManager(project);
    }

    //javdoc inherited
    public String toString() {
        return "[name=" + name + "]";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Feb-04	3090/1	ianw	VBM:2004021716 Added extra debugging and removed error masking for IBM projects problem

 27-Jan-04	2769/3	mat	VBM:2004012702 Added testcases

 27-Jan-04	2769/1	mat	VBM:2004012702 Add PolicySource

 ===========================================================================
*/

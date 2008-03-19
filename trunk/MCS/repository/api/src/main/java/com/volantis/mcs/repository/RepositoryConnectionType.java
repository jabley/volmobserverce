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
/* (c) Volantis Systems Ltd 2001.  */

package com.volantis.mcs.repository;

/**
 * Typesafe enumeration for RepositoryConnection types. There is one entry for each
 * type of repository that exists.
 *
 * @deprecated Don't use this again. 
 */
public final class RepositoryConnectionType {

    /** The name of the repository connection */
    private String name;

    /** 
     * Create a repositoryConnectionType
     * 
     * @param name The repository connection name
     */
    private RepositoryConnectionType(String name) {
        this.name = name;
    }

    public static final RepositoryConnectionType JDBC_REPOSITORY_CONNECTION =
        new RepositoryConnectionType("JDBC");
    public static final RepositoryConnectionType XML_REPOSITORY_CONNECTION =
        new RepositoryConnectionType("XML");

    // Javadoc inherited
    public String toString() {
        return name + "RepositoryConnection";
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-Feb-04	2761/1	mat	VBM:2004011910 Add Project repository

 05-Feb-04	2694/1	mat	VBM:2004011917 Rework for finding repositories

 26-Jan-04	2694/1	mat	VBM:2004011917 Improve the way repository connections are located

 ===========================================================================
*/

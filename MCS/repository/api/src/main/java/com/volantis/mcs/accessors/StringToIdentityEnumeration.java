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
 * $Header: /src/voyager/com/volantis/mcs/accessors/StringToIdentityEnumeration.java,v 1.2 2002/03/18 12:41:12 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Oct-01    Paul            VBM:2001092608 - Created.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.accessors;

import com.volantis.mcs.project.Project;
import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.project.Project;

/**
 * This class adapts a RepositoryEnumeration of Strings into a
 * RepositoryEnumeration of RepositoryObjectIdentity objects of a specified
 * class.
 */
public abstract class StringToIdentityEnumeration implements RepositoryEnumeration {

    private final Project project;

    /**
     * The wrapped enumeration.
     */
    protected RepositoryEnumeration enumeration;

    /**
   * Create a new <code>StringToIdentityEnumeration</code>.
   * @param enumeration The enumeration of Strings to wrap.
   * @param project the project that all the repository objects belong to.
   */
  public StringToIdentityEnumeration (RepositoryEnumeration enumeration,
                                      Project project) {
        this.enumeration = enumeration;
        this.project = project;
    }

    // Javadoc inherited from super class.
    public boolean hasNext()
        throws RepositoryException {

        return enumeration.hasNext();
    }

    // Javadoc inherited from super class.
    public Object next()
        throws RepositoryException {

        String name = (String) enumeration.next();
        return createIdentity(project, name);
    }

    protected abstract Object createIdentity(Project project, String name);

    // Javadoc inherited from super class.
    public void close()
        throws RepositoryException {

        enumeration.close();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Oct-05	9729/1	geoff	VBM:2005100507 Mariner Export fails with NPE

 28-Sep-05	9445/1	gkoch	VBM:2005090603 Introduced ComponentContainers to bring components and their assets together

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-May-04	4507/1	geoff	VBM:2004051809 pre populate policy caches

 ===========================================================================
*/

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
 * $Header: /cvs/architecture/architecture/api/src/java/com/volantis/mcs/objects/Project.java,v 1.2 2003/12/15 16:13:06 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 *
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.project;



/**
 * Represents a project.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with current and future releases of the
 * product at binary and source levels.</strong>
 * </p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @mock.generate
 * @since 3.5.1
 */
public interface Project {

    /**
     * Create a {@link PolicyManager} that can be used to manage policies in
     * this project.
     *
     * @return A newly created {@link PolicyManager}, or null if this project
     *         does not support managing of the policies, e.g. if it is remote.
     */
    PolicyManager createPolicyManager();

    /**
     * Create a {@link PolicyBuilderManager} that can be used to manage
     * policies in this project.
     *
     * @return A newly created {@link PolicyBuilderManager}, or null if this
     *         project does not support managing of the policies, e.g. if it is
     *         remote.
     * @since 3.6
     */
    PolicyBuilderManager createPolicyBuilderManager();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	8967/1	pduffin	VBM:2005070702 Refactored resolving of expressions into component identities

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 30-Jan-04	2767/1	claire	VBM:2004012701 Add project

 ===========================================================================
*/

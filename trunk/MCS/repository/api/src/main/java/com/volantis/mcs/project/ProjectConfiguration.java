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

package com.volantis.mcs.project;

import com.volantis.mcs.repository.Repository;

/**
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with current and future releases of the
 * product at binary and source levels.</strong>
 * </p>
 *
 * <table border="1" cellpadding="3" cellspacing="0" width="100%">
 *
 * <tr bgcolor="#ccccff" class="TableHeadingColor">
 * <td colspan="2"><font size="+2">
 * <b>Property Summary</b></font></td>
 * </tr>
 *
 * <tr id="repository">
 * <td align="right" valign="top" width="1%"><b>repository</b></td>
 * <td>the {@link Repository} within which the project's policies are
 * stored.</td>
 * </tr>
 *
 * <tr id="policyLocation">
 * <td align="right" valign="top" width="1%"><b>policy&nbsp;location</b></td>
 * <td>the location of the policies. For an XML repository this is the absolute
 * path to the directory containing the policy, for a JDBC repository this
 * is the project name used within the repository.</td>
 * </tr>
 *
 * <tr id="deleteProjectContents">
 * <td align="right" valign="top" width="1%"><b>delete&nbsp;project&nbsp;contents</b></td>
 * <td>indicates whether when the project is instantiated it should delete
 * the directory containing the policies. This only affects the XML
 * repository.</td>
 * </tr>
 *
 * </table>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @since 3.5.1
 */
public interface ProjectConfiguration {

    /**
     * Getter for the <a href="#repository">repository</a> property.
     * @return Value of the <a href="#repository">repository</a>
     * property.
     */
    Repository getRepository();

    /**
     * Setter for the <a href="#repository">repository</a> property.
     *
     * @param repository New value of the
     * <a href="#repository">repository</a> property.
     */
    void setRepository(Repository repository);

    /**
     * Getter for the <a href="#policyLocation">policy location</a> property.
     * @return Value of the <a href="#policyLocation">policy location</a>
     * property.
     */
    String getPolicyLocation();

    /**
     * Setter for the <a href="#policyLocation">policy location</a> property.
     *
     * @param policyLocation New value of the
     * <a href="#policyLocation">policy location</a> property.
     */
    void setPolicyLocation(String policyLocation);

    /**
     * Getter for the <a href="#deleteProjectContents">delete project contents</a> property.
     * @return Value of the <a href="#deleteProjectContents">delete project contents</a>
     * property.
     */
    boolean isDeleteProjectContents();

    /**
     * Setter for the <a href="#deleteProjectContents">delete project contents</a> property.
     *
     * @param deleteProjectContents New value of the
     */
    void setDeleteProjectContents(boolean deleteProjectContents);
}

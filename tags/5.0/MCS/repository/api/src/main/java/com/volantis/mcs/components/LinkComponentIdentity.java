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
 * This code was automatically generated by com.volantis.mcs.build.objects.CommonObjectsCodeGenerator
 * on 3/14/08 7:12 PM
 *
 * YOU MUST NOT MODIFY THIS FILE
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.components;

import com.volantis.mcs.objects.AbstractRepositoryObjectIdentity;

import com.volantis.mcs.project.Project;

/**
 * Encapsulates those properties of a LinkComponent which uniquely identify
 * it.
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @deprecated See {@link com.volantis.mcs.policies}.
 * This was deprecated in version 3.5.1.
 */
public class LinkComponentIdentity
  extends AbstractRepositoryObjectIdentity {

  /**
   * Create a new <code>LinkComponentIdentity</code>.
   * @param project  The project used with this object.   A null value is
   * possible if it
   * has not been set.
   * @param name  The name of the object.
   */
  public LinkComponentIdentity (Project project,
                                String name) {
    super(project, name);
  }

  /**
   * Create a new <code>LinkComponentIdentity</code>.
   * @param name  The name of the object.
   */
  public LinkComponentIdentity (String name) {
    super(null, name);
  }

  // Javadoc inherited from super class.
  public Class getObjectClass () {
    return LinkComponent.class;
  }
}

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
 * $Header: /src/voyager/com/volantis/mcs/components/AbstractComponent.java,v 1.1 2001/04/10 11:01:05 pduffin Exp
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Apr-01    Paul            Created.
 * 24-May-01    Paul            VBM:2001051103 - Made component serializable.
 * 26-Jun-01    Paul            VBM:2001051103 - Added some constructors,
 *                              plus clone, equals, hashCode and toString
 *                              methods.
 * 16-Jul-01    Paul            VBM:2001070508 - Added identityMatches method.
 * 24-Oct-01    Paul            VBM:2001092608 - Modified to make this an
 *                              extension of AbstractRepositoryObject which
 *                              involved removing the name property,
 *                              identityMatches, clone, equals (Object,Object),
 *                              hashCode (Object), toString and paramString
 *                              methods as they are all provided by
 *                              AbstractRepositoryObject.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 02-Apr-02    Mat             VBM:2002022009 - Added constructor that takes
 *                              an attributes class.
 * 03-Apr-02    Mat             VBM:2002022009 - Removed constructor that 
 *                              took an Attributes class.
 * 08-May-02    Paul            VBM:2002050305 - Added constructor which takes
 *                              an identity.
 * 17-May-02    Paul            VBM:2002050101 - Changed the names of some
 *                              annotations to make them more meaningful and
 * 26-Jul-02    Allan           VBM:2002072602 - fallbackTextComponentName
 *                              property removed along with equals(), 
 *                              hashCode() and paramString(). Added 
 *                              INTERNAL USE ONLY to class javadoc.
 * 02-Dec-02    Steve           VBM:2002090210 - Extends AbstractCacheableRepositoryObject
 *                              to pick up the remote policy cache information.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.components;

import com.volantis.mcs.objects.AbstractCacheableRepositoryObject;
import com.volantis.mcs.objects.RepositoryObjectIdentity;

/**
 * The AbstractComponent class is the parent class for all components.
 *
 * THIS CLASS IS FOR INTERNAL USE ONLY.
 *
 * @deprecated See {@link com.volantis.mcs.policies}.
 *             This was deprecated in version 3.5.1.
 */
public abstract class AbstractComponent
  extends AbstractCacheableRepositoryObject {

  /**
   * The copyright statement.
   */
  private static String mark = "(c) Volantis Systems Ltd 2001.";

  /**
   * Create an unnamed component.
   * @volantis-api-exclude-from PublicAPI
   * @volantis-api-exclude-from ProfessionalServicesAPI
   */
  public AbstractComponent () {
  }

  /**
   * Create a component with the specified name.
   * @param name The name of the component.
   * @volantis-api-exclude-from PublicAPI
   * @volantis-api-exclude-from ProfessionalServicesAPI
   */
  public AbstractComponent (String name) {
    setName (name);
  }

  /**
   * Create a new <code>AbstractComponent</code>.
   * @param identity The identity to use.
   * @volantis-api-exclude-from PublicAPI
   * @volantis-api-exclude-from ProfessionalServicesAPI
   */
  public AbstractComponent (RepositoryObjectIdentity identity) {
    super (identity);
  }
  
}

/*
 * Local variables:
 * c-basic-offset: 2
 * End:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 ===========================================================================
*/

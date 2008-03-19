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
 * $Header: /src/voyager/com/volantis/mcs/assets/Asset.java,v 1.14 2003/03/24 16:35:25 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 04-Jun-01    Paul            VBM:2001051103 - Added this change history and
 *                              also made Assets serializable.
 * 26-Jun-01    Paul            VBM:2001051103 - Added clone, equals, hashCode
 *                              and toString methods.
 * 09-Jul-01    Paul            VBM:2001070902 - Removed the encoding property
 *                              as it does not apply to all Assets, namely
 *                              ChartAsset.
 * 16-Jul-01    Paul            VBM:2001070508 - Added identityMatches method.
 * 26-Sep-01    Allan           VBM:2001091104 - Javadoc.
 * 24-Oct-01    Paul            VBM:2001092608 - Modified to make this an
 *                              extension of AbstractRepositoryObject which
 *                              involved removing the name property,
 *                              identityMatches, clone, equals (Object,Object),
 *                              hashCode (Object), toString and paramString
 *                              methods as they are all provided by
 *                              AbstractRepositoryObject.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 08-May-02    Paul            VBM:2002050305 - Added constructor which takes
 *                              an identity.
 * 17-May-02    Paul            VBM:2002050101 - Added annotations to constrain
 *                              field lengths.
 * 26-Jul-02    Allan           VBM:2002072508 - Removed assetGroupName and
 *                              value properties. Removed equals(), 
 *                              hashCode() and paramString(). These removed 
 *                              items are now in SubstantiveAsset.java.
 *                              Added INTERNAL USE ONLY to class javadoc.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.assets;

import com.volantis.mcs.objects.AbstractRepositoryObject;
import com.volantis.mcs.objects.RepositoryObjectIdentity;

/**
 * The Asset class is the parent of all assets. It provides common 
 * attributes and behaviour for all assets.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 *
 * @deprecated See {@link com.volantis.mcs.policies}.
 *             This was deprecated in version 3.5.1.
 */
public abstract class Asset
  extends AbstractRepositoryObject {

    /**
   * Creates a new <code>Asset</code> instance.
   */
  public Asset() {
  }
   
  /**
   * Create a new <code>Asset</code>.
   * @param identity The identity to use.
   */
  public Asset (RepositoryObjectIdentity identity) {
    super (identity);
  }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 30-Jan-04	2767/1	claire	VBM:2004012701 Add project

 ===========================================================================
*/

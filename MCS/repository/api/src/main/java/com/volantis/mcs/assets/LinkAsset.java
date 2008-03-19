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
 * $Header: /src/voyager/com/volantis/mcs/assets/LinkAsset.java,v 1.13 2003/03/24 16:35:25 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 21-Sep-01    Doug            VBM:2001090302 Created
 * 17-Oct-01    Paul            VBM:2001101701 - Added default constructor.
 * 24-Oct-01    Paul            VBM:2001092608 - Removed the identityMatches
 *                              method and added the createIdentity method.
 *                              Also made sure that any changes to those
 *                              fields which form part of this objects identity
 *                              causes the cached identity to be discarded.
 * 14-Jan-02    Paul            VBM:2002011414 - Added annotations to indicate
 *                              which of the properties of this class
 *                              constitute its identity.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from 
 *                              class to string.
 * 22-Mar-02    Adrian          VBM:2002031503 - add annotations for auto
 *                              generated accessors
 * 02-Apr-02    Mat             VBM:2002022009 - Added 
 *                              @mariner-generate-imd-accessor and also added
 *                              a constructor that takes an Attributes class.
 * 03-Apr-02    Mat             VBM:2002022009 - Removed constructor that 
 *                              took an Attributes class.
 * 26-Apr-02    Mat             VBM:2002040814 - Added 
 *                              @mariner-generate-remote-accessor.
 * 08-May-02    Paul            VBM:2002050305 - Added constructor which takes
 *                              an identity.
 * 17-May-02    Paul            VBM:2002050101 - Changed the names of some
 *                              annotations to make them more meaningful and
 *                              added annotations to constrain field lengths.
 * 26-Jul-02    Allan           VBM:2002072508 - Changed to extend
 *                              SubstantiveAsset.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.assets;

import com.volantis.mcs.objects.RepositoryObjectIdentity;

/**
 * The LinkAsset class represents a link asset associated with a particular
 * device or device family.
 *
 * @mariner-hidden-comment The following tags are used by a code generation
 * doclet to automatically generate an Identity class for this class. Each
 * tag specifies the name of a field which constitutes part of the identity.
 * The order of the fields is the order of the parameters to the Identity
 * classes constructor.
 *
 * @mariner-object-identity-field project
 * @mariner-object-identity-field name
 * @mariner-object-identity-field deviceName
 *
 * @mariner-object-null-is-empty-string-field value
 * @mariner-object-guardian com.volantis.mcs.components.LinkComponent
 *
 * @mariner-generate-imd-accessor
 *
 * @mariner-generate-property-lookup
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 *
 * @deprecated See {@link com.volantis.mcs.policies}.
 *             This was deprecated in version 3.5.1.
 */
public class LinkAsset
  extends SubstantiveAsset {

    /**
   * The name of the device or device family to which this asset applies
   *
   * @mariner-object-field-length 255
   */
  private String deviceName;

  /**
   * Creates a new <code>LinkAsset</code> instance.
   */
  public LinkAsset() {
    this(null, null, null, null);
  }

  /**
   * Creates a new <code>LinkAsset</code> instance.
   *
   * @param name name of the link asset
   */
  public LinkAsset(String name) {
    this(name, null, null, null);
  }

  /**
   * Creates a new <code>LinkAsset</code> instance.
   *
   * @param name The name of the LinkAsset
   * @param deviceName The device name associated with the LinkAsset
   * @param groupName The name of any associated AssetGroup
   * @param value the link in string format
   */
  public LinkAsset(String name, String deviceName,
                   String groupName, String value) {
    setName (name);
    setDeviceName (deviceName);
    setAssetGroupName (groupName);
    setValue (value);
  }
  
  /**
   * Create a new <code>LinkAsset</code>.
   * @param identity The identity of the <code>LinkAsset</code>.
   */
  public LinkAsset (LinkAssetIdentity identity) {
    super (identity);

    setDeviceName (identity.getDeviceName ());
  }
  
  /**
   * Access method for the deviceName property.
   *
   * @return   the current value of the deviceName property
   */
  public String getDeviceName () {
    return deviceName;
  }

  /**
   * Sets the value of the deviceName property.
   *
   * @param deviceName the new value of the deviceName property
   */
  public void setDeviceName (String deviceName) {
    this.deviceName = deviceName;
    identityChanged ();
  }

  // Javadoc inherited from super class.
  protected RepositoryObjectIdentity createIdentity () {
    return new LinkAssetIdentity(getProject(), getName(), getDeviceName());
  }

  /**
   * Test for equality against a given LinkAsset
   *
   * @param object LinkAsset to test against
   * @return true if object is equal to this LinkAsset, false otherwise
   */
  public boolean equals (Object object) {
    if (object instanceof LinkAsset) {
      LinkAsset o = (LinkAsset) object;
      return super.equals (o)
        && equals (deviceName, o.deviceName);
    }

    return false;
  }

  /**
   * Return a hash code value for this object
   *
   * @return the hash code value
   */
  public int hashCode () {
    return super.hashCode ()
      + hashCode (deviceName);
  }

  /**
   * Return a parameter String for this object
   *
   * @return the param string
   */
  protected String paramString () {
    return super.paramString ()
      + "," + deviceName;
  }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Oct-05	9789/1	emma	VBM:2005101113 Refactor JDBC Accessors to use chunked accessor

 28-Sep-05	9445/1	gkoch	VBM:2005090603 Introduced ComponentContainers to bring components and their assets together

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Feb-04	2846/1	claire	VBM:2004011915 Adding project init to identities. Fixing assetURL rewrite

 30-Jan-04	2767/1	claire	VBM:2004012701 Add project

 06-Jan-04	2362/1	mat	VBM:2004010207 Enable fields to be tagged to output empty string values for attributes

 03-Nov-03	1698/1	pcameron	VBM:2003102411 Added some new PolicyValue methods and refactored

 ===========================================================================
*/

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
 * $Header: /src/voyager/com/volantis/mcs/assets/DeviceAudioAsset.java,v 1.3 2003/03/24 16:35:25 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Nov-02    Mat             VBM:2002112213 - Created to enable targetted
 *                              audio assets.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.assets;

import com.volantis.mcs.objects.RepositoryObjectIdentity;

/**
 * DeviceAudioAsset represents an audio asset, such as an MP3 file or a Real Audio
 * file. The asset is targetted towards a specific device. 
 * The combination of device name and encoding attributes are unique in the
 * repository.
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
 * @mariner-object-required-field encoding
 *
 * @mariner-object-guardian com.volantis.mcs.components.AudioComponent
 * @mariner-object-base-identity-class com.volantis.mcs.objects.AbstractRepositoryObjectIdentity
 *
 * @mariner-generate-accessor-import com.volantis.mcs.assets.AudioAsset
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
public class DeviceAudioAsset extends AudioAsset {

    /**
   * The name of the device or device family to which this asset applies
   *
   * @mariner-object-field-length 255
   */
  private String deviceName;
  
  /**
   * Creates a new <code>DeviceAudioAsset</code> instance.
   *
   */
  public DeviceAudioAsset() {
    this ((String) null);
  }

  /**
   * Creates a new <code>DeviceAudioAsset</code> instance.
   *
   * @param name the name of this asset
   */
  public DeviceAudioAsset(String name) {
    this (name, null, null, null, BASIC_AUDIO);
  }

  /**
   * Creates a new <code>DeviceAudioAsset</code> instance.
   *
   * @param name the name of this asset
   * @param assetGroupName the name of the asset group for this asset
   * @param value the value of this asset
   * @param deviceName the deviceName of this asset.
   * @param encoding the encoding of this asset
   */
  public DeviceAudioAsset(String name, String assetGroupName, String value,
                          String deviceName, int encoding) {
    setName (name);
    setAssetGroupName (assetGroupName);
    setValue (value);
    setDeviceName(deviceName);
    setEncoding (encoding);
  }

  /**
   * Create a new <code>AudioAsset</code>.
   * @param identity The identity of the <code>AudioAsset</code>.
   */
  public DeviceAudioAsset(DeviceAudioAssetIdentity identity) {
    // As we do not have an AudioAssetIdentity with an encoding
    // we cannot call super(identity), so we call super with 
    // the name instead.
    super (identity.getName());

    setDeviceName (identity.getDeviceName ());
  }

  // Javadoc inherited from super class.
  protected RepositoryObjectIdentity createIdentity () {
    return new DeviceAudioAssetIdentity(getProject(), getName(),
            getDeviceName());
  }

  public boolean equals (Object object) {
    if (object instanceof DeviceAudioAsset) {
      DeviceAudioAsset o = (DeviceAudioAsset) object;
      return super.equals (o)
        && deviceName.equals(o.getDeviceName());
    }

    return false;
  }

  public int hashCode () {
    return super.hashCode ()
      + hashCode(deviceName);
  }

  protected String paramString () {
    return super.paramString ()
      + "," + deviceName;
  }

    /** Getter for property deviceName.
    * @return Value of property deviceName.
    *
    */
    public String getDeviceName() {
      return deviceName;
    }

    /** Setter for property deviceName.
    * @param deviceName New value of property deviceName.
    *
    */
    public void setDeviceName(String deviceName) {
      this.deviceName = deviceName;
      identityChanged();
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

 03-Nov-03	1698/1	pcameron	VBM:2003102411 Added some new PolicyValue methods and refactored

 ===========================================================================
*/

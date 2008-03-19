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
 * $Header: /src/voyager/com/volantis/mcs/assets/SubstantiveAsset.java,v 1.3 2003/03/24 16:35:25 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 26-Jul-02    Allan           VBM:2002072508 - An Asset that has substance.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.assets;

import com.volantis.mcs.objects.RepositoryObjectIdentity;
import com.volantis.mcs.project.Project;

/**
 * An Asset that has substance.
 *
 * <p><strong>
 * THIS CLASS IS FOR INTERNAL USE ONLY
 * </strong></p>
 *
 * <p>The asset group project specifies the project that contains the asset
 * group. If it is null then the asset group is assumed to be in the same
 * project as the asset.</p>
 *
 * @deprecated See {@link com.volantis.mcs.policies}.
 *             This was deprecated in version 3.5.1.
 */
public abstract class SubstantiveAsset extends Asset {

    /**
   * The name of the asset group to which this asset belongs. Asset groups 
   * define the location of groups of assets. If the asset group name is NULL 
   * it indicates that the normal Volantis rules for prefixing information 
   * from the configuration file are to be used
   *
   * @mariner-object-field-length 255
   */
  private String assetGroupName;

    /**
     * The project of the asset group to which this asset belongs.
     */
    private Project assetGroupProject;

  /**
   * The value associated with the asset. The meaning of the value depends 
   * on the type of asset group to which this asset belongs. For entries 
   * represented internally in the Volantis repository, the value is the 
   * final part of the URI that represents the file containing the data 
   * associated with the asset. This partial URI is combined with any prefix 
   * associated with the asset group to form the full URI for the file. For 
   * entries represented in external repositories, the value is passed to 
   * the appropriate accessor for use in computing the final URI.
   *
   * @mariner-object-field-length 255
   * @mariner-object-field-control-type URIFragment
   */
  private String value;

    /**
   * Creates a new <code>Asset</code> instance.
   */
  public SubstantiveAsset() {
  }
   
  /**
   * Create a new <code>Asset</code>.
   * @param identity The identity to use.
   */
  public SubstantiveAsset (RepositoryObjectIdentity identity) {
    super (identity);
  }

  /**
   * Retrieve the name of the asset group associated with this asset.
   *
   * @return the name of the asset group associated with this asset
   */
  public String getAssetGroupName() {
    return assetGroupName;
  }
   
  /**
   * Set the name of the asset group associated with this asset.
   *
   * @param assetGroupName the name asset group associated with this asset
   */
  public void setAssetGroupName(String assetGroupName) {
    this.assetGroupName = assetGroupName;
  }
   
    /**
     * Set the project that contains the asset group associated with this
     * asset.
     *
     * @param project The project of the asset group containing this asset.
     */
    public void setAssetGroupProject(Project project) {
        this.assetGroupProject = project;
    }

    /**
     * Get the project that contains the asset group associated with this
     * asset.
     *
     * @return The project of the asset group containing this asset.
     */
    public Project getAssetGroupProject() {
        return assetGroupProject;
    }

  /**
   * Retrieve the value associated with this asset.
   *
   * @return the value associated with this asset
   */
  public String getValue() {
    return value;
  }
   
  /**
   * Set the value associated with this asset.
   *
   * @param value the value associated with this asset
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Compare this asset object with another asset object to see if they are 
   * equivalent
   *
   * @param object the other object to compare with this object
   * @return true if the objects are equivalent and false otherwise
   */
  public boolean equals (Object object) {

      if(object instanceof SubstantiveAsset) {
          SubstantiveAsset o = (SubstantiveAsset) object;

          return super.equals(o) 
              && equals(assetGroupName, o.assetGroupName)
              && equals(value, o.value);
      }

      return false;
  }

  // Javadoc inherited from super class.
  public int hashCode () {
    return super.hashCode ()
      + hashCode (assetGroupName)
      + hashCode (value);
  }

  /**
   * Return a String representation of the state of the object.
   * @return The String representation of the state of the object.
   * @volantis-api-include-in PublicAPI
   */
  protected String paramString () {
    return super.paramString ()
      + "," + assetGroupName
      + "," + value;
  }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Mar-05	7426/1	philws	VBM:2004121405 Port URI fragment asset value encoding and decoding from 3.3

 15-Mar-05	7374/1	philws	VBM:2004121405 Allow asset values to contain space characters via URI encoding

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 ===========================================================================
*/

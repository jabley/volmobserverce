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
 * $Header: /src/voyager/com/volantis/mcs/imdapi/AssetGroupAttributes.java,v 1.2 2003/03/24 16:35:26 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 *
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Mar-02    Mat            VBM:2002022009 - Created
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.imdapi;

/**
 * The asset group attributes.
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public final class AssetGroupAttributes {

  /**
   * The copyright statement.
   */
  private static String mark = "(c) Volantis Systems Ltd 2001.";

  /**
   * The locationType attribute of the asset group element
   */
  private String locationType;

  /**
   * The name attribute of the asset group element
   */
  private String name;

  /**
   * The prefixURL attribute of the asset group element
   */
  private String prefixURL;

  /**
   * Create a new <code>AssetGroupAttributes</code>.
   */
  public AssetGroupAttributes () {
    // This constructor delegates all its work to the reinitialise method,
    // no extra initialisation should be added here, instead it should be
    // added to the reinitialise method.
    reinitialise ();
  }

  /**
   * Reinitialise all the data members. This is called from the constructor and
   * also from reset.
   */
  private void reinitialise () {
    locationType = null;
    name = null;
    prefixURL = null;
  }

  /**
   * Set the value of the locationType attribute.
   * @param locationType The new value of the locationType attribute.
   */
  public void setLocationType (String locationType) {
    this.locationType = locationType;
  }

  /**
   * Get the value of the locationType attribute.
   * @return The value of the locationType attribute.
   */
  public String getLocationType () {
    return locationType;
  }

  /**
   * Set the value of the name attribute.
   * @param name The new value of the name attribute.
   */
  public void setName (String name) {
    this.name = name;
  }

  /**
   * Get the value of the name attribute.
   * @return The value of the name attribute.
   */
  public String getName () {
    return name;
  }

  /**
   * Set the value of the prefixURL attribute.
   * @param prefixURL The new value of the prefixURL attribute.
   */
  public void setPrefixURL (String prefixURL) {
    this.prefixURL = prefixURL;
  }

  /**
   * Get the value of the prefixURL attribute.
   * @return The value of the prefixURL attribute.
   */
  public String getPrefixURL () {
    return prefixURL;
  }

    /**
     * Set the repository name for the asset group.
     *
     * @deprecated This is no longer supported so does nothing.
     *
     * @param repositoryName the repository name for this asset group
     */
    public void setRepositoryName (String repositoryName) {
    }

    /**
     * Retrieve the repository name for the asset group.
     *
     * @deprecated This is no longer supported so always returns null.
     *
     * @return null
     */
    public String getRepositoryName () {
      return null;
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

 28-Apr-05	7914/1	pduffin	VBM:2005042714 Removing ExternalPluginDefinitionsManager, AssetGroup#repositoryName and related classes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/

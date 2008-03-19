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
 * $Header: /src/voyager/com/volantis/mcs/assets/AssetGroup.java,v 1.20 2003/03/24 16:35:25 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Apr-01    Paul            Created.
 * 26-Jun-01    Paul            VBM:2001051103 - Added a constructor, plus
 *                              clone, equals, hashCode and toString methods.
 * 16-Jul-01    Paul            VBM:2001070508 - Added identityMatches method.
 * 26-Sep-01    Allan           VBM:2001091104 - Javadoc
 * 24-Oct-01    Paul            VBM:2001092608 - Modified to make this an
 *                              extension of AbstractRepositoryObject which
 *                              involved removing the name property,
 *                              identityMatches, clone, equals (Object,Object),
 *                              hashCode (Object), toString and paramString
 *                              methods as they are all provided by
 *                              AbstractRepositoryObject and added the
 *                              createIdentity method.
 * 23-Nov-01    Allan           VBM:2001102504 - Added CATEGORY constant.
 * 04-Jan-02    Paul            VBM:2002010403 - Removed CATEGORY constant and
 *                              updated Category class to explicitly set the
 *                              value. This was necessary because the use of
 *                              this constant created an undesirable dependency
 *                              between the objects package and this package.
 *                              It was only ever used by Category and it is
 *                              only used internally so it is better if it is
 *                              not exposed in this public class.
 * 14-Jan-02    Paul            VBM:2002011414 - Added annotations to indicate
 *                              which of the properties of this class
 *                              constitute its identity.
 * 24-Jan-02    Doug            VBM:2002011406 - Added a locationType property
 *                              to indicate whether an asset is located on the
 *                              device or on the server. Added the associated
 *                              getters and setters
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Mar-02    Adrian          VBM:2002031503 - add annotations for auto
 *                              generated accessors
 * 02-Apr-02    Mat             VBM:2002022009 - Added
 *                              @mariner-generate-imd-accessor
 * 26-Apr-02    Mat             VBM:2002040814 - Added
 *                              @mariner-generate-remote-accessor.
 * 08-May-02    Paul            VBM:2002050305 - Added constructor which takes
 *                              an identity.
 * 17-May-02    Paul            VBM:2002050101 - Changed the names of some
 *                              annotations to make them more meaningful and
 *                              added annotations to constrain field lengths,
 *                              and flag the required fields.
 * 17-Jun-02    Allan           VBM:2002030615 - Added xml-mappings for
 *                              locationType.
 * 09-Sep-02    Mat             VBM:2002040825 - Added shared accessor tag.
 * 02-Dec-02    Steve           VBM:2002090210 - Extends AbstractCacheableRepositoryObject
 *                              to pick up the remote policy cache information.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.assets;

import com.volantis.mcs.objects.AbstractCacheableRepositoryObject;
import com.volantis.mcs.objects.RepositoryObjectIdentity;

/**
 * An AssetGroup represents a group to which one or more assets can belong.
 * Asset groups define the location of groups of assets. Information associated
 * with an asset group is used to qualify the URL information from the asset
 * itself when computing its actual location.
 *
 * @mariner-hidden-comment The following tags are used by a code generation
 * doclet to automatically generate an Identity class for this class. Each
 * tag specifies the name of a field which constitutes part of the identity.
 * The order of the fields is the order of the parameters to the Identity
 * classes constructor.
 *
 * @mariner-object-identity-field project
 * @mariner-object-identity-field name
 *
 * @mariner-object-required-field locationType
 *
 * @mariner-generate-imd-accessor
 * @mariner-generate-property-lookup
 * 
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 *
 * @deprecated See {@link com.volantis.mcs.policies}.
 *             This was deprecated in version 3.5.1.
 */
public class AssetGroup extends AbstractCacheableRepositoryObject {

  private static String mark = "(c) Volantis Systems Ltd 2001.";

  /**
   * The value assigned to the locationType field to indicate an asset
   * is being served locally from a device.
   *
   * @deprecated
   */
  public static final int ON_SERVER = 0;

  /**
   * The value assigned to the locationType field to indicate an asset
   * is being served from a web or application server.
   */
  public static final int ON_DEVICE = 1;

    /**
     * The value assigned to the locationType field to indicate an asset
     * is being served from a web or application server and is relative to
     * the context.
     */
    public static final int CONTEXT = 2;

    /**
     * The value assigned to the locationType field to indicate an asset
     * is being served from a web or application server and is relative to
     * the host.
     */
    public static final int HOST = 3;

  /**
   * The prefix to be added to the URI of any asset belonging to this group
   *
   * @mariner-object-field-length 255
   */
  private String prefixURL;

  /**
   * Property to indicate the location of an asset. Defaults to ON_SERVER.
   *
   * @mariner-object-field-xml-mapping AssetGroup.ON_SERVER "server"
   * @mariner-object-field-xml-mapping AssetGroup.ON_DEVICE "device"
   */
  private int locationType;

  /**
   * Creates a new <code>AssetGroup</code> instance.
   *
   */
  public AssetGroup () {
      setLocationType(CONTEXT);
  }

  /**
   * Creates a new <code>AssetGroup</code> instance.
   *
   * @param name the name of the asset group
   */
  public AssetGroup (String name) {
    setName (name);
      setLocationType(CONTEXT);
  }

  /**
   * Create a new <code>AssetGroup</code>.
   * @param identity The identity of the <code>AssetGroup</code>.
   */
  public AssetGroup (AssetGroupIdentity identity) {
    super (identity);
  }

  /**
   * Retrieve the prefix URL for this asset group
   *
   * @return the prefix URL for this asset group
   */
  public String getPrefixURL () {
    return prefixURL;
  }

  /**
   * Set the prefix URLfor this asset groupParameters:
   *
   * @param prefixURL the prefix URL for this asset group
   */
  public void setPrefixURL (String prefixURL) {
    this.prefixURL = prefixURL;
  }

  /**
   * Retrieve the repository name for this asset group.
   *
   * @deprecated This is no longer supported so always returns null.
   *
   * @return null
   */
  public String getRepositoryName () {
    return null;
  }

  /**
   * Set the repository name for this asset group.
   *
   * @deprecated This is no longer supported so does nothing.
   *
   * @param repositoryName the repository name for this asset group
   */
  public void setRepositoryName (String repositoryName) {
  }

  /**
   * Retrieve the location type for this asset group
   * @return the location type
   */
  public int getLocationType() {
    return this.locationType;
  }

    /**
     * Set the location type for this asset group
     *
     * @param locationType the location type
     */
    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }

  // Javadoc inherited from super class.
  protected RepositoryObjectIdentity createIdentity () {
    return new AssetGroupIdentity(getProject(), getName());
  }

  /**
   * Compare this asset group object with another asset group object to see
   * if they are equivalent.
   *
   * @param object the object to compare with this object
   * @return true if the objects are equivalent and false otherwise
   */
  public boolean equals (Object object) {

    // Call the super class to check whether this object and the other object
    // are of the same type and have the same name.
    if (!super.equals (object)) {
      return false;
    }

    AssetGroup o = (AssetGroup) object;

    return equals (prefixURL, o.prefixURL)
      && locationType == o.locationType;
  }

  // Javadoc inherited from super class.
  public int hashCode () {
    return super.hashCode ()
      + hashCode (prefixURL)
      + locationType;
  }

  // Javadoc inherited from super class.
  protected String paramString () {
    return super.paramString ()
      + "," + prefixURL
      + "," + locationType;
  }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Oct-05	9789/1	emma	VBM:2005101113 Refactor JDBC Accessors to use chunked accessor

 02-Oct-05	9652/1	gkoch	VBM:2005092204 Tests for layoutFormat marshaller/unmarshaller

 28-Apr-05	7914/5	pduffin	VBM:2005042714 Addressing review comments

 28-Apr-05	7914/3	pduffin	VBM:2005042714 Removing ExternalPluginDefinitionsManager, AssetGroup#repositoryName and related classes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Feb-04	2846/1	claire	VBM:2004011915 Adding project init to identities. Fixing assetURL rewrite

 30-Jan-04	2767/1	claire	VBM:2004012701 Add project

 12-Jan-04	2532/1	andy	VBM:2004010903 implemented mariner-ignore-xml-attribute doclet tag

 03-Nov-03	1698/2	pcameron	VBM:2003102411 Added some new PolicyValue methods and refactored

 ===========================================================================
*/

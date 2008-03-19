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
 * $Header: /src/voyager/com/volantis/mcs/repository/LinkRepositoryManager.java,v 1.13 2003/03/12 16:10:43 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 21-Sep-01    Doug            VBM:2001090302 - Created
 * 16-Oct-01    Paul            VBM:2001082807 - Removed requester parameter
 *                              from the lock... method.
 * 29-Oct-01    Paul            VBM:2001102901 - Device has moved from
 *                              utilities package to devices package.
 * 29-Oct-01    Paul            VBM:2001102901 - Device has moved from
 *                              utilities package to devices package.
 * 02-Jan-02    Paul            VBM:2002010201 - Removed unnecessary import.
 * 31-Jan-02    Paul            VBM:2001122105 - Removed unused attributes and
 *                              imports and improved consistency of naming
 *                              of parameters.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from 
 *                              class to string.
 * 22-Mar-02    Adrian          VBM:2002031503 - removed retrieveBest methods
 *                              and modified to use AbstractCachingAccessor
 * 22-Mar-02    Adrian          VBM:2002031503 - removed import of old accessor
 * 08-May-02    Paul            VBM:2002050305 - Use an identity factory to
 *                              create the identity objects.
 * 27-May-02    Paul            VBM:2002050301 - Fixed some problems with
 *                              comments referring to other object types,
 *                              obviously a result of copy and paste.
 * 01-Aug-02    Phil W-S        VBM:2002080113 - Renamed any retrieveObjects,
 *                              retrieveObjectsImpl, renameObjects,
 *                              renameObjectsImpl and removeObjects methods,
 *                              replacing "Objects" with "Children".
 * 12-Mar-02    Steve           VBM:2003022403 - Added API doclet tags
 * 07-May-03    Allan           VBM:2003050704 - Caches are now in the 
 *                              synergetics package. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository;

import com.volantis.mcs.accessors.common.AssetAccessor;
import com.volantis.mcs.accessors.common.ComponentAccessor;
import com.volantis.mcs.assets.LinkAsset;
import com.volantis.mcs.assets.LinkAssetIdentity;
import com.volantis.mcs.components.LinkComponent;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.project.Project;
import com.volantis.synergetics.cache.GenericCache;

import java.util.Vector;

/**
 * This class provides the external interface to the management of
 * link components and assets within the repository.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @deprecated Use {@link com.volantis.mcs.project.PolicyManager}.
 *             This was deprecated in version 3.5.1.
 */
public final class LinkRepositoryManager
  extends RepositoryManager {

    /**
     * The type of policies managed by this class.
     */
    private static final PolicyType POLICY_TYPE = PolicyType.LINK;

    /**
   * The object that provides access to link components.
   */
  private final ComponentAccessor componentAccessor;

  /**
   * The object that provides access to link assets.
   */
  private final AssetAccessor assetAccessor;

    /**
   * Set the component cache.
   * @param cache GenericCache to use
   */
  public void setComponentCache (GenericCache cache) {
        // Unused.
  }

  /**
   * Set the asset cache.
   * @param cache GenericCache to use
   */
  public void setAssetCache (GenericCache cache) {
    throw new UnsupportedOperationException();
  }

  /**
   * Refresh the component cache.
   */
  public void refreshComponentCache () {
      flushCache(POLICY_TYPE);
  }

  /**
   * Refresh the asset cache.
   */
  public void refreshAssetCache () {
      throw new UnsupportedOperationException();
  }

  /**
   * Creates a new <code>LinkRepositoryManager</code> instance.
   *
   * @param connection a <code>RepositoryConnection</code> value
   */
  public LinkRepositoryManager (RepositoryConnection connection) {
    this (connection.getRepository ());
  }

    /**
     * Creates a new <code>LinkRepositoryManager</code> instance.
     *
     * @param repository a <code>Repository</code> value
     */
    public LinkRepositoryManager(Repository repository) {
        this(repository, null, null);
    }

    /**
     * Initializes a new <code>LinkRepositoryManager</code> instance.
     *
     * @param repository         the <code>Repository</code> to be used
     * @param project            the default project.
     * @param policyCacheFlusher allows manager to flush policy caches if used
     *                           at runtime.
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     * @volantis-api-exclude-from InternalAPI
     */
    public LinkRepositoryManager(
            Repository repository, Project project,
            DeprecatedPolicyCacheFlusher policyCacheFlusher) {
        super(repository, project, policyCacheFlusher);

        // Get the link accessor.
        componentAccessor = new ComponentAccessor(accessor,
                defaultProject, POLICY_TYPE);
        assetAccessor = new AssetAccessor(accessor, LinkAsset.class,
                defaultProject);
    }

  // ==========================================================================
  //                          Link components
  // ==========================================================================

  /**
   * Add the specified link component to the repository.
   * @param connection a connection to the repository
   * @param linkComponent the LinkComponent object that is to be added to
   * the repository
   * @exception RepositoryException if an error occurs
   */
  public void addLinkComponent (RepositoryConnection connection,
                                LinkComponent linkComponent)
    throws RepositoryException {

            componentAccessor.addObject(getConnection(connection), linkComponent);
  }

  /**
   * Remove the specified link component from the repository.
   * @param connection a connection to the repository
   * @param name  the name of the LinkComponent that is to be
   * removed from the repository
   * @exception RepositoryException if an error occurs
   */
  public void removeLinkComponent (RepositoryConnection connection,
                                   String name)
    throws RepositoryException {

        componentAccessor.removeObject(getConnection(connection), name);
  }

  /**
   * Retrieve the specified link component from the repository.
   * @param connection a connection to the repository
   * @param name  the name of the LinkComponent that is to be
   * retrieved from the repository
   * @return the LinkComponent that was retrieved from the repository or null
   * if no match was found.
   * @exception RepositoryException if an error occurs
   */
  public LinkComponent retrieveLinkComponent (RepositoryConnection connection,
                                              String name)
    throws RepositoryException {

    return (LinkComponent)
      componentAccessor.retrieveObject (getConnection(connection), name);
  }

  /**
   * Rename a link component
   * @param connection a connection to the repository
   * @param name the current name of the link component
   * @param newName the new name for the link component
   * @exception RepositoryException if an error occurs
   */
  public void renameLinkComponent (RepositoryConnection connection,
                                   String name, String newName)
    throws RepositoryException {

    componentAccessor.renameObject (getConnection(connection), name, newName);
  }

  /**
   * copy the fields from one link component to another link component
   * @param connection a connection to the repository
   * @param name the name of link component to copy from
   * @param newName the name of the link component to copy to
   * @exception RepositoryException if an error occurs
   *
   * @deprecated No longer supported, throws exception.
   */
  public void copyLinkComponent (RepositoryConnection connection,
                                 String name, String newName)
    throws RepositoryException {

      throw new UnsupportedOperationException();
  }

  /**
   * Retrieve all the link components in the repository, put them into
   * a Vector and return the Vector.
   * @param connection a connection to the repository
   * @return a Vector containing all the LinkComponent objects available
   * from the Repository
   * @exception RepositoryException if an error occurs
   */
  public Vector retrieveAllLinkComponents (RepositoryConnection connection)
    throws RepositoryException {
    return new Vector (componentAccessor.retrieveAllComponents(getConnection(connection))); // TBD OBJECTS
  }

  /**
   * Return an enumeration of names for all linkcomponents in the repository
   * @param connection a connection to the repository
   * @return the enumeration of names
   * @exception RepositoryException if an error occurs
   */
  public RepositoryEnumeration enumerateLinkComponentNames
    (RepositoryConnection connection) throws RepositoryException {

      return componentAccessor.enumerateComponentNames(getConnection(connection));
  }

  /**
   * Lock a link component.
   * @param connection a connection to the repository
   * @param name the name of the link component from the lock...
   * @exception RepositoryException if an error occurs
   *
   * @deprecated Policies can no longer be locked.
   */
  public void lockLinkComponent (RepositoryConnection connection,
                                 String name)
    throws RepositoryException {
  }

  /**
   * Unlock a link component.
   * @param connection a connection to the repository
   * @param name the name of the link component to unlock
   * @exception RepositoryException if an error occurs
   *
   * @deprecated Policies can no longer be locked.
   */
  public void unlockLinkComponent (RepositoryConnection connection,
                                   String name)
    throws RepositoryException {
  }

  // ==========================================================================
  //                          Link assets
  // ==========================================================================

  /**
   * Add the specified link asset to the repository.
   * @param connection a connection to the repository
   * @param linkAsset the link asset that is to be added to the repository
   * @exception RepositoryException if an error occurs
   */
  public void addLinkAsset (RepositoryConnection connection,
                            LinkAsset linkAsset)
    throws RepositoryException {

    assetAccessor.addObject (getConnection(connection), linkAsset);
  }

  /**
   * remove the specified link asset from the repository.
   * @param connection a connection to the repository
   * @param name the name of the associated link component
   * @param deviceName name of the associated device
   * @exception RepositoryException if an error occurs
   */
  public void removeLinkAsset (RepositoryConnection connection,
                               String name,
                               String deviceName)
    throws RepositoryException {
    LinkAssetIdentity identity 
      = new LinkAssetIdentity (defaultProject, name, deviceName);
    assetAccessor.removeObject (getConnection(connection), identity);
  }

  /**
   * Retrieve the specified link asset from the repository
   * @param connection a connection to the repository
   * @param name the name of the associated link component
   * @param deviceName name of the associated device
   * @return the link asset object from the repository
   * @exception RepositoryException if an error occurs
   */
  public LinkAsset retrieveLinkAsset (RepositoryConnection connection,
                                      String name,
                                      String deviceName)
    throws RepositoryException {
    LinkAssetIdentity identity 
      = new LinkAssetIdentity (defaultProject, name, deviceName);
    return (LinkAsset)
      assetAccessor.retrieveObject (getConnection(connection), identity);
  }

  /**
   * Rename the specified link asset entry
   * @param connection a connection to the repository
   * @param name the current name of the link asset
   * @param deviceName name of the associated device
   * @param newName the new name for the link asset
   * @exception RepositoryException if an error occurs
   */
  public void renameLinkAsset (RepositoryConnection connection,
                               String name,
                               String deviceName,
                               String newName)
    throws RepositoryException {
    LinkAssetIdentity identity 
      = new LinkAssetIdentity (defaultProject, name, deviceName);
    assetAccessor.moveAsset (getConnection(connection), identity, newName);
  }

  /**
   * copy the fields of a specified link asset entry to another
   * link asset entry
   * @param connection a connection to the repository
   * @param name the  name of the link asset to copy from
   * @param deviceName name of the associated device
   * @param newName the name for the link asset to copy to
   * @exception RepositoryException if an error occurs
   *
   * @deprecated No longer supported, throws exception.
   */
  public void copyLinkAsset (RepositoryConnection connection,
                             String name,
                             String deviceName,
                             String newName)
    throws RepositoryException {

      throw new UnsupportedOperationException();
  }

  /**
   * delete all link assets associated with a given link component
   * @param connection a connection to the repository
   * @param name name of the associated link component
   * @exception RepositoryException if an error occurs
   */
  public void removeComponentLinkAssets (RepositoryConnection connection,
                                         String name)
    throws RepositoryException {

    assetAccessor.removeChildren (getConnection(connection), name);
  }

  /**
   * Retrieve all the assets for the specified link component from the
   * repository.
   * @param connection a connection to the repository
   * @param name name of the associated link component.
   * @return a Vector containing the LinkComponents retrieved from the
   * repository
   * @exception RepositoryException if an error occurs
   */
  public Vector retrieveComponentLinkAssets (RepositoryConnection connection,
                                             String name)
    throws RepositoryException {

    return new Vector (assetAccessor.retrieveChildren (getConnection(connection), name));
  }

  /**
   * Rename all link assets for a given name
   * @param connection a connection to the repository
   * @param name the current name of the link assets
   * @param newName the new name for the link assets
   * @exception RepositoryException if an error occurs
   */
  public void renameComponentLinkAssets (RepositoryConnection connection,
                                         String name, String newName)
    throws RepositoryException {

    assetAccessor.moveAssets (getConnection(connection), name, newName);
  }

  /**
   * copy the fields of a specified link asset entry to another
   * link asset entry
   * @param connection a connection to the repository
   * @param name the  name of the link asset to copy from
   * @param newName the name for the link asset to copy to
   * @exception RepositoryException if an error occurs
   *
   * @deprecated No longer supported, throws exception.
   */
  public void copyComponentLinkAssets (RepositoryConnection connection,
                                       String name, String newName)
    throws RepositoryException {

      throw new UnsupportedOperationException();
  }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Oct-05	9729/1	geoff	VBM:2005100507 Mariner Export fails with NPE

 25-Jan-05	6712/1	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-Jun-04	4678/1	geoff	VBM:2004061001 old gui cleanup: remove folder support code

 05-Dec-03	2075/3	mat	VBM:2003120106 Correct javadoc and tidy imports

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 19-Nov-03	1964/1	mat	VBM:2003111104 Change add/remove methods to use the LinkRepositoryManager

 19-Nov-03	1955/1	mat	VBM:2003111104 Change add/remove methods to use the LinkRepositoryManager

 ===========================================================================
*/

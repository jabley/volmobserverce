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
 * $Header: /src/voyager/com/volantis/mcs/repository/DynamicVisualRepositoryManager.java,v 1.21 2003/03/12 16:10:43 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 26-Jun-01    Paul            VBM:2001051103 - Added this change history,
 *                              and also added methods to lock, unlock,
 *                              rename and copy.
 * 07-Sep-01    Allan           VBM:2001083118 - Add refreshAssetCache()
 *                              and refreshComponentCache(). Removed
 *                              comments saying "Dynamic Visual components"
 *                              and "Dynamic Visual assets".
 * 27-Sep-01    Allan           VBM:2001091104 - Javadoc
 * 16-Oct-01    Paul            VBM:2001082807 - Removed requester parameter
 *                              from the lock... method.
 * 29-Oct-01    Paul            VBM:2001102901 - Device has moved from
 *                              utilities package to devices package.
 * 02-Jan-02    Paul            VBM:2002010201 - Removed unnecessary import.
 * 31-Jan-02    Paul            VBM:2001122105 - Removed unused imports.
 * 04-Mar-02    Adrian          VBM:2002021908 - modified to use the new auto
 *                              generated dynamic visual asset and component
 *                              accessors.  This includes a change to remove
 *                              the retrieveBest methods as this is now a
 *                              function of DynamicVisualAssetSelectionPolicy
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 08-May-02    Paul            VBM:2002050305 - Use an identity factory to
 *                              create the identity objects.
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
import com.volantis.mcs.assets.DynamicVisualAsset;
import com.volantis.mcs.assets.DynamicVisualAssetIdentity;
import com.volantis.mcs.components.DynamicVisualComponent;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.project.Project;
import com.volantis.synergetics.cache.GenericCache;

import java.util.Vector;

/**
 * This class provides the external interface to the management of dynamic
 * visual components and assets within the repository. These include videos,
 * flash and shockwave movies and Real video.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @deprecated Use {@link com.volantis.mcs.project.PolicyManager}.
 *             This was deprecated in version 3.5.1.
 */
public final class DynamicVisualRepositoryManager extends RepositoryManager {

    /**
     * The type of policies managed by this class.
     */
    private static final PolicyType POLICY_TYPE = PolicyType.VIDEO;

    /**
   * The object that provides access to components.
   */
  private final ComponentAccessor componentAccessor;

  /**
   * The object that provides access to assets.
   */
  private final AssetAccessor assetAccessor;

    /**
   * Creates a new <code>DynamicVisualRepositoryManager</code> instance.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   */
  public DynamicVisualRepositoryManager(RepositoryConnection connection) {

    this(connection.getRepository());
  }

  /**
   * Creates a new <code>DynamicVisualRepositoryManager</code> instance.
   *
   * @param repository the <code>Repository</code> with which this manager is
   * associated
   */
  public DynamicVisualRepositoryManager(Repository repository) {
      this(repository, null);
  }

    /**
     * Initialise.
     *
     * @param repository the <code>Repository</code> with which this manager is
   * associated
     * @param project the project that this manager will access.
     */
    public DynamicVisualRepositoryManager(
            Repository repository, Project project) {
        this(repository, project, null);
    }

    /**
     * Initializes a new <code>DynamicVisualRepositoryManager</code> instance.
     *
     * @param repository         the <code>Repository</code> to be used
     * @param project            the default project.
     * @param policyCacheFlusher allows manager to flush policy caches if used
     *                           at runtime.
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     * @volantis-api-exclude-from InternalAPI
     */
    public DynamicVisualRepositoryManager(
            Repository repository, Project project,
            DeprecatedPolicyCacheFlusher policyCacheFlusher) {
        super(repository, project, policyCacheFlusher);

        // Get the accessors.
        componentAccessor = new ComponentAccessor(accessor,
                defaultProject, POLICY_TYPE);
        assetAccessor = new AssetAccessor(accessor,
                DynamicVisualAsset.class, defaultProject);
    }

    /**
   * Describe <code>addDynamicVisualComponent</code> method here.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param component the <code>DynamicVisualComponent</code> to be added
   * @exception RepositoryException an exception caused during access to the
   * repository
   */
  public void addDynamicVisualComponent(RepositoryConnection connection,
                                        DynamicVisualComponent component)
    throws RepositoryException {
    componentAccessor.addObject (getConnection(connection), component);
  }

  /**
   * Describe <code>removeDynamicVisualComponent</code> method here.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the dynamic visual to be
   * removed
   * @exception RepositoryException an exception caused during access to the
   * repository
   */
  public void removeDynamicVisualComponent(RepositoryConnection connection,
                                           String name)
    throws RepositoryException {

    componentAccessor.removeObject (getConnection(connection), name);
  }

  /**
   * Retrieve the specified dynamic visual component from the repository.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the component to be retrieved
   * @return the required <code>DynamicVisualComponent</code>
   * @exception RepositoryException an exception caused during access to the
   * repository
   */
  public DynamicVisualComponent retrieveDynamicVisualComponent (RepositoryConnection connection,
                                                                String name)
    throws RepositoryException {

    return (DynamicVisualComponent) componentAccessor.
      retrieveObject (getConnection(connection), name);
  }

  /**
   * Rename the specified dynamic visual component in the repository.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the existing name of the component to be renamed
   * @param newName the new name for the component
   * @exception RepositoryException an exception caused during access to the
   * repository
   */
  public void renameDynamicVisualComponent (RepositoryConnection connection,
                                            String name, String newName)
    throws RepositoryException {

    componentAccessor.renameObject (getConnection(connection), name, newName);
  }

  /**
   * Copy the specified dynamic visual component within the repository.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the component to be copied
   * @param newName the name for the new copy of the component
   * @exception RepositoryException an exception caused during access to the
   * repository
   *
   * @deprecated No longer supported, throws exception.
   */
  public void copyDynamicVisualComponent (RepositoryConnection connection,
                                          String name, String newName)
    throws RepositoryException {

      throw new UnsupportedOperationException();
  }

  /**
   * Retrieve all the dynamic visual components from the repository.
   *
   * @param conn the <code>RepositoryConnection</code> to the repository
   * @return the dynamic visual components as a <code>Vector</code>
   * @exception RepositoryException an exception caused during access to the
   * repository
   */
  public Vector retrieveAllDynamicVisualComponents(RepositoryConnection conn)
    throws RepositoryException {

    return new Vector(componentAccessor.retrieveAllComponents(conn)); // TBD OBJECTS
  }

  /**
   * Enumerate the names of all dynamic visual components within the repository
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @return the <code>RepositoryEnumeration</code> of the names of the
   * dynamic visual components
   * @exception RepositoryException an exception caused during access to the
   * repository
   */
  public RepositoryEnumeration enumerateDynamicVisualComponentNames (RepositoryConnection connection)
    throws RepositoryException {
      return componentAccessor.enumerateComponentNames(getConnection(connection));
  }

  /**
   * Lock a dynamicVisual component.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the dynamic visual from the lock...
   * @exception RepositoryException an exception caused during access to the
   * repository
   *
   * @deprecated Policies can no longer be locked.
   */
  public void lockDynamicVisualComponent (RepositoryConnection connection,
                                          String name)
    throws RepositoryException {
  }

  /**
   * Unlock a dynamicVisual component.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the dynamic visual
   * @exception RepositoryException an exception caused during access to the
   * repository
   *
   * @deprecated Policies can no longer be locked.
   */
  public void unlockDynamicVisualComponent (RepositoryConnection connection,
                                            String name)
    throws RepositoryException {
  }

  /**
   * Add the specified dynamic visual asset to the repository associated with
   * a component of the same name
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param dynamicVisualAsset the <code>DynamicVisualAsset</code> to be
   * added
   * @exception RepositoryException an exception caused during access to the
   * repository
   */
  public void addDynamicVisualAsset(RepositoryConnection connection,
                                    DynamicVisualAsset dynamicVisualAsset)
    throws RepositoryException {

    assetAccessor.addObject (getConnection(connection), dynamicVisualAsset);
  }

  /**
   * Remove the specified dynamic visual asset from the repository.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the dynamic visual
   * component for which this asset is to be removed
   * @param encoding the encoding of the asset
   * @param pixelsX the width of the asset in pixels
   * @param pixelsY the height of the asset in pixels
   * @exception RepositoryException an exception caused during access to the
   * repository
   */
  public void removeDynamicVisualAsset(RepositoryConnection connection,
                                       String name,
                                       int encoding, int pixelsX,
                                       int pixelsY)
    throws RepositoryException {

    DynamicVisualAssetIdentity 
      identity = new DynamicVisualAssetIdentity (defaultProject, name,
						 encoding, pixelsX, pixelsY);

    assetAccessor.removeObject (getConnection(connection), identity);
  }

  /**
   * Retrieve the specified dynamic visual asset from the repository.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the dynamic visual
   * component for which this asset is to be retrieved
   * @param encoding the encoding of the asset
   * @param pixelsX the width of the asset in pixels
   * @param pixelsY the height of the asset in pixels
   * @return the requested <code>DynamicVisualAsset</code>
   * @exception RepositoryException an exception caused during access to the
   * repository
   */
  public DynamicVisualAsset retrieveDynamicVisualAsset
    (RepositoryConnection connection, String name,
     int encoding, int pixelsX, int pixelsY)
    throws RepositoryException {

    DynamicVisualAssetIdentity 
      identity = new DynamicVisualAssetIdentity (defaultProject, name,
						 encoding, pixelsX, pixelsY);

    return (DynamicVisualAsset) assetAccessor.retrieveObject (getConnection(connection),
							      identity);
  }

  /**
   * Rename the specified dynamic visual asset.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the existing name of the dynamic visual asset
   * @param encoding the encoding of the asset
   * @param pixelsX the width of the asset in pixels
   * @param pixelsY the height of the asset in pixels
   * @param newName the new name for the dynamic visual asset
   * @exception RepositoryException an exception caused during access to the
   * repository
   */
  public void renameDynamicVisualAsset (RepositoryConnection connection,
                                        String name, int encoding,
                                        int pixelsX, int pixelsY,
                                        String newName)
    throws RepositoryException {

    DynamicVisualAssetIdentity 
      identity = new DynamicVisualAssetIdentity (defaultProject, name,
              encoding, pixelsX, pixelsY);

    assetAccessor.moveAsset (getConnection(connection), identity, newName);
  }

  /**
   * Describe <code>copyDynamicVisualAsset</code> method here.
   *
   * @param connection a <code>RepositoryConnection</code> value
   * @param name a <code>String</code> value
   * @param encoding the encoding of the asset
   * @param pixelsX the width of the asset in pixels
   * @param pixelsY the height of the asset in pixels
   * @param newName a <code>String</code> value
   * @exception RepositoryException an exception caused during access to the
   * repository
   *
   * @deprecated No longer supported, throws exception.
   */
  public void copyDynamicVisualAsset (RepositoryConnection connection,
                                      String name, int encoding,
                                      int pixelsX, int pixelsY,
                                      String newName)
    throws RepositoryException {

      throw new UnsupportedOperationException();
  }

  /**
   * Remove all dynamic visual assets for the specified component.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the dynamic visual
   * component whose assets are to be removed
   * @exception RepositoryException an exception caused during access to the
   * repository
   */
  public void removeComponentDynamicVisualAssets
    (RepositoryConnection connection, String name)
    throws RepositoryException {

    assetAccessor.removeChildren (getConnection(connection), name);
  }

  /**
   * Retrieve all the assets for the specified dynamic visual component from
   * the repository.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the dynamic visual component whose
   * assets are to be retrieved
   * @return the assets for the specified dynamic visual components in a
   *  <code>Vector</code> value
   * @exception RepositoryException an exception caused during access to the
   * repository
   */
  public Vector retrieveComponentDynamicVisualAssets (RepositoryConnection connection,
                                                      String name)
    throws RepositoryException {

    return new Vector(assetAccessor.retrieveChildren (getConnection(connection), name));
  }

  /**
   * Rename all the dynamic visual assets for the specified dynamic visual
   * component.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the existing name of the component and its assets
   * @param newName the new name for the assets
   * @exception RepositoryException an exception caused during access to the
   * repository
   */
  public void renameComponentDynamicVisualAssets(RepositoryConnection connection,
                                                 String name, String newName)
    throws RepositoryException {

    assetAccessor.moveAssets (getConnection(connection), name, newName);
  }

  /**
   * Copy all the dynamic visual assets for the specified dynamic visual
   * component.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the component whose assets are to be copied
   * @param newName the name for the copies of the assets
   * @exception RepositoryException an exception caused during access to the
   * repository
   *
   * @deprecated No longer supported, throws exception.
   */
  public void copyComponentDynamicVisualAssets(RepositoryConnection connection,
                                               String name, String newName)
    throws RepositoryException {

      throw new UnsupportedOperationException();
  }

  /**
   * Set the component cache. This method is for internal use only within the
   * Mariner repository.
   *
   * @param componentCache the <code>GenericCache</code> cache to use
   */
  public void setComponentCache(GenericCache componentCache) {
      // Unused
  }

  /**
   * Set the asset cache. This method is for internal use only within the
   * Mariner repository.
   *
   * @param assetCache the <code>GenericCache</code> to use
   *
   * @deprecated No longer has any effect, use {@link #refreshComponentCache}.
   */
  public void setAssetCache(GenericCache assetCache) {
    throw new UnsupportedOperationException();
  }

    /**
     * Refresh the component cache.
     */
    public void refreshComponentCache() {
        flushCache(POLICY_TYPE);
    }

  /**
   * Refresh the asset cache.
   *
   * @deprecated No longer has any effect, use {@link #refreshComponentCache}.
   */
  public void refreshAssetCache() {
      throw new UnsupportedOperationException();
  }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Oct-05	9729/1	geoff	VBM:2005100507 Mariner Export fails with NPE

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 05-Dec-03	2075/3	mat	VBM:2003120106 Correct javadoc and tidy imports

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 ===========================================================================
*/

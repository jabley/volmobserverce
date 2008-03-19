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
 * $Header: /src/voyager/com/volantis/mcs/repository/ScriptRepositoryManager.java,v 1.7 2003/03/12 16:10:43 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 06-Feb-02    Paul            VBM:2001122103 - Created to provide access
 *                              to script components consistent with the other
 *                              manager classes.
 * 11-Feb-02    Paul            VBM:2001122105 - Removed the
 *                              retrieveBestScriptAsset method.
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
import com.volantis.mcs.assets.ScriptAsset;
import com.volantis.mcs.assets.ScriptAssetIdentity;
import com.volantis.mcs.components.ScriptComponent;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.project.Project;
import com.volantis.synergetics.cache.GenericCache;

import java.util.Collection;

/**
 * This class provides the external interface to the management of
 * script components and assets within the repository.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @deprecated Use {@link com.volantis.mcs.project.PolicyManager}.
 *             This was deprecated in version 3.5.1.
 */
public final class ScriptRepositoryManager
  extends RepositoryManager {

    /**
     * The type of policies managed by this class.
     */
    private static final PolicyType POLICY_TYPE = PolicyType.SCRIPT;

    /**
   * The object that provides access to script components.
   */
  private final ComponentAccessor componentAccessor;

  /**
   * The object that provides access to script assets.
   */
  private final AssetAccessor assetAccessor;

    /**
   * Set the component cache for this manager. This method is for the internal
   * use within the Mariner repository only.
   */
  public void setComponentCache (GenericCache cache) {
        // Unused.
  }

  /**
   * Refresh the component cache.
   */
  public void refreshComponentCache () {
      flushCache(POLICY_TYPE);
  }

  /**
   * Set the asset cache for this manager. This method is for the internal use 
   * within the Mariner repository only.
   *
   * @deprecated No longer has any effect, use {@link #refreshComponentCache}.
   */
  public void setAssetCache (GenericCache cache) {
      throw new UnsupportedOperationException();

  }

  /**
   * Refresh the asset cache.
   *
   * @deprecated No longer has any effect, use {@link #refreshComponentCache}.
   */
  public void refreshAssetCache () {
      throw new UnsupportedOperationException();

  }

  /**
   * Creates a new <code>ScriptRepositoryManager</code> instance.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   */
  public ScriptRepositoryManager (RepositoryConnection connection)  {

    this (connection.getRepository ());
  }

    /**
     * Creates a new <code>ScriptRepositoryManager</code> instance.
     *
     * @param repository the <code>Repository</code> to be used
     */
    public ScriptRepositoryManager(Repository repository) {
        this(repository, null, null);
    }

    /**
     * Initializes a new <code>ScriptRepositoryManager</code> instance.
     *
     * @param repository         the <code>Repository</code> to be used
     * @param project            the default project.
     * @param policyCacheFlusher allows manager to flush policy caches if used
     *                           at runtime.
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     * @volantis-api-exclude-from InternalAPI
     */
    public ScriptRepositoryManager(
            Repository repository, Project project,
            DeprecatedPolicyCacheFlusher policyCacheFlusher) {
        super(repository, project, policyCacheFlusher);

        // Get the accessors.
        componentAccessor = new ComponentAccessor(accessor,
                defaultProject, POLICY_TYPE);
        assetAccessor = new AssetAccessor(accessor, ScriptAsset.class,
                defaultProject);
    }
   
  /**
   * Add the specified script component to the repository.
   *
   * @param connection the <code>RepositoryConnection</code> to the
   * repository
   * @param scriptComponent the <code>ScriptComponent</code> to be added
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public void addScriptComponent (RepositoryConnection connection, 
                                  ScriptComponent scriptComponent)
    throws RepositoryException {

    componentAccessor.addObject (getConnection(connection), scriptComponent);
  }
   
  /**
   * Remove the specified script component from the repository.
   *
   * @param connection a <code>RepositoryConnection</code> to the
   * repository
   * @param name the name of the script component to be removed
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public void removeScriptComponent (RepositoryConnection connection,
                                     String name) 
    throws RepositoryException {

    componentAccessor.removeObject (getConnection(connection), name);
  }
   
  /**
   * Retrieve the specified script component from the repository.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the script component to be retrieved
   * @return the requested <code>ScriptComponent</code>
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public
    ScriptComponent retrieveScriptComponent (RepositoryConnection connection, 
                                             String name) 
    throws RepositoryException {

    return (ScriptComponent) componentAccessor.retrieveObject (getConnection(connection), name);
  }
   
  /**
   * Rename the specified script component in the repository.
   *
   * @param connection the <code>RepositoryConnection</code> to the 
   * repository
   * @param name the existing name of the script component to be retrieved
   * @param newName the new name of the script component
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public void renameScriptComponent (RepositoryConnection connection,
                                     String name, String newName)
    throws RepositoryException {

    componentAccessor.renameObject (getConnection(connection), name, newName);
  }

  /**
   * Copy the specified script component within the repository.
   *
   * @param connection the <code>RepositoryConnection</code> to the 
   * repository
   * @param name the name of the script component to be retrieved
   * @param newName the name of the new copy of the script component
   * @exception RepositoryException an exception caused during access to the 
   * repository
   *
   * @deprecated No longer supported, throws exception.
   */
  public void copyScriptComponent (RepositoryConnection connection,
                                   String name, String newName)
    throws RepositoryException {

      throw new UnsupportedOperationException();
  }

  /**
   * Retrieve all the ScriptComponents in the repository.
   * @param connection <code>RepositoryConnection</code> to the repository
   * @return <code>Collection</code> containing all the ScriptComponents
   * available via the given RepositoryConnection.
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public
    Collection retrieveAllScriptComponents (RepositoryConnection connection) 
    throws RepositoryException {

    return componentAccessor.retrieveAllComponents(getConnection(connection));
  }

  /**
   * Describe <code>enumerateScriptComponentNames</code> method here.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @return a <code>RepositoryEnumeration</code> of the names of the script
   * components
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public RepositoryEnumeration enumerateScriptComponentNames (RepositoryConnection connection)
    throws RepositoryException {
      return componentAccessor.enumerateComponentNames(getConnection(connection));
  }

  /**
   * Lock an script component.
   *
   * @param connection the <code>RepositoryConnection</code> to the
   * repository
   * @param name the name of the script component from the lock...
   * @exception RepositoryException an exception caused during access to the 
   * repository
   *
   * @deprecated Policies can no longer be locked.
   */
  public void lockScriptComponent (RepositoryConnection connection,
                                   String name)
    throws RepositoryException {
  }

  /**
   * Unlock an script component.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the script component from the lock...
   * @exception RepositoryException an exception caused during access to the 
   * repository
   *
   * @deprecated Policies can no longer be locked.
   */
  public void unlockScriptComponent (RepositoryConnection connection,
                                     String name)
    throws RepositoryException {
  }

  /**
   * Add the specified script asset to the repository associated with a 
   * component of the same name.
   *
   * @param connection the <code>RepositoryConnection</code> to the
   * repository
   * @param script the <code>ScriptAsset</code> to be added
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public void addScriptAsset (RepositoryConnection connection, 
                              ScriptAsset script) 
    throws RepositoryException {

    assetAccessor.addObject (getConnection(connection), script);
  }
   
  /**
   * Remove the specified script asset from the repository.
   *
   * @param connection the <code>RepositoryConnection</code> to the
   * repository
   * @param name the name of the script asset to be removed
   * @param deviceName the name of the device the asset is associated with
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public void removeScriptAsset (RepositoryConnection connection,
                                 String name,
                                 String deviceName) 
    throws RepositoryException {

    ScriptAssetIdentity identity
      = new ScriptAssetIdentity (defaultProject, name, deviceName);

    assetAccessor.removeObject (getConnection(connection), identity);
  }
   
  /**
   * Retrieve the specified script asset from the repository.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the script asset to be retrieved
   * @param deviceName the name of the device the asset is associated with
   * @return the requested <code>ScriptAsset</code>
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public ScriptAsset retrieveScriptAsset (RepositoryConnection connection, 
                                          String name,
                                          String deviceName) 
    throws RepositoryException {

    ScriptAssetIdentity identity
      = new ScriptAssetIdentity (defaultProject, name, deviceName);

    return (ScriptAsset) assetAccessor.retrieveObject (getConnection(connection), identity);
  }

  /**
   * Rename the specified script asset.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the script asset to be retrieved
   * @param deviceName the name of the device the asset is associated with
   * @param newName the new name of the script asset
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public void renameScriptAsset (RepositoryConnection connection,
                                 String name,
                                 String deviceName,
                                 String newName)
    throws RepositoryException {

    ScriptAssetIdentity identity
      = new ScriptAssetIdentity (defaultProject, name, deviceName);

    assetAccessor.moveAsset (getConnection(connection), identity, newName);
  }

  /**
   * Copy the specified script asset.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the script asset to be retrieved
   * @param deviceName the name of the device the asset is associated with
   * @param newName the new name of the script asset
   * @exception RepositoryException an exception caused during access to the 
   * repository
   *
   * @deprecated No longer supported, throws exception.
   */
  public void copyScriptAsset (RepositoryConnection connection,
                               String name,
                               String deviceName,
                               String newName)
    throws RepositoryException {

      throw new UnsupportedOperationException();
  }

  /**
   * Remove all script assets for the specified script component.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the script component whose assets 
   * are to be removed
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public void removeComponentScriptAssets (RepositoryConnection connection,
                                           String name)
    throws RepositoryException {

    assetAccessor.removeChildren (getConnection(connection), name);
  }

  /**
   * Retrieve all the script assets in the repository that belong to
   * a specified script component; put them into a Collection and return the 
   * Collection.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the script component whose assets 
   * are to be retrieved
   * @return a <code>Collection</code> containing the requested assets.
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public
    Collection retrieveComponentScriptAssets (RepositoryConnection connection,
                                              String name) 
    throws RepositoryException {

    return assetAccessor.retrieveChildren (getConnection(connection), name);
  }

  /**
   * Rename all the script assets for the specified script component.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the script component whose assets are to be
   * renamed
   * @param newName the new name for the script assets
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public void renameComponentScriptAssets (RepositoryConnection connection,
                                           String name, String newName)
    throws RepositoryException {

    assetAccessor.moveAssets (getConnection(connection), name, newName);
  }

  /**
   * Copy all the script assets for the specified script component.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the script component whose assets are to be copied
   * @param newName the name for the copied script assets
   * @exception RepositoryException an exception caused during access to the 
   * repository
   *
   * @deprecated No longer supported, throws exception.
   */
  public void copyComponentScriptAssets (RepositoryConnection connection,
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

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Dec-03	2075/3	mat	VBM:2003120106 Correct javadoc and tidy imports

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 ===========================================================================
*/

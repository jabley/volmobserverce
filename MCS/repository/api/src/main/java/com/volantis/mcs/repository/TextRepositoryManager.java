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
 * $Header: /src/voyager/com/volantis/mcs/repository/TextRepositoryManager.java,v 1.21 2003/03/12 16:10:43 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 26-Jun-01    Paul            VBM:2001051103 - Added this change history,
 *                              sorted out some comments and changed the
 *                              indentation from the rose style of 3 spaces
 *                              to 2, also added methods to lock, unlock,
 *                              rename and copy, and added retrieve and
 *                              remove methods which assume the default
 *                              language.
 * 10-Sep-01    Allan           VBM:2001083118 - Add refreshAssetCache()
 *                              and refreshComponentCache(). Removed
 *                              comments saying "Text components" and
 *                              "Text assets".
 * 27-Sep-01    Allan           VBM:2001091104 - Javadoc.
 * 16-Oct-01    Paul            VBM:2001082807 - Removed requester parameter
 *                              from the lock... method.
 * 29-Oct-01    Paul            VBM:2001102901 - Device has moved from
 *                              utilities package to devices package.
 * 02-Jan-02    Paul            VBM:2002010201 - Removed unnecessary import.
 * 31-Jan-02    Paul            VBM:2001122105 - Removed unused attributes and
 *                              imports.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from 
 *                              class to string.
 * 22-Mar-02    Adrian          VBM:2002031503 - removed retrieveBest methods
 *                              and modified to use AbstractCachingAccessor
 * 22-Mar-02    Adrian          VBM:2002031503 - removed import of old accessor
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
import com.volantis.mcs.assets.TextAsset;
import com.volantis.mcs.assets.TextAssetIdentity;
import com.volantis.mcs.components.TextComponent;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.project.Project;
import com.volantis.synergetics.cache.GenericCache;

import java.util.Vector;

/**
 * This class provides the external interface to the management of
 * text components and assets within the repository.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @deprecated Use {@link com.volantis.mcs.project.PolicyManager}.
 *             This was deprecated in version 3.5.1.
 */
public final class TextRepositoryManager extends RepositoryManager {

    /**
     * The type of policies managed by this class.
     */
    private static final PolicyType POLICY_TYPE = PolicyType.TEXT;

    /**
   * The object that provides access to text components.
   */
  private final ComponentAccessor componentAccessor;

  /**
   * The object that provides access to text assets.
   */
  private final AssetAccessor assetAccessor;

    /** Set the component cache for this manager. This method is for internal use
   * within the Mariner repository only
   * @param cache The cache to use for text components
   */
  public void setComponentCache (GenericCache cache) {
        // Unused.
  }

  /** Set the asset cache for this manager. This method is for internal use
   * within the Mariner repository only
   * @param cache The cache to use for text assets
   *
   * @deprecated No longer has any effect, use {@link #refreshComponentCache}.
   */
  public void setAssetCache (GenericCache cache) {
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

    /**
     * Constructor
     *
     * @param connection The connection to the repository
     */
    public TextRepositoryManager(RepositoryConnection connection) {
        this(connection.getRepository());
    }

    /**
     * Constructor
     *
     * @param repository The repository with which this manager is associated
     */
    public TextRepositoryManager(Repository repository) {
        this(repository, null);
    }

    public TextRepositoryManager(Repository repository, Project project) {
        this(repository, project, null);
    }

    /**
     * Initializes a new <code>TextRepositoryManager</code> instance.
     *
     * @param repository         the <code>Repository</code> to be used
     * @param project            the default project.
     * @param policyCacheFlusher allows manager to flush policy caches if used
     *                           at runtime.
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     * @volantis-api-exclude-from InternalAPI
     */
    public TextRepositoryManager(
            Repository repository, Project project,
            DeprecatedPolicyCacheFlusher policyCacheFlusher) {
        super(repository, project, policyCacheFlusher);

        // Get the text accessor.
        componentAccessor = new ComponentAccessor(accessor,
                defaultProject, POLICY_TYPE);
        assetAccessor = new AssetAccessor(accessor, TextAsset.class,
                defaultProject);
    }

  /** Add the specified text component to the repository.
   * @param connection The connection to the repository
   * @param textComponent The text component to be added to the repository
   * @throws RepositoryException An exception caused during access to the
   * repository
   */
  public void addTextComponent (RepositoryConnection connection,
                                TextComponent textComponent)
    throws RepositoryException {

    componentAccessor.addObject (getConnection(connection), textComponent);
  }

  /** Remove the specified text component from the repository.
   * @param name The name of the text component to be removed
   * from the repository
   * @param connection The connection to the repository
   * @throws RepositoryException An exception caused during access to the
   * repository
   */
  public void removeTextComponent (RepositoryConnection connection,
                                   String name)
    throws RepositoryException {

    componentAccessor.removeObject (getConnection(connection), name);
  }

  /** Retrieve the specified text component from the repository.
   * @param name The name of the text component to be retrieved
   * from the repository
   * @param connection The connection to the repository
   * @throws RepositoryException An exception caused during access to the
   * repository
   * @return The required text component
   */
  public TextComponent retrieveTextComponent(RepositoryConnection connection,
                                             String name)
    throws RepositoryException {

    return (TextComponent) componentAccessor.retrieveObject (getConnection(connection), name);
  }

  /** Rename the specified text component in the repository.
   * @param name The existing name of the text component
   * @param newName The new name of the text component
   * @param connection The connection to the repository
   * @throws RepositoryException An exception caused during access to the
   * repository
   */
  public void renameTextComponent (RepositoryConnection connection,
                                   String name, String newName)
    throws RepositoryException {

    componentAccessor.renameObject (getConnection(connection), name, newName);
  }

  /** Copy the specified text component within the repository.
   * @param name The name of the text component
   * @param newName The name of the new copy of the text component
   * @param connection The connection to the repository
   * @throws RepositoryException An exception caused during access to the
   * repository
   *
   * @deprecated No longer supported, throws exception.
   */
  public void copyTextComponent (RepositoryConnection connection,
                                 String name, String newName)
    throws RepositoryException {

      throw new UnsupportedOperationException();
  }

  /** Retrieve all text components from the repository
   * @param connection The connection to the repository
   * @throws RepositoryException An exception caused during access to the
   * repository
   * @return The text components in the repository as a Vector
   */
  public Vector retrieveAllTextComponents (RepositoryConnection connection)
    throws RepositoryException {
    return new Vector(componentAccessor.retrieveAllComponents(getConnection(connection)));
  }

  /** Enumerate the names of text components in the repository
   * @param connection The connection to the repository
   * @throws RepositoryException An exception caused during access to the
   * repository
   * @return The text components in the repository as a Vector
   */
  public RepositoryEnumeration enumerateTextComponentNames (RepositoryConnection connection)

    throws RepositoryException {
      return componentAccessor.enumerateComponentNames(getConnection(connection));
  }

  /** Lock a text component.
   * @param connection The connection to the repository
   * @param name The name of the text componebt to be locked
   * @throws RepositoryException An exception caused during access to the
   * repository
   *
   * @deprecated Policies can no longer be locked.
   */
  public void lockTextComponent (RepositoryConnection connection,
                                 String name)
    throws RepositoryException {
  }

  /** Unock a text component.
   * @param connection The connection to the repository
   * @param name The name of the text componebt to be unlocked
   * @throws RepositoryException An exception caused during access to the
   * repository
   *
   * @deprecated Policies can no longer be locked.
   */
  public void unlockTextComponent (RepositoryConnection connection,
                                   String name)
    throws RepositoryException {
  }

  /** Add the specified text asset to the repository.
   * @param connection The connection to the repository
   * @param textAsset The text asset to be added to the repository
   * @throws RepositoryException An exception caused during access to the
   * repository
   */
  public void addTextAsset (RepositoryConnection connection,
                            TextAsset textAsset)
    throws RepositoryException {

    assetAccessor.addObject (getConnection(connection), textAsset);
  }

  /** Remove the specified text asset from the repository.
   * @param name The name of the text component associated with
   * the asset to be removed
   * @param deviceName The name of the device associated with the text asset
   * to be removed
   * @param language The language associated with the text asset to be removed
   * @param connection The connection to the repository
   * @throws RepositoryException An exception caused during access to the
   * repository
   */
  public void removeTextAsset (RepositoryConnection connection,
                               String name,
                               String deviceName,
                               String language)
    throws RepositoryException {

    TextAssetIdentity identity =
      new TextAssetIdentity (defaultProject, name, deviceName, language);
    assetAccessor.removeObject (getConnection(connection), identity);
  }

  /** Remove the specified text asset for the default language from the
   * repository.
   * @param name The name of the text component associated with
   * the asset to be removed
   * @param deviceName The name of the device associated with the text asset
   * to be removed
   * @param connection The connection to the repository
   * @throws RepositoryException An exception caused during access to the
   * repository
   */
  public void removeTextAsset (RepositoryConnection connection,
                               String name,
                               String deviceName)
    throws RepositoryException {

    TextAssetIdentity identity = new TextAssetIdentity
      (defaultProject, name, deviceName, TextAsset.DEFAULT_LANGUAGE);
    assetAccessor.removeObject (getConnection(connection), identity);
  }

  /** Retrieve the specified text asset from the repository.
   * @param name The name of the text component associated with
   * the asset to
   * be retrieved
   * @param deviceName The name of the device associated with the text asset
   * to be retrieved
   * @param language The language associated with the text asset to be
   * retrieved
   * @param connection The connection to the repository
   * @throws RepositoryException An exception caused during access to the
   * repository
   * @return The required text asset
   */
  public TextAsset retrieveTextAsset (RepositoryConnection connection,
                                      String name,
                                      String deviceName,
                                      String language)
    throws RepositoryException {
    TextAssetIdentity identity 
      = new TextAssetIdentity(defaultProject, name, deviceName, language);
    return (TextAsset) assetAccessor.retrieveObject (getConnection(connection), identity);
  }

  /** Retrieve the specified text asset from the repository.
   * @return The required text asset
   * @param name The name of the text component associated with
   * the asset to
   * be retrieved
   * @param deviceName The name of the device associated with the text asset
   * to be retrieved
   * @param connection The connection to the repository
   * @throws RepositoryException An exception caused during access to the
   * repository
   */
  public TextAsset retrieveTextAsset (RepositoryConnection connection,
                                      String name,
                                      String deviceName)
    throws RepositoryException {
    TextAssetIdentity identity = new TextAssetIdentity
      (defaultProject, name, deviceName, TextAsset.DEFAULT_LANGUAGE);
    return (TextAsset) assetAccessor.retrieveObject (getConnection(connection), identity);
  }

  /** Rename the specified text asset in the repository.
   * @param newName The new name for the text asset
   * @param name The existing name of the text asset
   * @param deviceName The name of the device associated with the text asset
   * to be renamed
   * @param language The language associated with the text asset to be renamed
   * @param connection The connection to the repository
   * @throws RepositoryException An exception caused during access to the
   * repository
   */
  public void renameTextAsset (RepositoryConnection connection,
                               String name,
                               String deviceName,
                               String language,
                               String newName)
    throws RepositoryException {
    TextAssetIdentity srcId 
      = new TextAssetIdentity(defaultProject, name, deviceName, language);
    assetAccessor.moveAsset (getConnection(connection), srcId, newName);
  }

  /** Copy the specified text asset within the repository.
   * @param newName The name for the new copy of the text asset
   * @param name The name of the text asset to be copied
   * @param deviceName The name of the device associated with the text asset
   * to be copied
   * @param language The language associated with the text asset to be copied
   * @param connection The connection to the repository
   * @throws RepositoryException An exception caused during access to the
   * repository
   *
   * @deprecated No longer supported, throws exception.
   */
  public void copyTextAsset (RepositoryConnection connection,
                             String name,
                             String deviceName,
                             String language,
                             String newName)
    throws RepositoryException {

      throw new UnsupportedOperationException();
  }

  /** Remove all the assets for the specified text component from the
   * repository.
   * @param name The name of the text component whose assets are
   * to be removed
   * @param connection The connection to the repository
   * @throws RepositoryException An exception caused during access to the
   * repository
   */
  public void removeComponentTextAssets (RepositoryConnection connection,
                                         String name)
    throws RepositoryException {

    assetAccessor.removeChildren (getConnection(connection), name);
  }

  /** Retrieve all the assets for the specified text component from the
   * repository.
   * @param name The name of the text component whose assets are
   * to be retrieved
   * @param connection The connection to the repository
   * @throws RepositoryException An exception caused during access to the
   * repository
   * @return The text assets for the component returned as Vector
   */
  public Vector retrieveComponentTextAssets (RepositoryConnection connection,
                                             String name)
    throws RepositoryException {

    return new Vector (assetAccessor.retrieveChildren (getConnection(connection), name));
  }

  /** Rename all the assets for the specified text component in the repository.
   * @param newName The new name for the assets
   * @param name The name of the text component whose assets are to be renamed
   * @param connection The connection to the repository
   * @throws RepositoryException An exception caused during access to the
   * repository
   */
  public void renameComponentTextAssets (RepositoryConnection connection,
                                         String name, String newName)
    throws RepositoryException {

    assetAccessor.moveAssets (getConnection(connection), name, newName);
  }

  /** Copy all the assets for the specified text component within the
   * repository.
   * @param newName The name for the new copy of the assets
   * @param name The name of the text component whose assets are to be copied
   * @param connection The connection to the repository
   * @throws RepositoryException An exception caused during access to the repository
   *
   * @deprecated No longer supported, throws exception.
   */
  public void copyComponentTextAssets (RepositoryConnection connection,
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

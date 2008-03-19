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
 * $Header: /src/voyager/com/volantis/mcs/repository/AudioRepositoryManager.java,v 1.25 2003/03/12 16:10:43 sfound Exp $
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
 * 10-Sep-01    Allan           VBM:2001083118 - Add refreshAssetCache()
 *                              and refreshComponentCache(). Removed
 *                              comments saying "Audio components" and 
 *                              "Audio assets".
 * 27-Sep-01    Allan           VBM:2001091104 - Javadoc.
 * 16-Oct-01    Paul            VBM:2001082807 - Removed requester parameter
 *                              from the lock... method.
 * 02-Jan-02    Paul            VBM:2002010201 - Removed unnecessary import.
 * 31-Jan-02    Paul            VBM:2001122105 - Improved consistency of naming
 *                              of parameters.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from 
 *                              class to string.
 * 22-Mar-02    Adrian          VBM:2002031503 - removed retrieveBest methods
 *                              and modified to use AbstractCachingAccessor 
 * 22-Mar-02    Adrian          VBM:2002031503 - removed import of old accessor
 * 01-May-02    Mat             VBM:2002040814 - Added a setter for the remote
 *                              cache.
 * 08-May-02    Paul            VBM:2002050305 - Use an identity factory to
 *                              create the identity objects.
 * 01-Aug-02    Phil W-S        VBM:2002080113 - Renamed any retrieveObjects,
 *                              retrieveObjectsImpl, renameObjects,
 *                              renameObjectsImpl and removeObjects methods,
 *                              replacing "Objects" with "Children".
 * 28-Nov-02    Mat             VBM:2002112213 - Added DeviceAudioAsset
 * 12-Mar-02    Steve           VBM:2003022403 - Added API doclet tags
 * 07-May-03    Allan           VBM:2003050704 - Caches are now in the 
 *                              synergetics package. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository;

import com.volantis.mcs.accessors.common.AssetAccessor;
import com.volantis.mcs.accessors.common.ComponentAccessor;
import com.volantis.mcs.assets.AudioAsset;
import com.volantis.mcs.assets.AudioAssetIdentity;
import com.volantis.mcs.assets.DeviceAudioAsset;
import com.volantis.mcs.assets.DeviceAudioAssetIdentity;
import com.volantis.mcs.components.AudioComponent;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.project.Project;
import com.volantis.synergetics.cache.GenericCache;

import java.util.Vector;

/**
 * This class provides the external interface to the management of
 * audio components and assets within the repository.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @deprecated Use {@link com.volantis.mcs.project.PolicyManager}.
 *             This was deprecated in version 3.5.1.
 */
public final class AudioRepositoryManager extends RepositoryManager {

    /**
     * The type of policies managed by this class.
     */
    private static final PolicyType POLICY_TYPE = PolicyType.AUDIO;

    /**
   * The object that provides access to components.
   */
  private final ComponentAccessor componentAccessor;

  /**
   * The object that provides access to audio assets.
   */
  private final AssetAccessor audioAssetAccessor;
  
  /**
   * The object that provides access to device audio assets.
   */
  private final AssetAccessor deviceAudioAssetAccessor;

    /**
   * Set the component cache for this manager. This method is for the internal
   *  use within the Mariner repository only.
   * @volantis-api-exclude-from PublicAPI
   * @volantis-api-exclude-from ProfessionalServicesAPI
   */
  public void setComponentCache(GenericCache cache) {
    // Unused.
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
   * Set the asset cache for this manager. This method is for the internal use 
   * within the Mariner repository only.
   * @volantis-api-exclude-from PublicAPI
   * @volantis-api-exclude-from ProfessionalServicesAPI
   *
   * @deprecated No longer has any effect, use {@link #refreshComponentCache}.
   */
  public void setAssetCache(GenericCache cache) {
      throw new UnsupportedOperationException();
  }


    /**
     * Creates a new <code>AudioRepositoryManager</code> instance.
     *
     * @param repository the <code>Repository</code> to be used
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     */
    public AudioRepositoryManager(Repository repository) {
        this(repository, null);
    }
   
    /**
     * Creates a new <code>AudioRepositoryManager</code> instance.
     *
     * @param repository the <code>Repository</code> to be used
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     * @volantis-api-exclude-from InternalAPI
     */
    public AudioRepositoryManager(Repository repository, Project project) {
        this(repository, project, null);
    }

    /**
     * Initializes a new <code>AudioRepositoryManager</code> instance.
     *
     * @param repository         the <code>Repository</code> to be used
     * @param project            the default project.
     * @param policyCacheFlusher allows manager to flush policy caches if used
     *                           at runtime.
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     * @volantis-api-exclude-from InternalAPI
     */
    public AudioRepositoryManager(
            Repository repository, Project project,
            DeprecatedPolicyCacheFlusher policyCacheFlusher) {
        super(repository, project, policyCacheFlusher);

        // Get the accessors.
        componentAccessor = new ComponentAccessor(accessor,
                defaultProject, POLICY_TYPE);
        audioAssetAccessor = new AssetAccessor(accessor,
                AudioAsset.class, defaultProject);
        deviceAudioAssetAccessor = new AssetAccessor(accessor,
                DeviceAudioAsset.class, defaultProject);
    }

  /**
   * Add the specified audio component to the repository.
   *
   * @param connection the <code>RepositoryConnection</code> to the
   * repository
   * @param audioComponent the <code>AudioComponent</code> to be added
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public void addAudioComponent(RepositoryConnection connection, 
                                AudioComponent audioComponent)
    throws RepositoryException {
    componentAccessor.addObject(getConnection(connection), audioComponent);
  }
   
  /**
   * Remove the specified audio component from the repository.
   *
   * @param connection a <code>RepositoryConnection</code> to the
   * repository
   * @param name the name of the audio component to be removed
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public void removeAudioComponent(RepositoryConnection connection,
                                   String name) 
    throws RepositoryException {

    componentAccessor.removeObject(getConnection(connection), name);
  }
   
  /**
   * Retrieve the specified audio component from the repository.
   *
   * @param conn the <code>RepositoryConnection</code> to the repository
   * @param name the name of the audio component to be retrieved
   * @return the requested <code>AudioComponent</code>
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public AudioComponent retrieveAudioComponent(RepositoryConnection conn, 
                                               String name) 
    throws RepositoryException {

    return (AudioComponent)componentAccessor.retrieveObject(conn, name);
  }
   
  /**
   * Rename the specified audio component in the repository.
   *
   * @param connection the <code>RepositoryConnection</code> to the 
   * repository
   * @param name the existing name of the audio component to be retrieved
   * @param newName the new name of the audio component
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public void renameAudioComponent (RepositoryConnection connection,
                                    String name, String newName)
    throws RepositoryException {

    componentAccessor.renameObject (getConnection(connection), name, newName);
  }

  /**
   * Copy the specified audio component within the repository.
   *
   * @param connection the <code>RepositoryConnection</code> to the 
   * repository
   * @param name the name of the audio component to be retrieved
   * @param newName the name of the new copy of the audio component
   * @exception RepositoryException an exception caused during access to the 
   * repository
   *
   * @deprecated No longer supported, throws exception.
   */
  public void copyAudioComponent (RepositoryConnection connection,
                                  String name, String newName)
    throws RepositoryException {

      throw new UnsupportedOperationException();
  }

  /**
   * Retrieve all the AudioComponents in the repository.
   * @param conn <code>RepositoryConnection</code> to the repository
   * @return <code>Vector</code> containing all the AudioComponents available 
   * via the given RepositoryConnection.
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public Vector retrieveAllAudioComponents(RepositoryConnection conn) 
    throws RepositoryException {
    return new Vector (componentAccessor.retrieveAllComponents(conn)); // TBD OBJECTS
  }

  /**
   * Describe <code>enumerateAudioComponentNames</code> method here.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @return a <code>RepositoryEnumeration</code> of the names of the audio
   * components
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public RepositoryEnumeration enumerateAudioComponentNames(RepositoryConnection connection)
    throws RepositoryException {
      return componentAccessor.enumerateComponentNames(getConnection(connection));
  }

  /**
   * Lock an audio component.
   *
   * @param connection the <code>RepositoryConnection</code> to the
   * repository
   * @param name the name of the audio component from the lock...
   * @exception RepositoryException an exception caused during access to the 
   * repository
   *
   * @deprecated Policies can no longer be locked.
   */
  public void lockAudioComponent (RepositoryConnection connection,
                                  String name)
    throws RepositoryException {
  }

  /**
   * Unlock an audio component.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the audio component from the lock...
   * @exception RepositoryException an exception caused during access to the 
   * repository
   *
   * @deprecated Policies can no longer be locked.
   */
  public void unlockAudioComponent (RepositoryConnection connection,
                                    String name)
    throws RepositoryException {
  }

  /**
   * Add the specified audio asset to the repository associated with a 
   * component of the same name.
   *
   * @param connection the <code>RepositoryConnection</code> to the
   * repository
   * @param audio the <code>AudioAsset</code> to be added
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public void addAudioAsset(RepositoryConnection connection, 
                            AudioAsset audio) 
    throws RepositoryException {

    audioAssetAccessor.addObject(getConnection(connection), audio);
  }
   
  /**
   * Remove the specified audio asset from the repository.
   *
   * @param connection the <code>RepositoryConnection</code> to the
   * repository
   * @param name the name of the audio asset to be removed
   * @param encoding the encoding of the audio asset
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public void removeAudioAsset(RepositoryConnection connection,
                               String name, int encoding) 
    throws RepositoryException {
    AudioAssetIdentity identity
      = new AudioAssetIdentity(defaultProject, name, encoding);
    audioAssetAccessor.removeObject(getConnection(connection), identity);
  }
   
  /**
   * Retrieve the specified audio asset from the repository.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the audio asset to be retrieved
   * @param encoding the encoding of the audio asset
   * @return the requested <code>AudioAsset</code>
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public AudioAsset retrieveAudioAsset(RepositoryConnection connection, 
                                       String name, int encoding) 
    throws RepositoryException {
    AudioAssetIdentity identity
      = new AudioAssetIdentity(defaultProject, name, encoding);
    return (AudioAsset)audioAssetAccessor.retrieveObject(
            getConnection(connection), identity);
  }

  /**
   * Rename the specified audio asset.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the audio asset to be retrieved
   * @param encoding the encoding of the audio asset
   * @param newName the new name of the audio asset
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public void renameAudioAsset (RepositoryConnection connection,
                                String name,
                                int encoding,
                                String newName)
    throws RepositoryException {
    AudioAssetIdentity identity
      = new AudioAssetIdentity(defaultProject, name, encoding);
    audioAssetAccessor.moveAsset (getConnection(connection), identity, newName);
  }

  /**
   * Copy the specified audio asset.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the audio asset to be retrieved
   * @param encoding the encoding of the audio asset
   * @param newName the new name of the audio asset
   * @exception RepositoryException an exception caused during access to the 
   * repository
   *
   * @deprecated No longer supported, throws exception.
   */
  public void copyAudioAsset (RepositoryConnection connection,
                              String name,
                              int encoding,
                              String newName)
    throws RepositoryException {

      throw new UnsupportedOperationException();
  }

  /**
   * Remove all audio assets for the specified audio component.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the audio component whose assets 
   * are to be removed
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public void removeComponentAudioAssets (RepositoryConnection connection,
                                          String name)
    throws RepositoryException {

    audioAssetAccessor.removeChildren (getConnection(connection), name);
  }

  /**
   * Retrieve all the audio assets in the repository that belong to
   * a specified image component; put them into a Vector and return the 
   * Vector.
   *
   * @param conn the <code>RepositoryConnection</code> to the repository
   * @param name the name of the audio component whose assets 
   * are to be retrieved
   * @return a <code>Vector</code> containing the requested assets.
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public Vector retrieveComponentAudioAssets(RepositoryConnection conn,
                                             String name) 
    throws RepositoryException {

    return new Vector (audioAssetAccessor.retrieveChildren(conn, name));
  }

  /**
   * Rename all the audio assets for the specified audio component.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the audio component whose assets are to be renamed
   * @param newName the new name for the audio assets
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public void renameComponentAudioAssets (RepositoryConnection connection,
                                          String name, String newName)
    throws RepositoryException {

    audioAssetAccessor.moveAssets (getConnection(connection), name, newName);
  }

  /**
   * Copy all the audio assets for the specified audio component.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the audio component whose assets are to be copied
   * @param newName the name for the copied audio assets
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public void copyComponentAudioAssets (RepositoryConnection connection,
                                        String name, String newName)
    throws RepositoryException {

      throw new UnsupportedOperationException();
  }
  
  /**
   * Add the specified audio asset to the repository associated with a 
   * component of the same name.
   *
   * @param connection the <code>RepositoryConnection</code> to the
   * repository
   * @param audio the <code>AudioAsset</code> to be added
   * @exception RepositoryException an exception caused during access to the 
   * repository
   *
   * @deprecated No longer supported, throws exception.
   */
  public void addDeviceAudioAsset(RepositoryConnection connection, 
                            AudioAsset audio) 
    throws RepositoryException {

    deviceAudioAssetAccessor.addObject(getConnection(connection), audio);
  }
   
  /**
   * Remove the specified audio asset from the repository.
   *
   * @param connection the <code>RepositoryConnection</code> to the
   * repository
   * @param name the name of the audio asset to be removed
   * @param deviceName the device name of the audio asset
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public void removeDeviceAudioAsset(RepositoryConnection connection,
                               String name, String deviceName) 
    throws RepositoryException {
    DeviceAudioAssetIdentity identity
      = new DeviceAudioAssetIdentity(defaultProject, name, deviceName);
    deviceAudioAssetAccessor.removeObject(getConnection(connection), identity);
  }
   
  /**
   * Retrieve the specified audio asset from the repository.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the audio asset to be retrieved
   * @param deviceName the device name of the audio asset
   * @return the requested <code>AudioAsset</code>
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public AudioAsset retrieveDeviceAudioAsset(RepositoryConnection connection, 
                                       String name, String deviceName) 
    throws RepositoryException {
    DeviceAudioAssetIdentity identity
      = new DeviceAudioAssetIdentity(defaultProject, name, deviceName);
    return (AudioAsset)deviceAudioAssetAccessor.retrieveObject(
            getConnection(connection), identity);
  }

  /**
   * Rename the specified audio asset.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the audio asset to be retrieved
   * @param deviceName the device name of the audio asset
   * @param newName the new name of the audio asset
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public void renameDeviceAudioAsset (RepositoryConnection connection,
                                String name,
                                String deviceName,
                                String newName)
    throws RepositoryException {
    DeviceAudioAssetIdentity identity
      = new DeviceAudioAssetIdentity(defaultProject, name, deviceName);
    deviceAudioAssetAccessor.moveAsset (getConnection(connection), identity,
            newName);
  }

  /**
   * Copy the specified audio asset.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the audio asset to be retrieved
   * @param deviceName the device name of the audio asset
   * @param newName the new name of the audio asset
   * @exception RepositoryException an exception caused during access to the 
   * repository
   *
   * @deprecated No longer supported, throws exception.
   */
  public void copyDeviceAudioAsset (RepositoryConnection connection,
                              String name,
                              String deviceName,
                              String newName)
    throws RepositoryException {

      throw new UnsupportedOperationException();
  }

  /**
   * Remove all audio assets for the specified audio component.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the audio component whose assets 
   * are to be removed
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public void removeComponentDeviceAudioAssets (RepositoryConnection connection,
                                          String name)
    throws RepositoryException {

    deviceAudioAssetAccessor.removeChildren(getConnection(connection), name);
  }

  /**
   * Retrieve all the audio assets in the repository that belong to
   * a specified image component; put them into a Vector and return the 
   * Vector.
   *
   * @param conn the <code>RepositoryConnection</code> to the repository
   * @param name the name of the audio component whose assets 
   * are to be retrieved
   * @return a <code>Vector</code> containing the requested assets.
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public Vector retrieveComponentDeviceAudioAssets(RepositoryConnection conn,
                                             String name) 
    throws RepositoryException {

    return new Vector (deviceAudioAssetAccessor.retrieveChildren(conn, name));
  }

  /**
   * Rename all the audio assets for the specified audio component.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the audio component whose assets are to be renamed
   * @param newName the new name for the audio assets
   * @exception RepositoryException an exception caused during access to the 
   * repository
   */
  public void renameComponentDeviceAudioAssets (RepositoryConnection connection,
                                          String name, String newName)
    throws RepositoryException {

    deviceAudioAssetAccessor.moveAssets(getConnection(connection), name,
            newName);
  }

  /**
   * Copy all the audio assets for the specified audio component.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the audio component whose assets are to be copied
   * @param newName the name for the copied audio assets
   * @exception RepositoryException an exception caused during access to the 
   * repository
   *
   * @deprecated No longer supported, throws exception.
   */
  public void copyComponentDeviceAudioAssets (RepositoryConnection connection,
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

 07-Aug-03	985/1	allan	VBM:2003080502 Stop extending GenericCache in GenericRemotePolicyCache

 ===========================================================================
*/

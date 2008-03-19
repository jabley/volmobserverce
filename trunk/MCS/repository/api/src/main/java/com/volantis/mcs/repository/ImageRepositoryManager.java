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
 * $Header: /src/voyager/com/volantis/mcs/repository/ImageRepositoryManager.java,v 1.37 2003/03/12 16:10:43 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 04-Jun-01    Paul            VBM:2001051103 - Added this change history and
 *                              also added support for locking image components.
 * 26-Jun-01    Paul            VBM:2001051103 - Added methods for locking
 *                              button and rollover components, renamed
 *                              ButtonComponent to ButtonImageComponent. Also
 *                              added methods to rename and copy.
 * 10-Sep-01    Allan           VBM:2001083118 - Add refreshAssetCache()
 *                              and refreshImageComponentCache(). Removed
 *                              comments saying "Device Image components" and
 *                              "Device Image assets" and other comments of
 *                              the same format. Added
 *                              refreshButtonImageComponentCache() and
 *                              refreshRolloverImageComponentCache().
 * 27-Sep-01    Allan           VBM:2001091104 - Javadoc.
 * 15-Oct-01    Paul            VBM:2001101202 - Improved the javadoc and
 *                              internally call the correct accessor depending
 *                              on the type of component being accessed.
 * 16-Oct-01    Paul            VBM:2001082807 - Removed requester parameter
 *                              from the lock... method.
 * 29-Oct-01    Paul            VBM:2001102901 - Device has moved from
 *                              utilities package to devices package.
 * 02-Jan-02    Paul            VBM:2002010201 - Removed unnecessary import.
 * 04-Jan-02    Paul            VBM:2002010403 - Removed dependency on
 *                              MarinerPageContext.
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
 * 27-Aug-02    Ian             VBM:2002081303 - Add support for 
 *                              ConvertibleImageAsset.
 * 06-Sep-02    Ian             VBM:2002081307 - Changes methods for the
 *                              ConvertibleImageAsset to reflect identity
 *                              change.
 * 11-Mar-03    Allan           VBM:2003022103 - Modified all the add and 
 *                              remove ... component type methods to use 
 *                              RepositoryObjectManager instead of the 
 *                              ImageComponentAccessor. 
 * 12-Mar-02    Steve           VBM:2003022403 - Added API doclet tags
 * 07-May-03    Allan           VBM:2003050704 - Caches are now in the 
 *                              synergetics package. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository;

import com.volantis.mcs.accessors.common.AssetAccessor;
import com.volantis.mcs.accessors.common.ComponentAccessor;
import com.volantis.mcs.assets.ConvertibleImageAsset;
import com.volantis.mcs.assets.ConvertibleImageAssetIdentity;
import com.volantis.mcs.assets.DeviceImageAsset;
import com.volantis.mcs.assets.DeviceImageAssetIdentity;
import com.volantis.mcs.assets.GenericImageAsset;
import com.volantis.mcs.assets.GenericImageAssetIdentity;
import com.volantis.mcs.components.ButtonImageComponent;
import com.volantis.mcs.components.ImageComponent;
import com.volantis.mcs.components.RolloverImageComponent;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.project.Project;
import com.volantis.synergetics.cache.GenericCache;

import java.util.Vector;

/**
 * The ImageRepositoryManager class is the external interface to the
 * management of image components and assets within the repository.
 * It implements the ImageRepositoryAccessor interface by invoking
 * methods on the ImageRepositoryAccessor class that it encapsulates.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @deprecated Use {@link com.volantis.mcs.project.PolicyManager}.
 *             This was deprecated in version 3.5.1.
 */
public final class ImageRepositoryManager
        extends RepositoryManager {

    /**
     * One of the type of policies managed by this class.
     */
    private static final PolicyType IMAGE_POLICY_TYPE = PolicyType.IMAGE;

    /**
     * One of the type of policies managed by this class.
     */
    private static final PolicyType BUTTON_IMAGE_POLICY_TYPE =
            PolicyType.BUTTON_IMAGE;

    /**
     * One of the type of policies managed by this class.
     */
    private static final PolicyType ROLLOVER_IMAGE_POLICY_TYPE =
            PolicyType.ROLLOVER_IMAGE;

    /**
     * The object that provides access to components.
     */
    private final ComponentAccessor componentAccessor;

    /**
     * The object that provides access to convertible image assets.
     */
    private final AssetAccessor convertibleImageAccessor;

    /**
     * The object that provides access to script assets.
     */
    private final AssetAccessor deviceImageAccessor;

    /**
     * The object that provides access to script assets.
     */
    private final AssetAccessor genericImageAccessor;

    /**
     * The object that provides access to the button image components
     * in the repository
     */
    private final ComponentAccessor buttonComponentAccessor;

    /**
     * The object that provides access to the rollover image components
     * in the repository
     */
    private final ComponentAccessor rolloverComponentAccessor;

    /**
     * Constructor
     *
     * @param repository The repository associated with this manager
     */
    public ImageRepositoryManager(Repository repository) {
        this(repository, null);
    }

    public ImageRepositoryManager(Repository repository, Project project) {
        this(repository, project, null);
    }

    /**
     * Initializes a new <code>ImageRepositoryManager</code> instance.
     *
     * @param repository         the <code>Repository</code> to be used
     * @param project            the default project.
     * @param policyCacheFlusher allows manager to flush policy caches if used
     *                           at runtime.
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     * @volantis-api-exclude-from InternalAPI
     */
    public ImageRepositoryManager(
            Repository repository, Project project,
            DeprecatedPolicyCacheFlusher policyCacheFlusher) {
        super(repository, project, policyCacheFlusher);

        // Get the accessors.
        componentAccessor =
                new ComponentAccessor(accessor,
                        defaultProject, IMAGE_POLICY_TYPE);

        convertibleImageAccessor = new AssetAccessor(accessor,
                ConvertibleImageAsset.class, defaultProject);

        deviceImageAccessor = new AssetAccessor(accessor,
                DeviceImageAsset.class, defaultProject);

        genericImageAccessor = new AssetAccessor(accessor,
                GenericImageAsset.class, defaultProject);

        // Get the button image accessor.
        buttonComponentAccessor = new ComponentAccessor(
                accessor,
                defaultProject, BUTTON_IMAGE_POLICY_TYPE);

        // Get the rollover image accessor.

        rolloverComponentAccessor = new ComponentAccessor(
                accessor, defaultProject, ROLLOVER_IMAGE_POLICY_TYPE);
    }

    /**
     * Set the image component cache for this manager. This method is for
     * internal use within the Mariner repository only.
     *
     * @param cache The cache to be used
     */
    public void setImageComponentCache(GenericCache cache) {
        // Unused
    }

    /**
     * Set the button image component cache for this manager. This method is for
     * internal use within the Mariner repository only.
     *
     * @param cache The cache to be used
     */
    public void setButtonImageComponentCache(GenericCache cache) {
        // Unused.
    }

    /**
     * Set the rollover image component cache for this manager. This method is
     * for internal use within the Mariner repository only.
     *
     * @param cache The cache to be used
     */
    public void setRolloverImageComponentCache(GenericCache cache) {
        // Unused
    }

    /**
     * Set the convertible image asset cache for this manager. This method is for
     * internal use within the Mariner repository only.
     *
     * @param cache The cache to be used
     * @deprecated No longer has any effect, use {@link #setImageComponentCache}.
     */
    public void setConvertibleAssetCache(GenericCache cache) {
        throw new UnsupportedOperationException();
    }

    /**
     * Set the device image asset cache for this manager. This method is for
     * internal use within the Mariner repository only.
     *
     * @param cache The cache to be used
     * @deprecated No longer has any effect, use {@link #setImageComponentCache}.
     */
    public void setDeviceAssetCache(GenericCache cache) {
        throw new UnsupportedOperationException();
    }

    /**
     * Set the generic image asset cache for this manager. This method is for
     * internal use within the Mariner repository only.
     *
     * @param cache The cache to be used
     * @deprecated No longer has any effect, use {@link #setImageComponentCache}.
     */
    public void setGenericAssetCache(GenericCache cache) {
        throw new UnsupportedOperationException();
    }

    /**
     * Refresh the image component cache
     */
    public void refreshImageComponentCache() {
        flushCache(IMAGE_POLICY_TYPE);
    }

    /**
     * Refresh the button image component cache.
     */
    public void refreshButtonImageComponentCache() {
        flushCache(BUTTON_IMAGE_POLICY_TYPE);
    }

    /**
     * Refresh the rollover image component cache.
     */
    public void refreshRolloverImageComponentCache() {
        flushCache(ROLLOVER_IMAGE_POLICY_TYPE);
    }

    /**
     * Refresh the convertible image asset cache.
     *
     * @deprecated No longer has any effect, use {@link #refreshImageComponentCache}.
     */
    public void refreshConvertibleAssetCache() {
        throw new UnsupportedOperationException();
    }

    /**
     * Refresh the device image asset cache.
     *
     * @deprecated No longer has any effect, use {@link #refreshImageComponentCache}.
     */
    public void refreshDeviceAssetCache() {
        throw new UnsupportedOperationException();
    }

    /**
     * Refresh the generic image asset cache.
     *
     * @deprecated No longer has any effect, use {@link #refreshImageComponentCache}.
     */
    public void refreshGenericAssetCache() {
        throw new UnsupportedOperationException();
    }

    /**
     * Add an image component to the repository
     *
     * @param connection The connection to the repository
     * @param component  The image component to be added to the repository
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public void addImageComponent(
            RepositoryConnection connection,
            ImageComponent component)
            throws RepositoryException {

        componentAccessor.addObject(getConnection(connection), component);
    }

    /**
     * Remove an image component from the repository
     *
     * @param connection The connection to the repository
     * @param name       The name of the image component to be removed from the
     *                   repository
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public void removeImageComponent(
            RepositoryConnection connection,
            String name)
            throws RepositoryException {

        componentAccessor.removeObject(getConnection(connection), name);
    }

    /**
     * Retrieve an image component from the repository
     *
     * @param name       The name of the image component to be removed from the
     *                   repository
     * @param connection The connection to the repository
     * @return The required image component
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public ImageComponent retrieveImageComponent(
            RepositoryConnection connection,
            String name)
            throws RepositoryException {

        return (ImageComponent) componentAccessor.retrieveObject(getConnection(connection),
                name);
    }

    /**
     * Rename an image component in the repository
     *
     * @param name       The existing name of the image component in the repository
     * @param newName    The new name for the image component in the repository
     * @param connection The connection to the repository
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public void renameImageComponent(
            RepositoryConnection connection,
            String name, String newName)
            throws RepositoryException {

        componentAccessor.renameObject(getConnection(connection), name, newName);
    }

    /**
     * Copy an image component within the repository
     *
     * @param name       The name of the image component to be copied
     * @param newName    The name for the new copy of the image component
     * @param connection The connection to the repository
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     * @deprecated No longer supported, throws exception.
     */
    public void copyImageComponent(
            RepositoryConnection connection,
            String name, String newName)
            throws RepositoryException {

        throw new UnsupportedOperationException();
    }

    /**
     * Retrieve all image components in the repository
     *
     * @param connection The connection to the repository
     * @return The image components in the repository as Vector
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public Vector retrieveAllImageComponents(RepositoryConnection connection)
            throws RepositoryException {

        return new Vector(componentAccessor.retrieveAllComponents(getConnection(connection)));
    }

    /**
     * Enumerate the names of the image components in the repository
     *
     * @param connection The connection to the repository
     * @return The enumeration of the names of the image components
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public RepositoryEnumeration enumerateImageComponentNames(
            RepositoryConnection connection)
            throws RepositoryException {
        return componentAccessor.enumerateComponentNames(getConnection(connection));
    }

    /**
     * Lock the specified image component
     *
     * @param connection The connection to the repository
     * @param name       The name of the image component from the lock...
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     * @deprecated Policies can no longer be locked.
     */
    public void lockImageComponent(
            RepositoryConnection connection,
            String name)
            throws RepositoryException {
    }

    /**
     * Unlock the specified image component
     *
     * @param connection The connection to the repository
     * @param name       The name of the image component to unlock
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     * @deprecated Policies can no longer be locked.
     */
    public void unlockImageComponent(
            RepositoryConnection connection,
            String name)
            throws RepositoryException {
    }

    /**
     * Add a button image component to the repository
     *
     * @param connection The connection to the repository
     * @param component  The image component to be added to the repository
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public void addButtonImageComponent(
            RepositoryConnection connection,
            ButtonImageComponent component)
            throws RepositoryException {
        buttonComponentAccessor.addObject(getConnection(connection), component);
    }

    /**
     * Remove a button image component from the repository
     *
     * @param connection The connection to the repository
     * @param name       The name of the image component to be removed from the
     *                   repository
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public void removeButtonImageComponent(
            RepositoryConnection connection,
            String name)
            throws RepositoryException {

        buttonComponentAccessor.removeObject(getConnection(connection), name);
    }

    /**
     * Retrieve a button image component from the repository
     *
     * @param name       The name of the image component to be removed from the
     *                   repository
     * @param connection The connection to the repository
     * @return The required image component
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public ButtonImageComponent
            retrieveButtonImageComponent(
            RepositoryConnection connection,
            String name)
            throws RepositoryException {

        return (ButtonImageComponent)
                buttonComponentAccessor.retrieveObject(getConnection(connection), name);
    }

    /**
     * Rename a button image component in the repository
     *
     * @param name       The existing name of the image component in the repository
     * @param newName    The new name for the image component in the repository
     * @param connection The connection to the repository
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public void renameButtonImageComponent(
            RepositoryConnection connection,
            String name, String newName)
            throws RepositoryException {

        buttonComponentAccessor.renameObject(getConnection(connection), name, newName);
    }

    /**
     * Copy a button image component within the repository
     *
     * @param name       The name of the image component to be copied
     * @param newName    The name for the new copy of the image component
     * @param connection The connection to the repository
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     * @deprecated No longer supported, throws exception.
     */
    public void copyButtonImageComponent(
            RepositoryConnection connection,
            String name, String newName)
            throws RepositoryException {

        throw new UnsupportedOperationException();
    }

    /**
     * Retrieve all button image components in the repository
     *
     * @param connection The connection to the repository
     * @return The image components in the repository as Vector
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public Vector retrieveAllButtonImageComponents(
            RepositoryConnection connection)
            throws RepositoryException {
        return new Vector(
                buttonComponentAccessor.retrieveAllComponents(getConnection(connection)));
    }

    /**
     * Enumerate the names of the button image components in the repository
     *
     * @param connection The connection to the repository
     * @return The enumeration of the names of the image components
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public RepositoryEnumeration
            enumerateButtonImageComponentNames(
            RepositoryConnection connection)
            throws RepositoryException {
        return componentAccessor.enumerateComponentNames(getConnection(connection));
    }

    /**
     * Lock the specified button image component
     *
     * @param connection The connection to the repository
     * @param name       The name of the image component from the lock...
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     * @deprecated Policies can no longer be locked.
     */
    public void lockButtonImageComponent(
            RepositoryConnection connection,
            String name)
            throws RepositoryException {
    }

    /**
     * Unlock the specified button image component
     *
     * @param connection The connection to the repository
     * @param name       The name of the image component to unlock
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     * @deprecated Policies can no longer be locked.
     */
    public void unlockButtonImageComponent(
            RepositoryConnection connection,
            String name)
            throws RepositoryException {
    }

    /**
     * Add a rollover image component to the repository
     *
     * @param connection The connection to the repository
     * @param component  The image component to be added to the repository
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public void addRolloverImageComponent(
            RepositoryConnection connection,
            RolloverImageComponent component)
            throws RepositoryException {

        rolloverComponentAccessor.addObject(getConnection(connection), component);
    }

    /**
     * Remove a rollover image component from the repository
     *
     * @param connection The connection to the repository
     * @param name       The name of the image component to be removed from the
     *                   repository
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public void removeRolloverImageComponent(
            RepositoryConnection connection,
            String name)
            throws RepositoryException {

        rolloverComponentAccessor.removeObject(getConnection(connection), name);
    }

    /**
     * Retrieve a rollover image component from the repository
     *
     * @param name       The name of the image component to be removed from the
     *                   repository
     * @param connection The connection to the repository
     * @return The required image component
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public RolloverImageComponent retrieveRolloverImageComponent(
            RepositoryConnection connection,
            String name)
            throws RepositoryException {

        return (RolloverImageComponent)
                rolloverComponentAccessor.retrieveObject(getConnection(connection), name);
    }

    /**
     * Rename a rollover image component in the repository
     *
     * @param name       The existing name of the image component in the repository
     * @param newName    The new name for the image component in the repository
     * @param connection The connection to the repository
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public void renameRolloverImageComponent(
            RepositoryConnection connection,
            String name, String newName)
            throws RepositoryException {

        rolloverComponentAccessor.renameObject(getConnection(connection), name, newName);
    }

    /**
     * Copy a rollover image component within the repository
     *
     * @param name       The name of the image component to be copied
     * @param newName    The name for the new copy of the image component
     * @param connection The connection to the repository
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     * @deprecated No longer supported, throws exception.
     */
    public void copyRolloverImageComponent(
            RepositoryConnection connection,
            String name, String newName)
            throws RepositoryException {

        throw new UnsupportedOperationException();
    }

    /**
     * Retrieve all rollover image components in the repository
     *
     * @param connection The connection to the repository
     * @return The image components in the repository as Vector
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public Vector retrieveAllRolloverImageComponents(
            RepositoryConnection connection)
            throws RepositoryException {

        return new Vector(
                rolloverComponentAccessor.retrieveAllComponents(getConnection(connection)));
    }

    /**
     * Enumerate the names of the rollover image components in the repository
     *
     * @param connection The connection to the repository
     * @return The enumeration of the names of the image components
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public RepositoryEnumeration enumerateRolloverImageComponentNames(
            RepositoryConnection connection)
            throws RepositoryException {
        return componentAccessor.enumerateComponentNames(getConnection(connection));
    }

    /**
     * Lock the specified rollover image component
     *
     * @param connection The connection to the repository
     * @param name       The name of the image component from the lock...
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     * @deprecated Policies can no longer be locked.
     */
    public void lockRolloverImageComponent(
            RepositoryConnection connection,
            String name)
            throws RepositoryException {
    }

    /**
     * Unlock the specified rollover image component
     *
     * @param connection The connection to the repository
     * @param name       The name of the image component to unlock
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     * @deprecated Policies can no longer be locked.
     */
    public void unlockRolloverImageComponent(
            RepositoryConnection connection,
            String name)
            throws RepositoryException {
    }


    /**
     * Add a convertible image asset to the repository
     *
     * @param connection The connection to the repository
     * @param asset      The convertibe image asset to be added to the repository
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public void addConvertibleImageAsset(
            RepositoryConnection connection,
            ConvertibleImageAsset asset)
            throws RepositoryException {

        convertibleImageAccessor.addObject(getConnection(connection), asset);
    }

    /**
     * Copy a convertibe image asset in the repository
     *
     * @param name    The name of the generic image asset
     * @param newName The name of the new copy of the convertible image asset
     * @throws RepositoryException An exception caused during access to the repository
     * @deprecated No longer supported, throws exception.
     */
    public void copyConvertibleImageAsset(
            RepositoryConnection connection,
            String name,
            String newName)
            throws RepositoryException {

        throw new UnsupportedOperationException();
    }

    /**
     * Remove a convertible image asset from the repository
     *
     * @param name The name of the convertible image asset to be removed from the
     *             repository
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public void removeConvertibleImageAsset(
            RepositoryConnection connection,
            String name)
            throws RepositoryException {
        ConvertibleImageAssetIdentity identity =
                new ConvertibleImageAssetIdentity(defaultProject, name);
        convertibleImageAccessor.removeObject(getConnection(connection), identity);
    }

    /**
     * Rename a convertible image asset in the repository
     *
     * @param newName    The new name of the convertible image asset
     * @param connection The connection to the repository
     * @param name       The existing name of the convertible image asset
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public void renameConvertibleImageAsset(
            RepositoryConnection connection,
            String name,
            String newName)
            throws RepositoryException {
        ConvertibleImageAssetIdentity identity =
                new ConvertibleImageAssetIdentity(defaultProject, name);
        convertibleImageAccessor.moveAsset(getConnection(connection), identity, newName);
    }


    /**
     * Retrieve a convertible image asset from the repository
     *
     * @param connection The connection to the repository
     * @param name       The name of the generic image asset to be retrieved from the
     *                   repository
     * @return The required convertible image asset
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public ConvertibleImageAsset retrieveConvertibleImageAsset(
            RepositoryConnection connection,
            String name)
            throws RepositoryException {
        ConvertibleImageAssetIdentity identity =
                new ConvertibleImageAssetIdentity(defaultProject, name);
        return (ConvertibleImageAsset)
                convertibleImageAccessor.retrieveObject(getConnection(connection), identity);
    }

    /**
     * Add a device image asset to the repository
     *
     * @param connection The connection to the repository
     * @param asset      The device image asset to be added to the repository
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public void addDeviceImageAsset(
            RepositoryConnection connection,
            DeviceImageAsset asset)
            throws RepositoryException {

        deviceImageAccessor.addObject(getConnection(connection), asset);
    }

    /**
     * Remove a device image asset from the repository
     *
     * @param connection The connection to the repository
     * @param name       The name of the device image asset to be removed from the
     *                   repository
     * @param deviceName The name of the device with which this asset is
     *                   associated
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public void removeDeviceImageAsset(
            RepositoryConnection connection,
            String name, String deviceName)
            throws RepositoryException {
        DeviceImageAssetIdentity identity =
                new DeviceImageAssetIdentity(defaultProject, name, deviceName);
        deviceImageAccessor.removeObject(getConnection(connection), identity);
    }

    /**
     * Retrieve a device image asset from the repository
     *
     * @param connection The connection to the repository
     * @param name       The name of the device image asset to be retrieved from the
     *                   repository
     * @param deviceName The name of the device with which this asset is
     *                   associated
     * @return The required device image asset
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public
    DeviceImageAsset retrieveDeviceImageAsset(
            RepositoryConnection connection,
            String name,
            String deviceName)
            throws RepositoryException {
        DeviceImageAssetIdentity identity =
                new DeviceImageAssetIdentity(defaultProject, name, deviceName);
        return (DeviceImageAsset)
                deviceImageAccessor.retrieveObject(getConnection(connection), identity);
    }

    /**
     * Rename a device image asset in the repository
     *
     * @param connection The connection to the repository
     * @param name       The existing name of the device image asset
     * @param newName    The new name of the device image asset
     * @param deviceName The name of the device with which this asset is
     *                   associated
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public void renameDeviceImageAsset(
            RepositoryConnection connection,
            String name,
            String deviceName,
            String newName)
            throws RepositoryException {
        DeviceImageAssetIdentity identity =
                new DeviceImageAssetIdentity(defaultProject, name, deviceName);
        deviceImageAccessor.moveAsset(getConnection(connection), identity, newName);
    }

    /**
     * Copy a device image asset within the repository
     *
     * @param connection The connection to the repository
     * @param name       The name of the device image asset to be copied
     * @param newName    The name of the new copy of the device image asset
     * @param deviceName The name of the device with which this asset is
     *                   associated
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     * @deprecated No longer supported, throws exception.
     */
    public void copyDeviceImageAsset(
            RepositoryConnection connection,
            String name,
            String deviceName,
            String newName)
            throws RepositoryException {

        throw new UnsupportedOperationException();
    }

    /**
     * Remove all the device image assets for the specified image component
     *
     * @param connection The connection to the repository
     * @param name       The name of the image component whose device
     *                   image assets are to be removed
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public
    void removeComponentDeviceImageAssets(
            RepositoryConnection connection,
            String name)
            throws RepositoryException {

        deviceImageAccessor.removeChildren(getConnection(connection), name);
    }

    /**
     * Retrieve all the device image assets for the specified image component
     *
     * @param connection The connection to the repository
     * @param name       The name of the image component whose device
     *                   image assets are to be removed
     * @return The device image assets returned in a Vector
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public
    Vector retrieveComponentDeviceImageAssets(
            RepositoryConnection connection,
            String name)
            throws RepositoryException {

        return new Vector(
                deviceImageAccessor.retrieveChildren(getConnection(connection), name));
    }

    /**
     * Rename all the device image assets for the specified image component
     *
     * @param connection The connection to the repository
     * @param name       The name of the image component whose device image assets
     *                   are to be renamed
     * @param newName    The new name to be associated with the device image assets
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public
    void renameComponentDeviceImageAssets(
            RepositoryConnection connection,
            String name, String newName)
            throws RepositoryException {

        deviceImageAccessor.moveAssets(getConnection(connection), name, newName);
    }

    /**
     * Copy all the device image assets for the specified image component
     *
     * @param connection The connection to the repository
     * @param name       The name of the image component whose device image assets are
     *                   to be copied
     * @param newName    The name to be associated with the new copy of the device
     *                   image assets
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     * @deprecated No longer supported, throws exception.
     */
    public
    void copyComponentDeviceImageAssets(
            RepositoryConnection connection,
            String name, String newName)
            throws RepositoryException {

        throw new UnsupportedOperationException();
    }

    /**
     * Add a generic image asset to the repository
     *
     * @param connection The connection to the repository
     * @param asset      The generic image asset to be added to the repository
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public void addGenericImageAsset(
            RepositoryConnection connection,
            GenericImageAsset asset)
            throws RepositoryException {

        genericImageAccessor.addObject(getConnection(connection), asset);
    }

    /**
     * Remove a generic image asset from the repository
     *
     * @param pixelsX    The width of the image in pixels
     * @param pixelsY    The height of the image in pixels
     * @param pixelDepth The number of bits per pixel
     * @param rendering  The image rendering
     * @param encoding   The image encoding
     * @param widthHint  The width hint for the image
     * @param connection The connection to the repository
     * @param name       The name of the generic image asset to be removed from the
     *                   repository
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public void removeGenericImageAsset(
            RepositoryConnection connection,
            String name,
            int pixelsX,
            int pixelsY,
            int pixelDepth,
            int rendering,
            int encoding,
            int widthHint)
            throws RepositoryException {
        GenericImageAssetIdentity identity =
                new GenericImageAssetIdentity(defaultProject,
                        name, pixelsX, pixelsY, pixelDepth,
                        rendering, encoding, widthHint);
        genericImageAccessor.removeObject(getConnection(connection), identity);
    }

    /**
     * Retrieve a generic image asset from the repository
     *
     * @param pixelsX    The width of the image in pixels
     * @param pixelsY    The height of the image in pixels
     * @param pixelDepth The number of bits per pixel
     * @param rendering  The image rendering
     * @param encoding   The image encoding
     * @param widthHint  The width hint for the image
     * @param connection The connection to the repository
     * @param name       The name of the generic image asset to be retrieved from the
     *                   repository
     * @return The required generic image asset
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public GenericImageAsset retrieveGenericImageAsset(
            RepositoryConnection connection,
            String name,
            int pixelsX,
            int pixelsY,
            int pixelDepth,
            int rendering,
            int encoding,
            int widthHint)
            throws RepositoryException {
        GenericImageAssetIdentity identity =
                new GenericImageAssetIdentity(defaultProject,
                        name, pixelsX, pixelsY, pixelDepth,
                        rendering, encoding, widthHint);
        return (GenericImageAsset)
                genericImageAccessor.retrieveObject(getConnection(connection), identity);
    }

    /**
     * Rename a generic image asset in the repository
     *
     * @param newName    The new name of the generic image asset
     * @param pixelsX    The width of the image in pixels
     * @param pixelsY    The height of the image in pixels
     * @param pixelDepth The number of bits per pixel
     * @param rendering  The image rendering
     * @param encoding   The image encoding
     * @param widthHint  The width hint for the image
     * @param connection The connection to the repository
     * @param name       The existing name of the generic image asset
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public void renameGenericImageAsset(
            RepositoryConnection connection,
            String name,
            int pixelsX,
            int pixelsY,
            int pixelDepth,
            int rendering,
            int encoding,
            int widthHint,
            String newName)
            throws RepositoryException {
        GenericImageAssetIdentity identity =
                new GenericImageAssetIdentity(defaultProject,
                        name, pixelsX, pixelsY, pixelDepth,
                        rendering, encoding, widthHint);
        genericImageAccessor.moveAsset(getConnection(connection), identity, newName);
    }

    /**
     * Copy a generic image asset in the repository
     *
     * @param newName    The name of the new copy of the generic image asset
     * @param pixelsX    The width of the image in pixels
     * @param pixelsY    The height of the image in pixels
     * @param pixelDepth The number of bits per pixel
     * @param rendering  The image rendering
     * @param encoding   The image encoding
     * @param widthHint  The width hint for the image
     * @param connection The connection to the repository
     * @param name       The name of the generic image asset
     * @throws RepositoryException An exception caused during access to the repository
     * @deprecated No longer supported, throws exception.
     */
    public void copyGenericImageAsset(
            RepositoryConnection connection,
            String name,
            int pixelsX,
            int pixelsY,
            int pixelDepth,
            int rendering,
            int encoding,
            int widthHint,
            String newName)
            throws RepositoryException {

        throw new UnsupportedOperationException();
    }

    /**
     * Remove all the generic image assets for the specified image component
     *
     * @param connection The connection to the repository
     * @param name       The name of the image component whose generic
     *                   image assets are to
     *                   be removed
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public
    void removeComponentGenericImageAssets(
            RepositoryConnection connection,
            String name)
            throws RepositoryException {

        genericImageAccessor.removeChildren(getConnection(connection), name);
    }

    /**
     * Retrieve all the generic image assets for the specified image component
     *
     * @param connection The connection to the repository
     * @param name       The name of the image component whose device
     *                   image assets are to be removed
     * @return The device image assets returned in a Vector
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public
    Vector retrieveComponentGenericImageAssets(
            RepositoryConnection connection,
            String name)
            throws RepositoryException {

        return new Vector(
                genericImageAccessor.retrieveChildren(getConnection(connection), name));
    }

    /**
     * Rename all the generic image assets for the specified image component
     *
     * @param connection The connection to the repository
     * @param name       The name of the image component whose generic image assets
     *                   are to be renamed
     * @param newName    The new name to be associated with the device image assets
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public
    void renameComponentGenericImageAssets(
            RepositoryConnection connection,
            String name, String newName)
            throws RepositoryException {

        genericImageAccessor.moveAssets(getConnection(connection), name, newName);
    }

    /**
     * Copy all the generic image assets for the specified image component
     *
     * @param connection The connection to the repository
     * @param name       The name of the image component whose geeric image assets are
     *                   to be copied
     * @param newName    The name to be associated with the new copy of the device
     *                   image assets
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     * @deprecated No longer supported, throws exception.
     */
    public
    void copyComponentGenericImageAssets(
            RepositoryConnection connection,
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

 ===========================================================================
*/

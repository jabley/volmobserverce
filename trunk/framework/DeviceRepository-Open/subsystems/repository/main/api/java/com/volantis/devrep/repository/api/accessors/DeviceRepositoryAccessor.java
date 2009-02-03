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
 * (c) Volantis Systems Ltd 2000-2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.devrep.repository.api.accessors;

import com.volantis.mcs.devices.Device;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.devices.category.CategoryDescriptor;
import com.volantis.mcs.devices.policy.PolicyDescriptor;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.synergetics.cache.GenericCache;

import java.util.List;
import java.util.Locale;

/**
 * This interface contains all the repository access methods needed for
 * Devices.
 *
 * @mock.generate
 */
public interface DeviceRepositoryAccessor {

  /**
   * Set the device cache.
   * @param deviceCache GenericCache to use
   */
  public void setDeviceCache(GenericCache deviceCache);

  /**
   * Refresh the device cache.
   */
  public void refreshDeviceCache();

  /**
   * Add the specified device to the repository.
   *
   * @param connection The connection to use to access the repository.
   * @param device The device to add.
   * @throws RepositoryException If there was a problem accessing the
   * repository.
   */
  public void addDevice (
          RepositoryConnection connection,
          Device device)
    throws RepositoryException;

  /**
   * Remove the specified device from the repository and all of its
   * children.
   *
   * @param connection The connection to use to access the repository.
   * @param deviceName The name of the device to remove.
   * @throws RepositoryException If there was a problem accessing the
   * repository.
   */
  public void removeDevice (
          RepositoryConnection connection,
          String deviceName)
    throws RepositoryException;

  /**
   * Remove the specified device from the repository.
   *
   * @param connection The connection to use to access the repository.
   * @param deviceName The name of the device to remove.
   * @param removeChildren true if the devices children are to be removed.
   * @throws RepositoryException If there was a problem accessing the
   * repository.
   */
  public void removeDevice (
          RepositoryConnection connection,
          String deviceName, boolean removeChildren)
    throws RepositoryException;


  /**
   * Retrieve the specified device from the repository.
   *
   * @param connection The connection to use to access the repository.
   * @param deviceName The name of the device to retrieve.
   * @return The device which was retrieved, or null if it could not be found.
   * @throws RepositoryException If there was a problem accessing the
   * repository.
   */
  public DefaultDevice retrieveDevice (
          RepositoryConnection connection,
          String deviceName)
    throws RepositoryException;

  /**
   * Remove the specified device from the repository.
   *
   * @param connection The connection to use to access the repository.
   * @param deviceName The name of the device to remove.
   * @param newName The new name for the device to remove.
   * @throws RepositoryException If there was a problem accessing the
   * repository.
   */
  public void renameDevice (RepositoryConnection connection,
                            String deviceName, String newName)
    throws RepositoryException;

    /**
   * Remove the policy from the repositoy.
   *
   * A temporary method which will be removed once the user interface
   * manages devices as whole objects.
   * @param connection The connection to use to access the repository.
   * @param policyName The name of the policy to remove.
   * @throws RepositoryException If there was a problem accessing the
   * repository.
   */
  public void removePolicy (RepositoryConnection connection,
                            String policyName)
    throws RepositoryException;

  /**
   * Enumerate the names of the devices in the repository
   *
   * @param connection The connection to use to access the repository.
   * @return The possible empty RepositoryEnumeration of the names of the
   * devices.
   * @throws RepositoryException If there was a problem accessing the
   * repository.
   */
  public RepositoryEnumeration enumerateDeviceNames (RepositoryConnection connection)
    throws RepositoryException;

    /**
     * Retrieve a list of all device names from the repository whose names
     * match the device name pattern supplied.
     *
     * @param connection        The connection to use to access the repository.
     * @param deviceNamePattern The pattern to match device names against.
     *                          This is currently a limited regular expression
     *                          that allows the use of the wildcard chracter.
     *
     * @return A list of names of devices that matched the pattern.
     *
     * @throws RepositoryException If there was a problem accessing the
     *                             repository.
     */
    public List enumerateDeviceNames(RepositoryConnection connection,
                                     String deviceNamePattern)
            throws RepositoryException;

  /**
   * Return an enumeration of device names and the names of the devices that
   * they fallback to.
   *
   * RepositoryEnumeration.next () returns an array of two Strings, the first
   * of which is the name of a device and the second is the name of the device
   * that it falls back to.
   *
   * @param connection The connection to use to access the repository.
   * @return An enumeration of device name and fallback device names.
   */
  public RepositoryEnumeration enumerateDeviceFallbacks (
          RepositoryConnection connection)
    throws RepositoryException;

  /**
   * Return an enumeration of the names of the specified devices children.
   * <p>
   * RepositoryEnumeration.next () returns a String which is the name of a
   * device which fallbacks to the specified device.
   * </p>
   * <p>
   * If the deviceName is null then the root device plus any orphaned devices
   * are returned.
   * </p>
   * @param connection The connection to use to access the repository.
   * @param deviceName The name of the device.
   * @return An enumeration of device names.
   */
  public RepositoryEnumeration enumerateDevicesChildren (
          RepositoryConnection connection,
          String deviceName)
    throws RepositoryException;

  /**
   * Return an enumeration of the device/TAC pairs in the repository
   * <p>
   * RepositoryEnumeration.next() returns a DeviceTACPair associating a
   * given TAC with a device name.
   * </p>
   * <p>
   * If no TAC data is available, this method returns an empty
   * RepositoryEnumeration.
   * </p>
   * @param connection The connection to use to access the repository.
   * @return An enumeration of device/TAC pairs
   */
  public RepositoryEnumeration enumerateDeviceTACs(
          RepositoryConnection connection)
          throws RepositoryException;

    /**
     * Retrieve the device name for a specified TAC.
     *
     * @param connection The connection to use to access the repository.
     * @param TAC The TAC for the device being queried
     * @return The name of the corresponding device, or null if it could not
     *         be found.
     * @throws RepositoryException If there was a problem accessing the
     * repository.
     */
    public String retrieveDeviceName(
            RepositoryConnection connection,
            long TAC)
            throws RepositoryException;

  /**
   * Pre-load the cache with all device patterns for better performance.
   *
   * @param connection The connection to use to access the repository.
   * @throws RepositoryException if something goes wrong
   */
  public void initializeDevicePatternCache (RepositoryConnection connection)
    throws RepositoryException;

  /**
   * Search through the device patterns, trying to match the specified user
   * agent string. If a match is found, either in the cache or from the
   * database then return the name of the matching device.
   *
   * @param userAgent The user agent string to match against.
   * @return The name of the device whose pattern matches the user agent
   * string, or null if no match was found.
   * @throws RepositoryException If there was a problem accessing the
   * repository.
   */
  public String retrieveMatchingDeviceName (String userAgent)
    throws RepositoryException;

  /**
   * Retrieve the specified device and all its fallback devices.
   *
   * @param connection The connection to use to access the repository.
   * @param deviceName The name of the device to retrieve.
   * @return The specified device which has its fallback device property set,
   * or null if the specified device could not be found.
   * @throws RepositoryException If there was a problem accessing the
   * repository.
   */
  public DefaultDevice getDeviceFallbackChain (
          RepositoryConnection connection,
          String deviceName)
    throws RepositoryException;


  /**
   * Update oldPolicyName with newPolicyName
   *
   * @param connection The connection to use to access the repository
   * @param oldPolicyName The old policy name
   * @param newPolicyName The new policy name
   * @exception RepositoryException if an error occurs
   */
  public void updatePolicyName(RepositoryConnection connection,
                                String oldPolicyName, String newPolicyName)
    throws RepositoryException;


    // ==========================================================================
    //   Policy And Policy Descriptor Methods
    // ==========================================================================

    /**
     * Enumerate the names of the policy descriptors in the repository.
     *
     * @param connection The connection to use to access the repository.
     * @return The possible empty RepositoryEnumeration of the names of the
     *      policy descriptors.
     * @throws RepositoryException If there was a problem accessing the
     *      repository.
     */
    RepositoryEnumeration enumeratePolicyNames(
            RepositoryConnection connection) throws RepositoryException;

    /**
     * Enumerate the names of the policy descriptors in the repository which
     * belong to the category provided.
     *
     * @param connection The connection to use to access the repository.
     * @param categoryName The name of the category to restrict the
     *      enumeration to.
     * @return The possible empty RepositoryEnumeration of the names of the
     *      policy descriptors.
     * @throws RepositoryException If there was a problem accessing the
     *      repository.
     */
    RepositoryEnumeration enumeratePolicyNames(
            RepositoryConnection connection, String categoryName)
            throws RepositoryException;

    /**
     * Retrieve the specified policy descriptor from the repository.
     *
     * @param connection The connection to use to access the repository
     * @param policyName The name of the policy.
     * @param locale The locale to be used.
     * @return The policy descriptor which describes the policy.
     * @throws RepositoryException If there was a problem accessing the
     *      repository.
     */
    PolicyDescriptor retrievePolicyDescriptor(
            RepositoryConnection connection,
            String policyName, Locale locale) throws RepositoryException;

    /**
     * Retireve all of the policy descriptors stored for the policy name.
     *
     * @param connection The connection to use to access the repository
     * @param policyName The name of the policy.
     * @return The policy descriptor which describes the policy.
     * @throws RepositoryException If there was a problem accessing the
     *      repository.
     */
    List retrievePolicyDescriptors(RepositoryConnection connection,
                                   String policyName)
        throws RepositoryException;

    /**
     * Retrieve the specified category descriptor from the repository.
     *
     * @param connection The connection to use to access the repository
     * @param categoryName The name of the policy.
     * @param locale The locale to be used.
     * @return The policy descriptor which describes the policy.
     * @throws RepositoryException If there was a problem accessing the
     *      repository.
     */
    CategoryDescriptor retrieveCategoryDescriptor(RepositoryConnection connection,
            String categoryName, Locale locale) throws RepositoryException;


    /**
     * Retireve all of the category descriptors stored for the category name.
     *
     * @param connection The connection to use to access the repository
     * @param categoryName The name of the category.
     * @return The category descriptor which describes the category.
     * @throws RepositoryException If there was a problem accessing the
     *      repository.
     */
    List retrieveCategoryDescriptors(RepositoryConnection connection,
                                     String categoryName)
        throws RepositoryException;

    /**
     * Add the specified policy descriptor to the repository.
     *
     * @param connection The connection to use to access the repository.
     * @param policyName The name of the policy to add the descriptor for.
     * @param descriptor The policy descriptor to add.
     * @throws RepositoryException If there was a problem accessing the
     *      repository.
     */
    void addPolicyDescriptor(
            RepositoryConnection connection,
            String policyName,
            PolicyDescriptor descriptor)
            throws RepositoryException;

    /**
     * Add the specified category descriptor to the repository.
     *
     * @param connection The connection to use to access the repository.
     * @param categoryName The name of the category to add the descriptor for.
     * @param descriptor The category descriptor to add.
     * @throws RepositoryException If there was a problem accessing the
     *      repository.
     */
    void addCategoryDescriptor(
            RepositoryConnection connection,
            String categoryName,
            CategoryDescriptor descriptor)
            throws RepositoryException;

    /**
     * Remove the specified policy descriptor from the repository.
     *
     * @param connection The connection to use to access the repository.
     * @param policyName The name of the policy to remove the descriptor for.
     * @throws RepositoryException If there was a problem accessing the
     *      repository.
     */
    void removePolicyDescriptor(
            RepositoryConnection connection,
            String policyName) throws RepositoryException;

    /**
     * Remove all policy descriptors from the repository.
     *
     * @param connection The connection to use to access the repository.
     * @throws RepositoryException If there was a problem accessing the
     *      repository.
     */
    public void removeAllPolicyDescriptors(
            RepositoryConnection connection) throws RepositoryException;

    /**
     * Enumerate the names of the categories in the repository.
     *
     * @param connection The connection to use to access the repository.
     * @return The possible empty RepositoryEnumeration of the names of the
     *      categories.
     * @throws RepositoryException If there was a problem accessing the
     *      repository.
     */
    RepositoryEnumeration enumerateCategoryNames(RepositoryConnection connection)
        throws RepositoryException;

    /**
     * Remove the specified category descriptors from the repository.
     *
     * @param connection The connection to use to access the repository.
     * @param categoryName The name of the category to remove the descriptors for.
     * @throws RepositoryException If there was a problem accessing the
     *      repository.
     */
    void removeCategoryDescriptor(RepositoryConnection connection,
                                  String categoryName)
        throws RepositoryException;

    /**
     * Remove all category descriptors from the repository.
     *
     * @param connection The connection to use to access the repository.
     * @throws RepositoryException If there was a problem accessing the
     *      repository.
     */
    public void removeAllCategoryDescriptors(RepositoryConnection connection)
            throws RepositoryException;
}

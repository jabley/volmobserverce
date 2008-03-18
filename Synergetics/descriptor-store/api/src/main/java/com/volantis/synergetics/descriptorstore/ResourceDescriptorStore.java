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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.descriptorstore;

/**
 * Represents a Configuration store implementation.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface ResourceDescriptorStore {

    /**
     * Return the generated item corresponding to the specified external ID.
     *
     * @param externalID
     * @return the generated item corresponding to the specified external ID.
     * @throws ResourceDescriptorStoreException if an item with the specified
     * external ID does not exist.
     */
    public ResourceDescriptor getDescriptor(String externalID)
        throws ResourceDescriptorStoreException;


    /**
     * Obtain an item that has the same config parameters as those specified.
     * If one does not already exist in the Store then one will be created
     * and added to the store.
     *
     * @param resourceType the type of the resource descriptor
     * @param configParams the configuration parameters
     * @param names the names of the response parameters that should be
     * returned.
     * @param initialTimeToLive the initial time to live value if a new item has
     * to be created
     * @return a ResourceDescriptor.
     */
    public ResourceDescriptor createDescriptor(String resourceType,
                                               Parameters configParams,
                                               ParameterNames names,
                                               long initialTimeToLive);

    /**
     * Update the stored version of the configuration item.
     *
     * @param descriptor the descriptor to update. This must have been obtained from the
     * {@link #createDescriptor} or {@link #getDescriptor} method
     */
    public void updateDescriptor(ResourceDescriptor descriptor);

    /**
     * Updates the time to live value of the descriptor item corresponding to
     * the specified external ID.
     * <p/>
     * If the time to live value of the descriptor is greater than the specified
     * value, the old value remains unchanged.
     *
     * @param externalId the external ID of the descriptor
     * @param timeToLive the time to live value
     * @throws ResourceDescriptorStoreException if an item with the specified
     * external ID does not exist.
     */
    public void updateDescriptorTimeToLive(String externalId, long timeToLive)
        throws ResourceDescriptorStoreException;

    /**
     * Create a Parameters object
     *
     * @return a new Parameters instance
     */
    public Parameters createParameters();

    /**
     * Create a new ParameterNames instance
     *
     * @return a new ParameterNames instance
     */
    public ParameterNames createParameterNames();


    /**
     * Shuts the configuration store down
     */
    public void shutdown();


}

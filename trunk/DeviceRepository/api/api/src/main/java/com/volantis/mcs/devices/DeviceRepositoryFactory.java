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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.devices;

import com.volantis.mcs.devices.jdbc.JDBCDeviceRepositoryConfiguration;
import com.volantis.mcs.devices.xml.XMLDeviceRepositoryConfiguration;

import java.net.URL;

import javax.sql.DataSource;

/**
 * This factory is used to generate a {@link DeviceRepository} for the given
 * {@link DataSource}or a {@link URL}.
 * 
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
abstract public class DeviceRepositoryFactory {

    

    /**
     * Get the default instance of this factory.
     * 
     * @return The default instance of this factory.
     */
    public static DeviceRepositoryFactory getDefaultInstance() {
        try {
            return (DeviceRepositoryFactory) Class.forName(
                    "com.volantis.devrep.repository.impl.devices.DefaultDeviceRepositoryFactory").
                    newInstance();
        } catch (InstantiationException e) {
            throw new IllegalStateException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    /**
     * Create a new instance of {@link JDBCDeviceRepositoryConfiguration}.
     *
     * @return A new instance of {@link JDBCDeviceRepositoryConfiguration}
     */
    public abstract JDBCDeviceRepositoryConfiguration
            createJDBCDeviceRepositoryConfiguration();


    /**
     * Create a new instance of {@link XMLDeviceRepositoryConfiguration}.
     *
     * @return A new instance of {@link XMLDeviceRepositoryConfiguration}
     */
    public abstract XMLDeviceRepositoryConfiguration
            createXMLDeviceRepositoryConfiguration();

    /**
     * Create a new {@link DeviceRepository} using the information supplied
     * in the {@link JDBCDeviceRepositoryConfiguration}.
     *
     * @param configuration The configuration for the device repository, may
     * not be null. The supplied object MUST NOT be modified after using this
     * method.
     * @return A new instance of {@link DeviceRepository}.
     * @throws DeviceRepositoryException If the {@link DeviceRepository} could
     * not be created, possibly because there was a problem with the
     * configuration.
     */
    public abstract DeviceRepository createDeviceRepository(
            JDBCDeviceRepositoryConfiguration configuration)
            throws DeviceRepositoryException;


    /**
     * Create a new {@link DeviceRepository} using the information supplied
     * in the {@link XMLDeviceRepositoryConfiguration}.
     *
     * @param configuration The configuration for the device repository, may
     * not be null. The supplied object MUST NOT be modified after using this
     * method.
     * @return A new instance of {@link DeviceRepository}.
     * @throws DeviceRepositoryException If the {@link DeviceRepository} could
     * not be created, possibly because there was a problem with the
     * configuration.
     */
    public DeviceRepository createDeviceRepository(
            XMLDeviceRepositoryConfiguration configuration)
            throws DeviceRepositoryException {

            return createDeviceRepository(configuration, null);
    }

    /**
     * Create a new {@link DeviceRepository} using the information supplied
     * in the {@link XMLDeviceRepositoryConfiguration}.
     *
     * @param configuration The configuration for the device repository, may
     * not be null. The supplied object MUST NOT be modified after using this
     * method.
     * @param unknownDevicesLogFileName log file name for abstract and unknown
     * devices
     * @return A new instance of {@link DeviceRepository}.
     * @throws DeviceRepositoryException If the {@link DeviceRepository} could
     * not be created, possibly because there was a problem with the
     * configuration.
     */
    public abstract DeviceRepository createDeviceRepository(
            XMLDeviceRepositoryConfiguration configuration,
            String unknownDEvicesLogFileName)
            throws DeviceRepositoryException;

    /**
     * This method returns the {@link DeviceRepository}for the given JDBC
     * {@link DataSource}.
     *
     * @param datasource
     *            The JDBC {@link DataSource}representing the underlying
     *            repository.
     * @param defaultDeviceProject
     *            The project to which the Device Repository belongs.
     * @throws DeviceRepositoryException
     *             if there is a failure in accessing the underlying repository.
     *
     * @deprecated Use a combination of
     * {@link #createJDBCDeviceRepositoryConfiguration} and
     * {@link #createDeviceRepository(JDBCDeviceRepositoryConfiguration)}.
     */
    public abstract DeviceRepository getDeviceRepository(DataSource datasource,
            String defaultDeviceProject) throws DeviceRepositoryException;

    /**
     * This method returns the {@link DeviceRepository}for the given a URL to
     * the XML Device Repository (.mdpr file)
     *
     * @param repositoryUrl
     *            The URL representing the underlying repository.
     * @throws DeviceRepositoryException
     *             if there is a failure in accessing the underlying repository.
     */
    public DeviceRepository getDeviceRepository(URL repositoryUrl)
            throws DeviceRepositoryException {
        return getDeviceRepository(repositoryUrl, null);
    }

    /**
     * This method returns the {@link DeviceRepository}for the given a URL to
     * the XML Device Repository (.mdpr file)
     *
     * @param repositoryUrl the URL representing the underlying repository
     * @param unknownDevicesLogFileName log file name for abstract and unknown
     * devices
     * @throws DeviceRepositoryException
     *             if there is a failure in accessing the underlying repository.
     */
    public abstract DeviceRepository getDeviceRepository(
                URL repositoryUrl, String unknownDevicesLogFileName)
            throws DeviceRepositoryException;
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 06-Oct-04	5710/1	geoff	VBM:2004052005 Short column name support

 27-Jul-04	4937/1	byron	VBM:2004072201 Public API for Device Repository: retrieve Device based on Request Headers - fix build dependencies

 21-Jul-04	4930/1	geoff	VBM:2004072104 Public API for Device Repository: Basics

 ===========================================================================
*/

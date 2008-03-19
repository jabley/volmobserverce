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
package com.volantis.mcs.eclipse.builder.editors.common;

import com.volantis.mcs.devices.DeviceRepository;
import com.volantis.mcs.devices.DeviceRepositoryException;
import com.volantis.mcs.devices.DeviceRepositoryFactory;
import com.volantis.mcs.eclipse.builder.editors.policies.PolicyEditorContext;
import com.volantis.mcs.eclipse.core.MCSProjectNature;

import java.io.File;
import java.net.MalformedURLException;
import java.lang.reflect.UndeclaredThrowableException;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.CoreException;

/**
 * Provide managed access to the device repository that is associated with
 * the MCS project in use. Managed access in this case means that a new
 * instance of DeviceRepository is only created if the previous instance
 * is older than the last modified date of the device repository file that
 * is associated with the MCS project in use.
 * <p/>
 * This means that access to the latest project device repository is
 * optimised for performance because a new DeviceRepository is only created
 * when it needs to be.
 * <p/>
 * todo - combine somehow with ProjectDeviceRepositoryProvider - seems a shame to have 2 so similar
 */
public class ProjectDeviceRepositoryAccessor {
    /**
     * Class to hold a device repository along with the last modified
     * date of that device repository.
     */
    private static class DeviceRepositoryDetails {
        /**
         * The DeviceRepository.
         */
        private final DeviceRepository deviceRepository;

        /**
         * The last modified date of the associated DeviceRepository.
         */
        private final long lastModifiedDate;

        /**
         * Construct a new DeviceRepositoryDetails.
         *
         * @param deviceRepository the DeviceRepository
         * @param lastModifiedDate the last modified date of the
         *                         DeviceRepository
         */
        public DeviceRepositoryDetails(DeviceRepository deviceRepository,
                                       long lastModifiedDate) {
            this.deviceRepository = deviceRepository;
            this.lastModifiedDate = lastModifiedDate;
        }

        // javadoc unecessary
        public DeviceRepository getDeviceRepository() {
            return deviceRepository;
        }

        // javadoc unecessary
        public long getLastModifiedDate() {
            return lastModifiedDate;
        }
    }

    /**
     * Get the DeviceRepository to reference.
     *
     * @param context the PolicyEditorContext of the policy editor that wants
     *                to reference the project device repository.
     * @return the DeviceRepository associated with the MCS project
     * @throws DeviceRepositoryException if there is a problem with the
     *                                   given device repository file
     * @throws MalformedURLException     if a valid URL cannot be contructed from
     *                                   the given device repository file
     */
    public static DeviceRepository getProjectDeviceRepository(
            PolicyEditorContext context)
            throws MalformedURLException, DeviceRepositoryException {

        DeviceRepositoryDetails drd;

        // Get the path to the device repository from the MCS Project Nature
        // i.e. use the device repository that is set as the device repository
        // for the current MCS project.
        String repositoryFilename = MCSProjectNature.
                getDeviceRepositoryName(context.getProject());

        File devRepFile = new File(repositoryFilename);
        long lastModified = devRepFile.lastModified();

        // The latest known DeviceRepositoryDetails are stored in the projects
        // session so that multiple projects can have different device
        // repositories.
        synchronized (context.getProject()) {
            QualifiedName propertyName =
                    new QualifiedName(
                            ProjectDeviceRepositoryAccessor.class.getName(),
                            "Project Device Repository");
            try {
                drd = (DeviceRepositoryDetails) context.getProject()
                        .getSessionProperty(propertyName);
            } catch (CoreException e) {
                throw new UndeclaredThrowableException(e);
            }


            if (drd == null || lastModified >
                    drd.getLastModifiedDate()) {
                // The device repository on File is more recent than the
                // device repository held in the project session so we need
                // to refresh the device repository we have in the session.
                drd = new DeviceRepositoryDetails(DeviceRepositoryFactory.
                        getDefaultInstance().
                        getDeviceRepository(devRepFile.toURL(),
                        null),
                        lastModified);
                try {
                    context.getProject().setSessionProperty(propertyName, drd);
                } catch (CoreException e) {
                    throw new UndeclaredThrowableException(e);
                }
            }
        }

        return drd.getDeviceRepository();
    }

}

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
package com.volantis.mcs.eclipse.core;

import com.volantis.mcs.repository.RepositoryException;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.JAXPTransformerMetaFactory;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.util.ListenerList;
import org.jdom.input.DefaultJDOMFactory;
import org.jdom.input.JDOMFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Provides accesses to the device repository associated with a project
 * via a DeviceRepositoryAccessorManager. The device repository associated
 * with a project is the device repository specified in the project
 * properties.
 *
 * This class represents what should be the single access point to the
 * device repository for a project. Because it is the single access point
 * it can update the underlying device repository manager when the
 * project device repository property changes or when there is a
 * modification to the device repository iself. This enables all callers
 * to automatically see changes to the project device repository when these
 * changes occur without the need to close and re-open resources.
 */
public class ProjectDeviceRepositoryProvider {

    /**
     * The JDOMFactory used to create DeviceRepositoryAccessorManagers
     */
    private static final JDOMFactory JDOM_FACTORY = new DefaultJDOMFactory();

    /**
     * A map of projects and their associated DeviceRepositoryAccessorManagers
     */
    private Map dramMap = Collections.synchronizedMap(new HashMap());

    /**
     * The map of listeners listening for changes to their project
     * device repository. A map is used because listeners only listen
     * to changes on a specific projects device repository. Map values
     * are ListenerList objects.
     */
    private Map listenerMap = new HashMap();

    /**
     * The singleton instance of this class.
     */
    private final static ProjectDeviceRepositoryProvider singleton =
            new ProjectDeviceRepositoryProvider();

    /**
     * The private constructor.
     */
    private ProjectDeviceRepositoryProvider() {
    }

    /**
     * Get the singleton instance of this class.
     * @return the singleton ProjectDeviceRepositoryProvider
     */
    public static ProjectDeviceRepositoryProvider getSingleton() {
        return singleton;
    }

    /**
     * Get the DeviceRepositoryAccessorManager for a given project.
     * @param project the IProject
     * @return the DeviceRepositoryAccessorManager managing the
     * device repository associated with the given IProject
     */
    public DeviceRepositoryAccessorManager
            getDeviceRepositoryAccessorManager(IProject project)
            throws RepositoryException {
        DeviceRepositoryAccessorManager dram = (DeviceRepositoryAccessorManager)
                dramMap.get(project);
        if (dram == null) {
            dram = createProjectDRAM(project);
            dramMap.put(project, dram);
        }

        return dram;
    }

    /**
     * Set the DeviceRepositoryAccessorManager for a given project. Listeners
     * will only be notified if the repository file in the new dram is
     * different from the original - assuming there was an original.
     * @param project the IProject that the device repository is
     * associated with
     * @param dram the DeviceRepositoryAccessorManager for the project
     * device repository
     */
    public void setDeviceRepositoryAccessorManager(IProject project,
                                                   DeviceRepositoryAccessorManager dram) {
        DeviceRepositoryAccessorManager orig = (DeviceRepositoryAccessorManager)
                dramMap.get(project);
        dramMap.put(project, dram);
        if (orig != null && !orig.getDeviceRepositoryName().
                equals(dram.getDeviceRepositoryName())) {
            notifyChangeListeners(project);
        }
    }

    /**
     * Add a listener that listens for changes to a project device repository.
     *
     * @param listener the ProjectDeviceRepositoryListener to add
     * @param project the IProject that the ProjectDeviceRepositoryListener
     * is associated with
     */
    public synchronized void addProjectDeviceRepositoryChangeListener(
            ProjectDeviceRepositoryChangeListener listener,
            IProject project) {
        ListenerList list = (ListenerList) listenerMap.get(project);
        if (list == null) {
            list = new ListenerList();
        }
        list.add(listener);
        listenerMap.put(project, list);
    }

    /**
     * Remove a listener that listens for changes to a project device
     * repository.
     * @param listener the ProjectDeviceRepositoryListener to remove
     * @param project the IProject that the ProjectDeviceRepositoryListener
     * is associated with
     */
    public synchronized void removeProjectDeviceRepositoryChangeListener(
            ProjectDeviceRepositoryChangeListener listener,
            IProject project) {

        ListenerList listeners = (ListenerList) listenerMap.get(project);
        if (listeners != null) {
            listeners.remove(listener);
            if (listeners.size() == 0) {
                listenerMap.remove(project);
            }
        }
    }

    /**
     * Inform the ProjectDeviceRepositoryProvider that the device
     * repository associated with a project has changed.
     * @param project the IProject
     * @param repositoryName the full path name of the new repository
     * associated with the project
     */
    void updateProjectDeviceRepository(IProject project,
                                       String repositoryName)
            throws RepositoryException {
        DeviceRepositoryAccessorManager dram = createProjectDRAM(project);
        dramMap.put(project, dram);
        notifyChangeListeners(project);
    }

    /**
     * Inform the ProjectDeviceRepositoryProvider that a device repository
     * has been updated. Note that it would be nicer to be able to
     * listen for changes to the resource for the device repository file and
     * the project file directly but currently Eclipse (v3.0) does not
     * provide a useful means of doing this. Therefore this class relies
     * on callers to call this method when it should be called.
     * @param dram the DeviceRepositoryAccessorManager that performed
     * the modification
     */
    void deviceRepositoryModified(DeviceRepositoryAccessorManager dram)
            throws RepositoryException {
        // Search through the dramMap. If the map contains a dram associated
        // with the same repository as the given dram then a project device
        // repository has been modified. If so change the stored dram to be
        // the given dram which contains the latest modification. Note that a
        // device repository could be associated with multiple projects.
        Iterator projects = dramMap.keySet().iterator();
        while (projects.hasNext()) {
            IProject project = (IProject) projects.next();
            DeviceRepositoryAccessorManager storedDRAM =
                    (DeviceRepositoryAccessorManager) dramMap.get(project);
            if (storedDRAM.getDeviceRepositoryName().
                    equals(dram.getDeviceRepositoryName())) {
                dramMap.put(project, dram);
                notifyChangeListeners(project);
            }
        }
    }

    /**
     * Create a DeviceRepositoryAccessorManager for a given project.
     * @param project the IProject
     * @return the DeviceRepositoryAccessorManager for the IProject
     * @throws RepositoryException if there is a problem creating the
     * DeviceRepositoryAccessorManager
     */
    private DeviceRepositoryAccessorManager createProjectDRAM(IProject project)
            throws RepositoryException {
        DeviceRepositoryAccessorManager dram = null;
        try {
            String repositoryFilename =
                    MCSProjectNature.getDeviceRepositoryName(project);
            
            if(repositoryFilename == null) {
                throw new RepositoryException("Cannot resolve repository file name");
            }
            
            dram = new DeviceRepositoryAccessorManager(
                    repositoryFilename, new JAXPTransformerMetaFactory(),
                    JDOM_FACTORY, false);
        } catch (IOException e) {
            throw new RepositoryException(e);
        }

        return dram;
    }

    /**
     * Notify ProjectDeviceRepositoryChangeListeners that there has been
     * a change to a project device repository.
     * @param project the IProject whose device repository has changed
     */
    private synchronized void notifyChangeListeners(IProject project) {
        ListenerList list = (ListenerList) listenerMap.get(project);
        if (list != null) {
            Object listeners [] = list.getListeners();
            for (int i = 0; i < listeners.length; i++) {
                ((ProjectDeviceRepositoryChangeListener) listeners[i]).
                        changed();
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Dec-05	10539/3	adrianj	VBM:2005111712 fixed up merge conflicts

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 14-Jan-05	6681/3	allan	VBM:2004081607 Rework issues

 14-Jan-05	6681/1	allan	VBM:2004081607 Allow device selectors and browser to see project device repository changes

 ===========================================================================
*/

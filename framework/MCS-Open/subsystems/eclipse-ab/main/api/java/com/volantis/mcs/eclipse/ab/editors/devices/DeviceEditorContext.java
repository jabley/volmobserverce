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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.editors.devices;

import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.ab.editors.SaveCommand;
import com.volantis.mcs.eclipse.ab.editors.SaveCommandFactory;
import com.volantis.mcs.eclipse.ab.editors.devices.odom.DeviceODOMElementFactory;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorContext;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMFactory;
import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoMementoOriginator;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import com.volantis.mcs.xml.validation.sax.ParserErrorException;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.xml.sax.SAXException;

import java.util.Collection;
import java.util.Iterator;

/**
 * A DeviceEditorContext is a kind of ODOMEditorContext that has extensions
 * specific to Devices.
 */
public class DeviceEditorContext extends ODOMEditorContext {
    /**
     * The device odom factory.
     */
    private static final ODOMFactory DEVICE_ODOM_FACTORY =
            new DeviceODOMElementFactory();

    /**
     * A DeviceRepositoryAccessorManager that can be used to access device
     * specific data.
     */
    private DeviceRepositoryAccessorManager deviceRepositoryAccessorManager;


    /**
     * Initializes a <code>DeviceEditorContext</code> with the given
     * arguments.
     * @param repositoryFile The <code>IFile</code> for the device repository
     * file. Cannot be null.
     * @param undoRedoMementoOriginator the
     * <code>UndoRedoMementoOriginator</code> for this context's
     * UndoRedoManager. Cannot be null.
     * @param rootElement the root Element for the innitial device repository
     * document
     * @param rootElementIdentifier the identifier for the root element i.e.
     * that identifies which document the root Element comes from
     * @throws IllegalArgumentException If any of the arguments are null.
     */
    private DeviceEditorContext(
            IFile repositoryFile,
            UndoRedoMementoOriginator undoRedoMementoOriginator,
            ODOMElement rootElement,
            String rootElementIdentifier,
            DeviceRepositoryAccessorManager deviceRepositoryAccessorManager)
            throws SAXException, ParserErrorException {
        super(repositoryFile,
                undoRedoMementoOriginator,
                rootElement, rootElementIdentifier);
        if (deviceRepositoryAccessorManager == null) {
            throw new IllegalArgumentException(
                    "deviceRepositoryAccessor cannot be null");
        }
        this.deviceRepositoryAccessorManager = deviceRepositoryAccessorManager;
    }

    /**
     * Creates a <code>DeviceEditorContext</code> instance
     * @param repositoryFile The <code>IFile</code> for the device repository
     * file. Cannot be null.
     * @param mementoOriginator the UndoRedoMemntoOriginator for this context
     * @param dram the DeviceRepositoryAccessorManager for this context
     * @return a DeviceEditorContext instance.
     */
    public static DeviceEditorContext createDeviceEditorContext(
            IFile repositoryFile,
            UndoRedoMementoOriginator mementoOriginator,
            DeviceRepositoryAccessorManager dram) {

        DeviceEditorContext context = null;

        try {
            ODOMElement hierarchyRoot =
                    (ODOMElement) dram.getDeviceHierarchyDocument().
                    getRootElement();

            context = new DeviceEditorContext(
                    repositoryFile,
                    mementoOriginator,
                    hierarchyRoot,
                    hierarchyRoot.getName(),
                    dram);
        } catch (ParserErrorException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        } catch (SAXException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }
        return context;
    }

    /**
     * DeviceEditorContexts may have more than one root elements (hierarchy,
     * identification, tacidentification, ...) so this method returns the
     * hierachy root element.
     *
     * If the editor has more than one hierarchy root element it throws an
     * IllegalStateException, if the editor has no hierarchy root element,
     * returns null.
     *
     * @return the hierarchy root element
     */
    public ODOMElement getRootElement() {

        final Collection rootElements = getRootElements();

        ODOMElement rootElement = null;

        for (Iterator iter = rootElements.iterator(); iter.hasNext();) {

            final ODOMElement element = (ODOMElement) iter.next();
            if (DeviceRepositorySchemaConstants.HIERARCHY_ELEMENT_NAME.equals(
                    element.getName()) &&
                MCSNamespace.DEVICE_HIERARCHY.getURI().equals(
                    element.getNamespaceURI())) {
                if (rootElement == null) {
                    rootElement = element;
                } else {
                    throw new IllegalStateException(
                        "More than one hierarchy root element.");
                }
                break;
            }
        }

        return rootElement;
    }

    /**
     * Override addRootElement to ensure that rootElementIdentifier is not
     * null for DeviceEditor root elements.
     *
     * Add a rootElement to be managed by this ODOMEditorContext. If the
     * given element is already managed then this method does nothing.
     *
     * The added element is validated as it is added.
     *
     * @param rootElement the root ODOMElement to be managed.
     * @param rootElementIdentifier a String that is uniquely associated with
     * the root element. This String is used for generating resource markers
     * and in the case of editors that edit multiple files within the same
     * resource is used to identify which file the problem is associated with
     * - e.g. the DeviceEditor.
     * @throws SAXException if there is a validation problem
     * @throws ParserErrorException if there is a validation problem
     * @throws IllegalArgumentException if rootElement is not a root element or
     * rootElementIdentifier is null.
     */
    public synchronized void addRootElement(ODOMElement rootElement,
                                            String rootElementIdentifier)
            throws SAXException, ParserErrorException {
        if(rootElementIdentifier==null) {
            throw new IllegalArgumentException("Cannot be null: " +
                    "rootElementIdentifier");
        }
        super.addRootElement(rootElement, rootElementIdentifier);
    }

    /**
     * Returns a {@link DeviceRepositoryAccessorManager} instance.
     * @return a <code>DeviceRepositoryAccessorManager</code>.
     */
    public DeviceRepositoryAccessorManager
            getDeviceRepositoryAccessorManager() {
        return deviceRepositoryAccessorManager;
    }

    // javadoc inherited
    protected SaveCommandFactory createSaveCommandFactory() {
        return new DeviceSaveCommandFactory();
    }

    // javadoc inherited
    public ODOMFactory getODOMFactory() {
        return DEVICE_ODOM_FACTORY;
    }

    /**
     * A <code>SaveCommandFactory</code> implementation that saves the device
     * repository associated with this context.
     */
    private class DeviceSaveCommandFactory implements SaveCommandFactory {
        // javadoc inherited
        public SaveCommand createSaveAsCommand(final IFile destinationFile) {
            return new SaveCommand() {
                // javadoc inherited
                public void save(IProgressMonitor progressMonitor)
                        throws CoreException {
                    try {
                        String filename = destinationFile.getLocation().toFile().
                                getAbsolutePath();
                        getDeviceRepositoryAccessorManager().
                                setRepositoryName(filename);
                        getDeviceRepositoryAccessorManager().writeRepository();
                    } catch (IllegalArgumentException e) {
                        EclipseCommonPlugin.handleError(ABPlugin.getDefault(),
                                e);
                    } catch (RepositoryException e) {
                        EclipseCommonPlugin.handleError(
                                ABPlugin.getDefault(), e);
                    }
                }
            };
        }

        // javadoc inherited
        public SaveCommand createSaveCommand() {
            // javadoc inherited
            return new SaveCommand() {
                // javadoc inherited
                public void save(IProgressMonitor progressMonitor)
                        throws CoreException {
                    // Ask the DeviceRepositoryAccessorManager to save the
                    // repository
                    try {
                        getDeviceRepositoryAccessorManager().writeRepository();
                    } catch (RepositoryException e) {
                        EclipseCommonPlugin.handleError(
                                ABPlugin.getDefault(), e);
                    }
                }
            };
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Nov-04	6012/4	allan	VBM:2004051307 Remove standard elements in admin mode.

 16-Nov-04	4394/3	allan	VBM:2004051018 Undo/Redo in device editor.

 13-May-04	4301/1	byron	VBM:2004051018 CC/PP section does not handle undo/redo

 10-Sep-04	5488/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Sep-04	5432/3	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Sep-04	5432/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 16-Aug-04	5206/1	allan	VBM:2004081201 Auto-migration of mdpr with dialog.

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 10-May-04	4239/2	allan	VBM:2004042207 SaveAs on DeviceEditor.

 27-Apr-04	4016/2	allan	VBM:2004031010 DevicePoliciesPart and CategoriesSection.

 22-Apr-04	3878/4	doug	VBM:2004032405 Created a basic DeviceEditor and overview page

 16-Apr-04	3743/1	doug	VBM:2004032101 Added a DeviceEditorContext class

 ===========================================================================
*/

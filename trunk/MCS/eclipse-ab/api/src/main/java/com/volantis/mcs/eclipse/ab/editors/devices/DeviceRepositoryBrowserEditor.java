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
package com.volantis.mcs.eclipse.ab.editors.devices;

import com.volantis.mcs.eclipse.ab.views.devices.DeviceRepositoryBrowserPage;
import com.volantis.mcs.eclipse.ab.views.devices.DeviceRepositoryForm;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.JAXPTransformerMetaFactory;
import com.volantis.synergetics.UndeclaredThrowableException;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.jdom.input.DefaultJDOMFactory;

/**
 * This is a view only EditorPart that allows viewing of the device
 * hierarchy, device patterns and device policy values.
 */
public class DeviceRepositoryBrowserEditor extends EditorPart {

    /**
     * The DeviceRepositoryAccessorManager associated with this
     * DeviceRepositoryBrowserEditor.
     */
    private DeviceRepositoryAccessorManager dram;

    public void doSave(IProgressMonitor iProgressMonitor) {
        // can't save becuase this editor is view only.
    }

    public void doSaveAs() {
        // not implemented - this is view only
    }

    public void gotoMarker(IMarker iMarker) {
        // there can never be any markers since this editor doesn't edit
    }

    // javadoc inherited
    public void init(IEditorSite editorSite, IEditorInput editorInput)
            throws PartInitException {

        // Assume that the IEditorInput is a file i.e the device repository file
        IFile file = getFile(editorInput);

        try {
            String fileName = file.getLocation().toOSString();
            dram = new DeviceRepositoryAccessorManager(fileName,
                    new JAXPTransformerMetaFactory(),
                    new DefaultJDOMFactory(), false);
        } catch (IOException e) {
            // PartInitExceptions are not logged by Eclipse so throw an
            // UndeclaredThrowable...
            throw new UndeclaredThrowableException(e, e.getMessage());
        } catch (RepositoryException e) {
            // PartInitExceptions are not logged by Eclipse so throw an
            // UndeclaredThrowable...
            throw new UndeclaredThrowableException(e, e.getMessage());
        }

        setTitle(file.getName());
        setSite(editorSite);
        setInput(editorInput);
    }


    /**
     * Override getAdapter() to provide the device repository page to callers.
     */
    // rest of javadoc inherited
    public Object getAdapter(Class adapterClass) {
        Object adapter = null;
        if (DeviceRepositoryBrowserPage.class.equals(adapterClass)) {
            adapter = new DeviceRepositoryBrowserPage(dram);
        } else {
            adapter = super.getAdapter(adapterClass);
        }

        return adapter;
    }

    /**
     * This always returns false because this editor only allows viewing hence
     * the content can never be dirty.
     * @return boolean flag
     */
    public boolean isDirty() {
        return false;
    }

    public boolean isSaveAsAllowed() {
        // can't save
        return false;
    }

    // javadoc inherited.
    public void createPartControl(Composite composite) {
        new DeviceRepositoryForm(composite, SWT.NONE,
                dram);
    }

    public void setFocus() {
    }


    /**
     * Get the IFile assoiciated with a given IEditorInput.
     * @param input The IEditorInput.
     * @return The IFile associated with the given IEditorInput.
     * @throws IllegalArgumentException If an IFile cannot be obtained
     * from the given IEditorInput.
     */
    protected IFile getFile(IEditorInput input) {
        IFile file = null;
        if (input instanceof IFileEditorInput) {
            file = ((IFileEditorInput) input).getFile();
        } else {
            throw new IllegalStateException("Unsupported IEditorInput: " + //$NON-NLS-1$
                    getEditorInput());
        }

        return file;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 14-Jan-05	6681/1	allan	VBM:2004081607 Allow device selectors and browser to see project device repository changes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 17-Nov-04	6012/1	allan	VBM:2004051307 Remove standard elements in admin mode.

 25-Aug-04	5298/2	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 17-May-04	4231/1	tom	VBM:2004042704 Fixedup the 2004032606 change

 16-Apr-04	3362/4	steve	VBM:2003082208 supermerged

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 08-Apr-04	3806/1	doug	VBM:2004040810 Paramaterized the DeviceRepositoryAccessorManager and the XMLDeviceRepositoryAccessor contstructors with a JDOMFactory

 11-Feb-04	2862/3	allan	VBM:2004020411 The DeviceRepositoryBrowser.

 ===========================================================================
*/

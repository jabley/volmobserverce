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
package com.volantis.mcs.eclipse.ab.views.devices;

import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;

import java.text.MessageFormat;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.MessagePage;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;

/**
 * A view that provides browsing capability of the device repository
 * associated with the current MCS project.
 */
public class DeviceRepositoryBrowser extends PageBookView {

    /**
     * The prefix for resources associated with this view.
     */
    private static final String RESOURCE_PREFIX = "DeviceRepositoryBrowser.";

    /**
     * Message to show on the default page.
     */
    private static final String DEFAULT_TEXT =
            DevicesMessages.getString(RESOURCE_PREFIX + "default.text");

    /**
     * The title format string.
     */
    private static final String TITLE_FORMAT =
            DevicesMessages.getString(RESOURCE_PREFIX + "title");


    /**
     * Default constructor.
     */
    public DeviceRepositoryBrowser() {
    }

    // javadoc inherited
    public void setFocus() {
    }

    // javadoc inherited
    protected IPage createDefaultPage(PageBook book) {
        MessagePage page = new MessagePage();
        initPage(page);
        page.createControl(book);
        page.setMessage(DEFAULT_TEXT);
        return page;
    }

    // javadoc inherited
    protected PageBookView.PageRec doCreatePage(IWorkbenchPart part) {
        PageBookView.PageRec pageRec = null;
        Object obj = part.getAdapter(DeviceRepositoryBrowserPage.class);
        if (obj instanceof DeviceRepositoryBrowserPage) {
            DeviceRepositoryBrowserPage page =
                    (DeviceRepositoryBrowserPage) obj;
            initPage(page);
            page.createControl(getPageBook());

            // Use the manager to retrieve version and revision information
            DeviceRepositoryAccessorManager deviceRAM =
                    page.getDeviceRepositoryAccessorManager();
            String version = deviceRAM.getVersion();
            String revision = deviceRAM.getRevision();

            // Create and set the title.
            MessageFormat titleFormat = new MessageFormat(TITLE_FORMAT);
            String title = titleFormat.format(new Object[]{version, revision});
            this.setTitle(title);

            pageRec = new PageBookView.PageRec(part, page);
        }

        return pageRec;
    }

    // javadoc inherited.
    protected void doDestroyPage(IWorkbenchPart part, PageBookView.PageRec rec) {
        rec.page.dispose();
        rec.dispose();
    }

    // javadoc inherited
    protected IWorkbenchPart getBootstrapPart() {
        IWorkbenchPart part = null;
        IWorkbenchPage page = getSite().getPage();
        if (page != null) {
            part = page.getActiveEditor();
        }

        return part;
    }

    // javadoc inherited
    protected boolean isImportant(IWorkbenchPart part) {
        // We only care about editors.
        return (part instanceof IEditorPart);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Apr-04	4050/3	pcameron	VBM:2004040701 Added a device Information page and augmented DeviceRepositoryBrowser's title

 11-Feb-04	2862/3	allan	VBM:2004020411 The DeviceRepositoryBrowser.

 ===========================================================================
*/

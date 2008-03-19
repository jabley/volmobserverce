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

package com.volantis.mcs.eclipse.ab.views.layout;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.MessagePage;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;
import com.volantis.mcs.eclipse.ab.editors.layout.LayoutMessages;

/**
 * The MCS format attributes view.
 */
public class FormatAttributesView extends PageBookView {

    /**
     * Message to show on the default page.
     */
    private static final String DEFAULT_TEXT = LayoutMessages.getString(
            "FormatAttributesView.default.text");

    /**
     * Default constructor.
     */
    public FormatAttributesView() {
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
        Object obj = part.getAdapter(FormatAttributesViewPage.class);
        if (obj instanceof FormatAttributesViewPage) {
            FormatAttributesViewPage page = (FormatAttributesViewPage) obj;
            if (page instanceof IPageBookViewPage) {
                initPage((IPageBookViewPage) page);
            }
            page.createControl(getPageBook());
            return new org.eclipse.ui.part.PageBookView.PageRec(part, page);
        } else {
            return null;
        }
    }

    // javadoc inherited.
    protected void doDestroyPage(IWorkbenchPart part, PageBookView.PageRec rec) {
        FormatAttributesViewPage page = (FormatAttributesViewPage) rec.page;
        page.dispose();
        rec.dispose();
    }

    // javadoc inherited
    protected IWorkbenchPart getBootstrapPart() {
        IWorkbenchPage page = getSite().getPage();
        if (page != null) {
            return page.getActiveEditor();
        } else {
            return null;
        }
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

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Nov-04	6210/1	adrianj	VBM:2004102102 Assign focus to FormatAttributesView when opened

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 28-Jan-04	2752/1	byron	VBM:2004012602 Address issues from review

 22-Jan-04	2540/1	byron	VBM:2003121505 Added main formats attribute page

 ===========================================================================
*/

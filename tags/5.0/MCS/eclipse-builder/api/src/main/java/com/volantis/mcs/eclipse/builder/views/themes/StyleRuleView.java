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
package com.volantis.mcs.eclipse.builder.views.themes;

import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;
import org.eclipse.ui.part.MessagePage;
import org.eclipse.ui.part.IPageBookViewPage;
import com.volantis.mcs.eclipse.builder.editors.themes.ThemeEditorContext;
import com.volantis.mcs.eclipse.builder.editors.common.EditorContext;
import com.volantis.mcs.eclipse.builder.editors.EditorMessages;

/**
 * A view providing a summary of a selected resource
 */
public class StyleRuleView extends PageBookView {
    protected IPage createDefaultPage(PageBook pageBook) {
        MessagePage page = new MessagePage();
        initPage(page);
        page.createControl(pageBook);
        page.setMessage(EditorMessages.getString("StyleRuleViewPage.noDeviceTheme"));
        return page;
    }

    protected PageRec doCreatePage(IWorkbenchPart part) {
        Object obj = part.getAdapter(EditorContext.class);
        if (obj instanceof ThemeEditorContext) {
            StyleRuleViewPage page = new StyleRuleViewPage((ThemeEditorContext) obj);
            initPage((IPageBookViewPage) page);
            page.createControl(getPageBook());
            return new org.eclipse.ui.part.PageBookView.PageRec(part, page);
        } else {
            return null;
        }
    }

    protected void doDestroyPage(IWorkbenchPart iWorkbenchPart, PageRec pageRec) {
    }

    protected IWorkbenchPart getBootstrapPart() {
        return null;
    }

    protected boolean isImportant(IWorkbenchPart part) {
        return (part instanceof IEditorPart);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Dec-05	10345/1	adrianj	VBM:2005111601 Add style rule view

 ===========================================================================
*/

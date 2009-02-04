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
package com.volantis.mcs.eclipse.ab.actions.layout;

import org.eclipse.ui.texteditor.ResourceAction;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.PartInitException;

import java.util.ResourceBundle;

import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.ab.ABPlugin;

/**
 * This is the Attributes action appropriate to the Layout Outline page and the
 * Layout Graphical Editor page. It allows the Format Attributes View
 * associated with the layout editor to be displayed.
 *
 * <p>This action expects the editor to provide the view via the {@link
 * org.eclipse.ui.part.EditorPart#getAdapter getAdapter} method.</p>
 */
public class AttributesAction extends ResourceAction {
    /**
     * Initializes the new instance using the given parameters.
     *
     * @param bundle    the bundle from which the action's properties will be
     *                  obtained. Must be specified
     * @param prefix    the resource naming prefix. May be null
     */
    public AttributesAction(ResourceBundle bundle,
                            String prefix) {
        super(bundle, prefix);
    }

    /**
     * The action ensures that the Format Attributes View associated with the
     * layout editor is displayed.
     */
    public void run() {
        try {
            IWorkbenchPage activePage =
                PlatformUI.getWorkbench().
                getActiveWorkbenchWindow().
                getActivePage();
            activePage.showView(
                        "com.volantis.mcs.eclipse.ab.views" +
                        ".layout.FormatAttributesView").setFocus();
        } catch (PartInitException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }
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

 03-Feb-04	2820/2	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 23-Jan-04	2727/3	philws	VBM:2004012301 Fix attributes view action

 23-Jan-04	2727/1	philws	VBM:2004012301 First draft of new, delete and clipboard actions in context menu

 13-Jan-04	2534/1	philws	VBM:2003121511 Action support classes and stub implementations of Layout actions

 ===========================================================================
*/

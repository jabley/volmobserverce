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
package com.volantis.mcs.eclipse.ab.search.devices;

import com.volantis.mcs.eclipse.ab.search.SearchSupport;
import org.apache.log4j.Logger;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.ISearchResultPage;
import org.eclipse.search.ui.ISearchResultViewPart;
import org.eclipse.search.ui.text.AbstractTextSearchViewPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionContext;

/**
 * The ISearchResultPage that displays the results of a device search.
 */
public class DeviceSearchResultPage extends AbstractTextSearchViewPage
        implements ISearchResultPage {

    /**
     * Logger for this class.
     */
    private static final Logger logger =
            Logger.getLogger(DeviceSearchResultPage.class);

    /**
     * The search result for this page.
     */
    private ISearchResult searchResult;

    /**
     * The content provider for the viewer of this page.
     */
    private DeviceSearchResultContentProvider contentProvider;

    /**
     * The DeviceSearchActionGroup containing the actions for the menu and
     * action bar.
     */
    private DeviceSearchActionGroup actionGroup;

    /**
     * Construct a new DeviceSearchResultPage.
     */
    public DeviceSearchResultPage() {
        super(FLAG_LAYOUT_TREE);
    }

    // javadoc inherited
    public void setInput(ISearchResult searchResult, Object viewState) {
        assert(searchResult instanceof DeviceSearchResult);
        this.searchResult = searchResult;

        if (searchResult != null) {
            contentProvider.
                    setDeviceSearchResult((DeviceSearchResult) searchResult);
        }
        super.setInput(searchResult, viewState);
    }

    // javadoc inherited
    public void setViewPart(ISearchResultViewPart searchResultViewPart) {
        super.setViewPart(searchResultViewPart);
        actionGroup = new DeviceSearchActionGroup(searchResultViewPart);
    }

    // javadoc inherited
    public String getLabel() {
        return searchResult == null ? "" : searchResult.getLabel();
    }

    // javadoc inherited
	protected void elementsChanged(Object[] objects) {
		if (contentProvider != null) {
			contentProvider.setDeviceSearchResult((DeviceSearchResult)searchResult);
            contentProvider.elementsChanged(objects);
        }
	}

    // javadoc inherited
    protected void clear() {
		if (contentProvider != null) {
			contentProvider.setDeviceSearchResult((DeviceSearchResult)searchResult);
            contentProvider.clear();
        }
    }

    // javadoc inherited
    protected void configureTreeViewer(final TreeViewer viewer) {
		viewer.setUseHashlookup(true);

        contentProvider = new DeviceSearchResultContentProvider(viewer);

        viewer.setContentProvider(contentProvider);
        viewer.setLabelProvider(new DecoratingLabelProvider(
                new DeviceSearchResultLabelProvider(),
                PlatformUI.getWorkbench().getDecoratorManager().
                getLabelDecorator()));

        // Add a double-click listener to go to the selected device.

        viewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                SearchSupport.gotoSelection(((IStructuredSelection)
                        viewer.getSelection()), getSite().getPage());
            }
        });

    }

    // javadoc inherited
    protected void configureTableViewer(TableViewer tableViewer) {
        throw new UnsupportedOperationException("Only tree view supported.");
    }

    /**
     * Dispose of the DeviceSearchActionGroup associated with this
     * DeviceSearchResultPage.
     */
    public void dispose() {
        actionGroup.dispose();
        super.dispose();
    }

    /**
     * Fills the context menuManager for this page. Subclasses may override this
     * method.
     *
     * @param mgr the menuManager manager representing the context menuManager
     */
    protected void fillContextMenu(IMenuManager mgr) {
        super.fillContextMenu(mgr);
        actionGroup.setContext(new ActionContext(getSite().getSelectionProvider().getSelection()));
        actionGroup.fillContextMenu(mgr);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 08-Oct-04	5557/5	allan	VBM:2004070608 Device search

 ===========================================================================
*/

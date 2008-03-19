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
package com.volantis.mcs.eclipse.ab.search;

import org.eclipse.search.ui.ISearchPageContainer;
import com.volantis.mcs.objects.FileExtension;

/**
 * Factory for creating SearchScope instances.
 */
public class SearchScopeFactory {
    /**
     * Create a SearchScope based on the scope specified by the given
     * ISearchPageContainer.
     * @param searchPageContainer the ISearchPageContainer that specifies the
     * required scope.
     * @param fileExtensions the FileExtensions that are applicable to the
     * search.
     * @return the right kind of SearchScope.
     */
    public SearchScope
            createSearchScope(ISearchPageContainer searchPageContainer,
                              FileExtension [] fileExtensions) {
        assert(searchPageContainer!=null);

        SearchScope searchScope = null;

        switch(searchPageContainer.getSelectedScope()) {
            case ISearchPageContainer.SELECTED_PROJECTS_SCOPE:
                searchScope = new SelectedProjectsScope(searchPageContainer.
                        getSelection(), fileExtensions);
                break;
            case ISearchPageContainer.SELECTION_SCOPE:
                searchScope = new SelectionScope(searchPageContainer.
                        getSelection(), fileExtensions);
                break;
            case ISearchPageContainer.WORKING_SET_SCOPE:
                searchScope = new WorkingSetScope(searchPageContainer.
                        getSelectedWorkingSets(), fileExtensions);
                break;
            case ISearchPageContainer.WORKSPACE_SCOPE:
                searchScope = new WorkspaceScope(fileExtensions);
                break;
        }

        return searchScope;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 08-Oct-04	5557/2	allan	VBM:2004070608 Device search

 ===========================================================================
*/

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.text.AbstractTextSearchResult;
import org.eclipse.search.ui.text.IEditorMatchAdapter;
import org.eclipse.search.ui.text.IFileMatchAdapter;
import org.eclipse.search.ui.text.Match;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.FileEditorInput;

/**
 * An ISearchResult for device searches.
 *
 * This class is abstract because it does not implement getQuery(). The
 * reason for this to avoid directly coupling this ISearchResult with the
 * ISearchQuery it is created by. The ISearchQuery that created
 * DeviceSearchResult objects is expected to implement getQuery() to return
 * itself.
 */
public abstract class DeviceSearchResult extends AbstractTextSearchResult {

    /**
     * An ArrayList of matches that represent the result of the search.
     */
    private List matches = Collections.synchronizedList(new ArrayList());

    /**
     * Add a match to this DeviceSearchResult
     * @param match the DeviceSearchMatch to add to this DeviceSearchResult.
     */
    protected void addMatch(DeviceSearchMatch match) {
        matches.add(match);
        super.addMatch(match);
    }

    // javadoc inherited
    public void addMatches(Match[] newMatches) {
        for (int i = 0; i < newMatches.length; i++) {
            if (!matches.contains(newMatches)) {
                matches.add(newMatches[i]);
            }
        }
        super.addMatches(newMatches);
    }


    // javadoc inherited - this implementation based on
    // org.eclipse.search.internal.ui.text.FileSearchResult
    public IEditorMatchAdapter getEditorMatchAdapter() {
        return new IEditorMatchAdapter() {

            public boolean isShownInEditor(Match match, IEditorPart editor) {
                boolean isShown = false;
                IEditorInput ei = editor.getEditorInput();
                if (ei instanceof IFileEditorInput) {
                    FileEditorInput fi = (FileEditorInput) ei;
                    isShown = match.getElement().equals(fi.getFile());
                }

                return isShown;
            }

            public Match[] computeContainedMatches(
                    AbstractTextSearchResult result, IEditorPart editor) {
                Match matches [];
                IEditorInput ei = editor.getEditorInput();
                if (ei instanceof IFileEditorInput) {
                    FileEditorInput fi = (FileEditorInput) ei;
                    matches = getMatches(fi.getFile());
                } else {
                    matches = new Match[0];
                }

                return matches;
            }
        };
    }

    // javadoc inherited - this implementation based on
    // org.eclipse.search.internal.ui.text.FileSearchResult
    public IFileMatchAdapter getFileMatchAdapter() {
        return new IFileMatchAdapter() {

            public Match[] computeContainedMatches(
                    AbstractTextSearchResult result, IFile file) {
                return getMatches(file);
            }

            public IFile getFile(Object element) {
                IFile file = null;
                if (element instanceof IFile) {
                    file = (IFile) element;
                }
                return file;
            }
        };
    }

    // javadoc inherited
    public String getLabel() {
        return getQuery().getLabel();
    }

    // javadoc inherited
    public String getTooltip() {
        return null;
    }

    // javadoc inherited
    public ImageDescriptor getImageDescriptor() {
        return null;
    }

    /**
     * Get the DeviceSearchMatches that make up this DeviceSearchResult.
     * @return an array of DeviceSearchMatch objects that make up this
     * result.
     */
    public DeviceSearchMatch[] getDeviceSearchMatches() {
        DeviceSearchMatch matchArray [] = new DeviceSearchMatch[matches.size()];
        return (DeviceSearchMatch[]) matches.toArray(matchArray);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 08-Oct-04	5557/3	allan	VBM:2004070608 Device search

 ===========================================================================
*/

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

package com.volantis.mcs.eclipse.controls;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.File;

import org.eclipse.core.resources.IResource;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * FileFilter is a ViewerFilter extention that filters files based on their
 * extentions. 
 * <p> The constructor takes a list of file extentions separated by
 * non alphabetic / numeric characters as a single string. An example of this
 * could be "*.doc;*.txt,*.sl1:*.po2" This would extract the extentions
 * ".doc", ".txt", ".sl1" and ".po2" as the ';,:' characters are not 
 * alphanumeric and denote the end of the extention.</p>    
 */
public class FileFilter extends ViewerFilter {
    
    /**
     * The image extentions that are allowed by this filter
     */
    private List fileExtentions = null;


    /**
     * Create a new FileFilter.
     * @param extentionList a list of file extentions that are to be allowed 
     * separated by non alphanumeric characters.
     */
    public FileFilter(String extentionList ) {
        fileExtentions = new ArrayList();
        createPatterns( extentionList );
    }

    /**
     * This method is called by a Viewer to filter the files that are to
     * be displayed. In this class, we are only interested in the 'element' 
     * object if it is a File instance. If it is, we get the name of the
     * file and check it against our list of extentions. Directories
     * are automatically allowed.
     *
     * @param viewer the viewer
     * @param parentElement the parent element
     * @param element the element
     * @return <code>true</code> if element is included in the
     *   filtered set, and <code>false</code> if excluded
     */
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        boolean allow = false;
        File file = null;
        if (element instanceof File) {
            file = (File) element;
            allow = file.isDirectory() || isVisible(file.getName());
        }
        return allow;
    }

    /**
     * Checks a file name to see if it ends with one of our extentions. This
     * has been separated from the select method so it can be used for
     * testing the filter without having to set up an IResource instance.
     *
     * @param name the filename to test
     * @return <code>true</code> if element is included in the
     *   filtered set, and <code>false</code> if excluded
     */
    public boolean isVisible(String name) {
        boolean allow = false;
        String lowerCaseName = name.toLowerCase();
        for (Iterator i = fileExtentions.iterator(); i.hasNext() && !allow;) {
            String suffix = (String) i.next();
            if(lowerCaseName.endsWith(suffix)) {
                allow = true;
            }
        }
        return allow;
    }

    /**
     * Given a list of file extentions, extract the extentions and store
     * each one. Each file extention is taken to be a '.' followed by an
     * extension. The extension ends with the first non-alphabetic character
     */
    private void createPatterns( String extentionList ) {
        int posn = 0;
        int length = extentionList.length();

        int idx = extentionList.indexOf('.');
        while ((idx > -1) && (idx < length)) {
            posn = idx + 1;

            char chr = extentionList.charAt(posn);
            posn++;
            while ((posn < length) && 
                  ((Character.isLetterOrDigit(chr)) || chr == '.' )) {
                chr = extentionList.charAt(posn);
                posn++;
            }
            if( posn < length) {
                posn--;
            }
            // Convert the extention to lower case. This will aid the
            // isVisible method to make case insensitive file extenstion
            // comparisons
            String ext = extentionList.substring(idx, posn).toLowerCase();
            fileExtentions.add( ext );
            idx = extentionList.indexOf('.', posn);
        }
    }

    /**
     * Return the List of file extentions we are filtering
     * @return the file extensions we are filtering.
     */
    public List getFilterExtentions() {
        return fileExtentions;
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Feb-04	3028/1	doug	VBM:2004011505 Ensured file extension comparisons are case insensitive

 15-Nov-03	1835/5	pcameron	VBM:2003102801 Some tweaks to GenericAssetCreationPage, and FileFilter

 31-Oct-03	1661/1	steve	VBM:2003102410 Moved messages to ControlsMessages and Factory should not be a singleton

 ===========================================================================
*/

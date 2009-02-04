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
package com.volantis.mcs.eclipse.ab.editors.dom;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;

/**
 * An action contributor for MultiPage ODOM editors.
 */
public class MultiPageODOMEditorContributor
        extends MultiPageEditorActionBarContributor {

    /**
     * Store the delegate instance for this contributor. Note that a
     * xxxContributor instance is created only once in the lifetime of an
     * eclipse GUI.
     */
    private ODOMEditorContribution delegate = new ODOMEditorContribution();

    // javadoc inherited.
    public void setActivePage(IEditorPart targetEditor) {
        delegate.doSetActiveEditor(targetEditor);
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Mar-04	3346/1	byron	VBM:2004021308 Cannot undo deletion or cutting of single or multiple assets

 16-Dec-03	2213/1	allan	VBM:2003121401 More editors and fixes for presentable values.

 ===========================================================================
*/

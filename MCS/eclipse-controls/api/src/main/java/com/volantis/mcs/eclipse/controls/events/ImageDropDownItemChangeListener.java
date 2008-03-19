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
package com.volantis.mcs.eclipse.controls.events;


/**
 * An interface describing change listeners for
 * {@link com.volantis.mcs.eclipse.controls.ImageDropDownItem}s.
 */
public interface ImageDropDownItemChangeListener {
    /**
     * Handles the change event on the
     * {@link com.volantis.mcs.eclipse.controls.ImageDropDownItem}.
     * @param event The ImageDropDownChangeEvent.
     */
    public void imageDropDownItemChanged(ImageDropDownChangeEvent event);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 30-Apr-04	4081/1	pcameron	VBM:2004031007 Added PoliciesSection

 01-Mar-04	3197/5	pcameron	VBM:2004021904 Rework issues

 01-Mar-04	3197/1	pcameron	VBM:2004021904 Added PolicyValueOriginSelector

 ===========================================================================
*/

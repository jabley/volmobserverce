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
package com.volantis.mcs.eclipse.ab.editors.devices;

import com.volantis.mcs.eclipse.controls.ImageDropDownItem;

import java.text.MessageFormat;

/**
 * This class defines the type of policy origin.
 */
public class PolicyOriginSelection {

    /**
     * The type of this PolicyOriginSelection. Package local for
     * {@link PolicyOriginSelector}.
     */
    final PolicyOriginSelectionType type;

    /**
     * The ImageDropDownItem for this origin type. Package local for
     * {@link PolicyOriginSelector}.
     */
    final ImageDropDownItem imageDDI;

    /**
     * The tooltip message format for this origin type. Package local for
     * {@link PolicyOriginSelector}.
     */
    final MessageFormat tooltipMF;

    /**
     * Creates a PolicyOriginSelection. Package local for
     * PolicyOriginSelectionType.
     */
    PolicyOriginSelection() {
        this(null, null, null);
    }

    /**
     * Creates a PolicyOriginSelection.
     * @param type the type of this PolicyOriginSelection
     * @param imageDDI the ImageDropDownItem of the origin
     * @param tooltipMF the tooltip message format of the origin
     */
    public PolicyOriginSelection(PolicyOriginSelectionType type,
                                 ImageDropDownItem imageDDI,
                                 MessageFormat tooltipMF) {
        this.type = type;
        this.imageDDI = imageDDI;
        this.tooltipMF = tooltipMF;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Apr-04	3683/3	pcameron	VBM:2004030401 Some further tweaks

 14-Apr-04	3683/1	pcameron	VBM:2004030401 Some tweaks to PolicyController and refactoring of PolicyOriginSelection

 11-Mar-04	3398/3	pcameron	VBM:2004030906 Some further renamings

 11-Mar-04	3398/1	pcameron	VBM:2004030906 Renamed PolicyValueOriginSelector and associated classes and added method to PolicyValueModifier interface

 ===========================================================================
*/

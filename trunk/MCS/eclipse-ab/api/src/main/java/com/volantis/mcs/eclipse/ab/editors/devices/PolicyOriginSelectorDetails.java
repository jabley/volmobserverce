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

/**
 * This class provides the details (a selected device and a short policy
 * name) to a PolicyOriginSelector control.
 */
public class PolicyOriginSelectorDetails {

    /**
     * The selected device name.
     */
    public final String selectedDeviceName;

    /**
     * The short policy name.
     */
    public final String policyName;

    /**
     * Constructs a PolicyOriginSelectorDetails object.
     * @param selectedDeviceName the selected device name. Cannot be null.
     * @param policyName the short policy name. Cannot be null.
     * @throws IllegalArgumentException if either argument is null
     */
    public PolicyOriginSelectorDetails(String selectedDeviceName,
                                       String policyName) {
        if (selectedDeviceName == null) {
            throw new IllegalArgumentException("Cannot be null: " +
                    "selectedDeviceName");
        }
        if (policyName == null) {
            throw new IllegalArgumentException("Cannot be null: " +
                    "policyName");
        }
        this.selectedDeviceName = selectedDeviceName;
        this.policyName = policyName;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-Mar-04	3398/2	pcameron	VBM:2004030906 Renamed PolicyValueOriginSelector and associated classes and added method to PolicyValueModifier interface

 01-Mar-04	3197/6	pcameron	VBM:2004021904 Rework issues

 01-Mar-04	3197/2	pcameron	VBM:2004021904 Added PolicyValueOriginSelector

 ===========================================================================
*/

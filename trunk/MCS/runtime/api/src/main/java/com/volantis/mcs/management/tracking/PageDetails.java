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
package com.volantis.mcs.management.tracking;

/**
 * Interface for providing information about a Page.
 * @volantis-api-include-in PublicAPI
 */
public interface PageDetails {

    /**
     * Return the CanvasDetails object associated with this page.
     */
    public CanvasDetails getCanvasDetails();

    /**
     * Return the name of the device associated with the Page.
     */
    public String getDeviceName();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Oct-04	5921/1	tom	VBM:2004101102 added public API documentation for canvas tracking

 22-Oct-04	5910/1	tom	VBM:2004101102 Added Public API documentation for canvas tracking

 11-Jun-04	4689/1	matthew	VBM:2004060706 add basic PageTracking facilities

 ===========================================================================
*/

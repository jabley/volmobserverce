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
 * Provide a method by which information about a canvas can be obtained.
 * @volantis-api-include-in PublicAPI
 */
public interface CanvasDetails {

    /**
     * Return the type of the Canvas to which the details are related.
     */
    public CanvasType getCanvasType();

    /**
     * Return hte name of the Theme associated with this canvas.
     */
    public String getThemeName();

    /**
     * Return the name of the Layout associated with this Canvas
     */
    public String getLayoutName();

    /**
     * Return the tile of this canvas.
     */
    public String getTitle();

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

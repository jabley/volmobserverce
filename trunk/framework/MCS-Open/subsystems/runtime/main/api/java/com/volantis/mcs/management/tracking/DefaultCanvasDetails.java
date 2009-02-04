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
 * Default implementation of CanvasDetails. This class simply
 * provides that values that it was constructed with.
 */
public class DefaultCanvasDetails implements CanvasDetails {

    /**
     * The type of the canvas.
     **/
    private CanvasType canvasType = null;

    /**
     * The title of the canvas.
     */
    private String title = null;

    /**
     * The name of the theme.
     */
    private String themeName = null;

    /**
     * The name of the layout.
     */
    private String layoutName = null;

    /**
     * Explicit constructor.
     * @param title the title of the canvas.
     * @param canvasType the type of the canvas.
     * @param themeName the name of the associated theme.
     * @param layoutName the name of the associated layout.
     * @throws IllegalArgumentException if canvasType or layoutName is null.
     */
    public DefaultCanvasDetails(String title, CanvasType canvasType,
                                String themeName, String layoutName) {
        if (canvasType == null) {
            throw new IllegalArgumentException("canvasType must not be null");
        }
        if (layoutName == null) {
            throw new IllegalArgumentException("layoutName must not be null");
        }

        this.canvasType = canvasType;
        this.title = title;
        this.themeName = themeName;
        this.layoutName = layoutName;

    }

    /**
     * javadoc inherited
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * javadoc inherited
     */
    public CanvasType getCanvasType() {
        return this.canvasType;
    }

    /**
     * javadoc inherited

     */
    public String getThemeName() {
        return this.themeName;
    }

    /**
     * javadoc inherited
     */
    public String getLayoutName() {
        return this.layoutName;
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-Jun-04	4702/5	matthew	VBM:2004061402 rework JMXPageTrackerFactory error handling

 21-Jun-04	4702/3	matthew	VBM:2004061402 rework PageTracking

 16-Jun-04	4702/1	matthew	VBM:2004061402 management functionality added

 11-Jun-04	4689/1	matthew	VBM:2004060706 add basic PageTracking facilities

 ===========================================================================
*/

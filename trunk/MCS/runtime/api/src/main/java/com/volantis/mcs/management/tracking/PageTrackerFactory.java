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
 * Interface that allows implementations of the CanvasDetails and PageDetails
 * interfaces to be instanciated.
 */
public interface PageTrackerFactory {

    /**
     * Return a new PageDetails object.
     * @param canvasDetails The Canvas details object this page details object
     * will contain.
     * @param deviceName the name of the device associated with this PageDetails
     * object.
     * @return an implementaion of the PageDetails interface.
     * @throws IllegalArgumentException if any of the arguments is null.
     */
    public PageDetails createPageDetails(CanvasDetails canvasDetails,
                                      String deviceName);

    /**
     * Return an implementaion of the CanvasDetials interface.
     * @param title the title of the Canvas.
     * @param canvasType the type of the Canvas.
     * @param themeName the name of the associated theme.
     * @param layoutName the name of the associated layout.
     * @return an implementaion of the CanvasDetials interface.
     * @throws IllegalArgumentException if any of the parameters is null.
     */
    public CanvasDetails createCanvasDetails(String title, CanvasType canvasType,
                                          String themeName, String layoutName);



    /**
     * Return an implementaion of the PageDetailsManager interface.
     * @throws PageTrackerException
     */
    public PageDetailsManager createPageDetailsManager() throws PageTrackerException;
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

 11-Jun-04	4689/3	matthew	VBM:2004060706 add basic PageTracking facilities

 11-Jun-04	4689/1	matthew	VBM:2004060706 add basic PageTracking facilities

 ===========================================================================
*/

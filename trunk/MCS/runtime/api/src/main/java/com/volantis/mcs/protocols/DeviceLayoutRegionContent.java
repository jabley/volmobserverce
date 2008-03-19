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
 
package com.volantis.mcs.protocols;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;

/**
 * Render a device layout in a region.
 * 
 * @author steve
 */
public final class DeviceLayoutRegionContent implements RegionContent {
    
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(DeviceLayoutRegionContent.class);
    
    /** The device layout context */
    private final DeviceLayoutContext dlc;
    
    /**
     * Create a new region contentfrom a device layout context
     */
    public DeviceLayoutRegionContent(DeviceLayoutContext layout) {
        dlc = layout;        
    }
    
    /**
     * Render a device layout within a region. 
     */
    public void render(FormatRendererContext context) {

        try {
            context.beginNestedLayout(dlc);
            DeviceLayoutRenderer renderer =
                    context.getDeviceLayoutRenderer();
            renderer.renderLayout(dlc, context);

        } catch (IOException e) {
            logger.error("layout-rendering-io-exception",
                    new Object[]{dlc}, e);
        } catch (RendererException e) {
            logger.error("layout-rendering-renderer-exception",
                    new Object[]{dlc}, e);
        } finally {
            context.endNestedLayout(dlc);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 25-Nov-05	10394/1	ibush	VBM:2005111812 interim commit for Willobs

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 10-Dec-04	6391/2	adrianj	VBM:2004120207 Refactoring DeviceLayoutRenderer

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 14-Jun-04	4698/1	geoff	VBM:2003061912 RegionContent should not store a MarinerPageContext

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 19-Jun-03	407/3	steve	VBM:2002121215 Copy DLC MarinerPageContext before rendering.

 19-Jun-03	407/1	steve	VBM:2002121215 Flow elements and PCData in regions

 ===========================================================================
*/

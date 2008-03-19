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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.widgets.renderers;

import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.layouts.RegionInstance;

/**
 * Renderer of tabs widget.
 * 
 * @mock.generate base="WidgetRenderer"
 */
public interface TabsRenderer extends WidgetRenderer {
        
    /** Method for rendering tabs element, called on element open */
    public void renderTabsOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException;
    
    /** Method for rendering tabs element, called on element close */
    public void renderTabsClose(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException;
    
    /** Method for rendering tab element, called on element open */
    public void renderTabOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException;
    
    /** Method for rendering tab element, called on element close */
    public void renderTabClose(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException;

    /** Set RegionInstance for Labels in TabsDefaultRenderer */ 
    public void setLabelsRegionInstance(RegionInstance labelsRegionInstance);

    /** Set RegionInstance for Tab's contents in TabsDefaultRenderer */ 
    public void setContentsRegionInstance(RegionInstance contentsRegionInstance);
    
}

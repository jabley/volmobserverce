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
package com.volantis.mcs.protocols.dissection;

import com.volantis.mcs.dissection.links.ShardLinkAttributes;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.MenuLabel;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteMenuItem;

/**
 * An extended MenuItem for shard links which adds the shard link attributes.
 * <p>
 * These attributes are used to create a SHARD LINK element in
 * {@link ShardLinkMenuItemRenderer}.
 * 
 * @see ShardLinkMenuModelBuilder
 */ 
public final class ShardLinkMenuItem extends ConcreteMenuItem {

    /**
     * The copyright statement.
     */
    private static final String mark = "(c) Volantis Systems Ltd 2004.";
    
    /**
     * The attributes to use for the SHARD LINK element.
     */ 
    private final ShardLinkAttributes shardLinkAttributes;

    /**
     * Construct an instance of this class.
     * 
     * @param elementDetails        The details of the PAPI element which
     *                              corresponds to this menu item.
     * @param label                 The presentation label used with this menu
     *                              item.
     * @param shardLinkAttributes   The attributes to use for the decorating
     *                              SHARD LINK element.
     */ 
    public ShardLinkMenuItem(ElementDetails elementDetails, MenuLabel label,
            ShardLinkAttributes shardLinkAttributes) {
        
        super(elementDetails, label);
        this.shardLinkAttributes = shardLinkAttributes;
    }

    /**
     * Returns the shard link attributes.
     */ 
    public ShardLinkAttributes getShardLinkAttributes() {
        return shardLinkAttributes;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Apr-04	4048/1	geoff	VBM:2004042606 Enhance Menu Support: WML Dissection

 ===========================================================================
*/

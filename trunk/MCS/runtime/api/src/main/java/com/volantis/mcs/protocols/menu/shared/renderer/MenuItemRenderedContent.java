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

package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.protocols.separator.SeparatedContent;

/**
 * A typesafe enumeration.
 */
public final class MenuItemRenderedContent
    implements SeparatedContent {

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole IMAGE
     */
    public static final MenuItemRenderedContent NONE
        = new MenuItemRenderedContent("NONE");

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole IMAGE
     */
    public static final MenuItemRenderedContent IMAGE
        = new MenuItemRenderedContent("IMAGE");

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole TEXT
     */
    public static final MenuItemRenderedContent TEXT
        = new MenuItemRenderedContent("TEXT");

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole BOTH
     */
    public static final MenuItemRenderedContent BOTH
        = new MenuItemRenderedContent("BOTH");

    /**
     * The internal name for the enumeration literal.
     */
    private final String name;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param name the internal name for the new literal
     */
    private MenuItemRenderedContent(String name) {
        this.name = name;
    }

    /**
     * Returns the internal name for the enumeration literal. This must not
     * be used for presentation purposes.
     *
     * @return internal name for the enumeration literal
     */
    public String toString() {
        return name;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-May-04	4164/1	pduffin	VBM:2004050404 Fixed some problems with HTML orientation separator

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 ===========================================================================
*/

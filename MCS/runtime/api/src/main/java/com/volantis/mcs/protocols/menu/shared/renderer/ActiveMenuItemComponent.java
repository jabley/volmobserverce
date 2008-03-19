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

/**
 * A typesafe enumeration.
 */
public class ActiveMenuItemComponent {

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole FIRST
     */
    public static final ActiveMenuItemComponent FIRST
        = new ActiveMenuItemComponent("FIRST");

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole SECOND
     */
    public static final ActiveMenuItemComponent SECOND
        = new ActiveMenuItemComponent("SECOND");

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole BOTH
     */
    public static final ActiveMenuItemComponent BOTH
        = new ActiveMenuItemComponent("BOTH");

    /**
     * The internal name for the enumeration literal.
     */
    private final String name;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param name the internal name for the new literal
     */
    private ActiveMenuItemComponent(String name) {
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

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 ===========================================================================
*/

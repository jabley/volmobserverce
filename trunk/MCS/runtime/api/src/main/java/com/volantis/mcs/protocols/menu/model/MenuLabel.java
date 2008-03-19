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

package com.volantis.mcs.protocols.menu.model;

/**
 * Labels are associated with menu items and (optionally) menus themselves.
 *
 * <p>A label associated with a menu must have the elementDetails (information
 * about the PAPI element associated with the menu) set. A label associated
 * with a menu item must not.</p>
 *
 * <p><strong>NOTE:</strong> the style information associated with a menu label
 * should not be used directly in any rendering. It is available for use within
 * the model implementation only (to handle creation of menu items from the
 * labels associated with menus).</p>
 */
public interface MenuLabel extends ModelElement, EventTarget, PaneTargeted, Titled {
    /**
     * A label may have an associated icon.
     * 
     * @labelDirection forward
     * @directed
     * @supplierRole icon
     * @supplierCardinality 0..1 
     * @link aggregationByValue
     */
    /*# MenuIcon lnkMenuIcon; */

    /**
     * A label always has an associated text.
     * 
     * @directed
     * @labelDirection forward
     * @supplierCardinality 1
     * @supplierRole text 
     * @link aggregationByValue
     */
    /*# MenuText lnkMenuText; */

    /**
     * Returns the optionally defined icon associated with the label.
     *
     * @return the icon associated with the label. May be null
     */
    MenuIcon getIcon();

    /**
     * Returns the text for the label.
     *
     * @return the label's text. Will not be null
     */
    MenuText getText();

    // Other JavaDoc inherited
    /**
     * <strong>This is for internal menu model usage only.</strong>
     */
    ElementDetails getElementDetails();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-May-04	4246/1	philws	VBM:2004050709 Fix StyleInfo handling for MenuIcon, MenuText and MenuLabel

 26-Mar-04	3491/3	philws	VBM:2004031912 Add handling of title to Menu Label

 23-Mar-04	3491/1	philws	VBM:2004031912 Make Menu Model conform to updated Architecture

 03-Mar-04	3288/1	philws	VBM:2004022702 Add Menu Model API

 ===========================================================================
*/

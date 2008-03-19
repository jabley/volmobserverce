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
 * This interface provides access to information about the PAPI element
 * associated with the menu entity.
 */
public interface ModelElement {
    /**
     * The information associated with this entity.
     *
     * @link aggregationByValue
     * @labelDirection forward
     * @directed
     * @supplierRole elementDetails
     * @supplierCardinality 0..1
     */
    /*# ElementDetails lnkElementDetails; */

    /**
     * Returns the information about the element associated with this entity.
     * It may be null, but if not it will have element name and styles set to
     * default values.
     *
     * @return the associated PAPI element information. May be null
     */
    ElementDetails getElementDetails();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Jun-05	8888/3	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-May-04	4246/1	philws	VBM:2004050709 Fix StyleInfo handling for MenuIcon, MenuText and MenuLabel

 05-Mar-04	3292/1	philws	VBM:2004022703 Added Menu Model Builder API

 03-Mar-04	3288/3	philws	VBM:2004022702 Add Menu Model API

 ===========================================================================
*/

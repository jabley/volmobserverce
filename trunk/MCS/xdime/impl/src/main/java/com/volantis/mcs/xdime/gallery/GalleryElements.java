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

package com.volantis.mcs.xdime.gallery;

import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.mcs.xml.schema.model.Namespace;
import com.volantis.mcs.xml.schema.model.ElementType;

/**
 * Defines element types for XDIME2 elements from gallery namespace
 */
public class GalleryElements {

    /**
     * The namespace containing all these elements.
     */
    public static final Namespace NAMESPACE =
            new Namespace(XDIMESchemata.GALLERY_NAMESPACE, "gallery");

    /**
     * Get the element type for the specified local name in the current
     * namespace.
     *
     * @param localName The local name of the element.
     * @return The element type.
     */
    private static ElementType getElement(String localName) {
        return NAMESPACE.addElement(localName);
    }


    /*
     * Add entry for each XDIME2 element needed by your widget, in the following
     * form: public static final ElementType ELEMENT_NAME =
     * getElement("element_name");
     */    
    public static final ElementType ITEM_DISPLAY = getElement("item-display");
    public static final ElementType ITEM_NUMBER = getElement("item-number");
    public static final ElementType ITEMS_COUNT = getElement("items-count");
    public static final ElementType START_ITEM_NUMBER = getElement("start-item-number");
    public static final ElementType END_ITEM_NUMBER = getElement("end-item-number");  
    public static final ElementType PAGE_NUMBER = getElement("page-number");  
    public static final ElementType PAGES_COUNT = getElement("pages-count");  
    public static final ElementType GALLERY = getElement("gallery");
    public static final ElementType SLIDESHOW = getElement("slideshow");
    public static final ElementType ITEMS = getElement("items");
    public static final ElementType ITEM = getElement("item");

}

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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.xdime.xforms.model;

/**
 * Represents an xforms si:item element in the MCS internal xforms model.
 */
public interface SIItem {

    /**
     * Get the identifier of the model in which this si:item was defined.
     *
     * @return String identifier of the model in which this si:item was defined
     */
    String getEnclosingModelID();

    /**
     * Get the name of this si:item. Must be unique within the xform.
     *
     * @return String name of this si:item
     */
    String getName();

    /**
     * If the item's value was defined as a comma separated list, then
     * translate this into a String array. If it was a single String value,
     * then return a String array with only one value.
     *
     * @return String array containing the value(s) of this item
     */
    String[] getValue();

    /**
     * Return the value of the item, without checking to see if it was defining
     * a comma separated list.
     *
     * @return String raw value
     */
    String getUnprocessedValue();

    /**
     * Returns true if the element has been referenced by a control element
     * and false otherwise.
     *
     * @return true if the element has been referenced by a control element and
     * false otherwise.
     */
    boolean isReferenced();

    /**
     * Indicate that this item has been referenced by a control element.
     */
    void setIsReferenced();

    /**
     * Return true if the specified model id is the same as the ID of the model
     * in which this item was declared.
     *
     * @param modelID   String id to compare to this item's enclosing model id
     * @return true if the specified model id is the same as the ID of the
     * model in which this item was declared. 
     */
    boolean isEnclosingModelID(String modelID);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Oct-05	9637/3	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 ===========================================================================
*/

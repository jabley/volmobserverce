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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom.sgml;

/**
 * Element Type Definition.
 *
 * <p>This implements equals and hashCode to support the optimization that is
 * performed by the SGMLBuilder.</p>
 */
public class ETDImpl
        implements ETD {

    /**
     * The model.
     */
    private ElementModel elementModel;

    /**
     * Indicates whether the end tag is optional.
     */
    private boolean endTagOptional;

    /**
     * Initialise.
     */
    public ETDImpl() {
        this.elementModel = ElementModel.UNKNOWN;
        this.endTagOptional = false;
    }

    /**
     * Setter for the elementModel property.
     *
     * @param elementModel The new value.
     */
    void setElementModel(ElementModel elementModel) {
        this.elementModel = elementModel;
    }

    /**
     * Getter for the elementModel property.
     *
     * @return The current value.
     */
    public ElementModel getElementModel() {
        return elementModel;
    }

    /**
     * Setter for the endTagOptional property.
     *
     * @param endTagOptional The new value.
     */
    void setEndTagOptional(boolean endTagOptional) {
        this.endTagOptional = endTagOptional;
    }

    /**
     * Getter for the endTagOptional property.
     *
     * @return The current value.
     */
    public boolean isEndTagOptional() {
        return endTagOptional;
    }

    // Javadoc inherited.
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ETDImpl etd = (ETDImpl) o;

        if (endTagOptional != etd.endTagOptional) return false;
        if (!elementModel.equals(etd.elementModel)) return false;

        return true;
    }

    // Javadoc inherited.
    public int hashCode() {
        int result;
        result = elementModel.hashCode();
        result = 31 * result + (endTagOptional ? 1 : 0);
        return result;
    }
}

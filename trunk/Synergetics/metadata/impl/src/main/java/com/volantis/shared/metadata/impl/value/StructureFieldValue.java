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
package com.volantis.shared.metadata.impl.value;

import com.volantis.shared.metadata.value.MetaDataValue;

/**
 * Class for structure field values. Created for JiBX.
 */
public class StructureFieldValue {

    /**
     * Name of the field.
     */
    private String name;

    /**
     * Value of the field.
     */
    private MetaDataValue value;

    /**
     * Default constructor left here for JiBX only.
     */
    public StructureFieldValue() {
    }

    /**
     * Creates a new structure field value with the given name and value.
     *
     * @param name the name of the field
     * @param value the value of the field
     */
    public StructureFieldValue(final String name, final MetaDataValue value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Returns the name of the field.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the field.
     *
     * @param name the new name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Returns the value of the field.
     */
    public MetaDataValue getValue() {
        return value;
    }

    /**
     * Sets a new value to the field.
     *
     * @param value the new value.
     */
    public void setValue(final MetaDataValue value) {
        this.value = value;
    }
}

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
package com.volantis.devrep.device.api.xml.policy;

import com.volantis.shared.iteration.ReadOnlyCollectionIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// todo: values and fields are mutually exclusive. Model this using inheritance.
public class PolicyEntry {

    private String name;

    private List values;

    private List fields;

    /**
     * The inherit value is not used during the import
     * It is defined here in order to prevent a jibx mapping exception during device policy import
     *
     */
    private String inherit;

    public String getName() {
        return name;
    }

    public String getInherit() {
        return inherit;
    }

    public Iterator values() {
        return new ReadOnlyCollectionIterator(values);
    }

    public Iterator fields() {
        return new ReadOnlyCollectionIterator(fields);
    }

    // JiBX specific methods to handle values which are spread across two
    // elements. Yuck.

    public void jibxAddValues(List values) {
        if (values != null) {
            allocateValues();
            this.values.addAll(values);
        }
    }

    public void jibxAddValue(String value) {
        if (value != null) {
            allocateValues();
            values.add(value);
        }
    }

    private void allocateValues() {
        if (this.values == null) {
            values = new ArrayList();
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 ===========================================================================
*/

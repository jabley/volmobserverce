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
package com.volantis.devrep.device.api.xml.definitions;

import com.volantis.shared.iteration.ReadOnlyCollectionIterator;

import java.util.Iterator;
import java.util.List;

public class DefinitionSet {

    private List types;

    private List categories;

    private boolean categoriesInitialised;

    public Iterator categories() {
        if (!categoriesInitialised) {
            Iterator categories = new ReadOnlyCollectionIterator(
                    this.categories);
            while (categories.hasNext()) {
                Category category = (Category) categories.next();
                category.initalize();
            }
            categoriesInitialised = true;
        }

        return new ReadOnlyCollectionIterator(categories);
    }

    /**
     *
     * @return an iterator over a collection of {@link TypeDeclaration}s.
     */
    public Iterator types() {
        return new ReadOnlyCollectionIterator(types);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Nov-05	10404/1	geoff	VBM:2005112301 Implement meta data for JiBX device repository accessor

 ===========================================================================
*/

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
package com.volantis.mcs.eclipse.builder.wizards.variants;

import com.volantis.mcs.policies.variants.metadata.EncodingCollection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Content provider for encoding collections.
 */
public class EncodingCollectionContentProvider implements IStructuredContentProvider {
    /**
     * Flag indicating whether the collection in its entirity should appear as
     * the first element.
     */
    private boolean includeCollection;

    /**
     * Create a new encoding collection content provider.
     *
     * @param includeCollection True if the collection itself should appear as
     *                          the first element, false otherwise
     */
    public EncodingCollectionContentProvider(boolean includeCollection) {
        this.includeCollection = includeCollection;
    }

    // Javadoc inherited
    public Object[] getElements(Object o) {
        EncodingCollection collection = (EncodingCollection) o;
        Iterator encodings = collection.iterator();
        List elements = new ArrayList();
        if (includeCollection) {
            elements.add(collection);
        }
        while (encodings.hasNext()) {
            elements.add(encodings.next());
        }
        return elements.toArray();
    }

    // Javadoc inherited
    public void dispose() {
    }

    // Javadoc inherited
    public void inputChanged(Viewer viewer, Object o, Object o1) {
    }
}

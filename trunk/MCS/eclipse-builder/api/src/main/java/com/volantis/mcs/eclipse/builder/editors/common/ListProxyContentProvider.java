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
package com.volantis.mcs.eclipse.builder.editors.common;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import com.volantis.mcs.interaction.ListProxy;
import com.volantis.mcs.interaction.Proxy;

/**
 * Content provider that provides the contents of a list proxy.
 */
public class ListProxyContentProvider implements IStructuredContentProvider {
    public static final Object NULL_VALUE = "";
    private boolean returnModel;

    /**
     * Creates a list proxy content provider that provides the proxies for the
     * items in the list.
     */
    public ListProxyContentProvider() {
        // Default to returning the proxy rather than the model.
        this(false);
    }

    /**
     * Creates a list proxy content provider.
     *
     * @param returnModel True if the values provided should be model objects,
     *                    false to return the proxies for the items in the list
     */
    public ListProxyContentProvider(boolean returnModel) {
        this.returnModel = returnModel;
    }

    // Javadoc inherited
    public Object[] getElements(Object proxy) {
        if (proxy instanceof ListProxy) {
            ListProxy listProxy = (ListProxy) proxy;
            Object[] values = new Object[listProxy.size()];
            for (int i = 0; i < values.length; i++) {
                Proxy itemProxy = listProxy.getItemProxy(i);
                values[i] =
                        returnModel ? itemProxy.getModelObject() : itemProxy;
                // Null values are not permitted, so we use a special value to
                // indicate null.
                if (values[i] == null) {
                    values[i] = NULL_VALUE;
                }
            }

            return values;
        } else {
            throw new IllegalArgumentException("Expected a ListProxy " +
                    "but was: " + proxy.getClass().getName());
        }
    }

    // Javadoc inherited
    public void dispose() {
    }

    // Javadoc inherited
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9886/3	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 28-Oct-05	9886/2	adrianj	VBM:2005101811 New theme GUI

 ===========================================================================
*/

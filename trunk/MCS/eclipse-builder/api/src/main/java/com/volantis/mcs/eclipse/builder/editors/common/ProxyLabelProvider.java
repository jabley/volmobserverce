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

import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.Proxy;

import com.volantis.mcs.model.property.PropertyIdentifier;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * A label provider for rendering either a proxy object itself or a specified
 * property of the proxy object. Uses the toString() method on the underlying
 * model object for rendering: this will work for most simple properties, but
 * a more sophisticated renderer may be required for some data types. This can
 * be achieved by extending this class and overriding the {@link #render}
 * method.
 */
public class ProxyLabelProvider extends LabelProvider {
    /**
     * The property identifier for the property of the displayed object to
     * render.
     */
    private PropertyIdentifier property;

    private Image defaultImage;

    /**
     * Construct a label provider that renders proxy objects from the
     * interaction layer by rendering the specified property.
     *
     * <p>If no property is specified, then the underlying model object itself
     * is rendered.</p>
     *
     * @param descriptor The property to render, or null to render the whole
     * @param image
     */
    public ProxyLabelProvider(PropertyIdentifier descriptor, Image image) {
        property = descriptor;
        defaultImage = image;
    }

    // Javdoc inherited
    public String getText(Object o) {
        String label = "";

        if (o != null) {
            Proxy proxy = (Proxy) o;
            if (property != null) {
                BeanProxy bean = (BeanProxy) o;
                proxy = bean.getPropertyProxy(property);
            }

            label = render(proxy.getModelObject());
        }

        return label;
    }

    /**
     * Renders an object as text.
     *
     * <p>This method should be overridden to create label providers that can
     * render more complex objects that can not render themselves using
     * {@link Object#toString}.</p>
     *
     * @param o The object to render
     * @return The string value of that object
     */
    protected String render(Object o) {
        String rendered = "";
        if (o != null) {
            rendered = o.toString();
        }
        return rendered;
    }

    // Javadoc inherited
    public Image getImage(Object o) {
        return (defaultImage == null) ? super.getImage(o) : defaultImage;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9886/2	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 28-Oct-05	9886/1	adrianj	VBM:2005101811 New theme GUI

 ===========================================================================
*/

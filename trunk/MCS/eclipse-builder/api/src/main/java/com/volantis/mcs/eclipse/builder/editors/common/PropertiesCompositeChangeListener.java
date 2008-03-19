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

import com.volantis.mcs.model.descriptor.PropertyDescriptor;

/**
 * Listener for changes to the values in a properties composite.
 */
public interface PropertiesCompositeChangeListener {
    /**
     * Called when a property changed in a composite.
     *
     * @param composite The properties composite that caused the change
     * @param property The descriptor for the property that changed
     * @param newValue The new value of the property
     */
    public void propertyChanged(PropertiesComposite composite,
                                PropertyDescriptor property, Object newValue);
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Oct-05	9961/1	pduffin	VBM:2005101811 Committing restructuring

 31-Oct-05	9886/3	adrianj	VBM:2005101811 New themes GUI

 28-Oct-05	9886/1	adrianj	VBM:2005101811 New theme GUI

 ===========================================================================
*/

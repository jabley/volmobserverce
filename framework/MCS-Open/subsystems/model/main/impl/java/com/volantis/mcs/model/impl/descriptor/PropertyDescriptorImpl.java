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

package com.volantis.mcs.model.impl.descriptor;

import com.volantis.mcs.model.descriptor.PropertyAccessor;
import com.volantis.mcs.model.descriptor.PropertyDescriptor;
import com.volantis.mcs.model.descriptor.TypeDescriptor;
import com.volantis.mcs.model.property.PropertyIdentifier;

public class PropertyDescriptorImpl
        implements PropertyDescriptor {

    protected PropertyIdentifier identifier;
    private TypeDescriptor propertyType;
    private PropertyAccessor propertyAccessor;
    private boolean required;

    /**
     * Internal use only.
     */
    public void setIdentifier(PropertyIdentifier identifier) {
        this.identifier = identifier;
    }

    public PropertyIdentifier getIdentifier() {
        return identifier;
    }

    /**
     * Internal use only.
     */
    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isRequired() {
        return required;
    }


    /**
     * Internal use only.
     */
    public void setPropertyType(TypeDescriptor propertyType) {
        this.propertyType = propertyType;
    }

    public TypeDescriptor getPropertyType() {
        return propertyType;
    }

    /**
     * Internal use only.
     */
    public void setPropertyAccessor(PropertyAccessor propertyAccessor) {
        this.propertyAccessor = propertyAccessor;
    }

    public PropertyAccessor getPropertyAccessor() {
        return propertyAccessor;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Oct-05	9961/7	pduffin	VBM:2005101811 Committing restructuring

 25-Oct-05	9961/3	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/

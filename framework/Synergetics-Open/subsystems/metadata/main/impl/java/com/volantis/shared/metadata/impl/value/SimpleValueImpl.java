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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.shared.metadata.impl.value;

import com.volantis.shared.metadata.value.SimpleValue;
import com.volantis.shared.metadata.impl.persistence.MetadataDAOVisitor;
import com.volantis.shared.metadata.impl.persistence.EntryDAO;

/**
 * Implementation of {@link SimpleValue}.
 */
abstract class SimpleValueImpl
        extends MetaDataValueImpl
        implements SimpleValue {

    /**
     * All simple values must implement this
     *
     * @param value the value to set the string from
     */
    public abstract void setFromString(String value);


    public void initializeInhibitor(MetadataDAOVisitor visitor) {
        EntryDAO entry = visitor.getNextEntry();
        // all simple values use the setFromString() value. Thier value
        // is the last fragment of the path (i.e. the name)
        setFromString(entry.getName());
        return;
    }

    /**
     * Simple base implementation that should work for all simple values
     *
     * @param visitor
     */
    public void visitInhibitor(MetadataDAOVisitor visitor) {
        visitor.add(getAsString(), getClassMapper(), false);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Jan-05	6560/3	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/

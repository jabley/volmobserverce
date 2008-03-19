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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.shared.metadata.value;


/**
 * Interface to mark objects that can be visited by an
 * ImmutableMetadataValueVisitor. Note that this is kept distinct from the
 * existing common superclasses and interfaces of immutable metadata
 * implementations, since they either act as superclasses to other types, or
 * form part of the public API.
 *
 * @see com.volantis.shared.metadata.value.ImmutableMetaDataValueVisitor
 */
public interface ImmutableMetaDataValueVisitee {
    /**
     * Accept a visit request for an immutable metadata value visitor.
     * Implementations should call the appropriate visit method on the
     * visitor.
     * 
     * @param visitor The visitor whose visit method should be invoked
     */
    public void accept(ImmutableMetaDataValueVisitor visitor);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Jan-05	6686/1	pduffin	VBM:2005010506 Completed code to read and write XML versions of the resource components, asset and templates. Also, fixed a few problems with the implementation of the MetaData API

 17-Jan-05	6670/1	adrianj	VBM:2005010506 Implementation of resource asset continued

 ===========================================================================
*/

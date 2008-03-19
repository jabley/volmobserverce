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

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.metadata.value.NumberValue;
import com.volantis.shared.metadata.value.ImmutableMetaDataValueVisitee;
import com.volantis.shared.metadata.value.ImmutableMetaDataValueVisitor;
import com.volantis.shared.metadata.value.immutable.ImmutableNumberValue;
import com.volantis.shared.metadata.impl.persistence.MetadataClassMapper;

import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Implementation of {@link ImmutableNumberValue}.
 */
final class ImmutableNumberValueImpl
        extends NumberValueImpl
        implements ImmutableNumberValue, Serializable,
        ImmutableMetaDataValueVisitee {

    /**
     * The Serial Version UID.
     */
    static final long serialVersionUID = -1462955755847567701L;

    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    public ImmutableNumberValueImpl(NumberValue value) {
        super(value);
    }

    /**
     * Protected method for future use by JDO.
     */
    protected ImmutableNumberValueImpl() {
    }

    /**
     * Used for testing
     *
     * @param number the number to set
     */
    protected ImmutableNumberValueImpl(Number number) {
        setValue(number);
    }

    /**
     * Override to return this object rather than create a new one.
     *
     * <p>This is simply a performance optimisation and has no impact on the
     * behaviour.</p>
     */
    public ImmutableInhibitor createImmutable() {
        return this;
    }

    // Javadoc inherited
    public void accept(ImmutableMetaDataValueVisitor visitor) {
        visitor.visit(this);
    }

    // Javadoc inherited
    public MetadataClassMapper getClassMapper() {
        return MetadataClassMapper.IMMUTABLE_NUMBER_VALUE;
    }

    /**
     * Serialization is buggered for NumberValue so do it by hand
     *
     * @param s
     */
    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeUTF(getAsString());
    }

    /**
     * Again, some custom deserialization
     *
     * @param s
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream s)
        throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        setFromString(s.readUTF());
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Jan-05	6686/1	pduffin	VBM:2005010506 Completed code to read and write XML versions of the resource components, asset and templates. Also, fixed a few problems with the implementation of the MetaData API

 17-Jan-05	6670/3	adrianj	VBM:2005010506 Implementation of resource asset continued

 14-Jan-05	6560/7	tom	VBM:2004122401 Added Inhibitor base class

 13-Jan-05	6560/5	tom	VBM:2004122401 More Metadata API implementation

 10-Jan-05	6560/3	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/

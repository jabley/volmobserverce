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
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.metadata.impl.MetaDataHelper;
import com.volantis.shared.metadata.impl.MetaDataObjectImpl;
import com.volantis.shared.metadata.impl.persistence.MetadataDAOVisitor;
import com.volantis.shared.metadata.impl.persistence.EntryDAO;
import com.volantis.shared.metadata.impl.persistence.MetadataClassMapper;
import com.volantis.shared.metadata.value.ChoiceValue;
import com.volantis.shared.metadata.value.MetaDataValue;
import com.volantis.shared.metadata.value.immutable.ImmutableMetaDataValue;
import com.volantis.synergetics.path.Path;

/**
 * Implementation of {@link ChoiceValue}.
 */
abstract class ChoiceValueImpl
        extends MetaDataValueImpl
        implements ChoiceValue {

    /**
     * The name of the choice.
     */
    private String choiceName;

    /**
     * The value of the choice.
     */
    private ImmutableMetaDataValue value;
    
    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    public ChoiceValueImpl(ChoiceValue value) {
        this.choiceName = value.getChoiceName();
        this.value = value.getValue();
    }

    /**
     * Protected method for future use by JDO.
     */
    protected ChoiceValueImpl() {
    }

    // Javadoc inherited.
    public ImmutableInhibitor createImmutable() {
        return new ImmutableChoiceValueImpl(this);
    }

    // Javadoc inherited.
    public MutableInhibitor createMutable() {
        return new MutableChoiceValueImpl(this);
    }

    // Javadoc inherited.
    public String getAsString() {
        StringBuffer s = new StringBuffer();
        s.append("@");
        s.append(choiceName);
        s.append("=");
        s.append(value.getAsString());
        return s.toString();
    }

    /**
     * Get the magnitude value.
     *
     * @return The magnitude value.
     */
    public String getChoiceName() {
        return choiceName;
    }

    /**
     * Sets the choice name.
     * 
     * @param name
     */
    public void setChoiceName(String name) {
        this.choiceName = name;
    }
    
    /**
     * Get the unit value.
     *
     * @return The unit value.
     */
    public ImmutableMetaDataValue getValue() {
        return value;
    }

    /**
     * Set the unit value.
     *
     * <p>This object stores an immutable instance of the supplied object.</p>
     *
     * <p>This is implemented here as a convenience to simplify the
     * implementation by not requiring derived classes from duplicating this
     * code.</p>
     *
     * <p><strong>Note</strong>: This must only be invoked through the
     * relevant mutator interface; it must never be called directly on this
     * object.</p>
     *
     * @param value The unit value.
     */
    public void setValue(MetaDataValue value) {
        this.value = (ImmutableMetaDataValue) MetaDataHelper.getImmutableOrNull(value);
    }

    // Javadoc inherited.
    public int hashCode() {
        return MetaDataHelper.hashCode(choiceName)
                + MetaDataHelper.hashCode(value);
    }

    // Javadoc inherited.
    public boolean equals(Object other) {
        return (other instanceof ChoiceValue)
                ? equalsChoiceValue((ChoiceValue) other)
                : false;
    }

    /**
     * Helper method for {@link #equals} which compares two objects of this type for
     * equality.
     * @param other The other <code>ChoiceValue</code> to compare this one to.
     * @return true if the all externally visible fields of the other
     *         <code>ChoiceValue</code> are equal to this one.
     */
    protected boolean equalsChoiceValue(ChoiceValue other) {
        return MetaDataHelper.equals(choiceName, other.getChoiceName())
                && MetaDataHelper.equals(value, other.getValue());
    }

    // Javadoc inherited
    public void visitInhibitor(MetadataDAOVisitor visitor) {
        visitor.push(getClassMapper().toString(), getClassMapper());
        visitor.add(choiceName, MetadataClassMapper.NULL, (null == choiceName));
        if (null == value) {
            visitor.add(null, MetadataClassMapper.NULL, true);
        }else {
            ((MetaDataObjectImpl)this.value).visitInhibitor(visitor);
        }
        visitor.pop();
    }

    //Javadoc inherited
    public void initializeInhibitor(MetadataDAOVisitor visitor) {
        // remove the entry corresponding to me
        visitor.getNextEntry();
        // get the entry for choiceName
        EntryDAO entry = visitor.getNextEntry();
        if (!entry.isNull()) {
            this.setChoiceName(Path.parse(entry.getPath()).getName());
        }
        this.value = (ImmutableMetaDataValue) visitor.getNextAsInhibitor();
    }
}

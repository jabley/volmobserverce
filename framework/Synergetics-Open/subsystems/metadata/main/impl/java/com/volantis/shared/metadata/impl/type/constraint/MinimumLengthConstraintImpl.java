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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.shared.metadata.impl.type.constraint;

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.metadata.type.constraint.MinimumLengthConstraint;
import com.volantis.shared.metadata.impl.persistence.MetadataDAOVisitor;
import com.volantis.shared.metadata.impl.persistence.EntryDAO;

/**
 * Implementation of {@link MinimumLengthConstraint}.
 */
abstract class MinimumLengthConstraintImpl extends ConstraintImpl
        implements MinimumLengthConstraint {

    private int limit;

    /**
     * Copy constructor.
     *
     * @param constraint The object to copy.
     */
    protected MinimumLengthConstraintImpl(
            final MinimumLengthConstraint constraint) {

        limit = constraint.getLimit();
    }

    /**
     * Protected constructor for sub classes.
     */
    protected MinimumLengthConstraintImpl() {
    }

    // javadoc inherited
    public ImmutableInhibitor createImmutable() {
        return new ImmutableMinimumLengthConstraintImpl(this);
    }

    // javadoc inherited
    public MutableInhibitor createMutable() {
        return new MutableMinimumLengthConstraintImpl(this);
    }

    // javadoc inherited
    public int getLimit() {
        return limit;
    }

    /**
     * Set the limit of the constraint.
     *
     * <p>This is implemented here as a convenience to simplify the
     * implementation by not requiring derived classes from duplicating this
     * code.</p>
     *
     * <p><strong>Note</strong>: This must only be invoked through the
     * relevant mutator interface; it must never be called directly on this
     * object.</p>
     *
     * @param limit the limit of the constraint.
     */
    public void setLimit(final int limit) {
        this.limit = limit;
    }

    // Javadoc inherited.
    public boolean equals(final Object other) {
        return (other instanceof MinimumLengthConstraint) &&
            getLimit() == ((MinimumLengthConstraint) other).getLimit();
    }

    // Javadoc inherited.
    public int hashCode() {
        return limit;
    }


    // Javadoc inherited
    public void initializeInhibitor(MetadataDAOVisitor visitor) {
        // get myself
        EntryDAO entry = visitor.getNextEntry();
        Integer.parseInt(entry.getName());
    }

    // Javadoc inherited
    public void visitInhibitor(MetadataDAOVisitor visitor) {
        visitor.add(Integer.toString(limit), getClassMapper(),
                    false);
    }
}

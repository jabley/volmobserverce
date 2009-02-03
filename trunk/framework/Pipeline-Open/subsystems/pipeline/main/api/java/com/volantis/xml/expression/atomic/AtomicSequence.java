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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.atomic;

import com.volantis.xml.expression.sequence.Sequence;
import com.volantis.xml.expression.sequence.Item;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.SequenceIndexOutOfBoundsException;
import com.volantis.xml.expression.ExpressionFactory;

import java.io.Serializable;

/**
 * Base class for AtomicValue implementations that avoid garbage from the
 * {@link #getSequence} method by ensuring that the atomic value is
 * its own sequence.
 *
 * <p>Note that an expression factory is taken and stored to simplify
 * the specializations, enabling them to construct other expression
 * types without hard-coding specialization relationships.</p>
 */
public abstract class AtomicSequence implements AtomicValue, Sequence, Serializable {
    /**
     * The factory by which this value was created.
     */
    protected final ExpressionFactory factory;

    /**
     * Initializes the new instance with the given parameters.
     *
     * @param factory the factory by which the value was created
     */
    protected AtomicSequence(ExpressionFactory factory) {
        this.factory = factory;
    }

    // javadoc inherited
    public Sequence getSequence() throws ExpressionException {
        return this;
    }

    // javadoc inherited
    public int getLength() {
        return 1;
    }

    // javadoc inherited
    public Item getItem(int index)
            throws SequenceIndexOutOfBoundsException {
        if (index == 1) {
            return this;
        } else {
            throw new SequenceIndexOutOfBoundsException(
                    "index " + index + " out of bounds (1..1)");
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	8967/1	pduffin	VBM:2005070702 Refactored resolving of expressions into component identities

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	222/1	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 ===========================================================================
*/

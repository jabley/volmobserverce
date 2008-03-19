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
package com.volantis.xml.expression.sequence;

import com.volantis.xml.expression.SequenceIndexOutOfBoundsException;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.atomic.StringValue;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * A sequence of items.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation in user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 *
 * <p><strong>Indices for sequence items are in the range from 1 to the length
 * of the sequence inclusive. Sequence indices are NOT zero based like Java
 * arrays.</strong></p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface Sequence extends Value {
    /**
     * An empty sequence.
     */
    public static final Sequence EMPTY =
            new SimpleSequence(ExpressionFactory.getDefaultInstance(),
                               null);

    /**
     * Get the length of the sequence.
     *
     * @return The length of the sequence.
     */
    public int getLength();

    /**
     * Get the item at the specified index.
     *
     * @param index
     * @return The item at the specified index.
     * @throws SequenceIndexOutOfBoundsException
     *          If the index is less than 1, or greater than the length.
     */
    public Item getItem(int index)
            throws SequenceIndexOutOfBoundsException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	222/2	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 ===========================================================================
*/

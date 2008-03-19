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

package com.volantis.xml.expression;

/**
 * Indicates that an attempt was made to access a sequence item using an index
 * that was out of bounds.
 *
 * <p>i.e. index &lt; 1, or greater than the length of the sequence.</p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public class SequenceIndexOutOfBoundsException extends ExpressionException {
    /**
     * Create a new <code>SequenceIndexOutOfBoundsException</code> with no
     * message.
     */
    public SequenceIndexOutOfBoundsException() {
    }

    /**
     * Create a new <code>SequenceIndexOutOfBoundsException</code> with the
     * specified message.
     *
     * @param message The message.
     */
    public SequenceIndexOutOfBoundsException(String message) {
        super(message);
    }

    /**
     * Create a new <code>SequenceIndexOutOfBoundsException</code> with the
     * specified message which was caused by the specified Throwable.
     *
     * @param message The message.
     * @param cause   The cause of this throwable being thrown.
     */
    public SequenceIndexOutOfBoundsException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Create a new <code>SequenceIndexOutOfBoundsException</code> which was
     * caused by the specified Throwable.
     *
     * @param cause The cause of this throwable being thrown.
     */
    public SequenceIndexOutOfBoundsException(Throwable cause) {
        super(cause);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	222/1	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 ===========================================================================
*/

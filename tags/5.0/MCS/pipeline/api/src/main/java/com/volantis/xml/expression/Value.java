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

import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.sequence.Sequence;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * The base interface for values that can be used in an expression.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation in user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 *
 * <h2>Immutable</h2>
 *
 * <p>All value objects are immutable once they have been created.</p>
 *
 * <h2>Null Value</h2>
 *
 * <p>An empty sequence is the XPath 2.0 equivalent of a Java null value. See
 * {@link Sequence#EMPTY}.</p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 *
 * @mock.generate 
 */
public interface Value {
    /**
     * Return the string value of this value.
     *
     * <p>The string value returned is the value that would be returned by the
     * XPath 2.0 <a href="http://www.w3.org/TR/xpath-datamodel/#dm-string-value">string-value</a>
     * accessor.
     *
     * @return The string value of this value.
     */
    public StringValue stringValue() throws ExpressionException;

    /**
     * Stream the value as a series of SAX events.
     *
     * @param contentHandler The ContentHandler into which the SAX events
     *                       should be sent.
     * @throws ExpressionException If there was an error in the value.
     * @throws SAXException        If there was an error returned by the
     *                             ContentHandler.
     */
    public void streamContents(ContentHandler contentHandler)
            throws ExpressionException, SAXException;

    /**
     * Get the sequence that encapsulates this object.
     *
     * <p>It is perfectly acceptable although implementation dependent for this
     * method to return a reference to this object.</p>
     *
     * @return The sequence that encapsulates this object.
     */
    public Sequence getSequence() throws ExpressionException;
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

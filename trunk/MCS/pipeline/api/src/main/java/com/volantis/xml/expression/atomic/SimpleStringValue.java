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

import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * This is a simple, generic implementation of the StringValue interface.
 *
 * @see com.volantis.xml.expression.ExpressionFactory
 */
public class SimpleStringValue extends AtomicSequence implements StringValue {
    /**
     * The value wrappered by this class.
     */
    private String value;

    /**
     * Initialize the new instance using the given parameters. A null
     * value is treated as the empty string for XPath 2.0 compatibility.
     *
     * @param factory the factory by which the value was created
     * @param value   the value to be wrappered
     */
    public SimpleStringValue(ExpressionFactory factory, String value) {
        super(factory);

        if (value == null) {
            this.value = "";
        } else {
            this.value = value;
        }
    }

    // javadoc inherited
    public StringValue stringValue() throws ExpressionException {
        return this;
    }

    // javadoc inherited
    public void streamContents(ContentHandler contentHandler)
            throws ExpressionException, SAXException {
        // No XML special character encoding is needed here
        contentHandler.characters(value.toCharArray(), 0, value.length());
    }

    // javadoc inherited
    public String asJavaString() {
        return value;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	222/1	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 ===========================================================================
*/

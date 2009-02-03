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
package com.volantis.xml.expression.atomic.numeric;

import com.volantis.xml.expression.atomic.AtomicSequence;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * This is a simple, generic implementation of the IntValue interface.
 *
 * @see com.volantis.xml.expression.ExpressionFactory
 */
public class SimpleIntValue extends AtomicSequence implements IntValue {
    /**
     * The value wrappered by this class.
     */
    private int value;

    /**
     * Initializes the new instance with the given parameters.
     *
     * @param factory the factory by which the value was created
     * @param value   the value to be wrappered
     */
    public SimpleIntValue(ExpressionFactory factory, int value) {
        super(factory);

        this.value = value;
    }

    // javadoc inherited
    public int asJavaInt() {
        return value;
    }

    // javadoc inherited
    public StringValue stringValue() throws ExpressionException {
        return factory.createStringValue(Integer.toString(value));
    }

    // javadoc inherited
    public void streamContents(ContentHandler contentHandler)
            throws ExpressionException, SAXException {
        String string = Integer.toString(value);
        contentHandler.characters(string.toCharArray(), 0, string.length());
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

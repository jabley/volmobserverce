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
 * This is a simple, generic implementation of the BooleanValue interface.
 *
 * @see com.volantis.xml.expression.ExpressionFactory
 */
public class SimpleBooleanValue extends AtomicSequence implements BooleanValue{
    /**
     * The value wrappered by this class.
     */
    private boolean value;

    /**
     * Initializes the new instance with the given parameters.
     *
     * @param factory the factory by which the value was created
     * @param value the value to be wrappered
     */
    public SimpleBooleanValue(ExpressionFactory factory,
                              boolean value) {
        super(factory);

        this.value = value;
    }

    // javadoc inherited
    public boolean asJavaBoolean() {
        return value;
    }

    // javadoc inherited
    public StringValue stringValue() throws ExpressionException {
        return factory.createStringValue(valueAsString());
    }

    // javadoc inherited
    public void streamContents(ContentHandler contentHandler)
            throws ExpressionException, SAXException {
        String string = valueAsString();

        contentHandler.characters(string.toCharArray(), 0, string.length());
    }

    /**
     * Returns a string equivalent to the current wrappered value.
     *
     * @return either "true" or "false" depending on the current wrappered
     *         value
     */
    private String valueAsString() {
        String string;

        if (value) {
            string = Boolean.TRUE.toString();
        } else {
            string = Boolean.FALSE.toString();
        }

        return string;
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

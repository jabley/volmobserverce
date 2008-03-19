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
package com.volantis.xml.expression.impl;

import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.sequence.Sequence;
import com.volantis.xml.expression.Variable;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.namespace.ImmutableExpandedName;
import org.xml.sax.ContentHandler;

/**
 * Simple implementation of the Variable interface.
 *
 * @see com.volantis.xml.expression.impl.SimpleExpressionFactory
 */
public class SimpleVariable implements Variable {

    /**
     * A special value that is used to indicate that the variable is unset,
     * could probably have used null but this seemed like the best idea at
     * the time.
     */
    public static final Value UNSET = new Value() {
        public StringValue stringValue() {
            throw new IllegalStateException("Variable not set");
        }

        public void streamContents(ContentHandler contentHandler) {
            throw new IllegalStateException("Variable not set");
        }

        public Sequence getSequence() {
            throw new IllegalStateException("Variable not set");
        }
    };

    /**
     * The factory by which this variable was created.
     */
    protected ExpressionFactory factory;

    /**
     * The name for this variable.
     */
    private final ImmutableExpandedName name;

    /**
     * The value for this variable.
     */
    private Value value;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param factory the factory by which this variable was created
     * @param name    the name for this variable
     * @param value   the value for this variable
     */
    public SimpleVariable(ExpressionFactory factory,
                          ExpandedName name,
                          Value value) {
        this.factory = factory;
        this.name = name.getImmutableExpandedName();

        this.value = value;
    }

    // javadoc inherited
    public ExpandedName getName() {
        return name;
    }

    // javadoc inherited
    public Value getValue() {
        if (value == UNSET) {
            throw new IllegalStateException("Variable " + name +
                    " has not been set");
        }
        return value;
    }

    // Javadoc inherited.
    public void setValue(Value value) {
        this.value = value;
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

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
package com.volantis.xml.expression.functions;

import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.ExpressionException;

/**
 * Abstract base class for {@link Function} implementations
 */
public abstract class AbstractFunction implements Function {

    /**
     * Provide the name of the function to be invoked as a String. This
     * is for use in messages.
     * @return The name of the function to be invokded.
     */
    protected abstract String getFunctionName();

    /**
     * Method that is intended to be used to test the number of
     * arguments that a functions receives. If the arg array does
     * not contain the expected number of <code>Value</code> arguments
     * an {@link ExpressionException} is thrown
     * @param args the array of <code>Value</code> objects
     * @param expectedArgCount the expected number of arguments
     * @throws ExpressionException if an error occurs
     */
    protected void assertArgumentCount(Value[] args, int expectedArgCount)
            throws ExpressionException {

        int actualArgCount = (args != null) ? args.length : 0;

        if (actualArgCount != expectedArgCount) {
            throw new ExpressionException(
                    "Invalid argument count for function: " +
                    getFunctionName() +
                    " expected argument count: " + expectedArgCount +
                    " actual argument count: " + actualArgCount);
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/

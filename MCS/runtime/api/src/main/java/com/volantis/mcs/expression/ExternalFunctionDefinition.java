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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.expression;

import com.volantis.xml.expression.Function;
import com.volantis.xml.namespace.ImmutableExpandedName;

/**
 * This class is a container for function definitions created outside of
 * the expressions package. It is used by the registration process to avoid 
 * the use of Map implementations.
 */
public class ExternalFunctionDefinition {

    /**
     *  Volantis copyright mark.
     */
    private static String mark =
        "(c) Volantis Systems Ltd 2004. ";
        
    /**
     * The ImmutableExpandedName for this function.
     */
    private ImmutableExpandedName immutableExpandedName;

    /**
     * The class implementing the function.
     */
    private Function function;
    
    /**
    * Construct a new ExternalFunctionDefinition.
    * @param immutableExpandedName The ImmutableExpandedName for this function.
    * @param function The implementing class for the function
    */
    public ExternalFunctionDefinition(
        ImmutableExpandedName immutableExpandedName, Function function) {

        this.immutableExpandedName = immutableExpandedName;
        this.function = function;
    }

    /**
     * Get the ImmutableExpandedName for this function.
     * @return the ImmutableExpandedName for this function.
     */
    public ImmutableExpandedName getImmutableExpandedName() {
        return this.immutableExpandedName;
    }
    
    /**
     * Get the class implementing the Function.
     * @return the class implementing the Function.
     */
    public Function getFunction() {
        return this.function;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Feb-04	2966/1	ianw	VBM:2004011923 Added mcsi:policy function

 ===========================================================================
*/

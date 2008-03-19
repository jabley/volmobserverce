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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.styling.impl.compiler;

import com.volantis.mcs.themes.StyleFunctionCall;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueVisitorStub;
import com.volantis.mcs.themes.StyleList;
import com.volantis.styling.compiler.FunctionResolver;
import com.volantis.styling.expressions.StylingFunction;
import com.volantis.styling.impl.expressions.FunctionCall;
import com.volantis.styling.impl.expressions.StyleCompiledExpression;
import com.volantis.styling.impl.expressions.ListExpression;
import com.volantis.styling.impl.expressions.ArgumentsImpl;
import com.volantis.styling.impl.expressions.ArgumentsImpl;
import com.volantis.styling.impl.expressions.Arguments;

import java.util.List;
import java.util.ArrayList;

/**
 * Compile a value into a more efficient representation if necessary.
 */
public class ValueCompilerImpl
        extends StyleValueVisitorStub
        implements ValueCompiler {

    private final FunctionResolver functionResolver;

    private StyleValue compiledValue;

    public ValueCompilerImpl(FunctionResolver functionResolver) {
        this.functionResolver = functionResolver;
    }

    // Javadoc inherited.
    public StyleValue compile(StyleValue value) {

        // Save this away just in case we are being called from with the visit
        // method.
        StyleValue previousCompiledValue = compiledValue;

        // Pass through the value by default.
        compiledValue = value;

        value.visit(this, null);

        StyleValue currentCompiledValue = compiledValue;

        // Reset the previous state.
        compiledValue = previousCompiledValue;

        return currentCompiledValue;
    }

    // Javadoc inherited.
    public void visit(StyleFunctionCall value, Object object) {
        String name = value.getName();
        StylingFunction function = functionResolver.resolve(name);
        if (function == null) {
            throw new IllegalStateException("Unknown function '" + name + "'");
        }

        List values = value.getArguments();
        Arguments arguments = compileArguments(values);
        FunctionCall call = new FunctionCall(name, function, arguments);

        compiledValue = new StyleCompiledExpression(call);
    }

    // Javadoc inherited.
    public void visit(StyleList value, Object object) {
        // This could contain values that need compiling so assume that it
        // does, create a new list to contain the compiled values and then
        // try and compile them. If any of them were compiled then create a
        // compiled version of the list, otherwise just return it.
        List compiledList = new ArrayList();
        List list = value.getList();
        boolean containsCompiledValue = false;
        for (int i = 0; i < list.size(); i++) {
            StyleValue item = (StyleValue) list.get(i);

            // Compile the item if the value changes then remember.
            StyleValue compiledItem = compile(item);
            if (compiledItem == null) {
                throw new IllegalStateException("List item " + i +
                        " '" + String.valueOf(item) +
                        "' could not be compiled");
            } else {
                compiledList.add(compiledItem);
            }
            if (compiledItem != item) {
                containsCompiledValue = true;
            }
        }

        // If the list contained any compiled values then create a special
        // list expression.
        if (containsCompiledValue) {
            ListExpression expression = new ListExpression(compiledList);
            compiledValue = new StyleCompiledExpression(expression);
        }
    }

    /**
     * Compile the list of arguments.
     *
     * @param arguments The list of arguments as {@link StyleValue}s.
     * @return The arguments.
     */
    private Arguments compileArguments(List arguments) {

        // This could contain values that need compiling so assume that it
        // does, create a new list to contain the compiled values and then
        // try and compile them. If any of them were compiled then create a
        // compiled version of the list, otherwise just return it.
        List compiledList = new ArrayList();
        boolean containsCompiledValue = false;
        for (int i = 0; i < arguments.size(); i++) {
            StyleValue item = (StyleValue) arguments.get(i);

            // Compile the item if the value changes then remember.
            StyleValue compiledItem = compile(item);
            if (compiledItem == null) {
                throw new IllegalStateException("Argument " + i +
                        " '" + String.valueOf(item) +
                        "' could not be compiled");
            } else {
                compiledList.add(compiledItem);
            }
            if (compiledItem != item) {
                containsCompiledValue = true;
            }
        }

        // If the list contained any compiled values then create a special
        // list expression.
        if (containsCompiledValue) {
            arguments = compiledList;
        }
        return new ArgumentsImpl(arguments, containsCompiledValue);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 ===========================================================================
*/

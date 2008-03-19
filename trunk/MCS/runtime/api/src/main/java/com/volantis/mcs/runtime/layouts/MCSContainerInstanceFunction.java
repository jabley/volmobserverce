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

package com.volantis.mcs.runtime.layouts;

import com.volantis.mcs.context.FormatReferenceFinder;
import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.themes.StyleString;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.expressions.AbstractStylingFunction;
import com.volantis.styling.expressions.EvaluationContext;

import java.util.List;

/**
 * Implementation of the mcs-container-instance function used within CSS.
 *
 * <p>The first argument is the name of the container (pane or region) as a
 * string, subsequent arguments are the indexes of the container instance
 * within the possibly many spatial iterators.</p>
 */
public class MCSContainerInstanceFunction
     extends AbstractStylingFunction {

    // Javadoc inherited.
    public StyleValue evaluate(
            EvaluationContext context, String name, List arguments) {

        StyleString string = (StyleString) arguments.get(0);
        String containerName = string.getString();

        // Create an array of the indeces.
        int indeces[] = new int[arguments.size() - 1];
        for (int i = 0; i < indeces.length; i++) {
            indeces[i] = getArgumentAsInt(name, arguments, i + 1);
        }

        FormatReferenceFinder finder = (FormatReferenceFinder)
                context.getProperty(FormatReferenceFinder.class);
        FormatReference reference = finder.getFormatReference(
                containerName, indeces);

        return new StyleFormatReference(reference);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 ===========================================================================
*/

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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.styling.impl.sheet;

import com.volantis.mcs.themes.Priority;
import com.volantis.styling.compiler.Source;
import com.volantis.styling.compiler.Specificity;
import com.volantis.styling.debug.Debuggable;
import com.volantis.styling.impl.engine.StylerContext;
import com.volantis.styling.impl.engine.StylerResult;
import com.volantis.styling.impl.engine.selectionstates.SelectionState;

/**
 * Conditionally updates styles in the context.
 *
 * <p>This first looks to see if this matches the element in the current
 * context. If it does then it updates the styles in the context, otherwise it
 * does nothing. Either way it returns the result of its efforts.</p>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate
 */
public interface Styler
        extends Debuggable {

    /**
     * Get the source of the styler.
     *
     * @return The source of the styler.
     */
    Source getSource();

    /**
     * Get the specificity of the styler.
     *
     * @return The specificity.
     */
    Specificity getSpecificity();

    /**
     * Get the priority of the styler.
     */
    Priority getPriority();

    /**
     * Get the style selection state required for this styler to trigger, if
     * any.
     *
     * <p>Note that this is <em>not</em> part of the matcher for this styler -
     * if the state is not present, then the matcher should not be called.</p>
     *
     * @return
     */
    SelectionState getRequiredSelectionState();

    /**
     * If the styler matches then update the styles in the context.
     *
     * @param context The context within which the styling is done.
     *
     * @return The result of styling.
     */
    StylerResult style(StylerContext context);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10641/1	geoff	VBM:2005113024 Pagination page rendering issues

 06-Dec-05	10621/1	geoff	VBM:2005113024 Pagination page rendering issues

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 18-Aug-05	9007/3	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 18-Jul-05	9029/2	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/

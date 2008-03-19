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

package com.volantis.styling.impl.engine;

import com.volantis.mcs.themes.Priority;
import com.volantis.styling.compiler.Source;
import com.volantis.styling.compiler.Specificity;
import com.volantis.styling.debug.DebugStylingWriter;
import com.volantis.styling.impl.engine.matchers.Matcher;
import com.volantis.styling.impl.engine.matchers.MatcherContext;
import com.volantis.styling.impl.engine.matchers.MatcherResult;
import com.volantis.styling.impl.sheet.Styler;
import com.volantis.styling.impl.sheet.StylesDelta;

/**
 * Implementation of {@link Styler}.
 */
public class StylerImpl
        implements Styler {

    /**
     * The source of the properties.
     */
    protected Source source;

    /**
     * The specificity of this styler.
     */
    protected final Specificity specificity;

    /**
     * The priority of this styler.
     */
    protected final Priority priority;

    /**
     * The object that is responsible for determining whether this matches
     * the current styler context.
     */
    protected final Matcher matcher;

    /**
     * The change to the style that is applied if the matcher matchers.
     */
    private final StylesDelta delta;

    /**
     * Initialise.
     *
     *@param source
     * @param specificity The specificity of the selector from which this
     * @param matcher The matcher that is responsible for determining whether
     * @param delta The change to apply to the styles being constructed.
     */
    public StylerImpl(
            Source source, Priority priority, Specificity specificity,
            Matcher matcher, StylesDelta delta) {


        if (source == null) {
            throw new IllegalArgumentException("source cannot be null");
        }
        if (priority == null) {
            throw new IllegalArgumentException("priority cannot be null");
        }
        if (specificity == null) {
            throw new IllegalArgumentException("specificity cannot be null");
        }
        if (matcher == null) {
            throw new IllegalArgumentException("matcher cannot be null");
        }
        if (delta == null) {
            throw new IllegalArgumentException("delta cannot be null");
        }

        this.source = source;
        this.priority = priority;
        this.specificity = specificity;
        this.matcher = matcher;
        this.delta = delta;
    }

    // Javadoc inherited.
    public Source getSource() {
        return source;
    }

    // Javadoc inherited.
    public Specificity getSpecificity() {
        return specificity;
    }

    // Javadoc inherited.
    public Priority getPriority() {
        return priority;
    }

    // Javadoc inherited.
    public StylerResult style(StylerContext context) {

        // Check to see whether the matcher matches the current element.
        MatcherContext matcherContext =
                context.getMatcherContext();
        MatcherResult result = matcher.matches(matcherContext);
        StylerResult stylerResult;
        if (result == MatcherResult.MATCHED) {
            // The matcher matched so merge the styles in.
            delta.applyTo(context);
            stylerResult = StylerResult.STYLED;

        } else if (result == MatcherResult.DEFERRED) {
            stylerResult = StylerResult.DEFERRED;
        } else {
            stylerResult = StylerResult.STYLED;
        }

        return stylerResult;
    }

    // Javadoc inherited.
    public void debug(DebugStylingWriter writer) {
        writer.print(source).print(", ").print(priority).print(", ").
                print(specificity).print(" ").print(matcher).print(" ").
                print(" ").print(delta).newline();
    }
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

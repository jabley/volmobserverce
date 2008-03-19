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

package com.volantis.styling.impl.engine.matchers.composites;

import com.volantis.styling.debug.DebugStylingWriter;
import com.volantis.styling.impl.engine.matchers.Matcher;
import com.volantis.styling.impl.engine.matchers.MatcherBuilderContext;
import com.volantis.styling.impl.engine.matchers.MatcherContext;
import com.volantis.styling.impl.engine.matchers.MatcherResult;
import com.volantis.styling.impl.state.StateIdentifier;

/**
 * Matches elements that have a specific relationship with one another.
 *
 * <p>This matches if and only if the subject matches an element that has a
 * specific relationship with the element matched by the contextual matcher.
 * Therefore, it first attempts to match an element against the contextual
 * matcher, recording whether it matched or not. If it did not match then it
 * tries again on the next element. If it did match then it tries to match the
 * subject matcher against all descendant elements.</p>
 *
 * <p>The relationship is defined by the state associated with the matcher.</p>
 */
public class CompositeMatcher
        implements Matcher, DepthChangeListener {

    /**
     * The contextual matcher.
     */
    private final Matcher contextual;

    /**
     * The subject matcher.
     */
    private final Matcher subject;

    /**
     * The identifier used to retrieve the state from the context.
     */
    private final StateIdentifier identifier;

    /**
     * The operator associated with the factory.
     */
    private final String operator;

    /**
     * Initialise.
     *
     * @param contextual The contextual matcher.
     * @param subject The subject matcher.
     * @param factory The factory to use to create the state.
     * @param context The context within which this is being built.
     */
    public CompositeMatcher(Matcher contextual, Matcher subject,
                            CompositeStateFactory factory,
                            MatcherBuilderContext context) {

        if (subject == null) {
            throw new IllegalArgumentException("subject cannot be null");
        }
        if (contextual == null) {
            throw new IllegalArgumentException("contextual cannot be null");
        }

        this.subject = subject;
        this.contextual = contextual;
        this.operator = factory.getOperator();

        this.identifier = context.register(factory);
        context.addDepthChangeListener(this);
    }

    // Javadoc inherited.
    public MatcherResult matches(MatcherContext context) {

        CompositeState state = (CompositeState) context.getState(identifier);

        // Match the subject selector if there is a potential relationship
        // between the current element and the element matched by the
        // contextual matcher.
        MatcherResult result = MatcherResult.FAILED;
        boolean directRelationship = state.hasDirectRelationship();
        boolean potentialRelationship = directRelationship ||
                state.hasPotentialIndirectRelationship();
        if (potentialRelationship) {
            // Update the context with information about whether the current
            // element has a direct relationship with the element matched by
            // the context.
            boolean oldDirectRelationship =
                    context.changeDirectRelationship(directRelationship);

            // Match the subject.
            result = subject.matches(context);

            // Restore the old value of the direct relationship flag so that
            // the following code that matches the context will do so within
            // the containing (if any) composite matcher.
            context.changeDirectRelationship(oldDirectRelationship);
        }

        // Try and match the contextual matcher against the current element if
        // one of the following holds.
        // 1) The current element does not have a direct relationship to the
        //    element matched by the context (if any).
        // 2) The state supports indirect relationships.
        if (!directRelationship || state.supportsIndirectRelationships()) {
            // The contextual matcher has not matched so try and match it.
            MatcherResult contextResult = contextual.matches(context);
            if (contextResult == MatcherResult.MATCHED) {
                state.contextMatched();
            }
        }

        return result;
    }

    // Javadoc inherited.
    public void beforeStartElement(MatcherContext context) {
        CompositeState state = (CompositeState) context.getState(identifier);
        state.beforeStartElement();
    }

    // Javadoc inherited.
    public void afterEndElement(MatcherContext context) {
        CompositeState state = (CompositeState) context.getState(identifier);
        state.afterEndElement();
    }

    // Javadoc inherited.
    public void debug(DebugStylingWriter writer) {
        writer.print(operator);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10583/1	pduffin	VBM:2005112205 Fixed issues with styling using nested child selectors

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Fixed issue with build

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 18-Jul-05	9029/2	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/

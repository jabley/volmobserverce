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

package com.volantis.styling.impl.engine.matchers;

import com.volantis.styling.debug.DebugStylingWriter;

/**
 * Supports the CSS <code>nth-child</code> pseudo element.
 *
 * <p>This is specified in a very confusing way so some examples will help
 * clarify its behaviour. The specification states that
 * <code>:nth-child(an+b)</code> matches those elements that have
 * <code>an + b - 1</code> preceding siblings, where <code>n</code> can be any
 * non-negative integer and <code>a</code> and <code>b</code> are integers,
 * either negative, zero, or positive.</p>
 *
 * <p>e.g. if <code>a</code> is <code>2</code> and <code>b</code> is
 * <code>1</code> then elements that have <code>2n</code> preceding siblings
 * match, i.e. those element that have <code>0</code>, <code>2</code>,
 * <code>4</code>, ... preceding siblings.</p>
 *
 * <p>Another way of specifying this that makes it easier to process is that
 * it matches elements that can satisfy the equation <code>p = an + b</code>,
 * for a non negative integer <code>n</code>, where <code>p</code> is the
 * <code>1</code> based position of the element within the list of sibling
 * elements.</p>
 *
 * <p>The above equation is not easy to test in as it requires looping though
 * values of <code>n</code> until either it matches, or exceeds the position of
 * the elemnt. However, some simple manipulation produces the following
 * equation which while still having the same problem looks simpler to test.</p>
 * <pre>(p - b) / a = n</pre>
 *
 * <p>Unfortunately, the manipulation is not mathematically valid as it does
 * not take into account the fact that <code>a</code> may be <code>0</code>. In
 * this case the original equation reduces to <code>p = b</code>. If this edge
 * case is dealt with separately then the above equation is valid again.</p>
 *
 * <p>A further manipulation takes advantage of the fact that if
 * <code>x</code>, <code>y<code> and <code>n</code> are all integers then
 * <code>x / y = n  if and only if  x % y = 0</code>. This means that the above
 * equation can be further reduced to the following which is very simple to
 * test.</p>
 * <pre>(p - b) % a = 0</pre>
 *
 * <p>One additional thing to consider is that if <code>(p - b) / a</code> is
 * negative then it never matches as <code>n</code> is non-negative. This
 * will only happen if the sign of <code>(p - b)</code> is different to the
 * sign of <code>a</code>. This should be tested before testing the result of
 * the above equation as modulus has some unintuitive behaviour when it comes
 * to handling negative numbers.</p>
 *
 * <h3>Behaviour</h3>
 *
 * <p>The following examples attempt to clarify the effect and possible usage
 * of the different combinations of values. All the examples represent the
 * sequence of child elements as a {@link java.util.List}
 * (<code>sequence</code>) to illustrate the behaviour.</p>
 *
 * <p>When <code>a = 0</code> then <code>b</code> specifies the position
 * of the matching element. i.e. the only element that matches is
 * <code>sequence.get(b)</code>.</p>
 *
 * <p>When <code>a = 1</code> then <code>b</code> specifies the position of the
 * first element that matches, and all following elements also match. i.e. the
 * elements that match are <code>sequence.subList(b, sequence.size())</code>.
 * </p>
 *
 * <p>When <code>a = -1</code> then <code>b</code> specifies the position of
 * the last element that matches, an all preceding elements also match. i.e.
 * the elements that match are <code>sequence.subList(0, b)</code>.</p>
 */
public class NthChildMatcher
        extends AbstractSimpleMatcher {

    /**
     * The parameter <code>a</code> in <code>p = an + b</code>.
     */
    private final int a;

    /**
     * The parameter <code>b</code> in <code>p = an + b</code>.
     */
    private final int b;

    public NthChildMatcher(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public MatcherResult matchesWithinContext(MatcherContext context) {
        int p = context.getPosition();

        // Check the edge case when a is 0 to avoid a divide by zero exception.
        if (a == 0) {
            return p == b ? MatcherResult.MATCHED : MatcherResult.FAILED;
        }

        // Check to see whether the signs of (p - b) and a are different, if
        // they are then the result of dividing them is going to be negative
        // which will never match so checking the signs first will prevent
        // us from doing an unnecessary division and will also allows us to
        // avoid having to use modulus with negative values which has 'strange'
        // behaviour. If (p - b) is 0 then it will always match, whatever the
        // sign of a.
        int pb = p - b;
        if (pb == 0) {
            return MatcherResult.MATCHED;
        } else {
            int pbSign = pb < 0 ? -1 : 1;
            int aSign = a < 0 ? -1 : 1;
            if (pbSign != aSign) {
                return MatcherResult.FAILED;
            }
        }

        // Now check to see whether this matches.
        return (pb % a == 0) ? MatcherResult.MATCHED : MatcherResult.FAILED;
    }

    // Javadoc inherited.
    public void debug(DebugStylingWriter writer) {
        writer.print("nth-child(").print(a).print("n+").print(b).print(")");
    }

    // Javadoc inherited
    public int hashCode() {
        return 31 * a + b;
    }

    // Javadoc inherited
    public boolean equals(Object obj) {
        if (obj != null && getClass() == obj.getClass()) {
            NthChildMatcher other = (NthChildMatcher) obj;
            return a == other.a && b == other.b;
        } else {
            return false;
        }
    }

    public String toString() {
        return "nth-child(" + a + "n+" + b + ")";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10583/1	pduffin	VBM:2005112205 Fixed issues with styling using nested child selectors

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/

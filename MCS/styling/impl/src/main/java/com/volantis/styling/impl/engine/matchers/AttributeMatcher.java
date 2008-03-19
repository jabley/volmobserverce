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

import com.volantis.styling.impl.engine.matchers.constraints.ValueConstraint;
import com.volantis.styling.impl.engine.matchers.constraints.ListContains;
import com.volantis.styling.impl.engine.matchers.constraints.Equals;
import com.volantis.styling.debug.DebugStylingWriter;

/**
 * Matches if an attribute value satisfies a particular constraint.
 *
 * <p>This class handles the retrieving of the attribute value from the current
 * context and delegates the work involved in checking if the constrain is
 * satisfied to another object.</p>
 */
public class AttributeMatcher
        extends AbstractSimpleMatcher {

    /**
     * The namespace of the attribute, will be null if the attribute belongs
     * to the element, rather than to a particular namespace.
     */
    private final String namespace;

    /**
     * The name of the attribute, may not be null.
     */
    private final String localName;

    /**
     * The constraint, may not be null.
     */
    private final ValueConstraint constraint;

    /**
     * Initialise.
     *
     * @param namespace The namespace of the attribute, null if the attribute
     * does not belong in a namespace, i.e. it belongs to the element.
     * @param localName The local name of the attribute, may not be null.
     * @param constraint The constraint used to test the attribute, may not be
     * null.
     */
    public AttributeMatcher(String namespace, String localName,
                            ValueConstraint constraint) {
        if (localName == null) {
            throw new IllegalArgumentException("localName cannot be null");
        }
        if (constraint == null) {
            throw new IllegalArgumentException("constraint cannot be null");
        }
        this.namespace = namespace;
        this.localName = localName;
        this.constraint = constraint;
    }

    // Javadoc inherited.
    public MatcherResult matchesWithinContext(MatcherContext context) {
        String value = context.getAttributeValue(namespace, localName);
        boolean matched = constraint.satisfied(value);
        return matched ? MatcherResult.MATCHED : MatcherResult.FAILED;
    }

    // Javadoc inherited.
    public void debug(DebugStylingWriter writer) {
        if (namespace == null && localName.equals("class")
                && constraint instanceof ListContains) {

            ListContains classConstraint = (ListContains) constraint;
            writer.print(".").print(classConstraint.getConstraintValue());
        } else if (namespace == null && localName.equals("class")
                && constraint instanceof Equals) {

            Equals equalsConstraint = (Equals) constraint;
            writer.print("#").print(equalsConstraint.getConstraintValue());

        } else {
            writer.print("[");
            if (namespace != null) {
                writer.print(namespace).print("|");
            }
            writer.print(localName)
                    .print(constraint)
                    .print("]");
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10583/1	pduffin	VBM:2005112205 Fixed issues with styling using nested child selectors

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/

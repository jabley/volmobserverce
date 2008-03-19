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
package com.volantis.mcs.eclipse.ab.editors.dom.validation;

import org.jdom.Element;
import com.volantis.mcs.xml.validation.ErrorReporter;
import com.volantis.xml.jdom.Visitor;
import com.volantis.xml.jdom.SimpleVisitor;
import com.volantis.xml.jdom.Walker;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.DOMConstraint;

import java.util.Map;
import java.util.HashMap;

/**
 * This class can be specialized to perform a set of given constraints against
 * a given DOM structure and using an optional error reporter. It can be asked
 * to execute all constraints over the entire given DOM sub-tree, or it can be
 * asked to terminate early if a violation is detected.
 */
public abstract class DOMConstraints implements DOMConstraint {
    /**
     * This map contains all the constraints that must be applied to
     * specifically named elements. The map is keyed on the names of the
     * element to which the constraint is to be applied and contains either a
     * single constraint instance or an array of constraints where needed.
     *
     * @supplierRole constraints
     * @supplierCardinality 0..*
     * @associates <{Constraint}>
     */
    protected Map constraints = new HashMap();

    /**
     * Indicates whether the checker should stop after the first violation
     */
    protected boolean stopOnFirstViolation;

    /**
     * Used to populate the {@link #constraints} map on construction.
     */
    protected abstract void initialize();

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param stopOnFirstViolation indicates how the checker should behave on
     *                             detecting a violation
     */
    public DOMConstraints(boolean stopOnFirstViolation) {
        this.stopOnFirstViolation = stopOnFirstViolation;

        initialize();
    }

    /**
     * Executes the entire set of assigned constraints across all nodes within
     * the DOM rooted at the given element (this may be a sub-tree).
     *
     * @param root          the element at which constraint checks should be
     *                      started
     * @return true if one or more constraint is violated
     */
    public boolean violated(final Element root,
                            final ErrorReporter errorReporter) {
        // Use an array to allow the result to be modified by the visitor
        final boolean[] result = new boolean[1];

        Visitor visitor = new SimpleVisitor() {
            // javadoc inherited
            public Visitor.Action visit(Element element) {
                Object elementConstraints = constraints.get(element.getName());
                Visitor.Action action = Visitor.Action.CONTINUE;

                if (elementConstraints != null) {
                    // The constraints should be an array of constraints or
                    // a single constraint
                    if (elementConstraints instanceof DOMConstraint[]) {
                        DOMConstraint[] constraints =
                            (DOMConstraint[])elementConstraints;

                        for (int i = 0; i < constraints.length; i++) {
                            // Update the result as needed
                            result[0] |=
                                constraints[i].violated(element,
                                                        errorReporter);
                        }
                    } else if (elementConstraints instanceof DOMConstraint) {
                        // Update the result as needed
                        result[0] |= ((DOMConstraint)elementConstraints).
                            violated(element,
                                     errorReporter);
                    } else {
                        throw new IllegalStateException(
                            "An unknown type of entry exists in the " + //$NON-NLS-1$
                            "constraints map, keyed on " + element.getName() + //$NON-NLS-1$
                            " (type is " + //$NON-NLS-1$
                            elementConstraints.getClass().getName());
                    }
                }

                if (stopOnFirstViolation && result[0]) {
                    action = Visitor.Action.STOP;
                }

                return action;
            }
        };

        // Walk the DOM structure from root down, visiting all elements and
        // executing the associated constraints
        new Walker().visit(root, visitor);

        // Return the violation status
        return result[0];
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Apr-05	7572/1	geoff	VBM:2005040612 Device repository merge report: create event model and XML report listeners

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 15-Jan-04	2583/1	philws	VBM:2003121512 Add layout constraints

 ===========================================================================
*/

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
package com.volantis.mcs.dom2theme.impl.model;

import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.PseudoElement;
import com.volantis.styling.PseudoStyleEntity;
import com.volantis.styling.PseudoStyleEntityIteratee;
import com.volantis.styling.PseudoStyleEntityVisitor;
import com.volantis.styling.StatefulPseudoClassSet;
import com.volantis.synergetics.ObjectHelper;

/**
 * The list of pseudo styleable entities which may be used as a critera to
 * select a particular set of style properties within an {@link OutputStyles}.
 *
 * <p>This may be thought of as a path through the naturally hierarchically
 * structured styles.</p>
 *
 * <p>An empty style path is used to select the style properties associated
 * with the entire styleable entity (element).</p>
 *
 * <p>The path can be of the form
 * <code>(&lt;pseudo-element&gt;)* &lt;pseudo-class-set&gt;?</code> i.e. an
 * optional list of pseudo elements followed by an optional set of pseudo
 * classes.</p>
 *
 * @mock.generate
 */
public class PseudoStylePath {

    /**
     * The empty path.
     */
    public static final PseudoStylePath EMPTY_PATH = new PseudoStylePath();

    /**
     * The pseudo elements.
     */
    private final PseudoElement[] pseudoElements;

    /**
     * The set of pseudo classes.
     */
    private StatefulPseudoClassSet pseudoClassSet;

    /**
     * Initialise.
     */
    PseudoStylePath() {
        this.pseudoElements = new PseudoElement[0];
    }

    /**
     * Initialise.
     *
     * @param pseudoElements The pseudo elements, may be null.
     * @param pseudoClassSet The set of pseudo classes, may be null.
     */
    private PseudoStylePath(
            PseudoElement[] pseudoElements,
            StatefulPseudoClassSet pseudoClassSet) {
        this.pseudoElements = pseudoElements;
        this.pseudoClassSet = pseudoClassSet;
    }

    /**
     * Add a pseudo element to the path.
     *
     * @param pseudoElement The pseudo element to add.
     * @return The new path that contains the elements from this path plus the
     *         newly added one.
     * @throws IllegalStateException If this contains a set of pseudo classes.
     */
    public PseudoStylePath addPseudoElement(PseudoElement pseudoElement) {
        if (containsPseudoClass()) {
            throw new IllegalArgumentException("Cannot add " + pseudoClassSet +
                    " to " + this + " as it already has a pseudo class");
        }

        int length = pseudoElements.length;
        PseudoElement[] newPseudoElements = new PseudoElement[length + 1];
        if (length > 0) {
            System.arraycopy(pseudoElements, 0, newPseudoElements, 0, length);
        }
        newPseudoElements[length] = pseudoElement;
        return new PseudoStylePath(newPseudoElements, null);
    }

    /**
     * Add a set of pseudo classes to the path.
     *
     * @param pseudoClassSet The set of pseudo classes to add.
     * @return The new path that contains the elements from this path plus the
     *         newly added set of pseudo classes.
     * @throws IllegalStateException If this already contains a set of pseudo
     *                               classes.
     */
    public PseudoStylePath addPseudoClassSet(
            StatefulPseudoClassSet pseudoClassSet) {
        if (containsPseudoClass()) {
            throw new IllegalArgumentException("Cannot add " + pseudoClassSet +
                    " to " + this + " as it already has a pseudo class");
        }

        return new PseudoStylePath(pseudoElements, pseudoClassSet);
    }

    /**
     * Iterate over the {@link PseudoStyleEntity}s within the path, from
     * the first to the last.
     *
     * @param pseudoStyleEntityIteratee the object called for each pseudo style
     *                                  entity in the path.
     */
    public void iterate(PseudoStyleEntityIteratee pseudoStyleEntityIteratee) {

        IterationAction action = IterationAction.CONTINUE;

        for (int i = 0; action == IterationAction.CONTINUE
                && i < pseudoElements.length; i++) {
            PseudoStyleEntity pse = pseudoElements[i];
            action = pseudoStyleEntityIteratee.next(pse);
        }

        if (action == IterationAction.CONTINUE && pseudoClassSet != null) {
            pseudoStyleEntityIteratee.next(pseudoClassSet);
        }
    }

    /**
     * Accept a visitor into the path to visit all the pseudo style entities
     * within the path, from the first to the last.
     *
     * @param pseudoStyleEntityVisitor
     */
    public void accept(PseudoStyleEntityVisitor pseudoStyleEntityVisitor) {

        for (int i = 0; i < pseudoElements.length; i++) {
            PseudoStyleEntity pse = pseudoElements[i];
            pse.accept(pseudoStyleEntityVisitor);
        }
        if (pseudoClassSet != null) {
            pseudoClassSet.accept(pseudoStyleEntityVisitor);
        }
    }

    /**
     * Check if this path is empty, returning true if so and false if not.
     * <p>
     * <strong>Note</strong> a path which is empty represents the path to the
     * properties associated with the entire stylable entity (element).
     *
     * @return true if the path is empty, otherwise false.
     */
    public boolean isEmpty() {
        return pseudoElements.length == 0 && pseudoClassSet == null;
    }

    /**
     * Check to see whether this contains a pseudo class.
     *
     * @return True if this contains a pseudo class, false otherwise.
     */
    public boolean containsPseudoClass() {
        return pseudoClassSet != null;
    }

    // Javadoc inherited.
    public boolean equals(Object obj) {

        if (obj instanceof PseudoStylePath) {
            PseudoStylePath other = (PseudoStylePath) obj;
            return ObjectHelper.equals(pseudoElements, other.pseudoElements) &&
                    ObjectHelper.equals(pseudoClassSet, other.pseudoClassSet);
        }
        return false;
    }

    // Javadoc inherited.
    public int hashCode() {
        int result = ObjectHelper.hashCode(pseudoElements);
        result = result * 37 + ObjectHelper.hashCode(pseudoClassSet);
        return result;
    }

    // Javadoc inherited.
    public String toString() {

        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        for (int i = 0; i < pseudoElements.length; i++) {
            PseudoStyleEntity pseudoStyleEntity = pseudoElements[i];
            buffer.append(pseudoStyleEntity);
        }
        if (pseudoClassSet != null) {
            buffer.append(pseudoClassSet);
        }
        buffer.append("]");
        return buffer.toString();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/6	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (5)

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Made session context create its contents lazily and optimised PseudoStylePath

 29-Nov-05	10370/2	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0

 18-Nov-05	10370/1	geoff	VBM:2005111405 MCS stability. Requesting pages over a 48 hour period lead to Errors

 18-Jul-05	8668/11	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/

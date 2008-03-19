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
package com.volantis.mcs.dom2theme.impl.generator.rule.type;

import com.volantis.mcs.dom2theme.impl.model.PseudoStylePath;
import com.volantis.styling.PseudoElement;
import com.volantis.styling.StatefulPseudoClass;
import com.volantis.styling.StatefulPseudoClassIteratee;
import com.volantis.styling.StatefulPseudoClassSet;

/**
 *
 * @mock.generate
 */
public class TypeSelectorSequence {

    /**
     * The type selector, if any. If null, indicates that this selector
     * sequence matches any element.
     */
    private String type;

    /**
     * The list of the pseudo style entities in the sequence.
     */
    private PseudoStylePath pseudoPath = PseudoStylePath.EMPTY_PATH;

    /**
     * The number of selectors in this sequence.
     */
    private int selectorCount;

    /**
     * The specificity calculated from the selectors in this sequence.
     */
    private int specificity;

    /**
     * Initialise.
     */
    public TypeSelectorSequence() {
    }

    /**
     * Returns the type selector for this sequence. May be null which indicates
     * that this sequence matches any element.
     *
     * @return the type selector, or null.
     */
    public String getType() {

        return type;
    }

    /**
     * Set the type selector for this sequence.
     * <p>
     * This method may only be called once.
     *
     * @param type the type selector for this sequence, may not be null.
     */
    public void setType(String type) {

        if (type == null && this.type == null) {
            // no change
        } else if (type != null && this.type == null) {
            // set a new type
            selectorCount ++;
            specificity += 1;
            this.type = type;
        } else if (type == null && this.type != null) {
            // clear an existing type.
            selectorCount --;
            specificity -= 1;

        }

        if (type != null && this.type == null) {
        }
    }

    public void addPseudoElement(PseudoElement pseudoElement) {

        pseudoPath = pseudoPath.addPseudoElement(pseudoElement);

        selectorCount ++;
        specificity += 1;
    }

    public void addPseudoClassSet(StatefulPseudoClassSet pseudoClassSet) {
        pseudoPath = pseudoPath.addPseudoClassSet(pseudoClassSet);

        selectorCount++;
        pseudoClassSet.iterate(new StatefulPseudoClassIteratee() {
            public void next(StatefulPseudoClass statefulPseudoClass) {
                specificity += 10;
            }
        });
    }

    public PseudoStylePath getPath() {

        return pseudoPath;
    }

    public boolean isComposite() {

        return selectorCount > 1;
    }

    public int getSpecificity() {

        return specificity;
    }

    public String toString() {
        return "[" +
                (type != null ? "" + type + "," : "") +
                (!pseudoPath.isEmpty() ? pseudoPath + "," : "") +
//                "" + componentCount + "," +
                "" + specificity + "]";
    }

    public boolean equals(Object obj) {

        if (obj instanceof TypeSelectorSequence) {
            TypeSelectorSequence other = (TypeSelectorSequence) obj;
            return
                    (type != null ?
                            type.equals(other.type) :
                            other.type == null) &&
                    (pseudoPath != null ?
                            pseudoPath.equals(other.pseudoPath) :
                            other.pseudoPath == null);
        } else {
            return false;
        }
    }

    public int hashCode() {

        int result = 17;
        result = 37 * result + (type != null ?
                type.hashCode() : 0);
        result = 37 * result + (pseudoPath != null ?
                pseudoPath.hashCode() : 0);
        return result;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Made session context create its contents lazily and optimised PseudoStylePath

 18-Jul-05	8668/8	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/

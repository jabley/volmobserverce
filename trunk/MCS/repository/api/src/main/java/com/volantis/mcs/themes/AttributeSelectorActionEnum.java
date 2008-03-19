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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.themes;

import com.volantis.mcs.themes.constraints.Constraint;
import com.volantis.mcs.themes.constraints.Contains;
import com.volantis.mcs.themes.constraints.ContainsWord;
import com.volantis.mcs.themes.constraints.Set;
import com.volantis.mcs.themes.constraints.EndsWith;
import com.volantis.mcs.themes.constraints.Equals;
import com.volantis.mcs.themes.constraints.StartsWith;
import com.volantis.mcs.themes.constraints.MatchesLanguage;
import com.volantis.mcs.themes.constraints.ConstraintFactory;

import java.util.Map;
import java.util.HashMap;

/**
 * Typesafe enumeration for attribute selector actions.
 */
public class AttributeSelectorActionEnum {

    /**
     * A map associating attribute selector actions with their description.
     */
    private static final Map constraintMappings = new HashMap();

    /**
     * A map associating attribute selector actions with their constraint
     * classes.
     */
    private static final Map enumMappings = new HashMap();

    /**
     * A simple description of the action.
     */
    private String description;

    /**
     * Action for checking whether an attribute is set.
     */
    public static final AttributeSelectorActionEnum SET =
            new AttributeSelectorActionEnum("set");

    /**
     * Action for checking whether an attribute is equal to a specified value.
     */
    public static final AttributeSelectorActionEnum EQUALS =
            new AttributeSelectorActionEnum("equals");

    /**
     * Action for checking whether an attribute contains a given word.
     */
    public static final AttributeSelectorActionEnum CONTAINS_WORD =
            new AttributeSelectorActionEnum("contains-word");

    /**
     * Action for checking whether an attribute is a hyphen separated list of
     * words beginning with the specified value.
     */
    public static final AttributeSelectorActionEnum LANGUAGE_MATCH =
            new AttributeSelectorActionEnum("language-match");

    /**
     * Action for checking whether an attribute starts with a specified value.
     */
    public static final AttributeSelectorActionEnum STARTS_WITH =
            new AttributeSelectorActionEnum("starts-with");

    /**
     * Action for checking whether an attribute ends with a specified value.
     */
    public static final AttributeSelectorActionEnum ENDS_WITH =
            new AttributeSelectorActionEnum("ends-with");

    /**
     * Action for checking whether an attribute contains a specified value.
     */
    public static final AttributeSelectorActionEnum CONTAINS =
            new AttributeSelectorActionEnum("contains");

    private static final ConstraintFactory CONSTRAINT_FACTORY =
        ConstraintFactory.getDefaultInstance();

    static {

        constraintMappings.put(Contains.class, CONTAINS);
        constraintMappings.put(ContainsWord.class, CONTAINS_WORD);
        constraintMappings.put(EndsWith.class, ENDS_WITH);
        constraintMappings.put(Equals.class, EQUALS);
        constraintMappings.put(MatchesLanguage.class, LANGUAGE_MATCH);
        constraintMappings.put(Set.class, SET);
        constraintMappings.put(StartsWith.class, STARTS_WITH);

        enumMappings.put(CONTAINS, new Worker(){
                protected Constraint createConstraint() {
                    return CONSTRAINT_FACTORY.createContains();
                }
            });
        enumMappings.put(CONTAINS_WORD, new Worker(){
                protected Constraint createConstraint() {
                    return CONSTRAINT_FACTORY.createContainsWord();
                }
            });
        enumMappings.put(ENDS_WITH, new Worker(){
                protected Constraint createConstraint() {
                    return CONSTRAINT_FACTORY.createEndsWith();
                }
            });
        enumMappings.put(EQUALS, new Worker(){
                protected Constraint createConstraint() {
                    return CONSTRAINT_FACTORY.createEquals();
                }
            });
        enumMappings.put(LANGUAGE_MATCH, new Worker(){
                protected Constraint createConstraint() {
                    return CONSTRAINT_FACTORY.createMatchesLanguage();
                }
            });
        enumMappings.put(SET, new Worker(){
                protected Constraint createConstraint() {
                    return CONSTRAINT_FACTORY.createSet();
                }
            });
        enumMappings.put(STARTS_WITH, new Worker(){
                protected Constraint createConstraint() {
                    return CONSTRAINT_FACTORY.createStartsWith();
                }
            });
    }

    /**
     * Private constructor to prevent instantiation outside this class.
     */
    private AttributeSelectorActionEnum(String description) {
        this.description = description;
    }

    // Javadoc inherited
    public String toString() {
        return description;
    }

    // Javadoc inherited
    public boolean equals(Object o) {
        boolean equal = false;
        if (o != null && o.getClass() == AttributeSelectorActionEnum.class) {
            AttributeSelectorActionEnum other =
                    (AttributeSelectorActionEnum) o;
            equal = (this.description.equals(other.description));
        }
        return equal;
    }

    // Javadoc inherited
    public int hashCode() {
        int hash = description.hashCode();
        return hash;
    }

    /**
     * Retrieves an attribute selector action based on its string description.
     * If there is no match null is returned.
     *
     * @param constraint The constraint for which to retrieve the enum value
     * @return The corresponding {@link AttributeSelectorActionEnum}
     */
    public static AttributeSelectorActionEnum getAttributeSelectorActionEnum(
            Constraint constraint) {
        AttributeSelectorActionEnum result =
                (AttributeSelectorActionEnum) constraintMappings.get(constraint);
        return result;
    }

    /**
     * Create the appropriate constraint for the given attribute selector action.
     *
     * @param action    for which to create a Constraint
     * @return Constraint which maps to the given attribute selector action
     * @throws IllegalAccessException if there was a problem creating the
     * Constraint
     * @throws InstantiationException if there was a problem creating the
     * Constraint
     */
    public static Constraint createConstraint(AttributeSelectorActionEnum action)
            throws IllegalAccessException, InstantiationException {
        return ((Worker)enumMappings.get(action)).createConstraint();
    }

    protected static abstract class Worker {
        protected abstract Constraint createConstraint();
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 05-Sep-05	9407/2	pduffin	VBM:2005083007 Removed old themes model

 21-Jul-05	8713/3	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/

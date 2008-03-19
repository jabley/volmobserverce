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

package com.volantis.styling;

/**
 * Implementation of {@link StatefulPseudoClassSet}.
 */
public class StatefulPseudoClassSetImpl
        implements StatefulPseudoClassSet {

    /**
     * The css representation of the pseudo class combination.
     */
    private final String cssRepresentation;

    /**
     * The bit set representing the combination of pseudo classes.
     */
    private final int combinationSet;

    /**
     * The bit set representing the pseudo classes that cannot be combined with
     * this
     */
    private final int exclusionSet;
    
    /**
     * Indicates whether conflicts between pseudo classes should be checked.
     */ 
    private final boolean checkForConflicts;

    /**
     * Initialise.
     *
     * <p>This set does not attempt to detect conflicts between its
     * contents.</p>
     */
    public StatefulPseudoClassSetImpl() {
        cssRepresentation = "";
        combinationSet = 0;
        exclusionSet = 0;
        checkForConflicts = false;
    }

    /**
     * Initialise.
     *
     * @param cssRepresentation The CSS representation.
     * @param combinationSet    The combined set.
     * @param exclusionSet      The exclusion set.
     * @param checkForConflicts Indicates whether the set should check for
     *                          conflicts.
     */
    public StatefulPseudoClassSetImpl(String cssRepresentation,
                                      int combinationSet,
                                      int exclusionSet,
                                      boolean checkForConflicts) {
        this.cssRepresentation = cssRepresentation;
        this.combinationSet = combinationSet;
        this.exclusionSet = exclusionSet;
        this.checkForConflicts = checkForConflicts;
    }

    // Javadoc inherited.
    public String getCSSRepresentation() {
        return cssRepresentation;
    }

    // Javadoc inherited.
    public void accept(PseudoStyleEntityVisitor visitor) {
        visitor.visit(this);
    }

    // Javadoc inherited.
    public StatefulPseudoClassSet add(StatefulPseudoClass other) {
        return combine(other.getSet());
    }

    // Javadoc inherited.
    public StatefulPseudoClassSet combine(StatefulPseudoClassSet other) {
        StatefulPseudoClassSetImpl impl = (StatefulPseudoClassSetImpl) other;

        if (checkForConflicts) {
            // Check the exclusions, as exclusions are symmetric it is only
            // necessary to check one set of exclusions to detect a conflict.
            int conflictSet = exclusionSet & impl.combinationSet;
            if (conflictSet != 0) {
                // Calculate the symmetric conflict set in order to provide a
                // useful error message.
                int symmetricConflictSet = combinationSet & impl.exclusionSet;
                throw new IllegalArgumentException("Cannot combine " +
                        this.getCSSRepresentation() +
                        " and " +
                        other.getCSSRepresentation() +
                        " due to conflict between " +
                        generateCSSRepresentation(conflictSet) +
                        " and " +
                        generateCSSRepresentation(symmetricConflictSet));
            }
        }

        // Combine the bit sets into a new one, ignoring overlaps.
        int newCombinationSet = combinationSet | impl.combinationSet;

        // Combine the exclusion sets into a new one as well.
        int newExclusionSet = exclusionSet | impl.exclusionSet;

        if (newCombinationSet == combinationSet) {
            assertExclusionSetsMatch(newExclusionSet, newCombinationSet,
                    exclusionSet, combinationSet);
            return this;
        } else if (newCombinationSet == impl.combinationSet &&
                checkForConflicts == impl.checkForConflicts) {
            assertExclusionSetsMatch(newExclusionSet, newCombinationSet,
                    impl.exclusionSet, impl.combinationSet);
            return other;
        } else {
            String cssRepresentation = generateCSSRepresentation(
                    newCombinationSet);
            return new StatefulPseudoClassSetImpl(cssRepresentation,
                    newCombinationSet, newExclusionSet, checkForConflicts);
        }
    }

    // Javadoc inherited.
    public void iterate(StatefulPseudoClassIteratee iteratee) {
        StatefulPseudoClassImpl[] individualClasses =
                StatefulPseudoClassImpl.INDIVIDUAL_CLASSES;
        for (int i = 0; i < individualClasses.length; i += 1) {
            StatefulPseudoClassImpl individual = individualClasses[i];
            if ((combinationSet & individual.pseudoClassBitField) != 0) {
                iteratee.next(individual);
            }
        }
    }

    /**
     * Make sure that the two exclusion sets match.
     *
     * @param exclusionSet1   An exclusion set.
     * @param combinationSet1 A combination set.
     * @param exclusionSet2   Another exclusion set.
     * @param combinationSet2 Another combination set.
     */
    private void assertExclusionSetsMatch(
            int exclusionSet1, int combinationSet1, final int exclusionSet2,
            final int combinationSet2) {
        if (exclusionSet1 != exclusionSet2) {
            throw new IllegalStateException("Combination sets (" +
                    Integer.toString(combinationSet1, 2) + " and "
                    + Integer.toString(combinationSet2, 2) +
                    ") match but exclusion sets (" +
                    Integer.toString(exclusionSet1, 2) + " and "
                    + Integer.toString(exclusionSet2, 2) +
                    ") do not");
        }
    }

    // Javadoc inherited.
    public boolean isSubSetOf(StatefulPseudoClassSet other) {
        StatefulPseudoClassSetImpl set = (StatefulPseudoClassSetImpl) other;

        return ((combinationSet & set.combinationSet) == combinationSet);
    }


    /**
     * Generate the CSS representation for the specified set.
     *
     * @param combinationSet The set whose CSS representation is needed.
     *
     * @return The CSS representation.
     */
    private static String generateCSSRepresentation(int combinationSet) {
        StringBuffer buffer = new StringBuffer();
        StatefulPseudoClassImpl[] individualClasses =
                StatefulPseudoClassImpl.INDIVIDUAL_CLASSES;
        for (int i = 0; i < individualClasses.length; i += 1) {
            StatefulPseudoClassImpl individual = individualClasses[i];
            if ((combinationSet & individual.pseudoClassBitField) != 0) {
                buffer.append(individual.cssRepresentation);
            }
        }
        return buffer.toString();
    }

    // Javadoc inherited.
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o instanceof StatefulPseudoClassSetImpl) {
            StatefulPseudoClassSetImpl other = (StatefulPseudoClassSetImpl) o;

            if (combinationSet == other.combinationSet) {
                assertExclusionSetsMatch(exclusionSet, combinationSet,
                        other.exclusionSet, other.combinationSet);
                return true;
            }
        }

        return false;
    }

    // Javadoc inherited.
    public int hashCode() {
        return combinationSet;
    }

    // Javadoc inherited.
    public String toString() {
        return getCSSRepresentation();
    }
}

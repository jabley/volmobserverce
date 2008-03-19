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

package com.volantis.styling;



/**
 * The implementation of {@link StatefulPseudoClass}.
 *
 * <p>A single pseudo class has a bit field indicating which pseudo class it
 * represents.</p>
 */
public class StatefulPseudoClassImpl
        implements StatefulPseudoClass {

    /**
     * The bit representing :link.
     */
    private static final int LINK_BIT = 1 << 0;

    /**
     * The bit representing :hover.
     */
    private static final int HOVER_BIT = 1 << 1;

    /**
     * The bit representing :focus.
     */
    private static final int FOCUS_BIT = 1 << 2;

    /**
     * The bit representing :active.
     */
    private static final int ACTIVE_BIT = 1 << 3;

    /**
     * The bit representing :visited.
     */
    private static final int VISITED_BIT = 1 << 4;

    /**
     * The bit representing :mcs-concealed.
     */
    private static final int MCS_CONCEALED_BIT = 1 << 5;

    /**
     * The bit representing :mcs-unfolded.
     */
    private static final int MCS_UNFOLDED_BIT = 1 << 6;

    /**
     * The bit representing :mcs-invalid.
     */
    private static final int MCS_INVALID_BIT = 1 << 7;

    /**
     * The bit representing :mcs-normal.
     */
    private static final int MCS_NORMAL_BIT = 1 << 8;

    /**
     * The bit representing :mcs-busy.
     */
    private static final int MCS_BUSY_BIT = 1 << 9;

    /**
     * The bit representing :mcs-failed.
     */
    private static final int MCS_FAILED_BIT = 1 << 10;

    /**
     * The bit representing :mcs-invalid.
     */
    private static final int MCS_SUSPENDED_BIT = 1 << 11;

    /**
     * The bit representing :mcs-disabled.
     */
    private static final int MCS_DISABLED_BIT = 1 << 12;
    
    /**
     * An empty set of pseudo classes.
     */
    private static final int EMPTY_EXCLUSION_SET = 0;

    /**
     * An full set of pseudo classes.
     */
    private static final int FULL_EXCLUSION_SET = 
        LINK_BIT |
        HOVER_BIT |
        FOCUS_BIT | 
        ACTIVE_BIT | 
        VISITED_BIT | 
        MCS_CONCEALED_BIT |
        MCS_UNFOLDED_BIT |
        MCS_INVALID_BIT |
        MCS_NORMAL_BIT | 
        MCS_BUSY_BIT |
        MCS_FAILED_BIT |
        MCS_SUSPENDED_BIT |
        MCS_DISABLED_BIT;
    
    /**
     * The set of pseudo classes that cannot be combined with :link.
     */
    private static final int LINK_EXCLUSION_SET = VISITED_BIT;

    /**
     * The set of pseudo classes that cannot be combined with :visited.
     */
    private static final int VISITED_EXCLUSION_SET = LINK_BIT;

    /**
     * The set of pseudo classes that cannot be combined with :mcs-concealed.
     */
    private static final int MCS_CONCEALED_EXCLUSION_SET = FULL_EXCLUSION_SET ^ MCS_CONCEALED_BIT;

    /**
     * The set of pseudo classes that cannot be combined with :mcs-normal.
     */
    private static final int MCS_NORMAL_EXCLUSION_SET = MCS_BUSY_BIT | MCS_FAILED_BIT | MCS_SUSPENDED_BIT;

    /**
     * The set of pseudo classes that cannot be combined with :mcs-busy.
     */
    private static final int MCS_BUSY_EXCLUSION_SET = MCS_NORMAL_BIT | MCS_FAILED_BIT | MCS_SUSPENDED_BIT;

    /**
     * The set of pseudo classes that cannot be combined with :mcs-failed.
     */
    private static final int MCS_FAILED_EXCLUSION_SET = MCS_NORMAL_BIT | MCS_BUSY_BIT | MCS_SUSPENDED_BIT;

    /**
     * The set of pseudo classes that cannot be combined with :mcs-suspended.
     */
    private static final int MCS_SUSPENDED_EXCLUSION_SET = MCS_NORMAL_BIT | MCS_BUSY_BIT | MCS_FAILED_BIT;
    
    /**
     * Represents the <code>:link</code> pseudo class.
     */
    public static final StatefulPseudoClassImpl LINK =
            new StatefulPseudoClassImpl(
                    ":link", LINK_BIT, LINK_EXCLUSION_SET);

    /**
     * Represents the <code>:hover</code> pseudo class.
     */
    public static final StatefulPseudoClassImpl HOVER =
            new StatefulPseudoClassImpl(
                    ":hover", HOVER_BIT, EMPTY_EXCLUSION_SET);

    /**
     * Represents the <code>:focus</code> pseudo class.
     */
    public static final StatefulPseudoClassImpl FOCUS =
            new StatefulPseudoClassImpl(
                    ":focus", FOCUS_BIT, EMPTY_EXCLUSION_SET);

    /**
     * Represents the <code>:active</code> pseudo class.
     */
    public static final StatefulPseudoClassImpl ACTIVE =
            new StatefulPseudoClassImpl(
                    ":active", ACTIVE_BIT, EMPTY_EXCLUSION_SET);

    /**
     * Represents the <code>:visited</code> pseudo class.
     */
    public static final StatefulPseudoClassImpl VISITED =
            new StatefulPseudoClassImpl(
                    ":visited", VISITED_BIT, VISITED_EXCLUSION_SET);

    /**
     * Represents the <code>:mcs-concealed</code> pseudo class.
     */
    public static final StatefulPseudoClassImpl MCS_CONCEALED =
            new StatefulPseudoClassImpl(
                    ":mcs-concealed", MCS_CONCEALED_BIT, MCS_CONCEALED_EXCLUSION_SET);

    /**
     * Represents the <code>:mcs-unfolded</code> pseudo class.
     */
    public static final StatefulPseudoClassImpl MCS_UNFOLDED =
            new StatefulPseudoClassImpl(
                    ":mcs-unfolded", MCS_UNFOLDED_BIT, EMPTY_EXCLUSION_SET);

    /**
     * Represents the <code>:mcs-invalid</code> pseudo class.
     */
    public static final StatefulPseudoClassImpl MCS_INVALID =
            new StatefulPseudoClassImpl(
                    ":mcs-invalid", MCS_INVALID_BIT, EMPTY_EXCLUSION_SET);

    /**
     * Represents the <code>:mcs-normal</code> pseudo class.
     */
    public static final StatefulPseudoClassImpl MCS_NORMAL =
            new StatefulPseudoClassImpl(
                    ":mcs-normal", MCS_NORMAL_BIT, MCS_NORMAL_EXCLUSION_SET);

    /**
     * Represents the <code>:mcs-busy</code> pseudo class.
     */
    public static final StatefulPseudoClassImpl MCS_BUSY =
            new StatefulPseudoClassImpl(
                    ":mcs-busy", MCS_BUSY_BIT, MCS_BUSY_EXCLUSION_SET);

    /**
     * Represents the <code>:mcs-failed</code> pseudo class.
     */
    public static final StatefulPseudoClassImpl MCS_FAILED =
            new StatefulPseudoClassImpl(
                    ":mcs-failed", MCS_FAILED_BIT, MCS_FAILED_EXCLUSION_SET);

    /**
     * Represents the <code>:mcs-suspended</code> pseudo class.
     */
    public static final StatefulPseudoClassImpl MCS_SUSPENDED =
            new StatefulPseudoClassImpl(
                    ":mcs-suspended", MCS_SUSPENDED_BIT, MCS_SUSPENDED_EXCLUSION_SET);

    /**
     * Represents the <code>:mcs-disabled</code> pseudo class.
     */
    public static final StatefulPseudoClassImpl MCS_DISABLED =
            new StatefulPseudoClassImpl(
                    ":mcs-disabled", MCS_DISABLED_BIT, EMPTY_EXCLUSION_SET);

    
    /**
     * The array of individual pseudo classes, used to iterate over when
     * creating CSS representations.
     */
    static final StatefulPseudoClassImpl[] INDIVIDUAL_CLASSES =
            new StatefulPseudoClassImpl[]{
                LINK,
                HOVER,
                FOCUS,
                ACTIVE,
                VISITED,
                MCS_CONCEALED,
                MCS_UNFOLDED,
                MCS_INVALID,
                MCS_NORMAL,
                MCS_BUSY,
                MCS_FAILED,
                MCS_SUSPENDED,
                MCS_DISABLED
            };

    /**
     * The pseudo class set.
     */
    private final StatefulPseudoClassSet pseudoClassSet;

    /**
     * The css representation of the pseudo class.
     */
    final String cssRepresentation;

    /**
     * The bit set indicating the pseudo class.
     */
    final int pseudoClassBitField;

    /**
     * Initialise.
     *
     * @param cssRepresentation The CSS representation of the combination.
     * @param combinationSet The set of bits that represent the combination.
     * @param exclusionSet The set of bits that cannot be combined with this
     * combination.
     */
    private StatefulPseudoClassImpl(
            String cssRepresentation, int combinationSet, int exclusionSet) {

        this.cssRepresentation = cssRepresentation;
        this.pseudoClassBitField = combinationSet;
        pseudoClassSet = new StatefulPseudoClassSetImpl(cssRepresentation,
                combinationSet, exclusionSet, true);
    }

    public StatefulPseudoClassSet getSet() {
        return pseudoClassSet;
    }

    // Javadoc inherited.
    public String getCSSRepresentation() {
        return cssRepresentation;
    }

    // Javadoc inherited.
    public String toString() {
        return getCSSRepresentation();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Jul-05	9110/2	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 18-Jul-05	9029/3	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/

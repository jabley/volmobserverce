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

import com.volantis.styling.values.MutablePropertyValues;

import java.util.Iterator;

/**
 * The set of styles that are associated with a specific stylable entity.
 *
 * <p>This encapsulates both the properties associated directly with the
 * stylable entity and those associated with any nested stylable entities.</p>
 *
 * <p>The order of the nested pseudo class associated property values is
 * important as it is determined by the cascade.</p>
 *
 * @mock.generate
 */
public interface Styles {

    /**
     * Get the property values associated directly with the owning
     * {@link PseudoStyleEntity}.
     *
     * @return The {@link MutablePropertyValues} associated with the entity,
     *      will never be null.
     */
    MutablePropertyValues getPropertyValues();

    /**
     * Find the property values associated directly with the owning
     * {@link PseudoStyleEntity}.
     *
     * @return The {@link MutablePropertyValues} associated with the entity,
     *      will be null if no properties have been added to it.
     */
    MutablePropertyValues findPropertyValues();

    /**
     * Find the styles associated with a nested stylable entity.
     *
     * @param entity The nested entity, may not be null.
     *
     * @return The {@link Styles} associated with the entity, or null if there
     * is no nested entity.
     */
    NestedStyles findNestedStyles(PseudoStyleEntity entity);

    /**
     * Find the styles associated with the individual pseudo class.
     *
     * @param pseudoClass The nested pseudo class, may not be null.
     *
     * @return The styles associated with the nested pseudo class, or null
     * if no such class exists.
     * @deprecated Use {@link #findNestedStyles(PseudoStyleEntity)}
     */
    NestedStyles findNestedStyles(StatefulPseudoClass pseudoClass);

    /**
     * Get the styles associated with a nested stylable entity.
     *
     * <p>If no such nested styles exist then one is created and associated
     * with the stylable entity.</p>
     *
     * @param entity The nested entity, may not be null.
     *
     * @return The {@link Styles} associated with the entity, will never be
     *      null.
     */
    NestedStyles getNestedStyles(PseudoStyleEntity entity);

    /**
     * Get the styles associated with the individual pseudo class.
     *
     * <p>If no such nested styles exist then one is created and associated
     * with the pseudo class.</p>
     *
     * @param pseudoClass The nested pseudo class, may not be null.
     * @return The {@link Styles} associated with the pseudo class, will never
     *         be null.
     * @deprecated Use {@link #getNestedStyles(PseudoStyleEntity)}
     */
    NestedStyles getNestedStyles(StatefulPseudoClass pseudoClass);

    /**
     * Remove the styles associated with the specified nested stylable entity.
     *
     * @param entity The entity whose styles is required.
     *
     * @return The styles associated with the nested stylable entity, or null
     * if no such entity exists.
     */
    NestedStyles removeNestedStyles(PseudoStyleEntity entity);

    /**
     * Remove the styles associated with the individual pseudo class.
     *
     * @param pseudoClass The pseudo class whose styles is required.
     *
     * @return The styles associated with the nested pseudo class, or null
     * if no such class exists.
     *
     * @deprecated Use {@link #removeNestedStyles(PseudoStyleEntity)}
     */
    NestedStyles removeNestedStyles(StatefulPseudoClass pseudoClass);

    /**
     * Get an external iterator over the contained sequence of nested
     * {@link Styles}' {@link PseudoStyleEntity}s.
     *
     * @return An iterator over a sequence of {@link Styles}.
     */
    Iterator iterator();

    /**
     * Iterate over the contained sequence of {@link NestedStyles}.
     *
     * @param iteratee will be called back for each {@link NestedStyles}'s.
     */
    void iterate(NestedStylesIteratee iteratee);

    /**
     * Return a copy of these Styles.
     *
     * @return Styles which are a copy of this object.
     */
    Styles copy();

    /**
     * Indicates whether this has any nested styles.
     *
     * @return True if it has nested styles and false otherwise.
     */
    boolean hasNestedStyles();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (3)

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Removed some unnecessary usages of setSpecifiedValue

 11-Nov-05	10253/1	emma	VBM:2005110902 Fixing two layout rendering bugs

 11-Nov-05	10282/1	emma	VBM:2005110902 Forward port: fixing two layout rendering bugs

 11-Nov-05	10253/1	emma	VBM:2005110902 Fixing two layout rendering bugs

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 08-Jun-05	7997/3	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 02-Jun-05	7997/1	pduffin	VBM:2005050324 Added styling API

 ===========================================================================
*/

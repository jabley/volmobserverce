package com.volantis.styling.impl.engine.sheet;

import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.impl.engine.selectionstates.SelectionState;
import com.volantis.styling.impl.sheet.StylerIteratee;

import java.util.BitSet;
import java.util.List;

/**
 * A Styler list that supports indexing by element and class.
 *
 * <p>An indexable styler list can be used to build up a map associating its
 * stylers with specific element and class values so that only stylers that
 * could match a particular element/class are checked.</p>
 *
 * <p>In order to use the indexing feature of an IndexableStylerList, the
 * {@link #lookupElement} and {@link #indexClass} methods can be used to
 * build up a list of stylers that can match those element/class values.</p>
 *
 * <p>Finally a call to {@link #indexedList} can be used to retrieve a List of
 * styles that could match the specified element/class values.</p>
 *
 * @mock.generate base="StylerList"
 */
public interface IndexableStylerList extends StylerList {
    /**
     * Looks up the stylers that require a specified element.
     *
     * @param indexRef The index reference for this lookup. Can either be the
     *                 result of a previous lookup operation or a clean index
     *                 reference created with {@link #createIndexRef}. Will be
     *                 modified by this call to include all stylers that can
     *                 accept this element.
     * @param element The element based on which the lookup should take place
     */
    public void lookupElement(BitSet indexRef, String element);

    /**
     * Looks up the stylers that require a specified class.
     *
     * @param indexRef The index reference for this lookup. Can either be the
     *                 result of a previous lookup operation or a clean index
     *                 reference created with {@link #createIndexRef}. Will be
     *                 modified by this call to include all stylers that can
     *                 accept this class.
     * @param className The class based on which the lookup should take place
     */
    public void lookupClass(BitSet indexRef, String className);

    /**
     * Looks up the stylers that require a specified state.
     *
     * @param indexRef The index reference for this lookup. Can either be the
     *                 result of a previous lookup operation or a clean index
     *                 reference created with {@link #createIndexRef}. Will be
     *                 modified by this call to include all stylers that can
     *                 accept this class.
     * @param className The state based on which the lookup should take place
     */
    public void lookupState(BitSet indexRef, SelectionState state);

    /**
     * Returns an index reference for all selectors which require a specific
     * state to be set before triggering.
     *
     * @return An index reference for all state-dependent selectors
     */
    public BitSet stateRequired();

    /**
     * Retrieve a list containing only those stylers matched by the specified
     * index reference.
     *
     * @param indexRef The index reference for the list to return
     * @return A list of stylers matching the provided index
     */
    public List indexedList(BitSet indexRef);

    /**
     * Creates a default index reference containing all stylers that can be
     * matched regardless of class/element (this would include rules that only
     * match on non-class attributes, for example).
     *
     * @return An initialised index reference
     */
    public BitSet createIndexRef();

    /**
     * Iterate over the {@link com.volantis.styling.impl.sheet.Styler stylers}
     * within the indexed style sheet.
     *
     * @param iteratee The object that will be invoked for each
     * {@link com.volantis.styling.impl.sheet.Styler}
     * @param indexRef The index reference specifying which stylers to apply
     */
    public IterationAction iterateIndexed(StylerIteratee iteratee,
                                          BitSet indexRef);
}

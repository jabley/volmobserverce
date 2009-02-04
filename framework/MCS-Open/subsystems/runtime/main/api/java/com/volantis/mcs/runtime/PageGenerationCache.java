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
 * $Header: /src/voyager/com/volantis/mcs/runtime/PageGenerationCache.java,v 1.8 2003/03/20 15:15:33 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Feb-02    Paul            VBM:2002021203 - Created from
 *                              MarinerSessionContext.
 * 14-Feb-02    Steve           VBM:2001101803 - Added more caches for form
 *                              fragments. These are implemented as maps since
 *                              each form has its own fragmentation state.
 * 15-Feb-02    Mat             VBM:2002021203 - Changed
 *                              makeFragmentationSpecifier() to prepend a 'c'
 *                              rather than an 'f'
 * 19-Feb-02    Paul            VBM:2001100102 - Fixed error in comment.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.HashMap;
import java.util.Map;

/**
 * This class caches all the information which we need to use when generating
 * pages such as fragmentation states, fragmentation changes, ...
 *
 * @mock.generate
 */
public class PageGenerationCache {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(PageGenerationCache.class);

    private static final char SEPARATION_CHARACTER = '.';

    /**
     * This acts as a marker in the canonical fragmentation state map to indicate
     * that the specifier maps to the empty state.
     */
    private static final FragmentationState EMPTY_STATE
            = new FragmentationState();

    /**
     * The cache of FragmentationState objects.
     */
    private final IndexedObjectCache fragmentationStateCache;

    /**
     * The cache of FragmentationState.Change objects.
     */
    private final IndexedObjectCache fragmentationChangeCache;

    /**
     * A map of fragmentation state caches, one for each form
     */
    private final HashMap formFragmentationStateMap;

    /**
     * A map of fragmentation state change caches, one for each form
     */
    private final HashMap formFragmentationStateChangeMap;

    /**
     * A mapping from fragment specifiers to the canonical state.
     * <code>
     * e.g.
     * c1.f2 -> f8
     * c2.f4 -> f8
     * </code>
     */
    private final Map canonicalFragmentationStates;

    /**
     * Create a new <code>PageGenerationCache</code>.
     */
    public PageGenerationCache() {
        fragmentationStateCache = new IndexedObjectCache();
        fragmentationChangeCache = new IndexedObjectCache();
        canonicalFragmentationStates = new HashMap();
        formFragmentationStateMap = new HashMap();
        formFragmentationStateChangeMap = new HashMap();
    }

    /**
     * Get the index for the specified <code>FragmentationState</code>.
     * <p>
     * If the <code>FragmentationState</code> was not found then it is added and
     * a new index is returned.
     * </p>
     *
     * @param fragmentationState The <code>FragmentationState</code> whose index
     *                           is needed.
     * @return The index to use to find the specified
     *         <code>FragmentationState</code> or an equivalent one.
     */
    public int getFragmentationIndex(FragmentationState fragmentationState) {
        return fragmentationStateCache.getIndex(fragmentationState);
    }

    /**
     * Get the <code>FragmentationState</code> for a particular index.
     *
     * @param index The index to use to retrieve the
     *              <code>FragmentationState</code>.
     * @return The <code>FragmentationState</code>, or null if the index was
     *         not valid.
     */
    public FragmentationState getFragmentationState(int index) {
        return (FragmentationState) fragmentationStateCache.getObject(index);
    }

    /**
     * Get the index for a <code>FragmentationState.Change</code> object.
     * <p>
     * If the specified <code>FragmentationState.Change</code> matches one
     * in the list then return its index, otherwise add it to the list and
     * return the new index.
     * </p>
     *
     * @param change The <code>FragmentationState.Change</code> object whose
     *               index is required.
     */
    public int getFragmentationStateChangeIndex(
            FragmentationState.Change change) {
        return fragmentationChangeCache.getIndex(change);
    }

    /**
     * Get the <code>FragmentationState.Change</code> for a particular index.
     *
     * @param index The index to use to retrieve the
     *              <code>FragmentationState.Change</code>.
     * @return The <code>FragmentationState.Change</code>, or null if the
     *         index was not valid.
     */
    public FragmentationState.Change getFragmentationStateChange(int index) {
        return (FragmentationState.Change)
                fragmentationChangeCache.getObject(index);
    }

    /**
     * Process the specified string and return the canonical
     * <code>FragmentationState</code> object based on the specified state and
     * possible changes to it.
     *
     * @param specifier The String which encodes details as to what the
     *                  <code>FragmentationState</code> of this page should look like.
     * @return The created <code>FragmentationState</code> or null if the state
     *         was empty. The returned object will have had its cache index set.
     */
    public FragmentationState getFragmentationState(String specifier) {
        // The specifier is of the following format.
        // c<state index>
        // [c<state index>.](f<fragment change index>|s<shard change index>)

        int stateIndex = -1;
        int changeIndex = -1;

        String changeSpecifier;

        // Look in the map from specifiers to fragment state first as that should
        // be faster than parsing the string and creating the objects.
        FragmentationState state = getCanonicalFragmentationState(specifier);
        if (state == EMPTY_STATE) {
            return null;
        } else if (state != null) {
            return state;
        }

        String value;
        int index;

        // If the first character is a 'c' then the text between it and the first
        // . is the current state index.
        if (specifier.charAt(0) == 'c') {
            index = specifier.indexOf(SEPARATION_CHARACTER);
            if (index == -1) {
                value = specifier.substring(1);
                changeSpecifier = null;
            } else {
                value = specifier.substring(1, index);
                changeSpecifier = specifier.substring(index + 1);
            }

            try {
                stateIndex = Integer.parseInt(value);
            }
            catch (NumberFormatException nfe) {
                logger.warn("state-index", new Object[]{value},
                        nfe);
                return null;
            }
        } else {
            changeSpecifier = specifier;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("State index " + stateIndex);
        }

        if (changeSpecifier == null) {
            // There is no change so it is a canonical specifier.
            changeIndex = -1;
        } else {
            // If the first character is a 'f' then the rest of the text is the index
            // of the fragment change object.
            if (changeSpecifier.charAt(0) == 'f') {
                value = changeSpecifier.substring(1);
                try {
                    changeIndex = Integer.parseInt(value);
                }
                catch (NumberFormatException nfe) {
                    logger.warn("fragment-index", new Object[]{value},
                            nfe);
                    return null;
                }
            } else if (changeSpecifier.charAt(0) == 's') {
                // The first character is a 's' so the rest of the text is the index
                // of the fragment change object.
                value = changeSpecifier.substring(1);
                try {
                    changeIndex = Integer.parseInt(value);
                }
                catch (NumberFormatException nfe) {
                    logger.warn("shard-index", new Object[]{value},
                            nfe);
                    return null;
                }
            } else {
                // Otherwise the string is invalid.
                logger.warn("fragment-or-shard-change-missing",
                        new Object[]{specifier});
                return null;
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Change index " + changeIndex);
        }

        // Get the change object if any.
        FragmentationState.Change change;
        if (changeIndex == -1) {
            change = null;
        } else {
            change = getFragmentationStateChange(changeIndex);
            if (change == null) {
                logger.warn("change-for-index-missing",
                        new Object[]{new Integer(changeIndex)});
                return null;
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Change " + change);
            }
        }

        // Get the fragmentation state object if any.
        if (stateIndex == -1) {

            // If no change has been specified then return straight away to avoid
            // creating an object which will only be thrown away.
            if (change == null) {
                state = null;
            } else {
                state = new FragmentationState();
                if (logger.isDebugEnabled()) {
                    logger.debug("Created " + state);
                }
            }
        } else {
            FragmentationState current = getFragmentationState(stateIndex);
            if (current == null) {
                logger.warn("state-not-found");
                return null;
            }

            // If a change has been specified then clone the object before we
            // make any changes to it.
            if (change == null) {
                state = current;
            } else {
                state = current.cloneState();

                if (logger.isDebugEnabled()) {
                    logger.debug("Cloned " + current + " as " + state);
                }
            }
        }

        // Apply the change if any.
        if (change != null) {
            change.apply(state);

            // If after the state change the state is now empty then discard the
            // state as the default processing will do the right thing.
            if (state.isEmpty()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("State " + state + " is empty");
                }
                state = null;
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("State after applying change " + state);
        }

        if (state == null) {
            putCanonicalFragmentationState(specifier, EMPTY_STATE);
            return state;
        }

        // If we reach here then the specifier was not canonical so the
        // fragmentation state cache could contain a FragmentationState object
        // which is the same as this one. In order to prevent us from having
        // lots of objects which are the same we look this object up in the
        // cache and return the object.
        FragmentationState canonicalState;
        int canonicalStateIndex;
        canonicalStateIndex = getFragmentationIndex(state);
        canonicalState = getFragmentationState(canonicalStateIndex);

        if (logger.isDebugEnabled()) {
            logger.debug("Canonical state index is " + canonicalStateIndex);
            logger.debug("Canonical state is " + canonicalState);
        }

        if (canonicalState == state) {
            // The cache did not contain an equivalent fragmentation state so
            // this is now the canonical fragmentation state so we need to
            // initialise the index and the canonical specifier.
            String canonicalSpecifier
                    = makeFragmentationSpecifier(canonicalStateIndex);
            canonicalState.setCacheIndex(stateIndex);
            canonicalState.setCanonicalSpecifier(canonicalSpecifier);
        }

        putCanonicalFragmentationState(specifier, canonicalState);

        return canonicalState;
    }

    private FragmentationState getCanonicalFragmentationState(
            String specifier) {

        synchronized (canonicalFragmentationStates) {
            return (FragmentationState) canonicalFragmentationStates
                    .get(specifier);
        }
    }

    private void putCanonicalFragmentationState(
            String specifier,
            FragmentationState state) {

        synchronized (canonicalFragmentationStates) {
            canonicalFragmentationStates.put(specifier, state);
        }
    }

    /**
     * Get the string which is used to indicate a change in the current fragment
     * of an inclusion.
     *
     * @param stateIndex          The index of the <code>FragmentationState</code> object
     *                            in this session.
     * @param fragmentChangeIndex The index of the
     *                            <code>FragmentationState.FragmentChange</code> object in this session.
     * @return The string which encodes the state and the change to apply to that
     *         state.
     */
    public static String makeFragmentChangeSpecifier(
            int stateIndex,
            int fragmentChangeIndex) {

        StringBuffer requestValue = new StringBuffer();

        if (stateIndex != -1) {
            requestValue.append('c').append(stateIndex)
                    .append(SEPARATION_CHARACTER);
        }

        requestValue.append('f').append(fragmentChangeIndex);

        return requestValue.toString();
    }

    private static String makeFragmentationSpecifier(int stateIndex) {
        return "c" + stateIndex;
    }

    /**
     * Get the string which is used to indicate a change in the current shard
     * in an inclusion.
     *
     * @param stateIndex       The index of the <code>FragmentationState</code> object
     *                         in this session.
     * @param shardChangeIndex The index of the
     *                         <code>FragmentationState.ShardChange</code> object in this session.
     * @return The string which encodes the state and the change to apply to that
     *         state.
     */
    public static String makeShardChangeSpecifier(
            int stateIndex,
            int shardChangeIndex) {

        StringBuffer requestValue = new StringBuffer();

        if (stateIndex != -1) {
            requestValue.append('c').append(stateIndex)
                    .append(SEPARATION_CHARACTER);
        }

        requestValue.append('s').append(shardChangeIndex);

        return requestValue.toString();
    }

    /**
     * Create new state caches for a given form
     *
     * @param formName The name of the form we are creating a cache for.
     */
    public void createFormFragmentationStates(String formName) {
        if (formFragmentationStateMap.containsKey(formName)) {
            if (logger.isDebugEnabled()) {
                logger.debug(
                        "Form fragmentation states already exist for form " +
                                formName);
            }
        } else {
            formFragmentationStateMap.put(formName, new IndexedObjectCache());
            formFragmentationStateChangeMap
                    .put(formName, new IndexedObjectCache());
            if (logger.isDebugEnabled()) {
                logger.debug("Created state caches for form " + formName);
            }
        }
    }

    /**
     * Get an indexed fragmentation state for a given form.
     *
     * @param formName The name of the form
     * @param index    The fragmentation state index
     * @return FragmentationState
     */
    public FragmentationState getFormFragmentationState(
            String formName, int index) {
        synchronized (formFragmentationStateMap) {
            if (logger.isDebugEnabled()) {
                logger.debug("Getting form fragmentation state. Form " +
                        formName + " index " + index);
            }
            IndexedObjectCache cache =
                    (IndexedObjectCache) formFragmentationStateMap
                            .get(formName);
            FragmentationState state =
                    (FragmentationState) cache.getObject(index);
            if (logger.isDebugEnabled()) {
                logger.debug("Returned state is " + state);
            }
            return state;
        }
    }

    /**
     * Get the index of a fragmentation state within a form. If the state does not exist for the
     * form it is added ( by the cache ).
     *
     * @param formName The name of the form in question
     * @param state    The FragmentationState we want the index for.
     * @return The index of the state.
     */
    public int getFormFragmentationIndex(
            String formName, FragmentationState state) {
        synchronized (formFragmentationStateMap) {
            if (logger.isDebugEnabled()) {
                logger.debug("Getting index of state " + state + " from form " +
                        formName);
            }
            IndexedObjectCache cache =
                    (IndexedObjectCache) formFragmentationStateMap
                            .get(formName);
            int idx = cache.getIndex(state);
            if (logger.isDebugEnabled()) {
                logger.debug("Returned index is " + idx);
            }
            return idx;
        }
    }

    /**
     * Get an indexed fragmentation state change for a given form.
     *
     * @param formName The name of the form
     * @param index    The fragmentation state index
     * @return FragmentationState
     */
    public FragmentationState.Change getFormFragmentationStateChange(
            String formName, int index) {
        synchronized (formFragmentationStateChangeMap) {
            if (logger.isDebugEnabled()) {
                logger.debug("Getting form fragmentation change. Form " +
                        formName + " index " + index);
            }
            IndexedObjectCache cache =
                    (IndexedObjectCache) formFragmentationStateChangeMap
                            .get(formName);
            FragmentationState.Change change =
                    (FragmentationState.Change) cache.getObject(index);
            if (logger.isDebugEnabled()) {
                logger.debug("Returned change is " + change);
            }
            return change;
        }
    }

    /**
     * Get the index for a fragmentation state change object.
     *
     * @param formName The name of the form in question
     * @param change   The FragmentationState.Change object whose
     *                 index is required.
     * @return The index for the change object.
     */
    public int getFormFragmentationStateChangeIndex(
            String formName, FragmentationState.Change change) {

        synchronized (formFragmentationStateChangeMap) {
            if (logger.isDebugEnabled()) {
                logger.debug("Getting index of change " + change +
                        " from form " + formName);
            }
            IndexedObjectCache cache =
                    (IndexedObjectCache) formFragmentationStateChangeMap
                            .get(formName);
            int idx = cache.getIndex(change);
            if (logger.isDebugEnabled()) {
                logger.debug("Returned index is " + idx);
            }
            return idx;
        }
    }

    /**
     * Process the specified string and create a FragmentationState object based on the current state and
     * possible changes to it.
     *
     * @param formName The name of the fragmented form.
     * @param update   The String which encodes details as to what the FragmentationState of this form should
     *                 look like.
     * @return The created FragmentationState or null if the state was empty.
     */
    public FragmentationState getFormFragmentationState(
            String formName, String update) {
        // The update string is of the following format.
        // [c<state index>.](f<fragment change index>)
        int index;

        int stateIndex = -1;
        int changeIndex = -1;

        String value;
        // If the first character is a 'c' then the text between it and the first
        // . is the current state index.
        if (update.charAt(0) == 'c') {
            index = update.indexOf('.');
            if (index == -1) {
                value = update.substring(1);
                update = "";
            } else {
                value = update.substring(1, index);
                update = update.substring(index + 1);
            }

            try {
                stateIndex = Integer.parseInt(value);
            }
            catch (NumberFormatException nfe) {
                logger.warn("state-index", new Object[]{value},
                        nfe);
                return null;
            }
        }

        // If the first character is a 'f' then the rest of the text is the index
        // of the fragment change object.
        if (update.charAt(0) == 'f') {
            value = update.substring(1);
            try {
                changeIndex = Integer.parseInt(value);
            }
            catch (NumberFormatException nfe) {
                logger.warn("fragment-index", new Object[]{value},
                        nfe);
                return null;
            }
        } else {
            logger.warn("fragment-change-missing", new Object[]{update});
            return null;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Form " + formName + " State index " + stateIndex
                    + " change index " + changeIndex);
        }

        FragmentationState state;
        if (stateIndex == -1) {
            state = new FragmentationState();
            if (logger.isDebugEnabled()) {
                logger.debug("Created " + state);
            }
        } else {
            FragmentationState current =
                    getFormFragmentationState(formName, stateIndex);
            if (current == null) {
                logger.warn("state-not-found");
                return null;
            }

            state = current.cloneState();
            if (logger.isDebugEnabled()) {
                logger.debug("Cloned " + current + " as " + state);
            }
        }

        FragmentationState.Change change
                = getFormFragmentationStateChange(formName, changeIndex);

        if (change == null) {
            logger.warn("change-for-index-missing",
                    new Object[]{new Integer(changeIndex)});
            return null;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Change " + change);
        }

        change.apply(state);

        // If after the state change the state is now empty then discard the
        // state as the default processing will do the right thing.
        if (state.isEmpty()) {
            state = null;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("State after applying change " + state);
        }

        return state;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/

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
package com.volantis.mcs.protocols.forms;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.MessageLocalizer;

import java.util.List;

/**
 * Encapsulates data that is common to XDIME1 and XDIME2 form fragment
 * implementations.
 */
public interface AbstractFormFragment {

    /**
     * Used to retrieve localized exception messages.
     */
    MessageLocalizer MESSAGE_LOCALIZER = LocalizationFactory.
            createMessageLocalizer(AbstractFormFragment.class);

    /**
     * Default internationalized fragment link labels.
     */
    String NEXT_TEXT = MESSAGE_LOCALIZER.format(
            "fragment-link-label-next");
    String PREVIOUS_TEXT = MESSAGE_LOCALIZER.format(
            "fragment-link-label-previous");
    String RESET_TEXT = MESSAGE_LOCALIZER.format(
            "fragment-link-label-reset");

    /**
     * Return the name of this fragment.
     *
     * @return String name of this fragment.
     */
    String getName();

    /**
     * Return the list of fragment links that should appear before the fragment.
     * May be empty, will not be null.
     *
     * @param previous  the fragment that will appear before this one
     * @param next      the fragment that should appear after this one
     * @return List of fragment links that should appear before the fragment
     */
    List getBeforeFragmentLinks(AbstractFormFragment previous,
            AbstractFormFragment next);

    /**
     * Return the list of fragment links that should appear after the fragment.
     * May be empty, will not be null.
     *
     * @param previous  the fragment that will appear before this one
     * @param next      the fragment that should appear after this one
     * @return List of fragment links that should appear after the fragment
     */
    List getAfterFragmentLinks(AbstractFormFragment previous,
            AbstractFormFragment next);

    /**
     * Retrieve the label of the fragment. This will be used as the label of a
     * fragment link which leads to this fragment if no other label was
     * specified.
     *
     * @return String label of the fragment
     */
    String getLabel();
}

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
package com.volantis.mcs.eclipse.common.odom;

/**
 * A class for configuring the workings of an ODOMSelectionFilter.
 *
 * An ODOMSelectionFilterConfiguration can configure an ODOMSelectionFilter to
 * filter-in or filter-out selection elements. In addition a filter can be
 * configured to ignore selections that after filtering are empty or not to
 * ignore empty selections. This allows users to specify whether or not they
 * they are interested in selection changes that are not related to the
 * elements known to the filter.
 */
public class ODOMSelectionFilterConfiguration {
    /**
     * Flag indicating whether the filter should be an inclusion or exclusion
     * type filter. If true all elements specified in the filter will be
     * included on the filter operation and all elements not specified in the
     * filter will be excluded on the filter operation. The reverse is true
     * is this flag is false.
     */
    private final boolean isInclusionFilter;

    /**
     * Flag indicating whether or not users of the filter should notify
     * selection listeners if there is a selection change that the filter
     * resolves to no elements. If this flag is true then selection changes
     * that post filtering have no elements will result in no selection
     * change event. If this flag is false then selection changes that
     * post filtering have no elements will result in a selection change
     * event that indicates nothing is selected.
     */
    private final boolean ignoreEmptyFilteredSelections;


    /**
     * Construct a new ODOMSelectionFilterConfiguration.
     * @param inclusionFilter the isInclusionFilter value
     * @param ignoreEmptyFilteredSelections the ignoreEmptyFilteredSelections
     * value
     */
    public ODOMSelectionFilterConfiguration(boolean inclusionFilter,
                                            boolean ignoreEmptyFilteredSelections) {
        this.isInclusionFilter = inclusionFilter;
        this.ignoreEmptyFilteredSelections = ignoreEmptyFilteredSelections;
    }

    /**
     * Indicate whether or not the filter is an inclusion filter.
     * @return true if the filter is an inclusion filter; false otherwise.
     */
    public boolean isInclusionFilter() {
        return isInclusionFilter;
    }

    /**
     * Indicate whether or not the filter users should ignore empty selections
     * post filtering.
     * @return true if filter users should ignore empty selections post
     * filtering; false otherwise
     */
    public boolean ignoreEmptyFilteredSelections() {
        return ignoreEmptyFilteredSelections;
    }

    // javadoc inherited
    public boolean equals(Object o) {
        boolean equals = o!=null && getClass().equals(o.getClass());

        if(equals) {
            ODOMSelectionFilterConfiguration config =
                    (ODOMSelectionFilterConfiguration) o;
            equals = isInclusionFilter() == config.isInclusionFilter() &&
                    ignoreEmptyFilteredSelections() ==
                    config.ignoreEmptyFilteredSelections();
        }

        return equals;
    }

    // javadoc inherited
    public int hashCode() {
        int i1 = (isInclusionFilter() ? 1 : 0) << 1;
        int i2 = ignoreEmptyFilteredSelections() ? 1 : 0;

        return i1 | i2;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Sep-04	5369/1	allan	VBM:2004051306 Don't unselect devices from Structure page selection

 ===========================================================================
*/

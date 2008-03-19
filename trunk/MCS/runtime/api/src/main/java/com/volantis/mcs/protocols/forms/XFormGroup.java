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

import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.MCSBreakAfterKeywords;
import com.volantis.styling.Styles;

/**
 * Encapsulates information provided for XForms Groups. A group does not
 * necessarily correspond to a form fragment (that would depend on whether or
 * this group's styles dictated that the form should fragment after this group)
 * but every XDIME2 form fragment will refer to a group.
 */
public final class XFormGroup {

    /**
     * String label which describes the group. Can be used as the link label to
     * a fragment if this group causes a form fragment to be created.
     */
    private String label;

    /**
     * Styles which describe the behaviour of the group i.e. whether or not it
     * should cause fragmentation, what it's links should say etc.
     */
    private final Styles styles;

    /**
     * Flag which indicates if this group should cause the form to fragment
     * after it.
     */
    private boolean causesFragmentation;

    /**
     * String which identifies the inclusion path of this group. It is the
     * value returned by {@link com.volantis.mcs.protocols.DeviceLayoutContext#getInclusionPath()}
     * at the point when the group is created It is used to create the
     * {@link com.volantis.mcs.runtime.FragmentationState.FragmentChange} in
     * {@link com.volantis.mcs.protocols.VolantisProtocol#doFormLink}.
     */
    private final String inclusionPath;

    /**
     * Initialize a new instance using the given parameters.
     * @param styles        styles which should be applied to the group
     * @param inclusionPath inclusion path for the group
     */
    public XFormGroup(Styles styles, String inclusionPath) {
        this.styles = styles;
        this.inclusionPath = inclusionPath;

        // Determine whether this fragment should cause fragmentation
        setCausesFragmentation(styles);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Styles getStyles() {
        return styles;
    }

    /**
     * Retrieve the inclusion path for this group. It is the value returned by
     * {@link com.volantis.mcs.protocols.DeviceLayoutContext#getInclusionPath()}
     * at the point when the group is created It is used to create the
     * {@link com.volantis.mcs.runtime.FragmentationState.FragmentChange} in
     * {@link com.volantis.mcs.protocols.VolantisProtocol#doFormLink}.
     *
     * @return String inclusion path of this group.
     */
    public String getInclusionPath() {
        return inclusionPath;
    }

    /**
     * Check the value of the {@link StylePropertyDetails#MCS_BREAK_AFTER}
     * property in order to determine whether this group should cause the form
     * to be fragmented from this point on.
     *
     * @return true if the form should be fragmented from this point on, and
     * false otherwise.
     */
    public boolean causesFragmentation() {
        return causesFragmentation;
    }

    /**
     * Determine whether or not this group causes fragmentation.
     *
     * @param styles    used to determine whether this group causes fragmentation
     */
    private void setCausesFragmentation(Styles styles) {
        StyleValue breakAfter = styles.getPropertyValues().
                getSpecifiedValue(StylePropertyDetails.MCS_BREAK_AFTER);
        // Only fragment if mcs-break-after == always.
        if (breakAfter == MCSBreakAfterKeywords.ALWAYS) {
            causesFragmentation = true;
        }
    }
}

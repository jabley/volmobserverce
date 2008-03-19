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

import java.util.List;

/**
 * Encapsulates data about a fragmentable form.
 */
public final class FragmentableFormData implements AbstractForm {

    /**
     * {@link FormDescriptor} which describes this form.
     */
    private final FormDescriptor formDescriptor;

    /**
     * Index of the form fragment that is currently being processed. May be -1
     * if this form is not being fragmented.
     */
    private int activeFragmentIndex = -1;

    /**
     * Flag which indicates if the form represented by this data is fragmented.
     */
    private boolean isFragmented;

    /**
     * List of this form's fragments.
     */
    private List formFragments;

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param formDescriptor
     * @param activeFragmentIndex
     * @param fragmented
     * @param formFragments
     */
    public FragmentableFormData(FormDescriptor formDescriptor,
            int activeFragmentIndex,
            boolean fragmented,
            List formFragments) {
        this.formDescriptor = formDescriptor;
        this.activeFragmentIndex = activeFragmentIndex;
        isFragmented = fragmented;
        this.formFragments = formFragments;
    }

    /**
     * Return the details of the form fragment that is currently active (i.e.
     * whose markup should be output). May be null if this form is not
     * fragmented.
     *
     * @return FormFragmentData details of the form fragment that is currently
     * active (i.e. whose markup should be output). May be null if this form is
     * not fragmented.
     */
    public AbstractFormFragment getActiveFormFragment() {

        FormFragmentData data = null;
        if (activeFragmentIndex >= 0 && !formFragments.isEmpty()) {
            data = (FormFragmentData) formFragments.get(activeFragmentIndex);
        }

        return data;
    }

    // Javadoc inherited.
    public String getName() {
        return formDescriptor.getName();
    }

    // Javadoc inherited.
    public boolean isFragmented() {
        return isFragmented;
    }

    // Javadoc inherited.
    public AbstractFormFragment getNextFormFragment(
            AbstractFormFragment current) {

        FormFragmentData data = null;
        int nextFragmentIndex = activeFragmentIndex + 1;
        if (nextFragmentIndex < formFragments.size()){
            data = (FormFragmentData) formFragments.get(nextFragmentIndex);
        }
        return data;
    }

    // Javadoc inherited.
    public AbstractFormFragment getPreviousFormFragment(
            AbstractFormFragment current) {

        FormFragmentData data = null;
        int previousFragmentIndex = activeFragmentIndex - 1;
        if (previousFragmentIndex >= 0) {
            data = (FormFragmentData) formFragments.get(previousFragmentIndex);
        }
        return data;
    }

    public void update(int activeFragmentIndex, boolean isFragmented, 
            List formFragments) {
        this.activeFragmentIndex = activeFragmentIndex;
        this.isFragmented = isFragmented;
        this.formFragments = formFragments;
    }

    public List getFormFragments() {
        return formFragments;
    }
}

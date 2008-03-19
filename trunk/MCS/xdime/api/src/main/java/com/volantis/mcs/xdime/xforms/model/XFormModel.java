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
package com.volantis.mcs.xdime.xforms.model;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.protocols.EventAttributes;
import com.volantis.mcs.protocols.XFFormAttributes;
import com.volantis.mcs.protocols.XFFormFieldAttributes;
import com.volantis.mcs.protocols.forms.EmulatedXFormDescriptor;
import com.volantis.mcs.protocols.forms.FieldDescriptor;
import com.volantis.mcs.protocols.forms.FormFragmentData;
import com.volantis.mcs.protocols.forms.FragmentableFormData;
import com.volantis.mcs.xdime.ElementOutputState;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.styling.Styles;

import java.util.Iterator;
import java.util.List;

/**
 * Interface which represents an xform model within MCS.
 * @mock.generate 
 */
public abstract class XFormModel {

    /**
     * Returns the String identifier for this xforms control.
     *
     * @return the identifier of this xforms control
     */
    public abstract String getID();

    /**
     * Add an item to the map of items that have been defined in this model.
     * The item may be referenced by name by controls wishing to have their
     * initial values set to the content of the item.
     *
     * @param name          of the item, unique within the model
     * @param content       String content of the item
     */
    public abstract void addItem(String name, String content)
            throws XDIMEException;

    /**
     * Returns an Iterator over the items that have been defined in this model.
     *
     * @return an Iterator over the items defined in this model. Will never be
     * null.
     */
    public abstract Iterator getItemIterator();

    /**
     * Returns the {@link SIItem} with the specified name.
     *
     * @param name  identifier of the item
     * @return the {@link SIItem} with the specified name. May be null if the
     * item wasn't defined in this model.
     */
    public abstract SIItem getItem(String name);

    /**
     * Add an submission to the map of submissions that have been defined in
     * this model.
     *
     * @param submissionID  Should be unique in this model
     * @param events        The events which apply for this submission.
     * @param action        URL to which controls referencing this submission
     *                      shoud be submitted.
     * @param method        of the submission (GET or POST)
     */
    public abstract void addSubmission(String submissionID,
                                       EventAttributes events,
                                       String action,
                                       String method) throws XDIMEException;

    /**
     * Returns the {@link XFSubmission} of this model.
     *
     * @return the {@link XFSubmission} of this model. 
     */
    public abstract XFSubmission getSubmission();

    /**
     * Return the XFFormAttributes which represent this model internally to MCS.
     *
     * @return XFFormAttributes which represent this model internally to MCS.
     */
    public abstract XFFormAttributes getXFFormAttributes();

    public abstract FragmentableFormData getFormData();

    /**
     * Return the EmulatedXFormDescriptor which represents this model.
     *
     * @return EmulatedXFormDescriptor which represents this model
     */
    public abstract EmulatedXFormDescriptor getXFormDescriptor();

    /**
     * Add the specific form control (or field in internal MCS lingo) to the
     * XFFormAttributes maintained by the model.
     *
     * @param fd                FieldDescriptor of the control/field
     * @param fieldAttributes   MCSAttributes of the control/field
     */
    public abstract void addControl(FieldDescriptor fd,
            XFFormFieldAttributes fieldAttributes);

    /**
     * Returns true if the model has already been written out, and false
     * otherwise. If this returns true when more controls are waiting to be
     * written out in this model, the xform is invalid.
     *
     * @return  true if the model has already been written out, and false
     * otherwise
     */
    public abstract ModelOutputState getModelState();

    /**
     * Moves the model to the next output state. It begins in BEFORE, then
     * progresses to DURING, then to AFTER.
     */
    public abstract void progressOutputState();

    /**
     * Return the {@link FormFragmentData} instance that is currently being
     * processed. May be null if we are currently outside a group.
     *
     * @return FormFragmentData that is currently being processed.
     */
    public abstract FormFragmentData getCurrentFormFragment();

    /**
     * Retrieve the index of the {@link FormFragmentData} that should be
     * returned to the client in the response. It is an index into the list
     * returned by {@link #getFormFragments()}.
     *
     * @return int index of the fragment which should be returned to the client.
     * Will return -1 if no fragment was requested or has been encountered.
     */
    public abstract int getRequestedFragmentIndex();

    /**
     * Return the map of {@link FormFragmentData} that has been generated for
     * this model. Should only be called when all xform body elements have been
     * encountered i.e. at the end tag of the body element.
     *
     * @return Map of form fragment data that has been generated for this model.
     * Will never be null.
     */
    public abstract List getFormFragments();

    /**
     * Determines if the model is currently in an active state (form controls
     * should be written out) or inactive (form controls should be suppressed
     * i.e. when the form is fragmented and the control is not in the active
     * fragment).
     *
     * @return true if the model is in an active state and controls should be
     * written out, and false otherwise
     */
    public abstract boolean isActive();

    /**
     * Returns the depth of nested groups currently being processed.
     *
     * @return int depth of the the nested groups currently being processed
     */
    public abstract int getNestingDepth();

    public abstract void updateFormFragmentationState(MarinerPageContext context);

    /**
     * The model keeps track of the output states of the group elements that it
     * is currently processing. This method allows new groups to be tracked.
     *
     * @param state     encapsulates the output state of the group element
     */
    public abstract void pushElementOutputState(ElementOutputState state);

    /**
     * Update the output state of all of the group elements that are currently
     * being processed by this model - changes the flag which indicates whether
     * or not the groups are currently inactive.
     *
     * @param isInactiveGroup   true if the groups that are currently being
     *                          processed by this model should become inactive,
     *                          false otherwise
     */
    public abstract void updateAllGroups(boolean isInactiveGroup);

    /**
     * The model keeps track of the output states of the group elements that it
     * is currently processing. This method stops tracking the current group.
     *
     * @return ElementOutputState     the output state at the top of the stack
     */
    public abstract ElementOutputState popElementOutputState();

    /**
     * Create a new {@link com.volantis.mcs.protocols.forms.XFormGroup}
     * instance using the styles provided and push it onto the stack.
     *
     * @param styles
     * @param pageContext
     */
    public abstract void pushGroup(Styles styles,
            MarinerPageContext pageContext);

    /**
     * Pop the current {@link com.volantis.mcs.protocols.forms.XFormGroup} off
     * the top of the stack.
     */
    public abstract void popGroup();

    /**
     * Set the label on the current group.
     *
     * @param label String
     */
    public abstract void setGroupLabel(String label);

    /**
     * Update the existing {@link EventAttributes} associated with this model.
     * (NB: Any events in these attributes will override those in the original)
     *
     * @param eventAttributes   with which to update the model event attributes
     */
    public abstract void updateEventAttributes(EventAttributes eventAttributes);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Oct-05	9637/5	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9637/3	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 ===========================================================================
*/

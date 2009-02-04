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
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.EventAttributes;
import com.volantis.mcs.protocols.XFFormAttributes;
import com.volantis.mcs.protocols.XFFormFieldAttributes;
import com.volantis.mcs.protocols.EventConstants;
import com.volantis.mcs.protocols.assets.implementation.LiteralLinkAssetReference;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.protocols.forms.EmulatedXFormDescriptor;
import com.volantis.mcs.protocols.forms.FieldDescriptor;
import com.volantis.mcs.protocols.forms.FormFragmentData;
import com.volantis.mcs.protocols.forms.FragmentableFormData;
import com.volantis.mcs.protocols.forms.XFormGroup;
import com.volantis.mcs.xdime.ElementOutputState;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.styling.Styles;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Concrete implementation of the internal MCS representation of an xforms
 * model.
 */
public class XFormModelImpl extends XFormModel {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(XFormModelImpl.class);

    /**
     * Map of {@link SIItem} instances that have been defined in this model,
     * indexed against their names.
     */
    private HashMap items;

    /**
     * The {@link XFSubmission} that has been defined for this model. Currently
     * only one can be defined per model (in order to convert into internal MCS
     * xfforms format) despite the fact that XForms allows multiple submission
     */
    private XFSubmission submission;

    /**
     * The unique identifier of this model.
     */
    private String id;

    /**
     * The {@link XFFormAttributes} that will be used when outputting this
     * model.
     */
    private XFFormAttributes attributes;

    /**
     * The {@link EmulatedXFormDescriptor} that will be used when outputting
     * this model.
     */
    private EmulatedXFormDescriptor formDescriptor;

    private FragmentableFormData formData;

    /**
     * Flag which determines whether none of the controls referenced by the
     * model have been written out, some have, or all have.
     */
    protected ModelOutputState modelOutputState =
            ModelOutputState.BEFORE;

    private static final String FORM_FRAG_NAME_PREFIX = "ff";

    protected int nestingDepth = 0;

    /**
     * Used to assign IDs to the form fragments that are unique in the
     * containing model.
     */
    int counter = 0;

    /**
     * The stack of output states of the group elements that are currently
     * being processed by this model.
     */
    private Stack elementOutputStates;

    protected Stack groups;
    protected List fragments;
    protected int requestedFragmentIndex = -1;
    protected int currentFragmentIndex = -1;

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param id    the identifier of the model. Must be non null.
     * @param fd    describes the structure of this new model.
     */
    public XFormModelImpl(String id, EmulatedXFormDescriptor fd) {
        this.id = id;

        formDescriptor = fd;
        attributes = new XFFormAttributes();
        attributes.setId(id);
        attributes.setName(id);
        // Create the reciprocal link between the fd and form attributes.
        attributes.setFormDescriptor(formDescriptor);
        formDescriptor.setFormAttributes(attributes);
        items = new HashMap();
        elementOutputStates = new Stack();
        groups = new Stack();
        fragments = new ArrayList();
    }

    // Javadoc inherited.
    public void addItem(String name, String content) throws XDIMEException {

        if (items.containsKey(name)) {
            throw new XDIMEException(EXCEPTION_LOCALIZER.format(
                    "xforms-entity-already-exists",
                    new String[]{"si:item", name}));
        }

        final SIItemImpl item =
                new SIItemImpl(content, name, id);
        items.put(name, item);

    }

    // Javadoc inherited.
    public Iterator getItemIterator() {
        return items.values().iterator();
    }

    // Javadoc inherited.
    public SIItem getItem(String name) {
        return (SIItem) items.get(name);
    }

    // Javadoc inherited.
    public void addSubmission(String submissionID, EventAttributes events, String action,
            String method) throws XDIMEException {

        if (submission != null) {
            throw new XDIMEException(EXCEPTION_LOCALIZER.format(
                    "xforms-entity-already-exists",
                    new Object[]{"xf:submission", submissionID}));
        }

        // todo: remove/fix submission object, is not actually used, see
        // http://mantis:8080/mantis/Mantis_View.jsp?mantisid=2006061614
        submission = new XFSubmissionImpl(submissionID, events, action, method, id);

        // creating a submission defaults the method to GET if null, so use that
        method = submission.getMethod().toString();

        // add the submission info to the XFFormAttributes
        attributes.setAction(new LiteralLinkAssetReference(action));
        attributes.setMethod(method);
        attributes.setEventAttributes(events);

        // add the submission info to the EmulatedXFormDescriptor
        formDescriptor.setFormURL(action);
        formDescriptor.setFormMethod(method);
    }

    // Javadoc inherited.
    public XFSubmission getSubmission() {
        return submission;
    }

    // Javadoc inherited.
    public void addControl(FieldDescriptor fd,
            XFFormFieldAttributes fieldAttributes) {
        formDescriptor.addField(fd);
        attributes.addField(fieldAttributes);
    }

    // Javadoc inherited.
    public String getID() {
        return id;
    }

    // Javadoc inherited.
    public XFFormAttributes getXFFormAttributes() {
        return attributes;
    }

    // Javadoc inherited.
    public FragmentableFormData getFormData() {
        int requestedFragmentIndex = getRequestedFragmentIndex();
        if (formData == null) {
            formData = new FragmentableFormData(formDescriptor,
                    requestedFragmentIndex, isFragmented(), fragments);
        } else {
            formData.update(requestedFragmentIndex, isFragmented(), fragments);
        }
        return formData;
    }

    // Javadoc inherited.
    public EmulatedXFormDescriptor getXFormDescriptor() {
        return formDescriptor;
    }

    // Javadoc inherited.
    public ModelOutputState getModelState() {
        return modelOutputState;
    }

    // Javadoc inherited.
    public void progressOutputState() {
        modelOutputState = modelOutputState.getNextState();
    }

    public int getRequestedFragmentIndex() {
        return requestedFragmentIndex;
    }

    // Javadoc inherited.
    public List getFormFragments() {
        return fragments;
    }

    public boolean isActive() {
        boolean isActive = true;

        if (currentFragmentIndex != requestedFragmentIndex) {
            isActive = false;
        }

        return isActive;
    }

    // Javadoc inherited.
    protected boolean isFragmented() {

        boolean isFragmented = false;
        // Form is fragmented if more than one fragment has been found, or if a
        // fragment other than the first has been explicitly requested by name.
        if (fragments.size() > 1 || requestedFragmentIndex > 0) {
            isFragmented = true;
        }
        return isFragmented;
    }

    // Javadoc inherited.
    public int getNestingDepth() {
        return groups.size();
    }

    // Javadoc inherited.
    public void updateFormFragmentationState(MarinerPageContext context) {
        // Determine which fragment was requested.
        if (requestedFragmentIndex < 0) {
            // Update the fragmentation state and get the name of the form
            // fragment that is being requested.
            String reqFragName = context.updateFormFragmentationState(id);

            // if the requested fragment name is still null then this is the
            // first request for this page. This means the requested fragment
            // is effectively the first one. If it isn't null, we can't map it
            // to an index yet e.g if we're processing the first fragment and
            // the requested name refers to the sixth - there will only be one
            // fragment in the list, and so we can't resolve the name yet.
            if (reqFragName == null) {
                // Can't generate the name because the first fragment may not
                // be the one that is generated first.
                requestedFragmentIndex = 0;
            } else {
                // It should correspond to the string "ff" + an index.
                if (!reqFragName.startsWith(FORM_FRAG_NAME_PREFIX)) {
                    throw new IllegalStateException(reqFragName +
                            " is not a valid fragment identifier");
                }
                requestedFragmentIndex = Integer.parseInt(reqFragName.substring(2));
            }
        }
    }

    /**
     * Creates a form fragment name which is unique within its containing model
     * and produces consistent results when applied to the same input markup.
     */
    private String generateFormFragmentName() {
       return FORM_FRAG_NAME_PREFIX + counter++;
    }

    // Javadoc inherited.
    public void pushElementOutputState(ElementOutputState state) {
        elementOutputStates.push(state);
    }

    // Javadoc inherited.
    public void updateAllGroups(boolean isInactiveGroup) {
        final int stateCount = elementOutputStates.size();
        for (int i = 0; i < stateCount; i++) {
            final ElementOutputState state =
                    (ElementOutputState) elementOutputStates.get(i);
            state.setIsInactiveGroup(isInactiveGroup);
        }
    }

    // Javadoc inherited.
    public ElementOutputState popElementOutputState() {
        return (ElementOutputState) elementOutputStates.pop();
    }

    // Javadoc inherited.
    public void pushGroup(Styles styles, MarinerPageContext pageContext) {
        updateFormFragmentationState(pageContext);
        String inclusionPath = pageContext.getDeviceLayoutContext().
                getInclusionPath();
        final XFormGroup group = new XFormGroup(styles, inclusionPath);

        // Add a new fragment if this group is not nested and either there are
        // no fragments or the last one caused fragmentation.
        final FormFragmentData current = getCurrentFormFragment();
        if (groups.size() == 0 &&
                (current == null || current.causesFragmentation())) {
            addNextFormFragment(group);
        } else if (!current.causesFragmentation()) {
            // Update the current form fragment to have the correct styles.
            updateCurrentFormFragment(group);
        }

        groups.push(group);
    }

    // Javadoc inherited.
    public void popGroup() {
        XFormGroup group = (XFormGroup) groups.pop();
        // Add a new fragment if the popped group is nested and causes
        // fragmentation.
        if (groups.size() > 0 && group.causesFragmentation()) {
            addNextFormFragment((XFormGroup) groups.peek());
        }
    }

    // Javadoc inherited.
    public FormFragmentData getCurrentFormFragment() {
        FormFragmentData formFragmentData =  null;

        int numFragments = fragments.size();
        if (numFragments > 0) {
            // return the last fragment in the list.
            formFragmentData = (FormFragmentData) fragments.get(numFragments - 1);
        }

        return formFragmentData;
    }

    // Javadoc inherited.
    public void setGroupLabel(String label) {
        if (!groups.isEmpty()) {
            final XFormGroup current = (XFormGroup) groups.peek();
            current.setLabel(label);
        }
    }

    /**
     * Create a form fragment with the next ID (which uses the group provided)
     * and increment the current fragment index.
     *
     * @param group which controls the fragment behaviour
     * @return FormFragmentData the new fragment
     */
    private FormFragmentData addNextFormFragment(XFormGroup group) {
        final String name = generateFormFragmentName();
        final FormFragmentData fragment = new FormFragmentData(name, group);
        fragments.add(fragment);
        currentFragmentIndex ++;
        return fragment;
    }

    private void updateCurrentFormFragment(XFormGroup group) {
        ((FormFragmentData)fragments.get(currentFragmentIndex)).resetGroup(group);
    }

    // Javadoc inherited.
    public void updateEventAttributes(EventAttributes eventAttributes) {
        EventAttributes eventAtts = attributes.getEventAttributes(true);
        for (int i=0; i< EventConstants.MAX_EVENTS; i++) {
            ScriptAssetReference event = eventAttributes.getEvent(i);
            // only override if the replacing one is non null.
            if (event != null) {
                eventAtts.setEvent(i, event);
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/2	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05	9637/7	emma	VBM:2005092807 Adding tests for XForms emulation

 02-Oct-05	9637/5	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9637/3	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 ===========================================================================
*/

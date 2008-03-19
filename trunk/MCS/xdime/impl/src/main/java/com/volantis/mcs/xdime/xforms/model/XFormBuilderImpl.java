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
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.XFFormAttributes;
import com.volantis.mcs.protocols.XFFormFieldAttributes;
import com.volantis.mcs.protocols.XFImplicitAttributes;
import com.volantis.mcs.protocols.forms.EmulatedXFormDescriptor;
import com.volantis.mcs.protocols.forms.FieldDescriptor;
import com.volantis.mcs.protocols.forms.FormDataManager;
import com.volantis.mcs.protocols.forms.ImplicitFieldType;
import com.volantis.mcs.protocols.forms.SessionFormData;
import com.volantis.mcs.xdime.XDIMEAttribute;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class XFormBuilderImpl implements XFormBuilder {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    XFormBuilderImpl.class);

    /**
     * The {@link XFormModel}s that have been defined in this context, indexed
     * against their id.
     */
    private HashMap xformModels;

    /**
     * The identifiers of the controls that have already been written out in
     * this xform, indexed against their id.
     */
    private HashSet encounteredControls;


    /**
     * The {@link XFormModel} which is currently being built.
     */
    protected XFormModel modelBeingBuilt;

    /**
     * The model that is currently being written out.
     */
    protected XFormModel modelBeingWritten;

    /**
     * Used to assign IDs to the form fragments that are unique in the
     * containing model.
     */
    int counter = 0;

    /**
     * Initialize a new instance.
     */
    public XFormBuilderImpl() {
        this.xformModels = new HashMap();
        this.encounteredControls = new HashSet();
    }

    // Javadoc inherited.
    public void addItem(String name, String content) throws XDIMEException {

        // if the model being built is null, the si:item appeared outside an
        // xf:model element, which is all wrong..
        if(modelBeingBuilt == null) {
            throw new XDIMEException(
                    EXCEPTION_LOCALIZER.format("xdime-invalid"));
        }
        modelBeingBuilt.addItem(name, content);
    }

    // Javadoc inherited.
    public SIItem getItem(String name, String modelID) throws XDIMEException {

        SIItem item = null;

        if (modelID != null) {
            XFormModel model = (XFormModel) xformModels.get(modelID);
            if (model == null) {
                // cannot specify a non existent model
                throw new XDIMEException(EXCEPTION_LOCALIZER.format(
                        "xforms-no-such-entity",
                        new String[]{"model", modelID}));
            }

            item = model.getItem(name);
        } else {
            XFormModel model = (XFormModel) xformModels.get(DEFAULT_MODEL_ID);
            if (model != null) {
                item = model.getItem(name);
            }
            // if there is no default or the item was not in the default model,
            // then look in all the others
            // @todo iterate in document order
            for (Iterator iterator = xformModels.values().iterator();
                    item == null && iterator.hasNext();) {
                model = (XFormModel) iterator.next();
                item = model.getItem(name);
            }
        }
        return item;
    }

    // Javadoc inherited.
    public XFormModel addModel(String id, EmulatedXFormDescriptor fd)
            throws XDIMEException {

        if (xformModels.containsKey(id)) {
            throw new XDIMEException(EXCEPTION_LOCALIZER.format(
                    "xforms-entity-already-exists", new Object[]{"model", id}));
        } else {
            modelBeingBuilt = new XFormModelImpl(id, fd);
            xformModels.put(id, modelBeingBuilt);
            return modelBeingBuilt;
        }
    }

    // Javadoc inherited.
    public void updateControl(FieldDescriptor fieldDescriptor,
            XFFormFieldAttributes fieldAttributes) {
        if (modelBeingWritten == null) {
            throw new IllegalStateException("updateControl should not be" +
                    "called unless a registerControl has been called");
        }
        modelBeingWritten.addControl(fieldDescriptor,
                fieldAttributes);
    }

    // Javadoc inherited.
    public boolean isModelBeingWritten(String modelID) {
        boolean result = false;

        if (modelBeingWritten != null) {
            final String currentID = modelBeingWritten.getID();
            if (currentID.equals(modelID)) {
                result = true;
            }
        }
        return result;
    }

    // Javadoc inherited.
    public XFormModel getModel(String id) {
        return (XFormModel) xformModels.get(id);
    }

    // Javadoc inherited.
    public XFormModel getModel(SIItem item) {
        return getModel(item.getEnclosingModelID());
    }

    // Javadoc inherited.
    public int numberOfModels() {
        int size = 0;
        if (xformModels != null) {
            size = xformModels.size();
        }
        return size;
    }

    // Javadoc inherited.
    public XFormModel registerControl(XDIMEAttributes attributes)
            throws XDIMEException {

        // check that a control with this id has not already been encountered
        final String controlID = getAttribute(XDIMEAttribute.ID, attributes);
        if (controlID != null ){
            if (encounteredControls.contains(controlID)) {
                throw new XDIMEException(EXCEPTION_LOCALIZER.format(
                        "xforms-entity-already-exists",
                        new String[]{"control", controlID}));
            } else {
                // log that we've encountered this control
                encounteredControls.add(controlID);
            }
        }

        XFormModel nextModel = getCandidateModel(attributes);

        if (nextModel != modelBeingWritten) {

            if (modelBeingWritten != null) {
                // Given that we've received a control which references a new
                // model, we should stop processing the old one.
                modelBeingWritten.progressOutputState();

                // Can't change groups while nested in another group, so check
                // that we're not.
                if (modelBeingWritten.getNestingDepth() > 0) {
                    throw new XDIMEException(EXCEPTION_LOCALIZER.format(
                            "xforms-mixed-groups", new String[] {
                                nextModel.getID(), modelBeingWritten.getID()}));
                }
            }

            // check the state of the new model
            if (!nextModel.getModelState().equals(ModelOutputState.BEFORE)) {
                throw new XDIMEException(
                        EXCEPTION_LOCALIZER.format("xforms-mixed-forms",
                                new String[]{nextModel.getID(),
                                             modelBeingWritten.getID()}));
            }

            modelBeingWritten = nextModel;
        }

        return nextModel;
    }

    // Javadoc inherited.
    public XFormModel registerGroup(XDIMEAttributes attributes)
            throws XDIMEException {
        XFormModel nextModel = getCandidateModel(attributes);
        if (modelBeingWritten != null && nextModel != modelBeingWritten &&
                modelBeingWritten.getNestingDepth() > 0) {
            throw new XDIMEException(
                    EXCEPTION_LOCALIZER.format("xforms-mixed-groups",
                            new String[]{nextModel.getID(),
                                         modelBeingWritten.getID()}));
        }
        modelBeingWritten = nextModel;
        return nextModel;
    }

    // Javadoc inherited.
    public void addSubmission(String submissionID, EventAttributes events,
            String action, String method) throws XDIMEException {

        // if the model being built is null, the xf:submission appeared outside
        // an xf:model element, which is all wrong..
        if (modelBeingBuilt == null) {
            throw new XDIMEException(
                    EXCEPTION_LOCALIZER.format("xdime-invalid"));
        }
        modelBeingBuilt.addSubmission(submissionID, events, action, method);
    }

    // Javadoc inherited.
    public XFSubmission getSubmission(String submissionID, String modelID)
            throws XDIMEException {

        // todo: remove/fix this method, it is not used.
        // see http://mantis:8080/mantis/Mantis_View.jsp?mantisid=2006061614

        XFSubmission submission = null;

        if (submissionID == null) {
            // this message is slightly bodgy, but this method will only be
            // called with id equals the value of the submission attribute
            throw new XDIMEException(EXCEPTION_LOCALIZER.format(
                    "xdime-attribute-value-invalid",
                    new String[]{submissionID, "submission"}));
        }

        if (modelID != null) {
            XFormModel model = (XFormModel) xformModels.get(modelID);
            if (model == null) {
                // cannot specify a non existent model
                throw new XDIMEException(EXCEPTION_LOCALIZER.format(
                    "xforms-no-such-entity",
                    new String[]{"model", modelID}));
            }

            submission = model.getSubmission();
        } else {
            XFormModel model = (XFormModel) xformModels.get(DEFAULT_MODEL_ID);
            if (model != null) {
                if (model.getSubmission().getId().equals(submissionID)) {
                    submission = model.getSubmission();
                }
            }
            // if there is no default or the item was not in the default model,
            // then look in all the others
            // @todo iterate in document order
            for (Iterator iterator = xformModels.values().iterator();
                    submission == null && iterator.hasNext();) {
                model = (XFormModel) iterator.next();
                if (model.getSubmission().getId().equals(submissionID)) {
                    submission = model.getSubmission();
                }
            }
        }

        // ensure that the submission being referenced exists.
        if (submission == null){
            throw new XDIMEException(EXCEPTION_LOCALIZER.format(
                    "xdime-invalid-submission-id", submissionID));
        }

        return submission;
    }

    // Javadoc inherited.
    public void generateImplicitElements(VolantisProtocol protocol) {
        Iterator modelIterator = xformModels.values().iterator();
        while (modelIterator.hasNext()) {
            generateImplicitElements((XFormModel)modelIterator.next(),
                    protocol);
        }
    }

    /**
     * Generates implicit elements for any si:items in the supplied model that
     * have not been referenced.
     *
     * @param model     for which to generate implicit elements
     * @param protocol  for which to generate the implicit elements
     */
    protected void generateImplicitElements(XFormModel model,
            VolantisProtocol protocol) {
        Iterator itemIterator = model.getItemIterator();
        while (itemIterator.hasNext()) {
            final XFImplicitAttributes attributes;
            final FieldDescriptor fd = new FieldDescriptor();
            SIItem item = (SIItem)itemIterator.next();
            if (!item.isReferenced()) {
                // populate XFFormFieldAttributes
                attributes = new XFImplicitAttributes();
                attributes.setName(item.getName());
                attributes.setValue(item.getUnprocessedValue());
                attributes.setContainingXFFormName(
                        model.getXFFormAttributes().getName());

                // populate FieldDescriptor
                fd.setName(item.getName());
                fd.setType(ImplicitFieldType.getSingleton());
                fd.setInitialValue(item.getUnprocessedValue());

                // add the control to the model
                model.addControl(fd, attributes);

                // populate the XFormAttributes
                attributes.setFormAttributes(model.getXFFormAttributes());

                // actually write out the implicit value
                protocol.doImplicitValue(attributes);
            }
        }
    }

    // Javadoc inherited.
    public void registerFormDescriptors(VolantisProtocol protocol) {
        Iterator modelIterator = xformModels.values().iterator();

        while (modelIterator.hasNext()) {
            XFormModel model = (XFormModel) modelIterator.next();
            final XFFormAttributes xfFormAttributes =
                    model.getXFFormAttributes();
            final EmulatedXFormDescriptor fd = model.getXFormDescriptor();

            MarinerPageContext pageContext = protocol.getMarinerPageContext();
            FormDataManager formDataManager = pageContext.getFormDataManager();

            // Store the form data in the XFFormAttributes.
            xfFormAttributes.setFormData(model.getFormData());

            // Get the form data from the session context. The act of
            // retrieving it causes it to be created if it isn't already there.
            SessionFormData formData = formDataManager.getSessionFormData(fd);
            // The fd in the session is built up over requests for subsequent
            // fragments, so add the fields found in this request to it.
            formData.addFieldDescriptors(fd.getFields());

            // Set the form specifier (which identifies the form in the
            // session) on the form descriptor and form attributes.
            String formSpecifier = formData.getFormSpecifier();
            xfFormAttributes.setFormSpecifier(formSpecifier);
            // Store everything else in the form descriptor and store it.
            fd.setFormSpecifier(formSpecifier);
            fd.setFormAttributes(xfFormAttributes);
        }
    }

    // Javadoc inherited.
    public XFormModel getCurrentModel() {
        return modelBeingWritten;
    }

    //Javadoc inherited.
    public XFormModel getFirstModel() {
        XFormModel model = null;
        if (numberOfModels() > 0) {
            model = (XFormModel)xformModels.values().iterator().next();
        }
        return model;
    }


    /**
     *
     * @param attributes
     * @return
     * @throws XDIMEException
     */
    private XFormModel getCandidateModel(XDIMEAttributes attributes)
            throws XDIMEException {

        final String ref = getAttribute(XDIMEAttribute.REF, attributes);
        final String modelID = getAttribute(XDIMEAttribute.MODEL, attributes);

        // get the item being referenced
        SIItem item = getItem(ref, modelID);

        // figure out which model this control belongs to
        XFormModel nextModel = null;
        if (item != null) {
            // use the model in which the referenced item was defined
            nextModel = getModel(item);
        } else {
            if (modelID != null) {
                nextModel = getModel(modelID);
            } else {
                // if no model is specified, but we are currently processing a
                // model, then use the current one...
                nextModel = getCurrentModel();
                if (nextModel == null) {
                    // ...otherwise use the default model.
                    nextModel = getModel(XFormBuilder.DEFAULT_MODEL_ID);
                }
                // If we don't have an explicit default model then the first
                // model is implicitly the default.
                if (nextModel == null) {
                    nextModel = getFirstModel();
                }
            }
        }

        // If no model can be identified at all, then throw an exception.
        if (nextModel == null) {
            throw new XDIMEException(
                    EXCEPTION_LOCALIZER.format("xforms-bad-reference"));
        }

        return nextModel;
    }

    /**
     * Get the value of the requested attribute qualified with the given
     * namespace from the supplied XDIMEAttributes. May return null if the
     * element has no value set for that attribute.
     *
     * @param attribute     XDIMEAttribute to retrieve
     * @param attributes    from which to retrieve the attribute value
     * @return String value of the specified attribute. May be null if the
     * element has no value set for the attribute.
     */
    protected String getAttribute(XDIMEAttribute attribute,
            XDIMEAttributes attributes) {
        return attributes.getValue("", attribute.toString());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05	10542/1	emma	VBM:2005112308 Forward port: Many bug fixes: xforms, GUI and pane styling

 01-Dec-05	10447/1	emma	VBM:2005112308 Many bug fixes: xforms, GUI and pane styling

 10-Oct-05	9673/4	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05	9637/8	emma	VBM:2005092807 Adding tests for XForms emulation

 02-Oct-05	9637/6	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9637/3	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 ===========================================================================
*/

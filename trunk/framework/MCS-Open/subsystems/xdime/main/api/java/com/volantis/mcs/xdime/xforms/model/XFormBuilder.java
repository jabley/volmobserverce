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

import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.XFFormFieldAttributes;
import com.volantis.mcs.protocols.EventAttributes;
import com.volantis.mcs.protocols.forms.EmulatedXFormDescriptor;
import com.volantis.mcs.protocols.forms.FieldDescriptor;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEException;

/**
 * An implementation of this interface is used to build the xform model.
 *
 * @mock.generate
 */
public interface XFormBuilder {

    /**
     * The identifier to use for the default (i.e. no id specified) model.
     * Should not be a valid XML ID.
     */
    String DEFAULT_MODEL_ID = "__DEFAULT__";

    /**
     * Add an item to the map of items that have been defined in this xform.
     * It will be considered to be defined in the model currently being built.
     *
     * @param name              of the item. Should be unique in this model
     * @param content           the value for the item. May be a comma
     *                          separated list of values or a single String value
     */
    public abstract void addItem(String name, String content) throws XDIMEException;

    /**
     * Checks if an si:item with the specified name has been defined in the
     * xform model with the specified id.
     * <p/>
     * If it does, it returns the corresponding {@link SIItem}.
     * <p/>
     * If the specified model id is null, it attempts to find an item with the
     * given id in the default model. If no item with that id was declared in
     * the default model, it attempts to find the item in any other models in
     * document order.
     * <p/>
     * The returned item may be null if no si:item with the specified name was
     * declared in this xform.
     *
     * @param name      of the SIItem to retrieve
     * @param modelID   of the model in which the item was declared. May be
     *                  null.
     * @return the SIItem with the specified name. May be null
     */
    public abstract SIItem getItem(String name, String modelID)
            throws XDIMEException;

    /**
     * Create a new {@link XFormModel} with the specified id and add it to the
     * list of xform models that we are currently building. An
     * {@link XDIMEException} is thrown if we are already building a model with
     * the specified id.
     *
     * @param id    String id of the xforms model to create
     * @param fd    describes the form represented by the new model
     * @return the {@link XFormModel} which has the specified id
     */
    XFormModel addModel(String id, EmulatedXFormDescriptor fd) throws XDIMEException;

    /**
     * Update the model that is currently being processed with the field
     * descriptor and attributes for the current control. These are needed to
     * ensure that the form is correctly registered with the session.
     *
     * @param fieldDescriptor
     * @param fieldAttributes
     */
    void updateControl(FieldDescriptor fieldDescriptor,
            XFFormFieldAttributes fieldAttributes);

    /**
     * Determine if the model with the specified id is currently being written
     * out.
     *
     * @param modelID   to check against the model currently being written out
     * @return true if this is the id of the model currently being written out,
     * false otherwise.
     */
    boolean isModelBeingWritten(String modelID);

    /**
     * Returns the {@link XFormModel} which has the specified id. May return
     * null.
     *
     * @param id of the xforms model to return
     * @return the xform model which has the specified id, may be null if no
     * model has been defined with that id.
     */
    XFormModel getModel(String id);

    /**
     * Returns the {@link XFormModel} in which the specified {@link SIItem} is
     * defined. May not return null.
     *
     * @param item whose enclosing model to return
     * @return the xform model in which the specified {@link SIItem} is defined.
     * May not be null.
     */
    XFormModel getModel(SIItem item);

    /**
     * Returns the number of xform models defined in this builder.
     *
     * @return the number of xform models defined in this builder
     */
    int numberOfModels();

    /**
     * Create a new {@link XFSubmission} with the specified id and add it to
     * the list of submissions that we are currently building. An
     * {@link XDIMEException} is thrown if we are already building a submission
     * with the specified id.
     *
     * @param submissionID        String id of the submission to create
     * @param events    the events which apply for this submission.
     * @param action    the URI to which the form data will be submitted for
     *                  processing
     * @param method    the HTTP method (get or post) by which the form will be
     */
    void addSubmission(String submissionID, EventAttributes events, String action,
            String method) throws XDIMEException;

    /**
     * Checks if an xf:submission with the specified id has been defined in
     * the xform model with the specified id.
     * <p/>
     * If it does, it returns the corresponding {@link XFSubmission}.
     * <p/>
     * If the specified model id is null, it attempts to find a submission with
     * the given id in the default model. If no submission with that id was
     * declared in the default model, it attempts to find the submission in any
     * other model in document order.
     * <p/>
     * If no submission with the specified id was declared in this xform an
     * exception will be thrown.
     *
     * @param id        of the {@link XFSubmission} to retrieve
     * @param modelID   of the model in which the submission was declared. May
     *                  be null.
     * @return XFSubmission with the specified id. Will never be null.
     */
    XFSubmission getSubmission(String id, String modelID)
            throws XDIMEException;

    /**
     * Register the control with the specified (in the attributes) id  with the
     * builder, and receive the model in which the control should appear. It
     * also validates that if the referenced item and model exist they are
     * correct and consistent.
     * <P/>
     * Controls should be registered with the builder before their initial
     * values are retrieved.
     * <P/>
     * If this control references a different model to the current one, we
     * assume that no more controls will reference this model. We therefore
     * generate implicit elements for any si:items that were defined in the
     * model but not referenced. It is an error to interlace controls which
     * reference different forms.
     *
     * @param attributes    which provide the id etc.
     * @return XFormModel in which the specified control should be included.
     * Maps to the identifier of the xform model which is referenced by this xform
     * @throws XDIMEException if there is a problem determining in which xfform
     * the xform control should be included.
     */
    XFormModel registerControl(XDIMEAttributes attributes)
            throws XDIMEException;

    /**
     * Register the specified group with the builder.
     *
     * @param attributes    which provide the id etc.
     * @return XFormModel in which the specified control should be included.
     * Maps to the identifier of the xform model which is referenced by this xform
     * @throws XDIMEException if there is a problem determining in which form
     * the group should be included.
     */
    XFormModel registerGroup(XDIMEAttributes attributes) throws XDIMEException;

    /**
     * This method should be called at the point that all xform controls have
     * been processed (i.e. when the body end tag has been reached).
     * <p>
     * Any si:items that were defined in the current model, but have not been
     * referenced should be output as implicit elements.
     * <p>
     * In addition to this, any models that are completely unreferenced should
     * have also have their si:items output as implicit elements.
     *
     * @param protocol  for which to generate the implicit elements
     */
    void generateImplicitElements(VolantisProtocol protocol);

    /**
     * Register the FormDescriptors that have been generated for each of the
     * {@link XFormModel} instances with the session context.
     *
     * @param protocol  to use when registering the form descriptors
     */
    void registerFormDescriptors(VolantisProtocol protocol);

    /**
     * Return the model that is currently being written out. May return null
     * if no model is being written.
     *
     * @return XFormModel that is currently being written out. May be null.
     */
    XFormModel getCurrentModel();

    /**
     * Get the first model.
     *
     * @return XFormModel the first model defined. May be null.
     */
    XFormModel getFirstModel();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/3	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05	9637/5	emma	VBM:2005092807 Adding tests for XForms emulation

 02-Oct-05	9637/3	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 ===========================================================================
*/

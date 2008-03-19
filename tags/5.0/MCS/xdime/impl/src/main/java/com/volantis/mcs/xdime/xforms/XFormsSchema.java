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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xdime.xforms;

import com.volantis.mcs.xdime.schema.SIElements;
import com.volantis.mcs.xdime.schema.XDIMECPInterimSIElements;
import com.volantis.mcs.xdime.schema.XDIMECPSIElements;
import com.volantis.mcs.xdime.xhtml2.XHTML2Schema;
import com.volantis.mcs.xml.schema.model.AbstractSchema;
import com.volantis.mcs.xml.schema.model.CompositeModel;
import com.volantis.mcs.xml.schema.model.ContentModel;
import com.volantis.mcs.xml.schema.model.ElementSchema;
import com.volantis.mcs.xml.schema.model.ElementType;

public class XFormsSchema
        extends AbstractSchema {

    ElementSchema group = createElementSchema(XFormElements.GROUP);
    ElementSchema input = createElementSchema(XFormElements.INPUT);
    ElementSchema instance = createElementSchema(XFormElements.INSTANCE);
    ElementSchema item = createElementSchema(XFormElements.ITEM);
    ElementSchema label = createElementSchema(XFormElements.LABEL);
    ElementSchema model = createElementSchema(XFormElements.MODEL);
    ElementSchema secret = createElementSchema(XFormElements.SECRET);
    ElementSchema select = createElementSchema(XFormElements.SELECT);
    ElementSchema select1 = createElementSchema(XFormElements.SELECT1);
    ElementSchema setvalue = createElementSchema(XFormElements.SETVALUE);
    ElementSchema submission = createElementSchema(XFormElements.SUBMISSION);
    ElementSchema submit = createElementSchema(XFormElements.SUBMIT);
    ElementSchema textarea = createElementSchema(XFormElements.TEXTAREA);
    ElementSchema value = createElementSchema(XFormElements.VALUE);

    public CompositeModel FORM_CONTROLS = choice();

    CompositeModel UI_INLINE = choice();
    private CompositeModel INSTANCE_CONTENT = choice();

    /**
     * The content model for an input element.
     */
    public CompositeModel INPUT_CONTENT = choice();

    public XFormsSchema(XHTML2Schema xhtml2) {

        // ====================================================================
        //             Core Module
        // ====================================================================

        // Add <model> to the XHTML 2 <head> content set.
        // See http://www.w3.org/TR/2006/WD-xhtml2-20060726/mod-xforms.html#XForms_Core
        // todo Should also add it to the structural and text content sets but
        // todo that doesn't seem quite right at the moment so we will leave
        // todo it so that it is only usable within the head for the moment.
        xhtml2.HEAD_CONTENT.add(model);

        // model: (instance|submission)*
        model.setContentModel(bounded(choice().add(instance).add(submission)));

        // instance: (ANY)*
        instance.setContentModel(INSTANCE_CONTENT);

        // submission: (EMPTY)
        submission.setContentModel(EMPTY);

        // ====================================================================
        //             Controls Module
        // ====================================================================

        // Define the form controls content set.
        // See http://www.w3.org/TR/2003/REC-xforms-20031014/slice8.html#id2625797
        FORM_CONTROLS.add(input).add(secret).add(textarea).add(submit)
                .add(select).add(select1);

        CompositeModel UI_COMMON = choice();

        // Add the form controls to the XHTML 2 Structural and Text content
        // sets.
        // See http://www.w3.org/TR/2006/WD-xhtml2-20060726/mod-xforms.html#XForms_Form_Controls
        xhtml2.STRUCTURAL.add(FORM_CONTROLS);
        xhtml2.TEXT.add(FORM_CONTROLS);

        // todo this has been commented out because the code cannot deal with
        // todo this correctly. Once LabelStrategy has been changed to collect
        // todo not just text but an OutputBuffer containing text inline markup
        // todo then the following can be uncommented.
//        // Add the XHTML 2 Text content set to UI_INLINE.
//        // See http://www.w3.org/TR/2006/WD-xhtml2-20060726/mod-xforms.html#XForms_Form_Controls
//         UI_INLINE.add(xhtml2.TEXT);
//
//        // Exclude any form controls from appearing within other form elements
//        // that can contain text.
//         UI_INLINE.exclude(FORM_CONTROLS);

        // (PCDATA|UI_INLINE)*
        ContentModel MIXED_UI_INLINE = bounded(
                choice().add(PCDATA).add(UI_INLINE));

        label.setContentModel(MIXED_UI_INLINE);

        // label, (UI_COMMON)*
        CompositeModel CONTROL_MODEL =
                sequence().add(label).add(bounded(UI_COMMON));

        INPUT_CONTENT.add(UI_COMMON);

        input.setContentModel(sequence().add(label).add(bounded(INPUT_CONTENT)));
        secret.setContentModel(CONTROL_MODEL);
        CompositeModel SUBMIT_CONTENT = choice().add(setvalue).add(UI_COMMON);
        submit.setContentModel(sequence().add(label).add(bounded(SUBMIT_CONTENT)));
        textarea.setContentModel(CONTROL_MODEL);

        // (PCDATA)
        value.setContentModel(PCDATA);
        setvalue.setContentModel(PCDATA);

        // label, value, (UI_COMMON)*
        item.setContentModel(
                sequence()
                        .add(label)
                        .add(value)
                        .add(bounded(UI_COMMON)));

        CompositeModel LIST_UI_COMMON = choice().add(item);

        // label, (LIST_UI_COMMON)+, (UI_COMMON)*
        CompositeModel SELECT_MODEL =
                sequence()
                        .add(label)
                        .add(bounded(LIST_UI_COMMON).atLeastOne())
                        .add(bounded(UI_COMMON));
        select.setContentModel(SELECT_MODEL);
        select1.setContentModel(SELECT_MODEL);

        // ====================================================================
        //             Group Module
        // ====================================================================

        // (FORM_CONTROLS|group|UI_COMMON)
        CompositeModel GROUP_CHOICE =
                choice().add(FORM_CONTROLS).add(group).add(UI_COMMON);
        group.setContentModel(
                sequence()
                        // label?,
                        .add(bounded(label).optional())
                        // (GROUP_CHOICE)*
                        .add(bounded(GROUP_CHOICE))
                        .add(bounded(xhtml2.STRUCTURAL))
        );

        // Add group to the XHTML 2 Structural and Text content sets.
        // See http://www.w3.org/TR/2006/WD-xhtml2-20060726/mod-xforms.html#XForms_Group
        xhtml2.STRUCTURAL.add(group);
        xhtml2.TEXT.add(group);

        // todo See above todo
//        // Exclude any form controls from appearing within other form elements
//        // that can contain text.
//         UI_INLINE.exclude(group);

        // ====================================================================
        //             Simple Initialization Module
        // ====================================================================

        addSimpleInitializationSchema(SIElements.INSTANCE, SIElements.ITEM);
        addSimpleInitializationSchema(XDIMECPSIElements.INSTANCE,
                XDIMECPSIElements.ITEM);
        addSimpleInitializationSchema(XDIMECPInterimSIElements.INSTANCE,
                XDIMECPInterimSIElements.ITEM);
    }

    private void addSimpleInitializationSchema(
            ElementType instanceElement, ElementType itemElement) {

        ElementSchema instance = createElementSchema(instanceElement);
        ElementSchema item = createElementSchema(itemElement);
        instance.setContentModel(bounded(choice().add(item)));
        item.setContentModel(PCDATA);

        INSTANCE_CONTENT.add(instance);
    }
}

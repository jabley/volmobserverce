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
 * $Header: /src/voyager/com/volantis/mcs/protocols/XFFormFieldAttributes.java,v 1.9 2003/03/03 17:46:28 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Jun-01    Paul            VBM:2001062810 - Created.
 * 24-Jul-01    Paul            VBM:2001071103 - Added abstract doFormField
 *                              method.
 * 26-Jul-01    Paul            VBM:2001071707 - Fixed problems with resetting
 *                              the state of the object.
 * 04-Sep-01    Paul            VBM:2001081707 - Changed the types of
 *                              caption, errmsg, help, prompt and shortcut
 *                              to Object to allow TextComponentNames to be
 *                              passed through to the protocol.
 * 19-Feb-02    Paul            VBM:2001100102 - Removed doFormField method
 *                              and added fieldDescriptor attribute.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 05-Jun-02    Byron           VBM:2002053002 - Added support for tabindex by
 *                              adding getTabindex and setTabindex methods
 * 17-Jun-02    Byron           VBM:2002061001 - Changed capitalisation for
 *                              xxxxtabindex
 * 03-Mar-03    Byron           VBM:2003022813 - Modified initial member
 *                              variable to be of type Object. Modified
 *                              relevant getter and setter.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.forms.AbstractForm;
import com.volantis.mcs.protocols.forms.FieldDescriptor;
import com.volantis.mcs.protocols.layouts.ContainerInstance;
import com.volantis.styling.Styles;

/**
 * The XFForm Field attributes.
 *
 * @mock.generate base="MCSAttributes"
 */
public abstract class XFFormFieldAttributes
        extends MCSAttributes
        implements CaptionAwareXFFormAttributes {

    private boolean isEmulatedXFormFieldAttributes = false;

    private TextAssetReference caption;
    private Styles captionStyles;
    private ContainerInstance captionContainerInstance;
    private ContainerInstance entryContainerInstance;
    private TextAssetReference errmsg;
    private String tabindex;

    /**
     * The descriptor of this field.
     */
    private FieldDescriptor fieldDescriptor;

    private AbstractForm form;
    private XFFormAttributes formAttributes;
    private TextAssetReference help;
    private TextAssetReference initial;
    private String name;
    private TextAssetReference prompt;
    private TextAssetReference shortcut;
    private String containingXFFormName;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    protected XFFormFieldAttributes() {
        initialise();
    }

    /**
     * This method should reset the state of this object back to its
     * state immediately after it was constructed.
     */
    public void resetAttributes() {
        super.resetAttributes();

        // Call this after calling super.resetAttributes to allow initialise to
        // override any inherited attributes.
        initialise();
    }

    /**
     * Initialise all the data members. This is called from the constructor
     * and also from resetAttributes.
     */
    private void initialise() {
        caption = null;
        captionStyles = null;
        captionContainerInstance = null;
        entryContainerInstance = null;
        errmsg = null;
        fieldDescriptor = null;
        form = null;
        formAttributes = null;
        help = null;
        initial = null;
        name = null;
        prompt = null;
        shortcut = null;
        tabindex = null;
        containingXFFormName = null;
    }

    // Javadoc inherited.
    public void setCaption(TextAssetReference caption) {
        this.caption = caption;
    }

    // Javadoc inherited.
    public TextAssetReference getCaption() {
        return caption;
    }

    // Javadoc inherited.
    public Styles getCaptionStyles() {
        return captionStyles;
    }

    // Javadoc inherited.
    public void setCaptionStyles(Styles captionStyles) {
        this.captionStyles = captionStyles;
    }

    // Javadoc inherited.
    public void setCaptionContainerInstance(
            ContainerInstance captionContainerInstance) {
        this.captionContainerInstance = captionContainerInstance;
    }

    // Javadoc inherited.
    public ContainerInstance getCaptionContainerInstance() {
        return captionContainerInstance;
    }

    // Javadoc inherited.
    public void setEntryContainerInstance(
            ContainerInstance entryContainerInstance) {
        this.entryContainerInstance = entryContainerInstance;
    }

    // Javadoc inherited.
    public ContainerInstance getEntryContainerInstance() {
        return entryContainerInstance;
    }

    /**
     * Set the value of the errmsg property.
     *
     * @param errmsg The new value of the errmsg property.
     */
    public void setErrmsg(TextAssetReference errmsg) {
        this.errmsg = errmsg;
    }

    /**
     * Get the value of the errmsg property.
     *
     * @return The value of the errmsg property.
     */
    public TextAssetReference getErrmsg() {
        return errmsg;
    }

    /**
     * Set the value of the field descriptor property.
     *
     * @param fieldDescriptor The new value of the field descriptor property.
     */
    public void setFieldDescriptor(FieldDescriptor fieldDescriptor) {
        this.fieldDescriptor = fieldDescriptor;
    }

    /**
     * Get the value of the field descriptor property.
     *
     * @return The value of the field descriptor property.
     */
    public FieldDescriptor getFieldDescriptor() {
        return fieldDescriptor;
    }

    /**
     * Set the value of the form property.
     *
     * @param form The new value of the form property.
     */
    public void setFormData(AbstractForm form) {
        this.form = form;
    }

    /**
     * Get the value of the form property.
     *
     * @return The value of the form property.
     */
    public AbstractForm getFormData() {
        return form;
    }

    /**
     * Set the value of the formAttributes property.
     *
     * @param formAttributes The new value of the formAttributes property.
     */
    public void setFormAttributes(XFFormAttributes formAttributes) {
        this.formAttributes = formAttributes;
    }

    /**
     * Get the value of the formAttributes property.
     *
     * @return The value of the formAttributes property.
     */
    public XFFormAttributes getFormAttributes() {
        return formAttributes;
    }

    /**
     * Set the value of the help property.
     *
     * @param help The new value of the help property.
     */
    public void setHelp(TextAssetReference help) {
        this.help = help;
    }

    /**
     * Get the value of the help property.
     *
     * @return The value of the help property.
     */
    public TextAssetReference getHelp() {
        return help;
    }

    /**
     * Set the value of the initial property.
     *
     * @param initial The new value of the initial property.
     */
    public void setInitial(TextAssetReference initial) {
        this.initial = initial;
    }

    /**
     * Set the value of the initial property.
     *
     * @param initial The new value of the initial property.
     */
    public void setInitial(String initial) {
        this.initial = new LiteralTextAssetReference(initial);
    }

    /**
     * Get the value of the initial property.
     *
     * @return The value of the initial property.
     */
    public TextAssetReference getInitial() {
        return initial;
    }

    /**
     * Set the value of the name property.
     *
     * @param name The new value of the name property.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the value of the name property.
     *
     * @return The value of the name property.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of the prompt property.
     *
     * @param prompt The new value of the prompt property.
     */
    public void setPrompt(TextAssetReference prompt) {
        this.prompt = prompt;
    }

    /**
     * Get the value of the prompt property.
     *
     * @return The value of the prompt property.
     */
    public TextAssetReference getPrompt() {
        return prompt;
    }

    /**
     * Set the value of the shortcut property.
     *
     * @param shortcut The new value of the shortcut property.
     */
    public void setShortcut(TextAssetReference shortcut) {
        this.shortcut = shortcut;
    }

    /**
     * Get the value of the shortcut property.
     *
     * @return The value of the shortcut property.
     */
    public TextAssetReference getShortcut() {
        return shortcut;
    }

    /**
     * Set the value of the tabindex property.
     *
     * @param tabindex The new value of the tabindex property.
     */
    public void setTabindex(String tabindex) {
        this.tabindex = tabindex;
    }

    /**
     * Get the value of the tabindex property.
     *
     * @return The value of the tabindex property.
     */
    public String getTabindex() {
        return tabindex;
    }

    /**
     * Retrieve the name of this xfform in which this form field appears. Only
     * applicable when converting XForm Controls into XFForms.
     *
     * @return String name of the xfform in which this form field should appear
     */
    public String getContainingXFFormName() {
        return containingXFFormName;
    }

    /**
     * Set the name of the xfform in which this form field should appear. Only
     * applicable when converting XForm Controls into XFForms.
     *
     * @param containingXFFormName name of the xfform in which this form
     *                             field should appear
     */
    public void setContainingXFFormName(String containingXFFormName) {
        this.containingXFFormName = containingXFFormName;
        // when containingXFFormName is specified
        // we know that attributes belongs to emulated xform field
        this.isEmulatedXFormFieldAttributes = true;
    }


    /**
     * check if attriubtes belongs to emulated xform field, it means
     * were created from XDIME2 input markup.
     * @return
     */
    public boolean isEulatedXFormFieldAttributes(){
        return this.isEmulatedXFormFieldAttributes;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 15-Sep-05	9524/1	emma	VBM:2005091503 Added ContainerInstance to allow regions and panes to be treated in the same way

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Nov-04	6135/1	byron	VBM:2004081726 Allow spatial format iterators within forms

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 ===========================================================================
*/

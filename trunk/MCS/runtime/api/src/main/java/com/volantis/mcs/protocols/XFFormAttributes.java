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
 * $Header: /src/voyager/com/volantis/mcs/protocols/XFFormAttributes.java,v 1.9 2002/03/18 12:41:18 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Jun-01    Paul            VBM:2001062810 - Created.
 * 24-Jul-01    Paul            VBM:2001071103 - Replaced the list of field
 *                              names with a list of fields.
 * 26-Jul-01    Paul            VBM:2001071707 - Fixed problems with resetting
 *                              the state of the object.
 * 10-Aug-01    Allan           VBM:2001081004 - Added segment property.
 * 04-Sep-01    Paul            VBM:2001081707 - Changed the types of
 *                              help and prompt to Object to allow
 *                              TextComponentNames to be passed through to the
 *                              protocol.
 * 21-Sep-01    Doug            VBM:2001090302 - Changed action's type to
 *                              Object to allow LinkComponentNames to be
 *                              passed through to the protocol.
 * 07-Dec-01    Paul            VBM:2001120703 - Remove default method value
 *                              as this needs to be handled elsewhere.
 * 19-Feb-02    Paul            VBM:2001100102 - Removed doFormField method
 *                              and added formSpecifier and formDescriptor
 *                              attributes.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.forms.AbstractForm;
import com.volantis.mcs.protocols.forms.FormDescriptor;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the attributes which are needed by the protocol methods
 * which handle the extended function form tag.
 *
 * The original FormAttributes cannot be used because it is based very closely
 * on HTML. The extended function form tags on the other hand provide a higher
 * level of abstraction and therefore has a different set of attributes. It is
 * the protocols responsibility to map those attributes to protocol specific
 * ones.
 */
public class XFFormAttributes
        extends MCSAttributes {

    private LinkAssetReference action;
    private int actionCount;
    private List fields;
    private AbstractForm form;

    /**
     * The information which describes the form.
     */
    private FormDescriptor formDescriptor;

    /**
     * The string which identifies the form descriptor within the form
     * descriptor cache.
     */
    private String formSpecifier;

    private TextAssetReference help;
    private String method;
    private String name;
    private TextAssetReference prompt;
    private String segment;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public XFFormAttributes() {
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

        // Set the default tag name, this is the name of the tag which makes
        // the most use of this class.
        setTagName("xfform");

        action = null;
        actionCount = 0;

        if (fields == null) {
            fields = new ArrayList();
        } else {
            fields.clear();
        }

        form = null;
        formDescriptor = null;
        formSpecifier = null;
        help = null;
        method = null;
        name = null;
        prompt = null;
        segment = null;
    }

    /**
     * Set the value of the action property.
     *
     * @param action The new value of the action property.
     */
    public void setAction(LinkAssetReference action) {
        this.action = action;
    }

    /**
     * Get the value of the action property.
     *
     * @return The value of the action property.
     */
    public LinkAssetReference getAction() {
        return action;
    }

    /**
     * Set the value of the actionCount property.
     *
     * @param actionCount The new value of the actionCount property.
     */
    public void setActionCount(int actionCount) {
        this.actionCount = actionCount;
    }

    /**
     * Get the value of the actionCount property.
     *
     * @return The value of the actionCount property.
     */
    public int getActionCount() {
        return actionCount;
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
     * Set the value of the form descriptor property.
     *
     * @param formDescriptor The new value of the form descriptor property.
     */
    public void setFormDescriptor(FormDescriptor formDescriptor) {
        this.formDescriptor = formDescriptor;
    }

    /**
     * Get the value of the form descriptor property.
     *
     * @return The value of the form descriptor property.
     */
    public FormDescriptor getFormDescriptor() {
        return formDescriptor;
    }

    /**
     * Set the value of the form specifier property.
     *
     * @param formSpecifier The new value of the form specifier property.
     */
    public void setFormSpecifier(String formSpecifier) {
        this.formSpecifier = formSpecifier;
    }

    /**
     * Get the value of the form specifier property.
     *
     * @return The value of the form specifier property.
     */
    public String getFormSpecifier() {
        return formSpecifier;
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
     * Set the value of the method property.
     *
     * @param method The new value of the method property.
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Get the value of the method property.
     *
     * @return The value of the method property.
     */
    public String getMethod() {
        return method;
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
     * Set the value of the segment property.
     *
     * @param segment The new value of the segment property.
     */
    public void setSegment(String segment) {
        this.segment = segment;
    }

    /**
     * Get the value of the segment property.
     *
     * @return The value of the segment property.
     */
    public String getSegment() {
        return segment;
    }

    /**
     * Add a form field to the collection of form fields.
     */
    public void addField(XFFormFieldAttributes field) {
        fields.add(field);
    }

    /**
     * Get the list of form fields.
     */
    public List getFields() {
        return fields;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Nov-04	6135/1	byron	VBM:2004081726 Allow spatial format iterators within forms

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 ===========================================================================
*/

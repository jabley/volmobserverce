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
 * $Header: /src/voyager/com/volantis/mcs/protocols/forms/FormDescriptor.java,v 1.2 2002/03/18 12:41:18 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Feb-02    Paul            VBM:2001100102 - Created to encapsulate the
 *                              information about a form.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 03-Jun-03    Allan           VBM:2003060301 - ObjectHelper moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.forms;

import com.volantis.mcs.protocols.XFFormAttributes;

/**
 * This class information extends {@link FormDescriptor} to encapsulate
 * information required to emulate XForms consistently across different
 * protocols.
 *
 * @mock.generate 
 */
public class EmulatedXFormDescriptor extends FormDescriptor{

    /**
     * String URL to which the form should be sent on submission
     */
    private String formURL;

    /**
     * String representing the method that should be used when submitting.
     * Either POST or GET (defaulting to GET).
     */
    private String formMethod;

    /**
     * The String identifier of the form in the session context.
     */
    private String formSpecifier;

    /**
     * The value of the "containingFormName" attribute that will be set on any
     * emulated XForm elements.
     */
    private String containingFormName;

    private XFFormAttributes formAttributes;

    /**
     * Initialize a new instance.
     */
    public EmulatedXFormDescriptor() {}

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param formURL       to which the form data should be sent on submission
     * @param formMethod    the HTTP method which should be used - GET or POST
     * @param formSpecifier the identifier of the form in the session context.
     */
    public EmulatedXFormDescriptor(String formURL, String formMethod,
            String formSpecifier) {
        this.formURL = formURL;
        this.formMethod = formMethod;
        this.formSpecifier = formSpecifier;
    }

    /**
     * Retrieve the URL to which the form data should be sent on submission.
     *
     * @return String URL
     */
    public String getFormURL() {
        return formURL;
    }

    /**
     * Retrieve the HTTP method which should be used when submitting (GET or
     * POST).
     *
     * @return String method to use when submitting
     */
    public String getFormMethod() {
        return formMethod;
    }

    /**
     * Retrieve the identifier of the form in the session context.
     *
     * @return String identifier of the form in the session context.
     */
    public String getFormSpecifier() {
        return formSpecifier;
    }

    public String getContainingFormName() {
        return containingFormName;
    }

    public void setFormURL(String formURL) {
        this.formURL = formURL;
    }

    public void setFormMethod(String formMethod) {
        this.formMethod = formMethod;
    }

    public void setFormSpecifier(String formSpecifier) {
        this.formSpecifier = formSpecifier;
    }

    public void setContainingFormName(String containingFormName) {
        this.containingFormName = containingFormName;
    }

    public XFFormAttributes getFormAttributes() {
        return formAttributes;
    }

    public void setFormAttributes(XFFormAttributes formAttributes) {
        this.formAttributes = formAttributes;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 ===========================================================================
*/

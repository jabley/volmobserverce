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

import com.volantis.mcs.xdime.xforms.model.XFormMethod;

import java.net.URI;

/**
 * Represents an xforms submission element in the MCS internal xforms model.
 */
public interface XFSubmission {

    /**
     * Retrieve the method of submission. This should be one of the values in
     * the enumeration {@link com.volantis.mcs.xdime.xforms.model.XFormMethod} i.e. GET or POST.
     * @return the method of submission
     */
    XFormMethod getMethod();

    /**
     * Set the method of submission to a valid {@link XFormMethod}.
     *
     * @param method value to which to set the method of submission
     */
    void setMethod(XFormMethod method);

    /**
     * Return the URI that this submission should be sent to.
     *
     * @return the URI that this submission should be sent to.
     */
    URI getAction();

    /**
     * Set the URI that this submission should be sent to.
     *
     * @param action URI that this submission should be sent to.
     */
    void setAction(URI action);

    /**
     * Retrieves the identifier of this submission. Must be unique in the
     * instance that it is defined in.
     *
     * @return the String identifier of this submission
     */
    String getId();

    /**
     * Set the identifier of this submission.
     *
     * @param id the identifier of this submission
     */
    void setId(String id);

    /**
     * Get the identifier of the model in which this submission is defined.
     *
     * @return the ID of the model in which this submission was defined.
     */
    String getEnclosingModelID();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 ===========================================================================
*/

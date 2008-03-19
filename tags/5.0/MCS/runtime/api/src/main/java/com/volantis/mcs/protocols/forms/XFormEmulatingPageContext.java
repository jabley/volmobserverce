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
package com.volantis.mcs.protocols.forms;

/**
 * Defines the methods required by a context which supports XForm emulation.
 */
public interface XFormEmulatingPageContext {

    /**
     * Return the {@link EmulatedXFormDescriptor} instance which has been
     * stored in the session for the form of the specified name. May be null if
     * this form has not been stored in the session.
     *
     * @param containingXFFormName    for which to retrieve a form descriptor
     * @return EmulatedXFormDescriptor which maps to the specified form name.
     * May be null if this form has not been stored in the session.
     */
    EmulatedXFormDescriptor getEmulatedXFormDescriptor(String containingXFFormName);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 ===========================================================================
*/

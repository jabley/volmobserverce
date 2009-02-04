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
package com.volantis.mcs.xdime;


/**
 * Type safe enumeration used by {@link XDIMEElement}s.
 */
public final class XDIMEResult {

    /**
     * Returned by {@link XDIMEElement#exprElementStart} if the caller should
     * process the element's body (contents).
     */
    public static final XDIMEResult PROCESS_ELEMENT_BODY =
            new XDIMEResult("PROCESS_ELEMENT_BODY");

    /**
     * Returned by {@link XDIMEElement#exprElementStart} if the caller should
     * <strong>NOT</strong> process the element's body (contents).
     */
    public static final XDIMEResult SKIP_ELEMENT_BODY =
            new XDIMEResult("SKIP_ELEMENT_BODY");

    /**
     * Returned by {@link XDIMEElement#exprElementEnd} if the caller should
     * continue processing the rest of the page (document).
     */
    public static final XDIMEResult CONTINUE_PROCESSING =
            new XDIMEResult("CONTINUE_PROCESSING");

    /**
     * Returned by {@link XDIMEElement#exprElementEnd} if the caller should
     * abort the processing the rest of the page (document).
     */
    public static final XDIMEResult ABORT_PROCESSING =
            new XDIMEResult("ABORT_PROCESSING");

    /**
     * The name of this constant.
     */
    private String name;

    /**
     * Private Class to avoid instantiation.
     */
    private XDIMEResult(String name) {
        this.name = name;
    }

    // Javadoc inherited.
    public String toString() {
        return name;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Sep-05	9415/1	emma	VBM:2005072710 Add mappings for DISelect Set XPath Functions

 25-Jul-05	9060/1	tom	VBM:2005071304 Interim Commit so Emma can see the changes we have made

 ===========================================================================
*/

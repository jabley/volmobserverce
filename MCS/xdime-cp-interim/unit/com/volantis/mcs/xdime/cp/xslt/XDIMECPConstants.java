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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xdime.cp.xslt;

public interface XDIMECPConstants {

    public static final String XFORMS_NAMESPACE = "http://www.w3.org/2002/xforms";
    public static final String XHTML2_NAMESPACE = "http://www.w3.org/2002/06/xhtml2";
    public static final String XSLT_NAMESPACE = "http://www.w3.org/1999/XSL/Transform";
    public static final String DISELECT_NAMESPACE = "http://www.w3.org/2004/06/diselect";
    public static final String XDIMECP_MCS_NAMESPACE = "http://www.volantis.com/xmlns/2004/06/xdimecp/mcs";
    public static final String MARLIN_CDM_NAMESPACE = "http://www.volantis.com/xmlns/marlin-cdm";
    public static final String CFG_NAMESPACE = "http://www.volantis.com/xmlns/2004/06/xdimecp/interim/cfg";
    public static final String SI_NAMESPACE = "http://www.volantis.com/xmlns/2004/06/xdimecp/interim/si";

    public static final String XFORMS_XMLNS = "xmlns:xf=\"" + XFORMS_NAMESPACE + "\"";
    public static final String XHTML2_XMLNS = "xmlns:xh2=\"" + XHTML2_NAMESPACE + "\"";
    public static final String XHTML2_DEFAULT_XMLNS = "xmlns=\"" + XHTML2_NAMESPACE + "\"";
    public static final String XSLT_XMLNS = "xmlns:xsl=\"" + XSLT_NAMESPACE + "\"";
    public static final String DISELECT_XMLNS = "xmlns:sel=\"" + DISELECT_NAMESPACE + "\"";
    public static final String XDIMECP_MCS_XMLNS = "xmlns:mcs=\"" + XDIMECP_MCS_NAMESPACE + "\"";
    public static final String MARLIN_CDM_XMLNS = "xmlns=\"" + MARLIN_CDM_NAMESPACE + "\"";
    public static final String CFG_XMLNS = "xmlns:cfg=\"" + CFG_NAMESPACE + "\"";
    public static final String SI_XMLNS = "xmlns:si=\"" + SI_NAMESPACE + "\"";
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Sep-04	5380/16	pcameron	VBM:2004090204 Fixed xfmuselect initialisation and reverted to interim si namespace

 27-Aug-04	5329/1	pcameron	VBM:2004082409 Added XForms initialisation to XDIME-CP Interim

 27-Aug-04	5310/1	pcameron	VBM:2004082409 Added XForms initialisation to XDIME-CP Interim

 21-Jun-04	4645/3	pcameron	VBM:2004060306 Committed for integration

 15-Jun-04	4645/1	pcameron	VBM:2004060306 Fixed test cases after integration

 08-Jun-04	4630/1	pduffin	VBM:2004060306 Added some constants

 ===========================================================================
*/

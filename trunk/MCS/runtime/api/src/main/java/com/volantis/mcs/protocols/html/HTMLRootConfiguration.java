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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/HTMLRootConfiguration.java,v 1.1 2002/05/23 09:49:21 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 23-May-02    Paul            VBM:2002042202 - Created to contain HTMLRoot
 *                              configuration.
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.devices.InternalDevice;

/**
 * This class extends the default dom outputter and does some WML specific
 * things. Actually they are probably more general than that but only wml
 * needs them now.
 */
public class HTMLRootConfiguration
        extends XHTMLFullConfiguration {

    /**
     * Initialise.
     */
    public HTMLRootConfiguration() {
        this(false);
    }

    /**
     * Initialise.
     *
     * @param supportsDissection Specifies whether the protocol supports
     *                           dissection.
     */
    protected HTMLRootConfiguration(boolean supportsDissection) {
        super(supportsDissection);

        emptyElementRequiresSpace = true;
    }

    /**
     * Override to force the style element to be treated as having a CDATA
     * content model.
     */
    protected String getXElementStyleContent(InternalDevice device) {
        return "CDATA";
    }

    /**
     * Override to force the script element to be treated as having a CDATA
     * content model.
     */
    protected String getXElementScriptContent(InternalDevice device) {
        return "CDATA";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 23-Jun-05	8833/1	pduffin	VBM:2005042901 Addressing review comments

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/

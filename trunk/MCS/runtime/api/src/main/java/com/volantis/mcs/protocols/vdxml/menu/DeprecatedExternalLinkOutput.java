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
package com.volantis.mcs.protocols.vdxml.menu;

import com.volantis.mcs.protocols.ProtocolException;

/**
 * An interface to allow the output of VDXML links (RACCOURCI) which are
 * external to the markup created for menu items. This renders the active part
 * of the link, the text is rendered separately in the normal place.
 * <p>
 * <strong>
 * Note that this is deprecated because we will hopefully be replacing this
 * simple "existing protocol api callback" with a properly designed system for
 * rendering protocol elements outside of the protocol classes in future.
 * </strong>
 */
public interface DeprecatedExternalLinkOutput {

    /**
     * Render the special RACCOURCI link with the shortcut and href provided.
     * <p>
     * Note that the output buffer for these links is managed by the protocol.
     *
     * @param shortcut  The shortcut for the option that the user will need
     *      to enter to activate the link in combination with a function key.
     *      May not be null.
     * @param href     The url that the link represents and that will be
     *      requested if the link is activated. May not be null.
     */
    public void outputExternalLink(String shortcut, String href)
            throws ProtocolException;

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Oct-04	5635/3	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 ===========================================================================
*/

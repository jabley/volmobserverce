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
 * A test implementation of {@link DeprecatedExternalLinkOutput} which writes
 * out the external link as simply as possible.
 */
public class TestDeprecatedExternalLinkOutput
        implements DeprecatedExternalLinkOutput {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * The buffer we store the output in.
     */
    private StringBuffer buffer = new StringBuffer();

    // Javadoc inherited.
    public void outputExternalLink(String shortcut, String href)
            throws ProtocolException {

        if (shortcut == null || href == null) {
            throw new IllegalStateException();
        }

        buffer.append(
                "<external-link " +
                    "href=\"" + href + "\" " +
                    "shortcut=\"" + shortcut + "\"" +
                "/>"
        );
    }

    /**
     * Returns the results of calling {@link #outputExternalLink}.
     */
    public String getResult() {

        return buffer.toString();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Oct-04	5635/2	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 ===========================================================================
*/

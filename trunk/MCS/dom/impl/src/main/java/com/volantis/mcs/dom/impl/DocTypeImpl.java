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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom.impl;

import com.volantis.mcs.dom.DocType;
import com.volantis.mcs.dom.MarkupFamily;

public class DocTypeImpl
        implements DocType {

    private final String publicId;
    private final String systemId;
    private final String internalDTD;
    private final MarkupFamily markupFamily;
    private final String root;

    public DocTypeImpl(
            String root, String publicId, String systemId, String internalDTD,
            MarkupFamily markupFamily) {

        if (root == null) {
            throw new IllegalArgumentException("root cannot be null");
        }
        
        if (publicId == null && systemId == null && internalDTD == null) {
            throw new IllegalArgumentException(
                    "At least one of public id, system id and internal " +
                            "DTD must be set");
        }

        if (markupFamily == null) {
            throw new IllegalArgumentException("markupFamily cannot be null");
        }

        this.root = root;
        this.publicId = publicId;
        this.systemId = systemId;
        this.internalDTD = internalDTD;
        this.markupFamily = markupFamily;
    }

    // javadoc inherited
    public String getRoot() {
        return root;
    }

    // javadoc inherited
    public String getPublicId() {
        return publicId;
    }

    // javadoc inherited
    public String getSystemId() {
        return systemId;
    }

    // javadoc inherited
    public String getInternalDTD() {
        return internalDTD;
    }

    // javadoc inherited
    public MarkupFamily getMarkupFamily() {
        return markupFamily;
    }

    // javadoc inherited
    public String getAsString() {
        final StringBuffer buffer = new StringBuffer();
        buffer.append("<!DOCTYPE ");
        buffer.append(root);
        buffer.append(" ");
        if (publicId != null) {
            buffer.append("PUBLIC ");
        } else if (systemId != null) {
            buffer.append("SYSTEM ");
        }
        String separator = "";
        if (publicId != null) {
            buffer.append('"');
            buffer.append(publicId);
            buffer.append('"');
            separator = " ";
        }
        if (systemId != null) {
            buffer.append(separator);
            buffer.append("\"");
            buffer.append(systemId);
            buffer.append('"');
        }
        if (internalDTD != null) {
            buffer.append(separator);
            buffer.append("[\n");
            buffer.append(internalDTD);
            buffer.append("\n]");
        }
        buffer.append(">");
        return buffer.toString();
    }
}

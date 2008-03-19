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

package com.volantis.mcs.policies.variants.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EncodingBuilder {

    private final String name;
    private final List mimeTypes;
    private final List extensions;

    public EncodingBuilder(String name) {
        this.name = name;
        mimeTypes = new ArrayList();
        extensions = new ArrayList();
    }

    public void addMimeType(String mimeType) {
        mimeTypes.add(mimeType);
    }

    public void addExtension(String extension) {
        extensions.add(extension);
    }

    public Encoding getEncoding() {
        List mimeTypes = Collections.unmodifiableList(this.mimeTypes);
        List extensions = Collections.unmodifiableList(this.extensions);
        Encoding encoding = new EncodingImpl(name, mimeTypes, extensions);
        return encoding;
    }
}

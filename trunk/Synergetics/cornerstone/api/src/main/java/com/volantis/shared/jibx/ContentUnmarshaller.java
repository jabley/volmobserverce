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
package com.volantis.shared.jibx;

import com.volantis.shared.content.ContentInput;
import com.volantis.shared.content.ContentStyle;
import org.jibx.runtime.JiBXException;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IUnmarshallingContext;

import java.io.IOException;

public class ContentUnmarshaller {

    private Class expectedClass;

    public ContentUnmarshaller(Class expectedClass) {
        this.expectedClass = expectedClass;
    }

    public Object unmarshallContent(ContentInput content, String name)
            throws JiBXException, IOException {

        Object readObject = null;

        IBindingFactory bfact = BindingDirectory.getFactory(expectedClass);
        IUnmarshallingContext uctx = bfact.createUnmarshallingContext();

        ContentStyle contentStyle = content.getContentStyle();
        if (contentStyle == ContentStyle.BINARY) {
            readObject = uctx.unmarshalDocument(content.getInputStream(), name,
                    null); // null encoding means auto-detect
        } else if (contentStyle == ContentStyle.TEXT) {
            readObject = uctx.unmarshalDocument(content.getReader(), name);
        } else {
            throw new IllegalStateException("No content available.");
        }

        return readObject;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Dec-05	10738/1	geoff	VBM:2005120813 JiBX is reading XML using system default encoding

 ===========================================================================
*/

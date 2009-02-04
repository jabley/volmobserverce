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
package com.volantis.mcs.eclipse.builder.editors.common;

import org.eclipse.jface.viewers.LabelProvider;
import com.volantis.mcs.policies.variants.metadata.Encoding;
import com.volantis.mcs.eclipse.builder.editors.EditorMessages;
import com.volantis.mcs.utilities.StringUtils;

/**
 * A label provider for encodings that uses the name of the encoding to access
 * an internationalised version of the name.
 */
public class EncodingLabelProvider extends LabelProvider {
    // Javadoc inherited
    public String getText(Object o) {
        Encoding encoding = (Encoding) o;

        String encodingName = encoding.getName().replaceAll(" ", "");
        return EditorMessages.getString(
                "Encoding." + StringUtils.toLowerIgnoreLocale(encodingName) +
                ".label");
    }
}

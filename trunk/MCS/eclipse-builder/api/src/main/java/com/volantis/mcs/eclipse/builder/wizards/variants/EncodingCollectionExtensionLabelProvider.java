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

package com.volantis.mcs.eclipse.builder.wizards.variants;

import com.volantis.mcs.eclipse.builder.editors.EditorMessages;
import com.volantis.mcs.eclipse.builder.wizards.WizardMessages;
import com.volantis.mcs.policies.variants.metadata.Encoding;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;
import com.volantis.mcs.utilities.StringUtils;
import org.eclipse.jface.viewers.LabelProvider;

import java.text.MessageFormat;
import java.util.Iterator;

/**
 * Label provider for encodings and encoding collections, including a list
 * of extensions for encodings.
 */
public class EncodingCollectionExtensionLabelProvider extends LabelProvider {
    /**
     * The message format for specifying the encoding type and the list of
     * extensions.
     */
    private static final String extensionListFormat = WizardMessages.getString(
            "EncodingCollectionExtensionLabelProvider.extensionList.format");

    /**
     * The separator to use in lists of extensions.
     */
    private static final String extensionSeparator = WizardMessages.getString(
            "EncodingCollectionExtensionLabelProvider.extensionList.separator");

    /**
     * The type of encoding (image, audio etc.)
     */
    private String encodingType;

    /**
     * Create a label provider for a specified encoding type.
     * @param encodingType
     */
    public EncodingCollectionExtensionLabelProvider(String encodingType) {
        this.encodingType = encodingType;
    }

    // Javadoc inherited
    public String getText(Object o) {
        String label = "";
        if (o instanceof EncodingCollection) {
            label = EditorMessages.getString("Encoding." + encodingType + ".allEncodings");
        } else {
            Encoding encoding = (Encoding) o;

            String encodingName = encoding.getName().replaceAll(" ", "");
            String encodingType = EditorMessages.getString(
                    "Encoding." +
                    StringUtils.toLowerIgnoreLocale(encodingName) +
                    ".label");
            StringBuffer encodingContents = new StringBuffer();
            Iterator it = encoding.extensions();
            while (it.hasNext()) {
                encodingContents.append("*.");
                encodingContents.append(it.next());
                if (it.hasNext()) {
                    encodingContents.append(extensionSeparator);
                }
            }
            label = MessageFormat.format(extensionListFormat, new Object[] { encodingType, encodingContents });
        }
        return label;
    }
}

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

import com.volantis.mcs.policies.variants.metadata.Encoding;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import java.io.File;
import java.util.Iterator;

/**
 * Viewer filter that filters out files that do not match any of the encodings
 * within a given collection.
 *
 * <p>This implementation only matches on extension.</p>
 */
public class EncodingCollectionViewerFilter extends ViewerFilter {
    private EncodingCollection encodingCollection;

    /**
     * Constructs a viewer filter that matches the file extensions of the
     * specified encodingCollection.
     *
     * @param encoding The encodingCollection to match
     */
    public EncodingCollectionViewerFilter(EncodingCollection encoding) {
        this.encodingCollection = encoding;
    }

    // Javadoc inherited
    public boolean select(Viewer viewer, Object o, Object o1) {
        boolean valid = false;
        if (o1 instanceof File) {
            File file = (File) o1;
            String fileName = file.getName();
            Iterator encodings = encodingCollection.iterator();
            valid = file.isDirectory();
            while (!valid && encodings.hasNext()) {
                Encoding encoding = (Encoding) encodings.next();
                Iterator extensions = encoding.extensions();
                while (!valid && extensions.hasNext()) {
                    String extension = (String) extensions.next();
                    if (fileName.length() > extension.length() &&
                        fileName.charAt(fileName.length() -
                            extension.length() - 1) == '.' &&
                        fileName.regionMatches(true, fileName.length() -
                            extension.length(), extension, 0, extension.length())) {
                        valid = true;
                    }
                }
            }
        }
        return valid;
    }
}

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
package com.volantis.mcs.eclipse.builder.editors.themes;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class provides a label and image for each {@link StyleCategory} node
 * in a tree. The implementation of the {@link #getImage} and {@link #getText}
 * methods should use a ResourceBundle to look up both the image and text keyed
 * on the String returned from invoking the StyleCategory node's getName
 * method. The implentation of the {@link #dispose} method must dispose of
 * all {@link org.eclipse.swt.graphics.Image} objects that the {@link #getImage} method returns. This
 * will require the Images to be cached.
 */
public class StyleCategoriesLabelProvider extends LabelProvider {

    /*
     * The top-level resource prefix for this class
     */
    private static final String RESOURCE_PREFIX =
        "StyleCategoriesLabelProvider.";

    /*
     * The label resource prefix for this class
     */
    private static final String LABEL_PREFIX = RESOURCE_PREFIX + "label.";

    /*
     * The image resource prefix for this class
     */
    private static final String IMAGE_PREFIX = RESOURCE_PREFIX + "image.";

    /*
     * The image cache: a category name to image map
     */
    private final Map categoryImageMap;

    /*
     * Constructor
     */
    public StyleCategoriesLabelProvider() {
        categoryImageMap = new HashMap();
    }

    // javadoc inherited
    public Image getImage(Object element) {

        // Get the name of the StyleCategory
        final String categoryName = ((StyleCategory)element).getName();

        // If we already have it in the map, just return it; else read it
        // in and put it in the map
        Image image = (Image)categoryImageMap.get(categoryName);
        if (image == null) {
            image = ThemesMessages.getImage(IMAGE_PREFIX + categoryName);

            // Returned image may be null (if so a dialogue will have been
            // displayed); if not null, put it in the map
            if (image != null) {
                categoryImageMap.put(categoryName, image);
            }
        }
        return image;
    }

    // javadoc inherited
    public String getText(Object element) {

        // Get the name of the StyleCategory
        final String categoryName = ((StyleCategory)element).getName();

        // Just return the looked-up value
        return ThemesMessages.getString(LABEL_PREFIX + categoryName);
    }

    // javadoc inherited
    public void dispose() {

        // Dispose of all the images
        Iterator imageIter = categoryImageMap.values().iterator();
        while (imageIter.hasNext()) {
            Image image = (Image) imageIter.next();
            image.dispose();
        }
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9886/2	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 28-Oct-05	9886/1	adrianj	VBM:2005101811 New theme GUI

 ===========================================================================
*/

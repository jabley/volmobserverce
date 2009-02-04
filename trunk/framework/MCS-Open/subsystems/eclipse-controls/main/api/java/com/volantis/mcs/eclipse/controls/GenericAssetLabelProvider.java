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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.controls;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Provides the labels for a filesystem content provider.
 */
public class GenericAssetLabelProvider extends LabelProvider {

    /**
     * An image cache used for caching and disposal of label images.
     */
    private static final HashMap imageCache = new HashMap();


    // javadoc inherited
    public Image getImage(Object node) {
        Image image = null;
        String key = ""; //$NON-NLS-1$
        if (node instanceof File) {
            File file = (File) node;
            if (file.isDirectory()) {
                key = "GenericAssetCreationPage.images.folder.closed"; //$NON-NLS-1$
            } else {
                key = "GenericAssetCreationPage.images.files.all"; //$NON-NLS-1$
            }
        }
        image = (Image) imageCache.get(key);
        if (image == null) {
            image = ControlsMessages.getImage(key);
            if (image != null) {
                imageCache.put(key, image);
            }
        }
        return image;
    }


    /**
     * Returns the name of the File object represented by the node.
     * @param node The File object.
     * @return The file name.
     */
    public String getText(Object node) {
        String text = ""; //$NON-NLS-1$
        if (node instanceof File) {
            text = ((File) node).getName();
        }
        return text;
    }

    /**
     * Disposes of the images in the image cache.
     */
    public void dispose() {
        super.dispose();
        for (Iterator it = imageCache.values().iterator(); it.hasNext();) {
            Image image = (Image) it.next();
            if (image != null) {
                image.dispose();
            }
        }
        imageCache.clear();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 15-Nov-03	1835/16	pcameron	VBM:2003102801 Some tweaks to GenericAssetCreationPage, and FileFilter

 14-Nov-03	1835/13	pcameron	VBM:2003102801 Added GenericAssetCreation wizard page and supporting resources

 ===========================================================================
*/

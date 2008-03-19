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
package com.volantis.mcs.eclipse.controls.images;

import org.eclipse.swt.graphics.ImageData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * The default implementation of an ImageProvider that acts as the
 * image provider for all images currently supported by SWT.
 */
public class DefaultImageProvider implements ImageProvider {

    /**
     * Returns an SWT ImageData representation of the image data in the file
     * for the supported image types in SWT.
     * @param file The file from which to read the image data.
     * @return An SWT ImageData representation of the image data.
     * @exception IOException if there are file related I/O errors
     * @exception org.eclipse.swt.SWTException if the image file contains
     * invalid data or an I/O error occurs while reading data
     */
    public ImageData provideImage(File file) throws IOException {
        FileInputStream fis = null;
        ImageData imageData = null;
        try {
            fis = new FileInputStream(file);
            imageData = new ImageData(fis);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return imageData;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-Nov-03	1795/12	pcameron	VBM:2003102804 Tweaked ImageProviders

 07-Nov-03	1795/10	pcameron	VBM:2003102804 Changed the ImageProvider exception processing

 06-Nov-03	1795/6	pcameron	VBM:2003102804 Added ImageProvider infrastructure and implementation

 04-Nov-03	1795/1	pcameron	VBM:2003102804 Committed to allow related work

 ===========================================================================
*/

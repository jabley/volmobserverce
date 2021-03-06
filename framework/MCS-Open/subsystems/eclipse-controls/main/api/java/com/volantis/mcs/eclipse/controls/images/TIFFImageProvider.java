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
import java.io.IOException;

/**
 * Class to load a Tagged Image File Format image and return it as an
 * SWT Image.
 */
public class TIFFImageProvider implements ImageProvider {

    /**
     * Returns an SWT ImageData representation of the TIFF image data
     * in the file.
     * @param file The file from which to read the image data.
     * @return An SWT ImageData representation of the image data
     * @exception java.io.FileNotFoundException if the file does not exist
     * @exception java.io.IOException if there are file related I/O errors
     * @exception IllegalArgumentException if the image file contains
     * invalid TIFF data
     */
    public ImageData provideImage(File file) throws IOException {
        TIFFLoader loader = new TIFFLoader();
        ImageData imageData = loader.load(file);
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

 07-Nov-03	1795/1	pcameron	VBM:2003102804 Changed the ImageProvider exception processing

 06-Nov-03	1795/1	pcameron	VBM:2003102804 Added ImageProvider infrastructure and implementation

 05-Nov-03	1801/1	steve	VBM:2003102804 TIFF Image Loader

 ===========================================================================
*/

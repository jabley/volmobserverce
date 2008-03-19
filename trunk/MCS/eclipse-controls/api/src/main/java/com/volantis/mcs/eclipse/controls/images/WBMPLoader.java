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

import com.sun.media.jai.codec.FileSeekableStream;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

/**
 * Load a WBMP image using JAI and return it as SWT ImageData
 */
public class WBMPLoader extends ImageLoader {
    private static final PaletteData monoPalette =
            new PaletteData(new RGB[]{new RGB(0, 0, 0), new RGB(255, 255, 255)});

    /**
     * Load a WBMP image from a file and return it as SWT image data.
     * @param file the <code>File</code> containing the image
     * @return an SWT <code>ImageData</code> version of the image data
     */
    public ImageData[] load(File file) throws IOException {
        WBMPImage wbmpImage = new WBMPImage(new FileSeekableStream(file));
        Raster rast = wbmpImage.getData();
        int width = rast.getWidth();
        int height = rast.getHeight();
        int[] pixels = new int[width * height];
        rast.getSamples(0, 0, width, height, 0, pixels);
        ImageData imageData = new ImageData(width, height, 1, monoPalette);
        imageData.setPixels(0, 0, width * height, pixels, 0);
        return new ImageData[]{imageData};
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Nov-03	1795/11	pcameron	VBM:2003102804 Added ImageProvider infrastructure and implementation

 04-Nov-03	1795/5	pcameron	VBM:2003102804 Committed to allow related work

 ===========================================================================
*/

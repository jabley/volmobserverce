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
 * $Header: /src/voyager/com/volantis/mcs/gui/repository/components/WBMPImage.java,v 1.2 2002/03/18 12:41:15 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 10-Aug-01    Paul            VBM:2001072505 - Created.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.eclipse.controls.images;

import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.SeekableStream;

import javax.media.jai.PlanarImage;
import java.awt.*;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.io.IOException;

public class WBMPImage
        extends PlanarImage {

    private SeekableStream input;
    private Raster tile;

    public WBMPImage(SeekableStream input)
            throws IOException {

        this.input = input;

        // Read the first two bytes which should both be zero.
        if (input.read() != 0 || input.read() != 0) {
            throw new IOException("Invalid WBMP header"); //$NON-NLS-1$
        }

        // The next two bytes are the width and height respectively.
        if ((width = readMultiByteInteger()) == -1) {
            throw new IOException("Invalid WBMP header: width"); //$NON-NLS-1$
        }
        if ((height = readMultiByteInteger()) == -1) {
            throw new IOException("Invalid WBMP header: height"); //$NON-NLS-1$
        }

        // Create the sample model, this is one bit per pixel with 8 pixels
        // per byte.
        sampleModel = new MultiPixelPackedSampleModel(DataBuffer.TYPE_BYTE,
                width, height, 1);

        // Create the color model.
        colorModel = ImageCodec.createGrayIndexColorModel(sampleModel, true);

        // WBMP only contains one tile.
        tileWidth = width;
        tileHeight = height;
    }

    protected int readMultiByteInteger()
            throws IOException {

        return WBMPImageCodec.readMultiByteInteger(input);
    }

    protected Raster readTile() {

        // Calculate the number of bytes needed to hold a single row and the
        // number of bytes needed to hold the whole image.
        int rowBytes = (width + 7) / 8;
        int size = rowBytes * height;

        // Create an array to hold the whole image.
        byte[] data = new byte[size];

        // Read in the image.
        try {
            if (input.read(data) != size) {
                throw new RuntimeException("Short read"); //$NON-NLS-1$
            }
        } catch (IOException ioe) {
            throw new RuntimeException("IOException: " + ioe); //$NON-NLS-1$
        }

        // Create the data buffer.
        DataBuffer buffer = new DataBufferByte(data, size);

        // Create the tile.
        Point origin = new Point(tileXToX(0), tileYToY(0));
        tile = Raster.createWritableRaster(sampleModel, buffer, origin);

        return tile;
    }

    public Raster getTile(int tileX, int tileY) {
        if (tileX != 0 || tileY != 0) {
            throw new IllegalArgumentException("Illegal tile (" //$NON-NLS-1$
                    + tileX + "," + tileY //$NON-NLS-1$
                    + ") requested"); //$NON-NLS-1$
        }

        if (tile == null) {
            tile = readTile();
        }

        return tile;
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

 07-Nov-03	1795/11	pcameron	VBM:2003102804 Corrected package of WBMPImage

 07-Nov-03	1795/8	pcameron	VBM:2003102804 Tweaked ImageProviders

 ===========================================================================
*/

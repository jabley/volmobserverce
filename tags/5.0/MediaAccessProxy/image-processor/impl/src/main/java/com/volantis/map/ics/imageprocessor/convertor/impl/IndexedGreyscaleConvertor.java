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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.map.ics.imageprocessor.convertor.impl;

import com.volantis.map.ics.imageprocessor.convertor.ImageConvertor;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertorException;
import com.volantis.map.common.param.Parameters;

import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

/**
 * Convert an image to indexed 256 level greyscale.
 */
public class IndexedGreyscaleConvertor extends GreyscaleConvertor
    implements ImageConvertor {

    /**
     * Used for transforming color model.
     */
    private RenderingHints rh = null;

    /**
     * Convert an image to 256 level greyscale.
     *
     * @param src the image to convert.
     * @return a 256 level greyscale version of the input image.
     */
    public RenderedOp convert(RenderedOp src, Parameters params)
        throws ImageConvertorException {

        //narrow color space to greyscale using superclass.
        RenderedOp out = super.convert(src, params);

        //create 256-level greyscale palette.
        synchronized (this) {
            if (rh == null) {
                byte r[] = new byte[256];
                byte g[] = new byte[256];
                byte b[] = new byte[256];
                //Make grayscale palette;
                for (int i = 0; i < 256; i++) {
                    r[i] = g[i] = b[i] = (byte) i;
                }

                ImageLayout il = new ImageLayout();
                ColorModel cm = new IndexColorModel(8, 256, r, g, b);
                il.setColorModel(cm);
                rh = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, il);
            }
        }

        //convert to indexed image.
        ParameterBlock formatPB = new ParameterBlock();
        formatPB.addSource(out);
        formatPB.add(DataBuffer.TYPE_BYTE);
        out = JAI.create("format", formatPB, rh);

        return out;
    }
}

/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.Â  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.Â  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.map.ics.imageprocessor.convertor.impl;

import com.volantis.map.ics.configuration.DitherMode;
import com.volantis.map.common.param.ParameterNames;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertor;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertorException;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertorFactory;
import com.volantis.map.ics.imageprocessor.convertor.ImageRule;
import com.volantis.map.ics.imageprocessor.logging.ImageLogger;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.common.param.Parameters;
import com.volantis.map.common.param.ParameterNames;
import com.volantis.synergetics.log.LogDispatcher;

import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.ColorCube;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.KernelJAI;
import javax.media.jai.LookupTableJAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.BinarizeDescriptor;
import javax.media.jai.operator.BandSelectDescriptor;

/**
 * Convert an image to an N level greyscale.
 */
public class GreyscaleNConvertor extends IndexedConvertor
    implements ImageConvertor {

    /**
     * The number of bits needed to represent the greylevels in the output
     * image.
     */
    private final int bitDepth;

    /**
     * Constructor that allows you to set the number of grey levels to be
     * produced.
     *
     * @param bitDepth the number of bits needed to represent the number of
     *                 grey levels you wish to have in your output image. (e.g.
     *                 4 bits is 16 levels).
     */
    public GreyscaleNConvertor(int bitDepth) {

        if (bitDepth > 8) {
            throw new IllegalArgumentException(
                "bitDepths greater then 8 are not supported");
        }
        this.bitDepth = bitDepth;
    }

    public int getRequiredNumBits() {
        return bitDepth;
    }

    public boolean isColor() {
        return false;
    }
}

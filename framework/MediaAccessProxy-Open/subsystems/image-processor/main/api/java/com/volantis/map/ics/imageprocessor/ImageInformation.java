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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.ics.imageprocessor;

import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.SampleModel;

import javax.media.jai.RenderedOp;

public class ImageInformation {

    /**
     * Stop this from being instanciated
     */
    private ImageInformation() {
    }


    /**
     * Convert a RenderedOp (image) into a String. Calling this method will
     * probably cause the RenderedOp/RendereableOp chain prior to the call to
     * be evaluated.
     *
     * @param image The image whose information you want converted to a String
     * @return the information about the RenderedOp.
     */
    public static String asString(RenderedOp image) {

        StringBuffer sb = new StringBuffer();
        ColorModel cModel = image.getColorModel();
        ColorSpace cSpace = cModel.getColorSpace();
        SampleModel sModel = image.getSampleModel();

        // Image attributes
        sb.append("\nImage class:\t\t\t ").append(image.getClass());
        sb.append("\nImage origin:\t\t\t ").append(image.getMinX()).append(", ").append(image.getMinY());
        sb.append("\nImage size:\t\t\t ").append(image.getWidth()).append(", ").append(image.getHeight());
        sb.append("\nNumber of bands:\t\t ").append(image.getNumBands());
        sb.append("\nNumber of tiles:\t\t ").append(image.getTileWidth()).append(", ").append(image.getTileHeight());

        // sample model attributes
        sb.append("\n\nSampleModel class:\t\t ").append(sModel.getClass());
        sb.append("\nDataType:\t\t\t ").append(sModel.getDataType());
        sb.append("\nDimensions:\t\t\t ").append(sModel.getWidth()).append(", ").append(sModel.getHeight());
        sb.append("\nSampleSize:\t\t\t ").append(ArrayUtils.asString(sModel.getSampleSize()));
        sb.append("\nTransferType:\t\t\t ").append(sModel.getTransferType());
        sb.append("\nNumber of bands:\t\t ").append(sModel.getNumBands());

        // colour model attributes
        sb.append("\n\nColorModel class:\t\t ").append(cModel.getClass());
        sb.append("\nPixel size:\t\t\t ").append(cModel.getPixelSize());
        sb.append("\nNumber of components:\t\t ").append(cModel.getNumComponents());
        sb.append("\nNumber of colour compoenents:\t ").append(cModel.getNumColorComponents());
        sb.append("\nTransparency:\t\t\t ").append(cModel.getTransparency());
        sb.append("\nHas alpha:\t\t\t ").append(cModel.hasAlpha());
        sb.append("\nComponent sizes:\t\t ").append(ArrayUtils.asString(cModel.getComponentSize()));
        sb.append("\nIs alpha premultiplied:\t\t ").append(cModel.isAlphaPremultiplied());

        // color space attributes.
        sb.append("\n\nColorSpace class:\t\t ").append(cSpace.getClass());
        sb.append("\nNumber of components:\t\t ").append(cSpace.getNumComponents());
        sb.append("\nType:\t\t\t\t ").append(cSpace.getType());
        sb.append("\nIs CS_sRGB:\t\t\t ").append(cSpace.isCS_sRGB());
        return sb.toString();
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Feb-05	311/1	rgreenall	VBM:2005012701 Resolved conflicts

 27-Jan-05	289/1	matthew	VBM:2005012617 Fix problems with transcoding gif/png with transparency and refactor test case

 24-Jan-05	276/1	matthew	VBM:2004121009 Allow transcoding of indexed images that have a transparency

 19-Jan-05	270/1	matthew	VBM:2004121009 Allow correct handling of gif images with transparency

 ===========================================================================
*/

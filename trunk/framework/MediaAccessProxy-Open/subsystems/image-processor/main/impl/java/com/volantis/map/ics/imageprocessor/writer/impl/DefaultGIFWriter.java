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

package com.volantis.map.ics.imageprocessor.writer.impl;

import com.sun.media.jai.operator.ImageWriteDescriptor;
import com.sun.imageio.plugins.gif.GIFImageMetadata;
import com.volantis.map.common.param.Parameters;
import com.volantis.map.common.streams.NoFlushSeekableOutputStream;
import com.volantis.map.ics.configuration.ImageConstants;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertor;
import com.volantis.map.ics.imageprocessor.impl.GenericCollectionImage;
import com.volantis.map.ics.imageprocessor.utilities.ImageUtils;
import com.volantis.map.ics.imageprocessor.writer.ImageWriterException;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.operation.ObjectParameters;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import javax.imageio.ImageWriteParam;
import javax.media.jai.CollectionImage;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.CompositeDescriptor;
import javax.media.jai.operator.OverlayDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;

/**
 * Output methods common to all GIF output classes.
 */
public abstract class DefaultGIFWriter extends DefaultWriter {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(DefaultGIFWriter.class);

    /**
     * Used for localizing exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(DefaultGIFWriter.class);

    protected RenderedOp[] getConvertedImage(RenderedOp[] image,
                                             Parameters params)
        throws ImageWriterException {
        return super.getConvertedImage(image, params);    // @todo implement this
    }

    // javadoc inherited
    protected NoFlushSeekableOutputStream outputImage(RenderedOp[] ops,
                                                   ObjectParameters params,
                                                   NoFlushSeekableOutputStream os)
        throws ImageWriterException {
        return outputImageSimple(ops, params, os);
    }

    // javadoc inherited
    protected long encodeImage(RenderedOp[] image, ObjectParameters params,
                               NoFlushSeekableOutputStream os)
        throws ImageWriterException, IOException {
        outputImageSimple(image, params, os);
        return os.getSize();
    }

    /**
     * Outputs image to GIF.
     *
     * @param ops    - image frames to output.
     * @param params - Parameters.
     * @param os     - output stream to write into.
     * @return output stream with GIF data.
     *
     * @throws ImageWriterException thrown if it is impossible to write image.
     */
    protected NoFlushSeekableOutputStream outputImageSimple(RenderedOp[] ops,
                                                            ObjectParameters params,
                                                            NoFlushSeekableOutputStream os)
        throws ImageWriterException {
        if (ops == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null",
                "ops");
            throw new IllegalArgumentException(msg);
        }
        if (params == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null",
                "params");
            throw new IllegalArgumentException(msg);
        }
        if (os == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null",
                "os");
            throw new IllegalArgumentException(msg);
        }

        os.mark();
        try {
            // If source wasn't animated gif we render only first frame.
            if (ops.length <2) {
                super.outputImage(new RenderedOp[]{ops[0]}, params, os);
            } else {

                List converted = new ArrayList(ops.length);
                //Collection composite = removeAlphaByCompositing(Arrays.asList(ops), 256);
                Collection composite = Arrays.asList(ops);
                GIFImageMetadata[] imageMetadata = new GIFImageMetadata[composite.size()];

                Iterator it = composite.iterator();
                int i=0;
                while (it.hasNext()) {

                    RenderedOp comp = (RenderedOp) it.next();
                    ImageConvertor cnv = getImageConverter(comp, params);
                    RenderedOp conv = cnv.convert(comp, params);

                    Object obj = conv.getProperty("JAI.ImageMetadata");

                    if (obj instanceof GIFImageMetadata) {
                        // this is a hacky mess but the writer does not obay
                        // the minX and minY coords of the image.
                        imageMetadata[i] = (GIFImageMetadata) obj;
                        imageMetadata[i].imageLeftPosition = conv.getMinX();
                        imageMetadata[i].imageTopPosition = conv.getMinY();
                    }

                    converted.add(conv);
                    i++;
                }

                ImageWriteDescriptor.createCollection(
                    converted,
                    os,
                    "gif",
                    Boolean.FALSE,
                    Boolean.FALSE,
                    Boolean.FALSE,
                    Boolean.FALSE,
                    null,
                    null,
                    imageMetadata,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);


            }
            return os;
        } catch (Exception e) {
            throw new ImageWriterException(e);
        }
    }


    /**
     * This removes the alpha channel from a collection of images by
     * compositing the first image with a white background. Subsequent images
     * are then composited with the previous images. This is quite expensive
     * but is the only reasonable solution for low bitdepth images (such
     * as cg4 or cg2 where a transparency pixel cannot be afforded
     *
     * @param images the images to composit
     * @return
     */
    public Collection removeAlphaByCompositing(Collection images, int size) {

        CollectionImage result = new GenericCollectionImage();
        Iterator it = images.iterator();
        RenderedOp previous = (RenderedOp) it.next();
        // this removes the alpha channel if there is one
        previous = ImageUtils.applyAlphaChannel(previous, size);
        // previous.removeProperty("JAI.ImageMetadata");
        result.add(previous);
        int i = 0;
        RenderedOp overlay = previous;
        while(it.hasNext()) {

            RenderedOp next = (RenderedOp) it.next();
            // extract the alpha channel on its own
            RenderedOp alphaNext = ImageUtils.extractAlphaChannel(next);
            // extract the non-aplha channels
            next = ImageUtils.removeAlphaChannel(next);

            // previous will be treated as opaque as no alpha is supplied.
            RenderedOp composite = CompositeDescriptor.create(next, overlay, alphaNext, null, Boolean.FALSE, CompositeDescriptor.NO_DESTINATION_ALPHA, null);

            composite.removeProperty("JAI.ImageMetadata");
            result.add(composite);

            // composite image with have dimensions/origin the same as the
            // "next" image. Therefore we overlay on the "previous image to
            // get our next
            overlay = OverlayDescriptor.create(overlay, composite, null);
            i++;
        }

        return result;
    }
    /**
     * Returns the first child of the parent node that has the specified
     * localname or null if no child with that name can be found
     *
     * @param parent the parent node
     * @param childLocalName the localname of the child to find.
     * @return
     */
    public static Node getChildByLocalName(Node parent, String childLocalName) {
        Node result = null;
        if(parent.hasChildNodes()) {
            Node child = parent.getFirstChild();
            do {
                if (child.getLocalName().equals(childLocalName)) {
                    result = child;
                }
                child = child.getNextSibling();
            } while (child != null && result == null);
        }
        return result;
    }
    
    // Javadoc inherited
    protected String getFileFormat() {
        return ImageConstants.GIF_EXTENSION;
    }

     // Javadoc inherited
    protected ImageWriteParam getImageWriteParam(RenderedOp theImage,
                                                 Parameters params) {
        return null;
    }

    // javadoc inherited
    public String mimeType() {
        return "image/gif";
    }

}

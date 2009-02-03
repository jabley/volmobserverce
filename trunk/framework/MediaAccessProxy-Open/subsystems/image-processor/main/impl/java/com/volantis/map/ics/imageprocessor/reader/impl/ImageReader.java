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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.ics.imageprocessor.reader.impl;

import com.sun.media.jai.operator.ImageReadDescriptor;
import com.volantis.map.common.param.MissingParameterException;
import com.volantis.map.ics.imageprocessor.impl.GenericCollectionImage;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.CollectionImage;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.FormatDescriptor;
import javax.media.jai.operator.RescaleDescriptor;
import javax.media.jai.operator.TranslateDescriptor;
import java.awt.image.DataBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Simple utility class that assists in reading images
 */
public class ImageReader {

    /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(ImageReader.class);

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(ImageReader.class);

    /**
     * List of preprocessors operating on the raw input
     */
    private static final List<InputImageConditioner> conditioners = initializePreprocessors();

    /**
     * Initialize the list of preprocessors
     */
    private static List initializePreprocessors() {
        // Note: in future we may read conditioners from the configuration    
        ArrayList<InputImageConditioner> list = new ArrayList();
        list.add(new JPEGConditioner());
        return list;
    }

    private ImageReader() {
        // hide constructor
    }

    /**
     * A utility method for loading images. This tries to load images by asking
     * the registered readers to identify it. If this fails then we try readers
     * registered against the mime type in the parameters (if it exists). If
     * this fails then we try the file name extension.
     *
     * @param inputData
     * @return
     *
     * @throws com.volantis.map.common.param.MissingParameterException
     *
     */
    public static RenderedOp[] loadImage(ImageInputStream inputData)
        throws MissingParameterException {
        if (inputData == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null",
                                                    "inputData");
            throw new IllegalArgumentException(msg);
        }

        CollectionImage input = null;
        CollectionImage translated = new GenericCollectionImage();

        try {
            for (InputImageConditioner c : conditioners) {
                inputData = c.process(inputData);
            }

            input = (CollectionImage)
                ImageReadDescriptor.createCollection(
                    inputData,
                    null,
                    Boolean.TRUE,
                    Boolean.FALSE,
                    Boolean.FALSE,
                    null,
                    null,
                    null,
                    null,
                    null);

            Iterator it = input.iterator();

            while(it.hasNext()) {
                int minX = 0;
                int minY = 0;
                RenderedOp op = (RenderedOp) it.next();
                // for some reason this method occasinally returns an Object
                // i.e. not an IIOMetadata instance
                Object imageMetaObj = op.getProperty("JAI.ImageMetadata");
                if (imageMetaObj instanceof IIOMetadata) {
                    IIOMetadata imageMeta =  (IIOMetadata) imageMetaObj;
                    if (imageMeta != null && imageMeta.isStandardMetadataFormatSupported()) {
                        Node root = imageMeta.getAsTree("javax_imageio_1.0");
                        Node dimension = getChildByLocalName(root, "Dimension");
                        Node minXNode = null;
                        Node minYNode = null;
                        if (dimension != null) {
                            minXNode = getChildByLocalName(
                                dimension, "HorizontalPixelOffset");
                            minYNode = getChildByLocalName(
                                dimension, "VerticalPixelOffset");
                        }
                        if (minXNode != null) {
                            minX = Integer.parseInt(minXNode.
                                getAttributes().getNamedItem("value").getNodeValue());
                        }
                        if (minYNode != null) {
                            minY = Integer.parseInt(minYNode.
                                getAttributes().getNamedItem("value").getNodeValue());
                        }
                    }
                }
                if (minX != 0 || minY != 0) {
                    translated.add(TranslateDescriptor.create(op, (float) minX, (float) minY, null, null));
                } else {
                    translated.add(op);
                }
            }          
        } catch (Exception e) {
            LOGGER.error("jai-operation-failure", new String[]{ "ImageRead", e.toString() });
            throw new RuntimeException(e);
        }

        final RenderedOp[] result = new RenderedOp[input.size()];
        int i = 0;
        for (Iterator iter = translated.iterator(); iter.hasNext(); i++) {
            RenderedOp op = (RenderedOp) iter.next();
            final int dataType = op.getSampleModel().getDataType();
            if (dataType != DataBuffer.TYPE_BYTE) {
                op = changeDataTypeToByte(op);
            }
            result[i] = op;
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

    /**
     * Changes the data type to byte.
     *
     * @param op the RenderedOp to convert. Cannot be null.
     * @return the converted RenderedOp
     * @throws IllegalArgumentException if the data type of the original image
     * is not byte, short or int.
     */
    private static RenderedOp changeDataTypeToByte(final RenderedOp op) {
        final int dataType = op.getSampleModel().getDataType();
        final double maxValue;
        switch (dataType) {
            case DataBuffer.TYPE_BYTE:
                maxValue = 255.0;
                break;
            case DataBuffer.TYPE_USHORT:
                maxValue = 65535.0;
                break;
            case DataBuffer.TYPE_INT:
                maxValue = 0xFFFFFFFF;
                break;
            default:
                throw new IllegalArgumentException(
                    "Unsupported data buffer type: " + dataType);
        }

        final RenderedOp rescaled = RescaleDescriptor.create(
            op, new double[]{255.0 / maxValue}, new double[]{0}, null);

        final RenderedOp formatted = FormatDescriptor.create(
            rescaled, DataBuffer.TYPE_BYTE, null);
        return formatted;
    }
}

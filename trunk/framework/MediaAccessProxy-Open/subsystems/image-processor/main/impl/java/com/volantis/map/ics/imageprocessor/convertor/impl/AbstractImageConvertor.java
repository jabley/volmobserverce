/* ----------------------------------------------------------------------------
 * Copyright Volantis Systems Ltd 2008. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.ics.imageprocessor.convertor.impl;

import com.volantis.map.ics.imageprocessor.convertor.ImageConvertor;

import java.awt.*;

/**
 * Some common methods are stored here that are used by different branches of
 * the inheritence hierarchy
 */
public abstract class AbstractImageConvertor implements ImageConvertor {

    /**
     * The RGB weights used for conversion to greyscale.
     */
    private static final double[] WEIGHTS = {0.114D, 0.587D, 0.299D};

    /**
     * Returns a copy of the weights to use when combining RGB images into
     * greyscale.
     *
     * @return a copy of the weights to use when combining RGB images into
     * greyscale.
     */
    protected double[] getGreyscaleWeights() {
        return new double[]{WEIGHTS[0], WEIGHTS[1], WEIGHTS[2]};
    }

    /**
     * Calculate the image size required to ensure that an indexed or greyscale
     * image fits into a given amount of memory.
     *
     * @param imageSize      the size of the original image.
     * @param compressedSize the size of the orignal image in bytes after
     *                       compression.
     * @param requiredSize   the size in bytes that the image needs to be
     *                       compressed to.
     * @return the image size that will fit into the given memory assuming the
     *         same compression ratio.
     */
    public Dimension calcScale(Dimension imageSize, long compressedSize,
                               long requiredSize) {
        Dimension newDim = new Dimension();

        long uncompressedSize = imageSize.width * imageSize.height;

        // Divide your whatsit by your thingy and kiss your arse goodbye.
        // In otherwords take a wild guess.
        if (uncompressedSize > (compressedSize * 2)) {
            requiredSize /= 3;
        }
        double percentage =
            (double) compressedSize / (double) uncompressedSize;

        double widthHeightRatio = (double) imageSize.width /
            (double) imageSize.height;

        double factor = widthHeightRatio * percentage;

        double height = Math.sqrt((double) requiredSize / factor);

        // Make sure we reduce the width by at least 1 to stop
        // the whole lot going into a loop.

        double widthDiff = imageSize.width - (height * widthHeightRatio);

        if ((widthDiff < 1) && (height > widthHeightRatio)) {
            height = height - widthHeightRatio;
        }
        newDim.setSize((int) (height * widthHeightRatio), (int) height);

        return newDim;
    }
}

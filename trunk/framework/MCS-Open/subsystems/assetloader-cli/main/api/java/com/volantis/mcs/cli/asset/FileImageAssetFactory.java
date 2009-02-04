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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.cli.asset;

import com.volantis.mcs.assets.ConvertibleImageAsset;
import com.volantis.mcs.assets.DeviceImageAsset;
import com.volantis.mcs.assets.GenericImageAsset;
import com.volantis.mcs.assets.ImageAsset;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.content.URLContentBuilder;
import com.volantis.mcs.policies.variants.image.GenericImageSelectionBuilder;
import com.volantis.mcs.policies.variants.image.ImageConversionMode;
import com.volantis.mcs.policies.variants.image.ImageEncoding;
import com.volantis.mcs.policies.variants.image.ImageMetaDataBuilder;
import com.volantis.mcs.policies.variants.image.ImageRendering;
import com.volantis.mcs.policies.variants.selection.DefaultSelectionBuilder;
import com.volantis.mcs.policies.variants.selection.TargetedSelectionBuilder;
import com.volantis.synergetics.log.LogDispatcher;

import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

/**
 * A factory for creating image assets from image files on disk.
 * <p>
 * This creates the assets by loading in the files and extracting the meta
 * data from the image itself, as far as is possible.
 * <p>
 * It supports all the types of images defined in {@link ImageAsset}.
 */
public class FileImageAssetFactory {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
                LocalizationFactory.createLogger(FileImageAssetFactory.class);

    private PolicyFactory factory = PolicyFactory.getDefaultInstance();

    /**
     * Create a {@link DeviceImageAsset} from the image file provided.
     *
     * @param imageFile the file containing the image data for the asset.
     * @param assetGroup the asset group to use for this asset, or null if one
     *      is not required.
     * @param deviceName the name of the device that this asset should apply
     *      for.
     * @return the asset created.
     * @throws IOException if there was a problem reading the image file.
     */
    public VariantBuilder createDeviceImageVariant(File imageFile,
            String assetGroup, String deviceName) throws IOException {

        VariantBuilder variantBuilder =
                factory.createVariantBuilder(VariantType.IMAGE);

        TargetedSelectionBuilder targeted =
                factory.createTargetedSelectionBuilder();
        targeted.addDevice(deviceName);
        variantBuilder.setSelectionBuilder(targeted);

        ImageMetaDataBuilder image = createImageMetaData(imageFile);
        variantBuilder.setMetaDataBuilder(image);

        URLContentBuilder content = createURLContent(assetGroup, imageFile);
        variantBuilder.setContentBuilder(content);

        return variantBuilder;
    }

    private URLContentBuilder createURLContent(String assetGroup, File imageFile) {
        URLContentBuilder content = factory.createURLContentBuilder();
        if (assetGroup != null) {
            PolicyReference reference = factory.createPolicyReference(
                    assetGroup, PolicyType.BASE_URL);
            content.setBaseURLPolicyReference(reference);
        }
        content.setURL(imageFile.getName());
        return content;
    }

    /**
     * Create a {@link GenericImageAsset} from the image file provided.
     *
     * @param imageFile the file containing the image data for the asset.
     * @param assetGroup the asset group to use for this asset, or null if one
     *      is not required.
     * @param widthHint a hint as to the width of the image, as a percentage.
     * @return the asset created.
     * @throws IOException if there was a problem reading the image file.
     */
    public VariantBuilder createGenericImageVariant(File imageFile,
            String assetGroup, int widthHint) throws IOException {

        VariantBuilder variantBuilder =
                factory.createVariantBuilder(VariantType.IMAGE);

        GenericImageSelectionBuilder generic =
                factory.createGenericImageSelectionBuilder();
        generic.setWidthHint(widthHint);
        variantBuilder.setSelectionBuilder(generic);

        ImageMetaDataBuilder image = createImageMetaData(imageFile);
        variantBuilder.setMetaDataBuilder(image);

        URLContentBuilder content = createURLContent(assetGroup, imageFile);
        variantBuilder.setContentBuilder(content);

        return variantBuilder;

    }

    /**
     * Create a {@link ConvertibleImageAsset} from the image file provided.
     *
     * @param imageFile the file containing the image data for the asset.
     * @param assetGroup the asset group to use for this asset, or null if one
     *      is not required.
     * @return the asset created.
     * @throws IOException if there was a problem reading the image file.
     */
    public VariantBuilder createConvertibleImageVariant(File imageFile,
            String assetGroup) throws IOException {

        VariantBuilder variantBuilder =
                factory.createVariantBuilder(VariantType.IMAGE);

        DefaultSelectionBuilder defaultSelection =
                factory.createDefaultSelectionBuilder();
        variantBuilder.setSelectionBuilder(defaultSelection);

        ImageMetaDataBuilder image = createImageMetaData(imageFile);
        image.setConversionMode(ImageConversionMode.ALWAYS_CONVERT);
        variantBuilder.setMetaDataBuilder(image);

        URLContentBuilder content = createURLContent(assetGroup, imageFile);
        variantBuilder.setContentBuilder(content);

        return variantBuilder;
    }

    /**
     * Create the meta data for the image file provided.
     *
     * @param imageFile the file containing the image data for the asset.
     * @return the meta data created.
     * @throws IOException if there was a problem reading the image file.
     */
    private ImageMetaDataBuilder createImageMetaData(File imageFile)
            throws IOException {

        ImageMetaDataBuilder builder;

        // Calculate the extension for this image.
        String imageName = imageFile.getName();
        String extension = getExtension(imageName);
        if (extension != null) {
            // And figure out the official encoding for this extension.
            ImageEncoding encoding = (ImageEncoding)
                    ImageEncoding.COLLECTION.getEncodingForExtension(extension);
            // If we support this extension
            if (encoding != null) {

                // Then read in the image...
                RenderedImage image = javax.imageio.ImageIO.read(imageFile);
                ColorModel color = image.getColorModel();

                // ... create the metaData ...
                builder = factory.createImageMetaDataBuilder();

                // ... and then use the image meta data to populate it.
                builder.setWidth(image.getWidth());
                builder.setHeight(image.getHeight());
                builder.setPixelDepth(color.getPixelSize());
                ImageRendering rendering;

                // todo pixel size is not enough, sometime there are levels of gray.
                if (color.getPixelSize() > 1) {
                    rendering = ImageRendering.COLOR;
                } else {
                    rendering = ImageRendering.GRAYSCALE;
                }
                builder.setRendering(rendering);
                builder.setImageEncoding(encoding);
                if (logger.isDebugEnabled()) {
                    logger.debug("Created metaData: " + builder);
                }

            } else {
                throw new IOException("Image extension '" + extension + "' " +
                        "has no supported encoding");
            }
        } else {
            throw new IOException("Image name '" + imageName + "' contained " +
                    "no extension");
        }
        return builder;
    }

    /**
     * Returns the extension of the image, or null if one does not exist.
     *
     * @param imageName the file name of the image.
     * @return the extension of the image, or null.
     */
    private String getExtension(String imageName) {

        String extension = null;

        int dot = imageName.lastIndexOf('.');
        if (dot > 0) {
            // Extract the extension from the image name.
            extension = imageName.substring(dot + 1);
            // Canonicalise the extension.
            extension = extension.trim().toLowerCase();
        }

        return extension;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/3	doug	VBM:2004111702 Refactored Logging framework

 29-Nov-04	6304/5	tom	VBM:2004112406 Changed the image read method

 16-Aug-04	5177/1	geoff	VBM:2004081014 Provide a bulk image loading CLI

 ===========================================================================
*/

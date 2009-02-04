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
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.policies.variants.content.BaseLocation;
import com.volantis.mcs.policies.variants.content.URLContent;
import com.volantis.mcs.policies.variants.image.GenericImageSelection;
import com.volantis.mcs.policies.variants.image.ImageEncoding;
import com.volantis.mcs.policies.variants.image.ImageMetaData;
import com.volantis.mcs.policies.variants.image.ImageRendering;
import com.volantis.mcs.policies.variants.selection.DefaultSelection;
import com.volantis.mcs.policies.variants.selection.DeviceReference;
import com.volantis.mcs.policies.variants.selection.TargetedSelection;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.synergetics.testtools.io.ResourceTemporaryFileCreator;
import com.volantis.synergetics.testtools.io.TemporaryFileExecutor;
import com.volantis.synergetics.testtools.io.TemporaryFileManager;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * A test case for {@link FileImageAssetFactory}.
 */
public class FileImageAssetFactoryTestCase extends TestCaseAbstract {

    /**
     * Test creating a {@link DeviceImageAsset}.
     *
     * @throws Exception
     */
    public void testDeviceImage() throws Exception {

        final String imageName = "tt_blackbox2.wbmp";
        TemporaryFileManager fileManager = new TemporaryFileManager(
                new ResourceTemporaryFileCreator(
                        FileImageAssetFactoryTestCase.class, imageName));

        fileManager.executeWith(new TemporaryFileExecutor() {
            public void execute(File temporaryFile) throws Exception {

                final String assetGroup = "assetgroup";
                final String deviceName = "wapdevice";
                FileImageAssetFactory factory = new FileImageAssetFactory();
                Variant variant = factory.createDeviceImageVariant(
                        temporaryFile, assetGroup, deviceName).getVariant();

                // Test selection.
                TargetedSelection selection = (TargetedSelection)
                        variant.getSelection();
                List devices = selection.getDeviceReferences();
                assertEquals("Device List Size", 1, devices.size());
                assertEquals("Device", deviceName,
                        ((DeviceReference) devices.get(0)).getDeviceName());

                // Test meta data.
                checkMetaData(variant, 96, 42, 1, ImageRendering.GRAYSCALE,
                        ImageEncoding.WBMP);

                // Test content.
                checkContent(variant, assetGroup, temporaryFile);
            }
        });

    }

    private void checkContent(
            Variant variant, final String assetGroup, File temporaryFile) {
        URLContent content = (URLContent) variant.getContent();
        PolicyReference baseURLReference =
                content.getBaseURLPolicyReference();
        assertEquals("Base URL name", assetGroup,
                baseURLReference.getName());
        assertEquals("Base URL type", PolicyType.BASE_URL,
                baseURLReference.getExpectedPolicyType());

        assertEquals("Value", temporaryFile.getName(),
                content.getURL());
        assertEquals("", BaseLocation.DEFAULT,
                content.getBaseLocation());
    }

    private void checkMetaData(
            Variant variant, final int expectedWidth, final int expectedHeight,
            final int expectedPixelDepth,
            final ImageRendering expectedRendering,
            final ImageEncoding expectedEncoding) {

        ImageMetaData image = (ImageMetaData) variant.getMetaData();

        assertEquals("", expectedWidth, image.getWidth());
        assertEquals("", expectedHeight, image.getHeight());
        assertEquals("", expectedPixelDepth, image.getPixelDepth());
        assertEquals("", expectedRendering, image.getRendering());
        assertEquals("", expectedEncoding, image.getImageEncoding());
    }

    /**
     * Test creating a {@link GenericImageAsset}.
     *
     * @throws Exception
     */
    public void testGenericImage() throws Exception {

        final String imageName = "gimp.gif";
        TemporaryFileManager fileManager = new TemporaryFileManager(
                new ResourceTemporaryFileCreator(
                        FileImageAssetFactoryTestCase.class, imageName));

        fileManager.executeWith(new TemporaryFileExecutor() {
            public void execute(File temporaryFile) throws Exception {

                FileImageAssetFactory factory = new FileImageAssetFactory();
                final String assetGroup = "assetgroup";
                Variant variant = factory.createGenericImageVariant(
                        temporaryFile, assetGroup, 50).getVariant();

                // Test selection.
                GenericImageSelection selection = (GenericImageSelection)
                        variant.getSelection();
                assertEquals("", 50, selection.getWidthHint());

                // Test meta data.
                checkMetaData(variant, 36, 36, 8, ImageRendering.COLOR,
                        ImageEncoding.GIF);

                // Test content.
                checkContent(variant, assetGroup, temporaryFile);
            }
        });

    }

    /**
     * Test creating a {@link ConvertibleImageAsset}.
     *
     * @throws Exception
     */
    public void testConvertibleImage() throws Exception {

        final String imageName = "tt_greenbrandlogo.png";
        TemporaryFileManager fileManager = new TemporaryFileManager(
                new ResourceTemporaryFileCreator(
                        FileImageAssetFactoryTestCase.class, imageName));

        fileManager.executeWith(new TemporaryFileExecutor() {
            public void execute(File temporaryFile) throws Exception {

                FileImageAssetFactory factory = new FileImageAssetFactory();
                final String assetGroup = "assetgroup";
                Variant variant = factory.createConvertibleImageVariant(
                        temporaryFile, assetGroup).getVariant();

                // Test selection.
                DefaultSelection selection = (DefaultSelection)
                        variant.getSelection();

                // Test meta data.
                checkMetaData(variant, 100, 75, 24, ImageRendering.COLOR,
                        ImageEncoding.PNG);

                // Test content.
                checkContent(variant, assetGroup, temporaryFile);
            }
        });

    }

    /**
     * Test creating an asset with an image which has no extension.
     *
     * @throws Exception
     */
    public void testNoExtension() throws Exception {

        final String image = "image";
        TemporaryFileManager fileManager = new TemporaryFileManager(
                new ResourceTemporaryFileCreator(
                        FileImageAssetFactoryTestCase.class, image));

        fileManager.executeWith(new TemporaryFileExecutor() {
            public void execute(File temporaryFile) throws Exception {

                FileImageAssetFactory factory = new FileImageAssetFactory();
                final String assetGroup = "assetgroup";
                try {
                    factory.createConvertibleImageVariant(
                            temporaryFile, assetGroup);

                    fail("created image without extension");
                } catch (IOException e) {
                    // this is expected.
                    assertTrue("", e.getMessage().endsWith("extension"));
                }
            }
        });

    }

    /**
     * Test creating an asset with an image which has no extension.
     *
     * @throws Exception
     */
    public void testNoEncoding() throws Exception {

        final String image = "image.unknown";
        TemporaryFileManager fileManager = new TemporaryFileManager(
                new ResourceTemporaryFileCreator(
                        FileImageAssetFactoryTestCase.class, image));

        fileManager.executeWith(new TemporaryFileExecutor() {
            public void execute(File temporaryFile) throws Exception {

                FileImageAssetFactory factory = new FileImageAssetFactory();
                final String assetGroup = "assetgroup";
                try {
                    factory.createConvertibleImageVariant(temporaryFile,
                            assetGroup);

                    fail("created image without encoding");
                } catch (IOException e) {
                    // this is expected.
                    assertTrue("", e.getMessage().endsWith("encoding"));
                }
            }
        });

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6304/1	tom	VBM:2004112406 changed image read method

 16-Aug-04	5177/1	geoff	VBM:2004081014 Provide a bulk image loading CLI

 ===========================================================================
*/

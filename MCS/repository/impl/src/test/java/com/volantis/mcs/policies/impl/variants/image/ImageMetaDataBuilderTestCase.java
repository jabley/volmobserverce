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

package com.volantis.mcs.policies.impl.variants.image;

import com.volantis.mcs.model.TestValidator;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.Validatable;
import com.volantis.mcs.policies.impl.PolicyMessages;
import com.volantis.mcs.policies.variants.image.ImageConversionMode;
import com.volantis.mcs.policies.variants.image.ImageMetaData;
import com.volantis.mcs.policies.variants.image.ImageRendering;
import com.volantis.mcs.policies.variants.image.ImageMetaDataBuilder;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link ImageMetaData}.
 */
public class ImageMetaDataBuilderTestCase
        extends TestCaseAbstract {

    /**
     * Test that the defaults are correct.
     */
    public void testDefaults() throws Exception {

        ImageMetaDataBuilder builder = new ImageMetaDataBuilderImpl();
        assertEquals("Rendering", ImageRendering.COLOR,
                builder.getRendering());
        assertEquals("Conversion Mode", ImageConversionMode.NEVER_CONVERT,
                builder.getConversionMode());
    }

    /**
     * Test that it is impossible to set a null rendering.
     */
    public void testSetNullRendering() {

        ImageMetaDataBuilder builder = new ImageMetaDataBuilderImpl();
        try {
            builder.setRendering(null);
            fail("Did not detect illegal rendering");
        } catch (IllegalArgumentException expected) {
        }
    }

    /**
     * Test that it is impossible to set a null conversion mode.
     */
    public void testSetNullConversionMode() {

        ImageMetaDataBuilder builder = new ImageMetaDataBuilderImpl();
        try {
            builder.setConversionMode(null);
            fail("Did not detect illegal conversion mode");
        } catch (IllegalArgumentException expected) {
        }
    }

    /**
     * Test that validation works.
     */
    public void testValidation() {

        ImageMetaDataBuilder builder = new ImageMetaDataBuilderImpl();
        builder.setHeight(-1);
        builder.setWidth(0);
        builder.setPixelDepth(0);

        TestValidator validator = new TestValidator();
        validator.expectDiagnostic(DiagnosticLevel.ERROR, "/encoding",
                PolicyMessages.ENCODING_UNSPECIFIED, null);

        validator.expectDiagnostic(DiagnosticLevel.ERROR, "/assetWidth",
                PolicyMessages.MINIMUM_EXCLUSIVE,
                new Object[]{
                    PolicyModel.WIDTH.getDescription(),
                    new Integer(0),
                    new Integer(0)
                });

        validator.expectDiagnostic(DiagnosticLevel.ERROR, "/assetHeight",
                PolicyMessages.MINIMUM_EXCLUSIVE,
                new Object[]{
                    PolicyModel.HEIGHT.getDescription(),
                    new Integer(0),
                    new Integer(-1)
                });

        validator.expectDiagnostic(DiagnosticLevel.ERROR, "/pixelDepth",
                PolicyMessages.RANGE_INCLUSIVE_INCLUSIVE,
                new Object[]{
                    PolicyModel.PIXEL_DEPTH.getDescription(),
                    new Integer(1),
                    new Integer(32),
                    new Integer(0)
                });

        validator.validate((Validatable) builder);
    }
}

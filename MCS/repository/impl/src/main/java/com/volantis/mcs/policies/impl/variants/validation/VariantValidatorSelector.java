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

package com.volantis.mcs.policies.impl.variants.validation;

import com.volantis.mcs.policies.impl.variants.CommonVariantValidator;
import com.volantis.mcs.policies.impl.variants.VariantValidator;
import com.volantis.mcs.policies.impl.variants.image.ImageVariantValidator;
import com.volantis.mcs.policies.impl.variants.selection.VariantSelectionTypes;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.content.ContentType;
import com.volantis.mcs.policies.variants.metadata.MetaDataType;

import java.util.HashMap;
import java.util.Map;

/**
 * @mock.generate
 */
public class VariantValidatorSelector {

    private static final Map variantType2Validator;

    static {
        variantType2Validator = new HashMap();
        
        VariantMetaDataValidator noMetaDataValidator =
                new VariantMetaDataValidator(new MetaDataType[]{
                    null
                });

        VariantContentValidator noContentValidator =
                new VariantContentValidator(new ContentType[]{
                    null
                });

        VariantContentValidator singleURLContentValidator =
                new VariantContentValidator(new ContentType[]{
                    ContentType.URL
                });

        VariantContentValidator singleURLOrTextualContentValidator =
                new VariantContentValidator(new ContentType[]{
                    ContentType.URL,
                    ContentType.EMBEDDED
                });

        // Audio
        VariantMetaDataValidator audioMetaDataValidator =
                new VariantMetaDataValidator(new MetaDataType[]{
                    MetaDataType.AUDIO
                });

        VariantValidator audioValidator = new CommonVariantValidator(
                getSelectionValidator(VariantType.AUDIO), audioMetaDataValidator,
                singleURLContentValidator, null);

        // Chart
        VariantMetaDataValidator chartMetaDataValidator =
                new VariantMetaDataValidator(new MetaDataType[]{
                    MetaDataType.CHART
                });

        VariantValidator chartValidator = new CommonVariantValidator(
                getSelectionValidator(VariantType.CHART), chartMetaDataValidator,
                noContentValidator, null);

        // Image
        VariantMetaDataValidator imageMetaDataValidator =
                new VariantMetaDataValidator(new MetaDataType[]{
                    MetaDataType.IMAGE
                });

        VariantContentValidator imageContentValidator =
                new VariantContentValidator(new ContentType[]{
                    ContentType.AUTOMATIC_URL_SEQUENCE,
                    ContentType.URL
                });

        VariantValidator imageValidator = new CommonVariantValidator(
                getSelectionValidator(VariantType.IMAGE), imageMetaDataValidator,
                imageContentValidator, new ImageVariantValidator());

        // Layout
        VariantContentValidator layoutContentValidator =
                new VariantContentValidator(new ContentType[]{
                    ContentType.LAYOUT
                });

        VariantValidator layoutValidator = new CommonVariantValidator(
                getSelectionValidator(VariantType.LAYOUT), noMetaDataValidator,
                layoutContentValidator, null);

        // Link
        VariantValidator linkValidator = new CommonVariantValidator(
                getSelectionValidator(VariantType.LINK), noMetaDataValidator,
                singleURLContentValidator, null);

        // Null
        VariantValidator nullValidator = new CommonVariantValidator(
                getSelectionValidator(VariantType.NULL), noMetaDataValidator,
                noContentValidator, null);

        // Script
        VariantMetaDataValidator scriptMetaDataValidator =
                new VariantMetaDataValidator(new MetaDataType[]{
                    MetaDataType.SCRIPT
                });

        VariantValidator scriptValidator = new CommonVariantValidator(
                getSelectionValidator(VariantType.SCRIPT), scriptMetaDataValidator,
                singleURLOrTextualContentValidator, null);

        // Text
        VariantMetaDataValidator textMetaDataValidator =
                new VariantMetaDataValidator(new MetaDataType[]{
                    MetaDataType.TEXT
                });

        VariantValidator textValidator = new CommonVariantValidator(
                getSelectionValidator(VariantType.TEXT), textMetaDataValidator,
                singleURLOrTextualContentValidator, null);

        // Theme
        VariantContentValidator themeContentValidator =
                new VariantContentValidator(new ContentType[]{
                    ContentType.THEME
                });

        VariantValidator themeValidator = new CommonVariantValidator(
                getSelectionValidator(VariantType.THEME), noMetaDataValidator,
                themeContentValidator, null);

        // Video
        VariantMetaDataValidator videoMetaDataValidator =
                new VariantMetaDataValidator(new MetaDataType[]{
                    MetaDataType.VIDEO
                });
        VariantValidator videoValidator = new CommonVariantValidator(
                getSelectionValidator(VariantType.VIDEO), videoMetaDataValidator,
                singleURLContentValidator, null);

        // Store validators
        variantType2Validator.put(VariantType.AUDIO, audioValidator);
        variantType2Validator.put(VariantType.CHART, chartValidator);
        variantType2Validator.put(VariantType.IMAGE, imageValidator);
        variantType2Validator.put(VariantType.LAYOUT, layoutValidator);
        variantType2Validator.put(VariantType.LINK, linkValidator);
        variantType2Validator.put(VariantType.NULL, nullValidator);
        variantType2Validator.put(VariantType.SCRIPT, scriptValidator);
        variantType2Validator.put(VariantType.TEXT, textValidator);
        variantType2Validator.put(VariantType.THEME, themeValidator);
        variantType2Validator.put(VariantType.VIDEO, videoValidator);
    }

    private static VariantSelectionValidator getSelectionValidator(
            VariantType type) {
        return new VariantSelectionValidator(
                VariantSelectionTypes.getValidSelectionTypes(type));
    }

    public static final VariantValidatorSelector SELECTOR =
            new VariantValidatorSelector();

    public VariantValidator selectValidator(VariantType variantType) {
        VariantValidator validator = (VariantValidator)
                variantType2Validator.get(variantType);

        if (validator == null) {
            throw new IllegalStateException(
                    "Unknown variants type: " + variantType);
        }
        return validator;
    }

}

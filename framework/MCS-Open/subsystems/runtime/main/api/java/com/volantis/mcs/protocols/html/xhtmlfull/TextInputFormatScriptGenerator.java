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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html.xhtmlfull;

import com.volantis.mcs.dom2theme.AssetResolver;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;
import com.volantis.mcs.policies.variants.metadata.EncodingCollectionFactory;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.protocols.XFTextInputAttributes;
import com.volantis.mcs.protocols.html.XHTMLFullValidationHelper;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.forms.validation.TextInputFormat;
import com.volantis.mcs.protocols.forms.validation.TextInputFormatParser;
import com.volantis.mcs.themes.StyleComponentURI;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleString;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.styling.Styles;

public class TextInputFormatScriptGenerator {

    private static final EncodingCollection REQUIRED_ENCODINGS;

    static {
        EncodingCollectionFactory factory =
                EncodingCollectionFactory.getDefaultInstance();
        REQUIRED_ENCODINGS =
                factory.createEncodingCollection(TextEncoding.FORM_VALIDATOR);
    }

    private final AssetResolver assetResolver;

    private final TextInputFormatParser parser;

    public TextInputFormatScriptGenerator(
            AssetResolver assetResolver,
            TextInputFormatParser parser) {
        this.assetResolver = assetResolver;
        this.parser = parser;
    }

    /**
     * Get the text input validation attribute in WML format.
     *
     * @param attributes The XFTextInputAttributes containing the validation
     * @return A validation String in WCSS format.
     * @todo This is public because it is used in a number of test cases, it
     * should really be protected but that would need some improved testing.
     */
    public TextInputFormat getTextInputFormat2(
            XFTextInputAttributes attributes) {
//        String result = null;

        Styles styles = attributes.getStyles();
        StyleValue value = styles.getPropertyValues()
                .getStyleValue(StylePropertyDetails.MCS_INPUT_FORMAT);

        String format;
        if (value == StyleKeywords.NONE) {
            // No validation.
            format = null;
        } else if (value instanceof StyleString) {
            StyleString string = (StyleString) value;
            format = string.getString();
        } else if (value instanceof StyleComponentURI) {
            StyleComponentURI uri = (StyleComponentURI) value;
            PolicyReference reference = assetResolver.evaluateExpression(
                    uri.getExpression());
            format = assetResolver.resolveText(reference, REQUIRED_ENCODINGS);
        } else {
            throw new IllegalStateException(
                    "Unknown " +
                            StylePropertyDetails.MCS_INPUT_FORMAT.getName() +
                            " value " + value);
        }

        TextInputFormat result;
        if (format == null) {
            result = null;
        } else {
            result = parser.parseFormat(attributes.getName(), format);
        }

//        if (XHTMLFullValidationHelper.getDefaultInstance() != null
//                && isFormatStrValid(attributes)) {
//            result = getTextFromReference(attributes.getValidate(),
//                                       TextEncoding.FORM_VALIDATOR);
//
//        result = XHTMLFullValidationHelper.getDefaultInstance()
//                .createTextInputFormat(result);
//        }
        return result;
    }

    public void writeJavaScriptValidation(
            XFTextInputAttributes attribute,
            StringBuffer sb) {
        TextInputFormat format = getTextInputFormat2(attribute);
        if (format != null) {
            String pattern = format.getFormat();
            pattern = extractPattern(pattern);

            String caption = getPlainText(attribute.getCaption());
            String errorMsg = getPlainText(attribute.getErrmsg());
            if (errorMsg == null) {
                // if no error message has been provided then we
                // need to generate a sensible message
                errorMsg = "Invalid input for " +
                        ((caption != null) ? caption : " a form field");
            }

            // We generate the JavaScript to perform the validation
            // If the attribute.emptryOK is true then we must test if
            // the form field is not empty and if it fails the regex
            // and only then generate the error message
            sb.append("if(!(new RegEx(\"").append(pattern).append("\",")
                    .append("form.")
                    .append(attribute.getName()).append(".value).match())");
            if (format.isEmptyOk()) {
                sb.append(" && (form.").append(attribute.getName())
                        .append(".value!='')");
            }
            sb.append(") {\n errMsg += \"\\n").append(errorMsg).append("\";")
                    .append("}\n");
        }
    }

    private String extractPattern(String format) {
        return XHTMLFullValidationHelper.getDefaultInstance()
                .createTextInputFormat(format);
    }

    /**
     * Get plain text from the reference.
     *
     * @param reference The reference from which the text is to be retrieved.
     * @return The text associated with the matching text asset, or null.
     */
    protected String getPlainText(TextAssetReference reference) {
        return reference == null ? null : reference.getText(TextEncoding.PLAIN);
    }
}

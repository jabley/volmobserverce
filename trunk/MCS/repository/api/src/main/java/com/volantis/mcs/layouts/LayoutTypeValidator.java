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
package com.volantis.mcs.layouts;

import com.volantis.mcs.accessors.xml.jdom.XMLLayoutAttributeTranslations;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LayoutTypeValidator {

    private static XMLLayoutAttributeTranslations translations =
            XMLLayoutAttributeTranslations.getInstance();

    private final static String colorNameType =
            "aqua|black|blue|fuchsia|gray|green|lime|maroon|navy|olive|" +
            "purple|red|silver|teal|white|yellow|ActiveBorder|ActiveCaption|" +
            "AppWorkspace|Background|ButtonFace|ButtonHighlight|ButtonShadow|" +
            "ButtonText|CaptionText|GrayText|Highlight|HighlightText|" +
            "InactiveBorder|InactiveCaption|InactiveCaptionText|" +
            "InfoBackground|InfoText|Menu|MenuText|Scrollbar|" +
            "ThreeDDarkShadow|ThreeDFace|ThreeDHighlight|ThreeDLightShadow|" +
            "ThreeDShadow|Window|WindowFrame|WindowText";

    private static String RGBType = "#(\\p{XDigit}{3}){1,2}";

    private static String RGBOrColorNameType =
            "(" + RGBType + ")|(" + colorNameType + ")";

    private static Pattern RGBOrColorNameTypePattern =
            compilePattern(RGBOrColorNameType);

    private final static String DIRECTION_TYPES = "l2r|r2l|fixed";

    private static Pattern DirectionTypePattern =
            compilePattern(DIRECTION_TYPES);

    private final static String themeClassNameType =
            "(" +
                "[_a-zA-Z]|" +
                "[\u00a0-\ufffd]|" +
                "(\\\\[0-9a-f]{1,6} ?)|" +
                "\\\\[ -~\u00a0-\ufffd]" +
            ")" +
            "(" +
                "[_a-zA-Z0-9\\\\-]|" +
                "[\u00a0-\ufffd]|" +
                "(\\\\[0-9a-f]{1,6} ?)|" +
                "\\\\[ -~\u00a0-\ufffd]" +
            ")*";

    private static Pattern themeClassNameTypePattern =
            compilePattern(themeClassNameType);

    private static String quotedComponentReferenceType = "\\{.*\\}";

    private static String quotedComponentReferenceOrStyleClassType =
            "(" + quotedComponentReferenceType + ")|" +
            "(" + themeClassNameType + ")";

    private static Pattern quotedComponentReferenceOrStyleClassTypePattern =
            compilePattern(quotedComponentReferenceOrStyleClassType);

    private static Pattern themeClassNameListPattern = compilePattern(
                toList(themeClassNameType));

    private static Pattern formatNameTypePattern = compilePattern(
                "[a-zA-Z_][a-zA-Z0-9\\-_.]*");

    private static String SMIL20FullClockValueType =
            "[0-9]+:[0-5][0-9]:[0-5][0-9](\\.[0-9]+)?";

    private static String SMIL20PartialClockValueType =
            "[0-5][0-9]:[0-5][0-9](\\.[0-9]+)?";

    private static String SMIL20TimeCountType =
            "[0-9]+(\\.[0-9]+)?(h|min|s|ms)?";

    private static String clockValueType =
            "(" + SMIL20FullClockValueType + ")|" +
            "(" + SMIL20PartialClockValueType + ")|" +
            "(" + SMIL20TimeCountType + ")";

    private static Pattern clockValueTypeListPattern =
            compilePattern(toList(clockValueType));

    private static Pattern unsignedPattern = compilePattern("\\p{Digit}*");

    /**
     * Convert an old format attribute value to a new format value.
     * <p>
     * This is required in only those few cases where the attribute is not a
     * keyword but still has a different form in the different versions.
     *
     * @param element
     * @param attribute
     * @param oldValue
     * @return
     */
    public static String newValue(String element, String attribute,
            String oldValue) {

        XMLLayoutAttributeTranslations translations = LayoutTypeValidator.translations;
        return translations.translateOldToNewAttributeValue(element, attribute,
                oldValue);
    }

    public static String oldValue(String element, String attribute,
            String newValue) {

        XMLLayoutAttributeTranslations translations = LayoutTypeValidator.translations;
        return translations.translateNewToOldAttributeValue(element, attribute,
                newValue);
    }

    /**
     * Get the set of old keywords that match the new keywords provided for the
     * element and attribute provided.
     * <p>
     * Note that this relies on the original mapping from new back to old
     * keeping any invalid new value as the old value (which is somewhat
     * dubious!).
     *
     * @param element
     * @param attribute
     * @param newKeywords
     * @return
     */
    public static Set getOldKeywords(String element, String attribute,
            String[] newKeywords) {

        HashSet oldKeywords = new HashSet();
        for (int i = 0; i < newKeywords.length; i++) {
            oldKeywords.add(oldValue(element, attribute, newKeywords[i]));
        }
        return oldKeywords;
    }

    public static boolean isPixelsOrPercentType(String element,
            String attribute, String units) {

        Set keywords = getOldKeywords(element, attribute,
                new String[] {"pixels", "percent"});
        return keywords.contains(units);
    }
    
    public static boolean isFormFragmentLinkPositionType(String element,
            String attribute, String linkPosition) {

        Set keywords = getOldKeywords(element, attribute,
                new String[] {"before", "after"});
        return keywords.contains(linkPosition);
    }

    public static boolean isQuotedComponentReferenceOrStyleClassType(
            String componentRefOrStyleClass) {

        return isMatch(quotedComponentReferenceOrStyleClassTypePattern,
                componentRefOrStyleClass);
    }

    public static boolean isThemeClassNameType(String themeClassNameType) {
        return isMatch(themeClassNameTypePattern, themeClassNameType);
    }

    public static boolean isBoolean(String booleanString) {
        return "true".equalsIgnoreCase(booleanString) ||
                "false".equalsIgnoreCase(booleanString);
    }

    public static boolean isRGBOrColorNameType(String color) {
        return isMatch(RGBOrColorNameTypePattern, color);
    }

    public static boolean isUnsigned(String number) {
        return isMatch(unsignedPattern, number);
    }

    public static boolean isThemeClassNameList(String styleClasses) {
        return isMatch(themeClassNameListPattern, styleClasses);
    }

    public static boolean isFormatNameType(String name) {
        return isMatch(formatNameTypePattern, name);
    }

    public static boolean isDirectionType(String name) {
        return isMatch(DirectionTypePattern, name);
    }

    public static boolean isClockValueTypeList(String clockValues) {
        return isMatch(clockValueTypeListPattern, clockValues);
    }

    private static Pattern compilePattern(final String regexString) {
        return Pattern.compile(regexString);
    }

    private static String toList(String pattern) {
        return
                "(" +
                    "(" + pattern + ")" +
                    "([ \t\r]+" +
                        "(" + pattern + ")" +
                    ")*" +
                ")?";
    }

    private static boolean isMatch(final Pattern pattern, String value) {

        if (pattern == null) {
            throw new IllegalArgumentException("regex cannot be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("value cannot be null");
        }

        final boolean isMatch;
        
        Matcher matcher = pattern.matcher(value);
        isMatch = matcher.matches();

        return isMatch;
    }

}

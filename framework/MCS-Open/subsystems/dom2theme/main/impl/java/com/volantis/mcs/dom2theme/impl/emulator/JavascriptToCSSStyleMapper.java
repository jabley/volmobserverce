/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.dom2theme.impl.emulator;

/**
 * Converts from css style property names to javascript style names. This
 * implementation is trivial at present (simply converting from names in the
 * form xxx-xxx to xxxXxx), but should be extended as and when exceptions are
 * noticed.
 */
public class JavascriptToCSSStyleMapper {

    private static final JavascriptToCSSStyleMapper MAPPER;

    static {
        MAPPER = new JavascriptToCSSStyleMapper();
    }

    /**
     * Restrict access to enforce singleton pattern.
     */
    private JavascriptToCSSStyleMapper() {
    }

    public static JavascriptToCSSStyleMapper getDefaultInstance() {
        return MAPPER;
    }

    /**
     * Get the equivalent Javascript equivalent of the CSS property name.
     *
     * @param cssPropertyName   name of the CSS Property
     * @return String name of the equivalent javascript style property. May be
     * null if there is no mapping.
     */
    public String getJSForCSS(String cssPropertyName) {
        String[] parts = cssPropertyName.split("-");
        StringBuffer jsName = new StringBuffer(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            final String part = parts[i];
            // Convert the lower case character into it's uppercase equivalent.
            jsName.append((char)(part.charAt(0) -32));
            jsName.append(part.substring(1));
        }
        return jsName.toString();
    }
}

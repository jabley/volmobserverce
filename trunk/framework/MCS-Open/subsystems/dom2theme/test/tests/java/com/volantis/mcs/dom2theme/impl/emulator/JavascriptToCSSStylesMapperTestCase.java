/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.dom2theme.impl.emulator;

import com.volantis.synergetics.testtools.TestCaseAbstract;

public class JavascriptToCSSStylesMapperTestCase extends TestCaseAbstract {

    public void testGetJSForCSS() {
        // Verify simple name is unchanged
        final String cssColor = "color";
        final JavascriptToCSSStyleMapper styleMapper =
                JavascriptToCSSStyleMapper.getDefaultInstance();
        final String jsColor = styleMapper.getJSForCSS(cssColor);
        assertEquals(cssColor, jsColor);

        // Verify name of the form xx-xx is converted to xxXx
        final String cssFontStyle = "font-style";
        final String jsFontStyle = styleMapper.getJSForCSS(cssFontStyle);
        assertNotEquals(cssFontStyle, jsFontStyle);
        assertEquals("fontStyle", jsFontStyle);
    }
}

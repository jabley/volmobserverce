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

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.dom2theme.AssetResolverMock;
import com.volantis.mcs.protocols.forms.validation.TextInputFormatParser;
import com.volantis.mcs.protocols.XFTextInputAttributes;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.styling.StylesBuilder;

/**
 * Test cases for {@link TextInputFormatScriptGenerator}.
 */
public class TextInputFormatScriptGeneratorTestCase
        extends TestCaseAbstract {

    /**
     * Ensure that the script generation produces the correct output.
     */
    public void testScriptGeneration() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final AssetResolverMock resolverMock =
                new AssetResolverMock("resolverMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        TextInputFormatScriptGenerator generator =
                new TextInputFormatScriptGenerator(resolverMock,
                        new TextInputFormatParser(false));

        XFTextInputAttributes attributes = new XFTextInputAttributes();
        attributes.setName("abc");
        attributes.setErrmsg(new LiteralTextAssetReference("error message"));
        attributes.setCaption(new LiteralTextAssetReference("caption"));
        attributes.setStyles(StylesBuilder.getSparseStyles(
                "mcs-input-format: \"M:M\""));

        StringBuffer buffer = new StringBuffer();
        generator.writeJavaScriptValidation(attributes, buffer);
        assertEquals("if(!(new RegEx(\"Z\",form.abc.value).match())) {\n" +
                " errMsg += \"\\nerror message\";}\n", buffer.toString());
    }
}

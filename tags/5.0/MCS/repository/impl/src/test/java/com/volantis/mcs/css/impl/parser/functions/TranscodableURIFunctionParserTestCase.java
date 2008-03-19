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
package com.volantis.mcs.css.impl.parser.functions;

import com.volantis.mcs.themes.StyleSheetTester;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StyleTranscodableURI;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Tests for mcs-transcodable-url() function.
 */
public class TranscodableURIFunctionParserTestCase extends TestCaseAbstract {

    public void testBackgroundImage() {
        final StyleProperties properties =
            StyleSheetTester.parseProperties("background-color: #FFFCCC; " +
                "background-image: mcs-transcodable-url(" +
                "'http://localhost:8080/image.gif')");
        final PropertyValue backgroundImage =
            properties.getPropertyValue(StylePropertyDetails.BACKGROUND_IMAGE);
        final StyleTranscodableURI transcodableURI =
            (StyleTranscodableURI) backgroundImage.getValue();
        assertEquals("http://localhost:8080/image.gif",
            transcodableURI.getUri());
    }
}

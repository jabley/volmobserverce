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

package com.volantis.mcs.css.impl.parser.properties;

import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.ContentKeywords;

import java.util.ArrayList;
import java.util.List;

/**
 * Parser for content property.
 */
public class ContentParserTestCase
    extends ParsingPropertiesMockTestCaseAbstract {

    /**
     * Test normal as the content.
     */
    public void testNormal() throws Exception {

        StyleValue content = ContentKeywords.NORMAL;

        expectSetProperty(StylePropertyDetails.CONTENT, content);

        parseDeclarations("content: normal");

    }

    /**
     * Test none as the content.
     */
    public void testNone() throws Exception {

        StyleValue content = ContentKeywords.NONE;

        expectSetProperty(StylePropertyDetails.CONTENT, content);

        parseDeclarations("content: none");

    }

    /**
     * Test a single string as the content.
     */
    public void testSingleString() throws Exception {

        List list = new ArrayList();
        list.add(STYLE_VALUE_FACTORY.getString(null, ">"));
        StyleValue content = STYLE_VALUE_FACTORY.getList(list);

        expectSetProperty(StylePropertyDetails.CONTENT, content);

        parseDeclarations("content: \">\"");

    }

    /**
     * Test a single function as the content.
     */
    public void testSingleFunction() throws Exception {

        List list = new ArrayList();
        list.add(STYLE_VALUE_FACTORY.getFunctionCall(
                null, "xyz", new ArrayList()));
        StyleValue content = STYLE_VALUE_FACTORY.getList(list);

        expectSetProperty(StylePropertyDetails.CONTENT, content);

        parseDeclarations("content: xyz()");
    }

    /**
     * Test a single url as the content.
     */
    public void testSingleURL() throws Exception {

        List list = new ArrayList();
        list.add(STYLE_VALUE_FACTORY.getURI(null, "/image.png"));
        StyleValue content = STYLE_VALUE_FACTORY.getList(list);

        expectSetProperty(StylePropertyDetails.CONTENT, content);

        parseDeclarations("content: url('/image.png')");
    }

    /**
     * Test a single component url as the content.
     */
    public void testSingleComponentURL() throws Exception {

        List list = new ArrayList();
        list.add(STYLE_VALUE_FACTORY.getComponentURI(null, "/image.mimg"));
        StyleValue content = STYLE_VALUE_FACTORY.getList(list);

        expectSetProperty(StylePropertyDetails.CONTENT, content);

        parseDeclarations("content: mcs-component-url('/image.mimg')");
    }

    /**
     * Test a single transcodable url as the content.
     */
    public void testSingleTranscodableURL() throws Exception {

        List list = new ArrayList();
        list.add(STYLE_VALUE_FACTORY.getTranscodableURI(null, "/image.mimg"));
        StyleValue content = STYLE_VALUE_FACTORY.getList(list);

        expectSetProperty(StylePropertyDetails.CONTENT, content);

        parseDeclarations("content: mcs-transcodable-url('/image.mimg')");
    }

    /**
     * Test a mixture of all of them.
     */
    public void testMixture() throws Exception {

        List list = new ArrayList();
        list.add(STYLE_VALUE_FACTORY.getString(null, ">"));
        list.add(STYLE_VALUE_FACTORY.getFunctionCall(null, "xyz", new ArrayList()));
        list.add(STYLE_VALUE_FACTORY.getURI(null, "/image.png"));
        list.add(STYLE_VALUE_FACTORY.getComponentURI(null, "/image.mimg"));
        list.add(STYLE_VALUE_FACTORY.getTranscodableURI(null, "/image.mimg"));
        StyleValue content = STYLE_VALUE_FACTORY.getList(list);

        expectSetProperty(StylePropertyDetails.CONTENT, content);

        parseDeclarations("content: '>' xyz() url('/image.png') " +
            "mcs-component-url('/image.mimg') " +
            "mcs-transcodable-url('/image.mimg')");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 ===========================================================================
*/

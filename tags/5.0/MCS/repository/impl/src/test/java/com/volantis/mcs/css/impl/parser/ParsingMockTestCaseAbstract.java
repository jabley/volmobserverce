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

package com.volantis.mcs.css.impl.parser;

import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.mcs.themes.StyleSheetFactoryMock;

/**
 * Base for test classes that test parsing using mocks.
 */
public abstract class ParsingMockTestCaseAbstract
        extends ParsingTestCaseAbstract {

    protected StyleSheetFactoryMock styleSheetFactoryMock;

    protected void setUp() throws Exception {
        super.setUp();

        styleSheetFactoryMock = new StyleSheetFactoryMock(
                "styleSheetFactoryMock", expectations);
    }

    protected StyleSheetFactory getStyleSheetFactory() {
        return styleSheetFactoryMock;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Sep-05	9487/4	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/

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
package com.volantis.mcs.dom2theme.impl;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.debug.StyledDocumentLogger;
import com.volantis.mcs.dom2theme.StyledDOMThemeExtractor;
import com.volantis.mcs.dom2theme.impl.generator.DOMThemeGenerator;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementList;
import com.volantis.mcs.dom2theme.impl.optimizer.StyledDOMOptimizer;
import com.volantis.mcs.themes.StyleSheet;

public class DefaultStyledDOMThemeExtractor
        implements StyledDOMThemeExtractor {


    private StyledDOMOptimizer optimizer;

    private DOMThemeGenerator generator;

    public DefaultStyledDOMThemeExtractor(StyledDOMOptimizer optimizer,
            DOMThemeGenerator generator) {
        this.optimizer = optimizer;
        this.generator = generator;
    }

    public StyleSheet extract(Document styledDom) {
        StyledDocumentLogger.logDocument(styledDom);

        // StyledDOMOptimizer -> OutputStyledElements -> OutputStyledThemeGenerator

        OutputStyledElementList elementList = optimizer.optimize(styledDom);

        return generator.generateStyleSheetFor(elementList);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 28-Nov-05	10443/1	ianw	VBM:2005111812 interim commit for IB

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 18-Jul-05	8668/3	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/

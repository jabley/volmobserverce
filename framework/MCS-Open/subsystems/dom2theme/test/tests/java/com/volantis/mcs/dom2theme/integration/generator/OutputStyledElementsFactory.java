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
package com.volantis.mcs.dom2theme.integration.generator;

import com.volantis.mcs.dom.WalkingDOMVisitorStub;
import com.volantis.mcs.dom.DOMWalker;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.WalkingDOMVisitor;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElement;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementList;
import com.volantis.mcs.dom2theme.impl.model.OutputStyles;
import com.volantis.styling.Styles;

public class OutputStyledElementsFactory {

    public OutputStyledElementList createOutputStyledElements(Document dom) {

        final OutputStyledElementList outputElementList =
                new OutputStyledElementList();

        // iterate over the dom, extracting all the elements into a list
        final OutputStylesFactory factory = new OutputStylesFactory();
        WalkingDOMVisitor visitor = new WalkingDOMVisitorStub() {
            public void visit(Element element) {
                OutputStyles outputStyles = null;
                Styles styles = element.getStyles();
                if (styles != null) {
                    outputStyles = factory.create(element.getName(), styles);
                }
                OutputStyledElement outputElement =
                        new OutputStyledElement(element, outputStyles);
                outputElementList.add(outputElement);
            }
        };
        DOMWalker walker = new DOMWalker(visitor);
        walker.walk(dom);
        return outputElementList;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Jul-05	8668/13	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/

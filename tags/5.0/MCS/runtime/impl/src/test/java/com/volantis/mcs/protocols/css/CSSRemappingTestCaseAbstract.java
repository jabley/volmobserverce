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

package com.volantis.mcs.protocols.css;

import com.volantis.mcs.dom.WalkingDOMVisitorStub;
import com.volantis.mcs.dom.DOMWalker;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.runtime.debug.StrictStyledDOMHelper;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public class CSSRemappingTestCaseAbstract extends TestCaseAbstract {

    /**
     * Return an &lt;img/&gt; element from a simple DOM.
     *
     * @return
     */
    protected Element createImgElement() {
        StrictStyledDOMHelper domHelper = new StrictStyledDOMHelper();
        Document dom = domHelper.parse(
                "<html>" +
                        "<head>" +
                        "<title>Hello</title>" +
                        "</head>" +
                        "<body>" +
                        "<div>" +
                        "<img src=\"some.png\" " +
                        "style=\"width: 53px; height: 41px\" />" +
                        "</div>" +
                        "</body>" +
                        "</html>");

        return selectImgElement(dom);
    }

    /**
     * Selects the first &lt;img/&gt; element from the specified Document.
     *
     * @param dom
     * @return
     */
    protected Element selectImgElement(Document dom) {

        final ElementSelectingVisitor visitor = new ElementSelectingVisitor() {

            // Javadoc inherited.
            public void visit(Element element) {
                if ("img".equals(element.getName()) && foundElement == null) {
                    this.foundElement = element;
                }
            }
        };

        final DOMWalker walker = new DOMWalker(visitor);
        walker.walk(dom);

        return visitor.foundElement;
    }

    /**
     * Return a <code>CSSRemappingRule</code> with the specied attribute name
     * and CSS property.
     *
     * @param attName
     * @param cssProperty
     * @return a CSSRemappingRule
     */
    protected CSSRemappingRule createCSSRemappingRule(
            final String attName,
            final String cssProperty) throws InvalidExpressionException {
        return new CSSRemappingRule(attName,
                "length(css('" + cssProperty + "'),'px')");
    }

    /**
     * Helper class to select an element from a DOM. It could also be done
     * using getHead/getTail, but that isn't as clear.
     */
    public static class ElementSelectingVisitor extends WalkingDOMVisitorStub {

        public Element foundElement;

    }
}

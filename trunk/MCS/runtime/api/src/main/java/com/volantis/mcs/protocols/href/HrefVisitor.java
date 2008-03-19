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
package com.volantis.mcs.protocols.href;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.WalkingDOMVisitorStub;

import java.util.HashMap;

/**
 * Visitor used to convert hrefs on XHTM2 elements into anchor elements.
 */
public class HrefVisitor extends WalkingDOMVisitorStub {

    private static final String HREF = "href";

    protected static final HrefInsideRule INSIDE = new HrefInsideRule();
    protected static final HrefOutsideRule OUTSIDE = new HrefOutsideRule();
    protected static final HrefIgnoreRule IGNORE = new HrefIgnoreRule();
    protected static final HrefPushDownRule PUSH_DOWN = new HrefPushDownRule();

    private HashMap ruleSet = null;


    /**
     * Create a visitor loaded with the supplied ruleset.
     *
     * @param ruleSet rule set used to transform the DOM. Assumed not to be
     *                null
     */
    public HrefVisitor(HashMap ruleSet) {
        this.ruleSet = ruleSet;
    }

    /**
     * Check the given element, if it contains a href attribute
     * look up the defined rule and call the transform method on it.
     *
     * @param element current element
     */
    public void visit(Element element) {
        String href = element.getAttributeValue(HREF);

        if (href != null) {

            HrefRule rule = (HrefRule)ruleSet.get(element.getName());

            if (rule != null) {
                rule.transform(element);
            }

        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9128/3	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 ===========================================================================
*/

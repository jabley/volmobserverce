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
package com.volantis.mcs.protocols;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.Text;

/**
 * This class is responsible for adding a whitespace character followed by
 * 3 "&nbsp;" before and after an element.
 */
public class SpaceAndNonBreakingSpacesOutsideStrategy
    extends AbstractWhiteSpaceFixStrategy {

    // javadoc inherited
    public void fixUpSpace(Element element, boolean isOpen) {

        if (isOpen) {
            appendTextToPreviousElement(element);
        } else {
            prependTextToNextElement(element);

        }
    }

    /**
     * Append the text to the previouse element. Does nothing if previous
     * element is not a Text element.
     *
     * @param element being fixed.
     */
    private void appendTextToPreviousElement(Element element) {
        Node node = element.getPrevious();
        if (node instanceof Text) {
            Text text = (Text)node;

            text.append(SINGLE_WHITESPACE);
            text.append(NON_BREAKING_SPACE);
            text.append(NON_BREAKING_SPACE);
            text.append(NON_BREAKING_SPACE);
        }
    }

    /**
     * Prepend spaces to next element. Does nothing if next element is
     * not a Text element.
     *
     * @param element being fixed.
     */
    private void prependTextToNextElement(Element element) {
        Node node = element.getNext();
        if (node instanceof Text) {
            prependText((Text)node, SINGLE_WHITESPACE + NON_BREAKING_SPACE +
                                    NON_BREAKING_SPACE + NON_BREAKING_SPACE);
        }
    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10803/2	pabbott	VBM:2005120901 Fix white space problem on LG-C1100

 ===========================================================================
*/

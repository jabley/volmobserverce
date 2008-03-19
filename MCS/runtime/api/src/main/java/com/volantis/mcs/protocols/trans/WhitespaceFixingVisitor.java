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
package com.volantis.mcs.protocols.trans;

import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.protocols.AbstractTransformingVisitor;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.WhiteSpaceFixStrategy;
import com.volantis.mcs.protocols.WhiteSpaceFixStrategyFactory;

import java.util.Set;

/**
 * This class is responsible for applying instances of
 * {@link WhiteSpaceFixStrategy} in order to manipulate the
 * document in such a way that spacing is rendered correctly when
 * inline styling elements and anchor elements are used on devices that
 * do not render whitespace correctly with these elements.
 */
public final class WhitespaceFixingVisitor
        extends AbstractTransformingVisitor {

    /**
     * Contains the name of inline styling elements that do not honour
     * whitespace
     */
    private final Set inlineStyleElements;

    /**
     * Contains the name of link based elements that do not honour whitespace
     */
    private final Set inlineLinkElements;

    /**
     * Strategy used for open anchor elements
     */
    private final WhiteSpaceFixStrategy openAnchorStrategy;

    /**
     * Strategy used for closing anchor elements
     */
    private final WhiteSpaceFixStrategy closeAnchorStrategy;

    /**
     * Strategy used for open inlined style elements
     */
    private final WhiteSpaceFixStrategy openStyleStrategy;

    /**
     * Strategy used for closing inline style elements
     */
    private final WhiteSpaceFixStrategy closeStyleStrategy;

    /**
     * Constructor for <code>WhiteSpaceFixVisitor</code> instances.
     *
     * @param inlineStyleElements Contains the name of inline styling elements
     * that do not honour whitespace
     * @param inlineLinkElements Contains the name of link based elements that
     * do not honour whitespace
     * @param protocol the associated protocol
     */
    public WhitespaceFixingVisitor(Set inlineStyleElements,
                                   Set inlineLinkElements,
                                   DOMProtocol protocol) {
        this.inlineStyleElements = inlineStyleElements;
        this.inlineLinkElements = inlineLinkElements;
        WhiteSpaceFixStrategyFactory factory =
                WhiteSpaceFixStrategyFactory.getInstance();

        String openAnchorStrategyRule =
                protocol.getMarinerPageContext().getDevicePolicyValue(
                        DevicePolicyConstants.FIX_FOR_OPEN_ANCHOR_ELEMENT);
        openAnchorStrategy =
                factory.getWhitespaceFixStrategy(openAnchorStrategyRule);

        String closeAnchorStrategyRule =
                protocol.getMarinerPageContext().getDevicePolicyValue(
                     DevicePolicyConstants.FIX_FOR_CLOSING_ANCHOR_ELEMENT);
        closeAnchorStrategy =
                factory.getWhitespaceFixStrategy(closeAnchorStrategyRule);


        String openStyleStrategyRule =
                protocol.getMarinerPageContext().getDevicePolicyValue(
                        DevicePolicyConstants.FIX_FOR_OPEN_INLINE_STYLING_ELEMENTS);
        openStyleStrategy =
                factory.getWhitespaceFixStrategy(openStyleStrategyRule);

        String closeStyleStrategyRule =
                protocol.getMarinerPageContext().getDevicePolicyValue(
                        DevicePolicyConstants.FIX_FOR_CLOSING_INLINE_STYLING_ELEMENTS);
        closeStyleStrategy =
                factory.getWhitespaceFixStrategy(closeStyleStrategyRule);


    }

    // javadoc inherited
    public void visit(Element element) {

        // Does the current element belong to the list of elements
        // that need to have some sort of space added?
        if (element.getPrevious() instanceof Text) {
            Text text = (Text) element.getPrevious();
            WhiteSpaceFixStrategy strategy =
                    getWhiteSpaceFixStrategy(element, true);
            int whitespaceCount = 0;
            if (strategy != null &&
                (whitespaceCount =
                    removeAndCountTrailingWhiteSpace(text)) > 0) {
                for (int i=0; i<whitespaceCount; i++) {
                    strategy.fixUpSpace(element, true);
                }
            }
        }

        if (element.getNext() instanceof Text) {
            Text text = (Text) element.getNext();
            WhiteSpaceFixStrategy strategy =
                    getWhiteSpaceFixStrategy(element, false);
            int whitespaceCount = 0;
            if (strategy != null &&
                    (whitespaceCount =
                    removeAndCountLeadingWhiteSpace(text)) > 0) {
                for (int i=0; i<whitespaceCount; i++) {
                    strategy.fixUpSpace(element, false);
                }
            }
        }

        // visit the children
        element.forEachChild(this);
    }

    /**
     * Returns the whitespace fixing strategy for the given element or null
     * if one is not applicable.
     *
     * @param elmement the candidate element
     * @param isOpen true if this is being applied on the opening of the
     * element
     * @return the <code>WhiteSpaceFixStrategy</code> or null if one is not
     * needed
     */
    private WhiteSpaceFixStrategy getWhiteSpaceFixStrategy(Element elmement,
                                                           boolean isOpen) {
        WhiteSpaceFixStrategy strategy = null;
        if (inlineStyleElements.contains(elmement.getName())) {
            strategy = (isOpen) ? openStyleStrategy : closeStyleStrategy;
        } else if (inlineLinkElements.contains(elmement.getName())) {
            strategy = (isOpen) ? openAnchorStrategy : closeAnchorStrategy;
        }
        return strategy;
    }

    /**
     * Removes and returns a count of any leading whitespace that the Text
     * node may contain.
     *
     * @param text the node that any leading whitespace will be removed
     * from
     * @return the number of whitespace characters removed.
     */
    private int removeAndCountLeadingWhiteSpace(Text text) {
        char[] contents = text.getContents();
        int length = text.getLength();
        boolean isWhitespace = true;
        int whitespaceCount = 0;
        if (contents != null && length > 0) {
            for (int i = 0; i<length && isWhitespace; i++) {
                isWhitespace = Character.isWhitespace(contents[i]);
                if (isWhitespace) {
                    whitespaceCount++;
                }
            }
        }
        if (whitespaceCount > 0) {
            text.clearContents();
            text.append(contents,
                        whitespaceCount,
                        length - whitespaceCount);
        }
        return whitespaceCount;
    }

    /**
     * Removes and returns a count of any trailing whitespace that the Text
     * node may contain.
     *
     * @param text the node that any trailing whitespace will be removed
     * from
     * @return the number of whitespace characters removed.
     */
    private int removeAndCountTrailingWhiteSpace(Text text) {
        char[] contents = text.getContents();
        int whitespaceCount = 0;
        int length = text.getLength();
        boolean isWhitespace = true;
        if (length > 0) {
            for (int i=length-1; i>=0 && isWhitespace; i--) {
                isWhitespace = Character.isWhitespace(contents[i]);
                if (isWhitespace) {
                    whitespaceCount++;
                }
            }
        }
        if (whitespaceCount > 0) {
            text.clearContents();
            text.append(contents, 0, length - whitespaceCount);
        }
        return whitespaceCount;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10803/2	pabbott	VBM:2005120901 Fix white space problem on LG-C1100

 08-Dec-05	10675/3	pduffin	VBM:2005110905 Ported forward some white space fixes from 3.2.3

 03-Aug-05	9139/3	doug	VBM:2005071403 Fixed whitespace issues

 02-Aug-05	9139/1	doug	VBM:2005071403 Finished off whitespace fixes

 ===========================================================================
*/

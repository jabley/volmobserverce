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
 * $Header: /src/voyager/com/volantis/mcs/protocols/wml/WMLDOMTransformer.java,v 1.11 2003/03/20 15:15:33 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Feb-02    Paul            VBM:2002021802 - Created. See class comment
 *                              for details.
 * 03-Apr-02    Paul            VBM:2002021802 - The addParagraphs method now
 *                              correctly wraps text nodes.
 * 09-May-02    Ian             VBM:2002031203 - Changed log4j to use string.
 * 05-Jun-02    Adrian          VBM:2002021103 - Added methods getNextChild,
 *                              getRealNode, isValidParentForKeeptogether,
 *                              promoteKeeptogether.
 * 10-Jun-02    Adrian          VBM:2002021103 - Changed keeptogether to
 *                              keepTogether.
 * 05-Dec-02    Allan           VBM:2002112906 - Added fixTable() and a call
 *                              to this from transformElement(). Also added
 *                              countTableColumns(), removeTable() and
 *                              removeChildTableCells().
 * 16-Jan-03    Chris W         VBM:2002111508 - After calling transformNode,
 *                              transform calls removeUnnecessaryParagraphs.
 *                              removeTable removes the table element
 *                              immediately after insert the tables children.
 *                              Again this prevents unnecessary paragraphs
 *                              being left behind.
 * 24-Jan-03    Geoff           VBM:2003012302 - Back out chris's change above,
 *                              apart from the remoteTable() fix, then add
 *                              boolean trimEmptyNodes to promoteNode(), modify
 *                              it to call the new element.promote(), and
 *                              modify all existing references to pass false
 *                              except for promoteParagraph where we pass true.
 * 13-Mar-03    Phil W-S        VBM:2003031110 - Updated addParagraphs to use
 *                              the protocol's configuration to determine which
 *                              elements are permitted as children of a card.
 *                              Changed indentation of this method's content.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 26-May-03    Chris W         VBM:2003052205 - dividehints converted into
 *                              keeptogethers by recursing through Document
 *                              passed into transform()
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.wml;

import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DOMTransformer;
import com.volantis.mcs.protocols.EmulateEmphasisTag;
import com.volantis.mcs.protocols.dissection.DissectionConstants;
import com.volantis.mcs.protocols.styles.DefaultingPropertyHandler;
import com.volantis.mcs.protocols.styles.KeywordValueHandler;
import com.volantis.mcs.protocols.styles.PropertyHandler;
import com.volantis.mcs.protocols.styles.ValueHandlerToPropertyAdapter;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.TextAlignKeywords;
import com.volantis.mcs.themes.properties.WhiteSpaceKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.synergetics.ObjectHelper;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.HashMap;

/**
 * This class fixes up the dom so it is valid WML.
 * <p/>
 * It does the same job that the tree transformer did in WMLContentTree.
 * </p>
 */
public final class WMLDOMTransformer implements DOMTransformer, WMLConstants,
        DissectionConstants {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(WMLDOMTransformer.class);

    private static final StylingFactory stylingFactory = StylingFactory.getDefaultInstance();

    /**
     * The mappings between element names and the prefix used to access all
     * values relating to the wml tag emulation.
     */
    private static final HashMap emulatedElements;

    // Initialise the static fields.
    static {
        emulatedElements = new HashMap();
        emulatedElements.put("big", DevicePolicyConstants.EMULATE_WML_BIG_TAG);
        emulatedElements.put("b", DevicePolicyConstants.EMULATE_WML_BOLD_TAG);
        emulatedElements.put("em",
                             DevicePolicyConstants.EMULATE_WML_EMPHASIZE_TAG);
        emulatedElements.put("i", DevicePolicyConstants.EMULATE_WML_ITALIC_TAG);
        emulatedElements.put("small",
                             DevicePolicyConstants.EMULATE_WML_SMALL_TAG);
        emulatedElements.put("strong",
                             DevicePolicyConstants.EMULATE_WML_STRONG_TAG);
        emulatedElements.put("u",
                             DevicePolicyConstants.EMULATE_WML_UNDERLINE_TAG);
        emulatedElements.put(LINK_ELEMENT,
                             DevicePolicyConstants.EMULATE_WML_LINK_HIGHLIGHTING);
        emulatedElements.put(ANCHOR_ELEMENT,
                             DevicePolicyConstants.EMULATE_WML_LINK_HIGHLIGHTING);
        emulatedElements.put(CARD_ELEMENT,
                             DevicePolicyConstants.EMULATE_WML_CARD_TITLE);
    }

    private final PropertyHandler paragraphAlignHandler;

    private final PropertyHandler paragraphModeHandler;

    /**
     * Specifies that the device honours alignment on paragraph tags when
     * mode="nowrap"
     */
    private final boolean deviceHonoursAlignOnParagraphWhenModeNowrap;

    private final DOMFactory factory;
    private final WMLRootConfiguration configuration;

    public WMLDOMTransformer(WMLRoot protocol, boolean honoursAlignment) {

        // Initialise the style value handlers.
        paragraphAlignHandler = new ValueHandlerToPropertyAdapter(
            StylePropertyDetails.TEXT_ALIGN,
            new KeywordValueHandler(
                    WMLParagraphAlignKeywordMapper.getDefaultInstance()));

        paragraphModeHandler = new DefaultingPropertyHandler(
                new ValueHandlerToPropertyAdapter(
                        StylePropertyDetails.WHITE_SPACE,
                        new KeywordValueHandler(WMLParagraphModeKeywordMapper
                                .getDefaultInstance())), "wrap");

        deviceHonoursAlignOnParagraphWhenModeNowrap = honoursAlignment;

        this.factory = protocol.getDOMFactory();

        // The protocol must use a WMLRootConfiguration derivation
        configuration = (WMLRootConfiguration) protocol.getProtocolConfiguration();

    }

    /**
     * Transforms the Document passed in.
     *
     * @param protocol The DOMProtocol used to render the Document.
     * @param document The Document to be transformed.
     */
    public Document transform(DOMProtocol protocol, Document document) {

        // Iterate through the tree to do processing which must happen before
        // the main processing.
        // Currently this is converting dividehints into keeptogethers.
        for (Node node = document.getContents(); node != null;
                node = node.getNext()) {
            iterateBefore(node);
        }

        // Iterate through the tree to convert the DOM to valid WML.
        for (Node node = document.getContents(); node != null;
                node = node.getNext()) {
            transformNode(node);
        }

        // Iterate through the tree to do processing which happens after the
        // main processing.
        // Currently this is converting logical block elements to a combination
        // of <p> and <br>.
        for (Node node = document.getContents(); node != null;
                node = node.getNext()) {
            node = iterateAfter(node);
        }

        return document;
    }

    /**
     * Iterates through the children of the node passed in.
     * <p>
     * This iteration happens before the main iteration.
     *
     * @param node
     */
    private void iterateBefore(Node node) {
        if (node instanceof Element) {
            Element element = (Element) node;

            // Do the pre-processing.
            convertDivideHintToKeepTogether(element);

            // Iterate recursively through the children of this element.
            Node child = element.getHead();
            while (child != null) {
                iterateBefore(child);
                child = child.getNext();
            }
        }
    }

    /**
     * Iterates through the children of the node passed in.
     * <p>
     * This iteration happens after the main iteration.
     *
     * @param node
     */
    private Node iterateAfter(Node node) {
        if (node instanceof Element) {
            Element element = (Element) node;

            // Iterate recursively through the children of this element.
            Node child = element.getHead();
            while (child != null) {
                child = iterateAfter(child);
                child = child.getNext();
            }

            // Now that we have rendered any child logical block elements into
            // <p> and <br> we can know where the <p>s are and can emulate
            // their styles appropriately. As an optimisation we only need to
            // do this for elements which may contain blocks/paragraphs.
            if (isValidParentForBlock(element.getName())) {
                emulateParagraphStyles(element);
            }

            // Now emulate this element
            element = emulateBlockElement(element);

            node = element;
        }

        return node;
    }

    /**
     * If the element passed in is a dividehint convert it to a keeptogether
     *
     * @param element
     */
    private void convertDivideHintToKeepTogether(Element element) {
        if (DIVIDE_HINT_ELEMENT.equals(element.getName())) {
            if (logger.isDebugEnabled()) {
                logger.debug("converting " + element + " into " +
                             KEEPTOGETHER_ELEMENT);
            }

            // Convert dividehint into a keeptogether element.
            element.setName(KEEPTOGETHER_ELEMENT);

            // A valid child of a keep togther element is any node except for
            // a zero length piece of text and a dividehint (which would be
            // converted into another keep together.
            boolean validChild = false;
            while (!validChild) {
                // Get the next node.
                Node next = element.getNext();

                if (next != null) {
                    if (next instanceof Text) {
                        Text text = (Text) next;

                        // If the next node is a piece of zero length text, we
                        // remove it and surround the following node with the
                        // keeptogether element.
                        if (text.getLength() == 0) {
                            next = text.getNext();
                            text.remove();
                            // Get the next node
                            continue;
                        }
                    }

                    if (next instanceof Element) {
                        // Deal with consecutive dividehints e.g.
                        // <dividehint/><dividehint/><p>.....</p>
                        Element element2 = (Element) next;
                        if (DIVIDE_HINT_ELEMENT.equals(element2.getName())) {
                            element2.remove();
                            // Get the next node
                            continue;
                        }
                    }

                    // Remove the node from the current list.
                    next.remove();
                    // Add it back as a child of the keeptogether element.
                    element.addHead(next);
                }

                // We've found and moved a valid child of keeptogether or
                // it was null
                validChild = true;
            }
        }
    }

    /**
     * Convert the logical block element into a combination of P and BR
     * elements.
     * <p>
     * Note that this must happen once the final document structure is
     * in place.
     *
     * @param element
     * @return the new current element
     *
     * @todo move this out into style emulation proper
     */
    private Element emulateBlockElement(Element element) {

        // Default the current element to the element we start with.
        Element current = element;

        // Once the block is at the topmost level, it is time for us to
        // translate it from a logical block into a the appropriate WML to
        // represent it. WML has two things which we can use to introduce the
        // whitespace necessary to represent a block, <p> or <br/>. According
        // to Mr Homer, <br> is more reliable so we will use that where
        // possible. This means that we can join up sequences of <p>'s where
        // they have the same white-space and text-align and replace them with
        // one <p> with <br/> separators.

        // If this element is a logical block ..
        if (WMLConstants.BLOCH_ELEMENT.equals(element.getName())) {
            // ... then we need to render it using either <p> or <br>

            boolean canJoinBlockWithP = false;

            // If the preceding element was a p ...
            Element previous = (Element) element.getPrevious();
            if (previous != null && "p".equals(previous.getName())) {

                // Then we may be able to join this block onto the previous one
                // with a br. Let's check....

                // NOTE: this could be a bit more efficient but I am interestedf
                Styles previousStyles = getStylesForElement(previous);
                Styles elementStyles = getStylesForElement(element);
                String previousMode =
                        paragraphModeHandler.getAsString(previousStyles);
                String mode =
                        paragraphModeHandler.getAsString(elementStyles);

                String previousAlign =
                        paragraphAlignHandler.getAsString(previousStyles);
                String align =
                        paragraphAlignHandler.getAsString(elementStyles);

                // ... if white-space & text-align of the p & block match ...
                if (ObjectHelper.equals(mode, previousMode) &&
                        ObjectHelper.equals(align, previousAlign)) {
                    // ... then we should be able to join the p and block
                    // together to create a "super p" with a br as separator.

                    canJoinBlockWithP = true;
                } else {
                    // The style properties of this element and the previous
                    // are different, so we can't join them together as a
                    // single p.
                }
            } else {
                // There is no previous element, so we obviously can't join
                // this element with it.
            }

            if (canJoinBlockWithP) {
//                System.out.println("Joining \n" + DOMUtilities.toString(previous)
//                        + " with \n" + DOMUtilities.toString(element));

                // Before we join the blocks, we need to deal with the special
                // case where the first block ends with a <br/>. If we were not
                // joining blocks this would be rendered as <br/></p> and the
                // <br/> in this case is rendundant as the browser would ignore
                // the <br/> in this case. However, since we are about to
                // replace the </p> with <br/> the browser will not
                // automatically ignore it. So, we must remove it manually.
                removeTrailingLineBreak(current);

                // Change the block into a br element and discard its
                // attributes since they are not appropriate for a br.
                element.setName("br");
                element.clearAttributes();
                element.clearStyles();

                // Remove the br (nee block) and add it to the tail of the
                // previous paragraph.
                element.remove();
                previous.addTail(element);

                // Move the children from the br (nee block) to the
                // previous paragraph.
                element.addChildrenToTail(previous);

//                System.out.println("Joined \n" + DOMUtilities.toString(previous)
//                        + " with \n" + DOMUtilities.toString(element));

                // Set the current element to the joined p so we continue on
                // from the correct point.
                current = previous;

            } else {
                // We cannot join the block with a previous p so we render the
                // block as a p.
                element.setName("p");

                // br at the end of a p is rendundant so we may as well
                // remove it.
                removeTrailingLineBreak(element);
            }
        }

        return current;
    }

    private void removeTrailingLineBreak(Element element) {
        Node tail = element.getTail();
        if (tail instanceof Element) {
            Element tailElement = (Element) tail;
            if ("br".equals(tailElement.getName())) {
                tailElement.remove();
            }
        }
    }

    /**
     * Emulate white-space and text-align for paragraphs.
     * <p>
     * This method must be called after the logical block elements have
     * been rendered into either <p> or <br> elements.
     * <p>
     * NOTE: see VBM:2005090804 for how L3 implemented this for 3.2.3.
     *
     * @todo move this out into style emulation proper
     */
    private void emulateParagraphStyles(Element parent) {
        // The wrap mode of a paragraph defaults to the wrap mode of the
        // previous paragraph, or "wrap" if none of the previous paragraphs
        // specified a wrap mode. This is different to how that style is used
        // in JSP pages so the wrap mode is always set for every paragraph.
        // This means that many paragraphs have a wrap mode set when they don't
        // need it which is a waste of the limited space available in WML, so
        // this method removes unnecessary setting of the wrap mode off
        // paragraphs.
        //
        // The wrap mode of the previous paragraph, this is either "wrap",
        // "nowrap", or null which means not known. The default value for the
        // first paragraph is wrap.
        String previousMode = "wrap";

        for (Node node = parent.getHead(); node != null;
             node = getNextChild(node, parent)) {
            if (node instanceof Element) {
                Element child = (Element) node;

                String name = child.getName();
                if (logger.isDebugEnabled()) {
                    logger.debug("Checking element " + name);
                }

                if ("p".equals(name)) {

                    Styles styles = getStylesForElement(child);
                    String mode = paragraphModeHandler.getAsString(styles);
                    String align = paragraphAlignHandler.getAsString(styles);

                    // Emulate mode= using white-space.
                    // This inherits the value from the previous paragraph so
                    // we don't need to render it out if it is the same as the
                    // previous.

                    if (!deviceHonoursAlignOnParagraphWhenModeNowrap) {
                        if ("center".equals(align) || "right".equals(align)) {
                            mode = "wrap";
                        }
                    }

                    if (!mode.equals(previousMode)) {
                        child.setAttribute("mode", mode);
                    }
                    previousMode = mode;

                    // Emulate align= using text-align.
                    // This does not inherit from the previous paragraph as
                    // mode does and is correspondingly simpler.
                    if (align != null) {
                        child.setAttribute("align", align);
                    }

                } else if (DISSECTABLE_CONTENTS_ELEMENT.equals(name)) {
                    // The part of the dissectable contents which will be written out
                    // changes from shard to shard and so we cannot tell what the wrap
                    // mode of the last paragraph written out will be so we set it to
                    // the 'unknown' value.
                    previousMode = null;
                }
            }
        }
    }

    protected void transformElement(
            Element element) {
        String name = element.getName();

        if ("table".equals(name)) {
            fixTable(element);
        } else if (DISSECTABLE_CONTENTS_ELEMENT.equals(name)) {
            transformDissectableElement(element);
        } else if (WMLConstants.BLOCH_ELEMENT.equals(name)) {
            promoteBlock(element);
        } else if (KEEPTOGETHER_ELEMENT.equals(name)) {
            promoteKeepTogether(element);
        } else if (SHARD_LINK_GROUP_ELEMENT.equals(name)) {
            transformShardLinkGroupElement(element);
        } else if (isFormFieldElement(name)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Found form field element " + name +
                             ", removing any parent tables");
            }
            removeParentTables(element);
        }

        EmulateEmphasisTag tag = configuration.getEmulateEmphasisTag(name);
        if (tag != null) {
            if (CARD_ELEMENT.equals(name)) {
                insertCardTitle(element, tag);
            } else if (LINK_ELEMENT.equals(name) ||
                    ANCHOR_ELEMENT.equals(name)) {
                emulateLinkHighlighting(element, tag);
            } else {
                emulateElement(element, tag, true);
            }
        }

        transformChildren(element);

        if (CARD_ELEMENT.equals(name)) {
            fixCard(element);
        } else if ("td".equals(name)) {
            fixTableCell(element);
        } else if ("head".equals(name)) {
            fixHead(element);
        }
    }

    private void transformChildren(
            Element element) {

        Node next;
        Node child = element.getHead();
        for (; child != null; child = next) {
            next = child.getNext();
            transformNode(child);
        }
    }

    /**
     * Get the next sibling of the specified node unless the node is a
     * <code>DissectionConstants.KEEPTOGETHER_ELEMENT</code> in which case
     * get the first child of the node.
     *
     * @param node The <code>Node</code> to get the next sibling.
     * @param stop The <code>Node</code>
     */
    private Node getNextChild(Node node, Node stop) {

        if (node == null) {
            return null;
        } else if (node == stop) {
            if (node instanceof Element) {
                Element element = (Element) node;
                Node next = element.getHead();
                if (next instanceof Element) {
                    Element nextElement = (Element) next;
                    if (KEEPTOGETHER_ELEMENT.equals(nextElement.getName())) {
                        return getRealNode(nextElement.getHead());
                    }
                }
                return next;
            } else {
                return null;
            }
        }

        if (node instanceof Element) {
            Element element = (Element) node;
            if (KEEPTOGETHER_ELEMENT.equals(element.getName())) {
                return getRealNode(element.getHead());
            }
        }

        Node next = node.getNext();
        if (next == null) {
            Node parent = node.getParent();
            if (parent != null) {
                if (parent.equals(stop)) {
                    return null;
                } else {
                    return getRealNode(parent.getNext());
                }
            } else {
                return null;
            }
        } else if (next instanceof Element) {
            Element element = (Element) next;
            if (KEEPTOGETHER_ELEMENT.equals(element.getName())) {
                return getRealNode(element.getHead());
            }
        }
        return next;
    }

    /**
     * if the specified node is a KeepTogether Element then return the first
     * descendant node that is not a KeepTogether Element.
     *
     * @param node The node to test.
     * @return either the originally specified node or the first descendant
     *         which is not a KeepTogether Element.
     */
    private Node getRealNode(Node node) {
        if (node == null) {
            return null;
        }

        if (node instanceof Element) {
            Element element = (Element) node;
            if (KEEPTOGETHER_ELEMENT.equals(element.getName())) {
                return getRealNode(element.getHead());
            }
        }
        return node;
    }

    private void transformNode(Node node) {
        if (node instanceof Element) {
            transformElement((Element) node);
        } else if (node instanceof Text) {
            // Nothing to do.
        } else {
            throw new IllegalStateException("Unknown node: " + node);
        }
    }

    private boolean canSplitElement(Element element) {
        String name = element.getName();
        return !"td".equals(name);

    }

    /**
     * Transform the dissectable element.
     *
     * @param element The dissectable element to transform.
     */
    private void transformDissectableElement(
            Element element) {

        if (promoteSpecialElement(element)) {
            // Do this after we have moved the dissectable node upwards.
            transformChildren(element);

            fixDissectable(element);
        }
    }

    /**
     * Promotes special elements such as keep together, dissectable contents
     * and shard link group.
     *
     * @param element
     * @return boolean Returns true if we could actually promote the specified
     *         element.
     */
    private boolean promoteSpecialElement(Element element) {
        // If this element has no children then discard it.
        if (element.isEmpty()) {
            element.remove();

            return false;
        }

        boolean keepTogetherElement = false;
        if (KEEPTOGETHER_ELEMENT.equals(element.getName())) {
            keepTogetherElement = true;
        }

        Element parent = element.getParent();
        while (true) {
            String parentName = parent.getName();

            // If the parent is a valid parent for a paragraph then stop. This is the
            // exit point from the while loop. Yuck!
            if (keepTogetherElement) {
                if (isValidParentForKeepTogether(parentName)) {
                    return true;
                }
            } else {
                if (isValidParentForBlock(parentName)) {
                    return true;
                }
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Promoting element " + element);
            }

            // Move this node up, splitting out parent, remember whether the
            // parent element can be discarded.
            boolean discardParent = promoteNode(element, false);

            // Reuse the parent node if we can but otherwise create a clone of
            // it and then insert it as the parent of this element's children.
            Element child;
            if (discardParent) {
                child = parent;
                parent = null;
                discardParent = false;
            } else {
                child = factory.createElement();
                child.copy(parent);
            }

            element.addChildrenToTail(child);

            element.addTail(child);

            // Move up the tree.
            parent = element.getParent();
        }
    }

    /**
     * Promote a node.
     * <p/>
     * This makes sure that it is valid to split the parent element before
     * promoting the node.
     * </p>
     *
     * @param node The node to promote.
     */
    private boolean promoteNode(Node node, boolean trimEmptyNodes) {

        Element parent = node.getParent();

        // Make sure that it makes sense to split our parent.
        if (!canSplitElement(parent)) {
            throw new IllegalStateException("Cannot split " + parent);
        }

        return node.promote(trimEmptyNodes);
    }

    private boolean isValidParentForBlock(String parentName) {
        return (CARD_ELEMENT.equals(parentName)
                || DISSECTABLE_CONTENTS_ELEMENT.equals(parentName)
                || KEEPTOGETHER_ELEMENT.equals(parentName)
                || SHARD_LINK_GROUP_ELEMENT.equals(parentName)
                || "td".equals(parentName)
                || parentName == null);
    }

    /**
     * Move the specified paragraph node up the tree until it is a child
     * of a card node, or a child of a dissectable node.
     *
     * @param element The paragraph node.
     */
    private void promoteBlock(Element element) {

        // If this element has no children then discard it.
        if (element.isEmpty()) {
            element.remove();

            return;
        }

        Element parent = element.getParent();
        while (true) {
            String parentName = parent.getName();

            // If the parent is a valid parent for a paragraph then stop.
            if (isValidParentForBlock(parentName)) {
                break;
            }

            // We need to promote this paragraph. We do this by splitting our
            // parent node into two nodes, the first one contains all our preceding
            // siblings and the other one contains all our following siblings. We
            // then insert this paragraph node between them.
            //
            // If the parent is a paragraph node then we need to merge its
            // attributes into this paragraph node and then discard it.
            // e.g.
            //            C
            //            |                        C
            //       ... P01 ...                _-~|~-_
            //         /  |  \       -->  ... P01 P03 P01 ...
            //       T02 P03 T05               |   |   |
            //            |                   T02 T04 T05
            //           T04
            //
            // If the parent is not a paragraph node then it needs to swap position
            // with this paragraph node.
            // e.g.
            //            C                        C
            //            |                     _-~|~-_
            //       ... B01 ...          ... B01 P03 B01 ...
            //         /  |  \       -->       |   |   |
            //       T02 P03 T05              T02 B01 T05
            //            |                        |
            //           T04                      T04
            //
            //

            if (logger.isDebugEnabled()) {
                logger.debug("Promoting element " + element);
            }

            // Move this node up, splitting out parent, remember whether the
            // parent element can be discarded.
            // NOTE: this also trims any Ps created which contain only whitespace.
            boolean discardParent = promoteNode(element, true);

            if (WMLConstants.BLOCH_ELEMENT.equals(parentName)) {
                // Merge the attributes from the parent paragraph node into this
                // one, existing values are not changed.
                element.mergeAttributes(parent, false);
                // Merge styles in as well.
                Styles elementStyles = getStylesForElement(element);
                Styles parentStyles = getStylesForElement(parent);
                stylingFactory.getStylesMerger().merge(elementStyles,
                        parentStyles);
            } else {
                // Reuse the parent node if we can but otherwise create a clone of
                // it and then insert it as the parent of this element's children.
                Element child;
                if (discardParent) {
                    child = parent;
                    parent = null;
                    discardParent = false;
                } else {
                    child = factory.createElement();
                    child.copy(parent);
                }

                element.addChildrenToTail(child);

                element.addTail(child);
            }

            // Move up the tree.
            parent = element.getParent();
        }
    }

    private boolean isValidParentForKeepTogether(String parentName) {
        return (CARD_ELEMENT.equals(parentName)
                || DISSECTABLE_CONTENTS_ELEMENT.equals(parentName)
                || KEEPTOGETHER_ELEMENT.equals(parentName));
    }

    /**
     * Move the specified paragraph node up the tree until it is a child
     * of a card node, or a child of a dissectable node.
     *
     * @param element The paragraph node.
     */
    private void promoteKeepTogether(Element element) {
        promoteSpecialElement(element);
    }

    /**
     * Fix up the card element and its children so that they are valid wml.
     * This involves adding paragraphs around tags which are not
     * valid at this level and ensuring that the mode attributes of the
     * paragraphs are set correctly.
     *
     * @param card The card element.
     */
    private void fixCard(Element card) {

        // Make sure that all of the children are valid for a card, if none of
        // the children are paragraphs then we need to add one.
        if (!addBlocks(card)) {
            Element p = factory.createElement(WMLConstants.BLOCH_ELEMENT);
            card.addTail(p);
        }
    }

    private Styles getStylesForElement(Element element) {
        Styles styles = element.getStyles();
        // Styles should never be null, but at the moment we create
        // non-papi related content improperly with null styles.
        // Work around this by creating a styles here anyway.
        if (styles == null) {
            MutablePropertyValues values = stylingFactory.createPropertyValues(
                    StylePropertyDetails.getDefinitions());
            styles = stylingFactory.createStyles(values);
            element.setStyles(styles);
        }
        return styles;
    }

    /**
     * Fix up the head element and its children so that they are valid wml. This
     * involves making sure that there is at least one child element of type
     * access, link or meta otherwise the head element has to be removed.
     *
     * @param head The head element.
     */
    private void fixHead(Element head) {

        // Assume that this element is empty.
        boolean emptyHead = true;
        for (Node node = head.getHead(); node != null; node = getNextChild(
                node, head)) {
            if (node instanceof Element) {
                Element element = (Element) node;
                String name = element.getName();
                if ("access".equals(name) || "link".equals(name)
                        || "meta".equals(name)) {
                    emptyHead = false;
                    break;
                }
            }
        }

        if (emptyHead) {
            head.remove();
        }
    }

    /**
     * Count the columns in a table element by looking at the child elements
     * rather than the columns attribute
     *
     * @param tableElement The table element
     * @return The actual number of columns in the table element
     */
    private int countTableColumns(Element tableElement) {
        int actualCols = 0;
        for (Node r = tableElement.getHead(); r != null;
             r = getNextChild(r, tableElement)) {
            if (!(r instanceof Element)) {
                continue;
            }

            Element row = (Element) r;
            if (!row.getName().equals("tr")) {
                continue;
            }

            int cells = 0;
            for (Node c = row.getHead(); c != null; c = c.getNext()) {
                if (!(c instanceof Element)) {
                    continue;
                }

                Element column = (Element) c;
                String columnName = column.getName();
                if (!columnName.equals("td")) {
                    continue;
                }
                cells += 1;
            }

            // If the number of cells in this row exceed the current column
            // count then update the column count.
            if (cells > actualCols) {
                actualCols = cells;
            }
        }

        return actualCols;
    }

    /**
     * Remove child td tags - i.e. just first level descendents
     *
     * @param element The Element whose child td tags to remove.
     */
    private void removeChildTableCells(Element element) {

        Node next;
        for (Node node = element.getHead(); node != null; node = next) {
            next = node.getNext();

            if (node instanceof Element &&
                    "td".equals(((Element) node).getName())) {
                Element child = (Element) node;

                String name = child.getName();

                // If the child element is a table, row or cell which is not valid
                // inside a td then we need to remove it and add its children directly
                // to this node in its place.
                if (("table".equals(name)) || ("tr".equals(name))
                        || ("td".equals(name))) {

                    if (!child.isEmpty()) {
                        // Insert the children for the child after the child itself.
                        child.insertChildrenAfter(child);
                    }

                    // Now remove the child element.
                    child.remove();
                    child = null;
                }
            }
        }
    }

    /**
     * Remove a table. This should really only by used by fixTable().
     *
     * @param tableElement The table element to remove.
     */
    private void removeTable(Element tableElement) {
        // We need to remove the table - this is a complicated process.
        // All the <tr> tags need to change to <p>. They also need
        // to be transformed themselves to removed nested <p> and
        // nested tables. However, transformElement() could remove
        // the element from the tree during promoteParagraph() and
        // we cannot allow <td> children to be separated by <br>
        // tags - though descendents further down must not have this
        // restriction. Also, we must be careful with tds that appear nested
        // since they might become unnested. If you change this code be sure
        // to check that the unit tests still work.
        Node currentSibling = tableElement.getHead();
        Node lastSibling = tableElement.getTail();
        tableElement.insertChildrenAfter(tableElement);
        tableElement.remove();
        boolean done = false;
        while (!done) {
            if (currentSibling == lastSibling) {
                done = true;
            }
            Node nextSibling = currentSibling.getNext();
            if (currentSibling instanceof Element) {
                Element currentSiblingElement =
                        (Element) currentSibling;
                if (currentSiblingElement.getName().equals("tr")) {
                    currentSiblingElement.setName(WMLConstants.BLOCH_ELEMENT);
                    removeChildTableCells(currentSiblingElement);
                    transformElement(currentSiblingElement);
                }
            }
            currentSibling = nextSibling;
        }
    }

    /**
     * Ensure that the columns attribute is correct and if the correct value
     * is 1 then remove the table while maintaining its non-table tag contents.
     *
     * @param tableElement The table cell element.
     */
    protected void fixTable(Element tableElement) {

        // If the table contained columns that were empty then they
        // will not be in the table. However, the cols value does not
        // know this so it could be incorrect. Additionally, since the
        // original cols value could be incorrect this means that
        // the real number of columns could be 1 and if that is the
        // case, in WML there should be no table. In any case, if there
        // is actually only 1 column in the table then we want to remove the
        // table.

        // First count the columns...
        int actualCols = countTableColumns(tableElement);
        if (actualCols == 1) {
            removeTable(tableElement);
        } else {
            // Fix the columns attribute
            String colsString = new Integer(actualCols).toString();
            tableElement.setAttribute("columns", colsString);
        }
    }

    /**
     * Fix up the table cell element and its children so that they are valid wml.
     * This involves removing paragraphs as they are not valid at this level.
     *
     * @param element The table cell element.
     */
    private void fixTableCell(Element element) {
        removeParagraphs(element);
        removeNestedTables(element);
    }

    /**
     * This method filters out nested tables from the wml
     *
     * @param element The td node that cannot have and td, tr
     */
    private void removeNestedTables(Element element) {

        Element br = null;

        // This is true when a line break needs to be added and false otherwise.
        boolean addLineBreak = false;

        Node next;
        for (Node node = element.getHead(); node != null; node = next) {
            next = getNextChild(node, element);

            if (node instanceof Element) {
                Element child = (Element) node;

                String name = child.getName();

                if (logger.isDebugEnabled()) {
                    logger.debug("Checking node " + name);
                }

                // If the previous node was a tr tag which was removed then we
                // need to add a line break.
                if (addLineBreak) {
                    br = factory.createElement("br");
                    br.insertBefore(node);

                    addLineBreak = false;
                }

                // If the child element is a table, row or cell which is not valid
                // inside a td then we need to remove it and add its children directly
                // to this node in its place.
                if (("table".equals(name)) || ("tr".equals(name))
                        || ("td".equals(name))) {

                    if (!child.isEmpty()) {
                        // Make sure we remove any nested table elements in the child
                        // first.
                        removeNestedTables(child);

                        // Insert the children for the child after the child itself.
                        child.insertChildrenAfter(child);

                        // We need to add a line break as we have stripped out a
                        addLineBreak = true;
                    }

                    // Now remove the child element.
                    child.remove();
                    child = null;
                }
            }
        }
    }

    /**
     * Fix up the dissectable element and its children so that they will be valid
     * wml when they are dissected. This involves different things depending
     * on what the parent of the dissectable element is. If its parent is a card
     * element then paragraphs need to be added and if it is a table cell element
     * then paragraphs need to be removed.
     *
     * @param element The dissectable element.
     */
    private void fixDissectable(Element element) {

        Element parent = element.getParent();
        String parentName = parent.getName();

        if (CARD_ELEMENT.equals(parentName)) {
            // Make sure that all of the children are paragraphs.
            addBlocks(element);

        } else if ("td".equals(parentName)) {
            // Make sure that none of the children a re paragraphs.
            removeParagraphs(element);
        } else {
            throw new IllegalStateException("Dissectable node is child of "
                                            + parentName + " node");
        }
    }

    /**
     * Replace any paragraph nodes which are children of the specified node
     * with their children, effectively removing the paragraph node. Empty
     * paragraphs are completely ignored. Line breaks are added before and
     * after the paragraph's content, with the exceptions that they are not
     * added at the beginning of the node, at the end of the node, or
     * immediately after another added line break.
     *
     * @param element The node which cannot contain paragraphs.
     */
    private void removeParagraphs(Element element) {

        if (element.isEmpty()) {
            return;
        }

        Element br = null;

        // This is true when a line break needs to be added and false otherwise.
        boolean addLineBreak = false;

        // This is true when a line break has been added before the current node.
        // This is initialised to true to prevent a line break being added at
        // the start.
        boolean addedLineBreak = true;

        Node next;
        for (Node node = element.getHead(); node != null; node = next) {
            next = getNextChild(node, element);

            if (node instanceof Element) {
                Element child = (Element) node;

                String name = child.getName();

                if (logger.isDebugEnabled()) {
                    logger.debug("Checking element " + name);
                }

                // If the previous node was a paragraph which was removed then we
                // need to add a line break.
                if (addLineBreak) {
                    br = factory.createElement("br");
                    br.insertBefore(child);

                    addLineBreak = false;
                    addedLineBreak = true;
                }

                // If the child node is a paragraph then it is not valid as a child of
                // td so we need to remove it and add its children directly to this
                // node in its place.
                if (WMLConstants.BLOCH_ELEMENT.equals(name)) {
                    if (!child.isEmpty()) {

                        if (!addedLineBreak) {
                            br = factory.createElement("br");
                            br.insertBefore(child);
                        }

                        // Add all the children of child to the list after child.
                        child.insertChildrenAfter(child);
                    }

                    // We need to add a line break between the end of this paragraph
                    // and any other text.
                    addLineBreak = true;

                    // Remember that we did not just add a line break.
                    addedLineBreak = false;

                    // Remove the child.
                    child.remove();

                    // Discard
                    child = null;

                    // The node has been consumed.
                    continue;
                }
            } else if (node instanceof Text) {
                Text text = (Text) node;
                if (text.isWhitespace()) {
                    // The text node is pure whitespace so we don't need to add a
                    // line break after this.
                    continue;
                }
            }

            // Remember that we did not just add a line break.
            addedLineBreak = false;
        }
    }

    /**
     * Add paragraphs around any of the children which are not valid children
     * of a card element. The number of extra paragraphs added is minimised by
     * adding one paragraph around each group of invalid children, rather than
     * each child having their own paragraph.
     *
     * @param card     The element which can contain paragraphs.
     * @return True if the parent now contains any paragraphs and false
     *         otherwise.
     */
    protected boolean addBlocks(Element card) {

        if (card.isEmpty()) {
            return false;
        }

        Element wrapper = null;
        boolean containsBlock = false;
        boolean wrapperNeeded;
        Node next;

        for (Node child = card.getHead(); child != null;
             child = next) {
            next = getNextChild(child, card);

            if (child instanceof Element) {
                Element element = (Element) child;
                String name = element.getName();

                // If we found a block element or an element which will contain
                // a block element...
                if (WMLConstants.BLOCH_ELEMENT.equals(name) ||
                        DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT.equals(name)) {
                    // Then say we contain blocks
                    containsBlock = true;
                    // And this element does not need wrapping.
                    wrapperNeeded = false;
                } else if (!DISSECTABLE_CONTENTS_ELEMENT.equals(name) &&
                        !DIVIDE_HINT_ELEMENT.equals(name) &&
                        !KEEPTOGETHER_ELEMENT.equals(name) &&
                        !DissectionConstants.SHARD_LINK_GROUP_ELEMENT.equals(
                                name) &&
                        !configuration.isPermittedCardChild(name)) {
                    wrapperNeeded = true;
                } else {
                    wrapperNeeded = false;
                }
            } else if (child instanceof Text) {
                wrapperNeeded = true;
            } else {
                throw new IllegalStateException("Unknown node " + child);
            }

            if (wrapperNeeded) {
                // If the wrapper paragraph is not still open then open one.
                if (wrapper == null) {
                    wrapper = factory.createElement(WMLConstants.BLOCH_ELEMENT);
                    // Find the styles for the wrapper. For now we just create
                    // an empty one, but really we should have a BLOCK element
                    // here for the content which would have the real styles
                    // on it.
                    MutablePropertyValues values =
                            stylingFactory.createPropertyValues(
                                    StylePropertyDetails.getDefinitions());
                    values.setComputedValue(StylePropertyDetails.WHITE_SPACE,
                            WhiteSpaceKeywords.NORMAL);
                    wrapper.setStyles(stylingFactory.createStyles(values));

                    if (logger.isDebugEnabled()) {
                        logger.debug("Opened new wrapper paragraph");
                    }
                    wrapper.insertAfter(child);
                }

                // Remove the child from the element.
                child.remove();

                if (logger.isDebugEnabled()) {
                    logger.debug("Adding " + child + " to wrapper paragraph");
                }

                wrapper.addTail(child);

                containsBlock = true;
            } else {
                // Close the wrapper paragraph.
                if (logger.isDebugEnabled()) {
                    logger.debug("Closed wrapper paragraph");
                }
                wrapper = null;
            }
        }

        return containsBlock;
    }

    /**
     * Transform the shard link group special element
     *
     * @param element The shard link group special element to transform.
     */
    private void transformShardLinkGroupElement(
            Element element) {

        if (promoteSpecialElement(element)) {
            // Do this after we have moved the shard link group element upwards.
            transformChildren(element);
        }
    }

    /**
     * Returns true if the element name supplied is one of the WML form field
     * elements.
     *
     * @param elementName the name of the element to check
     * @return true if the name supplied was a form field element name, false
     *         otherwise.
     */
    private boolean isFormFieldElement(String elementName) {
        return "input".equals(elementName) ||
                "select".equals(elementName) ||
                "fieldset".equals(elementName) ||
                "do".equals(elementName);
    }

    /**
     * Remove any containing parent tables of the element supplied.
     * <p/>
     * Currently this is used to remove tables which contain form fields as
     * these are invalid according to the WML DTD.
     * <p/>
     * In future we may wish to come up with a better solution to this problem.
     * <p/>
     * Note this only affects multiple column tables as single column grids in
     * the layout are not rendered using table elements.
     *
     * @param element the element to remove containing parent tables of.
     */
    private void removeParentTables(Element element) {

        // NOTE: This explicitly does not handle null element names, as it does
        // not treat the children of null elements as replacing the parent. As
        // far as I can tell, we should never normally have null element names
        // here - the only places which do explicitly add null element names
        // are other protocols' transformers.
        //
        // If this ever fails because of a null element name, the proper fix is
        // to prevent that name appearing in the first place rather than
        // hacking in here, as explained in VBM:2005011307.

        // NOTE: this would be a LOT easier to implement if we had internal
        // iterators that handled all the looping complexity for us.

        // Loop back up the DOM tree until we hit the last element before the
        // DOMOutputBuffer's root element. We must ignore that because it has
        // a null name.
        for (element = element.getParent();
             element != null && element.getParent() != null;
             element = element.getParent()) {

            if (element.getName() == null) {
                // See comment above.
                throw new IllegalStateException("null element names not " +
                                                "supported");
            }

            // If we found a table element...
            if ("table".equals(element.getName())) {
                // ... then loop down into the table, deleting the table
                // element and any contained td and td elements.

                if (logger.isDebugEnabled()) {
                    logger.debug("Found invalid " + element);
                }
                // Search for nested tr elements.
                boolean nextRow = false;
                Node rowNode = element.getHead();
                while (rowNode != null) {
                    if (rowNode instanceof Element) {
                        Element row = (Element) rowNode;
                        rowNode = rowNode.getNext();
                        if ("tr".equals(row.getName())) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Found invalid " + row);
                            }

                            // If we already did at least one row ...
                            if (nextRow) {
                                // ... then add a br element here to be
                                // consistent with the way that
                                // removeNestedTables works.
                                Element br = factory.createElement("br");
                                br.insertBefore(row);
                            }

                            // Search for nested td elements.
                            Node colNode = row.getHead();
                            while (colNode != null) {
                                if (colNode instanceof Element) {
                                    Element col = (Element) colNode;
                                    colNode = colNode.getNext();
                                    if ("td".equals(col.getName())) {
                                        if (logger.isDebugEnabled()) {
                                            logger.debug("Found invalid " +
                                                         col);
                                        }
                                        // Remove td element
                                        if (logger.isDebugEnabled()) {
                                            logger.debug("Deleting invalid " +
                                                         col);
                                        }
                                        deleteElement(col);
                                    } else {
                                        if (logger.isDebugEnabled()) {
                                            logger.debug("Searching for td, " +
                                                         "found " + col);
                                        }
                                    }
                                }
                            }
                            // Remove tr element
                            if (logger.isDebugEnabled()) {
                                logger.debug("Deleting invalid " + row);
                            }
                            deleteElement(row);
                            // Mark that we need a line break if we write
                            // another row.
                            nextRow = true;
                        } else {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Searching for tr, found " + row);
                            }
                        }
                    }
                }
                // Remove table element.
                if (logger.isDebugEnabled()) {
                    logger.debug("Deleting invalid " + element);
                }
                deleteElement(element);
            }
        }
    }

    /**
     * Safely delete a single element, maintaining the DOM structures that
     * surround it.
     * <p/>
     * In particular, this will add all the children of the element to be
     * deleted in place of the element to be deleted, so that the children of
     * the element are still part of the overall DOM tree.
     *
     * @param element the element to delete.
     * @return the node that takes it place in the DOM (the first child), or
     *         null if there were no children.
     */
    private Node deleteElement(Element element) {
        Node firstChild = element.getHead();
        element.insertChildrenAfter(element);
        element.remove();
        return firstChild;
    }

    /**
     * Replaces the element name and inserts a prefix and suffix if these
     * values are specified in the device policy (encapsulated in the
     * EmulateEmphasisTag).
     *
     * @param element    the element to be emulated
     * @param emulateTag the details of how to emulate the tag
     * @param replace    boolean indicating whether the element name should be
     *                   replaced with the alternate tag specified in the EmulateEmphasisTag
     */
    private void emulateElement(
            Element element,
            EmulateEmphasisTag emulateTag,
            boolean replace) {
        if (replace) {
            // this may set the element name to null
            element.setName(emulateTag.getAltTag());
        }

        String prefix = emulateTag.getPrefix();
        String suffix = emulateTag.getSuffix();

        if (prefix != null && !prefix.equals("")) {
            Text t = element.getDOMFactory().createText();
            t.append(prefix);
            element.addHead(t);
        }
        if (suffix != null && !suffix.equals("")) {
            Text t = element.getDOMFactory().createText();
            t.append(suffix);
            element.addTail(t);
        }
    }

    /**
     * Emulates an <a> or <anchor> tag as specified by the device policy.
     *
     * @param element    the link element to emulate
     * @param emulateTag the details of how to emulate the tag
     */
    private void emulateLinkHighlighting(
            Element element,
            EmulateEmphasisTag emulateTag) {

        Element altElement = element.getDOMFactory().createElement();
        altElement.insertAfter(element);
        element.remove();
        altElement.addHead(element);
        emulateElement(altElement, emulateTag, true);
    }

    /**
     * Add the card title as inline text. The original card title is not
     * removed, as this is usually only specified by devices that cannot show
     * the card title.
     *
     * @param element the card element to emulate
     * @param tag     the details of how to emulate the tag
     */
    private void insertCardTitle(Element element, EmulateEmphasisTag tag) {
        String value = element.getAttributeValue("title");
        if (value != null && !value.equals("")) {
            Element e = element.getDOMFactory().createElement();
            e.setName(WMLConstants.BLOCH_ELEMENT);
            Styles styles = getStylesForElement(e);
            styles.getPropertyValues().setComputedValue(
                    StylePropertyDetails.TEXT_ALIGN,
                    TextAlignKeywords.CENTER);
            Text t = element.getDOMFactory().createText();
            t.append(value);
            e.addHead(t);
            emulateElement(e, tag, true);
            element.addHead(e);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10779/1	geoff	VBM:2005121202 MCS35: WML vertical whitespace fix does not handle mode settings (take 2)

 29-Nov-05	10505/7	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/5	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (6)

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 21-Nov-05	10328/1	pabbott	VBM:2005110907 Align not honoured on p tags in WML

 21-Nov-05	10330/7	pabbott	VBM:2005110907 Align not honoured on p tags in WML

 17-Nov-05	10356/1	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 15-Nov-05	10333/1	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 21-Nov-05	10330/7	pabbott	VBM:2005110907 Align not honoured on p tags in WML

 15-Nov-05	10333/1	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 29-Sep-05	9600/4	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 19-Aug-05	9289/3	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 04-May-05	7982/1	emma	VBM:2005041321 Merge from 3.3.0 - Re-enabling wml tag emulation support

 04-May-05	7980/1	emma	VBM:2005041321 Merge from 3.2.3 - Re-enabling wml tag emulation support

 16-Mar-05	7372/2	emma	VBM:2005031008 Make cols attribute optional on the XDIME table element

 14-Jan-05	6346/2	geoff	VBM:2004110112 Certain form layouts generate invalid WML

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 17-Sep-04	5547/3	pcameron	VBM:2004091301 Fixed element allocation

 13-Jun-03	399/1	mat	VBM:2003060503 Handle new special dissection tags

 10-Jun-03	356/1	allan	VBM:2003060907 Moved some common testtools into Synergetics

 ===========================================================================
*/

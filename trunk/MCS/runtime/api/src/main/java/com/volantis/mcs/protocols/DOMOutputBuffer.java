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
 * $Header: /src/voyager/com/volantis/mcs/protocols/DOMOutputBuffer.java,v 1.8 2003/04/28 11:50:37 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Feb-02    Paul            VBM:2002021802 - Created. See class comment
 *                              for details.
 * 28-Mar-02    Allan           VBM:2002022007 - Added appendText(),
 *                              appendLiteral() and appendEncoded() char[],
 *                              int, int versions.
 * 24-Apr-02    Paul            VBM:2002042202 - Added ability to remember an
 *                              insertion point in the buffer and go back to
 *                              it later.
 * 26-Apr-02    Paul            VBM:2002042205 - Added getCurrentElement
 *                              method.
 * 09-May-02    Ian             VBM:2002031203 - Changed log4j to use string.
 * 06-Aug-02    Paul            VBM:2002073008 - Check before releasing the
 *                              root element.
 * 24-Apr-03    Chris W         VBM:2003030404 - addOutputBuffer checks to see
 *                              if output buffer being added is null. If so,
 *                              is ignored.
 * 24-Apr-03    Steve           VBM:2003041606 - Added whitespace processing 
 *                              state logic. Added javadoc comments while I was 
 *                              here to see what is going on. 
 *                              Moved setTrim to AbstractOutputBuffer as
 *                              this implementation of it simply sets a private
 *                              variable that is never used. This only clouds 
 *                              the current whitespace implementation.
 *                              Moved isWhitespace to AbstractOutputBuffer as
 *                              this implementation returns a variable that is
 *                              never set. It may as well always return false 
 *                              from the abstract parent class.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.NodeSequence;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Comment;
import com.volantis.mcs.dom.CharacterNode;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.StyleContainer;
import com.volantis.styling.Styles;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.Writer;

/**
 * This class extends AbtsractOutputBuffer with the methods which need to be
 * implemented by OutputBuffers which contain a DOM, or partial DOM.
 * <p/>
 * When creating output elements, the CSS display value needs to be set
 * correctly. There are three different types of elements we create:
 * <ol>
 * <li>Elements that map like for like with the input, e.g. a table on input
 * maps to a table on output. In this case the value of display should be
 * preserved from the input styles.
 * <li>Elements that are created from the input but are not like for like, e.g
 * table -> div, pane -> table. In this case the display should be overridden
 * with a value that is appropriate for the output. For example pane -> table
 * has display:block as input but should have display:table as output. Note
 * that where possible this should match the input in meaning as far as is
 * practical.
 * <li>As structural necessity, e.g. does not map to an input element and so
 * does not have any styles of its own and therefore has to inherit them from
 * its parent, e.g. tr. In this case the display should be set properly on
 * the output.
 * </ol>
 * <p/>
 * <strong>NOTE:</strong>
 * This class uses Java's Character.isWhitespace(char) to determine if a
 * character is a whitespace. The characters that Java defines to be whitespace
 * are a superset of those that XML defines to be whitespace. However, if the
 * extra characters considered to be Java whitespace are used in an XML
 * document, the XML parser will already have complained about these characters
 * being invalid.
 * </p>
 *
 * @mock.generate base="OutputBuffer"
 */
public class DOMOutputBuffer
    extends com.volantis.mcs.protocols.AbstractOutputBuffer 
    implements com.volantis.mcs.protocols.OutputBuffer {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(DOMOutputBuffer.class);


    /**
     * Factory to use to create DOM objects.
     */
    private final DOMFactory factory;

    /**
     * The root node. If this is null, then the DOM tree is empty
     */
    private Element root;

    /**
     * The current element.
     */
    private Element currentElement;

    /**
     * The position within the current element.
     * <p/>
     * Nodes are added after this node. If this node is null then a node is
     * added at the tail of the current element.
     */
    private Node currentPosition;

    /**
     * The saved element.
     */
    private Element savedElement;

    /**
     * The saved position.
     */
    private Node savedPosition;

    /**
     * The Writer.
     */
    private Writer writer;

    /**
     * Whether a whitespace character is pending on the writer
     */
    private boolean whitespacePending = false;

    /**
     * Whether text has been written between elements. Note that this does not
     * need to maintain state across nested elements because it is only concerned
     * with a "chunk" of text, where a chunk is a sequence of characters with
     * no intervening element.
     */
    private boolean textWritten = false;

    /**
     *  The current state for whitespace processing
     */
    private int whitespaceState = WS_KEEP;

    /**
     * The text node that has been created to contain the text but has not yet
     * been added into the DOM, because it is empty.
     */
    private Text emptyTextNode;

    private Comment currentComment;

  /**
     * Initialise with the specified factory.
     */
    public DOMOutputBuffer(DOMFactory factory) {
        this.factory = factory;

      clearPrivate();
    }

    /**
     * Initialise with the default factory.
     */
    public DOMOutputBuffer() {
        this(DOMFactory.getDefaultInstance());
    }

    /**
     * Initialise the output buffer.
     */
    public void initialise() {
    }

    /**
     * The private implementation of {@link #clear()} used to prevent issues
     * with using mock.
     */
    private void clearPrivate() {
        // Allocate a dummy root element and start adding to it.
        root = factory.createElement();
        resetPosition();
    }

    /**
     * Reset the position at which elements will be added.
     */
    private void resetPosition() {
        currentElement = root;
        currentPosition = null;
    }

    public void clear() {
      clearPrivate();
  }


    /**
     * Returns the root element for the output buffer
     *
     * @return the root element
     */
    public Element getRoot() {
        return root;
    }

    /**
     * Get the current element.
     *
     * @return The current element.
     */
    public Element getCurrentElement() {
        return currentElement;
    }

    // Javadoc inherited from super class.
    public Writer getWriter() {
        if (writer == null) {
            writer = new OutputBufferWriter(this);
        }
        return writer;
    }

    // Javadoc inherited from super class.
    public OutputBuffer getCurrentBuffer() {
        return this;
    }

    /**
     * Allocate an element with the specified name and style information
     * derived from the provided styles, leaving the CSS display value as is.
     * <p/>
     * <strong>NOTE:</strong> See the class comment for more information on
     * display values when creating output elements.
     *
     * @param name The name of the element.
     * @param styles the styles
     * @return The newly allocated element.
     */
    public Element allocateStyledElement(String name,
        Styles styles) {

        return allocateStyledElement(name, styles, null);
    }

    /**
     * Allocate an element with the specified name and style information derived
     * from the StyleContainer, allowing the CSS display value to be overridden.
     * <p/>
     * <strong>NOTE:</strong> See the class comment for more information on
     * display values when creating output elements.
     *
     * @param name The name of the element.
     * @param styles the styles
     * @param display the display value to use for this element, or null if the
     *    existing display value on the styles is to be used.
     * @return The newly allocated element.
     */
    private Element allocateStyledElement(String name,
        Styles styles, StyleValue display) {

        // TODO: enable this check and fix test case failures.
//        if (styles == null) {
//            throw new IllegalArgumentException("styles may not ne null");
//        }

        if (display != null) {
            styles.getPropertyValues().setComputedValue(StylePropertyDetails.DISPLAY, display);
        }

        Element element = factory.createStyledElement(styles);
        element.setName(name);

        return element;
    }

    /**
     * Allocate an element with the specified name and no style information.
     *
     * @param name The name of the element.
     * @return The newly allocated element.
     */
    public Element allocateElement(String name) {
        return factory.createElement(name);
    }

    /**
     * Add the specified node at the current insertion point.
     * The node is added as a sibling to currentPosition. If currentPosition
     * is null then it is added to the tail of currentElement. On exit
     * currentPosition will be set to the new node so subsequent nodes will
     * be added as siblings of this one.
     *
     * @param node The node to add.
     */
    protected void addNode(Node node) {
        if (logger.isDebugEnabled()) {
            logger.debug("Adding " + node + " element " + currentElement
                + " position " + currentPosition);
        }

        if (currentPosition == null) {
            currentElement.addTail(node);
        } else {
            node.insertAfter(currentPosition);
        }
        currentPosition = node;
    }

    /**
     * Add a new element. The element is added as a child of currentElement
     * by calling addNode(). Future calls to addNode() will add children to
     * this element.
     *
     * @param element  The element to add
     */
    protected void pushElement(Element element) {

        handleOpenElementWhitespace();

        addNode(element);

        // Update the insertion point.
        currentElement = element;

        currentPosition = null;

        if (logger.isDebugEnabled()) {
            logger.debug("Pushed element " + element);
        }
    }

    /**
     * Move back up the DOM tree by one element. The current position is set
     * to the current element so that new nodes are added after it. The current
     * node is then set to the current nodes parent so that new nodes are added
     * as children of the parent element.
     *
     * @return the parent element of the current element
     */
    protected Element popElement() {
        Element element = currentElement;

        // The next node which is added should be added after the current element.
        currentPosition = currentElement;

        // Change the current element to be the parent element.
        currentElement = currentElement.getParent();

        if (logger.isDebugEnabled()) {
            logger.debug("Popped element " + element);
        }

        handleCloseElementWhitespace();

        return element;
    }

    /**
     * Handle the whitespace state when opening an element
     */
    public void handleOpenElementWhitespace() {
        // Output a whitespace if there is one pending and either of the
        // following are true:
        //
        // o text has previously been written which means that this whitespace
        //   cannot be a leading space, so is safe to output.
        //
        // o text hasn't previously been written but the content can be mixed,
        //   and the space isn't a trailing one since a nested element has just
        //   been encountered; therefore the space can be output.
        //
        if (whitespacePending && (textWritten || isMixedContentElement())) {
            whitespaceState = WS_KEEP;
            appendEncoded(" ");
        }

        // A nested element has been encountered so reset the text and whitespace
        // processing to expect a new sequence of characters. Any pending
        // whitespace has been dealt with above, and the new whitespace processing
        // state is WS_IGNORE in readiness for any leading whitespace occurring
        // in the nested element's content.
        whitespacePending = false;
        textWritten = false;
        whitespaceState = WS_IGNORE;
    }

    /**
     * Handle the whitespace state when closing an element
     */
    public void handleCloseElementWhitespace() {
        whitespacePending = false;
        textWritten = false;
        whitespaceState = WS_PRUNING;
    }

    /**
     * Add the specified element as a child of the current element and then
     * make it the current element.
     *
     * @param element The element to add.
     * @return the added element (parameter)
     */
    public Element openElement(Element element) {
        pushElement(element);

        return element;
    }

    /**
     * Create a named element with style information derived from the
     * StyleContainer, add it as a child of the current element, and then make
     * it the current element.
     * <p/>
     * <strong>NOTE:</strong> See the class comment for more information on
     * display values when creating output elements.
     *
     * @param name      of the element
     * @param container to be passed into allocateStyledElement
     * @return the newly created styled element
     * @todo find inappropriate uses of this method and change to other overload
     * Since this method was originally the only overload, there are most likely
     * a lot of places calling this when the output markup differs from the
     * input markup or when there is no related input markup. Find and change.
     */
    public Element openStyledElement(String name, StyleContainer container) {

        return openStyledElement(name, container, null);
    }

    /**
     * Create a named element with style information derived from the
     * StyleContainer, add it as a child of the current element, and then make
     * it the current element.
     * <p/>
     * <strong>NOTE:</strong> See the class comment for more information on
     * display values when creating output elements.
     *
     * @param name of the element
     * @param container to be passed into allocateStyledElement
     * @param display the display value to use for this element, or null if the
     *    existing display value on the styles is to be used.
     * @return the newly created styled element
     */
    public Element openStyledElement(String name, StyleContainer container,
        StyleValue display) {

        // TODO: enable this check, remove the null check below and fix test case failures.
//        if (container == null) {
//            throw new IllegalArgumentException(
//                  "container may not be null, styles are mandatory");
//        }
        Styles styles = container == null ?
            null : container.getStyles();

        return openStyledElement(name, styles, display);
    }

    /**
     * Create a named element with style information, add it as a child of the
     * current element, and then make it the current element.
     * <p/>
     * <strong>NOTE:</strong> See the class comment for more information on
     * display values when creating output elements.
     *
     * @param name   of the element
     * @param styles to be passed into allocateStyledElement
     * @return the newly created styled element
     * @todo find inappropriate uses of this method and change to other overload
     * Since this method was originally the only overload, there are most likely
     * a lot of places calling this when the output markup differs from the
     * input markup or when there is no related input markup. Find and change.
     */
    public Element openStyledElement(String name, Styles styles) {

        return openStyledElement(name, styles, null);

    }

    /**
     * Create a named element with style information, add it as a child of the
     * current element, and then make it the current element.
     * <p/>
     * <strong>NOTE:</strong> See the class comment for more information on
     * display values when creating output elements.
     *
     * @param name of the element
     * @param styles to be passed into allocateStyledElement
     * @param display the display value to use for this element, or null if the
     * existing display value on the styles is to be used.
     * @return the newly created styled element
     */
    public Element openStyledElement(String name, Styles styles,
        StyleValue display) {

        Element element = allocateStyledElement(name, styles, display);
        pushElement(element);
        return element;

    }

    /**
     * Create a named element with no style, add it as a child of the current
     * element then make it the current element.
     *
     * @param name  The name of the element
     * @return the created element
     */
    public Element openElement(String name) {
        Element element = allocateElement(name);
        pushElement(element);
        return element;
    }

    /**
     * Add an element as a child of the current element.
     *
     * @param element the element to add
     * @return the added element (parameter)
     */
    public Element addElement(Element element) {

        handleOpenElementWhitespace();

        addNode(element);

        handleCloseElementWhitespace();

        return element;
    }

    /**
     * Create a named element with no style and then add it as a child of the
     * current element.
     *
     * @param name The name of the element
     * @return the created element
     */
    public Element addElement(String name) {

        handleOpenElementWhitespace();

        Element element = allocateElement(name);
        addNode(element);

        handleCloseElementWhitespace();

        return element;
    }

    /**
     * Create a named element with style information derived from the
     * StyleContainer and then add it as a child of the current element.
     * <p/>
     * <strong>NOTE:</strong> See the class comment for more information on
     * display values when creating output elements.
     *
     * @param name           The name of the element
     * @param styleContainer to be passed into allocateStyledElement
     * @return the new styled element
     * @todo find inappropriate uses of this method and change to other overload
     * Since this method was originally the only overload, there are most likely
     * a lot of places calling this when the output markup differs from the
     * input markup or when there is no related input markup. Find and change.
     */
    public Element addStyledElement(String name,
        StyleContainer styleContainer) {

        return addStyledElement(name, styleContainer, null);

    }

    /**
     * Create a named element with style information derived from the
     * StyleContainer and then add it as a child of the current element.
     * <p/>
     * <strong>NOTE:</strong> See the class comment for more information on
     * display values when creating output elements.
     *
     * @param name The name of the element
     * @param styleContainer to be passed into allocateStyledElement
     * @param display the display value to use for this element, or null if the
     * existing display value on the styles is to be used.
     * @return the new styled element
     */
    private Element addStyledElement(String name, StyleContainer styleContainer,
        StyleValue display) {

        handleOpenElementWhitespace();

        Styles styles = styleContainer == null ?
            null : styleContainer.getStyles();
        Element element = allocateStyledElement(name, styles, display);
        addNode(element);

        handleCloseElementWhitespace();

        return element;

    }

    /**
     * Close the current element and make its parent the current element. If
     * the name of the parent does not match the parameter name, an
     * IllegalStateException is thrown.
     *
     * @param name the expected name of the parent element
     * @return the parent of the current node
     */
    public Element closeElement(String name) {
        Element element = popElement();
        if (!name.equals(element.getName())) {
            throw new IllegalStateException
        ("Popped element " + element + " expected element named " + name);
        }

        return element;
    }

    /**
     * Close the current element and make its parent the current element. If
     * the parent does not match the parameter element, an
     * IllegalStateException is thrown.
     *
     * @param expectedElement the expected parent element
     * @return the parent of the current node
     */
    public Element closeElement(Element expectedElement) {
        Element element = popElement();
        if (element != expectedElement) {
            throw new IllegalStateException
        ("Popped element " + element + " expected element " + expectedElement);
        }
        return element;
    }

    /**
     * Add the contents of the specified output buffer to this buffer.
     *
     * @param buffer The buffer to add to this buffer.
     * @ TODO: check if whitespace is pending in ?EITHER? buffer,
     *    if so, output the appropriate space.
     * @ TODO: re-enable test case in VDXMLTextMenuItemRendererTestCase
     *    when this bug is fixed.
     */
    public void addOutputBuffer(DOMOutputBuffer buffer) {

        // avoid NullPointerException
        if (buffer != null) {
            NodeSequence nodes = buffer.removeContents();
            addNodeSequence(nodes);
        }
    }

    /**
     * Delegate to the {@link #addOutputBuffer} method.
     */
    public void transferContentsFrom(OutputBuffer buffer) {
        addOutputBuffer(((DOMOutputBuffer) buffer));
    }

    /**
     * Add a comment to the current element. A comment node containing the comment
     * is created and this is added as a child of the current element.
     *
     * @param comment the text of the comment to add
     */
    public void addComment(String comment) {
        Comment node = factory.createComment(comment);
        addNode(node);
    }

    /**
     * Get a text node into which character content can be added.
     *
     * <p>This will use the node at the current position, if it is a text
     * node with the correct encoding, otherwise it will create an empty
     * text node and store it away. It does not add it to the document
     * straight away because it may not actually have any characters added to
     * it, e.g. if they are all whitespace that is pruned. The
     * {@link #updateEmptyTextNode()} method must be called after the
     * characters have been added to this node, it will ensure that the text
     * node is added to the document if it is not empty.</p>
     *
     * <p>e.g.</p>
     * <pre>
     *     Text text = getActiveTextNode(false);
     *     text.append(text);
     *     updateActiveTextNode();
     * </pre>
     *
     * @param preEncoded Indicates whether the content to be added has been
     *                   pre encoded or not.
     * @return The active text node.
     */
    private CharacterNode getActiveCharacterNode(boolean preEncoded) {

        CharacterNode node = null;
        if (currentComment != null) {
            if (preEncoded) {
                throw new IllegalStateException(
                        "Cannot add pre encoded text to a comment");
            }

            node = currentComment;
        } else {
            // Try and reuse existing Text nodes if we can.
            Node previous = currentPosition == null ?
                    currentElement.getTail() : currentPosition;
            if (previous instanceof Text) {
                Text text = (Text) previous;
                // If the encoding of the text node matches the encoding of
                // the text being added then we reuse it, otherwise we have to
                // create a new node.
                if (text.isEncoded() == preEncoded) {
                    node = text;
                }
            }
        }

        // If the previous node is not a text node with the required encoding
        // set then use the empty text node if it exists.
        if (node == null) {
            // If the empty text node does not exist then create it.
            if (emptyTextNode == null) {
                emptyTextNode = factory.createText();
            }

            // Make sure that the empty text node has the correct encoding,
            // this is ok to do because it is empty.
            emptyTextNode.setEncoded(preEncoded);

            // Return the empty text node.
            node = emptyTextNode;
        }

        return node;
    }

    /**
     * Make sure that the empty text node is added to the DOM if it is no
     * longer empty.
     */
    private void updateEmptyTextNode() {
        if (emptyTextNode != null && emptyTextNode.getLength() > 0) {
            addNode(emptyTextNode);
            emptyTextNode = null;
        }
    }

    /**
     * Write a character to a text node paying attention to whitespace processing state
     *
     * @param node
     * @param chr
     */
    private void appendCharacter(CharacterNode node, char chr) {
        switch (whitespaceState) {
            case WS_KEEP:
                if (logger.isDebugEnabled()) {
                logger.debug("appendCharacter state=WS_KEEP, char='" + chr + 
                            "'");
                }
                node.append(chr);
                textWritten = true;
                // even if chr is a white-space character, we've already
                // appended it to the node
                whitespacePending = false;
                break;
            case WS_IGNORE:
                if (logger.isDebugEnabled()) {
                logger.debug("appendCharacter state=WS_IGNORE, char='" + chr + 
                            "', pending=" + whitespacePending + ")");
                }
                if (!Character.isWhitespace(chr)) {
                    if (whitespacePending) {
                        node.append(' ');
                        whitespacePending = false;
                    }
                    node.append(chr);
                    whitespaceState = WS_PRUNING;
                    textWritten = true;
                }
                break;
            case WS_PRUNING:
                if (logger.isDebugEnabled()) {
                logger.debug("appendCharacter state=WS_PRUNING, char='" + chr +
                            "'");
                }
                if (!Character.isWhitespace(chr)) {
                    node.append(chr);
                    textWritten = true;
                } else {
                    whitespacePending = true;
                    whitespaceState = WS_IGNORE;
                }
                break;
            default:
                throw new IllegalStateException("Invalid whitespace state " + whitespaceState);
        }
    }

    /**
     * Add text to the current element.
     * <p/>
     * If the last child of the current element is a text node and the encoding
     * of that node matches the preEncoding parameter then the text node is
     * retrieved and this text is appended to that node. Otherwise, a new text
     * node is created and added as a child of the current element.
     * <p/>
     * <strong>WARNING:</strong> when passing true for encoded, this method is
     * NOT generally safe to call. In particular:
     * <ul>
     *   <li> For WML protocols, the preEncoded flag is ignored. The text
     *        provided is treated as if it were false. Thus one cannot write
     *        markup directly as pre-encoded text for WML any more. Any such
     *        attempt will result in the markup being encoded again on output and
     *        the markup characters being displayed to the user as literal text.
     *   <li> For non WML protocols, preEncoded=true may be used judiciously to
     *        output comments and doctypes since MCS DOM does not have explicit
     *        support for these currently, but other than that it should be
     *        avoided.
     *   <li> For code which is shared between protocols, it must take a safety
     *        first approach and not depend on the encoded flag being respected.
     *        That is, never use preEncoded=true.
     *   <li> Elements currently attempt to pre-encode all their element content
     *        by setting their content writer to the protocol's encoding writer.
     *        This encoding writer in turn calls this method via
     *        {@link #writeText). This is duly ignored under WWL. I think it
     *        would be better (more consistent and easier to understand) if we
     *        moved to having all content in normal form and encoding only at
     *        writing time.
     * </ul>
     * Over time we will hopefully add support for comment and doctypes to the
     * MCS DOM and then we can get rid of the encoded parameter altogether, see
     * VBM:2003061610. This change will be probably be required if we want to
     * support a single dissector for both WBDOM and MCSDOM.
     *
     * @param text the text to append to the element
     * @param preEncoded whether or not the text has been pre-encoded; if true
     *                   it will not be encoded again in the output phase. NOTE
     *                   passing true here is dangerous, see method comment.
     */
    private void appendText(String text, boolean preEncoded) {

        if (text == null) {
            throw new IllegalArgumentException();
        }
        if (text.length() == 0) {
            return;
        }

        CharacterNode node = getActiveCharacterNode(preEncoded);
        for (int i = 0; i < text.length(); i++) {
            appendCharacter(node, text.charAt(i));
        }

        // Make sure that the active text node has been added to the DOM if
        // it contains any content.
        updateEmptyTextNode();
    }

    /**
     * Add text in the form of a character array to the current element.
     * <p/>
     * If the last child of the current element is a text node and the encoding
     * of that node matches the preEncoding parameter then the text node is
     * retrieved and this text is appended to that node. Otherwise, a new text
     * node is created and added as a child of the current element.
     * <p/>
     * <strong>WARNING:</strong> when passing true for encoded, this method is
     * NOT generally safe to call. In particular:
     * <ul>
     *   <li> For WML protocols, the preEncoded flag is ignored. The text
     *        provided is treated as if it were false. Thus one cannot write
     *        markup directly as pre-encoded text for WML any more. Any such
     *        attempt will result in the markup being encoded again on output and
     *        the markup characters being displayed to the user as literal text.
     *   <li> For non WML protocols, preEncoded=true may be used judiciously to
     *        output comments and doctypes since MCS DOM does not have explicit
     *        support for these currently, but other than that it should be
     *        avoided.
     *   <li> For code which is shared between protocols, it must take a safety
     *        first approach and not depend on the encoded flag being respected.
     *        That is, never use preEncoded=true.
     * </ul>
     * Over time we will hopefully add support for comment and doctypes to the
     * MCS DOM and then we can get rid of the encoded parameter altogether, see
     * VBM:2003061610. This change will be probably be required if we want to
     * support a single dissector for both WBDOM and MCSDOM.
     *
     * @param text an array of characters holding the text to add
     * @param off the index of the first character to write
     * @param len the number of characters to write
     * @param preEncoded whether or not the text has been pre-encoded; if true
     *                   it will not be encoded again in the output phase. NOTE
     *                   passing true here is dangerous, see method comment.
     */
    private void appendText(char[] text, int off, int len,
        boolean preEncoded) {

        if (text == null) {
            throw new IllegalArgumentException();
        }
        if (len == 0) {
            return;
        }

        CharacterNode node = getActiveCharacterNode(preEncoded);
        for (int i = off; i < off + len; i++) {
            appendCharacter(node, text[i]);
        }

        // Make sure that the active text node has been added to the DOM if
        // it contains any content.
        updateEmptyTextNode();
    }

    /**
     * Add text to the current element which will appear in the output markup
     * exactly as passed (if possible, given the character set in use). That is,
     * characters which may conflict with the markup WILL NOT be encoded.
     * <p/>
     * <strong>WARNING:</strong> this method is NOT generally safe to call.
     * It must NEVER be used in WML protocols. See {@link #appendText}.
     *
     * @param text the text to write
     * @return this output buffer
     */
    public DOMOutputBuffer appendLiteral(String text) {
        appendText(text, true);
        return this;
    }

    /**
     * Add text to the current element which will be rendered on the output
     * device exactly as passed (if possible, given the character set in use).
     * That is, characters which may conflict with the markup WILL be encoded
     * as entities.
     *
     * @param text the text to write
     * @return this output buffer
     */
    public DOMOutputBuffer appendEncoded(String text) {
        appendText(text, false);
        return this;
    }

    /**
     * Add text to the current element which will be rendered on the output
     * device exactly as passed (if possible, given the character set in use).
     * That is, characters which may conflict with the markup WILL be encoded
     * as entities.
     *
     * @param text an array of characters holding the text
     * @param off the first character to write from the array
     * @param len the number of characters to write
     * @return this output buffer
     */
    public DOMOutputBuffer appendEncoded(char[] text, int off, int len) {
        appendText(text, off, len, true);
        return this;
    }

    /**
     * Update the current whitespace state
     */
    private void updateWhitespaceState() {
        switch (whitespaceState) {
            case WS_KEEP:
                if (!isPreFormatted()) {
                    whitespaceState = WS_IGNORE;
                }
                break;
            case WS_IGNORE:
                if (isPreFormatted()) {
                    whitespaceState = WS_KEEP;
                }
                break;
            case WS_PRUNING:
                if (isPreFormatted()) {
                    whitespaceState = WS_KEEP;
                }
                break;
        }
    }

    // Javadoc inherited
    public void writeText(char[] text, int off, int len) {
        writeText(text, off, len, false);
    }

    // Javadoc inherited
    public void writeText(char[] text, int off, int len, boolean preEncoded) {
        updateWhitespaceState();
        appendText(text, off, len, preEncoded);
    }

    // Javadoc inherited
    public void writeText(String text) {
        writeText(text, false);
    }

    // Javadoc inherited
    public void writeText(String text, boolean preEncoded) {
        updateWhitespaceState();
        appendText(text, preEncoded);
    }

    /**
     * Save the current insertion point so it can be restored at a later date.
     * <p/>
     * This should probably return an object which encapsulates the insertion
     * point and which can be stored somewhere else as that is allows multiple
     * insertion points.
     * </p>
     */
    public void saveInsertionPoint() {
        if (savedElement != null) {
            throw new IllegalStateException("Insertion point already saved");
        }

        savedElement = currentElement;
        savedPosition = currentPosition;
    }

    /**
     * Indicates whether an insertion point was saved.
     */
    public boolean savedInsertionPoint() {
        return savedElement != null;
    }

    /**
     * Restore a previously saved insertion point.
     */
    public void restoreInsertionPoint() {
        if (savedElement == null) {
            throw new IllegalStateException("Insertion point not saved");
        }

        currentElement = savedElement;
        currentPosition = savedPosition;

        savedElement = null;
        savedPosition = null;
    }

    /**
     * Returns true iff this output buffer has an insertion point saved.
     *
     * @return true iff there is a saved insertion point
     */
    public boolean hasInsertionPoint() {
        return savedElement != null;
    }

    /**
     * Returns a boolean flag denoting whether or not this buffer is empty.
     *
     * @return true if the output buffer is empty, false otherwise.
     */
    public boolean isEmpty() {
        return root.getHead() == null;
    }

    // Javadoc inherited.
    public String stringValue() {
        // todo implement this
        return null;
    }

    /**
     * Get the PCDATA contents of the DOM output buffer as text, if any.
     * <p>
     * This buffer must correspond to the content of an input buffer which is
     * PCDATA only (eg title), i.e it must have at most one node which is a
     * Text node.
     *
     * @return the text of the buffer, or null if it was empty.
     */
    public String getPCDATAValue() {

        String pcdata = null;

        // Get the first node in the buffer.
        final Element root = getRoot();
        Node firstNode = root.getHead();
        // If there is at least one child.
        if (firstNode != null) {
            // Ensure there is only one child.
            if (firstNode != root.getTail()) {
                throw new IllegalStateException(
                    "head != tail => more than 1 child");
            }
            // Ensure that that child is a Text node.
            if (!(firstNode instanceof Text)) {
                throw new IllegalStateException("child is not Text");
            }

            Text text = (Text) firstNode;
            pcdata = new String(text.getContents(), 0, text.getLength());
        }
        // else the buffer is empty, so return null.

        return pcdata;

    }

    /**
     * Remove the contents of the buffer and return it as a sequence of nodes.
     *
     * <p>The returned sequence is not live.</p>
     *
     * @return The contents of the buffer as a sequence of nodes.
     */
    public NodeSequence removeContents() {
        NodeSequence contents = root.removeChildren();
        resetPosition();
        return contents;
    }

    /**
     * Add the nodes contained within the sequence to the current insertion
     * point.
     *
     * @param nodes The nodes to add.
     */
    public void addNodeSequence(NodeSequence nodes) {
        currentPosition = currentElement.insertAfter(nodes, currentPosition);
    }

    public Comment openComment() {
        if (currentComment != null) {
            throw new IllegalStateException(
                    "Cannot open a comment inside another comment");
        }

        currentComment = factory.createComment();

        return currentComment;
    }

    public void closeComment() {
        if (currentComment == null) {
            throw new IllegalStateException(
                    "Attempting to close comment when one is not open");
        }

        if (currentComment.getLength() > 0) {
            addNode(currentComment);
        }

        currentComment = null;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05	10562/1	ibush	VBM:2005113001 Fix extra whitespace characters appearing

 01-Dec-05	10517/1	ibush	VBM:2005113001 Fix extra whitespace characters appearing

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 22-Aug-05	9298/3	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 09-Jun-05	8665/4	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 09-Jun-05	8665/2	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 05-Apr-05	7459/4	tom	VBM:2005032101 Added SmartClientSkin protocol

 04-Apr-05	7459/1	tom	VBM:2005032101 Added SmartClientSkin protocol

 05-Apr-05	7513/1	geoff	VBM:2003100606 DOMOutputBuffer allows creation of text which renders incorrectly in WML

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/3	doug	VBM:2004111702 Refactored Logging framework

 26-Nov-04	6298/1	geoff	VBM:2004112405 MCS NullPointerException in wml code path

 22-Oct-04	5924/1	pcameron	VBM:2004101914 Fixed whitespace processing when popping elements

 04-Oct-04	5719/1	pcameron	VBM:2004092213 Fixed whitespace handling for mixed content types

 01-Oct-04	5635/1	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 30-Apr-04	3910/2	byron	VBM:2004021117 Fixed merge conflicts

 29-Apr-04	4013/4	pduffin	VBM:2004042210 Restructure menu item renderers

 29-Apr-04	4098/1	mat	VBM:2004042809 Made pooling of objects in the DOMProtocol configurable

 23-Mar-04	3555/1	allan	VBM:2004032205 Patch performance fixes from MCS 3.0GA

 22-Mar-04	3512/3	allan	VBM:2004032205 MCS performance enhancements.

 22-Mar-04	3512/1	allan	VBM:2004032205 MCS performance enhancements.

 05-Mar-04	3339/1	geoff	VBM:2003052104 Invalid usage of DOMOutputBuffer.appendLiteral()

 05-Mar-04	3337/1	geoff	VBM:2003052104 Invalid usage of DOMOutputBuffer.appendLiteral()

 05-Mar-04	3323/1	geoff	VBM:2003052104 Invalid usage of DOMOutputBuffer.appendLiteral()

 31-Oct-03	1184/4	geoff	VBM:2003081901 spaces appearing in rendered page (supermerge)

 19-Aug-03	1184/1	geoff	VBM:2003081901 spaces appearing in rendered page (merge from mimas)

 19-Aug-03	1182/1	geoff	VBM:2003081901 spaces appearing in rendered page (merge from metis)

 17-Sep-03	1412/1	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 20-Jun-03	424/2	byron	VBM:2003022825 Enhance behaviour of pane element within xfform

 19-Jun-03	407/1	steve	VBM:2002121215 Flow elements and PCData in regions

 10-Jun-03	360/1	mat	VBM:2003052701 Correct check for zero length text nodes and use XMLOutputter instead of WMLOutputter

 ===========================================================================
*/

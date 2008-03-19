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
 * $Header: /src/voyager/com/volantis/mcs/protocols/dissection/Dissector.java,v 1.4 2003/01/21 17:50:31 adrian Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Feb-02    Paul            VBM:2002021802 - Created. See class comment
 *                              for details.
 * 23-May-02    Paul            VBM:2002042202 - Use the ProtocolConfiguration
 *                              instance to control how to handle the different
 *                              elements.
 * 20-Jan-03    Adrian          VBM:2003011605 - Removed redundant line from 
 *                              writeShard which retrieved the 
 *                              MarinerPageContext from the Protocol. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.dissection;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.RecursingDOMVisitor;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.utilities.ReusableStringBuffer;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.io.Writer;

/**
 * This class takes a dom tree and then splits it up into chunks which can
 * be sent to devices with limited page sizes.
 * <p>
 * This class does not use a SAX parser to parse the content and so in order
 * to maintain backwards compatability whitespace must be handled explicitly.
 * <p>
 * Before a DissectingPane writes its contents to the page it checks to see
 * whether the content will be dissected. If it is then it wraps the output
 * within special protocol specific tags. When one of these special start tags
 * is found then a special DissectableNode is added to the parse tree and is
 * the root of the parse tree created from parsing the contents of the
 * DissectingPane.
 * <p>
 * During processing of elements any attributes which have default values
 * are ignored as they will pick them up from the DTD. Other attributes are
 * added as they are found directly to the open tag buffer with their values
 * correctly quoted. If an element node has no children and the element is
 * in the empty set then an empty element tag (one which ends in /&gt;) is
 * created, otherwise both an open and close tag are created.
 * <p>
 * After processing the tree looks something like this.
 * <pre>
 *                                 _+_
 *                              _-~   ~-_
 *                           _-~         ~-_
 *                        _-~               ~-_
 *                     _-~              @      ~-_
 *                  _-~     @          / \        ~-_
 *               _-~       / \        /   \          ~-_
 *            _-~         /   \      /     \            ~-_
 * </pre>
 * Where + is the root of the whole tree and @ are the roots of the dissectable
 * content. The area inside the tree but outside the two sub trees is the
 * fixed content and is the same regardless of what portion of the dissectable
 * content is used.
 * <p>
 * The size of the fixed content is calculated by recursing down the tree
 * summing up the sizes of all the nodes. The recursion ignores nodes below a
 * dissectable root. This value is then subtracted from the maximum page size
 * limit leaving the space available for the dissectable content.
 * <p>
 * This space has to be shared out amongst each of the dissectable trees
 * according to some scheme which has yet to be decided, probably by
 * associating some weight with each DissectingPane, currently each tree
 * has an equal share.
 * <p>
 * The dissectable trees are dissected independently of each other, although
 * the amount of space needed for navigation depends on the number of trees
 * and the names of the panes.
 * <p>
 * Each dissectable tree is split into multiple shards and each shard consists
 * of multiple nodes.
 * <pre>
 *                                  A
 *                                 _+_
 *                              _-~/ \~-_
 *                           _-~  /   \  ~-_
 *                        _-~    /     \    ~-_
 *                     _-~      /       \      ~-_
 *                  _-~        /         \        ~-_
 *               _-~          /           \          ~-_
 *            _-~            /             \            ~-_
 *        B +~            F +               + J            ~+ N
 *         /|\             /|\             /|\             /|\
 *        / | \           / | \           / | \           / | \
 *       /  |  \         /  |  \         /  |  \         /  |  \
 *      /   |   \       /   |   \       /   |   \       /   |   \
 *
 *    C     D     E   G     H     I   K     L     M   O     P     Q
 * </pre>
 *
 * In the above dissectable tree the shards could be as follows:
 * <ul>
 * <li>Shard 0 consists of nodes A, B, C, D
 * <pre>
 *                                  A
 *                                 _+
 *                              _-~
 *                           _-~
 *                        _-~
 *                     _-~
 *                  _-~
 *               _-~
 *            _-~
 *        B +~
 *         /|
 *        / |
 *       /  |
 *      /   |
 *
 *    C     D
 * </pre>
 * </li>
 * <li>Shard 1 consists of nodes A, B, E, F, G, H, I
 * <pre>
 *                                  A
 *                                 _+
 *                              _-~/
 *                           _-~  /
 *                        _-~    /
 *                     _-~      /
 *                  _-~        /
 *               _-~          /
 *            _-~            /
 *        B +~            F +
 *           \             /|\
 *            \           / | \
 *             \         /  |  \
 *              \       /   |   \
 *
 *                E   G     H     I
 * </pre>
 * </li>
 * <li>Shard 2 consists of nodes A, J, K, L
 * <pre>
 *                                  A
 *                                  +
 *                                   \
 *                                    \
 *                                     \
 *                                      \
 *                                       \
 *                                        \
 *                                         \
 *                                          + J
 *                                         /|
 *                                        / |
 *                                       /  |
 *                                      /   |
 *
 *                                    K     L
 * </pre>
 * </li>
 * <li>Shard 3 consists of nodes A, J, M, N, O, P, Q
 * <pre>
 *                                  A
 *                                  +_
 *                                   \~-_
 *                                    \  ~-_
 *                                     \    ~-_
 *                                      \      ~-_
 *                                       \        ~-_
 *                                        \          ~-_
 *                                         \            ~-_
 *                                          + J            ~+ N
 *                                           \             /|\
 *                                            \           / | \
 *                                             \         /  |  \
 *                                              \       /   |   \
 *
 *                                                M   O     P     Q
 * </pre>
 * </li>
 * </ul>
 * Each node can be in either one shard or a consecutive range of shards as is
 * shown below. A node cannot be in shards 1 and 3 without also being in 2.
 * Which means that each node simply has to store the first and last shards
 * which it belong to and not list each particular shard. The following list
 * shows this:
 * <ul>
 * <li>Node A is in shards 0, 1, 2, 3.</li>
 * <li>Node B is in shards 0, 1.</li>
 * <li>Nodes C and D are in shard 0.</li>
 * <li>Nodes E,F,G,H and I are in shard 1.</li>
 * <li>Node J is in shards 2, 3.</li>
 * <li>Nodes K and L are in shard 2.</li>
 * <li>Nodes M,N,O,P and Q are in shard 3.</li>
 * </ul>
 * This table lists those nodes which only belong in 1 shard and the number of
 * the shard that they belong to. It shows how each shard consists of a
 * sequence of leaf nodes in node order.
 * <center><table border="1">
 * <tr>
 * <th align="left">Node</th>
 * <td>C</td><td>D</td><td>E</td><td>F</td><td>G</td><td>H</td><td>I</td>
 * <td>K</td><td>L</td><td>M</td><td>N</td><td>O</td><td>P</td><td>Q</td>
 * </tr>
 * <tr>
 * <th align="left">Shard</th>
 * <td>0</td><td>0</td><td>1</td><td>1</td><td>1</td><td>1</td><td>1</td>
 * <td>2</td><td>2</td><td>3</td><td>3</td><td>3</td><td>3</td><td>3</td>
 * </tr>
 * </table></center>
 * <h3>Dissecting into shards</h3>
 * This is done as follows:
 * <ul>
 * <li>
 * Each node is visited in node order (that is alphabetical order in the
 * above tree) and added to the shard if there is enough room.
 * </li>
 * <li>
 * If a node is complete (which means that it and all its children already
 * belong to a shard) then it is ignored.
 * </li>
 * <li>
 * If the node will fit then it (and implicitly all its children) is added to
 * the shard, marked as complete and the available space is reduced by the size
 * of that nodes contents.
 * </li>
 * <li>
 * If a node will not fit in the shard and it has children then the amount of
 * space available is reduced by the node's overhead (which is typically the
 * size of the open and close tags, or the size of the navigation links) and
 * each child is visited.
 * </li>
 * <li>
 * If the node will not fit and doesn't have any children then the shard is
 * finished and this node will be the first node in the next shard.
 * </li>
 * <li>
 * When the last child of a node is added to a shard then that node is marked
 * as completed and won't be visited again.
 * </li>
 * <li>
 * If no element is added to a shard then a ProtocolException is thrown.
 * </li>
 * </ul>
 * <h3>Generating dissected contents</h3>
 * The contents of a dissected page are generated by recursing down through
 * the tree adding the open and close tags and the content to a buffer. When
 * the root of a dissectable tree is reached, it asks its children to generate
 * their content only if they belong to the requested shard.
 */
public final class Dissector
        implements DissectionConstants {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(Dissector.class);


    private final ProtocolConfiguration configuration;

    public Dissector(ProtocolConfiguration configuration) {

        this.configuration = configuration;
    }

    public void annotateDocument(
            DOMProtocol protocol,
            Document document) {

        DocumentAnnotation annotation = new DocumentAnnotation();
        annotation.setDocument(document);
        annotation.setProtocol(protocol);

        document.setObject(annotation);

        Annotator annotator = new Annotator(protocol);
        for (Node child = document.getContents(); child != null;
             child = child.getNext()) {

            child.accept(annotator);
        }
    }

    public int getContentsSize(
            DOMProtocol protocol,
            Document document) {

        DocumentAnnotation annotation =
                (DocumentAnnotation) document.getObject();
        return annotation.getContentsSize();
    }

    public void writeShard(Document document, Writer writer)
            throws IOException, ProtocolException {

        DocumentAnnotation annotation =
                (DocumentAnnotation) document.getObject();
        ReusableStringBuffer buffer = new ReusableStringBuffer();
        annotation.generateDissectedContents(buffer);
        writer.write(buffer.getChars(), 0, buffer.length());
    }

    private void annotateElement(
            DOMProtocol protocol,
            Element element) {

        String name = element.getName();

        ElementAnnotation annotation;

        if (DISSECTABLE_CONTENTS_ELEMENT.equals(name)) {
            annotation = new DissectableAnnotation();
        } else {
            annotation = new ElementAnnotation();
        }

        annotation.setElement(element);

        annotation.setProtocol(protocol);
        annotation.setAlwaysEmpty(configuration.isElementAlwaysEmpty(name));
        annotation.setAtomic(configuration.isElementAtomic(name));

        element.setObject(annotation);

        try {
            // Do any initialisation which depends on the above attributes.
            annotation.initialise();
        }
        catch (ProtocolException e) {
            logger.error("unexpected-exception", e);
        }
    }

    private void annotateText(
            DOMProtocol protocol,
            Text text) {

        TextAnnotation annotation = new TextAnnotation();
        annotation.setText(text);

        annotation.setProtocol(protocol);

        text.setObject(annotation);

        annotation.initialise();
    }

    public class Annotator
            extends RecursingDOMVisitor {

        private final DOMProtocol protocol;

        public Annotator(DOMProtocol protocol) {
            this.protocol = protocol;
        }


        public void visit(Element element) {
            annotateElement(protocol, element);

            element.forEachChild(this);
        }


        public void visit(Text text) {
            annotateText(protocol, text);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/

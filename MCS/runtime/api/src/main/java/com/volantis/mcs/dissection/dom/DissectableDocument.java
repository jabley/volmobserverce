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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-May-03    Paul            VBM:2003052901 - Created
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection.dom;

import com.volantis.mcs.dissection.string.DissectableString;
import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.dom.NodeAnnotation;
import com.volantis.mcs.dom.DocumentAnnotation;

/**
 * This interface defines the methods that need to be supported by a document
 * that can be dissected by this dissector.
 * <p>
 * An instance of this interface must be created for each document.
 * <h3>Document Normalisation</h3>
 * In order to ensure consistent dissection irrespective of the underlying
 * implementation a DissectableDocument must adhere to the following rules:
 * <ul>
 *   <li>Text nodes may not be adjacent to one another. If the underlying
 *       infrastructure contains adjacent text nodes then this interface must
 *       behave as if they have been merged into one.
 * </ul>
 * <h3>Output Document</h3>
 * <p>
 * This section describes some post conditions on the output document that
 * the dissector will satisfy.
 * <p>
 * If an element in the input document has children then any time that it is
 * present in the output document it will also have at least one child.
 * <p>
 * If an element is marked as atomic then any time that it is present in the
 * output document it will always have all the children that it had in the
 * input document.
 * <h3>Shared Content</h3>
 * <p>
 * Shared content is content that can be referenced (used) multiple times within
 * the document but its cost is the same no matter how many times it is
 * referenced. Each indiviual reference will have an overhead but that should
 * be relatively small compared to the cost of the shared content.
 * <p>
 * This means that the cost of a shared content (or actually a shared content
 * reference) depends on whether it has already been accounted for and so the
 * dissector needs to keep account of which shared content has already been
 * referenced; see {@link com.volantis.mcs.dissection.SharedContentUsages}.
 * <p>
 * All cost calculations are therefore performed within the context of a
 * SharedContentUsages in case a reference to shared content is found.
 * Calculating the cost of a shared content reference is done as follows:
 * <ol>
 *   <li>The SharedContentUsages is checked to see whether the shared content
 *       has already been referenced. If it has then the cost is simply the
 *       overhead of the reference itself.
 *   <li>Otherwise the cost is equal to the cost of the shared content plus
 *       the overhead of the reference. In addition the shared content usage is
 *       recorded in the SharedContentUsages object.
 * </ul>
 * <h3>Calculating Sizes</h3>
 * In any situation where the cost is not completely known (apart from that
 * already handled by the accumulator) the calculation methods,
 * {@link #addElementOverhead}, {@link #addShardLinkCost} and
 * {@link #addTextCost} must always assume the worst. e.g. in WBXML references
 * to shared content can be a variable number of bytes depending on where the
 * content is in the string table.
 * <h3>Multi Thread Support</h3>
 * // todo: Add documentation to each method explaining whether they can be
 * // todo: called concurrently by multiple threads.
 * <h3>Asset Packaging</h3>
 * It is the responsibility of the implementation of this interface to track
 * uses of assets within a dissected page.
 */
public interface DissectableDocument extends CostCalculator {

    /**
     * The copyright statement.
     */
    static String mark = "(c) Volantis Systems Ltd 2003.";

    // =========================================================================
    //   Constants
    // =========================================================================

    public static final int VARIABLE_COST = Accumulator.VARIABLE;

    // =========================================================================
    //   Document Methods
    // =========================================================================

    /**
     * Get the root element associated with this document.
     */
    public DissectableElement getRootElement();

    // =========================================================================
    //   Visit Methods
    // =========================================================================

    /**
     * Visit the document.
     * <p>
     * This method will invoke the appropriate method on the
     * <code>visitor</code> for the document.
     * @param visitor The objects whose visit methods should be invoked.
     * @throws DissectionException If the visitor method threw an exception.
     */
    public void visitDocument(DocumentVisitor visitor)
        throws DissectionException;

    /**
     * Visit the specified node.
     * <p>
     * This method will invoke the appropriate method on the
     * <code>visitor</code> for the type of node.
     * @param node The node to visit.
     * @param visitor The objects whose visit methods should be invoked.
     * @throws DissectionException If the visitor method threw an exception.
     */
    public void visitNode(DissectableNode node, DocumentVisitor visitor)
        throws DissectionException;

    /**
     * Visit all the children of the specified element in document order.
     * @param element The element whose children should be visited.
     * @param visitor The visitor class that will be called back for each of
     * the elements children.
     */
    public void visitChildren(DissectableElement element,
                              DocumentVisitor visitor)
        throws DissectionException;

    // =========================================================================
    //   Annotation Methods
    // =========================================================================

    /**
     * Set the annotation associated with the specified node.
     */
    public void setAnnotation(DissectableNode node,
                              NodeAnnotation annotation);

    /**
     * Get the annotation associated with the specified node.
     */
    public NodeAnnotation getAnnotation(DissectableNode node);

    /**
     * Set the annotation associated with this document.
     */
    public void setAnnotation(DocumentAnnotation annotation);

    /**
     * Get the annotation associated with this document.
     */
    public DocumentAnnotation getAnnotation();

    // =========================================================================
    //   Element Methods
    // =========================================================================

    /**
     * Return true if the specified element is atomic, i.e. must not be
     * dissected and false if it can be.
     * <p>
     * This method will not be invoked for shard link elements as the dissector
     * always treats them as atomic by the dissector.
     * @param element The element to check.
     * @return True if the element must not be dissected and false it if can
     * be.
     */
    public boolean isElementAtomic(DissectableElement element);

    /**
     * Check to see whether the element has any children.
     * @param element The element whose parenthood is being checked.
     * @return True if the elemnt has no children and false if it does.
     */
    public boolean isElementEmpty(DissectableElement element) throws DissectionException;

    /**
     * Get the type associated with the element.
     * <p>
     * This will be either one of the values returned by the following methods
     * or null for a 'plain' element.
     * <ul>
     *   <li>{@link DissectionElementTypes#getDissectableAreaType()}
     *   <li>{@link DissectionElementTypes#getKeepTogetherType()}
     *   <li>{@link DissectionElementTypes#getShardLinkConditionalType()}
     *   <li>{@link DissectionElementTypes#getShardLinkGroupType()}
     *   <li>{@link DissectionElementTypes#getShardLinkType()}
     *   <li>{@link DissectionElementTypes#getPlainElementType()}
     * </ul>
     * @param element The element whose type should be returned.
     * @return The element type.
     */
    public ElementType getElementType (DissectableElement element)
        throws DissectionException;

    // =========================================================================
    //   Text Methods
    // =========================================================================

    /**
     * Get a DissectiableString that encapsulates the content of the text node.
     * @param text The text node whose content should be encapsulated in the
     * returned DissectiableString.
     * @return A DissectableString that encapsulates the content of the text
     * node.
     * @throws DissectionException If there was a problem in this method.
     */
    public DissectableString getDissectableString(DissectableText text)
        throws DissectionException;

    // =========================================================================
    //   Iterator Methods
    // =========================================================================

    /**
     * Return an iterator that can be used to iterate over the child nodes of
     * the specified element starting with the specified child.
     * <p>
     * todo: Add in some example code.
     * <p>
     * The <code>start</code> parameter is provided because the dissector
     * needs to skip over children that have already been processed. It could
     * of course do it itself by using the iterator but it is possible that it
     * could be done more efficiently by the DOM itself.
     * @param element The element whose children should be iterated over.
     * @param iterator An iterator that was previously returned from this method
     * or null.
     * @param start The non negative index of the first child that should be
     * returned by the iterator.
     */
    public DissectableIterator childrenIterator(DissectableElement element,
                                                DissectableIterator iterator,
                                                int start);

    /**
     * Return an iterator that can be used to iterate over all the child nodes
     * of the specified element.
     * @param element
     * @param iterator
     * @return the dissectable iterator
     */
    public DissectableIterator childrenIterator(DissectableElement element,
                                                DissectableIterator iterator);

    // =========================================================================
    //   Walker Methods
    // =========================================================================

    /**
     * Get a walker that can be used to traverse the whole document.
     * @return A walker that can traverse the whole document.
     * todo: This needs looking at to see whether it is necessary, at the moment
     * todo: it is not needed.
     */
    //public DissectableWalker getWalker();

    // =========================================================================
    //   Shared Content Methods
    // =========================================================================

    /**
     * Get the number of pieces of shared content in this document.
     * @return The number of pieces of shared content in this document which
     * may be 0.
     */
    public int getSharedContentCount();

    // =========================================================================
    //   Debug Methods
    // =========================================================================

    // todo: document these
    
    /**
     * Get the element description.
     * @param element The element whose description should be returned.
     * @return A string describing the element.
     */
    public String getElementDescription(DissectableElement element)
        throws DissectionException;

    public String getEmptyElementDescription(DissectableElement element)
        throws DissectionException;

    public String getOpenElementDescription(DissectableElement element)
        throws DissectionException;

    public String getCloseElementDescription(DissectableElement element)
        throws DissectionException;

    public String getSharedContentDescription(int index)
        throws DissectionException;

    public String getTextDescription(DissectableText text)
        throws DissectionException;
}

/*
 * Local variables:
 * c-basic-offset: 4
 * End:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/2	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 30-Jun-03	605/1	geoff	VBM:2003060607 port from metis to mimas

 27-Jun-03	559/1	geoff	VBM:2003060607 make WML use protocol configuration again

 10-Jun-03	363/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/

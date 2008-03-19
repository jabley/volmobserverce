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
 * $Header: /src/voyager/com/volantis/mcs/protocols/dissection/DocumentAnnotation.java,v 1.4 2002/06/05 09:50:15 adrian Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Feb-02    Paul            VBM:2002021802 - Created. See class comment
 *                              for details.
 * 03-May-02    Paul            VBM:2002042203 - Wrapped all debug calls
 *                              which create a new String with a check to make
 *                              sure that debug is enabled. Also added support
 *                              for maximum content size.
 * 05-Jun-02    Adrian          VBM:2002021103 - Modified method generateDiss..
 *                              ..ectedContents to remove check for shardIndex
 *                              of -1 as this value can now never be returned.
 * 03-Jun-03    Allan           VBM:2003060301 - ObjectHelper moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.dissection;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.Comment;
import com.volantis.mcs.dom.DocType;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.runtime.FragmentationState;
import com.volantis.mcs.utilities.ReusableStringBuffer;
import com.volantis.synergetics.ObjectHelper;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Extra information associated with a document.
 */
public final class DocumentAnnotation {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(
                    DocumentAnnotation.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory
                    .createExceptionLocalizer(DocumentAnnotation.class);

    private Document document;

    private final List dissectableElements;

    private DOMProtocol protocol;

    public DocumentAnnotation() {
        dissectableElements = new ArrayList();
    }

    /**
     * Set the value of the protocol property.
     *
     * @param protocol The new value of the protocol property.
     */
    public void setProtocol(DOMProtocol protocol) {
        this.protocol = protocol;
    }

    /**
     * Get the value of the protocol property.
     *
     * @return The value of the protocol property.
     */
    public DOMProtocol getProtocol() {
        return protocol;
    }

    /**
     * Set the value of the document property.
     *
     * @param document The new value of the document property.
     */
    public void setDocument(Document document) {
        this.document = document;
    }

    private boolean duplicateDissectablePane(
            String inclusionPath,
            String dissectingPaneName) {

        int count = dissectableElements.size();
        for (int i = 0; i < count; i += 1) {
            Element element = (Element) dissectableElements.get(i);
            DissectableAnnotation annotation
                    = (DissectableAnnotation) element.getObject();
            String name = annotation.getDissectingPaneName();
            String path = annotation.getInclusionPath();
            if (ObjectHelper.equals(path, inclusionPath)
                    && name.equals(dissectingPaneName)) {
                return true;
            }
        }

        return false;
    }

    public void addDissectableElement(Element element)
            throws ProtocolException {

        DissectableAnnotation annotation
                = (DissectableAnnotation) element.getObject();

        String inclusionPath = annotation.getInclusionPath();
        String dissectingPaneName = annotation.getDissectingPaneName();
        if (duplicateDissectablePane(inclusionPath, dissectingPaneName)) {
            throw new ProtocolException(exceptionLocalizer.format(
                    "duplicate-dissectable-node",
                    new Object[]{inclusionPath, dissectingPaneName}));
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Adding element for dissectable pane "
                    + dissectingPaneName + " in inclusion "
                    + inclusionPath);
        }

        dissectableElements.add(element);
    }

    private int getFixedContentsSize() {
        int size = 0;
        final DocType docType = document.getDocType();
        if (docType != null) {
            size = docType.getAsString().length();
        }

        for (Node child = document.getContents(); child != null;
             child = child.getNext()) {
            if (!(child instanceof Comment)) {
                // ignore comments
                NodeAnnotation annotation = (NodeAnnotation) child.getObject();
                size += annotation.getFixedContentsSize();
            }
        }

        return size;
    }

    public int getContentsSize() {
        int size = 0;
        final DocType docType = document.getDocType();
        if (docType != null) {
            size = docType.getAsString().length();
        }

        for (Node child = document.getContents(); child != null;
             child = child.getNext()) {
            if (!(child instanceof Comment)) {
                // ignore comments
                NodeAnnotation annotation = (NodeAnnotation) child.getObject();
                size += annotation.getContentsSize();
            }
        }

        return size;
    }

    private void generateFixedContents(ReusableStringBuffer buffer) {
        for (Node child = document.getContents(); child != null;
             child = child.getNext()) {

            if (!(child instanceof Comment)) {

                NodeAnnotation annotation = (NodeAnnotation) child.getObject();
                annotation.generateFixedContents(buffer);
            }
        }
    }

    public void generateDissectedContents(ReusableStringBuffer buffer)
            throws ProtocolException {

        MarinerPageContext context = protocol.getMarinerPageContext();

        // Get the size of the fixed contents, that is the contents of all the
        // nodes apart from those underneath the DissectableNode.
        int fixedSize = getFixedContentsSize();

        if (logger.isDebugEnabled()) {
            logger.debug("Fixed contents size = " + fixedSize);
            ReusableStringBuffer fixedContents = new ReusableStringBuffer();
            generateFixedContents(fixedContents);
            if (logger.isDebugEnabled()) {
                logger.debug("Fixed contents are " + fixedContents);
            }
        }

        int limit = protocol.getMaxPageSize();
        int spaceRemaining = Integer.MAX_VALUE;
        int count = dissectableElements.size();

        // If the limit is -1 then there is no limit on the size of the page
        // but one of the dissecting panes must have a limit on its content.
        if (limit > -1) {
            // If the fixed size contents is greater than the limit then we have a
            // problem.
            if (fixedSize >= limit) {
                throw new ProtocolException(
                        exceptionLocalizer.format(
                                "fixed-size-contents-too-large"));
            }

            // Calculate the total space remaining.
            int totalRemaining = limit - fixedSize;
            if (logger.isDebugEnabled()) {
                logger.debug("Total space remaining is " + totalRemaining);
            }

            //
            // TODO: Multiple DissectingPanes
            //
            // Each dissecting pane should have a weight associated with it and
            // the total remaining space is shared out amongst all the dissecting
            // panes in a fragment according to the weighting.
            //

            // Each dissectable tree gets an equal share of the remaining space.
            int equalShare = (totalRemaining / count);
            if (logger.isDebugEnabled()) {
                logger.debug("Each of the " + count
                        + " dissectable areas has " + equalShare
                        + " space remaining");
            }
            spaceRemaining = equalShare;
        }

        // Get the fragmentation state.
        FragmentationState state = context.getFragmentationState();

        // Iterate over the dissectable elements and see whether they have a
        // shard specified. If they do then
        // Iterator through all of the dissectable trees requesting that they make
        // preparations for generating their shard contents. This involves finding
        // the shards.

        for (int i = 0; i < count; i += 1) {

            Element element = (Element) dissectableElements.get(i);

            DissectableAnnotation annotation
                    = (DissectableAnnotation) element.getObject();

            String dissectingPaneName = annotation.getDissectingPaneName();
            String inclusionPath = annotation.getInclusionPath();

            int shardIndex;
            if (state == null) {
                shardIndex = 0;
            } else {
                shardIndex =
                        state.getShardIndex(inclusionPath, dissectingPaneName);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Requested shard for dissectable pane "
                        + dissectingPaneName + " in inclusion "
                        + inclusionPath + " is " + shardIndex);
            }

            annotation.setRequestedShard(shardIndex);
            annotation.selectRequestedShard(spaceRemaining);
        }

        for (Node child = document.getContents(); child != null;
             child = child.getNext()) {
            if (!(child instanceof Comment)) {
                // ignore comments
                NodeAnnotation annotation = (NodeAnnotation) child.getObject();
                annotation.generateDissectedContents(buffer);
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/

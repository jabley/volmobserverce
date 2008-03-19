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

package com.volantis.mcs.dissection.annotation;

import com.volantis.mcs.dissection.*;
import com.volantis.mcs.dissection.dom.DissectableDocument;
import com.volantis.mcs.dissection.dom.DissectableElement;
import com.volantis.mcs.dissection.dom.DissectableNode;
import com.volantis.mcs.dissection.dom.DissectableText;
import com.volantis.mcs.dissection.impl.DissectionHelper;
import com.volantis.mcs.dissection.impl.ShardLinkActionImpl;
import com.volantis.mcs.dissection.impl.ShardLinkDetailsImpl;
import com.volantis.mcs.dissection.impl.UnsupportedVisitor;
import com.volantis.mcs.dissection.links.ShardLinkAttributes;
import com.volantis.mcs.dissection.links.ShardLinkConditionalAttributes;
import com.volantis.mcs.dissection.links.ShardLinkGroupAttributes;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.io.PrintWriter;
import java.util.*;

/**
 * This class is responsible for creating a DissectedDocument from a
 * DissectableDocument.
 * <p>
 * It does this as follows:
 * <p>
 * Firstly it traverses through those nodes that form part of the fixed content
 * and calculates the total cost of all the fixed content as well as the
 * contribution that the fixed content makes to the total content cost. This
 * traversal covers the root elements of the DissectableAreas but does not
 * cover their contents. It also creates a list of any shard link elements so
 * that they can be processed in the second phase.
 * <p>
 * It then traverses the contents of each DissectableArea in turn annotating
 * each node with information needed by the dissector and also updating the
 * total content cost.
 * <p>
 * The reason for separating this out into two phases is that the second phase
 * has to determine whether the overhead of an element or the cost of a text
 * node is variable, i.e. references shared content <strong>that is not
 * referenced from within fixed content</strong>. Obviously it is not possible
 * to know whether shared content is referred to from fixed content without
 * first visiting all the fixed content.
 * <p>
 * After all the above has been done it then checks to make sure that the
 * DissectableAreas have been initialised properly which means that all
 * DissectableAreas have been initialised and that all of them have at least one
 * next and one previous link.
 * <h3>Shard Links</h3>
 * <p>
 * Shard links must be outside a DissectableArea but must specify the
 * DissectableArea that they belong to. These references are resolved by this
 * class as part of its work.
 * <p>
 * The shard links towards their owning DissectableArea's overhead. They do not
 * affect either the fixed content or total content costs.
 */
public class Annotator {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(Annotator.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(Annotator.class);

    /**
     * This is used to accumulate the total cost of the fixed content.
     * <p>
     * If the page needs dissecting because the total cost of all the content
     * is greater than the device specific limit then this value is subtracted
     * from the device specific limit in order to find the space that can be
     * shared between the dissection areas.
     */
    private Cost fixedContentCost;

    /**
     * This is used to accumulate the total cost of all the content.
     * <p>
     * If this value is less than the device specific limit then the page does
     * not actually need dissecting. Therefore no dissection specific overhead
     * such as shard links should be added to this total.
     */
    private Cost totalContentCost;

    /**
     * The document annotation.
     */
    private DissectedDocumentImpl dissectedDocument;

    /**
     * The list of dissection areas.
     * todo: This should be moved into Phase1Visitor
     */
    private List dissectableAreas;

    /**
     * A map from dissectable area identity to dissectable areas.
     * todo: This should be moved into Phase1Visitor
     */
    private Map identityToArea;

    /**
     * This list contains all the shard link groups in the input document.
     * They are collated in this collection and processed during the second
     * phase of the annotation process.
     */
    private List shardLinkGroups;

    /**
     * A pathological set of shard link details that will provide us with a
     * worst case scenario for the size of a URL.
     */
    private ShardLinkDetailsImpl pathologicalShardLinkDetails;

    /**
     * This is the entry point to the annotator. All other methods should be
     * treated as private.
     */
    public DissectedDocumentImpl annotateDocument(DissectionContext dissectionContext,
                                                  DissectableDocument document,
                                                  DissectionCharacteristics characteristics,
                                                  DissectionURLManager urlManager,
                                                  DocumentInformation docInfo)
        throws DissectionException {

        // Create the document annotation.
        dissectedDocument = new DissectedDocumentImpl();
        dissectedDocument.setDocument(document);
        document.setAnnotation(dissectedDocument);

        // Get a Cost object to track the total content and the fixed content
        // costs.
        totalContentCost = dissectedDocument.getTotalContentCost();
        fixedContentCost = dissectedDocument.getFixedContentCost();

        // Create the list of dissection areas.
        dissectableAreas = new ArrayList();

        // Create a map from DissectableAreaIdentity to DissectableArea. We use
        // a TreeMap so that we can use a Comparator that works specifically on
        // a DissectableAreaIdentity rather than relying on the hashing
        // behaviour of the implementing class.
        Comparator comparator
            = DissectionHelper.DISSECTABLE_AREA_IDENTITY_COMPARATOR;
        identityToArea = new TreeMap(comparator);

        shardLinkGroups = new ArrayList();

        // Add the document overhead to the total and fixed costs.
        totalContentCost.addDocumentOverhead(document);
        fixedContentCost.addDocumentOverhead(document);

        // Phase 1: Visit all the fixed content, dissectable areas and shard
        // links.
        if (logger.isDebugEnabled()) {
            logger.debug("Beginning Phase 1");
        }
        Phase1Visitor phase1Visitor = new Phase1Visitor();
        phase1Visitor.process(document);

        if (logger.isDebugEnabled()) {
            logger.debug("Phase 1: Calculating shard link costs");
        }

        // Create an array that contains the overhead cost for each
        // dissectable area. This information could go in the DissectableArea
        // itself but it is only used in the Annotator so here it is.
        Cost[] overheadCosts = new Cost[dissectableAreas.size()];

        MarinerURL documentURL = docInfo.getDocumentURL();
        MarinerURL pathologicalURL
            = urlManager.makePathologicalURL(dissectionContext, documentURL);

        // Iterate through the shard links adding the overhead to the
        // dissectable areas.
        pathologicalShardLinkDetails = new ShardLinkDetailsImpl();
        pathologicalShardLinkDetails.setDestinationShardIndex(99);
        pathologicalShardLinkDetails.setURL(pathologicalURL);

        ShardLinkGroupVisitor shardLinkGroupVisitor
            = new ShardLinkGroupVisitor();
        final int shardLinkGroupCount = shardLinkGroups.size();
        for (int i = 0; i < shardLinkGroupCount; i += 1) {
            ShardLinkGroup shardLinkGroup
                = (ShardLinkGroup) shardLinkGroups.get(i);
            shardLinkGroupVisitor.process(document, overheadCosts, shardLinkGroup);
        }

        // Get the max links per group value from the visitor.
        int maxLinksPerGroup = shardLinkGroupVisitor.getMaxLinksPerGroup();

        // Copy the contents of the dissectable areas list into an array. They
        // are stored in an array as the number of them will not change and
        // accessing an array is faster than accessing a list. The cost of
        // converting the list to an array should be offset by the savings on
        // access time as the array will be accessed a lot.
        DissectableArea[] areaArray
            = new DissectableArea[dissectableAreas.size()];
        dissectableAreas.toArray(areaArray);

        if (logger.isDebugEnabled()) {
            logger.debug("Phase 1 complete, beginning Phase 2");
        }

        // Now iterate through the dissectable areas.
        Phase2Visitor phase2Visitor = new Phase2Visitor();
        final int dissectableAreaCount = areaArray.length;
        for (int i = 0; i < dissectableAreaCount; i += 1) {
            DissectableArea area = areaArray[i];

            // Set the overhead of the document.
            Cost overheadCost = overheadCosts[i];
            if (overheadCost == null) {
                throw new DissectionException(
                            exceptionLocalizer.format(
                                        "dissectable-area-no-overhead-cost",
                                        area.getIdentity()));
            }
            int overhead = overheadCost.getTotal();
            if (logger.isDebugEnabled()) {
                logger.debug("Setting overhead of dissectable area " + area
                             + " to " + overhead);
            }
            area.setOverhead(overhead);

            phase2Visitor.process(document, area);
        }

        // Set the location of the document.
        dissectedDocument.setDocumentLocation(docInfo.getDocumentURL());

        // Set the array of dissectable areas.
        dissectedDocument.setDissectableAreas(areaArray);

        dissectedDocument.setUrlManager(urlManager);

        // Set the max links per group property.
        dissectedDocument.setMaxLinksPerGroup(maxLinksPerGroup);

        // Calculate the available space in each dissectable area.
        int maxPageSize = characteristics.getMaxPageSize();
        int fixedContentSize = fixedContentCost.getTotal();

        // If the limit is DissectionCharacteristics.UNLIMITED_PAGE_SIZE then
        // there is no limit on the size of the page but one of the dissecting
        // panes must have a limit on its content.
        int spaceRemaining = Integer.MAX_VALUE;
        if (maxPageSize != DissectionCharacteristics.UNLIMITED_PAGE_SIZE) {

            // If the fixed size contents is greater than the limit then we
            // have a problem.
            if (fixedContentSize >= maxPageSize) {

                dissectedDocument.debugOutput(new PrintWriter(System.out));

                throw new DissectionException(exceptionLocalizer.format(
                            "fixed-content-size-too-large",
                            new Object[]{new Integer(maxPageSize),
                                         new Integer(fixedContentSize)}));
            }

            // Only calculate the space available if there are any dissectable
            // areas.
            if (dissectableAreaCount != 0) {
                // Calculate the total space remaining.
                int totalRemaining = maxPageSize - fixedContentSize;
                if (logger.isDebugEnabled()) {
                    logger.debug("Total space remaining is " + totalRemaining);
                }

                //
                // TODO: Multiple DissectingPanes
                //
                // Each dissecting pane should have a weight associated with it
                // and the total remaining space is shared out amongst all the
                // dissecting panes in a fragment according to the weighting.
                //

                // Each dissectable tree gets an equal share of the remaining
                // space.
                int equalShare = (totalRemaining / dissectableAreaCount);
                if (logger.isDebugEnabled()) {
                    logger.debug("Each of the " + dissectableAreaCount
                                 + " dissectable areas has " + equalShare
                                 + " space remaining");
                }
                spaceRemaining = equalShare;
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Space remaining for each dissectable area is "
                         + spaceRemaining);
        }

        // Iterate over the dissectable areas informing them of their available
        // space and then making sure that they are properly initialised.
        for (int i = 0; i < dissectableAreaCount; i += 1) {
            DissectableArea area = areaArray[i];
            area.setAvailableSpace(spaceRemaining);

            area.initialise(dissectedDocument);
        }

        return dissectedDocument;
    }

    // =========================================================================
    //   Dissectable Content Methods
    // =========================================================================

    private void bindNode(DissectableDocument document,
                          DissectableNode node,
                          DissectableNodeAnnotation annotation) {

        annotation.setDocument(document);
        document.setAnnotation(node, annotation);
    }

    private void bindElement(DissectableDocument document,
                             DissectableElement element,
                             ElementAnnotation annotation) {
        bindNode(document, element, annotation);
        annotation.setElement(element);
    }

    private void bindShardLinkElement(DissectableDocument document,
                                      DissectableElement element,
                                      AbstractShardElementAnnotation annotation) {

        document.setAnnotation(element, annotation);
        annotation.setElement(element);
    }

    private void bindText(DissectableDocument document,
                          DissectableText text,
                          TextAnnotation annotation) {
        bindNode(document, text, annotation);
        annotation.setText(text);
    }

    /**
     * Get the DissectableArea with the specified identity.
     * <p>
     * If the DissectableArea does not exist then this method will create it.
     * <p>
     * As far as this method is concerned a DissectableArea exists in two
     * states initialised and unintitialised. An initialised area is one that
     * has had its index set and been added to the list, an unitialised one has
     * not. However, they are both added into the map.
     * <p>
     * A DissectableArea can only be initialised once, an attempt to initialise
     * it again is assumed to be the result of areas with duplicate identities
     * within the page which is strictly forbidden and will result in an error.
     * <p>
     * The reason for this distinction is that it is possible that a shard link
     * will be processed before the DissectableArea that it refers to is
     * processed. If this happens then the shard link will create the
     * DissectableArea in an uninitialised state and it will be initialised when
     * the DissectableArea is visited.
     * <p>
     * If a shard link mistakenly refers to a DissectableArea that does not
     * exist then it will be caught in the post visting checking performed in
     * {@link #annotateDocument}.
     * @param identity The identity of the DissectableArea.
     * @param initialise If true then the DissectableArea that is returned
     * will be initialised. If it was already initialised then there is a
     * duplicate and an exception is thrown.
     * @return The DissectableArea associated with the specified identity.
     */
    private
        DissectableArea getDissectableArea(DissectableAreaIdentity identity,
                                           boolean initialise)
        throws DissectionException {

        DissectableArea area = (DissectableArea) identityToArea.get(identity);
        if (area == null) {
            area = new DissectableArea();
            identityToArea.put(identity, area);
        }

        if (initialise) {
            if (area.getIndex() == DissectableArea.UNINITIALISED_INDEX) {
                int index = dissectableAreas.size();
                dissectableAreas.add(area);
                area.setIndex(index);
            } else {
                throw new DissectionException(
                            exceptionLocalizer.format("duplicate-areas",
                                                      identity));
            }
        }

        return area;
    }

    /**
     * This class supports the first phase.
     */
    private class Phase1Visitor
        extends UnsupportedVisitor {

        public Phase1Visitor() {
        }

        public void process(DissectableDocument document)
            throws DissectionException {

            document.visitDocument(this);
        }

        public void visitDocument(DissectableDocument document)
            throws DissectionException {

            this.document = document;
            try {
                document.visitNode(document.getRootElement(), this);
            } finally {
                this.document = null;
            }
        }

        public void visitElement(DissectableElement element)
            throws DissectionException {

            totalContentCost.addElementOverhead(document, element);
            fixedContentCost.addElementOverhead(document, element);

            document.visitChildren(element, this);
        }

        public void visitText(DissectableText text)
            throws DissectionException {

            totalContentCost.addTextCost(document, text);
            fixedContentCost.addTextCost(document, text);
        }

        public void visitShardLinkGroup(DissectableElement element)
            throws DissectionException {

            ShardLinkGroupAttributes attributes
                = (ShardLinkGroupAttributes) document.getAnnotation(element);

            // The shard link group must specify a DissectableAreaIdentity.
            DissectableAreaIdentity identity = attributes.getDissectableArea();
            DissectableArea area;
            if (identity == null) {
                throw new DissectionException(
                            exceptionLocalizer.format(
                                        "dissectable-area-id-not-specified"));
            } else {
                area = getDissectableArea(identity, false);
            }

            // Create the ShardLink and initialise it.
            ShardLinkGroup shardLinkGroup = new ShardLinkGroup();

            // Create a link from the group to the dissectable area to which it
            // belongs.
            shardLinkGroup.setDissectableArea(area);
            //area.addShardLink(shardLinkGroup);

            // Bind the shard link group to the element.
            bindShardLinkElement(document, element, shardLinkGroup);

            // Accumulate all the shard link elements into a list so that they
            // can be processed as part of the second phase.
            shardLinkGroups.add(shardLinkGroup);

            // The children of this element are visited in phase 2.
        }

        /**
         * Initialise the DissectableArea.
         * <p>
         * The class documentation explains why this method does not visit the
         * children of the DissectableArea.
         */
        public void visitDissectableArea(DissectableElement element)
            throws DissectionException {

            DissectableAreaAttributes attributes
                = (DissectableAreaAttributes) document.getAnnotation(element);
            DissectableAreaIdentity identity = attributes.getIdentity();

            // Get the dissectable area and make sure that it is initialised.
            DissectableArea area = getDissectableArea(identity, true);
            area.setIdentity(identity);

            bindElement(document, element, area);

            // Create a SharedContentUsage object if necessary.
            SharedContentUsages usages;
            usages = DissectionHelper.createSharedContentUsages(document);
            area.setOverheadUsages(usages);

            if (logger.isDebugEnabled()) {
                logger.debug("Phase1: Skipping contents of dissectable area "
                             + identity);
            }
        }
    }

    private class ShardLinkGroupVisitor
        extends UnsupportedVisitor {

        /**
         * The maximum number of links per group in the whole document. This
         * is used to allow the serializer to create a single
         * {@link com.volantis.mcs.dissection.links.rules.ShardLinkContentRuleContext}
         * object that can be used for all the shard link groups.
         */
        private int maxLinksPerGroup;

        private ShardLinkGroup shardLinkGroup;

        private Cost overheadCost;

        /**
         * A list of the shard links associated with this group.
         */
        private List shardLinks;

        //private DissectableArea dissectableArea;

        public ShardLinkGroupVisitor() {
            // Create the list of shard links.
            shardLinks = new ArrayList();
        }

        public void process(DissectableDocument document,
                            Cost[] overheadCosts,
                            ShardLinkGroup shardLinkGroup)
            throws DissectionException {

            // Save away the document for use by the visit methods.
            this.document = document;

            DissectableArea dissectableArea
                = shardLinkGroup.getDissectableArea();
            this.shardLinkGroup = shardLinkGroup;

            // At this point the index may not have been set yet so make sure
            // that it is. If it is not then it means that the shard link
            // group referred to a dissectable area that did not exist.
            int index = dissectableArea.getIndex();
            if (index < 0) {
                // MCSDI0006X="Dissectable area {1} referenced from {2} does not exist"
                throw new DissectionException(
                            exceptionLocalizer.format(
                                        "dissectable-area-not-found",
                                        new Object[]{dissectableArea,
                                                     shardLinkGroup}));                    
            }
            overheadCost = overheadCosts[index];
            if (overheadCost == null) {
                overheadCost = new Cost("Dissectable Area",
                                        dissectableArea.getOverheadUsages(),
                                        true);
                overheadCosts[index] = overheadCost;
            }

            // Clear the list of shard links.
            shardLinks.clear();

            // Visit all the children of the shard link group. There is no
            // need to visit the shard link group itself.
            DissectableElement element = shardLinkGroup.getElement();
            document.visitChildren(element, this);

            // Create an array from the list as it will not change.
            int size = shardLinks.size();
            ShardLink[] links = new ShardLink[size];
            shardLinks.toArray(links);
            shardLinkGroup.setShardLinks(links);

            if (size > maxLinksPerGroup) {
                maxLinksPerGroup = size;
            }
        }

        public int getMaxLinksPerGroup() {
            return maxLinksPerGroup;
        }

        public void visitElement(DissectableElement element)
            throws DissectionException {

            overheadCost.addElementOverhead(document, element);

            document.visitChildren(element, this);
        }

        public void visitText(DissectableText text)
            throws DissectionException {

            overheadCost.addTextCost(document, text);
        }

        public void visitShardLink(DissectableElement element)
            throws DissectionException {

            // A shard link element is treated as a single entity for sizing
            // and output.
            overheadCost.addShardLinkCost(document, element,
                                          pathologicalShardLinkDetails);

            ShardLinkAttributes attributes
                = (ShardLinkAttributes) document.getAnnotation(element);

            // Create the ShardLink and initialise it.
            ShardLinkActionImpl action
                = (ShardLinkActionImpl) attributes.getAction();
            ShardLink shardLink = new ShardLink();
            shardLink.setAction(action);

            // Create a linkage between the shard link and the area to which it
            // belongs.
            shardLink.setShardLinkGroup(shardLinkGroup);
            shardLinks.add(shardLink);

            DissectableArea area = shardLinkGroup.getDissectableArea();
            area.addShardLink(shardLink, action);

            bindShardLinkElement(document, element, shardLink);
        }

        public void visitShardLinkConditional(DissectableElement element)
            throws DissectionException {

            ShardLinkConditionalAttributes attributes
                = (ShardLinkConditionalAttributes) document.getAnnotation(element);

            // Create the ShardLink and initialise it.
            ShardLinkConditional shardLinkConditional
                = new ShardLinkConditional();
            shardLinkConditional.setContentRule(attributes.getContentRule());

            // Create a linkage between the shard link and the area to which it
            // belongs.
            shardLinkConditional.setShardLinkGroup(shardLinkGroup);

            bindShardLinkElement(document, element, shardLinkConditional);

            // A shard link conditional has no cost but its content does.
            document.visitChildren(element, this);
        }
    }

    private class Phase2Visitor
        extends UnsupportedVisitor {

        private DissectableArea dissectableArea;

        /**
         * This is the cost of the dissectable contents of the shard.
         * <p>
         * It is updated by each node that is visited. Elements add their
         * overhead and text nodes add their cost.
         * <p>
         * In order to manage the propogation of sizes up the tree properly
         * each element resets this back to 0 and uses it to calculate its
         * overhead and the cost of all its children. It then restores this
         * to its previous state and adds its own aggregate cost into it.
         */
        private Cost dissectableCost;

        public Phase2Visitor() {
        }

        public void process(DissectableDocument document,
                            DissectableArea dissectableArea)
            throws DissectionException {

            // Save away the document for use by the visit methods.
            this.document = document;

            // Initialise the cost.
            dissectableCost = new Cost("DISSECTABLE-AREA",
                                       dissectableArea.getOverheadUsages(),
                                       false);

            document.visitNode(dissectableArea.getElement(), this);
        }

        protected void processElement(DissectableDocument document,
                                      DissectableElement element,
                                      ElementAnnotation annotation,
                                      boolean updateOverhead)
            throws DissectionException {

            // Save away the current cost of the containing element for use
            // later.
            String description = document.getElementDescription(element);
            String oldDescription = dissectableCost.saveDescription(description);
            int oldTotal = dissectableCost.saveTotal(0);
            int overhead;

            // Calculate the overhead of this element within dissectable
            // content. Store it in the annotation and update the total
            // dissectableCost.
            if (updateOverhead) {
                // Add the overhead of the element to the total contents size.
                totalContentCost.addElementOverhead(document, element);

                // Calculate the overhead of the element using a temporary
                // Cost variable.
                dissectableCost.addElementOverhead(document, element);
                overhead = dissectableCost.getTotal();

                // Store the overhead away.
                annotation.setOverhead(overhead);
            } else {
                // This must not update the overhead of the element as it will
                // have already been set.
                overhead = 0;
            }

            // Add the cost of the children to the element overhead to form the
            // aggregate cost of this element.
            document.visitChildren(element, this);

            // Restore the old total and get the aggregate cost of the element.
            int aggregateCost = dissectableCost.restoreTotal(oldTotal);
            dissectableCost.restoreDescription(oldDescription);

            // Add the aggregate cost of this element to the aggregate cost of
            // the containing element.
            dissectableCost.addElementCost(document, element, aggregateCost);

            // Store the aggregate cost away.
            annotation.setAggregateCost(aggregateCost);

        }

        public void visitElement(DissectableElement element)
            throws DissectionException {

            ElementAnnotation annotation = new ElementAnnotation();

            bindElement(document, element, annotation);

            // Set the atomic flag, it is done here rather than in the
            // processElement method because the special elements are not
            // atomic.
            annotation.setAtomic(document.isElementAtomic(element));

            processElement(document, element, annotation, true);
        }

        public void visitText(DissectableText text)
            throws DissectionException {

            TextAnnotation annotation = new TextAnnotation();

            bindText(document, text, annotation);

            totalContentCost.addTextCost(document, text);

            // Calculate the cost of this node.
            int oldTotal = dissectableCost.saveTotal(0);
            dissectableCost.addTextCost(document, text);
            int cost = dissectableCost.restoreTotal(oldTotal);

            // Add the text cost to the cost of the containing element.
            dissectableCost.addTextCost(document, text, cost);

            // Store the cost of the text.
            annotation.setAggregateCost(cost);
        }

        public void visitKeepTogether(DissectableElement element)
            throws DissectionException {

            KeepTogetherAttributes attributes
                = (KeepTogetherAttributes) document.getAnnotation(element);

            KeepTogether keepTogether = new KeepTogether();
            boolean forceDissection = false;
            if (attributes != null) {
                boolean forceBreakAfter;
                boolean forceBreakBefore;
                forceBreakAfter = attributes.getForceBreakAfter();
                forceBreakBefore = attributes.getForceBreakBefore();
                keepTogether.setForceBreakAfter(forceBreakAfter);
                keepTogether.setForceBreakBefore(forceBreakBefore);

                forceDissection = forceBreakAfter || forceBreakBefore;
            }

            bindElement(document, element, keepTogether);

            if (logger.isDebugEnabled()) {
                logger.debug("Visited Keep Together");
            }

            // The keep together element has no overhead in either fixed or
            // total content.
            processElement(document, element, keepTogether, false);

            // If we are required to force dissection then we pretend that this
            // element has an infinite overhead. This causes all ancestor
            // elements to have an infinite cost which causes them all to
            // be dissected.
            if (forceDissection) {
                dissectableCost.addElementOverhead(document, element,
                                                   Cost.INFINITE);
            }
        }

        public void visitDissectableArea(DissectableElement element)
            throws DissectionException {

            DissectableArea area
                = (DissectableArea) document.getAnnotation(element);

            // The dissectable area element has already calculated its overhead.
            processElement(document, element, area, false);
        }
    }

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

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/1	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 10-Jun-03	363/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/

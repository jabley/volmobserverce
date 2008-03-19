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

package com.volantis.mcs.dissection.impl;

import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.SharedContentUsages;
import com.volantis.mcs.dissection.annotation.*;
import com.volantis.mcs.dissection.dom.*;
import com.volantis.mcs.dissection.links.ShardLinkDetails;
import com.volantis.mcs.dissection.links.rules.ShardLinkContentRule;
import com.volantis.mcs.dissection.links.rules.StandardContentRules;
import com.volantis.mcs.dissection.string.StringSegment;

public class DissectedDocumentSerializer
    extends AbstractDocumentVisitor {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private DissectedContentHandler contentHandler;

    private DissectableArea currentArea;

    private Shard currentShard;

    private SelectedShards selectedShards;

    /**
     * The default context that will be used for all groups.
     */
    private ShardLinkContentRuleContextImpl defaultContentRuleContext;

    /**
     * The context within which the conditional shard rules will run. This
     * is only set within the body of a shard link group element.
     */
    private ShardLinkContentRuleContextImpl contentRuleContext;

    /**
     * If true then the current node is being visited before its children
     * (if any) otherwise it is being visited after them.
     */
    private boolean beforeChildren;

    /**
     * The current state of this object.
     */
    private int state;

    /**
     * In this state the serializer simply serializes all the elements it sees.
     */
    private static final int SERIALIZING_ALL = 0;

    /**
     * Indicates that this object is currently serializing a shard and therefore
     * needs to take into account whether a node belongs to a shard or not.
     */
    private static final int SERIALIZING_SHARD = 1;

    /**
     * Indicates that all the nodes that are in the shard are finished and
     * that all is needed is to return back to the dissectable area.
     */
    private static final int SHARD_COMPLETE = 2;

    /**
     * If this is not null then it is an ancestor node of the current node that
     * is completely inside a single shard. It is set the first time that the
     * node is visited and cleared the last time that the node is visited. It
     * is not set for leaf nodes.
     */
    private DissectableNode completeNode;

    public void visitDocument(DissectedDocumentImpl dissectedDocument,
                              SelectedShards selectedShards,
                              DissectedContentHandler contentHandler)
        throws DissectionException {

        this.selectedShards = selectedShards;
        this.contentHandler = contentHandler;

        document = dissectedDocument.getDocument();

        // Create a ShardLink
        int maxLinksPerGroup = dissectedDocument.getMaxLinksPerGroup();
        defaultContentRuleContext
            = new ShardLinkContentRuleContextImpl(maxLinksPerGroup);

        SharedContentUsages usages = mergeUsages(dissectedDocument,
                                                 selectedShards);

        contentHandler.startDocument(document, usages);

        document.visitDocument(this);

        contentHandler.endDocument();
    }

    SharedContentUsages mergeUsages(DissectedDocumentImpl annotation,
                                    SelectedShards selectedShards) {
        // Merge all the shared content tables together.
        SharedContentUsages union
            = DissectionHelper.createSharedContentUsages(document);

        if (union == null) {
            return union;
        }

        SharedContentUsages fixedUsages = annotation.getFixedContentUsages();
        union.addSharedContentUsages(fixedUsages);

        // Iterate over the DissectableAreas and the shards within them.
        int count = selectedShards.getCount();
        for (int i = 0; i < count; i += 1) {
            // Add all the shared references from the shards.
            Shard shard = selectedShards.getShard(i);
            SharedContentUsages shardUsages = shard.getSharedContentUsages();
            union.addSharedContentUsages(shardUsages);

            // Add all the shared references from the shard links but only if
            // they are actually needed.
            // todo: check this as I am not thinking straight.
            if (shard.getNextLink() != null
                || shard.getPreviousLink() != null) {

                DissectableArea area = annotation.getDissectableArea(i);
                SharedContentUsages overheadUsages = area.getOverheadUsages();
                union.addSharedContentUsages(overheadUsages);
            }
        }

        return union;
    }

    protected void visitDocumentImpl(DissectableDocument document)
        throws DissectionException {
        visitRootElement(document);
    }

    public void visitElement(DissectableElement element)
        throws DissectionException {

        visitElement(element, true);
    }

    public void visitElement(DissectableElement element,
                             boolean generateTags)
        throws DissectionException {

        if (state == SERIALIZING_ALL) {
            serializeElement(element, generateTags);
        } else {
            DissectableNodeAnnotation annotation;
            int relationship;

            // Get the relation ship of this element with the current
            // shard.
            annotation = (DissectableNodeAnnotation) document.getAnnotation(element);
            relationship = annotation.getRelationshipToShard(currentShard);
            switch (relationship) {
                case DissectableNodeAnnotation.INSIDE_SHARD:
                    // Serialize all the children of this element irrespective
                    // of whether they think they need to or not.
                    state = SERIALIZING_ALL;

                    serializeElement(element, generateTags);

                    // Revert the state back to what it was before.
                    state = SERIALIZING_SHARD;

                    // Return immediately as we have processed this element.
                    return;

                case DissectableNodeAnnotation.WHOLE_SHARD:
                case DissectableNodeAnnotation.BEGINNING_OF_SHARD:
                case DissectableNodeAnnotation.END_OF_SHARD:
                    // Nothing to do apart from processing the children as
                    // normal.
                    break;

                case DissectableNodeAnnotation.AFTER_SHARD:
                    // This node is after the required shard so the shard is
                    // complete.
                    state = SHARD_COMPLETE;
                    return;

                case DissectableNodeAnnotation.BEFORE_SHARD:
                    return;

                default:
                    throw new IllegalStateException("Unknown relationship "
                                                    + relationship);
            }

            if (document.isElementEmpty(element)) {
                if (generateTags) {
                    contentHandler.emptyElement(element);
                }
            } else {
                if (generateTags) {
                    contentHandler.startElement(element);
                }

                DissectableIterator iterator
                    = document.childrenIterator(element, null);
                for (; iterator.hasNext();) {
                    DissectableNode child = iterator.next();
                    document.visitNode(child, this);

                    // If the state has moved to finish then break out of the
                    // loop.
                    if (state == SHARD_COMPLETE) {
                        break;
                    }
                }

                if (generateTags) {
                    contentHandler.endElement(element);
                }
            }
        }
    }

    private void serializeElement(DissectableElement element,
                                  boolean generateTags)
        throws DissectionException {

        if (document.isElementEmpty(element)) {
            if (generateTags) {
                contentHandler.emptyElement(element);
            }
        } else {
            if (generateTags) {
                contentHandler.startElement(element);
            }

            document.visitChildren(element, this);

            if (generateTags) {
                contentHandler.endElement(element);
            }
        }

    }

    public void visitText(DissectableText text)
        throws DissectionException {

        if (state == SERIALIZING_ALL) {
            contentHandler.text(text);
        } else {
            DissectableNodeAnnotation annotation;
            int relationship;

            // Get the relation ship of this element with the current
            // shard.
            annotation = (DissectableNodeAnnotation) document.getAnnotation(text);
            relationship = annotation.getRelationshipToShard(currentShard);
            switch (relationship) {
                case DissectableNodeAnnotation.INSIDE_SHARD:
                    contentHandler.text(text);
                    return;

                case DissectableNodeAnnotation.WHOLE_SHARD:
                case DissectableNodeAnnotation.BEGINNING_OF_SHARD:
                case DissectableNodeAnnotation.END_OF_SHARD:
                    // todo: the segment presumably effectively tracks 
                    // todo: whether this is whole/beginning/end as far as is 
                    // todo: necessasry through presence or otherwise of prefix
                    // todo: and suffix. So maybe we can combine these three states?
                    TextAnnotation texta = (TextAnnotation) annotation;
                    StringSegment segment = texta.getSegment(currentShard);
                    contentHandler.text(text, segment);
                    break;

                case DissectableNodeAnnotation.AFTER_SHARD:
                    // This node is after the required shard so the shard is
                    // complete.
                    state = SHARD_COMPLETE;
                    return;

                case DissectableNodeAnnotation.BEFORE_SHARD:
                    return;

                default:
                    throw new IllegalStateException("Unknown relationship "
                                                    + relationship);
            }


        }
    }

    public void visitShardLink(DissectableElement element)
        throws DissectionException {

        // Retrieve the shard for which the ShardLink is used. The element
        // annotation is the ShardLink object, that has a reference to the
        // DissectableArea that it belongs to, that can be used to look up
        // the shard in the currently selected shards.
        ShardLink shardLink = (ShardLink) document.getAnnotation(element);
        DissectableArea area = shardLink.getDissectableArea();
        Shard shard = selectedShards.getShard(area);

        // Get the details from the shard.
        ShardLinkDetails details = shardLink.getDetails(shard);

        // If the details are null then the shard does not require that link
        // so we must not serialize it.
        if (details != null) {
            contentHandler.shardLink(element, details);

            // Use an unused separator if there was one.
            contentRuleContext.usedSeparator();
        }

        // Update the position whether or not we wrote out a shard link.
        contentRuleContext.nextPosition();
    }

    public void visitShardLinkGroup(DissectableElement element)
        throws DissectionException {

        ShardLinkGroup group
            = (ShardLinkGroup) document.getAnnotation(element);

        contentRuleContext = defaultContentRuleContext;
        contentRuleContext.initialise(group, selectedShards);

        // The shard link group does not output anything if none of the links
        // need outputting. It behaves exactly like a conditional with an any
        // rule.
        checkConditional(StandardContentRules.getAnyRule(), element);

        contentRuleContext = null;
    }

    private void checkConditional(ShardLinkContentRule contentRule,
                                  DissectableElement element)
        throws DissectionException {

        // If the rule evaluates to true then output the contents.
        if (contentRule.checkRule(contentRuleContext)) {
            document.visitChildren(element, this);
        }
    }

    public void visitShardLinkConditional(DissectableElement element)
        throws DissectionException {

        ShardLinkConditional conditional
            = (ShardLinkConditional) document.getAnnotation(element);
        ShardLinkContentRule contentRule = conditional.getContentRule();

        checkConditional(contentRule, element);
    }

    public void visitDissectableArea(DissectableElement element)
        throws DissectionException {

        currentArea = (DissectableArea) document.getAnnotation(element);
        currentShard = selectedShards.getShard(currentArea);

        // Change the state to indicate that we are serializing the shard.
        state = SERIALIZING_SHARD;

        visitElement(element, false);

        // At this point we have finished the shard and so now me need to
        // serialize the rest of the document.
        state = SERIALIZING_ALL;

        currentArea = null;
        currentShard = null;
    }

    public void visitKeepTogether(DissectableElement element)
        throws DissectionException {

        KeepTogether keepTogether
            = (KeepTogether) document.getAnnotation(element);

        // Just treat this like a normal element.
        visitElement(element, false);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/1	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 24-Jun-03	521/2	geoff	VBM:2003061005 mimas version of original metis changes

 24-Jun-03	365/2	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 12-Jun-03	363/3	pduffin	VBM:2003060302 Added some comments to aid in implementation of string dissector

 10-Jun-03	363/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/

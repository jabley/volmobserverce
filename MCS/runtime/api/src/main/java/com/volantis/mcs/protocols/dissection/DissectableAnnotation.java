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
 * $Header: /src/voyager/com/volantis/mcs/protocols/dissection/DissectableAnnotation.java,v 1.8 2003/04/17 10:21:07 geoff Exp $
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
 *                              for maximum content size and reduced the size
 *                              of the navigations links.
 * 23-May-02    Paul            VBM:2002042202 - Used the new XMLOutputter
 *                              class to render the shard links.
 * 04-Dec-02    Phil W-S        VBM:2002111208 - Added handling of previous
 *                              and next shard link style classes, as fields
 *                              and as a value passed into getShardLink.
 * 14-Mar-03    Doug            VBM:2003030409 - Added the isNextLinkFirst 
 *                              member variable. Modified the initialise()
 *                              method to initialise the isNextLinkFirst 
 *                              member from the element attributes.
 *                              Modified the generateDissectedContents() method
 *                              to write out the next/previous shard links
 *                              in the correct order.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for  
 *                              ProtocolException where necessary.
 * 23-May-03    Allan           VBM:2003052207 - Create shard links as a menu 
 *                              where necessary. Too many changes to list. 
 * 27-May-03    Allan           VBM:2003052207 - Set requiresVerticalSeparator 
 *                              to false in createMenuAttributes(). 
 * 28-May-03    Allan           VBM:2003051904 - Replace use of the 
 *                              menuIsVertical() method with menuOrientation() 
 *                              in protocols. Stop setting 
 *                              requiresVerticalSeparator in 
 *                              createMenuAttributes(). 
 * 20-May-03	Mat             VBM:2003042907 - Changed to create its 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.dissection;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.output.CharacterEncoder;
import com.volantis.mcs.dom.output.DOMDocumentOutputter;
import com.volantis.mcs.dom.output.DocumentOutputter;
import com.volantis.mcs.dom.output.XMLDocumentWriter;
import com.volantis.mcs.integration.PageURLRewriter;
import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.integration.URLRewriter;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.MenuAttributes;
import com.volantis.mcs.protocols.MenuItem;
import com.volantis.mcs.protocols.MenuOrientation;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ShardLinkAttributes;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.runtime.FragmentationState;
import com.volantis.mcs.runtime.PageGenerationCache;
import com.volantis.mcs.runtime.PageURLDetailsFactory;
import com.volantis.mcs.runtime.URLConstants;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.utilities.ReusableStringBuffer;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Extra information to associate with a dissectable element.
 */
public class DissectableAnnotation
        extends ElementAnnotation
        implements DissectionConstants {

    /**
     * The log4j object to log to.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(DissectableAnnotation.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory
                    .createExceptionLocalizer(DissectableAnnotation.class);

    /**
     * The number of shards that have been identified..
     */
    private int shardCount;

    /**
     * The shard which has been requested.
     */
    private int requestedShard;

    /**
     * The path to the inclusion.
     */
    private String inclusionPath;

    /**
     * The name of the dissecting pane.
     */
    private String dissectingPaneName;

    /**
     * The short cut to use for the link to the next shard.
     */
    private String nextShardShortcut;

    /**
     * The text to use for the link to the next shard.
     */
    private String nextShardLinkText;

    /**
     * The short cut to use for the link to the previous shard.
     */
    private String previousShardShortcut;

    /**
     * The text to use for the link to the previous shard.
     */
    private String previousShardLinkText;

    /**
     * The index of the next change.
     */
    private int nextChangeIndex;

    /**
     * The index of the previous change.
     */
    private int previousChangeIndex;

    /**
     * The maximum size of the contents of this dissectable tree.
     */
    private int maxContentSize;


    /**
     * Variable that indicates the ordering of the next and previous shard links
     */
    private boolean isNextLinkFirst;

    public DissectableAnnotation() {
    }

    /**
     * Initialise method
     *
     * @throws ProtocolException if an error occurs
     */
    public void initialise()
            throws ProtocolException {

        dissectingPaneName =
                element.getAttributeValue(DISSECTING_PANE_NAME_ATTRIBUTE);
        inclusionPath = element.getAttributeValue(INCLUSION_PATH_ATTRIBUTE);
        nextShardShortcut =
                element.getAttributeValue(NEXT_SHARD_SHORTCUT_ATTRIBUTE);
        nextShardLinkText =
                element.getAttributeValue(NEXT_SHARD_LINK_TEXT_ATTRIBUTE);
        previousShardShortcut
                = element.getAttributeValue(PREVIOUS_SHARD_SHORTCUT_ATTRIBUTE);
        previousShardLinkText
                = element.getAttributeValue(PREVIOUS_SHARD_LINK_TEXT_ATTRIBUTE);

        // Initialise the maximum content size.
        String value =
                element.getAttributeValue(MAXIMUM_CONTENT_SIZE_ATTRIBUTE);
        maxContentSize = Integer.MAX_VALUE;
        if (value != null) {
            try {
                int i = Integer.parseInt(value);
                if (i < 0) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(
                                "Ignoring maximum content size as it is less than 0");
                    }
                } else {
                    maxContentSize = i;
                    if (logger.isDebugEnabled()) {
                        logger.debug(
                                "Maximum content size is " + maxContentSize);
                    }
                }
            }
            catch (NumberFormatException nfe) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Ignoring maximum content size", nfe);
                }
            }
        }

        String nextFirst =
                element.getAttributeValue(GENERATE_NEXT_LINK_FIRST_ATTRIBUTE);
        isNextLinkFirst = Boolean.valueOf(nextFirst).booleanValue();

        MarinerPageContext context = protocol.getMarinerPageContext();

        PageGenerationCache pageGenerationCache
                = context.getPageGenerationCache();

        FragmentationState.Change change;

        change = new FragmentationState.ShardChange(inclusionPath,
                dissectingPaneName,
                +1);
        nextChangeIndex
                = pageGenerationCache.getFragmentationStateChangeIndex(change);

        change = new FragmentationState.ShardChange(inclusionPath,
                dissectingPaneName,
                -1);
        previousChangeIndex
                = pageGenerationCache.getFragmentationStateChangeIndex(change);

        Document document = protocol.getDocument();
        DocumentAnnotation documentAnnotation
                = (DocumentAnnotation) document.getObject();
        documentAnnotation.addDissectableElement(element);
    }

    /**
     * Set the index of the shard which has been requested from the client.
     *
     * @param requestedShard The index of the requested shard.
     */
    public void setRequestedShard(int requestedShard) {
        this.requestedShard = requestedShard;
    }

    /**
     * Get the path to the inclusion.
     *
     * @return The path to the inclusion.
     */
    public String getInclusionPath() {
        return inclusionPath;
    }

    /**
     * Get the name of the dissecting pane.
     *
     * @return The name of the dissecting pane.
     */
    public String getDissectingPaneName() {
        return dissectingPaneName;
    }

    /**
     * Select the requested shard. Shards have to be generated in sequence so
     * this method repeatedly generates the next shard until the requested
     * shard has been done.
     *
     * @param space The space available for the shard.
     */
    public void selectRequestedShard(int space)
            throws ProtocolException {

        if (logger.isDebugEnabled()) {
            logger.debug("Space allocated is " + space);
        }

        // If the user has specified a limit on the content size and it is less
        // than the space we have been allocated then we must only use that amount,
        // otherwise we should use all the space that we have been allocated.
        if (maxContentSize != Integer.MAX_VALUE) {
            // We should not expect the user to be able to factor in the overhead
            // for the navigation links so add that to the maximum content
            // size.
            int overhead = getOverheadSize();
            if (logger.isDebugEnabled()) {
                logger.debug(
                        "Increasing maximum content size of " + maxContentSize
                                + " by navigation overhead of " + overhead);
            }
            maxContentSize += overhead;

            if (maxContentSize < space) {
                if (logger.isDebugEnabled()) {
                    logger.debug(
                            "Using user specified limit of " + maxContentSize);
                }

                space = maxContentSize;
            }
        }

        // If we have not already identified the requested shard then we need
        // to scan through the tree to find each shard in turn until we have
        // found the requested one.
        if (requestedShard >= shardCount) {

            shardLoop:
            for (; shardCount <= requestedShard; shardCount += 1) {
                int result = markShardNodes(shardCount, space);
                switch (result) {
                    case IGNORE_NODE:
                        // Break out of the loop, we are done.
                        break shardLoop;

                    case NODE_CANNOT_FIT:
                        // One of the nodes cannot be split.
                        throw new ProtocolException(
                                exceptionLocalizer.format(
                                        "content-cannot-be-split"));

                    case SHARD_COMPLETE:
                        // The shard has been completed, break out of the switch.
                        break;

                    default:
                        // There is still space left after adding all the children to the
                        // shard so we have finished processing all the shards. Before
                        // breaking out of the loop we need to count the shard which we
                        // have just created.

                        shardCount += 1;

                        break shardLoop;
                }
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Found " + shardCount
                    + " shards in total");
        }

        // Make sure that the request shard is available.
        if (requestedShard >= shardCount) {
            logger.warn("shard-missing", new Integer(requestedShard));
            // if the shard doesn't exist (because the content has changed) then
            // render the first shard
            requestedShard = 0;
            protocol.getMarinerPageContext().resetFragmentationIndex();
        }

        // Nothing left to do here.
    }

    /**
     * Override this method to prevent open and close tags from being
     * needlessly generated.
     */
    protected void generateTags() {
        generatedTags = true;
    }

    /**
     * Override this method as this node does not have tags so their size is
     * always 0.
     */
    protected int calculateTagsSize() {
        return 0;
    }

    /**
     * Override this method as neither this node or its children contribute
     * anything towards the fixed contents.
     */
    protected int calculateFixedContentsSize() {
        return 0;
    }

    /**
     * Override this method to calculate the maximum overhead of the
     * navigation links.
     *
     * @return The size taken by a previous and next link at the largest size.
     */
    protected int calculateOverheadSize() throws ProtocolException {
        return createShardLinkMenu(Integer.MAX_VALUE, Integer.MAX_VALUE)
                .length();
    }

    /**
     * Create the menu for the shard links, using the previously calculated
     * next and previous change indexes.
     *
     * @return A StringBuffer containing the markup that represents the shard
     *         links as a menu.
     */
    protected StringBuffer createShardLinkMenu()
            throws ProtocolException {
        final int nextIndex = nextChangeIndex;
        final int prevIndex = previousChangeIndex;

        return createShardLinkMenu(nextIndex, prevIndex);

    }

    /**
     * Create the menu for the shard links.
     *
     * @return A StringBuffer containing the markup that represents the shard
     *         links as a menu.
     */
    private StringBuffer createShardLinkMenu(
            final int nextIndex,
            final int prevIndex)
            throws ProtocolException {
        MenuItem nextLink = new MenuItem();
        nextLink.setHref(createShardLinkHRef(nextIndex));
        nextLink.setShortcut(new LiteralTextAssetReference(nextShardShortcut));
        // todo XDIME-CP style dissection correctly.
//        nextLink.setStyleClass(nextShardLinkClass);
        nextLink.setText(nextShardLinkText);

        MenuItem previousLink = new MenuItem();
        previousLink.setHref(createShardLinkHRef(prevIndex));
        previousLink.setShortcut(new LiteralTextAssetReference(
                previousShardShortcut));
        // todo XDIME-CP style dissection correctly.
//        previousLink.setStyleClass(previousShardLinkClass);
        previousLink.setText(previousShardLinkText);

        MenuAttributes menuAttributes = createMenuAttributes(
                nextLink, previousLink);

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        MenuOrientation orientation = protocol.menuOrientation(menuAttributes);

        protocol.doMenu(buffer, menuAttributes, orientation, false);

        Element menuElement = buffer.getRoot();

        return elementToStringBufferInParagraph(menuElement);
    }

    /**
     * Initialize the MenuAttributes associated with the shard link menu. In
     * other words add the menu items (shard links) that think we need and in
     * the right order and set the orientation of the menu.
     */
    private MenuAttributes createMenuAttributes(
            MenuItem nextLink,
            MenuItem previousLink) {
        MenuAttributes menuAttributes = new MenuAttributes();

        if (isNextLinkFirst) {
            menuAttributes.addItem(nextLink);
            menuAttributes.addItem(previousLink);
        } else {
            menuAttributes.addItem(previousLink);
            menuAttributes.addItem(nextLink);
        }

        // The menu style class is that of the dissecting pane that it should
        // go inside.
        // todo XDIME-CP style dissection correctly.
//        menuAttributes.setStyleClass(dissectingPaneClass);

        return menuAttributes;
    }

    /**
     * Create the href for a shard link.
     *
     * @param shardIndex The index of the shard to link to.
     * @return A String containing the href for the shard link.
     */
    protected String createShardLinkHRef(int shardIndex) {
        MarinerPageContext context = protocol.getMarinerPageContext();
        int currentIndex = context.getFragmentationIndex();
        MarinerURL rootPageURL = context.getRootPageURL(true);

        // Generate the value of the request parameter.
        String value
                = PageGenerationCache.makeShardChangeSpecifier(currentIndex,
                shardIndex);

        // Set the request parameter.
        rootPageURL.setParameterValue(URLConstants.FRAGMENTATION_PARAMETER,
                value);
        MarinerRequestContext requestContext = context.getRequestContext();
        URLRewriter sessionURLRewriter = context.getSessionURLRewriter();
        MarinerURL sessionURL
                = sessionURLRewriter.mapToExternalURL(requestContext,
                rootPageURL);
        // As the link we generate is always back to this page we can reduce the
        // overhead by removing the protocol, authority and all but the last part
        // of the path. We do this before URL rewriting as we have no idea what
        // the URL will look like afterwards.
        sessionURL.setProtocol(null);
        sessionURL.setAuthority(null);
        String path = sessionURL.getPath();
        int index = path.lastIndexOf('/');
        if (index != -1) {
            path = path.substring(index + 1);
            sessionURL.setPath(path);
        }

        PageURLRewriter urlRewriter = context.getVolantisBean().
                getLayoutURLRewriter();
        MarinerURL externalURL =
                urlRewriter.rewriteURL(context.getRequestContext(),
                        sessionURL,
                        PageURLDetailsFactory.
                                createPageURLDetails(PageURLType.SHARD));

        return externalURL.getExternalForm();
    }


    /**
     * Create the next link as a standalone link
     */
    protected StringBuffer createStandAloneNextLink()
            throws ProtocolException {
        return createStandAloneShardLink(nextChangeIndex,
                nextShardLinkText,
                nextShardShortcut);

    }

    /**
     * Create the next link as a standalone link
     */
    protected StringBuffer createStandAlonePreviousLink()
            throws ProtocolException {
        return createStandAloneShardLink(previousChangeIndex,
                previousShardLinkText,
                previousShardShortcut);

    }

    /**
     * Returns the markup for a shard link
     *
     * @param shardIndex the index of the shard that the link refers to
     * @param linkText   the text to use for this link
     * @param shortcut   the shortcut for this link
     * @return the link markup
     */
    private StringBuffer createStandAloneShardLink(
            int shardIndex,
            String linkText,
            String shortcut)
            throws ProtocolException {

        // Initialise the shardAttributes.
        ShardLinkAttributes shardAttributes = new ShardLinkAttributes();

        shardAttributes.setHref(createShardLinkHRef(shardIndex));
        shardAttributes.setLinkText(linkText);
        // todo XDIME-CP style dissection correctly.
        shardAttributes.setShortcut(shortcut);

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        // return the link text.
        protocol.writeShardLink(buffer, shardAttributes);

        Element element = (Element) buffer.getRoot().getHead();

        return elementToStringBufferInParagraph(element);
    }

    /**
     * Given an Element convert this to a StringBuffer representation
     * inside a paragraph i.e. inside a string representaion of paragraph.
     * Note that paragraph creation is not protocol independent (and never
     * has been in shard link creation). The main reason for keeping it like
     * this is that openParagraph() and closeParagraph() in DOMProtocol are
     * protected.
     *
     * @param element The element to convert.
     * @return A StringBuffer version of the element
     */
    private StringBuffer elementToStringBufferInParagraph(Element element) {
        CharacterEncoder encoder = protocol.getCharacterEncoder();
        StringWriter writer = new StringWriter(80);
        DocumentOutputter outputter = new DOMDocumentOutputter(
                new XMLDocumentWriter(writer), encoder);
        try {
            writer.write("<p>");
            outputter.output(element);
            writer.write("</p>");
        } catch (IOException e) {
            logger.error("unexpected-ioexception", e);
        }
        return writer.getBuffer();
    }

    /**
     * Override this method as neither this node or its children contribute
     * anything towards the fixed contents.
     */
    public void generateFixedContents(ReusableStringBuffer buffer) {
        return;
    }

    /**
     * Generates the output for the requested shard for this pane and adds
     * shard navigation as needed.
     *
     * @param buffer The buffer into which the output should be written.
     */
    public void generateDissectedContents(ReusableStringBuffer buffer)
            throws ProtocolException {

        if (logger.isDebugEnabled()) {
            logger.debug("Generating contents of shard " + requestedShard);
        }

        generateShardContents(buffer, requestedShard, false);

        // If the whole contents were added to the same shard then we don't need
        // to generate any links so return immediately.
        if (completed && firstShard == lastShard) {
            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Requested shard is " + requestedShard
                    + " total shards found so far " + shardCount);
        }

        // NOTE: Any extra characters which are added around the URL must be
        // accounted for in the calculateOverheadSize method, otherwise some
        // pages will break.

        // the index of the last shard
        int lastShardIndex = shardCount - 1;

        // if we are writing out the last shard then there is no next link
        boolean needNextLink = (requestedShard < lastShardIndex ||
                (requestedShard == lastShardIndex && !completed));

        StringBuffer shardLinkMarkup;

        if (requestedShard == 0) {
            // We need to create a stand-alone next link
            shardLinkMarkup = createStandAloneNextLink();
        } else if (!needNextLink) {
            // We need to create a stand-alone previous link
            shardLinkMarkup = createStandAlonePreviousLink();
        } else {
            shardLinkMarkup = createShardLinkMenu();
        }


        if (logger.isDebugEnabled()) {
            logger.debug("Shard count " + shardCount
                    + " requestedShard " + requestedShard
                    + " completed " + completed);
            logger.debug("The output shardLinkMarkup is: " +
                    shardLinkMarkup.toString());
        }
        buffer.append(shardLinkMarkup);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/2	pduffin	VBM:2005111405 Massive changes for performance

 25-Nov-05	10453/1	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 25-Nov-05	9708/4	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 08-Aug-05	9205/1	rgreenall	VBM:2005062107 Forward port of VBM:2005062107

 27-Oct-05	10011/1	emma	VBM:2005031701 Forward port: Fix dissecting pane problems with small max page size

 27-Oct-05	7445/2	emma	VBM:2005031701 Forward port: Fix dissecting pane problems with small max page size

 25-Nov-05	9708/4	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 08-Aug-05	9205/1	rgreenall	VBM:2005062107 Forward port of VBM:2005062107

 27-Oct-05	7445/2	emma	VBM:2005031701 Forward port: Fix dissecting pane problems with small max page size

 30-Aug-05	9353/2	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 22-Mar-05	7441/1	emma	VBM:2005031701 Candidate fix for dissecting pane problems with small max page size

 15-Mar-05	7414/1	emma	VBM:2005031108 Merged from MCS 3.3.0 - fix for problem with dissecting panes with small max size

 15-Mar-05	7396/1	emma	VBM:2005031108 Fixing problem with dissecting panes with small max size

 03-Mar-05	7277/1	philws	VBM:2005011906 Port pane styling fix from MCS 3.3

 03-Mar-05	7273/1	philws	VBM:2005011906 Ensure panes are thematically styled as per the requesting XDIME style class specifications

 24-Feb-05	7129/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 24-Feb-05	7099/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 28-Jun-04	4733/1	allan	VBM:2004062105 Convert Volantis to use the new PageURLRewriter

 25-Feb-04	2974/4	steve	VBM:2004020608 supermerged

 17-Feb-04	2974/1	steve	VBM:2004020608 SGML Quote handling
 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)
 05-Feb-04	2794/1	steve	VBM:2004012613 HTML Quote handling

 ===========================================================================
*/

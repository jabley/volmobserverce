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
 * $Header: /src/voyager/com/volantis/mcs/protocols/mms/MMS_SMIL_2_0.java,v 1.11 2003/04/17 10:21:07 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 23-Oct-02    Sumit           VBM:2002102403 - Started work.
 * 05-Nov-02    Geoff           VBM:2002102403 - Taken over from Sumit,
 *                              did cleanup, testing....
 * 11-Nov-02    Chris W         VBM:2002102403 - Finished off first cut.
 * 14-Nov-02    Chris W         VBM:2002111402 - moved code that replaced
 *                              inline text with a <text> tag from closeParagraph
 *                              to writePaneContents.
 * 15-Nov-02    Sumit           VBM:2002102403 - Changed storePaneSrcContent(..)
 *                              to return a pane reference for use in
 *                              removeInlineText(..)
 * 09-Dec-02    Allan           VBM:2002120615 - Replaced String version of
 *                              Format type with FormatType where possible.
 * 29-Jan-03    Adrian          VBM:2003012104 - Handle plain text processing
 *                              in a similar way to SMS protocol. Added all
 *                              open/close..() methods for usual markup
 *                              elements. Added OutputBufferFactory so that
 *                              this protocol now uses MMS_DOMOutputBuffer
 *                              which handles plain text processing.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 28-Mar-03    Geoff           VBM:2003032803 - Forward ported 2003013010 -
 *                              (Added check to doImage to check that src was
 *                              not null before rendering the image tags),
 *                              (Skipped alttext processing as this breaks
 *                              smil validity), (Commented out renderAltText
 *                              as well).
 * 09-Apr-03    Phil W-S        VBM:2002111502 - Override
 *                              addPhoneNumberContents.
 * 14-Apr-03    Phil W-S        VBM:2002111502 - Minor update to remove space
 *                              prefix before phone number content in
 *                              addPhoneNumberContents.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for  
 *                              ProtocolException and FormatVisitorException 
 *                              where necessary, catch FormatVisitorException 
 *                              and rethrow ProtocolException in doHead().
 * 23-May-03    Mat             VBM:2003042907 - Changed to use new implementation
 *                              of XMLOutputter
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.mms;


import com.volantis.mcs.context.ApplicationContext;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.dtd.DTD;
import com.volantis.mcs.dom.output.DOMDocumentOutputter;
import com.volantis.mcs.dom.output.DocumentOutputter;
import com.volantis.mcs.dom.xml.XMLDTDBuilder;
import com.volantis.mcs.layouts.ColumnIteratorPane;
import com.volantis.mcs.layouts.DissectingPane;
import com.volantis.mcs.layouts.Form;
import com.volantis.mcs.layouts.FormFragment;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.layouts.FormatVisitor;
import com.volantis.mcs.layouts.FormatVisitorException;
import com.volantis.mcs.layouts.Fragment;
import com.volantis.mcs.layouts.Grid;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.layouts.Region;
import com.volantis.mcs.layouts.Replica;
import com.volantis.mcs.layouts.RowIteratorPane;
import com.volantis.mcs.layouts.Segment;
import com.volantis.mcs.layouts.SegmentGrid;
import com.volantis.mcs.layouts.SpatialFormatIterator;
import com.volantis.mcs.layouts.TemporalFormatIterator;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.AddressAttributes;
import com.volantis.mcs.protocols.AnchorAttributes;
import com.volantis.mcs.protocols.AudioAttributes;
import com.volantis.mcs.protocols.BlockQuoteAttributes;
import com.volantis.mcs.protocols.BodyAttributes;
import com.volantis.mcs.protocols.CanvasAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DefinitionDataAttributes;
import com.volantis.mcs.protocols.DefinitionListAttributes;
import com.volantis.mcs.protocols.DefinitionTermAttributes;
import com.volantis.mcs.protocols.DivAttributes;
import com.volantis.mcs.protocols.HeadingAttributes;
import com.volantis.mcs.protocols.HorizontalRuleAttributes;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.LineBreakAttributes;
import com.volantis.mcs.protocols.ListItemAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.NoScriptAttributes;
import com.volantis.mcs.protocols.OrderedListAttributes;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.OutputBufferFactory;
import com.volantis.mcs.protocols.ParagraphAttributes;
import com.volantis.mcs.protocols.PhoneNumberAttributes;
import com.volantis.mcs.protocols.PreAttributes;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.ScriptAttributes;
import com.volantis.mcs.protocols.SlideAttributes;
import com.volantis.mcs.protocols.StyleAttributes;
import com.volantis.mcs.protocols.TableAttributes;
import com.volantis.mcs.protocols.TableCellAttributes;
import com.volantis.mcs.protocols.TableRowAttributes;
import com.volantis.mcs.protocols.UnorderedListAttributes;
import com.volantis.mcs.protocols.text.ListItemIdGeneratorOrdered;
import com.volantis.mcs.protocols.text.ListItemIdGeneratorStack;
import com.volantis.mcs.protocols.text.ListItemIdGeneratorUnordered;
import com.volantis.mcs.protocols.text.QuietLogicalWhitespaceWriter;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * A restricted implementation of SMIL specifically for MMS. This is described
 * in the MMS Conformance Document 2.0 "agreed" between the phone vendors.
 * <p>
 * We've also used the documentation in Requirement 447 and MPS Architecture
 * Note AN036 as a guide for how to implement this.
 */
public class MMS_SMIL_2_0 extends DOMProtocol {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(MMS_SMIL_2_0.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(MMS_SMIL_2_0.class);

    // Format Visitor we use to generate the layout information in the head
    // of the SMIL format which mirrors the actual content in the body.
    private HeadFormatVisitor visitor;

    /**
     * Flag to indicate if we are in a temporal format iterator
     */
    private boolean inTemporal;

    /**
     * The object we use to simulate list item id generation, stored in a
     * stack so we can handle nested lists without barfing.
     */
    private ListItemIdGeneratorStack listItemIdGeneratorStack;


    /** Creates a new instance of MMS_SMIL_2_0 */
    public MMS_SMIL_2_0(ProtocolSupportFactory protocolSupportFactory,
            ProtocolConfiguration protocolConfiguration) {
        
        super(protocolSupportFactory, protocolConfiguration);
    }

    // Inherit javadoc.
    public void initialise() {
        super.initialise();
        if(logger.isDebugEnabled()){
            logger.debug("Initialising (MMS_SMIL_2_0)" + this);
        }
        inTemporal = false;
        listItemIdGeneratorStack = new ListItemIdGeneratorStack();
    }


    // Javadoc inherited.
    public OutputBufferFactory getOutputBufferFactory() {
        // Lazily initialise the factory to allow the pool time to be
        // constructed (or not) depending on configuration.
        if (outputBufferFactory == null) {
            outputBufferFactory = new MMS_DOMOutputBufferFactory(
                    getDOMFactory());
        }
        return outputBufferFactory;
    }

    private MMS_DOMOutputBuffer getMMSBuffer() {
        return (MMS_DOMOutputBuffer)context.getCurrentOutputBuffer();
    }

    // Inherit javadoc.
    public void writeInitialHeader() {
        // hide any boilerplate normally sent to HTML like devices
    }

    // Inherit javadoc.
    protected void doProtocolString(Document document) {
        // The MMS variant of SMIL does not specify a protocol string.
    }

    // Javadoc inherited from super class.
    public String defaultMimeType() {
        return "application/smil";
    }


    // Inherit Javadoc.
    protected void openCanvas(DOMOutputBuffer dom,
            CanvasAttributes attributes) throws ProtocolException {
        // An MMS SMIL document starts with <smil>
        Element smil = dom.openStyledElement("smil", attributes);

        // Normally openHead and closeHead are called by writePageHead()
        // However this is too late for us as we need to write the page's
        // title into a meta tag rather than into the smil element, and as
        // such we need the attributes.
        doHead(dom, false, attributes);
    }

    // Inherit Javadoc.
    protected void closeCanvas(DOMOutputBuffer dom,
            CanvasAttributes attributes) {
        // An MMS SMIL document ends with </smil>
        dom.closeElement("smil");
    }

    /**
     * Generate the <head> element for smil; this entails figuring out
     * what <region> tags we need from the layout.
     * @param dom
     * @param empty
     */
    private void doHead(DOMOutputBuffer dom, boolean empty,
                            CanvasAttributes attributes) throws ProtocolException {
        // @todo 2005060816 annotate child with style information if it's not inherited from the parent
        dom.openStyledElement("head", attributes);

        if (attributes.getPageTitle() != null || attributes.getTitle() != null)
        {
            // Add meta tag containing title.
            Element meta = dom.addElement("meta");
            meta.setAttribute("name", "title");

            if (attributes.getPageTitle() != null)
            {
                meta.setAttribute("content", attributes.getPageTitle());
            }
            else
            {
                meta.setAttribute("content", attributes.getTitle());
            }
        }

        dom.openElement("layout");

        // Root layout width and height default to the entire screen,
        // which is good enough for us, so no need to add width and height
        // attributes.
        dom.addElement("root-layout");

        // Search down the layout heirarchy, looking for Panes, and
        // generate a <region> tag for each one.
        visitor = new HeadFormatVisitor();
        Format rootFormat = getMarinerPageContext().getDeviceLayout().
                getRootFormat();
        try {
            rootFormat.visit(visitor, dom);
        } catch (FormatVisitorException e) {            
            throw new ProtocolException(
                        exceptionLocalizer.format("error-rendering-regions"),
                        e);
        }
        dom.closeElement("layout");
        dom.closeElement("head");
    }

    // Javadoc inherited from super class.
    protected void openBody (DOMOutputBuffer dom,
                             BodyAttributes attributes) {

        dom.openStyledElement ("body", attributes);

        // If we are not in a temporal format iterator then we have to manually
        // call openSlide. openSlide does not need to be called for each
        // paragraph. It only needs to be called once for the cell.
        if (!inTemporal)
        {
            openSlide(dom, null);
        }
    }

    // Javadoc inherited from super class.
    protected void closeBody (DOMOutputBuffer dom,
                              BodyAttributes attributes) {

        // If we are not in a temporal format iterator then we have to manually
        // call closeSlide
        if (!inTemporal)
        {
            closeSlide(dom, null);
        }

        dom.closeElement ("body");
    }


    // Javadoc inherited from super class.
    protected void openSlide(DOMOutputBuffer dom, SlideAttributes attributes) {
        Element element = dom.openStyledElement("par", attributes);
        if (attributes != null) {
            element.setAttribute("dur", attributes.getDuration());
        }
    }

    // Javadoc inherited from super class.
    protected void closeSlide(DOMOutputBuffer dom, SlideAttributes attributes) {
        dom.closeElement("par");
    }

    /**
     * Adds the image markup to the specified DOMOutputBuffer
     * @param dom The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void doImage(DOMOutputBuffer dom,
            ImageAttributes attributes) {

        if(logger.isDebugEnabled()){
            logger.debug("into doImage");
        }

        String value = attributes.getSrc();

        if (value != null) {
            Element element = dom.addStyledElement ("img", attributes);

            if (attributes.getAssetURLSuffix() != null) {
                value = value + attributes.getAssetURLSuffix();
            }            
            element.setAttribute ("src", value);
            // MMS Conformance doc specifies that the message must always contain
            // at most two regions one labelled as "Text", the other as "Image"
            element.setAttribute("region","Image");
        }
    }

    /**
     * Write the pane contents buffer directly to the page.
     * @param buffer The buffer to write.
     */
    public void writePaneContents (OutputBuffer buffer) throws IOException {
        DOMOutputBuffer domBuffer = (DOMOutputBuffer)buffer;
        removeInlineText(domBuffer);

        DOMOutputBuffer dom = getCurrentBuffer ();
        dom.addOutputBuffer ((DOMOutputBuffer) buffer/* trim */);
    }

    /**
     * Removes any text that has been in lined in
     * @param dom The DOMOutputBuffer.
     */
    private void removeInlineText(DOMOutputBuffer dom) {
        Element element = dom.getCurrentElement();
        Node child = element.getHead();

        StringBuffer content = new StringBuffer();
        for (child = element.getHead(); child!=null; child = child.getNext()) {
            if (child instanceof Text) {
                Text text = (Text)child;
                content.append(text.getContents(), 0, text.getLength());
            }
        }

        // MMS_SMIL layouts are so simple that the head and tail should point
        // to the same element.
        // If the head is a text node we throw away the actual text and
        // only add the attributes so that we get
        // <text src="myText.txt" region="Text"/>
        // instead of
        // Some text.<text src="myText.txt" region="Text"/>
        //if (head instanceof Text)
        if (content.length() != 0) {
            // Store the content of the text node. Get the pane name form
            // the app context
            //String paneName = storePaneSrcContent(new String(((Text) head).getContents()));
            String paneName = storePaneSrcContent(content.toString());

            if (logger.isDebugEnabled())
            {
                logger.debug("storing: paneName="+paneName);
            }

            element.clearChildren();

            Element e = dom.allocateElement ("text");
            e.setAttribute("src", paneName);

            // MMS Conformance doc specifies that the message must always contain
            // at most two regions one labelled as "Text", the other as "Image"
            e.setAttribute("region", "Text");
            element.addHead(e);
        }
    }

    /**
     * Stores the content of text panes in the ApplicationContext so that MPS
     * can write them out of multi-part MIME attachments.
     * @param content The text to go in the pane
     * @return the paneName that should be used in the src attribute
     */
    // TODO: Removed the commented code and the bogus return statement once
    // Application context is checked in
    private String  storePaneSrcContent(String content) {
        MarinerRequestContext requestContext = getMarinerPageContext().getRequestContext();
        ApplicationContext appContext = ContextInternals.getApplicationContext (requestContext);

        return appContext.mapSMILAsset(content.trim());
    }

    /**
     * Adds the audio markup to the specified DOMOutputBuffer
     * @param dom The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void doAudio(DOMOutputBuffer dom, AudioAttributes attributes) {
        Element e = dom.addStyledElement("audio", attributes);
        String value = attributes.getSrc();
        if (value != null) {
            if (attributes.getAssetURLSuffix() != null) {
                value = value + attributes.getAssetURLSuffix();
            }
        }        
        e.setAttribute("src", attributes.getSrc());
    }

    /**
     * A format visitor which creates the elements in the SMIL head
     * (i.e. the <code>root-layout</code> and the <code>region</code>s),
     * and which collects the sizing information which we then ignore
     * why?
     */
    private class HeadFormatVisitor implements FormatVisitor {

        /** Integer to keep track of the current left */
        int left = 0;

        /** Integer to keep track of the current top */
        int top = 0;

        // Inherit javadoc.
        public boolean visit(Pane pane, Object object) {

            DOMOutputBuffer dom = (DOMOutputBuffer) object;
            Element element = dom.addElement("region");

            // id must be Text or Image depending on what is stored in the pane
            // We assume that the user fills in the destination area property
            // using the GUI to save us from having to work this out.
            String destinationArea = pane.getDestinationArea();
            if(logger.isDebugEnabled()){
                logger.debug("destinationArea="+destinationArea);
            }
            if ( !("Text".equals(destinationArea)  || "Image".equals(destinationArea)) )
            {
                throw new MMS_SMIL_2_0_LayoutException("You must set the destination "
                     + "area to be either Text or Image");
            }
            element.setAttribute("id", destinationArea);

            // As device doesn't support percentages we shrink sizes of panes
            // to fit into device, maintaining relative sizes
            int paneWidth = calculatePaneWidth(pane);
            if(logger.isDebugEnabled()){
                logger.debug("paneWidth="+paneWidth);
            }
            int paneHeight = calculatePaneHeight(pane);
            if(logger.isDebugEnabled()){
                logger.debug("paneHeight="+paneHeight);
            }
            element.setAttribute("width", Integer.toString(paneWidth));
            element.setAttribute("height", Integer.toString(paneHeight));
            element.setAttribute("left", Integer.toString(left));
            element.setAttribute("top", Integer.toString(top));

            validateLayout(pane, paneWidth, paneHeight);
            return false;
        }

        /**
         * Checks that an MMS/SMIL layout is valid.
         * @param pane
         * @param paneWidth
         * @param paneHeight
         */
        private void validateLayout(Pane pane, int paneWidth, int paneHeight)
        {
            // MMS/SMIL layouts must consist of either two rows and one column,
            // or one row and two columns, or a single pane.
            Format parent = pane.getParent();

            if (parent != null)
            {
                FormatType formatType = parent.getFormatType();
                if (FormatType.GRID.equals(formatType))
                {
                    Grid grid = (Grid)parent;
                    int rows = grid.getRows();
                    int columns = grid.getColumns();

                    if (rows == 1 && columns == 2)
                    {
                        left += paneWidth;
                    }
                    else if (rows == 2 && columns == 1)
                    {
                        top += paneHeight;
                    }
                    else
                    {
                       throw new MMS_SMIL_2_0_LayoutException("Layouts rendered by "
                         + "MMS/SMIL must consist of either two rows and one column, "
                         + "one row and two columns, or just a single pane.");
                    }
                }
                else if ( !("TemporalFormatIterator".equals(formatType)
                       || formatType == null) )
                {
                    throw new MMS_SMIL_2_0_LayoutException("Layouts rendered by "
                        + "MMS/SMIL must consist of either two rows and one column, "
                        + "one row and two columns, or just a single pane.");
                }
            }
        }




        // Inherit javadoc.
        public boolean visit(FormFragment fragment, Object object) {
            return false;
        }

        // Inherit javadoc.
        public boolean visit(Replica replica, Object object) {
            return false;
        }

        // Inherit javadoc.
        public boolean visit(Fragment fragment, Object object) {
            return false;
        }

        // Inherit javadoc.
        public boolean visit(RowIteratorPane pane, Object object) {
            return false;
        }

        // Inherit javadoc.
        public boolean visit(Region region, Object object) {
            return false;
        }

        // Inherit javadoc.
        public boolean visit(SegmentGrid grid, Object object) {
            return false;
        }

        // Inherit javadoc.
        public boolean visit(ColumnIteratorPane pane, Object object) {
            return false;
        }

        // Inherit javadoc.
        public boolean visit(Segment segment, Object object) {
            return false;
        }

        // Inherit javadoc.
        public boolean visit(DissectingPane pane, Object object) {
            return false;
        }

        // Inherit javadoc.
        public boolean visit(Grid grid, Object object) 
                throws FormatVisitorException {
            return grid.visitChildren(this, object);
        }

        // Inherit javadoc.
        public boolean visit(Form form, Object object) {
            return false;
        }

        // Inherit javadoc.
        public boolean visit(SpatialFormatIterator spatial, Object object)
                throws FormatVisitorException {
            return spatial.visitChildren(this, object);
        }

        // Inherit javadoc.
        public boolean visit(TemporalFormatIterator temporal, Object object)
                throws FormatVisitorException {
            inTemporal = true;
            return temporal.visitChildren(this, object);
        }
    }

    // ========================================================================
    // MARKUP METHODS
    // ========================================================================

    // Inherit Javadoc.
    public void openAddress(DOMOutputBuffer dom, AddressAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void closeAddress(DOMOutputBuffer dom, AddressAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void closeAnchor(DOMOutputBuffer dom, AnchorAttributes attributes) {
        // If we have fallback text for the link
        String fallbackText = getTextFallbackFromLink(attributes.getHref());
        if (fallbackText != null) {
            // Write it out, inside brackets and with normalised space, after
            // the link body.
            // Note: the Spec doesn't specifically describe what to do in this
            // case, we could alternatively do nothing, or write the link
            // itself here.
            getMMSBuffer().getLogicalWriter().writeSpace();
            Writer normaliseWriter = getMMSBuffer().getWriter();
            try {
                normaliseWriter.write("(");
                normaliseWriter.write(fallbackText);
                normaliseWriter.write(")");
            } catch (IOException e) {
                logger.error("unexpected-ioexception", e);
                // and continue!
            }
        }
    }

    /**
     * Both the content and the full number are output in format similar to
     * that used in the SMS protocol (i.e. "<content> (<full number>)").
     */
    protected void addPhoneNumberContents(DOMOutputBuffer dom,
                                          PhoneNumberAttributes attributes) {
        Object contents = attributes.getContent();

        // MMS specifically uses a specialist DOMOutputBuffer
        if (contents instanceof MMS_DOMOutputBuffer) {
            MMS_DOMOutputBuffer contentBuffer = (MMS_DOMOutputBuffer)contents;

            if (!contentBuffer.isEmpty() && !contentBuffer.isWhitespace()) {
                dom.addOutputBuffer(contentBuffer);
            }

        } else if (contents instanceof DOMOutputBuffer) {
            throw new IllegalArgumentException(
                "MMS must be used with MMS_DOMOutputBuffers " +
                "(attributes.getContent() returned a DOMOutputBuffer)");
        } else if (contents != null) {
            dom.appendEncoded(contents.toString());
        }

        String defaultContents = attributes.getDefaultContents();

        if (defaultContents != null) {
            dom.appendEncoded(" (");

            dom.appendEncoded(defaultContents);

            dom.appendEncoded(")");
        }

        attributes.setContent(null);
    }

    // Inherit Javadoc.
    public void openBlockQuote(DOMOutputBuffer dom, BlockQuoteAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void closeBlockQuote(DOMOutputBuffer dom, BlockQuoteAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void openDefinitionList(DOMOutputBuffer dom, DefinitionListAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void closeDefinitionList(DOMOutputBuffer dom, DefinitionListAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void openDefinitionTerm(DOMOutputBuffer dom, DefinitionTermAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void closeDefinitionTerm(DOMOutputBuffer dom, DefinitionTermAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void openDefinitionData(DOMOutputBuffer dom, DefinitionDataAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
        // DD is normally a bit indented, but I can't be bothered to implement
        // this at the moment - you'd need a stack...
    }

    // Inherit Javadoc.
    public void closeDefinitionData(DOMOutputBuffer dom, DefinitionDataAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void openDiv(DOMOutputBuffer dom, DivAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void closeDiv(DOMOutputBuffer dom, DivAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void openHeading1(DOMOutputBuffer dom, HeadingAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void closeHeading1(DOMOutputBuffer dom, HeadingAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void openHeading2(DOMOutputBuffer dom, HeadingAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void closeHeading2(DOMOutputBuffer dom, HeadingAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void openHeading3(DOMOutputBuffer dom, HeadingAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void closeHeading3(DOMOutputBuffer dom, HeadingAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void openHeading4(DOMOutputBuffer dom, HeadingAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void closeHeading4(DOMOutputBuffer dom, HeadingAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void openHeading5(DOMOutputBuffer dom, HeadingAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void closeHeading5(DOMOutputBuffer dom, HeadingAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void openHeading6(DOMOutputBuffer dom, HeadingAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void closeHeading6(DOMOutputBuffer dom, HeadingAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void openListItem(DOMOutputBuffer dom, ListItemAttributes attributes) {
        // spec requires we emulate * or number
        QuietLogicalWhitespaceWriter logicalWriter =
                getMMSBuffer().getLogicalWriter();
        listItemIdGeneratorStack.peek().addNextListItemId(logicalWriter);
        logicalWriter.writeSpace();
    }

    // Inherit Javadoc.
    public void closeListItem(DOMOutputBuffer dom, ListItemAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void openNoScript(DOMOutputBuffer dom, NoScriptAttributes attributes) {
        ignoreStart();
    }

    // Inherit Javadoc.
    public void closeNoScript(DOMOutputBuffer dom, NoScriptAttributes attributes) {
        ignoreEnd();
    }

    // Inherit Javadoc.
    public void openOrderedList(DOMOutputBuffer dom, OrderedListAttributes attributes) {
        // set up emulation for numbers
        listItemIdGeneratorStack.push(new ListItemIdGeneratorOrdered());
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void closeOrderedList(DOMOutputBuffer dom, OrderedListAttributes attributes) {
        // clean up number emulation
        listItemIdGeneratorStack.pop();
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void openUnorderedList(DOMOutputBuffer dom, UnorderedListAttributes attributes) {
        // set up emulation for *
        listItemIdGeneratorStack.push(
                new ListItemIdGeneratorUnordered());
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void closeUnorderedList(DOMOutputBuffer dom, UnorderedListAttributes attributes) {
        // clean up emulation for *
        listItemIdGeneratorStack.pop();
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void openParagraph(DOMOutputBuffer dom, ParagraphAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void closeParagraph(DOMOutputBuffer dom, ParagraphAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void openPre(DOMOutputBuffer dom, PreAttributes attributes) {
        // NOTE: I chose to use a new buffer rather than switch modes
        // of the existing buffer inline since it was easier to impl.

        // Create a new output buffer
        MMS_DOMOutputBuffer outputBuffer = (MMS_DOMOutputBuffer)
                getOutputBufferFactory().createOutputBuffer();
        // Tell the buffer not to clean up subsequent PRE content whitespace.
        outputBuffer.setPreformatted(true);
        // And stick it on the stack to capture output from now on.
        context.pushOutputBuffer(outputBuffer);
    }

    // Inherit Javadoc.
    public void closePre(DOMOutputBuffer dom, PreAttributes attributes) {
        // Pop the PRE's OutputBuffer off the stack.
        MMS_DOMOutputBuffer outputBuffer =
                (MMS_DOMOutputBuffer)getCurrentBuffer();
        context.popOutputBuffer(outputBuffer);

        MMS_DOMOutputBuffer currentBuffer =
                (MMS_DOMOutputBuffer) getCurrentBuffer();

        String preContent = null;
        try {
            StringWriter writer = new StringWriter();

            // we need to get the maximum line length in chars so we can give
            // it to the writer
            int maximumLineLength = getMaximumLineLength();

            XMLDTDBuilder builder = new XMLDTDBuilder();
            builder.setMaximumLineLength(maximumLineLength);

            DTD dtd = builder.buildDTD();

            DocumentOutputter outputter = new DOMDocumentOutputter(
                dtd.createDocumentWriter(writer), getCharacterEncoder());

            outputter.output(outputBuffer.getRoot());

            preContent = writer.toString();
            boolean requiresSub = false;
            int startIndex = 0;
            int endIndex = preContent.length();
            if (preContent.startsWith("\n")) {
                startIndex = 1;
                requiresSub = true;
            }
            if (preContent.endsWith("\n")) {
                endIndex = endIndex - 1;
                requiresSub = true;
            }

            if (requiresSub) {
                preContent = preContent.substring(startIndex, endIndex);
            }

        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Could not add content of pre tag to output" +
                        " because of an unexpected Exception: ", e);
            }
        }

        if (preContent != null) {
            currentBuffer.getLogicalWriter().writeLine();
            currentBuffer.getLogicalWriter().write(preContent);
            currentBuffer.getLogicalWriter().writeLine();
        }
    }

    // Inherit Javadoc.
    public void openScript(DOMOutputBuffer dom, ScriptAttributes attributes) {
        ignoreStart();
    }

    // Inherit Javadoc.
    public void closeScript(DOMOutputBuffer dom, ScriptAttributes attributes) {
        ignoreEnd();
    }

    // Inherit Javadoc.
    public void openStyle(DOMOutputBuffer dom,
                          StyleAttributes attributes) {
        // SMS ignores styles
    }

    // Inherit Javadoc.
    public void closeStyle(DOMOutputBuffer dom,
                           StyleAttributes attributes) {
        // SMS ignores styles
    }

    // Inherit Javadoc.
    public void openTable(DOMOutputBuffer dom, TableAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void closeTable(DOMOutputBuffer dom, TableAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void openTableDataCell(DOMOutputBuffer dom, TableCellAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeSpace();
    }

    // Inherit Javadoc.
    public void closeTableDataCell(DOMOutputBuffer dom, TableCellAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeSpace();
    }

    // Inherit Javadoc.
    public void openTableHeaderCell(DOMOutputBuffer dom, TableCellAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeSpace();
    }

    // Inherit Javadoc.
    public void closeTableHeaderCell(DOMOutputBuffer dom, TableCellAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeSpace();
    }

    // Inherit Javadoc.
    public void openTableRow(DOMOutputBuffer dom, TableRowAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void closeTableRow(DOMOutputBuffer dom, TableRowAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // Inherit Javadoc.
    public void doHorizontalRule(DOMOutputBuffer dom,
                                 HorizontalRuleAttributes attributes) {
        // emulate a horizontal line
        QuietLogicalWhitespaceWriter logicalWriter =
                getMMSBuffer().getLogicalWriter();
        logicalWriter.write("---");
        logicalWriter.writeLine();
    }

    // Inherit Javadoc.
    protected void doLineBreak(DOMOutputBuffer dom,
                               LineBreakAttributes attributes) {
        getMMSBuffer().getLogicalWriter().writeLine();
    }

    // NOTE: SMS device should never not let any images, dynvis or charts get
    // rendered through this protocol, so all attempts to do so should generate
    // fallbacks instead of calling these methods. We throw exception if it
    // ever happens to ensure this is the case :-).

    // Inherit Javadoc.
    protected void renderAltText(String altText,
                                 MCSAttributes attributes) {

        // This breaks in so many ways speak to me (Ian) before implementing
        //try {
            // write out just the text, normalised
            //getMMSBuffer().getWriter().write(altText);

        logger.info("skipped-text-fallback", new Object[]{altText});
        //} catch (IOException e) {
         //
        //logger.error("ioexception", e);
            // and continue!
        //}
    }

    //
    // Helper methods for html tag stuff
    //

    /**
     * Start ignoring body content.
     */
    private void ignoreStart() {
        context.pushOutputBuffer(
                getOutputBufferFactory().createOutputBuffer());
    }

    /**
     * Finishing ignoring body content.
     */
    private void ignoreEnd() {
        MMS_DOMOutputBuffer currentBuffer = getMMSBuffer();
        context.popOutputBuffer(currentBuffer);
    }

    // ========================================================================
    // END OF MARKUP METHODS
    // ========================================================================


   /**
    * The FormatVisitor mechanism does not allow the methods to throw any
    * Exceptions. This subclass of RuntimeException is thrown when the MMS/SMIL
    * protocol is asked to render a layout that is too complicated.
    */
  protected class MMS_SMIL_2_0_LayoutException
    extends RuntimeException {

    /**
     * Create a new <code>MMS_SMIL_2_0_LayoutException</code>
     * @param msg The message explaining what's gone wrong.
     */
    public MMS_SMIL_2_0_LayoutException (String msg) {
      super(msg);
    }

  }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Nov-05	9708/2	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 09-Jun-05	8665/4	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 09-Jun-05	8665/2	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 24-Feb-05	7129/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 24-Feb-05	7099/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/6	doug	VBM:2004111702 Refactored Logging framework

 26-Nov-04	6298/2	geoff	VBM:2004112405 MCS NullPointerException in wml code path

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 08-Jun-04	4661/1	steve	VBM:2004060309 enable asset URL suffix attribute

 08-Jun-04	4643/1	steve	VBM:2004060309 enable asset URL suffix attribute

 05-Mar-04	3339/1	geoff	VBM:2003052104 Invalid usage of DOMOutputBuffer.appendLiteral()

 05-Mar-04	3337/1	geoff	VBM:2003052104 Invalid usage of DOMOutputBuffer.appendLiteral()

 25-Feb-04	2974/6	steve	VBM:2004020608 supermerged

 17-Feb-04	2974/3	steve	VBM:2004020608 SGML Quote handling

 19-Feb-04	2789/4	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/2	tony	VBM:2004012601 Localised logging (and exceptions)

 12-Feb-04	2958/1	philws	VBM:2004012715 Add protocol.content.type device policy
 05-Feb-04	2794/1	steve	VBM:2004012613 HTML Quote handling

 ===========================================================================
*/


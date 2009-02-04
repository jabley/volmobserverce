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
 * $Header: /src/voyager/com/volantis/mcs/protocols/text/SMS.java,v 1.7 2003/04/10 12:53:24 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 06-Nov-02    Geoff           VBM:2002103005 - Created.
 * 14-Nov-02    Geoff           VBM:2002103005 - Phase 2, get PRE to work,
 *                              fix bugs, add stacks, remove writeLayout
 * 20-Nov-02    Geoff           VBM:2002103005 - Phase 3, implement text
 *                              fallbacks.
 * 27-Nov-02    Geoff           VBM:2002103005 - Phase 4, test and fix iterator
 *                              panes and grids: use separate
 *                              LogicalWhitespaceWriter for page output,
 *                              refactor WhitespaceWriter handling into new
 *                              TextOutputBuffer and related error handling,
 *                              recode ignoring to be buffer rather than stream
 *                              based, avoid creating string in writeToPage.
 * 12-Dec-02    Geoff           VBM:2002121023 - Make Debug member name comply
 *                              with coding standards.
 * 23-Jan-03    Chris W         VBM:2002121022 - Added writeAltText() to write
 *                              out the alt text for image, mmflash, quicktime,
 *                              realAudio, realVideo, windowsAudio and
 *                              windowsVideo tags
 * 29-Jan-03    Adrian          VBM:2003012104 - Modified to use refactored
 *                              ListItemIdGenerator classes.
 * 12-Feb-03    Phil W-S        VBM:2003021206 - Add private writer member.
 *                              Update getOutputWriter. Refactor
 *                              closeCanvasPage to become writeCanvasContent.
 *                              Refactor closeMontagePage to become
 *                              writeMontageContent.
 * 09-Apr-03    Phil W-S        VBM:2002111502 - Override
 *                              writeClosePhoneNumber.
 * 25-Apr-03    Steve           VBM:2003041606 - EncodingWriter is an
 *                              OutputBufferWriter object so it needs to be
 *                              separated from directWriter as they are no longer
 *                              the same thing.
 * 28-May-03    Mat             VBM:2003042911 - Changed writeCanvasContent() &
 *                              writeMontageContent() to accept a
 *                              PackageBodySource instead of a writer.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.text;

import com.volantis.mcs.dom.dtd.DTD;
import com.volantis.mcs.dom.output.DOMDocumentOutputter;
import com.volantis.mcs.dom.output.DocumentOutputter;
import com.volantis.mcs.dom.xml.XMLDTDBuilder;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.AddressAttributes;
import com.volantis.mcs.protocols.AnchorAttributes;
import com.volantis.mcs.protocols.BlockQuoteAttributes;
import com.volantis.mcs.protocols.CanvasAttributes;
import com.volantis.mcs.protocols.ColumnIteratorPaneAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DefinitionDataAttributes;
import com.volantis.mcs.protocols.DefinitionListAttributes;
import com.volantis.mcs.protocols.DefinitionTermAttributes;
import com.volantis.mcs.protocols.DivAttributes;
import com.volantis.mcs.protocols.GridAttributes;
import com.volantis.mcs.protocols.GridChildAttributes;
import com.volantis.mcs.protocols.GridRowAttributes;
import com.volantis.mcs.protocols.HeadingAttributes;
import com.volantis.mcs.protocols.HorizontalRuleAttributes;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.LineBreakAttributes;
import com.volantis.mcs.protocols.ListItemAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.MontageAttributes;
import com.volantis.mcs.protocols.NoScriptAttributes;
import com.volantis.mcs.protocols.OrderedListAttributes;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.OutputBufferFactory;
import com.volantis.mcs.protocols.OutputBufferWriter;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.ParagraphAttributes;
import com.volantis.mcs.protocols.PhoneNumberAttributes;
import com.volantis.mcs.protocols.PreAttributes;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.ProtocolWriter;
import com.volantis.mcs.protocols.RowIteratorPaneAttributes;
import com.volantis.mcs.protocols.ScriptAttributes;
import com.volantis.mcs.protocols.StyleAttributes;
import com.volantis.mcs.protocols.TableAttributes;
import com.volantis.mcs.protocols.TableCellAttributes;
import com.volantis.mcs.protocols.TableRowAttributes;
import com.volantis.mcs.protocols.UnorderedListAttributes;
import com.volantis.mcs.protocols.ValidationHelper;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.runtime.packagers.PackageBodyOutput;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * A protocol implementation for sending SMS messages.
 * <p>
 * It's features are to be:
 * <ul>
 *   <li>Outputs tag body content with whitespace normalised.
 *   <li>Uses text fallbacks for non-text components if available.
 *   <li>Simulates some simple markup layout (see below).
 * </ul>
 * <p>
 * The markup simulated is as follows:
 * <ul>
 *   <li>Trims all linefeeds and unecessary spaces from tag contents
 *      (apart from PRE).
 *   <li>Ensures there is a line break in the output text before and after
 *      most block level tags (P, Hn, UL, OL, LI, DL, DT, DD, PRE, DIV,
 *      BLOCKQUOTE, HR, TABLE, ADDRESS), BR and TR
 *   <li>Adds whitespace around text for TD, TH and DT
 *   <li>Emulates ordered and unordered lists as appropriate, but only the most
 *      simple cases, the attributes of these elements are ignored.
 * </ul>
 * <p>
 * Some other features that should be noted are:
 * <ul>
 *   <li>Elements which create newlines (such as P) tend to break the emulated
 *      formatting; there is not much we can (simply) do about this.
 *   <li>Character encoding and trimming to size is handled by
 *      the SMS channel adapter.
 *   <li>All theme/style information is ignored.
 *   <li>All form content is ignored.
 *   <li>It doesn't provide support for inclusion.
 *   <li>It doesn't provide support for dissecting panes.
 *   <li>It doesn't provide support for segments or fragments.
 *   <li>Any HTML entities in the content will be ignored/passed through(!)
 *   <li>It doesn't use very much VolantisProtocol implementation!
 * </ul>
 * <p>
 * SMS output is quite different to "normal" HTML/XML markup output in that
 * whitespace is significant, so we have to take input which has effectively
 * random whitespace and normalise it so that it looks good in an SMS message.
 * <p>
 * This is handled by special writers provided by {@link SMS_DOMOutputBuffer}.
 */
public class SMS extends DOMProtocol {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(SMS.class);

    /**
     * Set this to true if you want to see in line debugging comments in the
     * output.
     */
    public static final boolean debug = false;

    //
    // Instance variables
    //

    /**
     * The object we use to simulate list item id generation, stored in a
     * stack so we can handle nested lists without barfing.
     */
    private ListItemIdGeneratorStack listItemIdGeneratorStack;

    /**
     * The writer that we use to write the final page, once we have prepared
     * all our output buffers.
     */
    private LogicalWhitespaceWriter outputWriter;

    /**
     * The writer to be wrappered by the outputWriter.
     */
    private Writer writer = null;

    public SMS(ProtocolSupportFactory protocolSupportFactory,
            ProtocolConfiguration protocolConfiguration) {

        super(protocolSupportFactory, protocolConfiguration);
        // NOTE: sms does not require the protocol support factory at the
        // moment but the protocol factory always supplies it.
    }

    //
    // General helper methods
    //

    /**
     * Returns the writer we use to write the final page.
     *
     * @return a writer.
     */

    private LogicalWhitespaceWriter getOutputWriter() {
        return ((SMS_DOMOutputBuffer)getCurrentBuffer()).getLogicalWriter();
    }

    //
    // Volantis stuff
    //

    //
    // Initialisation from VolantisProtocol
    //

    // Inherit Javadoc.
    public void initialise() {
        super.initialise();
        // Initialise our state.
        listItemIdGeneratorStack = new ListItemIdGeneratorStack();
    }

    // Javadoc inherited.
    protected void createWriters() {
        contentWriter = new OutputBufferWriter( new ProtocolWriter(this));
    }


    // Inherit Javadoc.
    public OutputBufferFactory getOutputBufferFactory() {
        // Lazily initialise the factory to allow the pool time to be
        // constructed (or not) depending on configuration.
        if (outputBufferFactory == null) {
            outputBufferFactory = new SMS_DOMOutputBufferFactory(
                    getDOMFactory());
        }
        return outputBufferFactory;
    }


    // javadoc inherited
    public void writeInitialHeader() {
        // empty because we do not want any header
    }

    protected void writeMontageContent(PackageBodyOutput output,
                                       MontageAttributes attributes)
        throws IOException, ProtocolException {
        throw new UnsupportedOperationException(
            "SMS does not support montage");
    }

    // Javadoc inherited
    public String defaultMimeType() {
        return "";
    }

    // Inherit Javadoc.
    public void openInclusionPage(CanvasAttributes attributes)
            throws IOException {
        throw new UnsupportedOperationException(
                "SMS does not support inclusion");
    }

    // Inherit Javadoc.
    public void closeInclusionPage(CanvasAttributes attributes)
            throws IOException, ProtocolException {
        throw new UnsupportedOperationException(
                "SMS does not support inclusion");
    }

    // Inherit Javadoc.
    public void openMontagePage(MontageAttributes attributes)
            throws IOException {
        throw new UnsupportedOperationException(
                "SMS does not support montage");
    }

    /**
     * Write line break using logical writer
     */
    private void writeLogicalLine(){
        ((SMS_DOMOutputBuffer)getCurrentBuffer()).getLogicalWriter().writeLine();
    }

    /**
     * write space using logical writer
     */
    private void writeLogicalSpace(){
        ((SMS_DOMOutputBuffer)getCurrentBuffer()).getLogicalWriter().writeSpace();
    }

    /**
     * Write content using logical writer
     * @param content
     */
    private void writeLogicalContent(String content){
        ((SMS_DOMOutputBuffer)getCurrentBuffer()).getLogicalWriter().write(content);
    }

    // Inherit Javadoc.
    public void openPane(DOMOutputBuffer outputBuffer, PaneAttributes attributes){
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void closePane(DOMOutputBuffer outputBuffer, PaneAttributes attributes){
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void writePaneContents(OutputBuffer buffer)
            throws IOException {
        DOMOutputBuffer dom = getCurrentBuffer ();
        dom.addOutputBuffer ((DOMOutputBuffer) buffer/* trim */);
    }


    // Inherit Javadoc.
    public void openGrid(DOMOutputBuffer outputBuffer, GridAttributes attributes){
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void closeGrid(DOMOutputBuffer outputBuffer, GridAttributes attributes){
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void openGridRow(DOMOutputBuffer outputBuffer, GridRowAttributes attributes){
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void closeGridRow(DOMOutputBuffer outputBuffer, GridRowAttributes attributes){
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void openGridChild(DOMOutputBuffer outputBuffer, GridChildAttributes attributes){
        writeLogicalSpace();
    }

    // Inherit Javadoc.
    public void closeGridChild(DOMOutputBuffer outputBuffer, GridChildAttributes attributes){
        writeLogicalSpace();
    }

    // Inherit Javadoc.
    public void openColumnIteratorPane(
            DOMOutputBuffer outputBuffer,
            ColumnIteratorPaneAttributes attributes){
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void closeColumnIteratorPane(
            DOMOutputBuffer outputBuffer,
            ColumnIteratorPaneAttributes attributes){
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void writeOpenColumnIteratorPaneElement(
            DOMOutputBuffer outputBuffer,
            ColumnIteratorPaneAttributes attributes)
            throws IOException {
        writeLogicalSpace();
    }

    // Inherit Javadoc.
    public void writeCloseColumnIteratorPaneElement(
            DOMOutputBuffer outputBuffer,
            ColumnIteratorPaneAttributes attributes){
        writeLogicalSpace();
    }

    // Inherit Javadoc.
    public void writeColumnIteratorPaneElementContents(OutputBuffer buffer){
        writeToPage(buffer);
    }

    // Inherit Javadoc.
    public void openRowIteratorPane(DOMOutputBuffer outputBuffer,
                                    RowIteratorPaneAttributes attributes){
        // do nothing; lines added by each open / close element
    }

    // Inherit Javadoc.
    public void closeRowIteratorPane(DOMOutputBuffer outputBuffer,
                                          RowIteratorPaneAttributes attributes){
        // do nothing; lines added by each open / close element
    }

    // Inherit Javadoc.
    public void openRowIteratorPaneElement(
            DOMOutputBuffer outputBuffer,
            RowIteratorPaneAttributes attributes){
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void closeRowIteratorPaneElement(
                DOMOutputBuffer outputBuffer,
                RowIteratorPaneAttributes attributes){
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void writeRowIteratorPaneElementContents(OutputBuffer buffer){
        writeToPage(buffer);
    }

    //
    // Helper methods for pane related stuff.
    //

    /**
     * Write an output buffer out to the final page representation. Named this
     * way to be similar to the way StringProtocol works. Pointless really but
     * there you go.
     *
     * @param outputBuffer the (SMS) buffer to write
     * @exception IOException
     */
    private void writeToPage(OutputBuffer outputBuffer){
        DOMOutputBuffer dom = (DOMOutputBuffer)outputBuffer;

        String content = null;
        StringWriter writer = new StringWriter();

        XMLDTDBuilder builder = new XMLDTDBuilder();
        DTD dtd = builder.buildDTD();

        DocumentOutputter outputter = new DOMDocumentOutputter(
            dtd.createDocumentWriter(writer), getCharacterEncoder());

        try {
            outputter.output(dom.getRoot());
            content = writer.toString();

            if (debug) {
                writeLogicalContent("<<");
            }
            if (content != null && content.length() > 0) {
                writeLogicalContent(content);
            }
            if (debug) {
                writeLogicalContent(">>");
            }
        } catch (IOException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Could not add content of pre tag to output" +
                        " because of an unexpected Exception: ", e);
            }
        }

    }

    //
    // Usual markup tags
    //

    // Inherit Javadoc.
    public void openAddress(DOMOutputBuffer outputBuffer,AddressAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void closeAddress(DOMOutputBuffer outputBuffer,AddressAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void writeCloseAnchor(AnchorAttributes attributes) {
        // If we have fallback text for the link
        String fallbackText = getTextFallbackFromLink(attributes.getHref());
        if (fallbackText != null) {
            // Write it out, inside brackets and with normalised space, after
            // the link body.
            // Note: the Spec doesn't specifically describe what to do in this
            // case, we could alternatively do nothing, or write the link
            // itself here.
            writeLogicalSpace();
            Writer normaliseWriter = getCurrentBuffer().getWriter();
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

    public void writeClosePhoneNumber(PhoneNumberAttributes attributes) {
        // Write the full number attribute out, inside brackets and with
        // normalised space, after the phone number body content.
        // Note: the requirement doesn't specifically described what to do in
        // this case (we could do nothing) but this has been done to
        // be consistent with how anchors are treated
        writeLogicalSpace();
        Writer normaliseWriter = getCurrentBuffer().getWriter();

        try {
            normaliseWriter.write("(");
            normaliseWriter.write(attributes.getDefaultContents());
            normaliseWriter.write(")");
        } catch (IOException e) {
            logger.error("unexpected-ioexception", e);
            // and continue!
        }
    }

    // Inherit Javadoc.
    public void openBlockQuote(DOMOutputBuffer outputBuffer,BlockQuoteAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void closeBlockQuote(DOMOutputBuffer outputBuffer,BlockQuoteAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void openDefinitionList(DOMOutputBuffer outputBuffer,DefinitionListAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void closeDefinitionList(DOMOutputBuffer outputBuffer,DefinitionListAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void openDefinitionTerm(DOMOutputBuffer outputBuffer,DefinitionTermAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void closeDefinitionTerm(DOMOutputBuffer outputBuffer,DefinitionTermAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void openDefinitionData(DOMOutputBuffer outputBuffer,DefinitionDataAttributes attributes) {
        writeLogicalLine();
        // DD is normally a bit indented, but I can't be bothered to implement
        // this at the moment - you'd need a stack...
    }

    // Inherit Javadoc.
    public void closeDefinitionData(DOMOutputBuffer outputBuffer,DefinitionDataAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void openDiv(DOMOutputBuffer outputBuffer,DivAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void closeDiv(DOMOutputBuffer outputBuffer,DivAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void openHeading1(DOMOutputBuffer outputBuffer,HeadingAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void closeHeading1(DOMOutputBuffer outputBuffer,HeadingAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void openHeading2(DOMOutputBuffer outputBuffer,HeadingAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void closeHeading2(DOMOutputBuffer outputBuffer,HeadingAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void openHeading3(DOMOutputBuffer outputBuffer,HeadingAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void closeHeading3(DOMOutputBuffer outputBuffer,HeadingAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void openHeading4(DOMOutputBuffer outputBuffer,HeadingAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void closeHeading4(DOMOutputBuffer outputBuffer,HeadingAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void openHeading5(DOMOutputBuffer outputBuffer,HeadingAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void closeHeading5(DOMOutputBuffer outputBuffer,HeadingAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void openHeading6(DOMOutputBuffer outputBuffer,HeadingAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void closeHeading6(DOMOutputBuffer outputBuffer,HeadingAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void openListItem(DOMOutputBuffer outputBuffer,ListItemAttributes attributes) {
        // spec requires we emulate * or number
        QuietLogicalWhitespaceWriter logicalWriter = (QuietLogicalWhitespaceWriter)getOutputWriter();
        listItemIdGeneratorStack.peek().addNextListItemId(logicalWriter);
        writeLogicalSpace();
    }

    // Inherit Javadoc.
    public void closeListItem(DOMOutputBuffer outputBuffer,ListItemAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void openNoScript(DOMOutputBuffer outputBuffer,NoScriptAttributes attributes) {
        ignoreStart();
    }

    // Inherit Javadoc.
    public void closeNoScript(DOMOutputBuffer outputBuffer,NoScriptAttributes attributes) {
        ignoreEnd();
    }

    // Inherit Javadoc.
    public void openOrderedList(DOMOutputBuffer outputBuffer,OrderedListAttributes attributes) {
        // set up emulation for numbers
        listItemIdGeneratorStack.push(new ListItemIdGeneratorOrdered());
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void closeOrderedList(DOMOutputBuffer outputBuffer,OrderedListAttributes attributes) {
        // clean up number emulation
        listItemIdGeneratorStack.pop();
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void openUnorderedList(DOMOutputBuffer outputBuffer,UnorderedListAttributes attributes) {
        // set up emulation for *
        listItemIdGeneratorStack.push(
                new ListItemIdGeneratorUnordered());
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void closeUnorderedList(DOMOutputBuffer outputBuffer,UnorderedListAttributes attributes) {
        // clean up emulation for *
        listItemIdGeneratorStack.pop();
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void openParagraph(DOMOutputBuffer outputBuffer,ParagraphAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void closeParagraph(DOMOutputBuffer outputBuffer,ParagraphAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void openPre(DOMOutputBuffer dom,PreAttributes attributes) {

        // Create a new output buffer
        SMS_DOMOutputBuffer outputBuffer = (SMS_DOMOutputBuffer)
                getOutputBufferFactory().createOutputBuffer();
        // Tell the buffer not to clean up subsequent PRE content whitespace.
        outputBuffer.setPreformatted(true);
        // And stick it on the stack to capture output from now on.
        context.pushOutputBuffer(outputBuffer);
    }

    // Inherit Javadoc.
    public void closePre(DOMOutputBuffer dom,PreAttributes attributes) {
        // Pop the PRE's OutputBuffer off the stack.
        SMS_DOMOutputBuffer prevOutputBuffer = (SMS_DOMOutputBuffer)getCurrentBuffer();
        context.popOutputBuffer(prevOutputBuffer);

        String content = null;
        StringWriter writer = new StringWriter();

        XMLDTDBuilder builder = new XMLDTDBuilder();
        DTD dtd = builder.buildDTD();

        DocumentOutputter outputter = new DOMDocumentOutputter(
            dtd.createDocumentWriter(writer), getCharacterEncoder());

        try {
            outputter.output(prevOutputBuffer.getRoot());
            content = writer.toString();
        } catch (IOException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Could not add content of pre tag to output" +
                        " because of an unexpected Exception: ", e);
            }        
        }

        // Normalise the surrounding whitespace and dump into parent buffer.
        // NOTE: The buffer really ought to handle whitespace trimming itself,
        // since it does in non-preformatted mode...
        if (content != null) {
            writeLogicalLine();
            writeLogicalContent(content);
            writeLogicalLine();
        }
    }

    // Inherit Javadoc.
    public void writeOpenScript(ScriptAttributes attributes) {
        ignoreStart();
    }

    // Inherit Javadoc.
    public void writeCloseScript(ScriptAttributes attributes) {
        ignoreEnd();
    }

    // Inherit Javadoc.
    public void writeOpenStyle(OutputBuffer out,
            StyleAttributes attributes) {
        // SMS ignores styles
    }

    // Inherit Javadoc.
    public void writeCloseStyle(OutputBuffer out,
            StyleAttributes attributes) {
        // SMS ignores styles
    }

    // Inherit Javadoc.
    public void openTable(DOMOutputBuffer outputBuffer,TableAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void closeTable(DOMOutputBuffer outputBuffer,TableAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void openTableDataCell(DOMOutputBuffer outputBuffer,TableCellAttributes attributes) {
        writeLogicalSpace();
    }

    // Inherit Javadoc.
    public void closeTableDataCell(DOMOutputBuffer outputBuffer,TableCellAttributes attributes) {
        writeLogicalSpace();
    }

    // Inherit Javadoc.
    public void openTableHeaderCell(DOMOutputBuffer outputBuffer,TableCellAttributes attributes) {
        writeLogicalSpace();
    }

    // Inherit Javadoc.
    public void closeTableHeaderCell(DOMOutputBuffer outputBuffer,TableCellAttributes attributes) {
        writeLogicalSpace();
    }

    // Inherit Javadoc.
    public void openTableRow(DOMOutputBuffer outputBuffer,TableRowAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void closeTableRow(DOMOutputBuffer outputBuffer,TableRowAttributes attributes) {
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void doHorizontalRule(DOMOutputBuffer outputBuffer,HorizontalRuleAttributes attributes) {
        writeLogicalContent("---");
        writeLogicalLine();
    }

    // Inherit Javadoc.
    public void doLineBreak(DOMOutputBuffer outputBuffer, LineBreakAttributes attributes) {
        writeLogicalLine();
    }

    // NOTE: SMS device should never not let any images, dynvis or charts get
    // rendered through this protocol, so all attempts to do so should generate
    // fallbacks instead of calling these methods. We throw exception if it
    // ever happens to ensure this is the case :-).

    // Inherit Javadoc.
    public void doImage(DOMOutputBuffer dom, ImageAttributes attributes) {
        // can't call writeAltText as ImageAttributes and
        // GenericMediaAttributes are in different parts of hierarchy. Common
        // super VolantisAttribute does not contain a getAltText method.
        TextAssetReference reference = attributes.getAltText();
        String altText = getPlainText(reference);

        if ((altText != null) && !altText.equals(""))
        {
            writeLogicalContent(altText);
        }
    }

    // @todo protocol needs writeChart method to enable per protocol fallbacks
    // for chart elements, but this is not the time to do it...

    // Inherit Javadoc.
    protected void renderAltText(String altText,
            MCSAttributes attributes) {
        writeLogicalContent(altText);
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
        DOMOutputBuffer currentBuffer = getCurrentBuffer();
        context.popOutputBuffer(currentBuffer);
    }

    //Javadoc inherited
    public ValidationHelper getValidationHelper() {
        return null;
    }

}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9298/5	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 22-Aug-05	9324/1	ianw	VBM:2005080202 Move validation for WapCSS into styling

 05-Apr-05	7513/3	geoff	VBM:2003100606 DOMOutputBuffer allows creation of text which renders incorrectly in WML

 05-Apr-05	7513/1	geoff	VBM:2003100606 DOMOutputBuffer allows creation of text which renders incorrectly in WML

 24-Feb-05	7129/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 24-Feb-05	7099/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 26-Nov-04	6298/1	geoff	VBM:2004112405 MCS NullPointerException in wml code path

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 30-Jun-04	4781/3	adrianj	VBM:2002111405 Created SMS test case and added check for null/empty mime types in protocols

 30-Jun-04	4781/1	adrianj	VBM:2002111405 VolantisProtocol.defaultMimeType() and DOMProtocol made abstract

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 06-May-04	4174/1	claire	VBM:2004050501 Enhanced Menus: (X)HTML menu renderer selectors and protocol integration

 29-Apr-04	4091/3	claire	VBM:2004042804 Enhanced menus: protocol integration and Openwave end to end

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 03-Nov-03	1760/1	philws	VBM:2003031710 Port image alt text component reference handling from PROTEUS

 02-Nov-03	1751/1	philws	VBM:2003031710 Permit image alt text to be component reference

 05-Jun-03	285/5	mat	VBM:2003042911 Merged with MCS

 ===========================================================================
*/

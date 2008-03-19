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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/VolantisProtocolStub.java,v 1.6 2003/04/23 09:44:20 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
  * 13-Jan-03    Doug           VBM:2002111806 - Created. This is a stubbed
 *                              Implementation of VolantisProtocol . It is
 *                              allows TestCase authors use a protocol without
 *                              all the initialisation hassles.
 *                              Nearky all the methods currently do nothing.
 * 30-Jan-03    Geoff           VBM:2003012101 - Added deprecation comment for
 *                              this class since we have the "equivalent"
 *                              TestVolantisProtocol class.
 * 12-Feb-03    Phil W-S        VBM:2003021206 - Update VolantisProtocol
 *                              specialization.
 * 17-Apr-03    Allan           VBM:2003041506 - Undeprecated this class.
 * 17-Apr-03    Geoff           VBM:2003040305 - Remove getScriptFromObject
 *                              overload since it has been removed from the
 *                              parent.
 * 25-Apr-03    Steve           VBM:2003041606 - getEncodingWriter returns an
 *                              OutputBufferWriter... actually it returns null
 *                              but it is supposed to return one of these.
 * 30-May-03    Mat             VBM:2003042911 - Change writeCanvasContent()
 *                              & writeMontageContent() to accept a
 *                              PackageBodyOutput instead of a Writer
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.css.renderer.StyleSheetRenderer;
import com.volantis.mcs.dom.output.CharacterEncoder;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.protocols.css.emulator.EmulatorRendererContext;
import com.volantis.mcs.protocols.forms.ActionFieldType;
import com.volantis.mcs.protocols.forms.BooleanFieldType;
import com.volantis.mcs.protocols.forms.ContentFieldType;
import com.volantis.mcs.protocols.forms.FieldHandler;
import com.volantis.mcs.protocols.forms.ImplicitFieldType;
import com.volantis.mcs.protocols.forms.MultipleSelectFieldType;
import com.volantis.mcs.protocols.forms.SingleSelectFieldType;
import com.volantis.mcs.protocols.forms.TextInputFieldType;
import com.volantis.mcs.protocols.layouts.ContainerInstance;
import com.volantis.mcs.runtime.packagers.PackageBodyOutput;
import com.volantis.mcs.utilities.MarinerURL;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * This is a stubbed Implementation of VolantisProtocol . It is allows
 * TestCase authors use a protocol without all the initialisation hassles.
 * <p>
 * NOTE: this is a duplication of {@link TestVolantisProtocol}. I think we
 * should use that rather than use this class for new development,
 * and finally make all existing usages of this class use that new class.
 *
 * @deprecated Please do not use this for new development; see the class
 * comments for the reasons why.
 */
public class VolantisProtocolStub
        extends VolantisProtocol {

    /**
     * The OutputBufferFactory
     */
    protected OutputBufferFactory bufferFactory;

    /**
     * Create a new VolantisProtocolStub instance
     */
    public VolantisProtocolStub() {
        super(null);
    }

    // javadoc inherited from superclass
    public OutputBufferFactory getOutputBufferFactory() {
        return bufferFactory;
    }

    /**
     * Helper method to allow TestCases to set the OutputBufferFactory that
     * the getOutputBufferFactory() returns
     * @param bufferFactory
     */
    public void setOutputBufferFactory(OutputBufferFactory bufferFactory) {
        this.bufferFactory = bufferFactory;
    }

    // javadoc inherited from superclass
    public void initialise() {
    }

    // javadoc inherited from superclass
    public void initialiseCanvas() {
    }

    // javadoc inherited from superclass
    protected void initialisePageHead() {
    }

    // javadoc inherited from superclass
    public void release() {

    }

    // javadoc inherited from superclass
    public void setDissecting(boolean dissecting) {

    }

    // javadoc inherited from superclass
    public boolean isDissecting() {
        return false;
    }

    // javadoc inherited from superclass
    public void setDissectionNeeded(boolean dissectionNeeded) {

    }

    // javadoc inherited from superclass
    public boolean isDissectionNeeded() {
        return false;
    }

    // javadoc inherited from superclass
    public void setCanvasAttributes(CanvasAttributes canvasAttributes) {

    }

    // javadoc inherited from superclass
    public CanvasAttributes getCanvasAttributes() {
        return null;
    }

    // javadoc inherited from superclass
    public void setInclusion(boolean b) {

    }

    // javadoc inherited from superclass
    public MarinerPageContext getMarinerPageContext() {
        return null;
    }

    // javadoc inherited from superclass
    public void setMarinerPageContext(MarinerPageContext context) {

    }

    // javadoc inherited from superclass
    public void setWriteHead(boolean b) {

    }

    // javadoc inherited from superclass
    public boolean getWriteHead() {
        return false;
    }

    // javadoc inherited from superclass
    public PageHead getPageHead() {
        return null;
    }

    // javadoc inherited from superclass
    public void setPageType(int pageType) {

    }

    // javadoc inherited from superclass

    public boolean isPageType(int pageType) {
        return false;
    }

    // javadoc inherited from superclass

    public StyleSheetRenderer getStyleSheetRenderer() {
        return null;
    }

    // javadoc inherited from superclass
    public EmulatorRendererContext getEmulatorRendererContext() {
        return null;
    }

    // javadoc inherited from superclass

    public String quoteTextString(String s) {
        return null;
    }

    // javadoc inherited from superclass

    public String quoteTextString(char[] chars, int off, int len) {
        return null;
    }

    // javadoc inherited from superclass
    protected String getTextFromObject(Object object, int encoding) {
        return null;
    }

    // javadoc inherited from superclass
    protected String getLinkFromObject(Object object) {
        return null;
    }

    // javadoc inherited from superclass
    protected String getLinkFromObject(Object object,
                                       boolean encodeSegmentURL) {
        return null;
    }

    // javadoc inherited from superclass
    protected String getTextFallbackFromLink(Object object) {
        return null;
    }

    // javadoc inherited from superclass
    protected String encodeSegmentURL(String url) {
        return null;
    }

    // javadoc inherited from superclass
    public String getInclusionStyleElement() {
        return null;
    }

    // javadoc inherited from superclass
    public String getInclusionStyleID() {
        return null;
    }

    // javadoc inherited from superclass
    public String mimeType() {
        return null;
    }

    // javadoc inherited from superclass
    public String defaultMimeType() {
        return null;
    }

    // javadoc inherited from superclass
    public void writeLayout(DeviceLayoutContext deviceLayoutContext)
            throws IOException {
    }

    // javadoc inherited from superclass
    public void writeStyleSheet()
            throws IOException {

    }

    // javadoc inherited from superclass
    protected int renderMode(int preference,
                             int defaultRenderingMode,
                             boolean externalable,
                             boolean internalable,
                             boolean importable) {
        return -1;
    }

    // javadoc inherited from superclass
    public void writeOpenBody(BodyAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public void writeCloseBody(BodyAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public void writeOpenCanvas(CanvasAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public void writeCloseCanvas(CanvasAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public void writeOpenInclusion(CanvasAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public void writeCloseInclusion(CanvasAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public void beginNestedInclusion() {

    }

    // javadoc inherited from superclass
    public void endNestedInclusion() {

    }

    // javadoc inherited from superclass
    public void writeInitialHeader() {

    }

    // javadoc inherited from superclass
    public void writeOpenMontage(MontageAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public void writeCloseMontage(MontageAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public void writeProtocolString() {

    }

    // javadoc inherited from superclass
    public void writeOpenStyle(OutputBuffer out,
                               StyleAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseStyle(OutputBuffer out,
                                StyleAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenSlide(SlideAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseSlide(SlideAttributes attributes) {

    }

    // javadoc inherited from superclass
    public int getMaxPageSize() {
        return -1;
    }

    public String encodeCharacter(int c) {
        return null;
    }

    public CharacterEncoder getCharacterEncoder() {
        return null;
    }

    // javadoc inherited from superclass
    public OutputBufferWriter getContentWriter() {
        return null;
    }

    // javadoc inherited from superclass
    public
            void writeOpenColumnIteratorPane(ColumnIteratorPaneAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public
            void writeCloseColumnIteratorPane(ColumnIteratorPaneAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public void writeOpenColumnIteratorPaneElement(ColumnIteratorPaneAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public void writeCloseColumnIteratorPaneElement(ColumnIteratorPaneAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public void writeColumnIteratorPaneElementContents(OutputBuffer buffer)
            throws IOException {

    }

    // javadoc inherited from superclass
    public
            void writeOpenDissectingPane(DissectingPaneAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public
            void writeCloseDissectingPane(DissectingPaneAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public void writeOpenForm(FormAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public void writeCloseForm(FormAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public void writeFormPreamble(OutputBuffer buffer)
            throws IOException {

    }

    // javadoc inherited from superclass
    public void writeFormPostamble(OutputBuffer buffer)
            throws IOException {

    }

    // javadoc inherited from superclass
    public void writeOpenGrid(GridAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseGrid(GridAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenGridChild(GridChildAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseGridChild(GridChildAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenGridRow(GridRowAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseGridRow(GridRowAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenLayout(LayoutAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public void writeCloseLayout(LayoutAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public void writeOpenPane(PaneAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public void writeClosePane(PaneAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public void writePaneContents(OutputBuffer buffer)
            throws IOException {

    }

    // javadoc inherited from superclass
    public
            void writeOpenRowIteratorPane(RowIteratorPaneAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public
            void writeCloseRowIteratorPane(RowIteratorPaneAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public
            void writeOpenRowIteratorPaneElement(RowIteratorPaneAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public void writeCloseRowIteratorPaneElement(RowIteratorPaneAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public void writeRowIteratorPaneElementContents(OutputBuffer buffer)
            throws IOException {

    }

    // javadoc inherited from superclass
    public void writeOpenSegment(SegmentAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public void writeCloseSegment(SegmentAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public void writeOpenSegmentGrid(SegmentGridAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public void writeCloseSegmentGrid(SegmentGridAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public void writeOpenAnchor(AnchorAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseAnchor(AnchorAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeDefaultSegmentLink(AnchorAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass
    public void writeFragmentLink(FraglinkAttributes attributes)
            throws IOException {

    }

    // javadoc inherited from superclass

    // javadoc inherited from superclass
    public void writeOpenAddress(AddressAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseAddress(AddressAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenBlockQuote(BlockQuoteAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseBlockQuote(BlockQuoteAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenDiv(DivAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseDiv(DivAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenHeading1(HeadingAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseHeading1(HeadingAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenHeading2(HeadingAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseHeading2(HeadingAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenHeading3(HeadingAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseHeading3(HeadingAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenHeading4(HeadingAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseHeading4(HeadingAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenHeading5(HeadingAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseHeading5(HeadingAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenHeading6(HeadingAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseHeading6(HeadingAttributes attributes) {

    }

    // javadoc inherited from superclass
    public
            void writeHorizontalRule(HorizontalRuleAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenParagraph(ParagraphAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseParagraph(ParagraphAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenPre(PreAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeClosePre(PreAttributes attributes) {

    }

    // javadoc inherited from superclass
    public
            void writeOpenDefinitionData(DefinitionDataAttributes attributes) {

    }

    // javadoc inherited from superclass
    public
            void writeCloseDefinitionData(DefinitionDataAttributes attributes) {

    }

    // javadoc inherited from superclass
    public
            void writeOpenDefinitionList(DefinitionListAttributes attributes) {

    }

    // javadoc inherited from superclass
    public
            void writeCloseDefinitionList(DefinitionListAttributes attributes) {

    }

    // javadoc inherited from superclass
    public
            void writeOpenDefinitionTerm(DefinitionTermAttributes attributes) {

    }

    // javadoc inherited from superclass
    public
            void writeCloseDefinitionTerm(DefinitionTermAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenListItem(ListItemAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseListItem(ListItemAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenOrderedList(OrderedListAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseOrderedList(OrderedListAttributes attributes) {

    }

    // javadoc inherited from superclass
    public
            void writeOpenUnorderedList(UnorderedListAttributes attributes) {

    }

    // javadoc inherited from superclass
    public
            void writeCloseUnorderedList(UnorderedListAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenTable(TableAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseTable(TableAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenTableBody(TableBodyAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseTableBody(TableBodyAttributes attributes) {

    }

    // javadoc inherited from superclass
    public
            void writeOpenTableDataCell(TableCellAttributes attributes) {

    }

    // javadoc inherited from superclass
    public
            void writeCloseTableDataCell(TableCellAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenTableFooter(TableFooterAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseTableFooter(TableFooterAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenTableHeader(TableHeaderAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseTableHeader(TableHeaderAttributes attributes) {

    }

    // javadoc inherited from superclass
    public
            void writeOpenTableHeaderCell(TableCellAttributes attributes) {

    }

    // javadoc inherited from superclass
    public
            void writeCloseTableHeaderCell(TableCellAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenTableRow(TableRowAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseTableRow(TableRowAttributes attributes) {

    }

    public void writeOpenBig(BigAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseBig(BigAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenBold(BoldAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseBold(BoldAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenCite(CiteAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseCite(CiteAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenCode(CodeAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseCode(CodeAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenEmphasis(EmphasisAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseEmphasis(EmphasisAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenItalic(ItalicAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseItalic(ItalicAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenKeyboard(KeyboardAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseKeyboard(KeyboardAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeLineBreak(LineBreakAttributes attributes) {

    }

    // javadoc inherited from superclass
    public
            void writeOpenMonospaceFont(MonospaceFontAttributes attributes) {

    }

    // javadoc inherited from superclass
    public
            void writeCloseMonospaceFont(MonospaceFontAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenSample(SampleAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseSample(SampleAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenSmall(SmallAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseSmall(SmallAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenSpan(SpanAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseSpan(SpanAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenStrong(StrongAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseStrong(StrongAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenSubscript(SubscriptAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseSubscript(SubscriptAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenSuperscript(SuperscriptAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseSuperscript(SuperscriptAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenUnderline(UnderlineAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseUnderline(UnderlineAttributes attributes) {

    }

    // javadoc inherited from superclass

    public void writeDivideHint(DivideHintAttributes attributes) {

    }

    // javadoc inherited from superclass

    public void writeImage(ImageAttributes attributes) {

    }

    // javadoc inherited from superclass

    public void writeMeta(MetaAttributes attributes) {

    }

    // javadoc inherited from superclass

    // javadoc inherited from superclass
    public void doMenu(MenuAttributes attributes) {

    }

    // javadoc inherited from superclass
    public boolean supportsJavaScript() {
        return false;
    }

    // javadoc inherited from superclass
    public void writeOpenNoScript(NoScriptAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseNoScript(NoScriptAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeOpenScript(ScriptAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseScript(ScriptAttributes attributes) {

    }

    // javadoc inherited from superclass
    public Writer getScriptBodyWriter() {
        return null;
    }

    // javadoc inherited from superclass
    public void doTimer(TimerAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void doForm(XFFormAttributes attributes) {

    }

    // javadoc inherited from superclass
    protected void openForm(XFFormAttributes attributes) {

    }

    // javadoc inherited from superclass
    protected void closeForm(XFFormAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void doContent(XFContentAttributes attributes) {

    }

    // javadoc inherited from superclass
    protected void writeContent(XFContentAttributes attributes) {

    }

    // javadoc inherited from superclass
    protected MarinerURL rewriteFormURL(MarinerURL url) {
        return null;
    }

    // javadoc inherited from superclass
    protected MarinerURL removeFormParameters(MarinerURL url) {
        return null;

    }

    // javadoc inherited from superclass
    protected String resolveFormAction(XFFormAttributes attributes) {
        return null;
    }

    // javadoc inherited from superclass
    public FieldHandler getFieldHandler(ContentFieldType type) {
        return null;
    }

    // javadoc inherited from superclass
    public FieldHandler getFieldHandler(TextInputFieldType type) {
        return null;
    }

    // javadoc inherited from superclass
    public void doTextInput(XFTextInputAttributes attributes) {

    }

    // javadoc inherited from superclass
    public FieldHandler getFieldHandler(BooleanFieldType type) {
        return null;
    }

    // javadoc inherited from superclass
    public void doBooleanInput(XFBooleanAttributes attributes) {

    }

    // javadoc inherited from superclass
    public FieldHandler getFieldHandler(SingleSelectFieldType type) {
        return null;
    }

    // javadoc inherited from superclass
    public FieldHandler getFieldHandler(MultipleSelectFieldType type) {
        return null;
    }

    // javadoc inherited from superclass
    public void doSelectInput(XFSelectAttributes attributes) {

    }

    // javadoc inherited from superclass
    public FieldHandler getFieldHandler(ActionFieldType type) {
        return null;
    }

    // javadoc inherited from superclass
    public void doActionInput(XFActionAttributes attributes) {

    }

    // javadoc inherited from superclass
    public FieldHandler getFieldHandler(ImplicitFieldType type) {
        return null;
    }

    // javadoc inherited from superclass
    public void doImplicitValue(XFImplicitAttributes attributes) {

    }

    // javadoc inherited from superclass
    protected void endCurrentBuffer(ContainerInstance containerInstance) {

    }

    // javadoc inherited from superclass
    protected String getInitialValue(XFFormFieldAttributes attributes) {
        return null;
    }

    // javadoc inherited from superclass
    protected void updateSelectedOptions(XFSelectAttributes attributes) {

    }

    // javadoc inherited from superclass
    protected void setMultipleSelected(List options, String[] initialValues) {

    }

    // javadoc inherited from superclass
    protected void setSingleSelected(List options, String initialValue) {

    }

    // javadoc inherited from superclass
    protected boolean isFormatStrValid(XFTextInputAttributes attributes) {
        return false;
    }

    // javadoc inherited from superclass
    protected boolean strContainsOnly(String str, String allowed) {
        return false;
    }

    // javadoc inherited from superclass
    public void writeOpenElement(CustomMarkupAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void writeCloseElement(CustomMarkupAttributes attributes) {

    }

    // javadoc inherited from superclass
    public boolean writeAltText(
            AltTextAttributes attributes) {
        return false;
    }

    // javadoc inherited from superclass
    protected void renderAltText(String altText,
                                 MCSAttributes attributes) {

    }

    // javadoc inherited from superclass
    public void pushHeadBuffer() {

    }

    // javadoc inherited from superclass
    public void popHeadBuffer() {

    }

    // javadoc inherited from superclass
    public int calculatePaneWidth(Pane pane) {
        return -1;
    }

    // javadoc inherited from superclass
    protected float calculateWidth(Pane pane) {
        return -1;
    }

    // javadoc inherited from superclass
    protected float calculateWidth(Format format, float width) {
        return -1;
    }

    // javadoc inherited from superclass
    public int calculatePaneHeight(Pane pane) {
        return -1;
    }

    // javadoc inherited from superclass
    public void openCanvasPage(CanvasAttributes attributes)
            throws IOException {
    }

    // javadoc inherited from superclass
    public void openInclusionPage(CanvasAttributes attributes)
            throws IOException {
    }

    // javadoc inherited from superclass
    public void closeInclusionPage(CanvasAttributes attributes)
            throws IOException, ProtocolException {
    }

    // javadoc inherited from superclass
    public void openMontagePage(MontageAttributes attributes)
            throws IOException {
    }

    protected void writeCanvasContent(PackageBodyOutput output,
                                      CanvasAttributes attributes)
        throws IOException, ProtocolException {
    }

    protected void writeMontageContent(PackageBodyOutput output,
                                       MontageAttributes attributes)
        throws IOException, ProtocolException {
    }

    public ValidationHelper getValidationHelper() {
        return null;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 15-Sep-05	9524/1	emma	VBM:2005091503 Added ContainerInstance to allow regions and panes to be treated in the same way

 02-Sep-05	9408/3	pabbott	VBM:2005083007 Move over to using JiBX accessor

 01-Sep-05	9407/1	pduffin	VBM:2005083007 Changed MIB2_1 and Netfront3 configuration to remove device specific theme, and replaced it with a new initial value finder that is device aware

 02-Sep-05	9407/4	pduffin	VBM:2005083007 Committing resolved conflicts

 01-Sep-05	9407/1	pduffin	VBM:2005083007 Changed MIB2_1 and Netfront3 configuration to remove device specific theme, and replaced it with a new initial value finder that is device aware

 01-Sep-05	9375/4	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9363/5	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/2	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/6	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 22-Aug-05	9324/1	ianw	VBM:2005080202 Move validation for WapCSS into styling

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 05-Apr-05	7513/1	geoff	VBM:2003100606 DOMOutputBuffer allows creation of text which renders incorrectly in WML

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-Nov-04	6076/4	tom	VBM:2004101509 Modified protocols to get their styles from MCSAttributes

 12-Nov-04	6135/1	byron	VBM:2004081726 Allow spatial format iterators within forms

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 02-Jul-04	4713/4	geoff	VBM:2004061004 Support iterated Regions (review comments)

 29-Jun-04	4713/1	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 01-Jul-04	4778/2	allan	VBM:2004062912 Use the Volantis.pageURLRewriter to rewrite page urls

 28-Jun-04	4733/1	allan	VBM:2004062105 Convert Volantis to use the new PageURLRewriter

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 06-May-04	4174/1	claire	VBM:2004050501 Enhanced Menus: (X)HTML menu renderer selectors and protocol integration

 29-Apr-04	4091/3	claire	VBM:2004042804 Enhanced menus: protocol integration and Openwave end to end

 26-Feb-04	3233/1	geoff	VBM:2004021609 styleclasses incorrectly rendered when within a region

 25-Feb-04	3179/1	geoff	VBM:2004021609 styleclasses incorrectly rendered when within a region

 12-Feb-04	2958/1	philws	VBM:2004012715 Add protocol.content.type device policy

 18-Aug-03	1146/1	geoff	VBM:2003042305 Add tearDown() to AppConfigurator

 18-Aug-03	1144/1	geoff	VBM:2003042305 Add tearDown() to AppConfigurator

 05-Jun-03	285/3	mat	VBM:2003042911 Merged with MCS

 ===========================================================================
*/

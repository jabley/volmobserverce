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
 * $Header: /src/voyager/testsuite/integration/com/volantis/integration/mcs/protocols/voicexml/VoiceXMLRootIntegrationTestAbstract.java,v 1.2 2003/04/30 08:35:40 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 03-Apr-03    Allan           VBM:2003040303 - Created. The start of the 
 *                              integration test for the VoiceXML protocol as 
 *                              an example of how to do it. 
 * 15-Apr-03    Byron           VBM:2003040302 - Added getXXX methods. Renamed
 *                              class and made it abstract.
 * 28-Apr-03    Allan           VBM:2003042802 - Moved from protocols package. 
 * 29-Apr-03    Byron           VBM:2003042812 - Removed commented out code and
 *                              added getExpectedDoImplicitValueResult.
 * ----------------------------------------------------------------------------
 */
package com.volantis.integration.mcs.protocols.voicexml;

/**
 * Integration test for the VoiceXML protocol.
 */
public abstract class VoiceXMLRootIntegrationTestAbstract
        extends VolantisProtocolIntegrationTestAbstract {

    private String getExpectedPromptResult() {
        return "<block><prompt/></block>" +
                "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenHeading1Result() {
        return getExpectedPromptResult();
    }

    protected String getExpectedWriteOpenHeading2Result() {
        return getExpectedPromptResult();
    }

    protected String getExpectedWriteOpenSmallResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenCanvasResult() {
        return "<vxml version=\"1.0\">" +
                "<form id=\"form-0\"><block><exit/></block></form></vxml>";
    }

    protected String getExpectedWriteOpenAnchorResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteCloseBigResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenCiteResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenCodeResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenColumnIteratorPaneElementResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenDefinitionListResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenDefinitionDataResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenDefinitionTermResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenListItemResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenUnorderedListResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenTableResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenTableBodyResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenTableDataCellResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenOrderedListResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenTableFooterResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenTableHeaderResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenBoldResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenTableHeaderCellResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenTableRowResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenBigResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteLineBreakResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenEmphasisResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenItalicResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenKeyboardResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenMonospaceFontResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenSampleResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenSpanResult() {
        return "<block><prompt/></block>" +
                "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenStrongResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenSubscriptResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteAudioResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteCloseHeading6Result() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteDivideHintResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteHorizontalRuleResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteImageResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteMetaResult() {
        return "<meta content=\"content\" http-equiv=\"httpEquiv\" name=\"name\"/>" +
                "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenHeading3Result() {
        return getExpectedPromptResult();
    }

    protected String getExpectedWriteOpenHeading4Result() {
        return getExpectedPromptResult();
    }

    protected String getExpectedWriteOpenHeading5Result() {
        return getExpectedPromptResult();
    }

    protected String getExpectedWriteOpenHeading6Result() {
        return getExpectedPromptResult();
    }

    protected String getExpectedWriteOpenParagraphResult() {
/*
        DOMProtocol version (incorrect --> cannot have form within a prompt)
        return "<block><prompt><form id=\"form-0\">" +
                "<block><exit/></block></form></prompt></block>";
*/
        // StringProtocol Version (correct)
        return "<block><prompt/></block><form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenPreResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenSuperscriptResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenUnderlineResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteLinkResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenBodyResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenColumnIteratorPaneResult() {
        return "<form id=\"form-0\"><block><goto next=\"#form-1\"/></block></form>" +
                "<form id=\"form-1\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenInclusionResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenMontageResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenNoScriptResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenScriptResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenSlideResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenSpatialFormatIteratorChildResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenSpatialFormatIteratorResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenSpatialFormatIteratorRowResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenStyleResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteProtocolStringResult() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<!DOCTYPE vxml SYSTEM \"http://www.voicexml.org/voicexml1-0.dtd\">" +
                "<form id=\"form-0\">" +
                    "<block><exit/></block>" +
                "</form>";
    }

    protected String getExpectedWriteColumnIteratorPaneElementContentsResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteFormPostambleResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteFormPreambleResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenDissectingPaneResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenFormResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenGridChildResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenGridResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteAltTextResult() {
        return "<block><prompt>altText</prompt></block>" +
                "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteDefaultSegmentLinkResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteFragmentLinkResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenAddressResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenBlockQuoteResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenElementResult() {
        return "<elementName key=\"value\"/>" +
                "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenGridRowResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenLayoutResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>" +
                "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenPaneResult() {
        return "<form id=\"form-0\"><block><goto next=\"#form-1\"/></block></form>" +
                "<form id=\"form-1\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenRowIteratorPaneElementResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenRowIteratorPaneResult() {
        return "<form id=\"form-0\"><block><goto next=\"#form-1\"/></block></form>" +
               "<form id=\"form-1\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenSegmentGridResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenSeqmentResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWritePaneContentsResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteRowIteratorPaneElementContentsResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteSSIConfigResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteSSIIncludeResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedWriteOpenDivResult() {
        return "<form id=\"form-0\"><block><exit/></block></form>";
    }

    protected String getExpectedDoImplicitValueResult() {
        return "<var expr=\"\'value\'\" name=\"name\" " +
                "containingXFFormName=\"containingXFFormName\"/>" +
            "<form id=\"form-0\"><block><exit/></block></form>";
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Oct-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 24-Jul-03	807/1	geoff	VBM:2003071405 now with fixed architecture

 ===========================================================================
*/

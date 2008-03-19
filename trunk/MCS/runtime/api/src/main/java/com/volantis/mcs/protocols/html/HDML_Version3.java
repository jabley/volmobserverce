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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/HDML_Version3.java,v 1.13 2003/04/10 12:53:24 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 23-Jul-01    Paul            VBM:2001070507 - Added this change history
 *                              and simplified by not creating StringBuffers
 *                              when returning a fixed string and also by
 *                              renaming all the parameters to attributes.
 * 11-Oct-01    Allan           VBM:2001090401 - Replaced TableDataCell and
 *                              TableHeaderCell Attributes with
 *                              TableCellAttributes.
 * 22-Jan-02    Doug            VBM:2002011003 - removed the methods openTitle
 *                              and closeTitle as doTitle is used to generate
 *                              the title element.
 * 31-Jan-02    Paul            VBM:2001122105 - Restructured to reflect
 *                              changes to protocols.
 * 28-Feb-02    Paul            VBM:2002022804 - Made methods consistent with
 *                              other StringProtocol based classes.
 * 13-Mar-02    Paul            VBM:2002030104 - Removed classic form methods.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 25-Apr-02    Paul            VBM:2002042202 - Moved from protocols and
 *                              made it generate a DOM.
 * 05-Jun-02    Adrian          VBM:2002021103 - Open KEEPTOGETHER_ELEMENT in
 *                              methods openTableBody and openDiv, and close in
 *                              methods closeTableBody and closeDiv
 * 10-Jun-02    Adrian          VBM:2002021103 - Changed keeptogether to
 *                              keepTogether.
 * 08-Aug-02    Allan           VBM:2002080703 - Complete the basic 
 *                              implementation of this protocol by supporting
 *                              openCanvas(), closeCanvas(), removing the
 *                              doImage() overide and overiding all open/close
 *                              grid and pane methods, doComment() and segment
 *                              methods to do nothing.
 * 13-Aug-02    Sumit           VBM:2002081317 - overrode doLineBreak() to
 *                              append <br> as a literal string in the dom 
 *                              rather than adding an element.
 * 13-Aug-02    Sumit           VBM:2002081314 - overrode doAnchor() and
 *                              addAnchorAttributes() to add HDML specfic
 *                              markup.
 * 13-Aug-02    Sumit           VBM:2002081318 - mimeType() now returns 
 *                              HDML content type
 * 10-Jan-03    Phil W-S        VBM:2002110402 - This protocol does not support
 *                              format optimization.
 * 15-Jan-03    Steve           VBM:2002120507 - This protocol does not support
 *                              forms. Removed overridden openForm() and 
 *                              closeForm() methods as these are only called 
 *                              by doForm(). Overrode doForm() to do nothing.
 *                              Any XFForm elements in a page will now be 
 *                              completely ignored.
 * 20-Jan-03    Geoff           VBM:2003011616 - Removed redundant 
 *                              supportsAccessKeyAttribute setting which was
 *                              just setting it to the same as the superclass.
 * 09-Apr-03    Phil W-S        VBM:2002111502 - Override
 *                              addPhoneNumberAttributes to ensure that no
 *                              attributes are added.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.protocols.AddressAttributes;
import com.volantis.mcs.protocols.AnchorAttributes;
import com.volantis.mcs.protocols.BigAttributes;
import com.volantis.mcs.protocols.BodyAttributes;
import com.volantis.mcs.protocols.BoldAttributes;
import com.volantis.mcs.protocols.CanvasAttributes;
import com.volantis.mcs.protocols.CodeAttributes;
import com.volantis.mcs.protocols.ColumnIteratorPaneAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DefinitionDataAttributes;
import com.volantis.mcs.protocols.DefinitionListAttributes;
import com.volantis.mcs.protocols.DefinitionTermAttributes;
import com.volantis.mcs.protocols.DivAttributes;
import com.volantis.mcs.protocols.EmphasisAttributes;
import com.volantis.mcs.protocols.GridAttributes;
import com.volantis.mcs.protocols.GridChildAttributes;
import com.volantis.mcs.protocols.GridRowAttributes;
import com.volantis.mcs.protocols.HeadingAttributes;
import com.volantis.mcs.protocols.ItalicAttributes;
import com.volantis.mcs.protocols.KeyboardAttributes;
import com.volantis.mcs.protocols.LineBreakAttributes;
import com.volantis.mcs.protocols.ListItemAttributes;
import com.volantis.mcs.protocols.MonospaceFontAttributes;
import com.volantis.mcs.protocols.NoScriptAttributes;
import com.volantis.mcs.protocols.OrderedListAttributes;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.ParagraphAttributes;
import com.volantis.mcs.protocols.PhoneNumberAttributes;
import com.volantis.mcs.protocols.PreAttributes;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.RowIteratorPaneAttributes;
import com.volantis.mcs.protocols.SampleAttributes;
import com.volantis.mcs.protocols.SegmentAttributes;
import com.volantis.mcs.protocols.SegmentGridAttributes;
import com.volantis.mcs.protocols.SmallAttributes;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.StrongAttributes;
import com.volantis.mcs.protocols.TableAttributes;
import com.volantis.mcs.protocols.TableBodyAttributes;
import com.volantis.mcs.protocols.TableCellAttributes;
import com.volantis.mcs.protocols.TableFooterAttributes;
import com.volantis.mcs.protocols.TableHeaderAttributes;
import com.volantis.mcs.protocols.TableRowAttributes;
import com.volantis.mcs.protocols.UnderlineAttributes;
import com.volantis.mcs.protocols.UnorderedListAttributes;
import com.volantis.mcs.protocols.XFActionAttributes;
import com.volantis.mcs.protocols.XFFormAttributes;

/**
 * Class: HDML_Version3
 *
 * A sub-class of the HTML root protocol class.
 *
 */
public class HDML_Version3
    extends HTMLRoot {

    /**
     * Constructor.
     */
    public HDML_Version3(ProtocolSupportFactory protocolSupportFactory,
            ProtocolConfiguration protocolConfiguration) {

        super(protocolSupportFactory, protocolConfiguration);
        supportsFormatOptimization = false;
    }

    // javadoc inherited.
    public void initialise() {
        super.initialise();

        supportsCSS = false;
    }

    public String defaultMimeType() {
        return "text/x-hdml";
    }

    public void openAnchor(DOMOutputBuffer dom, AnchorAttributes attributes)
            throws ProtocolException {
        String value;

        Element element = dom.openStyledElement ("a", attributes);

        if ((value = getLinkFromReference(attributes.getHref())) != null) {
            element.setAttribute("dest", value);
        }
        if (supportsAccessKeyAttribute
                && (value = getPlainText(attributes.getShortcut())) != null) {
            element.setAttribute("accesskey", value);
        }
        element.setAttribute("task","go");
        // Allow subclasses to add extra attributes to the anchor.
        addAnchorAttributes (element, attributes);
    }
    
    protected void addAnchorAttributes (Element element,
                                        AnchorAttributes attributes){

    }

    protected void addPhoneNumberAttributes(Element element,
                                            PhoneNumberAttributes attributes) {
        // Intentionally blank
    }

    /**
     * This is not supported in this protocol.
     */
    protected void addComment (DOMOutputBuffer dom,
                               String comment) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openGrid (DOMOutputBuffer dom,
                             GridAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeGrid (DOMOutputBuffer dom,
                              GridAttributes attributes) {
    }


    /**
     * This is not supported in this protocol.
     */
    protected void openGridChild (DOMOutputBuffer dom,
                                  GridChildAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeGridChild (DOMOutputBuffer dom,
                                   GridChildAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openGridRow (DOMOutputBuffer dom,
                                GridRowAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeGridRow (DOMOutputBuffer dom,
                                 GridRowAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openPane (DOMOutputBuffer dom,
                             PaneAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closePane (DOMOutputBuffer dom,
                              PaneAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected
        void openColumnIteratorPane (DOMOutputBuffer dom,
                                     ColumnIteratorPaneAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected
        void closeColumnIteratorPane (DOMOutputBuffer dom,
                                      ColumnIteratorPaneAttributes attributes){
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openColumnIteratorPaneElement (DOMOutputBuffer dom,
                                                  ColumnIteratorPaneAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeColumnIteratorPaneElement
        (DOMOutputBuffer dom, ColumnIteratorPaneAttributes attributes) {
        
    }

    /**
     * This is not supported in this protocol.
     */
    protected
        void openRowIteratorPane (DOMOutputBuffer dom,
                                  RowIteratorPaneAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected
        void closeRowIteratorPane (DOMOutputBuffer dom,
                                   RowIteratorPaneAttributes attributes) {
    }


    /**
     * This is not supported in this protocol.
     */
    protected void openRowIteratorPaneElement (DOMOutputBuffer dom,
                                               RowIteratorPaneAttributes attributes) {
    }


    /**
     * This is not supported in this protocol.
     */
    protected
        void closeRowIteratorPaneElement (DOMOutputBuffer dom,
                                          RowIteratorPaneAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openSegment (DOMOutputBuffer dom,
                                SegmentAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeSegment (DOMOutputBuffer dom,
                                 SegmentAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openSegmentGrid (DOMOutputBuffer dom,
                                    SegmentGridAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeSegmentGrid (DOMOutputBuffer dom,
                                     SegmentGridAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openBody (DOMOutputBuffer dom,
                             BodyAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeBody (DOMOutputBuffer dom,
                              BodyAttributes attributes) {
    }

    // javadoc inherited
    protected void openCanvas (DOMOutputBuffer dom,
                               CanvasAttributes attributes) {

        // @todo 2005060816 annotate child with style information if it's not inherited from the parent
        Element element = dom.openStyledElement("hdml", attributes);
        element.setAttribute("version", "3.0");

        dom.openElement("display");
    }

    // javadoc inherited
    protected void closeCanvas (DOMOutputBuffer dom,
                                CanvasAttributes attributes) {
        dom.closeElement("display");
        dom.closeElement("hdml");
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openHead (DOMOutputBuffer dom,
                             boolean empty) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeHead (DOMOutputBuffer dom,
                              boolean empty) {
    }

    protected void doProtocolString (Document document) {
        // This is not supported in this protocol.
    }

    /**
     * This is not supported in this protocol.
     */
    protected void doTitle (DOMOutputBuffer dom,
                            CanvasAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openAddress (DOMOutputBuffer dom,
                                AddressAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeAddress (DOMOutputBuffer dom,
                                 AddressAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    public void doForm (XFFormAttributes attributes) {
        // Do nothing. Forms are ignored in this protocol.
    }

    /**
     * This is not supported in this protocol.
     */
    public void doActionInput(DOMOutputBuffer dom,
                              XFActionAttributes attributes) {
        // Do nothing. Action inputs are not supported in this protocol.
        // Needs to be overridden in addition to doForm() as action inputs
        // can exist outside forms (with type="perform").
    }

    /**
     * This is not supported in this protocol.
     */
    public void openDiv (DOMOutputBuffer dom,
                            DivAttributes attributes) {
      if ("true".equals(attributes.getKeepTogether())) {
        dom.openElement(KEEPTOGETHER_ELEMENT);
      }
    }

    /**
     * This is not supported in this protocol.
     */
    public void closeDiv (DOMOutputBuffer dom,
                             DivAttributes attributes) {
      if ("true".equals(attributes.getKeepTogether())) {
        dom.closeElement(KEEPTOGETHER_ELEMENT);
      }
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openHeading1 (DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeHeading1 (DOMOutputBuffer dom,
                                  HeadingAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openHeading2 (DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeHeading2 (DOMOutputBuffer dom,
                                  HeadingAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openHeading3 (DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeHeading3 (DOMOutputBuffer dom,
                                  HeadingAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openHeading4 (DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeHeading4 (DOMOutputBuffer dom,
                                  HeadingAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openHeading5 (DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeHeading5 (DOMOutputBuffer dom,
                                  HeadingAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openHeading6 (DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeHeading6 (DOMOutputBuffer dom,
                                  HeadingAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openParagraph (DOMOutputBuffer dom,
                                  ParagraphAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeParagraph (DOMOutputBuffer dom,
                                   ParagraphAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openPre (DOMOutputBuffer dom,
                            PreAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closePre (DOMOutputBuffer dom,
                             PreAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected
        void openDefinitionData (DOMOutputBuffer dom,
                                 DefinitionDataAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected
        void closeDefinitionData (DOMOutputBuffer dom,
                                  DefinitionDataAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected
        void openDefinitionList (DOMOutputBuffer dom,
                                 DefinitionListAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected
        void closeDefinitionList (DOMOutputBuffer dom,
                                  DefinitionListAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected
        void openDefinitionTerm (DOMOutputBuffer dom,
                                 DefinitionTermAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected
        void closeDefinitionTerm (DOMOutputBuffer dom,
                                  DefinitionTermAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openListItem (DOMOutputBuffer dom,
                                 ListItemAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeListItem (DOMOutputBuffer dom,
                                  ListItemAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openOrderedList (DOMOutputBuffer dom,
                                    OrderedListAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeOrderedList (DOMOutputBuffer dom,
                                     OrderedListAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected
        void openUnorderedList (DOMOutputBuffer dom,
                                UnorderedListAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected
        void closeUnorderedList (DOMOutputBuffer dom,
                                 UnorderedListAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openTable (DOMOutputBuffer dom,
                              TableAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeTable (DOMOutputBuffer dom,
                               TableAttributes attributes) {
    }

    /**
     * Append the open table body markup to the specified StringBuffer.
     * @param dom The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openTableBody (DOMOutputBuffer dom,
                                  TableBodyAttributes attributes) {
      if ("true".equals(attributes.getKeepTogether())) {
        dom.openElement(KEEPTOGETHER_ELEMENT);
      }
    }

    /**
     * Append the close table body markup to the specified StringBuffer.
     * @param dom The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeTableBody (DOMOutputBuffer dom,
                                   TableBodyAttributes attributes) {
      if ("true".equals(attributes.getKeepTogether())) {
        dom.closeElement(KEEPTOGETHER_ELEMENT);
      }
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openTableDataCell (DOMOutputBuffer dom,
                                      TableCellAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeTableDataCell (DOMOutputBuffer dom,
                                       TableCellAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openTableFooter (DOMOutputBuffer dom,
                                    TableFooterAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeTableFooter (DOMOutputBuffer dom,
                                     TableFooterAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openTableHeader (DOMOutputBuffer dom,
                                    TableHeaderAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeTableHeader (DOMOutputBuffer dom,
                                     TableHeaderAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected
        void openTableHeaderCell (DOMOutputBuffer dom,
                                  TableCellAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected
        void closeTableHeaderCell (DOMOutputBuffer dom,
                                   TableCellAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openTableRow (DOMOutputBuffer dom,
                                 TableRowAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeTableRow (DOMOutputBuffer dom,
                                  TableRowAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openBig (DOMOutputBuffer dom,
                            BigAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeBig (DOMOutputBuffer dom,
                             BigAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openBold (DOMOutputBuffer dom,
                             BoldAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeBold (DOMOutputBuffer dom,
                              BoldAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openCode (DOMOutputBuffer dom,
                             CodeAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeCode (DOMOutputBuffer dom,
                              CodeAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openEmphasis (DOMOutputBuffer dom,
                                 EmphasisAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeEmphasis (DOMOutputBuffer dom,
                                  EmphasisAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openItalic (DOMOutputBuffer dom,
                               ItalicAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeItalic (DOMOutputBuffer dom,
                                ItalicAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openKeyboard (DOMOutputBuffer dom,
                                 KeyboardAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeKeyboard (DOMOutputBuffer dom,
                                  KeyboardAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected
        void openMonospaceFont (DOMOutputBuffer dom,
                                MonospaceFontAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected
        void closeMonospaceFont (DOMOutputBuffer dom,
                                 MonospaceFontAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openSample (DOMOutputBuffer dom,
                               SampleAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeSample (DOMOutputBuffer dom,
                                SampleAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openSmall (DOMOutputBuffer dom,
                              SmallAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeSmall (DOMOutputBuffer dom,
                               SmallAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openStrong (DOMOutputBuffer dom,
                               StrongAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeStrong (DOMOutputBuffer dom,
                                StrongAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openUnderline (DOMOutputBuffer dom,
                                  UnderlineAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeUnderline (DOMOutputBuffer dom,
                                   UnderlineAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openNoScript (DOMOutputBuffer dom,
                                 NoScriptAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeNoScript (DOMOutputBuffer dom,
                                  NoScriptAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    public void openSpan(DOMOutputBuffer dom, SpanAttributes attributes)
            throws ProtocolException {
    }

    /**
     * This is not supported in this protocol.
     */
    public void closeSpan(DOMOutputBuffer dom, SpanAttributes attributes) {
    }

    /**
     * The line break element needs to be <BR> without trailing
     * slash which is handled by the document writer.
     */
    protected void doLineBreak (DOMOutputBuffer dom,
                                LineBreakAttributes attributes) {
        dom.addElement ("br");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9363/1	emma	VBM:2005080405 Remove the 'supports multi class' properties

 09-Jun-05	8665/4	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 09-Jun-05	8665/2	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 16-Feb-05	6129/5	matthew	VBM:2004102019 yet another supermerge

 16-Feb-05	6129/3	matthew	VBM:2004102019 yet another supermerge

 23-Nov-04	6129/1	matthew	VBM:2004102019 Enable shortcut menu link rendering

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Oct-04	5816/1	byron	VBM:2004101318 Support style classes: Runtime XHTMLBasic

 08-Jul-04	4835/1	adrianj	VBM:2003040704 HDML protocol modified to ignore xfaction inputs outside forms

 17-May-04	4029/4	steve	VBM:2004042003 Supermerged again - Hurry up Mat :)

 26-Apr-04	4029/1	steve	VBM:2004042003 support hr element in netfront3 and MIB

 11-May-04	4217/1	geoff	VBM:2004042807 Enhance Menu Support: Renderers: Menu Markup

 06-May-04	4174/1	claire	VBM:2004050501 Enhanced Menus: (X)HTML menu renderer selectors and protocol integration

 12-Feb-04	2958/1	philws	VBM:2004012715 Add protocol.content.type device policy

 ===========================================================================
*/

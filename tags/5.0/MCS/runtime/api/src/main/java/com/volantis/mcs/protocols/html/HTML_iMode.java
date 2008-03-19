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
* $Header: /src/voyager/com/volantis/mcs/protocols/html/HTML_iMode.java,v 1.16 2003/04/17 10:21:07 geoff Exp
* ----------------------------------------------------------------------------
* (c) Volantis Systems Ltd 2000. 
* ----------------------------------------------------------------------------
* Change History:
*
* Date         Who             Description
* ---------    --------------- -----------------------------------------------
* 09-Jul-01    Paul            VBM:2001062810 - Added this header and fixed
*                              the code so that it only gets values out of
*                              attributes once and uses append for each
*                              separate part of the output string instead of
*                              inline string concatenation.
* 23-Jul-01    Paul            VBM:2001070507 - Simplified by not creating
*                              StringBuffers when returning a fixed string
*                              and also by renaming all the parameters to
*                              attributes.
* 26-Jul-01    Paul            VBM:2001071705 - Wrote the value associated
*                              with the vt:textarea tag into the body of the
*                              <textarea> tag.
* 26-Sep-01    Payal           VBM:2001091006 - Modified method openTextArea
*                              to get the rows and cols value from the style
*                              and not from the tag.
* 11-Oct-01    Allan           VBM:2001090401 - TableDataCell and
*                              TableHeaderCell type methods changed to use
*                              TableCellAttributes.
* 29-Oct-01    Paul            VBM:2001102901 - Remove Theme parameter from
*                              the ...NoScript methods.
* 31-Jan-02    Paul            VBM:2001122105 - Restructured to reflect
*                              changes to protocols.
* 28-Feb-02    Paul            VBM:2002022804 - Made methods consistent with
*                              other StringProtocol based classes.
* 13-Mar-02    Steve           VBM:2002021119 - Overrode the table and grid
*                              methods from XHTMLBasic as iHTML does not
*                              support tables in any shape or form.
*                              Overrode XHTML Basic methods to ensure that
*                              tags with the correct parameters are written.
*                              The paragraph tag may have a <font> tag
*                              inserted if the theme defines coloured text.
*                              The <body> tag has bgcolor and text attributes
*                              set if they are defined within the Theme.
* 13-Mar-02    Steve           VBM:2002021119 - Overrode the table and grid
*                              methods from XHTMLBasic as iHTML does not
*                              support tables in any shape or form.
*                              Overrode XHTML Basic methods to ensure that
*                              tags with the correct parameters are written.
*                              The paragraph tag may have a <font> tag
*                              inserted if the theme defines coloured text.
*                              The <body> tag has bgcolor and text attributes
*                              set if they are defined within the Theme.
* 13-Mar-02    Paul            VBM:2002030104 - Removed classic form methods.
* 14-Mar-02    Steve           VBM:2002021119 - Support for the istyle
*                              attribute in xftextinput and xftextarea. This
*                              allows for kana language input and is denoted
*                              by the volantis inputMode attribute.
* 14-Mar-02    Steve           VBM:2002021119 - Revamped to use the super
*                              classes as much as possible.
* 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
*                              class to string.
* 25-Apr-02    Paul            VBM:2002042202 - Moved from protocols and
*                              made it generate a DOM.
* 26-Apr-02    Paul            VBM:2002042205 - Overrode checkPaneRendering
*                              to prevent a pane from having a table wrapped
*                              around it.
* 02-May-02    Adrian          VBM:2002040808 - Added support for new
*                              implementation of CCS emulation.
* 05-Jun-02    Adrian          VBM:2002021103 - Open KEEPTOGETHER_ELEMENT in
*                              method openTableBody , and close in method
*                              closeTableBody
* 07-Jun-02    Byron           VBM:2002042407 - Modified openGridRow(),
*                              closeGridRow(), openGridChild() and
*                              closeGridChild() to separate rows using a
*                              paragraph separator (same as XHMLBasic if Nx1
*                              grid removed).
* 10-Jun-02    Adrian          VBM:2002021103 - Changed keeptogether to
*                              keepTogether.
* 11-Jun-02    Steve           VBM:2002040807 - Added mariner to protocol element
*                              mappings in the initialise method
* 27-Jun-02    Byron           VBM:2002062501 - Added getDissector() method
*                              and dissector attribute.
* 12-Jul-02    Byron           VBM:2002071204 - Removed the method
*                              createUniqueRuntimeStyle().
* 26-Jul-02    Steve           VBM:2002040807 - moved the protocol element
*                              mapping initialisation into the class
*                              constructor and out of initialise() so that
*                              it only gets called once.
* 16-Dec-02    Adrian          VBM:2002100203 - Added more comprehensive
*                              javadoc to constructor where elementMappings
*                              field is populated.
* 10-Jan-03    Phil W-S        VBM:2002110402 - This protocol does not support
*                              format optimization.
* 20-Jan-03    Geoff           VBM:2003011616 - Removed redundant
*                              supportsAccessKeyAttribute setting which was
*                              just setting it to the same as the superclass,
*                              usual IDEA cleanup.
* 29-Jan-03    Byron           VBM:2003012803 - Modified constructor to set
*                              protocolConfiguration value and any static
*                              variables dependent on it.
* 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
*                              statements in if(logger.isDebugEnabled()) block
* 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for
*                              ProtocolException where necessary.
* ----------------------------------------------------------------------------
*/

package com.volantis.mcs.protocols.html;

import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.AddressAttributes;
import com.volantis.mcs.protocols.BigAttributes;
import com.volantis.mcs.protocols.BodyAttributes;
import com.volantis.mcs.protocols.BoldAttributes;
import com.volantis.mcs.protocols.CiteAttributes;
import com.volantis.mcs.protocols.CodeAttributes;
import com.volantis.mcs.protocols.ColumnIteratorPaneAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMTransformer;
import com.volantis.mcs.protocols.EmphasisAttributes;
import com.volantis.mcs.protocols.GridAttributes;
import com.volantis.mcs.protocols.GridChildAttributes;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.ItalicAttributes;
import com.volantis.mcs.protocols.KeyboardAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.MonospaceFontAttributes;
import com.volantis.mcs.protocols.NoScriptAttributes;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.RowIteratorPaneAttributes;
import com.volantis.mcs.protocols.SampleAttributes;
import com.volantis.mcs.protocols.SmallAttributes;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.StrongAttributes;
import com.volantis.mcs.protocols.TableAttributes;
import com.volantis.mcs.protocols.TableCellAttributes;
import com.volantis.mcs.protocols.TableRowAttributes;
import com.volantis.mcs.protocols.UnderlineAttributes;
import com.volantis.mcs.protocols.XFFormAttributes;
import com.volantis.mcs.protocols.XFTextInputAttributes;
import com.volantis.mcs.protocols.html.htmlimode.HTMLiModeDivHorizontalAlignKeywordMapper;
import com.volantis.mcs.protocols.styles.DefaultingPropertyHandler;
import com.volantis.mcs.protocols.styles.KeywordValueHandler;
import com.volantis.mcs.protocols.styles.NoopPropertyUpdater;
import com.volantis.mcs.protocols.styles.PropertyClearer;
import com.volantis.mcs.protocols.styles.PropertyHandler;
import com.volantis.mcs.protocols.styles.ValueHandlerToPropertyAdapter;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.HashMap;

/**
 *
 * This is a sub-class of the HTMLRoot protocol class to provide the precise
 * definition of the HTML_iMode protocol.
 *
 */

public class HTML_iMode
        extends HTMLRoot {

    /**
     * A hashmap of the input modes supported by imode and their numeric
     * equivalents for the istyle attribute in input tags.
     */
    private final HashMap inputModes;

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(HTML_iMode.class);


    /**
     * Flag indicating whether native table support is available on
     * the requesting device.
     */
    protected boolean deviceTablesCapable = false;

    private PropertyHandler bodyBackgroundColorHandler;
    private PropertyHandler bodyColorHandler;

    public HTML_iMode(
            ProtocolSupportFactory supportFactory,
            ProtocolConfiguration configuration) {

        super(supportFactory, configuration);

        supportsScripts = false;        
        supportsExternalStyleSheets = false;
        supportsInlineStyles = false;

        // Generate tid= rather than classid=

        supportsImageButtons = false;

        inputModes = new HashMap();
        inputModes.put("katakana", "1");
        inputModes.put("hiragana", "2");
        inputModes.put("latin", "3");
        inputModes.put("latin digits", "4");

        supportsDissectingPanes = true;

        optimizingTransformer = null;

        supportsBackgroundInTable = false;

        supportsBackgroundInTD = false;

        supportsBackgroundInTH = false;

        // Set the protocol default for nested table support to false.
        // This may be updated by the initialise method depending on the
        // value in the device database.
        supportsNestedTables = false;

    }

    /**
     * Initialise the protocol. This is called after the context has been
     * initialised so it can be queried for information.
     */
    public void initialise() {
        super.initialise();

        // iMode doesn't support CSS.
        supportsCSS = false;

        deviceTablesCapable =
                context.getBooleanDevicePolicyValue(DevicePolicyConstants.
                UAPROF_TABLES_CAPABLE);
        if (logger.isDebugEnabled()) {
            logger.debug("UAProf.TablesCapable = " + deviceTablesCapable);
        }

        // If the device is tables capable then a format optimizing transformer is
        // applied
        supportsFormatOptimization = deviceTablesCapable;

    }

    // Javadoc inherited.
    protected void initialiseStyleHandlers() {
        super.initialiseStyleHandlers();

        // Initialise the style value handlers.
        horizontalAlignChecker = new ValueHandlerToPropertyAdapter(
                StylePropertyDetails.TEXT_ALIGN,
                new KeywordValueHandler(
                        HTMLiModeDivHorizontalAlignKeywordMapper.
                                getDefaultInstance()),
                NoopPropertyUpdater.getDefaultInstance());

        horizontalAlignHandler = new ValueHandlerToPropertyAdapter(
                StylePropertyDetails.TEXT_ALIGN,
                new KeywordValueHandler(
                        HTMLiModeDivHorizontalAlignKeywordMapper.
                                getDefaultInstance()),
                PropertyClearer.getDefaultInstance());

        // If no color is specified on body then default to black for text and
        // white for background.
        bodyColorHandler = new DefaultingPropertyHandler(
                colorHandler, "#000000");
        bodyBackgroundColorHandler = new DefaultingPropertyHandler(
                backgroundColorHandler, "#ffffff");
    }

    /**
     * Override to get the right transformer depending on the
     * deviceTablesCapable flag.
     *
     * @todo later refactor getDOMTransformer so that it can return a transformer set which will be used to add the transformers, rather than adding them here
     */
    protected DOMTransformer getDOMTransformer() {

        // Add the transformers to the transformer set in order.
        DOMTransformer nonStyleTransformer;
        if (deviceTablesCapable) {
            // This is a format optimizing transformer variant
            nonStyleTransformer =
                    new HTML_iModeUnabridgedTransformer(protocolConfiguration);
        } else {
            // This is not a format optimizing transformer
            nonStyleTransformer = new HTML_iModeTableEmulationTransformer();
        }
        addDOMTransformer(nonStyleTransformer);
        addDOMTransformer(new HTML_iModeDivRemovingTransformer());

        // Should only return null until this method has been refactored to
        // return the set of transformers that are required by this protocol.
        return null;
    }

    protected void addCoreAttributes(Element element,
                                     MCSAttributes attributes,
                                     boolean title) {
    }

    // Javadoc inherited.
    protected void doProtocolString(Document document) {
        // No DOCTYPE header is required 
    }

    /**
     * Allow subclasses to add extra attributes to a body.
     * @param element The Element to modify.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void addBodyAttributes(Element element,
                                     BodyAttributes attributes) {

        Styles styles = attributes.getStyles();
        String value;

        value = bodyColorHandler.getAsString(styles);
        if (value != null) {
            element.setAttribute("text", value);
        }

        value = bodyBackgroundColorHandler.getAsString(styles);
        if (value != null) {
            element.setAttribute("bgcolor", value);
        }
    }

    // Javadoc inherited from super class.
    protected void openPane(DOMOutputBuffer dom,
                            PaneAttributes attributes) {
        // @todo 2005060816 annotate child with style information if it's not inherited from the parent
        super.openPane(dom, attributes);

        if (!deviceTablesCapable) {
            Styles styles = attributes.getStyles();            
            
            String value = horizontalAlignChecker.getAsString(styles);
            if (value != null) {
                StylingFactory factory = StylingFactory.getDefaultInstance();
                // Use the styles from the pane attributes. The styles
                // generated for the table elements (in super#openPane) will
                // only include the property values that are inherited when the
                // display property is one of the table keywords. Given that
                // we know we don't support tables and they will be converted
                // into p/div tags anyway, we might as well get the right
                // inherited styles here.
                Styles divStyles = factory.createInheritedStyles(styles,
                        DisplayKeywords.BLOCK);
                Element element = dom.openStyledElement("div", divStyles);
                element.setAttribute("align", value);
            }
        }
    }

    // Javadoc inherited from super class.
    protected void closePane(DOMOutputBuffer dom,
                             PaneAttributes attributes) {
        if (!deviceTablesCapable) {
            Styles styles = attributes.getStyles();

            String value = horizontalAlignChecker.getAsString(styles);
            if (value != null) {
                dom.closeElement("div");
            }
        }

        super.closePane(dom, attributes);
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openAddress(DOMOutputBuffer dom,
                               AddressAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeAddress(DOMOutputBuffer dom,
                                AddressAttributes attributes) {
    }

    /**
     * Add general event attributes of which there are none in HTML_iMode.
     *
     * @param element    The Element to modify.
     * @param attributes The attributes object which contains the events.
     */
    protected void addGeneralEventAttributes(Element element,
                                           MCSAttributes attributes)
            throws ProtocolException {
    }

    // Javadoc inherited from super class.
    protected void addColumnIteratorPaneAttributes(Element element,
                                                   ColumnIteratorPaneAttributes attributes) {

        if (deviceTablesCapable) {
            Styles styles = attributes.getStyles();
            String value = backgroundColorHandler.getAsString(styles);
            if (value != null) {
                element.setAttribute("bgcolor", value);
            }

            if (attributes.getPane() != null) {
                addOptimizeAttribute(element, attributes.getPane());
            }

            addPaneTableAttributes(element, attributes);
        }
    }

    // Javadoc inherited from super class.
    protected void addGridAttributes(Element element,
                                     GridAttributes attributes) {
        if (deviceTablesCapable) {
            Styles styles = attributes.getStyles();
            String value;

            if ((value = backgroundColorHandler.getAsString(styles)) != null) {
                element.setAttribute("bgcolor", value);
            }

            // Add the border, cellpadding and cellspacing attributes
            addTableAttributes(element, attributes);

            // set width from the grid
            if ((value = widthHandler.getAsString(styles)) != null) {
                element.setAttribute("width", value);
            }
        }
    }

    // Javadoc inherited from super class.
    protected void addGridChildAttributes(Element element,
                                          GridChildAttributes attributes) {

    }

    // Javadoc inherited from super class.
    protected void addTableAttributes(Element element,
                                      TableAttributes attributes)
            throws ProtocolException {

        if (deviceTablesCapable) {
            super.addTableAttributes(element, attributes);
            Styles styles = attributes.getStyles();
            String value;

            // Only add the border attribute to the table if the device does
            // not allow this effect to be achieved using CSS.
            // @todo should this check should be performed before adding attributes for other properties that can be rendered using CSS? (i.e. bgcolor)
            if (!supportsCSS) {
                if ((value = borderHandler.getAsString(styles)) != null) {
                    element.setAttribute("border", value);
                }
            }
            if ((value = backgroundColorHandler.getAsString(styles)) != null) {
                element.setAttribute("bgcolor", value);
            }
            if ((value = borderSpacingHandler.getAsString(styles)) != null) {
                element.setAttribute("cellspacing", value);
            }
        }
    }

    /**
     * Add deprecated stylistic attributes that remain on the tag of
     * table row type tags. Tag attributes should override style attributes.
     * @param element The Element to modify.
     * @param attributes <code>TableRowAttributes</code> to add to the tag
     */
    protected void addTableRowAttributes(Element element,
                                         TableRowAttributes attributes)
            throws ProtocolException {

        if (deviceTablesCapable) {
            Styles styles = attributes.getStyles();
            String value;

            // Add the super class's attributes.
            super.addTableRowAttributes(element, attributes);

            if ((value = verticalAlignHandler.getAsString(styles)) != null) {
                element.setAttribute("valign", value);
            }
        }
    }

    /**
     * Add deprecated stylistic attributes that remain on the tag of table cell
     * type tags. Tag attributes should override style attributes.
     *
     * @param element    The Element to modify.
     * @param attributes <code>TableCellAttributes</code> to add to the tag
     */
    protected void addTableCellAttributes(Element element,
                                          TableCellAttributes attributes)
            throws ProtocolException {
        if (deviceTablesCapable) {

            Styles styles = attributes.getStyles();
            String value;

            // set colspan from layout
            if ((value = attributes.getColSpan()) != null) {
                element.setAttribute("colspan", value);
            }

            // set rowspan from layout
            if ((value = attributes.getRowSpan()) != null) {
                element.setAttribute("rowspan", value);
            }

            if ((value = backgroundColorHandler.getAsString(styles)) != null) {
                element.setAttribute("bgcolor", value);
            }

            if ((value = horizontalAlignHandler.getAsString(styles)) != null) {
                element.setAttribute("align", value);
            }

            if ((value = verticalAlignHandler.getAsString(styles)) != null) {
                element.setAttribute("valign", value);
            }
        }
    }

    // javadoc inherited
    protected void addPaneCellAttributes(Element element,
                                         Styles styles) {
        if (deviceTablesCapable) {
            String value;

            value = horizontalAlignHandler.getAsString(styles);
            if (value != null) {
                element.setAttribute("align", value);
            }
        }
    }

    // javadoc inherited
    protected void addPaneTableOrCellAttributes(Element element,
                                                PaneAttributes attributes,
                                                boolean table) {
        if (deviceTablesCapable) {
            Styles styles = attributes.getStyles();
            String value = backgroundColorHandler.getAsString(styles);
            if (value != null) {
                element.setAttribute("bgcolor", value);
            }

            // add width
            addPaneTableOrCellWidth(element, attributes, table);
        }
    }

    // Javadoc inherited from super class.
    protected void addRowIteratorPaneAttributes(
            Element element,
            RowIteratorPaneAttributes attributes) {
        if (deviceTablesCapable) {
            Styles styles = attributes.getStyles();
            String value = backgroundColorHandler.getAsString(styles);
            if (value != null) {
                element.setAttribute("bgcolor", value);
            }

            if (attributes.getPane() != null) {
                addOptimizeAttribute(element, attributes.getPane());
            }

            addPaneTableAttributes(element, attributes);
        }
    }

    // javadoc inherited
    protected void addRowIteratorPaneCellAttributes(
            Element element,
            RowIteratorPaneAttributes attributes) {
    }

    // Javadoc inherited from super class.
    protected void addColumnIteratorPaneElementAttributes(
            Element element,
            ColumnIteratorPaneAttributes attributes) {
    }


    /**
     * This is not supported in this protocol.
     */
    protected void openCite(DOMOutputBuffer dom,
                            CiteAttributes attributes) {
        openStyleMarker(dom, attributes);
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeCite(DOMOutputBuffer dom,
                             CiteAttributes attributes) {
        closeStyleMarker(dom);
    }


    /**
     * This is not supported in this protocol.
     */
    public void openSpan(DOMOutputBuffer dom,
                         SpanAttributes attributes) {
        openStyleMarker(dom, attributes);
    }

    /**
     * This is not supported in this protocol.
     */
    public void closeSpan(DOMOutputBuffer dom,
                          SpanAttributes attributes) {
        closeStyleMarker(dom);
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openBig(DOMOutputBuffer dom,
                           BigAttributes attributes) {
        openStyleMarker(dom, attributes);
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeBig(DOMOutputBuffer dom,
                            BigAttributes attributes) {
        closeStyleMarker(dom);
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openBold(DOMOutputBuffer dom,
                            BoldAttributes attributes) {
        openStyleMarker(dom, attributes);
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeBold(DOMOutputBuffer dom,
                             BoldAttributes attributes) {
        closeStyleMarker(dom);
    }

    // Javadoc inherited from super class.
    protected void openCode(DOMOutputBuffer dom,
                            CodeAttributes attributes) {
        dom.openStyledElement("plaintext", attributes);
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeCode(DOMOutputBuffer dom,
                             CodeAttributes attributes) {
        dom.closeElement("plaintext");
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openEmphasis(DOMOutputBuffer dom,
                                EmphasisAttributes attributes) {
        openStyleMarker(dom, attributes);
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeEmphasis(DOMOutputBuffer dom,
                                 EmphasisAttributes attributes) {
        closeStyleMarker(dom);
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openItalic(DOMOutputBuffer dom,
                              ItalicAttributes attributes) {
        openStyleMarker(dom, attributes);
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeItalic(DOMOutputBuffer dom,
                               ItalicAttributes attributes) {
        closeStyleMarker(dom);
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openKeyboard(DOMOutputBuffer dom,
                                KeyboardAttributes attributes) {
        dom.openStyledElement("plaintext", attributes);
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeKeyboard(DOMOutputBuffer dom,
                                 KeyboardAttributes attributes) {
        dom.closeElement("plaintext");
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openMonospaceFont(DOMOutputBuffer dom,
                                     MonospaceFontAttributes attributes) {
        dom.openStyledElement("plaintext", attributes);
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeMonospaceFont(DOMOutputBuffer dom,
                                      MonospaceFontAttributes attributes) {
        dom.closeElement("plaintext");
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openSample(DOMOutputBuffer dom,
                              SampleAttributes attributes) {
        dom.openStyledElement("plaintext", attributes);
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeSample(DOMOutputBuffer dom,
                               SampleAttributes attributes) {
        dom.closeElement("plaintext");
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openSmall(DOMOutputBuffer dom,
                             SmallAttributes attributes) {
        openStyleMarker(dom, attributes);
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeSmall(DOMOutputBuffer dom,
                              SmallAttributes attributes) {
        closeStyleMarker(dom);
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openStrong(DOMOutputBuffer dom,
                              StrongAttributes attributes) {
        openStyleMarker(dom, attributes);
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeStrong(DOMOutputBuffer dom,
                               StrongAttributes attributes) {
        closeStyleMarker(dom);
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openUnderline(DOMOutputBuffer dom,
                                 UnderlineAttributes attributes) {
        openStyleMarker(dom, attributes);
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeUnderline(DOMOutputBuffer dom,
                                  UnderlineAttributes attributes) {
        closeStyleMarker(dom);
    }

    /**
     * Method to allow subclasses to specify additional attributes for
     * the image element
     * @param element The Element to modify.
     * @param attributes the attributes
     */
    protected void addImageAttributes(Element element,
                                      ImageAttributes attributes) {
        String value;
        if ((value = attributes.getVSpace()) != null) {
            element.setAttribute("vspace", value);
        }
        if ((value = attributes.getHSpace()) != null) {
            element.setAttribute("hspace", value);
        }
        if ((value = attributes.getAlign()) != null) {
            element.setAttribute("align", value);
        }
    }


    /**
     * This is not supported in this protocol.
     */
    protected void openNoScript(DOMOutputBuffer dom,
                                NoScriptAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeNoScript(DOMOutputBuffer dom,
                                 NoScriptAttributes attributes) {
    }

    /**
     * Method to allow subclasses to specify additional attributes for
     * the a form  element. Overridden because XHTML transitional adds the name
     * attribute which is not allowed in iHTML
     * @param element The Element to modify.
     * @param attributes the attributes
     */
    protected void addXFFormAttributes(Element element,
                                       XFFormAttributes attributes) {
    }

    /**
     * Add extra attributes to the text input tag.
     * This version sets the istyle attribute from the Volantis inputMode attribute
     * but only if the input type is "text"
     * @param element The Element to modify.
     * @param attributes <code>XFTextInputAttributes</code> for the text
     * input form field.
     */
    protected void addTextInputAttributes(Element element,
                                          XFTextInputAttributes attributes) {
        String value;

        // istyle is ONLY valid for "text" input... Not for "password" etc
        if (logger.isDebugEnabled()) {
            logger.debug("Adding text input attributes.");
        }
        if ("text".equals(attributes.getType())) {
            if (logger.isDebugEnabled()) {
                logger.debug("Type is text");
            }
            if ((value = attributes.getInputMode()) != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("input mode is " + value);
                }
                String numeric = (String) inputModes.get(value);
                if (logger.isDebugEnabled()) {
                    logger.debug("Numeric input mode is " + numeric);
                }
                if (numeric != null) {
                    element.setAttribute("istyle", numeric);
                }
            }
        }
    }

    /**
     * Add extra attributes to the text area tag.
     * @param element The Element to modify.
     * @param attributes <code>XFTextInputAttributes</code> for the text
     * input form field.
     */
    protected void addTextAreaAttributes(Element element,
                                         XFTextInputAttributes attributes) {
        addTextInputAttributes(element, attributes);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10641/1	geoff	VBM:2005113024 Pagination page rendering issues

 06-Dec-05	10621/1	geoff	VBM:2005113024 Pagination page rendering issues

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 11-Nov-05	10284/2	emma	VBM:2005110906 Bug fix: partial box border from theme being rendered as complete box

 28-Oct-05	10024/1	geoff	VBM:2005102406 Height attributes incorrectly rendered

 28-Oct-05	10020/1	geoff	VBM:2005102406 Height attributes incorrectly rendered

 11-Nov-05	10293/1	emma	VBM:2005110906 Forward port: Bug fix: partial box border from theme being rendered as complete box

 11-Nov-05	10284/2	emma	VBM:2005110906 Bug fix: partial box border from theme being rendered as complete box

 28-Oct-05	10020/1	geoff	VBM:2005102406 Height attributes incorrectly rendered

 30-Sep-05	9637/3	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 14-Sep-05	9472/3	ibush	VBM:2005090808 Add default styling for sub/sup elements

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9363/4	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/1	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/5	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 22-Aug-05	9184/2	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 19-Aug-05	9289/6	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 18-Aug-05	9007/3	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 12-Jul-05	8990/2	pcameron	VBM:2005052606 Allow devices to override protocol optimisation of tables

 23-Jun-05	8833/1	pduffin	VBM:2005042901 Addressing review comments

 09-Jun-05	8665/4	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 09-Jun-05	8665/2	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 31-May-05	8591/1	philws	VBM:2005052311 Provide improved layout and theme based table attribute handling

 24-May-05	8491/1	tom	VBM:2005052311 Supermerge to MCS Mainline

 24-May-05	8489/1	tom	VBM:2005052311 Added width to tables and font colour to <a>

 18-May-05	8273/1	tom	VBM:2004091703 Added Stylistic Blink Support to iMode

 22-Apr-05	7791/1	philws	VBM:2005040113 Port openPane changes from 3.3

 22-Apr-05	7746/1	philws	VBM:2005040113 Correct pane rendering where width and/or alignment are specified

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 22-Nov-04	6183/3	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 19-Oct-04	5816/1	byron	VBM:2004101318 Support style classes: Runtime XHTMLBasic

 12-Oct-04	5778/3	adrianj	VBM:2004083106 Provide styling engine API

 11-Oct-04	5773/9	tom	VBM:2004093007 created an i-mode transmapper to resolve align disappearance problem after table optimisation

 28-Sep-04	5661/5	tom	VBM:2004091403 Added style emulation to i-mode and fixed bgcol in cells

 21-Sep-04	5551/3	tom	VBM:2004090611 Added div align for i-mode phones

 20-Jul-04	4897/1	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 02-Jul-04	4803/1	adrianj	VBM:2003041504 Fixed optimization attribute propagation for column iterator panes

 06-May-04	4153/1	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 26-Feb-04	3233/1	geoff	VBM:2004021609 styleclasses incorrectly rendered when within a region

 25-Feb-04	3179/3	geoff	VBM:2004021609 styleclasses incorrectly rendered when within a region

 24-Feb-04	3179/1	geoff	VBM:2004021609 styleclasses incorrectly rendered when within a region

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 14-Oct-03	1542/17	allan	VBM:2003101101 Remove non-iMode stylistic attributes from tables and table cells - ported

 14-Oct-03	1540/17	allan	VBM:2003101101 Remove non-iMode stylistic attributes from tables and table cells.

 13-Oct-03	1542/1	allan	VBM:2003101101 HTML_iMode table handling patched from Proteus2

 13-Oct-03	1540/6	allan	VBM:2003101101 Use UAPROF_TABLES_CAPABLE constant

 12-Oct-03	1540/4	allan	VBM:2003101101 Fixes for implementation review

 12-Oct-03	1540/2	allan	VBM:2003101101 Add emulated and native table support on HTML_iMode

 ===========================================================================
*/

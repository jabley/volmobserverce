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
 * $Header: /src/voyager/com/volantis/mcs/protocols/wml/WapTV5_WMLVersion1_3.java,v 1.47 2003/04/24 16:43:22 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-Jul-02    Ian             VBM:2002072603 - Created from
 *                              WapTV5_WMLVersion1.2.java
 * 31-Jul-02    Paul            VBM:2002073008 - Made compatible with changes
 *                              made to WMLRoot.
 * 06-Aug-02    Sumit           VBM:2002080509 - Addes support for <td> and
 *                              <card> on event handlers
 * 06-Aug-02    Paul            VBM:2002080604 - Added support for target
 *                              attribute.
 * 06-Aug-02    Ian             VBM:2002080603 - Support new WapTV stylistic
 *                              properties.
 * 06-Aug-02    Paul            VBM:2002080509 - Rewrote the event code to
 *                              get the scripts from the EventAttributes.
 * 07-Aug-02    Paul            VBM:2002080701 - Added support for generating
 *                              the mode in the card.
 * 07-Aug-02    Paul            VBM:2002080509 - Moved onenter processing
 *                              before noop processing, otherwise it would only
 *                              occur if there was something in noop buffer.
 * 13-Aug-02    Paul            VBM:2002080603 - Indicated that this protocol
 *                              supports img dimensions, prevented duplicate
 *                              onenter events being generated, prevent
 *                              unnecessary attributes being written out on
 *                              table elements, remove dependency on GUI
 *                              package, only generate a table for pane if it
 *                              is not already within a td.
 * 15-Aug-02    Paul            VBM:2002081421 - Renamed getActionInputType
 *                              to getActionDoType to match changes made in
 *                              WMLRoot and made sure that actions are always
 *                              rendered as do elements.
 * 20-Aug-02    Adrian          VBM:2002081316 - Updated doImage to prevent
 *                              output of image tag if source is null.
 * 11-Sep-02    Steve           VBM:2002040809 - Updated to use the getPaneStyle
 *                              method to get styles from the theme/layout
 *                              for pane rendering.
 * 16-Sep-02    Ian             VBM:2002091006 - Added marinerFocus attributes
 *                              to anchors, menuItems and xfActions.
 * 18-Sep-02    Ian             VBM:2002091705 - Added openSpan and closeSpan
 *                              methods to call openFont and closeFont
 *                              respectively to enable fonts to span text.
 * 27-Sep-02    Ian             VBM:2002092505 - Updated addTableRowAttributes
 *                              to add height attribute if defined in style.
 * 01-Oct-02    Allan           VBM:2002091804 - openColumnIteratorPane(),
 *                              openPane() and openRowIteratorPane() modified
 *                              to use getHorizontalCellPadding() and
 *                              getVerticalCellPadding() instead of
 *                              getCellPadding() for vPad and hPad.
 * 01-Oct-02    Adrian          VBM:2002092304 - Updated addTableAttributes to
 *                              stop it writing align values as these can be
 *                              assigned to the td in this protocol.  Overrode
 *                              openTableDataCell to prevent td aligns being
 *                              added to TableStatus object which would result
 *                              in align values being added to enclosing table.
 * 04-Oct-02    Allan           VBM:2002100206 - Removed unchecked first call
 *                              to addFontFamily() in openFont(). Updated
 *                              methods openHeading1() to openHeading6() to
 *                              remove the unused call to getStyle().
 * 11-Oct-02    Sumit           VBM:2002101005 - Conditional added in
 *                              openLayout() and closeLayout() to test if
 *                              this canvas is an overlay
 *                              || includingPageContext is null. If so
 *                              generate <p>
 * 16-Oct-02    Sumit           VBM:2002101501 - Added addGridChildAttributes
 *                              to add width and height. Called from
 *                              openGridChild
 * 17-Oct-02    Sumit           VBM:2002101504 - added a test for title being
 *                              null in doSelectInput before setting it to be
 *                              value of prompt
 * 30-Oct-02    Sumit           VBM:2002101702 - changed doSelectInput() to
 *                              generate select or input types by liststyle
 *                              Added handleOptionListDefault() recursive
 *                              function to handle optgroups
 * 22-Nov-02	Mat		        VBM:2002101503 - added openFont() and
 *              				closeFont() tags in doSelectInput()
 * 02-Dec-02    Sumit           VBM:2002101505 - OpenFont verifies if the
 *                              font element has attributes before adding it.
 *                              closeFont only closes font element if it exists
 * 17-Dec-02    Adrian          VBM:2002100313 - updated getTextInputFormat to
 *                              check if style.getMarinerRows() is null before
 *                              attempting to convert the value to an int. Also
 *                              this method previously returned the int value
 *                              as a String by calling String.valueOf(int). The
 *                              String value was already known since that was
 *                              how it was retrieved from the Style so this
 *                              value is returned instead.
 * 18-Dec-02    Adrian          VBM:2002110106 - Updated doAnchor to call
 *                              addDoAttributes.
 * 13-Jan-03    Steve           VBM:2002112101 - If pane is not supplied as an
 *                              attribute to doImage then get the current pane
 *                              from the page context.
 * 13-Jan-02    Allan           VBM:2002120209 - doSelectInput modified to
 *                              not support accesskey.
 * 14-Jan-03    Phil W-S        VBM:2002110402 - Add support for the Grid and
 *                              Pane optimization level attributes. This adds
 *                              the addOptimizeAttribute method. Also affects
 *                              getDOMTransformer, openPane, openGrid,
 *                              openColumnIteratorPane and openRowIteratorPane
 *                              methods.
 * 22-Jan-03    Doug            VBM:2002120213 - Rewrote the doSelectInput()
 *                              method. This involved removing numerous
 *                              supporting methods.
 *                              Added the inner SelectionRender classes.
 * 23-Jan-03    Doug            VBM:2003012304 - Fully qualified all access
 *                              to the VolantisProtocol mehtods from the
 *                              SelectionRenderer inner classes.
 * 27-Jan-03    Doug            VBM:2002120213 - Ensured that the
 *                              SelectionRender inner classes are correctly
 *                              initialised before try to renderer.
 * 27-Jan-03    Doug            VBM:2002120213 - The
 *                              WAPTV5ControlSelectionRenderer inner class
 *                              was not initialising the selectAttributes in
 *                              the initialise method.
 * 16-Jan-03    Adrian          VBM:2002110505 - Updated method addDoAttributes
 *                              to call style.addBackgroundPosition to add the
 *                              bgoffset attribute to "do" elements.
 * 17-Feb-03    Byron           VBM:2003021409 - Modified
 *                              addTableCellAttributes to add colspan and
 *                              rowspan values.
 * 24-Feb-03    Ian             VBM:2002072414 Modified taddTableAttributes to
 *                              add align attribute.
 * 25-Feb-03    Ian             VBM:2002072605 - Convert 3 digit colors to 6
 *                              digit colors in convertColour.
 * 25-Feb-03    Byron           VBM:2003022105 - Modified addEnterEvents.
 *                              Cleaned up javadocs and unused variables.
 * 07-Mar-03    Sumit           VBM:2003030711 - Overode doFormAction method
 *                              to add meta tag instead of doActionInput
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for
 *                              ProtocolException where necessary.
 * 19-Mar-03    Chris W         VBM:2003031804 - Added comment stating that
 *                              openLayout must call WMLRoot.openLayout.
 * 17-Apr-03    Geoff           VBM:2003040305 - Modified addEnterEvents to
 *                              use new Script object.
 * 24-Apr-03    Sumit           VBM:2003032713 - Added render support for menu
 *                              item groups in doMenu
 * 02-May-03    Byron           VBM:2003042208 - Override new protocol method
 *                              writeInitialFocus. Fix imports, unused
 *                              variables, etc.  Add initialFocusElement member
 *                              variable and closeCard() method.
 * 08-May-03    Byron           VBM:2003050705 - Reverted openListItem and
 *                              openPane to original 'state'.
 * 21-May-03    Chris W         VBM:2003040403 - Calls to DOMOutputBuffer.app..
 *                              endLiteral() changed to appendEncoded()
 * 28-May-03    Mat             VBM:2003042907 - Change to use new
 *                              XMLOutputter
 * 02-Jun-03    Mat             VBM:2003042906 - Removed doProtocolString()
 * 20-May-03    Byron           VBM:2003051903 - Modified
 *                              addTableCellAttributes to set the tabindex on
 *                              tablecellattributes. Fixed missing braces on
 *                              some if statements.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.dom.DocType;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.MarkupFamily;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.XMLDeclaration;
import com.volantis.mcs.dom.output.CharacterEncoder;
import com.volantis.mcs.dom.output.DOMDocumentOutputter;
import com.volantis.mcs.dom.output.DocumentOutputter;
import com.volantis.mcs.dom.output.XMLDocumentWriter;
import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.layouts.Region;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.AnchorAttributes;
import com.volantis.mcs.protocols.CanvasAttributes;
import com.volantis.mcs.protocols.ColumnIteratorPaneAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMTransformer;
import com.volantis.mcs.protocols.EventAttributes;
import com.volantis.mcs.protocols.EventConstants;
import com.volantis.mcs.protocols.GridAttributes;
import com.volantis.mcs.protocols.GridChildAttributes;
import com.volantis.mcs.protocols.GridRowAttributes;
import com.volantis.mcs.protocols.HeadingAttributes;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.LayoutAttributes;
import com.volantis.mcs.protocols.ListItemAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.MenuAttributes;
import com.volantis.mcs.protocols.MenuChildRendererVisitor;
import com.volantis.mcs.protocols.MenuChildVisitable;
import com.volantis.mcs.protocols.MenuItem;
import com.volantis.mcs.protocols.MenuOrientation;
import com.volantis.mcs.protocols.MenuRenderer;
import com.volantis.mcs.protocols.Option;
import com.volantis.mcs.protocols.OptionVisitor;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.ParagraphAttributes;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.RowIteratorPaneAttributes;
import com.volantis.mcs.protocols.Script;
import com.volantis.mcs.protocols.SelectOption;
import com.volantis.mcs.protocols.SelectOptionGroup;
import com.volantis.mcs.protocols.SelectionRenderer;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.TableAttributes;
import com.volantis.mcs.protocols.TableCellAttributes;
import com.volantis.mcs.protocols.TableRowAttributes;
import com.volantis.mcs.protocols.XFActionAttributes;
import com.volantis.mcs.protocols.XFBooleanAttributes;
import com.volantis.mcs.protocols.XFSelectAttributes;
import com.volantis.mcs.protocols.XFTextInputAttributes;
import com.volantis.mcs.protocols.forms.validation.TextInputFormat;
import com.volantis.mcs.protocols.css.emulator.EmulatorRendererContext;
import com.volantis.mcs.protocols.layouts.ContainerInstance;
import com.volantis.mcs.protocols.trans.OptimizationConstants;
import com.volantis.mcs.protocols.wml.css.emulator.styles.WapTV5_WMLVersion1_3Style;
import com.volantis.mcs.protocols.wml.waptv5.WapTV5UnabridgedTransformer;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.packagers.PackageBodyOutput;
import com.volantis.mcs.themes.StyleInteger;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.themes.properties.MCSHttpMethodHintKeywords;
import com.volantis.mcs.themes.properties.MCSMenuOrientationKeywords;
import com.volantis.mcs.themes.properties.MCSSelectionListStyleKeywords;
import com.volantis.mcs.themes.properties.OverflowKeywords;
import com.volantis.mcs.themes.properties.WhiteSpaceKeywords;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.styling.PseudoStyleEntity;
import com.volantis.styling.StatefulPseudoClasses;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.styling.values.PropertyValues;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.schema.W3CSchemata;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Protocol class for Sky Wap TV version 5 based on WMLVersion 1.3.
 * <p/>
 * WapTV extends WML to support nested tables but it still does not support
 * nested paragraphs. Nested table support means that we can now support
 * layout grids.
 * Paragraphs can be emulated using a table as a table can appear almost
 * everywhere.
 */
public class WapTV5_WMLVersion1_3
        extends WMLVersion1_3 implements MenuChildRendererVisitor {

    // ***********************************************************************
    // *******************************NOTE************************************
    // ***********************************************************************
    //
    // WAPTV5 IS NO LONGER IN USE BY ANY OF OUR CUSTOMERS AND AS SUCH IS NOT
    // BEING ACTIVELY MAINTAINED.
    //
    // THIS MEANS THAT IT SHOULD COMPILE, AND IT'S TESTS MUST PASS, BUT IT
    // WILL NOT NECESSARILY "WORK PROPERLY".
    //
    // ***********************************************************************
    // *******************************NOTE************************************
    // ***********************************************************************
    //
    // NOTE: Support for enhanced menus was *not* added.
    //
    // NOTE: Support for (standard) stylistic emulation markup was *not* added.
    //
    // NOTE: This comment is deliberately not a javadoc comment to prevent
    // this information being public.

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(WapTV5_WMLVersion1_3.class);

    private static final String MENU_DESTINATION_AREA = "Menu";
    private static final String PAGE_NOOP_BUFFER_NAME = "noop";

    /**
     * A flag which indicates whether the openPane method had to generate a pane
     * table or not.
     */
    private boolean generatedPaneTable = false;

    /**
     * The SelectionRenderer to use when rendering checkboxes and radio buttons
     */
    private SelectionRenderer controlSelectionRenderer;

    /**
     * Allow the protocol to store the initial focus element. This is used
     * whilst generating the initial focus
     */
    protected Element initialFocusElement = null;

    /**
     * The context to use to render styles when emulating CSS.
     */
    protected EmulatorRendererContext emulatorRendererContext;

    /**
     * Construct an instance of this protocol.
     */
    public WapTV5_WMLVersion1_3(ProtocolSupportFactory protocolSupportFactory,
            ProtocolConfiguration protocolConfiguration) {
        super(protocolSupportFactory, protocolConfiguration);
        supportsTitleOnTR = true;
        supportsTitleOnTD = true;
        supportsImgDimensions = true;
        alwaysUseDoForAction = true;

    }

    public void initialise() {
        super.initialise();

        // NOTE: this fixes nested tables support to true regardless of the
        // device repository setting. WapTV5TransFactory is hardcoded to expect
        // this so table flattening would behave randomly otherwise.
        supportsNestedTables = true;
    }

    // Javadoc inherited.
    protected DOMTransformer createDOMTransformer() {
        return new WapTV5UnabridgedTransformer(protocolConfiguration);
    }

    // javadoc inherited
    protected MenuRenderer createNumericShortcutMenuRenderer() {
        // WapTV doesn't support numeric shortcut menu rendering.
        return null;
    }

    // ========================================================================
    //   General helper methods.
    // ========================================================================

    public EmulatorRendererContext getEmulatorRendererContext() {
        if (emulatorRendererContext == null) {
            emulatorRendererContext
                    = new EmulatorRendererContext(context.getRequestContext(),
                            styleSheetRenderer, this,
                            getProtocolConfiguration().getCssVersion());
        }

        return emulatorRendererContext;
    }

    /**
     * A helper method to get the default style associated with the
     * attributes.
     *
     * @param attributes The attributes which are used to retrieve the style.
     * @return The Style, or null if a Style could not be found. could not be
     *         found.
     */
    public WapTV5_WMLVersion1_3Style getStyle(MCSAttributes attributes) {
        Styles styles = attributes.getStyles();

        return getStyle(styles);
    }

    private WapTV5_WMLVersion1_3Style getStyle(Styles styles) {
        if (styles != null) {

            MutablePropertyValues propertyValues = styles.getPropertyValues();
            // if there are no propertyValues then we want to return null
            if (propertyValues != null) {
                WapTV5_WMLVersion1_3Style style = null;
                style = new WapTV5_WMLVersion1_3Style(propertyValues, this);
                return style;
            }
        }
        return null;
    }

    /**
     * A helper method to get the default style associated with the
     * attributes.
     *
     * @param attributes The attributes which are used to retrieve the style.
     * @return The Style, or null if a Style could not be found.
     */
    public WapTV5_WMLVersion1_3Style getStyle(MCSAttributes attributes,
                          PseudoStyleEntity pseudoEntity) {
        WapTV5_WMLVersion1_3Style style = null;

        Styles styles = attributes.getStyles();

        if (styles != null) {
            Styles nestedStyles = styles.findNestedStyles(pseudoEntity);
            if (nestedStyles != null) {
                MutablePropertyValues propertyValues =
                    nestedStyles.getPropertyValues();
                // if there are no properties then we want to return null
                if (propertyValues != null) {
                    style = new WapTV5_WMLVersion1_3Style(propertyValues, this);
                }
            }
        }
        return style;
    }


    /**
     * Helper method that returns a font size for a given style
     *
     * @param style Object that may contain stylistic font information
     * @return the Size of the font as specified via the style. If no
     * font specifie then 0 is returned
     */
    /* protected int getFontSize(Style style){
       // Check for font size, scan through the possibilities. There are
       // several methods setting the size, include strings, percentages,
       // font pitch and pixel sizes.
       String value;
       int fontSize = 0;
       if (style != null && (value = style.getValue("font-size") != null)) {

         String[] szStr = { "xx-small", "x-small", "small", "medium",
                            "large", "x-large", "xx-large" };

         int[] percent = { 50, 50, 75, 100, 125, 150, 200 };

         double[] em = { 0.25, 0.50, 0.75, 1.00, 1.25, 1.50, 1.75 };

         int[] szInt = { 6, 8, 10, 12, 14, 16, 18 };

         for (int i = 0; i < szStr.length; i++) {
           if (szStr[i].equals(value)) {
             fontSize = szInt[i];
             break;
           }
         }

         String number;
         if (fontSize == 0 && value.endsWith("%")) {
           number = value.substring(0, value.length() - 1);
           int fpc = Integer.parseInt(number);
           for (int i = percent.length - 1; i >=0; i--) {
             if (fpc >= percent[i]) {
               fontSize = szInt[i];
               break;
             }
           }
         }

         if (fontSize == 0 && value.endsWith("px")) {
           number = value.substring(0, value.length() - 2);
           int fpx = Integer.parseInt(number);

           if (fpx < 12) {
             fontSize =  szInt[2];
           } else if (fpx > 12) {
             fontSize =  szInt[5];
           } else if (fpx == 12) {
             fontSize =  szInt[3];
           }
         }

         if (fontSize == 0 && value.endsWith("em")) {
           number = value.substring(0, value.length() - 2);
           double fem = Double.parseDouble(number);

           for (int i = em.length - 1; i >=0; i--) {
             if (fem >= em[i]) {
               fontSize = szInt[i];
               break;
             }

           }
         }
       }
       return fontSize;
     }*/

    /**
     * Called when opening a font tag, for protocols that do not support style
     * sheet (CSS) referencing. If style contained font information then this
     * method appends an open font tag to the buffer, otherwise it does nothing.
     *
     * @param dom          The DomOutputBuffer to which the open font tag should
     *                     be appended.
     * @param attributes   The attributes object that may or may not contain the
     *                     font data
     * @param isActionItem Indicates that this font resides in an action item
     */
    protected void openFont(
            DOMOutputBuffer dom, MCSAttributes attributes,
            boolean isActionItem) {

        Styles styles = attributes.getStyles();
        Styles activeStyles =
                styles.findNestedStyles(StatefulPseudoClasses.ACTIVE);
        Styles focusStyles = styles.findNestedStyles(
                StatefulPseudoClasses.FOCUS);

        Element element = domFactory.createStyledElement(attributes.getStyles());

        String value;
        if (activeStyles != null) {
            if ((value = colorHandler.getAsString(activeStyles)) != null) {
                element.setAttribute("fgactivated", value);
            }
        }
        if (focusStyles != null) {
            if ((value = colorHandler.getAsString(focusStyles)) != null) {
                element.setAttribute("fgfocused", value);
            }
        }

        String colorAttribute;
        if (isActionItem) {
            colorAttribute = "fgcolor";
        } else {
            colorAttribute = "fgstatic";
        }
        if ((value = colorHandler.getAsString(styles)) != null) {
            element.setAttribute(colorAttribute, value);
        }

        WapTV5_WMLVersion1_3Style specificStyle =
                (WapTV5_WMLVersion1_3Style) getStyle(attributes);
        if (specificStyle != null) {
            specificStyle.addFontSize(element, "size");
            specificStyle.addFontFamily(element, "id",
                                        element.getAttributes() != null);
        }
        //Only add the font element if any of the above code added
        //attributes
        if (element.getAttributes() != null) {
            element.setName("font");
            dom.openElement(element);
        }
    }

    /**
     * Called when closing a font tag, for protocols that do not support style
     * sheet (CSS) referencing. If style contained font information then this
     * method appends a close font tag to the buffer, otherwise it does nothing.
     *
     * @param dom The DomOutputBuffer to which the open font tag should be
     *            appended.
     */
    private void closeFont(DOMOutputBuffer dom) {
        String name = dom.getCurrentElement().getName();
        if (name != null && name.equals("font")) {
            dom.closeElement("font");
        }
    }

    // ========================================================================
    //   Page element methods.
    // ========================================================================

    // Javadoc inherited from super class.
    protected void doProtocolString(Document document) {
        DocType docType = domFactory.createDocType(
                "WML", null, null,
                "<!ENTITY nbsp \"&#160;\">\n" +
                "<!ENTITY quot \"&#34;\">\n" +
                "<!ENTITY amp \"&#38;#38;\">\n" +
                "<!ENTITY apos \"&#39;\">\n" +
                "<!ENTITY lt \"&#38;#60;\">\n" +
                "<!ENTITY gt \"&#62;\">\n" +
                "<!ENTITY shy \"&#173;\">\n" +
                "<!ENTITY eur \"&#xa4;\">",
                MarkupFamily.XML);
        document.setDocType(docType);

        XMLDeclaration declaration = domFactory.createXMLDeclaration();
        declaration.setEncoding("UTF-8");
        document.setDeclaration(declaration);
    }

    // Javadoc inherited from super class.
    protected void openCanvas(
            DOMOutputBuffer dom,
            CanvasAttributes attributes) {

        String value;

        Element element = dom.openElement("wml");

        RuntimeDeviceLayout layout = context.getDeviceLayout();

        if (layout != null) {
            if ((value = layout.getLayoutGroupName()) != null) {
                String baseDir
                        = context.getVolantisBean().getModeSetsBase();

                value = baseDir + "/" + value + ".wml";

                element.setAttribute("mode", value);
            }
        }

        element.setAttribute("xmlns", "http://waptv.com/xsd/wtvml");
        element.setAttribute("xmlns:xsi", W3CSchemata.XSI_NAMESPACE);
        element.setAttribute("xsi:schemaLocation",
                             "http://waptv.com/xsd/wtvml_5.0.xsd");
    }

    /**
     * Override this method to add extra attributes to the card.
     */
    protected void addCardAttributes(
            Element element,
            CanvasAttributes attributes) {

        // Add the super classes attributes first.
        super.addCardAttributes(element, attributes);

        Styles styles = attributes.getStyles();
        PropertyValues propertyValues = styles.getPropertyValues();
        StyleValue overflow = propertyValues.getComputedValue(
                StylePropertyDetails.OVERFLOW);
        if (overflow == OverflowKeywords.SCROLL) {
            element.setAttribute("scroll", "true");
        } else {
            element.setAttribute("scroll", "false");
        }

        WapTV5_WMLVersion1_3Style style = getStyle(attributes);

        if (style != null) {
            style.addMarinerParagraphGap(element, "paragap");
            // Use margins for vspace and hspace. On Sky Wap there are
            // only these two values whereas styles have top, bottom,
            // left and right. So we approximate by adding top and bottom,
            // and left and right and dividing by 2.
            addVSpace(element, style);
            addHSpace(element, style);
        }

        /*
        Layout layout = context.getDeviceLayout ();
        if ((layout.getLayoutGroupName () != null)) {
          element.setAttribute ("mode", "#" + layout.getLayoutName ());
        }
        */
    }

    //javadoc inherited
    protected void addEnterEvents(
            DOMOutputBuffer dom,
            CanvasAttributes attributes) throws ProtocolException {

        Script forwardTask = getTaskForEvent(attributes, "onenterforward",
                                             EventConstants.ON_ENTER_FORWARD);
        Script backwardTask = getTaskForEvent(attributes, "onenterbackward",
                                              EventConstants.ON_ENTER_BACKWARD);

        // It is assumed that if the forward and backward tasks are the same,
        // then the 'script' is the same. Ideally we should be checking that
        // this event is associated with the same asset.
        if ((forwardTask != null) && (backwardTask != null) &&
                forwardTask.equals(backwardTask)) {
            addOnEventElement(dom, attributes, "onenter", forwardTask);
        } else {
            addOnEventElement(dom, attributes, "onenterforward", forwardTask);
            addOnEventElement(dom, attributes, "onenterbackward", backwardTask);
        }
    }

    /**
     * Open the card tag.
     *
     * @param attributes the CanvasAttributes for this card tag
     */
    protected void openCard(
            DOMOutputBuffer dom,
            CanvasAttributes attributes) throws ProtocolException {

        super.openCard(dom, attributes);

        DOMOutputBuffer noop = getExtraBuffer(PAGE_NOOP_BUFFER_NAME,
                                              false);
        initialFocusElement = dom.getCurrentElement();
        if (noop == null) {
            return;
        }

        // @todo 2005060816 annotate child with style information if it's not inherited from the parent
        Element element = dom.openStyledElement("onevent", attributes);
        element.setAttribute("type", "onenterforward");

        Element noopElement = dom.openElement("noop");
        dom.addOutputBuffer(noop);
        dom.closeElement(noopElement);

        dom.closeElement(element);
    }

    // Javadoc inherited from super class.
    protected void closeCard(
            DOMOutputBuffer dom,
            CanvasAttributes attributes) {
        super.closeCard(dom, attributes);
        initialFocusElement = null;
    }

    protected void processBodyBuffer(DOMOutputBuffer buffer) {
        super.processBodyBuffer(buffer);

        RuntimeDeviceLayout deviceLayout = context.getDeviceLayout();
        String destinationLayout = deviceLayout.getDestinationLayout();
        if (destinationLayout == null
                || destinationLayout.length() == 0) {
            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Attempting to set mode on first card in body to "
                         + destinationLayout);
        }

        // Hack to add the mode to the first card.
        Element root = buffer.getRoot();
        Node node = root.getHead();
        if (node != null && node instanceof Element) {
            Element card = (Element) node;
            if ("card".equals(card.getName())) {
                card.setAttribute("mode", "#" + destinationLayout);
            }
        }
    }

    // ========================================================================
    //   Dissection methods
    // ========================================================================

    // ========================================================================
    //   Layout / format methods.
    // ========================================================================

    /**
     * Allows the optimization attribute to be added to a table element if
     * needed. Note that the default value will never be added (a null value is
     * never used to generate an attribute).
     *
     * @param table  the DOM table element to be updated
     * @param choice the optimization choice selected by the layout designer
     */
    private void addOptimizeAttribute(
            Element table,
            String choice) {
        if ("table".equals(table.getName()) &&
                (choice != null)) {
            if (FormatConstants.OPTIMIZATION_LEVEL_VALUE_ALWAYS.equals(choice)) {
                table.
                        setAttribute(
                                OptimizationConstants.OPTIMIZATION_ATTRIBUTE,
                                OptimizationConstants.OPTIMIZE_ALWAYS);
            } else if (FormatConstants.OPTIMIZATION_LEVEL_VALUE_LITTLE_IMPACT.
                    equals(choice)) {
                table.
                        setAttribute(
                                OptimizationConstants.OPTIMIZATION_ATTRIBUTE,
                                OptimizationConstants.OPTIMIZE_LITTLE_IMPACT);
            } else if (FormatConstants.OPTIMIZATION_LEVEL_VALUE_NEVER.equals(choice)) {
                // Specifically do nothing. This is the default for this
                // protocol set
            } else {
                logger.warn("unknown-optimization-level", new Object[]{choice});
            }
        }
    }

    // Javadoc inherited from super class.
    protected void openColumnIteratorPane(
            DOMOutputBuffer dom,
            ColumnIteratorPaneAttributes attributes) {

        Styles styles = attributes.getStyles();
        String value;

        // @todo 2005060816 annotate child with style information if it's not inherited from the parent
        Element element = dom.openStyledElement("table", attributes,
                DisplayKeywords.TABLE);

        addOptimizeAttribute(element,
                             attributes.getPane().getOptimizationLevel());

        if ((value = borderHandler.getAsString(styles)) != null) {
            element.setAttribute("borderwidth", value);
        }
        // todo fix cell padding
//    if ((value = style.getHorizontalCellPadding()) != null) {
//      element.setAttribute ("hpad", value);
//    }
//    if ((value = style.getVerticalCellPadding()) != null) {
//      element.setAttribute ("vpad", value);
//    }
        if ((value = borderSpacingHandler.getAsString(styles)) != null) {
            element.setAttribute("colgap", value);
            element.setAttribute("rowgap", value);
        }
        if ((value = backgroundColorHandler.getAsString(styles)) != null) {
            element.setAttribute("bgcolor", value);
        }
        addBackgroundImage(element, styles);

        dom.openElement("tr");
    }

    // Javadoc inherited from super class.
    protected void closeColumnIteratorPane(
            DOMOutputBuffer dom,
            ColumnIteratorPaneAttributes attributes) {
        dom.closeElement("tr");
        dom.closeElement("table");
    }

    // Javadoc inherited from super class.
    protected void openColumnIteratorPaneElement(
            DOMOutputBuffer dom,
            ColumnIteratorPaneAttributes attributes) {
        dom.openStyledElement("td", attributes, DisplayKeywords.TABLE_CELL);
    }

    // Javadoc inherited from super class.
    protected void closeColumnIteratorPaneElement(
            DOMOutputBuffer dom,
            ColumnIteratorPaneAttributes attributes) {
        dom.closeElement("td");
    }

    // Javadoc inherited from super class.
    protected void openGrid(
            DOMOutputBuffer dom,
            GridAttributes attributes) {
        String value;

        Element element = dom.openStyledElement("table", attributes,
                DisplayKeywords.TABLE);

        Styles styles = attributes.getStyles();

        addOptimizeAttribute(element,
                             (String) attributes.getFormat().
                                      getAttribute(
                                              FormatConstants.OPTIMIZATION_LEVEL_ATTRIBUTE));

        if ((value = borderHandler.getAsString(styles)) != null) {
            element.setAttribute("borderwidth", value);
        }
        // todo fix cell padding
//    if ((value = attributes.getCellPadding()) != null
//        && !value.equals("0")) {
//      element.setAttribute ("hpad", value);
//      element.setAttribute ("vpad", value);
//    }
        // todo Calculate a horizontal and a vertical spacing separately.
        if ((value = borderSpacingHandler.getAsString(styles)) != null) {
            element.setAttribute("colgap", value);
            element.setAttribute("rowgap", value);
        }
        if ((value = backgroundColorHandler.getAsString(styles)) != null) {
            element.setAttribute("bgcolor", value);
        }
        addBackgroundImage(element, styles);
        // NB: We do not need to set the column attribute because it will be
        // resolved and added in the associated transformer
    }

    // Javadoc inherited from super class.
    protected void closeGrid(
            DOMOutputBuffer dom,
            GridAttributes attributes) {
        dom.closeElement("table");
    }

    // Javadoc inherited from super class.
    protected void openGridChild(
            DOMOutputBuffer dom,
            GridChildAttributes attributes) {

        Element element = dom.openStyledElement("td", attributes,
                DisplayKeywords.TABLE_CELL);
        addGridChildAttributes(element, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeGridChild(
            DOMOutputBuffer dom,
            GridChildAttributes attributes) {
        dom.closeElement("td");
    }

    // Javadoc inherited from super class.
    protected void openGridRow(
            DOMOutputBuffer dom,
            GridRowAttributes attributes) {
        dom.openStyledElement("tr", attributes, DisplayKeywords.TABLE_ROW);
    }

    // Javadoc inherited from super class.
    protected void closeGridRow(
            DOMOutputBuffer dom,
            GridRowAttributes attributes) {
        dom.closeElement("tr");
    }

    /**
     * Generate an open paragraph tag to be added before the layout.
     *
     * @param attributes The attributes to use.
     */
    protected void openLayout(
            DOMOutputBuffer dom,
            LayoutAttributes attributes) {
        // This method must call its superclass version in WMLRoot. Failure
        // to do so means that the native markup written to wml.card.timer,
        // wml.card.onevent and wml.card.beforebody will not be written out.
        // See AN009 Marlin Markup Language p19 for more details.
        super.openLayout(dom, attributes);
        if (logger.isDebugEnabled()) {
            logger.debug(canvasAttributes);
        }
        if ((!inclusion)
                || attributes.getDeviceLayoutContext().isOverlay()) {
            dom.openStyledElement(WMLConstants.BLOCH_ELEMENT, attributes);
        }
    }

    /**
     * Generate a close paragraph tag to be added after the layout.
     *
     * @param attributes The attributes to use.
     */
    protected void closeLayout(
            DOMOutputBuffer dom,
            LayoutAttributes attributes) {
        if ((!inclusion)
                || attributes.getDeviceLayoutContext().isOverlay()) {
            dom.closeElement(WMLConstants.BLOCH_ELEMENT);
        }
        super.closeLayout(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void openPane(
            DOMOutputBuffer dom,
            PaneAttributes attributes) {

        Styles styles = attributes.getStyles();
        String value;

        // The pane should always use the enclosing table cell if there is one,
        // otherwise it should always generate a table.
        Element element = dom.getCurrentElement();
        if (element.getName().equals("td")) {
            generatedPaneTable = false;
        } else {
            // @todo 2005060816 annotate child with style information if it's not inherited from the parent
            Element table = dom.openStyledElement("table", styles,
                    DisplayKeywords.TABLE);

            addOptimizeAttribute(table,
                                 attributes.getPane().getOptimizationLevel());

            StylingFactory factory = StylingFactory.getDefaultInstance();
            Styles rowStyles = factory.createInheritedStyles(styles,
                    DisplayKeywords.TABLE_ROW);
            dom.openStyledElement("tr", rowStyles);
            Styles cellStyles = factory.createInheritedStyles(styles,
                    DisplayKeywords.TABLE_CELL);
            element = dom.openStyledElement("td", cellStyles);
            generatedPaneTable = true;
        }

        if ((value = borderHandler.getAsString(styles)) != null) {
            element.setAttribute("borderwidth", value);
        }

        // todo fix cell padding
//    if ((value = style.getHorizontalCellPadding()) != null
//        && !value.equals("0")) {
//      element.setAttribute ("hpad", value);
//    }
//
//    if ((value = style.getVerticalCellPadding()) != null
//        && !value.equals("0")) {
//      element.setAttribute ("vpad", value);
//    }

        if ((value = heightHandler.getAsString(styles)) != null) {
            element.setAttribute("height", value);
        }
        if ((value = widthHandler.getAsString(styles)) != null) {
            element.setAttribute("width", value);
        }
        if ((value = backgroundColorHandler.getAsString(styles)) != null) {
            element.setAttribute("bgcolor", value);
        }
        addBackgroundImage(element, styles);
    }

    // Javadoc inherited from super class.
    protected void closePane(
            DOMOutputBuffer dom,
            PaneAttributes attributes) {
        if (generatedPaneTable) {
            dom.closeElement("td");
            dom.closeElement("tr");
            dom.closeElement("table");
        }
    }

    // Javadoc inherited from super class.
    protected
    void openRowIteratorPane(
            DOMOutputBuffer dom,
            RowIteratorPaneAttributes attributes) {

        Styles styles = attributes.getStyles();
        String value;

        Element element = dom.openStyledElement("table", attributes,
                DisplayKeywords.TABLE);

        addOptimizeAttribute(element,
                             attributes.getPane().getOptimizationLevel());

        if ((value = borderHandler.getAsString(styles)) != null) {
            element.setAttribute("borderwidth", value);
        }

        // todo fix cell padding
//    if ((value = style.getHorizontalCellPadding()) != null) {
//      element.setAttribute ("hpad", value);
//    }
//
//    if ((value = style.getVerticalCellPadding()) != null) {
//      element.setAttribute ("vpad", value);
//    }

        if ((value = borderSpacingHandler.getAsString(styles)) != null) {
            element.setAttribute("colgap", value);
            element.setAttribute("rowgap", value);
        }
        if ((value = backgroundColorHandler.getAsString(styles)) != null) {
            element.setAttribute("bgcolor", value);
        }
        addBackgroundImage(element, styles);
    }

    // Javadoc inherited from super class.
    protected void closeRowIteratorPane(
            DOMOutputBuffer dom,
            RowIteratorPaneAttributes attributes) {
        dom.closeElement("table");
    }

    // Javadoc inherited from super class.
    protected void openRowIteratorPaneElement(
            DOMOutputBuffer dom,
            RowIteratorPaneAttributes attributes) {
        // @todo 2005060816 annotate child with style information if it's not inherited from the parent
        dom.openStyledElement("tr", attributes, DisplayKeywords.TABLE_ROW);
        dom.openElement("td");
    }

    // Javadoc inherited from super class.
    protected void closeRowIteratorPaneElement(
            DOMOutputBuffer dom,
            RowIteratorPaneAttributes attributes) {
        dom.closeElement("td");
        dom.closeElement("tr");
    }

    // ========================================================================
    //   Navigation methods.
    // ========================================================================

    // ========================================================================
    //   Block element methods.
    // ========================================================================

    // Javadoc inherited from super class.
    protected void openParagraph(
            DOMOutputBuffer dom,
            ParagraphAttributes attributes) {
        if (insideAnchorBody) {
            return;
        }

        WapTV5_WMLVersion1_3Style style = (WapTV5_WMLVersion1_3Style)
                getStyle(attributes);

        dom.addStyledElement("br", attributes);

        // @todo 2005060816 annotate child with style information if it's not inherited from the parent
        Element element = dom.openStyledElement("table", attributes,
                DisplayKeywords.TABLE);

        // Apply stylistic information to table.
        if (style != null) {
            style.addTextAlign(element, "align");
        }

        dom.openElement("tr");

        element = dom.openElement("td");

        addTitleAttribute(element, attributes, true);

        // Apply stylistic information to table cell.
        if (style != null) {
            style.addMarinerLineGap(element, "linegap");
        }

        WapTV5_WMLVersion1_3Style bodyStyle =
                (WapTV5_WMLVersion1_3Style) getStyle(attributes);
        if (bodyStyle != null) {
            bodyStyle.addVspace(element, "vspace");
        }

        openFont(dom, attributes, false);
    }

    // Javadoc inherited from super class.
    protected void closeParagraph(
            DOMOutputBuffer dom,
            ParagraphAttributes attributes) {

        if (insideAnchorBody) {
            return;
        }

        closeFont(dom);

        // No more attributes, close the tag and return the string
        dom.closeElement("td");
        dom.closeElement("tr");
        dom.closeElement("table");
        dom.addStyledElement("br", attributes);
    }

    // Javadoc inherited from super class.
    protected void openHeading(DOMOutputBuffer dom,
            HeadingAttributes attributes) {

        // If we are processing an anchor body then we cannot generate any
        // emphasis tags.
        if (insideAnchorBody) {
            return;
        }

        // Do the normal WML stylistic emulation as well.
        // In future we should combine the WapTV specific stuff in here too.
        // This is like this for now just to get the tests to pass...
        super.openHeading(dom, attributes);

        // Add the open font markup after the normal markup.
        openFont(dom, attributes, false);
    }

    // Javadoc inherited from super class.
    protected void closeHeading(DOMOutputBuffer dom,
            HeadingAttributes attributes) {

        // If we are processing an anchor body then we cannot generate any
        // emphasis tags.
        if (insideAnchorBody) {
            return;
        }

        // Close the font markup before the normal markup
        closeFont(dom);

        // Do the normal WML stylistic emulation as well.
        // In future we should combine the WapTV specific stuff in here too.
        // This is like this for now just to get the tests to pass...
        super.closeHeading(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void openMenu(
            DOMOutputBuffer dom,
            MenuAttributes attributes) {
        openFont(dom, attributes, false);
    }

    // Javadoc inherited from super class.
    protected void closeMenu(
            DOMOutputBuffer dom,
            MenuAttributes attributes) {
        closeFont(dom);
    }

    // Javadoc inherited from super class.
    public void openSpan(
            DOMOutputBuffer dom,
            SpanAttributes attributes) {
        openFont(dom, attributes, false);
    }

    // Javadoc inherited from super class.
    public void closeSpan(
            DOMOutputBuffer dom,
            SpanAttributes attributes) {
        closeFont(dom);
    }

    // ========================================================================
    //   List element methods.
    // ========================================================================

    // Javadoc inherited from super class.
    protected void openListItem(
            DOMOutputBuffer dom,
            ListItemAttributes attributes) {
        dom.addStyledElement("br", attributes);
        // Add the open font markup after the normal markup.
        // @todo later this has been commented out deliberately at some stage
        // but may have been a mistake (unused 'style' variable). A VBM has
        // been raised to address this issue.
        //Style style = getStyle (attributes);
        openFont(dom, attributes, false);
    }

    // Javadoc inherited from super class.
    protected void closeListItem(
            DOMOutputBuffer dom,
            ListItemAttributes attributes) {
        // Close the font markup before the normal markup
        closeFont(dom);
    }

    // ========================================================================
    //   Table element methods.
    // ========================================================================

    /**
     * Add the height and width to each td element in a grid
     */
    private void addGridChildAttributes(
            Element element,
            GridChildAttributes attributes) {

        Styles styles = attributes.getStyles();

        // Only add the width attribute to the cells in the first row.
        String value;

        value = widthHandler.getAsString(styles);
        if (value != null) {
            element.setAttribute("width", value);
        }
        value = heightHandler.getAsString(styles);
        if (value != null) {
            element.setAttribute("height", value);
        }
    }

    /**
     * Add the value for hpad based on style settings for left and right paddings
     * to a <code>StringBuffer</code>
     *
     * @param element <code>Element</code> to add the hpad attribute to
     * @param style   <code>Style</code> whose padding values to use
     */
    private void addHPad(Element element, WapTV5_WMLVersion1_3Style style) {
        style.addHorizontalPadding(element, "hpad");
    }

    /**
     * Add the value for vpad based on style settings for top and bottom padding
     * to a <code>StringBuffer</code>
     *
     * @param element <code>Element</code> to add the vpad attribute to
     * @param style   <code>Style</code> whose padding values to use
     */
    private void addVPad(Element element, WapTV5_WMLVersion1_3Style style) {
        style.addVerticalPadding(element, "vpad");
    }

    /**
     * Add the value for hspace based on style settings for left and right
     * margins to a <code>StringBuffer</code>
     *
     * @param element <code>StringBuffer</code> to add the hspace attribute to
     * @param style   <code>Style</code> whose margin values to use
     *                Add the value for hspace based on style settings for left and
     *                right margins to a <code>StringBuffer</code>
     */
    private void addHSpace(Element element, WapTV5_WMLVersion1_3Style style) {
        style.addHorizontalSpace(element, "hspace");
    }

    /**
     * Add the value for vspace based on style settings for top and bottom margin
     * to a <code>StringBuffer</code>
     *
     * @param element <code>StringBuffer</code> to add the vspace attribute to
     * @param style   <code>Style</code> whose margin values to use
     */
    private void addVSpace(Element element, WapTV5_WMLVersion1_3Style style) {
        style.addVerticalSpace(element, "vspace");
    }

    private void addBorderColor(Element element, Styles styles) {
        String value;
        if ((value = borderColorHandler.getAsString(styles)) != null) {
            element.setAttribute("bordercolor", value);
        }
    }

    private void addBorderActivated(Element element, Styles styles) {
        String value;
        if ((value = borderColorHandler.getAsString(styles)) != null) {
            element.setAttribute("borderactivated", value);
        }
    }

    private void addBorderFocused(Element element, Styles styles) {
        String value;
        if ((value = borderColorHandler.getAsString(styles)) != null) {
            element.setAttribute("borderfocused", value);
        }
    }

    private void addBorderUnFocused(Element element, Styles styles) {
        String value;
        if ((value = borderColorHandler.getAsString(styles)) != null) {
            element.setAttribute("borderunfocused", value);
        }
    }

    private void addBorderWidth(Element element, Styles styles) {
        String value;
        if ((value = borderHandler.getAsString(styles)) != null) {
            element.setAttribute("borderwidth", value);
        }
    }

    /**
     * Add protocol specific table data cell attributes.
     *
     * @param element    the <code>Element</code> to add table protocol specific
     *                   table attributes to.
     * @param attributes the <code>TableCellAttributes</code>
     */
    protected void addTableCellAttributes(
            Element element,
            TableCellAttributes attributes) {

        // Add the super classes attributes first.
        super.addTableCellAttributes(element, attributes);

        Styles styles = attributes.getStyles();
        Styles activeStyles =
                styles.findNestedStyles(StatefulPseudoClasses.ACTIVE);
        Styles focusStyles = styles.findNestedStyles(
                StatefulPseudoClasses.FOCUS);

        String value;
        if ((value = attributes.getColSpan()) != null) {
            element.setAttribute("colspan", value);
        }
        if ((value = attributes.getRowSpan()) != null) {
            element.setAttribute("rowspan", value);
        }

        addBackgroundImage(element, styles);

        if (activeStyles != null) {
            addBgActivated(element, activeStyles);
            addBorderActivated(element, activeStyles);
        }
        if (focusStyles != null) {
            addBgFocused(element, focusStyles);
            addBorderFocused(element, focusStyles);
        }

        if (supportsTabindex && attributes.getTabindex() != null) {
            element.setAttribute("tabindex", (String) attributes.getTabindex());
        }

        addBackgroundColor(element, styles);

        addBorderColor(element, styles);
        addBorderWidth(element, styles);
        addWidth(element, styles);
        addHeight(element, styles);

        StyleValue styleValue = styles.getPropertyValues()
                .getComputedValue(StylePropertyDetails.WHITE_SPACE);

        if (styleValue == WhiteSpaceKeywords.NOWRAP ||
                styleValue == WhiteSpaceKeywords.PRE) {
            element.setAttribute("mode", "nowrap");
        } else if (styleValue == WhiteSpaceKeywords.NORMAL) {
            element.setAttribute("mode", "wrap");
        }

        WapTV5_WMLVersion1_3Style style = (WapTV5_WMLVersion1_3Style)
                getStyle(attributes);
        if (style != null) {
            style.addMarinerLineGap(element, "linegap");
            style.addMarinerCornerRadius(element, "bgradius");
            style.addTextAlign(element, "align");


            // Use margins for vspace and hspace. On Sky Wap there are
            // only these two values whereas styles have top, bottom,
            // left and right. So we approximate by adding top and bottom,
            // and left and right and dividing by 2.
            addVSpace(element, style);
            addHSpace(element, style);

            // Use padding for vpad and hpad. Similarly to margins, Sky Wap
            // only has two values for padding. So again
            // we approximate by adding the appropriate values and dividing
            // by 2.
            addVPad(element, style);
            addHPad(element, style);
        }
    }

    private void addWidth(Element element, Styles styles) {
        String value;
        if ((value = widthHandler.getAsString(styles)) != null) {
            element.setAttribute("width", value);
        }
    }

    private void addHeight(Element element, Styles styles) {
        String value;
        if ((value = heightHandler.getAsString(styles)) != null) {
            element.setAttribute("height", value);
        }
    }

    private void addBackgroundColor(Element element, Styles styles) {
        String value;
        if ((value = backgroundColorHandler.getAsString(styles)) != null) {
            element.setAttribute("bgcolor", value);
        }
    }

    private void addBgFocused(Element element, Styles styles) {
        String value;
        if ((value = backgroundColorHandler.getAsString(styles)) != null) {
            element.setAttribute("bgfocused", value);
        }
    }

    private void addBgActivated(Element element, Styles styles) {
        String value;
        if ((value = backgroundColorHandler
                .getAsString(styles)) != null) {
            element.setAttribute("bgactivated", value);
        }
    }

    protected void addTableCellEvents(
            DOMOutputBuffer dom,
            TableCellAttributes attributes) throws ProtocolException {

        EventAttributes events = attributes.getEventAttributes(false);
        if (events == null) {
            return;
        }

        addOnEventElement(dom, attributes, "onfocus",
                          EventConstants.ON_FOCUS);

        addOnEventElement(dom, attributes, "onunfocus",
                          EventConstants.ON_BLUR);

        addOnEventElement(dom, attributes, "onactivate",
                          EventConstants.ON_CLICK);
    }

    protected void addTableRowAttributes(
            Element element,
            TableRowAttributes attributes) {

        super.addTableRowAttributes(element, attributes);

        Styles styles = attributes.getStyles();
        Styles activeStyles =
                styles.findNestedStyles(StatefulPseudoClasses.ACTIVE);
        Styles focusStyles = styles.findNestedStyles(
                StatefulPseudoClasses.FOCUS);

        if (activeStyles != null) {
            addBgActivated(element, activeStyles);
            addBorderActivated(element, activeStyles);
        }
        if (focusStyles != null) {
            addBgFocused(element, focusStyles);
            addBorderFocused(element, focusStyles);
        }
        addBackgroundImage(element, styles);

        addBackgroundColor(element, styles);
        addBorderColor(element, styles);
        addBorderWidth(element, styles);
        addHeight(element, styles);

        WapTV5_WMLVersion1_3Style style = getStyle(attributes);
        if (style != null) {
            style.addMarinerCornerRadius(element, "bgradius");
        }
    }


    /**
     * Add protocol specific table attributes.
     *
     * @param element    <code>Element</code> to add table protocol specific
     *                   table attributes to
     * @param attributes the <code>TableAttributes</code>
     */
    protected void addTableAttributes(
            Element element,
            TableAttributes attributes) {
        // Add the super classes attributes first.
        super.addTableAttributes(element, attributes);

        Styles styles = attributes.getStyles();

        addBackgroundImage(element, styles);
        addBorderColor(element, styles);
        addBorderWidth(element, styles);
        addWidth(element, styles);
        addHeight(element, styles);

        WapTV5_WMLVersion1_3Style style = (WapTV5_WMLVersion1_3Style)
                getStyle(attributes);

        if (style != null) {
            style.addBorderVerticalSpacing(element, "rowgap");
            style.addBorderHorizontalSpacing(element, "colgap");
            style.addMarinerCornerRadius(element, "bgradius");
            style.addTextAlign(element, "align");
            // Use margins for vspace and hspace. On Sky Wap there are
            // only these two values whereas styles have top, bottom,
            // left and right. So we approximate by adding top and bottom,
            // and left and right and dividing by 2.
            addVSpace(element, style);
            addHSpace(element, style);

            // Use padding for vpad and hpad. Similarly to margins, Sky Wap
            // only has two values for padding. So again
            // we approximate by adding the appropriate values and dividing
            // by 2.
            addVPad(element, style);
            addHPad(element, style);
        }
    }

    private void addBackgroundImage(Element element, Styles styles) {
        String value;
        value = backgroundComponentHandler.getAsString(styles);
        if (value != null) {
            element.setAttribute("bgimage", value);
        }
    }

    // Javadoc inherited from super class.
    protected void openTableDataCell(
            DOMOutputBuffer dom,
            TableCellAttributes attributes) throws ProtocolException {
        Element element = dom.openStyledElement("td", attributes,
                DisplayKeywords.TABLE_CELL);

        addTitleAttribute(element, attributes, supportsTitleOnTD);

        addTableCellAttributes(element, attributes);

        addTableCellEvents(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeTableDataCell(
            DOMOutputBuffer dom,
            TableCellAttributes attributes) {
        dom.closeElement("td");
    }

    // ========================================================================
    //   Inline element methods.
    // ========================================================================

    private void addTargetAttribute(
            Element goElement,
            String target) {

        // If no target has been specified then do nothing.
        if (target != null) {

            // We need to find the outermost page context as it is its regions which
            // define the mapping to browser panes.
            MarinerPageContext outermost = context;

         // This code is broken as we now only have a singe pageContext.
         // I could fix it by using the top level dlc but Paul D says not to.
            
         //   while (outermost.getIncludingPageContext() != null) {
         //       outermost = outermost.getIncludingPageContext();
         //   }

            // The value is the name of a region, if the region does not exist then
            // we do not generate a target.
            Region region = outermost.getRegion(target);
            if (region != null) {
                if ((target = region.getDestinationArea()) != null) {
                    goElement.setAttribute("target", target);
                }
            }
        }
    }


    /**
     * In this protocol accesskeys are supported but not directly on anchor,
     * instead they are supported using special do types. This method no longer
     * calls the super class if there is no access key set as it needs to
     * generate font tags.
     *
     * @param attributes The attributes to use when generating the mark up.
     */
    public void doAnchor(
            DOMOutputBuffer dom,
            AnchorAttributes attributes) {

        String href = getRewrittenLinkFromObject(attributes.getHref(),
                                                 attributes.getSegment() != null);

        // if the link is null then make it a string with a single space to avoid
        // to prevent the href attribute from being omitted.
        if (href == null) {
            href = " ";
        }
        String content = "";
        WapTV5_WMLVersion1_3Style style = getStyle(attributes);
        if (href != null) {
            String accessKey = getPlainText(attributes.getShortcut());

            openFont(dom, attributes, true);

            // We need to render the content of the anchor as a string.
            Object contentObject = attributes.getContent();
            if (contentObject instanceof DOMOutputBuffer) {
                StringWriter contentWriter = new StringWriter();
                DocumentOutputter outputter = new DOMDocumentOutputter(
                        new XMLDocumentWriter(contentWriter),
                        characterEncoder);


                DOMOutputBuffer contentBuffer = (DOMOutputBuffer) contentObject;
                Element contentRoot = contentBuffer.getRoot();
                try {
                    outputter.output(contentRoot);
                    content = contentWriter.getBuffer().toString();
                } catch (IOException ioe) {
                    logger.error("content-generation-error", ioe);
                }
            } else {
                content = contentObject.toString();
            }

            // @todo 2005060816 annotate child with style information if it's not inherited from the parent
            Element doElement = dom.openStyledElement("do", attributes);
            if (style != null) {
                style.addMarinerFocus(doElement, "focus");
            }
            doElement.setAttribute("label", content);
            doElement.setAttribute("name", context.generateWMLActionID());

            if (supportsTabindex && attributes.getTabindex() != null) {
                doElement.setAttribute("tabindex",
                                       (String) attributes.getTabindex());
            }
            setAttribute(doElement, "type", accessKey);
            addDoAttributes(doElement, attributes);

            Element goElement = dom.openElement("go");

            MarinerURL url = null;
            Map params = null;
            boolean post = false;

            Styles styles = attributes.getStyles();
            PropertyValues propertyValues = styles.getPropertyValues();
            StyleValue styleValue;

            styleValue = propertyValues.getComputedValue(
                    StylePropertyDetails.MCS_HTTP_METHOD_HINT);
            if (styleValue == MCSHttpMethodHintKeywords.POST) {
                post = true;

                // Check to see whether the URL has any parameters, if it has
                // then override the style.
                url = new MarinerURL(href);
                params = url.getParameterMap();
                if (params == null || params.isEmpty()) {
                    post = false;
                }
            }

            if (post) {
                int index = href.lastIndexOf('?');
                if (index != -1) {
                    href = href.substring(0, index);
                }
                setAttribute(goElement, "href", href);
                goElement.setAttribute("method", "post");
                Set keys = params.keySet();
                Iterator i = keys.iterator();
                while (i.hasNext()) {
                    String keyStr = (String) i.next();
                    String[] valueStr = (String[]) params.get(keyStr);
                    if (keyStr != null && valueStr != null) {
                        for (int j = 0; j < valueStr.length; j++) {
                            Element postElement = dom.openElement("postfield");
                            postElement.setAttribute("name", keyStr);
                            postElement.setAttribute("value", valueStr[j]);
                            dom.closeElement("postfield");
                        }
                    }
                }
            } else {
                setAttribute(goElement, "href", href);
            }

            // Handle the target attribute.
            addTargetAttribute(goElement, attributes.getTarget());

            dom.closeElement(goElement);
            dom.closeElement(doElement);
            closeFont(dom);
        } else {
            // OK no Link so see if we can get some text from a fallback
            // TextAsset
            String fallbackText =
                    getTextFallbackFromLink(attributes.getHref());
            if (fallbackText != null) {
                dom.appendEncoded(fallbackText);
            }
        }
    }

    // ========================================================================
    //   Special element methods.
    // ========================================================================

    /**
     * Do the image tag for WapTV. It is possible that the image
     * may be directed to a particular pane and this catered for here
     * using the noop and setattr elements WapTV and pane destination
     * area.
     */
    protected void addImageAttributes(
            Element element,
            ImageAttributes attributes) {

        super.addImageAttributes(element, attributes);

        WapTV5_WMLVersion1_3Style style = getStyle(attributes);

        Styles styles = attributes.getStyles();
        String value;
        if ((value = mcsImageHandler.getAsString(styles)) != null) {
            element.setAttribute("localsrc", value);
        }

        if (style != null) {
            style.addMarinerImageRepeatCount(element, "loop");
            style.addMarinerImageFrameInterval(element, "ticks");
            style.addMarinerImageInitialFrame(element, "index");

        }
    }

    /**
     * Do the image tag for WapTV. It is possible that the image
     * may be directed to a particular pane and this catered for here
     * using the noop and setattr elements WapTV and pane destination
     * area.
     *
     * @param dom        The DOMOutputBuffer to which elements will be added.
     * @param attributes The ImageAttributes for this element.
     */
    protected void doImage(
            DOMOutputBuffer dom,
            ImageAttributes attributes) {

        Pane pane = attributes.getPane();
        // If the pane is not defined then get the current pane from the
        // page context
        if (pane == null) {
            pane = context.getCurrentPane();
        }
        // If the pane is still null, throw an exception.
        if (pane == null) {
            throw new IllegalArgumentException
                    ("Image does not have an output pane.");
        }

        String destinationArea = pane.getDestinationArea();
        if (destinationArea == null) {
            super.doImage(dom, attributes);
            return;
        }

        // TODO: look at what behaviour should occur here when getSrc is null.
        // Currently we will end up with a WapTV browser pane without a background
        // image.
        if (attributes.getSrc() != null) {
            dom = getExtraBuffer(PAGE_NOOP_BUFFER_NAME, true);

            Element element = dom.addStyledElement("setattr", attributes);
            element.setAttribute("name", pane.getName() + ".src");
            element.setAttribute("value", attributes.getSrc());

            element = dom.addStyledElement("setattr", attributes);
            element.setAttribute("name", "banner.visible");
            element.setAttribute("value", "true");
        } else {
            logger.warn("destination-area-image-src-missing",
                        new Object[]{attributes.getPane().getDestinationArea()});
        }
    }

    // ========================================================================
    //   Menu element methods.
    // ========================================================================

    /**
     * Override this method to support top level menus.
     *
     * @param attributes The attributes to use when generating the mark up.
     */
    public void doMenu(MenuAttributes attributes) throws ProtocolException {
        Pane pane = attributes.getPane();
        String destinationArea = pane.getDestinationArea();

        if (logger.isDebugEnabled()) {
            logger.debug("Pane is " + pane.getName()
                         + ". Destination is " + destinationArea);
        }
        if (destinationArea == null
                || !destinationArea.equals(MENU_DESTINATION_AREA)) {

            super.doMenu(attributes);
            return;
        }

        DOMOutputBuffer dom
                = getExtraBuffer(PAGE_TEMPLATE_BUFFER_NAME, true);

        Collection items = attributes.getItems();
        for (Iterator i = items.iterator(); i.hasNext();) {
            MenuChildVisitable item = (MenuChildVisitable) i.next();
            item.visit(this, dom, attributes, i.hasNext(), false,
                       MenuOrientation.HORIZONTAL);

        }
    }

    public boolean doMenuItem(
            DOMOutputBuffer dom,
            MenuAttributes attributes,
            MenuItem item) {

        String value, type, fallbackText;

        // Attempt to get th resolved href for this menu item.
        // If we can't then just append the fallback text to the DOM
        // and return
        String resolvedHref = getRewrittenLinkFromObject(item.getHref(),
                                                         item.getSegment() != null);

        if (resolvedHref == null) {
            // OK could not resolve the link. See if there is a fallback
            // TextComponent for any specified LinkComponent
            fallbackText = getTextFallbackFromLink(item.getHref());
            if (fallbackText != null) {
                // fallback text so append to output
                dom.appendEncoded(fallbackText);
            }
            // finished with this menu item
            return false;
        }

        // We have a resolved href so continue to process menu item
        if ((value = getPlainText(item.getShortcut())) != null) {
            type = value;
        } else {
            type = "accept";
        }

        WapTV5_WMLVersion1_3Style style = getStyle(attributes);
        // @todo 2005060816 annotate child with style information if it's not inherited from the parent
        Element doElement = dom.openStyledElement("do", item);
        if (style != null) {
            style.addMarinerFocus(doElement, "focus");
        }
        doElement.setAttribute("label", item.getText());
        doElement.setAttribute("type", type);
        doElement.setAttribute("name", context.generateWMLActionID());

        Element goElement = dom.openElement("go");
        setAttribute(goElement, "href", resolvedHref);

        addDoAttributes(doElement, attributes);
        // Handle the target attribute.
        addTargetAttribute(goElement, item.getTarget());

        dom.closeElement(goElement);
        dom.closeElement(doElement);
        return false;
    }
    // ========================================================================
    //   Script element methods.
    // ========================================================================

    // ========================================================================
    //   Extended function form element methods.
    // ========================================================================

    /**
     * Add the wtv_connect_now meta tag to the page head.
     */
    private void doConnectNow() {
        // Only add the meta tag once.
        if (getPageHead().getAttribute("wtv_connect_now") == null) {
            DOMOutputBuffer dom = getHeadBuffer();
            Element element = dom.addElement("meta");
            element.setAttribute("name", "wtv_connect_now");
            element.setAttribute("content", "");

            getPageHead().setAttribute("wtv_connect_now", Boolean.TRUE);
        }
    }

    /**
     * If the rows is set and is greater than 1 then set the format to the
     * number of rows.
     */
    protected void addTextInputValidation(
            Element element, XFTextInputAttributes attributes) {

        Styles styles = attributes.getStyles();
        PropertyValues propertyValues = styles.getPropertyValues();
        StyleValue styleValue = propertyValues.getComputedValue(
                StylePropertyDetails.MCS_ROWS);

        StyleInteger rowsValue = (StyleInteger) styleValue;
        int rows = rowsValue.getInteger();
        if (rows > 1) {
            element.setAttribute("format", String.valueOf(rows));
        } else {
            super.addTextInputValidation(element, attributes);
        }
    }

    /**
     * Override doBooleanInput in parent to add do connect now meta tag.
     */
    public void doBooleanInput(XFBooleanAttributes attributes)
            throws ProtocolException {
        super.doBooleanInput(attributes);

        doConnectNow();
    }

    protected void addTextInputAttributes(
            Element element,
            XFTextInputAttributes attributes) {

        super.addTextInputAttributes(element, attributes);

        Styles styles = attributes.getStyles();
        String value;
        Styles focusStyles = styles.findNestedStyles(
                StatefulPseudoClasses.FOCUS);

        if (focusStyles != null) {
            addBorderFocused(element, focusStyles);
        }
        addBorderWidth(element, styles);
        addBorderColor(element, styles);
        addBackgroundColor(element, styles);
        addBorderUnFocused(element, styles);

        if ((value = mcsImageHandler.getAsString(styles)) != null) {
            element.setAttribute("localsrc", value);
        }

        WapTV5_WMLVersion1_3Style style = getStyle(attributes);
        if (style != null) {
            style.addMarinerCaretColor(element, "cursorcolor");
        }
    }

    /**
     * Override doTextInput in parent to add do connect now meta tag.
     */
    public void doTextInput(XFTextInputAttributes attributes)
            throws ProtocolException {
        ContainerInstance entryContainerInstance;

        // If the entry container is not set then return as there is nothing
        // else we can do.
        if ((entryContainerInstance =
                attributes.getEntryContainerInstance()) == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Entry container instance not set");
            }
            return;
        }

        // Direct the markup to the entry container's content buffer.
        getCurrentBuffer(entryContainerInstance);

        super.doTextInput(attributes);

        doConnectNow();
    }


    // javadoc inherited from superclass
    protected SelectionRenderer
            getSelectionRenderer(XFSelectAttributes attributes) {

        Styles styles = attributes.getStyles();
        PropertyValues propertyValues = styles.getPropertyValues();

        StyleValue listStyle = propertyValues.getComputedValue(
                StylePropertyDetails.MCS_SELECTION_LIST_STYLE);
        SelectionRenderer renderer = null;
        if (listStyle != MCSSelectionListStyleKeywords.CONTROLS) {
            if (null == defaultSelectionRenderer) {
                defaultSelectionRenderer = new WAPTV5DefaultSelectionRenderer();
            }
            renderer = defaultSelectionRenderer;
        } else {
            if (null == controlSelectionRenderer) {
                controlSelectionRenderer = new WAPTV5ControlSelectionRenderer();
            }
            renderer = controlSelectionRenderer;
        }
        return renderer;
    }

    /**
     * Override doSelectInput in parent to add do connect now meta tag.
     * Generate checkboxes, radio buttons or options list for xf*selects
     */
    public void doSelectInput(XFSelectAttributes attributes)
            throws ProtocolException {

        ContainerInstance captionContainerInstance;
        ContainerInstance entryContainerInstance;
        String value;
        // Add the caption to the caption container.
        if ((captionContainerInstance =
                attributes.getCaptionContainerInstance()) != null &&
                (value = getPlainText(attributes.getCaption())) != null) {
            addToBuffer(captionContainerInstance, value);
        }

        // If the entry container is not set then return as there is nothing
        // else we can do.
        if ((entryContainerInstance =
                attributes.getEntryContainerInstance()) == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Entry container instance not set");
            }
            return;
        }

        DOMOutputBuffer dom = getCurrentBuffer(entryContainerInstance);

        updateSelectedOptions(attributes);

        getSelectionRenderer(attributes).renderSelection(attributes, dom);
        doConnectNow();
    }

    /**
     * Override this method to set the action do type to the access key
     * if it is specified, otherwise fallback to the super class.
     *
     * @param attributes The attributes to use when generating the mark up.
     * @return The generated mark up.
     */
    protected String getActionDoType(XFActionAttributes attributes) {

        String value;

        if ((value = getPlainText(attributes.getShortcut())) != null) {
            return value;
        }

        return super.getActionDoType(attributes);
    }

    /**
     * Override doFormAction in parent to add do connect now meta tag.
     */
    protected void doFormAction(
            DOMOutputBuffer dom,
            XFActionAttributes attributes,
            String actionType,
            boolean inline) {
        super.doFormAction(dom, attributes, actionType, inline);
        doConnectNow();
    }


    //javadoc inherited
    protected void addActionDoAttributes(
            Element element,
            XFActionAttributes attributes) {
        WapTV5_WMLVersion1_3Style style = getStyle(attributes);
        if (style != null) {
            style.addMarinerFocus(element, "focus");
        }
    }

    // Javadoc inherited.
    protected void addOptionAttributes(
            Element element,
            MCSAttributes attributes) {

        Styles styles = attributes.getStyles();
        String value;
        if ((value = mcsImageHandler.getAsString(styles)) != null) {
            element.setAttribute("localsrc", value);
        }
    }

    // Javadoc inherited.
    protected void addOptGroupAttributes(
            Element element,
            XFSelectAttributes attributes) {

        Styles styles = attributes.getStyles();
        String value;
        if ((value = mcsImageHandler.getAsString(styles)) != null) {
            element.setAttribute("localsrc", value);
        }
    }

    //javadoc inherited
    protected void addDoAttributes(
            Element element,
            MCSAttributes attributes) {

        Styles styles = attributes.getStyles();

        String value;
        if ((value = backgroundComponentHandler.getAsString(styles)) != null) {
            element.setAttribute("bglocalsrc", value);
        }
        if ((value = mcsImageHandler.getAsString(styles)) != null) {
            element.setAttribute("localsrc", value);
        }

        WapTV5_WMLVersion1_3Style style = getStyle(attributes);
        if (style != null) {
            style.addFontFamily(element, "font");
            style.addPosition(element, "pos");
            style.addBackgroundPosition(element, "bgoffset");
        }
    }

    protected final class WAPTV5DefaultSelectionRenderer
            implements SelectionRenderer {

        private XFSelectAttributes attributes;

        private final RenderingVisitor visitor;

        public WAPTV5DefaultSelectionRenderer() {
            visitor = new RenderingVisitor();
        }

        protected void initialise(XFSelectAttributes selectAttributes) {
            this.attributes = selectAttributes;
        }


        /**
         * @param attributes
         * @param buffer
         */
        public void renderSelection(
                XFSelectAttributes attributes,
                OutputBuffer buffer) throws ProtocolException {

            initialise(attributes);

            DOMOutputBuffer dom = (DOMOutputBuffer) buffer;

            openSelect(dom);

            renderOptions(dom);

            closeSelect(dom);
        }

        private void renderOptions(DOMOutputBuffer dom)
                throws ProtocolException {
            renderOptions(attributes.getOptions(), dom);
        }

        private void renderOptions(List options, DOMOutputBuffer dom)
                throws ProtocolException {
            Option option;
            for (int i = 0; i < options.size(); i++) {
                option = (Option) options.get(i);
                option.visit(visitor, dom);
            }
        }

        private void openSelect(DOMOutputBuffer dom)
                throws ProtocolException {

            openFont(dom, attributes, false);

            Element element = dom.openStyledElement("select", attributes);

            // set attributes that should be set for all form fields.
            addFormFieldAttributes(element, attributes);

            Styles styles = attributes.getStyles();
            PropertyValues propertyValues = styles.getPropertyValues();
            String value;
            if ((value = mcsImageHandler.getAsString(styles)) != null) {
                element.setAttribute("localsrc", value);
            }

            StyleValue styleValue;
            styleValue = propertyValues.getComputedValue(
                    StylePropertyDetails.MCS_SELECTION_LIST_TRIGGER);
            element.setAttribute("style", styleValue.getStandardCSS());

            String tabindex = (String) attributes.getTabindex();

            if (supportsTabindex && tabindex != null) {
                element.setAttribute("tabindex", tabindex);
            }
            if (attributes.getTitle() == null) {
                // Need to fully qualify access to outer class due to bug in
                // Sun 1.2 jdk compiler
                String title = WapTV5_WMLVersion1_3.this.
                        getPlainText(attributes.getPrompt());

                attributes.setTitle(title);
            }

            addTitleAttribute(element, attributes, true);

            element.setAttribute("name", attributes.getName());
            String multiple = attributes.isMultiple() ? "true" : "false";
            element.setAttribute("multiple", multiple);

            String ivalue =
                    initialOptionHandler.getInitialValue(attributes);
            if (ivalue != null) {
                element.setAttribute("ivalue", ivalue);
            }

        }

        private void closeSelect(DOMOutputBuffer dom) {
            dom.closeElement("select");
            closeFont(dom);
        }

        private class RenderingVisitor implements OptionVisitor {

            public void visit(
                    SelectOption option,
                    Object object) {

                DOMOutputBuffer dom = (DOMOutputBuffer) object;

                String value = option.getValue();
                // Get the caption from the object, if it is null then use the
                // value.
                // Need to fully qualify access to outer class due to bug in
                // the Sun 1.2 jdk compiler
                String caption = WapTV5_WMLVersion1_3.this.
                        getPlainText(option.getCaption());

                if (caption == null) {
                    caption = value;
                }

                // Need to fully qualify access to outer class due to bug in
                // the Sun 1.2 jdk compiler
                String title = WapTV5_WMLVersion1_3.this.
                        getPlainText(option.getPrompt());

                option.setTitle(title);

                // If this is a multiple select field then we need to make sure
                // that there are no ; in the values as ; is used as a separator.
                if (attributes.isMultiple()) {
                    value = encodeMultipleSelectValue(value);
                }

                // deleagate the work to the doOption() method
                doOption(dom, option, value, caption);
            }

            public void visit(
                    SelectOptionGroup optionGroup,
                    Object object) throws ProtocolException {

                DOMOutputBuffer dom = (DOMOutputBuffer) object;

                // open the option group
                Element element = dom.openStyledElement("optgroup",
                                                        optionGroup);

                addOptGroupAttributes(element, attributes);

                // Need to fully qualify access to outer class due to bug in
                // the Sun 1.2 jdk compiler
                String title = WapTV5_WMLVersion1_3.this.
                        getPlainText(optionGroup.getCaption());

                if (title == null) {
                    // Need to fully qualify access to outer class due to bug
                    // in the Sun 1.2 jdk compiler
                    title = WapTV5_WMLVersion1_3.this.
                            getPlainText(optionGroup.getPrompt());
                }
                if (title != null) {
                    element.setAttribute("title", title);
                }

                // render any enclosed options
                renderOptions(optionGroup.getSelectOptionList(), dom);

                // close the option group
                dom.closeElement("optgroup");

            }

        }
    }

    protected final class WAPTV5ControlSelectionRenderer
            implements SelectionRenderer {

        private final RenderingVisitor visitor;

        private XFSelectAttributes selectAttributes;

        private boolean verticalOptions;

        private int optionCount;

        public WAPTV5ControlSelectionRenderer() {
            visitor = new RenderingVisitor();
        }

        /**
         * Initialise the member variables required when rendering a selection.
         * Member variables are required as we can't pass all the date
         * to the OptionVisitor.
         *
         * @param attributes the XFSelectAttributes
         */
        protected void initialise(XFSelectAttributes attributes) {

            selectAttributes = attributes;

            Styles styles = attributes.getStyles();
            PropertyValues propertyValues = styles.getPropertyValues();
            StyleValue styleValue = propertyValues.getComputedValue(
                    StylePropertyDetails.MCS_MENU_ORIENTATION);

            this.verticalOptions =
                    (styleValue == MCSMenuOrientationKeywords.VERTICAL);

            optionCount = 0;
        }

        // javadoc inherited from interface
        public void renderSelection(
                XFSelectAttributes attributes,
                OutputBuffer buffer) throws ProtocolException {

            initialise(attributes);
            DOMOutputBuffer dom = (DOMOutputBuffer) buffer;

            renderOptions(dom, attributes);
        }

        private void renderOptions(
                DOMOutputBuffer dom,
                XFSelectAttributes selectAttributes)
                throws ProtocolException {
            renderOptions(selectAttributes.getOptions(), dom);
        }

        private void renderOptions(List options, DOMOutputBuffer dom)
                throws ProtocolException {
            Option option;
            for (int i = 0; i < options.size(); i++) {
                option = (Option) options.get(i);
                option.visit(visitor, dom);
            }
        }

        private class RenderingVisitor implements OptionVisitor {

            public void visit(SelectOption option, Object object) {

                // Need to fully qualify access to outer class due to bug in
                // the Sun 1.2 jdk compiler
                String title = WapTV5_WMLVersion1_3.this.
                        getPlainText(option.getPrompt());

                option.setTitle(title);

                String value = option.getValue();

                // Get the caption from the object, if it is null then use the
                // value.
                // Need to fully qualify access to outer class due to bug in
                // the Sun 1.2 jdk compiler
                String caption = WapTV5_WMLVersion1_3.this.
                        getPlainText(option.getCaption());

                if (null == caption) {
                    caption = value;
                }

                //Pane captionPane = option.getCaptionContainerInstance();

                boolean writeSeperator = (optionCount++ > 0);

                DOMOutputBuffer dom = (DOMOutputBuffer) object;

                // if we need to write out a seperator then do it now
                if (writeSeperator) {
                    if (verticalOptions) {
                        addVerticalMenuItemSeparator(dom);
                    } else {
                        addHorizontalMenuItemSeparator(dom);
                    }
                }

                // write out the option

                // the option is either written to dedicated container instance
                // or the current buffer
                ContainerInstance entryContainerInstance =
                        option.getEntryContainerInstance();
                // Need to fully qualify access to outer class due to bug in
                // in the Sun 1.2 jdk compiler
                DOMOutputBuffer optionBuffer =
                        (null != entryContainerInstance) ?
                        WapTV5_WMLVersion1_3.this.getCurrentBuffer(
                                entryContainerInstance)
                        : dom;

                Element element = optionBuffer.addStyledElement("input",
                                                                option);

                // set attributes that should be set for all form fields.
                addFormFieldAttributes(element, selectAttributes);

                String tabindex = (String) selectAttributes.getTabindex();
                if (supportsTabindex && tabindex != null) {
                    element.setAttribute("tabindex", tabindex);
                }

                addTitleAttribute(element, option, true);

                String selectType = selectAttributes.isMultiple()
                        ? "checkbox" : "radio";

                element.setAttribute("type", selectType);
                element.setAttribute("name", selectAttributes.getName());
                element.setAttribute("value", value);
                element.setAttribute("label", caption);
                // set the pos attribute to align the label.
                // TODO element.setAttribute("pos", ???);
            }

            public void visit(
                    SelectOptionGroup optionGroup,
                    Object object) throws ProtocolException {
                // control options cannot be enclosed in an optgroup.
                // just write out any nested options
                renderOptions(optionGroup.getSelectOptionList(),
                              (DOMOutputBuffer) object);

            }

        }
    }

    // javadoc inherited
    public void writeInitialFocus(String tabindex) {
        if ((initialFocusElement != null) && (tabindex != null)) {
            initialFocusElement.setAttribute("tabindex", tabindex);
        }
    }


    // javadoc inherited
    protected void writeDocument(PackageBodyOutput output)
            throws IOException {

        /* No dissection required as there is no maximum deck size.
         * If dissection was required, we would be in a spot of bother as we
         * have yet to plug the new dissector into the MCSDOM.
         */
        Writer writer = output.getWriter();
        createDocumentOutputter(writer).output(document);
    }

    // javadoc inherited
    public CharacterEncoder getCharacterEncoder() {
        return this.characterEncoder;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-05	10701/1	pduffin	VBM:2005110905 Porting forward changes from 3.5

 08-Dec-05	10675/3	pduffin	VBM:2005110905 Ported forward some white space fixes from 3.2.3

 07-Dec-05	10321/4	emma	VBM:2005103109 Supermerge required

 15-Nov-05	10321/1	emma	VBM:2005103109 Forward port: Styling not applied correctly to some xf selectors

 14-Nov-05	10300/1	emma	VBM:2005103109 Styling not applied correctly to some xf selectors

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/2	pduffin	VBM:2005111405 Massive changes for performance

 25-Nov-05	10453/1	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 25-Nov-05	9708/3	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 08-Aug-05	9205/1	rgreenall	VBM:2005062107 Forward port of VBM:2005062107

 25-Nov-05	9708/3	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 08-Aug-05	9205/1	rgreenall	VBM:2005062107 Forward port of VBM:2005062107

 03-Oct-05	9600/7	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 30-Sep-05	9600/4	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 29-Sep-05	9600/1	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 02-Oct-05	9590/6	schaloner	VBM:2005092204 Migrated XMLLayoutAccessor and XMLDeviceLayoutAccessor to JiBX

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 02-Oct-05	9652/2	gkoch	VBM:2005092204 Tests for layoutFormat marshaller/unmarshaller

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 15-Sep-05	9524/1	emma	VBM:2005091503 Added ContainerInstance to allow regions and panes to be treated in the same way

 12-Sep-05	9372/1	ianw	VBM:2005082221 Allow only one instance of MarinerPageContext for a page

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 30-Aug-05	9353/2	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 19-Aug-05	9289/4	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 18-Aug-05	9007/4	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 10-Aug-05	9187/1	tom	VBM:2005080509 release of 2005080509 [extract WML default styles into a CSS file]

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 23-Jun-05	8833/4	pduffin	VBM:2005042901 Addressing review comments

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 17-Jun-05	8830/1	pduffin	VBM:2005042901 Merging from 3.2.3 - Added support for specifying symbols, punctuation marks, or numbers in a text format

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 09-Jun-05	8665/4	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 09-Jun-05	8665/2	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 05-Apr-05	7513/1	geoff	VBM:2003100606 DOMOutputBuffer allows creation of text which renders incorrectly in WML

 16-Mar-05	7372/2	emma	VBM:2005031008 Make cols attribute optional on the XDIME table element

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/7	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 26-Nov-04	6076/4	tom	VBM:2004101509 Modified protocols to get their styles from MCSAttributes

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 20-Jul-04	4897/3	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 14-Jul-04	4783/8	geoff	VBM:2004062302 Implementation of theme style options: WML Family (fixes after style inversion approval)

 12-Jul-04	4783/4	geoff	VBM:2004062302 Implementation of theme style options: WML Family

 13-Jul-04	4752/1	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities

 01-Jul-04	4778/1	allan	VBM:2004062912 Use the Volantis.pageURLRewriter to rewrite page urls

 01-Jun-04	4616/1	geoff	VBM:2004060103 Add proper support for protocol page debug outputter

 01-Jun-04	4614/1	geoff	VBM:2004060103 Add proper support for protocol page debug outputter

 06-May-04	4153/1	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 25-Feb-04	2974/4	steve	VBM:2004020608 supermerged

 17-Feb-04	2974/1	steve	VBM:2004020608 SGML Quote handling

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 19-Dec-03	2275/1	steve	VBM:2003121601 Dollar encoding in WAP TV - Merged from Proteus2

 19-Dec-03	2263/1	steve	VBM:2003121601 Dollar encoding on WAPTV

 26-Oct-03	1648/1	steve	VBM:2003090305 WapTV Doctype header

 02-Oct-03	1469/2	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 10-Sep-03	1301/2	byron	VBM:2003082107 Support Openwave GUI Browser extensions

 21-Aug-03	1240/3	chrisw	VBM:2003070811 implemented rework

 21-Aug-03	1240/1	chrisw	VBM:2003070811 Ported emulation of CSS2 border-spacing from mimas to proteus

 21-Aug-03	1219/3	chrisw	VBM:2003070811 Ported emulation of CSS2 border-spacing from metis to mimas

 20-Aug-03	1152/1	chrisw	VBM:2003070811 Emulate CSS2 border-spacing using cellspacing on table element

 17-Aug-03	1052/1	allan	VBM:2003073101 Support styles on menu and menuitems

 20-Jun-03	464/1	chrisw	VBM:2003061907 WapTV output is no longer routed throught the wml pipeline

 06-Jun-03	335/3	mat	VBM:2003042906 Merged changes to MCS

 06-Jun-03	208/3	byron	VBM:2003051903 Commit after conflict resolution

 ===========================================================================
*/

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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.vdxml;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.DocType;
import com.volantis.mcs.dom.XMLDeclaration;
import com.volantis.mcs.dom.MarkupFamily;
import com.volantis.mcs.dom.output.DOMDocumentOutputter;
import com.volantis.mcs.dom.output.DocumentOutputter;
import com.volantis.mcs.dom.output.XMLDocumentWriter;
import com.volantis.mcs.layouts.DissectingPane;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.FormatVisitorAdapter;
import com.volantis.mcs.layouts.FormatVisitorException;
import com.volantis.mcs.layouts.Fragment;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.AddressAttributes;
import com.volantis.mcs.protocols.AnchorAttributes;
import com.volantis.mcs.protocols.BigAttributes;
import com.volantis.mcs.protocols.BlockQuoteAttributes;
import com.volantis.mcs.protocols.BodyAttributes;
import com.volantis.mcs.protocols.BoldAttributes;
import com.volantis.mcs.protocols.CanvasAttributes;
import com.volantis.mcs.protocols.CiteAttributes;
import com.volantis.mcs.protocols.CodeAttributes;
import com.volantis.mcs.protocols.ColumnIteratorPaneAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DOMTransformer;
import com.volantis.mcs.protocols.DefinitionDataAttributes;
import com.volantis.mcs.protocols.DefinitionListAttributes;
import com.volantis.mcs.protocols.DefinitionTermAttributes;
import com.volantis.mcs.protocols.DissectingPaneAttributes;
import com.volantis.mcs.protocols.DivAttributes;
import com.volantis.mcs.protocols.EmphasisAttributes;
import com.volantis.mcs.protocols.FormAttributes;
import com.volantis.mcs.protocols.FraglinkAttributes;
import com.volantis.mcs.protocols.FragmentLinkRenderer;
import com.volantis.mcs.protocols.GridAttributes;
import com.volantis.mcs.protocols.GridChildAttributes;
import com.volantis.mcs.protocols.GridRowAttributes;
import com.volantis.mcs.protocols.HeadingAttributes;
import com.volantis.mcs.protocols.HorizontalRuleAttributes;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.ItalicAttributes;
import com.volantis.mcs.protocols.KeyboardAttributes;
import com.volantis.mcs.protocols.LayoutAttributes;
import com.volantis.mcs.protocols.LineBreakAttributes;
import com.volantis.mcs.protocols.ListItemAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.MenuAttributes;
import com.volantis.mcs.protocols.MonospaceFontAttributes;
import com.volantis.mcs.protocols.MontageAttributes;
import com.volantis.mcs.protocols.NativeMarkupAttributes;
import com.volantis.mcs.protocols.NoScriptAttributes;
import com.volantis.mcs.protocols.Option;
import com.volantis.mcs.protocols.OptionVisitor;
import com.volantis.mcs.protocols.OrderedListAttributes;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.ParagraphAttributes;
import com.volantis.mcs.protocols.PhoneNumberAttributes;
import com.volantis.mcs.protocols.PreAttributes;
import com.volantis.mcs.protocols.ProtocolCharacterEncoder;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.RowIteratorPaneAttributes;
import com.volantis.mcs.protocols.SampleAttributes;
import com.volantis.mcs.protocols.ScriptAttributes;
import com.volantis.mcs.protocols.SegmentAttributes;
import com.volantis.mcs.protocols.SegmentGridAttributes;
import com.volantis.mcs.protocols.SelectOption;
import com.volantis.mcs.protocols.SelectOptionGroup;
import com.volantis.mcs.protocols.SelectionRenderer;
import com.volantis.mcs.protocols.SlideAttributes;
import com.volantis.mcs.protocols.SmallAttributes;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.SpatialFormatIteratorAttributes;
import com.volantis.mcs.protocols.StrongAttributes;
import com.volantis.mcs.protocols.StyleAttributes;
import com.volantis.mcs.protocols.SubscriptAttributes;
import com.volantis.mcs.protocols.SuperscriptAttributes;
import com.volantis.mcs.protocols.TableAttributes;
import com.volantis.mcs.protocols.TableBodyAttributes;
import com.volantis.mcs.protocols.TableCellAttributes;
import com.volantis.mcs.protocols.TableFooterAttributes;
import com.volantis.mcs.protocols.TableHeaderAttributes;
import com.volantis.mcs.protocols.TableRowAttributes;
import com.volantis.mcs.protocols.UnderlineAttributes;
import com.volantis.mcs.protocols.UnorderedListAttributes;
import com.volantis.mcs.protocols.XFActionAttributes;
import com.volantis.mcs.protocols.XFBooleanAttributes;
import com.volantis.mcs.protocols.XFFormAttributes;
import com.volantis.mcs.protocols.XFFormFieldAttributes;
import com.volantis.mcs.protocols.XFImplicitAttributes;
import com.volantis.mcs.protocols.XFSelectAttributes;
import com.volantis.mcs.protocols.XFTextInputAttributes;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.forms.ActionFieldType;
import com.volantis.mcs.protocols.forms.EmulatedXFormDescriptor;
import com.volantis.mcs.protocols.forms.FieldHandler;
import com.volantis.mcs.protocols.forms.MultipleSelectFieldType;
import com.volantis.mcs.protocols.layouts.ContainerInstance;
import com.volantis.mcs.protocols.menu.MenuModule;
import com.volantis.mcs.protocols.menu.MenuModuleRendererFactory;
import com.volantis.mcs.protocols.menu.MenuModuleRendererFactoryFilter;
import com.volantis.mcs.protocols.menu.shared.DefaultMenuModule;
import com.volantis.mcs.protocols.vdxml.menu.DeprecatedExternalLinkOutput;
import com.volantis.mcs.protocols.vdxml.menu.VDXMLMenuModuleRendererFactory;
import com.volantis.mcs.protocols.vdxml.style.VDXMLStyleFactory;
import com.volantis.mcs.protocols.vdxml.style.values.VDXMLColorValue;
import com.volantis.mcs.runtime.URLConstants;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.themes.StyleColor;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StylePair;
import com.volantis.mcs.themes.StylePercentage;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.TextAlignKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.styling.values.PropertyValues;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

/**
 * This protocol implements VDXML 2.0.x required to support Minitel devices.
 * It commonly generates pseudo markup for the various XDIME and layout
 * constructs which is then converted into the final VDXML by the protocol's
 * associated transformer.
 *
 * @todo update VDXML styling to use the style container rather than StyleProperties
 */
public final class VDXMLVersion2_0 extends DOMProtocol
        implements DeprecatedExternalLinkOutput {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(VDXMLVersion2_0.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(VDXMLVersion2_0.class);

    /**
     * Layouts etc. are specified in terms of pixel sizings but VDXML is
     * aimed at character based devices.  Therefore there needs to be a
     * translation from one to the other.  This constant provides the
     * necessary value for that to be done.
     */
    private static final int PIXELS_PER_CHAR = 8;

    /**
     * Because links need to appear at the top of the VDXML page then a
     * link buffer is used to collate this stuff together during page
     * generation.  This is a the name of that buffer.
     */
    private static final String PAGE_LINKS_BUFFER_NAME = "LINK_BUFFER";

    /**
     * A means of identifying a help zone destination in the layout.
     */
    public static final String HELP_ZONE_DESTINATION = "ZONEAIDE";

    /**
     * A means of identifying a navigation destination in the layout.
     */
    public static final String NAVIGATION_DESTINATION = "NAVIG";

    /**
     * A means of identifying a fragment links destination in the layout.
     */
    private static final String FRAGMENT_LINKS_DESTINATION = "FragmentLinks";

    /**
     * A reference to any pane with a destination of {@link
     * #HELP_ZONE_DESTINATION} in the current request (be that entire layout or
     * fragment). If this is null then there is no such pane in the
     * layout/fragment. Knowing this is necessary for generating help and
     * without it no help tags should be output.
     */
    private Pane helpZonePane = null;

    /**
     * A reference to any pane with a destination of {@link
     * #NAVIGATION_DESTINATION} in the current request (be that entire layout
     * or fragment).  If this is null then there is no such pane in the
     * layout/fragment.
     */
    private Pane navigationPane = null;

    /**
     * A reference to any pane with a destination of {@link
     * #FRAGMENT_LINKS_DESTINATION} in the current request (be that entire
     * layout or fragment).  If this is null then there is no such pane in the
     * layout/fragment.
     */
    private Pane fragmentLinksPane = null;

    /**
     * A flag that indicates whether the protocol has already looked for the
     * special panes or not.
     */
    private boolean foundPanes = false;

    /**
     * A shared transformer instance. This could be a compound transformer
     * (VDXMLTransformer and VDXMLXDIMETransformer). 
     */
    private static final DOMTransformer transformer = new VDXMLTransformer(
            new VDXMLXDIMETransformer());

    private static final VDXMLStyleFactory styleFactory = new VDXMLStyleFactory();

    /**
     * Initializes the new instance.
     */
    public VDXMLVersion2_0(ProtocolSupportFactory protocolSupportFactory,
            ProtocolConfiguration protocolConfiguration) {
        super(protocolSupportFactory, protocolConfiguration);

        quoteTable[160] = "&nbsp;";

        characterEncoder = new ProtocolCharacterEncoder(this);

        // Ensure we render the fragment links before the fragment itself
        // so that the layout rendering finds them.
        supportsFragmentLinkListTargetting = true;
    }

    // JavaDoc inherited
    private void findSpecialPanes() {

        if (!foundPanes) {
            foundPanes = true;
            VDXMLSpecialPaneVisitor visitor = new VDXMLSpecialPaneVisitor();

            try {
                Fragment currentFragment = context.getCurrentFragment();

                if (currentFragment != null) {
                    // Check the children of this fragment
                    currentFragment.visitChildren(visitor, null);
                } else {
                    // This is not a fragmented layout so use the layout root
                    RuntimeDeviceLayout layout = context.getDeviceLayout();
                    layout.getRootFormat().visit(visitor, null);
                }
            } catch (FormatVisitorException fve) {
                // Do nothing - if we can't visit we can't set a pane!
            }

            if (helpZonePane != null) {
                // Fake output into the output buffer associated with this pane
                // to force generation of this pane in the output. The
                // assumption is that this pane is not in a format iterator!
                addComment(getCurrentBuffer(helpZonePane),
                           HELP_ZONE_DESTINATION);
            }

            if (navigationPane != null) {
                // Fake output into the output buffer associated with this pane
                // to force generation of this pane in the output. The
                // assumption is that this pane is not in a format iterator!
                addComment(getCurrentBuffer(navigationPane),
                           NAVIGATION_DESTINATION);
            }
        }
    }

    // javadoc inherited
    protected void doProtocolString(Document document) {
        DocType docType = domFactory.createDocType(
                "VDXML", null, "vdxml.dtd", null,
                MarkupFamily.XML);
        document.setDocType(docType);

        addXMLDeclaration(document);
    }

    // javadoc inherited
    public String defaultMimeType() {
        return "text/vdxml";
    }

    // javadoc inherited
    protected DOMTransformer getDOMTransformer() {
        return transformer;
    }

    // javadoc inherited
    public DocumentOutputter createDocumentOutputter(Writer writer) {
        return new DOMDocumentOutputter(
                new VDXMLDocumentWriter(new XMLDocumentWriter(writer)),
                getCharacterEncoder());
    }

    // javadoc inherited
    public DocumentOutputter createDebugDocumentOutputter(Writer writer) {
        return new DOMDocumentOutputter(new XMLDocumentWriter(writer),
                                        getCharacterEncoder());
    }

    /**
     * Used to warn that the XDIME markup represented by the given attributes
     * is not supported by this protocol.
     *
     * @param attributes the attributes for the XDIME markup that is not
     *                   supported
     */
    private void warn(MCSAttributes attributes) {
        if (logger.isWarnEnabled()) {
            logger.warn("usage-not-support", attributes.getTagName());
        }
    }

    /**
     * Used to flag that the layout format represented by the given attributes
     * is not supported by this protocol. Throws a runtime exception.
     *
     * @param attributes the attributes for the layout format that is not
     *                   supported
     */
    private void error(MCSAttributes attributes) {
        logger.error("usage-not-support", attributes.getTagName());

        throw new ExtendedRuntimeException(
                    exceptionLocalizer.format("usage-not-support",
                                              attributes.getTagName()));
    }

    /**
     * Get the number of characters, as a string, equivalent to the given
     * length. The length must be a pixels value or must be 100% otherwise an
     * exception is thrown. The length should also be a multiple of {@link
     * #PIXELS_PER_CHAR}.
     *
     * @param value the length value to be converted. May be null
     * @return a string of the number of characters required to match the given
     *         length. May be null
     */
    private String convertToCharCount(StyleValue value) {
        String pixels = null;

        if (value != null) {
            if (value instanceof StyleLength) {
                StyleLength length = (StyleLength)value;

                pixels = convertToCharCount(length.pixels());
            } else if (value instanceof StylePercentage) {
                StylePercentage percent = (StylePercentage)value;
                validatePercentageValue(percent);
            } else if (value instanceof StylePair) {
                StylePair stylePair = (StylePair) value;
                String firstCharCount =
                        convertToCharCount(stylePair.getFirst());
                String secondCharCount =
                        convertToCharCount(stylePair.getSecond());

                pixels = Integer.toString(Integer.parseInt(firstCharCount) +
                        Integer.parseInt(secondCharCount));
            }
        }

        return pixels;
    }

    /**
     * Validates that the supplied percentage value is 100%.  If this is
     * not true then this method will throw an IllegalStateException.
     *
     * @param percent the percentage to be tested for being equal
     * to 100%.
     */
    private void validatePercentageValue(StylePercentage percent) {

        if (percent.getPercentage() != 100) {
            logger.error("percentage-other-than-found",
                                 new Object[] {"100%", new Double(
                                             percent.getPercentage())});

            throw new IllegalStateException(
                                "A size percentage value other than 100% " +
                                "has been found (" +
                                percent.getPercentage() + ")");
         }
    }

    /**
     * Get the number of characters, as a string, equivalent to the given
     * number of pixels. The latter should be a multiple of {@link
     * #PIXELS_PER_CHAR}.
     *
     * @param pixels the number of pixels to be converted into a number of
     *               characters
     * @return the number of characters equivalent to the given number of
     *         pixels
     */
    private String convertToCharCount(int pixels) {
        if ((pixels % PIXELS_PER_CHAR != 0) &&
                logger.isWarnEnabled()) {
                logger.warn("pixel-need-to-be-exact",
                            new Object[] { new Integer(PIXELS_PER_CHAR),
                                           new Integer(pixels)});
        }

        return Integer.toString(pixels / PIXELS_PER_CHAR);
    }

    /**
     * Get the VDXML colour name equivalent to the given colour. Will return
     * null if the colour cannot be mapped across.
     *
     * @param colour the colour to be converted
     * @return the VDXML colour name equivalent to the given colour. Could be
     *         null
     */
    private String convertToColourName(StyleColor colour) {
        final VDXMLColorValue color = styleFactory.getColor(colour);
        String result = null;

        if (color != null) {
            result = color.toString();
        }

        return result;
    }

    /**
     * Gets the VDXML align keyword equivalent to the given alignment. Null is
     * returned if the value is not valid for VDXML.
     *
     * @param textAlign the text align keyword to be translated
     * @return the equivalent alignment string. May be null
     */
    private String convertToAlign(StyleKeyword textAlign) {
        String align = null;
        if (textAlign == TextAlignKeywords.CENTER) {
            align = "CENTER";
        } else if (textAlign == TextAlignKeywords.LEFT) {
            align = "LEFT";
        } else if (textAlign == TextAlignKeywords.RIGHT) {
            align = "RIGHT";
        }
        return align;
    }

    /**
     * Returns the format's name if it has one or the tag name otherwise.
     *
     * @param format     the format who's name should be returned
     * @param attributes the attributes containing the tag name
     * @return a format or tag name
     */
    private String getFormatName(Format format,
                                   MCSAttributes attributes) {
        String formatName;

        if ((formatName = format.getName()) == null) {
            formatName = attributes.getTagName();
        }

        return formatName;
    }

    /**
     * Adds the align attribute to the given element if the value is
     * appropriate.
     *
     * @param element    the element to be updated with the align attribute
     * @param value      the value to be interpreted and set as the align
     *                   attribute value
     * @param formatName the name of the format for which the alignment is to
     *                   be added
     */
    private void addAlignAttribute(Element element,
                                     StyleValue value,
                                     String formatName) {
        if (value != null) {
            String align = convertToAlign((StyleKeyword)value);

            if (align != null) {
                element.setAttribute(VDXMLConstants.ALIGN_ATTRIBUTE,
                                     align);
            } else if (logger.isWarnEnabled()) {
                logger.warn("horizontal-align-invalid-for-format", formatName);
            }
        }
    }

    /**
     * Adds the specified attribute to the given element if the value is
     * appropriate for addition.
     *
     * @param element    the element to be updated
     * @param value      the value to be converted into a VDXML colour
     * @param formatName the name of the format for which the colour is being
     *                   added
     * @param attribute  the attribute to be added
     */
    private void addColourAttribute(Element element,
                                      StyleValue value,
                                      String formatName,
                                      String attribute) {
        addColourAttribute(element, value, formatName, attribute, false);
    }

    /**
     * Adds the specified attribute to the given element if the value is
     * appropriate for addition.
     *
     * @param element    the element to be updated
     * @param value      the value to be converted into a VDXML colour
     * @param formatName the name of the format for which the colour is being
     *                   added
     * @param attribute  the attribute to be added
     */
    private void addColourAttribute(Element element,
                                      StyleValue value,
                                      String formatName,
                                      String attribute,
                                      boolean inherit) {
        if (value != null) {


            String colour = null;
            if (value instanceof StyleColor) {
                colour = convertToColourName((StyleColor)value);
            }

            if (colour != null) {
                element.setAttribute(
                        attribute,
                        colour);
                inherit = false;
            } else if (logger.isWarnEnabled()) {
                logger.warn("incompatable-backgroud-color-for-format",
                            formatName);
            }
        }

        if (inherit) {
            String colour = getInheritedColour(element, attribute);

            if (colour != null) {
                element.setAttribute(attribute, colour);
            }
        }
    }

    // Javadoc inherited.
    protected FragmentLinkRenderer getFragmentLinkRenderer(
            FraglinkAttributes attrs) {

        return new VDXMLFragmentLinkRenderer(
                new VDXMLFragmentLinkRendererContext(this));
    }

    /**
     * Returns the special output buffer where the text associated with
     * fragment links which are part of a parent/peer link list should be
     * rendered into.
     *
     * @param fragmentName the name of the fragment we are rendering links for.
     * @return the buffer to render fragment links into.
     * @throws ProtocolException
     */
    public DOMOutputBuffer getFragmentLinksBuffer(String fragmentName)
            throws ProtocolException {

        // Ensure that the fragment contained a pane with the special
        // destination area for the fragment links.
        if (fragmentLinksPane == null) {
            throw new ProtocolException("No pane with destination area of " +
                    VDXMLVersion2_0.FRAGMENT_LINKS_DESTINATION +
                    " was found for fragment " + fragmentName);
        }

        // Return the output buffer for that special pane.
        return getCurrentBuffer(fragmentLinksPane);
    }

    /**
     * Used to open a block style markup element.
     *
     * @param dom        the output buffer to which the markup should be
     *                   written
     * @param attributes the attributes associated with the element to be
     *                   written
     */
    private void openBlock(DOMOutputBuffer dom,
                             MCSAttributes attributes) {
        openStyled(dom, VDXMLConstants.PSEUDO_BLOCK_ELEMENT, attributes);
    }

    /**
     * Used to close a block style markup element.
     *
     * @param dom        the output buffer to which the markup should be
     *                   written
     * @param attributes the attributes associated with the element to be
     *                   written
     */
    private void closeBlock(DOMOutputBuffer dom,
                              MCSAttributes attributes) {
        dom.closeElement(VDXMLConstants.PSEUDO_BLOCK_ELEMENT);
    }

    /**
     * Used to open an inline markup element.
     *
     * @param dom        the output buffer to which the markup should be
     *                   written
     * @param attributes the attributes associated with the element to be
     *                   written
     */
    private void openInline(DOMOutputBuffer dom,
                              MCSAttributes attributes) {
        openStyled(dom, VDXMLConstants.PSEUDO_INLINE_ELEMENT, attributes);
    }

    /**
     * Used to close an inline markup element.
     *
     * @param dom        the output buffer to which the markup should be
     *                   written
     * @param attributes the attributes associated with the element to be
     *                   written
     */
    private void closeInline(DOMOutputBuffer dom,
                               MCSAttributes attributes) {
        dom.closeElement(VDXMLConstants.PSEUDO_INLINE_ELEMENT);
    }

    /**
     * Open a styled pseudo element, this will be either a block or inline
     * element.
     *
     * @param dom        the output buffer to which the markup should be
     *                   written
     * @param pseudoElementName the name of the pseudo element to open.
     * @param attributes the attributes associated with the element to be
     *                   written
     */
    private void openStyled(DOMOutputBuffer dom, String pseudoElementName,
            MCSAttributes attributes) {

        dom.openStyledElement(pseudoElementName, attributes);
    }


    // javadoc inherited
    protected void openAddress(DOMOutputBuffer dom,
                               AddressAttributes attributes)
            throws ProtocolException {
        openBlock(dom, attributes);
    }

    // javadoc inherited
    protected void closeAddress(DOMOutputBuffer dom,
                                AddressAttributes attributes) {
        closeBlock(dom, attributes);
    }

    // javadoc inherited
    public void openAnchor(DOMOutputBuffer dom,
                              AnchorAttributes attributes)
            throws ProtocolException {
        openInline(dom, attributes);
    }

    // javadoc inherited
    public void closeAnchor(DOMOutputBuffer dom,
                               AnchorAttributes attributes) {
        closeInline(dom, attributes);
    }

    // javadoc inherited
    protected void openBig(DOMOutputBuffer dom,
                           BigAttributes attributes) throws ProtocolException {
        openInline(dom, attributes);
    }

    // javadoc inherited
    protected void closeBig(DOMOutputBuffer dom,
                            BigAttributes attributes) {
        closeInline(dom, attributes);
    }

    // javadoc inherited
    protected void openBlockQuote(DOMOutputBuffer dom,
                                  BlockQuoteAttributes attributes)
            throws ProtocolException {
        openBlock(dom, attributes);
    }

    // javadoc inherited
    protected void closeBlockQuote(DOMOutputBuffer dom,
                                   BlockQuoteAttributes attributes) {
        closeBlock(dom, attributes);
    }

    // javadoc inherited
    protected void openBody(DOMOutputBuffer dom,
                            BodyAttributes attributes)
            throws ProtocolException {
        // Intentionally does nothing
    }

    // javadoc inherited
    protected void closeBody(DOMOutputBuffer dom,
                             BodyAttributes attributes) {
        // Intentionally does nothing
    }

    // javadoc inherited
    protected void openBold(DOMOutputBuffer dom,
                            BoldAttributes attributes)
            throws ProtocolException {
        openInline(dom, attributes);
    }

    // javadoc inherited
    protected void closeBold(DOMOutputBuffer dom,
                             BoldAttributes attributes) {
        closeInline(dom, attributes);
    }

    // javadoc inherited
    protected void openCanvas(DOMOutputBuffer dom,
                              CanvasAttributes attributes)
            throws ProtocolException {
        findSpecialPanes();
        Styles styles = attributes.getStyles();

        Element element = dom.openElement(VDXMLConstants.VDXML_ELEMENT);

        if (styles != null) {
            addColourAttribute(element,
                    styles.getPropertyValues().getComputedValue(
                            StylePropertyDetails.BACKGROUND_COLOR),
                    "canvas", VDXMLConstants.BACKGROUND_COLOUR_ATTRIBUTE);

            // @todo later store the style properties to allow the inline and block style processing to pick up the default values
        }

        // Add reload functionality to the page
        dom = getExtraBuffer(PAGE_LINKS_BUFFER_NAME, true);
        
        String url = context.getRootPageURL(false).getExternalForm();
        Element reloadElement = dom.addElement(VDXMLConstants.LINK_ELEMENT);
        reloadElement.setAttribute(VDXMLConstants.URL_ATTRIBUTE, url);
        reloadElement.setAttribute(VDXMLConstants.FUNCTION_ATTRIBUTE,
                                   VDXMLConstants.RELOAD_FUNCTION_STRING);
    }

    // javadoc inherited
    protected void closeCanvas(DOMOutputBuffer dom,
                               CanvasAttributes attributes) {
        dom.closeElement(VDXMLConstants.VDXML_ELEMENT);
    }

    // javadoc inherited
    protected void openCite(DOMOutputBuffer dom,
                            CiteAttributes attributes)
            throws ProtocolException {
        openInline(dom, attributes);
    }

    // javadoc inherited
    protected void closeCite(DOMOutputBuffer dom,
                             CiteAttributes attributes) {
        closeInline(dom, attributes);
    }

    // javadoc inherited
    protected void openCode(DOMOutputBuffer dom,
                            CodeAttributes attributes)
            throws ProtocolException {
        openInline(dom, attributes);
    }

    // javadoc inherited
    protected void closeCode(DOMOutputBuffer dom,
                             CodeAttributes attributes) {
        closeInline(dom, attributes);
    }

    // javadoc inherited
    protected void openColumnIteratorPane(
            DOMOutputBuffer dom,
            ColumnIteratorPaneAttributes attributes) {
        Element element = dom.openElement(VDXMLConstants.PSEUDO_GRID_ELEMENT);

        addColumnIteratorPaneAttributes(element, attributes);

        // No row height written to this; the assumption is that the pane has
        // the required height
        dom.openElement(VDXMLConstants.PSEUDO_ROW_ELEMENT);
    }

    /**
     * Add attributes from a column iterator pane to the element provided.
     *
     * @param element    The element on which to set the attributes
     * @param attributes The attributes from which to extract values
     */
    private void addColumnIteratorPaneAttributes(
            Element element,
            ColumnIteratorPaneAttributes attributes) {

        addGridAttributes(element, attributes, attributes.getPane());
    }

    /**
     * Add various attributes given an element and some attributes.  Colour,
     * padding, spacing, and alignment attributes are all set on the element
     * based on the other parameters provided.
     *
     * @param element           The element on which to set the attributes
     * @param attributes        The attributes from which to extract values
     * @param format            The format for which the styles should be used
     */
    private void addGridAttributes(Element element,
                                     MCSAttributes attributes,
                                     Format format) {

        Styles styles = attributes.getStyles();
        PropertyValues propertyValues = styles.getPropertyValues();

        StyleValue value = propertyValues.getComputedValue(
                    StylePropertyDetails.BACKGROUND_COLOR);
        StyleValue align = propertyValues.getComputedValue(
                    StylePropertyDetails.TEXT_ALIGN);

        addColourAttribute(element,
                           value,
                           getFormatName(format, attributes),
                           VDXMLConstants.BACKGROUND_COLOUR_ATTRIBUTE,
                           true);

        // Padding is the same all the way round so arbitrarily use the top
        value = propertyValues.getComputedValue(
                StylePropertyDetails.PADDING_TOP);
        if (value != null) {
            element.setAttribute(
                    VDXMLConstants.PSEUDO_PADDING_ATTRIBUTE,
                    convertToCharCount(value));
        }

        value = propertyValues.getComputedValue(
                StylePropertyDetails.BORDER_SPACING);
        if (value != null) {
            element.setAttribute(
                    VDXMLConstants.PSEUDO_SPACING_ATTRIBUTE,
                    convertToCharCount(value));
        }

        addAlignAttribute(element, align,
                          getFormatName(format, attributes));
    }

    /**
     * A degenerate version of {@link #addGridAttributes} for use with
     * the fake panes required for fragmentation.
     * <p>
     * This was created using the cut and paste anti-pattern because there
     * are no unit/integration tests for the original method and I don't want
     * to risk breaking it at the moment.
     *
     * @todo factor back into addGridAttributes when we have some unit tests.
     */
    private void addFakeGridAttributes(Element element,
                                         String formatName,
                                         MutablePropertyValues propertyValues) {

        if (propertyValues != null) {
            addColourAttribute(element,
                    propertyValues.getComputedValue(
                            StylePropertyDetails.BACKGROUND_COLOR),
                    formatName,
                    VDXMLConstants.BACKGROUND_COLOUR_ATTRIBUTE,
                    true);

            // Padding is the same all the way round so arbitrarily use the top
            // @todo later allow theme to override padding and spacing
            StyleValue paddingTop = propertyValues.getComputedValue(
                    StylePropertyDetails.PADDING_TOP);
            if (paddingTop != null) {
                element.setAttribute(VDXMLConstants.PSEUDO_PADDING_ATTRIBUTE,
                        convertToCharCount(paddingTop));
            }

            StyleValue borderSpacing = propertyValues.getComputedValue(
                    StylePropertyDetails.BORDER_SPACING);
            if (borderSpacing != null) {
                element.setAttribute(VDXMLConstants.PSEUDO_SPACING_ATTRIBUTE,
                        convertToCharCount(borderSpacing));
            }

            addAlignAttribute(element, propertyValues.getComputedValue(
                    StylePropertyDetails.TEXT_ALIGN), formatName);
        }
    }

    // javadoc inherited
    protected void closeColumnIteratorPane(
            DOMOutputBuffer dom,
            ColumnIteratorPaneAttributes attributes) {
        dom.closeElement(VDXMLConstants.PSEUDO_ROW_ELEMENT);
        dom.closeElement(VDXMLConstants.PSEUDO_GRID_ELEMENT);
    }

    // javadoc inherited
    protected void openColumnIteratorPaneElement(
            DOMOutputBuffer dom,
            ColumnIteratorPaneAttributes attributes) {
        // No column width written to this; the assumption is that the pane has
        // the required width
        dom.openElement(VDXMLConstants.PSEUDO_CELL_ELEMENT);

        openPane(dom, attributes);
    }

    // javadoc inherited
    protected void closeColumnIteratorPaneElement(
            DOMOutputBuffer dom,
            ColumnIteratorPaneAttributes attributes) {
        closePane(dom, attributes);

        dom.closeElement(VDXMLConstants.PSEUDO_CELL_ELEMENT);
    }

    // javadoc inherited
    protected void openDefinitionData(
            DOMOutputBuffer dom,
            DefinitionDataAttributes attributes) throws ProtocolException {
        warn(attributes);
    }

    // javadoc inherited
    protected void closeDefinitionData(DOMOutputBuffer dom,
                                       DefinitionDataAttributes attributes) {
        // Intentionally does nothing
    }

    // javadoc inherited
    protected void openDefinitionList(
            DOMOutputBuffer dom,
            DefinitionListAttributes attributes) throws ProtocolException {
        warn(attributes);
    }

    // javadoc inherited
    protected void closeDefinitionList(DOMOutputBuffer dom,
                                       DefinitionListAttributes attributes) {
        // Intentionally does nothing
    }

    // javadoc inherited
    protected void openDefinitionTerm(
            DOMOutputBuffer dom,
            DefinitionTermAttributes attributes) throws ProtocolException {
        warn(attributes);
    }

    // javadoc inherited
    protected void closeDefinitionTerm(DOMOutputBuffer dom,
                                       DefinitionTermAttributes attributes) {
        // Intentionally does nothing
    }

    // javadoc inherited
    protected void openDissectingPane(DOMOutputBuffer dom,
                                      DissectingPaneAttributes attributes) {
        dom.openElement(VDXMLConstants.PSEUDO_DISSECTION_ELEMENT);
        addDissectingPaneAttributes(dom, attributes);
    }

    // javadoc inherited
    protected void closeDissectingPane(DOMOutputBuffer dom,
                                       DissectingPaneAttributes attributes) {
        dom.closeElement(VDXMLConstants.PSEUDO_DISSECTION_ELEMENT);
    }

    /**
     * Setup a dissecting pane fake content with the various attributes that
     * are necessary for Minitel/VDXML.
     *
     * @param dom        The current output buffer to use
     * @param attributes The attributes to retrieve the values from and use
     */
    private void addDissectingPaneAttributes(
            DOMOutputBuffer dom,
            DissectingPaneAttributes attributes) {

        DissectingPane pane = attributes.getDissectingPane();
        Element element =
                dom.openElement(VDXMLConstants.PSEUDO_DISSECTION_CONTENT);

        // Save the pane so it can be used when handling the standard
        // pane attributes.
        element.getParent().setObject(pane);

        // Now do the next/previous help information
        if (helpZonePane != null) {
            // It is only possible to set the next and previous text links
            // if there is a help area in which to put them!
            String value;

            // Set next stuff - actual link not needed as NEXT/PREVIOUS used
            String nextText = null;
            if ((value = attributes.getLinkText()) != null) {
                 nextText = value;
            } else if ((value = pane.getNextShardLinkText()) != null ) {
                nextText = value;
            }
            MCSAttributes nextStyle = null;
            // todo XDIME-CP style dissection correctly.
            if (((value = pane.getNextShardLinkClass()) != null) &&
                !"".equals(value)){
//                attributes.setStyleClass(value);
                nextStyle = attributes;
            }

            // Set previous stuff - actual link not needed as NEXT/PREVIOUS used
            String prevText = null;
            if ((value = attributes.getBackLinkText()) != null) {
                prevText = value;
            } else if ((value = pane.getPreviousShardLinkText()) != null ) {
                prevText = value;
            }
            MCSAttributes prevStyle = null;
            // todo XDIME-CP style dissection correctly.
            if (((value = pane.getPreviousShardLinkClass()) != null) &&
                !"".equals(value)) {
//                attributes.setStyleClass(value);
                prevStyle = attributes;
            }

            // Now generate a bunch of markup where appropriate
            if (nextText != null) {
                dom.openElement(VDXMLConstants.DISSECT_NEXT_HELP);
                createHelpElement(dom, nextText, nextStyle);
                dom.closeElement(VDXMLConstants.DISSECT_NEXT_HELP);
            }
            if (prevText != null) {
                dom.openElement(VDXMLConstants.DISSECT_PREVIOUS_HELP);
                createHelpElement(dom, prevText, prevStyle);
                dom.closeElement(VDXMLConstants.DISSECT_PREVIOUS_HELP);
            }
            if (prevText != null || nextText != null) {
                dom.openElement(VDXMLConstants.DISSECT_PREVIOUS_NEXT_HELP);
                String message = "";
                MCSAttributes style = null;
                if (prevText != null) {
                    message += prevText;
                    if (prevStyle != null) {
                        style = prevStyle;
                    }
                }
                if (prevText != null && nextText != null) {
                    message += " ";
                }
                if (nextText != null) {
                    message += nextText;
                    if (nextStyle != null  && style == null) {
                        // Previous style takes precedence over next
                        style = nextStyle;
                    }
                }
                createHelpElement(dom, message, style);
                dom.closeElement(VDXMLConstants.DISSECT_PREVIOUS_NEXT_HELP);
            }
        }
        dom.closeElement(VDXMLConstants.PSEUDO_DISSECTION_CONTENT);
    }

    /**
     * This creates a help element and inserts it into the dom in the
     * current position.  The attributes, if not null, are used to provide
     * styling around the message AKA a span wrapped around that text.  The
     * style of the help element itself is based on the help zone pane.
     *
     * @param dom        The current dom in which to insert the help element
     * @param text       The message to display in the help area
     * @param attributes The attributes to use for extra styling around the
     *                   help message.  These are automatically used if they
     *                   are not null.  Otherwise no extra style information
     *                   is generated.
     */
    private void createHelpElement(DOMOutputBuffer dom,
                                   String text,
                                   MCSAttributes attributes) {
        Element helpElement = dom.openElement(VDXMLConstants.HELP_ELEMENT);

        // Inherit the colours from the help zone pane
        Styles styles = attributes.getStyles();
        PropertyValues propertyValues = styles.getPropertyValues();

        addColourAttribute(helpElement,
                propertyValues.getComputedValue(
                        StylePropertyDetails.BACKGROUND_COLOR),
                VDXMLConstants.HELP_ELEMENT,
                VDXMLConstants.BACKGROUND_COLOUR_ATTRIBUTE);
        addColourAttribute(helpElement,
                propertyValues.getComputedValue(StylePropertyDetails.COLOR),
                VDXMLConstants.HELP_ELEMENT,
                VDXMLConstants.TEXT_COLOUR_ATTRIBUTE);

        // Add the prompt message, styled differently if attributes != null
        if (attributes != null) {
            openInline(dom, attributes);
        }
        dom.appendEncoded(text);
        if (attributes != null) {
            closeInline(dom, attributes);
        }

        // properties are stored on the help element to allow the
        // inline and block style processing to pick up the default
        // values
        helpElement.setStyles(StylingFactory.getDefaultInstance().
                createStyles(propertyValues));
        dom.closeElement(helpElement);
    }

    /**
     * Given a pane instance and an element the various heights, widths,
     * and colours will be added.  Additionally borders and the special
     * help zone pane specific to Minitel/VDXML are handled here.
     *
     * @param pane       The pane to generate
     * @param element    Element to which the attributes should be added
     * @param attributes The attributes to base the styles on
     */
    private void setPaneAttributes(Pane pane,
                                   Element element,
                                   MCSAttributes attributes) {
        final String destinationArea = pane.getDestinationArea();
        Styles styles = attributes.getStyles();
        PropertyValues propertyValues = styles.getPropertyValues();

        // Handle dimensions first
        // Themes don't override pane widths and heights
        String width = calculatePaneWidth(pane, propertyValues, element);
        String height = calculatePaneHeight(pane, propertyValues, element);
        String borderWidth = convertToCharCount(propertyValues.getComputedValue(
                StylePropertyDetails.BORDER_TOP_WIDTH));

        element.setAttribute(VDXMLConstants.WIDTH_ATTRIBUTE, width);
        element.setAttribute(VDXMLConstants.HEIGHT_ATTRIBUTE, height);

        if (destinationArea != null) {
            element.setAttribute(
                    VDXMLConstants.PSEUDO_DESTINATION_AREA_ATTRIBUTE,
                    destinationArea);

            if ((NAVIGATION_DESTINATION.equals(destinationArea)) &&
                    (helpZonePane != null)) {
                element.setAttribute(
                        VDXMLConstants.PSEUDO_HAS_HELP_ZONE_ATTRIBUTE,
                        "true");
            }
        }

        if (borderWidth != null) {
            element.setAttribute(VDXMLConstants.PSEUDO_BORDER_ATTRIBUTE,
                                 borderWidth);
            addColourAttribute(
                    element, propertyValues.getComputedValue(
                                StylePropertyDetails.BORDER_TOP_COLOR),
                        getFormatName(pane, attributes),
                        VDXMLConstants.PSEUDO_BORDER_COLOUR_ATTRIBUTE);
            }

        // The help zone "pane" doesn't want any further style attributes and
        // doesn't define a "display context" (so don't store the style
        // properties)
        if (pane != helpZonePane) {
                addColourAttribute(element,propertyValues.getComputedValue(
                        StylePropertyDetails.COLOR),
                        getFormatName(pane, attributes),
                        VDXMLConstants.TEXT_COLOUR_ATTRIBUTE);

            addGridAttributes(element, attributes, pane);
        }
    }

    /**
     * Returns the width of this pane in number of characters.
     * <p>
     * The width of a pane may be set using pixels or a percentage value
     * of 100%.  If the width of the pane is set using pixels, then this
     * pixel value will be converted to characters by dividing by
     * {@link #PIXELS_PER_CHAR}.
     * <p>
     * If the width has been set using a percentage value of 100% then
     * the containing format will be inspected to obtain an absolute
     * value for "100%" in the particular context.  If there is no containing
     * layout, ie we have a simple layout with a single pane, the the number
     * of characters required for the width will be calculated from
     * the maximum number of pixels avaliable in the x-axis on the
     * requesting device.
     *
     * @param pane the pane whose width is characters is required.
     * @param styleProperties the pane properties.
     * @param element the element to which the width value will be added.
     *
     * @return the width required for this pane in characters.
     */
    private String calculatePaneWidth(Pane pane,
                                      PropertyValues styleProperties,
                                      Element element) {
        String width = null;
        if (pane.getParent() == null) {

            // This pane is the root layout. Therefore if the pane
            // has a width specified as a percentage then we need to set
            // the width to the maximum number of pixels available on the
            // device. Note that only a percentage width of 100% is valid.
            StyleValue paneWidth = styleProperties.getComputedValue(
                StylePropertyDetails.WIDTH);
            if (paneWidth instanceof StylePercentage) {

                StylePercentage percentage = (StylePercentage)paneWidth;
                validatePercentageValue(percentage);
                width = Integer.toString(
                            context.getDevice().getPixelsX()/PIXELS_PER_CHAR);
            } else {
                width = convertToCharCount(styleProperties.getComputedValue(
                                    StylePropertyDetails.WIDTH));
            }
        } else {

            width = convertToCharCount(styleProperties.getComputedValue(
                                    StylePropertyDetails.WIDTH));
            if (width == null) {
                width = getInheritedWidth(element);
            }
        }

        if (width == null) {
            logger.error("pane-missing-attribute-on-grid",
                         new Object[] {pane.getName(), "width", "column"});
            throw new IllegalStateException(
                        "Pane " + pane.getName() + " has no width " +
                        "specified directly or on a containing grid column");
        }
        return width;
    }

    /**
     * Returns the height of this pane in number of characters.
     * <p>
     * The height of a pane may be set using pixels or a percentage value
     * of 100%.  If the width of the pane is set using pixels, then this
     * pixel value will be converted to characters by dividing by
     * {@link #PIXELS_PER_CHAR}.
     * <p>
     * If the height has been set using a percentage value of 100% then
     * the containing format will be inspected to obtain an absolute
     * value for "100%" in the particular context.  If there is no containing
     * layout, ie we have a simple layout with a single pane, the the number
     * of characters required for the height will be calculated from
     * the maximum number of pixels avaliable in the y-axis
     * on the requesting device.
     *
     * @param pane the pane whose width is characters is required.
     * @param styleProperties the pane properties.
     * @param element the element to which the width value will be added.
     *
     * @return the width required for this pane in characters.
     */
    private String calculatePaneHeight(Pane pane,
                                       PropertyValues styleProperties,
                                       Element element) {
        String height = null;
        if (pane.getParent() == null) {

            // This pane is the root layout. Therefore if the pane
            // has a width specified as a percentage then we need to set
            // the width to the maximum number of pixels available on the
            // device. Note that only a percentage width of 100% is valid.
            StyleValue paneHeight = styleProperties.getComputedValue(
                    StylePropertyDetails.HEIGHT);
            if (paneHeight instanceof StylePercentage) {

                StylePercentage percentage = (StylePercentage)paneHeight;
                validatePercentageValue(percentage);
                height = Integer.toString(
                            context.getDevice().getPixelsY()/PIXELS_PER_CHAR);
            } else {
                height = convertToCharCount(styleProperties.getComputedValue(
                        StylePropertyDetails.HEIGHT));
            }
        } else {

            height = convertToCharCount(styleProperties.getComputedValue(
                    StylePropertyDetails.HEIGHT));

            if (height == null) {
                height = getInheritedHeight(element);
            }
        }

        if (height == null) {

            logger.error("pane-missing-attribute-on-grid",
                         new Object[] {pane.getName(), "height", "row"});
            throw new IllegalStateException(
                        "Pane " + pane.getName() + " has no height " +
                        "specified directly or on a containing grid row");
        }

        return height;
    }

    // javadoc inherited
    public void openDiv(DOMOutputBuffer dom,
                           DivAttributes attributes) throws ProtocolException {
        openBlock(dom, attributes);
    }

    // javadoc inherited
    public void closeDiv(DOMOutputBuffer dom,
                            DivAttributes attributes) {
        closeBlock(dom, attributes);
    }

    // javadoc inherited
    protected void openEmphasis(DOMOutputBuffer dom,
                                EmphasisAttributes attributes)
            throws ProtocolException {
        openInline(dom, attributes);
    }

    // javadoc inherited
    protected void closeEmphasis(DOMOutputBuffer dom,
                                 EmphasisAttributes attributes) {
        closeInline(dom, attributes);
    }

    // javadoc inherited
    protected void openForm(DOMOutputBuffer dom,
                            FormAttributes attributes) {
        // Restore the insertion point back to the saved position before
        // rendering the contents of the form.
        dom.restoreInsertionPoint();
    }

    // JavaDoc inherited
    protected void openForm(XFFormAttributes attributes)
            throws ProtocolException {

        DOMOutputBuffer dom = getContentBuffer(attributes.getFormData ());
        String value;
        Element element = dom.openElement(VDXMLConstants.FORM_ELEMENT);

        // Note: if action attribute is specified via a LinkComponent
        // and no suitable Link Asset is found it does not make sense
        // to fallback to a TextComonent.
        value = resolveFormAction(attributes);
        if (!value.equals("")) {
            element.setAttribute(VDXMLConstants.ACTION_ATTRIBUTE, value);
        }

        if ((value = attributes.getMethod()) != null) {
            element.setAttribute(VDXMLConstants.METHOD_ATTRIBUTE,
                                 value.toUpperCase());
        }

        // allow subclasses to add additional attributes
        addXFFormAttributes(element, attributes);

        // Add the top fragment links.
        doTopFragmentLinks(dom, attributes);

        // Add an implicit field which is used to specify the form descriptor.
        Element inputElement = dom.addElement(VDXMLConstants.IMPLICIT_ELEMENT);
        inputElement.setAttribute(VDXMLConstants.NAME_ATTRIBUTE,
                                  URLConstants.FORM_PARAMETER);
        inputElement.setAttribute(VDXMLConstants.VALUE_ATTRIBUTE,
                                  getFormSpecifier(attributes));

        // Remember the current insertion point in the dom buffer as that is
        // where we need to insert the contents of the form which will only be
        // generated during the rendering of the layout.
        dom.saveInsertionPoint ();

    }

    // Javadoc inherited.
    public Element createXFormEmulationElement(String formName,
            EmulatedXFormDescriptor fd) {

        Element element = domFactory.createElement();
        element.setName(VDXMLConstants.FORM_ELEMENT);

        // Note: if action attribute is specified via a LinkComponent
        // and no suitable Link Asset is found it does not make sense
        // to fallback to a TextComonent.
        String resolvedURL = resolveFormAction(fd.getFormAttributes());
        if (!resolvedURL.equals("")) {
            element.setAttribute(VDXMLConstants.ACTION_ATTRIBUTE, resolvedURL);
        }

        final String method = fd.getFormMethod();
        if (method != null) {
            element.setAttribute(VDXMLConstants.METHOD_ATTRIBUTE,
                                 method.toUpperCase());
        }
        return element;
    }

    // Javadoc inherited.
    public boolean isXFormEmulationElement(Element element) {
        boolean result = false;
        if (element.getName().equals(VDXMLConstants.FORM_ELEMENT)) {
            result = true;
        }
        return result;
    }

    // Javadoc inherited.
    public boolean isImplicitEmulationElement(Element element) {
       boolean result = false;
        if (element.getName().equals(VDXMLConstants.IMPLICIT_ELEMENT)) {
            result = true;
        }
        return result;
    }

    // Javadoc inherited.
    public Element createVFormElement(String formSpecifier) {
        // Add an implicit field which is used to specify the form descriptor.
        Element inputElement = domFactory.createElement();
        inputElement.setName(VDXMLConstants.IMPLICIT_ELEMENT);
        inputElement.setAttribute(VDXMLConstants.NAME_ATTRIBUTE,
                URLConstants.FORM_PARAMETER);
        inputElement.setAttribute(VDXMLConstants.VALUE_ATTRIBUTE, formSpecifier);
        return inputElement;
    }

    /**
     * Method to allow subclasses to specify additional attributes for
     * the form element
     *
     * @param element    The Element to modify.
     * @param attributes The attributes
     */
    private void addXFFormAttributes(Element element,
                                       XFFormAttributes attributes)
            throws ProtocolException {
    }

    // JavaDoc inherited
    protected void closeForm(XFFormAttributes attributes)
            throws ProtocolException {

        DOMOutputBuffer dom = getContentBuffer(attributes.getFormData ());

        // Add the bottom fragment links.
        doBottomFragmentLinks(dom, attributes);
        context.clearFormFragmentResetState();

        dom.closeElement (VDXMLConstants.FORM_ELEMENT);
    }

    // javadoc inherited
    protected void openGrid(DOMOutputBuffer dom,
                            GridAttributes attributes) {

        Element element = dom.openElement(VDXMLConstants.PSEUDO_GRID_ELEMENT);

        addGridAttributes(element, attributes, attributes.getFormat());
    }

    // javadoc inherited
    protected void closeGrid(DOMOutputBuffer dom,
                             GridAttributes attributes) {
        dom.closeElement(VDXMLConstants.PSEUDO_GRID_ELEMENT);
    }

    // javadoc inherited
    protected void openGridChild(DOMOutputBuffer dom,
                                 GridChildAttributes attributes) {
        Element element = dom.openElement(VDXMLConstants.PSEUDO_CELL_ELEMENT);

        addGridCellAttributes(element, attributes);
    }

    /**
     * Add the width of the element in the current grid cell. given an
     * element and some grid attributes.  This is done based on the value
     * of the item in the cell.
     *
     * @param element    The element on which to set the attributes
     * @param attributes The attributes from which to extract values
     */
    private void addGridCellAttributes(Element element,
                                         GridChildAttributes attributes) {

        Styles styles = attributes.getStyles();
        addLengthAttribute(element, styles, StylePropertyDetails.WIDTH, VDXMLConstants.WIDTH_ATTRIBUTE);
    }

    private void addLengthAttribute(
            Element element, Styles styles,
            final StyleProperty property,
            final String attributeName) {

        PropertyValues propertyValues = styles.getPropertyValues();
        StyleValue styleValue = propertyValues.getComputedValue(property);
        if (styleValue instanceof StyleLength) {
            StyleLength length = (StyleLength) styleValue;
            if (length.getUnit() == LengthUnit.PX) {
                int value = (int) length.getNumber();
                element.setAttribute(attributeName, convertToCharCount(value));
            }
        }
    }

    // javadoc inherited
    protected void closeGridChild(DOMOutputBuffer dom,
                                  GridChildAttributes attributes) {
        dom.closeElement(VDXMLConstants.PSEUDO_CELL_ELEMENT);
    }

    // javadoc inherited
    protected void openGridRow(DOMOutputBuffer dom,
                               GridRowAttributes attributes) {
        Element element = dom.openElement(VDXMLConstants.PSEUDO_ROW_ELEMENT);

        addGridRowAttributes(element, attributes);
    }

    /**
     * Add the height of the element in the current grid cell. given an
     * element and some grid attributes.  This is done based on the value
     * in the grid row attributes.
     *
     * @param element    The element on which to set the attributes
     * @param attributes The attributes from which to extract values
     */
    private void addGridRowAttributes(Element element,
                                        GridRowAttributes attributes) {

        // todo Use a property handler of some sort.
        Styles styles = attributes.getStyles();
        addLengthAttribute(element, styles, StylePropertyDetails.HEIGHT,
                           VDXMLConstants.HEIGHT_ATTRIBUTE);

        // @todo later store vertical align to handle cell alignments?
    }

    // javadoc inherited
    protected void closeGridRow(DOMOutputBuffer dom,
                                GridRowAttributes attributes) {
        dom.closeElement(VDXMLConstants.PSEUDO_ROW_ELEMENT);
    }

    // javadoc inherited
    protected void openHead(DOMOutputBuffer dom, boolean empty) {
        // Intentionally does nothing
    }

    // javadoc inherited
    protected void closeHead(DOMOutputBuffer dom, boolean empty) {
        // Intentionally does nothing
    }

    // javadoc inherited
    protected void openHeading1(DOMOutputBuffer dom,
                                HeadingAttributes attributes)
            throws ProtocolException {
        openBlock(dom, attributes);
    }

    // javadoc inherited
    protected void closeHeading1(DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {
        closeBlock(dom, attributes);
    }

    // javadoc inherited
    protected void openHeading2(DOMOutputBuffer dom,
                                HeadingAttributes attributes)
            throws ProtocolException {
        openBlock(dom, attributes);
    }

    // javadoc inherited
    protected void closeHeading2(DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {
        closeBlock(dom, attributes);
    }

    // javadoc inherited
    protected void openHeading3(DOMOutputBuffer dom,
                                HeadingAttributes attributes)
            throws ProtocolException {
        openBlock(dom, attributes);
    }

    // javadoc inherited
    protected void closeHeading3(DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {
        closeBlock(dom, attributes);
    }

    // javadoc inherited
    protected void openHeading4(DOMOutputBuffer dom,
                                HeadingAttributes attributes)
            throws ProtocolException {
        openBlock(dom, attributes);
    }

    // javadoc inherited
    protected void closeHeading4(DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {
        closeBlock(dom, attributes);
    }

    // javadoc inherited
    protected void openHeading5(DOMOutputBuffer dom,
                                HeadingAttributes attributes)
            throws ProtocolException {
        openBlock(dom, attributes);
    }

    // javadoc inherited
    protected void closeHeading5(DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {
        closeBlock(dom, attributes);
    }

    // javadoc inherited
    protected void openHeading6(DOMOutputBuffer dom,
                                HeadingAttributes attributes)
            throws ProtocolException {
        openBlock(dom, attributes);
    }

    // javadoc inherited
    protected void closeHeading6(DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {
        closeBlock(dom, attributes);
    }

    // javadoc inherited
    protected void openInclusion(
            DOMOutputBuffer dom,
            CanvasAttributes attributes) throws ProtocolException {
        // @todo later implement
    }

    // javadoc inherited
    protected void closeInclusion(DOMOutputBuffer dom,
                                  CanvasAttributes attributes) {
        // @todo later implement
    }

    // javadoc inherited
    public void openInclusionPage(CanvasAttributes attributes)
            throws IOException, ProtocolException {
        // @todo needed?
        super.openInclusionPage(attributes);
    }

    // javadoc inherited
    public void closeInclusionPage(CanvasAttributes attributes)
            throws IOException, ProtocolException {
        // @todo needed?
        super.closeInclusionPage(attributes);
    }

    // javadoc inherited
    protected void openItalic(DOMOutputBuffer dom,
                              ItalicAttributes attributes)
            throws ProtocolException {
        openInline(dom, attributes);
    }

    // javadoc inherited
    protected void closeItalic(DOMOutputBuffer dom,
                               ItalicAttributes attributes) {
        closeInline(dom, attributes);
    }

    // javadoc inherited
    protected void openKeyboard(DOMOutputBuffer dom,
                                KeyboardAttributes attributes)
            throws ProtocolException {
        openInline(dom, attributes);
    }

    // javadoc inherited
    protected void closeKeyboard(DOMOutputBuffer dom,
                                 KeyboardAttributes attributes) {
        closeInline(dom, attributes);
    }

    // javadoc inherited
    protected void openLayout(DOMOutputBuffer dom,
                              LayoutAttributes attributes) {
        // @todo needed?
        super.openLayout(dom, attributes);
    }

    // javadoc inherited
    protected void closeLayout(DOMOutputBuffer dom,
                               LayoutAttributes attributes) {
        // @todo needed?
        super.closeLayout(dom, attributes);
    }

    // javadoc inherited
    protected void openListItem(DOMOutputBuffer dom,
                                ListItemAttributes attributes)
            throws ProtocolException {
        warn(attributes);
    }

    // javadoc inherited
    protected void closeListItem(DOMOutputBuffer dom,
                                 ListItemAttributes attributes) {
        // Intentionally does nothing
    }

    // javadoc inherited
    protected void openMenu(DOMOutputBuffer dom, MenuAttributes attributes) {
        // Intentionally does nothing
    }

    // javadoc inherited
    protected void closeMenu(DOMOutputBuffer dom, MenuAttributes attributes) {
        // Intentionally does nothing
    }

    // javadoc inherited
    protected void openMonospaceFont(
            DOMOutputBuffer dom,
            MonospaceFontAttributes attributes) throws ProtocolException {
        openInline(dom, attributes);
    }

    // javadoc inherited
    protected void closeMonospaceFont(DOMOutputBuffer dom,
                                      MonospaceFontAttributes attributes) {
        closeInline(dom, attributes);
    }

    // javadoc inherited
    protected void openMontage(DOMOutputBuffer dom,
                               MontageAttributes attributes) {
        error(attributes);
    }

    // javadoc inherited
    protected void closeMontage(DOMOutputBuffer dom,
                                MontageAttributes attributes) {
        // Intentionally does nothing
    }

    // javadoc inherited
    protected void openNoScript(DOMOutputBuffer dom,
                                NoScriptAttributes attributes)
            throws ProtocolException {
        warn(attributes);
    }

    // javadoc inherited
    protected void closeNoScript(DOMOutputBuffer dom,
                                 NoScriptAttributes attributes) {
        // Intentionally does nothing
    }

    // javadoc inherited
    protected void openOrderedList(DOMOutputBuffer dom,
                                   OrderedListAttributes attributes)
            throws ProtocolException {
        warn(attributes);
    }

    // javadoc inherited
    protected void closeOrderedList(DOMOutputBuffer dom,
                                    OrderedListAttributes attributes) {
        // Intentionally does nothing
    }

    // javadoc inherited
    protected void openPane(DOMOutputBuffer dom,
                            PaneAttributes attributes) {
        Element element = dom.openElement(VDXMLConstants.PSEUDO_PANE_ELEMENT);

        addPaneAttributes(element, attributes);
    }

    /**
     * Given some pane attributes and an element, decide which pane should be
     * used and then use that pane and its attributes to add various visual
     * VDXML attributes to the element.
     *
     * @param element    The element on which to set the attributes
     * @param attributes The attributes from which to extract values
     */
    private void addPaneAttributes(Element element,
                                     PaneAttributes attributes) {
        Pane pane = attributes.getPane();

        // Check whether the current element's parent is a dissect element
        // In which case get the object back from that element which
        // will be the dissecting pane.
        Element parent = element.getParent();

        if (parent != null) {
            String name = parent.getName();

            if (name != null &&
                    name.equals(VDXMLConstants.PSEUDO_DISSECTION_ELEMENT)) {
                pane = (DissectingPane) parent.getObject();
                element.setAttribute(VDXMLConstants.PSEUDO_DISSECTION_ATTRIBUTE,
                                     "true");
            }
        }

        setPaneAttributes(pane, element, attributes);
    }

    /**
     * Returns a width (in characters) string if there is an inheritable width
     * or null otherwise. A width is inheritable from an immediately containing
     * cell or an immediately containing VDXML element.
     *
     * @param element the element for which a width is required
     * @return the inherited width or null
     */
    private String getInheritedWidth(Element element) {
        Element parent = element.getParent();
        String width = null;

        // Step out a further level if this is in a dissection
        if ((parent != null) &&
                (VDXMLConstants.PSEUDO_DISSECTION_ELEMENT.equals(
                        parent.getName()))) {
            parent = parent.getParent();
        }

        if (parent != null) {
            if (VDXMLConstants.VDXML_ELEMENT.equals(parent.getName())) {
                width = VDXMLConstants.DISPLAY_WIDTH_IN_CHARS;
            } else if (VDXMLConstants.PSEUDO_CELL_ELEMENT.equals(
                        parent.getName())) {
                width = parent.getAttributeValue(VDXMLConstants.WIDTH_ATTRIBUTE);
            }
        }

        return width;
    }

    /**
     * Returns a height (in characters) string if there is an inheritable
     * height or null otherwise. A height is inheritable from the row for an
     * immediately containing cell or from an immediately containing VDXML
     * element.
     *
     * @param element the element for which a height is required
     * @return the inherited height or null
     */
    private String getInheritedHeight(Element element) {
        Element parent = element.getParent();
        String height = null;

        // Step out a further level if this is in a dissection
        if ((parent != null) &&
                (VDXMLConstants.PSEUDO_DISSECTION_ELEMENT.equals(
                        parent.getName()))) {
            parent = parent.getParent();
        }

        if (parent != null) {
            if (VDXMLConstants.VDXML_ELEMENT.equals(parent.getName())) {
                height = VDXMLConstants.DISPLAY_HEIGHT_IN_CHARS;
            } else {
                parent = parent.getParent();

                if ((parent != null) &&
                        (VDXMLConstants.PSEUDO_ROW_ELEMENT.equals(
                                parent.getName()))) {
                    height = parent.getAttributeValue(VDXMLConstants.
                                                 HEIGHT_ATTRIBUTE);
                }
            }
        }

        return height;
    }

    /**
     * Unwinds through the parents of a given element, starting at the
     * immediate parent, to find inherited colours using the attribute
     * specified.
     *
     * @param element   The element on which to set the attributes
     * @param attribute The attribute to look for
     *
     * @return The name of the colour to use if one is found, or null otherwise
     */
    private String getInheritedColour(Element element, String attribute) {
        String colour = null;
        Element parent = element.getParent();

        while ((colour == null) && (parent != null)) {
            colour = parent.getAttributeValue(attribute);
            parent = parent.getParent();
        }

        return colour;
    }

    // javadoc inherited
    protected void closePane(DOMOutputBuffer dom,
                             PaneAttributes attributes) {
        dom.closeElement(VDXMLConstants.PSEUDO_PANE_ELEMENT);
    }

    /**
     * A degenerate version of {@link #addPaneAttributes} for use with
     * the fake panes required for fragmentation.
     * <p>
     * This was created using the cut and paste anti-pattern because there
     * are no unit/integration tests for the original method and I don't want
     * to risk breaking it at the moment.
     *
     * @todo factor back into addPaneAttributes when we have some unit tests.
     */
    protected void addFakePaneAttributes(Element element, String formatName,
            MutablePropertyValues propertyValues) {

        // Handle dimensions first
        String width = null;
        String height = null;
        String borderWidth = null;

        if (propertyValues != null) {
            width = convertToCharCount(propertyValues.getComputedValue(
                    StylePropertyDetails.WIDTH));
            height = convertToCharCount(propertyValues.getComputedValue(
                    StylePropertyDetails.HEIGHT));
            borderWidth = convertToCharCount(propertyValues.getComputedValue(
                    StylePropertyDetails.BORDER_TOP_WIDTH));
        }

        if (width == null) {
            width = getInheritedWidth(element);
        }

        if (height == null) {
            height = getInheritedHeight(element);
        }

        if (width == null) {
            logger.error("pane-missing-attribute-on-grid",
                         new Object[] {formatName, "width", "column"});
            throw new IllegalStateException(
                        "Pane " + formatName + " has no width " +
                        "specified directly or on a containing grid column");
        } else if (height == null) {            
            logger.error("pane-missing-attribute-on-grid",
                         new Object[] {formatName, "height", "row"});
            throw new IllegalStateException(
                        "Pane " + formatName + " has no height " +
                        "specified directly or on a containing grid row");
        }

        element.setAttribute(VDXMLConstants.WIDTH_ATTRIBUTE, width);
        element.setAttribute(VDXMLConstants.HEIGHT_ATTRIBUTE, height);

        if (propertyValues != null ) {
            if (borderWidth != null) {
                element.setAttribute(VDXMLConstants.PSEUDO_BORDER_ATTRIBUTE,
                        borderWidth);
                StyleValue borderTopColor = propertyValues.getComputedValue(
                        StylePropertyDetails.BORDER_TOP_COLOR);
                if (borderTopColor != null) {
                    addColourAttribute(element, borderTopColor, formatName,
                            VDXMLConstants.PSEUDO_BORDER_COLOUR_ATTRIBUTE);
                }
            }

            StyleValue color = propertyValues.getComputedValue(
                    StylePropertyDetails.COLOR);
            if (color != null) {
                addColourAttribute(element, color, formatName,
                        VDXMLConstants.TEXT_COLOUR_ATTRIBUTE);
            }
        }

        addFakeGridAttributes(element, formatName, propertyValues);

        // The merged properties are stored on the pane element to allow
        // the inline and block style processing to pick up the default
        // values
        element.setStyles(StylingFactory.getDefaultInstance().
                createStyles(propertyValues));
    }

    // javadoc inherited
    protected void openParagraph(DOMOutputBuffer dom,
                                 ParagraphAttributes attributes)
            throws ProtocolException {
        openBlock(dom, attributes);
    }

    // javadoc inherited
    protected void closeParagraph(DOMOutputBuffer dom,
                                  ParagraphAttributes attributes) {
        closeBlock(dom, attributes);
    }

    // javadoc inherited
    protected void openPhoneNumber(
            DOMOutputBuffer dom,
            PhoneNumberAttributes attributes) throws ProtocolException {
        openInline(dom, attributes);
    }

    // javadoc inherited
    protected void closePhoneNumber(DOMOutputBuffer dom,
                                    PhoneNumberAttributes attributes) {
        closeInline(dom, attributes);
    }

    // javadoc inherited
    protected void openPre(DOMOutputBuffer dom,
                           PreAttributes attributes) throws ProtocolException {
        openBlock(dom, attributes);
    }

    // javadoc inherited
    protected void closePre(DOMOutputBuffer dom,
                            PreAttributes attributes) {
        closeBlock(dom, attributes);
    }

    // javadoc inherited
    protected void openRowIteratorPane(
            DOMOutputBuffer dom,
            RowIteratorPaneAttributes attributes) {
        Element element = dom.openElement(VDXMLConstants.PSEUDO_GRID_ELEMENT);

        addRowIteratorPaneAttributes(element, attributes);
    }

    /**
     * Add attributes from a row iterator pane to the element provided.
     *
     * @param element    The element on which to set the attributes
     * @param attributes The attributes from which to extract values
     */
    private void addRowIteratorPaneAttributes(
            Element element,
            RowIteratorPaneAttributes attributes) {

        addGridAttributes(element, attributes, attributes.getPane());
    }

    // javadoc inherited
    protected void closeRowIteratorPane(DOMOutputBuffer dom,
                                        RowIteratorPaneAttributes attributes) {
        dom.closeElement(VDXMLConstants.PSEUDO_GRID_ELEMENT);
    }

    // javadoc inherited
    protected void openRowIteratorPaneElement(
            DOMOutputBuffer dom,
            RowIteratorPaneAttributes attributes) {
        // No row height written to this; the assumption is that the pane has
        // the required height
        dom.openElement(VDXMLConstants.PSEUDO_ROW_ELEMENT);

        // No column width written to this; the assumption is that the pane has
        // the required width
        dom.openElement(VDXMLConstants.PSEUDO_CELL_ELEMENT);

        openPane(dom, attributes);
    }

    // javadoc inherited
    protected void closeRowIteratorPaneElement(
            DOMOutputBuffer dom,
            RowIteratorPaneAttributes attributes) {
        closePane(dom, attributes);

        dom.closeElement(VDXMLConstants.PSEUDO_CELL_ELEMENT);

        dom.closeElement(VDXMLConstants.PSEUDO_ROW_ELEMENT);
    }

    // javadoc inherited
    protected void openSample(DOMOutputBuffer dom,
                              SampleAttributes attributes)
            throws ProtocolException {
        openInline(dom, attributes);
    }

    // javadoc inherited
    protected void closeSample(DOMOutputBuffer dom,
                               SampleAttributes attributes) {
        closeInline(dom, attributes);
    }

    // javadoc inherited
    protected void openScript(DOMOutputBuffer dom,
                              ScriptAttributes attributes) {
        warn(attributes);
    }

    // javadoc inherited
    protected void closeScript(DOMOutputBuffer dom,
                               ScriptAttributes attributes) {
        // Intentionally does nothing
    }

    // javadoc inherited
    protected void openSegment(DOMOutputBuffer dom,
                               SegmentAttributes attributes) {
        error(attributes);
    }

    // javadoc inherited
    protected void closeSegment(DOMOutputBuffer dom,
                                SegmentAttributes attributes) {
        // Intentionally does nothing
    }

    // javadoc inherited
    protected void openSegmentGrid(DOMOutputBuffer dom,
                                   SegmentGridAttributes attributes) {
        error(attributes);
    }

    // javadoc inherited
    protected void closeSegmentGrid(DOMOutputBuffer dom,
                                    SegmentGridAttributes attributes) {
        // Intentionally does nothing
    }

    // javadoc inherited
    protected void openSlide(DOMOutputBuffer dom,
                             SlideAttributes attributes) {
        error(attributes);
    }

    // javadoc inherited
    protected void closeSlide(DOMOutputBuffer dom,
                              SlideAttributes attributes) {
        // Intentionally does nothing
    }

    // javadoc inherited
    protected void openSmall(DOMOutputBuffer dom,
                             SmallAttributes attributes)
            throws ProtocolException {
        openInline(dom, attributes);
    }

    // javadoc inherited
    protected void closeSmall(DOMOutputBuffer dom,
                              SmallAttributes attributes) {
        closeInline(dom, attributes);
    }

    // javadoc inherited
    public void openSpan(DOMOutputBuffer dom,
                            SpanAttributes attributes)
            throws ProtocolException {
        openInline(dom, attributes);
    }

    // javadoc inherited
    public void closeSpan(DOMOutputBuffer dom,
                             SpanAttributes attributes) {
        closeInline(dom, attributes);
    }

    // javadoc inherited
    protected void openSpatialFormatIterator(
            DOMOutputBuffer dom,
            SpatialFormatIteratorAttributes attributes) {

        Element element = dom.openElement(VDXMLConstants.PSEUDO_GRID_ELEMENT);

        addGridAttributes(element, attributes, attributes.getFormat());
    }

    // javadoc inherited
    protected void closeSpatialFormatIterator(
            DOMOutputBuffer dom,
            SpatialFormatIteratorAttributes attributes) {
        dom.closeElement(VDXMLConstants.PSEUDO_GRID_ELEMENT);
    }

    // javadoc inherited
    protected void openSpatialFormatIteratorChild(
            DOMOutputBuffer dom,
            SpatialFormatIteratorAttributes attributes) {
        dom.openElement(VDXMLConstants.PSEUDO_CELL_ELEMENT);
    }

    // javadoc inherited
    protected void closeSpatialFormatIteratorChild(
            DOMOutputBuffer dom,
            SpatialFormatIteratorAttributes attributes) {
        dom.closeElement(VDXMLConstants.PSEUDO_CELL_ELEMENT);
    }

    // javadoc inherited
    protected void openSpatialFormatIteratorRow(
            DOMOutputBuffer dom,
            SpatialFormatIteratorAttributes attributes) {
        dom.openElement(VDXMLConstants.PSEUDO_ROW_ELEMENT);
    }

    // javadoc inherited
    protected void closeSpatialFormatIteratorRow(
            DOMOutputBuffer dom,
            SpatialFormatIteratorAttributes attributes) {
        dom.closeElement(VDXMLConstants.PSEUDO_ROW_ELEMENT);
    }

    // javadoc inherited
    protected void openStrong(DOMOutputBuffer dom,
                              StrongAttributes attributes)
            throws ProtocolException {
        openInline(dom, attributes);
    }

    // javadoc inherited
    protected void closeStrong(DOMOutputBuffer dom,
                               StrongAttributes attributes) {
        closeInline(dom, attributes);
    }

    // javadoc inherited
    protected void openStyle(DOMOutputBuffer dom,
                             StyleAttributes attributes) {
        warn(attributes);
    }

    // javadoc inherited
    protected void closeStyle(DOMOutputBuffer dom,
                              StyleAttributes attributes) {
        // Intentionally does nothing
    }

    // javadoc inherited
    protected void openSubscript(DOMOutputBuffer dom,
                                 SubscriptAttributes attributes)
            throws ProtocolException {
        openInline(dom, attributes);
    }

    // javadoc inherited
    protected void closeSubscript(DOMOutputBuffer dom,
                                  SubscriptAttributes attributes) {
        closeInline(dom, attributes);
    }

    // javadoc inherited
    protected void openSuperscript(DOMOutputBuffer dom,
                                   SuperscriptAttributes attributes)
            throws ProtocolException {
        openInline(dom, attributes);
    }

    // javadoc inherited
    protected void closeSuperscript(DOMOutputBuffer dom,
                                    SuperscriptAttributes attributes) {
        closeInline(dom, attributes);
    }

    // javadoc inherited
    protected void openTable(DOMOutputBuffer dom,
                             TableAttributes attributes)
            throws ProtocolException {
        warn(attributes);
    }

    // javadoc inherited
    protected void closeTable(DOMOutputBuffer dom,
                              TableAttributes attributes) {
        // Intentionally does nothing
    }

    // javadoc inherited
    protected void openTableBody(DOMOutputBuffer dom,
                                 TableBodyAttributes attributes)
            throws ProtocolException {
        warn(attributes);
    }

    // javadoc inherited
    protected void closeTableBody(DOMOutputBuffer dom,
                                  TableBodyAttributes attributes) {
        // Intentionally does nothing
    }

    // javadoc inherited
    protected void openTableDataCell(DOMOutputBuffer dom,
                                     TableCellAttributes attributes)
            throws ProtocolException {
        warn(attributes);
    }

    // javadoc inherited
    protected void closeTableDataCell(DOMOutputBuffer dom,
                                      TableCellAttributes attributes) {
        // Intentionally does nothing
    }

    // javadoc inherited
    protected void openTableFooter(DOMOutputBuffer dom,
                                   TableFooterAttributes attributes)
            throws ProtocolException {
        warn(attributes);
    }

    // javadoc inherited
    protected void closeTableFooter(DOMOutputBuffer dom,
                                    TableFooterAttributes attributes) {
        // Intentionally does nothing
    }

    // javadoc inherited
    protected void openTableHeader(DOMOutputBuffer dom,
                                   TableHeaderAttributes attributes)
            throws ProtocolException {
        warn(attributes);
    }

    // javadoc inherited
    protected void closeTableHeader(DOMOutputBuffer dom,
                                    TableHeaderAttributes attributes) {
        // Intentionally does nothing
    }

    // javadoc inherited
    protected void openTableHeaderCell(DOMOutputBuffer dom,
                                       TableCellAttributes attributes)
            throws ProtocolException {
        warn(attributes);
    }

    // javadoc inherited
    protected void closeTableHeaderCell(DOMOutputBuffer dom,
                                        TableCellAttributes attributes) {
        // Intentionally does nothing
    }

    // javadoc inherited
    protected void openTableRow(DOMOutputBuffer dom,
                                TableRowAttributes attributes)
            throws ProtocolException {
        warn(attributes);
    }

    // javadoc inherited
    protected void closeTableRow(DOMOutputBuffer dom,
                                 TableRowAttributes attributes) {
        // Intentionally does nothing
    }

    // javadoc inherited
    protected void openUnderline(DOMOutputBuffer dom,
                                 UnderlineAttributes attributes)
            throws ProtocolException {

        // TODO: we should be adding text-decoration: underline here.

        openInline(dom, attributes);
    }

    // javadoc inherited
    protected void closeUnderline(DOMOutputBuffer dom,
                                  UnderlineAttributes attributes) {
        closeInline(dom, attributes);
    }

    // javadoc inherited
    protected void openUnorderedList(DOMOutputBuffer dom,
                                     UnorderedListAttributes attributes)
            throws ProtocolException {
        warn(attributes);
    }

    // javadoc inherited
    protected void closeUnorderedList(DOMOutputBuffer dom,
                                      UnorderedListAttributes attributes) {
        // Intentionally does nothing
    }

    // javadoc inherited
    protected void doImage(DOMOutputBuffer dom,
                           ImageAttributes attributes) {
        String value = attributes.getSrc();

        if (value != null) {
            Element element = dom.addElement(VDXMLConstants.IMAGE_ELEMENT);

            if (attributes.getAssetURLSuffix() != null) {
                value = value + attributes.getAssetURLSuffix();
            }
            element.setAttribute(VDXMLConstants.SOURCE_ATTRIBUTE, value);
        }
    }

    // javadoc inherited
    protected void doHorizontalRule(DOMOutputBuffer dom,
                                    HorizontalRuleAttributes attributes)
            throws ProtocolException {
        // @todo later could handle the additional attributes using some thematic properties
        dom.addElement(VDXMLConstants.HORIZONTAL_RULE_ELEMENT);
    }

    // JavaDoc inherited
    public void doImplicitValue(DOMOutputBuffer dom,
            XFImplicitAttributes attributes) {
        
        Element element = dom.openElement(VDXMLConstants.IMPLICIT_ELEMENT);
        addFormFieldAttributes(element, attributes);
        element.setAttribute(VDXMLConstants.NAME_ATTRIBUTE,
                             attributes.getName());
        element.setAttribute(VDXMLConstants.VALUE_ATTRIBUTE,
                             attributes.getValue());

        dom.closeElement(element);
    }

    // JavaDoc inherited
    public void doBooleanInput(XFBooleanAttributes attributes)
            throws ProtocolException {

        DOMOutputBuffer dom;
        ContainerInstance entryContainerInstance =
                attributes.getEntryContainerInstance();

        // Add the caption to the caption containerInstance.
        addFormCaption(attributes);

        // If the entry containerInstance is not set then return as there is
        // nothing to do
        if (entryContainerInstance == null) {
            if (logger.isDebugEnabled()){
                logger.debug("Entry container instance not set");
            }
            return;
        }

        // Direct the markup to the entry container's content buffer.
        dom = getCurrentBuffer(entryContainerInstance);

        // Get the comma separated list of false values and parse it into
        // an array of strings.
        String falseText = getPlainText(attributes.getFalseValues());
        String[] falseValues = null;
        if (falseText != null) {
            falseValues = parseCommaSeparatedList(falseText);
        }

        // Get the comma separated list of false values and parse it into
        // an array of strings.
        String trueText = getPlainText(attributes.getTrueValues());
        String[] trueValues = null;
        if (trueText != null) {
            trueValues = parseCommaSeparatedList(trueText);
        }

        // Generate the markup
        Element element = dom.openElement(VDXMLConstants.INPUT_FIELD_ELEMENT);

        // set any attribute values that should be applied for all form fields.
        addFormFieldAttributes(element, attributes);

        // set specific attribute values
        element.setAttribute(VDXMLConstants.NAME_ATTRIBUTE,
                             attributes.getName());
        element.setAttribute(VDXMLConstants.LIBREVAL_ATTRIBUTE,
                             VDXMLConstants.NO_VALUE);

        addStandardColours(attributes, element,
                           VDXMLConstants.INPUT_FIELD_ELEMENT);

        String initialValue = getInitialValue(attributes);
        String init = null;
        if (initialValue != null &&
                initialValue.equals(VDXMLConstants.FALSE_NUMERIC_VALUE)) {
            if (falseValues != null) {
                // Set to the first of the false values
                init = falseValues[0];
            } else {
                // Default
                init = VDXMLConstants.NO_STRING;
            }
        } else {
            if (trueValues != null) {
                // Set to the first of the true values
                init = trueValues[0];
            } else {
                // Default
                init = VDXMLConstants.YES_STRING;
            }
        }
        element.setAttribute(VDXMLConstants.INIT_ATTRIBUTE, init);

        // Handle the False ALIAS(es)
        handleAliases(falseValues,
                      dom,
                      VDXMLConstants.NO_STRING,
                      VDXMLConstants.FALSE_NUMERIC_VALUE);

        // Handle the True ALIAS(es)
        handleAliases(falseValues,
                      dom,
                      VDXMLConstants.YES_STRING,
                      VDXMLConstants.TRUE_NUMERIC_VALUE);

        // Add any helpful prompt set for this node
        addPrompt(dom, attributes.getPrompt());

        dom.closeElement(element);
    }

    /**
     * Given a list of string values for an alias, generate suitable alias
     * markup in the dom provided using the value provided with those alias
     * strings.  The defaultAlias is used if no string values are provided/
     *
     * @param values       The values to generate aliases for.  This may be
     *                     null or empty in which case defaultAlias will be
     *                     used and should be valid.
     * @param dom          The output buffer to add the markup to
     * @param defaultAlias The default alias to use if there are no values
     * @param value        The value to use for the aliases.
     */
    private void handleAliases(String[] values,
                               DOMOutputBuffer dom,
                               String defaultAlias,
                               String value) {
        if (values == null || values.length == 0) {
            addAliasElement(dom, defaultAlias, value);
        } else {
            for (int i = 0; i < values.length; i++) {
                addAliasElement(dom, values[i], value);
            }
        }
    }

    // JavaDoc inherited
    public void doSelectInput(XFSelectAttributes attributes)
            throws ProtocolException {

        DOMOutputBuffer dom;
        ContainerInstance entryContainerInstance =
                attributes.getEntryContainerInstance();

        // Add the caption to the caption container.
        if (!attributes.isMultiple()) {
            addFormCaption(attributes);
        }

        // If the entry containerInstance is not set then return as there is
        // nothing to do
        if (entryContainerInstance == null) {
            if (logger.isDebugEnabled()){
                logger.debug("Entry container instance not set");
            }
            return;
        }

        // Ensure that any form fragmentation is considered when
        // determining what should be selected
        updateSelectedOptions(attributes);

        SelectionRenderer selectionRenderer = getSelectionRenderer(attributes);

        // Direct the markup to the entry paneInstance's content buffer.
        dom = getCurrentBuffer(entryContainerInstance);

        selectionRenderer.renderSelection(attributes, dom);
    }

    /**
     * Obtain the selection renderer to use, given the select attributes.
     *
     * @param attributes The select attributes to render
     * @return           An instance of the selection renderer to use when
     *                   generating markup for a select option in a form
     */
    private SelectionRenderer getSelectionRenderer(
            XFSelectAttributes attributes) {
        return new DefaultSelectionRenderer();
    }

    /**
     * Generate opening markup for an input field element and setup any
     * attributes on that element as necessary.
     *
     * @param dom          The output buffer to add the markup to
     * @param attributes   The attributes to extract information from
     * @param initialValue The initial value specified in the xdime
     * @return             The element created
     */
    private Element openFormFieldElement(DOMOutputBuffer dom,
                                         XFSelectAttributes attributes,
                                         String initialValue) {
        Element element = dom.openElement(VDXMLConstants.INPUT_FIELD_ELEMENT);

        // set any attribute values that should be applied for all form fields.
        addFormFieldAttributes(element, attributes);

        // set specific attribute values
        element.setAttribute(VDXMLConstants.NAME_ATTRIBUTE,
                             attributes.getName());
        element.setAttribute(VDXMLConstants.LIBREVAL_ATTRIBUTE,
                             VDXMLConstants.NO_VALUE);
        addStandardColours(attributes, element,
                           VDXMLConstants.INPUT_FIELD_ELEMENT);

        if (initialValue != null) {
            String init = findInitialValue(attributes, initialValue);
            if (init != null) {
                element.setAttribute(VDXMLConstants.INIT_ATTRIBUTE, init);
            }
        }
        return element;
    }

    /**
     * Generate closing markup for an input field element.
     *
     * @param dom The output buffer to add the markup to
     */
    private void closeFormFieldElement(DOMOutputBuffer dom) {
        dom.closeElement(VDXMLConstants.INPUT_FIELD_ELEMENT);
    }

    /**
     * A convenience method that given some attributes and an element (along
     * with its name) will add the text colour and background colour attributes
     * to that element.  It will also retrieve the styles necessary to be
     * able to generate this markup.
     *
     * @param attributes  The attributes to extract the colour information from
     * @param element     The element to add the colours to
     * @param elementName The name of the element
     */
    private void addStandardColours(XFFormFieldAttributes attributes,
                                      Element element,
                                      String elementName) {
        // Needed to set up the colours using the theme
        Styles styles = attributes.getStyles();

        if (styles != null) {
            PropertyValues propertyValues = styles.getPropertyValues();
            if (propertyValues != null) {
                // Style/theme exists to copy attributes from, so do so!
                addColourAttribute(element,
                        propertyValues.getComputedValue(
                                StylePropertyDetails.COLOR),
                        elementName,
                        VDXMLConstants.TEXT_COLOUR_ATTRIBUTE);

                addColourAttribute(element,
                        propertyValues.getComputedValue(
                                StylePropertyDetails.BACKGROUND_COLOR),
                        elementName,
                        VDXMLConstants.BACKGROUND_COLOUR_ATTRIBUTE);
            }
        }
    }

    /**
     * Add prompt information to a given output buffer.  This is helpful
     * information that is provided by the xdime and can be displayed to the
     * user as necessary.  In VDXML this is achieved through the use of the
     * {@link VDXMLConstants#HELP_ZONE_ELEMENT}.
     *
     * @param dom    The output buffer to add the element to
     * @param prompt The messsage to output as information to ther user
     */
    private void addPrompt(DOMOutputBuffer dom, TextAssetReference prompt) {

        if (helpZonePane != null) {
            // Only output help information if there is a valid help zone
            // pane in the layout otherwise it will create invalid VDXML

            if (prompt != null) {
                // Ensure there is also a help object to use!

                // Add the select and the actual message
                dom.openElement(VDXMLConstants.SELECT_ELEMENT);
                createHelpElement(dom,
                                  getPlainText(prompt),
                                  null);
                dom.closeElement(VDXMLConstants.SELECT_ELEMENT);

                // Add a deselect to clear the prompt (like onExit event)
                dom.openElement(VDXMLConstants.DESELECT_ELEMENT);
                dom.addElement(VDXMLConstants.HELP_ELEMENT);
                dom.closeElement(VDXMLConstants.DESELECT_ELEMENT);
            }
        }
    }

    /**
     * Create an alias element in the dom provided with the value and alias
     * attributes as specified.
     *
     * @param dom   The output buffer to add the element to
     * @param value The value to set on the alias element
     * @param alias The alias to set on the alias element
     * @return      The element created
     */
    private Element addAliasElement(DOMOutputBuffer dom,
                                    String value,
                                    String alias) {
        Element aliasElement = dom.addElement(VDXMLConstants.ALIAS_ELEMENT);
        aliasElement.setAttribute(VDXMLConstants.VALUE_ATTRIBUTE, value);
        aliasElement.setAttribute(VDXMLConstants.ALIAS_ATTRIBUTE, alias);

        return aliasElement;
    }

    /**
     * Given select attributes, extract all possible options and then
     * generate an alias element for each of them.
     *
     * @param dom        The output buffer to add the markup to
     * @param attributes The attributes to extract the options from
     */
    private void generateALIASElements(DOMOutputBuffer dom,
                                       XFSelectAttributes attributes) {
        // Handle top level options
        handleOptionGroups(dom, attributes.getOptionGroup());
    }

    /**
     * Recurse over the list of options and option groups provided and generate
     * alias elements for all those encountered.
     *
     * @param dom         The output buffer to add the markup to
     * @param optionGroup The list of options to handle
     */
    private void handleOptionGroups(DOMOutputBuffer dom, List optionGroup) {
        for (Iterator i = optionGroup.iterator(); i.hasNext(); /* */) {
            Object item = i.next();
            if (item instanceof SelectOption) {
                SelectOption option = (SelectOption)item;
                String aliasValAttribute =
                        getPlainText(option.getCaption());
                addAliasElement(dom, aliasValAttribute, option.getValue());
            } else if (item instanceof SelectOptionGroup) {
                handleOptionGroups(
                        dom,
                        ((SelectOptionGroup)item).getSelectOptionList());
            }
        }
    }

    /**
     * Given a some form select attributes and an initial string (as specified
     * in the xdime markup) then search through the options within those
     * attributes to find the appropriate initial string to display to the
     * user on the device.  This is necessary because the options can be
     * mapped in VDXML from  the value that the user sees to another value
     * that the server expects.

     * @param attributes The attributes from which to extract the options
     * @param init       The initial value to match
     * @return           The string to display to the user, which may be null
     *                   if no match was found.
     */
    private String findInitialValue(XFSelectAttributes attributes,
                                    String init) {
        String initialString = null;

        List options = attributes.getOptions();
        Iterator i = options.iterator();
        boolean finished = false;

        while ((i.hasNext()) && (!finished)) {
            SelectOption option = (SelectOption)i.next();
            String value = option.getValue();
            if (value != null && value.equals(init)) {
                initialString = getPlainText(option.getCaption());
                finished = true;
            }
        }
        if (!finished) {
            initialString =
                    findInitialValueinGroups(attributes.getOptionGroup(), init);
        }
        return initialString;
    }

    /**
     * Given a list of option groups and an initial string (as specified in the
     * xdime markup) then search through the options to find the appropriate
     * initial string to display to the user on the device.  This is necessary
     * because the options can be mapped in VDXML from  the value that the
     * user sees to another value that the server expects.
     *
     * @param optionGroup Theoption groups to search through
     * @param init        The initial value to match
     * @return            The string to display to the user, which may be null
     *                    if no match was found.
     */
    private String findInitialValueinGroups(List optionGroup, String init) {
        String initialString = null;
        Iterator i = optionGroup.iterator();
        boolean finished = false;
        while ((i.hasNext()) && (!finished)) {
            Object item = i.next();
            if (item instanceof SelectOption) {
                SelectOption option = (SelectOption)item;
                String value = option.getValue();
                if (value != null && value.equals(init)) {
                    initialString = getPlainText(option.getCaption());
                    finished = true;
                }
            } else if (item instanceof SelectOptionGroup) {
                findInitialValueinGroups(
                        ((SelectOptionGroup)item).getSelectOptionList(),
                        init);
            }
        }
        return initialString;
    }

    // JavaDoc inherited
    public FieldHandler getFieldHandler(MultipleSelectFieldType type) {
        return VDXMLMultipleSelectFieldHandler.getSingleton();
    }

    // JavaDoc inherited
    public FieldHandler getFieldHandler (ActionFieldType type) {
        return VDXMLActionFieldHandler.getSingleton();
    }

    // JavaDoc inherited
    public void doTextInput(XFTextInputAttributes attributes)
            throws ProtocolException {

        DOMOutputBuffer dom;
        ContainerInstance entryContainerInstance =
                attributes.getEntryContainerInstance();

        // Add the caption to the caption container.
        addFormCaption(attributes);

        // If the entry containerInstance is not set then return as there is
        // nothing to do
        if (entryContainerInstance == null) {
            if (logger.isDebugEnabled()){
                logger.debug("Entry container instance not set");
            }
            return;
        }

        // Direct the markup to the entry containerInstance's content buffer.
        dom = getCurrentBuffer(entryContainerInstance);

        // Generate the markup
        Element element = dom.openElement(VDXMLConstants.INPUT_FIELD_ELEMENT);

        // set any attribute values that should be applied for all form fields.
        addFormFieldAttributes(element, attributes);

        // set specific attribute values
        element.setAttribute(VDXMLConstants.NAME_ATTRIBUTE,
                             attributes.getName());
        element.setAttribute(VDXMLConstants.LIBREVAL_ATTRIBUTE,
                             VDXMLConstants.YES_VALUE);
        addStandardColours(attributes, element,
                           VDXMLConstants.INPUT_FIELD_ELEMENT);

        String initialValue = getInitialValue(attributes);
        if (initialValue != null && !initialValue.equals("")) {
            element.setAttribute(VDXMLConstants.INIT_ATTRIBUTE, initialValue);
        }

        // Add any helpful prompt set for this node
        addPrompt(dom, attributes.getPrompt());

        dom.closeElement(element);
    }

    /**
     * Add any caption value to the caption container.
     *
     * @param attributes the form field attributes with the caption value.
     */
    private void addFormCaption(XFFormFieldAttributes attributes) {
        ContainerInstance captionContainerInstance =
                attributes.getCaptionContainerInstance();
        String captionValue;

        // Add the caption to the caption container.
        if (captionContainerInstance != null
            && (captionValue = getPlainText(attributes.getCaption())) != null) {
            writeCaption(captionContainerInstance, attributes, captionValue);
        }
    }

    /**
     * Allows a caption to be added to a specific container with the given text
     * using the attributes specified.  If the caption container and value are
     * to be extracted out of the attributes then {@link #addFormCaption}
     * should be used instead of this method.
     *
     * @param captionContainerInstance  The containerInstance to direct the caption to
     * @param attributes   The form field attributes containing stylistic info
     * @param captionValue The caption to output
     */
    private void writeCaption(ContainerInstance captionContainerInstance,
                              XFFormFieldAttributes attributes,
                              String captionValue) {
        // We need to fake an inline style for the caption to have the
        // same style as the input.
        DOMOutputBuffer captionDom = getCurrentBuffer(captionContainerInstance);
        openInline(captionDom, attributes);
        addToBuffer(captionContainerInstance, captionValue);
        closeInline(captionDom, attributes);
    }

    // JavaDoc inherited
    protected void doActionInput(DOMOutputBuffer dom,
            XFActionAttributes attributes) throws ProtocolException {

        String name = attributes.getName();
        String value = attributes.getValue();
        if (name != null && value != null) {
            Element element = dom.openElement(VDXMLConstants.IMPLICIT_ELEMENT);
            // set any attribute values that should be applied for all form fields.
            addFormFieldAttributes(element, attributes);
            element.setAttribute(VDXMLConstants.NAME_ATTRIBUTE,
                                 name);
            element.setAttribute(VDXMLConstants.VALUE_ATTRIBUTE,
                                 VDXMLConstants.ENCODED_FIELD_VALUE + value);
            dom.closeElement(element);
        }
    }

    // JavaDoc inherited
    protected void addVerticalMenuItemSeparator(DOMOutputBuffer dom) {
        dom.addElement(VDXMLConstants.LINE_BREAK_ELEMENT);
    }

    // JavaDoc inherited
    protected void addHorizontalMenuItemSeparator(DOMOutputBuffer dom) {
        // @todo later This should use the theme value as should all other implementations of this method!
        dom.appendEncoded(VDXMLConstants.VDXML_NBSP);
    }

    // JavaDoc inherited
    protected void doLineBreak(DOMOutputBuffer dom,
                               LineBreakAttributes attributes) {
        dom.addElement(VDXMLConstants.LINE_BREAK_ELEMENT);
    }

    // Javadoc inherited.
    public void outputExternalLink(String shortcut, String url) {

        if (shortcut == null) {
            throw new IllegalArgumentException("shortcut may not be null");
        }
        if (url == null) {
            throw new IllegalArgumentException("url may not be null");
        }

        // Grab a reference to the special buffer that we render RACCOURCIs
        // into. This is just inside the root of the VDXML element.
        DOMOutputBuffer dom = getExtraBuffer(PAGE_LINKS_BUFFER_NAME, true);
        Element element = dom.addElement(VDXMLConstants.LINK_ELEMENT);
        element.setAttribute(VDXMLConstants.URL_ATTRIBUTE, url);
        element.setAttribute(VDXMLConstants.FUNCTION_ATTRIBUTE,
                             VDXMLConstants.SEND_FUNCTION_STRING);
        element.setAttribute(VDXMLConstants.TEXT_ATTRIBUTE, shortcut);
    }

    // JavaDoc inherited
    protected void processBodyBuffer(DOMOutputBuffer buffer) {
        //All the RACCOURI elements need to be first in the page.
        DOMOutputBuffer links = getExtraBuffer(PAGE_LINKS_BUFFER_NAME, false);

        if (links != null) {
            buffer.addOutputBuffer(links);
        }

        // Process the everything else
        super.processBodyBuffer(buffer);
    }

    //javadoc inherited
    public boolean supportsNativeMarkup() {
        return true;
    }    

    /**
     * A default renderer for VDXML that is able to generate suitable markup
     * given form selection in xdime.
     */
    protected final class DefaultSelectionRenderer implements SelectionRenderer {

        /**
         * The attributes of the xfselect that is being rendered by this code.
         */
        private XFSelectAttributes attributes;

        /**
         * The visitor to use for the Options and Option Groups themselves.
         */
        private final RenderingVisitor visitor;

        /**
         * Initialise a new instance of this selection renderer.
         */
        public DefaultSelectionRenderer() {
            visitor = new RenderingVisitor();
        }

        // JavaDoc inherited
        public void renderSelection(
                XFSelectAttributes attributes,
                OutputBuffer buffer)
                throws ProtocolException {
            this.attributes = attributes;

            DOMOutputBuffer dom = (DOMOutputBuffer)buffer;
            openSelect(dom);
            renderOptions(dom);
            closeSelect(dom);
        }

        /**
         * Outputs any markup that is necessary (i.e. opening tags) for an
         * xfselect option before rendering any of the options.  If any
         * specialist behaviour is needed then this method can be overridden.
         *
         * @param dom The dom to output the markup to
         * @throws ProtocolException If there is a problem generating the markup
         */
        protected void openSelect(DOMOutputBuffer dom)
                throws ProtocolException {

            if (!attributes.isMultiple()) {
                // open the option element
                openFormFieldElement(dom,
                                     attributes,
                                     getInitialValue(attributes));
                // Add options
                generateALIASElements(dom, attributes);
                // Add any helpful prompt set for this node
                addPrompt(dom, attributes.getPrompt());
            }

        }

        /**
         * Outputs any markup that is necessary (i.e. closing tags) for an
         * xfselect option after rendering any options.    If any
         * specialist behaviour is needed then this method can be overridden.
         *
         * @param dom The dom to output the markup to
         */
        protected void closeSelect(DOMOutputBuffer dom) {
            if (!attributes.isMultiple()) {
                closeFormFieldElement(dom);
            }
        }

        /**
         * Renders the Options and Option Groups contained in the attributes
         * of the select statement to the given output buffer.
         *
         * @param dom The dom to output the markup to
         * @throws ProtocolException If there is a problem generating the markup
         */
        private void renderOptions(DOMOutputBuffer dom)
                throws ProtocolException {
            renderOptions(attributes.getOptions(), dom);
        }

        /**
         * Renders the Options and Option Groups to the given output buffer.
         * If any specialist behaviour is needed then this method can be
         * overridden.
         *
         * @param options The list of options and groups to render
         * @param dom     The dom to output the markup to
         * @throws ProtocolException If there is a problem generating the markup
         */
        protected void renderOptions(List options, DOMOutputBuffer dom)
                throws ProtocolException {
            Option option;

            for (int i = 0; i < options.size(); i++) {
                option = (Option)options.get(i);
                option.visit(visitor, dom);
            }
            visitor.reset();
        }

        /**
         * An implementation of a visitor that allows the code to visit
         * Options and Option Groups.  In this case it visits these in order
         * to render them to the markup.
         */
        private class RenderingVisitor implements OptionVisitor {

            // JavaDoc inherited
            public void visit(SelectOption option, Object object) {

                // Need to fully qualify access to outer class due to bug in
                // the Sun 1.2 jdk compiler
                String title = getPlainText(
                        option.getPrompt());
                option.setTitle(title);

                ContainerInstance entryContainerInstance =
                        option.getEntryContainerInstance();
                if (entryContainerInstance == null) {
                    entryContainerInstance =
                            attributes.getEntryContainerInstance();
                }

                // Get the appropriate buffer based on the entry-containerInstance
                DOMOutputBuffer dom = getCurrentBuffer(entryContainerInstance);

                String value = option.getValue();

                // Try using the caption from the option, otherwise use value

                // Need to fully qualify access to outer class due to bug in
                // the Sun 1.2 jdk compiler.
                String caption = getPlainText(
                        option.getCaption());
                if (caption == null) {
                    caption = value;
                }

                if (attributes.isMultiple()) {
                    // open the option element
                    Element optionElement =
                            dom.openElement(VDXMLConstants.INPUT_FIELD_ELEMENT);

                    optionElement.setAttribute(VDXMLConstants.NAME_ATTRIBUTE,
                                               attributes.getName());
                    optionElement.setAttribute(VDXMLConstants.LIBREVAL_ATTRIBUTE,
                                               VDXMLConstants.NO_VALUE);
                    addStandardColours(attributes, optionElement,
                                       VDXMLConstants.INPUT_FIELD_ELEMENT);

                    // Set a suitable initial value
                    if (option.isSelected()) {
                        optionElement.setAttribute(
                                VDXMLConstants.INIT_ATTRIBUTE,
                                VDXMLConstants.CHOICE_CAPITAL);
                    } else {
                        optionElement.setAttribute(
                                VDXMLConstants.INIT_ATTRIBUTE,
                                VDXMLConstants.CHOICE_EMPTY);
                    }


                    // Add selected ALIASes
                    VDXMLVersion2_0.this.addAliasElement(
                            dom,
                            VDXMLConstants.CHOICE_CAPITAL,
                            VDXMLConstants.ENCODED_FIELD_VALUE +
                                    option.getValue());
                    VDXMLVersion2_0.this.addAliasElement(
                            dom,
                            VDXMLConstants.CHOICE_LOWERCASE,
                            VDXMLConstants.ENCODED_FIELD_VALUE +
                                    option.getValue());

                    // Add not selected alias
                    addAliasElement(dom,
                                    VDXMLConstants.CHOICE_EMPTY,
                                    VDXMLConstants.CHOICE_EMPTY);

                    // Add any helpful prompt set for this node
                    addPrompt(dom, option.getPrompt());

                    // Add the caption for this option
                    ContainerInstance captionContainerInstance =
                            option.getCaptionContainerInstance();
                    if (captionContainerInstance == null) {
                        captionContainerInstance =
                                attributes.getCaptionContainerInstance();
                    }
                    writeCaption(captionContainerInstance, attributes, caption);

                    dom.closeElement(optionElement);
                } else {
                    // Single selects handled already
                }

            }

            // JavaDoc inherited
            public void visit(SelectOptionGroup optionGroup, Object object)
                    throws ProtocolException {
                // No need to handle groups - just handle their options
            }

            /**
             * Resets the state of the visitor.
             */
            public void reset() {
            }
        }
    }

    /**
     * This class allows us to search for the VDXML special destinations on
     * panes. These are special panes in VDXML and knowing that they exist may
     * be important to the markup generated.
     *
     * <p>Implemented as an inner class as it has limited applicability outside
     * of the class and it needs access to class variables.</p>
     */
    class VDXMLSpecialPaneVisitor extends FormatVisitorAdapter {

        // JavaDoc inherited
        public boolean visit(Pane pane, Object object) {
            boolean returnValue = false;

            String destination = pane.getDestinationArea();

            if (destination != null) {
                if (destination.equals(HELP_ZONE_DESTINATION)) {
                    helpZonePane = pane;
                    returnValue =  false;
                } else if (destination.equals(NAVIGATION_DESTINATION)) {
                    navigationPane = pane;
                    returnValue =  false;
                } else if (destination.equals(FRAGMENT_LINKS_DESTINATION)) {
                    fragmentLinksPane = pane;
                    returnValue =  false;
                }
            }
            return returnValue;
        }

        // JavaDoc inherited
        public boolean visit(Fragment fragment, Object object) {
            // Don't look beyond the existing fragment
            return false;
        }
    }

    //========================================================================
    // MenuModule related implementation.
    //========================================================================

    /**
     * Creates a default menu module for this protocol.
     */
    // Other javadoc inherited.
    protected MenuModule createMenuModule(
            MenuModuleRendererFactoryFilter metaFactory) {

        MenuModuleRendererFactory rendererFactory =
                new VDXMLMenuModuleRendererFactory(getRendererContext(),
                        getDeprecatedOutputLocator(),
                        getMenuModuleCustomisation(), this);

        if (metaFactory != null) {
            rendererFactory = metaFactory.decorate(rendererFactory);
        }

        return new DefaultMenuModule(getRendererContext(), rendererFactory);
    }

    /**
     * Returns the OutputBuffer in which native markup will be written.
     *
     * @param attributes Attributes that determine the output buffer to use
     * @return OutputBuffer the output buffer to be used
     * @throws ProtocolException if there is a problem with the attribute
     *                           settings or in processing the request
     */
    protected OutputBuffer getNativeMarkupOutputBuffer(
        NativeMarkupAttributes attributes) throws ProtocolException {
        String target = attributes.getTargetLocation();

        OutputBuffer buffer = null;

        if (NativeMarkupAttributes.VDXML_HEAD.equals(target)) {
            if (logger.isDebugEnabled()) {
                logger.debug("nativemarkup goes in head element");
            }

            buffer = getExtraBuffer(PAGE_LINKS_BUFFER_NAME, false);

            if (buffer == null) {
                throw new ProtocolException("No canvas has been created for" +
                        "targetting the nativemarkup");
            }
        } else {
            buffer = super.getNativeMarkupOutputBuffer(attributes);
        }

        return buffer;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/7	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/5	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (6)

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 25-Nov-05	10453/1	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 25-Nov-05	9708/3	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 25-Nov-05	9708/3	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 11-Oct-05	9729/1	geoff	VBM:2005100507 Mariner Export fails with NPE

 03-Oct-05	9673/2	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 02-Oct-05	9637/8	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9637/5	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 15-Sep-05	9524/1	emma	VBM:2005091503 Added ContainerInstance to allow regions and panes to be treated in the same way

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 30-Aug-05	9353/2	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 19-Aug-05	9289/4	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 18-Aug-05	9007/3	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 19-Jul-05	9039/4	emma	VBM:2005071401 supermerge required

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 12-Jul-05	9011/1	pduffin	VBM:2005071214 Refactored StyleValueFactory to change static methods to non static

 30-Jun-05	8893/3	emma	VBM:2005062406 Annotate DOM elements generated from VDXML with styles

 09-Jun-05	8665/6	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info, some tests commented out temporarily

 20-May-05	8277/1	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 22-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 01-Oct-04	5635/5	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 24-Sep-04	5613/2	geoff	VBM:2004092215 Port VDXML to MCS: update fragment link support

 23-Sep-04	5599/1	geoff	VBM:2004092214 Port VDXML to MCS: port existing protocol code

 14-Jun-04	4643/5	steve	VBM:2004060309 Merged

 08-Jun-04	4643/2	steve	VBM:2004060309 enable asset URL suffix attribute

 09-Jun-04	4495/51	claire	VBM:2004051807 Fixed multiple select handling, including incorporating architecture clarification

 07-Jun-04	4495/48	claire	VBM:2004051807 Added JavaDoc where missing, and tidied up some existing comments

 07-Jun-04	4495/44	claire	VBM:2004051807 JavaDoc tidy up

 07-Jun-04	4652/1	geoff	VBM:2003091004 Fragment List Orientation gives incorrect orientation

 07-Jun-04	4495/42	claire	VBM:2004051807 Stopped option values being output as part of captions

 07-Jun-04	4575/28	geoff	VBM:2004051807 Minitel VDXML protocol support (add styles for form captions)

 07-Jun-04	4575/26	geoff	VBM:2004051807 Minitel VDXML protocol support

 04-Jun-04	4483/46	philws	VBM:2004051807 Fix form option handling

 04-Jun-04	4483/44	philws	VBM:2004051807 Help zone and input field colour fixes

 04-Jun-04	4575/24	geoff	VBM:2004051807 Minitel VDXML protocol support

 04-Jun-04	4495/40	claire	VBM:2004051807 Implemented dissecting panes and ensured elements are created from the DOM pool

 04-Jun-04	4483/42	philws	VBM:2004051807 Add inheritance of width and height from the VDXML element itself

 03-Jun-04	4483/40	philws	VBM:2004051807 Fix handling of NAVIG destination area

 02-Jun-04	4483/38	philws	VBM:2004051807 Fix content character encoding

 02-Jun-04	4575/19	geoff	VBM:2004051807 Minitel VDXML protocol support (reverse video, tests and some cleanup)

 02-Jun-04	4495/35	claire	VBM:2004051807 Ensure menus are treated as block elements

 02-Jun-04	4495/33	claire	VBM:2004051807 Null style fix, tidy up form fragmentation, utilise DOMPool when transforming, and some JavaDoc additions

 01-Jun-04	4495/31	claire	VBM:2004051807 Fixing form fragmentation

 01-Jun-04	4575/16	geoff	VBM:2004051807 Minitel VDXML protocol support

 01-Jun-04	4495/29	claire	VBM:2004051807 Horizontal menus and line breaks

 01-Jun-04	4575/12	geoff	VBM:2004051807 Minitel VDXML protocol support

 01-Jun-04	4495/26	claire	VBM:2004051807 Line break handling

 01-Jun-04	4483/35	philws	VBM:2004051807 Row iterated panes

 01-Jun-04	4483/33	philws	VBM:2004051807 Added image and horizontal rule handling

 28-May-04	4483/30	philws	VBM:2004051807 Navigation handling

 28-May-04	4495/23	claire	VBM:2004051807 Handling NBSP as a named entity

 28-May-04	4495/21	claire	VBM:2004051807 Form fragment links

 28-May-04	4575/10	geoff	VBM:2004051807 Minitel VDXML protocol support (block element support)

 28-May-04	4483/27	philws	VBM:2004051807 Basic help working

 28-May-04	4575/6	geoff	VBM:2004051807 Minitel VDXML protocol support (fix underline)

 28-May-04	4495/18	claire	VBM:2004051807 Menu handling and reload links

 28-May-04	4483/22	philws	VBM:2004051807 Fix form text block handling

 28-May-04	4575/3	geoff	VBM:2004051807 Minitel VDXML protocol support (incomplete inline integration)

 28-May-04	4483/18	philws	VBM:2004051807 Updates for initial property state for inline and block element processing

 28-May-04	4495/14	claire	VBM:2004051807 Colour handling for form fields and basic action support

 28-May-04	4495/11	claire	VBM:2004051807 Refactoring some of the form handling code

 27-May-04	4483/14	philws	VBM:2004051807 Better colour handling

 27-May-04	4483/11	philws	VBM:2004051807 Better colour handling

 27-May-04	4495/7	claire	VBM:2004051807 More xform capabilities

 27-May-04	4495/5	claire	VBM:2004051807 Very basic VDXML form handling

 27-May-04	4483/6	philws	VBM:2004051807 Initial end-to-end layout rendering

 27-May-04	4483/3	philws	VBM:2004051807 Functional layout transformer

 26-May-04	4495/1	claire	VBM:2004051807 Checking for NAVIG and ZONEAIDE pane destinations

 25-May-04	4483/1	philws	VBM:2004051807 Add prototypical VDXML protocol

 ===========================================================================
*/

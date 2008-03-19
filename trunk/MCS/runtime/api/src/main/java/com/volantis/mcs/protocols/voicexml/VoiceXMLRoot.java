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
 * $Header: /src/voyager/com/volantis/mcs/protocols/voicexml/VoiceXMLRoot.java,v 1.4 2003/04/29 09:03:00 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Jun-01    Paul            VBM:2001062810 - Created.
 * 23-Jul-01    Paul            VBM:2001070507 - Simplified by renaming all
 *                              the parameters to attributes.
 * 24-Jul-01    Paul            VBM:2001071103 - Changed the doActionInput
 *                              method to get the field names from the list
 *                              of fields rather than from the list of field
 *                              names.
 * 26-Jul-01    Paul            VBM:2001072301 - Fixed the generation of
 *                              prompts for paragraphs and headings and
 *                              chained the dummy forms and real forms
 *                              together.
 * 01-Aug-01    Paul            VBM:2001072506 - Implemented support for the
 *                              multiple select tag.
 * 04-Sep-01    Paul            VBM:2001081707 - Use getTextFromReference to get
 *                              the text in the correct encoding for those
 *                              attributes whose value could be a
 *                              TextComponentName.
 * 20-Sep-01    Paul            VBM:2001091202 - Added support for implicit
 *                              values.
 * 21-Sep-01    Doug            VBM:2001090302  - Use getLinkFromReference to get
 *                              a link for those attributes whose value could
 *                              be a LinkComponentName.
 * 10-Oct-01    Allan           VBM:2001083120 - Removed doMeta().
 * 15-Oct-01    Paul            VBM:2001101204 - Ignore fields which have no
 *                              entry pane when processing action fields.
 * 29-Oct-01    Paul            VBM:2001102901 - Modified to use new methods
 *                              in VolantisProtocol to retrieve context
 *                              information from Formats.
 * 07-Dec-01    Paul            VBM:2001120702 - Prevented the implicit values
 *                              from being ignored when generating the actions.
 *                              They were being ignored because they had no
 *                              entry pane set but they should always be
 *                              generated.
 * 10-Dec-01    Paul            VBM:2001110101 - Wrapped <exit/> inside
 *                              <block>..</block> as it is executable content
 *                              which is not valid directly inside <form>.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 31-Jan-02    Paul            VBM:2001122105 - Restructured to reflect
 *                              changes to protocols.
 * 13-Feb-02    Paul            VBM:2002021203 - Added getContentExtension
 *                              method.
 * 19-Feb-02    Paul            VBM:2001100102 - Added the form specifier
 *                              parameter to the parameters to be sent when
 *                              submitting a form and also make use of the new
 *                              value attribute on xfaction.
 * 28-Feb-02    Paul            VBM:2002022804 - Made methods consistent with
 *                              other StringProtocol based classes.
 * 04-Mar-02    Paul            VBM:2001101803 - Modified doActionInput due
 *                              to changes to StringProtocol.
 * 04-Mar-02    Paul            VBM:2001101803 - Modified doActionInput due
 *                              to changes to StringProtocol.
 * 13-Mar-02    Paul            VBM:2002031301 - Renamed get/endContentBuffer
 *                              to get/endCurrentBuffer respectively.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 25-Apr-02    Paul            VBM:2002042202 - Removed classic form comments.
 * 07-May-02    Adrian          VBM:2002042302 - Removed method getContent..
 *                              ..Extension() as the cached file extension is
 *                              defined in the device policies.
 * 01-Aug-02    Sumit           VBM:2002073109 - optgroup support added. Moved
 *                              option manupilation into recursive functions and
 *                              changed doMultipleSelectInput to use them
 * 09-Apr-03    Phil W-S        VBM:2002111502 - Override
 *                              resolveQualifiedFullNumber and
 *                              addPhoneNumberContents.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 17-Apr-03    Byron           VBM:2003032608 - VoiceXMLRoot now derives from
 *                              DOMProtocol. Updated (c). Modified most
 *                              methods. Added appendVoiceXMLLiteral().
 * 17-Apr-03    Adrian          VBM:2003040903 - Implemented doMenu and
 *                              doMenuItem to write vxml menus.
 * 24-Apr-03    Byron           VBM:2003042402 - Modified doMenu and changed
 *                              signature of doMenuItem.
 * 22-Apr-03    Allan           VBM:2003041710 - Added overridden methods
 *                              openSpan(), closeSpan() and skipElementBody().
 * 22-Apr-03    Byron           VBM:2003040302 - Added doDivideHint with empty
 *                              implementation (override DOMProtocol's
 *                              behaviour to ensure output is the same what the
 *                              StringProtocol's was)
 * 23-Apr-03    Allan           VBM:2003042302 - Added
 *                              createElementFromString(). Modified openSpan()
 *                              to add a src prompt as an element rather than a
 *                              literal. Modified doMenu() and doMenuItem() to
 *                              add prompts as elements rather than literals.
 *                              literal.
 * 23-Apr-03    Adrian          VBM:2003041104 - Updated doMenu to use css
 *                              emulation for dtmf and scope attributes
 * 28-Apr-03    Allan           VBM:2003042802 - Moved from protocols package.
 * 28-Apr-03    Byron           VBM:2003032608 - Modified doImplicitValue to
 *                              correctly quote the elements attribute.
 * 28-Apr-03    Adrian          VBM:2003042807 - made this an abstract class.
 * 29-Apr-03    Byron           VBM:2003041104 - Modified doMenuItem to get
 *                              PLAIN text from the object. Added check for
 *                              null values before integer is parsed.
 * 12-May-03    Phil W-S        VBM:2002111502 - Change sayas class from phone
 *                              to literal to allow handling of MSISDN
 *                              international dialling conventions.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.voicexml;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.MCSDOMContentHandler;
import com.volantis.mcs.layouts.ColumnIteratorPane;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.layouts.RowIteratorPane;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.protocols.CanvasAttributes;
import com.volantis.mcs.protocols.ColumnIteratorPaneAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DOMTransformer;
import com.volantis.mcs.protocols.DivideHintAttributes;
import com.volantis.mcs.protocols.FormAttributes;
import com.volantis.mcs.protocols.HeadingAttributes;
import com.volantis.mcs.protocols.LayoutAttributes;
import com.volantis.mcs.protocols.MenuAttributes;
import com.volantis.mcs.protocols.MenuChildVisitable;
import com.volantis.mcs.protocols.MenuItem;
import com.volantis.mcs.protocols.MenuItemGroupAttributes;
import com.volantis.mcs.protocols.MenuOrientation;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.ParagraphAttributes;
import com.volantis.mcs.protocols.PhoneNumberAttributes;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.RowIteratorPaneAttributes;
import com.volantis.mcs.protocols.SelectOption;
import com.volantis.mcs.protocols.SelectOptionGroup;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.XFActionAttributes;
import com.volantis.mcs.protocols.XFBooleanAttributes;
import com.volantis.mcs.protocols.XFFormAttributes;
import com.volantis.mcs.protocols.XFFormFieldAttributes;
import com.volantis.mcs.protocols.XFImplicitAttributes;
import com.volantis.mcs.protocols.XFSelectAttributes;
import com.volantis.mcs.protocols.XFTextInputAttributes;
import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.css.renderer.RuntimeCSSStyleSheetRenderer;
import com.volantis.mcs.protocols.forms.ActionFieldType;
import com.volantis.mcs.protocols.forms.EmulatedXFormDescriptor;
import com.volantis.mcs.protocols.forms.FieldDescriptor;
import com.volantis.mcs.protocols.layouts.ContainerInstance;
import com.volantis.mcs.runtime.URLConstants;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.MCSAuralDTMFAllocationKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.values.PropertyValues;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.Iterator;

/**
 * This class is the root for all classes which support the VoiceXML protocol.
 *
 * Issues.
 *
 * VoiceXML is built around a form, nothing happens unless it is in a form.
 * In order to support paragraphs and headings dummy forms are created around
 * each pane which is not in a form. When a form is completed it does not
 * automatically drop through to the next form in the page it simply exits so
 * we need to link the dummy forms and real forms in a chain. The dummy forms
 * are assigned an id which consists of a prefix and a number which is
 * incremented after every dummy form. When the dummy forms are generated they
 * add a <goto> tag which jumps to the next dummy form. Before generating the
 * standard form it first generates a dummy form which jumps straight to the
 * standard form.
 */
public abstract class VoiceXMLRoot extends DOMProtocol {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(VoiceXMLRoot.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(VoiceXMLRoot.class);

    private static final VoiceXMLGrammar nuanceGrammar = new NuanceGrammar();

    private final VoiceXMLGrammar grammar = nuanceGrammar;

    private final String dummyFormPrefix = "form-";
    private int nestingDepth;
    private int formCount;

    /**
     * Tag name used when writing out implicit content.
     */
    private static final String IMPLICIT_ELEMENT = "var";

    /**
     * Tag name used when writing out form emulation elements.
     */
    private static final String FORM_EMULATION_ELEMENT = "form";

    /**
     * This buffer is used by form fields if they need to create sub dialog
     * forms. It is appended to the end of the form postamble buffer, after
     * the form has been closed.
     */
    private DOMOutputBuffer subDialogs;

    /**
     * The main transformer for this protocol.
     */
    private final DOMTransformer transformer = new VoiceXMLTransformer();

    protected VoiceXMLRoot(ProtocolSupportFactory supportFactory,
            ProtocolConfiguration configuration) {
        super(supportFactory, configuration);

        styleSheetRenderer = RuntimeCSSStyleSheetRenderer.getSingleton();
    }

    // Javadoc inherited from super class.
    public void initialise() {
        super.initialise();
    }

    // Javadoc inherited from super class.
    public String defaultMimeType() {
        // NOTE: Currently returns an empty string, which is consistent with
        // previous behaviour. There appears to be a proposed mime type for
        // VoiceXML (application/voicexml+xml) which should probably be
        // substituted here if it is accepted.
        // See: http://eikenes.alvestrand.no/pipermail/ietf-types/2003-December/000133.html
        return "";
    }

    /**
     * Override this method to return a VoiceXMLTransformer.
     */
    protected DOMTransformer getDOMTransformer() {
        return transformer;
    }

    /**
     * Override skipElementBody to return the current value of skipElementBody
     * and then reset.
     * @return skipElementBody
     */
    public boolean skipElementBody() {
        boolean result = super.skipElementBody();
        setSkipElementBody(false);
        return result;
    }

    /**
     * Given some text that is well formed xml, create an element structure
     * that fully represents all the text in DOM format.
     * <p/>
     * <strong>NOTE:</strong> the text must currently be a complete DOM
     * document (i.e. it must have a single root node).
     *
     * @param s The xml text.
     * @return The Element that is a DOM representation of text.
     * @todo later enable this method to handle document fragments
     */
    protected Element createElementFromString(String s)
            throws ProtocolException {

        // Create the SAX and DOM Parsers, and link them.
        MCSDOMContentHandler domParser = new MCSDOMContentHandler();
        // Note: explicit package for SAXParser to be clear about it's source.
        XMLReader saxParser =
                new com.volantis.xml.xerces.parsers.SAXParser();
        saxParser.setContentHandler(domParser);

        try {
            // Parse the XML.
            StringReader stringReader = new StringReader(s);
            InputSource source = new InputSource(stringReader);
            saxParser.parse(source);
        } catch(SAXException e) {
            throw new ProtocolException(
                        exceptionLocalizer.format("parse-error", s), e);
        } catch(IOException e) {
            // NOTE: we are currently reading from a string, so this ought
            // never to happen anyway.
            throw new ProtocolException(
                        exceptionLocalizer.format("parse-error", s), e);
        }

        // Add the parsed nodeValue into the dom, minus the root element.
        Document document = domParser.getDocument();

        return document.getRootElement();
    }


    /**
     * Add the open span markup to the specified DOMOutputBuffer.
     * @param dom The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    public void openSpan (DOMOutputBuffer dom,
                             SpanAttributes attributes)
            throws ProtocolException {

        openPrompt(dom);

        TextAssetReference src = attributes.getSrc();
        if(src!=null) {
            String value = getTextFromReference(src, TextEncoding.VOICE_XML_PROMPT);
            if(value!=null) {
                // We expect the value to be valid voice xml - or more
                // importantly valid xml. In order for the a DOMTransformer
                // to work with this valid xml, it (the xml) must be added
                // to the dom an node hierarchy as oppose to literal text.
                Element element = createElementFromString(value);
                dom.addElement(element);
                setSkipElementBody(true);
            }
        }
    }

    /**
     * Add the close span markup to the specified DOMOutputBuffer.
     * @param dom The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    public void closeSpan (DOMOutputBuffer dom,
                              SpanAttributes attributes) {
        closePrompt(dom);
    }

    // ==========================================================================
    //   General helper methods
    // ==========================================================================
    private void openPrompt(DOMOutputBuffer dom) {

        // If we are not within a <block><prompt> then generate one
        if (nestingDepth == 0) {
            dom.openElement("block");
            dom.openElement("prompt");
        }

        // Keep count of how many tags who have asked for a prompt are
        // still open.
        nestingDepth += 1;
    }

    private void closePrompt(DOMOutputBuffer dom) {

        // Keep count of how many tags who have asked for a prompt are
        // still open.
        nestingDepth -= 1;

        // If no tags are still open then close.
        if (nestingDepth == 0) {
            dom.closeElement("prompt");
            dom.closeElement("block");
        }
    }

    // ==========================================================================
    //   Page element methods
    // ==========================================================================

    // Javadoc inherited from super class.
    protected void openCanvas(DOMOutputBuffer dom,
                              CanvasAttributes attributes) {

        Element element = dom.openStyledElement("vxml", attributes);
        element.setAttribute("version", "1.0");
    }

    // Javadoc inherited from super class.
    protected void closeCanvas(DOMOutputBuffer dom,
                               CanvasAttributes attributes) {
        dom.closeElement("vxml");
    }

    // ==========================================================================
    //   Dissection methods
    // ==========================================================================

    // ==========================================================================
    //   Dialling methods
    // ==========================================================================

    /**
     * The full number is always output (even if there is content), wrappered
     * in a sayas to ensure that it is correctly vocalized as a phone number.
     */
    protected void addPhoneNumberContents(DOMOutputBuffer dom,
                                          PhoneNumberAttributes attributes) {
        Object contents = attributes.getContent();
        String defaultContents = attributes.getDefaultContents();

        openPrompt(dom);

        if (contents instanceof DOMOutputBuffer) {
            DOMOutputBuffer contentBuffer = (DOMOutputBuffer) contents;

            if (!contentBuffer.isEmpty()) {
                dom.addOutputBuffer(contentBuffer);
            }
        } else if (contents != null) {
            dom.appendEncoded(contents.toString());
        }

        if (defaultContents != null) {

            // By checking for markup we allow for the use of a text asset that
            // has been specifically tailored for VoiceXML
            if (defaultContents.indexOf("<") == -1) { // @todo put test in separate method
                Element element = dom.openStyledElement("sayas", attributes);
                element.setAttribute("class", "literal");
                dom.appendEncoded(defaultContents);
                dom.closeElement("sayas");
            } else {
                dom.appendEncoded(defaultContents);
            }
        }

        closePrompt(dom);
    }

    /**
     * Augments the superclass version to ensure that the qualified full number
     * does not include any '+' prefix (which is not allowed by this protocol)
     */
    protected String resolveQualifiedFullNumber(String fullNumber) {
        String noPrefixFullNumber = fullNumber;

        if (noPrefixFullNumber.charAt(0) == '+') {
            noPrefixFullNumber = noPrefixFullNumber.substring(1);
        }

        return super.resolveQualifiedFullNumber(noPrefixFullNumber);
    }

    // ==========================================================================
    //   Layout / format methods
    // ==========================================================================

    // Javadoc inherited from super class.
    protected
            void openColumnIteratorPane(DOMOutputBuffer dom,
                                        ColumnIteratorPaneAttributes attributes) {
        openPane(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected
            void closeColumnIteratorPane(DOMOutputBuffer dom,
                                         ColumnIteratorPaneAttributes attributes) {
        closePane(dom, attributes);
    }

    protected void openForm(DOMOutputBuffer dom,
                            FormAttributes attributes) {
        // Restore the insertion point back to the saved position before
        // rendering the contents of the form.
        dom.restoreInsertionPoint();
    }

    // Javadoc inherited from super class.
    protected void closeForm(DOMOutputBuffer dom,
                             FormAttributes form) {
    }

    // Javadoc inherited from super class.
    protected void openLayout(DOMOutputBuffer dom,
                              LayoutAttributes attributes) {
    }

    // Javadoc inherited from super class.
    protected void closeLayout(DOMOutputBuffer dom,
                               LayoutAttributes attributes) {

        // Create the terminating form in the chain of forms.
        // @todo 2005060816 annotate child with style information if it's not inherited from the parent
        Element element = dom.openStyledElement("form", attributes);
        element.setAttribute("id", dummyFormPrefix + formCount);
        dom.openElement("block");
        dom.addElement("exit");
        dom.closeElement("block");
        dom.closeElement("form");
    }

    // Javadoc inherited from super class.
    protected void openPane(DOMOutputBuffer dom,
                            PaneAttributes attributes) {

        Pane pane = attributes.getPane();

        // If this pane is not within a form then write out a <form> tag as
        // the content assumes that it is in a form.
        if (pane.getEnclosingForm() == null) {

            // A form with a dummy id which is used to move from one
            // form to another.
            Element element = dom.openStyledElement("form", attributes);
            element.setAttribute("id", dummyFormPrefix + formCount);
        }
    }

    // Javadoc inherited from super class.
    protected void closePane(DOMOutputBuffer dom,
                             PaneAttributes attributes) {

        Pane pane = attributes.getPane();

        // If this pane is not within a form then write out a <form> tag as
        // the content assumes that it is in a form.
        if (pane.getEnclosingForm() == null) {
            // @todo 2005060816 annotate child with style information if it's not inherited from the parent
            dom.openStyledElement("block", attributes);
            Element element = dom.openElement("goto");

            // Move onto the next form.
            formCount += 1;

            // Generate a goto which will move onto the next form.
            element.setAttribute("next", "#" + dummyFormPrefix + formCount);
            dom.closeElement("goto");
            dom.closeElement("block");
            dom.closeElement("form");
        }
    }

    // Javadoc inherited from super class.
    protected
            void openRowIteratorPane(DOMOutputBuffer dom,
                                     RowIteratorPaneAttributes attributes) {

        openPane(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected
            void closeRowIteratorPane(DOMOutputBuffer dom,
                                      RowIteratorPaneAttributes attributes) {

        closePane(dom, attributes);
    }

    // ==========================================================================
    //   Navigation methods.
    // ==========================================================================

    // ==========================================================================
    //   Block element methods.
    // ==========================================================================

    // Javadoc inherited from super class.
    protected void openHeading1(DOMOutputBuffer dom,
                                HeadingAttributes attributes) {
        openPrompt(dom);
    }

    // Javadoc inherited from super class.
    protected void closeHeading1(DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {
        closePrompt(dom);
    }

    // Javadoc inherited from super class.
    protected void openHeading2(DOMOutputBuffer dom,
                                HeadingAttributes attributes) {
        openPrompt(dom);
    }

    // Javadoc inherited from super class.
    protected void closeHeading2(DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {
        closePrompt(dom);
    }

    // Javadoc inherited from super class.
    protected void openHeading3(DOMOutputBuffer dom,
                                HeadingAttributes attributes) {
        openPrompt(dom);
    }

    // Javadoc inherited from super class.
    protected void closeHeading3(DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {
        closePrompt(dom);
    }

    // Javadoc inherited from super class.
    protected void openHeading4(DOMOutputBuffer dom,
                                HeadingAttributes attributes) {
        openPrompt(dom);
    }

    // Javadoc inherited from super class.
    protected void closeHeading4(DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {
        closePrompt(dom);
    }

    // Javadoc inherited from super class.
    protected void openHeading5(DOMOutputBuffer dom,
                                HeadingAttributes attributes) {
        openPrompt(dom);
    }

    // Javadoc inherited from super class.
    protected void closeHeading5(DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {
        closePrompt(dom);
    }

    // Javadoc inherited from super class.
    protected void openHeading6(DOMOutputBuffer dom,
                                HeadingAttributes attributes) {
        openPrompt(dom);
    }

    // Javadoc inherited from super class.
    protected void closeHeading6(DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {
        closePrompt(dom);
    }

    // Javadoc inherited from super class.
    protected void openParagraph(DOMOutputBuffer dom,
                                 ParagraphAttributes attributes) {
        openPrompt(dom);
    }

    // Javadoc inherited from super class.
    protected void closeParagraph(DOMOutputBuffer dom,
                                  ParagraphAttributes attributes) {
        closePrompt(dom);
    }

    // ==========================================================================
    //   List element methods.
    // ==========================================================================

    // ==========================================================================
    //   Table element methods.
    // ==========================================================================

    // ==========================================================================
    //   Inline element methods.
    // ==========================================================================

    // ==========================================================================
    //   Special element methods.
    // ==========================================================================

    // ==========================================================================
    //   Menu element methods.
    // ==========================================================================

    // Javadoc inherited.
    public void doMenu(MenuAttributes attributes) throws ProtocolException {

        Styles styles = attributes.getStyles();
        PropertyValues propertyValues = styles.getPropertyValues();

        // Initialise the content buffer.
        Pane pane = attributes.getPane();
        // NOTE: old menu code is unused.
        ContainerInstance containerInstance =
                (ContainerInstance) context.getFormatInstance(pane,
                        NDimensionalIndex.ZERO_DIMENSIONS);
        DOMOutputBuffer dom = (DOMOutputBuffer)
                containerInstance.getCurrentBuffer();

        Element menu = dom.openStyledElement("menu", attributes);

        // Add the id attribute if there is one.
        String id = attributes.getId();
        if (id != null) {
            menu.setAttribute("id", id);
        }

        StyleValue styleValue;
        styleValue = propertyValues.getComputedValue(
                StylePropertyDetails.MCS_AURAL_MENU_SCOPE);
        String scope = styleValue.getStandardCSS();
        menu.setAttribute("scope", scope);

        styleValue = propertyValues.getComputedValue(
                StylePropertyDetails.MCS_AURAL_DTMF_ALLOCATION);
        boolean manualDTMF = styleValue == MCSAuralDTMFAllocationKeywords.MANUAL;
        if (!manualDTMF) {
            menu.setAttribute("dtmf", "true");
        }
        attributes.setManualDTMF(manualDTMF);

        // Add the prompt markup.
        TextAssetReference object = attributes.getPrompt();
        if (object != null) {
            String prompt =
                    getTextFromReference(object, TextEncoding.VOICE_XML_PROMPT);
            if (prompt != null) {
                Element element = createElementFromString(prompt);
                dom.addElement(element);
            }
        }

        boolean isIteratorPane = false;
        if (pane instanceof RowIteratorPane) {
            isIteratorPane = true;
        } else if (pane instanceof ColumnIteratorPane) {
            isIteratorPane = true;
        }

        // Add the menu items.
        Collection items = attributes.getItems();
        for (Iterator i = items.iterator(); i.hasNext();) {

            ((MenuChildVisitable) i.next()).visit(
                    this, dom, attributes,i.hasNext(), isIteratorPane,
                    MenuOrientation.HORIZONTAL);
            // The pane is an iterator pane which matches the orientation
            // required for the menu so each menu item goes in its own buffer.
            // End the current buffer and then get the next buffer.
            containerInstance.endCurrentBuffer();
            dom = (DOMOutputBuffer) containerInstance.getCurrentBuffer();
        }

        // Add the help markup
        object = attributes.getHelp();
        if (object != null) {
            String help =
                    getTextFromReference(object, TextEncoding.VOICE_XML_HELP);
            if (help != null) {
                dom.appendLiteral(help);
            }
        }

        // Add the help markup
        object = attributes.getErrmsg();
        if (object != null) {
            String error =
                    getTextFromReference(object, TextEncoding.VOICE_XML_ERROR);
            if (error != null) {
                dom.appendLiteral(error);
            }
        }

        // close the element.
        dom.closeElement(menu);
    }

    // javadoc inherited
    public void renderMenuChild(DOMOutputBuffer dom,
                                MenuAttributes attributes,
                                MenuItem child,
                                boolean notLast,
                                boolean iteratorPane,
                                MenuOrientation orientation)
            throws ProtocolException {
        doMenuItem(dom, attributes, child);
    }

    // javadoc inherited
    public void renderMenuChild(DOMOutputBuffer dom,
                                MenuAttributes attributes,
                                MenuItemGroupAttributes child,
                                boolean notLast,
                                boolean iteratorPane,
                                MenuOrientation orientation)
            throws ProtocolException {

    }

    // javadoc inherited
    protected boolean doMenuItem(DOMOutputBuffer dom,
                                 MenuAttributes attributes,
                                 MenuItem item) throws ProtocolException {

        // Add a menu item to the specified DOMOutputBuffer.  If the manualDTMF
        // argument is true the a dtmf value is added from the shortcut property
        // of the menuitem.  Otherwise the specified index is used as the dtmf.
        // All dtmf values are validated to check that they are in the range 1..9
        // before we attempt to add the value.
        String resolvedHref = null;
        LinkAssetReference href = item.getHref();
        if (href != null) {
            resolvedHref = getRewrittenLinkFromObject(href,
                    item.getSegment() != null);
        }

        if (resolvedHref != null) {

            Element choice = dom.openStyledElement("choice", item);
            choice.setAttribute("next", resolvedHref);

            TextAssetReference object = null;

            if (attributes.isManualDTMF()) {
                object = item.getShortcut();
                String accessKey = getPlainText(object);
                if (accessKey != null) {
                    try {
                        int dtmf = Integer.parseInt(accessKey);
                        if (dtmf < 1 || dtmf > 9) {
                            logger.warn("dtmf-range-bounds-error", new Object[]{new Integer(dtmf)});
                        } else {
                            choice.setAttribute("dtmf", String.valueOf(dtmf));
                        }
                    } catch (NumberFormatException nfe) {
                        logger.warn("dtmf-menuitem-failure", new Object[]{item, accessKey});
                    }
                }
            }

            // Add the prompt
            object = item.getPrompt();
            if (object != null) {
                String prompt =
                        getTextFromReference(object, TextEncoding.VOICE_XML_PROMPT);
                if (prompt != null) {
                    Element element = createElementFromString(prompt);
                    dom.addElement(element);
                }
            } else {
                // if there wasn't a prompt then fallback to using the content
                dom.appendEncoded(item.getText());
            }

            dom.closeElement(choice);
        }
        return false;
    }

    // ==========================================================================
    //   Script element methods.
    // ==========================================================================

    // ==========================================================================
    //   Extended function form element methods.
    // ==========================================================================

    // ==========================================================================
    //   Extended function form element methods.
    // ==========================================================================

    protected void openForm(XFFormAttributes attributes) {
        DOMOutputBuffer dom = getContentBuffer(attributes.getFormData());

        // Remember that we are inside the form.
        if (nestingDepth != 0) {
            throw new IllegalStateException("Nesting depth is " + nestingDepth);
        }
        nestingDepth = 1;

        // Generate the markup.
        // @todo 2005060816 annotate child with style information if it's not inherited from the parent
        Element element = dom.openStyledElement("form", attributes);
        element.setAttribute("id", attributes.getName());

        String value;
        // Add the prompt which should already be properly quoted as it contains
        // VoiceXML markup.
        if ((value = getTextFromReference(attributes.getPrompt(),
                                       TextEncoding.VOICE_XML_PROMPT)) != null) {
            dom.openElement("block");
            dom.appendLiteral(value);
            dom.closeElement("block");
        }

        // Add the help which should already be properly quoted as it contains
        // VoiceXML markup.
        if ((value = getTextFromReference(attributes.getHelp(),
                                       TextEncoding.VOICE_XML_HELP)) != null) {
            dom.appendLiteral(value);
        }

        // Initialise a variable which contains the form specifier.
        element = dom.addElement("var");
        element.setAttribute("name", URLConstants.FORM_PARAMETER);
        element.setAttribute("expr", "'" + getFormSpecifier(attributes) + "'");

        // Create a new sub dialog buffer.
        //dom.saveInsertionPoint();
        subDialogs = allocateOutputBuffer();

        // Remember the current insertion point in the dom buffer as that is
        // where we need to insert the contents of the form which will only be
        // generated during the rendering of the layout.
        dom.saveInsertionPoint();

    }

    // Javadoc inherited.
    public Element createXFormEmulationElement(String formName,
            EmulatedXFormDescriptor fd) {

        // Generate the markup.
        Element element = domFactory.createElement();
        element.setName(FORM_EMULATION_ELEMENT);
        element.setAttribute("id", formName);
        return element;
    }

    // Javadoc inherited.
    public boolean isXFormEmulationElement(Element element) {
        boolean result = false;
        if (element.getName().equals(FORM_EMULATION_ELEMENT)) {
            result = true;
        }
        return result;
    }

    // Javadoc inherited.
    public boolean isImplicitEmulationElement(Element element) {
       boolean result = false;
        if (element.getName().equals(IMPLICIT_ELEMENT)) {
            result = true;
        }
        return result;
    }

    // Javadoc inherited.
    public Element createVFormElement(String formSpecifier) {
          // Initialise a variable which contains the form specifier.
        Element element = domFactory.createElement();
        element.setName("var");
        element.setAttribute("name", URLConstants.FORM_PARAMETER);
        element.setAttribute("expr", "'" + formSpecifier + "'");
        return element;
    }

    protected void closeForm(XFFormAttributes attributes) {
        DOMOutputBuffer dom = getContentBuffer(attributes.getFormData());

        // Remember that we are no longer inside the form.
        if (nestingDepth != 1) {
            throw new IllegalStateException();
        }
        nestingDepth = 0;

        dom.closeElement("form");

        // Append the sub dialog buffer and then reset it.
        dom.addElement(subDialogs.getRoot());

        // Disconnect the DOM tree from the sub dialog in order to prevent
        // it being prematurely released with the output buffer
        subDialogs.initialise();
        subDialogs = null;
    }

    public void doTextInput(XFTextInputAttributes attributes) {

        DOMOutputBuffer dom;
        ContainerInstance entryContainerInstance;

        // If the entry container Instance is not set then return as there is
        // nothing else we can do.
        if ((entryContainerInstance =
                attributes.getEntryContainerInstance()) == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Entry container instance not set");
            }
            return;
        }

        // Direct the markup to the entry container's content buffer.
        dom = getCurrentBuffer(entryContainerInstance);

        // Generate the markup.
        Element element = dom.openStyledElement("field", attributes);

        // set any attribute values that should be applied for all form fields.
        addFormFieldAttributes(element, attributes);

        element.setAttribute("name", attributes.getName());

        appendVoiceXMLLiteral(dom, attributes);

        TextAssetReference reference = getTextReferenceFromStyleValue(
                attributes.getStyles(), StylePropertyDetails.MCS_INPUT_FORMAT);

        // Add the grammar which should already be properly quoted as it
        // contains VoiceXML markup.
        grammar.generateGrammarFromObject(dom, reference);
        dom.closeElement("field");
    }

    // javadoc inherited
    public void doBooleanInput(XFBooleanAttributes attributes) {

        DOMOutputBuffer dom;
        ContainerInstance entryContainerInstance;
        String name;

        // If the entry ContainerInstance is not set then return as there is
        // nothing else we can do.
        if ((entryContainerInstance =
                attributes.getEntryContainerInstance()) == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Entry container instance not set");
            }
            return;
        }

        // Get the name of the field.
        name = attributes.getName();

        // Direct the markup to the entry ContainerInstance's content buffer.
        dom = getCurrentBuffer(entryContainerInstance);

        // Generate the markup.
        // @todo 2005060816 annotate child with style information if it's not inherited from the parent
        Element element = dom.openStyledElement("field", attributes);
        // set any attribute values that should be applied for all form fields.
        addFormFieldAttributes(element, attributes);
        element.setAttribute("name", name);

        appendVoiceXMLLiteral(dom, attributes);

        // Generate the grammar.
        element = dom.openElement("grammar");
        grammar.generateBooleanGrammar(
                dom, name, attributes.getFalseValues(),
                attributes.getTrueValues());
        dom.closeElement("grammar");
        dom.closeElement("field");
    }

    public void doSelectInput(XFSelectAttributes attributes) {
        if (attributes.isMultiple()) {
            doMultipleSelectInput(attributes);
        } else {
            doSingleSelectInput(attributes);
        }
    }

    private void doSingleSelectInput(XFSelectAttributes attributes) {

        DOMOutputBuffer dom;
        ContainerInstance entryContainerInstance;
        String name;

        // If the entry ContainerInstance is not set then return as there is
        // nothing else we can do.
        if ((entryContainerInstance =
                attributes.getEntryContainerInstance()) == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Entry container instance not set");
            }
            return;
        }

        // Get the name of the field.
        name = attributes.getName();

        // Direct the markup to the entry container's content buffer.
        dom = getCurrentBuffer(entryContainerInstance);

        // Generate the markup.
        // @todo 2005060816 annotate child with style information if it's not inherited from the parent
        Element element = dom.openStyledElement("field", attributes);
        // set any attribute values that should be applied for all form fields.
        addFormFieldAttributes(element, attributes);
        element.setAttribute("name", name);

        appendVoiceXMLLiteral(dom, attributes);

        // Generate the grammar.
        Collection options = attributes.getOptions();

        dom.openElement("grammar");
        grammar.generateSingleSelectGrammar(dom, this, name, options);
        dom.closeElement("grammar");
        dom.closeElement("field");
    }

    private void doMultipleSelectInput(XFSelectAttributes attributes) {

        DOMOutputBuffer dom;
        ContainerInstance entryContainerInstance;
        String name;

        // If the entry container instance is not set then return as there is
        // nothing else we can do.
        if ((entryContainerInstance =
                attributes.getEntryContainerInstance()) == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Entry container instance not set");
            }
            return;
        }

        // Get the name of the field.
        name = attributes.getName();

        // How to determine the name of the xfform (in which this subdialog
        // appears) varies depending on whether this is an emulated XForm or an
        // actual XFForm. If this is representing an emulated xfform, then the
        // XFFormAttributes will not be set, but the containingXFFormAttribute
        // will be and vice versa.
        String containingXFFormName;
        XFFormAttributes formAttributes = attributes.getFormAttributes();
        if (formAttributes != null) {
            containingXFFormName = formAttributes.getName();
        } else {
            containingXFFormName = attributes.getContainingXFFormName();
        }

        // Generate an id for the sub dialog, it consists of the form id
        // separated from the field name with a -.
        String subId = containingXFFormName + "-" + name;

        // Direct the markup to the entry ContainerInstance's content buffer.
        dom = getCurrentBuffer(entryContainerInstance);

        // Generate the markup.
        // @todo 2005060816 annotate child with style information if it's not inherited from the parent
        Element element = dom.openStyledElement("subdialog", attributes);
        // set any attribute values that should be applied for all form fields.
        addFormFieldAttributes(element, attributes);
        element.setAttribute("name", name);
        element.setAttribute("src", "#" + subId);

        element = dom.openElement("filled");
        element = dom.addElement("assign");
        element.setAttribute("name", name);
        element.setAttribute("expr", name + ".result");
        dom.closeElement("filled");

        dom.closeElement("subdialog");

        // Direct the rest of the markup to the sub dialogs buffer.
        dom = subDialogs;

        // @todo 2005060816 annotate child with style information if it's not inherited from the parent
        element = dom.openStyledElement("form", attributes);
        element.setAttribute("id", subId);

        // Generate the grammar.
        Collection options = attributes.getOptions();
        dom.openElement("grammar");
        grammar.generateMultipleSelectGrammar(dom, name, options);
        dom.closeElement("grammar");

        dom.openElement("initial");
        appendVoiceXMLLiteral(dom, attributes);
        dom.closeElement("initial");


        // Add the fields, one for each option.
        int o = 0;
        handleOptionFields(options, dom, o);

        // When the user input has been processed return a semi-colon separated
        // list of those options which have been selected.
        element = dom.openElement("filled");
        element.setAttribute("mode", "any");

        element = dom.openElement("script");
        dom.appendEncoded("var separator = \"\";var result = \"\";");
        element = dom.closeElement("script");

        element = dom.addElement("return");
        element.setAttribute("namelist", "result");
        dom.closeElement("filled");
        dom.closeElement("form");
    }

    private void handleOptionFields(Collection options, DOMOutputBuffer dom,
                                      int count) {
        for (Iterator i = options.iterator(); i.hasNext(); count += 1) {
            Object unknown = i.next();
            if (unknown instanceof SelectOption) {
                SelectOption option = (SelectOption) unknown;
                Element element = dom.addStyledElement("field", option);
                // @todo better (BW) - why is the option variable not used?
                element.setAttribute("name", "option" + count);
                element.setAttribute("type", "boolean");
            } else {
                handleOptionFields(
                        ((SelectOptionGroup) unknown).getSelectOptionList(),
                        dom,
                        count);
            }
        }
    }

    /**
     * Append any voice xml literal values. This method was added a a result of
     * refactoring.
     *
     * @param dom        the DOMOutputBuffer
     * @param attributes the form field attributes to be used.
     */
    private void appendVoiceXMLLiteral(DOMOutputBuffer dom,
                                       XFFormFieldAttributes attributes) {
        String value;
        // Add the prompt which should already be properly quoted as it contains
        // VoiceXML markup.
        if ((value = getTextFromReference(attributes.getPrompt(),
                                       TextEncoding.VOICE_XML_PROMPT)) != null) {
            dom.appendLiteral(value);
        }

        // Add the help which should already be properly quoted as it contains
        // VoiceXML markup.
        if ((value = getTextFromReference(attributes.getHelp(),
                                       TextEncoding.VOICE_XML_HELP)) != null) {
            dom.appendLiteral(value);
        }

        // Add the error message which should already be properly quoted as it
        // contains VoiceXML markup.
        if ((value = getTextFromReference(attributes.getErrmsg(),
                                       TextEncoding.VOICE_XML_ERROR)) != null) {
            dom.appendLiteral(value);
        }
    }

    /**
     * Add an implicit value to the form. Only one action is currently
     * supported and it automatically triggers when the form has been filled
     * in. The names of all the fields apart from other action fields are added
     * to the list of variable names whose values should be submitted.
     *
     * @param attributes The attributes to use when generating the mark up.
     */
    public void doActionInput(DOMOutputBuffer dom,
                              XFActionAttributes attributes) {

       XFFormAttributes formAttributes = attributes.getFormAttributes();

        // if this action is being rendered in a form, then only the first
        // action should do anything in VoiceXML, the rest are ignored.
        if (formAttributes != null) {
            if (formAttributes.getActionCount() != 0) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Ignoring action");
                }
                return;
            }

            formAttributes.setActionCount(1);
        }

        String value;
        Element element;

        // Generate the markup.
        // @todo 2005060816 annotate child with style information if it's not inherited from the parent
        element = dom.openStyledElement("filled", attributes);

        // set any attribute values that should be applied for all form fields.
        addFormFieldAttributes(element, attributes);

        // If the field name is set for this action then we need to initialise
        // a variable with the same name so the value will be submitted.
        String fieldName = attributes.getName();
        if (fieldName != null) {
            value = attributes.getValue();
            if (value == null) {
                value = " ";
            }
            element = dom.addElement("var");
            element.setAttribute("name", fieldName);
            element.setAttribute("expr", value);
        }

        element = dom.addElement("submit");

        // Check if processing an emulated XForm or real XFForm markup because
        // we cannot populate the submit element's next and method attributes
        // until later (after XFormEmulationTransformer) if it's emulated.
        if (formAttributes != null) {

            // Set the next and method attributes to those associated
            // with the form
            String resolvedHref = resolveFormAction(formAttributes);
            element.setAttribute("next", resolvedHref);
            element.setAttribute("method", formAttributes.getMethod());

            // Set the namelist attribute to the fields defined in the
            // form attributes.
            StringBuffer buffer = new StringBuffer(URLConstants.FORM_PARAMETER);
            for (Iterator i = formAttributes.getFields().iterator(); i.hasNext();) {

                XFFormFieldAttributes field = (XFFormFieldAttributes) i.next();

                // Ignore other action fields when generating the field names.
                if (field != attributes && field instanceof XFActionAttributes) {
                    continue;
                }

                // Ignore any non implicit fields which do not have an entry pane
                // set.
                if (!(field instanceof XFImplicitAttributes)
                        && field.getEntryContainerInstance() == null) {
                    continue;
                }

                // Action fields don't have to have a name so make sure that the name
                // is set before adding to the name list.
                fieldName = field.getName();
                if (fieldName != null) {
                    buffer.append(" ").append(fieldName);
                }
            }
            element.setAttribute("namelist", buffer.toString());
        }
        dom.closeElement("filled");
    }

    // Javadoc inherited.
    public void populateEmulatedActionElement(Element element,
            EmulatedXFormDescriptor fd) {

        // check if the control element is an action element
        if ("submit".equals(element.getName())) {
            // Set the next and method attributes to those associated with the form
            String resolvedHref = resolveFormAction(fd.getFormAttributes());
            element.setAttribute("next", resolvedHref);
            element.setAttribute("method", fd.getFormMethod());

            // Set the namelist attribute to the fields defined in the
            // form descriptor.
            StringBuffer buffer = new StringBuffer(URLConstants.FORM_PARAMETER);
            for (Iterator i = fd.getFields().iterator(); i.hasNext();) {

                FieldDescriptor field = (FieldDescriptor)i.next();

                // Ignore other action fields when generating the field names.
                if (field.getType() instanceof ActionFieldType &&
                        element.getName().equalsIgnoreCase(field.getName())) {
                    continue;
                }

                // Make sure that the name is set before adding it.
                if (field.getName() != null) {
                    buffer.append(" ").append(field.getName());
                } 
            }
            element.setAttribute("namelist", buffer.toString());
        }
    }

    // Javadoc inherited.
    public void doImplicitValue(DOMOutputBuffer dom,
            XFImplicitAttributes attributes) {
        // In VoiceXMLRoot we add an implicit value to the form by setting a
        // form variable with the specified name to the specified value in the
        // form preamble buffer. The name is automatically added to the list of
        // variables whose values should be sent by doActionInput.
        Element element = dom.addStyledElement(IMPLICIT_ELEMENT, attributes);
        addFormFieldAttributes(element, attributes);
        element.setAttribute("name", attributes.getName());
        element.setAttribute("expr", "'" + attributes.getValue() + "'");
    }

    protected void doDivideHint(DOMOutputBuffer dom,
                                DivideHintAttributes attributes) {

    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 10-Oct-05	9637/10	emma	VBM:2005092807 Adding tests for XForms emulation

 03-Oct-05	9673/2	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 02-Oct-05	9637/8	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9637/5	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 15-Sep-05	9524/1	emma	VBM:2005091503 Added ContainerInstance to allow regions and panes to be treated in the same way

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 19-Aug-05	9289/4	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 23-Jun-05	8833/1	pduffin	VBM:2005042901 Addressing review comments

 09-Jun-05	8665/4	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 09-Jun-05	8665/2	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 22-Nov-04	5733/5	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/3	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 02-Jul-04	4713/6	geoff	VBM:2004061004 Support iterated Regions (review comments)

 29-Jun-04	4713/3	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 01-Jul-04	4778/2	allan	VBM:2004062912 Use the Volantis.pageURLRewriter to rewrite page urls

 30-Jun-04	4781/3	adrianj	VBM:2002111405 Created SMS test case and added check for null/empty mime types in protocols

 30-Jun-04	4781/1	adrianj	VBM:2002111405 VolantisProtocol.defaultMimeType() and DOMProtocol made abstract

 16-Jun-04	4704/3	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name. (use generic name for current format index)

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 06-May-04	4153/1	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 05-Mar-04	3339/1	geoff	VBM:2003052104 Invalid usage of DOMOutputBuffer.appendLiteral()

 05-Mar-04	3337/2	geoff	VBM:2003052104 Invalid usage of DOMOutputBuffer.appendLiteral()

 05-Mar-04	3323/1	geoff	VBM:2003052104 Invalid usage of DOMOutputBuffer.appendLiteral()

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 17-Aug-03	1052/2	allan	VBM:2003073101 Support styles on menu and menuitems

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 23-Jul-03	807/1	geoff	VBM:2003071405 works and tested but no design review yet

 ===========================================================================
*/

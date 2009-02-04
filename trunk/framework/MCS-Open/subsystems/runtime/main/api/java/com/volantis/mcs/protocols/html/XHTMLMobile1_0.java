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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.DocType;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.MarkupFamily;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.protocols.AnchorAttributes;
import com.volantis.mcs.protocols.BigAttributes;
import com.volantis.mcs.protocols.BoldAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMTransformer;
import com.volantis.mcs.protocols.HorizontalRuleAttributes;
import com.volantis.mcs.protocols.ItalicAttributes;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.SmallAttributes;
import com.volantis.mcs.protocols.SpatialFormatIteratorAttributes;
import com.volantis.mcs.protocols.StyleAttributes;
import com.volantis.mcs.protocols.XFActionAttributes;
import com.volantis.mcs.protocols.XFBooleanAttributes;
import com.volantis.mcs.protocols.XFFormAttributes;
import com.volantis.mcs.protocols.XFSelectAttributes;
import com.volantis.mcs.protocols.XFTextInputAttributes;
import com.volantis.mcs.protocols.forms.validation.TextInputFormat;
import com.volantis.mcs.protocols.trans.CompoundTransformer;
import com.volantis.mcs.protocols.trans.WhiteSpaceFixTransformer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of the XHTML Mobile Profile protocol.
 */
public class XHTMLMobile1_0 extends XHTMLBasic {

    /**
     * Set of inline stylistic elements that certain devices have issues
     * honoring whitespace with
     */
    private static final Set inlineStyleWhitespaceElements = new HashSet();

    /**
     * Set of inline link based elements that certain devices have issues
     * honoring whitespace with
     */
    private static final Set inlineLinkWhitespaceElements = new HashSet();

    static {
        // add the various elements that some devices have trouble honouring
        // whitespace with the the sets.
        inlineStyleWhitespaceElements.add("b");
        inlineStyleWhitespaceElements.add("big");
        inlineStyleWhitespaceElements.add("em");
        inlineStyleWhitespaceElements.add("i");
        inlineStyleWhitespaceElements.add("small");
        inlineStyleWhitespaceElements.add("strong");
        inlineStyleWhitespaceElements.add("sub");
        inlineStyleWhitespaceElements.add("sup");

        inlineLinkWhitespaceElements.add("a");
    }

    /**
     * Initializes the new instance with the given parameters.
     */
    public XHTMLMobile1_0(ProtocolSupportFactory supportFactory,
                          ProtocolConfiguration configuration) {

        super(supportFactory, configuration);

        // Even though this protocol is similar to XHTML Basic, it does have
        // some XHTML strict extensions. Of these extensions we make use of
        // the ability to have style elements for inline (internal) stylesheets
        supportsInlineStyles = true;

        // The following defines how this protocol expects to render
        // the various types of stylesheet by default (can be overridden by
        // device settings)
        protocolThemeStylesheetPreference = StylesheetRenderMode.INTERNAL;

        // hr is supported
    }

    // javadoc inherited
    protected void doProtocolString(Document document) {
        DocType docType = domFactory.createDocType(
                "html", "-//WAPFORUM//DTD XHTML Mobile 1.0//EN",
                "http://www.wapforum.org/DTD/xhtml-mobile10.dtd", null,
                MarkupFamily.XML);
        document.setDocType(docType);

        addXMLDeclaration(document);
    }

    // javadoc inherited
    protected void openStyle(DOMOutputBuffer dom,
                             StyleAttributes attributes) {

        performDefaultOpenStyle(dom, attributes, false);
    }

    // javadoc inherited
    protected void closeStyle(DOMOutputBuffer dom,
                              StyleAttributes attributes) {

        performDefaultCloseStyle(dom, false);
    }

    // Javadoc inherited from super class.
    public String defaultMimeType() {
        return "application/vnd.wap.xhtml+xml";
    }
    
    // ========================================================================
    //   Navigation methods.
    // ========================================================================

    protected void addAnchorAttributes (Element element,
            AnchorAttributes attributes) throws ProtocolException {

        // call super method first
        super.addAnchorAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);

        // Add the focus event attributes.
        addFocusEventAttributes (element, attributes);
    }

    protected void writeJavaScriptValidation(XFTextInputAttributes attribute,
                                             StringBuffer sb) {
        
        TextInputFormat pattern = getTextInputFormat(attribute);
        
        if (pattern != null) {
            
            String caption = getTextFromReference (attribute.getCaption (),
                                                TextEncoding.PLAIN);
            String errorMsg = getTextFromReference (attribute.getErrmsg (),
                                                 TextEncoding.PLAIN);
            if(errorMsg == null) {
                // if no error message has been provided then we
                // need to generate a sensible message
                errorMsg = "Invalid input for " +
                    ((caption != null)?caption:" a form field");
            }

            // We generate the JavaScript to perform the validation
            // If the attribute.emptryOK is true then we must test if
            // the form field is not empty and if it fails the regex
            // and only then generate the error message
            sb.append("if(!(new RegEx(\"" + pattern + "\",")
                .append("form." + attribute.getName() + ".value).match())");
            
            if(pattern.isEmptyOk()) {
                sb.append(" && (form." + attribute.getName() +".value!='')");
            }
            sb.append(") {\n errMsg += \"\\n" + errorMsg + "\";")
                .append("}\n");
        }
    }

    protected boolean doValidation(XFFormAttributes attributes) {
        // if device does not support JavaScript return
        if(!supportsJavaScript()) {
            return false;
        }
        StringBuffer sb = new StringBuffer();
        Object obj  = null;
        XFTextInputAttributes attribute = null;
        Iterator i = attributes.getFields().iterator();
        while(i.hasNext()) {
            obj = i.next();
            if(obj instanceof XFTextInputAttributes) {
                attribute = (XFTextInputAttributes)obj;
                writeJavaScriptValidation(attribute, sb);
            }
        }
        if(sb.length() > 0) {
            DOMOutputBuffer dom = getHeadBuffer ();

            // Calculate the url to the validate script. This could be done
            // once for the whole application.
            String validateURL = "/" + context.getVolantisBean().getPageBase() + 
                    "/" + context.getVolantisBean().getScriptsBase() + "/validate.js";

            Element element = dom.addElement ("script");
            element.setAttribute ("language", "JavaScript");
            element.setAttribute ("src", validateURL);
            dom = getScriptBuffer ();
            
            String formFunction = attributes.getFormData().getName();
            dom.appendLiteral ("function validateForm")
                .appendLiteral (formFunction)
                .appendLiteral ("(form) {")
                .appendLiteral ("var errMsg = \"\";")
                .appendLiteral (sb.toString())
                .appendLiteral ("if(errMsg != \"\")")
                .appendLiteral ("{alert(\"Form Validation Error\\n\\n\" + errMsg);")
                .appendLiteral ("return false;}")
                .appendLiteral ("return true")
                .appendLiteral ("}");

            return true;
        }
        return false;
    }


    //==========================================================================
    // Spatial Format Iterator methods
    //==========================================================================
    protected void openSpatialFormatIterator(
            DOMOutputBuffer dom,
            SpatialFormatIteratorAttributes attributes) {
        openSpatialFormatIteratorOriginal(dom, attributes);
    }

    protected void addXFFormAttributes (Element element,
            XFFormAttributes attributes) throws ProtocolException {

        // Add the super classes attributes first.
        super.addXFFormAttributes (element, attributes);

        if (doValidation(attributes)) {
            String formName = attributes.getFormData().getName();
            element.setAttribute ("onSubmit", "return validateForm"
                                    +formName+"(this);");
        }

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);

        // Add the form event attributes.
        addFormEventAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected void addActionAttributes (Element element,
            XFActionAttributes attributes) throws ProtocolException {

        super.addActionAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);

        // Add the form field attributes.
        addFieldEventAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected void addBooleanAttributes (Element element,
            XFBooleanAttributes attributes) throws ProtocolException {

        super.addBooleanAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);

        // Add the form field attributes.
        addFieldEventAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected void addSelectMenuAttributes (Element element,
            XFSelectAttributes attributes) throws ProtocolException {

        super.addSelectMenuAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);

        // Add the form field attributes.
        addFieldEventAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected void addTextAreaAttributes (Element element,
            XFTextInputAttributes attributes) throws ProtocolException {

        super.addTextAreaAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);

        // Add the form field attributes.
        addFieldEventAttributes (element, attributes);
    }

    /**
     * Add extra attributes to the text input tag.
     * @param element the Element to modify.
     * @param attributes <code>XFTextInputAttributes</code> for the text
     * input form field.
     */
    protected void addTextInputAttributes (Element element,
            XFTextInputAttributes attributes) throws ProtocolException {

        // Add the super classes attributes first.
        super.addTextInputAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);

        // Add the form field attributes.
        addFieldEventAttributes (element, attributes);
    }

    // ========================================================================
    //   Presentational element methods.
    // ========================================================================
    /**
     * Allow subclasses to add extra attributes.
     * @param element The Element to modify.
     * @param attributes The attributes to use when generating the markup.
     */
    private void addBigAttributes (Element element,
                                     BigAttributes attributes) {
    }
    /**
     * Allow subclasses to add extra attributes.
     * @param element The Element to modify.
     * @param attributes The attributes to use when generating the markup.
     */
    private void addBoldAttributes (Element element,
                                      BoldAttributes attributes) {
    }
    /**
     * Allow subclasses to add extra attributes.
     * @param element The Element to modify.
     * @param attributes The attributes to use when generating the markup.
     */
    protected
        void addHorizontalRuleAttributes (Element element,
                                          HorizontalRuleAttributes attributes) {
    }
    /**
     * Allow subclasses to add extra attributes.
     * @param element The Element to modify.
     * @param attributes The attributes to use when generating the markup.
     */
    private void addItalicAttributes (Element element,
                                        ItalicAttributes attributes) {
    }
    /**
     * Allow subclasses to add extra attributes.
     * @param element The Element to modify.
     * @param attributes The attributes to use when generating the markup.
     */
    private void addSmallAttributes (Element element,
                                        SmallAttributes attributes) {
    }

    // Javadoc inherited from super class.
    protected void closeBig(DOMOutputBuffer dom, BigAttributes attributes) {
        dom.closeElement("big");
    }

    // Javadoc inherited from super class.
    protected void closeBold(DOMOutputBuffer dom, BoldAttributes attributes) {
        dom.closeElement("b");
    }

    // Javadoc inherited from super class.
    protected void closeItalic(
        DOMOutputBuffer dom,
        ItalicAttributes attributes) {
        
        dom.closeElement("i");
    }

    // Javadoc inherited from super class.
    protected void closeSmall(
        DOMOutputBuffer dom,
        SmallAttributes attributes) {
            
        dom.closeElement("small");
    }

    // Javadoc inherited from super class.
    protected void openBig(DOMOutputBuffer dom, BigAttributes attributes)
        throws ProtocolException {
        Element element = dom.openStyledElement("big", attributes);
        addCoreAttributes(element, attributes);
        addBigAttributes(element, attributes);
    }

    // Javadoc inherited from super class.
    protected void openBold(DOMOutputBuffer dom, BoldAttributes attributes)
        throws ProtocolException {
        
        Element element = dom.openStyledElement("b", attributes);
        addCoreAttributes(element, attributes);
        addBoldAttributes(element, attributes);

    }

    // Javadoc inherited from super class.
    protected void openItalic(DOMOutputBuffer dom, ItalicAttributes attributes)
        throws ProtocolException {
            
        Element element = dom.openStyledElement("i", attributes);
        addCoreAttributes(element, attributes);
        addItalicAttributes(element, attributes);

    }

    // Javadoc inherited from super class.
    protected void openSmall(DOMOutputBuffer dom, SmallAttributes attributes)
        throws ProtocolException {
            
        Element element = dom.openStyledElement("small", attributes);
        addCoreAttributes(element, attributes);
        addSmallAttributes(element, attributes);
    }

    // javadoc inhertied
    protected DOMTransformer getDOMTransformer() {
        DOMTransformer transformer =
                new XHTMLMobile1_0_UnabridgedTransformer(protocolConfiguration);

        if (!deviceHonoursSpacingForInlineStylingOpenElements()) {
            transformer = new CompoundTransformer(
                    transformer,
                    new WhiteSpaceFixTransformer(inlineStyleWhitespaceElements,
                                                 inlineLinkWhitespaceElements));
        }
        return transformer;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Dec-05	10803/5	pabbott	VBM:2005120901 Fix white space problem on LG-C1100

 14-Dec-05	10803/2	pabbott	VBM:2005120901 Fix white space problem on LG-C1100

 14-Dec-05	10799/1	geoff	VBM:2005081506 Port 2005071314 forward to MCS

 08-Dec-05	10701/1	pduffin	VBM:2005110905 Porting forward changes from 3.5

 08-Dec-05	10675/2	pduffin	VBM:2005110905 Ported forward some white space fixes from 3.2.3

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9324/3	ianw	VBM:2005080202 Move validation for WapCSS into styling

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 29-Jun-05	8552/9	pabbott	VBM:2005051902 JIBX Theme accessors

 29-Jun-05	8552/7	pabbott	VBM:2005051902 JIBX Theme accessors

 23-Jun-05	8833/3	pduffin	VBM:2005042901 Addressing review comments

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 17-Jun-05	8830/1	pduffin	VBM:2005042901 Merging from 3.2.3 - Added support for specifying symbols, punctuation marks, or numbers in a text format

 13-Jun-05	8552/3	pabbott	VBM:2005051902 An Eclipse editor fix

 09-Jun-05	8665/2	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 11-May-05	8123/2	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 03-Feb-05	6855/1	tom	VBM:2005020212 Changed default theme style sheet to internal

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 26-Apr-04	4029/1	steve	VBM:2004042003 support hr element in netfront3 and MIB

 23-Apr-04	4020/1	steve	VBM:2004042003 support hr element in netfront3 and MIB

 12-Feb-04	2958/1	philws	VBM:2004012715 Add protocol.content.type device policy

 05-Dec-03	2075/3	mat	VBM:2003120106 Correct javadoc and tidy imports

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 12-Nov-03	1861/1	mat	VBM:2003110602 Added presentation markup to XHTMLMobile1_0 and corrected mime types

 15-Sep-03	1321/5	adrian	VBM:2003082111 Fixed bug in wap-input-required styleproperty generation

 10-Sep-03	1321/3	adrian	VBM:2003082111 output validation css in same rule as theme properties

 05-Sep-03	1321/1	adrian	VBM:2003082111 added wcss input validation for xhtmlmobile

 30-Jun-03	569/1	philws	VBM:2003062604 Add XHTML Mobile Profile protocol

 ===========================================================================
*/

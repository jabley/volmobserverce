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
package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom2wbsax.WBSAXAttributeValueSerialiser;
import com.volantis.mcs.dom2wbsax.WBSAXContentValueSerialiser;
import com.volantis.mcs.dom2wbsax.WBSAXDefaultValueProcessor;
import com.volantis.mcs.dom2wbsax.WBSAXIgnoreElementProcessor;
import com.volantis.mcs.dom2wbsax.WBSAXProcessorContext;
import com.volantis.mcs.dom2wbsax.WBSAXStringSerialiser;
import com.volantis.mcs.dom2wbsax.WBSAXValueProcessor;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * An element processor which registers a "accesskey" attribute value
 * processor and a content value processor to create accesskey values
 * inside the accesskey annotation element.
 * <p/>
 * It deals with content in the following format:
 * <pre>
 *   &lt;accesskey_annotation&gt;
 *     &lt;elt accesskey="x"&gt;x ...&lt;elt&gt;
 *   &lt;accesskey_annotation&gt;
 * </pre>
 * and translates the x's into accesskey opaque values, which are then
 * found by the {@link AccesskeyWBSAXFilter} and "customised" to have the
 * correct value at that point.
 * <p/>
 * Note that the accesskey annotation element itself is not transferred
 * from the MCSDOM to the WBDOM, as the WBDOM has a more detailed structure
 * which means that it is easier to customise at the attribute value level.
 */
public final class WBSAXAccesskeyAnnotationElementProcessor
        extends WBSAXIgnoreElementProcessor {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(
                    WBSAXAccesskeyAnnotationElementProcessor.class);


    /**
     * A constant to make it easier to refer to the dummy accesskey value.
     */
    private static final char DUMMY_ACCESSKEY_VALUE =
            AccesskeyConstants.DUMMY_ACCESSKEY_VALUE_CHAR;

    /**
     * The attribute value processor to use for accesskey attribute values.
     */
    private final WBSAXValueProcessor accesskeyAttributeValueProcessor;

    /**
     * Flag that indicates whether elements that have values auto assigned for
     * accesskey attributes should have there content prefixed with the
     * accesskey value.
     */
    private final boolean prefixAccesskeyContent;

    /**
     * Construct an instance of this class, using the shared context provided.
     *
     * @param context                the shared context used by this processor.
     * @param prefixAccesskeyContent true if we should prefix the content of
     *                               elements which contain an auto assigned accesskey attribute with
     *                               the accesskey value.
     */
    public WBSAXAccesskeyAnnotationElementProcessor(
            WBSAXProcessorContext context, boolean prefixAccesskeyContent) {

        super(context);
        
        // Set up a special attribute value processor and serialiser for 
        // accesskey attributes. Note that the matching content value 
        // processor and serialiser must be set up for each element since it 
        // contains state. 
        accesskeyAttributeValueProcessor = new WBSAXDefaultValueProcessor(new WBSAXStringSerialiser(
                context.getEncoding(),
                new AccesskeyWBSAXAttributeValueSerialiser(context)));

        this.prefixAccesskeyContent = prefixAccesskeyContent;

    }

    // Inherit Javadoc.
    public void elementStart(Element element, boolean content)
            throws WBSAXException {

        // Ignore the accesskey annotation element start itself.

        // But register our value processors.
        // Attributes are processed with a shared processor. 
        context.pushAttributeValueProcessor("accesskey",
                                            accesskeyAttributeValueProcessor);

        if (prefixAccesskeyContent) {
            // Content is processed with a new processor and serialiser each
            // time as they maintain state which is important.
            WBSAXValueProcessor contentValueProcessor =
                    new WBSAXDefaultValueProcessor(new WBSAXStringSerialiser(
                            context.getEncoding(),
                            new AccesskeyWBSAXContentValueSerialiser(context)));
            context.pushContentValueProcessor(contentValueProcessor);
        }

    }

    // Inherit Javadoc.
    public void elementEnd(Element element, boolean content)
            throws WBSAXException {

        // Ignore the accesskey annotation element end itself.
            
        // But de-register our value processors.
        if (prefixAccesskeyContent) {
            context.popContentValueProcessor();
        }

        context.popAttributeValueProcessor("accesskey");

    }

    /**
     * An attribute value serialiser which translate inline string data
     * into accesskey opaque values.
     * <p/>
     * Note that this assumes that the content of the accesskey annotation
     * element as is as described in the parent class comment.
     */
    private class AccesskeyWBSAXAttributeValueSerialiser
            extends WBSAXAttributeValueSerialiser {

        public AccesskeyWBSAXAttributeValueSerialiser(
                WBSAXProcessorContext context) {

            super(context);

        }

        // Inherit Javadoc.
        public void addString(char[] chars, int offset, int length)
                throws WBSAXException {

            // If the attribute value matched what we expected ("x") ...
            if (length == 1 && chars[offset] == DUMMY_ACCESSKEY_VALUE) {
                // Create an empty accesskey opaque value to represent the
                // logical attribute value ("x"). We will add the actual,
                // calculated value of the accesskey into it later.
                if (logger.isDebugEnabled()) {
                    logger.debug("Adding attribute string: found " +
                                 DUMMY_ACCESSKEY_VALUE);
                }
                AccesskeyOpaqueValue value = new AccesskeyOpaqueValue();
                context.getContentHandler().addAttributeValueOpaque(value);
            } else {
                // The accessskey annotation element contained an accesskey
                // attribute with a value other than "x". This should never
                // happen and we can't handle it so throw an exception.
                throw new IllegalStateException(AccesskeyConstants.ACCESSKEY_ANNOTATION_ELEMENT +
                                                " element attribute value invalid: chars=" + chars +
                                                " offset=" + offset + " length=" + length);
            }

        }

    }

    /**
     * A content value serialiser which translate inline string data
     * into accesskey opaque values.
     * <p/>
     * Note that this assumes that this assumes that the content of the
     * accesskey annotation element as is as described in the parent class
     * comment.
     */
    private class AccesskeyWBSAXContentValueSerialiser
            extends WBSAXContentValueSerialiser {

        boolean used;

        public AccesskeyWBSAXContentValueSerialiser(
                WBSAXProcessorContext context) {

            super(context);

        }

        // Inherit Javadoc.
        public void addString(char[] chars, int offset, int length)
                throws WBSAXException {

            // We only add an opaque value before the very first content value
            // of the very first content. We also require that the logical
            // content prefix is passed in a single call ("x " rather than "x"
            // and " ").
            if (!used) {

                // HACK: work around VBM:2005030109.
                // TODO: Remove this code block once that VBM has been fixed.
                // If we have found just "x" is it probably because the
                // trailing space has been thrown away by DOMOutputBuffer bug.
                // So, lets pretend that it did it correctly by replacing it
                // with 'x '. Hopefully this will not break anything else...
                if (offset == 0 && length == 1 &&
                        chars[0] == DUMMY_ACCESSKEY_VALUE) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Extending '" + DUMMY_ACCESSKEY_VALUE +
                                     "' to '" + DUMMY_ACCESSKEY_VALUE + " '");
                    }
                    length = 2;
                    chars = new char[]{DUMMY_ACCESSKEY_VALUE, ' '};
                }

                // If the content value started with what we expected ("x ") ...
                if (length >= 2 &&
                        chars[offset] == DUMMY_ACCESSKEY_VALUE &&
                        chars[offset + 1] == ' ') {
                    // Create an empty accesskey opaque value to represent the
                    // logical content prefix value ("x "). We will add the
                    // actual, calculated value of the accesskey into it later.
                    // Note that we strip "x " rather than "x" because
                    // sometimes the accesskey may not have a value at all in
                    // which case we need to avoid rendering the separating
                    // space as well.
                    if (logger.isDebugEnabled()) {
                        logger.debug("Adding content string: found prefix");
                    }
                    AccesskeyOpaqueValue value = new AccesskeyOpaqueValue();
                    context.getContentHandler().addContentValueOpaque(value);
                    // If there was an additional content after the prefix, add
                    // that as a normal string.
                    if (length > 2) {
                        context.getContentHandler().addContentValue(context.getStrings().create(
                                chars, offset + 2,
                                length - 2));
                    }
                    // Note that we already processed the prefix.
                    used = true;
                } else {
                    // The accessskey annotation element contained content
                    // starting with a value other than "x ". This should never
                    // happen and we can't handle it so throw an exception.
                    throw new IllegalStateException(AccesskeyConstants.ACCESSKEY_ANNOTATION_ELEMENT +
                                                    " element content value invalid: chars=" + chars +
                                                    " offset=" + offset + " length=" + length);
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Adding content string: " +
                                 "found remaining content");
                }
                // This is remaining content, just do this normally.
                super.addString(chars, offset, length);
            }
        }

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 02-Mar-05	7243/9	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 02-Mar-05	7120/3	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 02-Mar-05	7120/1	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Oct-03	1469/4	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 ===========================================================================
*/

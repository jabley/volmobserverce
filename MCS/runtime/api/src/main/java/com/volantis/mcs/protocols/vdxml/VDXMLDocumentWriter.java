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
package com.volantis.mcs.protocols.vdxml;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.output.CharacterEncoder;
import com.volantis.mcs.dom.output.DelegatingDocumentWriter;
import com.volantis.mcs.dom.output.DocumentWriter;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.vdxml.style.VDXMLStyleFactory;
import com.volantis.mcs.protocols.vdxml.style.VDXMLStyleProperties;
import com.volantis.mcs.protocols.vdxml.style.VDXMLStyleProperty;
import com.volantis.mcs.protocols.vdxml.style.VDXMLStyleRange;
import com.volantis.mcs.protocols.vdxml.style.VDXMLStyleValueVisitor;
import com.volantis.mcs.protocols.vdxml.style.properties.VDXMLReverseVideoStyleProperty;
import com.volantis.mcs.protocols.vdxml.style.values.VDXMLBinaryValue;
import com.volantis.mcs.protocols.vdxml.style.values.VDXMLColorValue;
import com.volantis.mcs.protocols.vdxml.style.values.VDXMLFontSizeValue;
import com.volantis.styling.Styles;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.io.Writer;
import java.util.Stack;

/**
 * A delegating document writer for VDXML documents.
 * <p>
 * The primary purpose of this class is to handle the rendering of VDXML style
 * information for inline and block styles. It delegates to a contained writer
 * to write out most other sorts of elements.
 * <p>
 * When this class encounters a pseudo block or inline element, it writes out
 * any VDXML markup required to start the associated VDXMLStyleProperties 
 * before the content of the element, and writes out any VDXML markup required 
 * to end the style after the content of the element. Note that the actual 
 * inline and block elements are not rendered, but a &lt;BR /&gt; is rendered 
 * after each block element content (apart from the last one).
 * <p>
 * Exactly how this is done varies depending on whether the style involved is 
 * a binary (DE/FI) style or a "normal" (keyword) type style. Binary styles 
 * can be ended simply by quoting the end of range value whilst normal styles 
 * must be ended by re-quoting the last known (parent) keyword value.
 * <p>
 * When this class encounters an element with an associated StyleProperties 
 * object, it treats this as the start of a new "display context" (See 
 * "The VDXML Language" Section 7.2). This is normally set up by the protocol
 * when starting a new TEXTE for example. When this occurs it resets the 
 * stylistic state back to the default values as per the reference above.
 */ 
public final class VDXMLDocumentWriter
        extends DelegatingDocumentWriter {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(VDXMLDocumentWriter.class);


    /**
     * A factory for creating VDXML style properties from standard ones.
     */ 
    private static final VDXMLStyleFactory STYLE_FACTORY = new VDXMLStyleFactory();

    /**
     * A visitor to render VDXML style values.
     */ 
    private static final StyleValueRenderer STYLE_VALUE_RENDERER =
            new StyleValueRenderer();
    
//    /**
//     * The document writer we delegate to to do the normal element writing.
//     * <p>
//     * This is required because the VDXML document writer only really writes
//     * elements for VDXML style values and BRs ;-). Probably even these should
//     * be routed through this writer but currently they are just hardcoded.
//     */
//    private final DocumentWriter delegate;

    /**
     * The stack used to keep the canonical VDXML style properties associated 
     * with each styled element.
     * <p>
     * These are just the styles associated with that single element, not
     * including any parent style values.
     * <p>
     * By canonical we mean that any values which duplicate existing parent 
     * values have been removed.     
     */ 
    private final Stack canonicalStyles = new Stack();

    /**
     * The stack used to keep the summary VDXML style properties associated
     * with each styled element.
     * <p>
     * These are the combination of the styles associated with that single 
     * element, and all it's parent element's style properties. 
     * <p>
     * Note that each display context has a separate summary scope, as each 
     * one begins anew with default values.
     */ 
    private final Stack summaryStyles = new Stack();

    /**
     * A flag to indicate when we have queued a &ltBR /&gt; tag that must be
     * written before any new content. This is used for paragraph separation
     * for block elements. 
     */ 
    private boolean queuedBreak;

    /**
     * Initialise.
     *
     * @param delegate the document writer we delegate to to write normal
     */
    public VDXMLDocumentWriter(DocumentWriter delegate) {
        super(delegate);
    }

    /**
     * Flushes any queued break tag.
     * <p>
     * This is called before any output to the stream (text output or style
     * output).
     *
     * @param out the writer to write to.
     * @throws IOException
     */
    private void flushBreak(Writer out) throws IOException {
        if (queuedBreak) {
            out.write("<BR />");
            queuedBreak = false;
        }
    }

    // JavaDoc inherited
    public void outputText(Text text, CharacterEncoder encoder)
            throws IOException {

        // Ignore empty nodes. Really shouldn't need this!
        if (text.getLength() > 0) {
            // Write out any break that we had already queued before this 
            // content.
            flushBreak(writer);
        }
        
        super.outputText(text, encoder);
    }

    // Javadoc inherited.
    public boolean outputOpenTag(
            Element element, CharacterEncoder encoder) throws IOException {

        boolean opened = false;

        if (logger.isDebugEnabled()) {
            logger.debug("Opening tag: " + element.getName());
        }

        final String name = element.getName();
        if (name.equals(VDXMLConstants.PSEUDO_INLINE_ELEMENT) ||
                name.equals(VDXMLConstants.PSEUDO_BLOCK_ELEMENT)) {
            // Start a styled inline or block element.
            openStyled(element, writer);
            // Always call close for styled elements.
            // This seems like overkill but is necessary for block elements
            // so we can add the break and it's easier to be consistent.
            opened = true;
        } else {
            // not inline or block, must be a pane/layout element.

            // Write it out the open tag as normal
            opened = super.outputOpenTag(element, encoder);
            // If we opened a tag, then we may need a new display context.
            if (opened) {
                // If this element has a style object attached then
                // this must be the start of a new Display Context.
                Styles styles = element.getStyles();
                // todo: get upper levels to render to VDXML version?
                openDisplayContext(STYLE_FACTORY.getVDXMLStyle(styles));
            }
        }
        return opened;
    }

    // JavaDoc inherited
    public void outputCloseTag(Element element)
            throws IOException {

        if (logger.isDebugEnabled()) {
            logger.debug("Closing tag: " + element.getName());
        }

        final String name = element.getName();
        if (name.equals(VDXMLConstants.PSEUDO_INLINE_ELEMENT)) {
            // Close a styled inline element.
            closeStyled(element, writer);
        } else if (name.equals(VDXMLConstants.PSEUDO_BLOCK_ELEMENT)) {
            // Close a styled block element.
            closeStyled(element, writer);
            // And queue a break to come after any more content
            queuedBreak = true;
        } else {
            // not inline or block, must be a pane/layout element.

            // If this element has a style object attached then
            // this must be the end of a Display Context.
            Styles styles = element.getStyles();
            closeDisplayContext(STYLE_FACTORY.getVDXMLStyle(styles));

            // Write out the close tag as normal.
            super.outputCloseTag(element);
        }
    }

    /**
     * Called before writing out the content of an element with an associated 
     * StyleProperties object. The protocol is responsible for associating 
     * such an object with each element which represents a new display context, 
     * eg TEXTE.
     * <p>
     * This adds a new VDXML style properties onto the context stacks which
     * is based on the style supplied, but with default values filled in
     * for all those values not specified. This is because each new display 
     * context starts with default values. 
     * <p>
     * Note that we have enabled basic support for nested display contexts, 
     * which apparently only happens with TEXTEDEF and AIDE. 
     *  
     * @param style the style to initialise the new display context with.
     */
    private void openDisplayContext(VDXMLStyleProperties style) {

        if (logger.isDebugEnabled()) {
            if (canonicalStyles.empty() && summaryStyles.empty()) {
                logger.debug("Opening normal display context: " + style);
            } else {
                logger.debug("Opening nested display context: " + style);
            }
        }
        
        // If the pane/layout's style didn't have any vdxml component, then
        // the vdxml style will be null. In this case, we need to create an
        // empty one to put the defaults in.
        if (style == null) {
            style = new VDXMLStyleProperties();
        }

        // Dump in default values for display contexts, if the user has
        // not specified them.
        style = VDXMLStyleProperties.DEFAULTS.merge(style);

        // We are starting from scratch for a new display context, so push the
        // same initial value onto both the canonical and summary stacks.
        canonicalStyles.push(style);
        summaryStyles.push(style);

    }

    /**
     * Called after writing out the content of an element with an associated
     * StyleProperties object. 
     * <p>
     * This removes the VDXML style properties which were added onto the 
     * context stacks in the matching open call, and discards them.
     * <p>
     * Note that the basic nested display context support does NOT currently
     * include the queued break. This may need to be addressed in future.
     * 
     * @param style used to initialise the new display context with.
     */
    private void closeDisplayContext(VDXMLStyleProperties style) {

        if (logger.isDebugEnabled()) {
            if (canonicalStyles.size() <= 1 && summaryStyles.size() <= 1) {
                logger.debug("Closing normal display context: " + style);
            } else {
                logger.debug("Closing nested display context: " + style);
            }
        }

        // Remove the initial values that we added originally, this will 
        // expose whatever values were added by the any previous nested 
        // context, if any.
        canonicalStyles.pop();
        summaryStyles.pop();
        
        // @todo later This value may possibly need to be stacked in future...
        queuedBreak = false;
    }

    /**
     * Called before writing out the content of a pseudo inline or block 
     * element.
     * <p>
     * This adds the appropriate VDXML styling markup to ensure that the 
     * subsequent element content appears with the correct styling on screen.
     * <p>
     * This involves opening any boolean values by quoting their range starting
     * values, or inverting the values if they are already set, and opening any
     * normal values by quoting the appropriate keyword.
     * 
     * @param element the element who's styled content is to be written.
     * @param out the writer to write the VDXML styling markup to.
     * @throws IOException
     */ 
    private void openStyled(Element element, Writer out) throws IOException {

        // If the block/inline element had associated style information
        Styles styles = element.getStyles();
        VDXMLStyleProperties style = STYLE_FACTORY.getVDXMLStyle(styles);
        if (style != null) {
            // Then we will may need to write out some styling markup.

            // First, calculate if the style has any properties that are not
            // already in effect from the parent.
            VDXMLStyleProperties current = (VDXMLStyleProperties)
                    summaryStyles.peek();
            VDXMLStyleProperties canonical = style.removeDuplicate(current);
            // If there were some non-duplicate values
            if (!canonical.isEmpty()) {
                // Then we definitely need to write out some styling markup.

                // Before we do, see if we can use the synthetic reverse video
                // property instead of the more verbose explicit background
                // and foreground color changes.
                // If the colours we are to use are reversed from the current
                if (current.isReverseVideo(canonical)) {
                    // Then set the synthetic reverse video property to
                    // be the inverse of the parent value.
                    VDXMLBinaryValue currentReverseVideo = (VDXMLBinaryValue)
                            current.getReverseVideo().getValue();
                    canonical.setReverseVideo(
                            new VDXMLReverseVideoStyleProperty(
                                    currentReverseVideo.inverse()));
                }

                // Save away the styles we are using for the end call so child
                // elements can use these values to re-establish non-boolean
                // styles which they changed.
                canonicalStyles.push(canonical);

                // Update the summary state properties so child elements can
                // figure out what styles are in effect at the moment.
                VDXMLStyleProperties summary = current.merge(canonical);
                summaryStyles.push(summary);

                // Write out the styles as the start of a range (for booleans)
                writeStyle(VDXMLStyleRange.START, canonical, out);

                // And log what happened.
                if (logger.isDebugEnabled()) {
                    logger.debug("Output open " + element.getName() +
                            ": actual:" + style + ", canonical:" + canonical +
                            ", summary:" + summary);
                }
            } else {
                // Turns out we had nothing to write after all.

                // Set the style to null to prevent us trying to write out
                // closing styling markup that we never actually opened.
                element.clearStyles();

                // And log what happened.
                if (logger.isDebugEnabled()) {
                    logger.debug("Output open " + element.getName() +
                            ": ignored:" + style);
                }
            }
        }
    }

    /**
     * Called after writing out the content of a pseudo inline or block 
     * element.
     * <p>
     * This adds the appropriate VDXML styling markup to ensure that the 
     * subsequent (parent) element content appears with the correct styling 
     * on screen.
     * <p>
     * This involves closing any boolean properties by quoting their range
     * ending values, and closing any normal values by quoting the values
     * that their parents used.
     * 
     * @param element the element who's styled content has been written.
     * @param out the writer to write the VDXML styling markup to.
     * @throws IOException
     */ 
    private void closeStyled(Element element, Writer out) throws IOException {

        // If this element is styled...
        Styles styles = element.getStyles();
        VDXMLStyleProperties style = STYLE_FACTORY.getVDXMLStyle(styles);
        if (style != null) {
            // Then we need to see if we need some closing VDXML style markup.

            // Extract the style values we created for this element
            VDXMLStyleProperties canonical =
                    (VDXMLStyleProperties) canonicalStyles.pop();
            VDXMLStyleProperties summary =
                    (VDXMLStyleProperties) summaryStyles.pop();
            // And the parent style values as well.
            VDXMLStyleProperties parentCanonical =
                    (VDXMLStyleProperties) canonicalStyles.peek();
            VDXMLStyleProperties parentSummary =
                    (VDXMLStyleProperties) summaryStyles.peek();

            // Log what we are up to.
            if (logger.isDebugEnabled()) {
                logger.debug("Output close " + element.getName() +
                        ": actual:" + style + ", canonical:" + canonical +
                        ", summary:" + summary +
                        ", parent canonical:" + parentCanonical +
                        ", parent summary:" + parentSummary);
            }

            // Calculate the correct properties to wind up the tag style.
            VDXMLStyleProperties ending;
            // For normal properties, this means re-stating the parent summary
            // values if the child changed them.
            VDXMLStyleProperties parentNormal = parentSummary.normalOnly();
            ending = parentNormal.removeMissing(canonical);
            // For boolean properties, this means ending the existing ranges
            // using the child values.
            VDXMLStyleProperties childBoolean = canonical.booleanOnly();
            ending = ending.merge(childBoolean);

            // If we found any properties
            if (!ending.isEmpty()) {
                // render them.
                writeStyle(VDXMLStyleRange.STOP, ending, out);
            }
        }
    }

    /**
     * Write out the VDXML stylistic markup for the VDXML style properties
     * provided, suitable for either the start or end of an element depending
     * on the range value provided.
     * <p>
     * Note that boolean properties can be automatically closed depending on 
     * their range value, however normal closing properties need to be 
     * calculated explicitly from the parent style properties before calling
     * this method. 
     * 
     * @param range enum of either the start of end of a range (for booleans).
     * @param style the style to write.
     * @param out the writer to 
     * @throws IOException
     */ 
    private void writeStyle(VDXMLStyleRange range,
                            VDXMLStyleProperties style,
                            Writer out) throws IOException {

        // Write out any break that we had already queued before this content.
        flushBreak(out);


        VDXMLStyleProperty backgroundColor = style.getBackgroundColor();
        VDXMLStyleProperty underline = style.getUnderline();
        VDXMLStyleProperty fontColor = style.getFontColor();
        VDXMLStyleProperty fontSize = style.getFontSize();
        VDXMLStyleProperty blink = style.getBlink();
        VDXMLStyleProperty reverseVideo = style.getReverseVideo();

        // reverse video trumps explicit colors when set.
        if (reverseVideo != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Writing reverse video for " + fontColor + "," +
                        backgroundColor);
            }
            fontColor = null;
            backgroundColor = null;
        }

        // Area
        if (
                (backgroundColor != null && reverseVideo == null) ||
                underline != null
        ) {
            out.write("<AZ");
            writeProperty(range, backgroundColor, out);
            writeProperty(range, underline, out);
            out.write("/>");
        }

        // Character
        if (
                (fontColor != null || reverseVideo != null) ||
                fontSize != null ||
                blink != null
        ) {
            out.write("<AC");
            writeProperty(range, fontColor, out);
            writeProperty(range, fontSize, out);
            writeProperty(range, blink, out);
            writeProperty(range, reverseVideo, out);
            out.write("/>");
        }
    }

    /**
     * Write out the VDXML stylistic markup for an individual VDXML style
     * property.
     * 
     * @param range enum of either the start of end of a range (for booleans).
     * @param property the property to write.
     * @param out the writer to 
     * @throws IOException
     */ 
    private void writeProperty(VDXMLStyleRange range,
                                VDXMLStyleProperty property,
                                Writer out) throws IOException {

        if (property != null) {
            // Figure out the value.
            STYLE_VALUE_RENDERER.setStyleRange(range);
            property.getValue().accept(STYLE_VALUE_RENDERER);
            String value = STYLE_VALUE_RENDERER.getRenderedValue();
            // Write out the attribute.
            out.write(" " + property.getName() + "=\"" +
                    value + "\"");
        }
    }
    
    /**
     * Visitor to render VDXML style values.
     * <p>
     * This renders start and end attribute values for boolean properties 
     * automagically as they are so simple. Boolean values map trivially onto 
     * the range that they apply for, and the ending value does not need to 
     * re-quote the parent value.
     * <p>
     * Properties with other types of values must have pre-calculated those
     * values for ending or starting the range as appropriate before using this
     * visitor as they are more complicated - their values do not map trivially
     * onto the range that they apply for and rendering the end of a range
     * require access to the parent values in order to requote that value. 
     */ 
    private static class StyleValueRenderer implements VDXMLStyleValueVisitor {

        /**
         * Indicates if we are at the start or end of the range.
         */ 
        private VDXMLStyleRange styleRange;
        
        /**
         * Rendered value once we have visited.
         */ 
        private String renderedValue;

        void setStyleRange(VDXMLStyleRange styleRange) {
            reset();
            this.styleRange = styleRange;
        }

        /**
         * Render the the binary value automagically depending on it's value
         * and whether we are starting or ending it's range.  
         */ 
        public void visit(VDXMLBinaryValue value) {
            VDXMLStyleRange range;
            // If this value turned something on ...
            if (value == VDXMLBinaryValue.TRUE) {
                // ... then the start and end values are the normal range 
                // start and end (DE and FI respectively).
                range = styleRange;
            // Else if this value turned something off ...
            } else if (value == VDXMLBinaryValue.FALSE) {
                // ... then start and end values are the inverse of the normal
                // range start and end values (FI and DE respectively).
                range = styleRange.inverse();
            } else {
                // Should never happen.
                throw new IllegalStateException();
            }
            renderedValue = range.getAttributeValue();
        }

        /**
         * Render a color value that was pre-calculated.
         */ 
        public void visit(VDXMLColorValue value) {
            renderedValue = value.getAttributeValue();
        }

        /**
         * Render a font size value that was pre-calulated.
         */ 
        public void visit(VDXMLFontSizeValue value) {
            renderedValue = value.getAttributeValue();
        }

        String getRenderedValue() {
            return renderedValue;
        }

        void reset() {
            renderedValue = null;
            styleRange = null;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Nov-05	9708/2	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 30-Jun-05	8893/2	emma	VBM:2005062406 Annotate DOM elements generated from VDXML with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 23-Sep-04	5599/1	geoff	VBM:2004092214 Port VDXML to MCS: port existing protocol code

 08-Jun-04	4575/33	geoff	VBM:2004051807 Minitel VDXML protocol support (fix rechercher color bug)

 08-Jun-04	4575/31	geoff	VBM:2004051807 Minitel VDXML protocol support (javadoc and make rendering clearer)

 04-Jun-04	4575/27	geoff	VBM:2004051807 Minitel VDXML protocol support

 04-Jun-04	4495/7	claire	VBM:2004051807 Implemented dissecting panes and ensured elements are created from the DOM pool

 03-Jun-04	4575/25	geoff	VBM:2004051807 Minitel VDXML protocol support (checkin before fragmentation)

 02-Jun-04	4575/22	geoff	VBM:2004051807 Minitel VDXML protocol support (reverse video, tests and some cleanup)

 02-Jun-04	4495/3	claire	VBM:2004051807 Null style fix, tidy up form fragmentation, utilise DOMPool when transforming, and some JavaDoc additions

 01-Jun-04	4575/18	geoff	VBM:2004051807 Minitel VDXML protocol support

 28-May-04	4575/16	geoff	VBM:2004051807 Minitel VDXML protocol support (add block test case)

 28-May-04	4575/14	geoff	VBM:2004051807 Minitel VDXML protocol support (add block test case)

 28-May-04	4575/12	geoff	VBM:2004051807 Minitel VDXML protocol support (block element support)

 28-May-04	4575/10	geoff	VBM:2004051807 Minitel VDXML protocol support (fix underline)

 28-May-04	4495/1	claire	VBM:2004051807 Menu handling and reload links

 28-May-04	4575/5	geoff	VBM:2004051807 Minitel VDXML protocol support (working inline)

 28-May-04	4575/3	geoff	VBM:2004051807 Minitel VDXML protocol support (incomplete inline integration)

 27-May-04	4575/1	geoff	VBM:2004051807 Minitel VDXML protocol support

 ===========================================================================
*/

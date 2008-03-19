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
 * $Header: /src/voyager/com/volantis/mcs/css/renderer/RendererContext.java,v 1.14 2002/12/31 16:58:32 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Apr-02    Allan           VBM:2002042404 - Created. The context in which
 *                              the rendering of style sheet and its component
 *                              parts takes place.
 * 02-May-02    Adrian          VBM:2002040808 - Added protected constructor
 *                              for emulator subclass
 * 20-May-02    Allan           VBM:2002042404 - Removed println from
 *                              flushStyleSheet() to stop output of css to
 *                              stdout.
 * 17-May-02    Doug            VBM:2002051701 - Added a KeywordMapperFactory
 *                              property. Added getter and setter methods for
 *                              the KeywordMapperFactory property.
 * 06-Jun-02    Byron           VBM:2002051303 - Added setInlineCSS() and
 *                              renderSelectorSequence() that store
 *                              the class/id/element for inline selector
 *                              generation and visit the sequence and with a
 *                              renderer respectively.
 * 18-Jun-02    Steve           VBM:2002040807 - Added method to set the mariner
 *                              to protocol element mappings for rendering. Added
 *                              method to set the level of multiple class support
 *                              when transforming the style sheet prior to
 *                              rendering.
 * 18-Jun-02    Steve           VBM:2002040807 - Added methods to determine if
 *                              a root canvas is being rendered, in which case
 *                              Portal mappings will be used in CSS transforms
 *                              or not... in which case portlet mappings will
 *                              be used.
 * 20-Jun-02    Steve           VBM:2002040807 - Added methods to set and get
 *                              the level of multiclass support for CSS
 *                              transformations. The level should be one of
 *                              SELECTOR, ATTRIBUTE or NONE... as read from the
 *                              css.multiclass.support device value.
 * 28-Jun-02    Paul            VBM:2002051302 - Added support for
 *                              ValuesRenderer which have been separated out of
 *                              the ValuesRendererVisitor class.
 * 14-Oct-02    Sumit           VBM:2002070803 Changed the contstructor to take
 *                              a ReusableStringBuffer as a param an if !null
 *                              use it to create the ReusableStringWriter
 * 31-Dec-02    Byron           VBM:2002071015 - Removed LocalRSBPool import.
 *                              Fixed javadoc errors.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.css.renderer;

import com.volantis.mcs.css.version.CSSProperty;
import com.volantis.mcs.css.version.CSSVersion;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.Selector;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.mappers.DefaultKeywordMapper;
import com.volantis.mcs.themes.mappers.KeywordMapper;
import com.volantis.mcs.themes.mappers.KeywordMapperAccessor;
import com.volantis.mcs.themes.mappers.KeywordMapperFactory;
import com.volantis.mcs.themes.properties.VerticalAlignKeywords;
import com.volantis.mcs.utilities.ReusableStringBuffer;
import com.volantis.mcs.utilities.ReusableStringWriter;
import com.volantis.styling.properties.StyleProperty;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * The context in which the rendering of style sheet and its component
 * parts takes place.
 */
public class RendererContext {

    /**
     * The ValuesRenderer for this RendererContext.
     */
    protected ValuesRenderer valuesRenderer;

    /**
     * The ValuesRendererVisitor instance which is shared by all instances of
     * this class.
     */
    private static ValuesRendererVisitor valuesRendererVisitor
            = ValuesRendererVisitor.getSingleton();

    /**
     * The Writer that this RenderContext writes out to.
     */
    private Writer writer;

    /**
     * The writer used to write values into. This is required so that should
     * there be a problem during the rendering process, then it is possible
     * to reset the buffer that the ResuableStringWriter writes too. When
     * the device theme has been all written to the proxy writer, a flush
     * call will write the proxy writer to the real writer.
     */
    private ReusableStringWriter proxyWriter;
    private PrintWriter proxyPrintWriter;

    /**
     * The KeywordMapper in use.
     */
    private KeywordMapper keywordMapper;

    /**
     * The KeywordMapperFactory used to create KeywordMapper instances
     */
    private KeywordMapperFactory keywordMapperFactory;

    /**
     * Store the sequence for inline CSS
     */
    private SelectorSequence sequence = null;
    private KeywordMapperAccessor keywordMapperAccessor;

    /**
     * The version of css that this rendering it targetting.
     * <p>
     * May be null, in which case we are just rendering MCS CSS.
     */
    private CSSVersion cssVersion;

    /**
     * The selector renderer visitor to use for rendering selectors.
     */
    private SelectorRendererVisitor selectorRendererVisitor;
    private KeywordMapper firstKeywordMapper;
    private KeywordMapper secondKeywordMapper;

    /**
     * A map that overrides the default keyword mappers returned by the
     * keyword mapper accessor.
     */
    private Map keywordMapperOverrides;

    /**
     * The separator between items in a list, a ',' by default.
     */
    private String listSeparator = ",";

    /**
     * Construct a RendererContext that will write the renderer style sheet to a
     * given Writer utilizing the given StyleSheetRenderer.
     *
     * @param writer   the Writer
     * @param renderer the StyleSheetRenderer
     * @param buffer   the re-usable StringBuffer
     * @param cssVersion the css version to target, if any (may be null).
     */
    public RendererContext(Writer writer,
                           StyleSheetRenderer renderer,
                           ReusableStringBuffer buffer,
                           CSSVersion cssVersion) {

        this.writer = writer;
        if (buffer != null) {
            this.proxyWriter = new ReusableStringWriter(buffer);
        } else {
            this.proxyWriter = new ReusableStringWriter(
                    new ReusableStringBuffer());
        }
        this.proxyPrintWriter = new PrintWriter(proxyWriter);

        if (renderer != null) {
            this.keywordMapperFactory = renderer.getKeywordMapperFactory();
            this.valuesRenderer = renderer.getValuesRenderer();
            selectorRendererVisitor = renderer.getSelectorRendererVisitor(this);
        }

        this.cssVersion = cssVersion;

        keywordMapperOverrides = new HashMap();

        if (cssVersion != null) {
            createKeywordMapperOverrides();
        }
    }

    private void createKeywordMapperOverrides() {
        CSSProperty property =
                cssVersion.getProperty(StylePropertyDetails.VERTICAL_ALIGN);
        if (property != null) {

            boolean supportsSuper =
                    property.supportsKeyword(VerticalAlignKeywords.SUPER);
            boolean supportsSub =
                    property.supportsKeyword(VerticalAlignKeywords.SUB);
            boolean supportsTop =
                    property.supportsKeyword(VerticalAlignKeywords.TOP);
            boolean supportsBottom =
                    property.supportsKeyword(VerticalAlignKeywords.BOTTOM);

            // If it supports super but not top, or sub but not bottom (or vice
            // versa) then create a keyword mapper to map the unsupported ones
            // to the closest supported one i.e. If one or more of the
            // properties but not all the properties are supported then work
            // out what we need to override.
            if (supportsSuper != supportsTop || supportsSub != supportsBottom) {

                // Create a keyword mapper with the standard mappings.
                DefaultKeywordMapper mapper = new DefaultKeywordMapper(
                        VerticalAlignKeywords.getDefaultInstance());

                if (supportsSuper && !supportsTop) {
                    mapper.addMapping(VerticalAlignKeywords.TOP, "super");
                } else if (supportsTop && !supportsSuper) {
                    mapper.addMapping(VerticalAlignKeywords.SUPER, "top");
                }

                if (supportsSub && !supportsBottom) {
                    mapper.addMapping(VerticalAlignKeywords.BOTTOM, "sub");
                } else if (supportsBottom && !supportsSub) {
                    mapper.addMapping(VerticalAlignKeywords.SUB, "bottom");
                }

                keywordMapperOverrides.put(
                        StylePropertyDetails.VERTICAL_ALIGN.getName(), mapper);
            }
        }
    }

    public RendererContext(Writer writer,
                           StyleSheetRenderer renderer) {
        this(writer, renderer, null, null);
    }

    /**
     * Get the Writer to write to.
     * @return the writer to write rendered styles to.
     */
    public Writer getWriter() {
        return proxyWriter;
    }

    /**
     * Get the PrintWriter version of the Writer to write to.
     * @return the PrintWriter to print rendered styles to.
     */
    public PrintWriter getPrintWriter() {
        return proxyPrintWriter;
    }

    /**
     * Get the number of characters renderer so far.
     * @return the number of characters rendered so far
     */
    public int getRenderLength() {
        return proxyWriter.getBuffer().length();
    }

    /**
     * Rewind the render the writer to a specified index. This method is used to
     * effectively erase characters that have already been rendered.
     * @param index the index to rewind to(i.e. the new render length).
     */
    public void rewindWriter(int index) {
        if (index >= getRenderLength()) {
            return;
        }

        proxyWriter.getBuffer().setLength(index);
    }

    /**
     * Write out the contents of the writer that is used to write values into
     * the RendererContext to the writer that the RendererContext writes out
     * to(i.e. write the proxy writer to the real writer).
     * @throws IOException if a problem is encountered in the write operation
     */
    public void flushStyleSheet() throws IOException {
        ReusableStringBuffer buffer = proxyWriter.getBuffer();
        writer.write(buffer.getChars(), 0, buffer.length());
    }

    /**
     * Set the value of the current keyword mapper property.
     * @param keywordMapper The new value of the keyword mapper
     * property.
     */
    public void setKeywordMapper(KeywordMapper keywordMapper) {
        this.keywordMapper = keywordMapper;

        this.firstKeywordMapper = keywordMapper;
        this.secondKeywordMapper = keywordMapper;
    }

    /**
     * Get the value of the keyword mapper property.
     * @return The value of the keyword mapper property.
     */
    public KeywordMapper getKeywordMapper() {
        return keywordMapper;
    }

    /**
     * Get the keyword mapper for the {@link StyleProperty} parameter.
     *
     * @param property the {@link StyleProperty} instance.
     * @return the keyword mapper for the {@link StyleProperty}
     *         parameter.
     */
    public KeywordMapper getKeywordMapper(StyleProperty property) {
        if (keywordMapperAccessor == null) {
            keywordMapperAccessor = new KeywordMapperAccessor(keywordMapperFactory);
        }
        String name = property.getName();
        KeywordMapper mapper = (KeywordMapper) keywordMapperOverrides.get(name);
        if (mapper == null) {
            mapper = keywordMapperAccessor.getKeywordMapper(name);
        }

        return mapper;
    }

    /**
     * Get the value of keywordMapperFactory.
     * @return value of keywordMapperFactory.
     *
     * @deprecated Use {@link #getKeywordMapper()} instead as this does not
     * allow the keyword mappers to be overridden by the CSSVersion.
     */
    public KeywordMapperFactory getKeywordMapperFactory() {
        return keywordMapperFactory;
    }

    /**
     * Set the value of keywordMapperFactory.
     * @param v  Value to assign to keywordMapperFactory.
     */
    public void setKeywordMapperFactory(KeywordMapperFactory v) {
        this.keywordMapperFactory = v;
    }

    public KeywordMapper getFirstKeywordMapper() {
        return firstKeywordMapper;
    }

    public void setFirstKeywordMapper(KeywordMapper firstKeywordMapper) {
        this.firstKeywordMapper = firstKeywordMapper;
    }

    public KeywordMapper getSecondKeywordMapper() {
        return secondKeywordMapper;
    }

    public void setSecondKeywordMapper(KeywordMapper secondKeywordMapper) {
        this.secondKeywordMapper = secondKeywordMapper;
    }

    /**
     * Render the Selector Sequence (only if there is one)
     * @throws IOException if there is a problem rendering to the writer.
     */
    public boolean renderSelectorSequence()
            throws IOException {

        if (sequence != null) {
            sequence.accept(selectorRendererVisitor);
            return true;
        }
        return false;
    }

    /**
     * Render a Selector.
     *
     * @param  selector    the Selector to render
     * @throws IOException if there is a problem rendering to the writer.
     */
    public void renderSelector(Selector selector)
            throws IOException {

        if (selector == null) {
            return;
        }

        selector.accept(selectorRendererVisitor);
    }

    /**
     * Render a StyleValue.
     * @param value the StyleValue to render
     * @throws IOException if there is a problem rendering to the writer.
     */
    public void renderValue(StyleValue value)
            throws IOException {

        if (value == null) {
            return;
        }

        value.visit(valuesRendererVisitor, this);
    }

    /**
     * Render the priority.
     * @param priority the priority to render
     */
    public void renderPriority(Priority priority) {
        if (priority == Priority.IMPORTANT) {
            proxyWriter.write(" !important");
        }
    }

    /**
     * Set the sequence for the inline CSS
     * @param sequence the sequence variable
     */
    public void setInlineCSS(SelectorSequence sequence) {
        this.sequence = sequence;
    }

    /**
     * Get the value of the valuesRenderer property.
     * @return The value of the valuesRenderer property.
     */
    public ValuesRenderer getValuesRenderer() {
        return valuesRenderer;
    }

    /**
     * Return the version of CSS that this rendering is targetting, if any.
     *
     * @return the version of CSS to target, or null.
     */
    public CSSVersion getCSSVersion() {
        return cssVersion;
    }

    public void setListSeparator(String listSeparator) {
        this.listSeparator = listSeparator;
    }

    public String getListSeparator() {
        return listSeparator;
    }

    public void resetListSeparator() {
        listSeparator = ",";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10835/1	geoff	VBM:2005121405 P900; superscript does not work with CssMobleProfile

 14-Dec-05	10830/5	pduffin	VBM:2005121405 Added Ian Willoughby's comment to make my code easier to non technical people to understand

 14-Dec-05	10830/3	pduffin	VBM:2005121405 Allowed keyword mapper used by renderer to be overridden by CSSVersion, created keyword mapper for vertical-align

 14-Dec-05	10830/1	pduffin	VBM:2005121405 Allowed keyword mapper used by renderer to be overridden by CSSVersion, created keyword mapper for vertical-align

 12-Dec-05	10374/4	emma	VBM:2005111705 Interim commit

 12-Dec-05	10374/2	emma	VBM:2005111705 Interim commit

 06-Dec-05	10612/1	pduffin	VBM:2005120504 Fixed counter parsing issue and some counter test cases

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 01-Sep-05	9412/2	adrianj	VBM:2005083007 CSS renderer using new model

 29-Jul-05	9114/3	geoff	VBM:2005072120 XDIMECP: Implement CSS Counters

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	5733/5	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/3	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Oct-04	5862/1	tom	VBM:2004101909 refactored SelectorVisitor and Selector method names to be inline with Visitor Design Pattern

 28-Apr-04	3937/2	byron	VBM:2004032308 Enhance Menu Support: Theme Changes: Update renderers and parsers

 ===========================================================================
*/

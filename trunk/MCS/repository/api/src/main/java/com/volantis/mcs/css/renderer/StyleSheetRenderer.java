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
 * $Header: /src/voyager/com/volantis/mcs/css/renderer/StyleSheetRenderer.java,v 1.17 2003/03/20 15:15:29 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Apr-02    Allan           VBM:2002042404 - Created. A class that renders
 *                              css style sheets and the components of css
 *                              style sheets.
 * 13-May-02    Allan           VBM:2002042404 - Removed the extraneous ; from
 *                              the end of the last rendered style property in
 *                              renderStyleProperties()
 * 22-May-02    Doug            VBM:2002051701 - Added the abstract
 *                              getKeywordMapperFactory() method.
 * 06-Jun-02    Byron           VBM:2002051303 - Modified renderStyleRule() to
 *                              call the renderSelectorSequence() method whilst
 *                              rendering a rule. This method will do nothing
 *                              if no sequence has been defined.
 * 18-Jun-02    Steve           VBM:2002040807 - gets the mulitple CSS class
 *                              support level for the device and passes this
 *                              to the CSS transformation class
 * 27-Jun-02    Doug            VBM:2002052102 - Added a renderStyleSelectors
 *                              method.
 * 28-Jun-02    Paul            VBM:2002051302 - Changed ValueRendererVisitor
 *                              to be a ValuesRenderer instead.
 * 16-Jul-02    Doug            VBM:2002070801 - Modified the method 
 *                              renderStyleSelectors() to take a list of 
 *                              selectors as an argument rather than a Rule. 
 *                              Modified renderStyleRule() so pass the correct
 *                              arguments to renderStyleSelectors().
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.css.renderer;

import com.volantis.mcs.css.version.CSSSelectorFilter;
import com.volantis.mcs.css.version.CSSStylePropertiesFilter;
import com.volantis.mcs.css.version.CSSVersion;
import com.volantis.mcs.css.version.DefaultCSSSelectorFilter;
import com.volantis.mcs.css.version.DefaultCSSStylePropertiesFilter;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.themes.Selector;
import com.volantis.mcs.themes.StyleException;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.Rule;
import com.volantis.mcs.themes.mappers.KeywordMapperFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A class that renders css style sheets and the components of css style
 * sheets.
 */
public abstract class StyleSheetRenderer {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(StyleSheetRenderer.class);

    /**
     * The property renderers for rendering this style sheet. This should
     * be instanciated by the sub-classes of this class.
     */
    protected Collection propertyRenderers;

    /**
     * The renderer for rendering selectors. This should
     * be instantiated by the sub-classes of this class.
     */
    protected SelectorRendererVisitor selectorRendererVisitor;

    /**
     * The renderer for rendering values.  This should
     * be instantiated by the sub-classes of this class.
     */
    protected ValuesRenderer valuesRenderer;

    /**
     * Initialize the ValuesRenderer. (This method should probably
     * be made abstract and moved down to specific implementations of
     * StyleSheetRenderer.)
     */
    protected void initializeValuesRenderer() {
        valuesRenderer = BasicValuesRenderer.getSingleton();
    }

    /**
     * The protected constructor for this class. Sub-classes should implement
     * getSingleton() to return instances of themselves.
     */
    protected StyleSheetRenderer() {
        initializeValuesRenderer();
    }

    /**
     * Method to return the correct KeywordMapperFactory for this
     * StyleSheetRenderer
     *
     * @return a KeywordMapperFactory object.
     */
    public abstract KeywordMapperFactory getKeywordMapperFactory();

    /**
     * Get the value renderers for this style sheet renderer.
     *
     * @return the ValuesRenderer for this style sheet renderer
     */
    public ValuesRenderer getValuesRenderer() {
        return valuesRenderer;
    }

    /**
     * Get the propertyRenderers for this StyleSheetRenderer.
     *
     * @return propertyRenderers
     */
    public Collection getPropertyRenderers() {
        return propertyRenderers;
    }

    public void renderStyleSheet(StyleSheet styleSheet,
                                 RendererContext rendererContext) throws IOException {

        if (logger.isDebugEnabled()) {
            logger.debug("Rendering style sheet " + styleSheet);
        }

        List rules = styleSheet.getRules();

        // Not sure if we really need this but the old renderDeviceTheme had
        // it so we replicate it here. Really the caller should avoid calling
        // us with an empty style sheet since it is a waste of time.
        if (rules == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Ignored attempt to render empty style sheet");
            }
            return;
        }

        Writer writer = rendererContext.getWriter();

        Iterator iterator = rules.iterator();

        while (iterator.hasNext()) {
            int renderLength = rendererContext.getRenderLength();
            Rule rule = (Rule) iterator.next();
            renderStyleRule(rule, rendererContext);
            if (iterator.hasNext() && renderLength <
                    rendererContext.getRenderLength()) {
                writer.write('\n');
            }
        }

        // Write out the completed style sheet.
        rendererContext.flushStyleSheet();
    }

    /**
     * Render an individual CSS Rule (i.e. print the css for one Rule into
     * a Writer provided by a RendererContext).
     *
     * @param rendererContext the context in which to renderer the device
     */
    public void renderStyleSelectors(List selectors,
                                     RendererContext rendererContext)
            throws IOException {

        if (selectors == null) {
            logger.warn("selector-missing");
            return;
        }

        CSSVersion cssVersion = rendererContext.getCSSVersion();

        int origRenderLength = rendererContext.getRenderLength();
        Writer writer = rendererContext.getWriter();
        Iterator selectorIterator = selectors.iterator();
        int renderLength = origRenderLength;

        // If this render context has CSS version information, create a filter
        // for removing selector sequences that contain unsupported selectors.
        CSSSelectorFilter cssFilter = null;
        if (cssVersion != null) {
            cssFilter = new DefaultCSSSelectorFilter(cssVersion);
        }

        while (selectorIterator.hasNext()) {
            Selector selector = (Selector) selectorIterator.next();
            try {
                if (rendererContext.renderSelectorSequence()) {
                    writer.write(' ');
                }

                // If we have a filter...
                if (cssFilter != null) {
                    // ... then filter the MCS values down to the set understood by
                    // the version of CSS that we are targetting. This reduces page
                    // weight in the output.
                    selector = cssFilter.filter(selector);
                }

                if (selector != null) {
                    rendererContext.renderSelector(selector);
                    renderLength = rendererContext.getRenderLength();
                }
            } catch (StyleException e) {
                // For some reason a selector could not be renderer.
                logger.info("selector-value-invalid", e);
                rendererContext.rewindWriter(renderLength);
            }
            if (selectorIterator.hasNext() && selector != null) {
                writer.write(',');
            }
        }

    }

    /**
     * Render an individual CSS Rule (i.e. print the css for one Rule into
     * a Writer provided by a RendererContext).
     *
     * @param rule            the Rule to render
     * @param rendererContext the context in which to renderer the device
     */
    public void renderStyleRule(Rule rule, RendererContext rendererContext)
            throws IOException {

        int origRenderLength = rendererContext.getRenderLength();
        renderStyleSelectors(rule.getSelectors(), rendererContext);
        int currentRenderLength = rendererContext.getRenderLength();
        if (origRenderLength == currentRenderLength) {
            // No selectors have been rendered so there is no point in trying to
            // render the StyleProperties.
            if (logger.isDebugEnabled()) {
                logger.debug("No selectors rendered for this rule. " +
                        "Ignoring StyleProperties");
            }
        } else {
            // Now render the StyleProperties
            Writer writer = rendererContext.getWriter();
            writer.write('{');
            int propertyStartRenderLength = currentRenderLength + 1;
            renderStyleProperties(rule.getProperties(), rendererContext);
            currentRenderLength = rendererContext.getRenderLength();

            // If the render length is unchanged after the call to
            // renderStyleProperties it means that no properties were rendered for this
            // rule. In this case, we need to rewind the the writer in order to omit
            // this rule.
            if (propertyStartRenderLength == currentRenderLength) {
                rendererContext.rewindWriter(origRenderLength);
            } else {
                writer.write('}');
            }
        }
    }

    /**
     * Render all the style properties in a StyleProperties object (i.e. print
     * the css for a set of style properties into a Writer provided by a
     * RendererContext).
     *
     * @param styleProperties the StyleProperties to render
     * @param rendererContext the context in which to renderer the device
     */
    public void renderStyleProperties(StyleProperties styleProperties,
                                      RendererContext rendererContext)
            throws IOException {

        // If this render context has css version information...
        CSSVersion cssVersion = rendererContext.getCSSVersion();
        if (cssVersion != null) {
            // ... then filter the MCS values down to the set understood by
            // the version of CSS that we are targetting. This reduces page
            // weight in the output.
            CSSStylePropertiesFilter cssFilter = new DefaultCSSStylePropertiesFilter(cssVersion);
            styleProperties = cssFilter.filter(styleProperties);
        }

        int origRenderLength, renderLength = 0, newRenderLength = 0;
        origRenderLength = rendererContext.getRenderLength();
        Iterator iterator = propertyRenderers.iterator();
        while (iterator.hasNext()) {
            renderLength = rendererContext.getRenderLength();
            PropertyRenderer propertyRenderer =
                    (PropertyRenderer) iterator.next();
            try {
                // Make sure thet there is no keyword mapper lying around from
                // a previous call.
                rendererContext.setKeywordMapper(null);

                propertyRenderer.render(styleProperties, rendererContext);
                newRenderLength = rendererContext.getRenderLength();
            } catch (StyleException e) {
                // If we are here it probably means that the propertyRenderer tried
                // to render a StyleValue whose value is not supported by the version
                // of css in use. Such exceptions should only occur if the theme has
                // a value that is not valid on the device. In any case, we need to
                // rewind the writer.
                rendererContext.rewindWriter(renderLength);
                if (logger.isDebugEnabled()) {
                    logger.debug("Tried to render a property with an invalid value.");
                }
            }
        }

        if (renderLength > origRenderLength) {
            // There will always be a ; on the end of the last rendered property.
            // This ; is unecessary so remove it.
            rendererContext.rewindWriter(newRenderLength - 1);
        }
    }

    /**
     * Get the SelectorRenderer for this style sheet renderer.
     *
     * @param context The renderer context for the renderer
     * @return the SelectorRenderer for this style sheet renderer
     */
    public abstract SelectorRendererVisitor
            getSelectorRendererVisitor(RendererContext context);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Nov-05	10246/1	adrianj	VBM:2005110434 Allow user-friendly data entry for style properties

 09-Nov-05	10197/1	adrianj	VBM:2005110434 Allow user-friendly entry of strings and component URIs

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 01-Sep-05	9412/1	adrianj	VBM:2005083007 CSS renderer using new model

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 11-May-05	8123/2	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/6	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 22-Nov-04	5733/5	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/3	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 29-Mar-04	3651/1	steve	VBM:2003052208 Changed logging to debug

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/

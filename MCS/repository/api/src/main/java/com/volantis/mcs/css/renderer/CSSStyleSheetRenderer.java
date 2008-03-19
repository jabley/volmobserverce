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
 * $Header: /src/voyager/com/volantis/mcs/css/renderer/css2/CSSStyleSheetRenderer.java,v 1.11 2002/10/02 11:07:09 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 26-Apr-02    Allan           VBM:2002042404 - Created. A class that renders
 *                              css2 style sheets and the components of css2 
 *                              style sheets. 
 * 14-May-02    Allan           VBM:2002042404 - Removed line-height renderer
 *                              from populatePropertyRenderers() and removed
 *                              getLineHeightRenderer(). (line-height is 
 *                              really part of font rendering.
 * 22-May-02    Doug            VBM:2002051701 - Added a 
 *                              getKeywordMapperFactory() method.
 * 28-Jun-02    Paul            VBM:2002051302 - The css specific mappers have
 *                              moved from the themes.mappers package to the
 *                              css.mappers package.
 * 05-Aug-02    Sumit           VBM:2002073011 - Added new renderers and their
 *                              get methods
 * 16-Sep-02    Ian             VBM:2002091006 - Added renderer for 
 *                              marinerFocus.
 * 01-Oct-02    Ian             VBM:2002092509 - Added borderSpacingRenderer.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.css.renderer;

import com.volantis.mcs.css.mappers.CSSKeywordMapperFactory;
import com.volantis.mcs.css.renderer.shorthand.BorderRenderer;
import com.volantis.mcs.css.renderer.shorthand.FontRenderer;
import com.volantis.mcs.css.renderer.shorthand.MarqueeRenderer;
import com.volantis.mcs.css.renderer.shorthand.MCSBorderRadiusRenderer;
import com.volantis.mcs.css.renderer.shorthand.ShorthandPropertyRenderer;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleShorthands;
import com.volantis.mcs.themes.mappers.KeywordMapperFactory;
import com.volantis.styling.properties.StyleProperty;

import java.util.ArrayList;

/**
 * The style sheet renderer for CSS style sheets.
 *
 * todo: merge this together with StyleSheetRenderer
 */
public class CSSStyleSheetRenderer extends StyleSheetRenderer {

    /**
     * The singleton instance of this StyleSheetRenderer.
     */
    private static CSSStyleSheetRenderer singleton =
        new CSSStyleSheetRenderer();

    /**
     * Get the background property renderer.
     */
    protected PropertyRenderer getBackgroundRenderer() {
        return new ShorthandPropertyRenderer(StyleShorthands.BACKGROUND);
    }

    /**
     * Get the border property renderer.
     */
    protected PropertyRenderer getBorderRenderer() {
        return new BorderRenderer();
    }
    
    
    /**
     * Get the counter increment renderer.
     */
    protected CounterIncrementRenderer getCounterIncrementRenderer() {
        return new CounterIncrementRenderer();
    }

    /**
     * Get the counter reset renderer.
     */
    protected CounterResetRenderer getCounterResetRenderer() {
        return new CounterResetRenderer();
    }


    /**
     * Get the font property renderer.
     */
    protected PropertyRenderer getFontRenderer() {
        return new FontRenderer();
    }

    /**
     * Get the list-style property renderer.
     */
    protected PropertyRenderer getListStyleRenderer() {
        return new ListStyleRenderer();
    }

    /**
     * Get the margin property renderer.
     */
    protected PropertyRenderer getMarginRenderer() {
        return new ShorthandPropertyRenderer(StyleShorthands.MARGIN);
    }

    /**
     * Get the padding property renderer.
     */
    protected PropertyRenderer getPaddingRenderer() {
        return new ShorthandPropertyRenderer(StyleShorthands.PADDING);
    }

    /**
     * Get the textDecoration property renderer.
     */
    protected PropertyRenderer getTextDecorationRenderer() {
        return new TextDecorationRenderer();
    }

    /**
     * Get the MCSBorderRadius property renderer.
     */
    protected PropertyRenderer getMCSBorderRadiusRenderer() {
        return new MCSBorderRadiusRenderer();
    }    
    
    /**
     * Get the MCSBorderRadius shorthand property renderer.
     */
    protected PropertyRenderer getMCSBorderRadiusShorthandRenderer() {
        return new MCSBorderRadiusRenderer();
    }    
    
    
    /**
     * Get the WAP InputFormat property renderer.
     */
    protected PropertyRenderer getWapInputFormatRenderer() {
        return new WapInputFormatRenderer();

    }

    /**
     * Get the WAP InputRequired property renderer.
     */
    protected PropertyRenderer getWapInputRequiredRenderer() {
        return new WapInputRequiredRenderer();

    }

    /**
     * Get the Marquee property renderer.
     * @return PropertyRenderer the renderer for marquee style properties.
     */
    protected PropertyRenderer getMarqueeRenderer() {
        return new MarqueeRenderer();
    }

    /**
     * Construct a new CSSStyleSheetRenderer and initialise its
     * rendererers.
     */
    protected CSSStyleSheetRenderer() {
        populatePropertyRenderers();
    }

    /**
     * Populate the collection of PropertyRenderers for this StyleSheetRenderer.
     */
    protected void populatePropertyRenderers() {
        propertyRenderers = new ArrayList();

        addRenderer(getBackgroundRenderer());
        addRenderer(getBorderRenderer());

        addSimpleRenderers(new StyleProperty[]{
            StylePropertyDetails.BORDER_SPACING,
            StylePropertyDetails.BORDER_COLLAPSE,
            StylePropertyDetails.CAPTION_SIDE,            
            StylePropertyDetails.CLEAR,
            StylePropertyDetails.COLOR,
        });

        addRenderer(getCounterResetRenderer());
        addRenderer(getCounterIncrementRenderer());


        addSimpleRenderers(new StyleProperty[] {
            StylePropertyDetails.DISPLAY,
            StylePropertyDetails.FLOAT,
        });

        addRenderer(getFontRenderer());

        addSimpleRenderers(new StyleProperty[] {
            StylePropertyDetails.FONT_SIZE_ADJUST,
            StylePropertyDetails.FONT_STRETCH,
            StylePropertyDetails.HEIGHT,
            StylePropertyDetails.LETTER_SPACING,
        });

        addRenderer(getListStyleRenderer());
        addRenderer(getMarginRenderer());
        addRenderer(getPaddingRenderer());

        addSimpleRenderers(new StyleProperty[] {
            StylePropertyDetails.TEXT_ALIGN,
        });

        addRenderer(getTextDecorationRenderer());

        addRenderer(getMCSBorderRadiusRenderer());
        
        addSimpleRenderers(new StyleProperty[] {
            StylePropertyDetails.TEXT_INDENT,
            StylePropertyDetails.TEXT_TRANSFORM,
            StylePropertyDetails.VERTICAL_ALIGN,
            StylePropertyDetails.VISIBILITY,
            StylePropertyDetails.WHITE_SPACE,
            StylePropertyDetails.WIDTH,
            StylePropertyDetails.WORD_SPACING,

            StylePropertyDetails.MCS_CARET_COLOR,
            StylePropertyDetails.MCS_FOCUS,
            StylePropertyDetails.MCS_CORNER_RADIUS,
            StylePropertyDetails.MCS_LINE_GAP,
            StylePropertyDetails.MCS_PARAGRAPH_GAP,

            StylePropertyDetails.MCS_IMAGE_INITIAL_FRAME,
            StylePropertyDetails.MCS_IMAGE,
            StylePropertyDetails.MCS_IMAGE_REPEAT_COUNT,
            StylePropertyDetails.MCS_SELECTION_LIST_TRIGGER,
            StylePropertyDetails.OVERFLOW,
            StylePropertyDetails.POSITION,
            StylePropertyDetails.LEFT,
            StylePropertyDetails.TOP,
            StylePropertyDetails.BOTTOM,
            StylePropertyDetails.RIGHT,

            StylePropertyDetails.MCS_BACKGROUND_DYNAMIC_VISUAL,

            StylePropertyDetails.MCS_CHART_ADORNMENTS,
            StylePropertyDetails.MCS_CHART_FOREGROUND_COLORS,
            StylePropertyDetails.MCS_CHART_GRID_COLOR,
            StylePropertyDetails.MCS_CHART_HEIGHT,
            StylePropertyDetails.MCS_CHART_WIDTH,
            StylePropertyDetails.MCS_CHART_LABEL_VALUES,
            StylePropertyDetails.MCS_CHART_X_AXIS_ANGLE,
            StylePropertyDetails.MCS_CHART_Y_AXIS_ANGLE,

            StylePropertyDetails.CONTENT,
            StylePropertyDetails.COUNTER_INCREMENT,
            StylePropertyDetails.COUNTER_RESET,

            StylePropertyDetails.MCS_MMFLASH_ALIGN,
            StylePropertyDetails.MCS_MMFLASH_QUALITY,
            StylePropertyDetails.MCS_MMFLASH_SCALE,
            StylePropertyDetails.MCS_MMFLASH_SCALED_ALIGN,
            StylePropertyDetails.MCS_MMFLASH_WINDOWS_MODE,

            StylePropertyDetails.MCS_FORM_ACTION_STYLE,
            StylePropertyDetails.MCS_FORM_ACTION_IMAGE,
            StylePropertyDetails.MCS_COLUMNS,
            StylePropertyDetails.MCS_ROWS,
            StylePropertyDetails.MCS_SELECTION_LIST_OPTION_LAYOUT,
            StylePropertyDetails.MCS_SELECTION_LIST_STYLE,
            StylePropertyDetails.MCS_SELECTION_LIST_TRIGGER,
            StylePropertyDetails.MCS_HTTP_METHOD_HINT,
            StylePropertyDetails.MCS_BREAK_AFTER,

            StylePropertyDetails.MCS_CONTAINER,
            StylePropertyDetails.MCS_LAYOUT,
            StylePropertyDetails.MCS_FRAGMENT_LIST_ORIENTATION,
            StylePropertyDetails.MCS_FRAGMENT_LIST_POSITION,
            StylePropertyDetails.MCS_LINK_MEDIA,
            StylePropertyDetails.MCS_LINK_STYLE,

            StylePropertyDetails.MCS_IMAGE_SAVING,
            StylePropertyDetails.MCS_IMAGE_FRAME_INTERVAL,

            StylePropertyDetails.MCS_AURAL_DTMF_ALLOCATION,
            StylePropertyDetails.MCS_AURAL_MENU_SCOPE,
            StylePropertyDetails.MCS_MENU_STYLE,
            StylePropertyDetails.MCS_MENU_LINK_STYLE,
            StylePropertyDetails.MCS_MENU_IMAGE_STYLE,
            StylePropertyDetails.MCS_MENU_TEXT_STYLE,
            StylePropertyDetails.MCS_MENU_ORIENTATION,
            StylePropertyDetails.MCS_MENU_ITEM_ITERATOR_ALLOCATION,
            StylePropertyDetails.MCS_MENU_ITEM_ORDER,
            StylePropertyDetails.MCS_MENU_ITEM_ORIENTATION,
            StylePropertyDetails.MCS_MENU_ITEM_ACTIVE_AREA,
            StylePropertyDetails.MCS_MENU_ITEM_SHORTCUT_ACTIVE,
            StylePropertyDetails.MCS_MENU_SEPARATOR_CHARACTERS,
            StylePropertyDetails.MCS_MENU_SEPARATOR_POSITION,
            StylePropertyDetails.MCS_MENU_SEPARATOR_REPEAT,
            StylePropertyDetails.MCS_MENU_SEPARATOR_IMAGE,
            StylePropertyDetails.MCS_MENU_SEPARATOR_TYPE,
            StylePropertyDetails.MCS_MENU_HORIZONTAL_SEPARATOR,

            StylePropertyDetails.MCS_FRAME_RATE,
            StylePropertyDetails.MCS_EFFECT_DURATION,
            StylePropertyDetails.MCS_EFFECT_STYLE,
            StylePropertyDetails.MCS_TRANSITION_INTERVAL,
            StylePropertyDetails.MCS_INITIAL_STATE,
            StylePropertyDetails.MCS_TOGGLE_EVENT,
            StylePropertyDetails.MCS_VALIDATION_ERROR_ACTION,
            StylePropertyDetails.MCS_CANCELABLE,
            StylePropertyDetails.DIRECTION
        });

        addRenderer(getMarqueeRenderer());

        addRenderer(getWapInputFormatRenderer());
        addRenderer(getWapInputRequiredRenderer());
    }

    private void addSimpleRenderers(StyleProperty[] properties) {
        for (int i = 0; i < properties.length; i++) {
            StyleProperty property = properties[i];
            addRenderer(new SimplePropertyRenderer(property));
        }
    }

    /**
     * Add a property renderer to the list of property renderers.
     *
     * @param renderer the renderer to add.
     */
    private void addRenderer(final PropertyRenderer renderer) {

        if (renderer != null) {
            propertyRenderers.add(renderer);
        }
    }

    /**
     * Get the singleton instance of this StyleSheetRenderer.
     * @return the singleton CSSSelectorRendererVisitor
     */
    public static StyleSheetRenderer getSingleton() {
        return singleton;
    }

    // Javadoc inherited
    public KeywordMapperFactory getKeywordMapperFactory() {
      return CSSKeywordMapperFactory.getSingleton();
    }

    // Javadoc inherited
    public SelectorRendererVisitor getSelectorRendererVisitor(
            RendererContext context) {
        return new CSSSelectorRendererVisitor(context);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10585/1	pduffin	VBM:2005112407 Fixed pair rendering issue, also fixed string rendering as well

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 01-Sep-05	9412/2	adrianj	VBM:2005083007 CSS renderer using new model

 22-Aug-05	9324/3	ianw	VBM:2005080202 Move validation for WapCSS into styling

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 29-Jul-05	9114/3	geoff	VBM:2005072120 XDIMECP: Implement CSS Counters

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	5733/12	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 22-Nov-04	5733/10	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/8	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 28-Apr-04	3937/1	byron	VBM:2004032308 Enhance Menu Support: Theme Changes: Update renderers and parsers

 ===========================================================================
*/

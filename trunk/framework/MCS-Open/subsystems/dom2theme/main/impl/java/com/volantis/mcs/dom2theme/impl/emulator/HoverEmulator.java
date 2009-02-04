/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.dom2theme.impl.emulator;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom2theme.impl.model.OutputStyles;
import com.volantis.mcs.dom2theme.impl.model.PseudoStylePath;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.PropertyValueIteratee;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.StatefulPseudoClassImpl;
import com.volantis.styling.properties.StyleProperty;

/**
 * @link JavascriptStylesEmulator} implementation which emulates :hover styling
 * by setting the onmouseover and onmouseout attributes to the appropriate
 * styles.
 */
public class HoverEmulator extends JavascriptStylesEmulator {

    /**
     * Javascript event names.
     */
    public static final String ON_MOUSE_OUT = "onmouseout";
    public static final String ON_MOUSE_OVER = "onmouseover";

    // Javadoc unnecessary.
    public HoverEmulator() {
        super();
    }

    /**
     * Add events to change the styling when the user moves their mouse over or
     * out of the element we are rendering (in order to emulate .hover styling
     * on devices that do not support it i.e. IE6 and under).
     * <p/>
     * The OutputStyles are sparsely populated, so only the styles which were
     * explicitly set on this element will be emulated in javascript.
     *
     * @param element       whose styles are being emulated
     * @param outputStyles  styles to emulate
     */
    public void emulate(Element element,
                        OutputStyles outputStyles) {

        // Only need to emulate the output styles if there are some...
        if (outputStyles != null) {
            MutableStyleProperties hoverProperties =
                    outputStyles.getPathProperties(
                            PseudoStylePath.EMPTY_PATH.addPseudoClassSet(
                                    StatefulPseudoClassImpl.HOVER.getSet()));

            if (hoverProperties != null && !hoverProperties.isEmpty()) {
                final String previousOnMouseOver =
                        element.getAttributeValue(ON_MOUSE_OVER);
                final String previousOnMouseOut =
                        element.getAttributeValue(ON_MOUSE_OUT);

                StringBuffer mouseOverBuffer = previousOnMouseOver == null?
                        new StringBuffer(): new StringBuffer(previousOnMouseOver);
                StringBuffer mouseOutBuffer = previousOnMouseOut == null?
                        new StringBuffer(): new StringBuffer(previousOnMouseOut);
                final HoverStylePropertyIteratee iteratee =
                        new HoverStylePropertyIteratee(mouseOverBuffer,
                                mouseOutBuffer);
                hoverProperties.iteratePropertyValues(iteratee);
                // emulate onmouseover styles (those when hovering).
                element.setAttribute(ON_MOUSE_OVER, mouseOverBuffer.toString());
                // emulate onmouseout styles (those when not hovering).
                element.setAttribute(ON_MOUSE_OUT, mouseOutBuffer.toString());
            }
        }
    }

    /**
     * {@link PropertyValueIteratee} implementation which emulates :hover
     * styling.
     */
    private class HoverStylePropertyIteratee implements PropertyValueIteratee {
        private final StringBuffer mouseOverBuffer;
        private final StringBuffer mouseOutBuffer;

        /**
         * Initialize a new instance using the given parameters.
         *
         * @param mouseOverBuffer   to which the emulated :hover styles should
         *                          be added
         * @param mouseOutBuffer    to which the normal styles should be added
         */
        public HoverStylePropertyIteratee(StringBuffer mouseOverBuffer,
                                          StringBuffer mouseOutBuffer) {
            this.mouseOverBuffer = mouseOverBuffer;
            this.mouseOutBuffer = mouseOutBuffer;
        }

        // Javadoc unnecessary.
        public IterationAction next(PropertyValue propertyValue) {

            // The property being emulated.
            final StyleProperty property = propertyValue.getProperty();
            final String jsName = JavascriptToCSSStyleMapper.
                    getDefaultInstance().getJSForCSS(property.getName());

            // Add the hover property value as the mouse over value.
            final String hoverValue = propertyValue.getValue().getStandardCSS();
            appendStyleValue(mouseOverBuffer, jsName, hoverValue);

            // Clear the styles on mouse out events.
            appendStyleValue(mouseOutBuffer, jsName, "");

            return IterationAction.CONTINUE;
        }

        /**
         * Convenience method for appending a style property to the buffer
         * which will be used to set the event attribute.
         *
         * @param buffer    to which to append the value
         * @param name      name of the style property
         * @param value     value of the style property
         */
        private void appendStyleValue(StringBuffer buffer,
                                      String name,
                                      String value) {
            // Append the separator if necessary.
            if (buffer.length() != 0) {
                buffer.append(";");
            }
            buffer.append("style.").append(name).append("='");
            buffer.append(value).append("'");
        }
    }
}


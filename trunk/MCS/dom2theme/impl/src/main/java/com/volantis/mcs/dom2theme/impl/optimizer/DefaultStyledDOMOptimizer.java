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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.dom2theme.impl.optimizer;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.NodeIteratee;
import com.volantis.mcs.dom.RecursingDOMVisitor;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElement;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementList;
import com.volantis.mcs.dom2theme.impl.model.OutputStyles;
import com.volantis.mcs.themes.StyleValues;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.device.DeviceStyles;
import com.volantis.styling.device.DeviceStylingEngine;
import com.volantis.styling.engine.Attributes;
import com.volantis.styling.sheet.CompiledStyleSheet;

/**
 * Optimize a styled document.
 *
 * <p>Traverses the document optimizing the styles associated with the
 * elements converting them into a list.</p>
 *
 * <p>The styles for an element are optimized after the styles for the children
 * as the children need access to the parent styles in order to determine
 * whether they are inherited or not.</p>
 *
 * <p>Before processing the children an element's styles are preprocessed in
 * order to fix up a couple of issues with the input that affects
 * inheritance.</p>
 *
 * <p>The device specific styles are applied to the element so that they can
 * be used to determine which properties need to be written out.</p>
 */
public class DefaultStyledDOMOptimizer
        extends RecursingDOMVisitor
        implements StyledDOMOptimizer, NodeIteratee, Attributes {

    /**
     * The root styles for the document.
     */
    private final StyleValues rootStyles;

    /**
     * The optimizer for a {@link Styles}.
     */
    private final StylesOptimizer optimizer;

    /**
     * The engine to use to apply the device styles.
     */
    private final DeviceStylingEngine deviceStylingEngine;

    /**
     * The current element being styled.
     */
    private Element element;

    /**
     * The list of {@link OutputStyledElement}.
     */
    private OutputStyledElementList elementList;

    /**
     * The values for the parent.
     */
    private StyleValues parentValues;

    /**
     * Initialise.
     *
     * @param propertiesOptimizer The optimizer for properties.
     * @param rootStyles          The styles for the root.
     * @param deviceStyleSheet    The device specific style sheet.
     */
    public DefaultStyledDOMOptimizer(
            InputPropertiesOptimizer propertiesOptimizer,
            final StyleValues rootStyles,
            CompiledStyleSheet deviceStyleSheet) {

        optimizer = new StylesOptimizer(propertiesOptimizer);

        // First, create the default styles that apply to start with.
        this.rootStyles = rootStyles;

        deviceStylingEngine = StylingFactory.getDefaultInstance()
                .createDeviceStylingEngine(deviceStyleSheet);
    }

    // Javadoc inherited.
    public OutputStyledElementList optimize(Document dom) {

        elementList = new OutputStyledElementList();

        this.parentValues = rootStyles;

        dom.forEachChild(this);

        return elementList;
    }

    // Javadoc inherited.
    public void visit(final Element element) {

        DeviceStyles deviceStyles = null;
        String localName = element.getName();

        // An element is treated as internal if it uses an upper case name for
        // the element.
        boolean internalElement = Character.isUpperCase(localName.charAt(0));

        if (!internalElement) {
            // Style the element before processing its children.
            this.element = element;
            deviceStyles = deviceStylingEngine.startElement(
                                localName, this);
        }

        Styles inputStyles = element.getStyles();
        StyleValues oldParentValues = parentValues;
        if (inputStyles != null) {
            parentValues = inputStyles.getPropertyValues();
        }

        // Process the children.
        element.forEachChild(this);

        // Reset the parent values.
        parentValues = oldParentValues;

        if (!internalElement) {
            // Normalize the input styles.
            OutputStyles outputStyles;
            if (inputStyles == null) {
                outputStyles = null;
            } else {
                outputStyles = optimizer.calculateOutputStyles(localName,
                        inputStyles, parentValues, deviceStyles);
            }

            // add the OutputStyledElement to the list
            OutputStyledElement outputElement = new OutputStyledElement(element,
                    outputStyles);

            elementList.add(outputElement);

            deviceStylingEngine.endElement(localName);

            // Clear the styles from the element as they are no longer needed.
            element.clearStyles();
        }
    }

    // Javadoc inherited.
    public String getAttributeValue(String namespace, String localName) {
        return element.getAttributeValue(localName);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/11	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10505/7	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (5)

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Made session context create its contents lazily and optimised PseudoStylePath

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (4)

 29-Nov-05	10370/2	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (4)

 18-Nov-05	10370/1	geoff	VBM:2005111405 interim commit

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 16-Aug-05	9286/1	geoff	VBM:2005072208 Normalizing of inferrable properties does not work properly.

 18-Jul-05	8668/13	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/

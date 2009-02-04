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
package com.volantis.mcs.protocols.css.emulator;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.css.emulator.renderer.StyleEmulationPropertiesRenderer;
import com.volantis.styling.Styles;
import com.volantis.styling.values.MutablePropertyValues;

import java.util.HashSet;

/**
 * Default implementation of {@link StyleEmulationRenderer}.
 * <p/>
 * This calls back on the protocol and protocol configuration to do the bulk of
 * the work of the style emulation rendering.
 */
public final class DefaultStyleEmulationRenderer
        implements StyleEmulationRenderer {

    /**
     * The main underlying interface to style emulation.
     */
    private final StyleEmulationPropertiesRenderer propertiesRenderer;

    /**
     * Those elements that should not have style emulation rendering added.
     */
    private final HashSet exclusions = new HashSet();

    /**
     * Initialise.
     *
     * @param propertiesRenderer main underlying interface to style emulation.
     */
    public DefaultStyleEmulationRenderer(
            StyleEmulationPropertiesRenderer propertiesRenderer) {

        this.propertiesRenderer = propertiesRenderer;
    }

    // Javadoc inherited.
    public void applyStyleToElement(Element element) {

        if (isElementToBeStyled(element)) {

            Styles styles = element.getStyles();

            if (styles != null) {
                MutablePropertyValues propertyValues =
                        styles.getPropertyValues();
                if (propertyValues != null) {
                    propertiesRenderer.applyProperties(element, propertyValues);
                }
            }

        }
    }

    /**
     * Return true if the element has not been excluded from styling.
     *
     * @param element elements to be checked.
     * @return
     */
    private boolean isElementToBeStyled(Element element) {
        return !exclusions.contains(element.getName());
    }

    /**
     * Tell the renderer that the given element name should not have style
     * emulation applied to it.
     *
     * @param elementName
     */
    public void exclude(String elementName) {
        exclusions.add(elementName);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Sep-05	9128/5	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 06-Sep-05	9413/1	schaloner	VBM:2005070406 Implemented before and after pseudo-element support

 22-Aug-05	9184/3	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 19-Jul-05	8668/1	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 22-Jun-05	8483/4	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/2	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6183/1	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 20-Jul-04	4897/3	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 15-Jul-04	4869/1	geoff	VBM:2004062303 Implementation of theme style options: HTMLVersion 3.2 Family

 12-Jul-04	4783/1	geoff	VBM:2004062302 Implementation of theme style options: WML Family

 ===========================================================================
*/

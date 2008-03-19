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
 * $Header: /src/voyager/com/volantis/mcs/protocols/wml/WMLOpenWave1_3.java,v 1.2 2002/10/10 10:22:12 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 10-Oct-02    Allan           VBM:2002100801 - Created. Protocol for the
 *                              OpenWave browser based on WML 1.3.
 * 02-Jun-03    Mat             VBM:2003042906 - Removed doProtocolString()
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.FragmentLinkRenderer;
import com.volantis.mcs.protocols.HorizontalRuleAttributes;
import com.volantis.mcs.protocols.MenuRenderer;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.SelectionRenderer;
import com.volantis.mcs.protocols.XFSelectAttributes;
import com.volantis.mcs.protocols.menu.MenuModule;
import com.volantis.mcs.protocols.menu.MenuModuleRendererFactory;
import com.volantis.mcs.protocols.menu.MenuModuleRendererFactoryFilter;
import com.volantis.mcs.protocols.wml.menu.OpenwaveMenuModule;
import com.volantis.mcs.protocols.wml.menu.OpenwaveMenuModuleRendererFactory;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.MCSSelectionListStyleKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.values.PropertyValues;

/**
 * Protocol class for the version of the OpenWave browser that supports
 * WML version 1.3.
 * <p/>
 * NOTE: This is the protocol that supports the OpenwaveVersion 5 (and up)
 * browser. The DTD is specified in PublicIdFactory.
 */
public class WMLOpenWave1_3 extends WMLVersion1_3 {

    /**
     * Initialise this object.
     *
     * @param supportFactory The factory used by the protocol to obtain support
     *                       objects.
     * @param configuration  The protocol specific configuration.
     */
    public WMLOpenWave1_3(
            ProtocolSupportFactory supportFactory,
            ProtocolConfiguration configuration) {

        super(supportFactory, configuration);
    }

    public void initialise() {
        super.initialise();

        supportsAutomaticShortcutPrefixDisplay = true;
    }

    // javadoc inherited from superclass
    protected void doHorizontalRule(
            DOMOutputBuffer dom,
            HorizontalRuleAttributes attributes) {
        dom.addStyledElement("hr", attributes);
    }

    // javadoc inherited
    public MenuRenderer createNumericShortcutMenuRenderer() {
        return new OpenwaveNumericShortcutMenuRenderer(this, context);
    }

    // Inherit Javadoc.
    protected FragmentLinkRenderer createNumericShortcutFragmentLinkRenderer() {

        WMLFragmentLinkRendererContext context =
                (WMLFragmentLinkRendererContext)
                getFragmentLinkRendererContext();

        return new OpenWaveNumericShortcutFragmentLinkRenderer(context);

    }

    // javadoc inherited from superclass
    protected SelectionRenderer
            getSelectionRenderer(XFSelectAttributes attributes) {

        return new WMLDefaultSelectionRenderer(this) {
            protected void doSelectAttributes(Element element) {

                super.doSelectAttributes(element);

                if (!this.attributes.isMultiple()) {
                    Styles styles = attributes.getStyles();
                    PropertyValues propertyValues = styles.getPropertyValues();
                    StyleValue value = propertyValues.getComputedValue(
                            StylePropertyDetails.MCS_SELECTION_LIST_STYLE);
                    String listStyle = null;
                    if (value == MCSSelectionListStyleKeywords.CIRCULAR_LIST) {
                        listStyle = "spin";
                    } else if (value == MCSSelectionListStyleKeywords.MENU) {
                        listStyle = "popup";
                    } else if (value == MCSSelectionListStyleKeywords.CONTROLS) {
                        listStyle = "radio";
                    }

                    if (listStyle != null) {
                        element.setAttribute("type", listStyle);
                    }
                }
            }
        };
    }

    //========================================================================
    // MenuModule related implementation.
    //========================================================================

    /**
     * Create an OpenWave / Phone.com specific menu module.
     */
    protected MenuModule createMenuModule(
            MenuModuleRendererFactoryFilter factoryFilter) {

        // Create a new instance of the super class's menu module. This cannot
        // call the super class's getMenuModule() method as that would invoke
        // this createMenuModule() method causing infinite recursion.
        MenuModule delegate = super.createMenuModule(factoryFilter);

        MenuModuleRendererFactory rendererFactory =
                new OpenwaveMenuModuleRendererFactory(getRendererContext(),
                                                      getDeprecatedOutputLocator(),
                                                      getMenuModuleCustomisation());

        if (factoryFilter != null) {
            rendererFactory = factoryFilter.decorate(rendererFactory);
        }

        return new OpenwaveMenuModule(getRendererContext(), rendererFactory,
                                      delegate);
    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 23-Jun-05	8833/1	pduffin	VBM:2005042901 Addressing review comments

 09-Jun-05	8665/2	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 20-Jul-04	4897/1	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 14-May-04	4315/10	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 13-May-04	4315/7	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 13-May-04	4315/5	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 13-May-04	4315/3	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 12-May-04	4315/1	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 12-May-04	4279/4	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 11-May-04	4217/1	geoff	VBM:2004042807 Enhance Menu Support: Renderers: Menu Markup

 06-May-04	4153/1	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 30-Apr-04	4124/3	claire	VBM:2004042805 Openwave and WML menu renderer selectors

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 29-Apr-04	4091/3	claire	VBM:2004042804 Enhanced menus: protocol integration and Openwave end to end

 02-Oct-03	1469/2	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 25-Sep-03	1412/14	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links (sigh, rework as per dougs request)

 23-Sep-03	1412/10	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links (rework to get link style from source fragment)

 17-Sep-03	1412/7	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 17-Sep-03	1394/5	doug	VBM:2003090902 added support for openwave numeric shortcut menus

 16-Sep-03	1301/5	byron	VBM:2003082107 Support Openwave GUI Browser extensions - single select controls handled as type=radio

 10-Sep-03	1301/1	byron	VBM:2003082107 Support Openwave GUI Browser extensions

 04-Sep-03	1331/1	byron	VBM:2003090201 OpenWave protocols not using correct DTD

 06-Jun-03	335/1	mat	VBM:2003042906 Merged changes to MCS

 ===========================================================================
*/

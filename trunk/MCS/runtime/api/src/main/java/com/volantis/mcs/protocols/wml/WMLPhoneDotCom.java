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
 * $Header: /src/voyager/com/volantis/mcs/protocols/wml/WMLPhoneDotCom.java,v 1.2 2002/03/22 18:24:28 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 06-Sep-01    Kula            VBM:2001083116 - Created to support phone.com
 *                              browsers, which supports accesskeys.
 * 07-Sep-01    Kula            VBM:2001083116 - javadoc updated for the class
 *                              and constructor.
 * 20-Sep-01    Allan           VBM:2001083116 - Corrected the protocol
 *                              string.
 * 04-Oct-01    Doug            VBM:2001100201 - The inherited property
 *                              supportsAccessKeys has been renamed to
 *                              supportsAccessKeyAttribute in super class.
 *                              Modified the constructor to reflect this name
 *                              change.
 * 31-Jan-02    Paul            VBM:2001122105 - Restructured to reflect
 *                              changes to protocols.
 * 22-Feb-02    Paul            VBM:2002021802 - Moved from protocols package
 *                              and modified to make it generate a DOM.
 * 02-Jun-03    Mat             VBM:2003042906 - Removed doProtocolString()
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.wml;

import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.protocols.FragmentLinkRenderer;
import com.volantis.mcs.protocols.MenuRenderer;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.menu.MenuModule;
import com.volantis.mcs.protocols.menu.MenuModuleRendererFactory;
import com.volantis.mcs.protocols.menu.MenuModuleRendererFactoryFilter;
import com.volantis.mcs.protocols.wml.menu.OpenwaveMenuModule;
import com.volantis.mcs.protocols.wml.menu.OpenwaveMenuModuleRendererFactory;


/**
 * This class is used to support the phone.com browsers, which support
 * accesskeys.
 */
public class WMLPhoneDotCom extends WMLVersion1_1 {

    /**
     * this will enabled the access keys
     */
    public WMLPhoneDotCom(ProtocolSupportFactory protocolSupportFactory,
            ProtocolConfiguration protocolConfiguration) {

        super(protocolSupportFactory, protocolConfiguration);

        // Define the default accesskey attribute support for this protocol
        // family
        supportsAccessKeyAttribute = true;
    }

    // javadoc inherited
    public MenuRenderer createNumericShortcutMenuRenderer() {
        return new OpenwaveNumericShortcutMenuRenderer(this, context);
    }

    // Inherit Javadoc.
    protected FragmentLinkRenderer createNumericShortcutFragmentLinkRenderer() {

        // Here we use emulation rather than the native openwave way since
        // it only works well on openwave 5 rather than openwave 4 which this
        // protocol is.

        WMLFragmentLinkRendererContext rendererContext =
                (WMLFragmentLinkRendererContext)
                getFragmentLinkRendererContext();

        // add a filter that handles special accesskey value generation
        // The accesskey value should prefix the menu label
        boolean doesDeviceDisplayAccesskeyNums =
                context.getDevice().getBooleanPolicyValue(DevicePolicyConstants.
                                                          SUPPORTS_WML_ACCESSKEY_AUTOMAGIC_NUMBER_DISPLAY);

        return new WMLNumericShortcutFragmentLinkRenderer(rendererContext,
                                                          !doesDeviceDisplayAccesskeyNums);

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

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 23-Jun-05	8833/1	pduffin	VBM:2005042901 Addressing review comments

 24-May-05	8462/1	philws	VBM:2005052310 Port device access key override from 3.3

 24-May-05	8430/1	philws	VBM:2005052310 Allow the device to override a protocol's access key support

 16-Feb-05	6129/5	matthew	VBM:2004102019 yet another supermerge

 16-Feb-05	6129/3	matthew	VBM:2004102019 yet another supermerge

 23-Nov-04	6129/1	matthew	VBM:2004102019 Enable shortcut menu link rendering

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4315/10	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 13-May-04	4315/7	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 13-May-04	4315/5	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 13-May-04	4315/3	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 12-May-04	4315/1	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 12-May-04	4279/4	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 11-May-04	4217/1	geoff	VBM:2004042807 Enhance Menu Support: Renderers: Menu Markup

 06-May-04	4174/3	claire	VBM:2004050501 Enhanced Menus: (X)HTML menu renderer selectors and protocol integration

 06-May-04	4153/1	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 05-May-04	4124/1	claire	VBM:2004042805 Refining menu renderer selectors

 02-Oct-03	1469/3	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 25-Sep-03	1412/3	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links (sigh, rework as per dougs request)

 17-Sep-03	1394/1	doug	VBM:2003090902 added support for openwave numeric shortcut menus

 10-Jun-03	358/1	geoff	VBM:2003060901 implement accesskey attribute for WBSAX output; not tested on phone.

 06-Jun-03	335/1	mat	VBM:2003042906 Merged changes to MCS
 10-Jun-03	347/1	geoff	VBM:2003060901 implement accesskey attribute for WBSAX output; not tested on phone.

 ===========================================================================
*/

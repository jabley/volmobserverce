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
 * $Header: /src/voyager/com/volantis/mcs/protocols/wml/WMLVersion1_2.java,v 1.3 2003/03/17 11:21:44 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Jul-01    Paul            VBM:2001062810 - Added this header, fixed
 *                              the code so that it only gets values out of
 *                              attributes once and uses append for each
 *                              separate part of the output string instead of
 *                              inline string concatenation. Also removed
 *                              DOS end of line characters.
 * 23-Jul-01    Paul            VBM:2001070507 - Stopped creating StringBuffers
 *                              when returning a fixed string and reduced the
 *                              amount of duplicated code by setting the
 *                              supportsAccessKeys flag to true and overridding
 *                              addGoAttributes.
 * 04-Oct-01    Doug            VBM:2001100201 - The inherited property
 *                              supportsAccessKeys has been renamed to
 *                              supportsAccessKeyAttribute in super class.
 *                              Modified the constructor to reflect this name
 *                              change.
 * 31-Jan-02    Paul            VBM:2001122105 - Restructured to reflect
 *                              changes to protocols.
 * 22-Feb-02    Paul            VBM:2002021802 - Moved from protocols package
 *                              and modified to make it generate a DOM.
 * 12-Mar-03    Phil W-S        VBM:2003031110 - Added usage of new 1.2
 *                              configuration singleton, with updated outputter
 *                              and dissector (with associated getXMLOutputter
 *                              and getDissector methods). Correct the imports
 *                              and updated the constructor to store the
 *                              correct configuration instance. Fixed file
 *                              indent. 
 * 23-May-03    Mat             VBM:2003042907 - Removed XMLOutputter static
 *                              and getXMLOutputter.
 * 30-May-03    Mat             VBM:2003042911 - Removed doProtocolString()
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.wml;

import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.FragmentLinkRenderer;
import com.volantis.mcs.protocols.MenuRenderer;
import com.volantis.mcs.protocols.PreAttributes;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolSupportFactory;

/**
 * NOTE: WML1.2 is abstract because we don't support it externally. It is
 * purely here to hang features that were added in 1.2 onto.
 */
public abstract class WMLVersion1_2 extends WMLVersion1_1 {

    /**
     * Initialise this object.
     *
     * @param supportFactory The factory used by the protocol to obtain support
     *                       objects.
     * @param configuration  The protocol specific configuration.
     */
    protected WMLVersion1_2(
            ProtocolSupportFactory supportFactory,
            ProtocolConfiguration configuration) {

        super(supportFactory, configuration);

        // WML 1.2 has no protocol configuration since it is abstract and by
        // the current "design" the protocol configuration must be overridden
        // by it's subclasses.

        // Define the default accesskey attribute support for this protocol
        // family
        supportsAccessKeyAttribute = true;
    }

    // Javadoc inherited from super class.
    protected void openPre(
            DOMOutputBuffer dom,
            PreAttributes attributes) {

        dom.openStyledElement("pre", attributes);
        
    }

    // Javadoc inherited from super class.
    protected void closePre(
            DOMOutputBuffer dom,
            PreAttributes attributes) {

        dom.closeElement("pre");
    }

    // javadoc inherited
    protected MenuRenderer createNumericShortcutMenuRenderer() {
        return new WMLNumericShortcutMenuRenderer(this, context);
    }

    // Inherit Javadoc.
    protected FragmentLinkRenderer createNumericShortcutFragmentLinkRenderer() {

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

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 22-Aug-05	9184/1	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 23-Jun-05	8833/1	pduffin	VBM:2005042901 Addressing review comments

 09-Jun-05	8665/2	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 24-May-05	8462/1	philws	VBM:2005052310 Port device access key override from 3.3

 24-May-05	8430/1	philws	VBM:2005052310 Allow the device to override a protocol's access key support

 16-Feb-05	6129/5	matthew	VBM:2004102019 yet another supermerge

 16-Feb-05	6129/3	matthew	VBM:2004102019 yet another supermerge

 23-Nov-04	6129/1	matthew	VBM:2004102019 Enable shortcut menu link rendering

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Jul-04	4783/7	geoff	VBM:2004062302 Implementation of theme style options: WML Family (fixes after style inversion approval)

 12-Jul-04	4783/4	geoff	VBM:2004062302 Implementation of theme style options: WML Family

 14-Jul-04	4752/3	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities - addressed rework issues

 13-Jul-04	4752/1	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities

 02-Oct-03	1469/4	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 26-Sep-03	1423/1	doug	VBM:2003091701 try and commit so geoff can have his own wspace

 30-Jun-03	605/1	geoff	VBM:2003060607 port from metis to mimas

 05-Jun-03	285/6	mat	VBM:2003042911 Merged with MCS

 ===========================================================================
*/

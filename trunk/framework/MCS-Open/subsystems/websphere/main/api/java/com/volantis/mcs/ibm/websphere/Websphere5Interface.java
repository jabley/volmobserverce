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
package com.volantis.mcs.ibm.websphere;

import com.volantis.mcs.expression.ExpressionSupport;
import com.volantis.mcs.ibm.websphere.mcsi.MCSIContentHandlerFactory;
import com.volantis.mcs.ibm.websphere.mcsi.expression.functions.PolicyFunction;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.marlin.sax.NamespaceSwitchContentHandlerMap;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.namespace.ImmutableExpandedName;

/**
 * Methods to access information from WebSphere 5.0/5.1 and the WebSphere
 * mobile portal server. This initializes the MCSI namespace content handler
 * mapping and the MCSI prefix and functions for expression handling.
 */
public class Websphere5Interface extends Websphere40Interface {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(Websphere5Interface.class);

    /**
     * The MCSI namespace URI specific to WebSphere.
     */
    public static final String MCSI_URI =
            "http://www.ibm.com/mwp/mcs-integration";

    /**
     * Flag that ensures that MCSI initialization is only performed once.
     */
    private boolean initialized = false;

    /**
     * Initializes the new instance.
     */
    public Websphere5Interface() {
        super();
    }

    // javadoc inherited
    protected String appServerVersion() {
        return "5";
    }

    // Javadoc inherited from interface.
    public void setVolantisBean(Volantis bean) {
        volantisBean = bean;

        // Only perform the extra MCSI initialization once
        if (!initialized) {
            initialized = true;

            // Initialise a content handler for the MCSI namespace.
            NamespaceSwitchContentHandlerMap.getInstance().addContentHandler(
                    MCSI_URI, new MCSIContentHandlerFactory());

            // Initialise MCSI expression facilities
            ExpressionSupport.registerExternalNameSpacePrefix("mcsi", MCSI_URI);
            ExpressionSupport.registerExternalFunction(
                    new ImmutableExpandedName(MCSI_URI, "policy"),
                    new PolicyFunction());
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 31-Aug-05	9391/1	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 04-Jul-05	8940/2	philws	VBM:2005060606 Provider installer compatible app server interface implementations

 28-Apr-05	7922/1	pduffin	VBM:2005042801 Removed User and UserFactory classes

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/7	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 21-Jun-04	4702/3	matthew	VBM:2004061402 rework PageTracking

 16-Jun-04	4702/1	matthew	VBM:2004061402 management functionality added

 19-Feb-04	2789/5	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/3	tony	VBM:2004012601 update localisation services

 16-Feb-04	2966/4	ianw	VBM:2004011923 Fixed namespace issues

 13-Feb-04	2966/2	ianw	VBM:2004011923 Added mcsi:policy function

 04-Feb-04	2828/1	ianw	VBM:2004011922 Added MCSI content handler

 02-Feb-04	2802/3	ianw	VBM:2004011921 Fixed copyright and added into AppServerInterface

 02-Feb-04	2802/1	ianw	VBM:2004011921 Added mechanism to enable AppServer interfaces to configure NamespaceSwitchContentHandler

 ===========================================================================
*/

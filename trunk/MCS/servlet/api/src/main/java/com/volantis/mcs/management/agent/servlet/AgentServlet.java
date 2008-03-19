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
package com.volantis.mcs.management.agent.servlet;

import com.volantis.mcs.management.agent.MarinerAgent;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.servlet.MarinerServletApplication;
import com.volantis.mcs.application.ApplicationInternals;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;

import org.apache.log4j.helpers.OptionConverter;

/**
 * This class allows the MarinerAgent thread to be stopped and
 * started via a servlet.
 */
public class AgentServlet extends HttpServlet {

    /**
     * The name of the enabled parameter.
     */
    private static final String ENABLED = "enabled";

    /**
     * The name of the host parameter.
     */
    private static final String HOST = "host";

    /**
     * The name of the port parameter.
     */
    private static final String PORT = "port";

    /**
     * The name of the password parameter.
     */
    private static final String PASSWORD = "password";

    /**
     * The MarinerAgent instance.
     */
    private MarinerAgent marinerAgent;


    /**
     * Initializes the servlet.
     */
    public void init() throws ServletException {

        if("true".equals(OptionConverter.substVars(getInitParameter(ENABLED), null))) {
            String password =
                OptionConverter.substVars(getInitParameter(PASSWORD), null);
            String host =
                OptionConverter.substVars(getInitParameter(HOST), null);
            String portString =
                OptionConverter.substVars(getInitParameter(PORT), null);
            if (password != null && portString != null) {
                int port = Integer.parseInt(portString);
                MarinerServletApplication msa =
                        MarinerServletApplication.getInstance(
                                this.getServletContext());
                Volantis volantis = ApplicationInternals.getVolantisBean(msa);
                marinerAgent = new MarinerAgent(password, host, port, volantis);
                marinerAgent.start();
            }
        }
    }

    /**
     * Destroys the servlet.
     */
    public void destroy() {
        // recommended as stop method on thread is deprecated
        if (marinerAgent != null) {        
            marinerAgent.stopAgent();
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-05	9933/1	gkoch	VBM:2005071113 Enabled usage of system properties in parameter values for AgentServlet

 20-Oct-05	9913/1	gkoch	VBM:2005071113 enabled usage of system properties for values in AgentServlet

 14-Sep-05	9514/1	pcameron	VBM:2005071113 Added host parameter to MarinerAgent

 12-Sep-05	9420/3	pcameron	VBM:2005071113 Added host parameter to MarinerAgent

 23-May-05	8441/1	emma	VBM:2005052308 Guarding AgentServlet.destroy() against null MarinerAgent

 19-May-05	8158/1	emma	VBM:2005041508 Merge from 330: Moving MarinerAgent management from Volantis to a servlet

 18-May-05	8156/1	emma	VBM:2005041508 Commit to allow RelMCS to build (product dependency problem)

 10-May-05	8093/1	emma	VBM:2005041508 Refactoring MarinerAgent startup into a servlet to fix a redeploy problem on Weblogic

 ===========================================================================
*/

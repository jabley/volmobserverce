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
package com.volantis.mcs.cli;

import com.volantis.synergetics.localization.MessageLocalizer;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * User interface which defines the methods used by the Exporter class object
 * to interact with users, a simple implementation of this interface would use
 * System.out.println() and System.in.read().
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface UserInterface {

    /**
     * Report an error message to user.
     *
     * @param message to display
     */
    public void reportError(String message);

    /**
     * Report an exception to user.
     *
     * @param throwable
     */
    public void reportException(Throwable throwable);

    /**
     * Report status or information message to user.
     *
     * @param message to display
     */
    public void reportStatus(String message);

    /**
     * Get a line of text from the user.
     *
     * @return line of text or null
     */
    public String getInputLine();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10413/5	pabbott	VBM:2005111811 Provide Exporter public API

 ===========================================================================
*/

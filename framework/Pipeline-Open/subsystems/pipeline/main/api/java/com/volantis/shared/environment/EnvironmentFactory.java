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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.shared.environment;

/**
 * A class for creating environment objects.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public abstract class EnvironmentFactory {

    /**
     * Default EnvironmentFactory instance
     *
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     */
    static EnvironmentFactory defaultEnvironmentFactory =
            new DefaultEnvironmentFactory();

    /**
     * Get the default instance of this factory.
     * @return The default instance of this factory.
     */
    public static EnvironmentFactory getDefaultInstance() {
        return defaultEnvironmentFactory;
    }

    /**
     * Create an EnvironmentInteractionTracker.
     * @return An EnvironmentInteractionTracker that can be used to track
     *         usages of {@link EnvironmentInteraction}s.
     */
    public abstract EnvironmentInteractionTracker createInteractionTracker();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	271/1	doug	VBM:2003073002 Implemented various environment fatories

 ===========================================================================
*/

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
package com.volantis.mcs.runtime;

import com.volantis.devrep.repository.api.accessors.DeviceRepositoryLocation;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import com.volantis.mcs.runtime.configuration.PolicyCacheConfiguration;
import com.volantis.mcs.servlet.ServletConfigContext;
import com.volantis.testtools.stubs.ServletContextStub;

/**
 * A test only class to allow us to access the private internals of Volantis,
 * to work around a bug in IBM JDK 1.4.1.
 * <p>
 * This is only required because the IBM 1.4.1 JDK has broken reflection, 
 * otherwise we could use ReflectionManager to access it's private internals.
 * <p>
 * Please avoid the temptation to add any more methods to this class if 
 * possible; IMHO, it is poor design to need access to private internals for 
 * testing, so new code should avoid this kind of thing if possible.
 * 
 * @see com.volantis.testtools.reflection.ReflectionManager
 */ 
public class VolantisInternals {

    private Volantis volantis;

    public VolantisInternals(Volantis volantis) {
        this.volantis = volantis;
    }

    public DeviceRepositoryLocation getDeviceRepositoryLocation()
            throws ConfigurationException {
        return volantis.getDeviceRepositoryLocation(
                new ServletConfigContext(new ServletContextStub()));

    }

    public PolicyCacheConfiguration getPolicyCacheConfiguration(PolicyType policyType) {
        return volantis.getPolicyCacheConfiguration(policyType);
    }

    /**
     * Gert the package private MarinerConfiguration object
     * @return The MarinerConfiguration object
     */
    public MarinerConfiguration getMarinerConfig() {
        return volantis.getConfig().getMarinerConfiguration();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Mar-05	6842/2	emma	VBM:2005020302 Making file references in config files relative to those files

 21-Feb-05	6986/2	emma	VBM:2005021411 Changes merged from MCS3.3

 15-Feb-05	6974/1	emma	VBM:2005021411 Allowing relative paths to devices.mdpr and xml repository

 08-Dec-04	6416/5	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 03-Sep-03	1295/3	geoff	VBM:2003082109 rework issues

 02-Sep-03	1295/1	geoff	VBM:2003082109 Certify & package GUIs, runtime & CLIs against IBM JRE/JDK 1.4

 ===========================================================================
*/

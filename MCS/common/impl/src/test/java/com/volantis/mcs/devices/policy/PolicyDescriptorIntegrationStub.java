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
package com.volantis.mcs.devices.policy;

import com.volantis.mcs.devices.policy.types.PolicyType;
import com.volantis.shared.metadata.type.immutable.ImmutableMetaDataType;

/**
 * Stub implementation of {@link PolicyDescriptor}.
 *
 * @deprecated the version in unit is the real version.
 *      This was only created to work around build problems and should be
 *      removed as soon as it is fixed. See VBM:2004091507.
 */
public class PolicyDescriptorIntegrationStub implements PolicyDescriptor {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    public PolicyType getPolicyType() {
        throw new IllegalStateException();
    }

    public ImmutableMetaDataType getPolicyMetaDataType() {
        throw new IllegalStateException();
    }

    public String getPolicyDescriptiveName() {
        throw new IllegalStateException();
    }

    public String getPolicyHelp() {
        throw new IllegalStateException();
    }

    public String getCategoryName() {
        throw new IllegalStateException();
    }

    public String getLanguage() {
        throw new IllegalStateException();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-05	8833/2	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 27-Apr-05	7645/1	geoff	VBM:2005041309 Device repository merge report: mergeable document event generation

 13-Apr-05	7572/1	geoff	VBM:2005040612 Device repository merge report: create event model and XML report listeners

 ===========================================================================
*/

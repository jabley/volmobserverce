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
 * This interface provides access to the descriptive information for a
 * PolicyValue.
 * 
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 * 
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface PolicyDescriptor {

    /**
     * This method returns the {@link PolicyType} of this policy.
     *
     * @return The {@link PolicyType}
     *
     * @deprecated Use {@link #getPolicyMetaDataType} instead.
     */
    public PolicyType getPolicyType();

    /**
     * This method returns the {@link ImmutableMetaDataType} of this policy.
     *
     * @return The {@link PolicyType}
     */
    public ImmutableMetaDataType getPolicyMetaDataType();

    /**
     * Returns the descriptive name for the device policy name. This method will
     * return null if it is used against a JDBC repository.
     *
     * @return The descriptive name
     */
    public String getPolicyDescriptiveName();

    /**
     * Returns the help text associated with device policy name. This method
     * will return null if it is used against a JDBC repository.
     *
     * @return The help text
     */
    public String getPolicyHelp();

    /**
     * Get the category name for this descriptor.
     *
     * @return the category name for this descriptor.
     */
    public String getCategoryName();

    /**
     * The string representation of the language of descriptive name and help.
     *
     * <p>A language has the following format:</p>
     *
     * <pre><language-code>_<country_code>_<variant></pre>
     *
     * <p>Any of the 3 parts can be empty string</p>
     *
     * <p>May return null indicating that no language has been set.</p>
     *
     * @return the language used for descriptive name and help.
     */
    public String getLanguage();
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6707/1	pduffin	VBM:2005011710 Refactored device repository API to fix couple of performance and code duplication issues. Added support for retrieving device policy values as meta data

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 21-Jul-04	4930/1	geoff	VBM:2004072104 Public API for Device Repository: Basics

 ===========================================================================
*/

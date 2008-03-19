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
package com.volantis.devrep.repository.impl.devices.policy;

import com.volantis.devrep.repository.impl.devices.policy.types.InternalPolicyType;
import com.volantis.mcs.devices.policy.PolicyDescriptor;
import com.volantis.mcs.devices.policy.types.PolicyType;
import com.volantis.shared.metadata.type.immutable.ImmutableMetaDataType;

/**
 * Default implementation of {@link com.volantis.mcs.devices.policy.PolicyDescriptor}.
 */
public class DefaultPolicyDescriptor implements PolicyDescriptor {

    /**
     * The type of the policy.
     */
    private PolicyType policyType;

    /**
     * The meta data type of the policy.
     */
    private ImmutableMetaDataType policyMetaDataType;

    /**
     * The descriptive name of the policy.
     * <p>
     * NOTE: this is currently null if this policy descriptor was created from
     * a JDBC repository, since JDBC does not currently store this information.
     */
    private String policyDescriptiveName;

    /**
     * The help text of the policy.
     * <p>
     * NOTE: this is currently null if this policy descriptor was created from
     * a JDBC repository, since JDBC does not currently store this information.
     */
    private String policyHelp;

    /**
     * The category that the policy belongs to.
     * <p>
     * Note that the category is not part of the public API. It was added here
     * as a way to pass the category information relating to policies between
     * the XML and JDBC accessors when doing the import. As such, it is only
     * currently written to by the XML accessor and read from by the JDBC
     * accessor - it may be null at other times.
     * <p>
     * It may be better in future to have explicit control over categories
     * separately to policy descriptors in the DeviceRepositoryAccessor.
     */
    private String category;
    private String language;

    /**
     * Set the policy type for this descriptor.
     *
     * @param policyType the policy type for this descriptor.
     */
    public void setPolicyType(PolicyType policyType) {
        this.policyType = policyType;
    }

    // Javadoc inherited.
    public PolicyType getPolicyType() {
        return policyType;
    }

    // Javadoc inherited.
    public ImmutableMetaDataType getPolicyMetaDataType() {
        // Only create this if asked and if there is an old style policy type
        // to create it.
        if (policyType != null) {
            // Synchronize to prevent concurrency problems.
            synchronized (this) {
                if (policyMetaDataType == null) {
                    InternalPolicyType internalPolicyType =
                            (InternalPolicyType) policyType;
                    policyMetaDataType = internalPolicyType.createMetaDataType();
                }
            }
        }
        return policyMetaDataType;
    }

    /**
     * Set the descriptive name for this descriptor.
     *
     * @param policyDescriptiveName the descriptive name for this descriptor.
     * @see #policyDescriptiveName
     */
    public void setPolicyDescriptiveName(String policyDescriptiveName) {
        this.policyDescriptiveName = policyDescriptiveName;
    }

    // Javadoc inherited.
    public String getPolicyDescriptiveName() {
        return policyDescriptiveName;
    }

    /**
     * Set the help text for this descriptor.
     *
     * @param policyHelp the help text for this descriptor.
     * @see #policyHelp
     */
    public void setPolicyHelp(String policyHelp) {
        this.policyHelp = policyHelp;
    }

    // Javadoc inherited.
    public String getPolicyHelp() {
        return policyHelp;
    }

    /**
     * Set the category for this descriptor.
     *
     * @param category the category for this descriptor.
     * @see #category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Get the category for this descriptor.
     *
     * @return the category for this descriptor.
     * @see #category
     */
    public String getCategoryName() {
        return category;
    }

    // Javadoc inherited.
    public String toString() {
        // This unusual format is so we can diff the output and also to
        // make it a bit more readable.
        return "[DefaultPolicyDescriptor:\n" +
                "  category=" + category + "\n" +
                "  type=" + policyType + "\n" +
                "  descriptiveName=" + policyDescriptiveName + "\n" +
                "  help=" + policyHelp + "\n" +
                "  language=" + language + "\n" +
                "  ]";
    }

    /**
     * Sets the language.
     *
     * @param language the new language.
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    // javadoc inherited
    public String getLanguage() {
        return language;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6707/1	pduffin	VBM:2005011710 Refactored device repository API to fix couple of performance and code duplication issues. Added support for retrieving device policy values as meta data

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 30-Jul-04	4993/1	geoff	VBM:2004072804 Public API for Device Repository: Final cleanup and javadoc

 28-Jul-04	4956/1	geoff	VBM:2004072305 Public API for Device Repository: metadata support for import tool (finalise)

 27-Jul-04	4961/1	claire	VBM:2004072601 Public API for Device Repository: Implement XML metadata read support

 23-Jul-04	4945/1	geoff	VBM:2004072205 Public API for Device Repository: Common Metadata Infrastructure

 ===========================================================================
*/

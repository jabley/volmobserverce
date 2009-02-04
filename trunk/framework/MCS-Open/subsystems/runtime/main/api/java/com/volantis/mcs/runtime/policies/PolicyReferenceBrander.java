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

package com.volantis.mcs.runtime.policies;

import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.repository.remote.RemoteRepositoryHelper;
import com.volantis.mcs.utilities.MarinerURL;

/**
 * Brands a policy reference.
 */
public class PolicyReferenceBrander {

    /**
     * A dummy MarinerURL that is used when creating a normalizable policy
     * reference.
     */
    private static final MarinerURL DUMMY;
    static {
        DUMMY = new MarinerURL();
        DUMMY.makeReadOnly();
    }

    private final PolicyReferenceFactory referenceFactory;

    public PolicyReferenceBrander(PolicyReferenceFactory referenceFactory) {
        this.referenceFactory = referenceFactory;
    }

    public RuntimePolicyReference getBrandedReference(
            RuntimePolicyReference reference, String brandName) {

        if (brandName == null || !reference.isBrandable()) {
            return reference;
        }

        // Check to see whether the brand name is remote or not.
        boolean remoteBrandName =
                RemoteRepositoryHelper.isRemoteName(brandName);

        // The brand name must end with a '/', and be either remote or start
        // with /.
        if (!brandName.endsWith("/")) {
            throw new IllegalStateException(
                    "Brand name must end with a / but was '" + brandName + "'");
        } else if (!remoteBrandName && !brandName.startsWith("/")) {
            throw new IllegalStateException(
                    "Brand name must start with either http://, or / " +
                    "but was '" + brandName + "'");
        }

        // The name in the reference must be project relative in order for it
        // to be branded.
        String projectRelativeName = reference.getName();
        if (!projectRelativeName.startsWith("/")) {
            throw new IllegalStateException("Expected project relative URL " +
                    "for branding but was '" + projectRelativeName + "'");
        }

        // Create the branded name by prepending it to the project relative
        // name minus the leading /.
        String brandedName = brandName + projectRelativeName.substring(1);

        RuntimeProject project = (RuntimeProject) reference.getProject();
        PolicyType expectedPolicyType = reference.getExpectedPolicyType();
        if (remoteBrandName) {
            // The brand name is remote so we need to renormalize the
            // branded reference, as it could refer to another policy.
            reference = referenceFactory.createLazyNormalizedReference(
                    project, DUMMY, brandedName, expectedPolicyType);
        } else {
            // Return an already normalized reference with the branded name
            // in the same project as the unbranded reference.
            reference = referenceFactory.createNormalizedReference(project,
                    brandedName, expectedPolicyType);
        }

        return reference;
    }
}

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
 * $Header: /src/voyager/com/volantis/mcs/repository/RepositoryManager.java,v 1.7 2003/03/12 16:10:43 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 04-Jun-01    Paul            VBM:2001051103 - Added this change history and
 *                              also changed cache key separator to / as it is
 *                              an illegal character in names.
 * 27-Jun-01    Paul            VBM:2001062704 - Sorted out the copyright.
 * 27-Sep-01    Allan           VBM:2001091104 - Removed CACHE_KEY_SEPARATOR
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 12-Mar-02    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository;

import com.volantis.mcs.accessors.PolicyBuilderAccessor;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.compatibility.New2OldConverter;
import com.volantis.mcs.policies.compatibility.Old2NewConverter;
import com.volantis.mcs.project.InternalProject;
import com.volantis.mcs.project.LocalPolicySource;
import com.volantis.mcs.project.Project;


/**
 * The RepositoryManager class is the parent class for all repository
 * managers. It provides attributes and operations common across repository
 * managers.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 *
 * @deprecated Use {@link com.volantis.mcs.project.PolicyManager}.
 *             This was deprecated in version 3.5.1.
 */
public abstract class RepositoryManager {

    final PolicyBuilderAccessor accessor;

    final New2OldConverter new2Old = new New2OldConverter();

    final Old2NewConverter old2New = new Old2NewConverter();

    protected final Project defaultProject;

    private final DeprecatedPolicyCacheFlusher policyCacheFlusher;

    RepositoryManager(
            Repository repository, Project project,
            DeprecatedPolicyCacheFlusher policyCacheFlusher) {
        this.policyCacheFlusher = policyCacheFlusher;

        Project defaultProject = null;
        if (repository instanceof DeprecatedRepository) {
            DeprecatedRepository deprecatedRepository =
                    (DeprecatedRepository) repository;
            defaultProject = deprecatedRepository.getDefaultProject();
        }

        if (project == null) {
            project = defaultProject;
        }

        if (project == null) {
            throw new IllegalArgumentException("defaultProject cannot be null");
        }

        InternalProject internalProject = (InternalProject) project;
        LocalPolicySource source = (LocalPolicySource)
                internalProject.getPolicySource();
        accessor = source.getPolicyBuilderAccessor();

        this.defaultProject = defaultProject;
    }

    RepositoryManager(Repository repository) {
        this(repository, null, null);
    }

    RepositoryConnection getConnection(RepositoryConnection connection)
            throws RepositoryException {
        return connection.getUnderLyingConnection();
    }

    /**
     * Flush the cache for the specified policy type.
     *
     * @param policyType The specified policy type.
     */
    protected void flushCache(PolicyType policyType) {
        if (policyCacheFlusher != null) {
            policyCacheFlusher.flush(policyType);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
